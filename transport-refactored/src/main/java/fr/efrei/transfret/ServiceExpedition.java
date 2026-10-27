package fr.efrei.transfret;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ServiceExpedition {

    private static final double TAUX_TVA = 0.20;
    private static final int DEBUT_SUFFIXE_ID = 1000;
    private static final int PLAGE_SUFFIXE_ID = 9000;
    private static final double METRES_PAR_MILLE_MARIN = 1.852;
    private static final int DISTANCE_DECOLLAGE = 2;
    private static final int ALTITUDE_VOL = 35000;
    private static final double TEMPERATURE_REFRIGERATION = 4;
    private static final double FRAIS_ANNULATION_EN_COURS = 0.5;

    public static int compteur = 0;

    private Database db = Database.getInstance();
    private CalculateurPrix calculateur = new CalculateurPrix();
    private AffectationVehicule affectation = new AffectationVehicule();
    private ValidateurExpedition validateur = new ValidateurExpedition();
    private EstimateurDelai estimateurDelai = new EstimateurDelai();
    private Notificateur notificateur = new Notificateur();

    public Expedition creerExpedition(Client client, List<Colis> colis, String villeDepart, String villeArrivee, String mode, boolean urgent, boolean assurance) {
        ModeTransport modeTransport = ModeTransport.valueOf(mode);
        String erreurValidation = validateur.valider(client, colis, modeTransport, urgent);
        if (erreurValidation != null) {
            System.out.println(erreurValidation);
            return null;
        }
        int distance = Utils.getDistance(villeDepart, villeArrivee);
        if (distance == -1) {
            System.out.println("ERREUR : trajet inconnu " + villeDepart + " -> " + villeArrivee);
            return null;
        }

        Vehicule v = affectation.trouverVehicule(colis, modeTransport);
        if (v == null) {
            System.out.println("ERREUR : aucun véhicule disponible pour cette expédition");
            return null;
        }
        for (Colis c : colis) {
            v.charger(c);
        }

        Expedition e = new Expedition();
        compteur++;
        e.setId("EXP-" + compteur + "-" + (DEBUT_SUFFIXE_ID + new Random().nextInt(PLAGE_SUFFIXE_ID)));
        e.setClient(client);
        e.setColis(colis);
        e.setVilleDepart(villeDepart);
        e.setVilleArrivee(villeArrivee);
        e.setDistance(distance);
        e.setMode(modeTransport);
        e.setUrgent(urgent);
        e.setAssurance(assurance);
        e.setStatut(StatutExpedition.CREEE);
        e.setVehicule(v);
        e.setPrixHT(calculateur.calculerPrixHT(colis, distance, modeTransport, urgent, assurance, client.getType()));
        e.setPrixTTC(Utils.arrondir(e.getPrixHT() * (1 + TAUX_TVA)));
        e.setDateCreation(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(e.getDateCreation());
        cal.add(Calendar.DAY_OF_MONTH, estimateurDelai.estimer(distance, modeTransport, urgent));
        e.setDateLivraisonPrevue(cal.getTime());

        db.sauvegarderExpedition(e);
        notificateur.notifier(client, "Confirmation de votre expédition " + e.getId(),
                "Votre expédition " + villeDepart + " -> " + villeArrivee + " (" + mode + ") a été enregistrée. Montant : " + e.getPrixTTC() + " € TTC.",
                "TransFret : expédition " + e.getId() + " créée, livraison prévue le " + Utils.formaterDate(e.getDateLivraisonPrevue()));
        return e;
    }

    // retourne le prix TTC, -1 si le trajet est inconnu
    public double calculerDevis(List<Colis> colis, String villeDepart, String villeArrivee, String mode, boolean urgent, boolean assurance, String typeClient) {
        ModeTransport modeTransport = ModeTransport.valueOf(mode);
        TypeClient typeClientEnum = TypeClient.valueOf(typeClient);
        int distance = Utils.getDistance(villeDepart, villeArrivee);
        if (distance == -1) {
            return -1;
        }
        double ht = calculateur.calculerPrixHT(colis, distance, modeTransport, urgent, assurance, typeClientEnum);
        return Utils.arrondir(ht * (1 + TAUX_TVA));
    }

    // retourne le délai de livraison en jours
    public int calculerDelai(int distance, String mode, boolean urgent) {
        return estimateurDelai.estimer(distance, ModeTransport.valueOf(mode), urgent);
    }

    public boolean demarrerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() != StatutExpedition.CREEE) {
            System.out.println("ERREUR : impossible de démarrer l'expédition " + id);
            return false;
        }
        e.setStatut(StatutExpedition.EN_COURS);
        Vehicule v = e.getVehicule();
        v.setDisponible(false);
        try {
            v.faireLePlein();
        } catch (Exception ex) {
        }
        if (e.getMode() == ModeTransport.ROUTE || e.getMode() == ModeTransport.RAIL) {
            v.rouler(e.getDistance());
        } else if (e.getMode() == ModeTransport.MER) {
            v.naviguer((int) (e.getDistance() / METRES_PAR_MILLE_MARIN));
        } else if (e.getMode() == ModeTransport.AIR) {
            v.rouler(DISTANCE_DECOLLAGE);
            v.voler(ALTITUDE_VOL);
        }
        for (Colis c : e.getColis()) {
            if (c.getType() == TypeMarchandise.REFRIGERE) {
                v.activerRefrigeration(TEMPERATURE_REFRIGERATION);
                break;
            }
        }
        db.mettreAJourStatut(e);
        notificateur.notifier(e.getClient(), "Votre expédition " + e.getId() + " est en route",
                "Votre expédition a quitté " + e.getVilleDepart() + ".",
                "TransFret : expédition " + e.getId() + " partie de " + e.getVilleDepart());
        return true;
    }

    public boolean livrerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() != StatutExpedition.EN_COURS) {
            System.out.println("ERREUR : impossible de livrer l'expédition " + id);
            return false;
        }
        e.setStatut(StatutExpedition.LIVREE);
        libererVehicule(e);
        db.mettreAJourStatut(e);
        notificateur.notifier(e.getClient(), "Votre expédition " + e.getId() + " est livrée",
                "Votre expédition est arrivée à " + e.getVilleArrivee() + ". Merci de votre confiance.",
                "TransFret : expédition " + e.getId() + " livrée à " + e.getVilleArrivee());
        return true;
    }

    public boolean annulerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() == StatutExpedition.LIVREE || e.getStatut() == StatutExpedition.ANNULEE) {
            System.out.println("ERREUR : impossible d'annuler l'expédition " + id);
            return false;
        }
        if (e.getStatut() == StatutExpedition.EN_COURS) {
            // 50% de frais si déjà partie
            e.setFraisAnnulation(Utils.arrondir(e.getPrixTTC() * FRAIS_ANNULATION_EN_COURS));
        }
        e.setStatut(StatutExpedition.ANNULEE);
        libererVehicule(e);
        db.mettreAJourStatut(e);
        notificateur.notifier(e.getClient(), "Annulation de votre expédition " + e.getId(),
                "Votre expédition a été annulée. Frais d'annulation : " + e.getFraisAnnulation() + " €",
                "TransFret : expédition " + e.getId() + " annulée");
        return true;
    }

    private void libererVehicule(Expedition e) {
        for (Colis c : e.getColis()) {
            e.getVehicule().decharger(c);
        }
        e.getVehicule().setDisponible(true);
    }

}
