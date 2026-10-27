package fr.efrei.transfret;

public interface ClientRepository {

    void sauvegarderClient(Client client);

    Client trouverClient(String email);
}
