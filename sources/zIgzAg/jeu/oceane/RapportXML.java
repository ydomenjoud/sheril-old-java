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
		return creerNode(nom.toLowerCase(), documentG, attribut, valeur);
	}

	public Element creerNode(String nom, Element parent, String[] attribut, String[] valeur) {
		return creerNode(nom.toLowerCase(), parent, attribut, valeur, null);

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

		Element retour = (Element) (documentG.createElement(nom.toLowerCase()));
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);
		if (textContent != null)
			retour.setTextContent(textContent);

		parent.appendChild(retour);

		return retour;
	}

	public Element creerNode(String nom, Element parent) {

		Element retour = (Element) (documentG.createElement(nom.toLowerCase()));
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

		Element retour = (Element) documentG.createElement(nom.toLowerCase());
		for (int i = 0; i < attribut.length; i++)
			retour.setAttribute(attribut[i], valeur[i]);

		parent.appendChild(retour);

		return retour;

	}

	// Constructeur
	public RapportXML(Commandant commandant) {

		c = commandant;
		chemin = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour() + "/";
        Locale locale = Locale.FRENCH;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// ------------------------construction du fichier general
			// xml--------------------------
			documentG = builder.newDocument();

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
					creerNode("B",budget, new String[] { "type", "valeur" }, new String[] { Utile.maj(budgetListe[i]),Float.toString(Utile.a1D(c.getBudget(i))) });
				}
			}
			
			// PNA //
            Element pna = creerNode("PNA", com);
			int[] lpna = c.listePactesDeNonAgression();
			for (int i = 0; i < lpna.length; i++)
				creerNode("P", pna, new String[] { "com" }, new String[] { ""
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
            Element systems = creerNode("SYSTEMES", com);
			Systeme[] listeS = Univers.listeSystemes(c.listePossession());
			for (int i = 0; i < listeS.length; i++) {
				Systeme s = listeS[i];
				Possession poss = c.getPossession(s.getPosition());
				Element sys = creerNode("S",
                        systems,
						new String[] { "pos", "nom", "typeEtoile", "nbpla", "politique", "btech", "besp", "bcont","hscan","revenu","entretien","pdc"},
						new String[] {
								"" + s.getPosition(),
								s.getNom(),
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
                Element planetes = creerNode("PLANETES", sys);
				Planete[] listeP = s.getPlanetes();
				for (int a = 0; a < listeP.length; a++) {
					Planete p = listeP[a];

					Element pla = creerNode(
							"P",
                            planetes,
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
            Element flottes = creerNode("FLOTTES", com);
			Flotte[] listeF = c.listeFlottes();
			for (int i = 0; i < listeF.length; i++) {
				Flotte f = listeF[i];
				int num = c.numeroFlotte(f);
				Heros h = c.getHerosSurFlotte(num);
				Position d = f.getDirection();
				if (d == null)
					d = new Position(-1, -1, -1);
				Element flotte = creerNode(
						"F",
                        flottes,
						new String[] { "num", "pos", "direction", "vitesse", "as",
								"ap", "puissance", "nom", "directive", "directive_precision", "hscan" },
						new String[] { "" + num, 
								"" + f.getPosition(), 
								"" + ( d.equals(f.getPosition()) ? "" : d),
								"" + f.getCapaciteMouvement(false, h, c),
								"" + f.getForceSpatiale(),
								"" + f.getForcePlanetaire(),
                                "" + f.getPuissance(),
								"" + f.getNom(),
                                "" + f.getDirective(),
                                "" + f.getDirectivePrecision(),
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
				Element posteElement = creerNode("P", postesCommerciaux,
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
            Element lieutenants = creerNode("LIEUTENANTS", com, new String[] {}, new String[] {});
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
						"L",
                        lieutenants,
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
						new String[] { "pos", "nom", "nbpla", "pop", "popMax","typeEtoile" },
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
							Element message = creerNode("M",messages, new String[] {"type"}, new String[] {type}, liste[i].get(j).toString() );
//							sb.append(liste[i].get(j));
						}
//						Element message = creerNode(type,messages, new String[] {"description"}, new String[] { sb.toString() });
					}
				}
			}

            // vaisseaux connus ( sans les publiques )
            PlanDeVaisseau[] vlist = c.listePlansConnusNonPublics();
            Element vlistNode = creerNode("PLANS", com, new String[]{}, new String[]{});
            for (int i = 0; i < vlist.length; i++) {
                PlanDeVaisseau pdv = vlist[i];
                Element p = creerNode("P", vlistNode,
                        new String[]{"nom", "concepteur", "marque", "tour", "taille", "vitesse", "pc", "centaures", "minerai", "as", "ap", "royalties"},
                        new String[]{pdv.getNom(), pdv.getNomConcepteurBis(locale), pdv.getMarque(locale), pdv.getTourDeCration() + "",
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
			File fg = new File(baseDir, "rapport.xml");
			StreamResult resultG = new StreamResult(fg);
			DOMSource sourceG = new DOMSource(documentG);
			transformer.transform(sourceG, resultG);

		} catch (Exception e) {

			System.out.println(e + " : " + e.getMessage());
			e.printStackTrace();
		}

	} // Fin Ã©crire rapportXML

}
