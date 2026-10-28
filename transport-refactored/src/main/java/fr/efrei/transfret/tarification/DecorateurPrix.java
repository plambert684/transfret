package fr.efrei.transfret.tarification;

public abstract class DecorateurPrix implements CalculPrix {

    protected final CalculPrix composant;

    protected DecorateurPrix(CalculPrix composant) {
        this.composant = composant;
    }
}
