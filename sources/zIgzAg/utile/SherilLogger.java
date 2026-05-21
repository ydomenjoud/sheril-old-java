package zIgzAg.utile;

import zIgzAg.jeu.oceane.Chemin;
import zIgzAg.jeu.oceane.Univers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SherilLogger {
    private static final Logger logger = Logger.getLogger("Sheril");

    static {
        try {
            int tour = Univers.getTour();
            FileHandler fh = new FileHandler("data/logs/tour"+tour+".log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.err.println("Impossible d'initialiser le fichier de log : " + e.getMessage());
        }
    }

    public static void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void error(String msg, Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }
}
