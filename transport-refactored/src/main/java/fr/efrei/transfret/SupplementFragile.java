package fr.efrei.transfret;

public class SupplementFragile extends DecorateurPrix {

    private static final double COEFFICIENT_FRAGILE = 1.15;

    public SupplementFragile(CalculPrix composant) {
        super(composant);
    }

    @Override
    public double calculer(Expedition expedition) {
        return composant.calculer(expedition) * COEFFICIENT_FRAGILE;
    }
}
