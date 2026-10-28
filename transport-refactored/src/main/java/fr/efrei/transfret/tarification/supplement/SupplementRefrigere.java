package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.tarification.CalculPrix;
import fr.efrei.transfret.tarification.DecorateurPrix;

public class SupplementRefrigere extends DecorateurPrix {

    private static final double COEFFICIENT_REFRIGERE = 1.20;

    public SupplementRefrigere(CalculPrix composant) {
        super(composant);
    }

    @Override
    public double calculer(Expedition expedition) {
        return composant.calculer(expedition) * COEFFICIENT_REFRIGERE;
    }
}
