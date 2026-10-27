package fr.efrei.transfret.expedition.validation;

import fr.efrei.transfret.Client;
import fr.efrei.transfret.Colis;

import java.util.List;

public class ValidateurExpedition {

    public String valider(Client client, List<Colis> colis, String mode, boolean urgent) {
        if (client == null) {
            return "ERREUR : client obligatoire";
        }
        if (colis == null || colis.isEmpty()) {
            return "ERREUR : au moins un colis est nécessaire";
        }

        boolean dangereux = false;
        boolean refrigere = false;
        for (Colis c : colis) {
            if (c.getPoids() <= 0) {
                return "ERREUR : poids invalide pour " + c.getDescription();
            }
            if (c.getType() == 2) dangereux = true;
            if (c.getType() == 3) refrigere = true;
        }
        if (dangereux && mode.equals("AIR")) {
            return "ERREUR : les matières dangereuses sont interdites en transport aérien";
        }
        if (refrigere && !(mode.equals("ROUTE") || mode.equals("MER"))) {
            return "ERREUR : le transport réfrigéré n'est possible que par route ou par mer";
        }
        if (urgent && mode.equals("MER")) {
            return "ERREUR : pas d'option urgente pour le transport maritime";
        }
        return null;
    }
}