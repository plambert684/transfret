package fr.efrei.transfret;

import java.util.List;

public class Peniche extends Vehicule implements Motorise {

    public Peniche(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax);
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        return mode == ModeTransport.FLUVIAL && !contient(colis, TypeMarchandise.REFRIGERE);
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("La péniche " + immatriculation + " navigue sur " + distance + " km de voies fluviales");
    }

    public void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }
}
