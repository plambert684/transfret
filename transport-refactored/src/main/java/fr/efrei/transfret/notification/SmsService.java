package fr.efrei.transfret.notification;

import java.util.ArrayList;
import java.util.List;

public class SmsService implements CanalNotification {

    // garde une trace des SMS envoyés (facturés par l'opérateur)
    private final List<String[]> journal = new ArrayList<>();

    private String apiKey = "sk_live_8f7d6e5c4b3a2918";

    @Override
    public void envoyer(String numero, String sujet, String message) {
        if (message.length() > 160) {
            message = message.substring(0, 157) + "...";
        }
        System.out.println("[SMS] -> " + numero + " : " + message);
        journal.add(new String[]{numero, message});
    }

    public List<String[]> getJournal() {
        return journal;
    }
}
