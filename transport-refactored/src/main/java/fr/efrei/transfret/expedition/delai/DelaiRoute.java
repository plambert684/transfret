package fr.efrei.transfret.expedition.delai;

public class DelaiRoute implements StrategieDelai {

    @Override
    public int calculerDelai(int distance, boolean urgent) {
        double heures = distance / 70.0;
        if (urgent) {
            return (int) Math.ceil(heures / 18);
        }
        return (int) Math.ceil(heures / 9);
    }
}