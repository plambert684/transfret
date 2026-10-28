package fr.efrei.transfret;

public class SupplementUrgence extends DecorateurPrix {

    private static final double COEFFICIENT_URGENCE = 1.5;

    public SupplementUrgence(CalculPrix composant) {
        super(composant);
    }

    @Override
    public double calculer(Expedition expedition) {
        return composant.calculer(expedition) * COEFFICIENT_URGENCE;
    }
}
