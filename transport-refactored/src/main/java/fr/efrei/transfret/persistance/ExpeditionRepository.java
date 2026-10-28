package fr.efrei.transfret.persistance;

import fr.efrei.transfret.modele.Expedition;
import java.util.List;

public interface ExpeditionRepository {

    void sauvegarderExpedition(Expedition expedition);

    void mettreAJourStatut(Expedition expedition);

    Expedition trouverExpedition(String id);

    List<Expedition> toutesLesExpeditions();
}
