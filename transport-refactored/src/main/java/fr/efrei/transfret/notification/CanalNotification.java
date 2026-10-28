package fr.efrei.transfret.notification;

public interface CanalNotification {

    void envoyer(String destinataire, String sujet, String contenu);
}
