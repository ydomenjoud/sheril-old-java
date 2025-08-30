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
        int nbSys = Const.NB_SYSTEME;
        int nbFlottes = Const.NB_FLOTTE_NEUTRE;
        int nbSecteursVides = Const.NB_SECTEUR_VIDE;
        int[] secteursVides = new int[nbSecteursVides];

        for (int i = 0; i < nbSecteursVides; i++) {
            boolean dejaVu = false;
            int choix = Univers.getInt(Const.NB_SECTEURS) + 1;
            for (int j = 0; j < i; j++)
                if (secteursVides[j] == choix)
                    dejaVu = true;
            if (!dejaVu)
                secteursVides[i] = choix;
            else
                i--;
        }

        for (int i = 0; i < Const.NB_PORTES; i++)
            presenceSys[Const.PASSAGES_GALACTIQUES[i][0] - 1][Const.PASSAGES_GALACTIQUES[i][1] - 1] = true;

        // on va ajouter NB_SYSTEME équitablement entre chaque secteur
        int nbSystemBySectorMax = nbSys / Const.NB_SECTEURS;
        int[] systemsBySector = new int[Const.NB_SECTEURS];

//        for (int i = 0; i < nbSys; i++) {
//            System.out.print("S" + i + "-");
//            pos = Position.auHasard(numero);
//            if (!presenceSys[pos.getY() - 1][pos.getX() - 1]) {
//                boolean sV = false;
//                for (int j = 0; j < nbSecteursVides; j++)
//                    if (pos.getNumeroSecteur() == secteursVides[j])
//                        sV = true;
//                if ((!sV) || (Univers.getTest(10))) {
//                    Univers.setSysteme(Systeme.creerAuHasard(pos));
//                    commandantNeutre.ajouterPossession(pos,
//                            Possession.creerAuHasard());
//                    presenceSys[pos.getY() - 1][pos.getX() - 1] = true;
//                } else
//                    i--;
//            } else
//                i--;
//        }


        for (int i = 0; i < nbSys; i++) {
            pos = Position.auHasard(numero);
            int sectorIndexAt0 = pos.getNumeroSecteur() - 1;
            if (presenceSys[pos.getY() - 1][pos.getX() - 1]
                    || systemsBySector[sectorIndexAt0] >= nbSystemBySectorMax) {
//                System.out.println("sector" + sectorIndexAt0 + " full ( " + systemsBySector[sectorIndexAt0] + "), no place for " + pos.toString());
                i--;
            } else {
                System.out.print("S" + i + "-");
                Univers.setSysteme(Systeme.creerAuHasard(pos));
                commandantNeutre.ajouterPossession(pos, Possession.creerAuHasard());
                presenceSys[pos.getY() - 1][pos.getX() - 1] = true;
                systemsBySector[sectorIndexAt0]++;
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
