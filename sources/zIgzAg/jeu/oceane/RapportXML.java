// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RapportXML {

	public static String version = "1.152";

	private Commandant c;
	private String chemin;
	private Document documentG;

	public Element creerNode(String nom, String[] attribut, String[] valeur) {
		return creerNode(nom, documentG, attribut, valeur);
	}

	public Element creerNode(String nom, Element parent, String[] attribut, String[] valeur) {
		return creerNode(nom, parent, attribut, valeur, null);

	}

	public Element creerNode(String nom, Element parent, String[] attribut,
			String[] valeur, String textContent) {
		if (attribut.length != valeur.length) {
			System.out.println(" Erreur dans RapportXML ( creerNode "+nom+"), le nombre d'attributs et le nombre de valeur est différent ..");
			System.out.println(" Attributs : ");
			for (int i = 0; i < attribut.length; i++)
				System.out.print(attribut[i] + "-");
			System.out.println(" Valeurs: ");
			for (int i = 0; i < valeur.length; i++)
				System.out.print(valeur[i] + "-");
			System.exit(0);
		}

		Element retour = (Element) (documentG.createElement(nom));
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);
		if (textContent != null)
			retour.setTextContent(textContent);

		parent.appendChild(retour);

		return retour;
	}

	public Element creerNode(String nom, Element parent) {

		Element retour = (Element) (documentG.createElement(nom));
		parent.appendChild(retour);

		return retour;
	}

	public Element creerNode(String nom, Document parent, String[] attribut,
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

		Element retour = (Element) documentG.createElement(nom);
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);

		parent.appendChild(retour);

		return retour;

	}

	// Constructeur
	public RapportXML(Commandant commandant) {

		c = commandant;
		chemin = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour() + "/";

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// ------------------------construction du fichier general
			// xml--------------------------
			documentG = builder.newDocument();
			
			//////////////////////////////rajout du xsl pour l'affichage /////////////////
			Node pi = documentG.createProcessingInstruction("xml-stylesheet", "href=\"mep.xsl\" type=\"text/xsl\""); 
			documentG.appendChild(pi);
			/////////////////////////////////////////////////////


			// Element racine <corps>
			Element corps = creerNode("RAPPORT", new String[] { "numTour",
					"version" },
					new String[] { "" + Univers.getTour(), version });

			// Creation de l'element Commandant
			String capitale = (c.getCapitale() != null ? c.getCapitale().toString() : "" );
			Element com = creerNode("COMMANDANT", corps, 
					new String[] { "numero", "nom", "race","puissance","planetes","grade","reputation","statut","capitale" }, 
					new String[] { c.getNumero() + "", c.getNom(), c.getRace() + "", c.getPuissance()+"",c.getNombrePlanetesPossedees()+"",c.getGrade(),c.getReputation()+"",c.getStatutReputation(), capitale });

			// Element Budget
			Element budget = creerNode(
					"BUDGETS",
					com,
					new String[] { "totPrec", "totRec", "totDep" },
					new String[] {
							"" + Utile.a1D(c.getBudget(0)),
							"" + Utile.a1D(c.getBudget(Const.BUDGET_COMMANDANT_TOTAL_RECETTE)),
							"" + Utile.a1D(c.getBudget(Const.BUDGET_COMMANDANT_TOTAL_DEPENSE)) });
			
			String[] budgetListe = Univers.getTableauMessage("CHAMPS_BUDGET", c.getLocale());
			
			for (int i = 1; i < Const.BUDGET_COMMANDANT_TOTAL_DEPENSE; i++){
				if ( i != Const.BUDGET_COMMANDANT_TOTAL_RECETTE &&  c.getBudget(i) != 0F) {
					creerNode("BUDGET",budget, new String[] { "type", "valeur" }, new String[] { Utile.maj(budgetListe[i]),Float.toString(Utile.a1D(c.getBudget(i))) });
				}
			}
			
			// PNA //
			int[] lpna = c.listePactesDeNonAgression();
			for (int i = 0; i < lpna.length; i++)
				creerNode("PNA", com, new String[] { "com" }, new String[] { ""
						+ lpna[i] });

			// ALLIANCE
			int[] numerosAlliances = c.numerosAlliances();
			for (int i = 0; i < numerosAlliances.length; i++){
				Alliance alliance = Univers.getAlliance(numerosAlliances[i]);
				Element allianceElement = creerNode("ALLIANCE", com, 
						new String[] { "nom","type","createur","dirigeant", "droits","secrete" }, 
						new String[] { alliance.getNom(),  alliance.getDescriptionType(Locale.getDefault()), alliance.getNumeroConcepteur()+"",
										alliance.getNumeroDirigeant()+"", alliance.getDroitsEntree()+"", (alliance.estSecrete() ? 1 : 0) + ""});
			
				// liste des membres
				Commandant[] liste = alliance.getAdherents();
				for(Commandant adherent:liste){
					creerNode("COM", allianceElement, new String[] { "num" }, new String[] { ""+ adherent.getNumero() });
				}
			}
			
			

			// Les technologies connues et recherchables
			Element techno = creerNode("TECHNOLOGIES", com);

			String[] listeTC = c.listeTechnologiesNonPubliquesConnues();
			for (int a = 0; a < listeTC.length; a++) {
				Element technoc = creerNode("CONNUE", techno,
						new String[] { "code" }, new String[] { listeTC[a] });
			}

			String[] listeTR = c.listeTechnologiesPouvantEtreCherchees();
			for (int a = 0; a < listeTR.length; a++) {
				Element technor = creerNode("ATTEIGNABLE", techno,
						new String[] { "code" }, new String[] { listeTR[a] });
			}
			
			// Element Systeme
			Systeme[] listeS = Univers.listeSystemes(c.listePossession());
			for (int i = 0; i < listeS.length; i++) {
				Systeme s = listeS[i];
				Possession poss = c.getPossession(s.getPosition());
				Element sys = creerNode("SYSTEME",
						com,
						new String[] { "pos", "nom", "typeEtoile", "nombrePla", "politique", "btech", "besp", "bcont","hscan","revenu","entretien","pdc"},
						new String[] {
								"" + s.getPosition(),
								"" + s.getNom(),
								"" + s.getTypeEtoile(),
								"" + s.getNombrePlanetes(),
								"" + poss.getPolitique(),
								"" + poss.getBudget(Const.DOMAINES_BUDGET_TECHNOLOGIQUE),
								"" + poss.getBudget(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE),
								"" + poss.getBudget(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX),
								"" + s.getPorteeRadar(c.getNumero()),
								"" + s.getRevenu(c.getNumero(), c.getGouverneurSurPossession(s.getPosition()), poss),
								"" + s.getEntretien(c.getNumero(), c.getGouverneurSurPossession(s.getPosition()), poss),
								"" + s.getPointsDeConstructionModifie(c.getNumero(), c.getGouverneurSurPossession(s.getPosition()), poss, s.getPosition())
						});
				
				
				// evo stab
				int[] evostabParams = poss.getEvolutionStabilite(c, s);
				String[] evonames = new String[]{"gouverneur","politique","marchandise","position","taxation","race"};
				for(int a=0;a<evonames.length;a++){
					int evo = evostabParams[a];
					if( evo != 0 ){
						Element evonode = creerNode("EVOSTAB", sys, new String[] { "t","v" }, new String[] { evonames[a],evo+"" });
					}
				}

				// Les planêtes
				Planete[] listeP = s.getPlanetes();
				for (int a = 0; a < listeP.length; a++) {
					Planete p = listeP[a];

					Element pla = creerNode(
							"PLANETE",
							sys,
							new String[] { "num", "type", "prop", "tai", "atm",
									"grav", "temp", "rad", "prod", "revenumin", "stockmin","pdc","terra",
									"tax", "stab", "revolt" },
							new String[] { "" + a, 
									"" + p.getType(),
									"" + p.getProprio(), "" + p.getTaille(),
									"" + p.getAtmosphere(),
									"" + p.getGravite(),
									"" + p.getTemperature(),
									"" + p.getRadiation(),
									"" + p.getProductionMinerai(),
									"" + p.calculeRevenuMinerai(),
									"" + p.getStockMinerai(),
									"" + p.getPointsDeConstruction(),
									"" + p.getTerraformation(),
									"" + p.getTaxation(),
									"" + p.getStabilite(),
									"" + (p.getRevolte() ? 1 : 0) });

					// Les populations
					int[] rp = p.racesPresentes();
					for (int b = 0; b < rp.length; b++) {
						if (b < 0 || b > rp.length - 1)
							System.exit(0);
						Element pop = creerNode(
								"POPULATION",
								pla,
								new String[] { "race", "popAct", "popMax",
										"popAug" },
								new String[] { "" + rp[b],
										"" + p.getPopActuelle(rp[b]),
										"" + p.calculeMaxPop(rp[b]),
										"" + p.calculeProgressionPop(rp[b]) });
					}

					// Les colons
					// int[] rp = p.racesPresentes();
					for (int b = 0; b < Const.NB_RACES - 1; b++) {
						if (!p.racePresente(b)) {
							Element col = creerNode(
									"COLONISATION",
									pla,
									new String[] { "race", "popMax" },
									new String[] { "" + b,
											"" + p.calculeMaxPop(b) });
						}
					}

					// Les batiments
					Map.Entry[] listeE = p.listeEquipement();
					for (int d = 0; d < listeE.length; d++) {
						Element bat = creerNode(
								"BATIMENT",
								pla,
								new String[] { "code", "nombre" },
								new String[] {
										Univers.getTechnologie(
												(String) listeE[d].getKey())
												.getCode(),
										""
												+ ((Integer) listeE[d]
														.getValue()).toString() });

					} // Fin batiment
				} // Fin planete
			} // fin System
			
			// Element Flotte
			Flotte[] listeF = c.listeFlottes();
			for (int i = 0; i < listeF.length; i++) {
				Flotte f = listeF[i];
				int num = c.numeroFlotte(f);
				Heros h = c.getHerosSurFlotte(num);
				Position d = f.getDirection();
				if (d == null)
					d = new Position(-1, -1, -1);
				Element flotte = creerNode(
						"FLOTTE",
						com,
						new String[] { "num", "pos", "direction", "vit", "spa",
								"pla", "nom", "dir", "nbVso","hscan" },
						new String[] { "" + num, 
								"" + f.getPosition(), 
								"" + ( d.equals(f.getPosition()) ? "" : d),
								"" + f.getCapaciteMouvement(false, h, c),
								"" + f.getForceSpatiale(),
								"" + f.getForcePlanetaire(), 
								"" + f.getNom(),
								"" + Messages.DIRECTIVES[f.getDirective()],
								"" + f.getNombreDeVaisseaux() ,
								"" + f.getPorteeScannerFlotte()
								});

				// les vso de la flotte
				Vaisseau[] v = f.listeVaisseaux();
				for (int a = 0; a < v.length; a++) {
					Element vaisseau = creerNode(
							"VAISSEAU",
							flotte,
							new String[] { "plan", "type", "exp", "race",
									"moral" },
							new String[] { "" + v[a].getPlan().getNom(),
									"" + v[a].getType(),
									"" + v[a].getExperience(),
									"" + v[a].getRaceEquipage(),
									"" + v[a].getMoral() });

				}
			}

			// Postes commerciaux
			// On récupère la liste des positions systèmes + positions détectées + système survolés
			ArrayList<Systeme> listeSystemesPosteCommerciaux = new ArrayList<Systeme>();
			for(Position p:c.listePossession()){ // liste positions possédées
				listeSystemesPosteCommerciaux.add(Univers.getSysteme(p));
			}
			for(Flotte f:c.listeFlottes()){ // Systèmes survolés
				Position p = f.getPosition();
				boolean exists = Univers.existenceSysteme(p);
				
				if( exists ){
					Systeme s = Univers.getSysteme(p);
					if(!listeSystemesPosteCommerciaux.contains(s)){
						listeSystemesPosteCommerciaux.add(s);
					}
				}
			}
			
			for(Position detected:c.getSystemesDetectes() ){ // détectés
				listeSystemesPosteCommerciaux.add(Univers.getSysteme(detected));
			}
			
			// On créé une classe intermédiaire car c'est impossible à gérer sinon
			class PosteCommercial {
				public Position position;
				public Possession possession;
				public Commandant commandant;
				public Systeme systeme;
				
				public PosteCommercial(Position position, Possession possession, Commandant commandant,Systeme systeme) {
					super();
					this.position = position;
					this.possession = possession;
					this.commandant = commandant;
					this.systeme = systeme;
				}
				
				public boolean equals(PosteCommercial pc){
					return 
						pc.position.equals(position) &&
						pc.possession.equals(possession) && 
						pc.commandant.equals(commandant) &&
						pc.systeme.equals(systeme);
				}
				
				
			}
			
			// Pour chaque position, on va récupérer une liste de possession
			ArrayList<PosteCommercial> listePossessionsPosteCommerciaux = new ArrayList<PosteCommercial>();
			for(Systeme systeme: listeSystemesPosteCommerciaux){
				// Récupération des proprios
				Position position = systeme.getPosition();
				int[] proprios = systeme.getProprios();
				for(int num:proprios){
					Commandant proprio = Univers.getCommandant(num);
					Possession possession = proprio.getPossession(position);
					PosteCommercial postecommercial = new PosteCommercial(position, possession, proprio, systeme);
					if( !listePossessionsPosteCommerciaux.contains(postecommercial)){
						listePossessionsPosteCommerciaux.add(postecommercial);
					}
				}
			}
			
			Element postesCommerciaux = creerNode("POSTES_COMMERCIAUX", com, new String[] {}, new String[] {});
			
			// Pour chaque possession on va récupérer le poste commercial
			for(PosteCommercial posteCommercial:listePossessionsPosteCommerciaux){
				Element posteElement = creerNode("POSTE", postesCommerciaux, 
						new String[] { "pos", "proprio"},
						new String[] {posteCommercial.position+"", posteCommercial.commandant.getNumero()+""  });
				
				Possession possession = posteCommercial.possession;
				Commandant proprio = posteCommercial.commandant;
				Systeme systeme = posteCommercial.systeme;
				// On ajoute toutes les marchandises
				for(int i=0; i<Messages.MARCHANDISES.length; i++){
					
					Element marchandise = creerNode("M", posteElement, 
							new String[]{"code", "nb","prod"}, 
							new String[]{i+"", possession.getQuantiteMarchandise(i)+"",systeme.getProductionMarchandise(proprio.getNumero(), i) +""});
				}
			}
			
			
			
			// Lieutenants
			Leader[] l = c.listeLieutenants();
			for (int i = 0; i < l.length; i++) {

				String position_leader = "";
				if (l[i].estHeros())
					position_leader = "" + ((Heros) l[i]).getPosition();
				else if (l[i].estGouverneur())
					position_leader = "" + ((Gouverneur) l[i]).getPosition();

				if (l[i].estEnReserve())
					position_leader = "-1";

				Element leader = creerNode(
						"LEADER",
						com,
						new String[] { "type", "pos", "niv", "race", "vit",
								"att", "def", "mor", "mar", "exp", "valeur","nom" },
						new String[] { "" + (l[i].estHeros() ? 0 : 1),
								position_leader, "" + l[i].getNiveau(),
								"" + l[i].getRace(), "" + l[i].getVitesse(),
								"" + l[i].getAttaque(), "" + l[i].getDefense(),
								"" + l[i].getMoral(), "" + l[i].getMarchand(),
								"" + l[i].getExperience(),
								"" + l[i].getValeur(),
								"" + l[i].getNom()
								});

				// les compÃ©tences des heros
				Map.Entry[] m = l[i].getListeCompetences();
				for (int a = 0; a < m.length; a++) {
					int compe = ((Integer) m[a].getKey()).intValue();
					int value = Math.max(0,
							((Integer) m[a].getValue()).intValue() - 1);
					Element competence = creerNode("COMPETENCE", leader, new String[] { "comp", "val" }, new String[] { "" + compe, "" + value });

				}

			}

			Element detections = creerNode("DETECTIONS", com, new String[] {}, new String[] {});
			int[][] b = c.getFlottesDetectees();
			for (int i = 0; i < b.length; i++) {
				Commandant cmdt = Univers.getCommandant(b[i][0]);
				Flotte f = cmdt.getFlotte(b[i][1]);
				Element detectFlotte = creerNode(
						"FLOTTE",
						detections,
						new String[] { "pos", "nom", "nbVso", "proprio",
								"puiss", "num" },
						new String[] {
								"" + f.getPosition(),
								f.getNom(),
								f.getNombreDeVaisseaux() + "",
								cmdt.getNumero() + "",
								f.getDescriptionPuissance(commandant
										.getLocale()), b[i][1] + "" });
			}

			Position[] listeDetectionsSyst = c.getSystemesDetectes();
			for (Position p : listeDetectionsSyst) {
				Systeme s = Univers.getSysteme(p);
				Element detecStyst = creerNode(
						"SYSTEME",
						detections,
						new String[] { "pos", "nom", "nbPla", "pop", "popMax","typeEtoile" },
						new String[] { "" + s.getPosition(), s.getNom(),
								s.getNombrePlanetes() + "",
								s.getPopulation(-1) + "",
								s.getPopulationMaximale(-1) + "",
								s.getTypeEtoile() + "" });
				for (int pro = 0; pro < s.getProprios().length; pro++) {
					Element competence = creerNode("PROPRIO", detecStyst, new String[] { }, new String[] { }, s.getProprios()[pro]+"" );
				}
			}
			// Les technos connnues
			ArrayList<String> tech = new ArrayList<String>();
			for(String nomTech:c.listeTechnologiesConnues()){ tech.add(nomTech); }
			for(String nomTech:c.listeTechnologiesPouvantEtreCherchees()){ tech.add(nomTech); }
			for(String nomTech:(String[])c.listeEquipementArray().toArray(new String[]{})){
				if( !tech.contains(nomTech)){ tech.add(nomTech); }
			}
			
			// Transformation String[] -> Technologie[] triÃ©e
			Technologie[] tab = Univers.trierAlphabetiquementTechnologies(Technologie.transformationCode(tech.toArray(new String[]{})));

			// Const.TECHNOLOGIE_TYPE_BATIMENT
			for (int i = 0; i < tab.length; i++) {
				Technologie t = tab[i];
                if(t == null) {
//                    System.out.println(String.join(",", tech));
//                    System.out.println(i);
                    continue;
                }
				Element technor = creerNode(
						"TECHNOLOGIE",
						corps,
						new String[] { "base", "code", "niv", "type", "recherche", "nom", "connue" },
						new String[] {
								t.getCodeBase(),
								t.getCode(), 
								"" + t.getNiveau(),
								"" + t.getTypeDeTechno(),
								"" + t.getPointsDeRecherche(),
								t.getNom(Locale.FRENCH),
								c.estTechnologieConnue(t.getCode())?"1":"0"
								});

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
							new String[] { "prix", "structure", "pc","min", "arme" },
							new String[] {
									"" + t1.getPrix(),
									t1.getPointsDeStructure() + "",
									t1.getPointsDeConstruction() + "",
									t1.getMineraiNecessaire()+"",
									(t1.getCodeArme() != null ? t1
											.getCodeArme() : "") });
				} else if (type == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {
					t2 = (ComposantDeVaisseau) t;
					Element addtech = creerNode("SPECIFICATION", technor,
							new String[] { "prix", "type", "case", "min" },
							new String[] { "" + t2.getPrix(), t2.getTypeArme(),
									t2.getNombreDeCases() + "",t2.getMineraiNecessaire()+"" });
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
									new String[] { "code", "nb" },
									new String[] { "" + march[a][0], "" + march[a][1] });

						}
				}
				Element adddesc = creerNode("DESCRIPTION", technor, new String[] {}, new String[] {}, t.getDescription(Locale.FRENCH));
				
				String[] parents = t.getParents();
				if( parents != null ){
					for(String parent:t.getParents()){
						Element addParent = creerNode("PARENT", technor, new String[] {"code"}, new String[] {Univers.getTechnologie(parent).getCode()});
					}
				}
			}
			
			
			// Messages
			Element messages = creerNode("MESSAGES",com, new String[] {}, new String[] { });

			HashMap<String, Commentaire> msgList = new HashMap<String, Commentaire>();
			msgList.put("ERR", c.getErreurs());
			msgList.put("EVT", c.getEvenements());
			msgList.put("CBT", c.getCombats());
			msgList.put("ORD", c.getOrdres());
			
			Iterator<String> msglistIterator = msgList.keySet().iterator();
			
			while(msglistIterator.hasNext()){
				String type = msglistIterator.next();
				Commentaire commentaire = msgList.get(type);
				if( commentaire.nbMessages() > 0 ){
					ArrayList[] liste = commentaire.listeMessages(c.getLocale(), Const.MESSAGE_TYPE_COMMANDANT);
					for (int i = 0; i < liste.length; i++) {
						StringBuffer sb = new StringBuffer();
						for (int j = 0; j < liste[i].size(); j++) {
							Element message = creerNode("MESSAGE",messages, new String[] {"type"}, new String[] {type}, liste[i].get(j).toString() );
//							sb.append(liste[i].get(j));
						}
//						Element message = creerNode(type,messages, new String[] {"description"}, new String[] { sb.toString() });
					}
				}
			}
			
			// LISTES
			Element data = creerNode("DATAS",corps, new String[] {}, new String[] { });

			Element marchandises = creerNode("MARCHANDISES",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.MARCHANDISES.length; i++){
				String name = Messages.MARCHANDISES[i];
				Element marchandiseElt = creerNode("M", marchandises, new String[] {"code","nom"}, new String[] {i+"",name});
			}
			
			Element politiques = creerNode("POLITIQUES",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.POLITIQUES.length; i++){
				String name = Messages.POLITIQUES[i];
				Element politiquesElt = creerNode("P", politiques, new String[] {"code","nom"}, new String[] {i+"",name});
			}

			Element caractsBatiments = creerNode("CARACTERISTIQUES_BATIMENT",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.CARAC_SPECIALES_BATIMENTS.length; i++){
				String name = Messages.CARAC_SPECIALES_BATIMENTS[i];
				Element caractsBatimentsElt = creerNode("C", caractsBatiments, new String[] {"code","nom"}, new String[] {i+"",name});
			}

			Element caractsComposants = creerNode("CARACTERISTIQUES_COMPOSANT",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.CARAC_SPECIALES_COMPOSANTS.length; i++){
				String name = Messages.CARAC_SPECIALES_COMPOSANTS[i];
				Element caractsComposantsElt = creerNode("C", caractsComposants, new String[] {"code","nom"}, new String[] {i+"",name});
			}

			Element competences = creerNode("LEADER_COMPETENCES",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.COMPETENCES_LEADER.length; i++){
				String name = Messages.COMPETENCES_LEADER[i];
				Element caractsComposantsElt = creerNode("C", competences, new String[] {"code","nom"}, new String[] {i+"",name});
			}
			
			Element races = creerNode("RACES",data, new String[] {}, new String[] { });
			for(int i=0; i<Messages.RACES.length; i++){
				String name = Messages.RACES[i];
				Element racesElt = creerNode("C", races, new String[] {"code","nom"}, new String[] {i+"",name});
			}
			
			Element listeJoueurs = creerNode("JOUEURS",data, new String[] {}, new String[] { });
			Commandant[] cl = Univers.getListeCommandantsHumains();
			for(int i=0; i<cl.length; i++){
				Commandant joueur = cl[i];
				Element joueurElt = creerNode("C", listeJoueurs, 
						new String[] {"num","nom","race","pui","pla","rep"}, 
						new String[] { joueur.numero+"", joueur.nom, joueur.race+"",joueur.getPuissance()+"", joueur.getNombrePlanetesPossedees()+"", joueur.getReputation()+"" }
				);
			}
			

		} // Fin try
		catch (Exception e) {
			System.out.println(e + " : " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void ecrireRapportXML() {

		try {

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

			// Rapport general
			File baseDir = new File(chemin);
			File fg = new File(baseDir, "Rapport.xml");
			StreamResult resultG = new StreamResult(fg);
			DOMSource sourceG = new DOMSource(documentG);
			transformer.transform(sourceG, resultG);

		} catch (Exception e) {

			System.out.println(e + " : " + e.getMessage());
			e.printStackTrace();
		}

	} // Fin Ã©crire rapportXML

}
