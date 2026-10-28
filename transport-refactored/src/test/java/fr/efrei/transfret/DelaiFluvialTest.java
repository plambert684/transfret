package fr.efrei.transfret;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiFluvialTest {

    private final DelaiFluvial delai = new DelaiFluvial();

    @Test
    void arrondit_au_superieur_et_ajoute_un_jour() {
        assertEquals(3, delai.estimer(365, false));
    }

    @Test
    void distance_exacte() {
        assertEquals(2, delai.estimer(360, false));
    }
}
