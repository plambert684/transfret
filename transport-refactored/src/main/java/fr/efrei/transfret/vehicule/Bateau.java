package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import java.util.List;

public class Bateau extends Vehicule implements Motorise, Refrigerable {

    private static final double KM_PAR_MILLE_MARIN = 1.852;

    private final boolean conteneursRefrigeres;

    public Bateau(String immatriculation, double capaciteMax, boolean conteneursRefrigeres) {
        super(immatriculation, capaciteMax);
        this.conteneursRefrigeres = conteneursRefrigeres;
    }

    protected boolean accepte(List<Colis> colis, ModeTransport mode) {
        return mode == ModeTransport.MER && (conteneursRefrigeres || !contient(colis, TypeMarchandise.REFRIGERE));
    }

    public void partir(int distance) {
        faireLePlein();
        System.out.println("Le navire " + immatriculation + " navigue sur " + (int) (distance / KM_PAR_MILLE_MARIN) + " milles nautiques");
        if (contient(chargement, TypeMarchandise.REFRIGERE)) {
            activerRefrigeration(TEMPERATURE_REFRIGERATION);
        }
    }

    public void faireLePlein() {
        System.out.println("Plein effectué pour " + immatriculation);
    }

    public void activerRefrigeration(double temperature) {
        System.out.println("Conteneurs frigorifiques réglés à " + temperature + "°C");
    }
}
