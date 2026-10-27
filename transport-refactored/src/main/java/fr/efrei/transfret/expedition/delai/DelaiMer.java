package fr.efrei.transfret.expedition.delai;

public class DelaiMer implements StrategieDelai {

    @Override
    public int calculerDelai(int distance, boolean urgent) {
        return (int) Math.ceil(distance / 720.0) + 2;
    }
}