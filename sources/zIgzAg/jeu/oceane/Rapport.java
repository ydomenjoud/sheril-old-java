// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import zIgzAg.html.BaliseHTML;
import zIgzAg.html.DocumentHTML;
import zIgzAg.utile.Copie;

public class Rapport {

	public static final String[] cC = { "#000000", "#FFB400", "#008080",
			"#FBF7AF", "#967DFF", "#EE82EE", "#00F1AF", "#EB9F96", "#6F9EC3",
			"#ffdead" };

	private static final Map<String, Integer> COLOR_INDEX_MAP = new HashMap<>();
	static {
		for (int i = 0; i < cC.length; i++) {
			COLOR_INDEX_MAP.put(cC[i], i);
		}
	}


	// noir,jaune,bleu foncé(couleur des liens),rouge,violet clair(couleur
	// /pop),violet,vert clair (neutre),saumon,
	// vert foncé,
	private static final String nomRepImage = "/images";

	private static final String cI = ".." + nomRepImage + "/";


	// le bg pour les technologies.
	public static final String[] COULEURS_RACES = { "#CC00FF", "#0066CC",
			"#FFCC00", "#CC0033", "#009933", "#777777","#CC6600" };


	public static final String[] COULEURS_GALAXIES = { "#be43fe" };

	public static final String PRINCIPAL = "principal.htm";

	private static final String[][] attributsBaseHeadMeta1 = {
			{ BaliseHTML.HTTP_EQUIV, "Content-Type" },
			{ BaliseHTML.CONTENT, "text/html; charset=UTF8" } };

	private static final String[][] attributsBaseHeadMeta2 = {
			{ BaliseHTML.NAME, "Author" }, { BaliseHTML.CONTENT, "Myst" } };

	private static final String[][] attributsBaseHeadMeta3 = {
			{ BaliseHTML.HREF, "../images/rapport.css" },
			{ BaliseHTML.TYPE, "text/css" }, { BaliseHTML.REL, "stylesheet" } };

	private static final String[][] attributsBaseTable = {
			{ BaliseHTML.BORDER, "1" }, { BaliseHTML.CELLSPACING, "0" },
			{ BaliseHTML.CELLPADING, "1" } };

	private static final BaliseHTML BASE = new BaliseHTML(BaliseHTML.HTML);

	private static final BaliseHTML BODY = new BaliseHTML(BaliseHTML.BODY);

	private static final BaliseHTML DIV = new BaliseHTML(BaliseHTML.DIV,
			BaliseHTML.ALIGN, BaliseHTML.CENTER);

	private static final BaliseHTML TABLE = new BaliseHTML(BaliseHTML.TABLE,
			attributsBaseTable);

	private static BaliseHTML HEAD;

	private static final String LIEN_RESUME_SYSTEME = "resumeS";

	private static final String LIEN_RESUME_FLOTTE = "resumeF";

	private static final String LIEN_RESUME_TECHNOLOGIE = "resumeTechno";

	private static final String LIEN_ALLIANCES = "alliances";

	private static final String LIEN_HEROS = "heros";

	private static final String LIEN_DETECTION_FLOTTES = "detectF";

	private static final String LIEN_DETECTION_SYSTEMES = "detectS";

	private static final String LIEN_GOUVERNEURS = "gouverneurs";

	private static final String LIEN_PACTES_NON_AGRESSION = "pactes";

	private static final String LIEN_MESSAGES = "mess";

	private static final String DETAIL_FLOTTE = "detailF.htm";

	private static final String DETAIL_TECHNOLOGIES = "detailT.htm";

	public static final String DETAIL_COMBAT = "combat.htm";

	private static HashMap BALISES_RACES;

	private final temp/* ArrayList */listeDocuments;

	private final Commandant c;

	private final String chemin;

	private final List listeLiens;

	// méthodes pour gérer la balise de base et des méthodes standardes.

	public static BaliseHTML getHead() {
		if (HEAD == null) {
			HEAD = new BaliseHTML(BaliseHTML.HEAD);
			BaliseHTML meta1 = new BaliseHTML(BaliseHTML.META,
					attributsBaseHeadMeta1);
			BaliseHTML meta2 = new BaliseHTML(BaliseHTML.META,
					attributsBaseHeadMeta2);
			BaliseHTML meta3 = new BaliseHTML(BaliseHTML.LINK,
					attributsBaseHeadMeta3);

            BaliseHTML script = new BaliseHTML(
                    "script",
                    """
						const marchandisesBonusList = [
						/* "produits alimentaires" */		"+5% sur le taux d'augmentation de la population.",
						/* "dechets" */					 	"Ni bonus ni malus",
						/* "articles de luxe" */			"+10% sur les revenus.",
						/* "holofilms et hololivres" */		"+1% de stabilité.",
						/* "Systèmes de guidage" */			"Ni bonus ni malus",
						/* "médicaments" */				 	"+10% sur le taux d'augmentation de la population.",
						/* "logiciels" */				 	"+25% en recherche technologique.",
						/* "robots" */				 		"+5 points de construction.",
						/* "composants électroniques" */	"+25% en service spéciaux et contre-espionnage",
						/* "armement et explosifs" */		"-1% de stab , +50% de défense (sans toutefois dépasser la population)",
						/* "unité energétique" */			"-50% entretien flottes stationnées sur le système",
						/* "pièces industrielles" */		"-10% entretien système",
						/* "métaux précieux" */			 	"+5% sur les revenus.",
						/* "tixium" */			 			"Ni bonus ni malus",
						/* "lixiam" */			 			"Ni bonus ni malus",
						/* "oxole" */			 			"Ni bonus ni malus",
						];

                                
                                document.addEventListener("DOMContentLoaded", function () {
									document.querySelectorAll('[data-marchandise]').forEach(el =>
										el.dataset.tooltip = marchandisesBonusList[el.dataset.marchandise]
									);
                                
                                    document.querySelectorAll("table").forEach(table => {
                                        const input = document.createElement("input");
                                        input.setAttribute("placeholder", "");
                                        input.addEventListener("input", e => {
                                            const filter = e.target.value.toLowerCase();
                                            table.querySelectorAll("tbody tr").forEach(row => {
                                                const text = row.textContent.toLowerCase();
                                                const rowContentHTML = row.innerHTML.toLowerCase();
                                                const isTitle = ['titre_table','titre_caption'].some(l => rowContentHTML.indexOf(l) > -1);
                                                if(isTitle) {
                                                    row.style.display = "";
                                                } else {
                                                    row.style.display = text.includes(filter) ? "" : "none";
                                                }
                                            });
                                        });
                                        table.querySelector("tr:first-child td, tr:first-child th").append(input);
                                    });

									// On cible les en-têtes (th)
									const headers = document.querySelectorAll("table.sortable th");
								
										headers.forEach((header, index) => {
										 if(header.dataset.nosort === "1"){
										  return;
										 }
								
										header.style.cursor = "pointer";
										header.addEventListener("click", () => {
											const table = header.closest("table");
											const tbody = table.querySelector("tbody");
											const rows = Array.from(tbody.querySelectorAll("tr:not(:has(th))"));
											// on reset le tri de toutes les autres colonnes
										  headers.forEach(h => {
												  if (h !== header) {
													  delete h.dataset.sortOrder;
												  }
											  });
											// Déterminer la direction du tri en fonction de ce qu'il y avait avant
											// soit y'a rien ou asc , on passe à desc
											const isAscending = header.dataset.sortOrder === "desc";
											header.dataset.sortOrder = isAscending ? "asc" : "desc"; // on inverse
								
											const sortedRows = rows.sort((a, b) => {
												// Fonction pour extraire uniquement le nombre principal
												const extractValue = (row) => {
													const cell = row.children[index];
													// On clone pour ne pas toucher au DOM original
													const temp = cell.cloneNode(true);
													// On supprime les spans de progression pour ne garder que le texte brut
													temp.querySelectorAll('span, font, div, sup').forEach(s => s.remove());
								
													// On nettoie le texte : on enlève les espaces, les &nbsp; et on remplace la virgule par un point
													let text = temp.textContent.trim()
														.replace(/[^0-9]/g, '')      // Enlève espaces standards
														.replace(/\\s+/g, '')       // Enlève espaces standards
														.replace(/\\u00a0/g, '')    // Enlève les &nbsp;
														.replace(',', '.');        // Séparateur décimal
								
													return parseFloat(text) || 0;
												};
								
												const valA = extractValue(a);
												const valB = extractValue(b);
								
												return isAscending ? valA - valB : valB - valA;
											});
								
											// Ré-insertion des lignes triées
											sortedRows.forEach(row => tbody.appendChild(row));
								
										});
									});
								});

						"""
            );

			HEAD.ajout(meta1).ajout(meta2).ajout(meta3).ajout(script);
		}
		return (BaliseHTML) HEAD.clone();
	}

	public static BaliseHTML getHead(String titre) {
		return getHead().ajout(new BaliseHTML(BaliseHTML.TITLE, titre));
	}

	public static BaliseHTML getBody() {
		return (BaliseHTML) BODY.clone();
	}

	public static BaliseHTML getDiv() {
		return (BaliseHTML) DIV.clone();
	}

	public static BaliseHTML getTable() {
		return (BaliseHTML) TABLE.clone();
	}

	public static BaliseHTML getTable(String className) {
		return ((BaliseHTML) TABLE.clone()).ajout("class", className);
	}

	public static BaliseHTML getTH() {
		return getTH(null, null);
	}
	public static BaliseHTML getTH(String align, String colspan) {
		BaliseHTML retour = new BaliseHTML("th");
		if (align != null)
			retour.ajout(BaliseHTML.ALIGN, align);
		if (colspan != null)
			retour.ajout(BaliseHTML.COLSPAN, colspan);
		return retour;
	}

	public static BaliseHTML getTD(String align, String colspan) {
		BaliseHTML retour = new BaliseHTML(BaliseHTML.TD);
		if (align != null)
			retour.ajout(BaliseHTML.ALIGN, align);
		if (colspan != null)
			retour.ajout(BaliseHTML.COLSPAN, colspan);
		return retour;
	}

	public static BaliseHTML getTD(String align, String colspan, String color) {
		BaliseHTML retour = new BaliseHTML(BaliseHTML.TD);
		if (align != null)
			retour.ajout(BaliseHTML.ALIGN, align);
		if (colspan != null)
			retour.ajout(BaliseHTML.COLSPAN, colspan);
		if (color != null)
			retour.ajout(BaliseHTML.BGCOLOR, color);
		return retour;
	}
	public static BaliseHTML getSpan() {
		return new BaliseHTML(BaliseHTML.SPAN);
	}
	public static BaliseHTML getSpan(String className) {
		BaliseHTML retour = new BaliseHTML(BaliseHTML.SPAN);
		if (className != null)
			retour.ajout(BaliseHTML.CLASS, className);
		return retour;
	}

	public static BaliseHTML getFont(String color, String size) {
		BaliseHTML retour = new BaliseHTML(BaliseHTML.SPAN);
		List<String> c = new ArrayList<>();
		if (color != null) {
			int index = COLOR_INDEX_MAP.getOrDefault(color, -1);
			if(index >= 0) {
				c.add("c" + index);
			}
			// fallback
			else {
				retour.ajout("style", "color: " + color);
			}
		}
		if (size != null) {
			c.add("s" + size);
		}
		retour.ajout(BaliseHTML.CLASS, String.join(" ", c));
		return retour;
	}

	public static BaliseHTML getText(String texte) {
		return new BaliseHTML("", texte);
	}

	/**
	 * public static String getText(String texte){ return texte; }
	 */
	public static BaliseHTML getTextI(String texte) {
		return getText(texte);
	}

	/**
	 * Balise vide
	 */
	public BaliseHTML vide() {
		return new BaliseHTML("B", "&nbsp;");
	}

	public static BaliseHTML getImage(String chemin, int hauteur, int largeur) {
		BaliseHTML image = new BaliseHTML(BaliseHTML.IMG, BaliseHTML.SRC, chemin);
		if (hauteur != 0)
			image.ajout(BaliseHTML.HEIGHT, Integer.toString(hauteur));
		if (largeur != 0)
			image.ajout(BaliseHTML.WIDTH, Integer.toString(largeur));
		return image.ajout(BaliseHTML.BORDER, "0");
	}

	public static BaliseHTML getABorne(String nom) {
		return new BaliseHTML(BaliseHTML.A, BaliseHTML.NAME, nom);
	}

	public static BaliseHTML getALienI(String lien) {
		return new BaliseHTML(BaliseHTML.A, BaliseHTML.HREF, "#" + lien);
	}

	public static BaliseHTML getALienM(String lien) {
		return new BaliseHTML(BaliseHTML.A, BaliseHTML.HREF, "mailto:" + lien);
	}

	public static BaliseHTML getALienE(String lien) {
		return new BaliseHTML(BaliseHTML.A, BaliseHTML.HREF, lien);
	}

	public static BaliseHTML getP() {
		return new BaliseHTML(BaliseHTML.P, BaliseHTML.ALIGN, BaliseHTML.CENTER);
	}

	public static BaliseHTML sautP() {
		return new BaliseHTML(BaliseHTML.P, "&nbsp;");
	}

	// méthodes utililitaires

	public static String getCheminEtoile(int num) {
		return cI + "etoile" + (num + 1) + ".gif";
	}

	public static String getCheminPlanete(int num) {
		return cI + "Planete" + (num + 1) + ".gif";
	}

	public static String getNomDocHTMLPlanetes(Position p, int numC) {
		return p.toString() + numC + "P.htm";
	}

	public static String getNomDocHTMLAlliance(Alliance a) {
		return a.getNumero() + "A.htm";
	}

	public static String getLienPosteCo(Position p, int numC) {
		return p.toString() + numC + "PO.htm";
	}

	public static String getLienFlotte(int num) {
		return "FLO" + num;
	}

	public BaliseHTML getRace(int race) {
		return getSpanRace(race).setTexteContenu(
				Utile.maj(Univers.getMessage("RACES", race, c.getLocale())));
	}

	public BaliseHTML getTitreSection(String title){
		return getTitreSection().ajout(title);
	}
	public BaliseHTML getTitreSection() {
		return new BaliseHTML(BaliseHTML.SPAN, BaliseHTML.CLASS, "titre_section");
	}

	public BaliseHTML getTitreTable() {
		return new BaliseHTML(BaliseHTML.SPAN, BaliseHTML.CLASS, "titre_table");
	}

	public static BaliseHTML getTitreCaption() {
		return new BaliseHTML(BaliseHTML.SPAN, BaliseHTML.CLASS, "titre_caption");
	}

	public static BaliseHTML getRace(int race, Locale l) {
		// Ca ne marche qu'avec une seule Locale!
		if (BALISES_RACES == null)
			BALISES_RACES = new HashMap();
		Integer cle = Integer.valueOf(race);
		Object o = BALISES_RACES.get(cle);
		if (o == null) {
			BALISES_RACES.put(
					cle,
					getCouleurRace(race).ajout(
							getText(Utile.maj(Univers.getMessage("RACES", race,
									l)))));
			return (BaliseHTML) BALISES_RACES.get(cle);
		}
		return (BaliseHTML) o;
	}

	public BaliseHTML getSpanRace(int race) {
		if( race == 6 ){
			 getFont(COULEURS_RACES[race], null);
		}
		BaliseHTML rtr = new BaliseHTML(BaliseHTML.SPAN, BaliseHTML.CLASS,
				"race" + race);
		return rtr;
	}

	public static BaliseHTML getCouleurRace(int race) {
		return getFont(COULEURS_RACES[race], null);
	}

	public static DocumentHTML getDocument(String chemin, String titre,
			BaliseHTML b) {
		return new DocumentHTML(chemin, ((BaliseHTML) BASE.clone()).ajout(
				getHead(titre)).ajout(b));
	}

	public static DocumentHTML getDocumentMenu(String chemin, BaliseHTML b) {
		return new DocumentHTML(
				chemin,
				((BaliseHTML) BASE.clone())
						.ajout(getHead("")
								.setTexteContenu(
										"<script LANGUAGE=\"JavaScript\"><!--document.onmouseover = doDocumentOnMouseOver;"
												+ "document.onmouseout = doDocumentOnMouseOut;\n\nfunction doDocumentOnMouseOver() {"
												+ "var eSrc = window.event.srcElement ; if (eSrc.className == \"item\") {"
												+ "window.event.srcElement.className = \"highlight\"; }}\n\n"
												+ "function doDocumentOnMouseOut() {var eSrc = window.event.srcElement ;"
												+ "if (eSrc.className == \"highlight\") { window.event.srcElement.className = \"item\";"
												+ "} }\n\nvar bV=parseInt(navigator.appVersion);NS4=(document.layers) ? true : false;"
												+ "IE4=((document.all)&&(bV>=4)) ? true : false;DOM=(!document.layers && !document.all && bV>=4) ?"
												+ " true : false;capable = (NS4 || IE4 || DOM) ? true : false;\n\nfunction expandIt(){return}"
												+ "function expandAll(){return} //--></script><script LANGUAGE=\"JavaScript1.2\" src=\"menu2.js\">"
												+ "</script><base target=\"p\">"))
						.ajout(b));
	}

	// méthodes utilisées au cours du tour de jeu.

	public static String lienRapportEspion(Position p) {
		return "./" + p.toString() + "RE.htm";
	}

	// méthodes statiques "hors-rapport"

	public static void ecrireMessagesSystemes() {
		Rapport r = new Rapport(new Commandant());
		BaliseHTML b = r.ecrireMessages(Univers.getMessagesErreurs(), Const.MESSAGE_TYPE_SYSTEME);
		r.listeDocuments.add(getDocument(
				Chemin.MJ + "syst" + Univers.getTour()
						+ ".htm", "Erreurs système", getBody().ajout(b)));
		BaliseHTML b2 = r.ecrireMessages(Univers.getMessagesEvenements(),
				Const.MESSAGE_TYPE_COMMANDANT);
		r.listeDocuments.add(getDocument(Chemin.STATS + "evt.htm",
				"Evenements", getBody().ajout(b2)));

		BaliseHTML b3 = r.ecrireMessages(Univers.getMessagesArticles(),
				Const.MESSAGE_TYPE_COMMANDANT);
		r.listeDocuments.add(getDocument(Chemin.MJ + "articles.htm",
				"Articles", getBody().ajout(b3)));

		ecrireRapportCombat(Locale.FRENCH);

		String[] t = (String[]) Univers.getMessageRapport("PLANS_DE_VAISSEAUX",
				Locale.FRENCH);
		BaliseHTML b4 = getPlansDeVaisseaux(Univers.listePlansDeVaisseaux(), t,
				Locale.FRENCH);
		r.listeDocuments.add(getDocument(
				Chemin.MJ + "plans" + Univers.getTour()
						+ ".htm", "Plans", getBody().ajout(b4)));

		BaliseHTML div = getDiv();
		BaliseHTML TD_1 = getTD(BaliseHTML.CENTER, BaliseHTML.T_6).ajout(
				getFont(cC[3], BaliseHTML.T_5).ajout(getText(null)));
		BaliseHTML TD_2 = getTD(BaliseHTML.CENTER, null).ajout(getText(null));
		BaliseHTML TD_3 = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[5], null).ajout(getText(null)));
		BaliseHTML TD_4 = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(null)));
		BaliseHTML TD_5 = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(getText(null)));
		BaliseHTML TD_6 = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(getText(null)));

		String N_TD_1 = "TD_1";
		String N_TD_2 = "TD_2";
		String N_TD_3 = "TD_3";
		String N_TD_4 = "TD_4";
		String N_TD_5 = "TD_5";
		String N_TD_6 = "TD_6";

		BaliseHTML.ajouterModele(N_TD_1, TD_1);
		BaliseHTML.ajouterModele(N_TD_2, TD_2);
		BaliseHTML.ajouterModele(N_TD_3, TD_3);
		BaliseHTML.ajouterModele(N_TD_4, TD_4);
		BaliseHTML.ajouterModele(N_TD_5, TD_5);
		BaliseHTML.ajouterModele(N_TD_6, TD_6);
		String[] races_d = new String[Const.NB_RACES];
		for (int i = 0; i < Const.NB_RACES; i++)
			races_d[i] = getRace(i, Locale.FRENCH).toString();

		for (int i = 0; i < Const.NB_GALAXIES; i++)
			for (int j = 1; j < Const.NB_SECTEURS + 1; j++) {
				Systeme[] sys = Univers.listeSystemes(Univers
						.listePositionsSystemesParSecteur(i, j));

				BaliseHTML[][] a = new BaliseHTML[sys.length + 1][8];
				a[0][0] = new BaliseHTML(N_TD_1, "Secteur "
						+ j + " de la galaxie "
						+ Messages.NOMS_GALAXIES[i], true);

				for (int m = 0; m < sys.length; m++) {

					a[m + 1][0] = new BaliseHTML(N_TD_2, sys[m].getPosition()
							.getDescription(), true);

					a[m + 1][1] = new BaliseHTML(N_TD_3, sys[m].getNom(), true);

					a[m + 1][2] = new BaliseHTML(N_TD_2,
							Integer.toString(sys[m].getPopulation(-1)), true);

					a[m + 1][3] = new BaliseHTML(N_TD_4,
							Integer.toString(sys[m].getPopulationMaximale(-1)),
							true);

					a[m + 1][4] = new BaliseHTML(
							N_TD_5,
							Commandant.getListeCommandants(sys[m].getProprios()),
							true);

					String racesPresentes = null;
					for (int k = 0; k < Const.NB_RACES; k++) {
						if (sys[m].populationPresente(-1, k)) {
							String d = races_d[k] +  "( "  + sys[m].getPopulationDeRace(-1, k) + ')';
							if (racesPresentes == null) {
								racesPresentes = d;
							} else {
								racesPresentes += d;
							}
						}
					}
					a[m + 1][5] = new BaliseHTML(N_TD_5, racesPresentes, true);
					a[m + 1][6] = new BaliseHTML(N_TD_6,
							Integer.toString(sys[m].getNombrePlanetes()), true);

					sys[m] = null;

				}

				div.ajout(DocumentHTML.creerTable(getTable(), a))
						.ajout(sautP());

			}
		r.listeDocuments.add(getDocument(
				Chemin.MJ + "sys" + Univers.getTour()
						+ ".htm", "Systemes", getBody().ajout(div)));

		/*
		 * Alliance[] al=Univers.getListeAlliances()
		 *
		 *
		 * BaliseHTML racine=getDiv(); if(p.length>0){ BaliseHTML
		 * lien=getABorne("ALLI");
		 * ajouterLienPrincipal(getALienE(PRINCIPAL+"#ALLI"
		 * ).ajout(getText("Alliances")));
		 *
		 * racine.ajout(lien);
		 *
		 * String[]
		 * t=(String[])Univers.getMessageRapport("ALLIANCES",c.getLocale());
		 * String[]
		 * t2=(String[])Univers.getMessageRapport("ALLIANCES_DETAIL",c.getLocale
		 * ()); String
		 * t3=(String)Univers.getMessageRapport("RETOUR_PRINCIPAL",c.
		 * getLocale());
		 * racine.ajout(getFont(cC[3],"4").ajout(getABorne(LIEN_ALLIANCES
		 * ).ajout(getText(t[0]))).ajout(sautP())); BaliseHTML[][] a=new
		 * BaliseHTML[p.length+1][7]; for(int i=0;i<7;i++)
		 * a[0][i]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getFont(cC[4],null).ajout(getTextI(t[i+1])));
		 * for(int i=0;i<p.length;i++){ Alliance b=Univers.getAlliance(p[i]);
		 * Commandant[] lc=b.getAdherents();
		 * if((!b.estSecrete())||(b.estDemocratique
		 * ())||(b.estDirigeePar(c.getNumero())))
		 * a[i+1][0]=getTD(BaliseHTML.CENTER
		 * ,null).ajout(getALienE(getNomDocHTMLAlliance
		 * (b)).ajout(getFont(cC[1],null).ajout( getText(b.getNom())))); else
		 * a[i
		 * +1][0]=getTD(BaliseHTML.CENTER,null).ajout(getFont(cC[1],null).ajout
		 * (getText(b.getNom())));
		 * a[i+1][1]=getTD(BaliseHTML.CENTER,null).ajout(
		 * getText(Utile.maj(b.getDescriptionType(c.getLocale()))));
		 * a[i+1][2]=getTD
		 * (BaliseHTML.CENTER,null).ajout(getText(b.getDescriptionConcepteur
		 * (c.getLocale())));
		 * a[i+1][3]=getTD(BaliseHTML.CENTER,null).ajout(getText
		 * (b.getDescriptionDirigeant(c.getLocale())));
		 * a[i+1][4]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getText(Float.toString(Utile
		 * .a1D(b.getDroitsEntree()))));
		 * a[i+1][5]=getTD(BaliseHTML.CENTER,null).ajout(getText((b.estSecrete()
		 * ? Univers.getMessage("OUI_NON",0,c.getLocale()) :
		 * Univers.getMessage("OUI_NON",1,c.getLocale()) )));
		 * if((!b.estSecrete()
		 * )||(b.estDemocratique())||(b.estDirigeePar(c.getNumero()))){
		 * a[i+1][6]=getTD(BaliseHTML.CENTER,null).ajout(getText(Commandant.
		 * getListeCommandants(lc)));
		 *
		 * BaliseHTML[][] d=new BaliseHTML[lc.length+1][8]; for(int j=0;j<8;j++)
		 * d
		 * [0][j]=getTD(BaliseHTML.CENTER,null).ajout(getFont(cC[4],null).ajout(
		 * getTextI(t2[j+1]))); for(int j=0;j<lc.length;j++){
		 * d[j+1][0]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getText(lc[j].getNom()));
		 * d[j+1][1]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getALienM(lc[j].getAdresseElectronique()).ajout(
		 * getText(lc[j].getAdresseElectronique())));
		 * d[j+1][2]=getTD(BaliseHTML.
		 * CENTER,null).ajout(getRace(lc[j].getRace()));
		 * d[j+1][3]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getText(Integer.toString(lc[j].getPuissance())));
		 * d
		 * [j+1][4]=getTD(BaliseHTML.CENTER,null).ajout(getText(Integer.toString
		 * (lc[j].getNombrePlanetesPossedees())));
		 * d[j+1][5]=getTD(BaliseHTML.CENTER
		 * ,null).ajout(getText(Utile.maj(lc[j].getGrade())));
		 * d[j+1][6]=getTD(BaliseHTML
		 * .CENTER,null).ajout(getText(Integer.toString
		 * (lc[j].getReputation())));
		 * d[j+1][7]=getTD(BaliseHTML.CENTER,null).ajout
		 * (getText(Utile.maj(lc[j].getStatutReputation()))); } BaliseHTML
		 * div=getDiv
		 * ().ajout(getFont(cC[3],"4").ajout(getText(t2[0]+b.getNom()))
		 * ).ajout(sautP()).ajout(
		 * DocumentHTML.creerTable(getTable(),d)).ajout(sautP()).ajout(
		 * ecrireMessages
		 * (b.getEvenements(),Const.MESSAGE_TYPE_COMMANDANT)).ajout
		 * (sautP()).ajout(
		 * getALienE(PRINCIPAL+"#"+LIEN_ALLIANCES).ajout(getText(t3)));
		 * listeDocuments
		 * .add(getDocument(chemin+getNomDocHTMLAlliance(b),b.getNom
		 * (),getBody().ajout(div))); } else
		 * a[i+1][6]=getTD(null,null).setTexteContenu("&nbsp;"); }
		 * racine.ajout(DocumentHTML
		 * .creerTable(getTable(),a).ajout(BaliseHTML.WIDTH,"100%")); } return
		 * racine;
		 */

		Map m = Univers.getTransferts();
		SortedMap sm = new TreeMap(m);
		Map.Entry[] me = (Map.Entry[]) sm.entrySet().toArray(new Map.Entry[0]);
		BaliseHTML racine = getDiv();
		for (int i = 0; i < me.length; i++) {
			Object jou = Joueur
					.getJoueur(((Integer) me[i].getKey()).intValue());
			racine.ajout(getFont(cC[3], "4").ajout(
					getText((jou instanceof Joueur ? ((Joueur) jou)
							.getNomNumeroHtml() : (String) jou))));
			racine.ajout(sautP());
			List l = (List) me[i].getValue();
			BaliseHTML[][] a = new BaliseHTML[l.size()][2];
			for (int j = 0; j < l.size(); j++) {
				String[] s = (String[]) l.get(j);
				Object jou2 = Joueur.getJoueur(Integer.parseInt(s[0]));
				String couleur = null;
				if (s[1].equals("D"))
					couleur = cC[4];
				else
					couleur = cC[6];
				if (s[2].startsWith("C*")) {
					couleur = cC[3];
					s[2] = s[2].substring(2);
				}
				a[j][0] = getTD(null, null)
						.ajout(getFont(couleur, null)
								.ajout(getText((jou2 instanceof Joueur ? ((Joueur) jou2)
										.getNomNumeroHtml() : (String) jou2))));
				a[j][1] = getTD(null, null).ajout(
						getFont(couleur, null).ajout(getText(s[2])));
			}
			racine.ajout(DocumentHTML.creerTable(getTable(), a)).ajout(sautP());
			if (jou instanceof String)
				Univers.supprimerTransferts(me[i].getKey());
		}
		r.listeDocuments.add(getDocument(
				Chemin.MJ + "transfert" + Univers.getTour()
						+ ".htm", "Transferts", getBody().ajout(racine)));
		Univers.terminerTransfertsPourLeTour();

		/*
		 * BaliseHTML rC=new BaliseHTML(BaliseHTML.OL);
		 * ajouterTypeCombat(rC,Univers.listeCombatsSpatiaux(),"Combats
		 * planétaires");
		 * ajouterTypeCombat(rC,Univers.listeCombatsSpatiaux(),"Combats
		 * spatiaux");
		 * r.listeDocuments.add(getDocument(Chemin.MJ+"combats"+Integer
		 * .toString(Univers.getTour())+".htm","Combats",
		 * getBody().ajout(racine)));
		 */

		r.ecriture();
	}

	/*
	 * private static void ajouterTypeCombat(BaliseHTML r,Map m,String titre){
	 * BaliseHTML li=new BaliseHTML(BaliseHTML.LI);
	 * r.ajout(li.ajout(getFont(null,"4").ajout(getText(titre+"<BR><BR>"))));
	 * for(int i=0;i<Const.NB_GALAXIES;i++){ BaliseHTML u=new
	 * BaliseHTML(BaliseHTML.U);
	 * li.ajout(u.ajout(getFont(cC[4],null).ajout(getText
	 * (Univers.getMessage("NOMS_GALAXIES",i,Locale.FRENCH)+"<BR><BR>"))));
	 * BaliseHTML ul=new BaliseHTML(BaliseHTML.UL);
	 *
	 * li.ajout(u.ajout( }
	 */
	// Le constructeur
	public Rapport(Commandant commandant) {
		c = commandant;
		listeDocuments = new /* ArrayList */temp();
		chemin = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour() + "/";
		listeLiens = new ArrayList();
	}

	// méthodes des liens

	private void ajouterLienPrincipal(BaliseHTML lien) {
		listeLiens.add(lien);
	}

	private void ajouterLienSecondaire(BaliseHTML lien) {
		Object o = listeLiens.get(listeLiens.size() - 1);
		if (o instanceof BaliseHTML) {
			ArrayList a = new ArrayList();
			a.add(o);
			a.add(lien);
			listeLiens.remove(o);
			listeLiens.add(a);
		} else
			((ArrayList) o).add(lien);
	}

	// méthode pour écrire le menu

	private void ecrireMenu() {
		BaliseHTML racine = new BaliseHTML(BaliseHTML.B);

		for (int i = 0; i < listeLiens.size(); i++) {
			Object o = listeLiens.get(i);
			if (o instanceof BaliseHTML lien) {
				BaliseHTML parent = new BaliseHTML(BaliseHTML.DIV,
						new String[][] {
								{
										"ID",
										"el" + (i + 1)
												+ "Parent" },
								{ BaliseHTML.CLASS, "parent" } });
                lien.ajout(BaliseHTML.CLASS, "item").ajout(BaliseHTML.TARGET,
						"p");
				parent.ajout(lien);
				racine.ajout(parent);
			} else {
				List l = (List) o;
				BaliseHTML parent = new BaliseHTML(BaliseHTML.DIV,
						new String[][] {
								{
										"ID",
										"el" + (i + 1)
												+ "Parent" },
								{ BaliseHTML.CLASS, "parent" } });
				BaliseHTML lien1 = (BaliseHTML) l.get(0);
				lien1.ajout(BaliseHTML.CLASS, "item").ajout("onClick",
						"expandIt('el" + (i + 1) + "');");
				BaliseHTML lien2 = (BaliseHTML) lien1.clone();
				lien1.setDescendants(null);
				lien1.ajout("onClick", "expandIt('el" + (i + 1)
						+ "'); return false;");
				BaliseHTML imag = new BaliseHTML(
						BaliseHTML.IMG,
						new String[][] {
								{ "NAME", "imEx" },
								{ "SRC", "../images/plus.gif" },
								{ "BORDER", "0" },
								{ "ALT", "+" },
								{ "width", "9" },
								{ "height", "9" },
								{ "ID", "el" + (i + 1) + "Img" } });
				lien1.ajout(imag);
				parent.ajout(lien1);
				parent.ajout(lien2.ajout(BaliseHTML.TARGET, "p")
						.setTexteContenu("&nbsp;"));
				racine.ajout(parent);

				BaliseHTML enfant = new BaliseHTML(BaliseHTML.DIV,
						new String[][] {
								{
										"ID",
										"el" + (i + 1)
												+ "Child" },
								{ BaliseHTML.CLASS, "child" } });
				for (int j = 1; j < l.size(); j++) {
					BaliseHTML enfantVrai = (BaliseHTML) l.get(j);
					enfantVrai.ajout(BaliseHTML.TARGET, "p").ajout(
							BaliseHTML.CLASS, "item");
					enfantVrai.setTexteContenu("<nobr>&nbsp;&nbsp;&nbsp;"
							+ enfantVrai.getTexteContenu() + "</nobr><BR>");
					enfant.ajout(enfantVrai);
				}
				racine.ajout(enfant);
			}
		}

		racine.ajout(getText("<script LANGUAGE=\"JavaScript1.2\"> \n <!-- \n if (NS4) { firstEl = \"el1Parent\"; "
				+ "firstInd = getIndex(firstEl); showAll(); arrange(); }  //--> </script>"));

		listeDocuments.add(getDocumentMenu(chemin + "menu.htm", getBody()
				.ajout("class", "bg "+ "body-r"+c.getRace()).ajout(racine)));
	}

	// méthodes principales

	public void creation() {
		BaliseHTML body = getBody().ajout("class", "body-r"+c.getRace());

		BaliseHTML splitter = getSpan("split");
		splitter.ajout(getInfoGenerales());
		// la carto
		ajouterLienPrincipal(
				getALienE("https://ydomenjoud.github.io/test-interface-sheril/")
				.ajout(getText("Cartographie")));
		ajouterLienPrincipal(
				getALienE("https://sheril.pbem-france.net/stats_general.php")
						.ajout("Statistiques"));
//		ajouterLienSecondaire(getALienE(PRINCIPAL + "#SYSTEMES_GENERAL").ajout(""));

		splitter.ajout(getBudget());
		splitter.ajout(
				getDiv().ajout("style", "display: flex;justify-content: space-between;flex-direction: column; gap: 5px;")
						.ajout(getVotrePeuple()).ajout(getResumePDV()));
		body.ajout(splitter);

		body.ajout(sautP());
		body.ajout(getResumeSystemes());
		body.ajout(sautP());
		body.ajout(getResumeFlottes());
		body.ajout(sautP());
		body.ajout(getSystemes());
		body.ajout(sautP());
		body.ajout(getFlottes());
		body.ajout(sautP());
		body.ajout(getPostesCommerciaux());
		body.ajout(sautP());
		body.ajout(getResumeTechnologies());
		body.ajout(sautP());
		body.ajout(getLeaders());
		body.ajout(sautP());
		body.ajout(getAlliances());
		body.ajout(sautP());
		body.ajout(getPacteNonAgression());
		body.ajout(sautP());
		body.ajout(getPlansDeVaisseaux());
		body.ajout(sautP());
		body.ajout(getVenteGalactique());
		body.ajout(sautP());
		body.ajout(getStrategiesDeCombat());
		body.ajout(sautP());
		body.ajout(getDetectionFlottes());
		body.ajout(sautP());
		body.ajout(getDetectionSystemes());
		body.ajout(sautP());
		body.ajout(getContacts());
		body.ajout(sautP());
		body.ajout(getMessages());
		body.ajout(sautP());

		listeDocuments.add(getDocument(
				chemin + PRINCIPAL,
				Univers.getMessageRapport("TITRE_RAPPORT",
						c.getLocale())
						+ c.getNomNumeroHtml(), body));
		listeDocuments.add(getDocument(
				chemin + DETAIL_FLOTTE,
				(String) Univers.getMessageRapport("TITRE_DETAIL_FLOTTES",
						c.getLocale()), getDetailFlottes()));
		listeDocuments.add(getDocument(chemin + DETAIL_TECHNOLOGIES,
				(String) Univers.getMessageRapport("TITRE_DETAIL_TECHNOLOGIES",
						c.getLocale()), getDetailTechnologies()));

		Position[] p = c.listePossession();
		ajouterLienPrincipal(getALienE(PRINCIPAL).ajout(
				getText("Détail des systèmes")));
		for (int i = 0; i < p.length; i++) {
			Systeme s = Univers.getSysteme(p[i]);
			listeDocuments.add(getDocument(
					chemin + getNomDocHTMLPlanetes(p[i], c.getNumero()),
					Univers.getMessageRapport("TITRE_DETAIL_SYSTEME",
							c.getLocale())
							+ s.getNomPositionSimple(c.getLocale()),
					getBody()
							.ajout(getDocHTMLPlanetes(s, c.getNumero(),
									c.getLocale()))));
		}
		p = c.getPositionsEspionnees();
		for (int i = 0; i < p.length; i++) {
			Systeme s = Univers.getSysteme(p[i]);
			listeDocuments.add(getDocument(
					chemin + lienRapportEspion(p[i]),
					Univers.getMessageRapport("TITRE_DETAIL_SYSTEME",
							c.getLocale())
							+ s.getNomPositionSimple(c.getLocale()), getBody()
							.ajout(getDocHTMLPlanetes(s, -1, c.getLocale()))));

		}

		ecrireMenu();
		Copie.copie(Chemin.FICHIERS + "RAPPORT.htm", chemin + "RAPPORT.htm");
		Copie.copie(Chemin.FICHIERS + "menu2.js", chemin + "menu2.js");
		// Copie.copieRepertoire(Chemin.RAPPORTS_IMAGES,chemin+nomRepImage);
	}

	public void ecriture() {/*
							 * while(listeDocuments.size()>0){
							 * ((DocumentHTML)listeDocuments.get(0)).ecrire();
							 * listeDocuments.remove(0); }
							 */
	}

	// méthodes donnant des balises.

	public BaliseHTML getDetailFlottes() {
		String[] t = (String[]) Univers.getMessageRapport("DETAIL_FLOTTES",
				c.getLocale());
		BaliseHTML div = getDiv();

		BaliseHTML lien = getABorne("DETAIL_FLOTTES");
		ajouterLienPrincipal(getALienE(DETAIL_FLOTTE + "#DETAIL_FLOTTES")
				.ajout(getText(t[0])));

		Map.Entry[] m = c.listeFlottesEtNumeros();
		div.ajout(lien).ajout(getFont(cC[3], "4").ajout(getText(t[0])))
				.ajout(sautP());

		// calcul du nombre de vaisseau, si nb>5000, on affiche rien (bug fin
		// o2) ->
		int nbV = 0;
		for (int i = 0; i < m.length; i++) {
			Flotte f = (Flotte) m[i].getValue();
			nbV = nbV + f.getNombreDeVaisseaux();
		}
		if (nbV > 2000)
			return getDiv();

		for (int i = 0; i < m.length; i++) {
			Flotte f = (Flotte) m[i].getValue();
			int num = ((Integer) m[i].getKey()).intValue();
			Vaisseau[] v = f.listeVaisseaux();
			BaliseHTML[][] a = new BaliseHTML[3 + v.length][7];

			ajouterLienSecondaire(getALienE(
					DETAIL_FLOTTE + "#" + getLienFlotte(num)).setTexteContenu(
					f.getNomNumeroHTML(num)));

			a[0][0] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2)
					.ajout(getFont(cC[4], null).ajout(
							getABorne(getLienFlotte(num)).ajout(getText(t[1]))));
			a[0][1] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getALienE(PRINCIPAL + "#" + getLienFlotte(num)).ajout(
							getFont(cC[7], null).ajout(getText(f.getNom()))));
			a[0][2] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(cC[4], null).ajout(getText(t[2])));
			a[0][3] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[7], null).ajout(
							getText(Integer.toString(num + 1))));
			a[1][0] = getTD(null, "7").setTexteContenu("&nbsp;");
			for (int j = 0; j < 7; j++)
				a[2][j] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[3], null).ajout(getTextI(t[3 + j])));
			for (int j = 0; j < v.length; j++) {
				a[j + 3][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(null, BaliseHTML.T_2).ajout(
								getText(v[j].getNom())));
				a[j + 3][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[5], null).ajout(getText(v[j].getType())));
				a[j + 3][2] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(v[j]
                                .nombreTotalPointsDeDommage()
								+ "/"
								+ v[j].getNbCases()));
				a[j + 3][3] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Utile.maj(v[j].getDescriptionNiveauExperience(c
								.getLocale()))));
				a[j + 3][4] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Utile.maj(v[j].getDescriptionNiveauMoral(c
								.getLocale()))));
				a[j + 3][5] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[6], BaliseHTML.T_2).ajout(
								getText(v[j].getDescriptionComposantsDetruits(c
										.getLocale()))));
				a[j + 3][6] = getTD(BaliseHTML.CENTER, null).ajout(
						getRace(v[j].getRaceEquipage()));
			}
			div.ajout(
					DocumentHTML.creerTable(getTable("detail_flotte"), a)).ajout(sautP());
		}
		return getBody().ajout(div);
	}

	public String CalculerColonisationSurPlanete(Planete p) {
		String colonisation = "";

		for (int r = 0; r < Const.NB_RACES - 1; r++) {
			String race = Messages.RACES[r];

			if (p.calculeMaxPop(r) != 0) {
				colonisation = colonisation
						+ "<span class=race"
						+ r
						+ " "
						+ (p.racePresente(r) ? "style=\"text-decoration: line-through\""
								: "") + ">";
				colonisation = colonisation + Messages.RACES[r] + " : "
						+ p.calculeMaxPop(r);
				colonisation = colonisation + "</span> . ";
			}
		}

		if (colonisation == "") {
			colonisation = "Aucune colonisation suppl&eacute;mentaire possible";
		}
		return colonisation;
	}

	public BaliseHTML getDocHTMLPlanetes(Systeme s, int num, Locale l) {
		String[] t = (String[]) Univers.getMessageRapport("SYSTEME", l);
		String[] t2 = (String[]) Univers.getMessageRapport("DETAIL_SYSTEME", l);
		ajouterLienSecondaire(getALienE(
				getNomDocHTMLPlanetes(s.getPosition(), num)).setTexteContenu(
				s.getNomPosition()));

		BaliseHTML div = getDiv();
		div.ajout(
				getFont(cC[3], "4").ajout(getText(t2[0] + s.getNomPosition())))
				.ajout(sautP());
		for (int i = 0; i < s.getNombrePlanetes(); i++) {
			Planete p = s.getPlanete(i);
			BaliseHTML[][] a = new BaliseHTML[40][6];
			int ligne = 0;
			a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
					getALienE(PRINCIPAL + "#" + s.getPosition().toString())
							.ajout(getImage(getCheminPlanete(p.getType()),
									25 + (p.getTaille() - 1) * 4,
									25 + (p.getTaille() - 1) * 4))).ajout(
					getText("(taille " + p.getTaille() + ")"));
			a[ligne][1] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(cC[5], "4")
							.ajout(getText(s.getNomNumeroPlanete(i))));
			a[ligne++][2] = getTD(BaliseHTML.CENTER, "3").ajout(
					getFont(cC[4], null).ajout(getText(t2[1]))).ajout(
					getText(Univers.getCommandant(p.getProprio())
							.getNomNumeroHtml()));

			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg")
					.ajout(getFont(cC[7], null).ajout(getText(t[2])));

			ArrayList ar = p.getPopulations();
			for (int j = 0; j < ar.size(); j++) {
				Population po = (Population) ar.get(j);
				a[ligne][0] = getTD(null, null).ajout(getRace(po.getRace()));
				a[ligne][1] = getTD(null, null)
						.ajout(getCouleurRace(po.getRace()).ajout(
								getText(Integer.toString(po.getPopActuelle()))));
				a[ligne][2] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[3])));
				a[ligne][3] = getTD(null, null).ajout(
						getCouleurRace(po.getRace()).ajout(
								getText(Integer.toString(p.calculeMaxPop(po
										.getRace())))));
				a[ligne][4] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[4])));
				a[ligne++][5] = getTD(null, null).ajout(
						getCouleurRace(po.getRace()).ajout(
								getText(p
                                        .calculeProgressionPop(po.getRace())
										+ "%")));
			}
			if (ligne != 2) {
				for (int j = 0; j < 6; j++)
					a[ligne][j] = getTD(null, null).setTexteContenu("&nbsp;");
				ligne++;
			}
			a[ligne][0] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t[5])));
			a[ligne][1] = getTD(null, null).ajout(
					getText(Integer.toString(p.populationTotale())));
			a[ligne][2] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t[6])));
			a[ligne][3] = getTD(null, null).ajout(
					getText(Integer.toString(p.populationMaximaleTotale())));
			a[ligne][4] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t[7])));
			a[ligne++][5] = getTD(null, null).ajout(
					getText(p.augmentationMoyenne() + "%"));

			a[ligne][0] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(
							getText("Colonisations possibles")));

			a[ligne][1] = getTD(BaliseHTML.CENTER, "5").ajout(
					getText(CalculerColonisationSurPlanete(p)));

			ligne++;

			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg")
					.ajout(getFont(cC[7], null).ajout(getText(t2[2])));
			a[ligne][0] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t2[3])));
			a[ligne][1] = getTD(null, null).ajout(
					getText(Utile.maj(Univers.getMessage("SORTES_ATMOSPHERES",
							p.getAtmosphere(), l))));
			a[ligne][2] = getTD(null, BaliseHTML.T_2).ajout(
					getFont(cC[4], null).ajout(getText(t2[4])));
			a[ligne++][3] = getTD(null, BaliseHTML.T_2)
					.ajout(getText((p.getNombreProductionMarchandise() == 0 ? Utile
							.maj(Univers.getMessage(
									"PLANETE_MARCHANDISE_INCULTE",
									c.getLocale()))
							: Utile.maj(Univers.getMessage("MARCHANDISES",
									p.getProductionMarchandise(), c.getLocale()))
									+ " : "
									+ p
                                      .getNombreProductionMarchandise())));
			a[ligne][0] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t2[5])));
			a[ligne][1] = getTD(null, null).ajout(
					getText(p.getRadiation() + "mR"));
			a[ligne][2] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t2[6])));
			a[ligne][3] = getTD(null, null).ajout(
					getText(p.getTemperature() + "°C"));
			a[ligne][4] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText(t2[7])));
			a[ligne++][5] = getTD(null, null)
					.ajout(getText(Utile.a1D(((float) p
                            .getGravite()) / 10F) + "g"));

			a[ligne][0] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText("Encombrement")));
			a[ligne][1] = getTD(null, null).ajout(
					getText(p.getEncombrement() + ""));
			a[ligne][2] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText("Sur")));
			a[ligne][3] = getTD(null, null).ajout(
					getText(p.getCapaciteEncombrement() + ""));
			a[ligne][4] = getTD(null, null).ajout(
					getFont(cC[4], null).ajout(getText("Total ")));
			a[ligne++][5] = getTD(null, null).ajout(
					getText("&nbsp;"
							+ convertir((float) (p.getEncombrement())
									/ (float) (p.getCapaciteEncombrement())
									* 100) + " %"));

			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg")
					.ajout(getFont(cC[7], null).ajout(getText(t2[8])));
			if ((num == -1) || (num == p.getProprio())) {
				a[ligne][0] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t2[11])));
				a[ligne][1] = getTD(null, null).ajout(
						getText(Integer.toString(p.getProductionMinerai())));
				a[ligne][2] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[10])));
				a[ligne][3] = getTD(null, null).ajout(
						getText(Integer.toString(p.getStockMinerai())));
				a[ligne][4] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[18])));
				a[ligne++][5] = getTD(null, null).ajout(
						getText(Integer.toString(p.getTerraformation())));
				a[ligne][0] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[13])));
				a[ligne][1] = getTD(null, null).ajout(
						getText(Integer.toString(p.getTaxation())));
				a[ligne][2] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[14])));
				a[ligne][3] = getTD(null, null).ajout(
						getText(Integer.toString(p.getStabilite())));
				a[ligne][4] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t2[9])));
				a[ligne++][5] = getTD(null, null).ajout(
						getText(Utile.maj(((p.getRevolte()) ? Univers
								.getMessage("OUI_NON", 0, l) : Univers
								.getMessage("OUI_NON", 1, l)))));
				a[ligne][0] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(
								getText("Evolution de la stabilité")));

				/*
				 * int modRelation=0; int[] race=Planete.racesPresentes();
				 * for(int a=0;a<race.length;a++) for(int
				 * j=i+1;j<race.length;j++)
				 * modRelation=modRelation+s.niveauRelation
				 * (Univers.getRelationRaces(s.getPos,race[a],race[j]));
				 */
				a[ligne++][1] = getTD(BaliseHTML.CENTER, "5").ajout(
						getText(" "));
			}

			else
				a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(
						getFont(cC[3], null).ajout(getText(t2[10])));

			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg").ajout(
					getFont(cC[7], null).ajout(getText(t[29])));
			Map.Entry[] listeE = p.listeEquipement();
			int k;
			for (k = 0; k < listeE.length; k++) {
				a[ligne][(k % 3) * 2] = getTD(null, null).ajout(
						getFont(cC[6], BaliseHTML.T_2).ajout(
								getText(((Integer) listeE[k].getValue())
										.intValue() == 1 ? Utile.maj(Univers
										.getTechnologie(
												(String) listeE[k].getKey())
										.getNomComplet(l)) : Utile.maj(Univers
										.getTechnologie(
												(String) listeE[k].getKey())
										.getNomPlurielComplet(l)))));
				a[ligne][1 + (k % 3) * 2] = getTD(null, null).ajout(
						getText(((Integer) listeE[k].getValue()).toString()));
				if (k % 3 == 2)
					ligne++;
			}
			if (k % 3 == 1)
				a[ligne++][5] = getTD(null, "4").setTexteContenu("&nbsp;");
			if (k % 3 == 2)
				a[ligne++][5] = getTD(null, BaliseHTML.T_2).setTexteContenu(
						"&nbsp;");

			div.ajout(
					DocumentHTML.creerTable(getTable(), a).ajout(
							BaliseHTML.WIDTH, "90%")).ajout(sautP());
		}

		return div;
	}

	public BaliseHTML getInfoGenerales() {
		String[] t = (String[]) Univers.getMessageRapport("INFO_GENERALES",c.getLocale());

		ajouterLienPrincipal(getALienE(PRINCIPAL + "#INFO_GENERALES").ajout(
				getText(t[0])));

		BaliseHTML[][] a = new BaliseHTML[t.length][2];
		a[0][0] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
				getTitreTable().ajout(getText(t[0]))).ajout("id", "INFO_GENERALES");
		for (int i = 1; i < t.length; i++)
			a[i][0] = getTD(null, null).ajout(getText(t[i]));
		int j = 1;
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(
				Integer.toString(Univers.getTour()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Utile.getDateRapport());
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(
				Integer.toString(c.getNumero()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(c.getNom());
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(getRace(c.getRace()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Integer.toString(c.getPuissance()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Integer.toString(c.getNombrePlanetesPossedees()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Utile.maj(c.getGrade()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Integer.toString(c.getReputation()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(Utile.maj(c.getStatutReputation()));
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(c.getNombreMaximalDeTransfertEntreSysteme()+"");
		a[j++][1] = getTD(BaliseHTML.CENTER, null).ajout(c.getPointsDeVictoire()+"");

		return DocumentHTML.creerTable(getTable("table_half"), a);
	}

	public BaliseHTML getVotrePeuple() {
		String[] t = (String[]) Univers.getMessageRapport("VOTRE_PEUPLE",
				c.getLocale());

		BaliseHTML lien = getABorne("VOTRE_PEUPLE");
//		ajouterLienPrincipal(getALienE(PRINCIPAL + "#VOTRE_PEUPLE").ajout(
//				getText(t[0])));

		BaliseHTML[][] a = new BaliseHTML[4 + Const.NB_RACES][3];

		a[0][0] = getTD(BaliseHTML.CENTER, "4").ajout(
				getTitreTable().ajout(getText(t[0])));

		a[1][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getTitreCaption().ajout(getText(t[1])));
		a[1][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getTitreCaption().ajout(getText(t[2])));
		a[1][2] = getTD(BaliseHTML.CENTER, null).ajout(
				getTitreCaption().ajout(getText(t[3])));

		for (int i = 0; i < Const.NB_RACES; i++) {
			a[i + 2][0] = getTD(BaliseHTML.CENTER, null).ajout(getRace(i));
			a[i + 2][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getSpanRace(i).ajout(
							getText(Integer.toString(c
									.getNombrePopulationDeRace(i)))));
			a[i + 2][2] = getTD(BaliseHTML.CENTER, null).ajout(
					getSpanRace(i).ajout(
							getText(Integer.toString(c
									.getNombrePopulationMaxDeRace(i)))));

		}
		for (int i = 0; i < 3; i++)
			a[2 + Const.NB_RACES][i] = getTD(null, null).setTexteContenu(
					"&nbsp;");
		a[3 + Const.NB_RACES][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getText(t[3]));
		a[3 + Const.NB_RACES][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getText(Integer.toString(c.getPopulationTotale())));
		a[3 + Const.NB_RACES][2] = getTD(BaliseHTML.CENTER, null).ajout(
				getText(Integer.toString(c.getPopulationTotaleMaximale())));

        return DocumentHTML.creerTable(getTable("table_half"), a);
	}

	public String getPDVInfos(PointDeVictoireCategorie categorie) {
		return c.pointDeVictoireHistory.stream()
				.filter(pdv -> pdv.type() == categorie)
				.map(pdv -> "T" + pdv.tour() + "<span class='plus'>(+" + pdv.points() + ")</span>")
				.collect(Collectors.joining(", "));
	}

	public BaliseHTML getResumePDV(){
        String sb = """
                <table>
                	<thead>
                	<tr><th colspan="2" class="titre_table">Résumé des points de victoire</th></tr>
                	</thead>
                	<tbody>
                	<tr><td class="titre_caption">Type</td><td class="titre_caption">Historique</td></tr>
                	<tr><td>Territorial</td><td>%s</td></tr>
                	<tr><td>Bataille</td><td>%s</td></tr>
                	<tr><td>Culturelle</td><td>%s</td></tr>
                	<tr><td>Scientifique</td><td>%s</td></tr>
                	<tr><td>Merveille</td><td>%s</td></tr>
                	<tr><td>Coloniale</td><td>%s</td></tr>
                	</tbody>
                	</table>
                """.formatted(
                getPDVInfos(PointDeVictoireCategorie.PLANETES),
                getPDVInfos(PointDeVictoireCategorie.COMBATS),
                getPDVInfos(PointDeVictoireCategorie.MERVEILLE),
                getPDVInfos(PointDeVictoireCategorie.RECHERCHE),
                getPDVInfos(PointDeVictoireCategorie.MERVEILLE),
                getPDVInfos(PointDeVictoireCategorie.POPULATION_VS)
        );

		return getText(sb);
	}

	public BaliseHTML getBudget() {
		String t = (String) Univers.getMessageRapport("RAPPORT_FINANCIER", c.getLocale());
		String[] b = Univers.getTableauMessage("CHAMPS_BUDGET", c.getLocale());

		BaliseHTML lien = getABorne("RAPPORT_FINANCIER");
//		ajouterLienPrincipal(getALienE(PRINCIPAL + "#RAPPORT_FINANCIER").ajout(
//				getText(t)));

		BaliseHTML[][] a = new BaliseHTML[Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE + 1][2];
		a[0][0] = getTD(BaliseHTML.CENTER, "2").ajout(getTitreTable().ajout(getText(t)));
		a[1][0] = getTD(null, "1").ajout(getTitreCaption().ajout(getText(b[0].toUpperCase())));
		a[1][1] = getTD("right", null).ajout("class", "cur").ajout(Utile.a1DS(c.getBudget(0)));
		int ligne = 2;
		for (int i = 1; i < Const.BUDGET_COMMANDANT_TOTAL_RECETTE; i++)
			if (c.getBudget(i) != 0F) {
				a[ligne][0] = getTD(null, null).ajout(getText(Utile.maj(b[i])));
				a[ligne++][1] = getTD("right", null).ajout("class", "cur plus").ajout( getText("+" + (Utile.a1DS(c.getBudget(i)))));
			}
		a[ligne][0] = getTD(null, "1").ajout(
				getTitreCaption().ajout(
						getText(b[Const.BUDGET_COMMANDANT_TOTAL_RECETTE]
								.toUpperCase())));
		a[ligne++][1] = getTD("right", null)
				.ajout(getTitreCaption()
						.ajout("class", "cur plus bold")
						.ajout(getText("+" + (Utile.a1DS(c
								.getBudget(Const.BUDGET_COMMANDANT_TOTAL_RECETTE))))));
		for (int i = Const.BUDGET_COMMANDANT_TOTAL_RECETTE + 1; i < Const.BUDGET_COMMANDANT_TOTAL_DEPENSE; i++)
			if (c.getBudget(i) != 0F) {
				a[ligne][0] = getTD(null, null).ajout(getText(Utile.maj(b[i])));
				a[ligne++][1] = getTD("right", null)
						.ajout("class", "cur moins")
						.ajout(getText((Utile.a1DS(c.getBudget(i)))));
			}
		a[ligne][0] = getTD(null, "1").ajout(
				getTitreCaption().ajout(
						getText(b[Const.BUDGET_COMMANDANT_TOTAL_DEPENSE]
								.toUpperCase())));
		a[ligne++][1] = getTD("right", null)
				.ajout(getTitreCaption()
						.ajout("class", "cur moins bold")
						.ajout(getText((Utile.a1DS(c
								.getBudget(Const.BUDGET_COMMANDANT_TOTAL_DEPENSE))))));
		a[ligne][0] = getTD(null, "1").ajout(
				getTitreCaption().ajout(
						getText(b[Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE]
								.toUpperCase())));
		a[ligne++][1] = getTD("right", null)
				.ajout(getTitreCaption()
						.ajout("class", "cur bold")
						.ajout(getText((Utile.a1DS(c
								.getBudget(Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE))))));

		return DocumentHTML.creerTable(getTable("table_half"), a);
	}


	public BaliseHTML getVenteGalactique() {

		List<OffreMarche> encheres = Univers.getListeOffresMarche().stream()
				.filter(o -> o.getNumeroVendeur() == c.getNumero()).toList();


		ajouterLienPrincipal(getALienE(PRINCIPAL + "#MARCHE").ajout("Marché Galactique"));

		StringBuilder table = new StringBuilder("""
				<A name="MARCHE"></A>
				<SPAN class="titre_section">Liste de vos enchères galactiques</SPAN>
				<table class="table_full stripped">
					<thead>
						<tr>
							<th>Quantité</th>
							<th>Marchandise</th>
							<th>Depuis le système</th>
							<th>Prix</th>
							<th>Termine au tour</th>
						</tr>
					</thead>
					<tbody>
				""");

		for(OffreMarche o : encheres) {
			table.append("""
					<tr>
						<td>%d</td>
						<td>%s</td>
						<td><a href='%s#%s' class='systeme'>%s</a></td>
						<td><span class='cur'>%,d</span></td>
						<td>%d</td>
					</tr>""".formatted(
								o.getQuantite(),
								ObjetTransporte.traductionChargement(o.getCodeMarchandise(),o.getQuantite(), Locale.getDefault()),
								Rapport.PRINCIPAL,
								o.getPositionOrigine().toString(),
								Univers.getSysteme(o.getPositionOrigine()).getNomPosition(),
								o.getPrixTotal(),
								o.getTourFin()
			));
		}
		table.append("<tbody></table>");


		return getDiv().ajout(table.toString());

	}

	public BaliseHTML getPlansDeVaisseaux() {
		String[] t = (String[]) Univers.getMessageRapport("PLANS_DE_VAISSEAUX",
				c.getLocale());

		ajouterLienPrincipal(getALienE(PRINCIPAL + "#PLAN")
				.ajout(getText(t[0])));
		BaliseHTML lien = getABorne("PLAN");
		BaliseHTML titre = getTitreSection().ajout(getText(t[0]));

		BaliseHTML racine = getDiv().ajout(lien).ajout(titre);
		ajouterLienPrincipal(getALienE("https://sheril.pbem-france.net/stats/vapub.htm")
				.ajout(getText("Plans Publics")));

		PlanDeVaisseau[] p = c.listePlansConnusNonPublics();

		return racine.ajout(getPlansDeVaisseaux(p, t, c.getLocale()));
	}

	public static BaliseHTML getPlansDeVaisseaux(PlanDeVaisseau[] p,
			String[] t, Locale loc) {
		BaliseHTML[][] a = new BaliseHTML[p.length + 1][16];

		for (int i = 0; i < 15; i++)
			a[0][i] = getTD(BaliseHTML.CENTER, null).ajout(
					getTitreCaption().ajout(getText(t[i + 1])));
		for (int i = 0; i < p.length; i++) {

			a[i + 1][0] = getTD(BaliseHTML.CENTER, null)
					.ajout(getFont(null, BaliseHTML.T_2).ajout(
							getText(p[i].getNom())));
			a[i + 1][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(p[i].estdAlliancePrivee() ? "Secret" : p[i]
									.getNomConcepteur(loc))));
			a[i + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(p[i].getMarque(loc))));
			a[i + 1][3] = getTD(BaliseHTML.CENTER, null)
					.ajout(getFont(cC[4], BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i].getTourDeCration()))));
			a[i + 1][4] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i].getTaille() + 1))));
			a[i + 1][5] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i]
									.getCapaciteMouvement(false)))));
			a[i + 1][6] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i]
									.getPointsDeConstructions()))));
			a[i + 1][7] = getTD(BaliseHTML.CENTER, null)
					.ajout(getFont(null, BaliseHTML.T_2).ajout(
							"<span class='cur'>" + Utile.a1DS(p[i].getPrix()) + "</span>"));
			a[i + 1][8] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i]
									.getMineraiNecessaire()))));
			a[i + 1][9] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[6], BaliseHTML.T_2).ajout(
							getText(p[i].getListeMarchandises(loc))));
			a[i + 1][10] = getTD(BaliseHTML.CENTER, null)
					.ajout(getFont(null, BaliseHTML.T_2).ajout(
							getText(Integer.toString(p[i].getForceSpatiale()))));
			a[i + 1][11] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2)
							.ajout(getText(Integer.toString(p[i]
									.getForcePlanetaire()))));
			a[i + 1][12] = getTD(BaliseHTML.CENTER, null).ajout(
					getSpan("technologie").ajout(
							getText(p[i].descriptionComposants(loc))));
			a[i + 1][13] = getTD(BaliseHTML.CENTER, null)
					.ajout(getFont(null, BaliseHTML.T_2)
							.ajout(getText(p[i].getRoyalties()
									+ "%")));
			a[i + 1][14] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(p[i].getDescriptionDomaine(loc))));
		}
		return DocumentHTML.creerTable(getTable("table_full stripped"), a);
	}

	public BaliseHTML getAlliances() {
		int[] p = c.numerosAlliances();
		BaliseHTML racine = getDiv();
		if (p.length > 0) {
			BaliseHTML lien = getABorne("ALLI");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#ALLI").ajout(getText("Alliances")));

			racine.ajout(lien);

			String[] t = (String[]) Univers.getMessageRapport("ALLIANCES",c.getLocale());
			String[] t2 = (String[]) Univers.getMessageRapport("ALLIANCES_DETAIL", c.getLocale());
			String t3 = (String) Univers.getMessageRapport("RETOUR_PRINCIPAL",c.getLocale());
			racine.ajout(getTitreSection().ajout(t[0]).ajout(getABorne(LIEN_ALLIANCES)));
			BaliseHTML[][] a = new BaliseHTML[p.length + 1][7];
			for (int i = 0; i < 7; i++)
				a[0][i] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getTextI(t[i + 1])));
			for (int i = 0; i < p.length; i++) {
				Alliance b = Univers.getAlliance(p[i]);
				Commandant[] lc = b.getAdherents();
				if ((!b.estSecrete()) || (b.estDemocratique())
						|| (b.estDirigeePar(c.getNumero())))
					a[i + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
							getALienE(getNomDocHTMLAlliance(b)).ajout(
									getFont(cC[1], null).ajout(
											getText(b.getNom()))));
				else
					a[i + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
							getFont(cC[1], null).ajout(getText(b.getNom())));
				a[i + 1][1] = getTD(BaliseHTML.CENTER, null)
						.ajout(getText(Utile.maj(b.getDescriptionType(c
								.getLocale()))));
				a[i + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(b.getDescriptionConcepteur(c.getLocale())));
				a[i + 1][3] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(b.getDescriptionDirigeant(c.getLocale())));
				a[i + 1][4] = getTD(BaliseHTML.CENTER, null)
						.ajout("<span class='cur'>"+Utile.a1DS(b.getDroitsEntree())+"</span>");
				a[i + 1][5] = getTD(BaliseHTML.CENTER, null).ajout(
						getText((b.estSecrete() ? Univers.getMessage("OUI_NON",
								0, c.getLocale()) : Univers.getMessage(
								"OUI_NON", 1, c.getLocale()))));
				if ((!b.estSecrete()) || (b.estDemocratique())
						|| (b.estDirigeePar(c.getNumero()))) {
					a[i + 1][6] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(Commandant.getListeCommandants(lc)));

					BaliseHTML[][] d = new BaliseHTML[lc.length + 1][8];
					for (int j = 0; j < 8; j++)
						d[0][j] = getTD(BaliseHTML.CENTER, null)
								.ajout(getFont(cC[4], null).ajout(
										getTextI(t2[j + 1])));
					for (int j = 0; j < lc.length; j++) {
						d[j + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(lc[j].getNom()));
						d[j + 1][1] = getTD(BaliseHTML.CENTER, null).ajout(
								getALienM(lc[j].getAdresseElectronique())
										.ajout(getText(lc[j]
												.getAdresseElectronique())));
						d[j + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
								getRace(lc[j].getRace()));
						d[j + 1][3] = getTD(BaliseHTML.CENTER, null)
								.ajout(getText(Integer.toString(lc[j]
										.getPuissance())));
						d[j + 1][4] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(Integer.toString(lc[j]
										.getNombrePlanetesPossedees())));
						d[j + 1][5] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(Utile.maj(lc[j].getGrade())));
						d[j + 1][6] = getTD(BaliseHTML.CENTER, null)
								.ajout(getText(Integer.toString(lc[j]
										.getReputation())));
						d[j + 1][7] = getTD(BaliseHTML.CENTER, null)
								.ajout(getText(Utile.maj(lc[j]
										.getStatutReputation())));
					}
					BaliseHTML div = getDiv()
							.ajout(getFont(cC[3], "4").ajout(
									getText(t2[0] + b.getNom())))
							.ajout(sautP())
							.ajout(DocumentHTML.creerTable(getTable(), d))
							.ajout(sautP())
							.ajout(ecrireMessages(b.getEvenements(),
									Const.MESSAGE_TYPE_COMMANDANT))
							.ajout(sautP())
							.ajout(getALienE(PRINCIPAL + "#" + LIEN_ALLIANCES)
									.ajout(getText(t3)));
					listeDocuments.add(getDocument(chemin
							+ getNomDocHTMLAlliance(b), b.getNom(), getBody()
							.ajout(div)));
				} else
					a[i + 1][6] = getTD(null, null).setTexteContenu("&nbsp;");
			}
			racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a).ajout(
					BaliseHTML.WIDTH, "100%"));
		}

		return racine;
	}

	public BaliseHTML getContacts() {
		Map<Integer, Integer> p = c.getContacts();
		BaliseHTML racine = getDiv();
		if (p.size() > 0) {
			String t = "Commandants rencontrés";

			BaliseHTML lien = getABorne("CONTACTS");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#CONTACTS").ajout(getText("Contacts")));

			racine.ajout(lien);

			racine.ajout(getTitreSection().ajout(getText(t)));

			BaliseHTML[][] a = new BaliseHTML[p.size() + 1][3];
			a[0][0] = getTD(BaliseHTML.CENTER, null).ajout(getTitreCaption().ajout(getText("Nom/Numéro")));
			a[0][1] = getTD(BaliseHTML.CENTER, null).ajout(getTitreCaption().ajout(getText("Tour de rencontre")));
			a[0][2] = getTD(BaliseHTML.CENTER, null).ajout(getTitreCaption().ajout(getText("Email")));

			int i = 1;
			for (Map.Entry<Integer, Integer> entry : p.entrySet()) {
				Integer num = entry.getKey();
				Integer tour = entry.getValue();
				Commandant comm = Univers.getCommandant(num.intValue());
				if (comm != null) {
					String mail = comm.getAdresseElectronique();
					a[i][0] = getTD(BaliseHTML.CENTER, null).ajout(getText(comm.getNomNumeroHtml()));
					a[i][1] = getTD(BaliseHTML.CENTER, null).ajout(getText(tour.toString()));
					a[i][2] = getTD(BaliseHTML.CENTER, null).ajout(getText(
							"<a href=\"mailto:" + mail + "\">" + mail + "</a>"
					));
					i++;
				}
			}

			// Redimensionner le tableau si certains commandants n'existent plus
			if (i < a.length) {
				BaliseHTML[][] b = new BaliseHTML[i][3];
				System.arraycopy(a, 0, b, 0, i);
				a = b;
			}

			racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a).ajout(
					BaliseHTML.WIDTH, "100%"));
		}
		return racine;
	}

	public BaliseHTML getPacteNonAgression() {
		int[] p = c.listePactesDeNonAgression();
		BaliseHTML racine = getDiv();
		if (p.length > 0) {
			String t = (String) Univers.getMessageRapport(
					"PACTE_NON_AGRESSION", c.getLocale());

			BaliseHTML lien = getABorne("PACTE");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#PACTE").ajout(
					getText("Pacte NA")));

			racine.ajout(lien);

			racine.ajout(getTitreSection().ajout(t).ajout(getABorne(LIEN_PACTES_NON_AGRESSION)));
			BaliseHTML[][] a = new BaliseHTML[1][1];
			String inter = "";
			for (int i = 0; i < p.length; i++)
				inter = inter + Univers.getCommandant(p[i]).getNomNumeroHtml()
						+ "&nbsp;";
			a[0][0] = getTD(BaliseHTML.CENTER, null).ajout(getText(inter));
			racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a).ajout(
					BaliseHTML.WIDTH, "100%"));
		}
		return racine;
	}

	public BaliseHTML getLeaders() {
		Heros[] h = c.listeHeros();
		Gouverneur[] g = c.listeGouverneur();
		BaliseHTML racine = getDiv();
		if (h.length > 0) {
			String t = (String) Univers.getMessageRapport("HEROS",
					c.getLocale());

			BaliseHTML lien = getABorne("HEROS");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#HEROS").ajout(
					getText("Héros")));

			racine.ajout(lien);

			racine.ajout(getTitreSection().ajout(getText(t))).ajout(
					getABorne(LIEN_HEROS));
			racine.ajout(getListeLeaders(h, c.getLocale())).ajout(sautP());
		}
		if (g.length > 0) {
			String t = (String) Univers.getMessageRapport("GOUVERNEURS",
					c.getLocale());

			BaliseHTML lien = getABorne(LIEN_GOUVERNEURS);
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#" + LIEN_GOUVERNEURS).ajout(
					getText("Gouverneurs")));

			racine.ajout(lien);

			racine.ajout(getTitreSection().ajout(getText(t)));
			racine.ajout(getListeLeaders(g, c.getLocale())).ajout(sautP());
		}
		return racine;
	}

	public static BaliseHTML getListeLeaders(Leader[] l, Locale loc) {
		String[] t = (String[]) Univers.getMessageRapport("LEADERS", loc);
		BaliseHTML[][] a = new BaliseHTML[l.length + 1][12];
		for (int i = 0; i < 12; i++)
			a[0][i] = getTD(BaliseHTML.CENTER, null).ajout(
					getTitreCaption().ajout(getText(t[i])));
		for (int i = 0; i < l.length; i++) {
			a[i + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(l[i].getNom() + (l[i].estNomme() ? "" : "(*)")));
			a[i + 1][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(l[i].descriptionPosition(loc)));
			a[i + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getNiveau())));
			a[i + 1][3] = getTD(BaliseHTML.CENTER, null).ajout(
					getRace(l[i].getRace(), loc));
			a[i + 1][4] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getVitesse())));
			a[i + 1][5] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getAttaque())));
			a[i + 1][6] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getDefense())));
			a[i + 1][7] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getMoral())));
			a[i + 1][8] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getMarchand())));
			a[i + 1][9] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[6], BaliseHTML.T_2).ajout(
							getText(l[i].getDescriptionCompetences(loc))));
			a[i + 1][10] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(l[i].getExperience())));
			a[i + 1][11] = getTD(BaliseHTML.CENTER, null).ajout(
					"<span class='cur'>"+Utile.a1DS(l[i].getValeur())+"</span>"
			);
		}
		return DocumentHTML.creerTable(getTable("table_full stripped"), a);
	}

	// Dans Rapport.java

	public BaliseHTML getDetectionFlottes() {
		// modifié par ilmir (ilmir@aol.com)
		//
		// les flottes apparaissent classés :o)
		// de plus, la fonction contient moins de constantes figées

		String ligneDonnees;
		String clef;
		Iterator it1;
		Iterator it2;
		ArrayList infos;
		ArrayList donnees;
		Commandant commandant;
		Flotte f;
		BaliseHTML lien;

		String[] t;
		BaliseHTML[][] a;

		int p1 = 1;
		int p2 = 0;
		BaliseHTML racine = getDiv();
		TreeMap orderDetect = new TreeMap();
		int[][] b = c.getFlottesDetectees();

		// si on detecte rien... on fait rien.
		if (b.length > 0) {
			t = (String[]) Univers.getMessageRapport("DETECTION_FLOTTES",
					c.getLocale());

			lien = getABorne("DET_F");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#DET_F").ajout(
					getText("D&eacute;tection Flottes")));
			racine.ajout(lien);

			// on met le titre du tableau contenu dans t[0]
			racine.ajout(getABorne(LIEN_DETECTION_FLOTTES));
			racine.ajout(getTitreSection().ajout(getText(t[0])));

			// on créé notre futur tableau avec une ligne en plus pour le titre
			// des colonnes et
			// un colonne en moins car t[0] est le titre du tableau
			a = new BaliseHTML[b.length + 1][t.length - 1];

			// titres des colonnes du tableau
			for (int i = 0; i < t.length - 1; i++)
				a[0][i] = getTH(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getTextI(t[i + 1])));

			for (int i = 0; i < b.length; i++) {
				// on annalyse le tableau des flottes détéctées et on recupére
				// les informations qu'on
				// veut faire apparaitre dans le rapport
				commandant = Univers.getCommandant(b[i][0]);
				f = commandant.getFlotte(b[i][1]);
				infos = new ArrayList(t.length - 1);
				// on définie la clef de la Map qui doit être comparable
				// ici le tri sera fait par position puis par commandant puis
				// par numéro de flotte
				// attention, la clef doit être unique
				clef = Utile.pareil(f.getPosition()) + "%"
						+ Utile.formatNumber(b[i][0], 3) + "%"
						+ Utile.formatNumber(b[i][1], 6);
				// System.out.println(clef);
				// clef = ""+f.getPosition().hashCode();
				// attention il faut autant d'information que t.length-1 sinon
				// l'affichage risque d'avoir des soucis

				infos.add(f.getPosition().getDescription());
				infos.add(f.getNom());
				infos.add((b[i][1] + 1) + "");
				infos.add(commandant.getNomNumeroHtml());
				infos.add(Utile.maj("<span class='c4'>"
						+ f.getNombreDeVaisseaux() + "</span>"));
				int[] bornes = f.getBornesPuissance();
				String tooltip = bornes[0] + " - " + bornes[1];
				String description = "<span data-tooltip=\""+tooltip+"\">"
						+ Utile.maj(f.getDescriptionPuissance(c.getLocale()))
						+ "</span>";
				infos.add(description);
				orderDetect.put(clef, infos);
			}
			// la classe TreeMap implémente ShortedMap, du coup elle est déjà
			// triée selon la clef
			// il nous reste donc plus qu'à la parcourir
			it1 = orderDetect.values().iterator();
			while (it1.hasNext()) {
				donnees = (ArrayList) it1.next();

				// on recupére les infos dans l'ordre ou on les a mis...
				it2 = donnees.iterator();
				p2 = 0;
				while (it2.hasNext()) {
					ligneDonnees = (String) it2.next();
					a[p1][p2] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(ligneDonnees));
					p2++;
				}
				p1++;
			}
			racine.ajout(DocumentHTML.creerTable(getTable("table_full sortable stripped"), a));
		}
		return racine;
	}

	public BaliseHTML getDetectionSystemes() {
		Position[] b = c.getSystemesDetectes();
		BaliseHTML racine = getDiv();
		if (b.length > 0) {
			String[] t = (String[]) Univers.getMessageRapport(
					"DETECTION_SYSTEMES", c.getLocale());

			BaliseHTML lien = getABorne("DET_S");
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#DET_S").ajout(
					getText("Détection Systèmes")));

			racine.ajout(lien);
			racine.ajout(getABorne(LIEN_DETECTION_SYSTEMES).ajout(sautP()));
			racine.ajout(getTitreSection().ajout(getText(t[0])));

			BaliseHTML[][] a = new BaliseHTML[b.length + 1][6];
			a[0][0] = getTD(null, null).setTexteContenu("&nbsp;");
			for (int i = 0; i < 5; i++)
				a[0][i + 1] = getTH(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getTextI(t[i + 1])));
			for (int i = 0; i < b.length; i++) {
				Systeme s = Univers.getSysteme(b[i]);
				a[i + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getImage(getCheminEtoile(s.getTypeEtoile()), 15, 15));
				a[i + 1][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText("<!-- Systeme Scanne -->"
								+ s.getPosition().getDescription()));
				a[i + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(s.getNom()));
				a[i + 1][3] = getTD(BaliseHTML.CENTER, null).ajout(
						Integer.toString(s.getPopulation(-1))).ajout(
						getFont(cC[4], null).setTexteContenu(
								"/"
										+ s
                                        .getPopulationMaximale(-1)));
				a[i + 1][4] = getTD(BaliseHTML.CENTER, null).ajout(
						Integer.toString(s.getNombrePlanetes()));
				a[i + 1][5] = getTD(BaliseHTML.CENTER, null).setTexteContenu(
						Commandant.getListeCommandants(s.getProprios()));
			}
			racine.ajout(DocumentHTML.creerTable(getTable("table_full sortable stripped"), a));
		}
		return racine;
	}

	public BaliseHTML getMessages() {
		BaliseHTML racine = getDiv();
		String[] t = (String[]) Univers.getMessageRapport("MESSAGES", c.getLocale());
		Commentaire er = c.getErreurs();
		Commentaire ev = c.getEvenements();
		Commentaire co = c.getCombats();
		Commentaire or = c.getOrdres();
		if (er.nbMessages() != 0) {
			racine.ajout(getTitreSection().ajout(getText(t[0])));
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#ERREUR").ajout(getText("Erreurs ordres")));
			racine.ajout(getABorne("ERREUR"));
			racine.ajout(ecrireMessages(er, Const.MESSAGE_TYPE_COMMANDANT));
		}
		if (ev.nbMessages() != 0) {
			racine.ajout(getTitreSection().ajout(getABorne(LIEN_MESSAGES)).ajout(getText(t[1])));
			ajouterLienPrincipal(getALienE(PRINCIPAL + "#EVT").ajout(getText("Evenements")));
			racine.ajout(getABorne("EVT"));
			racine.ajout(ecrireMessages(ev, Const.MESSAGE_TYPE_COMMANDANT));
		}
		if (co.nbMessages() != 0) {
			BaliseHTML d = getDiv();
			d.ajout(getTitreSection().ajout(getText(t[2])));
			ajouterLienPrincipal(getALienE(DETAIL_COMBAT).ajout(getText("Combats")));
			d.ajout(ecrireMessages(co, Const.MESSAGE_TYPE_COMMANDANT));
			listeDocuments.add(getDocument(chemin + DETAIL_COMBAT, (String) Univers.getMessageRapport("TITRE_DETAIL_COMBAT",c.getLocale()), getBody().ajout(d)));
		}
		if (or.nbMessages() != 0) {
			racine.ajout(getTitreSection().ajout(getText(t[3])));
			racine.ajout(ecrireMessages(or, Const.MESSAGE_TYPE_COMMANDANT));
		}
		return racine;
	}

	public BaliseHTML ecrireMessages(Commentaire co, int type) {
		ArrayList[] a = co.listeMessages(c.getLocale(), type);
		BaliseHTML retour = new BaliseHTML(BaliseHTML.B);
		for (int i = 0; i < a.length; i++) {
			BaliseHTML p = getP();
			StringBuffer sb = new StringBuffer(10000);
			for (int j = 0; j < a[i].size(); j++) {
				sb.append(a[i].get(j));
				sb.append("<BR>");
			}
			p.setTexteContenu(sb.toString());
			retour.ajout(p);
		}
		return getP().ajout(retour);
	}

	public BaliseHTML getResumeSystemes() {
		if (c.getNombrePossessions() > 0) {
			String[] t = (String[]) Univers.getMessageRapport( "RESUME_SYSTEMES", c.getLocale());
			Position[] p = c.listePossession();

			int secteur = 0;
			int numero = 0;

			ajouterLienPrincipal(getALienE(
					PRINCIPAL + "#" + LIEN_RESUME_SYSTEME).ajout(
					getText("R&eacute;sum&eacute; syst&egrave;mes")));

			BaliseHTML[][] a = new BaliseHTML[2 + c.getNombrePossessions()][t.length - 1];
			a[0][0] = getTD(BaliseHTML.CENTER, Integer.toString(t.length - 1)).ajout(getTitreTable().ajout(getText(t[0])));
			for (int i = 0; i < t.length - 1; i++)
				a[1][i] = getTD(BaliseHTML.CENTER, null).ajout(getTitreCaption().ajout(getText(t[i + 1])));
			for (int i = 0; i < c.getNombrePossessions(); i++) {
				Systeme s = Univers.getSysteme(p[i]);
				Possession fief = c.getPossession(p[i]);
				BaliseHTML color = (s.getPopulation(c.getNumero()) != 0) ? getSpanRace(s.getRaceMajoritaire(c.getNumero())) : getText("");
				int plaPossedees = s.getNombrePlanetesPossedees(c.getNumero());
				int plaTotal =  s.getNombrePlanetes();
				String className = (plaPossedees == plaTotal) ? "plus" : (plaPossedees > plaTotal / 2) ? "half" : "less";

				a[i + 2][0] = getTD(null, null).ajout(getALienI(p[i].toString()).ajout(getImage(getCheminEtoile(s.getTypeEtoile()), 15, 15)));
				a[i + 2][1] = getTD(BaliseHTML.CENTER, null).ajout( getText(p[i].getDescription()));
				a[i + 2][2] = getTD(BaliseHTML.CENTER, null).ajout(getText(s.getNomHTML()));
				a[i + 2][3] = getTD(BaliseHTML.CENTER, null).ajout( color.ajout(getText(Integer.toString(s.getPopulation(c .getNumero())))));
				a[i + 2][4] = getTD(BaliseHTML.CENTER, null) .ajout(getFont(cC[4], null) .ajout(getText(Integer.toString(s .getPopulationMaximale(c.getNumero())))));
				a[i + 2][5] = getTD(BaliseHTML.CENTER, null).ajout(
						new BaliseHTML("span").ajout("class", className).ajout(Integer.toString(plaPossedees))
				).ajout( getFont(cC[4], null).ajout( getText("/" + plaTotal)));
				a[i + 2][6] = getTD(BaliseHTML.CENTER, null) .ajout(getText(Integer.toString(s.getTaxation(c .getNumero()))));
				a[i + 2][7] = getTD(BaliseHTML.CENTER, null).ajout( getFont(cC[4], null).ajout( getText(Integer.toString(s.getStabilite(c .getNumero())))));

				a[i + 2][8] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[3], null).ajout(getText(Integer.toString(s
								.getPointsDeConstructionModifie(c.getNumero(), c.getGouverneurSurPossession(s.getPosition()), fief,s.getPosition())))));
				a[i + 2][9] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[3], null).ajout(
								getText(Integer.toString(s.getRevenuMinerai(c
										.getNumero())))));

				a[i + 2][10] = getTD(BaliseHTML.CENTER, null)
						.ajout(getFont(cC[6], null)
								.ajout(getText(Integer.toString(fief
										.getBudget(Const.DOMAINES_BUDGET_TECHNOLOGIQUE)))));
				a[i + 2][11] = getTD(BaliseHTML.CENTER, null)
						.ajout(getText(Integer.toString(fief
								.getBudget(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX))));
				a[i + 2][12] = getTD(BaliseHTML.CENTER, null)
						.ajout(getFont(cC[4], null)
								.ajout(getText(Integer.toString(fief
										.getBudget(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE)))));
				a[i + 2][13] = getTD(BaliseHTML.CENTER, null)
						.ajout(getText(Commandant.getListeCommandants(s
								.getProprios())));
				if (c.existenceGouverneurSurPossession(p[i]))
					a[i + 2][14] = getTD(BaliseHTML.CENTER, null)
							.ajout(getALienI(LIEN_GOUVERNEURS)
									.ajout(getFont(cC[3], null)
											.ajout(getText(c
													.getGouverneurSurPossession(
															p[i]).getNom()))));
				else
					a[i + 2][14] = getTD(null, null).ajout(
							getText("&nbsp;"));


				a[i + 2][15] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Messages.POLITIQUES[(c.getPossession(p[i]))
								.getPolitique()]));
				String constructionEnCoursCode = c.getPossession(p[i]).getProgrammationConstruction();
				String constructionEnCours = "";
				if(constructionEnCoursCode != null){
					constructionEnCours = "<span class='technologie'>"
					+ Univers.getTechnologie(constructionEnCoursCode).getNomComplet(Locale.getDefault())
					+ "</span>";
				}
				a[i + 2][16] = getTD(BaliseHTML.CENTER, null).ajout(getText(constructionEnCours));
				a[i + 2][17] = getTD(BaliseHTML.CENTER, null).ajout(((int)s.getInfluenceRayonnement(c.getNumero()))+"");
			}
			return getABorne(LIEN_RESUME_SYSTEME).ajout(
					getDiv().ajout(DocumentHTML.creerTable(getTable("table_full resume_systeme stripped"), a)));
		} else
			return vide();
	}

	public BaliseHTML getSystemes() {
		String[] t = (String[]) Univers.getMessageRapport("SYSTEMES_GENERAL",
				c.getLocale());

		BaliseHTML lien = getABorne("SYSTEMES_GENERAL");
		ajouterLienPrincipal(getALienE(PRINCIPAL + "#SYSTEMES_GENERAL").ajout(
				getText(t[0])));

		BaliseHTML racine = getDiv().ajout(lien);
		racine.ajout(getTitreSection().ajout(getText(t[0])));
		if (c.existenceCapitale())
			racine.ajout(getP().ajout(getFont(null, "4").ajout(getText(t[1])))
					.ajout(getFont(cC[5], "4").ajout(
							getText(Univers.getSysteme(c.getCapitale())
									.getNomPositionHTML(c.getLocale())))));
		else
			racine.ajout(getP().ajout(getFont(null, "4").ajout(getText(t[1])))
					.ajout(getFont(cC[5], "4").ajout(getText(t[2]))));
		Position[] p = c.listePossession();
		for (int i = 0; i < c.getNombrePossessions(); i++)
			racine.ajout(getSysteme(c.getNumero(), Univers.getSysteme(p[i]),
					c.getPossession(p[i]), c.getLocale()));

		return racine;
	}

	public String convertir(float a) {
		DecimalFormat df = new DecimalFormat("###.00");
		return df.format(a);
	}

	private String getPlaneteBuildingSpan(Planete pl, List<Batiment> batList, String title){
		String tooltip = String.join("&#10;", batList.stream().distinct().map(t2 -> pl.getBatimentsDeType(t2.getCode()).length + " " + t2.getNomComplet(Locale.getDefault())).toList());
		int count = batList.size();
		String nombre = count > 0 ? "<span class='plus'>" + count + "</span>" : "0";
		return count>0 ? "<span data-tooltip=\""+tooltip+"\">"+title+":&nbsp;"+nombre+"</span>" : "<span>" +title + ":&nbsp;0</span>";
	};

	public BaliseHTML getSysteme(int num, Systeme s, Possession p, Locale l) {

		String[] t = (String[]) Univers.getMessageRapport("SYSTEME",
				c.getLocale());
		int ligne = 0;

		BaliseHTML lien = getABorne(s.getPosition().toString());
		ajouterLienSecondaire(getALienE(
				PRINCIPAL + "#" + s.getPosition().toString()).setTexteContenu(
				s.getNomPosition()));

		BaliseHTML[][] a = new BaliseHTML[1500][6];
		a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getALienI(LIEN_RESUME_SYSTEME).ajout(
						getImage(getCheminEtoile(s.getTypeEtoile()), 0, 0)));
		a[ligne][1] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
				getFont(cC[5], "4").ajout(getText(s.getNomPosition(l))));
		a[ligne++][2] = getTD(BaliseHTML.CENTER, "3")
				.ajout(getFont(cC[4], null).ajout(getText(t[0])))
				.ajout(getText(s
                        .getNombrePlanetesPossedees(num)
						+ "/"
						+ s.getNombrePlanetes()))
				.ajout(getALienE(getNomDocHTMLPlanetes(s.getPosition(), num))
						.ajout(getText(t[1])));

		if (c.existenceGouverneurSurPossession(s.getPosition())) {
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg").ajout(
					getFont(cC[7], null).ajout(getText(t[31])));
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(
					getALienI(LIEN_GOUVERNEURS).ajout(
							getFont(cC[3], null).ajout(
									getText(c.getGouverneurSurPossession(
											s.getPosition()).getNom()))));
		}

		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText(t[2])));

		for (int i = 0; i < Const.NB_RACES; i++)
			if (s.populationPresente(num, i)) {
				a[ligne][0] = getTD(null, null).ajout(getRace(i));
				a[ligne][1] = getTD(null, null).ajout(
						getSpanRace(i).ajout(
								getText(Integer.toString(s.getPopulationDeRace(
										num, i)))));
				a[ligne][2] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[3])));
				a[ligne][3] = getTD(null, null)
						.ajout(getSpanRace(i).ajout(
								getText(Integer.toString(s
										.getPopulationMaximaleDeRace(num, i)))));
				a[ligne][4] = getTD(null, null).ajout(
						getFont(cC[4], null).ajout(getText(t[4])));
				a[ligne++][5] = getTD(null, null)
						.ajout(getSpanRace(i)
								.ajout(getText(s
                                        .getAugmentationMoyennePopulationDeRace(
                                                num, i)
										+ "%")));
			}
		if (ligne != 2) {
			for (int i = 0; i < 6; i++)
				a[ligne][i] = getTD(null, null).setTexteContenu("&nbsp;");
			ligne++;
		}
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[5])));
		a[ligne][1] = getTD(null, null).ajout(
				getText(Integer.toString(s.getPopulation(num))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[6])));
		a[ligne][3] = getTD(null, null).ajout(
				getText(Integer.toString(s.getPopulationMaximale(num))));
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[7])));
		a[ligne++][5] = getTD(null, null).ajout(
				getText(s
                        .getAugmentationMoyennePopulation(num) + "%"));

		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText(t[8])));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[9])));
		int nbMine = s.getNombreDeMineUtile(num);
		String indicationMine = nbMine == s.getNombreDeMineMax(num)
				? "<span class='plus'>"+ nbMine + "</span>"
				: nbMine+"";
		a[ligne][1] = getTD(null, null).ajout(
				getText( indicationMine + "/"+ s.getNombreDeMineMax(num))
		);
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[10])));
		a[ligne][3] = getTD(null, null).ajout(
				getText(s.getStockMinerai(num) + " (<span class='plus'>+"
						+ s.getRevenuMinerai(num) + "</span>)"));
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[11])));
		a[ligne++][5] = getTD(null, null).ajout(
				getText(Integer.toString(s.getPointsDeConstructionModifie(num,
						c.getGouverneurSurPossession(s.getPosition()), p,
						s.getPosition()))));

		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText("Encombrement")));
		a[ligne][1] = getTD(null, null)
				.ajout(getText(s.getEncombrement(num) + ""));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText("Encombrement Max")));
		a[ligne][3] = getTD(null, null).ajout(
				getText(s.getCapaciteEncombrement() + ""));
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText("Encombrement Total")));
		float pourcentageEncombrement = (float)s.getEncombrement() / s.getCapaciteEncombrement() * 100;
		a[ligne++][5] = getTD(null, null).ajout("%05.2f".formatted(pourcentageEncombrement)+"%");

		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText(t[12])));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[13])));
		a[ligne][1] = getTD(null, null).ajout(
				getText(Integer.toString(s.getTaxation(num))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[14])));
		a[ligne][3] = getTD(null, null).ajout(
				"<span data-tooltip=\"Chance d'avoir au moins une planète en révolte: "+s.calculerChanceRevolteGlobale(num)+"%\">" + s.getStabilite(num) + "</span>"
		);
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[15])));
		a[ligne++][5] = getTD(null, null).ajout(
				getText(Utile.maj(p.getDescriptionPolitique(l))));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[16])));
		a[ligne][1] = getTD(null, null).ajout(
				"<span class='cur plus'>+"+Utile.a1DS(s.getRevenu(num,
						c.getGouverneurSurPossession(s.getPosition()), p))+"</span>"
		);
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[17])));
		a[ligne][3] = getTD(null, null).ajout(
				getText(s.getNombrePlanetesRevoltees(num)
						+ "/"
						+ s.getNombrePlanetesPossedees(num)));
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[18])));
		a[ligne++][5] = getTD(null, null).ajout(
				getText(Integer.toString(s.getTerraformation(num))));

		a[ligne][0] = getTD(null, null).ajout(getFont(cC[4], null).ajout("Entretien"));
		a[ligne][1] = getTD(null, null).ajout(
				"<span class='cur moins'>%,.1f</span>".formatted(-s.getEntretien(num, c.getGouverneurSurPossession(s.getPosition()), p))
		);
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout("Evolution de la stabilité")
						.ajout("data-tooltip", p.getStringEvolutionStabilite(c, s, false))
		);
		a[ligne][3] = getTD(null, null).ajout(p.getStringEvolutionStabilite(c, s, true));
		a[ligne][4] = getTD(null, null).ajout(getFont(cC[4], null).ajout(getText("Rayonnement"))
				.ajout("data-tooltip", " revenu/10  + encombrement + entretien + (expérience gouverneur ) " +
						"+ (nb produit de luxe) + ( nb holofilm) " +
						"+ (nb metaux précieux ) + (nb LIXIAM) * 10"));
		a[ligne++][5] = getTD(null, null).ajout(((int)s.getInfluenceRayonnement(c.getNumero()))+"");


		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText(t[19])));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[20])));
		a[ligne][1] = getTD(null, null)
				.ajout(getText(p.getBudget(Const.DOMAINES_BUDGET_TECHNOLOGIQUE) + "% "));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[21])));
		a[ligne][3] = getTD(null, null).ajout(
				getText(p.getBudget(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX)+ "%"));
		a[ligne][4] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[22])));
		a[ligne++][5] = getTD(null, null).ajout(
				getText(p.getBudget(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE)+ "% "));
		if (p.possedeProgrammationConstruction()) {
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg").ajout(
					getFont(cC[7], null).ajout(getText(t[32])));
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(
					getFont(cC[3], null).ajout(
							getText((Univers.existenceTechnologie(p
									.getProgrammationConstruction()) ? Utile
									.maj(Univers.getTechnologie(
											p.getProgrammationConstruction())
											.getNomComplet(c.getLocale())) : p
									.getProgrammationConstruction()))));
		}

		if (p.constructionEnCours()) {
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
					.ajout("class", "bg").ajout(
					getFont(cC[7], null).ajout(getText(t[23])));
			a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout(getText(t[24])));
			a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout(getText(t[25])));
			a[ligne][2] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(cC[4], null).ajout(getText(t[26])));
			a[ligne++][3] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(cC[4], null).ajout(getText(t[27])));
			Construction[] listeC = p.listeConstructions();
			for (int i = 0; i < listeC.length; i++) {
				a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getText((Univers.existenceTechnologie(listeC[i]
								.getCode()) ? Univers.getTechnologie(
								listeC[i].getCode()).getNomComplet(
								c.getLocale()) : listeC[i].getCode())));
				a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Integer.toString(listeC[i].getNombre())));
				a[ligne][2] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
						getText(Integer.toString(listeC[i]
								.getPointsNecessaires())));
				a[ligne++][3] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2)
						.ajout(getText(listeC[i].getPlanete() == Integer.MIN_VALUE ? t[28]
								: Integer.toString(listeC[i].getPlanete() + 1)));
			}
		}

		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText(t[29])));
		Map.Entry[] listeE = s.listeEquipement(num);
		int k;
		for (k = 0; k < listeE.length; k++) {
			Technologie techno = Univers.getTechnologie((String) listeE[k].getKey());
			String infobulle = techno.getInfobulle(l);
			a[ligne][(k % 3) * 2] = getTD(null, null)
					.ajout(getSpan("technologie")
							.ajout(getText((Integer) listeE[k].getValue() == 1
									? Utile.maj(techno.getNomComplet(l))
									: Utile.maj(techno.getNomPlurielComplet(l))))

							.ajout("data-tooltip", infobulle));
			a[ligne][1 + (k % 3) * 2] = getTD(null, null).ajout(
					getText(((Integer) listeE[k].getValue()).toString()));
			if (k % 3 == 2)
				ligne++;
		}
		if (k % 3 == 1)
			a[ligne++][5] = getTD(null, "4").setTexteContenu("&nbsp;");
		if (k % 3 == 2)
			a[ligne++][5] = getTD(null, BaliseHTML.T_2).setTexteContenu(
					"&nbsp;");

		// Ajout liste des planètes
		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText("Détail des planètes")));


		Planete[] planetesList = s.getPlanetes();
		BaliseHTML[][] b = new BaliseHTML[planetesList.length+1][11];
		int bligne = 0;

		// Header du tableau des planètes
		int bcol = 0;
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Nom")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Minerai")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Terra.")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Impôts")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Stab.")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Rév.")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Proprio.")));
		b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getText("Population")));
		b[bligne++][bcol] = getTD(BaliseHTML.CENTER, null).ajout("style", "width:450px").ajout(getFont(cC[4], null).ajout(getText("Bâtiments")));

		for (int i = 0; i < planetesList.length; i++) {
			Planete pl = planetesList[i];
			boolean proprio = pl.getProprio() == num;
			bcol = 0;
			String nomP = (pl.getNom() == null || pl.getNom().isEmpty() ? "P" + (i + 1) : pl.getNom());
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(nomP));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(
				proprio ? (pl.getStockMinerai() + "(+" + pl.calculeRevenuMinerai() + ")") : "-"
			));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(
					proprio ? Integer.toString(pl.getTerraformation()) : "-"
			));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(
					proprio ? Integer.toString(pl.getTaxation()) : "-"
			));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(
					proprio ? pl.getStabilite() +"%" : "-"
			));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getText(
					proprio ? (pl.getRevolte() ? "<span class=\"moins\">Oui</span>" : "Non") : "-"
			));
			b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(Univers.getCommandant(pl.getProprio()).getNomNumeroHtml());

			int[] racesPresentes = pl.racesPresentes();
			if(racesPresentes.length == 0){
				b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getSpan().ajout(getText("0/0")));
			} else {
				int race = racesPresentes[0];
				String classeRace = "race" + race;
				b[bligne][bcol++] = getTD(BaliseHTML.CENTER, null).ajout(getSpan(classeRace).ajout(
						pl.getPopActuelle(race)+ "/" + pl.calculeMaxPop(race)
				));
			}


			String summary = "";
			// Trop de choses à afficher, il faut les regrouper :
			// mine / production / construction / bouclier / batterie / Autre
			List<Batiment> bat_mines = pl.getBatimentsParType(BatimentType.MINAGE);
			List<Batiment> bat_production = pl.getBatimentsParType(BatimentType.PRODUCTION);
			List<Batiment> bat_construction = pl.getBatimentsParType(BatimentType.CONSTRUCTION);
			List<Batiment> bat_bouclier = pl.getBatimentsParType(BatimentType.BOUCLIER);
			List<Batiment> bat_arme = pl.getBatimentsParType(BatimentType.ARME);
			List<Batiment> bat_autre = pl.getBatimentsParType(null);

			summary += getPlaneteBuildingSpan(pl, bat_mines, "Mine")+",";
			summary += getPlaneteBuildingSpan(pl, bat_production, "Production")+",";
			summary += getPlaneteBuildingSpan(pl, bat_construction, "Construction")+",";
			summary += getPlaneteBuildingSpan(pl, bat_bouclier, "Bouclier")+",";
			summary += getPlaneteBuildingSpan(pl, bat_arme, "Défense")+",";
			summary += getPlaneteBuildingSpan(pl, bat_autre, "Autre");

			b[bligne++][bcol] = getTD(null, null).ajout(getDiv().ajout("class", "batiments-list").ajout(summary));
		}

		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(DocumentHTML.creerTable(getTable("table_full stripped"), b));

		// poste  commercial
		int[] proprios = s.getProprios();
		List<String> liens = new ArrayList<>();
		for (int j = 0; j < proprios.length; j++) {
			liens.add(getALienI(getLienPosteCo(s.getPosition(), proprios[j]))
					.ajout(getFont(cC[5], null)
							.ajout(getText(Univers.getCommandant(proprios[j]).getNomNumeroHtml())))
					.toString());
		}

		BaliseHTML[][] posteTable = new BaliseHTML[5][4*2];
		posteTable[0][0] = getTD(BaliseHTML.CENTER, "8")
				.ajout("class", "bg").ajout(
				getFont(cC[7], null).ajout(getText("Poste commercial : " + String.join(", ", liens))));
		for (int col = 0; col < 4; col++) {
			for(int row = 0; row < 4; row++) {
				int marchandise = col*4 + row;
				// entête
				posteTable[row+1][col*2] = getTD(null, null)
						.ajout("data-marchandise", marchandise+"")
                        .ajout("<div class='marchandise'>" + getText(Utile.maj(Univers.getMessage("MARCHANDISES", marchandise,c.getLocale()))) + "</div>");

				// stock + prod
                int prod = s.getProductionMarchandise(c.getNumero(), marchandise);
                int stock = c.getPossession(s.getPosition()).getQuantiteMarchandise(marchandise);
                posteTable[row+1][col*2 + 1] = getPosteCell(stock, prod, marchandise);
			}
		}
		a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(DocumentHTML.creerTable(getTable("table_full stripped"), posteTable));

		return getDiv()
				.ajout(lien)
				.ajout(DocumentHTML.creerTable(getTable("table_full"), a)).ajout(sautP());
	}

	public BaliseHTML getPostesCommerciaux() {
		String[] t = (String[]) Univers.getMessageRapport("POSTES_COMMERCIAUX",
				c.getLocale());
		BaliseHTML racine = getDiv();

//		ajouterLienPrincipal(getSpan("menu_separator").ajout(getText("Poste commerciaux")));
		racine.ajout(getTitreSection().ajout(getText(t[0])));
		Position[] p = c.listePossession();
		Possession[] po = c.listeVraiePossession();
		racine.ajout(getPostes(p, po, null, t, true));

		racine.ajout(sautP()).ajout(getTitreSection().ajout(getText(t[2])));
		ArrayList pE = new ArrayList();
		ArrayList poE = new ArrayList();
		ArrayList nE = new ArrayList();
		for (int i = 0; i < p.length; i++) {
			Systeme s = Univers.getSysteme(p[i]);
			int[] proprio = s.getProprios();
			for (int j = 0; j < proprio.length; j++)
				if (proprio[j] != c.getNumero()) {
					pE.add(p[i]);
					poE.add(Univers.getCommandant(proprio[j]).getPossession(p[i]));
					nE.add(Integer.valueOf(proprio[j]));
				}
		}
		Flotte[] f = c.listeFlottes();
		for (int i = 0; i < f.length; i++)
			if ((!pE.contains(f[i].getPosition()))
					&& (Univers.existenceSysteme(f[i].getPosition()))) {
				Systeme s = Univers.getSysteme(f[i].getPosition());
				int[] proprio = s.getProprios();
				for (int j = 0; j < proprio.length; j++)
					if (proprio[j] != c.getNumero()) {
						pE.add(f[i].getPosition());
						poE.add(Univers.getCommandant(proprio[j])
								.getPossession(f[i].getPosition()));
						nE.add(Integer.valueOf(proprio[j]));
					}
			}
		racine.ajout(getPostes((Position[]) pE.toArray(new Position[0]),
				(Possession[]) poE.toArray(new Possession[0]),
				Utile.transformer((Integer[]) nE.toArray(new Integer[0])), t, true));

		return racine;
	}

	/**
	 * permet de créer une cellule de tableau pour un poste avec les classes en fonction du stock et de la production
	 */
	public BaliseHTML getPosteCell(int stock, int prod, int marchandise) {
		List<String> classes = new ArrayList<>(List.of("poste"));
		if(stock >= 100) classes.add("bonus");
		else if(stock>0) classes.add("small");
		if(prod == 0) classes.add("noprod");
		String description = "<span class='stock'>%d</span><span class='prod %s'>(+%d)</span>".formatted(
				stock, prod>0 ? "plus": "", prod
		);

		return getTD(BaliseHTML.CENTER, null)
				.ajout("class", String.join(" ", classes))
				.ajout(getText(description));
	}

	public BaliseHTML getPostes(Position[] p, Possession[] po, int[] n, String[] t, boolean addLink) {
		// Dimensions : NB_MARCHANDISES lignes de données + 1 en-tête, p.length colonnes de données + 1 libellé
		BaliseHTML[][] a = new BaliseHTML[Const.NB_MARCHANDISES + 1][p.length + 1];
		BaliseHTML lien = getABorne("POSTE" + (n == null ? "1" : "2"));

		if (addLink) {
			ajouterLienPrincipal(getALienE(
					PRINCIPAL + "#POSTE" + (n == null ? "1" : "2"))
					.ajout(getText("Postes commerciaux"
							+ (n == null ? "" : " étrangers"))));
		}

		if(p.length == 0 || po.length == 0){
			return lien;
		}

		// En-tête [0][0] : label général
		a[0][0] = getTD(BaliseHTML.CENTER, null)
				.ajout(getFont(cC[3], null).ajout(getTextI(t[1])));

		// En-tête [0][i+1] : une colonne par système
		for (int i = 0; i < p.length; i++) {
			Systeme s = Univers.getSysteme(p[i]);
			if (addLink) {
				ajouterLienSecondaire(getALienE(
						PRINCIPAL + "#" + getLienPosteCo(p[i], n == null ? c.getNumero() : n[i]))
						.setTexteContenu(s.getNomPosition()));
			}


            String description = "<span class=\"systeme\">" + s.getNom() + "</span>";
            description += "<span class=\"position\">" + s.getPosition() .getDescription()+ "</span>";
            if(n != null){
                description += Univers.getCommandant(n[i]).getNomNumeroHtml();
            }
			a[0][i + 1] = getTD(BaliseHTML.CENTER, null)
					.ajout(
							getALienI(p[i].toString())
									.ajout("name", getLienPosteCo(p[i], n == null ? c.getNumero() : n[i]))
									.ajout("class", "entete").ajout(description));
		}

		// Une ligne par marchandise
		for (int j = 0; j < Const.NB_MARCHANDISES; j++) {
			// Colonne 0 : nom de la marchandise
			a[j + 1][0] = getTD("left", null)
					.ajout("data-marchandise", j+"")
					.ajout("<div class='marchandise'>" + getText(Utile.maj(Univers.getMessage("MARCHANDISES", j, c.getLocale())))+ "</div>");

			// Une cellule par système : stock (production)
			for (int i = 0; i < p.length; i++) {
				Systeme s = Univers.getSysteme(p[i]);
				int prod = s.getProductionMarchandise(n == null ? c.getNumero() : n[i], j);
				int stock = po[i].getQuantiteMarchandise(j);
				a[j + 1][i + 1] = getPosteCell(stock, prod, j);
			}
		}

		return getDiv()
				.ajout("class", "container")
				.ajout(DocumentHTML.creerTable(getTable(), a).ajout(BaliseHTML.CLASS, "poste_commerciaux table_full stripped"));
	}

	public BaliseHTML getResumeFlottes() {
		if (c.getNombreDeFlottes() > 0) {
			String[] t = (String[]) Univers.getMessageRapport("RESUME_FLOTTES",
					c.getLocale());
			Map.Entry[] m = c.listeFlottesEtNumeros();

			ajouterLienPrincipal(getALienE(PRINCIPAL + "#" + LIEN_RESUME_FLOTTE)
					.ajout(getText("Résumé des Flottes")));

			BaliseHTML[][] a = new BaliseHTML[2 + c.getNombreDeFlottes()][t.length - 1];
			a[0][0] = getTD(BaliseHTML.CENTER, Integer.toString(t.length - 1))
					.ajout(getTitreTable().ajout(getText(t[0])));
			for (int i = 0; i < t.length - 1; i++)
				a[1][i] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText(t[i + 1])));
			for (int i = 0; i < m.length; i++) {
				Flotte f = (Flotte) m[i].getValue();
				int num = ((Integer) m[i].getKey()).intValue();

				a[i + 2][0] = getTD(BaliseHTML.CENTER, null).ajout(f.getNomNumeroHTML(num));
				a[i + 2][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(f.getPosition().getDescription()));
				if (f.getPosition().equals(f.getDirection()))
					a[i + 2][2] = getTD(null, null).setTexteContenu("&nbsp;");
				else
					a[i + 2][2] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(f.getDirection().getDescription()));
				a[i + 2][3] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[6], BaliseHTML.T_2).ajout(
								getText(Utile.maj(f.getDescriptionDirective(c
										.getLocale())))));
				a[i + 2][4] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[4], null)
								.ajout(getText(f
										.getDescriptionCapaciteMouvement(
												c.getHerosSurFlotte(num),
												c.getLocale(), c))));
				a[i + 2][5] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Integer.toString(f.getForceSpatiale())));
				a[i + 2][6] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[4], null)
								.ajout(getText(Integer.toString(f
										.getForcePlanetaire()))));
				a[i + 2][7] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(null, BaliseHTML.T_2).ajout(
								getText(Utile.maj(f.getDescriptionEtat(c
										.getLocale())))));
					a[i + 2][8] = getTD(BaliseHTML.CENTER, null).setTexteContenu(f.capaciteNavireUsine()+"");

				if (c.existenceHerosSurFlotte(num))
					a[i + 2][9] = getTD(BaliseHTML.CENTER, null).ajout(
							getALienI(LIEN_HEROS).ajout(
									getFont(cC[3], null).ajout(
											getText(c.getHerosSurFlotte(num)
													.getNom()))));
				else
					a[i + 2][9] = getTD(null, null).ajout(getText("&nbsp;"));
				a[i + 2][10] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[6], BaliseHTML.T_2).ajout(
								getText(f.getNombreDeVaisseaux() + "")));

				String nomConstruction = f.getConstructionEnCours() != null
						? f.getConstructionEnCours()
						: "";
				a[i + 2][11] = getTD(BaliseHTML.CENTER, null).setTexteContenu(nomConstruction);

			}
			return getABorne(LIEN_RESUME_FLOTTE).ajout(
					getDiv().ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a)));
		} else
			return vide();
	}

	public BaliseHTML getFlottes() {
		String[] t = (String[]) Univers.getMessageRapport("FLOTTES",
				c.getLocale());

		ajouterLienPrincipal(getALienE(PRINCIPAL + "#" + DETAIL_FLOTTE).ajout(
				getText("Flottes")));

		BaliseHTML racine = getDiv().ajout(getABorne(DETAIL_FLOTTE));
		racine.ajout(getTitreSection().ajout(getText(t[0])));
		racine.ajout(getP().ajout(
				getFont(null, "4").ajout(
						getALienE(DETAIL_FLOTTE).ajout(getText(t[1])))));
		Map.Entry[] m = c.listeFlottesEtNumeros();
		for (int i = 0; i < m.length; i++)
			racine.ajout(getFlotte((Flotte) m[i].getValue(),
					((Integer) m[i].getKey()).intValue(), t));

		return racine;
	}

	public BaliseHTML getFlotte(Flotte f, int num, String[] t) {
		BaliseHTML[][] a = new BaliseHTML[50][4];
		int ligne = 0;

		ajouterLienSecondaire(getALienE(PRINCIPAL + "#" + getLienFlotte(num))
				.setTexteContenu(f.getNomNumeroHTML(num)));

		a[ligne][0] = getTD(null, null).ajout(
				getABorne(getLienFlotte(num)).ajout(
						getFont(cC[3], null).ajout(getText(t[2]))));
		a[ligne++][1] = getTD(BaliseHTML.CENTER, "3").ajout(
				getALienE(DETAIL_FLOTTE + "#" + getLienFlotte(num)).ajout(
						getSpan("flotte_desc").ajout(f.getNom())));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[3], null).ajout(getText(t[3])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(getText(Integer.toString(num + 1))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[3], null).ajout(getText(t[4])));
		a[ligne++][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(f.getPosition().getDescription())));
		if ((c.existenceHerosSurFlotte(num))
				|| (!f.getDirection().equals(f.getPosition()))) {
			if (c.existenceHerosSurFlotte(num)) {
				a[ligne][0] = getTD(null, null).ajout(
						getFont(cC[3], null).ajout(getText(t[6])));
				a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getALienI(LIEN_HEROS).ajout(
								getFont(cC[6], null).ajout(
										getText(c.getHerosSurFlotte(num)
												.getNom()))));
			} else
				a[ligne][0] = getTD(null, BaliseHTML.T_2).setTexteContenu(
						"&nbsp;");
			if (!f.getDirection().equals(f.getPosition())) {
				a[ligne][2] = getTD(null, null).ajout(
						getFont(cC[3], null).ajout(getText(t[5])));
				a[ligne][3] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[6], null).ajout(
								getText(f.getDirection().getDescription())));
			} else
				a[ligne][2] = getTD(null, BaliseHTML.T_2).setTexteContenu(
						"&nbsp;");
			ligne++;
		}

		if (f.aCapaciteNavireUsine()) {
			a[ligne][0] = getTD(null, "2").ajout(
					getFont(cC[3], null).ajout(getText(t[23])));
			a[ligne][1] = getTD(BaliseHTML.CENTER, "2").ajout(
					getFont(cC[6], null).ajout(
							getText(f.getConstructionEnCours())));
			ligne++;
		}

		Vaisseau[] v = f.listeVaisseauxCargos();

		if (v.length > 0) {

			a[ligne][0] = getTD(null, "2").ajout(
					getFont(cC[3], null).ajout(getText(t[24])));
			a[ligne][1] = getTD(BaliseHTML.CENTER, "2").ajout(
					getFont(cC[6], null).ajout(
							getText(f.sommeCapaciteCargo(true) + "")));
			ligne++;
		}

		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[7])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(Utile.maj(f.getDescriptionDirective(c
								.getLocale())))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[8])));
		a[ligne++][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(Utile.maj(f.getDescriptionPuissance(c
								.getLocale())))));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[9])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(Integer.toString(f.getForceSpatiale()))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[10])));
		a[ligne++][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(Integer.toString(f.getForcePlanetaire()))));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[11])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(f.getDescriptionCapaciteMouvement(
								c.getHerosSurFlotte(num), c.getLocale(), c))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[12])));
		a[ligne++][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null)
						.ajout(getText(Utile.maj(f.getDescriptionEtat(c
								.getLocale())))));
		a[ligne][0] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[13])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null).ajout(
						getText(Utile.maj(f.getDescriptionExperience(c
								.getLocale())))));
		a[ligne][2] = getTD(null, null).ajout(
				getFont(cC[4], null).ajout(getText(t[14])));
		a[ligne++][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[6], null)
						.ajout(getText(Utile.maj(f.getDescriptionMoral(c
								.getLocale())))));

		Map.Entry[] hm = f.listeVaisseauxParType();
		int i = 0;
		for (i = 0; i < hm.length; i++) {
				String nomVso = (String) hm[i].getKey();
				PlanDeVaisseau pV = Univers.getPlanDeVaisseau(nomVso);
				String infoBulle = "nothing";
				if (pV != null) {
					infoBulle = pV.getInfoBulle(c.getLocale());
				}

				a[ligne][2 * (i % 2)] = getTD(null, null).ajout(
						new BaliseHTML("span", nomVso).ajout("data-tooltip", infoBulle).ajout("class", "pdv"));
			a[ligne][1 + 2 * (i % 2)] = getTD(null, null).ajout(
					getText(((Integer) hm[i].getValue()).toString()));
			if (i % 2 == 1)
				ligne++;
		}
		if (i % 2 == 1)
			a[ligne++][3] = getTD(null, BaliseHTML.T_2).setTexteContenu(
					"&nbsp;");

		int memoire = ligne++;
		boolean mem = false;
		if (Univers.existenceSysteme(f.getPosition())) {
			mem = true;
			Systeme s = Univers.getSysteme(f.getPosition());
			int[] proprios = s.getProprios();
			String intermediaire = "";
			for (int j = 0; j < proprios.length; j++) {
				intermediaire = intermediaire
						+ getALienI(
								getLienPosteCo(s.getPosition(), proprios[j]))
								.ajout(getFont(cC[5], null).ajout(
										getText(Univers.getCommandant(
												proprios[j]).getNomNumeroHtml())))
								.toString();
				if (j != proprios.length - 1)
					intermediaire = intermediaire + " - ";
			}
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "6").ajout(
					getText(intermediaire));
		}
		if (mem)
			a[memoire][0] = getTD(BaliseHTML.CENTER, "4").ajout(
					getFont(cC[4], null).ajout(getText(t[15])));

		v = f.listeVaisseauxCargos();
		BaliseHTML[][] b = null;
		if (v.length > 0) {
			b = new BaliseHTML[1 + v.length][4];
			for (int j = 0; j < 4; j++)
				b[0][j] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText(t[16 + j])));
			for (int j = 0; j < v.length; j++) {
				ObjetTransporte[] oT = v[j].listeCargaison();
				b[j + 1][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getFont(cC[4], null).ajout(
								getText(Integer.toString(j + 1))));
				String nomVso = v[j].getType();
				PlanDeVaisseau pV = Univers.getPlanDeVaisseau(nomVso);
				String infoBulle = "";
				if (pV != null) {
					infoBulle = pV.getInfoBulle(c.getLocale());
				}

				b[j + 1][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(nomVso).ajout("data-tooltip", infoBulle));
				if (oT.length > 0)
					b[j + 1][2] = getTD(BaliseHTML.CENTER, null).ajout(
							getFont(cC[6], BaliseHTML.T_2).ajout(
									getText(ObjetTransporte
											.getDescriptionListeChargement(oT,
													c.getLocale()))));
				else
					b[j + 1][2] = getTD(null, null).setTexteContenu("&nbsp;");
				b[j + 1][3] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(v[j].capaciteCargoUtilisee()
								+ "/"
								+ v[j].capaciteCargo(true)));
			}
		}

		int[] table_vs = { Const.COMPOSANT_CAPACITE_VILLE_SPATIALE,
				Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO,
				Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE,
				Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE };

		String[] desc = { "Citadin", "Scientifique", "Integriste", "Espions" };

		float[] potentiel = new float[table_vs.length];
		float potentielVS = 0;
		for (int g = 0; g < table_vs.length; g++) {
			potentiel[g] = f.getCapaciteVilleSpatiale(table_vs[g]);
			potentielVS = potentielVS + potentiel[g];
		}

		BaliseHTML[][] vs = null;
		if (potentielVS > 0) {
			int size = 0;
			for (int g = 0; g < table_vs.length; g++)
				if (potentiel[g] > 0)
					size = size
							+ f
									.getPopulationVilleSpatiale(table_vs[g])
									.entrySet().toArray(new Map.Entry[0]).length
							+ 1;

			vs = new BaliseHTML[size + 2][4];
			vs[0][0] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout("Population Pr&eacute;sente"));
			vs[0][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout("Pop actuelle"));
			vs[0][2] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout("Pop max"));
			vs[0][3] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[4], null).ajout("Revenus"));

			int ct = 1;
			for (int g = 0; g < table_vs.length; g++)
				if (potentiel[g] > 0) {
					Map.Entry[] m = (Map.Entry[]) f
							.getPopulationVilleSpatiale(table_vs[g]).entrySet()
							.toArray(new Map.Entry[0]);
					vs[ct][0] = getTD(null, "2").ajout(
							getFont(cC[3], null).ajout(desc[g]));
					vs[ct][1] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(""
									+ f.getCapaciteVilleSpatiale(table_vs[g])));
					vs[ct++][2] = getTD(BaliseHTML.CENTER, null)
							.ajout(getText(""
									+ f.entretienVilleSpatiale(table_vs[g])));
					for (int j = 0; j < m.length; j++) {
						vs[ct][0] = getTD(BaliseHTML.CENTER, null).ajout(
								getRace(((Integer) m[j].getKey()).intValue()));
						vs[ct][1] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(m[j].getValue().toString()));
						vs[ct][2] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(""));
						vs[ct++][3] = getTD(BaliseHTML.CENTER, null).ajout(
								getText(""));
					}

				}

			/**
			 *
			 *
			 * vs[0][0]=getTD(BaliseHTML.CENTER,null).ajout(getFont(cC[4],null).
			 * ajout(getText(t[20])));
			 * vs[0][1]=getTD(BaliseHTML.CENTER,null).ajout
			 * (getText(Integer.toString(potentielVS)));
			 * vs[1][0]=getTD(BaliseHTML
			 * .CENTER,null).ajout(getFont(cC[3],null).ajout(getText(t[21])));
			 * vs[1][1]=getTD(BaliseHTML.CENTER,null).ajout(getFont(cC[3],null).
			 * ajout(getText(t[22]))); for(int j=0;j<m.length;j++){
			 * vs[j+2][0]=getTD(BaliseHTML.CENTER,null).ajout(getRace(
			 * ((Integer)m[j].getKey()).intValue() ));
			 * vs[j+2][1]=getTD(BaliseHTML
			 * .CENTER,null).ajout(getText(m[j].getValue().toString())); }
			 */

		}

		BaliseHTML retour = getDiv().ajout("class", "flotte").ajout(
				DocumentHTML.creerTable(getTable(), a));
		if (b != null)
			retour.ajout(new BaliseHTML(BaliseHTML.BR)).ajout(
					DocumentHTML.creerTable(getTable(), b));
		if (vs != null)
			retour.ajout(new BaliseHTML(BaliseHTML.BR)).ajout(
					DocumentHTML.creerTable(getTable(), vs));
		return retour.ajout(sautP());
	}

	public BaliseHTML getResumeTechnologies() {
		String[] t = (String[]) Univers.getMessageRapport(
				"RESUME_TECHNOLOGIES", c.getLocale());
		BaliseHTML racine = getDiv();
		racine.ajout(getABorne(LIEN_RESUME_TECHNOLOGIE).ajout(getText("")));
		racine.ajout(getTitreSection().ajout(getText(t[0])));

		BaliseHTML lien = getABorne("TECHNO");

		racine.ajout(lien);

		BaliseHTML[][] a = null;
		String[] re = c.recherchesActuelles();
		if (re.length > 0) {
			a = new BaliseHTML[2 + re.length][3];
			a[0][0] = getTD(BaliseHTML.CENTER, "3").ajout(
					getTitreSection().ajout(getText(t[23])));
			for (int i = 0; i < 3; i++)
				a[1][i] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreTable().ajout(getText(t[24 + i])));
			for (int i = 0; i < re.length; i++) {
				a[2 + i][0] = getTD(null, null).ajout(
						getText(Univers.getTechnologie(re[i]).getNomHTML(c.getLocale())));
				a[2 + i][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(c.pourcentageAffecte(re[i])
								+ "%"));
				a[2 + i][2] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Integer.toString(c
								.nombreDePointsDeRecherche(re[i]))));
			}
			racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a)
					.setTexteContenu("<BR><BR>"));
		}

		racine.ajout(getTitreSection().ajout(getText("<BR><BR>" + t[1])));
		racine.ajout(getTitreCaption().ajout(
				getText("<BR><BR>" + t[22] + "<BR><BR>")));
		a = new BaliseHTML[600][8];
		int ligne = 0;
		Technologie[] tab = Technologie.transformationCode(c
				.listeTechnologiesConnues());
		// ecrireTechnologies(chemin+"/technologies.txt",tab);
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_BATIMENT, t);
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU, t);
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_SIMPLE, t);
		racine.ajout(
				DocumentHTML.creerTable(getTable("table_full stripped"), a)).ajout(sautP());

		racine.ajout(getTitreSection().ajout(getText(t[2])));
		a = new BaliseHTML[200][8];
		ligne = 0;
		tab = Technologie.transformationCode(c
				.listeTechnologiesPouvantEtreCherchees());
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_BATIMENT, t);
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU, t);
		ligne = ajoutTableauTechnologique(a, ligne, tab,
				Const.TECHNOLOGIE_TYPE_SIMPLE, t);

		return racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a));
	}

	public int ajoutTableauTechnologique(BaliseHTML[][] a, int ligne,
			Technologie[] tabO, int type, String[] t) {
		Technologie[] tab = Univers.trierTechnologies(tabO, type);

		if (tab.length > 0) {
			String inter = null;
			if (type == Const.TECHNOLOGIE_TYPE_BATIMENT)
				inter = t[3];
			else if (type == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU)
				inter = t[11];
			else
				inter = t[18];
			a[ligne++][0] = getTD(BaliseHTML.CENTER, "8").ajout(
					getTitreTable().ajout(getText(inter)));

			if (type == Const.TECHNOLOGIE_TYPE_BATIMENT) {
				a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText(t[4])));
				a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText("Seuil")));
				for (int i = 1; i < 6; i++)
					a[ligne][i + 1] = getTD(BaliseHTML.CENTER, null).ajout(
							getTitreCaption().ajout(getText(t[4 + i])));
			} else if (type == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {
				a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText(t[12])));
				a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText("Seuil")));
				for (int i = 1; i < 5; i++)
					a[ligne][i + 1] = getTD(BaliseHTML.CENTER, null).ajout(
							getTitreCaption().ajout(getText(t[12 + i])));
				a[ligne][6] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
						getTitreCaption().ajout(getText(t[17])));
			} else {
				a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText(t[19])));
				a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getTitreCaption().ajout(getText("Seuil")));
				a[ligne][2] = getTD(BaliseHTML.CENTER, "4").ajout(
						getTitreCaption().ajout(getText(t[20])));
				a[ligne][3] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
						getTitreCaption().ajout(getText(t[21])));
			}
			ligne++;

			for (int i = 0; i < tab.length; i++)
				a[ligne++] = getResumeTechnologie(tab[i]);
		}

		return ligne;
	}

	public BaliseHTML[] getResumeTechnologie(Technologie t) {
		BaliseHTML[] r = new BaliseHTML[8];
		boolean isPublic = Univers.estTechnologiePublique(t.getCode());
		String nom = Utile.maj(t.getNomComplet(c.getLocale()));
		r[0] = getTD(null, null).ajout(
				getALienE(DETAIL_TECHNOLOGIES + "#" + t.getCode())
						.ajout(t.getNomHTML(c.getLocale())));
		r[1] = getTD(BaliseHTML.CENTER, null).ajout(
				getText("" + t.getPointsDeRecherche() ));
		if (t.estBatiment()) {
			Batiment b = (Batiment) t;
			r[2] = getTD(BaliseHTML.CENTER, null).ajout(
					"<span class='cur'>" + Utile.a1DS(b.getPrix()) + "</span>"
			);
			r[3] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(b.getPointsDeConstruction() + " (" + ObjetTransporte
							.getEncombrementChargement(b.getCode()) + ")"));
			r[4] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(b.getMineraiNecessaire())));
			r[5] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[6], BaliseHTML.T_2).ajout(
							getText(b.getListeMarchandises(c.getLocale()))));
			r[6] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(t.getDescriptionParents(c.getLocale()))));
		}
		if (t.estComposantDeVaisseau()) {
			ComposantDeVaisseau b = (ComposantDeVaisseau) t;
			r[2] = getTD(BaliseHTML.CENTER, null).ajout(
					"<span class='cur'>" + Utile.a1DS(b.getPrix()) + "</span>"
			);
			r[3] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(b.getNombreDeCasesPrises())));
			r[4] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(b.getMineraiNecessaire())));
			r[5] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(cC[6], BaliseHTML.T_2).ajout(
							getTextI(b.getListeMarchandises(c.getLocale()))));
			r[6] = getTD(BaliseHTML.CENTER, null).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(t.getDescriptionParents(c.getLocale()))));
		}
		if (t.estTechnologieSimple()) {
			r[2] = getTD(null, "4").ajout(
					getText(t.getDescription(c.getLocale())));
			r[3] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(null, BaliseHTML.T_2).ajout(
							getText(t.getDescriptionParents(c.getLocale()))));
		}
		return r;
	}

	public BaliseHTML getDetailTechnologies() {
		String[] t = (String[]) Univers.getMessageRapport("TECHNOLOGIES",
				c.getLocale());
		BaliseHTML racine = getDiv();
		racine.ajout(getFont(cC[3], "4").ajout(getText(t[0])));
		ajouterLienPrincipal(getALienE(PRINCIPAL + "#TECHNO").ajout(
				getText("Technologies")));
		racine.ajout(getFont(cC[3], "5").ajout(getText("<BR><BR>" + t[1])));

		Technologie[] tab = Technologie.transformationCode(c
				.listeTechnologiesConnues());
		for (int i = 0; i < tab.length; i++)
			racine.ajout(getTechnologie(tab[i], t));

		racine.ajout(sautP()).ajout(getFont(cC[3], "3").ajout(getText(t[2])));
		tab = Technologie.transformationCode(c
				.listeTechnologiesPouvantEtreCherchees());
		for (int i = 0; i < tab.length; i++)
			racine.ajout(getTechnologie(tab[i], t));

		return getBody().ajout(racine);
	}

	public BaliseHTML getTechnologie(Technologie te, String[] t) {
		BaliseHTML[][] a = new BaliseHTML[10][10];

		ajouterLienSecondaire(getALienE(
				DETAIL_TECHNOLOGIES + "#" + te.getCode()).setTexteContenu(
				Utile.maj(te.getNomComplet(c.getLocale()))));

		a[0][0] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
				getABorne(te.getCode()).ajout(
						getFont(cC[4], null).ajout(getText(t[3]))));
		a[0][1] = getTD(BaliseHTML.CENTER, "8").ajout(
				getALienE(PRINCIPAL + "#" + LIEN_RESUME_TECHNOLOGIE).ajout(
						getFont(cC[1], null).ajout(
								getText(Utile.maj(te.getNomComplet(c
										.getLocale()))))));
		a[1][0] = getTD(BaliseHTML.CENTER, "10").ajout(
				getFont(cC[7], null).ajout(
						getText(te.getDescription(c.getLocale()))));

		if (te.estBatiment())
			for (int i = 0; i < 5; i++)
				a[2][i] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
						getFont(cC[4], null).ajout(getText(t[4 + i])));
		else if (te.estComposantDeVaisseau())
			if (((ComposantDeVaisseau) te).estArme())
				for (int i = 0; i < 10; i++)
					a[2][i] = getTD(BaliseHTML.CENTER, null).ajout(
							getFont(cC[4], null).ajout(getText(t[9 + i])));
			else {
				for (int i = 0; i < 3; i++)
					a[2][i] = getTD(BaliseHTML.CENTER, null).ajout(
							getFont(cC[4], null).ajout(getText(t[9 + i])));
				a[2][3] = getTD(BaliseHTML.CENTER, "7").ajout(
						getFont(cC[4], null).ajout(getText(t[12])));
			}

		if (te.estBatiment()) {
			Batiment b = (Batiment) te;
			a[3][1] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					"<span class='cur'>" + Utile.a1DS(b.getPrix()) + "</span>"
			);
			a[3][3] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getText(Integer.toString(b.getPointsDeConstruction())));
			a[3][2] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getText(Integer.toString(b.getMineraiNecessaire())));
			a[3][4] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getFont(cC[6], BaliseHTML.T_2).ajout(
							getText(b.getListeMarchandises(c.getLocale()))));
			a[3][5] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
					getText(Integer.toString(b.getPointsDeStructure())));
		}
		if (te.estComposantDeVaisseau()) {
			ComposantDeVaisseau b = (ComposantDeVaisseau) te;
			a[3][0] = getTD(BaliseHTML.CENTER, null).ajout(
					"<span class='cur'>" + Utile.a1DS(b.getPrix()) + "</span>"
			);
			a[3][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(b.getMineraiNecessaire())));
			a[3][2] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(b.getNombreDeCasesPrises())));
			a[3][3] = getTD(BaliseHTML.CENTER, (b.estArme() ? null : "7"))
					.ajout(getFont(cC[6], BaliseHTML.T_2).ajout(
							getText(b.getListeMarchandises(c.getLocale()))));
			if (b.estArme()) {
				int[] inter2 = ((Arme) b).getCaracArme();
				for (int i = 0; i < inter2.length; i++)
					a[3][i + 4] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(Integer.toString(inter2[i])));
				a[4][0] = getTD(BaliseHTML.CENTER, "10").ajout(
						getFont(cC[4], null).ajout(getTextI(t[19])));
				for (int i = 0; i < 10; i++)
					a[5][i] = getTD(BaliseHTML.CENTER, null).ajout(
							getFont(cC[7], null).ajout(
									getText(Integer.toString(i + 1))));
				inter2 = ((Arme) b).getChanceToucher();
				for (int i = 0; i < 10; i++)
					a[6][i] = getTD(BaliseHTML.CENTER, null).ajout(
							getText(inter2[i] + "%")).ajout(
							BaliseHTML.WIDTH, "10%");
			}
		}

		String cara = te.getDescriptionCaracteristiquesSpeciales(c.getLocale());
		if (cara != null) {
			a[7][0] = getTD(BaliseHTML.CENTER, "10").ajout(
					getFont(cC[4], null).ajout(getTextI(t[20])));
			a[8][0] = getTD(BaliseHTML.CENTER, "10").ajout(getTextI(cara));
		}

		return DocumentHTML.creerTable(getTable("table_full stripped"), a)
				.ajout(sautP());
	}

	public BaliseHTML getStrategiesDeCombat() {
		String[] liste = c.listeCodesStrategies();

		if (liste.length > 0) {
			String[] t = (String[]) Univers.getMessageRapport("STRATEGIES", c.getLocale());

			BaliseHTML racine = getDiv();

			BaliseHTML lien = getABorne("STRATEGIES");
			racine.ajout( lien );

			ajouterLienPrincipal(getALienE(PRINCIPAL + "#STRATEGIES").ajout( getText("Stratégies")));

			racine.ajout(getTitreSection().ajout(getText(t[0])));

			ArrayList<ArrayList<BaliseHTML>> globalBalisesList = new ArrayList<ArrayList<BaliseHTML>>();
			BaliseHTML temp;
			ArrayList<BaliseHTML> balisesList;

			for (int i = 0; i < liste.length; i++) {
				StrategieDeCombatSpatial strategie = c.getStrategie(liste[i]);

				balisesList = new ArrayList<BaliseHTML>();

				for (int j = 0; j < 3; j++){
					temp = getTD(BaliseHTML.CENTER, "4").ajout(getFont(cC[3], null).ajout(getTextI(t[j + 1])));
					balisesList.add(temp);
				}
				globalBalisesList.add(balisesList);

				balisesList = new ArrayList<BaliseHTML>();

				temp = getTD(BaliseHTML.CENTER, "4").ajout(getText(strategie.getNom()));
				balisesList.add(temp);
				temp = getTD(BaliseHTML.CENTER, "4").ajout(getText(Utile.maj(Univers.getMessage("STRATEGIE_AGRESSIVITE",strategie.getAgressivite(), c.getLocale()))));
				balisesList.add(temp);
				temp = getTD(BaliseHTML.CENTER, "4").ajout(getText(Utile.maj(Univers.getMessage("STRATEGIE_CIBLE", strategie.getTypeCible(), c.getLocale()))));
				balisesList.add(temp);

				globalBalisesList.add(balisesList);

				String[] inter3 = strategie.getTypesDeVaisseau();
				if (inter3.length != 0) {
					balisesList = new ArrayList<BaliseHTML>();
					for (int j = 0; j < 12; j++){
						temp = getTD(BaliseHTML.CENTER, null).ajout(getFont(cC[4], null).ajout(getTextI(t[j + 4])));
						balisesList.add(temp);
					}

					globalBalisesList.add(balisesList);

					for (int k = 0; k < inter3.length; k++) {

						balisesList = new ArrayList<BaliseHTML>();
						temp = getTD(BaliseHTML.CENTER, null).ajout(getText(inter3[k]));
						balisesList.add(temp);

						int[] inter2 = strategie.getPositionnement(inter3[k]);
						String posi = inter2[0] + "-" + inter2[1];
						temp = getTD(BaliseHTML.CENTER, null).ajout( getText(posi));
						balisesList.add(temp);

						for (int m = 0; m < 10; m++){
							temp = getTD(BaliseHTML.CENTER, null).ajout(getText(Integer.toString(strategie.getCibles(inter3[k])[m])));
							balisesList.add(temp);
						}
						globalBalisesList.add(balisesList);
					}
				}
			}

			BaliseHTML[][] a = new BaliseHTML[globalBalisesList.size()][12];

			int counter1 = 0;
			for(ArrayList<BaliseHTML> listLevel1:globalBalisesList){

				int counter2 = 0;
				for(BaliseHTML balise:listLevel1){
					a[counter1][counter2] = balise;
					counter2++;
				}

				counter1++;
			}

			return racine.ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a));
		} else
			return vide();
	}

	// méthodes servant autrepart.

	public static BaliseHTML getPopulation(Systeme[] s, Locale l) {
		String[] t = (String[]) Univers.getMessageRapport("STATS_UNIVERS_POPULATION", l);

		BaliseHTML[][] a = new BaliseHTML[4 + Const.NB_RACES][2];
		a[0][0] = getTD(BaliseHTML.CENTER, BaliseHTML.T_2).ajout(
				getFont(cC[3], "4").ajout(getText(t[0])));
		a[1][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(t[1])));
		a[1][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(t[2])));
		for (int i = 0; i < Const.NB_RACES; i++) {
			a[i + 2][0] = getTD(BaliseHTML.CENTER, null).ajout(getRace(i, l));
			a[i + 2][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getCouleurRace(i).ajout(
							getText(Integer.toString(Systeme
									.getPopulationTotaleDeRace(s, i)))));
		}
		for (int i = 0; i < 2; i++)
			a[2 + Const.NB_RACES][i] = getTD(null, null).setTexteContenu(
					"&nbsp;");
		a[3 + Const.NB_RACES][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getText(t[3]));
		a[3 + Const.NB_RACES][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getText(Integer.toString(Systeme.getPopulationTotale(s))));

		return getDiv().ajout(DocumentHTML.creerTable(getTable(), a)).ajout(
				sautP());
	}

	public static BaliseHTML getPoste(int[][] tab, Locale l) {
		String[] t = (String[]) Univers.getMessageRapport( "STATS_UNIVERS_POSTE", l);

		BaliseHTML[][] a = new BaliseHTML[2 + Const.NB_MARCHANDISES][3];
		a[0][0] = getTD(BaliseHTML.CENTER, "3").ajout(
				getFont(cC[3], "4").ajout(getText(t[0])));
		a[1][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(t[1])));
		a[1][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(t[2])));
		a[1][2] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[4], null).ajout(getText(t[3])));
		for (int i = 0; i < Const.NB_MARCHANDISES; i++) {
			a[i + 2][0] = getTD(BaliseHTML.CENTER, null)
					.ajout(getText(Utile.maj(Univers.getMessage("MARCHANDISES",
							i, l))));
			a[i + 2][1] = getTD(BaliseHTML.CENTER, null).ajout(
					getText(Integer.toString(tab[i][0])));
			a[i + 2][2] = getTD(BaliseHTML.CENTER, null)
					.ajout(getText(Float.toString(Utile
							.a1D(((float) tab[i][1]) / 10))));
		}

		return getDiv().ajout(DocumentHTML.creerTable(getTable("table_full stripped"), a)).ajout(
				sautP());
	}

	public static BaliseHTML getRelations(int galaxie, int secteur, Locale l) {
		String[] t = (String[]) Univers.getMessageRapport( "STATS_UNIVERS_RELATIONS", l);

		BaliseHTML[][] a = new BaliseHTML[2 + Const.NB_RACES][1 + Const.NB_RACES];
		a[0][0] = getTD(BaliseHTML.CENTER, Integer.toString(1 + Const.NB_RACES))
				.ajout(getFont(cC[3], "4").ajout(getText(t[0])));
		a[1][0] = getTD(null, null).setTexteContenu("&nbsp;");
		for (int i = 0; i < Const.NB_RACES; i++)
			a[1][i + 1] = getTD(BaliseHTML.CENTER, null).ajout(getRace(i, l));
		for (int i = 0; i < Const.NB_RACES; i++) {
			a[i + 2][0] = getTD(BaliseHTML.CENTER, null).ajout(getRace(i, l));
			for (int j = i; j < Const.NB_RACES; j++)
				a[i + 2][j + 1] = getTD(BaliseHTML.CENTER, null).ajout(
						getText(Univers.getRelationRaces(
                                galaxie, secteur, i, j)
								+ " ("
								+ Utile.maj(Univers.getMessage("RELATIONS",
										5 + Systeme.niveauRelation(Univers
												.getRelationRaces(galaxie,
														secteur, i, j)), l))
								+ ")"));
			for (int j = 0; j < i; j++)
				a[i + 2][j + 1] = getTD(null, null).setTexteContenu("&nbsp;");
		}

		return getDiv().ajout(DocumentHTML.creerTable(getTable(), a)).ajout(
				sautP());
	}

	public static void ecrireLiensSites(Locale l) {
		String[] t = (String[]) Univers.getMessageRapport(
				"STATS_UNIVERS_SITES", l);

		BaliseHTML[][] a = new BaliseHTML[100][2];
		a[0][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[3], null).ajout(getText(t[0])));
		a[0][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[3], null).ajout(getText(t[1])));
		Map alliances = new TreeMap();
		Map commandants = new TreeMap();
		Alliance[] aL = Univers.getListeAlliances();
		for (int i = 0; i < aL.length; i++)
			if (aL[i].aUnSite())
				alliances.put(aL[i].getNom(), aL[i].getSite());
		Commandant[] co = Univers.getListeCommandantsHumains();
		for (int i = 0; i < co.length; i++)
			if (co[i].aUnSite())
				commandants.put(co[i].getNomNumeroHtml(), co[i].getSite());
		Map.Entry[] ma = (Map.Entry[]) alliances.entrySet().toArray(
				new Map.Entry[0]);
		Map.Entry[] mc = (Map.Entry[]) commandants.entrySet().toArray(
				new Map.Entry[0]);
		for (int i = 0; i < Math.max(alliances.size(), commandants.size()); i++) {
			if (i < commandants.size())
				a[1 + i][0] = getTD(BaliseHTML.CENTER, null).ajout(
						getALienE((String) mc[i].getValue()).ajout(
								getText((String) mc[i].getKey())).ajout(
								BaliseHTML.TARGET, "_blank"));
			else
				a[1 + i][0] = getTD(null, null).setTexteContenu("&nbsp;");
			if (i < alliances.size())
				a[1 + i][1] = getTD(BaliseHTML.CENTER, null).ajout(
						getALienE((String) ma[i].getValue()).ajout(
								getText((String) ma[i].getKey())).ajout(
								BaliseHTML.TARGET, "_blank"));
			else
				a[1 + i][1] = getTD(null, null).setTexteContenu("&nbsp;");
		}

		BaliseHTML div = getDiv().ajout(
				DocumentHTML.creerTable(
						getTable().ajout(BaliseHTML.WIDTH, "80%"), a));
		DocumentHTML d = getDocument(Chemin.STATS + "liensJ.htm", t[2],
				getBody().ajout(div));
		d.ecrire();
	}

	public static void ecrireRapportCombat(Locale l) {
		String[] t = (String[]) Univers.getMessageRapport("RAPPORT_COMBAT", l);
		BaliseHTML[][] a = new BaliseHTML[400][5];
		int ligne = 0;
		a[ligne][0] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[8], null).ajout(getText(t[0])));
		a[ligne][1] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[8], null).ajout(getText(t[1])));
		a[ligne][2] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[8], null).ajout(getText(t[2])));
		a[ligne][3] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[8], null).ajout(getText(t[3])));
		a[ligne++][4] = getTD(BaliseHTML.CENTER, null).ajout(
				getFont(cC[8], null).ajout(getText(t[4])));
		for (int i = 0; i < Const.NB_GALAXIES; i++)
			for (int j = 0; j < Const.NB_SECTEURS; j++) {
				RapportCombat[] r = Univers.getRapportsCombat(i, j + 1);
				if (r.length != 0)
					a[ligne++][0] = getTD(BaliseHTML.CENTER, "5").ajout(
							getFont(cC[3], null).ajout(
									getText(Univers.getMessage("NOMS_GALAXIES",
											i, l)
											+ " : <span class='position'>secteur"
											+ (j + 1)
											+ "</font>")));

				// +COULEURS_RACES[Utile.getCouleurSecteur(j+1)]+

				for (int k = 0; k < r.length; k++)
					a[ligne++] = r[k].getDescription();
			}

		BaliseHTML div = getDiv().ajout(DocumentHTML.creerTable(getTable(), a));
		DocumentHTML z = getDocument(
				Chemin.MJ + "combats_tour" + Univers.getTour() + ".htm",
				"liste des combats", getBody().ajout(div));
		DocumentHTML d = getDocument(Chemin.STATS + "combats.htm",
				"liste des combats", getBody().ajout(div));
		d.ecrire();
		z.ecrire();
	}

	/*
	 * public void getresumeTech(){ FileWriter fluxEcriture; String
	 * laius="Technologies Connues\n\n"; Technologie[]
	 * tab=Technologie.transformationCode(c.listeTechnologiesConnues());
	 * Technologie[]
	 * tab1=Univers.trierTechnologies(tab,Const.TECHNOLOGIE_TYPE_BATIMENT);
	 * Technologie[] tab2=Univers.trierTechnologies(tab,Const.
	 * TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU); Technologie[]
	 * tab3=Univers.trierTechnologies(tab,Const.TECHNOLOGIE_TYPE_SIMPLE);
	 * laius+="Batiments\n"; for(int z=0;z<tab1.length;z++){
	 * laius=laius+Utile.maj(tab1[z].getNomComplet(Locale.FRENCH))+"\n";}
	 * laius+="Composants\n"; for(int z=0;z<tab2.length;z++){
	 * laius=laius+Utile.maj(tab2[z].getNomComplet(Locale.FRENCH))+"\n";}
	 * laius+="Maitrise\n"; for(int z=0;z<tab2.length;z++){
	 * laius=laius+Utile.maj(tab2[z].getNomComplet(Locale.FRENCH))+"\n";}
	 *
	 * fluxEcriture = new FileWriter(chemin+TECHNOLOGIES);
	 * fluxEcriture.write(laius); fluxEcriture.close(); }
	 */

}
