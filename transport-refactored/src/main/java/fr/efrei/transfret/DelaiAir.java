package fr.efrei.transfret;

public class DelaiAir implements StrategieDelai {

    private static final int SEUIL_LONG_COURRIER = 3000;
    private static final int DELAI_STANDARD = 1;
    private static final int DELAI_LONG_COURRIER = 2;

    @Override
    public int estimer(int distance, boolean urgent) {
        return urgent || distance <= SEUIL_LONG_COURRIER ? DELAI_STANDARD : DELAI_LONG_COURRIER;
    }
}
