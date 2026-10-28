package fr.efrei.transfret;

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
