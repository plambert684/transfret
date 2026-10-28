package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplementHorsGabaritTest {

    private static Colis colis(double longueur, double largeur, double hauteur) {
        return new Colis("Colis", 100, longueur, largeur, hauteur, TypeMarchandise.STANDARD, 1000);
    }

    @Test
    void majore_de_10_pourcent() {
        assertEquals(110, new SupplementHorsGabarit(e -> 100).calculer(new Expedition()), 0.001);
    }

    @Test
    void concerne_un_colis_de_plus_de_200_cm() {
        assertTrue(SupplementHorsGabarit.concerne(List.of(colis(100, 100, 100), colis(100, 201, 100))));
    }

    @Test
    void ne_concerne_pas_un_colis_de_200_cm() {
        assertFalse(SupplementHorsGabarit.concerne(List.of(colis(200, 200, 200))));
    }
}
