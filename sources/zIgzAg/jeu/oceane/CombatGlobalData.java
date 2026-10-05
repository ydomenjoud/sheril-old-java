package zIgzAg.jeu.oceane;

import java.util.ArrayList;
import java.util.List;

public class CombatGlobalData {

    public String typeCombat; // "FLOTTE_PLANETE" ou "FLOTTE_FLOTTE"
    public String positionSysteme;
    public String nomSysteme;
    public int numPlanete = -1;
    public String nomPlanete;

    public ParticipantData attaquant = new ParticipantData();
    public ParticipantData defenseur = new ParticipantData();

    // État au tout début du combat (Tour 0 / Avant échanges)
    public RapportCombatData initial;

    // Liste chronologique des tours du combat
    public List<RapportCombatData> tours = new ArrayList<>();

    public static class ParticipantData {
        public int id;
        public String nom;
        public int numFlotte = -1;
        public String nomFlotte;
    }

    /**
     * Clé unique permettant d'identifier si un tour appartient à cette confrontation
     */
    public String getCleUnique() {
        if ("FLOTTE_PLANETE".equals(typeCombat)) {
            return String.format("FP_A%d_F%d_D%d_P%s_Pla%d",
                    attaquant.id, attaquant.numFlotte, defenseur.id, positionSysteme, numPlanete);
        } else {
            return String.format("FF_A%d_F%d_D%d_F%d_P%s",
                    attaquant.id, attaquant.numFlotte, defenseur.id, defenseur.numFlotte, positionSysteme);
        }
    }
}