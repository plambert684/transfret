package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PenicheTest {

    private final Peniche peniche = new Peniche("PEN-0001", 2500000);

    private static Colis colis(double poids, TypeMarchandise type) {
        return new Colis("Colis", poids, 100, 100, 100, type, 1000);
    }

    @Test
    void accepte_le_fluvial() {
        assertTrue(peniche.peutPrendreEnCharge(List.of(colis(1000, TypeMarchandise.STANDARD)), ModeTransport.FLUVIAL));
    }

    @Test
    void refuse_les_autres_modes() {
        assertFalse(peniche.peutPrendreEnCharge(List.of(colis(1000, TypeMarchandise.STANDARD)), ModeTransport.MER));
    }

    @Test
    void refuse_le_refrigere() {
        assertFalse(peniche.peutPrendreEnCharge(List.of(colis(1000, TypeMarchandise.REFRIGERE)), ModeTransport.FLUVIAL));
    }

    @Test
    void refuse_au_dela_de_la_capacite() {
        assertFalse(peniche.peutPrendreEnCharge(List.of(colis(2500001, TypeMarchandise.STANDARD)), ModeTransport.FLUVIAL));
    }
}
