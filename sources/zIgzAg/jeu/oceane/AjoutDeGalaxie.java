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

    // Paramètres des paquets de départ
    public static final int SYSTEMES_NEUTRES_PAR_PAQUET = 8;
    public static final int SYSTEMES_NEUTRES_PROCHES = 4;
    private static final int DISTANCE_MIN_PROCHE = 1;
    private static final int DISTANCE_MAX_PROCHE = 2;
    private static final int DISTANCE_MIN_ELOIGNE = 3;
    private static final int DISTANCE_MAX_ELOIGNE = 4;
    private static final int TENTATIVES_EXCLUSIONS = 200;

    // Budget planétaire par paquet de départ (capitale + 8 neutres)
    private static final int PLANETES_MIN_PAQUET = 155;
    private static final int PLANETES_MAX_PAQUET = 165;
    private static final int PLANETES_MIN_SYSTEME = 13;
    private static final int PLANETES_MAX_SYSTEME = 20;

    // Nombre de systèmes neutres régionaux additionnels par zone de Voronoï
    public static int SYSTEMES_REGIONAUX_PAR_PAQUET = Const.SYSTEMES_REGIONAUX_PAR_PAQUET;

    public static void main(String args[]) {
        if (args.length < 1)
            System.exit(0);
        int numero = Integer.parseInt(args[0]);
        // Chargement du nombre de joueurs (depuis l'argument ou config.properties par défaut)
        int nombreJoueurs = (args.length >= 2) ? Integer.parseInt(args[1]) : Const.NB_JOUEURS;
        if (nombreJoueurs < 30 || nombreJoueurs > 42)
            throw new IllegalArgumentException("Le nombre de joueurs doit être compris entre 30 et 42.");

        // Adaptation dynamique de la taille de galaxie (50x50 pour 30-35 joueurs, 60x60 pour 36-42)
        if (nombreJoueurs <= 35) {
            Const.BORNE_MAX = 50;
        } else {
            Const.BORNE_MAX = 60;
        }
        // Persistance de BORNE_MAX pour les tours suivants (Start newRound)
        zIgzAg.utile.Fiche.initialisation(Chemin.BORNE_MAX_FILE);
        zIgzAg.utile.Fiche.ecriture(Chemin.BORNE_MAX_FILE, Integer.toString(Const.BORNE_MAX));

        Univers univers = new Univers(true, Const.MESSAGE_U_00001);

        Commandant commandantNeutre = Univers.getCommandant(0);
        boolean[][] presenceSys = new boolean[Const.BORNE_MAX][Const.BORNE_MAX];
        ArrayList paquets = new ArrayList(nombreJoueurs);
        List capitales = positionsCapitales(numero, nombreJoueurs);
        List positionsOccupees = new ArrayList();

        // Réserver toutes les capitales avant de construire les paquets évite les recouvrements.
        for (int i = 0; i < capitales.size(); i++) {
            Position capitale = (Position) capitales.get(i);
            presenceSys[capitale.getY() - 1][capitale.getX() - 1] = true;
            positionsOccupees.add(capitale);
        }

        // Création des paquets de départ (capitale + 2nd système + 8 neutres)
        System.out.println("Création paquets de départ");
        for (int i = 0; i < nombreJoueurs; i++) {
            PaquetDepart paquet = creerPaquet((Position) capitales.get(i), capitales,
                    presenceSys, commandantNeutre);
            if (paquet == null)
                throw new IllegalStateException("Impossible de placer tous les paquets de départ.");
            paquets.add(paquet);
            positionsOccupees.add(paquet.getSecondSysteme());
            for (int j = 0; j < paquet.getSystemesNeutres().length; j++)
                positionsOccupees.add(paquet.getSystemesNeutres()[j]);
        }
        Univers.setPaquetsDepart(paquets);

        // Création des systèmes neutres régionaux dans chaque territoire de Voronoï
        System.out.println("Création systèmes régionaux");
        for (int i = 0; i < capitales.size(); i++) {
            Position capitale = (Position) capitales.get(i);
            for (int j = 0; j < SYSTEMES_REGIONAUX_PAR_PAQUET; j++) {
                Position position = positionRegionale(capitale, capitales, presenceSys,
                        positionsOccupees);
                creerSystemeNeutre(position, commandantNeutre, -1);
                presenceSys[position.getY() - 1][position.getX() - 1] = true;
                positionsOccupees.add(position);
            }
        }

        System.out.println("Création flottes");
        var positionsList = Univers.listePositionsSystemes();
        for (Position position : positionsList) {
            Flotte f = Flotte.creerAuHasard(position, "Flotte neutre",
                    Univers.getInt(Messages.RACES.length),
                    50 + Univers.getInt(100));
            f.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE);
            commandantNeutre.ajouterFlotte(f);
        }
        System.out.println("Fin création flottes");

        Univers.setCommandant(commandantNeutre);
        System.out.println("Fin SET NEUTRE");
        univers.sauvegarder();
        System.out.println("Fin sauvegarde");
        VisualisationUnivers.genererCarteHTML();
        System.out.println("Fin génération carte HTML");

    }

    private static PaquetDepart creerPaquet(Position capitale, List capitales,
            boolean[][] presenceSys,
            Commandant neutre) {
        List positionsProches = positionsPaquetLibres(capitale, capitales, presenceSys,
                DISTANCE_MIN_PROCHE, DISTANCE_MAX_PROCHE);
        List positionsEloignees = positionsPaquetLibres(capitale, capitales, presenceSys,
                DISTANCE_MIN_ELOIGNE, DISTANCE_MAX_ELOIGNE);
        if (positionsProches.size() < SYSTEMES_NEUTRES_PROCHES + 1
                || positionsEloignees.size() < SYSTEMES_NEUTRES_PAR_PAQUET
                        - SYSTEMES_NEUTRES_PROCHES)
            return null;

        // Le second système et quatre neutres occupent l'anneau proche ; les quatre autres
        // neutres sont sélectionnés dans l'anneau extérieur.
        Collections.shuffle(positionsProches);
        Collections.shuffle(positionsEloignees);
        Position secondSysteme = (Position) positionsProches.remove(0);
        presenceSys[secondSysteme.getY() - 1][secondSysteme.getX() - 1] = true;
        Position[] neutres = new Position[SYSTEMES_NEUTRES_PAR_PAQUET];
        int[] planetes = repartitionPlanetesPaquet();
        creerSystemeNeutre(capitale, neutre, planetes[0]);
        for (int i = 0; i < SYSTEMES_NEUTRES_PROCHES; i++) {
            neutres[i] = (Position) positionsProches.get(i);
            creerSystemeNeutre(neutres[i], neutre, planetes[i + 1]);
            presenceSys[neutres[i].getY() - 1][neutres[i].getX() - 1] = true;
        }
        for (int i = SYSTEMES_NEUTRES_PROCHES; i < neutres.length; i++) {
            neutres[i] = (Position) positionsEloignees.get(i - SYSTEMES_NEUTRES_PROCHES);
            creerSystemeNeutre(neutres[i], neutre, planetes[i + 1]);
            presenceSys[neutres[i].getY() - 1][neutres[i].getX() - 1] = true;
        }
        return new PaquetDepart(capitale, secondSysteme, neutres);
    }

    private static int[] repartitionPlanetesPaquet() {
        int[] repartition = new int[SYSTEMES_NEUTRES_PAR_PAQUET + 1];
        for (int i = 0; i < repartition.length; i++) {
            repartition[i] = PLANETES_MIN_SYSTEME;
        }
        int totalVise = PLANETES_MIN_PAQUET
                + Univers.getInt(PLANETES_MAX_PAQUET - PLANETES_MIN_PAQUET + 1);
        int excedent = totalVise - (repartition.length * PLANETES_MIN_SYSTEME);

        while (excedent > 0) {
            int idx = Univers.getInt(repartition.length);
            if (repartition[idx] < PLANETES_MAX_SYSTEME) {
                repartition[idx]++;
                excedent--;
            }
        }
        return repartition;
    }

    private static List positionsPaquetLibres(Position centre, List capitales,
            boolean[][] presenceSys, int distanceMin, int distanceMax) {
        ArrayList positions = new ArrayList();
        for (int y = 1; y <= Const.BORNE_MAX; y++) {
            for (int x = 1; x <= Const.BORNE_MAX; x++) {
                Position position = new Position(centre.getNumeroGalaxie(), y, x);
                if (!presenceSys[y - 1][x - 1]
                    && Position.distance(centre, position) >= distanceMin
                    && Position.distance(centre, position) <= distanceMax
                        && appartientAuTerritoire(position, centre, capitales))
                    positions.add(position);
            }
        }
        return positions;
    }

    private static boolean appartientAuTerritoire(Position position, Position capitale,
            List capitales) {
        int distanceCapitale = Position.distance(position, capitale);
        for (int i = 0; i < capitales.size(); i++) {
            Position autreCapitale = (Position) capitales.get(i);
            if (!autreCapitale.equals(capitale)
                    && Position.distance(position, autreCapitale) <= distanceCapitale)
                return false;
        }
        return true;
    }

    private static Position positionRegionale(Position capitale, List capitales,
            boolean[][] presenceSys, List positionsOccupees) {
        ArrayList candidates = new ArrayList();
        for (int y = 1; y <= Const.BORNE_MAX; y++) {
            for (int x = 1; x <= Const.BORNE_MAX; x++) {
                Position position = new Position(capitale.getNumeroGalaxie(), y, x);
                if (!presenceSys[y - 1][x - 1]
                        && appartientAuTerritoire(position, capitale, capitales))
                    candidates.add(position);
            }
        }
        if (candidates.isEmpty())
            throw new IllegalStateException("Impossible de répartir les systèmes régionaux.");

        // Chaque ajout maximise son éloignement des systèmes déjà placés dans la galaxie.
        int distanceMax = -1;
        ArrayList meilleurs = new ArrayList();
        for (int i = 0; i < candidates.size(); i++) {
            Position candidate = (Position) candidates.get(i);
            int distance = distanceMinimale(candidate, positionsOccupees);
            if (distance > distanceMax) {
                distanceMax = distance;
                meilleurs.clear();
                meilleurs.add(candidate);
            } else if (distance == distanceMax) {
                meilleurs.add(candidate);
            }
        }
        return (Position) meilleurs.get(Univers.getInt(meilleurs.size()));
    }

    private static int distanceMinimale(Position position, List positions) {
        int distance = Integer.MAX_VALUE;
        for (int i = 0; i < positions.size(); i++)
            distance = Math.min(distance, Position.distance(position, (Position) positions.get(i)));
        return distance;
    }

    private static List positionsCapitales(int galaxie, int nombreJoueurs) {
        int gridDim = (nombreJoueurs <= 35) ? 6 : 7;
        int[] positionsY = axeCapitales(gridDim);
        int[] positionsX = axeCapitales(gridDim);
        int nombreExclues = gridDim * gridDim - nombreJoueurs;
        boolean[][] exclues = exclusionsReparties(gridDim, gridDim, nombreExclues);

        ArrayList capitales = new ArrayList(nombreJoueurs);
        for (int ligne = 0; ligne < gridDim; ligne++) {
            for (int colonne = 0; colonne < gridDim; colonne++) {
                if (!exclues[ligne][colonne])
                    capitales.add(new Position(galaxie, positionsY[ligne], positionsX[colonne]));
            }
        }
        Collections.shuffle(capitales);
        return capitales;
    }

    private static boolean[][] exclusionsReparties(int lines, int cols, int nombreExclues) {
        for (int tentative = 0; tentative < TENTATIVES_EXCLUSIONS; tentative++) {
            boolean[][] exclues = new boolean[lines][cols];
            int exclusionsPlacees = 0;
            while (exclusionsPlacees < nombreExclues) {
                ArrayList candidates = new ArrayList();
                for (int ligne = 0; ligne < lines; ligne++) {
                    for (int colonne = 0; colonne < cols; colonne++) {
                        if (!exclues[ligne][colonne] && peutExclure(ligne, colonne, exclues, lines, cols))
                            candidates.add(new int[] {ligne, colonne});
                    }
                }
                if (candidates.isEmpty())
                    break;
                int[] candidate = (int[]) candidates.get(Univers.getInt(candidates.size()));
                exclues[candidate[0]][candidate[1]] = true;
                exclusionsPlacees++;
            }
            if (exclusionsPlacees == nombreExclues)
                return exclues;
        }
        throw new IllegalStateException("Impossible de répartir les emplacements de départ retirés.");
    }

    private static int[] axeCapitales(int taille) {
        ArrayList ecarts = new ArrayList(taille);
        int nbNeuf = (taille == 6) ? 2 : 4;
        for (int i = 0; i < taille; i++)
            ecarts.add(new Integer(i < nbNeuf ? 9 : 8));
        Collections.shuffle(ecarts);

        int[] positions = new int[taille];
        positions[0] = Univers.getInt(Const.BORNE_MAX) + 1;
        for (int i = 1; i < taille; i++) {
            int ecart = ((Integer) ecarts.get(i - 1)).intValue();
            positions[i] = (positions[i - 1] - 1 + ecart) % Const.BORNE_MAX + 1;
        }
        return positions;
    }

    private static boolean peutExclure(int ligne, int colonne, boolean[][] exclues, int lines, int cols) {
        if (nombreExclusionsLigne(ligne, exclues, cols) >= 2
                || nombreExclusionsColonne(colonne, exclues, lines) >= 2)
            return false;
        for (int decalageLigne = -1; decalageLigne <= 0; decalageLigne++)
            for (int decalageColonne = -1; decalageColonne <= 0; decalageColonne++)
                if (formeBlocVide(ligne, colonne, decalageLigne, decalageColonne, exclues, lines, cols))
                    return false;
        return true;
    }

    private static int nombreExclusionsLigne(int ligne, boolean[][] exclues, int cols) {
        int nombre = 0;
        for (int colonne = 0; colonne < cols; colonne++)
            if (exclues[ligne][colonne])
                nombre++;
        return nombre;
    }

    private static int nombreExclusionsColonne(int colonne, boolean[][] exclues, int lines) {
        int nombre = 0;
        for (int ligne = 0; ligne < lines; ligne++)
            if (exclues[ligne][colonne])
                nombre++;
        return nombre;
    }

    private static boolean formeBlocVide(int ligne, int colonne, int decalageLigne,
            int decalageColonne, boolean[][] exclues, int lines, int cols) {
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int autreLigne = (ligne + decalageLigne + y + lines) % lines;
                int autreColonne = (colonne + decalageColonne + x + cols) % cols;
                if (autreLigne != ligne || autreColonne != colonne)
                    if (!exclues[autreLigne][autreColonne])
                        return false;
            }
        }
        return true;
    }

    private static void creerSystemeNeutre(Position pos, Commandant neutre, int nbPlanetes) {
        Univers.setSysteme(Systeme.creerAuHasard(pos, nbPlanetes));
        neutre.ajouterPossession(pos, Possession.creerAuHasard());
    }

}
