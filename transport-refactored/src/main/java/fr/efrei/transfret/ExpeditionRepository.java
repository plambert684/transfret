package fr.efrei.transfret;

import java.util.List;

public interface ExpeditionRepository {

    void sauvegarderExpedition(Expedition expedition);

    void mettreAJourStatut(Expedition expedition);

    Expedition trouverExpedition(String id);

    List<Expedition> toutesLesExpeditions();
}
