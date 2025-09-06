// v2.00 10/04/2006
// Copyright 2006 Yannick Domenjoud All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;

import zIgzAg.utile.Mdt;

import javax.xml.transform.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

import java.util.Locale;

public class RapportTechnoXML {

	private static String version = RapportXML.version;

	private Commandant c;
	private String chemin;
	private Document document;

	public Element creerNode(String nom, String[] attribut, String[] valeur) {
		return creerNode(nom.toLowerCase(), document, attribut, valeur);
	}

	public Element creerNode(String nom, Element parent, String[] attribut,
			String[] valeur) {
		return creerNode(nom.toLowerCase(), parent, attribut, valeur, null);

	}

	public Element creerNode(String nom, Element parent, String[] attribut,
			String[] valeur, String textContent) {

		if (attribut.length != valeur.length) {
			System.out
					.println(" Erreur dans RapportXML ( creerNode), le nombre d'attribu et le nombre de valeur est diffÃ©rent ..");
			System.out.println(" Attributs : ");
			for (int i = 0; i < attribut.length; i++)
				System.out.print(attribut[i] + "-");
			System.out.println(" Valeurs: ");
			for (int i = 0; i < valeur.length; i++)
				System.out.print(valeur[i] + "-");
			System.exit(0);
		}

		Element retour = (Element) (document.createElement(nom.toLowerCase()));
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);
		if (textContent != null)
			retour.setTextContent(textContent);

		parent.appendChild(retour);

		return retour;
	}

	public Element creerNode(String nom, Element parent) {

		Element retour = (Element) (document.createElement(nom.toLowerCase()));
		parent.appendChild(retour);

		return retour;
	}

	public Element creerNode(String nom, Document parent, String[] attribut,
			String[] valeur) {
		if (attribut.length != valeur.length) {
			System.out
					.println(" Erreur dans RapportXML ( creerNode), le nombre d'attribu et le nombre de valeur est diffÃ©rent ..");
			System.out.println(" Attributs : ");
			for (int i = 0; i < attribut.length; i++)
				System.out.print(attribut[i] + "-");
			System.out.println(" Valeurs: ");
			for (int i = 0; i < valeur.length; i++)
				System.out.print(valeur[i] + "-");
			System.exit(0);
		}

		Element retour = (Element) document.createElement(nom.toLowerCase());
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);

		parent.appendChild(retour);

		return retour;

	}

	// Constructeur
	public RapportTechnoXML(Commandant commandant) {

		c = commandant;
		chemin = Chemin.RAPPORTS + Integer.toString(c.getNumero()) + "/";

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// ------------------------construction du fichier techno
			// xml--------------------------

			document = builder.newDocument();
			// Element racine <corps>
			Element corpsT = creerNode("RAPPORT", document, new String[] {
					"numTour", "version" },
					new String[] { "" + Univers.getTour(), version });

			// Les technos connnues
			String[] tech = (String[]) Mdt.fusion(
					c.listeTechnologiesNonPubliquesConnues(),
					c.listeTechnologiesPouvantEtreCherchees());
			// Transformation String[] -> Technologie[] triÃ©e
			Technologie[] tab = Univers
					.trierAlphabetiquementTechnologies(Technologie
							.transformationCode(tech));

			// Const.TECHNOLOGIE_TYPE_BATIMENT
			for (int i = 0; i < tab.length; i++) {
				Technologie t = tab[i];
				Element technor = creerNode(
						"TECHNOLOGIE",
						corpsT,
						new String[] { "code", "niv", "type", "recherche",
								"nom" },
						new String[] { t.getCode(), "" + t.getNiveau(),
								"" + t.getTypeDeTechno(),
								"" + t.getPointsDeRecherche(),
								t.getNom(Locale.FRENCH) });

				int type = t.getTypeDeTechno();
				String[] el = new String[] { "plop" };
				String[] va = new String[] { "hehe" };
				Batiment t1;
				ComposantDeVaisseau t2;
				if (type == Const.TECHNOLOGIE_TYPE_BATIMENT) {
					t1 = (Batiment) t;
					Element addtech = creerNode(
							"SPECIFICATION",
							technor,
							new String[] { "prix", "structure", "pc", "arme" },
							new String[] {
									"" + t1.getPrix(),
									t1.getPointsDeStructure() + "",
									t1.getPointsDeConstruction() + "",
									(t1.getCodeArme() != null ? t1
											.getCodeArme() : "") });
				} else if (type == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {
					t2 = (ComposantDeVaisseau) t;
					Element addtech = creerNode("SPECIFICATION", technor,
							new String[] { "prix", "type", "case" },
							new String[] { "" + t2.getPrix(), t2.getTypeArme(),
									t2.getNombreDeCases() + "" });
				}



				int[][] carac = t.getCaracteristiquesSpeciales();
				if (carac != null)
					for (int a = 0; a < carac.length; a++) {
						int ele = carac[a][0];
						int val = carac[a][1];
						Element addcarac = creerNode("CARACTERISTIQUE",
								technor, new String[] { "code", "value" },
								new String[] { "" + ele, "" + val });
					}

				if (type != Const.TECHNOLOGIE_TYPE_SIMPLE) {
					Produit p = (Produit) t;
					int[][] march = p.getMarchandises();
					if (march != null)
						for (int a = 0; a < march.length; a++) {
							Element produit = creerNode("MARCHANDISE", technor,
									new String[] { "march", "nb" },
									new String[] { "" + march[a][0],
											"" + march[a][1] });

						}
				}
				Element adddesc = creerNode("DESCRIPTION", technor,
						new String[] {}, new String[] {},
						t.getDescription(Locale.FRENCH));

			} // Fin tech

		} // Fin try
		catch (Exception e) {
			System.out.println(e + " : " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void ecrireRapportXML() {

		try {

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

			// Rapport techno
			File baseDir = new File(chemin);
			File ft = new File(baseDir, "Techno.xml");
			StreamResult resultT = new StreamResult(ft);
			DOMSource sourceT = new DOMSource(document);
			transformer.transform(sourceT, resultT);

		} catch (Exception e) {

			System.out.println(e + " : " + e.getMessage());
			e.printStackTrace();
		}

	} // Fin Ã©crire rapportXML

}
