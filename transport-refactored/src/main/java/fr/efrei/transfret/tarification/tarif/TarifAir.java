package fr.efrei.transfret.tarification.tarif;

public class TarifAir implements TarifTransport {

    private static final double COEFFICIENT_POIDS_VOLUMETRIQUE = 167;
    private static final double TARIF_PAR_KM = 0.6;
    private static final double TARIF_PAR_KG = 4.5;

    @Override
    public double calculer(int distance, double poids, double volume) {
        double poidsTaxable = Math.max(poids, volume * COEFFICIENT_POIDS_VOLUMETRIQUE);
        return distance * TARIF_PAR_KM + poidsTaxable * TARIF_PAR_KG;
    }
}
