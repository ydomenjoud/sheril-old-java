import zIgzAg.jeu.oceane.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        // Récupération du numéro du tour
        Univers.chargerNumeroTour();
        Const.NOTIFY_BOT = false;
        Const.FAKE_TURN = true;
        new Univers(true, "Déroulement du tour...");

        Commandant c1 = Commandant.creerCommandant("P1", "p1@test.com", 4, new HashMap());
        Commandant c2 = Commandant.creerCommandant("P2", "p2@test.com", 1, new HashMap());

        c1.initialiserListesMessages();
        c2.initialiserListesMessages();
        ReceptionOrdres ro = new ReceptionOrdres();
        Univers.phaseSuivante();
        Position pos = new Position(0, 10, 10);

//         String n, int[][] comp, int vit, int att, int def, int mor, int march, int rac, int tour
        var hero1 = new Heros("fergok", new int[][]{}, 2, 2, 2, 2, 2, 4, 1);
        String[] v1 = { "Intercepteur standard", "Chasseur standard",
                "Torpilleur standard", "Bombardier standard",
                "Grand Bombardier standard", "Destroyer standard",
                "Fregate standard", "Croiseur standard",
                "Supercroiseur standard", "Eclaireur standard", "Colonisateur" };
        int[] n1 = { 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Flotte flotte1 = creerFlotte(c1, pos, v1, n1);
        c1.ajouterHeros(hero1);

        String[] v2 = { "Intercepteur standard", "Chasseur standard",
                "Torpilleur standard", "Bombardier standard",
                "Grand Bombardier standard", "Destroyer standard",
                "Fregate standard", "Croiseur standard",
                "Supercroiseur standard", "Eclaireur standard", "Colonisateur" };
//        int[] n2 = { 10, 20, 20, 20, 20, 10, 10, 5, 3, 3, 10};
        int[] n2 = { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        creerFlotte(c2, pos, v2, n2);

        // "FA;CA;FB;CB;TOUR;CATT;PLANA;RACEA;POSA;PLANB;POSB;DIST;ARME;CHANCE;MODIFIER;RESULT;SHIELDED;DEGAT"
        Combat.resolutionCombats();


    }

    static Flotte creerFlotte(Commandant c, Position pos, String[] v, int[] n){
        for (int i = 0; i < v.length; i++)
            if (!Univers.existencePlanDeVaisseau(v[i]))
                System.out.println(v[i]);
        int[] u = n.clone();

        Flotte flotte = new Flotte(Univers.getMessage(
                "DENOMINATION_FLOTTE_DE_DEPART", c.getLocale()), c
                .getCapitale());

        for (int i = 0; i < v.length; i++) {
            PlanDeVaisseau p = Univers.getPlanDeVaisseau(v[i]);
            for (int j = 0; j < u[i]; j++) {
                // centaures=centaures-p.getPrix();
                if (p == null) {
                    System.out.println("p : "+v[i]);
                }
                Vaisseau vai = Vaisseau.creer(p.getNom(), c.getRace());
                flotte.ajouterVaisseau(vai);
            }
        }
        flotte.setPosition(pos);
        flotte.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_TOUTE_FLOTTES);

        c.ajouterFlotte(flotte);
        return flotte;
    }
}
