package fr.efrei.transfret;

import java.util.Map;

public final class StrategiesTransport {

    private StrategiesTransport() {
    }

    public static Map<ModeTransport, TarifTransport> tarifs() {
        return Map.of(
                ModeTransport.ROUTE, new TarifRoute(),
                ModeTransport.RAIL, new TarifRail(),
                ModeTransport.MER, new TarifMer(),
                ModeTransport.AIR, new TarifAir());
    }

    public static Map<TypeClient, PolitiqueRemise> politiquesRemise() {
        return Map.of(
                TypeClient.PARTICULIER, new PolitiqueRemiseParticulier(),
                TypeClient.ENTREPRISE, new PolitiqueRemiseEntreprise(),
                TypeClient.PREMIUM, new PolitiqueRemisePremium());
    }

    public static Map<ModeTransport, StrategieDelai> delais() {
        return Map.of(
                ModeTransport.ROUTE, new DelaiRoute(),
                ModeTransport.RAIL, new DelaiRail(),
                ModeTransport.MER, new DelaiMer(),
                ModeTransport.AIR, new DelaiAir());
    }
}
