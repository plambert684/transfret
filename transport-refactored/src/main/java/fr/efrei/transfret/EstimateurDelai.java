package fr.efrei.transfret;

import java.util.Map;
import java.util.Objects;

public class EstimateurDelai {

    private static final int DELAI_MINIMUM = 1;

    private final Map<ModeTransport, StrategieDelai> delais;

    public EstimateurDelai(Map<ModeTransport, StrategieDelai> delais) {
        this.delais = Map.copyOf(delais);
    }

    public int estimer(int distance, ModeTransport mode, boolean urgent) {
        int jours = Objects.requireNonNull(delais.get(mode), "Aucun délai défini pour le mode " + mode)
                .estimer(distance, urgent);
        return Math.max(jours, DELAI_MINIMUM);
    }
}
