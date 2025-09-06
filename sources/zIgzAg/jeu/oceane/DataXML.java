package zIgzAg.jeu.oceane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataXML {
    private static Document documentG;

    public static Element creerNode(String nom, String[] attribut, String[] valeur) {
        return creerNode(nom.toLowerCase(), documentG, attribut, valeur);
    }

    public static Element creerNode(String nom, Element parent, String[] attribut, String[] valeur) {
        return creerNode(nom.toLowerCase(), parent, attribut, valeur, null);

    }

    public static Element creerNode(String nom, Element parent, String[] attribut,
                                    String[] valeur, String textContent) {
        if (attribut.length != valeur.length) {
            System.out.println(" Erreur dans RapportXML ( creerNode " + nom + "), le nombre d'attributs et le nombre de valeur est différent ..");
            System.out.println(" Attributs : ");
            for (int i = 0; i < attribut.length; i++)
                System.out.print(attribut[i] + "-");
            System.out.println(" Valeurs: ");
            for (int i = 0; i < valeur.length; i++)
                System.out.print(valeur[i] + "-");
            System.exit(0);
        }

        Element retour = (Element) (documentG.createElement(nom.toLowerCase()));
        for (int i = 0; i < attribut.length; i++)
            retour.setAttribute(attribut[i], valeur[i]);
        if (textContent != null)
            retour.setTextContent(textContent);

        parent.appendChild(retour);

        return retour;
    }

    public static Element creerNode(String nom, Element parent) {

        Element retour = (Element) (documentG.createElement(nom.toLowerCase()));
        parent.appendChild(retour);

        return retour;
    }

    public static Element creerNode(String nom, Document parent, String[] attribut,
                                    String[] valeur) {
        if (attribut.length != valeur.length) {
            System.out
                    .println(" Erreur dans RapportXML ( creerNode), le nombre d'attributs et le nombre de valeur est différent ..");
            System.out.println(" Attributs : ");
            for (int i = 0; i < attribut.length; i++)
                System.out.print(attribut[i] + "-");
            System.out.println(" Valeurs: ");
            for (int i = 0; i < valeur.length; i++)
                System.out.print(valeur[i] + "-");
            System.exit(0);
        }

        Element retour = (Element) documentG.createElement(nom.toLowerCase());
        for (int i = 0; i < attribut.length; i++)
            retour.setAttribute(attribut[i], valeur[i]);

        parent.appendChild(retour);

        return retour;

    }


    public static void main(String[] args) {
        Locale l = Locale.FRENCH;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            // ------------------------construction du fichier general
            // xml--------------------------
            documentG = builder.newDocument();

            // Element racine <corps>
            Element corps = creerNode("DATA", new String[]{}, new String[]{});


            Element tech = creerNode("TECHNOLOGIES", corps, new String[]{}, new String[]{});

            Technologie[] tab = Univers.trierAlphabetiquementTechnologies(Univers.getListeTechnologies());

            // Const.TECHNOLOGIE_TYPE_BATIMENT
            for (int i = 0; i < tab.length; i++) {
                Technologie t = tab[i];
                if (t == null) {
                    System.out.println(i);
                    continue;
                }
                Element technor = creerNode(
                        "T",
                        tech,
                        new String[]{"base", "code", "niv", "type", "recherche", "nom"},
                        new String[]{
                                t.getCodeBase(),
                                t.getCode(),
                                "" + t.getNiveau(),
                                "" + t.getTypeDeTechno(),
                                "" + t.getPointsDeRecherche(),
                                t.getNom(Locale.FRENCH),
                        });

                int type = t.getTypeDeTechno();
                if (type == Const.TECHNOLOGIE_TYPE_BATIMENT) {
                    Batiment t1 = (Batiment) t;
                    Element addtech = creerNode(
                            "SPECIFICATION",
                            technor,
                            new String[]{"prix", "structure", "pc", "min", "arme"},
                            new String[]{
                                    "" + t1.getPrix(),
                                    t1.getPointsDeStructure() + "",
                                    t1.getPointsDeConstruction() + "",
                                    t1.getMineraiNecessaire() + "",
                                    (t1.getCodeArme() != null ? t1
                                            .getCodeArme() : "")});
                } else if (type == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {
                    ComposantDeVaisseau t2 = (ComposantDeVaisseau) t;
                    Element addtech = creerNode("SPECIFICATION", technor,
                            new String[]{"prix", "type", "case", "min"},
                            new String[]{"" + t2.getPrix(), t2.getTypeArme(),
                                    t2.getNombreDeCases() + "", t2.getMineraiNecessaire() + ""});
                }


                int[][] carac = t.getCaracteristiquesSpeciales();
                if (carac != null)
                    for (int a = 0; a < carac.length; a++) {
                        int ele = carac[a][0];
                        int val = carac[a][1];
                        Element addcarac = creerNode("CARACTERISTIQUE",
                                technor, new String[]{"code", "value"},
                                new String[]{"" + ele, "" + val});
                    }

                if (type != Const.TECHNOLOGIE_TYPE_SIMPLE) {
                    Produit p = (Produit) t;
                    int[][] march = p.getMarchandises();
                    if (march != null)
                        for (int a = 0; a < march.length; a++) {
                            creerNode("MARCHANDISE", technor,
                                    new String[]{"code", "nb"},
                                    new String[]{"" + march[a][0], "" + march[a][1]});

                        }
                }
                creerNode("DESCRIPTION", technor, new String[]{}, new String[]{}, t.getDescription(Locale.FRENCH));

                String[] parents = t.getParents();
                if (parents != null) {
                    for (String parent : t.getParents()) {
                        Technologie p = Univers.getTechnologie(parent);
                        if (p != null) {
                            creerNode("PARENT", technor, new String[]{"code"}, new String[]{p.getCode()});
                        } else {
                            System.out.println("null parent for " + t.getNomComplet(l));
                        }
                    }
                }
            }

            // autres
            Element marchandises = creerNode("MARCHANDISES", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.MARCHANDISES.length; i++) {
                String name = Messages.MARCHANDISES[i];
                creerNode("M", marchandises, new String[]{"code", "nom"}, new String[]{i + "", name});
            }

            Element politiques = creerNode("POLITIQUES", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.POLITIQUES.length; i++) {
                String name = Messages.POLITIQUES[i];
                creerNode("P", politiques, new String[]{"code", "nom"}, new String[]{i + "", name});
            }

            Element caractsBatiments = creerNode("CARACTERISTIQUES_BATIMENT", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.CARAC_SPECIALES_BATIMENTS.length; i++) {
                String name = Messages.CARAC_SPECIALES_BATIMENTS[i];
                creerNode("C", caractsBatiments, new String[]{"code", "nom"}, new String[]{i + "", name});
            }

            Element caractsComposants = creerNode("CARACTERISTIQUES_COMPOSANT", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.CARAC_SPECIALES_COMPOSANTS.length; i++) {
                String name = Messages.CARAC_SPECIALES_COMPOSANTS[i];
                creerNode("C", caractsComposants, new String[]{"code", "nom"}, new String[]{i + "", name});
            }

            Element competences = creerNode("leader_competences", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.COMPETENCES_LEADER.length; i++) {
                String name = Messages.COMPETENCES_LEADER[i];
                creerNode("C", competences, new String[]{"code", "nom"}, new String[]{i + "", name});
            }

            Element races = creerNode("races", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Messages.RACES.length; i++) {
                String name = Messages.RACES[i];
                creerNode("r", races,
                        new String[]{"code", "nom", "couleur", "rad_min", "rad_max", "temp_min", "temp_max", "grav_min", "grav_max"},
                        new String[]{i + "", name, Rapport.COULEURS_RACES[i],
                                Const.HABITAT_RADIATION[i][0] + "", Const.HABITAT_RADIATION[i][1] + "",
                                Const.HABITAT_TEMPERATURE[i][0] + "", Const.HABITAT_TEMPERATURE[i][1] + "",
                                Const.HABITAT_GRAVITE[i][0] + "", Const.HABITAT_GRAVITE[i][1] + "",
                        });
            }

            // vaisseaux publiques
            PlanDeVaisseau[] vlist = Univers.listePlansDeVaisseauxPublics();
            Element vlistNode = creerNode("PLANPUBLIC", corps, new String[]{}, new String[]{});
            for (int i = 0; i < vlist.length; i++) {
                PlanDeVaisseau pdv = vlist[i];
                Element p = creerNode("P", vlistNode,
                        new String[]{"nom", "concepteur", "marque", "tour", "taille", "vitesse", "pc", "centaures", "minerai", "as", "ap", "royalties"},
                        new String[]{pdv.getNom(), pdv.getNomConcepteurBis(l), pdv.getMarque(l), pdv.getTourDeCration() + "",
                                pdv.getTaille() + "", pdv.getCapaciteMouvement(false) + "", pdv.getPointsDeConstructions() + "", pdv.getPrix() + "",
                                pdv.getMineraiNecessaire() + "", pdv.getForceSpatiale() + "", pdv.getForcePlanetaire() + "",
                                pdv.getRoyalties() + ""
                        });
                ComposantDeVaisseau[] c = pdv.getComposants();
                HashMap<ComposantDeVaisseau, Integer> hm = new HashMap<>(c.length);
                for (int x = 0; x < c.length; x++) {
                    Integer o = hm.get(c[x]);
                    if (o == null)
                        hm.put(c[x], 1);
                    else
                        hm.put(c[x], o + 1);
                }
                Map.Entry<ComposantDeVaisseau, Integer>[] m = hm.entrySet().toArray(new Map.Entry[0]);

                for (int z = 0; z < m.length; z++) {
                    int nb = m[z].getValue();
                    ComposantDeVaisseau co = m[z].getKey();
                    creerNode("comp", p,
                            new String[]{"nb", "code"},
                            new String[]{
                                    Integer.toString(nb),
                                    co.getCode()
                            }
                    );
                }

            }


            // modificateurs
            Element stabDistanceNode = creerNode("MODIFICATEUR_STABILITE", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Const.MODIFICATEUR_STABILITE_CAPITALE.length; i++) {
                int[] m = Const.MODIFICATEUR_STABILITE_CAPITALE[i];
                creerNode("M", stabDistanceNode,
                        new String[]{"distance", "modif"},
                        new String[]{m[0] + "", m[1] + ""});

            }
            Element stabTaxationNode = creerNode("MODIFICATEUR_STABILITE_TAXATION", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Const.MODIFICATEUR_STABILITE_TAXATION.length; i++) {
                int m = Const.MODIFICATEUR_STABILITE_TAXATION[i];
                creerNode("M", stabTaxationNode,
                        new String[]{"taxation", "modif"},
                        new String[]{(i + 1) + "", m + ""});

            }


            // PDV
            Element stabTailleVaisseauNode = creerNode("TAILLE_VAISSEAUX", corps, new String[]{}, new String[]{});
            for (int i = 0; i < Const.TAILLE_VAISSEAUX.length; i++) {
                int[] m = Const.TAILLE_VAISSEAUX[i];
                creerNode("T", stabTailleVaisseauNode,
                        new String[]{"taille", "min", "max", "vitesse"},
                        new String[]{(i + 1) + "", m[0] + "", m[1] + "", m[2] + ""});

            }

            Element listeJoueurs = creerNode("COMMANDANTS", corps, new String[]{}, new String[]{});
            Commandant[] cl = Univers.getListeCommandantsHumains();
            for (int i = 0; i < cl.length; i++) {
                Commandant joueur = cl[i];
                creerNode("C", listeJoueurs,
                        new String[]{"num", "nom", "race", "pui", "pla", "rep"},
                        new String[]{joueur.getNumero() + "", joueur.getNom(), joueur.getRace() + "", joueur.getPuissance() + "", joueur.getNombrePlanetesPossedees() + "", joueur.getReputation() + ""}
                );
            }


            ecrireDataXML();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    public static void ecrireDataXML() {

        try {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            // Rapport general
            File baseDir = new File(Chemin.STATS);
            File fg = new File(baseDir, "data.xml");
            System.out.println("Ecriture du fichier data.xml dans " + fg.getAbsolutePath());
            StreamResult resultG = new StreamResult(fg);
            DOMSource sourceG = new DOMSource(documentG);
            transformer.transform(sourceG, resultG);

        } catch (Exception e) {

            System.out.println(e + " : " + e.getMessage());
            e.printStackTrace();
        }

    } // Fin écrire data.xml
}
