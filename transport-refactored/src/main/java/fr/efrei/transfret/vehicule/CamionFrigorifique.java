package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import java.util.List;

public class CamionFrigorifique extends Vehicule implements Motorise, Refrigerable {

    public CamionFrigorifique(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax);
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        if (mode != ModeTransport.ROUTE) {
            return false;
        }
        for (Colis c : colis) {
            if (c.getType() != TypeMarchandise.REFRIGERE) {
                return false;
            }
        }
        return true;
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("Le camion " + immatriculation + " roule sur " + distance + " km");
        activerRefrigeration(TEMPERATURE_REFRIGERATION);
    }

    public void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }

    public void activerRefrigeration(double temperature) {
        System.out.println("Réfrigération activée à " + temperature + "°C sur " + immatriculation);
    }
}
