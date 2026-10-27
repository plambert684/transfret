package fr.efrei.transfret;

import fr.efrei.transfret.expedition.delai.EstimateurDelai;
import fr.efrei.transfret.expedition.notification.EmailCanalNotification;
import fr.efrei.transfret.expedition.notification.Notificateur;
import fr.efrei.transfret.expedition.notification.SmsCanalNotification;
import fr.efrei.transfret.expedition.persistance.ExpeditionRepository;
import fr.efrei.transfret.expedition.validation.ValidateurExpedition;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ServiceExpedition {

    public static int compteur = 0;

    private final ExpeditionRepository db;
    private final CalculateurPrix calculateur;
    private final AffectationVehicule affectation;
    private final ValidateurExpedition validateur;
    private final EstimateurDelai estimateurDelai;
    private final Notificateur notificateur;

    public ServiceExpedition() {
        this(Database.getInstance(), new CalculateurPrix(), new AffectationVehicule(),
                new ValidateurExpedition(), new EstimateurDelai(),
                new Notificateur(new EmailCanalNotification(), new SmsCanalNotification()));
    }

    public ServiceExpedition(ExpeditionRepository db, CalculateurPrix calculateur,
                             AffectationVehicule affectation, ValidateurExpedition validateur,
                             EstimateurDelai estimateurDelai, Notificateur notificateur) {
        this.db = db;
        this.calculateur = calculateur;
        this.affectation = affectation;
        this.validateur = validateur;
        this.estimateurDelai = estimateurDelai;
        this.notificateur = notificateur;
    }

    public Expedition creerExpedition(Client client, List<Colis> colis, String villeDepart, String villeArrivee, String mode, boolean urgent, boolean assurance) {
        String erreurValidation = validateur.valider(client, colis, mode, urgent);
        if (erreurValidation != null) {
            System.out.println(erreurValidation);
            return null;
        }
        int distance = Utils.getDistance(villeDepart, villeArrivee);
        if (distance == -1) {
            System.out.println("ERREUR : trajet inconnu " + villeDepart + " -> " + villeArrivee);
            return null;
        }

        Vehicule v = affectation.trouverVehicule(colis, mode);
        if (v == null) {
            System.out.println("ERREUR : aucun véhicule disponible pour cette expédition");
            return null;
        }
        for (Colis c : colis) {
            v.charger(c);
        }

        Expedition e = new Expedition();
        compteur++;
        e.setId("EXP-" + compteur + "-" + (1000 + new Random().nextInt(9000)));
        e.setClient(client);
        e.setColis(colis);
        e.setVilleDepart(villeDepart);
        e.setVilleArrivee(villeArrivee);
        e.setDistance(distance);
        e.setMode(mode);
        e.setUrgent(urgent);
        e.setAssurance(assurance);
        e.setStatut("CREEE");
        e.setVehicule(v);
        e.setPrixHT(calculateur.calculerPrixHT(colis, distance, mode, urgent, assurance, client.getType()));
        e.setPrixTTC(Math.round(e.getPrixHT() * 1.2 * 100.0) / 100.0);
        e.setDateCreation(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(e.getDateCreation());
        cal.add(Calendar.DAY_OF_MONTH, calculerDelai(distance, mode, urgent));
        e.setDateLivraisonPrevue(cal.getTime());

        db.sauvegarderExpedition(e);
        notificateur.notifier(client, "Confirmation de votre expédition " + e.getId(),
                "Votre expédition " + villeDepart + " -> " + villeArrivee + " (" + mode + ") a été enregistrée. Montant : " + e.getPrixTTC() + " € TTC.",
                "TransFret : expédition " + e.getId() + " créée, livraison prévue le " + Utils.formaterDate(e.getDateLivraisonPrevue()));
        return e;
    }

    // retourne le prix TTC, -1 si le trajet est inconnu
    public double calculerDevis(List<Colis> colis, String villeDepart, String villeArrivee, String mode, boolean urgent, boolean assurance, String typeClient) {
        int distance = Utils.getDistance(villeDepart, villeArrivee);
        if (distance == -1) {
            return -1;
        }
        double ht = calculateur.calculerPrixHT(colis, distance, mode, urgent, assurance, typeClient);
        return Math.round(ht * 1.2 * 100.0) / 100.0;
    }

    // retourne le délai de livraison en jours
    public int calculerDelai(int distance, String mode, boolean urgent) {
        return estimateurDelai.estimer(distance, mode, urgent);
    }

    public boolean demarrerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || !e.getStatut().equals("CREEE")) {
            System.out.println("ERREUR : impossible de démarrer l'expédition " + id);
            return false;
        }
        e.setStatut("EN_COURS");
        Vehicule v = e.getVehicule();
        v.setDisponible(false);
        try {
            v.faireLePlein();
        } catch (Exception ex) {
        }
        if (e.getMode().equals("ROUTE") || e.getMode().equals("RAIL")) {
            v.rouler(e.getDistance());
        } else if (e.getMode().equals("MER")) {
            v.naviguer((int) (e.getDistance() / 1.852));
        } else if (e.getMode().equals("AIR")) {
            v.rouler(2);
            v.voler(35000);
        }
        for (Colis c : e.getColis()) {
            if (c.getType() == 3) {
                v.activerRefrigeration(4);
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
        if (e == null || !e.getStatut().equals("EN_COURS")) {
            System.out.println("ERREUR : impossible de livrer l'expédition " + id);
            return false;
        }
        e.setStatut("LIVREE");
        libererVehicule(e);
        db.mettreAJourStatut(e);
        notificateur.notifier(e.getClient(), "Votre expédition " + e.getId() + " est livrée",
                "Votre expédition est arrivée à " + e.getVilleArrivee() + ". Merci de votre confiance.",
                "TransFret : expédition " + e.getId() + " livrée à " + e.getVilleArrivee());
        return true;
    }

    public boolean annulerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut().equals("LIVREE") || e.getStatut().equals("ANNULEE")) {
            System.out.println("ERREUR : impossible d'annuler l'expédition " + id);
            return false;
        }
        if (e.getStatut().equals("EN_COURS")) {
            // 50% de frais si déjà partie
            e.setFraisAnnulation(Math.round(e.getPrixTTC() * 0.5 * 100.0) / 100.0);
        }
        e.setStatut("ANNULEE");
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
