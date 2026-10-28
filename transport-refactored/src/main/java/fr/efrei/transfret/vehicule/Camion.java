package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import java.util.List;

public class Camion extends Vehicule implements Motorise {

    private final boolean adr;

    public Camion(String immatriculation, double capaciteMax, boolean adr) {
        super(immatriculation, capaciteMax);
        this.adr = adr;
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        return mode == ModeTransport.ROUTE
                && !contient(colis, TypeMarchandise.REFRIGERE)
                && (adr || !contient(colis, TypeMarchandise.DANGEREUX));
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("Le camion " + immatriculation + " roule sur " + distance + " km");
    }

    public void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }
}
