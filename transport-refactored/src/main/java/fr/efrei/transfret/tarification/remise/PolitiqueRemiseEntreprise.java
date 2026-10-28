package fr.efrei.transfret.tarification.remise;

public class PolitiqueRemiseEntreprise implements PolitiqueRemise {

    private static final double SEUIL_REMISE_SUPERIEURE = 5000;
    private static final double COEFFICIENT_REMISE_SUPERIEURE = 0.85;
    private static final double COEFFICIENT_REMISE = 0.90;

    @Override
    public double appliquer(double prix) {
        return prix * (prix > SEUIL_REMISE_SUPERIEURE
                ? COEFFICIENT_REMISE_SUPERIEURE
                : COEFFICIENT_REMISE);
    }
}
