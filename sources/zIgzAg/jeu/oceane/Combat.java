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
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.text.MessageFormat;

import zIgzAg.utile.Mdt;
import zIgzAg.html.BaliseHTML;
import zIgzAg.html.DocumentHTML;
import zIgzAg.utile.SherilLogger;

public class Combat {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH_mm_ss");
    private static BufferedWriter writer;
    private static String combatEnCours = "";

    /**
     * Total des points de dommages (tous camps confondus : vaisseaux,
     * bâtiments et milices) infligés lors du dernier combat résolu par
     * combatFlotteFlotte ou combatFlottePlanete.
     * Sert à qualifier l'intensité du combat dans les statistiques publiques.
     */
    private static int degatsDernierCombat = 0;

    public static void logln(String message, Object... parameters) {
        log(message, true, parameters);
    }

    public static void log(String message, Object... parameters) {
        log(message, false, parameters);
    }

    // Détail du journal (une ligne par vaisseau et par tir d'arme), coupé si
    // COMBAT_LOG_DETAIL = false dans config.properties.

    public static void logDetailln(String message, Object... parameters) {
        if (Const.COMBAT_LOG_DETAIL)
            log(message, true, parameters);
    }

    public static void logDetail(String message, Object... parameters) {
        if (Const.COMBAT_LOG_DETAIL)
            log(message, false, parameters);
    }

    private static void log(String message, boolean newLine, Object... parameters) {
        if (!ReglesCombat.actives().journal)
            return;
        try {
            if(writer == null) {
                Path dir = Paths.get(Chemin.RACINE, "tour" + (Univers.getTour()+1), "combats");
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

        RapportCombat rapport = null;
        if ((dC.getNumero() != 0) && (aC.getNumero() != 0)) {
            Univers.ajouterTransfert(aC, dC, "C*attaque flotte");
            rapport = new RapportCombat(aF.getPosition(), aC, dC,
                    RapportCombat.TYPE_COMBAT_SPATIAL);
            rapport.setCombatSpatial(aF, dC.getFlotte(d[1]));
            Univers.ajouterRapportCombat(rapport);
        }

        boolean retour = combatFlotteFlotte(aC, dC, a[1], d[1]);
        if (rapport != null)
            rapport.setDegats(degatsDernierCombat);
        return retour;
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
        Map<Integer, Integer> degatsParDefenseur = new HashMap<>();
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
                inter = combatFlottePlanete(c, v[1], d, s, ordre[i],
                        nbTour, dommages);
                degatsParDefenseur.merge(d.getNumero(), degatsDernierCombat,
                        Integer::sum);
                if (inter == -1) {
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
                        f.getNomNumeroHTML(c.numeroFlotte(f)), s.getPosition(),
                        nbPrise);
            else
                c.ajouterEvenement("COMBAT_SYSTEME_0006",
                        f.getNomNumeroHTML(c.numeroFlotte(f)), s.getPosition(),
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
                        f.getNomNumeroHTML(c.numeroFlotte(f)), s.getPosition(),
                        c.getNomNumeroHtml(), nbPrisesd[i][1]);
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
                    rapport.setDegats(degatsParDefenseur.getOrDefault(
                            c2.getNumero(), 0));
                    Univers.ajouterRapportCombat(rapport);
                }
            }
        } else
            c.ajouterEvenement("COMBAT_SYSTEME_0008",
                    f.getNomNumeroHTML(c.numeroFlotte(f)));

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
                rapport.setDegats(degatsDernierCombat);
                Univers.ajouterRapportCombat(rapport);
            }
        } else
            c.ajouterEvenement("COMBAT_SYSTEME_0014",
                    f.getNomNumeroHTML(c.numeroFlotte(f)));
        f.setDirective(Const.DIRECTIVE_FLOTTE_ATTITUDE_NEUTRE);
    }

    public static int combatFlottePlanete(Commandant c1, int numFlotte,
                                          Commandant c2, Systeme s, int numPla, int tour, int[] dommagesFlotte) {

        Flotte f = c1.getFlotte(numFlotte);
        degatsDernierCombat = 0;

        if (f.getPuissance() < Const.PUISSANCE_ATTAQUE_PLANETAIRE_MINIMALE) {
            c1.ajouterEvenement("COMBAT_SYSTEME_0015",
                    f.getNomNumeroHTML(numFlotte),
                    f.getPuissance(),
                    Const.PUISSANCE_ATTAQUE_PLANETAIRE_MINIMALE
                    );
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

        int nbPopDefensive = (p.populationTotale() * p.getStabilite()) / 100;
//        // Plus besoin de ça.
//        if (c2.estJoueurNeutre()) {
//            // si le joueur est Neutre, on le nerf les 10 premiers tours de l'arrivée d'un joueur pour faciliter les prises de planetes
//            nbPopDefensive = (int) (nbPopDefensive * Math.min(1.0,  0.1 * (1+Univers.getTour() - c1.getTourArrivee())));
//        }

        if (c2.getPossession(s.getPosition()).getPolitique() == Const.POLITIQUE_DEFENSE)
            nbPopDefensive = Math.min(p.populationTotale(), nbPopDefensive
                    + nbPopDefensive / 2);
        if (c2.getPossession(s.getPosition()).possedeStockImportantPoste(
                Const.PRODUIT_ARMEMENT))
            nbPopDefensive = Math.min(p.populationTotale(),
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

            int dommagesFlotteAvant = f.dommagesTotaux();
            int dommagesBatimentsAvant = dommagesBatiments(p);

            tirDefensesPlanetaires(listeC, strato, sol, g, h, true,c2);
            nbPopDefensive = tirAirSol(c1, strato, listeC, nbPopDefensive, true, g,
                    h);
            tirMilicesPlanetaires(nbPopDefensive, sol, g, h,c2);

            degatsDernierCombat += dommagesBatiments(p) - dommagesBatimentsAvant;
            p.eliminerPertesBatiments();
            listeC = p.getBatiments();
            dommagesBatimentsAvant = dommagesBatiments(p);

            nbPopDefensive = tirAirSol(c1, sol, listeC, nbPopDefensive, false, g, h);

            degatsDernierCombat += dommagesBatiments(p) - dommagesBatimentsAvant;
            degatsDernierCombat += f.dommagesTotaux() - dommagesFlotteAvant;
            // chaque milicien perdu compte comme un point de dommage
            degatsDernierCombat += memoirePopTour - Math.max(0, nbPopDefensive);
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
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumeroHtml());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0001",
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumeroHtml());
                }
                if (f.getDirective() == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE) {
                    c1.ajouterEvenement("COMBAT_SYSTEME_0010",
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumeroHtml());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0011",
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumeroHtml());
                }
                if (f.getDirective() == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE) {
                    c1.ajouterEvenement("COMBAT_SYSTEME_0012",
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c2.getNomNumeroHtml());
                    c2.ajouterEvenement("COMBAT_SYSTEME_0013",
                            f.getNomNumeroHTML(numFlotte),
                            s.getNomNumeroPlanete(numPla), s.getPosition(),
                            c1.getNomNumeroHtml());
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
                    f.getNomNumeroHTML(numFlotte), s.getNomNumeroPlanete(numPla),
                    s.getPosition(), c2.getNomNumeroHtml());
            c2.ajouterEvenement("COMBAT_SYSTEME_0003",
                    f.getNomNumeroHTML(numFlotte), s.getNomNumeroPlanete(numPla),
                    s.getPosition(), c1.getNomNumeroHtml());
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

    private static int dommagesBatiments(Planete p) {
        int retour = 0;
        ConstructionPlanetaire[] c = p.getBatiments();
        for (int i = 0; i < c.length; i++)
            retour += c[i].getDommages();
        return retour;
    }

    private static void ecrireDetailCombatFlottePlanete(Commandant c1,
                                                        Commandant c2, Flotte f, Systeme s, int numPla, int tour, Map mf,
                                                        Map mm, Map.Entry[] df, Map.Entry[] dm, Map nf, Map nm,
                                                        boolean attaquant, int popm, int popn) {

        // On instancie et on distribue le rapport de données UNIQUEMENT lors du passage de l'attaquant
        if (attaquant) {
            RapportCombatData data = RapportCombatData.fromCombatPlanete(
                    c1, c2, f, s, numPla, tour, mf, mm, df, dm, nf, nm, true, popm, popn
            );
            c1.addRapportCombatData(data);
            c2.addRapportCombatData(data);
        }

        String[] t = (String[]) Univers.getMessageRapport("COMBAT_FLOTTE", c1.getLocale());
        String[] t2 = (String[]) Univers.getMessageRapport("COMBAT_PLANETE", c1.getLocale());

        if (attaquant) {
            c1.ajouterCombat("COMBAT_SYSTEME_0005", c2.getNomNumeroHtml(),
                    f.getNomNumeroHTML(c1.numeroFlotte(f)),
                    s.getNomNumeroPlanete(numPla), s.getPosition(), tour + 1);
        } else {
            c1.ajouterCombat("COMBAT_SYSTEME_0004", c2.getNomNumeroHtml(),
                    f.getNomNumeroHTML(c2.numeroFlotte(f)),
                    s.getNomNumeroPlanete(numPla), s.getPosition(), tour + 1);
        }

        c1.ajouterCombat(Rapport.getABorne(c1.getNumero() + "-" + s.getPosition().toString()));
        if (attaquant) {
            c1.ajouterCombat(ecrireDetailCombatVaisseaux(c1, f, mf, df, nf, t));
            c1.ajouterCombat(ecrireDetailCombatPlanete(c2, s, numPla, mm, dm, nm, popm, popn, t2));
        } else {
            c1.ajouterCombat(ecrireDetailCombatPlanete(c1, s, numPla, mm, dm, nm, popm, popn, t2));
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
        inter2[1] = c.getNomNumeroHtml();
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
                DocumentHTML.creerTable(Rapport.getTable("unsearchable"), a));
    }

    private static void tirDefensesPlanetaires(ConstructionPlanetaire[] listeC,
                                               ArrayList strato, ArrayList sol, Gouverneur g, Heros h,
                                               boolean boutPortant, Commandant defenseur) { // On utilise defenseur passé en paramètre
        ArrayList inter = null;

        
        if (strato.size() > 0)
            inter = strato;
        else
            inter = sol;
        for (int i = 0; i < listeC.length; i++){
            for (int j = 0; j < Const.NOMBRE_SALVE_BATTERIE; j++) {
                Vaisseau cible = (Vaisseau) inter.get(Univers.getInt(inter.size()));
                // --- CAPTURE DES DEGATS ---
                int dommagesAvant = listeC[i].getDommagesEffectues();
                listeC[i].tir(cible, g, h, boutPortant);
                int degatsDuTir = listeC[i].getDommagesEffectues() - dommagesAvant;

                if (degatsDuTir > 0 && defenseur != null) {
                defenseur.ajouterDegats((float)degatsDuTir);
                }

                //LOG
              SherilLogger.log(String.format(
                    "[DEB-2.4] BATTERIES -> FLOTTE |  Cible: %s | Dégâts: %d | Total Défenseur %s: %.2f",
                    cible.getPlan().getNom(),
                    degatsDuTir,
                      defenseur != null ? defenseur.getNomNumeroText() : "",
                      defenseur != null ? defenseur.getDegatsInfligesCeTour() : 0
                ));
            }
        }
    }

    private static void tirMilicesPlanetaires(int nbPopDefensives,
                                              ArrayList sol, Gouverneur g, Heros h, Commandant defenseur) { // On utilise defenseur passé en paramètre
        ConstructionPlanetaire[] c = new ConstructionPlanetaire[1];
        c[0] = new ConstructionPlanetaire("battlaI");
        int nbTirs = 0;
        if (nbPopDefensives > 50)
            nbTirs = 1 + (nbPopDefensives / (2 * Const.NOMBRE_SALVE_BATTERIE));
        if (!sol.isEmpty()) {
            SherilLogger.log("[DEB-2.3] DEBUT TIR MILICE | Population: " + nbPopDefensives + " | Nombre de salves prévues: " + nbTirs);
            for (int i = 0; i < nbTirs; i++)
                tirDefensesPlanetaires(c, sol, sol, g, h, false, defenseur);
        }
               
    }

    private static int tirAirSol(Commandant c1, ArrayList strato,
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
            if (!v.estDetruit()) {
                int dommagesAvant = v.getDommagesEffectues();
                int popAvantCeVaisseau = retour;
                int morts = 0;

                if ((cibles != null) && ((construCible) || (listeBoucliers.size() > 0))) {
                    v.tirSurConstruction(cibles, h, g, construCible);
                } else {
                    morts = v.tirSurMilices(h, g, construCible, retour);
                    retour = retour - morts;
                }

                int impactPop = popAvantCeVaisseau - retour;
                int degatsDuTir = v.getDommagesEffectues() - dommagesAvant;
                if (degatsDuTir > 0) {
                    c1.ajouterDegats((float) degatsDuTir);
                }

                SherilLogger.log(String.format(
                        "[DEB-2.1/2.2] AIR-SOL | Vaisseau: %s | Dégâts Structure: %d | Morts Milice: %d | Total Commandant %s: %.2f",
                        v.getPlan().getNom(),
                        degatsDuTir,
                        impactPop,
                        c1.getNomNumeroText(),
                        c1.getDegatsInfligesCeTour()
                ));
            }
        }

        return retour;
    }

    public static boolean combatFlotteFlotte(Commandant c1, Commandant c2, int numFlotte1, int numFlotte2) {

        combatEnCours = "[F" + numFlotte1 + "_" + c1.getNumero() + " VS F" + numFlotte2 + "_" + c2.getNumero() + "] ";

        Combat.logln("RESOLUTION COMBAT " + combatEnCours);

        System.out.print("FF( " + c1.getNomNumeroText() + " F" + numFlotte1 + "," + c2.getNomNumeroText() + " F" + numFlotte2 + " ) - ");
        if(!c1.estJoueurNeutre() && !c2.estJoueurNeutre()) {
            Univers.notify("Combat entre " + c1.getNomNumeroText() + " et " + c2.getNomNumeroText());
        }
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
        int nbVaisseauxDepart1 = f1.getNombreDeVaisseaux();
        int nbVaisseauxDepart2 = f2.getNombreDeVaisseaux();
        Map.Entry[] memfin1 = (Map.Entry[]) f1.listeVaisseauxParTypePourCombat().entrySet().toArray(new Map.Entry[0]);
        Map.Entry[] memfin2 = (Map.Entry[]) f2.listeVaisseauxParTypePourCombat().entrySet().toArray(new Map.Entry[0]);
        StrategieDeCombatSpatial s1 = c1.getStrategie(f1.getStrategie());
        StrategieDeCombatSpatial s2 = c2.getStrategie(f2.getStrategie());

        ResultatCombatSpatial resultat = resoudreCombatSpatial(c1, c2, f1, f2, s1, s2, h1, h2, memfin1, memfin2);
        boolean vainqueurCombat1 = resultat.vainqueur1;
        boolean vainqueurCombat2 = resultat.vainqueur2;
        int nbDommages1 = resultat.dommages1;
        int nbDommages2 = resultat.dommages2;
        Map mem1 = resultat.mem1;
        Map mem2 = resultat.mem2;

        // FIN DE TOUR
        System.out.println("");

        degatsDernierCombat = nbDommages1 + nbDommages2;

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

    /**
     * Cœur du combat spatial : positionnement puis boucle des rounds, sans aucun effet
     * sur les commandants ni sur l'univers (rapports, événements, débris, directives).
     * c1, c2, memfin1 et memfin2 peuvent être nuls (banc d'essai) : le détail du combat
     * n'est alors pas écrit et les dégâts ne sont pas crédités aux commandants.
     */
    public static ResultatCombatSpatial resoudreCombatSpatial(Commandant c1, Commandant c2, Flotte f1, Flotte f2,
                                                              StrategieDeCombatSpatial s1, StrategieDeCombatSpatial s2,
                                                              Heros h1, Heros h2,
                                                              Map.Entry[] memfin1, Map.Entry[] memfin2) {
        ReglesCombat regles = ReglesCombat.actives();

        f1.calculeCombativite(h1);
        f2.calculeCombativite(h2);

        boolean vainqueurCombat1 = false;
        boolean vainqueurCombat2 = false;
        int nbDommages1 = 0;
        int nbDommages2 = 0;
        Map mem1 = null;
        Map mem2 = null;

        // phase de positionnement et d'initialisation des cibles

        Map.Entry[] m1 = f1.listeCompleteVaisseaux();
        Map.Entry[] m2 = f2.listeCompleteVaisseaux();

        HashMap hp1 = positionnement(s1, m1, true, h1);
        HashMap hp2 = positionnement(s2, m2, false, h2);

        HashMap hc1 = initialisationCibles(m1);
        HashMap hc2 = initialisationCibles(m2);

        boolean finDeTour = false;
        int numTour = 0;
        while (!finDeTour) {
            if (regles.journal)
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

            if (regles.tirContinu)
                combatContinu(f1, s1, hp1, hc1, ht1, h1, c1, f2, s2, hp2, hc2, ht2, h2, c2);
            else
                combat(f1, hp1, hc1, ht1, h1, c1, f2, hp2, hc2, ht2, h2, c2, 0);

            // Tirs sur cibles multiples (regles.multiCible = false : ancien tir sur cible unique).
            // Seuls les vaisseaux qui ont encore des armes à tirer désignent une nouvelle cible,
            // et on s'arrête dès qu'aucun d'eux ne peut plus en trouver : les itérations
            // suivantes ne produiraient aucun tir.
            int nbTirsSupplementaires = (regles.multiCible && !regles.tirContinu) ? Const.NB_CIBLES[tailleMax] : 0;
            for (int i = 1; i <= nbTirsSupplementaires
                    && (resteCibleNouvelle(m1, hc1, f2, i) || resteCibleNouvelle(m2, hc2, f1, i)); i++) {
                determinationCible(m1, s1, hc1, hp1, hp2, f2, i);
                determinationCible(m2, s2, hc2, hp2, hp1, f1, i);
                combat(f1, hp1, hc1, ht1, h1, c1, f2, hp2, hc2, ht2, h2, c2, i);
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
            if ((c1 != null) && (c2 != null)) {
                ecrireDetailCombatFlotteFlotte(c1, c2, f1, f2, mem1, mem2, numTour,
                        memfin1, memfin2, inter1, inter2);
                ecrireDetailCombatFlotteFlotte(c2, c1, f2, f1, mem2, mem1, numTour,
                        memfin2, memfin1, inter2, inter1);
            }
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

        return new ResultatCombatSpatial(vainqueurCombat1, vainqueurCombat2, nbDommages1, nbDommages2, numTour, mem1, mem2);
    }

    private static void ajouterEvenements(Commandant c1, Commandant c2,
                                          Flotte f1, Flotte f2, int numF1, int numF2, boolean vC1,
                                          boolean vC2, int nbD1, int nbD2, int nbP1, int nbP2,
                                          Map.Entry[] d1, Map.Entry[] d2, Map n1, Map n2) {
        c1.ajouterEvenement("COMBAT_FLOTTE_0000",
                f1.getNomNumeroHTML(numF1),
                f2.getNomNumeroHTML(numF2),
                c2.getNomNumeroHtml(),
                f1.getPosition());
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

        // On instancie le rapport une seule fois lors du premier passage
        if (c1.getNumero() < c2.getNumero()) {
            RapportCombatData data = RapportCombatData.fromCombatFlotte(c1, c2, f1, f2, m1, m2, tour, d1, d2, n1, n2);
            c1.addRapportCombatData(data);
            c2.addRapportCombatData(data);
        }

        String[] t = (String[]) Univers.getMessageRapport("COMBAT_FLOTTE",
                c1.getLocale());

        c1.ajouterCombat("COMBAT_FLOTTE_0005", c2.getNomNumeroHtml(),
                f2.getNomNumeroHTML(c2.numeroFlotte(f2)),
                f1.getNomNumeroHTML(c1.numeroFlotte(f1)), tour + 1);
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
        inter2[0] = f.getNomNumeroHTML(c.numeroFlotte(f));
        inter2[1] = c.getNomNumeroHtml();
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
        inter2[0] = f.getNomNumeroHTML(c.numeroFlotte(f));
        inter2[1] = c.getNomNumeroHtml();
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
                // copie : la stratégie du joueur ne doit pas être modifiée
                // (sinon un défenseur sur deux du même type reprend la position
                // non retournée, et la stratégie enregistrée est faussée)
                int[] inter = ((int[]) o).clone();
                if (!att) {
                    // miroir dans la zone du défenseur (y 20 à 30) : arrière 0 -> 30
                    inter[1] = 2 * Const.COMBAT_Y_MAX + Const.COMBAT_Y_ESPACE
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

    /**
     * Vrai si le vaisseau peut encore tirer pendant ce round : en vie, combatif, et avec au
     * moins une arme qui n'a pas encore servi.
     */
    private static boolean peutEncoreTirer(Vaisseau v) {
        return (!v.estDetruit()) && v.estCombatif() && (v.getNombreArmesValides() > 0);
    }

    /**
     * Vrai si au moins un vaisseau de m, encore capable de tirer, peut désigner au tir numTir
     * un vaisseau de la flotte f qu'il n'a pas déjà pris pour cible pendant ce round.
     */
    private static boolean resteCibleNouvelle(Map.Entry[] m, HashMap hc, Flotte f, int numTir) {
        int nbAdversaires = f.getNombreDeVaisseaux();
        for (int i = 0; i < m.length; i++) {
            int[] inter = (int[]) hc.get(m[i].getKey());
            if ((inter.length > numTir) && peutEncoreTirer((Vaisseau) m[i].getValue())) {
                int dejaPrises = 0;
                for (int j = 0; j < numTir; j++)
                    if (inter[j] != -1)
                        dejaPrises++;
                if (dejaPrises < nbAdversaires)
                    return true;
            }
        }
        return false;
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
        // La flotte adverse ne change pas pendant la désignation : ses vaisseaux sont répartis
        // par taille une seule fois, au lieu d'être refiltrés pour chaque tireur.
        Integer[][] parTailleType = null;
        Integer[][] parTailleTous = null;
        // Pour chaque liste de cibles non filtrée : ses positions, et la cible déjà trouvée pour
        // chaque position de tireur (deux tireurs au même endroit ont la même cible la plus proche).
        IdentityHashMap positionsParListe = new IdentityHashMap();
        IdentityHashMap ciblesParListe = new IdentityHashMap();
        for (int i = 0; i < m.length; i++) {
            int[] inter = (int[]) hc.get(m[i].getKey());

            Vaisseau vaisseau = ((Vaisseau) m[i].getValue());
            if ((numTir > 0) && (inter.length > numTir) && (!peutEncoreTirer(vaisseau))) {
                // cible supplémentaire inutile : ce vaisseau ne tirera plus pendant ce round
                inter[numTir] = -1;
            } else if (inter.length > numTir) {

                // détermination des cibles possibles (par type, puis par taille).

                if (parTailleType == null)
                    parTailleType = repartirParTaille(f, f.listeNumerosVaisseauxDeType(s.getTypeCible()));
                HashSet dejaPrises = ciblesDejaPrises(inter, numTir);
                Integer[] choix2 = null;

                Object o = s.getCibles(((Vaisseau) m[i].getValue()).getType());

                while (choix2 == null)
                    for (int j = 0; j < Const.TAILLE_MAXIMAL_VAISSEAU; j++) {
                        if (Univers.getTest(50))
                            choix2 = cibleNonDejaChoisie(choixPossibleCible(parTailleType, o, j), dejaPrises);
                        if ((choix2 != null) && (choix2.length > 0))
                            break;
                    }

                int compteurTour = 0;
                while ((choix2.length == 0) && (compteurTour < 5)) {
                    if (parTailleTous == null)
                        parTailleTous = repartirParTaille(f, f.listeNumerosVaisseaux2());
                    for (int j = 0; j < Const.TAILLE_MAXIMAL_VAISSEAU; j++) {
                        if (Univers.getTest(50))
                            choix2 = cibleNonDejaChoisie(choixPossibleCible(parTailleTous, o, j), dejaPrises);
                        if ((choix2 != null) && (choix2.length > 0))
                            break;
                    }
                    compteurTour++;
                }

                if (choix2.length == 0)
                    inter[numTir] = -1;
                else {
                    // choix de la cible.
                    Position3D posTireur = (Position3D) hp1.get(m[i].getKey());
                    if (dejaPrises != null)
                        inter[numTir] = choix2[indexPlusProche(posTireur, positionsDe(choix2, hp2))];
                    else {
                        Position3D[] posChoix = (Position3D[]) positionsParListe.get(choix2);
                        HashMap dejaTrouvees = (HashMap) ciblesParListe.get(choix2);
                        if (posChoix == null) {
                            posChoix = positionsDe(choix2, hp2);
                            dejaTrouvees = new HashMap();
                            positionsParListe.put(choix2, posChoix);
                            ciblesParListe.put(choix2, dejaTrouvees);
                        }
                        String clePosition = posTireur.getPos()[0] + "," + posTireur.getPos()[1] + "," + posTireur.getPos()[2];
                        Integer trouvee = (Integer) dejaTrouvees.get(clePosition);
                        if (trouvee == null) {
                            trouvee = choix2[indexPlusProche(posTireur, posChoix)];
                            dejaTrouvees.put(clePosition, trouvee);
                        }
                        inter[numTir] = trouvee.intValue();
                    }
                }
            }
        }
    }

    private static final Integer[] AUCUN_VAISSEAU = new Integer[0];

    private static Position3D[] positionsDe(Integer[] numeros, HashMap positions) {
        Position3D[] retour = new Position3D[numeros.length];
        for (int i = 0; i < numeros.length; i++)
            retour[i] = (Position3D) positions.get(numeros[i]);
        return retour;
    }

    /** Index de la position la plus proche de depart ; la première de la liste en cas d'égalité. */
    private static int indexPlusProche(Position3D depart, Position3D[] arrivees) {
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < arrivees.length; i++) {
            int d = Position3D.distance(depart, arrivees[i]);
            if (d < min) {
                min = d;
                index = i;
            }
        }
        return index;
    }

    /**
     * Répartit des numéros de vaisseaux de la flotte f par taille. Chaque liste est rangée dans
     * l'ordre de parcours d'un HashSet, comme le faisait la sélection d'origine : cet ordre
     * départage les cibles situées à la même distance.
     */
    private static Integer[][] repartirParTaille(Flotte f, Integer[] depart) {
        ArrayList[] listes = new ArrayList[Const.TAILLE_MAXIMAL_VAISSEAU];
        for (int i = 0; i < listes.length; i++)
            listes[i] = new ArrayList();
        boolean vivantes = ReglesCombat.actives().ciblesVivantes;
        for (int i = 0; i < depart.length; i++) {
            Vaisseau v = f.getVaisseau(depart[i]);
            if (!(vivantes && v.estDetruit()))
                listes[v.getTaille()].add(depart[i]);
        }
        Integer[][] retour = new Integer[listes.length][];
        for (int i = 0; i < listes.length; i++)
            retour[i] = (Integer[]) new HashSet(listes[i]).toArray(new Integer[0]);
        return retour;
    }

    private static Integer[] choixPossibleCible(Integer[][] parTaille,
                                                Object cibles, int compteur) {
        int tailleCible = 0;
        if (cibles == null)
            tailleCible = Const.TAILLE_MAXIMAL_VAISSEAU - compteur - 1;
        else
            tailleCible = ((int[]) cibles)[compteur] - 1;
        if ((tailleCible < 0) || (tailleCible >= parTaille.length))
            return AUCUN_VAISSEAU;
        return parTaille[tailleCible];
    }

    /** Cibles déjà désignées par un vaisseau pendant ce round, ou null s'il n'y en a pas. */
    private static HashSet ciblesDejaPrises(int[] ciblePre, int numTir) {
        if (numTir == 0)
            return null;
        HashSet prises = new HashSet(numTir * 2);
        for (int i = 0; i < numTir; i++)
            prises.add(Integer.valueOf(ciblePre[i]));
        return prises;
    }

    private static Integer[] cibleNonDejaChoisie(Integer[] depart, HashSet dejaPrises) {
        if ((dejaPrises == null) || (depart.length == 0))
            return depart;
        ArrayList restantes = new ArrayList(depart.length);
        for (int i = 0; i < depart.length; i++)
            if (!dejaPrises.contains(depart[i]))
                restantes.add(depart[i]);
        return (Integer[]) restantes.toArray(new Integer[0]);
    }

    private static TreeMap determinationTempo(Map.Entry[] m, Heros h) {
        TreeMap hm = new TreeMap();
        HashMap suivant = new HashMap(m.length * 2);
        int modificateurHeros = h.getVitesseModifie();
        int tempo;
        for (int i = 0; i < m.length; i++) {
            tempo = modificateurHeros * Univers.getInt(100)
                    + ((Vaisseau) m[i].getValue()).getTempo()
                    + Univers.getInt(500);
            hm.put(trouverTempoLibre(suivant, tempo), m[i].getKey());
        }
        return hm;
    }

    /**
     * Premier tempo libre à partir de t, qui est alors réservé. suivant associe à chaque tempo
     * déjà pris un tempo plus grand à essayer, ce qui évite de reparcourir une suite de tempos pris.
     */
    private static Integer trouverTempoLibre(HashMap suivant, int t) {
        int libre = t;
        Integer s = (Integer) suivant.get(Integer.valueOf(libre));
        while (s != null) {
            libre = s.intValue();
            s = (Integer) suivant.get(Integer.valueOf(libre));
        }
        Integer apres = Integer.valueOf(libre + 1);
        int courant = t;
        while (courant != libre) {
            int prochain = ((Integer) suivant.get(Integer.valueOf(courant))).intValue();
            suivant.put(Integer.valueOf(courant), apres);
            courant = prochain;
        }
        suivant.put(Integer.valueOf(libre), apres);
        return Integer.valueOf(libre);
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
                               TreeMap ht1, Heros h1, Commandant c1, Flotte f2, HashMap pos2, HashMap cible2,
                               TreeMap ht2, Heros h2, Commandant c2, int numTir) {

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
                tirVaisseau(mt1[compteur1].getValue(), f1, pos1, pos2, cible1, f2, numTir, h1, h2, c1);
                compteur1--;
            } else {
                tirVaisseau(mt2[compteur2].getValue(), f2, pos2, pos1, cible2, f1, numTir, h2, h1, c2);
                compteur2--;
            }
        }
    }

    /**
     * Tir continu (ReglesCombat.tirContinu) : les vaisseaux des deux flottes tirent dans l'ordre
     * du tempo ; chacun, à son tour, tire sur sa première cible puis en désigne de nouvelles
     * (jusqu'à getNbCibleMax) tant qu'il lui reste des armes.
     * Avec ReglesCombat.salve, un passage est limité à salve × (taille + 1)² armes : les vaisseaux
     * qui en ont encore repassent ensuite, dans le même ordre, jusqu'à ce que plus personne ne tire.
     */
    private static void combatContinu(Flotte f1, StrategieDeCombatSpatial s1, HashMap pos1, HashMap cible1,
                                      TreeMap ht1, Heros h1, Commandant c1, Flotte f2, StrategieDeCombatSpatial s2,
                                      HashMap pos2, HashMap cible2, TreeMap ht2, Heros h2, Commandant c2) {
        Map.Entry[] mt1 = (Map.Entry[]) ht1.entrySet().toArray(new Map.Entry[0]);
        Map.Entry[] mt2 = (Map.Entry[]) ht2.entrySet().toArray(new Map.Entry[0]);
        // un état par flotte : les deux flottes numérotent leurs vaisseaux de la même façon
        HashMap etats1 = new HashMap();
        HashMap etats2 = new HashMap();
        boolean repasser = true;
        while (repasser) {
            repasser = false;
            int compteur1 = mt1.length - 1;
            int compteur2 = mt2.length - 1;
            while ((compteur1 > -1) || (compteur2 > -1)) {
                boolean premier = (compteur2 == -1) || ((compteur1 != -1)
                        && (((Integer) mt1[compteur1].getKey()).compareTo((Integer) mt2[compteur2].getKey()) > 0));
                if (premier)
                    repasser |= tirVaisseauContinu(mt1[compteur1--].getValue(), f1, s1, pos1, pos2, cible1, f2, h1, h2, c1, etats1);
                else
                    repasser |= tirVaisseauContinu(mt2[compteur2--].getValue(), f2, s2, pos2, pos1, cible2, f1, h2, h1, c2, etats2);
            }
        }
        for (Object cle : ht1.values())
            f1.getVaisseau((Integer) cle).setTirsSalve(-1);
        for (Object cle : ht2.values())
            f2.getVaisseau((Integer) cle).setTirsSalve(-1);
    }

    /**
     * Un passage d'un vaisseau. etats garde, d'un passage à l'autre, {tir en cours, recherches
     * de cible vides, prochaine cible à désigner (1) ou cible en cours à reprendre (0), fini}.
     * Rend vrai si la salve a interrompu le vaisseau et qu'il doit repasser.
     */
    private static boolean tirVaisseauContinu(Object cle, Flotte f1, StrategieDeCombatSpatial s, HashMap pos1,
                                              HashMap pos2, HashMap cible, Flotte f2, Heros h1, Heros h2,
                                              Commandant com, HashMap etats) {
        int[] e = (int[]) etats.get(cle);
        if (e == null) {
            e = new int[]{0, 0, 0, 0};
            etats.put(cle, e);
        }
        if (e[3] == 1)
            return false;
        Vaisseau v = f1.getVaisseau((Integer) cle);
        int salve = ReglesCombat.actives().salve;
        // taille du plan, pas getTaille() : celle-ci remplit le cache des composants valides et
        // changerait la suite du combat
        int t = (salve > 0) ? v.getPlan().getTaille() + 1 : 0;
        v.setTirsSalve(salve > 0 ? salve * t * t : -1);
        int[] cibles = (int[]) cible.get(cle);
        Map.Entry[] seul = {new java.util.AbstractMap.SimpleEntry(cle, v)};
        int i = e[0];
        boolean designer = e[2] == 1;
        while (true) {
            if (designer) {
                if ((i >= cibles.length) || !peutEncoreTirer(v))
                    break;
                determinationCible(seul, s, cible, pos1, pos2, f2, i);
                if (cibles[i] == -1) {
                    // la recherche parcourt les tailles au hasard et peut échouer : on la refait
                    if (++e[1] > 3)
                        break; // plus de cible : le vaisseau garde ses armes restantes
                    i++;
                    continue;
                }
                if (f2.getVaisseau(Integer.valueOf(cibles[i])).estDetruit())
                    break; // épave (seulement sans ciblesVivantes)
            }
            tirVaisseau(cle, f1, pos1, pos2, cible, f2, i, h1, h2, com);
            if ((v.getTirsSalve() == 0) && peutEncoreTirer(v)) {
                // salve épuisée : au passage suivant, reprise sur la même cible si elle vit encore
                boolean cibleVivante = (cibles[i] != -1)
                        && !f2.getVaisseau(Integer.valueOf(cibles[i])).estDetruit();
                e[0] = cibleVivante ? i : i + 1;
                e[2] = cibleVivante ? 0 : 1;
                return true;
            }
            i++;
            designer = true;
        }
        e[3] = 1;
        return false;
    }

    private static void tirVaisseau(Object cle, Flotte f1, HashMap pos1, HashMap pos2, HashMap cible, Flotte f2, int numTir, Heros h1, Heros h2, Commandant com) {
        Vaisseau v = f1.getVaisseau((Integer) cle);
        int nbCibleMax = v.getNbCibleMax();
        boolean estCombatif = v.estCombatif();
        boolean estVivant = !v.estDetruit();
        Position3D att = (Position3D) pos1.get(cle);
        boolean journal = ReglesCombat.actives().journal;
        boolean vaisseau_a_tire = false;
        if (journal && Const.COMBAT_LOG_DETAIL)
            Combat.logDetail(combatEnCours + " C{0} , tir vaisseau {6} N°{1}/{2} ({3}, race: {4}) , attP: {5} ",
                    com == null ? -1 : com.getNumero(), numTir, nbCibleMax, v.getPlan().getNom(), v.getRaceEquipage(), att, cle);
        if ((numTir <= nbCibleMax) && (estCombatif) && (estVivant)) {
            Integer cle2 = new Integer(((int[]) cible.get(cle))[numTir]);
            if (cle2.intValue() != -1) {
                Vaisseau c = f2.getVaisseau(cle2);
                if (!c.estDetruit()) {
                    Position3D def = (Position3D) pos2.get(cle2);
                    int distance = Position3D.distance(att, def);

                    // --- CAPTURE DES DEGATS ---
                    int dommagesAvant = v.getDommagesEffectues();
                    v.tir(c, distance, h1, h2);
                    int degatsDuTir = v.getDommagesEffectues() - dommagesAvant;
                    if ((degatsDuTir > 0) && (com != null)) {
                        com.ajouterDegats((float)degatsDuTir);
                    }
                    // --------------------------

                    // Log exhaustif pour traçabilité
                    if (journal && Const.COMBAT_LOG_DETAIL && (com != null))
                        SherilLogger.log(String.format(
                            "[COMBAT-SPATIAL] Tir: %s (ID:%s) sur %s | Dégâts: %d | Cumul Commandant %s: %.2f",
                            v.getPlan().getNom(),
                            cle.toString(),
                            c.getPlan().getNom(),
                            degatsDuTir,
                            com.getNomNumeroText(),
                            com.getDegatsInfligesCeTour()
                        ));
                    //--------------
                    
                    if (journal)
                        Combat.logDetailln(", cible: {2}, deffP: {0}, distance: {1}", def, distance, c.getPlan().getNom());
                    vaisseau_a_tire = true;
                    
                }
            } else {
                Combat.logDetail("pas de cible");
            }
        }
        if (!vaisseau_a_tire) {
            Combat.logDetailln("");
        }
    }

}
