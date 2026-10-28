package fr.efrei.transfret;

import java.util.List;

public interface IVehicule {

    String getImmatriculation();

    double getCapaciteMax();

    double getChargeActuelle();

    boolean estDisponible();

    void setDisponible(boolean disponible);

    void charger(Colis colis);

    void decharger(Colis colis);

    boolean peutPrendreEnCharge(List<Colis> colis, ModeTransport mode);

    void partir(int distance);
}
