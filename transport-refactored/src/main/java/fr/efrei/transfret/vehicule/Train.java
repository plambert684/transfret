package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import java.util.List;

public class Train extends Vehicule {

    public Train(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax);
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        return mode == ModeTransport.RAIL;
    }

    public void partir(int distance) {
        System.out.println("Le train " + immatriculation + " circule sur " + distance + " km de voies ferrées");
    }
}
