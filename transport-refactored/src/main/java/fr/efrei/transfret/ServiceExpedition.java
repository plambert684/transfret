package fr.efrei.transfret;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ServiceExpedition {

    private static final double TAUX_TVA = 0.20;
    private static final int DEBUT_SUFFIXE_ID = 1000;
    private static final int PLAGE_SUFFIXE_ID = 9000;
    private static final double VITESSE_ROUTE_KM_PAR_HEURE = 70.0;
    private static final double HEURES_ROUTE_URGENT_PAR_JOUR = 18.0;
    private static final double HEURES_ROUTE_NORMAL_PAR_JOUR = 9.0;
    private static final double DISTANCE_RAIL_PAR_JOUR = 1440.0;
    private static final int JOURS_SUPPLEMENTAIRES_RAIL = 1;
    private static final int JOURS_REDUITS_URGENCE_RAIL = 1;
    private static final double DISTANCE_MER_PAR_JOUR = 720.0;
    private static final int JOURS_SUPPLEMENTAIRES_MER = 2;
    private static final int SEUIL_DISTANCE_AIR_LONG_COURRIER = 3000;
    private static final int DELAI_AIR_STANDARD = 1;
    private static final int DELAI_AIR_LONG_COURRIER = 2;
    private static final int DELAI_MINIMUM = 1;
    private static final double METRES_PAR_MILLE_MARIN = 1.852;
    private static final int DISTANCE_DECOLLAGE = 2;
    private static final int ALTITUDE_VOL = 35000;
    private static final double TEMPERATURE_REFRIGERATION = 4;
    private static final double FRAIS_ANNULATION_EN_COURS = 0.5;

    public static int compteur = 0;

    private Database db = Database.getInstance();
    private CalculateurPrix calculateur = new CalculateurPrix();
    private AffectationVehicule affectation = new AffectationVehicule();

    public Expedition creerExpedition(Client client, List<Colis> colis, String villeDepart, String villeArrivee, String mode, boolean urgent, boolean assurance) {
        ModeTransport modeTransport = ModeTransport.valueOf(mode);
        if (client == null) {
            System.out.println("ERREUR : client obligatoire");
            return null;
        }
        if (colis == null || colis.isEmpty()) {
            System.out.println("ERREUR : au moins un colis est nécessaire");
            return null;
        }
        boolean dangereux = false;
        boolean refrigere = false;
        for (Colis c : colis) {
            if (c.getPoids() <= 0) {
                System.out.println("ERREUR : poids invalide pour " + c.getDescription());
                return null;
            }
            if (c.getType() == TypeMarchandise.DANGEREUX) dangereux = true;
            if (c.getType() == TypeMarchandise.REFRIGERE) refrigere = true;
        }
        if (dangereux && modeTransport == ModeTransport.AIR) {
            System.out.println("ERREUR : les matières dangereuses sont interdites en transport aérien");
            return null;
        }
        if (refrigere && !(modeTransport == ModeTransport.ROUTE || modeTransport == ModeTransport.MER)) {
            System.out.println("ERREUR : le transport réfrigéré n'est possible que par route ou par mer");
            return null;
        }
        if (urgent && modeTransport == ModeTransport.MER) {
            System.out.println("ERREUR : pas d'option urgente pour le transport maritime");
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
        cal.add(Calendar.DAY_OF_MONTH, calculerDelai(distance, mode, urgent));
        e.setDateLivraisonPrevue(cal.getTime());

        db.sauvegarderExpedition(e);
        notifier(client, "Confirmation de votre expédition " + e.getId(),
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
        ModeTransport modeTransport = ModeTransport.valueOf(mode);
        int jours = 0;
        switch (modeTransport) {
            case ROUTE:
                double heures = distance / VITESSE_ROUTE_KM_PAR_HEURE;
                if (urgent) {
                    jours = (int) Math.ceil(heures / HEURES_ROUTE_URGENT_PAR_JOUR);
                } else {
                    jours = (int) Math.ceil(heures / HEURES_ROUTE_NORMAL_PAR_JOUR);
                }
                break;
            case RAIL:
                jours = (int) Math.ceil(distance / DISTANCE_RAIL_PAR_JOUR) + JOURS_SUPPLEMENTAIRES_RAIL;
                if (urgent) jours = jours - JOURS_REDUITS_URGENCE_RAIL;
                break;
            case MER:
                jours = (int) Math.ceil(distance / DISTANCE_MER_PAR_JOUR) + JOURS_SUPPLEMENTAIRES_MER;
                break;
            case AIR:
                jours = DELAI_AIR_STANDARD;
                if (distance > SEUIL_DISTANCE_AIR_LONG_COURRIER) jours = DELAI_AIR_LONG_COURRIER;
                if (urgent) jours = DELAI_AIR_STANDARD;
                break;
        }
        if (jours < DELAI_MINIMUM) jours = DELAI_MINIMUM;
        return jours;
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
        notifier(e.getClient(), "Votre expédition " + e.getId() + " est en route",
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
        notifier(e.getClient(), "Votre expédition " + e.getId() + " est livrée",
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
        notifier(e.getClient(), "Annulation de votre expédition " + e.getId(),
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

    private void notifier(Client client, String sujet, String corps, String sms) {
        EmailService.envoyer(client.getEmail(), sujet, corps);
        if (client.getType() == TypeClient.PREMIUM) {
            new SmsService().envoyerSms(client.getTelephone(), sms);
        }
    }
}
