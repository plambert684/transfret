package fr.efrei.transfret.tarification.tarif;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifFluvialTest {

    private final TarifFluvial tarif = new TarifFluvial();

    @Test
    void calcule_au_km_et_au_kg() {
        assertEquals(365 * 0.40 + 10000 * 0.02, tarif.calculer(365, 10000, 0), 0.001);
    }

    @Test
    void applique_le_minimum() {
        assertEquals(300, tarif.calculer(365, 100, 0), 0.001);
    }
}
