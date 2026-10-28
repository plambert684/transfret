package fr.efrei.transfret.tarification;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarification.remise.PolitiqueRemise;
import fr.efrei.transfret.tarification.supplement.Assurance;
import fr.efrei.transfret.tarification.supplement.DecorateurRemise;
import fr.efrei.transfret.tarification.supplement.SupplementDangereux;
import fr.efrei.transfret.tarification.supplement.SupplementFragile;
import fr.efrei.transfret.tarification.supplement.SupplementHorsGabarit;
import fr.efrei.transfret.tarification.supplement.SupplementRefrigere;
import fr.efrei.transfret.tarification.supplement.SupplementUrgence;
import fr.efrei.transfret.tarification.tarif.TarifTransport;
import java.util.List;
import java.util.Map;

public class AssembleurPrix {

    private final Map<ModeTransport, TarifTransport> tarifs;
    private final Map<TypeClient, PolitiqueRemise> politiquesRemise;

    public AssembleurPrix(Map<ModeTransport, TarifTransport> tarifs,
                          Map<TypeClient, PolitiqueRemise> politiquesRemise) {
        this.tarifs = tarifs;
        this.politiquesRemise = politiquesRemise;
    }

    public CalculPrix assembler(Expedition expedition) {
        CalculPrix calcul = new TarifDeBase(tarifs);
        if (contient(expedition.getColis(), TypeMarchandise.FRAGILE)) {
            calcul = new SupplementFragile(calcul);
        }
        if (contient(expedition.getColis(), TypeMarchandise.DANGEREUX)) {
            calcul = new SupplementDangereux(calcul);
        }
        if (contient(expedition.getColis(), TypeMarchandise.REFRIGERE)) {
            calcul = new SupplementRefrigere(calcul);
        }
        if (SupplementHorsGabarit.concerne(expedition.getColis())) {
            calcul = new SupplementHorsGabarit(calcul);
        }
        if (expedition.isUrgent()) {
            calcul = new SupplementUrgence(calcul);
        }
        calcul = new DecorateurRemise(calcul, politiquesRemise);
        if (expedition.isAssurance()) {
            calcul = new Assurance(calcul);
        }
        return calcul;
    }

    private static boolean contient(List<Colis> colis, TypeMarchandise type) {
        for (Colis c : colis) {
            if (c.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
