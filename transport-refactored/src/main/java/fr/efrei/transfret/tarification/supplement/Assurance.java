package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.tarification.CalculPrix;
import fr.efrei.transfret.tarification.DecorateurPrix;

public class Assurance extends DecorateurPrix {

    private static final double TAUX_ASSURANCE = 0.02;
    private static final double MONTANT_MINIMUM_ASSURANCE = 15;

    public Assurance(CalculPrix composant) {
        super(composant);
    }

    @Override
    public double calculer(Expedition expedition) {
        double prix = composant.calculer(expedition);
        double montantAssurance = expedition.getValeurTotale() * TAUX_ASSURANCE;
        if (montantAssurance < MONTANT_MINIMUM_ASSURANCE) {
            montantAssurance = MONTANT_MINIMUM_ASSURANCE;
        }
        return prix + montantAssurance;
    }
}
