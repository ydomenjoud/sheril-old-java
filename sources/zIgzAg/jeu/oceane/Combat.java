// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.text.MessageFormat;

import zIgzAg.utile.Mdt;
import zIgzAg.html.BaliseHTML;
import zIgzAg.html.DocumentHTML;

public class Combat {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH_mm_ss");
    private static BufferedWriter writer;
    private static String combatEnCours = "";

    public static void logln(String message, Object... parameters) {
        log(message, true, parameters);
    }

    public static void log(String message, Object... parameters) {
        log(message, false, parameters);
    }

    private static void log(String message, boolean newLine, Object... parameters) {
        try {
            if(writer == null) {
                Path dir = Paths.get(Chemin.RACINE, "tour" + Univers.getTour(), "combats");
                if (Files.notExists(dir)) {
                    Files.createDirectories(dir);
                }
                String file = dir.resolve(LocalTime.now().format(TIME_FORMAT) + "_combat.log").toString();
                writer = new BufferedWriter(new FileWriter(file, true));
            }
            writer.write(MessageFormat.format(message, parameters));
            if (newLine) {
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean commandantsAllies(Commandant c1, Commandant c2) {
        int[] a = c1.numerosAlliances();
        for (int i = 0; i < a.length; i++)
            if (c2.appartientAAlliance(a[i]))
                return true;
        return false;
    }

    public static boolean peutCombattre(int c1, int c2) {

        if (c1 == c2)
            return false;
        Commandant cI = Univers.getCommandant(c1);
        if (cI.existencePacteDeNonAgression(c2))
            return false;
        Commandant cII = Univers.getCommandant(c2);

        return !commandantsAllies(cI, cII);
    }

    public static void resolutionCombats() {
        Map m = Univers.listeFlottesDirectives();
        Position[] p = (Position[]) m.keySet().toArray(new Position[0]);
        for (int i = 0; i < p.length; i++) {
            resolutionCombatsSurUneCase((Map) m.get(p[i]), p[i]);
            m.remove(p[i]);
        }
    }

    public static void resolutionCombatsSurUneCase(Map h, Position p) {
        Integer[] d = (Integer[]) h.keySet().toArray(new Integer[0]);

        int[][][] vaisseauC = (int[][][]) h.values().toArray(new int[0][0][0]);
        int compteur = 0;
        for (int i = 0; i < vaisseauC.length; i++)
            compteur = compteur + vaisseauC[i].length;
        int[][] vaisseaux = new int[compteur][2];
        compteur = 0;
        for (int i = 0; i < vaisseauC.length; i++)
            for (int j = 0; j < vaisseauC[i].length; j++)
                vaisseaux[compteur++] = vaisseauC[i][j];
        int[] ordre2 = Utile.ordreAuHasard(vaisseaux.length);

        int[][] dP = new int[d.length][2];
        for (int i = 0; i < d.length; i++)
            dP[i] = Flotte.nombreDonneDirective(d[i].intValue());
        try {

            for (int i = 0; i < d.length; i++)
                if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR) {
                    int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                    for (int j = 0; j < vaisseauC[i].length; j++) {
                        int f = 0;

                        while ((f = resolutionAttaqueFlotteJoueur(vaisseaux,
                                dP[i][1], vaisseauC[i][ordre[j]], f, ordre2)) != -1)
                            ;
                    }
                }

            for (int i = 0; i < d.length; i++)
                if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE) {
                    int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                    for (int j = 0; j < vaisseauC[i].length; j++) {
                        resolutionAttaqueFlottePreventive(vaisseauC, dP,
                                vaisseauC[i][ordre[j]]);
                    }
                }

            for (int i = 0; i < d.length; i++)
                if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_TOUTE_FLOTTES) {
                    int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                    for (int j = 0; j < vaisseauC[i].length; j++) {
                        int f = 0;
                        while ((f = resolutionAttaqueFlotteToutes(vaisseaux,
                                vaisseauC[i][ordre[j]], f, ordre2)) != -1)
                            ;
                    }
                }

            if (Univers.existenceSysteme(p)) {
                Systeme s = Univers.getSysteme(p);

                for (int i = 0; i < d.length; i++)
                    if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_SYSTEME) {
                        int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                        for (int j = 0; j < vaisseauC[i].length; j++)
                            resolutionAttaqueSysteme(s, vaisseauC[i][ordre[j]]);
                    }

                for (int i = 0; i < d.length; i++)
                    if (dP[i][0] == Const.DIRECTIVE_FLOTTE_PILLAGE_SYSTEME) {
                        int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                        for (int j = 0; j < vaisseauC[i].length; j++)
                            resolutionAttaqueSysteme(s, vaisseauC[i][ordre[j]]);
                    }

                for (int i = 0; i < d.length; i++)
                    if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE) {
                        int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                        for (int j = 0; j < vaisseauC[i].length; j++)
                            resolutionAttaquePlanete(s, vaisseauC[i][ordre[j]],
                                    dP[i][1]);
                    }

                for (int i = 0; i < d.length; i++)
                    if (dP[i][0] == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE) {
                        int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                        for (int j = 0; j < vaisseauC[i].length; j++)
                            resolutionAttaquePlanete(s, vaisseauC[i][ordre[j]],
                                    dP[i][1]);
                    }

                for (int i = 0; i < d.length; i++)
                    if (dP[i][0] == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE) {
                        int[] ordre = Utile.ordreAuHasard(vaisseauC[i].length);
                        for (int j = 0; j < vaisseauC[i].length; j++)
                            resolutionAttaquePlanete(s, vaisseauC[i][ordre[j]],
                                    dP[i][1]);
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
            for (int k = 0; k < d.length; k++) {
                System.out.println(dP[k][0] + "-" + dP[k][1] + ":");
                for (int m = 0; m < vaisseauC[k].length; m++)
                    System.out.println("*" + vaisseauC[k][m][0] + "-"
                            + vaisseauC[k][m][1]);
            }
            System.out.println(p);
            System.exit(0);
        }

    }

    public static int resolutionAttaqueFlotteJoueur(int[][] v, int c,
                                                    int[] index, int depart, int[] ordre2) {
        for (int i = depart; i < v.length; i++)
            if ((v[ordre2[i]][0] == c) && (peutCombattre(index[0], c))) {
                if (attaqueFlotte(index, v[ordre2[i]]))
                    return i + 1;
                else
                    return -1;
            }
        return -1;
    }

    public static void resolutionAttaqueFlottePreventive(int[][][] v,
                                                         int[][] dP, int[] index) {
        for (int i = 0; i < v.length; i++)
            if ((dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_SYSTEME)
                    || (dP[i][0] == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)
                    || (dP[i][0] == Const.DIRECTIVE_FLOTTE_PILLAGE_SYSTEME)
                    || (dP[i][0] == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)
                    || (dP[i][0] == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE))
                for (int j = 0; j < v[i].length; j++)
                    if (peutCombattre(index[0], v[i][j][0]))
                        if (!attaqueFlotte(index, v[i][j]))
                            return;
    }

    public static int resolutionAttaqueFlotteToutes(int[][] v, int[] index,
                                                    int depart, int[] ordre2) {
        for (int i = depart; i < v.length; i++)
            if (peutCombattre(index[0], v[ordre2[i]][0]))
                if (attaqueFlotte(index, v[ordre2[i]]))
                    return i + 1;
                else
                    return -1;
        return -1;
    }

    public static boolean attaqueFlotte(int[] a, int[] d) {
        Commandant aC = Univers.getCommandant(a[0]);
        if (!aC.existenceFlotte(a[1]))
            return false;
        Flotte aF = aC.getFlotte(a[1]);
        if (aF.getDirective() == Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE)
            return false;
        Commandant dC = Univers.getCommandant(d[0]);
        if (!dC.existenceFlotte(d[1]))
            return false;

        if ((dC.getNumero() != 0) && (aC.getNumero() != 0)) {
            Univers.ajouterTransfert(aC, dC, "C*attaque flotte");
            RapportCombat rapport = new RapportCombat(aF.getPosition(), aC, dC,
                    RapportCombat.TYPE_COMBAT_SPATIAL);
            rapport.setCombatSpatial(aF, dC.getFlotte(d[1]));
            Univers.ajouterRapportCombat(rapport);
        }

        return combatFlotteFlotte(aC, dC, a[1], d[1]);
    }

    public static void resolutionAttaqueSysteme(Systeme s, int[] v) {
        Commandant c = Univers.getCommandant(v[0]);
        if (!c.existenceFlotte(v[1]))
            return;
        Flotte f = c.getFlotte(v[1]);
        if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE)
            return;
        int[] ordre = Utile.ordreAuHasard(s.getNombrePlanetes());

        // System.out.println("FS(");

        Univers.phaseSuivante();
        boolean pillage = (f.getDirective() == Const.DIRECTIVE_FLOTTE_PILLAGE_SYSTEME);

        int nbTour = 0;
        int inter = 0;
        int memPla = -1;
        int nbPrise = 0;
        int[] dommages = new int[2]; // dommages des flotte 0 -> pt domages
        // 1->Vaisseaux dÃ©truits.
        int[][] nbPrisesd = null; // 0 numÃ©ro de commandant 1 nombre de
        // planÃštes prises
        Map<Integer, int[]> numPrises = new HashMap<>(); // les numÃ©ros des
        // planÃštes prises
        for (int i = 0; i < ordre.length; i++)
            if (peutCombattre(v[0], s.getPlanete(ordre[i]).getProprio())) {
                if (memPla == -1)
                    memPla = ordre[i];
                if (Mdt.indexCorrespondant(nbPrisesd, s.getPlanete(ordre[i])
                        .getProprio()) == -1) {
                    int[] inter2 = new int[2];
                    inter2[0] = s.getPlanete(ordre[i]).getProprio();
                    nbPrisesd = Mdt.ajoutCoupleIndex(nbPrisesd, inter2);
                }
                Commandant d = Univers.getCommandant(s.getPlanete(ordre[i])
                        .getProprio());
                if ((d.getNumero() != 0)
                        && (Mdt.valeurCorrespondante(nbPrisesd, d.getNumero()) == 0)) {
                    Univers.ajouterTransfert(c, d, "C*attaque système");
                    Univers.ajouterTransfert(d, c, "C*attaque flotte");
                }
                if ((inter = combatFlottePlanete(c, v[1], d, s, ordre[i],
                        nbTour, dommages)) == -1) {
                    if (s.getPlanete(ordre[i]).getProprio() != d.getNumero()) {
                        nbPrise++;
                        Mdt.modifierIndex(nbPrisesd, d.getNumero(), 1);
                        int[] interx = (int[]) numPrises.get(d.getNumero());
                        interx = Mdt.ajout(interx, ordre[i]);
                        numPrises.put(d.getNumero(), interx);
                    }
                    break;
                }
                nbTour = inter;
                nbPrise++;
                Mdt.modifierIndex(nbPrisesd, d.getNumero(), 1);
                int[] interx = numPrises.get(d.getNumero());
                interx = Mdt.ajout(interx, ordre[i]);
                numPrises.put(d.getNumero(), interx);
            }

        if (memPla != -1) {
            if (pillage)
                c.ajouterEvenement("COMBAT_SYSTEME_0009",
                        f.getNomNumero(c.numeroFlotte(f)), s.getPosition(),
                        nbPrise);
            else
                c.ajouterEvenement("COMBAT_SYSTEME_0006",
                        f.getNomNumero(c.numeroFlotte(f)), s.getPosition(),
                        nbPrise);
            c.ajouterEvenement(
                    "{0}",
                    Rapport.getALienE(
                            Rapport.DETAIL_COMBAT + "#"
                                    + Integer.toString(c.getNumero()) + "-"
                                    + s.getPosition().toString()).ajout(
                            Rapport.getTextI(Univers.getMessageInfo(
                                    "COMBAT_FLOTTE_0006", c.getLocale()))));
            for (int i = 0; i < nbPrisesd.length; i++) {
                Commandant c2 = Univers.getCommandant(nbPrisesd[i][0]);
                c2.ajouterEvenement("COMBAT_SYSTEME_0007",
                        f.getNomNumero(c.numeroFlotte(f)), s.getPosition(),
                        c.getNomNumero(), nbPrisesd[i][1]);
                c2.ajouterEvenement(
                        "{0}",
                        Rapport.getALienE(
                                        Rapport.DETAIL_COMBAT + "#"
                                                + Integer.toString(c2.getNumero())
                                                + "-" + s.getPosition().toString())
                                .ajout(Rapport.getTextI(Univers.getMessageInfo(
                                        "COMBAT_FLOTTE_0006", c2.getLocale()))));
                if (c2.getNumero() != 0) {
                    int[] listePrises = (int[]) numPrises.get(c2.getNumero());
                    RapportCombat rapport = new RapportCombat(s.getPosition(),
                            c, c2, RapportCombat.TYPE_COMBAT_PLANETAIRE);
                    rapport.setCombatPlanetaire(f, s, listePrises);
                    Univers.ajouterRapportCombat(rapport);
                }
            }
        } else
            c.ajouterEvenement("COMBAT_SYSTEME_0008",
                    f.getNomNumero(c.numeroFlotte(f)));

        f.setDirective(Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE);
    }

    public static void resolutionAttaquePlanete(Systeme s, int[] v, int numPla) {
        Commandant c = Univers.getCommandant(v[0]);
        if (!c.existenceFlotte(v[1]))
            return;
        Flotte f = c.getFlotte(v[1]);
        if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE)
            return;
        if (s.getNombrePlanetes() <= numPla)
            return;

        // System.out.println("Debut d'un combat Flotte-PlanÃšte.");
        Univers.phaseSuivante();

        if (peutCombattre(v[0], s.getPlanete(numPla).getProprio())) {
            Commandant d = Univers.getCommandant(s.getPlanete(numPla)
                    .getProprio());
            if (d.getNumero() != 0)
                Univers.ajouterTransfert(c, d, "C*attaque planÃšte");
            combatFlottePlanete(c, v[1], d, s, numPla, 0, null);
            if (d.getNumero() != 0) {
                RapportCombat rapport = new RapportCombat(s.getPosition(), c,
                        d, RapportCombat.TYPE_COMBAT_PLANETAIRE);
                if (s.getPlanete(numPla).getProprio() != d.getNumero())
                    rapport.setCombatPlanetaire(f, s, new int[]{numPla});
                else
                    rapport.setCombatPlanetaire(f, s, null);
                Univers.ajouterRapportCombat(rapport);
            }
        } else
            c.ajouterEvenement("COMBAT_SYSTEME_0014",
                    f.getNomNumero(c.numeroFlotte(f)));
        f.setDirective(Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE);
    }

    public static int combatFlottePlanete(Commandant c1, int numFlotte,
                                          Commandant c2, Systeme s, int numPla, int tour, int[] dommagesFlotte) {

        Flotte f = c1.getFlotte(numFlotte);

        if (f.getPuissance() < Const.PUISSANCE_ATTAQUE_PLANETAIRE_MINIMALE) {
            c1.ajouterEvenement("COMBAT_SYSTEME_0015",
                    f.getNomNumero(numFlotte));
            return -1;
        }

        Heros h = c1.getHerosSurFlotte(numFlotte);
        if (h == null)
            h = Heros.HEROS_NON_PRESENT;

        Planete p = s.getPlanete(numPla);
        Gouverneur g = c2.getGouverneurSurPossession(s.getPosition());
        if (g == null)
            g = Gouverneur.GOUVERNEUR_NON_PRESENT;
        int nbTourMax = f.calculeCombativiteMoyenne(h);
        StrategieDeCombatSpatial strategie = c1.getStrategie(f.getStrategie());

        int division = 1 + Math.max(0,
                10 - (Univers.getTour() - c1.getTourArrivee()));
        int nbPopDefensive = ((c2.estJoueurNeutre()) ? ((p.populationTotale()
                * p.getStabilite() / 100) / division)
                : ((p.populationTotale() * p.getStabilite()) / 100));

        if (c2.getPossession(s.getPosition()).getPolitique() == Const.POLITIQUE_DEFENSE)
            nbPopDefensive = Math.min(p.populationTotale(), nbPopDefensive
                    + nbPopDefensive / 2);
        if (c2.getPossession(s.getPosition()).possedeStockImportantPoste(
                Const.PRODUIT_ARMEMENT))
            nbPopDefensive = Math.min(p.populationTotale(),
                    (nbPopDefensive * 3) / 2);
        // Pour les atalantes et fergok bonus de défense
        if (c2.getRace() == 1 || c2.getRace() == 4)
            nbPopDefensive = Math.min(p.populationTotale() * 3 / 2,
                    (nbPopDefensive * 3) / 2);
        Map.Entry[] materielPlanete = (Map.Entry[]) p
                .listeEquipementsNombresDommages().entrySet()
                .toArray(new Map.Entry[0]);
        Map.Entry[] memfin = (Map.Entry[]) f.listeVaisseauxParTypePourCombat()
                .entrySet().toArray(new Map.Entry[0]);
        Map materiel = null;
        Map mem = null;

        int memoirePop = nbPopDefensive;
        int memoireTot = p.populationTotale() / 10;

        boolean finDeTour = false;
        int numTour = tour;
        while (!finDeTour) {

            ConstructionPlanetaire[] listeC = p.getBatiments();
            f.preparerAuCombat(false);

            mem = f.listeVaisseauxParTypePourCombat();
            materiel = p.listeEquipementsNombresDommages();
            int memoirePopTour = nbPopDefensive;

            ArrayList strato = f.forceAttaqueStratospherique(strategie
                    .getAgressivite());
            ArrayList sol = f.forceAttaqueAirSol(strategie.getAgressivite());

            tirDefensesPlanetaires(listeC, strato, sol, g, h, true);
            nbPopDefensive = tirAirSol(strato, listeC, nbPopDefensive, true, g,
                    h);
            tirMilicesPlanetaires(nbPopDefensive, sol, g, h);

            p.eliminerPertesBatiments();
            listeC = p.getBatiments();

            nbPopDefensive = tirAirSol(sol, listeC, nbPopDefensive, false, g, h);

            f.eliminerPertesVaisseaux();
            p.eliminerPertesBatiments();

            Map inter1 = p.listeEquipementsNombresDommages();
            Map inter2 = f.listeVaisseauxParTypePourCombat();
            ecrireDetailCombatFlottePlanete(c1, c2, f, s, numPla, numTour, mem,
                    materiel, memfin, materielPlanete, inter2, inter1, true,
                    memoirePopTour, nbPopDefensive);
            ecrireDetailCombatFlottePlanete(c2, c1, f, s, numPla, numTour, mem,
                    materiel, memfin, materielPlanete, inter2, inter1, false,
                    memoirePopTour, nbPopDefensive);
            materiel = inter1;
            mem = inter2;

            if (nbPopDefensive <= 0)
                finDeTour = true;
            if (f.getNombreDeVaisseaux() == 0)
                finDeTour = true;
            numTour++;
            if (numTour == nbTourMax)
                finDeTour = true;
        }

        f.finaliserCombat();

        if (nbPopDefensive <= 0) {
            if (dommagesFlotte == null) {
                if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE) {
                    c1.ajouterEvenement("COMBAT_SYSTEME_0000",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumero());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0001",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumero());
                }
                if (f.getDirective() == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE) {
                    c1.ajouterEvenement("COMBAT_SYSTEME_0010",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumero());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0011",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumero());
                }
                if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE) {
                    c1.ajouterEvenement("COMBAT_SYSTEME_0012",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumero());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0013",
                            f.getNomNumero(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumero());
                }

            }
            if ((f.getDirective() == Const.DIRECTIVE_FLOTTE_ATTAQUE_SYSTEME)
                    || (f.getDirective() == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)) {
                Commandant.transfertPlanete(c2, c1, s, numPla);
                if ((c2.estJoueurHumain())
                        && (c2.getStatutReputationIndex() >= 2)) {
                    c1.ajouterReputation(Const.REPUTATION_ATTAQUER_PLANETE);
                    Univers.ajouterRelationRaces(s.getPosition(), c1.getRace(),
                            c2.getRace(), Const.RELATION_ATTAQUE_PLANETE);
                    if ((c2.estJoueurHumain())
                            && (c2.getStatutReputationIndex() >= 2)
                            && c1.existenceCapitale()
                            && s.getPosition().getNumeroSecteur() != c1
                            .getCapitale().getNumeroSecteur()) {
                        Univers.ajouterRelationRaces(c1.getCapitale(),
                                c1.getRace(), c2.getRace(),
                                Const.RELATION_ATTAQUE_PLANETE);
                    }
                }
            }
            if ((f.getDirective() == Const.DIRECTIVE_FLOTTE_PILLAGE_SYSTEME)
                    || (f.getDirective() == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)) {
                Commandant neutre = Univers.getCommandant(0);
                Commandant.transfertPlanete(c2, neutre, s, numPla);
                c1.modifierBudget(Const.BUDGET_COMMANDANT_PILLAGE_PLANETE,
                        (float) memoirePop - Math.max(0, nbPopDefensive));
                c1.ajouterReputation(Const.REPUTATION_ATTAQUER_PLANETE
                        - memoirePop - Math.max(0, nbPopDefensive));
                Univers.ajouterRelationRaces(s.getPosition(), c1.getRace(),
                        c2.getRace(), Const.RELATION_ATTAQUE_PILLAGE);
                if (c1.existenceCapitale()
                        && s.getPosition().getNumeroSecteur() != c1
                        .getCapitale().getNumeroSecteur()) {
                    Univers.ajouterRelationRaces(c1.getCapitale(),
                            c1.getRace(), c2.getRace(),
                            Const.RELATION_ATTAQUE_PILLAGE);
                }

                s.getPlanete(numPla).diminuerPopulation(
                        s.getPlanete(numPla).populationTotale());
            }
            if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE) {
                Commandant neutre = Univers.getCommandant(0);
                Commandant.transfertPlanete(c2, neutre, s, numPla);
                c1.ajouterReputation(Const.REPUTATION_ATTAQUER_PLANETE
                        - p.populationMaximaleTotale());
                Univers.ajouterRelationRaces(s.getPosition(), c1.getRace(),
                        c2.getRace(), Const.RELATION_ATTAQUE_ERADICATION);

                if (c1.existenceCapitale()
                        && s.getPosition().getNumeroSecteur() != c1
                        .getCapitale().getNumeroSecteur()) {
                    Univers.ajouterRelationRaces(c1.getCapitale(),
                            c1.getRace(), c2.getRace(),
                            Const.RELATION_ATTAQUE_ERADICATION);
                }

                p.initialiserPopulation();
            }
            if (!s.estProprio(c2.getNumero()))
                if (g != Gouverneur.GOUVERNEUR_NON_PRESENT)
                    g.mourir(c2);
        } else if (dommagesFlotte == null) {
            c1.ajouterEvenement("COMBAT_SYSTEME_0002",
                    f.getNomNumero(numFlotte), s.getNomNumeroPlanete(numPla),
                    s.getPosition(), c2.getNomNumero());
            c2.ajouterEvenement("COMBAT_SYSTEME_0003",
                    f.getNomNumero(numFlotte), s.getNomNumeroPlanete(numPla),
                    s.getPosition(), c1.getNomNumero());
        }
        if (dommagesFlotte == null) {
            c1.ajouterEvenement(
                    "{0}",
                    Rapport.getALienE(
                            Rapport.DETAIL_COMBAT + "#"
                                    + Integer.toString(c1.getNumero()) + "-"
                                    + s.getPosition().toString()).ajout(
                            Rapport.getTextI(Univers.getMessageInfo(
                                    "COMBAT_FLOTTE_0006", c1.getLocale()))));
            c2.ajouterEvenement(
                    "{0}",
                    Rapport.getALienE(
                            Rapport.DETAIL_COMBAT + "#"
                                    + Integer.toString(c2.getNumero()) + "-"
                                    + s.getPosition().toString()).ajout(
                            Rapport.getTextI(Univers.getMessageInfo(
                                    "COMBAT_FLOTTE_0006", c2.getLocale()))));
        }

        if (f.getNombreDeVaisseaux() == 0) {
            if (h != Heros.HEROS_NON_PRESENT)
                h.mourir(c1);
            c1.eliminerFlotte(numFlotte);
        }

        s.getPlanete(numPla).diminuerPopulation(
                Math.min(memoirePop - Math.max(0, nbPopDefensive), memoireTot));

        if ((numTour == nbTourMax) || (f.getNombreDeVaisseaux() == 0))
            return -1;
        else
            return numTour;
    }

    private static void ecrireDetailCombatFlottePlanete(Commandant c1,
                                                        Commandant c2, Flotte f, Systeme s, int numPla, int tour, Map mf,
                                                        Map mm, Map.Entry[] df, Map.Entry[] dm, Map nf, Map nm,
                                                        boolean attaquant, int popm, int popn) {
        String[] t = (String[]) Univers.getMessageRapport("COMBAT_FLOTTE",
                c1.getLocale());
        String[] t2 = (String[]) Univers.getMessageRapport("COMBAT_PLANETE",
                c1.getLocale());

        if (attaquant)
            c1.ajouterCombat("COMBAT_SYSTEME_0005", c2.getNomNumero(),
                    f.getNomNumero(c1.numeroFlotte(f)),
                    s.getNomNumeroPlanete(numPla), s.getPosition(), tour + 1);
        else
            c1.ajouterCombat("COMBAT_SYSTEME_0004", c2.getNomNumero(),
                    f.getNomNumero(c2.numeroFlotte(f)),
                    s.getNomNumeroPlanete(numPla), s.getPosition(), tour + 1);

        c1.ajouterCombat(Rapport.getABorne(Integer.toString(c1.getNumero())
                + "-" + s.getPosition().toString()));
        if (attaquant) {
            c1.ajouterCombat(ecrireDetailCombatVaisseaux(c1, f, mf, df, nf, t));
            c1.ajouterCombat(ecrireDetailCombatPlanete(c2, s, numPla, mm, dm,
                    nm, popm, popn, t2));
        } else {
            c1.ajouterCombat(ecrireDetailCombatPlanete(c1, s, numPla, mm, dm,
                    nm, popm, popn, t2));
            c1.ajouterCombat(ecrireDetailCombatVaisseaux(c2, f, mf, df, nf, t));
        }
    }

    private static BaliseHTML ecrireDetailCombatPlanete(Commandant c,
                                                        Systeme s, int numPla, Map m, Map.Entry[] d, Map n, int popm,
                                                        int popn, String[] t) {
        BaliseHTML[][] a = new BaliseHTML[4 + d.length][3];
        int ligne = 0;
        MessageFormat message = new MessageFormat(t[0]);
        String[] inter2 = new String[2];
        inter2[0] = s.getNomNumeroPlanete(numPla);
        inter2[1] = c.getNomNumero();
        a[ligne++][0] = Rapport.getTD("center", "3").ajout(
                Rapport.getText(message.format(inter2)));
        a[ligne][0] = Rapport.getTD("center", null).setTexteContenu("&nbsp;");
        a[ligne][1] = Rapport.getTD("center", "2").ajout(
                Rapport.getFont(Rapport.cC[4], null).ajout(
                        Rapport.getText(t[2])));
        ligne++;
        a[ligne][0] = Rapport.getTD("center", null)
                .ajout(Rapport.getText(t[1]));
        if (Math.max(0, popn) == popm)
            a[ligne][1] = Rapport.getTD("center", "2").ajout(
                    Rapport.getText(Integer.toString(Math.max(0, popn))));
        else
            a[ligne][1] = Rapport
                    .getTD("center", "2")
                    .ajout(Rapport.getText(Integer.toString(Math.max(0, popn))))
                    .ajout(Rapport
                            .getFont(Rapport.cC[6], null)
                            .ajout(Rapport.getText("("
                                    + Integer.toString(Math.max(0, popn) - popm)
                                    + ")")));
        ligne++;
        if (d.length > 0) {
            a[ligne][0] = Rapport.getTD("center", null).ajout(
                    Rapport.getFont(Rapport.cC[4], null).ajout(
                            Rapport.getText(t[4])));
            a[ligne][1] = Rapport.getTD("center", null).ajout(
                    Rapport.getFont(Rapport.cC[4], null).ajout(
                            Rapport.getText(t[2])));
            a[ligne][2] = Rapport.getTD("center", null).ajout(
                    Rapport.getFont(Rapport.cC[4], null).ajout(
                            Rapport.getText(t[3])));
            ligne++;
            for (int i = 0; i < d.length; i++) {
                Batiment b = (Batiment) Univers.getTechnologie((String) d[i]
                        .getKey());
                a[ligne][0] = Rapport.getTD("center", null)
                        .ajout(Rapport.getText(Utile.maj(b.getNomComplet(c
                                .getLocale()))));
                int[] dT = (int[]) d[i].getValue();
                Object o = null;
                int[] mT = ((o = m.get(d[i].getKey())) == null ? new int[2]
                        : (int[]) o);
                int[] nT = ((o = n.get(d[i].getKey())) == null ? new int[2]
                        : (int[]) o);
                int nbCases = b.getPointsDeStructure();
                if (mT[0] == nT[0])
                    a[ligne][1] = Rapport.getTD("center", null).ajout(
                            Rapport.getText(Integer.toString(nT[0])));
                else
                    a[ligne][1] = Rapport
                            .getTD("center", null)
                            .ajout(Rapport.getText(Integer.toString(nT[0])))
                            .ajout(Rapport.getFont(Rapport.cC[6], null).ajout(
                                    Rapport.getText("("
                                            + Integer.toString(nT[0] - mT[0])
                                            + ")")));
                int dom = nT[1] + (-nT[0] + dT[0]) * nbCases;
                int domA = mT[1] + (-mT[0] + dT[0]) * nbCases;
                if (dom == domA)
                    a[ligne][2] = Rapport.getTD("center", null).ajout(
                            Rapport.getText(Integer.toString(dom)));
                else
                    a[ligne][2] = Rapport
                            .getTD("center", null)
                            .ajout(Rapport.getText(Integer.toString(dom)))
                            .ajout(Rapport.getFont(Rapport.cC[3], null).ajout(
                                    Rapport.getText("(+"
                                            + Integer.toString(dom - domA)
                                            + ")")));
                ligne++;
            }
        }

        return Rapport.getDiv().ajout(
                DocumentHTML.creerTable(Rapport.getTable(), a));
    }

    private static void tirDefensesPlanetaires(ConstructionPlanetaire[] listeC,
                                               ArrayList strato, ArrayList sol, Gouverneur g, Heros h,
                                               boolean boutPortant) {
        ArrayList inter = null;
        if (strato.size() > 0)
            inter = strato;
        else
            inter = sol;
        for (int i = 0; i < listeC.length; i++)
            for (int j = 0; j < Const.NOMBRE_SALVE_BATTERIE; j++) {
                listeC[i].tir(
                        (Vaisseau) inter.get(Univers.getInt(inter.size())), g,
                        h, boutPortant);
            }
    }

    private static void tirMilicesPlanetaires(int nbPopDefensives,
                                              ArrayList sol, Gouverneur g, Heros h) {
        ConstructionPlanetaire[] c = new ConstructionPlanetaire[1];
        c[0] = new ConstructionPlanetaire("battlaI");
        int nbTirs = 0;
        if (nbPopDefensives > 50)
            nbTirs = 1 + (nbPopDefensives / (2 * Const.NOMBRE_SALVE_BATTERIE));
        if (sol.size() > 0)
            for (int i = 0; i < nbTirs; i++)
                tirDefensesPlanetaires(c, sol, sol, g, h, false);
    }

    private static int tirAirSol(ArrayList strato,
                                 ConstructionPlanetaire[] listeC, int nbPopDefensive,
                                 boolean construCible, Gouverneur g, Heros h) {
        int retour = nbPopDefensive;
        ConstructionPlanetaire[] cibles = null;

        ArrayList listeBoucliers = new ArrayList();
        for (int i = 0; i < listeC.length; i++)
            if (listeC[i].estBouclier())
                listeBoucliers.add(listeC[i]);
        if (listeBoucliers.size() > 0)
            cibles = (ConstructionPlanetaire[]) listeBoucliers
                    .toArray(new ConstructionPlanetaire[0]);
        else if (listeC.length > 0)
            cibles = listeC;

        for (int i = 0; i < strato.size(); i++) {
            Vaisseau v = (Vaisseau) strato.get(i);
            if (!v.estDetruit())
                if ((cibles != null)
                        && ((construCible) || (listeBoucliers.size() > 0)))
                    v.tirSurConstruction(cibles, h, g, construCible);
                else
                    retour = retour - v.tirSurMilices(h, g, construCible);
        }

        return retour;
    }

    public static boolean combatFlotteFlotte(Commandant c1, Commandant c2, int numFlotte1, int numFlotte2) {

        combatEnCours = "[F" + numFlotte1 + "_" + c1.getNumero() + " VS F" + numFlotte2 + "_" + c2.getNumero() + "] ";

        Combat.logln("RESOLUTION COMBAT " + combatEnCours);

        System.out.print("FF( " + c1.getNomNumerobis() + " F" + numFlotte1 + "," + c2.getNomNumerobis() + " F" + numFlotte2 + " ) - ");
        Univers.notify("Combat entre " + c1.getNomNumerobis() + " et " + c2.getNomNumerobis());
        Univers.phaseSuivante();

        // phase d'initialisation.

        Flotte f1 = c1.getFlotte(numFlotte1);
        Flotte f2 = c2.getFlotte(numFlotte2);

        Heros h1 = c1.getHerosSurFlotte(numFlotte1);
        Heros h2 = c2.getHerosSurFlotte(numFlotte2);
        if (h1 == null)
            h1 = Heros.HEROS_NON_PRESENT;
        if (h2 == null)
            h2 = Heros.HEROS_NON_PRESENT;

        ArrayList marchandises1 = f1.listeCargaisonTransporte();
        ArrayList marchandises2 = f2.listeCargaisonTransporte();

        f1.calculeCombativite(h1);
        f2.calculeCombativite(h2);

        if ((c1.estJoueurHumain()) && (c2.estJoueurHumain()))
            Univers.ajouterRelationRaces(
                    f1.getPosition(),
                    c1.getRace(),
                    c2.getRace(),
                    -2
                            * (Vaisseau.retournerNiveauPuissance(f1
                            .getPuissance()) + Vaisseau
                            .retournerNiveauPuissance(f2.getPuissance())));

        if ((c1.estJoueurHumain())
                && (c2.estJoueurHumain())
                && c1.existenceCapitale()
                && f1.getPosition().getNumeroSecteur() != c1.getCapitale()
                .getNumeroSecteur()) {
            Univers.ajouterRelationRaces(c1.getCapitale(), c1.getRace(),
                    c2.getRace(), Const.RELATION_ATTAQUE_PLANETE);
        }

        boolean gardeDirective1 = (f1.getPuissance() >= f2.getPuissance() * 5);
        boolean gardeDirective2 = (f2.getPuissance() >= f1.getPuissance() * 5);
        boolean vainqueurCombat1 = false;
        boolean vainqueurCombat2 = false;
        int nbVaisseauxDepart1 = f1.getNombreDeVaisseaux();
        int nbVaisseauxDepart2 = f2.getNombreDeVaisseaux();
        int nbDommages1 = 0;
        int nbDommages2 = 0;
        Map.Entry[] memfin1 = (Map.Entry[]) f1.listeVaisseauxParTypePourCombat().entrySet().toArray(new Map.Entry[0]);
        Map.Entry[] memfin2 = (Map.Entry[]) f2.listeVaisseauxParTypePourCombat().entrySet().toArray(new Map.Entry[0]);
        Map mem1 = null;
        Map mem2 = null;

        // phase de positionnement et d'initialisation des cibles

        StrategieDeCombatSpatial s1 = c1.getStrategie(f1.getStrategie());
        Map.Entry[] m1 = f1.listeCompleteVaisseaux();
        StrategieDeCombatSpatial s2 = c2.getStrategie(f2.getStrategie());
        Map.Entry[] m2 = f2.listeCompleteVaisseaux();

        HashMap hp1 = positionnement(s1, m1, true, h1);
        HashMap hp2 = positionnement(s2, m2, false, h2);

        HashMap hc1 = initialisationCibles(m1);
        HashMap hc2 = initialisationCibles(m2);

        boolean finDeTour = false;
        int numTour = 0;
        while (!finDeTour) {
            System.out.print("-t" + numTour);
            Combat.logln("TOUR DE COMBAT " + (numTour + 1));

            mem1 = f1.listeVaisseauxParTypePourCombat();
            mem2 = f2.listeVaisseauxParTypePourCombat();

            // phase de détermination de cible.
            int tailleMax = tailleVaisseauMaximale(m1, m2);

            f1.preparerAuCombat(true);
            f2.preparerAuCombat(true);


            TreeMap ht1 = determinationTempo(m1, h1);
            TreeMap ht2 = determinationTempo(m2, h2);

            determinationCible(m1, s1, hc1, hp1, hp2, f2, 0);
            determinationCible(m2, s2, hc2, hp2, hp1, f1, 0);

            // phase de mouvement.

            mouvement(f1, s1, hp1, hc1, ht1, f2, s2, hp2, hc2, ht2);

            // phase de combat.

            int temp1 = f1.dommagesTotaux();
            int temp2 = f2.dommagesTotaux();

            combat(f1, hp1, hc1, ht1, h1, f2, hp2, hc2, ht2, h2, 0);

            //Const.NB_CIBLES[tailleMax]
            for (int i = 1; i <= 0 /* Const.NB_CIBLES[tailleMax] */; i++) {
                determinationCible(m1, s1, hc1, hp1, hp2, f2, i);
                determinationCible(m2, s2, hc2, hp2, hp1, f1, i);
                combat(f1, hp1, hc1, ht1, h1, f2, hp2, hc2, ht2, h2, i);
            }

            nbDommages1 = nbDommages1 + f1.dommagesTotaux() - temp1;
            nbDommages2 = nbDommages2 + f2.dommagesTotaux() - temp2;

            f1.eliminerPertesVaisseaux();
            f2.eliminerPertesVaisseaux();

            f1.diminuerCombativite();
            f2.diminuerCombativite();
            m1 = f1.listeCompleteVaisseaux();
            m2 = f2.listeCompleteVaisseaux();

            Map inter1 = f1.listeVaisseauxParTypePourCombat();
            Map inter2 = f2.listeVaisseauxParTypePourCombat();
            ecrireDetailCombatFlotteFlotte(c1, c2, f1, f2, mem1, mem2, numTour,
                    memfin1, memfin2, inter1, inter2);
            ecrireDetailCombatFlotteFlotte(c2, c1, f2, f1, mem2, mem1, numTour,
                    memfin2, memfin1, inter2, inter1);
            mem1 = inter1;
            mem2 = inter2;

            if (f1.getNombreDeVaisseaux() == 0)
                vainqueurCombat2 = true;
            if (f2.getNombreDeVaisseaux() == 0)
                vainqueurCombat1 = true;
            if ((!f1.estCombative()) && (!f2.estCombative()))
                finDeTour = true;
            if ((vainqueurCombat1) || (vainqueurCombat2))
                finDeTour = true;

            Combat.logln("FIN DE TOUR: F1  " + nbDommages1 + ", F2 : " + nbDommages2);
            numTour++;
        }

        // FIN DE TOUR
        System.out.println("");

        f1.finaliserCombat();
        f2.finaliserCombat();

        f1.recupererMarchandises(marchandises2, f2);
        f2.recupererMarchandises(marchandises1, f1);

        ajouterEvenements(c1, c2, f1, f2, numFlotte1, numFlotte2,
                vainqueurCombat1, vainqueurCombat2, nbDommages1, nbDommages2,
                nbVaisseauxDepart1 - f1.getNombreDeVaisseaux(),
                nbVaisseauxDepart2 - f2.getNombreDeVaisseaux(), memfin1,
                memfin2, mem1, mem2);
        ajouterEvenements(c2, c1, f2, f1, numFlotte2, numFlotte1,
                vainqueurCombat2, vainqueurCombat1, nbDommages2, nbDommages1,
                nbVaisseauxDepart2 - f2.getNombreDeVaisseaux(),
                nbVaisseauxDepart1 - f1.getNombreDeVaisseaux(), memfin2,
                memfin1, mem2, mem1);

        if (vainqueurCombat1) {
            if (h2 != Heros.HEROS_NON_PRESENT)
                h2.mourir(c2);
            c2.eliminerFlotte(numFlotte2);
        }
        if (vainqueurCombat2) {
            if (h1 != Heros.HEROS_NON_PRESENT)
                h1.mourir(c1);
            c1.eliminerFlotte(numFlotte1);
        }

        if (nbDommages1 + nbDommages2 > 200) {
            Debris debris = new Debris(f1.getPosition(),
                    Const.DEBRIS_RESTE_VAISSEAUX, nbDommages1 + nbDommages2);
            Univers.ajouterDebris(debris);
        }

        if (!gardeDirective2)
            f2.setDirective(Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE);
        if (!gardeDirective1)
            f1.setDirective(Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE);
        if ((!gardeDirective1) || (vainqueurCombat2))
            return false;
        else
            return true;
    }

    private static void ajouterEvenements(Commandant c1, Commandant c2,
                                          Flotte f1, Flotte f2, int numF1, int numF2, boolean vC1,
                                          boolean vC2, int nbD1, int nbD2, int nbP1, int nbP2,
                                          Map.Entry[] d1, Map.Entry[] d2, Map n1, Map n2) {
        c1.ajouterEvenement("COMBAT_FLOTTE_0000", f1.getNomNumero(numF1),
                f2.getNomNumero(numF2), c2.getNomNumero(), f1.getPosition());
        if (vC2)
            c1.ajouterEvenement("COMBAT_FLOTTE_0001");
        else
            c1.ajouterEvenement("COMBAT_FLOTTE_0002", nbD1, nbP1);
        if (vC1)
            c1.ajouterEvenement("COMBAT_FLOTTE_0003");
        else
            c1.ajouterEvenement("COMBAT_FLOTTE_0004", nbD2, nbP2);
        c1.ajouterEvenement("COMBAT_FLOTTE_0007");
        String[] t = (String[]) Univers.getMessageRapport("COMBAT_FLOTTE", c1.getLocale());
        c1.ajouterEvenement("{0}", ecrireDetailCombatVaisseauxFinal(c1, f1, d1, n1, t));
        c1.ajouterEvenement("{0}", ecrireDetailCombatVaisseauxFinal(c2, f2, d2, n2, t));
        c1.ajouterEvenement(
                "{0}",
                Rapport.getALienE(
                        Rapport.DETAIL_COMBAT + "#"
                                + Integer.toString(c1.getNumero()) + "-"
                                + Integer.toString(c2.getNumero()) + "-"
                                + Integer.toString(c1.numeroFlotte(f1)) + "-"
                                + Integer.toString(c2.numeroFlotte(f2))).ajout(
                        Rapport.getTextI(Univers.getMessageInfo(
                                "COMBAT_FLOTTE_0006", c1.getLocale()))));
    }

    private static void ecrireDetailCombatFlotteFlotte(Commandant c1,
                                                       Commandant c2, Flotte f1, Flotte f2, Map m1, Map m2, int tour,
                                                       Map.Entry[] d1, Map.Entry[] d2, Map n1, Map n2) {
        String[] t = (String[]) Univers.getMessageRapport("COMBAT_FLOTTE",
                c1.getLocale());

        c1.ajouterCombat("COMBAT_FLOTTE_0005", c2.getNomNumero(),
                f2.getNomNumero(c2.numeroFlotte(f2)),
                f1.getNomNumero(c1.numeroFlotte(f1)), tour + 1);
        c1.ajouterCombat(Rapport.getABorne(
                        Integer.toString(c1.getNumero()) + "-"
                                + Integer.toString(c2.getNumero()) + "-"
                                + Integer.toString(c1.numeroFlotte(f1)) + "-"
                                + Integer.toString(c2.numeroFlotte(f2)))
                .setTexteContenu("&nbsp;"));
        c1.ajouterCombat(ecrireDetailCombatVaisseaux(c1, f1, m1, d1, n1, t));
        c1.ajouterCombat(ecrireDetailCombatVaisseaux(c2, f2, m2, d2, n2, t));
    }

    private static BaliseHTML ecrireDetailCombatVaisseauxFinal(Commandant c,
                                                               Flotte f, Map.Entry[] d, Map n, String[] t) {
        BaliseHTML[][] a = new BaliseHTML[50][3];
        int ligne = 0;
        MessageFormat message = new MessageFormat(t[1]);
        String[] inter2 = new String[2];
        inter2[0] = f.getNomNumero(c.numeroFlotte(f));
        inter2[1] = c.getNomNumero();
        a[ligne++][0] = Rapport.getTD("center", "3").ajout(
                Rapport.getText(message.format(inter2)));
        for (int i = 0; i < 3; i++)
            a[ligne][i] = Rapport.getTD("center", null).ajout(
                    Rapport.getFont(Rapport.cC[4], null).ajout(
                            Rapport.getText(t[i + 2])));
        ligne++;
        for (int i = 0; i < d.length; i++) {
            a[ligne][0] = Rapport.getTD("center", null).ajout(Rapport.getText((String) d[i].getKey()));

            int[] dT = (int[]) d[i].getValue();
            Object o = null;
            int[] nT = ((o = n.get(d[i].getKey())) == null ? new int[3] : (int[]) o);
            int nbCases = Univers.getPlanDeVaisseau((String) d[i].getKey()).getNombreDeCases();
            if (dT[0] == nT[0]) {
                a[ligne][1] = Rapport.getTD("center", null).ajout(
                        Rapport.getText(Integer.toString(nT[0])));
            } else {
                a[ligne][1] = Rapport
                        .getTD("center", null)
                        .ajout(Rapport.getText(Integer.toString(nT[0])))
                        .ajout(Rapport
                                .getFont(Rapport.cC[6], null)
                                .ajout(Rapport.getText("("
                                        + Integer.toString(nT[0] - dT[0]) + ")")));
            }
            int dom = nT[1] + (-nT[0] + dT[0]) * nbCases;
            int domA = dT[1];
            if (dom == domA) {
                a[ligne][2] = Rapport.getTD("center", null).ajout(
                        Rapport.getText(Integer.toString(dom)));
            } else {
                a[ligne][2] = Rapport
                        .getTD("center", null)
                        .ajout(Rapport.getText(Integer.toString(dom)))
                        .ajout(Rapport.getFont(Rapport.cC[3], null).ajout(
                                Rapport.getText("(+" + Integer.toString(dom - domA) + ")")));
            }
            ligne++;
        }

        return Rapport.getDiv().ajout(
                DocumentHTML.creerTable(Rapport.getTable(), a));
    }

    private static BaliseHTML ecrireDetailCombatVaisseaux(Commandant c,
                                                          Flotte f, Map m, Map.Entry[] d, Map n, String[] t) {
        BaliseHTML[][] a = new BaliseHTML[100][4];
        int ligne = 0;
        MessageFormat message = new MessageFormat(t[1]);
        String[] inter2 = new String[2];
        inter2[0] = f.getNomNumero(c.numeroFlotte(f));
        inter2[1] = c.getNomNumero();
        a[ligne++][0] = Rapport.getTD("center", "4").ajout(
                Rapport.getText(message.format(inter2)));
        for (int i = 0; i < 4; i++)
            a[ligne][i] = Rapport.getTD("center", null).ajout(
                    Rapport.getFont(Rapport.cC[4], null).ajout(
                            Rapport.getText(t[i + 2])));
        ligne++;
        for (int i = 0; i < d.length; i++) {
            a[ligne][0] = Rapport.getTD("center", null).ajout(
                    Rapport.getText((String) d[i].getKey()));

            int[] dT = (int[]) d[i].getValue();
            Object o = null;
            int[] mT = ((o = m.get(d[i].getKey())) == null ? new int[3]
                    : (int[]) o);
            int[] nT = ((o = n.get(d[i].getKey())) == null ? new int[3]
                    : (int[]) o);
            int nbCases = Univers.getPlanDeVaisseau((String) d[i].getKey())
                    .getNombreDeCases();
            if (mT[0] == nT[0])
                a[ligne][1] = Rapport.getTD("center", null).ajout(
                        Rapport.getText(Integer.toString(nT[0])));
            else
                a[ligne][1] = Rapport
                        .getTD("center", null)
                        .ajout(Rapport.getText(Integer.toString(nT[0])))
                        .ajout(Rapport
                                .getFont(Rapport.cC[6], null)
                                .ajout(Rapport.getText("("
                                        + Integer.toString(nT[0] - mT[0]) + ")")));
            int dom = nT[1] + (-nT[0] + dT[0]) * nbCases;
            int domA = mT[1] + (-mT[0] + dT[0]) * nbCases;
            if (dom == domA)
                a[ligne][2] = Rapport.getTD("center", null).ajout(
                        Rapport.getText(Integer.toString(dom)));
            else
                a[ligne][2] = Rapport
                        .getTD("center", null)
                        .ajout(Rapport.getText(Integer.toString(dom)))
                        .ajout(Rapport.getFont(Rapport.cC[3], null).ajout(
                                Rapport.getText("(+"
                                        + Integer.toString(dom - domA) + ")")));
            a[ligne++][3] = Rapport.getTD("center", null).ajout(
                    Rapport.getText(Integer.toString(nT[2])));
        }

        return Rapport.getDiv().ajout(
                DocumentHTML.creerTable(Rapport.getTable(), a));
    }

    private static int tailleVaisseauMaximale(Map.Entry[] m1, Map.Entry[] m2) {
        int max = 0;
        for (int i = 0; i < m1.length; i++)
            if (((Vaisseau) m1[i].getValue()).getTaille() > max)
                max = ((Vaisseau) m1[i].getValue()).getTaille();
        for (int i = 0; i < m2.length; i++)
            if (((Vaisseau) m2[i].getValue()).getTaille() > max)
                max = ((Vaisseau) m2[i].getValue()).getTaille();
        return max;
    }

    private static HashMap positionnement(StrategieDeCombatSpatial s,
                                          Map.Entry[] m, boolean att, Heros h) {
        HashMap hm = new HashMap(m.length * 2, 0.5F);
        for (int i = 0; i < m.length; i++) {
            Object o = s.getPositionnement(((Vaisseau) m[i].getValue())
                    .getType());
            if (o == null) {
                int[] inter = new int[3];
                inter[0] = Univers.getInt(Const.COMBAT_X_MAX);
                if (att)
                    inter[1] = Univers.getInt(Const.COMBAT_Y_MAX);
                else
                    inter[1] = Univers.getInt(Const.COMBAT_Y_MAX)
                            + Const.COMBAT_Y_ESPACE + Const.COMBAT_Y_MAX;
                hm.put(m[i].getKey(),
                        Position3D.auHasard(
                                inter,
                                h.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_SAVOIR)));
            } else {
                int[] inter = (int[]) o;
                if (!att) {
                    inter[1] = Const.COMBAT_Y_ESPACE + Const.COMBAT_Y_MAX
                            - inter[1];
                    inter[0] = Const.COMBAT_X_MAX - inter[0];
                }
                hm.put(m[i].getKey(),
                        Position3D.auHasard(
                                inter,
                                h.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_SAVOIR)));
            }
        }
        return hm;
    }

    private static HashMap initialisationCibles(Map.Entry[] m) {
        HashMap retour = new HashMap(m.length * 2, 0.5F);
        for (int i = 0; i < m.length; i++)
            retour.put(m[i].getKey(),
                    new int[((Vaisseau) m[i].getValue()).getNbCibleMax() + 1]);
        return retour;
    }

    private static void determinationCible(Map.Entry[] m,
                                           StrategieDeCombatSpatial s, HashMap hc, HashMap hp1, HashMap hp2,
                                           Flotte f, int numTir) {
        for (int i = 0; i < m.length; i++) {
            int[] inter = (int[]) hc.get(m[i].getKey());

            Vaisseau vaisseau = ((Vaisseau) m[i].getValue());
            if (inter.length > numTir) {

                // détermination des cibles possibles (par type, puis par taille).

                Integer[] choix1 = f.listeNumerosVaisseauxDeType(s.getTypeCible());
                Integer[] choix2 = null;

                Object o = s.getCibles(((Vaisseau) m[i].getValue()).getType());

                while (choix2 == null)
                    for (int j = 0; j < Const.TAILLE_MAXIMAL_VAISSEAU; j++) {
                        if (Univers.getTest(50))
                            choix2 = cibleNonDejaChoisie(choixPossibleCible(choix1, f, o, j), inter, numTir);
                        if ((choix2 != null) && (choix2.length > 0))
                            break;
                    }

                int compteurTour = 0;
                while ((choix2.length == 0) && (compteurTour < 5)) {
                    choix1 = f.listeNumerosVaisseaux2();
                    for (int j = 0; j < Const.TAILLE_MAXIMAL_VAISSEAU; j++) {
                        if (Univers.getTest(50))
                            choix2 = cibleNonDejaChoisie(choixPossibleCible(choix1, f, o, j), inter, numTir);
                        if ((choix2 != null) && (choix2.length > 0))
                            break;
                    }
                    compteurTour++;
                }

                if (choix2.length == 0)
                    inter[numTir] = -1;
                else {
                    // choix de la cible.
                    Position3D[] posChoix = new Position3D[choix2.length];
                    for (int j = 0; j < choix2.length; j++)
                        posChoix[j] = (Position3D) hp2.get(choix2[j]);

                    Position3D posFinale = Position3D.positionLaPlusProche((Position3D) hp1.get(m[i].getKey()), posChoix);
                    inter[numTir] = choix2[Mdt.position(posChoix, posFinale)];
                }
            }
        }
    }

    private static Integer[] choixPossibleCible(Integer[] depart, Flotte f,
                                                Object cibles, int compteur) {
        int tailleCible = 0;
        if (cibles == null)
            tailleCible = Const.TAILLE_MAXIMAL_VAISSEAU - compteur - 1;
        else
            tailleCible = ((int[]) cibles)[compteur] - 1;
        return f.listeNumerosVaisseauxDeTaille(tailleCible, depart);
    }

    private static Integer[] cibleNonDejaChoisie(Integer[] depart,
                                                 int[] ciblePre, int numTir) {
        HashSet yo = new HashSet(Arrays.asList(depart));
        for (int i = 0; i < numTir; i++)
            yo.remove(new Integer(ciblePre[i]));
        return (Integer[]) yo.toArray(new Integer[0]);
    }

    private static TreeMap determinationTempo(Map.Entry[] m, Heros h) {
        TreeMap hm = new TreeMap();
        int modificateurHeros = h.getVitesseModifie();
        int tempo;
        for (int i = 0; i < m.length; i++) {
            tempo = modificateurHeros * Univers.getInt(100)
                    + ((Vaisseau) m[i].getValue()).getTempo()
                    + Univers.getInt(500);
            hm.put(trouverTempoLibre(hm, tempo), m[i].getKey());
        }
        return hm;
    }

    private static Integer trouverTempoLibre(TreeMap hm, int t) {
        int retour = t;
        while (hm.containsKey(new Integer(retour)))
            retour++;
        return new Integer(retour);
    }

    private static boolean fuiteTactique(Flotte f1, Flotte f2,
                                         StrategieDeCombatSpatial s) {
        if (s.getAgressivite() == Const.STRATEGIE_AGRESSIVITE_FUYARD)
            return true;
        if (s.getAgressivite() == Const.STRATEGIE_AGRESSIVITE_PRUDENT)
            if (f1.getPuissance() < f2.getPuissance() / 2)
                return true;
        if (s.getAgressivite() == Const.STRATEGIE_AGRESSIVITE_NORMAL)
            if (f1.getPuissance() < f2.getPuissance() / 4)
                return true;
        if (s.getAgressivite() == Const.STRATEGIE_AGRESSIVITE_COMBATIF)
            if (f1.getPuissance() < f2.getPuissance() / 8)
                return true;
        if (s.getAgressivite() == Const.STRATEGIE_AGRESSIVITE_PILLAGE) {
            if (s.getTypeCible() == Const.STRATEGIE_CIBLE_CARGO)
                if (f2.nbVaisseauxCargos() == 0)
                    return true;
            if (s.getTypeCible() == Const.STRATEGIE_CIBLE_BOMBARDIER)
                if (f2.nbVaisseauxBombardiers() == 0)
                    return true;
            if (s.getTypeCible() == Const.STRATEGIE_CIBLE_CHASSEUR)
                if (f2.nbVaisseauxChasseurs() == 0)
                    return true;
        }
        return false;
    }

    private static void mouvement(Flotte f1, StrategieDeCombatSpatial s1,
                                  HashMap pos1, HashMap cible1, TreeMap ht1, Flotte f2,
                                  StrategieDeCombatSpatial s2, HashMap pos2, HashMap cible2,
                                  TreeMap ht2) {

        boolean fuiteTactique1 = fuiteTactique(f1, f2, s1);
        boolean fuiteTactique2 = fuiteTactique(f2, f1, s2);

        Map.Entry[] mt1 = (Map.Entry[]) ht1.entrySet()
                .toArray(new Map.Entry[0]);
        Map.Entry[] mt2 = (Map.Entry[]) ht2.entrySet()
                .toArray(new Map.Entry[0]);

        int compteur1 = 0;
        int compteur2 = 0;

        boolean premier;
        while ((compteur1 < mt1.length) || (compteur2 < mt2.length)) {
            premier = false;
            if (compteur2 == mt2.length)
                premier = true;
            else if (compteur1 != mt1.length)
                if (((Integer) mt1[compteur1].getKey())
                        .compareTo((Integer) mt2[compteur2].getKey()) < 0)
                    premier = true;

            if (premier) {
                mouvementVaisseau(mt1[compteur1].getValue(), f1, pos1, pos2,
                        cible1, fuiteTactique1, true);
                compteur1++;
            } else {
                mouvementVaisseau(mt2[compteur2].getValue(), f2, pos2, pos1,
                        cible2, fuiteTactique2, false);
                compteur2++;
            }
        }
    }

    private static void mouvementVaisseau(Object cle, Flotte f, HashMap pos1,
                                          HashMap pos2, HashMap cible, boolean fuite, boolean att) {
        Vaisseau v = f.getVaisseau((Integer) cle);
        Position3D inter = (Position3D) pos1.get(cle);

        if ((fuite) || (!v.estCombatif()) || (v.getNombreArmesValides() == 0))
            pos1.put(
                    cle,
                    Position3D.positionAtteinte(inter,
                            Position3D.positionDeFuite(att),
                            Math.max(0, v.getCapaciteMouvement(false) - 2)));
        else {
            int ci = ((int[]) cible.get(cle))[0];
            pos1.put(
                    cle,
                    Position3D.positionAtteinte(inter,
                            (Position3D) pos2.get(new Integer(ci)),
                            v.getCapaciteMouvement(false)));
        }
    }

    private static void combat(Flotte f1, HashMap pos1, HashMap cible1,
                               TreeMap ht1, Heros h1, Flotte f2, HashMap pos2, HashMap cible2,
                               TreeMap ht2, Heros h2, int numTir) {

        Map.Entry[] mt1 = (Map.Entry[]) ht1.entrySet()
                .toArray(new Map.Entry[0]);
        Map.Entry[] mt2 = (Map.Entry[]) ht2.entrySet()
                .toArray(new Map.Entry[0]);

        int compteur1 = mt1.length - 1;
        int compteur2 = mt2.length - 1;

        boolean premier;
        while ((compteur1 > -1) || (compteur2 > -1)) {
            premier = false;
            if (compteur2 == -1) {
                premier = true;
            } else if (compteur1 != -1) {
                if (((Integer) mt1[compteur1].getKey())
                        .compareTo((Integer) mt2[compteur2].getKey()) > 0)
                    premier = true;
            }

            if (premier) {
                tirVaisseau(mt1[compteur1].getValue(), f1, pos1, pos2, cible1, f2, numTir, h1, h2);
                compteur1--;
            } else {
                tirVaisseau(mt2[compteur2].getValue(), f2, pos2, pos1, cible2, f1, numTir, h2, h1);
                compteur2--;
            }
        }
    }

    private static void tirVaisseau(Object cle, Flotte f1, HashMap pos1, HashMap pos2, HashMap cible, Flotte f2, int numTir, Heros h1, Heros h2) {
        Vaisseau v = f1.getVaisseau((Integer) cle);
        int nbCibleMax = v.getNbCibleMax();
        boolean estCombatif = v.estCombatif();
        boolean estVivant = !v.estDetruit();
        Position3D att = (Position3D) pos1.get(cle);
        Commandant com = Univers.getCommandantFromFlotte(f1);
        boolean vaisseau_a_tire = false;
        Combat.log(combatEnCours + " C{0} , tir vaisseau N°{1}/{2} ({3}, race: {4}) , attP: {5} ",
                com.getNumero(), numTir, nbCibleMax, v.getPlan().getNom(), v.getRaceEquipage(), att);
        if ((numTir <= nbCibleMax) && (estCombatif) && (estVivant)) {
            Integer cle2 = new Integer(((int[]) cible.get(cle))[numTir]);
            if (cle2.intValue() != -1) {
                Vaisseau c = f2.getVaisseau(cle2);
                if (!c.estDetruit()) {
                    Position3D def = (Position3D) pos2.get(cle2);
                    int distance = Position3D.distance(att, def);
                    Combat.logln(", cible: {2}, deffP: {0}, distance: {1}", def, distance, c.getPlan().getNom());
                    vaisseau_a_tire = true;
                    v.tir(c, distance, h1, h2);
                }
            } else {
                Combat.log("pas de cible");
            }
        }
        if (!vaisseau_a_tire) {
            Combat.logln("");
        }
    }

}
