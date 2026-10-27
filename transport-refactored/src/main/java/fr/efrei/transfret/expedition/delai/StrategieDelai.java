package fr.efrei.transfret.expedition.delai;

public interface StrategieDelai {

    int calculerDelai(int distance, boolean urgent);
}