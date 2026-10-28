package fr.efrei.transfret.config;

import fr.efrei.transfret.delai.DelaiAir;
import fr.efrei.transfret.delai.DelaiFluvial;
import fr.efrei.transfret.delai.DelaiMer;
import fr.efrei.transfret.delai.DelaiRail;
import fr.efrei.transfret.delai.DelaiRoute;
import fr.efrei.transfret.delai.StrategieDelai;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.tarification.remise.PolitiqueRemise;
import fr.efrei.transfret.tarification.remise.PolitiqueRemiseEntreprise;
import fr.efrei.transfret.tarification.remise.PolitiqueRemiseParticulier;
import fr.efrei.transfret.tarification.remise.PolitiqueRemisePremium;
import fr.efrei.transfret.tarification.tarif.TarifAir;
import fr.efrei.transfret.tarification.tarif.TarifFluvial;
import fr.efrei.transfret.tarification.tarif.TarifMer;
import fr.efrei.transfret.tarification.tarif.TarifRail;
import fr.efrei.transfret.tarification.tarif.TarifRoute;
import fr.efrei.transfret.tarification.tarif.TarifTransport;
import java.util.Map;

public final class StrategiesTransport {

    private StrategiesTransport() {
    }

    public static Map<ModeTransport, TarifTransport> tarifs() {
        return Map.of(
                ModeTransport.ROUTE, new TarifRoute(),
                ModeTransport.RAIL, new TarifRail(),
                ModeTransport.MER, new TarifMer(),
                ModeTransport.AIR, new TarifAir(),
                ModeTransport.FLUVIAL, new TarifFluvial());
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
                ModeTransport.AIR, new DelaiAir(),
                ModeTransport.FLUVIAL, new DelaiFluvial());
    }
}
