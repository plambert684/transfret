package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.tarification.CalculPrix;
import fr.efrei.transfret.tarification.DecorateurPrix;

public class SupplementDangereux extends DecorateurPrix {

    private static final double COEFFICIENT_DANGEREUX = 1.30;

    public SupplementDangereux(CalculPrix composant) {
        super(composant);
    }

    @Override
    public double calculer(Expedition expedition) {
        return composant.calculer(expedition) * COEFFICIENT_DANGEREUX;
    }
}
