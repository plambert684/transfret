package fr.efrei.transfret.expedition.persistance;

import fr.efrei.transfret.Expedition;

public interface ExpeditionRepository {

    void sauvegarderExpedition(Expedition expedition);

    Expedition trouverExpedition(String id);

    void mettreAJourStatut(Expedition expedition);
}