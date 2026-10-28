package fr.efrei.transfret.persistance;

import fr.efrei.transfret.modele.Client;

public interface ClientRepository {

    void sauvegarderClient(Client client);

    Client trouverClient(String email);
}
