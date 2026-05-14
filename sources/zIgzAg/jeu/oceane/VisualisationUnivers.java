package zIgzAg.jeu.oceane;

import java.io.FileWriter;
import java.io.IOException;

public class VisualisationUnivers {

    public static void genererCarteHTML(String cheminFichier) {
        try {
            FileWriter writer = new FileWriter(cheminFichier);
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n");
            writer.write("<meta charset='UTF-8'>\n");
            writer.write("<title>Carte de l'Univers</title>\n");
            writer.write("<style>\n");
            writer.write("td:has(.commandant-num) {background-color: #076148;}\n");
            writer.write("table { border-collapse: collapse; background-color: #000; }\n");
            writer.write("td { width: 40px; height: 40px; border: 1px solid #333; text-align: center; vertical-align: middle; position: relative; padding: 0; }\n");
            writer.write(".systeme { display: block; width: 30px; height: 30px; margin: auto; border-radius: 50%; position: relative; border: 1px solid #555; }\n");
            writer.write(".commandant-num { text-shadow: 2px 2px black; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: white; font-weight: bold; font-size: 17px; text-shadow: 1px 1px 2px black; z-index: 10; }\n");
            writer.write(".empty { color: #222; font-size: 8px; }\n");
            writer.write(".grid-container { display: flex; flex-direction: column; align-items: center; padding: 20px; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            writer.write("<div class='grid-container'>\n");
            writer.write("<h1>Carte de la Galaxie (Grille 30x30)</h1>\n");
            writer.write("<table>\n");
            
            int nbRacesTotal = Messages.RACES.length;
            int[] sommePopActuelle = new int[nbRacesTotal];
            int[] sommePopMaximale = new int[nbRacesTotal];
            int[] sommeNbPlanetes = new int[nbRacesTotal];

            for (int y = 1; y <= 30; y++) {
                writer.write("<tr>\n");
                for (int x = 1; x <= 30; x++) {
                    Position pos = new Position(0, y, x);
                    Systeme sys = Univers.getSysteme(pos);
                    writer.write("<td>");
                    if (sys != null) {
                        int[] racesPresentes = new int[Rapport.COULEURS_RACES.length];
                        int totalPop = 0;
                        int nbRaces = 0;
                        
                        for (int r = 0; r < Rapport.COULEURS_RACES.length; r++) {
                            int popRace = sys.getPopulationDeRace(-1, r); // -1 pour toutes les populations du système
                            if (popRace > 0) {
                                racesPresentes[r] = popRace;
                                totalPop += popRace;
                                nbRaces++;
                                
                                sommePopActuelle[r] += popRace;
                                sommePopMaximale[r] += sys.getPopulationMaximaleDeRace(-1, r);
                                // On compte le nombre de planètes où la race est présente dans ce système
                                for (int p = 0; p < sys.getNombrePlanetes(); p++) {
                                    if (sys.getPlanete(p).racePresente(r)) {
                                        sommeNbPlanetes[r]++;
                                    }
                                }
                            }
                        }

                        StringBuilder gradient = new StringBuilder("conic-gradient(");
                        if (nbRaces > 0) {
                            float currentPercent = 0;
                            boolean first = true;
                            for (int r = 0; r < racesPresentes.length; r++) {
                                if (racesPresentes[r] > 0) {
                                    float percent = (float) racesPresentes[r] * 100 / totalPop;
                                    if (!first) gradient.append(", ");
                                    gradient.append(Rapport.COULEURS_RACES[r]).append(" ").append(currentPercent).append("% ").append(currentPercent + percent).append("%");
                                    currentPercent += percent;
                                    first = false;
                                }
                            }
                        } else {
                            gradient.append("#777 0% 100%");
                        }
                        gradient.append(")");

                        writer.write("<div class='systeme' style='background: " + gradient + ";' title='" + sys.getNom() + "'>");
                        
                        // Chercher s'il y a un propriétaire (numéro du commandant au centre)
                        int proprio = 0;
                        Planete[] planetes = sys.getPlanetes();
                        for(int i=0; i<planetes.length; i++) {
                            if (planetes[i].getProprio() != 0) {
                                proprio = planetes[i].getProprio();
                                break;
                            }
                        }
                        
                        if (proprio != 0) {
                            writer.write("<span class='commandant-num'>" + proprio + "</span>");
                        }
                        writer.write("</div>");
                    } else {
                        writer.write("<span class='empty'>.</span>");
                    }
                    writer.write("</td>\n");
                }
                writer.write("</tr>\n");
            }
            writer.write("</table>\n");

            writer.write("<br/>\n");
            writer.write("<table style='color: white; border: 1px solid #555; width: 600px;'>\n");
            writer.write("<tr style='background-color: #222;'>");
            writer.write("<th>Race</th><th>Pop. Actuelle</th><th>Pop. Maximale</th><th>Planètes</th>");
            writer.write("</tr>\n");

            for (int r = 0; r < nbRacesTotal; r++) {
                writer.write("<tr>\n");
                writer.write("<td style='padding: 5px;'><span style='display: inline-block; width: 12px; height: 12px; background-color: " + Rapport.COULEURS_RACES[r] + "; margin-right: 10px;'></span>" + Messages.RACES[r] + "</td>");
                writer.write("<td style='text-align: right; padding: 5px;'>" + sommePopActuelle[r] + "</td>");
                writer.write("<td style='text-align: right; padding: 5px;'>" + sommePopMaximale[r] + "</td>");
                writer.write("<td style='text-align: right; padding: 5px;'>" + sommeNbPlanetes[r] + "</td>");
                writer.write("</tr>\n");
            }
            writer.write("</table>\n");

            writer.write("</div>\n");

            writer.write("</body>\n</html>\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération de la carte HTML : " + e.getMessage());
        }
    }
}
