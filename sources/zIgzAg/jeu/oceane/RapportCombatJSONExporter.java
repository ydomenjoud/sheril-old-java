package zIgzAg.jeu.oceane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RapportCombatJSONExporter {

    // Instanciation de Gson avec un formatage propre (indentation)
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping() // Empêche de convertir les caractères comme '<' ou '>' en unicode
            .create();

    /**
     * Génère une chaîne JSON contenant la liste des combats.
     */
    public static String exporterString(List<CombatGlobalData> combatsGroupes) {
        if (combatsGroupes == null || combatsGroupes.isEmpty()) {
            return "[]";
        }
        return gson.toJson(combatsGroupes);
    }

    /**
     * Exporte la liste des combats dans un fichier JSON sur le disque.
     */
    public static void exporterFichier(List<CombatGlobalData> combatsGroupes, File fichierCible) throws IOException {
        if (combatsGroupes != null && !combatsGroupes.isEmpty()) {
            try (FileWriter writer = new FileWriter(fichierCible)) {
                gson.toJson(combatsGroupes, writer);
            }
        }
    }
}