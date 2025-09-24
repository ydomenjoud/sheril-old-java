import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import zIgzAg.jeu.oceane.*;
import zIgzAg.utile.Copie;

public class Start {

    public static void displayHelp() {
        System.out.println("Usage: java -jar sheril.jar <action>");
        System.out.println("Actions disponibles:");
        System.out.println("init: initialisation de l'Univers");
        System.out.println("addNewGalaxy <num>: Ajoute une galaxy à l'Univers, deuxième");
        System.out.println("newRound: passe le tour");
    }

    public static void main(String[] args) {

        // Contrôle arguments
        if (args.length < 1) {
            System.out.println("Vous devez spécifier au moins un paramètre");
            displayHelp();
            System.exit(-1);
        }

        if (args[0].equals("init")) {
            initUnivers();
        } else if (args[0].equals("addNewGalaxy")) {
            if (args.length != 2) {
                System.out.println("Il faut spécifier le numéro de galaxy");
                displayHelp();
                System.exit(-1);
            }
            addNewGalaxy(args[1]);
        } else if (args[0].equals("newRound")) {
            newRound();
        } else if (args[0].equals("help")) {
            displayHelp();
        } else {
            System.out.println("Vous devez spécifier au moins un paramètre");
            displayHelp();
        }

    }

    public static void initUnivers() {

        System.out.println("Univers init");
        Univers.initialisation();
        addNewGalaxy("0");
        Rapport.ecrireMessagesSystemes();

    }

    public static void addNewGalaxy(String num) {
        System.out.println("Add new Galaxy");
        AjoutDeGalaxie.main(new String[]{num});
    }

    public static void newRound() {

        // Récupération du numéro du tour
        Univers.chargerNumeroTour();
        int numTour = Univers.getTour();
        int nextTour = numTour + 1;

        // si le dossier du prochain tour existe déjà, on stop
        // car les données risquent de se mélanger ( comme la production des ordres )
        if (new File(Chemin.RACINE + "tour" + nextTour).exists()) {
            System.out.println("Le dossier du prochain tour existe déjà, on fait quoi ?");
            System.exit(0);
        }

        // Première phrase
        Univers.notify("Bonjour!");

        try {
            Univers.notify("Démarrage du nouveau tour");
            System.out.println(" FAKE " + (Const.FAKE_TURN ? "OUI" : "NON" ) + ", NOTIFY " + (Const.NOTIFY_BOT ? "OUI" : "NON" ) );

            // On déroule le tour
            DeroulementDuTour.main(null);

            if (!Const.FAKE_TURN) {
                // On va vider les anciens ordres
                ProductionOrdres.viderAnciensOrdres();

                // Insérer les nouveaux
                ProductionOrdres.insertionOrdres();
                Univers.notify("Insertion des nouveaux ordres");

                Start.uploadBySSH(Chemin.NUMERO_DU_TOUR, "");

                // Déplacements des rapports
                String rapportsDir = Chemin.RACINE_ZIP + nextTour;
                Copie.copieRepertoire(Chemin.ZIP, rapportsDir);
                Start.uploadBySSH(rapportsDir, "rapports");
                Univers.notify("Rapports disponibles");


                // Déplacements des statistiques
                Copie.copieRepertoire(Chemin.STATS, Chemin.PATH_STATS);
                Univers.notify("Mise en place des stats");
                Start.uploadBySSH(Chemin.PATH_STATS, "");

            }

            Univers.notify("Fin du tour");
            Univers.notify("Au revoir!");

            Univers.setTour(Const.FAKE_TURN ? numTour : nextTour);
            Univers.sauvegarderNumeroTour();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isToday() {
        String test = "";
        try {
            test = getRemoteFileContent("https://sheril.pbem-france.net/autres/next-turn.php");
        } catch (Exception e) {
            System.out.println("Erreur dans le chargement de https://sheril.pbem-france.net/autres/next-turn.php");
            e.printStackTrace();
            System.exit(-1);
        }
        return test.equalsIgnoreCase("yes");
    }


    public static void uploadBySSH(
            String localPath,
            String remotePath
    ) {
        System.out.println("upload " + localPath + " vers " + remotePath);
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "scp",
                    "-r",
                    "-P",
                    Const.SSH_PORT,
                    localPath,
                    Const.SSH_BASE_PATH + remotePath
            );
            pb.inheritIO(); // pour voir stdout/stderr dans ta console
            Process process = pb.start();
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Erreur dans la copie SSH de " + localPath + " vers " + remotePath);
        }
    }

    public static String getNextTurn() {
        try {
            return getRemoteFileContent("http://sheril.pbem-france.net/autres/next-turn.php?act=next");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String getRemoteFileContent(String urlString) throws Exception {

        StringBuffer source = new StringBuffer("");

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.connect();

        Scanner scanner = new Scanner(conn.getInputStream());
        while (scanner.hasNextLine()) {
            source.append(scanner.nextLine());
        }

        return source.toString();

    }
}
