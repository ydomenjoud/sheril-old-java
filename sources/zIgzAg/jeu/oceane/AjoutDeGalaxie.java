// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.util.Arrays;

public class AjoutDeGalaxie {

    /*
     * Classes a modifier :
     *
     * Const : REPARTITION_DES_RACES,
     * POLITIQUE_..,CHANCE_AUGMENTER_CARACTERISTIQUE_LEADER
     * ,CHANCE_TROUVER_COMPETENCE_HEROS,
     * CHANCE_TROUVER_COMPETENCE_GOUVERNEUR,HABITAT_RADIATION
     * ,HABITAT_TEMPERATURE,HABITAT_GRAVITE,RACES_ATMOSPHERES,
     * RACES_CARACTERISTIQUES,RACE_TECHNOLOGIES
     *
     * Messages : RACES, POLITIQUES,
     *
     * Possession : racePolitiqueAnti(int)
     *
     * Rapport : COULEURS_RACES, COULEURS_GALAXIES
     */

    public static void main(String args[]) {
        if (args.length != 1)
            System.exit(0);
        int numero = Integer.parseInt(args[0]);

        Univers univers = new Univers(true, Const.MESSAGE_U_00001);

        Commandant commandantNeutre = Univers.getCommandant(0);
        Position pos = null;

        boolean[][] presenceSys = new boolean[Const.BORNE_MAX][Const.BORNE_MAX];
        boolean[][] presenceFlo = new boolean[Const.BORNE_MAX][Const.BORNE_MAX];
        int nbFlottes = Const.NB_FLOTTE_NEUTRE;

        int nbSystemes = Const.NB_SYSTEME;
        int currentSystemCount = 0;

        // On divise la grille 30x30 en cellules de 2x2 -> 15x15 = 225 cellules.
        // On va répartir les 198 systèmes dans ces 225 cellules.
        java.util.List<int[]> cellules = new java.util.ArrayList<>();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                cellules.add(new int[]{row, col});
            }
        }
        java.util.Collections.shuffle(cellules);

        for (int i = 0; i < nbSystemes && i < cellules.size(); i++) {
            int[] cell = cellules.get(i);
            int row = cell[0];
            int col = cell[1];

            int x = col * 2 + 1 + Univers.getInt(2);
            int y = row * 2 + 1 + Univers.getInt(2);
            pos = new Position(numero, y, x);

            if (presenceSys[pos.getY() - 1][pos.getX() - 1]) {
                // Devrait théoriquement pas arriver avec des cellules disjointes,
                // mais on garde la sécurité.
                i--; 
                continue;
            }

            System.out.print("S" + currentSystemCount + "-");
            Univers.setSysteme(Systeme.creerAuHasard(pos));
            commandantNeutre.ajouterPossession(pos, Possession.creerAuHasard());
            presenceSys[pos.getY() - 1][pos.getX() - 1] = true;
            currentSystemCount++;
        }

        
        System.out.println();
        System.out.println(Univers.listePositionsSystemes().length + " systèmes");

        for (int i = 0; i < nbFlottes; i++) {
            pos = Position.auHasard(numero);
            if (presenceSys[pos.getY() - 1][pos.getX() - 1]
                    && !presenceFlo[pos.getY() - 1][pos.getX() - 1]) {
                System.out.print("F" + i + "-");
                Flotte f = Flotte.creerAuHasard(pos, "Flotte neutre",
                        Univers.getInt(Messages.RACES.length),
                        Univers.getInt(100));
                f.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE);
                commandantNeutre.ajouterFlotte(f);
                presenceFlo[pos.getY() - 1][pos.getX() - 1] = true;
            } else
                i--;
        }
        System.out.println("");

        Univers.setCommandant(commandantNeutre);
        univers.sauvegarder();

        VisualisationUnivers.genererCarteHTML("data/commun/carte.html");

    }

}
