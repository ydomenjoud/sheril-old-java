// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.NoSuchElementException;

public class Alliance implements Serializable {

	static final long serialVersionUID = -157847950319813219L;

	private int numeroAlliance;

	private String nom;
	private float droitsEntree;
	private int type;

	private boolean secrete;
	private int dirigeantNum;

	private int concepteurNum;
	private String concepteurNom;
	private int tourDeCreation;
	private String adresseElectronique;

	private transient Commentaire evenements;
	private transient HashMap<Integer, Integer> votesDirigeant;
	private transient HashMap<Integer, Integer> votesExclusion;

	private transient ArrayList<int[]> listeflottesDetectees;
	private transient ArrayList<Position> listeSystemesDetectees;

	// les methodes d'acces.

	public ArrayList<int[]> getListeflottesDetectees() {
		return listeflottesDetectees;
	}

	public ArrayList<Position> getListeSystemesDetectees() {
		return listeSystemesDetectees;
	}

	public int getNumero() {
		return numeroAlliance;
	}

	public boolean estSecrete() {
		return secrete;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String n) {
		nom = n;
	}

	public String toString() {
		return nom;
	}

	public float getDroitsEntree() {
		return droitsEntree;
	}

	public String getDescriptionType(Locale l) {
		return Univers.getMessage("TYPE_ALLIANCE", type, l);
	}

	public String getDescriptionConcepteur(Locale l) {
		if (concepteurInconnu())
			return Univers.getMessage("ALLIANCE_CONCEPTEUR_INCONNU", l);
		else
			return concepteurNom
					+ (concepteurNum == -1 ? "" : "("
							+ Integer.toString(concepteurNum) + ")");
	}

	public String getDescriptionDirigeant(Locale l) {
		if (allianceNonDirigee())
			return Univers.getMessage("NON_EXISTENCE_DIRIGEANT_ALLIANCE", l);
		else
			return Univers.getCommandant(dirigeantNum).getNomNumero();
	}

	public boolean aUnSite() {
		return adresseElectronique != null;
	}

	public String getSite() {
		if (adresseElectronique == null)
			return "&nbsp;";
		else
			return adresseElectronique;
	}

	public int getNumeroDirigeant() {
		return dirigeantNum;
	}

	public int getNumeroConcepteur() {
		return concepteurNum;
	}

	public void concepteurDisparu() {
		concepteurNum = -1;
	}

	public boolean estDirigeePar(int numC) {
		return numC == dirigeantNum;
	}

	public void setDirigeant(int numD) {
		dirigeantNum = numD;
	}

	public boolean estAutocratique() {
		return type == Const.ALLIANCE_TYPE_AUTOCRATIQUE;
	}

	public boolean estDemocratique() {
		return type == Const.ALLIANCE_TYPE_DEMOCRATIQUE;
	}

	public boolean estAnarchique() {
		return type == Const.ALLIANCE_TYPE_ANARCHIQUE;
	}

	public Commentaire getEvenements() {
		initialiserEvenements();
		return evenements;
	}

	public int hashCode() {
		return numeroAlliance * 10000 + tourDeCreation;
	}

	public Commandant[] getAdherents() {
		Commandant[] c = Univers.getListeCommandants();
		ArrayList<Commandant> a = new ArrayList<>(c.length);
		for (int i = 0; i < c.length; i++)
			if (c[i].appartientAAlliance(numeroAlliance))
				a.add(c[i]);
		return a.toArray(new Commandant[0]);
	}

	public Map<Integer, Commandant> getListeAdherents() {
		Commandant[] c = getAdherents();
		HashMap<Integer, Commandant> retour = new HashMap<>(c.length);
		for (int i = 0; i < c.length; i++)
			retour.put(c[i].getNumero(), c[i]);
		return retour;
	}

	public int nombreDeMembres() {
		return getListeAdherents().size();
	}

	public void setAdresseElectronique(String entree) {
		adresseElectronique = entree;
	}

	public boolean concepteurInconnu() {
		if (concepteurNom == null)
			return true;
		else
			return false;
	}

	public boolean allianceNonDirigee() {
		if (dirigeantNum == -1)
			return true;
		else
			return false;
	}
	
	public void determinerFlottesDetectees(){
		listeflottesDetectees = new ArrayList<int[]>();
		for(Commandant commandant:getAdherents()){
			ArrayList<int[]> liste = commandant.getFlottesDetecteesArrayList();
			for (int[] detect : liste) {
				if( !listeflottesDetectees.contains(detect) ){
					listeflottesDetectees.add(detect);
				}
			}
		}
	}

	public void determinerSystemesDetectes(){
		listeSystemesDetectees = new ArrayList<Position>();
		for(Commandant commandant:getAdherents()){
			ArrayList<Position> liste = commandant.getSystemesDetectesArrayList();
			for (Position detect : liste) {
				if( !listeSystemesDetectees.contains(detect) ){
					listeSystemesDetectees.add(detect);
				}
			}
		}
	}

	public static void gererAlliances1() {
		Alliance[] a = Univers.getListeAlliances();
		for (int i = 0; i < a.length; i++) {
			Map<Integer, Commandant> m = a[i].getListeAdherents();
			if (m.size() != 0) {
				Commandant[] c = m.values().toArray(
						new Commandant[0]);
				if (a[i].allianceNonDirigee()) {
					SortedMap<Integer, Commandant> sm = Stats.trierParPuissance(c);
					try {
						if (a[i].estAutocratique())
							a[i].setDirigeant(Stats
									.getPremier(sm).getNumero());
						if (a[i].estAnarchique())
							a[i].setDirigeant(Stats
									.getDernier(sm).getNumero());
					} catch (NoSuchElementException e) {
						e.printStackTrace();
					}
				}
				for (int j = 0; j < c.length; j++)
					if (a[i].estAnarchique()) {
						if (c[j].getPlaceAlliance(a[i].getNumero()) < 2)
							c[j].modifierBudget(
									Const.BUDGET_COMMANDANT_REVENUS_ALLIANCE,
									Const.REVENU_ALLIANCE_ANARCHIQUE);
					} else if ((a[i].estDirigeePar(c[j].getNumero()))
							&& (a[i].estAutocratique())) {
						if (c[j].getPlaceAlliance(a[i].getNumero()) < 2)
							c[j].modifierBudget(
									Const.BUDGET_COMMANDANT_REVENUS_ALLIANCE, 5
											* (c.length - 1) * (c.length - 1)
											* 1F);
					} else if (c[j].getPlaceAlliance(a[i].getNumero()) < 2)
						if (a[i].estDemocratique())
							c[j].modifierBudget(
									Const.BUDGET_COMMANDANT_REVENUS_ALLIANCE,
									10 * (c.length - 1) * 1F);
			}
		}
	}

	public static void gererAlliances2() {
		Alliance[] a = Univers.getListeAlliances();
		for (int i = 0; i < a.length; i++) {
			if (a[i].getListeAdherents().size() == 0) {
				Univers.eliminerAlliance(a[i].getNumero());
				Univers.ajouterEvenement("PUBLIC_ALLIANCE_0000", a[i].getNom());
			}
		}
	}

	public static void traiterVotes() {
		Alliance[] a = Univers.getListeAlliances();
		for (int i = 0; i < a.length; i++) {
			a[i].traiterVoteDirigeant();
			a[i].traiterVoteExclusion();
		}
	}

	public void ajouterVoteDirigeant(int num) {
		if (votesDirigeant == null)
			votesDirigeant = new HashMap<>();
		Integer o = votesDirigeant.get(num);
		if (o == null)
			votesDirigeant.put(num, 1);
		else
			votesDirigeant.put(num, o + 1);
	}

	@SuppressWarnings("unchecked")
	public void traiterVoteDirigeant() {
		if (votesDirigeant != null) {
			Map.Entry<Integer, Integer>[] m = votesDirigeant.entrySet().toArray(
					new Map.Entry[0]);
			int max = 0;
			int index = 0;
			ajouterEvenement("EV_ALLIANCE_0002");
			String annonce = "";
			for (int i = 0; i < m.length; i++) {
				int nb = m[i].getValue();
				if (max < nb) {
					max = nb;
					index = i;
				}
				annonce = annonce
						+ Univers.getCommandant(
								m[i].getKey())
								.getNomNumero() + " : " + Integer.toString(nb)
						+ "<BR>";
			}
			ajouterEvenement(annonce + "<BR>");
			int nbMembres = nombreDeMembres();
			Commandant c = Univers.getCommandant(m[index].getKey());
			if (max >= nbMembres / 2) {
				setDirigeant(m[index].getKey());
				ajouterEvenement("EV_ALLIANCE_0003", c.getNomNumero(),
						getNom(), max, nbMembres);
			} else
				ajouterEvenement("EV_ALLIANCE_0010", c.getNomNumero(),
						getNom(), max, nbMembres);
		}
	}

	public void ajouterVoteExclusion(int num) {
		if (votesExclusion == null)
			votesExclusion = new HashMap<>();
		Integer o = votesExclusion.get(num);
		if (o == null)
			votesExclusion.put(num, 1);
		else
			votesExclusion.put(num, o + 1);
	}

	@SuppressWarnings("unchecked")
	public void traiterVoteExclusion() {
		if (votesExclusion != null) {
			Map.Entry<Integer, Integer>[] m = votesExclusion.entrySet().toArray(
					new Map.Entry[0]);
			int nbMembres = nombreDeMembres();
			for (int i = 0; i < m.length; i++) {
				boolean reussite = false;
				if (estAutocratique()) {
					ajouterEvenement(
							"EV_ALLIANCE_0004",
							Univers.getCommandant(
									m[i].getKey())
									.getNomNumero());
					reussite = true;
				}
				if (estDemocratique()) {
					int nbVoix = m[i].getValue();
					if (nbVoix >= nbMembres / 2) {
						ajouterEvenement(
								"EV_ALLIANCE_0005",
								Univers.getCommandant(
										m[i].getKey())
										.getNomNumero(), nbVoix, nbMembres);
						reussite = true;
					} else
						ajouterEvenement(
								"EV_ALLIANCE_0006",
								Univers.getCommandant(
										m[i].getKey())
										.getNomNumero(), nbVoix, nbMembres);
				}
				if (reussite) {
					Commandant c = Univers.getCommandant(m[i]
							.getKey());
					c.enleverAlliance(numeroAlliance);
					c.ajouterEvenement("EV_COMMANDANT_ALLIANCE_0005", getNom());
				}
			}
		}
	}

	public void initialiserEvenements() {
		if (evenements == null)
			evenements = new Commentaire();
	}

	public void ajouterEvenement(String message) {
		initialiserEvenements();
		evenements.ajouter(message);
	}

	public void ajouterEvenement(String message, String var) {
		initialiserEvenements();
		evenements.ajouter(message, var);
	}

	public void ajouterEvenement(String message, String var1, int var2, int var3) {
		initialiserEvenements();
		evenements.ajouter(message, var1, var2, var3);
	}

	public void ajouterEvenement(String message, String var1, String var2,
			int var3, int var4) {
		initialiserEvenements();
		evenements.ajouter(message, var1, var2, var3,
				var4);
	}

	// Le constructeur.

	private Alliance() {
	}

	public Alliance(int num, String n, float droits, int ty, boolean secret,
			int conceptNum, String conceptNom, int tour) {
		numeroAlliance = num;
		nom = n;
		droitsEntree = droits;
		type = ty;
		secrete = secret;
		concepteurNum = conceptNum;
		concepteurNom = conceptNom;
		tourDeCreation = tour;

		dirigeantNum = conceptNum;
	}

}
