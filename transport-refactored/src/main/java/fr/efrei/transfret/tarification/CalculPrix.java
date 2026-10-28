package fr.efrei.transfret.tarification;

import fr.efrei.transfret.modele.Expedition;

public interface CalculPrix {
    double calculer(Expedition expedition);
}
