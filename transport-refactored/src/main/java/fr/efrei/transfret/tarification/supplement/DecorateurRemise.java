package fr.efrei.transfret.tarification.supplement;

import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.tarification.CalculPrix;
import fr.efrei.transfret.tarification.DecorateurPrix;
import fr.efrei.transfret.tarification.remise.PolitiqueRemise;
import java.util.Map;
import java.util.Objects;

public class DecorateurRemise extends DecorateurPrix {

    private final Map<TypeClient, PolitiqueRemise> politiquesRemise;

    public DecorateurRemise(CalculPrix composant, Map<TypeClient, PolitiqueRemise> politiquesRemise) {
        super(composant);
        this.politiquesRemise = Map.copyOf(politiquesRemise);
    }

    @Override
    public double calculer(Expedition expedition) {
        double prix = composant.calculer(expedition);
        TypeClient typeClient = expedition.getClient().getType();
        return Objects.requireNonNull(politiquesRemise.get(typeClient),
                "Aucune politique de remise définie pour le type " + typeClient).appliquer(prix);
    }
}
