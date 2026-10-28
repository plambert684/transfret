package fr.efrei.transfret;

public class PolitiqueRemiseParticulier implements PolitiqueRemise {

    @Override
    public double appliquer(double prix) {
        return prix;
    }
}
