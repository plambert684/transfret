package fr.efrei.transfret;

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
