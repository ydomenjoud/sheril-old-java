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

        int systemsPerSector = Const.NB_SYSTEME / Const.NB_SECTEURS;
        int currentSystemCount = 0;

        for (int sector = 1; sector <= Const.NB_SECTEURS; sector++) {
            for (int i = 0; i < systemsPerSector; i++) {
                pos = Position.auHasardInSector(numero, sector);
                if (presenceSys[pos.getY() - 1][pos.getX() - 1]) {
                    i--;
                } else {
                    System.out.print("S" + currentSystemCount + "-");
                    Univers.setSysteme(Systeme.creerAuHasard(pos));
                    commandantNeutre.ajouterPossession(pos, Possession.creerAuHasard());
                    presenceSys[pos.getY() - 1][pos.getX() - 1] = true;
                    currentSystemCount++;
                }
            }
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
                        Univers.getInt(300));
                f.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE);
                commandantNeutre.ajouterFlotte(f);
                presenceFlo[pos.getY() - 1][pos.getX() - 1] = true;
            } else
                i--;
        }
        System.out.println("");

        Univers.setCommandant(commandantNeutre);
        univers.sauvegarder();

    }

}
