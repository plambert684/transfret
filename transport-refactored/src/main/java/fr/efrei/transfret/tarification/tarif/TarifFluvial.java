package fr.efrei.transfret.tarification.tarif;

public class TarifFluvial implements TarifTransport {

    private static final double TARIF_PAR_KM = 0.40;
    private static final double TARIF_PAR_KG = 0.02;
    private static final double MINIMUM = 300;

    @Override
    public double calculer(int distance, double poids, double volume) {
        return Math.max(distance * TARIF_PAR_KM + poids * TARIF_PAR_KG, MINIMUM);
    }
}
