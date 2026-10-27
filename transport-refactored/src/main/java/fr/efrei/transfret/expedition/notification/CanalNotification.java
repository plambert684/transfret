package fr.efrei.transfret.expedition.notification;

public interface CanalNotification {

    void envoyer(String destinataire, String sujet, String message);
}