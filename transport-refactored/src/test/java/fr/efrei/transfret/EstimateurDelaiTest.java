package fr.efrei.transfret;

import fr.efrei.transfret.expedition.delai.EstimateurDelai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstimateurDelaiTest {

    private final EstimateurDelai estimateur = new EstimateurDelai();

    @Test
    void routeUrgenteUtiliseLaStrategieRoute() {
        assertEquals(2, estimateur.estimer(1400, "ROUTE", true));
    }

    @Test
    void airLongCourrierPrendDeuxJoursEtUnJourEnUrgence() {
        assertEquals(2, estimateur.estimer(3001, "AIR", false));
        assertEquals(1, estimateur.estimer(3001, "AIR", true));
    }

    @Test
    void merAjouteDeuxJoursDeTransit() {
        assertEquals(3, estimateur.estimer(720, "MER", false));
    }

    @Test
    void delaiMinimalEstUnJour() {
        assertEquals(1, estimateur.estimer(0, "ROUTE", false));
        assertEquals(1, estimateur.estimer(100, "MODE_INCONNU", false));
    }
}