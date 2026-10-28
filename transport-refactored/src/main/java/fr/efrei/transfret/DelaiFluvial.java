package fr.efrei.transfret;

public class DelaiFluvial implements StrategieDelai {

    private static final double DISTANCE_PAR_JOUR = 360.0;
    private static final int JOURS_SUPPLEMENTAIRES = 1;

    @Override
    public int estimer(int distance, boolean urgent) {
        return (int) Math.ceil(distance / DISTANCE_PAR_JOUR) + JOURS_SUPPLEMENTAIRES;
    }
}
