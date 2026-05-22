package zIgzAg.utile;

import zIgzAg.jeu.oceane.Univers;
import java.io.IOException;
import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SherilLogger {
    private static final Logger logger = Logger.getLogger("Sheril");

    static {
        try {
            int tour = Univers.getTour();
            FileHandler fh = new FileHandler("data/logs/tour" + tour + ".log", true);

            logger.setUseParentHandlers(false);

            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = df.format(new Date(record.getMillis()));
                    return String.format("[%s] [%s] %s%n",
                            dateStr,
                            record.getLevel(),
                            record.getMessage());
                }
            });

            logger.addHandler(fh);

        } catch (IOException e) {
            System.err.println("Impossible d'initialiser le fichier de log : " + e.getMessage());
        }
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void error(String msg, Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }
}