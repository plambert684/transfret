package fr.efrei.transfret;

public class TarifMer implements TarifTransport {

    private static final double TARIF_PAR_KM = 0.5;
    private static final double TARIF_PAR_KG = 0.03;
    private static final double MINIMUM = 500;

    @Override
    public double calculer(int distance, double poids, double volume) {
        return Math.max(distance * TARIF_PAR_KM + poids * TARIF_PAR_KG, MINIMUM);
    }
}
