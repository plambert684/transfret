package fr.efrei.transfret;

public class DelaiRoute implements StrategieDelai {

    private static final double VITESSE_KM_PAR_HEURE = 70.0;
    private static final double HEURES_URGENT_PAR_JOUR = 18.0;
    private static final double HEURES_NORMAL_PAR_JOUR = 9.0;

    @Override
    public int estimer(int distance, boolean urgent) {
        double heures = distance / VITESSE_KM_PAR_HEURE;
        double heuresParJour = urgent ? HEURES_URGENT_PAR_JOUR : HEURES_NORMAL_PAR_JOUR;
        return (int) Math.ceil(heures / heuresParJour);
    }
}
