package fr.efrei.transfret;

public class DelaiMer implements StrategieDelai {

    private static final double DISTANCE_PAR_JOUR = 720.0;
    private static final int JOURS_SUPPLEMENTAIRES = 2;

    @Override
    public int estimer(int distance, boolean urgent) {
        return (int) Math.ceil(distance / DISTANCE_PAR_JOUR) + JOURS_SUPPLEMENTAIRES;
    }
}
