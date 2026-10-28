package fr.efrei.transfret.tarification.remise;

public class PolitiqueRemiseParticulier implements PolitiqueRemise {

    @Override
    public double appliquer(double prix) {
        return prix;
    }
}
