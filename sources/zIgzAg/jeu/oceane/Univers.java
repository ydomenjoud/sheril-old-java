// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mdt;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.io.*;


public class Univers {


	private static final String SESSION_MYSQL = "zo";

	private static final Locale LANGUE = Locale.FRENCH;
    public static final String config = "config.properties";

    private static ListeDescription LISTE_DESCRIPTION;

	private static Messages MESSAGES;

	private static MessagesInfo MESSAGES_INFO;

	private static Random HASARD;

	private static TreeMap LISTE_TECHNOLOGIES;

	private static TreeMap LISTE_CARAC_ARMES;

	private static TreeMap LISTE_CHANCES_TOUCHE;

	private static TreeMap SYSTEMES;

	private static TreeMap DEBRIS;

	private static TreeMap COMMANDANTS;

	private static TreeMap PLANS_DE_VAISSEAUX;

	private static TreeMap ALLIANCES;

	private static TreeMap systemesReparations;

	private static HashMap STATS;

	private static HashMap RELATIONS_RACES;

	private static HashMap TRANSFERTS;

	private static ArrayList LEADERS_EN_VENTE;

	private static ArrayList TECHNOLOGIES_PUBLIQUES;

	private static int NUMERO_DU_TOUR;

	private static Integer PHASE;

	private static Commentaire ORDRES_NON_CONFORMES;

	private static Commentaire EVENEMENTS_PUBLICS;

	private static Commentaire ARTICLES;

	private static Map<Integer, Collection<RapportCombat>> RAPPORTS_COMBAT;

	private boolean complet;

	private String descriptionSession;

	// private int numeroUnivers;

	private static transient HashMap CAPACITES_SPECIALES_BATIMENTS;

	private static transient int[][] MARCHANDISES_UNIVERS;

	// Pour obtenir la langue système.

	public static Locale getLocale() {
		return LANGUE;
	}

	// Pour obtenir un message système.

	public static String getMessageSysteme(String code) {
		return (String) ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.MessagesSystemes", LANGUE))
				.handleGetObject(code);
	}

	public static boolean existenceMessageSysteme(String code) {
		return ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.MessagesSystemes", Locale.FRENCH))
				.handleGetObject(code) != null;
	}

	// Les mÃ©thodes statiques pour obtenir un message de base du programme
	// (respectivement Ã  partir d'une String ou d'un tableau de String).

	public static String getMessage(String code, Locale loc) {
		return (String) ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.Messages", loc)).handleGetObject(code);
	}

	public static String getMessage(String code, int index, Locale loc) {
		return getTableauMessage(code, loc)[index];
	}

	public static String[] getTableauMessage(String code, Locale loc) {
		return (String[]) ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.Messages", loc)).handleGetObject(code);
	}

	// Les mÃ©thodes statiques pour obtenir un Ã©lÃ©ment de document HTML.
	// (respectivement Ã  partir d'une String ou d'un tableau de String).

	public static Object getMessageRapport(String code, Locale loc) {
		return ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.MessagesRapport", loc))
				.handleGetObject(code);
	}

	public static String getMessageRapport(String code, int index, Locale loc) {
		return ((String[]) getMessageRapport(code, loc))[index];
	}

	// La mÃ©thode statique pour obtenir un message d'information du
	// programme.

	public static String getMessageInfo(String code, Locale loc) {
		return (String) ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.MessagesInfo", loc)).handleGetObject(code);
	}

	public static boolean existenceMessageInfo(String code) {
		return ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.MessagesInfo", Locale.FRENCH))
				.handleGetObject(code) != null;
	}

	// Les mÃ©thodes statiques pour obtenir les descriptions d'un type de
	// technologie.

	public static Object getTechnoDescription(String code, Locale loc) {
		return ((ListResourceBundle) ResourceBundle.getBundle(
				"zIgzAg.jeu.oceane.ListeDescription", loc))
				.handleGetObject(code);
	}

	public static String getNomTechno(String code, Locale loc) {

		Object o = getTechnoDescription(code, loc);
		if (o != null)
			return ((Description) o).getNom();
		else {
			return "<font color=\"#00F1AF\">"
					+ getNomTechno(getTechnologie(code).getCorpsCode(), loc)
					+ "</font>";
		}
	}

	public static String getNomPlurielTechno(String code, Locale loc) {
		Object o = getTechnoDescription(code, loc);
		if (o != null)
			return ((Description) o).getNomPluriel();
		else
			return getNomPlurielTechno(getTechnologie(code).getCorpsCode(), loc);
	}

	public static String getDescriptionTechno(String code, Locale loc) {
		Object o = getTechnoDescription(code, loc);
		if (o != null)
			return ((Description) o).getDescription();
		else
			return getNomPlurielTechno(getTechnologie(code).getCorpsCode(), loc);
	}

	// Les mÃ©thodes statiques pour obtenir les caractÃ©ristiques d'un type
	// d'arme.

	public static int[] getCaracArme(String code) {
		return (int[]) LISTE_CARAC_ARMES.get(code);
	}

	// //Les mÃ©thodes statiques pour obtenir les chances de toucher d'un type
	// d'arme.

	public static int[] getChanceToucheArme(String code) {
		return (int[]) LISTE_CHANCES_TOUCHE.get(code);
	}

	// mÃ©thode statique pour savoir si un code est pris ou non

	public static boolean existenceCode(String code) {
		if (Univers.existenceTechnologie(code))
			return true;
		if (Univers.existencePlanDeVaisseau(code))
			return true;
		return false;
	}

	// Les mÃ©thodes statiques pour accÃ©der aux technologies.

	public static Technologie getTechnologie(String code) {
		return (Technologie) LISTE_TECHNOLOGIES.get(code);
	}

	public static boolean existenceTechnologie(String code) {
		return LISTE_TECHNOLOGIES.containsKey(code);
	}

	public static boolean existenceTechnologieBatiment(String code) {
		Technologie t = getTechnologie(code);
		if ((t != null) && (t.estBatiment()))
			return true;
		else
			return false;
	}

	public static String[] getListeCodeTechnologie() {
		return (String[]) LISTE_TECHNOLOGIES.keySet().toArray(new String[0]);
	}

	public static Map.Entry[] getTechnologies() {
		return (Map.Entry[]) LISTE_TECHNOLOGIES.entrySet().toArray(
				new Map.Entry[0]);
	}

	public static Technologie[] getListeTechnologies() {
		return (Technologie[]) LISTE_TECHNOLOGIES.values().toArray(
				new Technologie[0]);
	}


	public static Technologie[] getListeTechnologiesDeTypeBatiment() {
		return trierTechnologies((Technologie[]) LISTE_TECHNOLOGIES.values()
				.toArray(new Technologie[0]), Const.TECHNOLOGIE_TYPE_BATIMENT);
	}

	public static Technologie[] getListeTechnologiesDeTypeComposantDeVaisseau() {
		return trierTechnologies((Technologie[]) LISTE_TECHNOLOGIES.values()
				.toArray(new Technologie[0]),
				Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU);
	}

	// trie une liste de techno dans l'odre alphabÃ©tique
	public static Technologie[] trierAlphabetiquementTechnologies(
			Technologie[] tab) {

		// TreeMap de techno
		TreeMap orderTechno = new TreeMap();
		String clef = "";

		for (int i = 0; i < tab.length; i++) {
			// on crÃ©Ã© la clef NOM + NIVEAU
			clef = "";
			clef += tab[i].getNom(Locale.FRENCH);
			clef += tab[i].getNiveau();
			orderTechno.put(clef, tab[i]);
		}

		// On crÃ©Ã© le tableau de tech
		Technologie[] t = new Technologie[tab.length];

		// et on le rempli
		int rang = 0;
		Iterator it1 = orderTechno.values().iterator();
		while (it1.hasNext())
			t[rang++] = (Technologie) it1.next();

		return t;
	}

	public static Technologie[] trierTechnologies(Technologie[] t,
			int typeTechno) {

		ArrayList l = new ArrayList(t.length);

		for (int i = 0; i < t.length; i++)
			if (typeTechno == Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU) {
				if (t[i].estComposantDeVaisseau())
					l.add(t[i]);
			} else if (typeTechno == Const.TECHNOLOGIE_TYPE_BATIMENT) {
				if (t[i].estBatiment())
					l.add(t[i]);
			} else if (t[i].estTechnologieSimple())
				l.add(t[i]);
		return trierAlphabetiquementTechnologies((Technologie[]) l
				.toArray(new Technologie[0]));
	}

	public static Technologie[] getTechnologiesPossedantCapaciteSpeciale(
			int carac, int typeTechno) {
		Technologie[] t = null;
		if (typeTechno == Const.TECHNOLOGIE_TYPE_BATIMENT) {
			Object o = CAPACITES_SPECIALES_BATIMENTS.get(new Integer(carac));
			if (o == null)
				t = getListeTechnologiesDeTypeBatiment();
			else
				return (Technologie[]) o;
		}
		if (t == null)
			return null;

		ArrayList liste = new ArrayList(t.length);
		for (int i = 0; i < t.length; i++)
			if (t[i].possedeCaracteristiqueSpeciale(carac))
				liste.add(t[i]);
		t = (Technologie[]) liste.toArray(new Technologie[0]);

		if (typeTechno == Const.TECHNOLOGIE_TYPE_BATIMENT)
			CAPACITES_SPECIALES_BATIMENTS.put(new Integer(carac), t);
		return t;
	}

	// Les mÃ©thodes statiques pour accÃ©der aux technologies publiques.

	public static void ajouterTechnologieAuDomainePublic(String code) {
		TECHNOLOGIES_PUBLIQUES.add(code);
	}

	public static boolean estTechnologiePublique(String code) {
		return TECHNOLOGIES_PUBLIQUES.contains(code);
	}

	public static String[] getListeCodeTechnologiePubliques() {
		return (String[]) TECHNOLOGIES_PUBLIQUES.toArray(new String[0]);
	}

	public static void supprimerTechnologiePublique(String code) {
		TECHNOLOGIES_PUBLIQUES.remove(code);
	}

	// Les mÃ©thodes statiques pour obtenir un nombre, un boolÃ©en, un
	// tableau
	// ou le rÃ©sultat d'un test(en pourcentage) au hasard.

	public static int getInt(int borne) {
		return HASARD.nextInt(borne);
	}

	public static int[] getTabInt(int borne, int ajout, int dimension) {
		int[] retour = new int[dimension];
		for (int i = 0; i < dimension; i++)
			retour[i] = getInt(borne) + ajout;
		return retour;
	}

	public static boolean getBoolean() {
		return HASARD.nextBoolean();
	}

	public static int getInd() {
		if (getBoolean())
			return 1;
		else
			return -1;
	}

	public static boolean getTest(int chance) {
		if (getInt(100) < chance)
			return true;
		else
			return false;
	}

	// les mÃ©thodes d'accès au numÃ©ro du tour.

	public static void setTour(int t) {
        System.out.println("set tour " + t);
		NUMERO_DU_TOUR = t;
	}

	public static int getTour() {
		return NUMERO_DU_TOUR;
	}

	public static void augmenterNumeroTour() {
		NUMERO_DU_TOUR++;
		Chemin.initialiserChemins(NUMERO_DU_TOUR);
	}

	public static void chargerNumeroTour() {
		String s = Fiche.lecture(Chemin.NUMERO_DU_TOUR, 1);
		if (s == null)
			NUMERO_DU_TOUR = 0;
		else
			NUMERO_DU_TOUR = Integer.parseInt(s);
		Chemin.initialiserChemins(NUMERO_DU_TOUR);
	}

	public static void sauvegarderNumeroTour() {
		if( false && Const.FAKE_TURN ){

		} else {
            System.out.println("Sauvegarde du numéro du tour");
			Fiche.initialisation(Chemin.NUMERO_DU_TOUR);
			Fiche.ecriture(Chemin.NUMERO_DU_TOUR, Integer.toString(NUMERO_DU_TOUR));
		}
	}

	// les mÃ©thodes d'accès aux leaders en vente.

	public static void ajouterLeaderEnVente(Leader l) {
		l.mettreEnReserve();
		LEADERS_EN_VENTE.add(l);
	}

	public static Leader[] listeLeadersEnVente() {
		return (Leader[]) LEADERS_EN_VENTE.toArray(new Leader[0]);
	}

	public static String[] listeLeadersEnVenteGrade() {
		Leader[] l = listeLeadersEnVente();
		String[] retr = new String[l.length];
		for (int i = 0; i < l.length; i++)
			retr[i] = l[i].estHeros() ? "H" : "G";
		return retr;
	}

	public static void retirerLeaderEnVente(Leader l) {
		LEADERS_EN_VENTE.remove(l);
	}

	public static void initialiserLeadersEnVente() {
		for (int i = 0; i < listeLeadersEnVente().length; i++)
			retirerLeaderEnVente(listeLeadersEnVente()[i]);
	}

	// les mÃ©thodes d'accès aux alliances.

	public static Map clonerListeAlliance() {
		return (Map) ALLIANCES.clone();
	}

	public static void ajouterAlliance(Alliance entree) {
		ALLIANCES.put(new Integer(entree.getNumero()), entree);
	}

	public static void eliminerAlliance(int numero) {
		ALLIANCES.remove(new Integer(numero));
	}

	public static int getNombreAlliances() {
		return ALLIANCES.size();
	}

	public static Alliance[] getListeAlliances() {
		return (Alliance[]) ALLIANCES.values().toArray(new Alliance[0]);
	}

	public static Alliance[] getListeAlliancesNonSecretes() {
		Alliance[] a = getListeAlliances();
		ArrayList b = new ArrayList(a.length);
		for (int i = 0; i < a.length; i++)
			if (!a[i].estSecrete())
				b.add(a[i]);
		return (Alliance[]) b.toArray(new Alliance[0]);
	}

	public static Integer[] getNumerosAlliances() {
		return (Integer[]) ALLIANCES.keySet().toArray(new Integer[0]);
	}

	public static Alliance getAlliance(int numero) {
		return (Alliance) ALLIANCES.get(new Integer(numero));
	}

	public static boolean allianceExistante(int numero) {
		return ALLIANCES.containsKey(new Integer(numero));
	}

	public static int trouverNumeroLibreAlliance() {
		Integer[] l = getNumerosAlliances();
		for (int i = 0; i < l.length; i++)
			if (l[i].intValue() != i)
				return i;
		return l.length;
	}

	// mÃ©thode en sursis -->
	public static int getNumeroAlliance(String nom) {
		Alliance[] a = getListeAlliances();
		for (int i = 0; i < a.length; i++)
			if (a[i].getNom().equals(nom))
				return a[i].getNumero();
		return -1;
	}

	// mÃ©thode en sursis -->
	public static boolean allianceExistante(String nom) {
		if (getNumeroAlliance(nom) == -1)
			return false;
		else
			return true;
	}

	// les mÃ©thodes d'accès aux dÃ©bris.

	public static void setDebris(Debris entree) {
		DEBRIS.put(entree.getPosition(), entree);
	}

	public static void ajouterDebris(Debris entree) {
		Debris d = getDebris(entree.getPosition());
		if (d == null)
			setDebris(entree);
		else
			setDebris(d.fusion(entree));
	}

	public static Debris getDebris(Position pos) {
		Object o = DEBRIS.get(pos);
		if (o == null)
			return null;
		else
			return (Debris) DEBRIS.get(pos);
	}

	public static Position[] listePositionDebris() {
		return (Position[]) DEBRIS.keySet().toArray(new Position[0]);
	}

	// les mÃ©thodes d'accès aux systèmes de l'univers.

	public static void setSysteme(Systeme entree) {
		SYSTEMES.put(entree.getPosition(), entree);
	}

	public static Systeme getSysteme(Position pos) {
		return (Systeme) SYSTEMES.get(pos);
	}

	public static void removeSysteme(Systeme s) {
		SYSTEMES.remove(s.getPosition());
	}

	public static Systeme[] listeSystemes(Position[] pos) {
		Systeme[] retour = new Systeme[pos.length];
		for (int i = 0; i < pos.length; i++)
			retour[i] = getSysteme(pos[i]);
		return retour;
	}

	public static boolean existenceSysteme(Position pos) {
		return SYSTEMES.containsKey(pos);
	}

	public static Position[] listePositionsSystemes() {
		return (Position[]) SYSTEMES.keySet().toArray(new Position[0]);
	}

	public static Position[] listePositionsSystemesParGalaxie(int galaxie) {
		Position[] p = listePositionsSystemes();
		ArrayList a = new ArrayList(1500);
		for (int i = 0; i < p.length; i++)
			if (p[i].getNumeroGalaxie() == galaxie)
				a.add(p[i]);
		return (Position[]) a.toArray(new Position[0]);
	}

	public static int getTheSecteur(Position p) {
		int secteur = p.getNumeroSecteur();
		return secteur;
	}

	public static Position[] listePositionsSystemesParSecteur(int galaxie,
			int secteur) {
		Position[] p = listePositionsSystemes();
		ArrayList a = new ArrayList(100);
		for (int i = 0; i < p.length; i++)
			if ((p[i].getNumeroGalaxie() == galaxie)
					&& (p[i].getNumeroSecteur() == secteur))
				a.add(p[i]);
		return (Position[]) a.toArray(new Position[0]);
	}

	public static Position[] listePositionsSystemesParRaceDeDepart(int race) {
		Position[] p = listePositionsSystemes();
		ArrayList a = new ArrayList(500);
		for (int i = 0; i < p.length; i++)
			if (Utile.getRaceDeDepart(p[i]) == race)
				a.add(p[i]);
		return (Position[]) a.toArray(new Position[0]);
	}

	public static Position[] listePositionsSystemesDisponibles(int race) {
		Position[] p = null;
		p = listePositionsSystemes();

		ArrayList a = new ArrayList(500);
		for (int i = 0; i < p.length; i++) {
			Systeme s = getSysteme(p[i]);
			if ((s.nbProprios() == 1) && (s.estProprio(0))
					&& (s.getPopulationMaximale(0) > 8000))
				a.add(p[i]);
		}

		return (Position[]) a.toArray(new Position[0]);
	}

	public static Position[] listeSystemesNeutres() {
		Position[] p = listePositionsSystemes();
		ArrayList a = new ArrayList(500);

		for (int i = 0; i < p.length; i++) {
			Systeme s = getSysteme(p[i]);
			if ((s.nbProprios() == 1) && (s.estProprio(0)))
				a.add(p[i]);
		}
		return (Position[]) a.toArray(new Position[0]);
	}

	public static boolean estCorrectementPlace(Position p, int distance) {

		Position[] l = Position.getPositionsADistance(p,
				listePositionsSystemes(), distance);
		int compteur = 0;

		for (int i = 0; i < l.length; i++) {
			Systeme s = getSysteme(l[i]);
			if (s.nbProprios() > 1 || !s.estProprio(0))
				return false;
			else
				compteur++;
		}

		if (compteur > 3)
			return true;

		return false;
	}

    public static Commandant getCommandantFromFlotte(Flotte flotte) {
        HashMap<int[], Flotte> flottes = listeFlottes();
        for (Map.Entry<int[], Flotte> entry : flottes.entrySet()) {
            if (entry.getValue() == flotte) {
                return getCommandant(entry.getKey()[0]);
            }
        }
        return null;
    }

	public static HashMap<int[], Flotte> listeFlottes() {
        HashMap<int[], Flotte> retour = new HashMap<int[], Flotte>();
        Commandant[] c = getListeCommandants();
        for (int i = 0; i < c.length; i++) {
            Map.Entry[] m = c[i].listeFlottesEtNumeros();
            for (int j = 0; j < m.length; j++) {
                int[] inter = new int[2];
                inter[0] = c[i].getNumero();
                inter[1] = (Integer) m[j].getKey();
                retour.put(inter, (Flotte) m[j].getValue());
            }
        }
        return retour;
	}

	// liste des flottes en jeu par directive :
	// cle --> position valeur --> map h
	// h : cle --> directive(Integer) valeur --> int[n][2] : [numero
	// joueur][numÃ©ro flotte]

	public static Map listeFlottesDirectives() {
		HashMap retour = new HashMap();
		Commandant[] c = getListeCommandants();
		for (int i = 0; i < c.length; i++) {
			Map.Entry[] m = c[i].listeFlottesEtNumeros();
			for (int j = 0; j < m.length; j++) {
				Flotte f = (Flotte) m[j].getValue();
				int[] inter = new int[2];
				inter[0] = c[i].getNumero();
				inter[1] = ((Integer) m[j].getKey()).intValue();
				int[][] inter2 = new int[1][2];
				inter2[0] = inter;
				Integer cle = new Integer(f.getDirectiveComplete());
				Object o = retour.get(f.getPosition());

				if (o == null) {
					HashMap h = new HashMap(3);
					h.put(cle, inter2);
					retour.put(f.getPosition(), h);
				} else {
					Map h = (Map) o;
					o = h.get(cle);
					if (o == null)
						h.put(cle, inter2);
					else
						h.put(cle, Mdt.ajoutCoupleIndex((int[][]) o, inter));
				}
			}
		}
		return retour;
	}

	public static Map listeVaisseauxParType() {
		Commandant[] c = getListeCommandants();
		HashMap retour = new HashMap();
		for (int i = 0; i < c.length; i++) {
			Map m = c[i].listeVaisseauxTriee();
			Map.Entry[] e = (Map.Entry[]) m.entrySet()
					.toArray(new Map.Entry[0]);
			for (int j = 0; j < e.length; j++) {
				Object o = retour.get(e[j].getKey());
				if (o == null)
					retour.put(e[j].getKey(), e[j].getValue());
				else
					retour.put(e[j].getKey(),
							new Integer(((Integer) e[j].getValue()).intValue()
									+ ((Integer) o).intValue()));
			}
		}
		return retour;
	}

	// Les mÃ©thodes d'accès aux joueurs & commandants.

	public static void setJoueur(Joueur entree) {
		COMMANDANTS.put(new Integer(entree.getNumero()), entree);
	}

	public static Joueur getJoueur(int numero) {
		return (Joueur) COMMANDANTS.get(new Integer(numero));
	}

	public static Map clonerListeCommandants() {
		return (Map) COMMANDANTS.clone();
	}

	public static int getNombreCommandants() {
		return COMMANDANTS.size();
	}

	public static void setCommandant(Commandant entree) {
		COMMANDANTS.put(new Integer(entree.getNumero()), entree);
	}

	public static Commandant getCommandant(int numero) {
		return (Commandant) COMMANDANTS.get(new Integer(numero));
	}

	public static void supprimerCommandant(int numero) {
		COMMANDANTS.remove(new Integer(numero));
	}

	public static boolean existenceCommandant(int numero) {
		return COMMANDANTS.containsKey(new Integer(numero));
	}

	public static Commandant[] getListeCommandants() {
		return (Commandant[]) COMMANDANTS.values().toArray(new Commandant[0]);
	}

	public static Commandant[] getListeCommandantsHumains() {
		Commandant[] d = getListeCommandants();
		ArrayList a = new ArrayList(d.length);
		for (int i = 0; i < d.length; i++)
			if (d[i].estJoueurHumain())
				a.add(d[i]);
		return (Commandant[]) a.toArray(new Commandant[0]);
	}

	public static boolean existenceLogin(String login) {
		Commandant[] c = getListeCommandantsHumains();
		for (int i = 0; i < c.length; i++)
			if (c[i].getLogin().equals(login))
				return true;
		return false;
	}

	public static int[] getNumerosCommandants() {
		Integer[] inter = (Integer[]) COMMANDANTS.keySet().toArray(
				new Integer[0]);
		int[] retour = new int[inter.length];
		for (int i = 0; i < inter.length; i++)
			retour[i] = inter[i].intValue();
		return retour;
	}

	// Les méthodes d'accès aux plans de vaisseaux.

	public static void ajouterPlanDeVaisseau(PlanDeVaisseau entree) {
		PLANS_DE_VAISSEAUX.put(entree.getNom(), entree);
	}

	public static void supprimerPlanDeVaisseau(String nom) {
		PLANS_DE_VAISSEAUX.remove(nom);
	}

	public static PlanDeVaisseau getPlanDeVaisseau(String nom) {
		return (PlanDeVaisseau) PLANS_DE_VAISSEAUX.get(nom);
	}

	public static boolean existencePlanDeVaisseau(String nom) {
		return PLANS_DE_VAISSEAUX.containsKey(nom);
	}

	public static PlanDeVaisseau[] listePlansDeVaisseaux() {
		return (PlanDeVaisseau[]) PLANS_DE_VAISSEAUX.values().toArray(
				new PlanDeVaisseau[0]);
	}

	public static PlanDeVaisseau[] listePlansDeVaisseauxPublics() {
		PlanDeVaisseau[] l = listePlansDeVaisseaux();
		ArrayList a = new ArrayList(l.length);
		for (int i = 0; i < l.length; i++)
			if (l[i].estPublic())
				a.add(l[i]);
		return (PlanDeVaisseau[]) a.toArray(new PlanDeVaisseau[0]);
	}

	// Les méthodes d'accès aux composants de vaisseaux.

	// Les méthodes d'accès aux batiments.

	// Les méthodes d'accès aux stats.
	public static Map getStats() {
		return STATS;
	}

	public static Map getStatsDernierTour() {
		String chemin = Chemin.RACINE + "tour"
				+ Integer.toString(getTour() - 1) + "/donnees/" + "stats.txt";
		return chargerMap(chemin);
	}

	public static Map getStatsDuTour(int tour) {
		String chemin = Chemin.RACINE + "tour" + Integer.toString(tour)
				+ "/donnees/" + "stats.txt";
		return chargerMap(chemin);
	}

	// Les méthodes d'accès aux relations entre races.

	public static void ajouterRelationRaces(Position pos, int race1, int race2,
			int modif) {
		if (pos != null)
			ajouterRelationRaces(pos.getNumeroGalaxie(),
					pos.getNumeroSecteur(), race1, race2, modif);
	}

	public static void ajouterRelationRaces(int galaxie, int secteur,
			int race1, int race2, int modif) {
		String cle = Integer.toString(galaxie) + "-"
				+ Integer.toString(secteur);
		Object o = RELATIONS_RACES.get(cle);
		String cle2 = null;
		if (race1 > race2)
			cle2 = Integer.toString(race2) + "-" + Integer.toString(race1);
		else
			cle2 = Integer.toString(race1) + "-" + Integer.toString(race2);
		if (o == null) {
			HashMap h = new HashMap();
			h.put(cle2, new Integer(modif));
			RELATIONS_RACES.put(cle, h);
		} else {
			Object o2 = ((HashMap) o).get(cle2);
			if (o2 == null)
				((HashMap) o).put(cle2, new Integer(modif));
			else
				((HashMap) o).put(cle2, new Integer(((Integer) o2).intValue()
						+ modif));
		}
	}

	public static int getRelationRaces(Position pos, int race1, int race2) {
		return getRelationRaces(pos.getNumeroGalaxie(), pos.getNumeroSecteur(),
				race1, race2);
	}

	public static int getRelationRaces(int galaxie, int secteur, int race1,
			int race2) {
		String cle = Integer.toString(galaxie) + "-"
				+ Integer.toString(secteur);
		Object o = RELATIONS_RACES.get(cle);
		if (o == null)
			return 0;
		String cle2 = null;
		if (race1 > race2)
			cle2 = Integer.toString(race2) + "-" + Integer.toString(race1);
		else
			cle2 = Integer.toString(race1) + "-" + Integer.toString(race2);
		Object o2 = ((HashMap) o).get(cle2);
		if (o2 == null)
			return 0;
		else
			return ((Integer) o2).intValue();
	}

	// méthodes d'accès aux différents transferts.
	private static List ajouterTransfert(Object l, String cle, String mode,
			String message) {
		if (l == null)
			l = new ArrayList();
		String[] inter = new String[3];
		inter[0] = cle;
		inter[1] = mode;
		inter[2] = message + " (" + Integer.toString(Univers.getTour()) + ")";
		((List) l).add(inter);
		return (List) l;
	}

	public static void ajouterTransfert(Joueur donneur, Joueur beneficiaire,
			String message) {
		Integer cle = new Integer(donneur.hashCode());
		Integer cle2 = new Integer(beneficiaire.hashCode());
		TRANSFERTS.put(
				cle,
				ajouterTransfert(TRANSFERTS.get(cle), cle2.toString(), "D",
						message));
		TRANSFERTS.put(
				cle2,
				ajouterTransfert(TRANSFERTS.get(cle2), cle.toString(), "R",
						message));
	}

	public static void supprimerTransferts(Object cle) {
		TRANSFERTS.remove(cle);
	}

	public static Map getTransferts() {
		return TRANSFERTS;
	}

	public static void terminerTransfertsPourLeTour() {
		sauvegarderMap(Chemin.TRANSFERTS, TRANSFERTS);
		TRANSFERTS = null;
	}

	public static void viderTransferts() {
		TRANSFERTS = new HashMap();
	}

	// pour stocker les rapports de combats par secteur -->

	public static void ajouterRapportCombat(RapportCombat r) {
		int galaxie = r.getPosition().getNumeroGalaxie();
		int secteur = r.getPosition().getNumeroSecteur();
		int cle = secteur + (Const.NB_SECTEURS * galaxie);
		Collection<RapportCombat> c = RAPPORTS_COMBAT.get(cle);
		if (c == null) {
			c = new ArrayList<>();
			RAPPORTS_COMBAT.put(cle, c);
		}
		c.add(r);
	}

	// pour obtenir les rapports de combat par secteur -->

	public static RapportCombat[] getRapportsCombat(int galaxie, int secteur) {
		int cle = secteur + (Const.NB_SECTEURS * galaxie);
		Collection<RapportCombat> c = RAPPORTS_COMBAT.get(cle);
		if (c == null)
			return new RapportCombat[0];
		return c.toArray(new RapportCombat[0]);
	}

	// méthodes d'accès aux prix moyens de marchandises de l'univers

	public static int getPrixMoyenMarchandise(int marchandise) {
		return MARCHANDISES_UNIVERS[marchandise][1];
	}

	protected Univers() {
	}

	// Le constructeur de cet Univers.

	public Univers(boolean e1, String e2) {

		// reparation
		systemesReparations = new TreeMap();

		complet = e1;
		descriptionSession = e2;
		PHASE = new Integer(0);
		ORDRES_NON_CONFORMES = new Commentaire();
		EVENEMENTS_PUBLICS = new Commentaire();
		ARTICLES = new Commentaire();
		LISTE_DESCRIPTION = new ListeDescription();
		MESSAGES = new Messages();
		MESSAGES_INFO = new MessagesInfo();
		HASARD = new Random();
		LISTE_CARAC_ARMES = chargerDynamiquement(LISTE_CARAC_ARMES, "zIgzAg.jeu.oceane.ListeCaracArmes");
		LISTE_CHANCES_TOUCHE = chargerDynamiquement(LISTE_CHANCES_TOUCHE, "zIgzAg.jeu.oceane.ListeChancesDeToucherArmes");
		LISTE_TECHNOLOGIES = chargerDynamiquement(LISTE_TECHNOLOGIES, "zIgzAg.jeu.oceane.ListeTechnologique");
		charger();
		CAPACITES_SPECIALES_BATIMENTS = new HashMap();
		STATS = new HashMap();
		RAPPORTS_COMBAT = new HashMap<>();
		MARCHANDISES_UNIVERS = Systeme.getMarchandises(Univers.listeSystemes(Univers.listePositionsSystemes()));
		System.out.println("Fin du chargement de l'Univers...");
		System.out.println("mem Libre/tot:" + Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().totalMemory());
	}

	// fonctions de "bidouilles";
	public void setDescription(String s) {
		descriptionSession = s;
	}

	// fonction initialisatrice : à n'appeler qu'une fois, lors de la création
	// de l'univers.

	// les plans de départ -->
	private static final Object[][] PLAN_DEPART = {
			{ "Inconnu",
					new String[] { "moteurI", "bombeI" },
					new int[] { 1, 4 }, "Bombardier standard", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "bombeI" },
					new int[] { 1, 7 }, "Grand Bombardier standard", "Galaxia" },
			{ "Inconnu",
					new String[] { "moteurI", "laserI" },
					new int[] { 1, 1 }, "Intercepteur standard", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "laserI" },
					new int[] { 1, 4 }, "Chasseur standard", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "torpI" },
					new int[] { 1, 4 }, "Torpilleur standard", "Dune" },
			{"Inconnu",
					new String[] { "moteurI", "bouclierI", "plasmaI" },
					new int[] { 1, 5, 10 }, "Fregate standard", "Dune" },
			{"Inconnu",
					new String[] { "moteurI", "bouclierI", "laserI", "plasmaI"},
					new int[] { 1, 5, 10, 10, 10 }, "Destroyer standard", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "bouclierI", "plasmaI", "torpI", "missI" },
					new int[] { 1, 10, 10, 10, 10 }, "Croiseur standard", "Dune" },
			{"Inconnu",
					new String[] { "moteurI", "bouclierI", "bombeI", "laserI", "plasmaI", "torpI" },
					new int[] { 1, 10, 10, 20, 20, 20 }, "Supercroiseur standard", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "robocoI" },
					new int[] { 1, 1 }, "Colonisateur", "Dune" },
			{ "Inconnu",
					new String[] { "moteurI", "scanI" },
					new int[] { 1, 1 }, "Eclaireur standard", "Dune" }
	/**
	 * ,{"Inconnu",new String[]{"moteurI","cargoII"},new int[]{1,16},"Cargo
	 * Standard","Dune"},*
	 */

	};

	private static final Object[][] PLAN_RACIAUX = {
			{ "Inconnu", new String[] { "moteurI", "scanIV" },
					new int[] { 1, 1, 1 }, "Sidjin", "FreeTech", new Integer(0) },
			{ "Inconnu", new String[] { "moteurII", "bouclierIII", "plasmaI" },
					new int[] { 1, 1, 7 }, "Gardien", "Atatech", new Integer(1) },
			{ "Inconnu", new String[] { "moteurII", "bouclierIII", "bombeI" },
					new int[] { 1, 1, 10 }, "Bombardier Zwaia", "Zwatech",
					new Integer(2) },
			{ "Inconnu", new String[] { "moteurII", "villeII" },
					new int[] { 1, 1 }, "Colonie Crawl", "Zwatech",
					new Integer(2) },
			{ "Inconnu", new String[] { "moteurII", "mconstruII" },
					new int[] { 1, 1 }, "Plateforme Spatiale", "Yoktech",
					new Integer(3) },
			{ "Inconnu", new String[] { "moteurII", "absorbI", "plasmaI" },
					new int[] { 1, 1, 4 }, "Esquivateur", "Fertech",
					new Integer(4) },
			{ "Inconnu", new String[] { "moteurII", "cyb_vs_tt_I" },
					new int[] { 1, 1 }, "Centre de recherche", "Cybtech",
					new Integer(5) },
			{ "Inconnu", new String[] { "moteurII", "villeI" },
					new int[] { 1, 1 }, "Colonie Byeril", "Cybtech",
					new Integer(5) },
			{ "Inconnu", new String[] { "moteurII", "cyb_vs_tc_I" },
					new int[] { 1, 1 }, "A-M loe", "Cybtech", new Integer(5) },
			{ "Inconnu", new String[] { "moteurII", "cyb_vs_te_I" },
					new int[] { 1, 1 }, "SpyFlight", "Cybtech", new Integer(5) }
	/**
	 * ,{"Inconnu",new String[]{"moteurIII","cyb_mdc_t1_V",
	 * "cyb_vs_tc_IV","cyb_vs_tt_IV"
	 * ,"cyb_vs_te_IV","villeV","bouclierVIII","scanI"},new
	 * int[]{1,1,1,1,1,1,5,1},"Nexus","Cybtech",new Integer(5)} *
	 */

	};

	public static String supprimerAccent(String nom){
		nom = nom.replaceAll("[èéêë]","e");
	    nom = nom.replaceAll("[ûù]","u");
	    nom = nom.replaceAll("[ïî]","i");
	    nom = nom.replaceAll("[àâ]","a");
	    nom = nom.replaceAll("Ô","o");
	    return nom;
	}

	public static void corrigerPlanDeVaisseau(){
		PlanDeVaisseau[] liste = listePlansDeVaisseaux();
		for(PlanDeVaisseau plan:liste){
			String nom = plan.getNom();

			// On supprime l'entrée dans la liste
			PLANS_DE_VAISSEAUX.remove(nom);

			// On corrige le nom
		    nom = supprimerAccent(nom);

		    // On modifie le nom
		    plan.setNom(nom);

		    // On réajoute l'entrée corrigée dans la liste
		    PLANS_DE_VAISSEAUX.put(nom, plan);
		}
		//System.exit(0);
	}

	public static void rechargerPlan() {
		Commandant neutre = Univers.getCommandant(0);
		PLANS_DE_VAISSEAUX = new TreeMap();
		// le commandant neutre connait un certain nombre de technos pour
		// pouvoir concevoir les plans -->
		for (int i = 0; i < PLAN_DEPART.length; i++) {
			String[] s = (String[]) PLAN_DEPART[i][1];
			System.out.println((String) PLAN_DEPART[i][3]);
			for (int j = 0; j < s.length; j++) {
				if (neutre == null)
					System.out.println(s[j]);
				neutre.ajouterTechnologieConnue(s[j]);
			}
		}
		for (int i = 0; i < PLAN_RACIAUX.length; i++) {
			String[] s = (String[]) PLAN_RACIAUX[i][1];
			for (int j = 0; j < s.length; j++)
				neutre.ajouterTechnologieConnue(s[j]);
		}

		// créatoin des plans de publics ->
		for (int i = 0; i < PLAN_DEPART.length; i++) {
			String[] comp = (String[]) PLAN_DEPART[i][1];
			int[] num = (int[]) PLAN_DEPART[i][2];
			ArrayList inter = new ArrayList();
			for (int j = 0; j < comp.length; j++)
				for (int k = 0; k < num[j]; k++)
					inter.add(comp[j]);
			comp = (String[]) inter.toArray(new String[0]);

			if (!PlanDeVaisseau.verificationConformite(neutre, comp,
					(String) PLAN_DEPART[i][3])) {
				System.out.println("erreur : le plan " + PLAN_DEPART[i][3]
						+ " n'est pas valide ");
			}

			PlanDeVaisseau plan = new PlanDeVaisseau(0,
					(String) PLAN_DEPART[i][0], comp,
					(String) PLAN_DEPART[i][3], (String) PLAN_DEPART[i][4], "",
					0, -1, 0,
					PlanDeVaisseau.determinerCaracteristiquesSpeciales(neutre,
							comp), PlanDeVaisseau.determinerMineraiNecessaire(
							neutre, comp), PlanDeVaisseau.determinerPrix(
							neutre, comp, 0),
					PlanDeVaisseau.determinerMarchandisesNecessaires(neutre,
							comp),
					PlanDeVaisseau.determinerNombreDeCases(comp), 0, true);
			Univers.ajouterPlanDeVaisseau(plan);
		}

		// créatoin des plans raciaux ->
		for (int i = 0; i < PLAN_RACIAUX.length; i++) {
			String[] comp = (String[]) PLAN_RACIAUX[i][1];
			int[] num = (int[]) PLAN_RACIAUX[i][2];
			ArrayList inter = new ArrayList();
			for (int j = 0; j < comp.length; j++)
				for (int k = 0; k < num[j]; k++)
					inter.add(comp[j]);
			comp = (String[]) inter.toArray(new String[0]);

			if (!PlanDeVaisseau.verificationConformite(neutre, comp,
					(String) PLAN_RACIAUX[i][3])) {
				System.out.println("erreur : le plan " + PLAN_RACIAUX[i][3]
						+ " n'est pas valide ");

			}

			PlanDeVaisseau plan = new PlanDeVaisseau(0,
					(String) PLAN_RACIAUX[i][0], comp,
					(String) PLAN_RACIAUX[i][3], (String) PLAN_RACIAUX[i][4],
					"", 3, PLAN_RACIAUX[i][5].hashCode(), 0,
					PlanDeVaisseau.determinerCaracteristiquesSpeciales(neutre,
							comp), PlanDeVaisseau.determinerMineraiNecessaire(
							neutre, comp), PlanDeVaisseau.determinerPrix(
							neutre, comp, 0),
					PlanDeVaisseau.determinerMarchandisesNecessaires(neutre,
							comp),
					PlanDeVaisseau.determinerNombreDeCases(comp), 0, true);
			Univers.ajouterPlanDeVaisseau(plan);
		}

		// supression des technologies du neutre ->
		neutre.initialiserTechnologiesConnues();

	}

	public static void initialisation() {
		Univers univers = new Univers(true, Const.MESSAGE_U_00000);

		// créatoin du commandant neutre ->
		Commandant neutre = new Commandant("neutre", 0, 0, "neutre@neutre.fr",
				"nnnnnnnnnn", "nnnnnnnnnn", 0);
		neutre.setTypeDeJoueur(1);
		// le commandant neutre connait un certain nombre de technos pour
		// pouvoir concevoir les plans -->
		for (int i = 0; i < PLAN_DEPART.length; i++) {
			String[] s = (String[]) PLAN_DEPART[i][1];
			for (int j = 0; j < s.length; j++)
				neutre.ajouterTechnologieConnue(s[j]);
		}
		for (int i = 0; i < PLAN_RACIAUX.length; i++) {
			String[] s = (String[]) PLAN_RACIAUX[i][1];
			for (int j = 0; j < s.length; j++)
				neutre.ajouterTechnologieConnue(s[j]);
		}

		setCommandant(neutre);

		// créatoin des plans de publics ->
		for (int i = 0; i < PLAN_DEPART.length; i++) {
			String[] comp = (String[]) PLAN_DEPART[i][1];
			int[] num = (int[]) PLAN_DEPART[i][2];
			ArrayList inter = new ArrayList();
			for (int j = 0; j < comp.length; j++)
				for (int k = 0; k < num[j]; k++)
					inter.add(comp[j]);
			comp = (String[]) inter.toArray(new String[0]);

			if (!PlanDeVaisseau.verificationConformite(neutre, comp,
					(String) PLAN_DEPART[i][3])) {
				System.out.println("erreur : le plan " + PLAN_DEPART[i][3]
						+ " n'est pas valide ");
			}

			PlanDeVaisseau plan = new PlanDeVaisseau(0,
					(String) PLAN_DEPART[i][0], comp,
					(String) PLAN_DEPART[i][3], (String) PLAN_DEPART[i][4], "",
					0, -1, 0,
					PlanDeVaisseau.determinerCaracteristiquesSpeciales(neutre,
							comp), PlanDeVaisseau.determinerMineraiNecessaire(
							neutre, comp), PlanDeVaisseau.determinerPrix(
							neutre, comp, 0),
					PlanDeVaisseau.determinerMarchandisesNecessaires(neutre,
							comp),
					PlanDeVaisseau.determinerNombreDeCases(comp), 0, true);
			Univers.ajouterPlanDeVaisseau(plan);
		}

		// créatoin des plans raciaux ->
		for (int i = 0; i < PLAN_RACIAUX.length; i++) {
			String[] comp = (String[]) PLAN_RACIAUX[i][1];
			int[] num = (int[]) PLAN_RACIAUX[i][2];
			ArrayList inter = new ArrayList();
			for (int j = 0; j < comp.length; j++)
				for (int k = 0; k < num[j]; k++)
					inter.add(comp[j]);
			comp = (String[]) inter.toArray(new String[0]);

			if (!PlanDeVaisseau.verificationConformite(neutre, comp,
					(String) PLAN_RACIAUX[i][3])) {
				System.out.println("erreur : le plan " + PLAN_RACIAUX[i][3]
						+ " n'est pas valide ");

			}

			PlanDeVaisseau plan = new PlanDeVaisseau(0,
					(String) PLAN_RACIAUX[i][0], comp,
					(String) PLAN_RACIAUX[i][3], (String) PLAN_RACIAUX[i][4],
					"", 3, PLAN_RACIAUX[i][5].hashCode(), 0,
					PlanDeVaisseau.determinerCaracteristiquesSpeciales(neutre,
							comp), PlanDeVaisseau.determinerMineraiNecessaire(
							neutre, comp), PlanDeVaisseau.determinerPrix(
							neutre, comp, 0),
					PlanDeVaisseau.determinerMarchandisesNecessaires(neutre,
							comp),
					PlanDeVaisseau.determinerNombreDeCases(comp), 0, true);
			Univers.ajouterPlanDeVaisseau(plan);
		}

		// supression des technologies du neutre ->
		neutre.initialiserTechnologiesConnues();

		univers.sauvegarder();
	}

	public static void main(String[] s) {
		initialisation();
	}

	// Les accès fichiers.

	public static Map chargerMap(String fichier) {
		File fiche = new File(fichier);
		if (!fiche.exists())
			return null;
		Map retour = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					fiche));
			try {
				retour = (Map) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				System.exit(0);
			} finally {
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return retour;
	}

	public ArrayList chargerArrayList(String fichier) {
		File fiche = new File(fichier);
		if (!fiche.exists())
			return new ArrayList();
		ArrayList retour = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					fiche));
			try {
				retour = (ArrayList) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}

		return retour;
	}

	public static void sauvegarderMap(String fichier, Map champ) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fichier));
			oos.writeObject(champ);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sauvegarderArrayList(String fichier, ArrayList champ) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fichier));
			oos.writeObject(champ);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void charger() {
		ecrireDansCarnetDeBord("Ouverture session  : " + descriptionSession);
		chargerNumeroTour();
		if (descriptionSession.equals(SESSION_MYSQL))
			chargerMysql();
		else if (complet) {
			System.out.print("c");
			COMMANDANTS = (TreeMap) chargerMap(Chemin.COMMANDANTS);
			if (COMMANDANTS == null)
				COMMANDANTS = new TreeMap();
			System.out.print("d");
			DEBRIS = (TreeMap) chargerMap(Chemin.DEBRIS);
			if (DEBRIS == null)
				DEBRIS = new TreeMap();
			System.out.print("s");
			SYSTEMES = (TreeMap) chargerMap(Chemin.SYSTEMES);
			if (SYSTEMES == null)
				SYSTEMES = new TreeMap();
			System.out.print("p");
			PLANS_DE_VAISSEAUX = (TreeMap) chargerMap(Chemin.PLANS_DE_VAISSEAUX);
			if (PLANS_DE_VAISSEAUX == null)
				PLANS_DE_VAISSEAUX = new TreeMap();
			System.out.print("a");
			ALLIANCES = (TreeMap) chargerMap(Chemin.ALLIANCES);
			if (ALLIANCES == null)
				ALLIANCES = new TreeMap();
			System.out.print("r");
			RELATIONS_RACES = (HashMap) chargerMap(Chemin.RELATIONS_RACES);
			if (RELATIONS_RACES == null)
				RELATIONS_RACES = new HashMap();
			System.out.print("t");
			TRANSFERTS = (HashMap) chargerMap(Chemin.TRANSFERTS);
			if (TRANSFERTS == null)
				TRANSFERTS = new HashMap();
			System.out.print("l");
			LEADERS_EN_VENTE = chargerArrayList(Chemin.LEADERS_EN_VENTE);
			System.out.print("t");
			TECHNOLOGIES_PUBLIQUES = chargerArrayList(Chemin.TECHNOLOGIES_PUBLIQUES);
		} else {
			COMMANDANTS = (TreeMap) chargerMap(Chemin.COMMANDANTS);
			DEBRIS = null;
			SYSTEMES = null;
			PLANS_DE_VAISSEAUX = null;
			ALLIANCES = null;
		}
	}

	public void sauvegarderMysql() {

	}

	public void chargerMysql() {

	}

	public void sauvegarder() {
		sauvegarderNumeroTour();
		System.out.println("mem Libre/tot:" + Runtime.getRuntime().freeMemory()
				+ "/" + Runtime.getRuntime().totalMemory());
		if (descriptionSession.equals(SESSION_MYSQL))
			sauvegarderMysql();
		else if (complet) {
			sauvegarderMap(Chemin.COMMANDANTS, COMMANDANTS);
			sauvegarderMap(Chemin.DEBRIS, DEBRIS);
			sauvegarderMap(Chemin.SYSTEMES, SYSTEMES);
			sauvegarderMap(Chemin.PLANS_DE_VAISSEAUX, PLANS_DE_VAISSEAUX);
			sauvegarderMap(Chemin.ALLIANCES, ALLIANCES);
			sauvegarderMap(Chemin.BASE_STATS, STATS);// System.out.println(STATS.size());
			sauvegarderMap(Chemin.RELATIONS_RACES, RELATIONS_RACES);
			// sauvegarderMap(Chemin.TRANSFERTS,TRANSFERTS);
			sauvegarderArrayList(Chemin.LEADERS_EN_VENTE, LEADERS_EN_VENTE);
			sauvegarderArrayList(Chemin.TECHNOLOGIES_PUBLIQUES,
					TECHNOLOGIES_PUBLIQUES);
		} else
			sauvegarderMap(Chemin.COMMANDANTS, COMMANDANTS);

		ecrireDansCarnetDeBord("Sauvegarde session : " + descriptionSession);
	}

	private TreeMap chargerDynamiquement(TreeMap t, String nomDeClasse) {
		t = new TreeMap();
		try {
			Field[] champs = (Class.forName(nomDeClasse)).getFields();
			for (int i = 0; i < champs.length; i++)
				t.put(champs[i].getName(), champs[i].get(null));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}

	public static void ecrireDansCarnetDeBord(String laius) {
		Fiche.ecriture(Chemin.CARNET_DE_BORD, Utile.getDate() + " " + laius);
	}

	// pour gérer les phases de développement de l'univers.

	public static Integer getPhase() {
		return PHASE;
	}

	public static void phaseSuivante() {
		PHASE = new Integer(PHASE.intValue() + 1);
	}

	// pour gérer les ordres non conformes(erreur de programme,répétition de
	// mêmes ordres ou triche).

	public static Commentaire getMessagesErreurs() {
		return ORDRES_NON_CONFORMES;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			Object var1, Object var2) {
		ORDRES_NON_CONFORMES.ajouter(message, var1, var2, nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message) {
		ORDRES_NON_CONFORMES.ajouter(message, nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			int var) {
		ORDRES_NON_CONFORMES.ajouter(message, new Integer(var), nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			Object var) {
		ORDRES_NON_CONFORMES.ajouter(message, var, nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			Object var1, int var2) {
		ORDRES_NON_CONFORMES.ajouter(message, var1, new Integer(var2),
				nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			int var1, int var2) {
		ORDRES_NON_CONFORMES.ajouter(message, new Integer(var1), new Integer(
				var2), nomCommandant);
		return false;
	}

	public static boolean ajouterErreur(String nomCommandant, String message,
			Object var1, int var2, int var3) {
		ORDRES_NON_CONFORMES.ajouter(message, var1, new Integer(var2),
				new Integer(var3), nomCommandant);
		return false;
	}

	// pour gérer les événements publics.

	public static Commentaire getMessagesEvenements() {
		return EVENEMENTS_PUBLICS;
	}

	public static boolean ajouterEvenement(String message, Object var1) {
		EVENEMENTS_PUBLICS.ajouter(message, var1);
		return true;
	}

	public static boolean ajouterEvenement(String message, Object var1,
			Object var2) {
		EVENEMENTS_PUBLICS.ajouter(message, var1, var2);
		return true;
	}

	public static boolean ajouterEvenement(String message, Object var1,
			Object var2, int var3) {
		EVENEMENTS_PUBLICS.ajouter(message, var1, var2, new Integer(var3));
		return true;
	}

	public static boolean ajouterEvenement(String message, Object var1,
			Object var2, Object var3) {
		EVENEMENTS_PUBLICS.ajouter(message, var1, var2, var3);
		return true;
	}

	public static boolean ajouterEvenement(String message, Object var1,
			Object var2, Object var3, Object var4) {
		EVENEMENTS_PUBLICS.ajouter(message, var1, var2, var3, var4);
		return true;
	}

	public static boolean ajouterEvenement(String message, Object var1,
			Object var2, float var3) {
		EVENEMENTS_PUBLICS.ajouter(message, var1, var2, new Float(var3));
		return true;
	}

	// pour gérer les articles produits par les commandants.

	public static Commentaire getMessagesArticles() {
		return ARTICLES;
	}

	public static void ajouterArticle(Commandant auteur, String article) {
		ARTICLES.ajouter("<HR><BR>" + article
				+ "<BR><BR><B><font color=\"red\">" + auteur.getNomNumero()
				+ "</font></B><BR>\n");
	}

	public static void gestionTechno() {

	}

	public static void fechier(Commandant neutre) {

		Object[][] PLAN_AJOUT = {
				{ "Muad dib", new String[] { "moteurV", "molIX", "cargoX" },
						new int[] { 1, 1, 9 }, "Harmonie - C", "harmonisation",
						new Integer(11) },
				{ "Inconnu", new String[] { "moteurII", "cyb_vs_tt_I" },
						new int[] { 1, 1 }, "Chercheurs", "Cybtech",
						new Integer(5) },
				{ "Inconnu", new String[] { "moteurII", "villeII" },
						new int[] { 1, 1 }, "Citadins", "Cybtech",
						new Integer(5) },
				{ "Inconnu", new String[] { "moteurII", "cyb_vs_tc_I" },
						new int[] { 1, 1 }, "Contres", "Cybtech",
						new Integer(5) },
				{ "Inconnu", new String[] { "moteurII", "cyb_vs_te_I" },
						new int[] { 1, 1 }, "Espions", "Cybtech",
						new Integer(5) }, };

		// le commandant neutre connait un certain nombre de technos pour
		// pouvoir concevoir les plans -->

		for (int i = 0; i < PLAN_AJOUT.length; i++) {
			String[] s = (String[]) PLAN_AJOUT[i][1];
			for (int j = 0; j < s.length; j++)
				neutre.ajouterTechnologieConnue(s[j]);
		}

		String[] comp = (String[]) PLAN_AJOUT[0][1];
		int[] num = (int[]) PLAN_AJOUT[0][2];
		ArrayList inter = new ArrayList();
		for (int j = 0; j < comp.length; j++)
			for (int k = 0; k < num[j]; k++)
				inter.add(comp[j]);
		comp = (String[]) inter.toArray(new String[0]);

		PlanDeVaisseau plan = new PlanDeVaisseau(0, (String) PLAN_AJOUT[0][0],
				comp, (String) PLAN_AJOUT[0][3], (String) PLAN_AJOUT[0][4], "",
				2, PLAN_AJOUT[0][5].hashCode(), 0,
				PlanDeVaisseau
						.determinerCaracteristiquesSpeciales(neutre, comp),
				PlanDeVaisseau.determinerMineraiNecessaire(neutre, comp),
				PlanDeVaisseau.determinerPrix(neutre, comp, 0),
				PlanDeVaisseau.determinerMarchandisesNecessaires(neutre, comp),
				PlanDeVaisseau.determinerNombreDeCases(comp), 31, true);
		Univers.ajouterPlanDeVaisseau(plan);

	}

	// reparation de flotte depuis systeme

	public static int getPointsRepareDunSysteme(Position p, int numero) {
		// On construit la clef
		String clef = Utile.pareil(p) + "%" + Utile.formatNumber(numero, 3);
		if (systemesReparations.containsKey(clef))
			return ((Integer) (systemesReparations.get(clef))).intValue();
		return 0;
	}

	public static void setPointsRepareDunSysteme(Position p, int numero, int nb) {
		// On construit la clef
		String clef = Utile.pareil(p) + "%" + Utile.formatNumber(numero, 3);
		// On regarde si la clef existe et on la supprime
		if (systemesReparations.containsKey(clef))
			systemesReparations.remove(clef);

		// On ajoute la nouvelle clef
		systemesReparations.put(clef, new Integer(nb));

	}


//	public static synchronized void notify(String msg){
//		System.out.println(msg);
//        String webhookUrl = Const.DISCORD_WEBHOOK_URL;
//        String message = "{\"content\":\"TOUR "+ Univers.NUMERO_DU_TOUR + " : " + msg+"\"}";
//
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL(webhookUrl);
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setDoOutput(true);
//
//            try (OutputStream os = con.getOutputStream()) {
//                byte[] input = message.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            con.getResponseCode();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    private static String messageId = null;
    private static String currentContent = "";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static synchronized void notify(String msg) {
        System.out.println(msg);

        if(Const.FAKE_TURN || !Const.NOTIFY_BOT) {
            return;
        }

        String webhookUrl = Const.DISCORD_WEBHOOK_URL;

        // Timestamp de l'appel
        String timestamp = LocalTime.now().format(TIME_FORMAT);

        // Ajouter une nouvelle ligne
        if (currentContent.isEmpty()) {
            currentContent = "TOUR " + (Univers.NUMERO_DU_TOUR+1);
        } else {
            currentContent += "\n";
        }
        currentContent += timestamp + " " + msg;

        try {
            if (messageId == null) {
                // Premier envoi -> POST avec ?wait=true
                String response = doRequest("POST", webhookUrl + "?wait=true",
                        "{\"content\":\"" + escape(currentContent) + "\"}");
                messageId = response.split("\"id\":\"")[1].split("\"")[0];
            } else {
                // Éditer le message existant
                doRequest("PATCH", webhookUrl + "/messages/" + messageId,
                        "{\"content\":\"" + escape(currentContent) + "\"}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String doRequest(String method, String urlStr, String body) throws Exception {
        while (true) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int code = response.statusCode();
            String respBody = response.body();

            if (code == 429) {
                // Rate limit
                String retryAfterStr = respBody.split("\"retry_after\":")[1].split("[,}]")[0].trim();
                long waitMs = (long) (Double.parseDouble(retryAfterStr) * 1000);
                System.out.println("Rate limited, waiting " + waitMs + "ms");
                Thread.sleep(waitMs);
                continue;
            }

            if (code >= 200 && code < 300) {
                return respBody;
            } else {
                throw new RuntimeException("HTTP " + code + " : " + respBody);
            }
        }
    }

    private static String escape(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

}
