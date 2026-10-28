package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.tarification.CalculPrix;
import fr.efrei.transfret.tarification.DecorateurPrix;
import java.util.List;

public class SupplementHorsGabarit extends DecorateurPrix {

    private static final double COEFFICIENT_HORS_GABARIT = 1.10;
    private static final double DIMENSION_MAX_CM = 200;

    public SupplementHorsGabarit(CalculPrix composant) {
        super(composant);
    }

    public static boolean concerne(List<Colis> colis) {
        for (Colis c : colis) {
            if (c.getLongueur() > DIMENSION_MAX_CM || c.getLargeur() > DIMENSION_MAX_CM || c.getHauteur() > DIMENSION_MAX_CM) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double calculer(Expedition expedition) {
        return composant.calculer(expedition) * COEFFICIENT_HORS_GABARIT;
    }
}
