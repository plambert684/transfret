package fr.efrei.transfret;

import java.util.List;
import java.util.Map;

public class CalculateurPrix {

    private final AssembleurPrix assembleur;

    public CalculateurPrix(Map<ModeTransport, TarifTransport> tarifs,
                           Map<TypeClient, PolitiqueRemise> politiquesRemise) {
        this.assembleur = new AssembleurPrix(tarifs, politiquesRemise);
    }

    public double calculerPrixHT(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        Expedition expedition = new Expedition();
        expedition.setColis(colis);
        expedition.setDistance(distance);
        expedition.setMode(mode);
        expedition.setUrgent(urgent);
        expedition.setAssurance(assurance);
        Client client = new Client();
        client.setType(typeClient);
        expedition.setClient(client);
        return Utils.arrondir(assembleur.assembler(expedition).calculer(expedition));
    }
}
