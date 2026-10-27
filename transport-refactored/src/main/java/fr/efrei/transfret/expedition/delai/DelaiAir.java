package fr.efrei.transfret.expedition.delai;

public class DelaiAir implements StrategieDelai {

    @Override
    public int calculerDelai(int distance, boolean urgent) {
        if (urgent || distance <= 3000) {
            return 1;
        }
        return 2;
    }
}