package fr.efrei.transfret.service;

import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.notification.CanalNotification;
import fr.efrei.transfret.persistance.ClientRepository;
import fr.efrei.transfret.validation.ValidateurClient;

public class ServiceClient {

    private final ClientRepository repository;
    private final CanalNotification email;
    private final ValidateurClient validateur;

    public ServiceClient(ClientRepository repository, CanalNotification email, ValidateurClient validateur) {
        this.repository = repository;
        this.email = email;
        this.validateur = validateur;
    }

    public Client creerClient(String nom, String email, String telephone, String adresse, String type, String siret) {
        TypeClient typeClient = validateur.valider(nom, email, type, siret);
        if (typeClient == null) {
            return null;
        }
        Client c = new Client(nom, email, telephone, adresse, typeClient, siret);
        repository.sauvegarderClient(c);
        this.email.envoyer(c.getEmail(), "Bienvenue chez TransFret", "Bonjour " + c.getNom() + ", votre compte " + c.getType().name().toLowerCase() + " a été créé.");
        return c;
    }
}
