package zIgzAg.jeu.oceane;

import java.io.File;
import java.util.Arrays;

/**
 * Classe permettant de régénérer les statistiques pour les tours passés.
 * Elle parcourt les répertoires de données de chaque tour et appelle la production des statistiques.
 */
public class GenerateurStatsPassees {

    public static void main(String[] args) {
        Integer tourCible = null;
        if (args.length > 0) {
            try {
                tourCible = Integer.parseInt(args[0]);
                System.out.println("Tour cible spécifié : " + tourCible);
            } catch (NumberFormatException e) {
                System.err.println("L'argument doit être un numéro de tour valide.");
                return;
            }
        }

        System.out.println("Démarrage de la génération des statistiques passées...");

        // On récupère le chemin des données depuis la configuration
        String pathData = Chemin.RACINE;
        File dataDir = new File(pathData);

        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("Le répertoire de données n'existe pas : " + pathData);
            return;
        }

        // On cherche les répertoires de type tourX
        File[] tourDirs;
        if (tourCible != null) {
            File targetedDir = new File(dataDir, "tour" + tourCible);
            if (!targetedDir.exists() || !targetedDir.isDirectory()) {
                System.err.println("Le répertoire pour le tour " + tourCible + " n'existe pas : " + targetedDir.getAbsolutePath());
                return;
            }
            tourDirs = new File[]{targetedDir};
        } else {
            tourDirs = dataDir.listFiles(f -> f.isDirectory() && f.getName().startsWith("tour"));
        }

        if (tourDirs == null || tourDirs.length == 0) {
            System.out.println("Aucun répertoire de tour trouvé dans " + pathData);
            return;
        }

        // On trie les tours par numéro pour les traiter dans l'ordre (utile surtout si on traite tout)
        Arrays.sort(tourDirs, (a, b) -> {
            int numA = extraireNumeroTour(a.getName());
            int numB = extraireNumeroTour(b.getName());
            return Integer.compare(numA, numB);
        });

        for (File tourDir : tourDirs) {
            int numTour = extraireNumeroTour(tourDir.getName());
            if (numTour == -1) continue;

            System.out.println("Traitement du tour " + numTour + "...");

            try {
                // Initialisation des chemins pour ce tour spécifique
                Chemin.initialiserChemins(numTour);

                // Initialisation de l'univers pour ce tour
                // On utilise le constructeur qui permet de charger les données
                Univers univers = new Univers(true, "Chargement tour " + numTour);
                // On force le tour dans l'univers pour que produireStatistiques() utilise le bon numéro
                Univers.setTour(numTour);
                univers.charger();

                // Production des statistiques pour ce tour
                ProductionOrdres.produireStatistiques();

                System.out.println("Statistiques du tour " + numTour + " générées avec succès.");
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement du tour " + numTour + " :");
                e.printStackTrace();
            }
        }

        System.out.println("Génération terminée.");
    }

    private static int extraireNumeroTour(String name) {
        try {
            return Integer.parseInt(name.substring(4));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
