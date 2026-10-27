package fr.efrei.transfret.expedition.delai;

public class DelaiRail implements StrategieDelai {

    @Override
    public int calculerDelai(int distance, boolean urgent) {
        int jours = (int) Math.ceil(distance / 1440.0) + 1;
        if (urgent) jours--;
        return jours;
    }
}