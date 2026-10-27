package fr.efrei.transfret.expedition.delai;

import java.util.Map;

public class EstimateurDelai {

    private final Map<String, StrategieDelai> strategies;

    public EstimateurDelai() {
        this(Map.of(
                "ROUTE", new DelaiRoute(),
                "RAIL", new DelaiRail(),
                "MER", new DelaiMer(),
                "AIR", new DelaiAir()
        ));
    }

    public EstimateurDelai(Map<String, StrategieDelai> strategies) {
        this.strategies = Map.copyOf(strategies);
    }

    public int estimer(int distance, String mode, boolean urgent) {
        StrategieDelai strategie = strategies.get(mode);
        int jours = strategie == null ? 0 : strategie.calculerDelai(distance, urgent);
        return Math.max(jours, 1);
    }
}