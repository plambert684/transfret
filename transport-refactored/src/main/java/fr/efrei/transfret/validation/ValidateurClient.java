package fr.efrei.transfret.validation;

import fr.efrei.transfret.modele.TypeClient;

public class ValidateurClient {

    public TypeClient valider(String nom, String email, String type, String siret) {
        if (nom == null || nom.trim().length() == 0) {
            System.out.println("ERREUR : nom obligatoire");
            return null;
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("ERREUR : email invalide (" + email + ")");
            return null;
        }
        TypeClient typeClient;
        try {
            typeClient = TypeClient.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("ERREUR : type de client inconnu");
            return null;
        }
        if (typeClient == TypeClient.ENTREPRISE && (siret == null || siret.length() != 14)) {
            System.out.println("ERREUR : SIRET obligatoire (14 chiffres) pour une entreprise");
            return null;
        }
        return typeClient;
    }
}
