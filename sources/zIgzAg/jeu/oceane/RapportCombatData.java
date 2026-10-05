package zIgzAg.jeu.oceane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RapportCombatData {

    public String typeCombat; // "FLOTTE_PLANETE" ou "FLOTTE_FLOTTE"
    public String positionSysteme;
    public String nomSysteme;
    public int numPlanete = -1;
    public String nomPlanete;
    public int tourNumber;

    public ParticipantData attaquant = new ParticipantData();
    public ParticipantData defenseur = new ParticipantData();

    public List<EntiteCombatData> flotteAttaquante = new ArrayList<>();
    public List<EntiteCombatData> flotteDefenseuse = new ArrayList<>(); // Pour Flotte-Flotte

    public EntiteCombatData milice; // Pour Flotte-Planète
    public List<EntiteCombatData> batiments = new ArrayList<>(); // Pour Flotte-Planète

    public static class ParticipantData {
        public int id;
        public String nom;
        public int numFlotte = -1;
        public String nomFlotte;
    }

    public static class EntiteCombatData {
        public String nom;
        public int nombre;
        public int variationNombre;
        public int degatsEncaisses;
        public int variationDegats;
        public int degatsInfliges;
    }

    // =========================================================================
    // FACTORY 3 : État Initial Flotte vs Planète (Tour 0 / Avant échanges)
    // =========================================================================
    public static RapportCombatData fromInitialPlanete(
            Commandant c1, Commandant c2, Flotte f, Systeme s, int numPla,
            Map mf, Map mm, int popm) {

        RapportCombatData data = new RapportCombatData();
        data.typeCombat = "FLOTTE_PLANETE";
        data.positionSysteme = s.getPosition().toString();
        data.nomSysteme = s.getNom();
        data.numPlanete = numPla + 1;
        data.nomPlanete = s.getPlanete(numPla).getNom();
        data.tourNumber = 0; // 0 indique l'état au départ

        data.attaquant.id = c1.getNumero();
        data.attaquant.nom = c1.getNom();
        data.attaquant.nomFlotte = f.getNom();
        data.attaquant.numFlotte = c1.numeroFlotte(f);

        data.defenseur.id = c2.getNumero();
        data.defenseur.nom = c2.getNom();

        // 1. Vaisseaux au départ (champs issus de mf)
        data.flotteAttaquante = extraireVaisseauxInitiaux(mf);

        // 2. Milice au départ
        data.milice = new EntiteCombatData();
        data.milice.nom = "Milices";
        data.milice.nombre = Math.max(0, popm);

        // 3. Bâtiments au départ (champs issus de mm)
        for (Object key : mm.keySet()) {
            String codeBatiment = (String) key;
            Batiment b = (Batiment) Univers.getTechnologie(codeBatiment);
            int[] mT = (int[]) mm.get(codeBatiment);

            EntiteCombatData bat = new EntiteCombatData();
            bat.nom = Utile.maj(b.getNomComplet(c1.getLocale()));
            bat.nombre = mT[0];
            bat.degatsEncaisses = mT[1];

            data.batiments.add(bat);
        }

        return data;
    }

    // =========================================================================
    // FACTORY 4 : État Initial Flotte vs Flotte (Tour 0 / Avant échanges)
    // =========================================================================
    public static RapportCombatData fromInitialFlotte(
            Commandant c1, Commandant c2, Flotte f1, Flotte f2,
            Map m1, Map m2) {

        RapportCombatData data = new RapportCombatData();
        data.typeCombat = "FLOTTE_FLOTTE";
        data.positionSysteme = f1.getPosition().toString();
        data.tourNumber = 0;

        data.attaquant.id = c1.getNumero();
        data.attaquant.nom = c1.getNom();
        data.attaquant.nomFlotte = f1.getNom();
        data.attaquant.numFlotte = c1.numeroFlotte(f1);

        data.defenseur.id = c2.getNumero();
        data.defenseur.nom = c2.getNom();
        data.defenseur.nomFlotte = f2.getNom();
        data.defenseur.numFlotte = c2.numeroFlotte(f2);

        data.flotteAttaquante = extraireVaisseauxInitiaux(m1);
        data.flotteDefenseuse = extraireVaisseauxInitiaux(m2);

        return data;
    }

    // Helper pour capturer l'état des vaisseaux sans les variations
    private static List<EntiteCombatData> extraireVaisseauxInitiaux(Map m) {
        List<EntiteCombatData> liste = new ArrayList<>();
        if (m == null) return liste;

        for (Object key : m.keySet()) {
            String nomVso = (String) key;
            int[] mT = (int[]) m.get(nomVso);

            EntiteCombatData e = new EntiteCombatData();
            e.nom = nomVso;
            e.nombre = mT[0];
            e.degatsEncaisses = mT[1];
            e.degatsInfliges = 0; // Pas encore de dégâts infligés au départ

            liste.add(e);
        }
        return liste;
    }

    // =========================================================================
    // FACTORY 1 : Combat Flotte vs Planète
    // =========================================================================
    public static RapportCombatData fromCombatPlanete(
            Commandant c1, Commandant c2, Flotte f, Systeme s, int numPla, int tour,
            Map mf, Map mm, Map.Entry[] df, Map.Entry[] dm, Map nf, Map nm,
            boolean attaquant, int popm, int popn) {

        RapportCombatData data = new RapportCombatData();
        data.typeCombat = "FLOTTE_PLANETE";
        data.positionSysteme = s.getPosition().toString();
        data.nomSysteme = s.getNom();
        data.numPlanete = numPla + 1;
        data.nomPlanete = s.getPlanete(numPla).getNom();
        data.tourNumber = tour + 1;

        data.attaquant.id = c1.getNumero();
        data.attaquant.nom = c1.getNom();
        data.attaquant.nomFlotte = f.getNom();
        data.attaquant.numFlotte = c1.numeroFlotte(f);

        data.defenseur.id = c2.getNumero();
        data.defenseur.nom = c2.getNom();

        // 1. Flotte Attaquante
        data.flotteAttaquante = extraireVaisseaux(df, mf, nf);

        // 2. Milice
        int popAffichee = Math.max(0, popn);
        data.milice = new EntiteCombatData();
        data.milice.nom = "Milices";
        data.milice.nombre = popAffichee;
        data.milice.variationNombre = popAffichee - popm;

        // 3. Bâtiments
        for (int i = 0; i < dm.length; i++) {
            String codeBatiment = (String) dm[i].getKey();
            Batiment b = (Batiment) Univers.getTechnologie(codeBatiment);

            int[] dT = (int[]) dm[i].getValue();
            Object o = null;
            int[] mT = (o = mm.get(codeBatiment)) == null ? new int[2] : (int[]) o;
            int[] nT = (o = nm.get(codeBatiment)) == null ? new int[2] : (int[]) o;

            int nbCases = b.getPointsDeStructure();

            EntiteCombatData bat = new EntiteCombatData();
            bat.nom = Utile.maj(b.getNomComplet(c1.getLocale()));
            bat.nombre = nT[0];
            bat.variationNombre = nT[0] - mT[0];

            int dom = nT[1] + (-nT[0] + dT[0]) * nbCases;
            int domA = mT[1] + (-mT[0] + dT[0]) * nbCases;

            bat.degatsEncaisses = dom;
            bat.variationDegats = dom - domA;

            data.batiments.add(bat);
        }

        return data;
    }

    // =========================================================================
    // FACTORY 2 : Combat Flotte vs Flotte
    // =========================================================================
    public static RapportCombatData fromCombatFlotte(
            Commandant c1, Commandant c2, Flotte f1, Flotte f2,
            Map m1, Map m2, int tour,
            Map.Entry[] d1, Map.Entry[] d2, Map n1, Map n2) {

        RapportCombatData data = new RapportCombatData();
        data.typeCombat = "FLOTTE_FLOTTE";
        data.positionSysteme = f1.getPosition().toString();
        data.tourNumber = tour + 1;

        data.attaquant.id = c1.getNumero();
        data.attaquant.nom = c1.getNom();
        data.attaquant.nomFlotte = f1.getNom();
        data.attaquant.numFlotte = c1.numeroFlotte(f1);

        data.defenseur.id = c2.getNumero();
        data.defenseur.nom = c2.getNom();
        data.defenseur.nomFlotte = f2.getNom();
        data.defenseur.numFlotte = c2.numeroFlotte(f2);

        // 1. Vaisseaux Flotte 1 (Attaquant)
        data.flotteAttaquante = extraireVaisseaux(d1, m1, n1);

        // 2. Vaisseaux Flotte 2 (Défenseur)
        data.flotteDefenseuse = extraireVaisseaux(d2, m2, n2);

        return data;
    }

    // --- Helper interne pour éviter la duplication de code ---
    private static List<EntiteCombatData> extraireVaisseaux(Map.Entry[] d, Map m, Map n) {
        List<EntiteCombatData> liste = new ArrayList<>();
        for (int i = 0; i < d.length; i++) {
            String nomVso = (String) d[i].getKey();
            int[] dT = (int[]) d[i].getValue();
            Object o = null;
            int[] mT = (o = m.get(nomVso)) == null ? new int[3] : (int[]) o;
            int[] nT = (o = n.get(nomVso)) == null ? new int[3] : (int[]) o;

            PlanDeVaisseau plan = Univers.getPlanDeVaisseau(nomVso);
            int nbCases = plan != null ? plan.getNombreDeCases() : 1;

            EntiteCombatData e = new EntiteCombatData();
            e.nom = nomVso;
            e.nombre = nT[0];
            e.variationNombre = nT[0] - mT[0];

            int dom = nT[1] + (-nT[0] + dT[0]) * nbCases;
            int domA = mT[1] + (-mT[0] + dT[0]) * nbCases;

            e.degatsEncaisses = dom;
            e.variationDegats = dom - domA;
            e.degatsInfliges = nT[2];

            liste.add(e);
        }
        return liste;
    }

    public void write(){

    }
}