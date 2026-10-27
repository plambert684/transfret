package fr.efrei.transfret;

public class ServiceClient {

    private ValidateurClient validateur = new ValidateurClient();

    public Client creerClient(String nom, String email, String telephone, String adresse, String type, String siret) {
        TypeClient typeClient = validateur.valider(nom, email, type, siret);
        if (typeClient == null) {
            return null;
        }
        Client c = new Client(nom, email, telephone, adresse, typeClient, siret);
        Database.getInstance().sauvegarderClient(c);
        EmailService.envoyer(c.getEmail(), "Bienvenue chez TransFret", "Bonjour " + c.getNom() + ", votre compte " + c.getType().name().toLowerCase() + " a été créé.");
        return c;
    }
}
