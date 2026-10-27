package fr.efrei.transfret.expedition.notification;

import fr.efrei.transfret.SmsService;

public class SmsCanalNotification implements CanalNotification {

    private final SmsService smsService;

    public SmsCanalNotification() {
        this(new SmsService());
    }

    public SmsCanalNotification(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void envoyer(String destinataire, String sujet, String message) {
        smsService.envoyerSms(destinataire, message);
    }
}