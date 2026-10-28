package fr.efrei.transfret.tarification.remise;

public class PolitiqueRemisePremium implements PolitiqueRemise {

    private static final double COEFFICIENT_REMISE = 0.80;

    @Override
    public double appliquer(double prix) {
        return prix * COEFFICIENT_REMISE;
    }
}
