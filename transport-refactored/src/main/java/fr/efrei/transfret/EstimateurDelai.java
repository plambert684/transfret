package fr.efrei.transfret;

public class EstimateurDelai {

    private static final double VITESSE_ROUTE_KM_PAR_HEURE = 70.0;
    private static final double HEURES_ROUTE_URGENT_PAR_JOUR = 18.0;
    private static final double HEURES_ROUTE_NORMAL_PAR_JOUR = 9.0;
    private static final double DISTANCE_RAIL_PAR_JOUR = 1440.0;
    private static final int JOURS_SUPPLEMENTAIRES_RAIL = 1;
    private static final int JOURS_REDUITS_URGENCE_RAIL = 1;
    private static final double DISTANCE_MER_PAR_JOUR = 720.0;
    private static final int JOURS_SUPPLEMENTAIRES_MER = 2;
    private static final int SEUIL_DISTANCE_AIR_LONG_COURRIER = 3000;
    private static final int DELAI_AIR_STANDARD = 1;
    private static final int DELAI_AIR_LONG_COURRIER = 2;
    private static final int DELAI_MINIMUM = 1;

    public int estimer(int distance, ModeTransport mode, boolean urgent) {
        int jours = 0;
        switch (mode) {
            case ROUTE:
                double heures = distance / VITESSE_ROUTE_KM_PAR_HEURE;
                if (urgent) {
                    jours = (int) Math.ceil(heures / HEURES_ROUTE_URGENT_PAR_JOUR);
                } else {
                    jours = (int) Math.ceil(heures / HEURES_ROUTE_NORMAL_PAR_JOUR);
                }
                break;
            case RAIL:
                jours = (int) Math.ceil(distance / DISTANCE_RAIL_PAR_JOUR) + JOURS_SUPPLEMENTAIRES_RAIL;
                if (urgent) jours = jours - JOURS_REDUITS_URGENCE_RAIL;
                break;
            case MER:
                jours = (int) Math.ceil(distance / DISTANCE_MER_PAR_JOUR) + JOURS_SUPPLEMENTAIRES_MER;
                break;
            case AIR:
                jours = DELAI_AIR_STANDARD;
                if (distance > SEUIL_DISTANCE_AIR_LONG_COURRIER) jours = DELAI_AIR_LONG_COURRIER;
                if (urgent) jours = DELAI_AIR_STANDARD;
                break;
        }
        if (jours < DELAI_MINIMUM) jours = DELAI_MINIMUM;
        return jours;
    }
}
