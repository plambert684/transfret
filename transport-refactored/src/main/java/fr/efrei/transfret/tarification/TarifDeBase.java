package fr.efrei.transfret.tarification;

import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.tarification.tarif.TarifTransport;
import java.util.Map;
import java.util.Objects;

public class TarifDeBase implements CalculPrix {

    private final Map<ModeTransport, TarifTransport> tarifs;

    public TarifDeBase(Map<ModeTransport, TarifTransport> tarifs) {
        this.tarifs = Map.copyOf(tarifs);
    }

    @Override
    public double calculer(Expedition expedition) {
        return Objects.requireNonNull(tarifs.get(expedition.getMode()),
                "Aucun tarif défini pour le mode " + expedition.getMode())
                .calculer(expedition.getDistance(), expedition.getPoidsTotal(), expedition.getVolumeTotal());
    }
}
