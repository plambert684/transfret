package fr.efrei.transfret.expedition.notification;

import fr.efrei.transfret.Client;

public class Notificateur {

    private final CanalNotification email;
    private final CanalNotification sms;

    public Notificateur(CanalNotification email, CanalNotification sms) {
        this.email = email;
        this.sms = sms;
    }

    public void notifier(Client client, String sujet, String corps, String messageSms) {
        email.envoyer(client.getEmail(), sujet, corps);
        if (client.getType().equals("PREMIUM")) {
            sms.envoyer(client.getTelephone(), null, messageSms);
        }
    }
}