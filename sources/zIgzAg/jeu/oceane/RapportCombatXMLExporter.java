package zIgzAg.jeu.oceane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zIgzAg.jeu.oceane.CombatGlobalData;
import zIgzAg.jeu.oceane.RapportCombatData;
import zIgzAg.jeu.oceane.RapportCombatData.EntiteCombatData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class RapportCombatXMLExporter {

    /**
     * Génère et renvoie le nœud DOM <combats> contenant tous les affrontements.
     */
    public static Element genererElementCombats(List<CombatGlobalData> combatsGroupes, Document docCible) throws Exception {
        if (combatsGroupes == null || combatsGroupes.isEmpty()) {
            return null;
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document tempDoc = docBuilder.newDocument();

        // Noeud racine <combats> dans le document temporaire
        Element root = tempDoc.createElement("combats");
        tempDoc.appendChild(root);

        for (CombatGlobalData combat : combatsGroupes) {
            Element c = tempDoc.createElement("c");
            c.setAttribute("attaquant", String.valueOf(combat.attaquant.id));
            c.setAttribute("defenseur", String.valueOf(combat.defenseur.id));
            c.setAttribute("position", combat.positionSysteme.replace("-", "_"));
            c.setAttribute("type", combat.typeCombat);
            root.appendChild(c);

            // 1. Noeud <initial>
            Element initialNode = tempDoc.createElement("initial");
            if (combat.initial != null) {
                construireContenuEtat(tempDoc, initialNode, combat, combat.initial, false);
            } else if (!combat.tours.isEmpty()) {
                RapportCombatData premierTour = combat.tours.get(0);
                construireContenuEtatInitialReconstruit(tempDoc, initialNode, combat, premierTour);
            }
            c.appendChild(initialNode);

            // 2. Noeuds <tour>
            for (RapportCombatData tourData : combat.tours) {
                Element tourNode = tempDoc.createElement("tour");
                tourNode.setAttribute("numero", String.valueOf(tourData.tourNumber));
                construireContenuEtat(tempDoc, tourNode, combat, tourData, true);
                c.appendChild(tourNode);
            }
        }

        // Importation du nœud temporaire vers le document XML cible (documentG)
        return (Element) docCible.importNode(root, true);
    }

    public static void exporterFichier(CombatGlobalData combat, File fichierCible) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Racine <combats>
        Element root = doc.createElement("combats");
        doc.appendChild(root);

        // Noeud principal du combat <c>
        Element c = doc.createElement("c");
        c.setAttribute("attaquant", String.valueOf(combat.attaquant.id));
        c.setAttribute("defenseur", String.valueOf(combat.defenseur.id));
        c.setAttribute("type", combat.typeCombat);
        c.setAttribute("position", combat.positionSysteme);
        root.appendChild(c);

        // 1. Génération du noeud <initial>
        Element initialNode = doc.createElement("initial");
        if (combat.initial != null) {
            construireContenuEtat(doc, initialNode, combat, combat.initial, false);
        } else if (!combat.tours.isEmpty()) {
            // Si pas d'initial explicite, on reconstruit l'état 0 à partir du 1er tour
            RapportCombatData premierTour = combat.tours.get(0);
            construireContenuEtatInitialReconstruit(doc, initialNode, combat, premierTour);
        }
        c.appendChild(initialNode);

        // 2. Noeuds <tour>
        for (RapportCombatData tourData : combat.tours) {
            Element tourNode = doc.createElement("tour");
            tourNode.setAttribute("numero", String.valueOf(tourData.tourNumber));
            construireContenuEtat(doc, tourNode, combat, tourData, true);
            c.appendChild(tourNode);
        }

        // Transformation et écriture XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(new DOMSource(doc), new StreamResult(fichierCible));
    }

    /**
     * Reconstruit l'état initial (avant tirs) en annulant les variations du 1er tour
     */
    private static void construireContenuEtatInitialReconstruit(Document doc, Element parentNode, CombatGlobalData combat, RapportCombatData premierTour) {
        // --- Flotte Attaquante ---
        Element flotteAtt = doc.createElement("flotte");
        flotteAtt.setAttribute("proprio", String.valueOf(combat.attaquant.id));
        if (combat.attaquant.numFlotte > 0) {
            flotteAtt.setAttribute("numero", String.valueOf(combat.attaquant.numFlotte));
        }

        for (EntiteCombatData v : premierTour.flotteAttaquante) {
            Element vEl = doc.createElement("vaisseau");
            vEl.setAttribute("nom", v.nom);
            // Nombre initial = Nombre après tour - Variation (ex: 20 - (-3) = 23)
            vEl.setAttribute("nombre", String.valueOf(v.nombre - v.variationNombre));
            // Dégâts initiaux = Dégâts après tour - Variation dégâts
            vEl.setAttribute("encaisses", String.valueOf(v.degatsEncaisses - v.variationDegats));
            flotteAtt.appendChild(vEl);
        }
        parentNode.appendChild(flotteAtt);

        // --- Défenseur (Flotte vs Flotte) ---
        if ("FLOTTE_FLOTTE".equals(combat.typeCombat)) {
            Element flotteDef = doc.createElement("flotte");
            flotteDef.setAttribute("proprio", String.valueOf(combat.defenseur.id));
            if (combat.defenseur.numFlotte > 0) {
                flotteDef.setAttribute("numero", String.valueOf(combat.defenseur.numFlotte));
            }

            for (EntiteCombatData v : premierTour.flotteDefenseuse) {
                Element vEl = doc.createElement("vaisseau");
                vEl.setAttribute("nom", v.nom);
                vEl.setAttribute("nombre", String.valueOf(v.nombre - v.variationNombre));
                vEl.setAttribute("encaisses", String.valueOf(v.degatsEncaisses - v.variationDegats));
                flotteDef.appendChild(vEl);
            }
            parentNode.appendChild(flotteDef);

        } else if ("FLOTTE_PLANETE".equals(combat.typeCombat)) { // --- Défenseur (Flotte vs Planète) ---
            Element planete = doc.createElement("planete");
            planete.setAttribute("numero", String.valueOf(combat.numPlanete));
            if (combat.nomPlanete != null) {
                planete.setAttribute("nom", combat.nomPlanete);
            }
            planete.setAttribute("proprio", String.valueOf(combat.defenseur.id));

            if (premierTour.milice != null) {
                Element milice = doc.createElement("milice");
                milice.setAttribute("nombre", String.valueOf(premierTour.milice.nombre - premierTour.milice.variationNombre));
                planete.appendChild(milice);
            }

            for (EntiteCombatData b : premierTour.batiments) {
                Element bat = doc.createElement("batiment");
                bat.setAttribute("nom", b.nom);
                bat.setAttribute("nombre", String.valueOf(b.nombre - b.variationNombre));
                bat.setAttribute("encaisses", String.valueOf(b.degatsEncaisses - b.variationDegats));
                planete.appendChild(bat);
            }
            parentNode.appendChild(planete);
        }
    }
    private static void construireContenuEtat(Document doc, Element parentNode, CombatGlobalData combat, RapportCombatData etat, boolean inclureInfliges) {
        // --- Flotte Attaquante ---
        Element flotteAtt = doc.createElement("flotte");
        flotteAtt.setAttribute("proprio", String.valueOf(combat.attaquant.id));
        if (combat.attaquant.numFlotte > 0) {
            flotteAtt.setAttribute("numero", String.valueOf(combat.attaquant.numFlotte));
        }

        for (EntiteCombatData v : etat.flotteAttaquante) {
            Element vEl = doc.createElement("vaisseau");
            vEl.setAttribute("nom", v.nom);
            vEl.setAttribute("nombre", String.valueOf(v.nombre));
            vEl.setAttribute("encaisses", String.valueOf(v.degatsEncaisses));
            if (inclureInfliges) {
                vEl.setAttribute("infliges", String.valueOf(v.degatsInfliges));
            }
            flotteAtt.appendChild(vEl);
        }
        parentNode.appendChild(flotteAtt);

        // --- Défenseur (Flotte vs Flotte) ---
        if ("FLOTTE_FLOTTE".equals(combat.typeCombat)) {
            Element flotteDef = doc.createElement("flotte");
            flotteDef.setAttribute("proprio", String.valueOf(combat.defenseur.id));
            if (combat.defenseur.numFlotte > 0) {
                flotteDef.setAttribute("numero", String.valueOf(combat.defenseur.numFlotte));
            }

            for (EntiteCombatData v : etat.flotteDefenseuse) {
                Element vEl = doc.createElement("vaisseau");
                vEl.setAttribute("nom", v.nom);
                vEl.setAttribute("nombre", String.valueOf(v.nombre));
                vEl.setAttribute("encaisses", String.valueOf(v.degatsEncaisses));
                if (inclureInfliges) {
                    vEl.setAttribute("infliges", String.valueOf(v.degatsInfliges));
                }
                flotteDef.appendChild(vEl);
            }
            parentNode.appendChild(flotteDef);

        } else if ("FLOTTE_PLANETE".equals(combat.typeCombat)) { // --- Défenseur (Flotte vs Planète) ---
            Element planete = doc.createElement("planete");
            planete.setAttribute("numero", String.valueOf(combat.numPlanete));
            if (combat.nomPlanete != null) {
                planete.setAttribute("nom", combat.nomPlanete);
            }
            planete.setAttribute("proprio", String.valueOf(combat.defenseur.id));

            if (etat.milice != null) {
                Element milice = doc.createElement("milice");
                milice.setAttribute("nombre", String.valueOf(etat.milice.nombre));
                planete.appendChild(milice);
            }

            for (EntiteCombatData b : etat.batiments) {
                Element bat = doc.createElement("batiment");
                bat.setAttribute("nom", b.nom);
                bat.setAttribute("nombre", String.valueOf(b.nombre));
                bat.setAttribute("encaisses", String.valueOf(b.degatsEncaisses));
                planete.appendChild(bat);
            }
            parentNode.appendChild(planete);
        }
    }
}