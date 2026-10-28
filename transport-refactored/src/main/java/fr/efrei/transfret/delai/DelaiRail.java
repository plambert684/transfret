package fr.efrei.transfret.delai;

public class DelaiRail implements StrategieDelai {

    private static final double DISTANCE_PAR_JOUR = 1440.0;
    private static final int JOURS_SUPPLEMENTAIRES = 1;
    private static final int JOURS_REDUITS_URGENCE = 1;

    @Override
    public int estimer(int distance, boolean urgent) {
        int jours = (int) Math.ceil(distance / DISTANCE_PAR_JOUR) + JOURS_SUPPLEMENTAIRES;
        return urgent ? jours - JOURS_REDUITS_URGENCE : jours;
    }
}
