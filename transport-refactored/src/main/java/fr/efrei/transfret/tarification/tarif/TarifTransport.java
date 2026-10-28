package fr.efrei.transfret.tarification.tarif;

public interface TarifTransport {
    double calculer(int distance, double poids, double volume);
}
