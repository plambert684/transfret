package fr.efrei.transfret.expedition.notification;

import fr.efrei.transfret.EmailService;

public class EmailCanalNotification implements CanalNotification {

    @Override
    public void envoyer(String destinataire, String sujet, String message) {
        EmailService.envoyer(destinataire, sujet, message);
    }
}