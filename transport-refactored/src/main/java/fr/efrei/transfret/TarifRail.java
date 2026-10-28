package fr.efrei.transfret;

public class TarifRail implements TarifTransport {

    private static final double TARIF_PAR_KM = 0.8;
    private static final double TARIF_PAR_KG = 0.07;
    private static final double MINIMUM = 150;

    @Override
    public double calculer(int distance, double poids, double volume) {
        return Math.max(distance * TARIF_PAR_KM + poids * TARIF_PAR_KG, MINIMUM);
    }
}
