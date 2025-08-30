// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Flotte implements Serializable {

	static final long serialVersionUID = 8343323531539867063L;

	private String nom;

	private String constructionEnCours;

	private HashMap<Integer, Vaisseau> vaisseaux;

	private Position position;

	private Position direction;

	private int directive;

	private int directivePrecision;

	private String strategie;

	private transient boolean deplacement;

	private transient boolean larguageMines;

	// méthodes d'accès

	public void setNom(String entree) {
		nom = entree;
	}

	public void setPositionFixe(Position entree) {
		setPosition(entree);
		setDirection(entree);
	}

	public void setPosition(Position p) {
		position = (Position) p.clone();
	}

	public void setDirection(Position p) {
		direction = (Position) p.clone();
	}

	public void setDirective(int entree) {
		directive = entree;
	}

	public void setDirectiveComplete(int d1, int d2) {
		directive = d1;
		directivePrecision = d2;
	}

	public int getDirectiveComplete() {
		return directiveDonneNombre(directive, directivePrecision);
	}

	public int getDirective() {
		return directive;
	}

	public int getDirectivePrecision() {
		return directivePrecision;
	}

	public void setStrategie(String entree) {
		strategie = entree;
	}

	public void setConstructionEnCours(String c) {
		constructionEnCours = c;
	}

	public String getConstructionEnCours() {
		return constructionEnCours;
	}

	public boolean nonDeplacee() {
		return !deplacement;
	}

	public void marquerDeplacement() {
		deplacement = true;
	}

	public boolean estEnGarnison() {
		if ((nonDeplacee()) && (Univers.existenceSysteme(position)))
			return true;
		else
			return false;
	}

	public static String getNomOut(Flotte f) {
		return f.getNom();
	}

	public String getNom() {
		if (nom.equals(""))
			return "&nbsp;";
		else
			return nom;
	}

	public Position getPosition() {
		return position;
	}

	public Position getDirection() {
		return direction;
	}

	public String getNomNumero(int num) {
		return nom + "(" + Integer.toString(num + 1) + ")";
	}

	public String getDescriptionVersionMab(int numF, Commandant c, Locale l) {
		return Univers.getMessage("DESCRIPTION_FLOTTE1", l)
				+ Integer.toString(numF + 1)
				+ Univers.getMessage("DESCRIPTION_FLOTTE2", l)
				+ c.getNomNumero();
	}

	public String getDescription(int numF, String nomC, Locale l) {
		return Univers.getMessage("DESCRIPTION_FLOTTE1", l)
				+ Integer.toString(numF + 1)
				+ Univers.getMessage("DESCRIPTION_FLOTTE2", l);
	}

	public String getStrategie() {
		return strategie;
	}

	public void initialiserVaisseaux() {
		vaisseaux = new HashMap<>(11);
	}

	public Vaisseau[] listeVaisseaux() {
		return vaisseaux.values().toArray(new Vaisseau[0]);
	}

	@SuppressWarnings("unchecked")
	public Map.Entry<Integer, Vaisseau>[] listeCompleteVaisseaux() {
		return vaisseaux.entrySet().toArray(new Map.Entry[0]);
	}

	public Integer[] listeNumerosVaisseaux2() {
		return vaisseaux.keySet().toArray(new Integer[0]);
	}

	public int[] listeNumerosVaisseaux() {
		Integer[] liste = listeNumerosVaisseaux2();
		return Utile.transformer(liste);
	}

	public int numeroVaisseauDisponible() {
		int[] travail = listeNumerosVaisseaux();
		if (travail.length == 0)
			return 0;
		Arrays.sort(travail);
		for (int i = 0; i < travail.length; i++)
			if (travail[i] != i)
				return i;
		return travail.length;
	}

	public void ajouterVaisseau(Vaisseau entree) {
		vaisseaux.put(numeroVaisseauDisponible(), entree);
	}

	public void supprimerVaisseau(Integer cle) {
		vaisseaux.remove(cle);
	}

	public void supprimerVaisseau(int cle) {
		supprimerVaisseau(Integer.valueOf(cle));
	}

	public void transfererVaisseau(Flotte receptrice, int numVaisseau) {
		receptrice.ajouterVaisseau(getVaisseau(numVaisseau));
		supprimerVaisseau(numVaisseau);
	}

	public Vaisseau getVaisseau(Integer cle) {
		return vaisseaux.get(cle);
	}

	public Vaisseau getVaisseau(int cle) {
		return getVaisseau(Integer.valueOf(cle));
	}

	public int getNombreDeVaisseaux() {
		return vaisseaux.size();
	}

	// le constructeur

	private Flotte() {
	}

	public Flotte(String n, Position pos) {
		nom = n;
		setPositionFixe(pos);
		initialiserVaisseaux();
		strategie = Const.STRATEGIE_DEFAUT.getNom();
	}

	// mÃ©thodes statiques

	public static Flotte creerAuHasard(Position pos, String denomination,
			int race, int coeff) {
		Flotte flotte = new Flotte(denomination, pos);

		flotte.ajouterVaisseau(Vaisseau.creer("Intercepteur standard", race));
		int potentiel = coeff;
		PlanDeVaisseau[] lp = Univers.listePlansDeVaisseauxPublics();

		while (Univers.getTest(potentiel)) {
			Vaisseau v = Vaisseau.creer(lp[Univers.getInt(lp.length)].getNom(),
					race);
			flotte.ajouterVaisseau(v);
			potentiel = potentiel - v.getNbCases();
		}
		return flotte;
	}

	public static void deplacerVersBut() {
		Commandant[] c = Univers.getListeCommandants();
		for (int i = 0; i < c.length; i++) {

			Flotte[] f = c[i].listeFlottes();
			for (int j = 0; j < f.length; j++)
				if ((f[j].nonDeplacee()) && (!f[j].getPosition().equals(f[j].getDirection())))
					f[j].deplacer(f[j].getDirection(), f[j].getDirective(), f[j].getDirectivePrecision(), c[i].getHerosSurFlotte(c[i].numeroFlotte(f[j])), f[j].getStrategie(), c[i]);
		}
	}

	public static void choixFlotteDeDepart(Commandant c, Map m) {
		String[] v = { "Intercepteur standard", "Chasseur standard",
				"Torpilleur standard", "Bombardier standard",
				"Grand Bombardier standard", "Destroyer standard",
				"Fregate standard", "Croiseur standard",
				"Supercroiseur standard", "Eclaireur standard", "Colonisateur" };
		for (int i = 0; i < v.length; i++)
			if (!Univers.existencePlanDeVaisseau(v[i]))
				System.out.println(v[i]);
		int[] n = { 10, 20, 20, 20, 20 + Univers.getTour() * 2, 10, 10, 5, 3, 3, c.getRace() == 5 ? 0 : 10};
		int[] u = (int[]) n.clone();
		if (m.size() != 0) {
			u = new int[n.length];
			Map.Entry[] inter = (Map.Entry[]) m.entrySet().toArray(
					new Map.Entry[0]);
			for (int i = 0; i < inter.length; i++)
				u[Integer.parseInt((String) inter[i].getKey()) - 1] = Integer
						.parseInt((String) inter[i].getValue());
		}

		Flotte flotte = new Flotte(Univers.getMessage(
				"DENOMINATION_FLOTTE_DE_DEPART", c.getLocale()), c
				.getCapitale());

		// Cyborg
		if (c.getRace() == 5 && false) {
			for (int d = 0; d < 5; d++) {
				PlanDeVaisseau p = Univers.getPlanDeVaisseau("Nexus");
				Vaisseau vai = Vaisseau.creer(p.getNom(), c.getRace());
				flotte.ajouterVaisseau(vai);
			}
		}

		for (int i = 0; i < v.length; i++) {
			PlanDeVaisseau p = Univers.getPlanDeVaisseau(v[i]);
			for (int j = 0; j < u[i]; j++) {
				// centaures=centaures-p.getPrix();
				if (p == null) {
					System.out.println("p : "+v[i]);
				}
				Vaisseau vai = Vaisseau.creer(p.getNom(), c.getRace());
				flotte.ajouterVaisseau(vai);
			}
		}

		c.ajouterFlotte(flotte);
	}

	public static boolean directiveExistante(int d, int p) {
		if ((d < 0) || (d >= Const.NB_DIRECTIVES))
			return false;
		if ((d == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)
				|| (d == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)
				|| (d == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE)
				|| (d == Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR))
			if (p < 0)
				return false;
		return true;
	}

	public static String traductionDirective(int d, int p, Locale loc) {
		String inter = null;
		try {
			inter = Univers.getMessage("DIRECTIVES", d, loc);
		} catch (Exception e) {
			System.out.println(d + "-" + p + "erreur directive");
			return "";
		}
		if ((d == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)
				|| (d == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)
				|| (d == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE))
			inter = inter + Integer.toString(p + 1);
		else if (d == Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR)
			if (Univers.existenceCommandant(p))
				inter = inter + Univers.getCommandant(p).getNomNumero();
		return inter;
	}

	public static int[] retourneNumerosDirectivePossibles() {
		int[] retour = new int[5 + Univers.getNombreCommandants() + 3
				* Const.NB_PLANETES_PAR_SYSTEMES];
		int k = 0;
		for (int i = 0; i < Const.NB_DIRECTIVES; i++)
			if (i == Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR) {
				int[] inter = Univers.getNumerosCommandants();
				for (int j = 0; j < inter.length; j++)
					retour[k++] = directiveDonneNombre(i, inter[j]);
			} else if ((i == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)
					|| (i == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)
					|| (i == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE))
				for (int j = 0; j < Const.NB_PLANETES_PAR_SYSTEMES; j++)
					retour[k++] = directiveDonneNombre(i, j);
			else
				retour[k++] = directiveDonneNombre(i, 0);
		return retour;
	}

	public static String[] retourneDirectivesPossibles(Locale l) {
		String[] retour = new String[5 + Univers.getNombreCommandants() + 3
				* Const.NB_PLANETES_PAR_SYSTEMES];
		int k = 0;
		for (int i = 0; i < Const.NB_DIRECTIVES; i++)
			if (i == Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR) {
				int[] inter = Univers.getNumerosCommandants();
				for (int j = 0; j < inter.length; j++)
					retour[k++] = traductionDirective(i, inter[j], l);
			} else if ((i == Const.DIRECTIVE_FLOTTE_ATTAQUE_PLANETE)
					|| (i == Const.DIRECTIVE_FLOTTE_PILLAGE_PLANETE)
					|| (i == Const.DIRECTIVE_FLOTTE_ERADICATION_PLANETE))
				for (int j = 0; j < Const.NB_PLANETES_PAR_SYSTEMES; j++)
					retour[k++] = traductionDirective(i, j, l);
			else
				retour[k++] = traductionDirective(i, 0, l);
		return retour;
	}

	public static int[] nombreDonneDirective(int n) {
		int[] retour = new int[2];
		retour[0] = n % 100;
		retour[1] = n / 100;
		return retour;
	}

	public static int directiveDonneNombre(int d, int p) {
		return d + 100 * p;
	}

	public String getDescriptionDirective(Locale loc) {
		return traductionDirective(directive, directivePrecision, loc);
	}

	// autres mÃ©thodes

	public void initialiserEquipages() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].initialiserEquipage();
	}

	public void planifierTransmission(int numeroDonneur, int tourRetour) {
		strategie = Const.STRATEGIE_DEFAUT.getNom();
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].planifierTransmission(numeroDonneur, tourRetour);
	}

	public boolean estLoueEnPartie() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estLoue())
				return true;
		return false;
	}

	public void retourLocation(Commandant c, int tour, int proprio) {
		HashMap<Integer, List<Integer>> h = new HashMap<>();
		Vaisseau[] v = listeVaisseaux();
		int[] numeros = listeNumerosVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estLoue())
				if ((tour == -1) || (v[i].getTourRetourLocation() <= tour))
					if ((proprio == -1)
							|| (v[i].getVeritableProprietaire() == proprio)) {
						Integer cle = v[i]
								.getVeritableProprietaire();
						List<Integer> o = h.get(cle);
						if (o == null) {
							ArrayList<Integer> a = new ArrayList<>();
							a.add(i);
							h.put(cle, a);
						} else {
							o.add(i);
						}
					}

		Integer[] l = h.keySet().toArray(new Integer[0]);
		for (int i = 0; i < l.length; i++) {
			Commandant retour = Univers.getCommandant(l[i]);
			if (retour != c) {
				Flotte f = new Flotte(Univers.getMessage("RETOUR_DE_PRET",
						retour.getLocale()), position);
				List<Integer> listeV = h.get(l[i]);
				for (int j = 0; j < listeV.size(); j++) {
					v[listeV.get(j)]
							.initialiserLocation();
					f.ajouterVaisseau(v[listeV.get(j)]);
					supprimerVaisseau(numeros[listeV.get(j)]);
				}
				retour.ajouterFlotte(f);
				retour.ajouterEvenement("RETOUR_PRET_FLOTTE_0000", c
						.getNomNumero());
				c.ajouterEvenement("RETOUR_PRET_FLOTTE_0001", retour
						.getNomNumero());
			}
		}

		if (getNombreDeVaisseaux() == 0)
			c.eliminerFlotte(c.numeroFlotte(this));
	}

	public void deplacer(Position p, int d, int dp, Heros h, String strat, Commandant commandant) {
		deplacement = true;
		int capaciteMouvement = getCapaciteMouvement(true, h, commandant);
		setPosition(Position.deplacementVers(position, p, capaciteMouvement, estInterGalactique()));
		setDirection(p);
		directive = d;
		directivePrecision = dp;
		setStrategie(strat);
	}

	public String getDescriptionCapaciteMouvement(Heros h, Locale l, Commandant c) {
		String retour = Integer.toString(getCapaciteMouvement(false, h, c));
		
		if (getCapaciteMouvement(true, h, c) > 90)
			retour = retour
					+ "<BR>("
					+ Univers.getMessage("COMPETENCES_LEADER",
							Const.COMPETENCE_LEADER_VOYAGE_INTRAGALACTIQUE, l)
					+ ")";
		if ((estInterGalactique())
				|| ((h != null) && (h
						.possedeCompetence(Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE))))
			retour = retour
					+ "<BR>("
					+ Univers.getMessage("COMPETENCES_LEADER",
							Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE, l)
					+ ")";
		return retour;
	}

	public int getCapaciteMouvement(boolean intraGalactique, Heros h, Commandant commandant) {
		Vaisseau[] v = listeVaisseaux();
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < v.length; i++){
			min = Math.min(min, v[i].getCapaciteMouvementFixe(intraGalactique));
		}
		
		/**
		 * Patch zwaia sur la vitesse des vaisseaux
		 */
		if( commandant.getRace() == 2 ){
			min = min + 2;
		}
		
		if (h != null) {
			int ajout = h.getNiveauCompetence(Const.COMPETENCE_LEADER_VOYAGEUR);
			if ((intraGalactique)&& (h.possedeCompetence(Const.COMPETENCE_LEADER_VOYAGE_INTRAGALACTIQUE)))
				ajout = 100;
			if (min != Integer.MAX_VALUE)
				min = min + ajout;
			else
				min = ajout;
		}
		if (min != Integer.MAX_VALUE)
			return min;
		else
			return 0;
	}

	public boolean estInterGalactique() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (!v[i].estInterGalactique())
				return false;
		return true;
	}

	public boolean estIntraGalactique() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (!v[i].estIntraGalactique())
				return false;
		return true;
	}

	public boolean estLanceurDeMines() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estLanceurDeMines())
				return true;
		return false;
	}

	public int nbVaisseauxBombardiers() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estBombardier())
				retour++;
		return retour;
	}

	public int nbVaisseauxChasseurs() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estChasseur())
				retour++;
		return retour;
	}

	public float getValeur() {
		float retour = 0F;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getValeur();
		return retour;
	}

	public float getEntretien(Heros h, boolean carburant) {
		float retour = getValeur() / 20F;
		if (estEnGarnison())
			retour = retour / 3F;
		if (carburant)
			retour = retour / 2F;
		if (h != null)
			retour = retour
					- (20 * retour * h.getNiveauCompetence(Const.COMPETENCE_LEADER_ENTRETIEN_FLOTTE)) / 100F;
		return retour + Const.BASE_ENTRETIEN_FLOTTE;
	}

	public void resolutionConstruction(Commandant c) {
		// int potentiel=0;
		if (c.getNumero() != 0) {
			if (capaciteNavireUsine() != 0) {
				if (constructionEnCours == null)
					constructionEnCours = "Chasseur standard";
				if (!c.estPlanDeVaisseauConnu(constructionEnCours))
					constructionEnCours = "Chasseur standard";
				if (Univers.getPlanDeVaisseau(constructionEnCours)
						.capaciteNavireUsine() != 0)
					constructionEnCours = "Chasseur standard";
				int nbConstruIntercepteur = 0;
				int nbConstruAutre = 0;

				// int capaciteMax = getCapaciteMaximaleConstruction();
				int ptUniteIntercepteur = Construction.getPointsNecessaires("Chasseur standard");
				int ptUniteAutre = Construction.getPointsNecessaires(constructionEnCours);
				float coutcritik;

				boolean pasArgent = false;
				Vaisseau[] v = listeVaisseaux();

				for (int i = 0; i < v.length; i++)
					if (v[i].capaciteNavireUsine() > 0) {
						int race = 5; //on met les cyborg = c.getRace();
						int capaciteVaisseau = v[i].capaciteNavireUsine();
						if (capaciteVaisseau >= ptUniteAutre) {
							// Le nombre de construction est de minimum 1 et
							// maximum capaciteVaisseau/4
							int nbConstru = Math.max(1, Math.min( (capaciteVaisseau / ptUniteAutre), capaciteVaisseau / 4));
							// Le cout est de nbConstruÂ² * le prix Espace
							float cout = nbConstru
									* nbConstru
									* Construction
											.getPrixConstructionEspace(constructionEnCours);
							

							if (c.getCentaures() < cout)
								pasArgent = true;
							else {
								// Si il a de l'argent et que la construction
								// lui coute rien on le pÃ©nalise
								if (Construction
										.getPrixConstructionEspace(constructionEnCours) == 0) {
									c.ajouterEvenement(
											"EV_COMMANDANT_CONSTRUCTION_00010",
											getNomNumero(c.numeroFlotte(this)),
											constructionEnCours, nbConstru);
									// cout de la construction
									coutcritik = nbConstru
											* nbConstru
											* Construction
													.getPrixConstructionEspace("Chasseur standard");
									c
											.modifierBudget(
													Const.BUDGET_COMMANDANT_REALISATION_CONSTRUCTION,
													-coutcritik);
								} else {
									// Sinon On construit normalement
									for (int j = 0; j < nbConstru; j++)
										ajouterVaisseau(Vaisseau.creer(
												constructionEnCours, race));
									nbConstruAutre = nbConstruAutre + nbConstru;
									c
											.modifierBudget(
													Const.BUDGET_COMMANDANT_REALISATION_CONSTRUCTION,
													-cout);
									// rajout royalties ->
									if ((cout != 0F) && (c.getNumero() != 0)) {
										PlanDeVaisseau plan = Univers
												.getPlanDeVaisseau(constructionEnCours);
										if ((plan.getRoyalties() != 0)
												&& (plan.concepteurExistant())) {
											Commandant beneficiaire = Univers
													.getCommandant(plan
															.getNumeroConcepteur());
											float benefice = (plan
													.getRoyalties() * cout)
													/ (100 + plan
															.getRoyalties());
											beneficiaire
													.modifierBudget(
															Const.BUDGET_COMMANDANT_PERCEPTION_ROYALTIES,
															benefice);
										}
									}
								}
							}
						} else {
							int nbConstru = 1;
							float cout = nbConstru
									* Construction
											.getPrixConstructionEspace("Chasseur standard");
							if (c.getCentaures() < cout)
								pasArgent = true;
							else {
								for (int j = 0; j < nbConstru; j++)
									ajouterVaisseau(Vaisseau.creer(
											"Chasseur standard", race));
								nbConstruIntercepteur = nbConstruIntercepteur
										+ nbConstru;
								c
										.modifierBudget(
												Const.BUDGET_COMMANDANT_REALISATION_CONSTRUCTION,
												-cout);
							}
						}
					}

				// Si on a pas construit d'inter, alors on a construit
				if (nbConstruIntercepteur == 0)
					c.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0005",
							getNomNumero(c.numeroFlotte(this)),
							constructionEnCours, nbConstruAutre);
				else
					c.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0008",
							getNomNumero(c.numeroFlotte(this)),
							constructionEnCours, "Chasseur standard",
							nbConstruAutre, nbConstruIntercepteur);
				if (pasArgent)
					c.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0009");

			}
			// si la flotte n'a pas de mconstru, on initialise Ã  null ->
			else
				constructionEnCours = null;
		}
	}

	public int niveauMaximalNavireUsine() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = Math.max(v[i].niveauNavireUsine(), retour);
		return retour;
	}

	public int getCapaciteMaximaleConstruction() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = Math.max(v[i].capaciteNavireUsine(), retour);
		return retour;
	}

	public int capaciteNavireUsine() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].capaciteNavireUsine();
		return retour;
	}

	public boolean aCapaciteNavireUsine() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].capaciteNavireUsine() != 0)
				return true;
		return false;
	}

	public boolean estTransporteur() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estTransporteur())
				return true;
		return false;
	}

	public int nbVaisseauxCargos() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estTransporteur())
				retour++;
		return retour;
	}

	public int getPorteeScannerSysteme() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = Math.max(retour, v[i].getPorteeScannerSysteme());
		return retour;
	}

	public int getPorteeScannerFlotte() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = Math.max(retour, v[i].getPorteeScannerFlotte());
		return retour;
	}

	public int getBrouillageRadar() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = Math.max(retour, v[i].getBrouillageRadar());
		return retour;
	}

	public int sommeCapaciteCargo(boolean aVide) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].capaciteCargo(aVide);
		return retour;
	}

	public boolean possedeCapaciteRayonTracteur() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].possedeRayonTracteur())
				return true;
		return false;
	}

	public int nombreChargementPouvantEtreTransporte(String code,
			int numTransport) {
		int charge = ObjetTransporte.getEncombrementChargement(code);
		if (numTransport == Integer.MIN_VALUE) {
			int retour = 0;
			Vaisseau[] v = listeVaisseaux();
			for (int i = 0; i < v.length; i++)
				retour = retour + v[i].capaciteNombreChargement(charge);
			return retour;
		} else {
			Vaisseau v = getVaisseau(trouverTransportNumero(numTransport));
			if (v != null)
				return v.capaciteNombreChargement(charge);
			else
				return 0;
		}
	}

	public int trouverTransportNumero(int numTransport) {
		int compteur = -1;
		Vaisseau[] v = listeVaisseaux();
		Integer[] c = listeNumerosVaisseaux2();
		for (int i = 0; i < v.length; i++)
			if (v[i].estTransporteur())
				if ((++compteur) == numTransport)
					return c[i].intValue();
		return -1;
	}

	public void chargerChargement(ObjetTransporte objet, int numTransport) {
		if (objet.getNombreObjets() == 0)
			return;
		int charge = ObjetTransporte.getEncombrementChargement(objet.getCode());
		ObjetTransporte cl = (ObjetTransporte) objet.clone();
		if (numTransport == Integer.MIN_VALUE) {
			int nb = cl.getNombreObjets();
			Vaisseau[] v = listeVaisseaux();
			for (int i = 0; i < v.length; i++)
				if ((nb > 0) && (v[i].capaciteNombreChargement(charge) > 0)) {
					int nbAjouter = Math.min(v[i]
							.capaciteNombreChargement(charge), nb);
					v[i].ajouterCargaison((ObjetTransporte) cl
							.suppression(nbAjouter));
					nb = nb - nbAjouter;
				}
		} else {
			Vaisseau v = getVaisseau(trouverTransportNumero(numTransport));
			if (v != null)
				v.ajouterCargaison(objet);
		}
	}

	public int nombreChargementTransporte(String code, int numTransport) {
		if (numTransport == Integer.MIN_VALUE) {
			int retour = 0;
			Vaisseau[] v = listeVaisseaux();
			for (int i = 0; i < v.length; i++)
				retour = retour + v[i].getNombreCargaison(code);
			return retour;
		} else
			return getVaisseau(trouverTransportNumero(numTransport))
					.getNombreCargaison(code);
	}

	public ArrayList<ObjetTransporte> listeCargaisonTransporte() {
		ArrayList<ObjetTransporte> retour = new ArrayList<>();
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour.addAll(Arrays.asList(v[i].listeCargaison()));
		return retour;
	}

	public ObjetTransporte[] listeCargaisonTransporteTriee() {
		ArrayList<ObjetTransporte> liste = listeCargaisonTransporte();
		ArrayList<ObjetTransporte> retour = new ArrayList<>(liste.size());
		for (int i = 0; i < liste.size(); i++) {
			boolean dejaVu = false;
			for (int j = 0; j < retour.size(); j++)
				if (liste.get(i)
						.estDeType(retour.get(j).getCode())) {
					retour.get(j).ajout(liste.get(i));
					dejaVu = true;
				}
			if (!dejaVu)
				retour.add((ObjetTransporte) liste.get(i).clone());
		}
		return retour.toArray(new ObjetTransporte[0]);
	}

	public ObjetTransporte dechargerChargement(String code, int nombre, int numTransport) {
		if (numTransport == Integer.MIN_VALUE) {
			int nb = nombre;
			ObjetTransporte retour = null;
			Vaisseau[] v = listeVaisseaux();
			for (int i = 0; i < v.length; i++)
				if ((nb > 0) && (v[i].getNombreCargaison(code) > 0)) {
					int nbSupprimer = Math.min(v[i].getNombreCargaison(code),
							nb);
					if (retour == null)
						retour = v[i].supprimerCargaison(code, nbSupprimer);
					else
						retour
								.ajout(v[i].supprimerCargaison(code,
										nbSupprimer));
					nb = nb - nbSupprimer;
				}
			return retour;
		} else
			return getVaisseau(trouverTransportNumero(numTransport))
					.supprimerCargaison(code, nombre);
	}

	public Flotte diviserFlotte(String[] code, int[] nb, String nouveauNom) {
		Flotte retour = new Flotte(nouveauNom, position);
		retour.constructionEnCours = constructionEnCours;
		for (int i = 0; i < code.length; i++) {
			int index = trouverNumeroVaisseauLePlusEndommage(code[i]);
			int compteur = 0;
			while ((index != -1) && (compteur < nb[i])) {
				transfererVaisseau(retour, index);
				compteur++;
				index = trouverNumeroVaisseauLePlusEndommage(code[i]);
			}
		}
		return retour;
	}

	public int trouverNumeroVaisseauLePlusEndommage(String code) {
		Vaisseau[] v = listeVaisseaux();
		Integer[] l = listeNumerosVaisseaux2();
		int max = -1;
		int index = -1;
		for (int i = 0; i < v.length; i++)
			if ((v[i].estDeType(code))
					&& (v[i].nombreTotalPointsDeDommage() > max)) {
				max = v[i].nombreTotalPointsDeDommage();
				index = i;
			}
		if (index == -1)
			return -1;
		else
			return l[index].intValue();
	}

	public Flotte fusion(Flotte f) {
		Vaisseau[] v = f.listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			ajouterVaisseau(v[i]);
		if (constructionEnCours == null)
			constructionEnCours = f.getConstructionEnCours();
		return this;
	}

	public boolean pister(Flotte f, int proprioF, Heros h, Commandant commandant) {
		if (!deplacement)
			if (Position.deplacementVers(position, f.getPosition(),
					getCapaciteMouvement(false, h, commandant ), false).equals(
					f.getPosition())) {
				setPositionFixe(f.getPosition());
				deplacement = true;
				directive = Const.DIRECTIVE_FLOTTE_ATTAQUE_JOUEUR;
				directivePrecision = proprioF;
				return true;
			}
		return false;
	}

	public int lancerMines(int numJoueur) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estLanceurDeMines())
				retour = retour + v[i].lancerMines(position, numJoueur);
		return retour;
	}

	public int capaciteDraguageMines() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].capaciteDraguageMines();
		return retour;
	}

	public int gererCollision(int tailleDebris) {
		int retour = 0;
		Map.Entry[] m = listeCompleteVaisseaux();
		for (int i = 0; i < m.length; i++) {
			Vaisseau v = (Vaisseau) m[i].getValue();
			int numV = ((Integer) m[i].getKey()).intValue();
			int detection = v.capaciteDetectionMines();
			boolean vulnerable = true;
			if (detection > 0)
				if (Univers.getTest(detection))
					vulnerable = false;
			if (v.protectionDraguageMines())
				vulnerable = false;
			if (vulnerable) {
				// O2 transition --> division par 10 ->
				int dommages = Univers.getInt(Math.max(1,
						(tailleDebris / (Univers.getInt(500) + 1))
								/ (11 - v.getTaille()))) / 10;
				if (!v.ajouterDommagesAuHasard(dommages))
					supprimerVaisseau(numV);
				retour = retour + dommages;
			}
		}
		return retour;
	}

	public int gererCollisionVaisseaux(int tailleDebris) {
		int retour = 0;
		int frisk = tailleDebris * (5 + Univers.getInt(3)) + Univers.getInt(10);
		Map.Entry[] m = listeCompleteVaisseaux();
		for (int i = 0; i < m.length; i++) {
			Vaisseau v = (Vaisseau) m[i].getValue();
			int numV = ((Integer) m[i].getKey()).intValue();
			int detection = v.capaciteDetectionMines();
			boolean vulnerable = true;
			if (detection > 0)
				if (Univers.getTest(detection))
					vulnerable = false;
			if (v.protectionDraguageMines())
				vulnerable = false;
			if (vulnerable) {
				int dommages = Univers.getInt(Math.max(1, (tailleDebris / (v
						.getTaille() + 1))));
				if (!v.ajouterDommagesAuHasard(dommages))
					supprimerVaisseau(numV);
				retour = retour + dommages;
				frisk = frisk - dommages;
				if (frisk <= 0) {
					return retour;
				}
			}
		}
		return retour;
	}

	public boolean contientColonisateur() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estColonisateur())
				return true;
		return false;
	}

	public int trouverNumeroColonisateur() {
		Vaisseau[] v = listeVaisseaux();
		Integer[] c = listeNumerosVaisseaux2();
		for (int i = 0; i < v.length; i++)
			if (v[i].estColonisateur())
				return c[i].intValue();
		return -1;
	}

	public Vaisseau trouverColonisateur() {
		return getVaisseau(trouverNumeroColonisateur());
	}

	// VS normale
	public float entretienVilleSpatiale(int type) {
		float revenu = 0F;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].capaciteVilleSpatiale(type) != 0) {
				revenu = revenu
						+ ((float) v[i].getPopulationVilleSpatiale(type)
								* (v[i].getMoyenneNiveauVillesSpatiales(type) + 2) / 10);
				// System.out.println("tax "
				// +(v[i].getMoyenneNiveauVillesSpatiales(type)/10));
			}
		return revenu;
	}

	public Map getPopulationVilleSpatiale(int type) {
		Map m = null;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].capaciteVilleSpatiale(type) != 0) {
				if (m == null)
					m = new TreeMap();
				Integer cle = new Integer(v[i].getRaceEquipage());
				Object o = m.get(cle);
				if (o == null)
					m.put(cle, new Integer(v[i]
							.getPopulationVilleSpatiale(type)));
				else
					m.put(cle, new Integer(v[i]
							.getPopulationVilleSpatiale(type)
							+ ((Integer) o).intValue()));
			}
		return m;
	}

	public void augmenterPopulationVilleSpatiale() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].augmenterPopulationVilleSpatiale();
	}

	public int getCapaciteVilleSpatiale(int type) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].capaciteVilleSpatiale(type);
		return retour;
	}

	// Fin VS

	public void calculeCombativite(Heros h) {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].calculeCombativite(h);
	}

	public int calculeCombativiteMoyenne(Heros h) {
		calculeCombativite(h);
		Vaisseau[] v = listeVaisseaux();
		int inter = 0;
		for (int i = 0; i < v.length; i++)
			inter = inter + v[i].getCombativite();
		if (v.length != 0)
			return inter / v.length;
		else
			return 0;
	}

	public void diminuerCombativite() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].diminuerCombativite();
	}

	public boolean estCombative() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].estCombatif())
				return true;
		return false;
	}

	public Integer[] listeNumerosVaisseauxDeTaille(int taille, Integer[] table) {
		ArrayList<Integer> a = new ArrayList<>(table.length);
		for (int i = 0; i < table.length; i++)
			if (getVaisseau(table[i]).getTaille() == taille)
				a.add(table[i]);
		return a.toArray(new Integer[0]);
	}

	public Integer[] listeNumerosVaisseauxDeType(int type) {
		ArrayList<Integer> a = new ArrayList<>(getNombreDeVaisseaux());
		Map.Entry<Integer, Vaisseau>[] v = listeCompleteVaisseaux();

		if (type == Const.STRATEGIE_CIBLE_CARGO)
			for (int i = 0; i < v.length; i++)
				if (!v[i].getValue().estDetruit())
					if (v[i].getValue().estTransporteur())
						a.add(v[i].getKey());
		if (type == Const.STRATEGIE_CIBLE_BOMBARDIER)
			for (int i = 0; i < v.length; i++)
				if (!v[i].getValue().estDetruit())
					if (v[i].getValue().estBombardier())
						a.add(v[i].getKey());
		if (type == Const.STRATEGIE_CIBLE_CHASSEUR)
			for (int i = 0; i < v.length; i++)
				if (!v[i].getValue().estDetruit())
					if (v[i].getValue().estChasseur())
						a.add(v[i].getKey());
		if (type == Const.STRATEGIE_CIBLE_PROCHE)
			for (int i = 0; i < v.length; i++)
				if (!v[i].getValue().estDetruit())
					a.add(v[i].getKey());

		return a.toArray(new Integer[0]);
	}

	public ArrayList<Vaisseau> forceAttaqueStratospherique(int strat) {
		if ((strat == Const.STRATEGIE_AGRESSIVITE_PRUDENT)
				|| (strat == Const.STRATEGIE_AGRESSIVITE_FUYARD))
			return new ArrayList<>(Arrays.asList(listeVaisseaux()));
		ArrayList<Vaisseau> a = new ArrayList<>(getNombreDeVaisseaux());
		Vaisseau[] v = listeVaisseaux();
		if ((strat == Const.STRATEGIE_AGRESSIVITE_NORMAL)
				|| (strat == Const.STRATEGIE_AGRESSIVITE_PILLAGE))
			for (int i = 0; i < v.length; i++)
				if (v[i].estBombardier())
					a.add(v[i]);
		if (strat == Const.STRATEGIE_AGRESSIVITE_COMBATIF)
			for (int i = 0; i < v.length; i++)
				if (!v[i].estChasseur())
					a.add(v[i]);
		return a;
	}

	public ArrayList<Vaisseau> forceAttaqueAirSol(int strat) {
		if ((strat == Const.STRATEGIE_AGRESSIVITE_PRUDENT)
				|| (strat == Const.STRATEGIE_AGRESSIVITE_FUYARD))
			return new ArrayList<>();
		if (strat == Const.STRATEGIE_AGRESSIVITE_RAGE)
			return new ArrayList<>(Arrays.asList(listeVaisseaux()));
		ArrayList<Vaisseau> a = new ArrayList<>(getNombreDeVaisseaux());
		Vaisseau[] v = listeVaisseaux();
		if ((strat == Const.STRATEGIE_AGRESSIVITE_NORMAL)
				|| (strat == Const.STRATEGIE_AGRESSIVITE_PILLAGE))
			for (int i = 0; i < v.length; i++)
				if (!v[i].estBombardier())
					a.add(v[i]);
		if (strat == Const.STRATEGIE_AGRESSIVITE_COMBATIF)
			for (int i = 0; i < v.length; i++)
				if (v[i].estChasseur())
					a.add(v[i]);
		return a;
	}

	public void preparerAuCombat(boolean combatSpatial) {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			v[i].preparerAuCombat(combatSpatial);
	}

	public int getPuissance() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getPuissance();
		return retour;
	}

	public String getDescriptionPuissance(Locale l) {
		return Univers.getMessage("PUISSANCE", Vaisseau
				.retournerNiveauPuissance(getPuissance()), l);
	}

	public int getForceSpatiale() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getForceSpatiale();
		return retour;
	}

	public int getForcePlanetaire() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getForcePlanetaire();
		return retour;
	}

	public void eliminerPertesVaisseaux() {
		Map.Entry[] v = listeCompleteVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (((Vaisseau) v[i].getValue()).estDetruit())
				supprimerVaisseau((Integer) v[i].getKey());
	}

	public void finaliserCombat() {
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			if (v[i].capaciteCargo(false) < 0)
				v[i].initialiserCargaison();
	}

	@SuppressWarnings("unchecked")
	public void recupererMarchandises(ArrayList<ObjetTransporte> marchandises, Flotte cible) {
		ArrayList<ObjetTransporte> m = cible.listeCargaisonTransporte();
		if ((m.size() != marchandises.size())
				&& (possedeCapaciteRayonTracteur())) {
			ArrayList<ObjetTransporte> recupere = new ArrayList<>();
			for (int i = 0; i < marchandises.size(); i++)
				if (!m.contains(marchandises.get(i)))
					recupere.add(marchandises.get(i));
			for (int i = 0; i < recupere.size(); i++) {
				ObjetTransporte o = recupere.get(i);
				if (nombreChargementPouvantEtreTransporte(o.getCode(),
						Integer.MIN_VALUE) >= o.getNombreObjets())
					chargerChargement(o, Integer.MIN_VALUE);
			}
		}
	}

	public int reparer(int nbDommages) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].reparer(nbDommages - retour);
		return retour;
	}

	public int dommagesTotaux() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].nombreTotalPointsDeDommage();
		return retour;
	}

	public int nombreTotalCases() {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getNbCases();
		return retour;
	}

	public String getDescriptionEtat(Locale loc) {
		int d = dommagesTotaux();
		int result = 0;
		if (d != 0) {
			int c = nombreTotalCases();
			c = (100 * (c - d)) / c;
			if (c > 90)
				result = 1;
			else if (c > 70)
				result = 2;
			else if (c > 50)
				result = 3;
			else if (c > 20)
				result = 4;
			else
				result = 5;
		}
		return Univers.getMessage("DOMMAGES", result, loc);
	}

	public String getDescriptionExperience(Locale loc) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getExperience();
		try {
			return Univers.getMessage("EXPERIENCE", Vaisseau
					.retournerNiveauExperience(retour / v.length), loc);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getDescriptionMoral(Locale loc) {
		int retour = 0;
		Vaisseau[] v = listeVaisseaux();
		for (int i = 0; i < v.length; i++)
			retour = retour + v[i].getMoral();
		return Univers.getMessage("MORAL", Vaisseau.retournerNiveauMoral(retour
				/ v.length), loc);
	}

	@SuppressWarnings("unchecked")
	public Map.Entry<String, Integer>[] listeVaisseauxParType() {
		Vaisseau[] v = listeVaisseaux();
		HashMap<String, Integer> retour = new HashMap<>(v.length);
		for (int i = 0; i < v.length; i++)
			if (retour.containsKey(v[i].getType()))
				retour.put(v[i].getType(), retour.get(v[i].getType()) + 1);
			else
				retour.put(v[i].getType(), 1);
		return retour.entrySet().toArray(new Map.Entry[0]);
	}

	public Map<String, int[]> listeVaisseauxParTypePourCombat() {
		Vaisseau[] v = listeVaisseaux();
		HashMap<String, int[]> retour = new HashMap<>(v.length);
		for (int i = 0; i < v.length; i++)
			if (retour.containsKey(v[i].getType())) {
				int[] inter = retour.get(v[i].getType());
				inter[0]++;
				inter[1] = inter[1] + v[i].nombreTotalPointsDeDommage();
				inter[2] = inter[2] + v[i].getDommagesEffectues();
				retour.put(v[i].getType(), inter);
			} else {
				int[] inter = new int[3];
				inter[0] = 1;
				inter[1] = v[i].nombreTotalPointsDeDommage();
				inter[2] = v[i].getDommagesEffectues();
				retour.put(v[i].getType(), inter);
			}
		return retour;
	}

	// cle -> type de vaisseau : valeur -> 0:nombre 1:dommages pris 2:dommages
	// effectues.

	public Vaisseau[] listeVaisseauxCargos() {
		Vaisseau[] v = listeVaisseaux();
		ArrayList<Vaisseau> inter = new ArrayList<>(v.length);
		for (int i = 0; i < v.length; i++)
			if (v[i].estTransporteur())
				inter.add(v[i]);
		return inter.toArray(new Vaisseau[0]);
	}

	public int[] numeroPorteIntraGalactique() {
		for (int i = 0; i < Const.PORTES.length; i++)
			if (position.getNumeroGalaxie() == Const.PORTES[i][0])
				for (int j = 0; j < 2; j++)
					if ((position.getY() == Const.PORTES[i][2 * j + 1])
							&& (position.getX() == Const.PORTES[i][2 * (j + 1)]))
						return new int[] { i, j };
		return null;
	}

	public boolean estSurPorteIntraGalactique() {
		return (numeroPorteIntraGalactique() == null ? false : true);
	}

}
