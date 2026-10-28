package fr.efrei.transfret;

import java.util.List;

public class Avion extends Vehicule implements Motorise {

    private static final int ALTITUDE_VOL = 35000;

    public Avion(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax);
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        return mode == ModeTransport.AIR;
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("L'avion " + immatriculation + " roule sur la piste");
        System.out.println("L'avion " + immatriculation + " vole à " + ALTITUDE_VOL + " pieds");
    }

    public void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }
}
