// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.util.GregorianCalendar;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Map;

import zIgzAg.utile.Mdt;

public class Utile {

	public static final String[] ROMAINS = { "I", "II", "III", "IV", "V", "VI",
			"VII", "VIII", "IX", "X" };

	// Les chiffres romains de 1 Ã  10

	public static String getNom() {
		char[] alpha = { 'A', 'E', 'I', 'O', 'U', 'Y', 'B', 'C', 'D', 'F', 'G',
				'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V',
				'W', 'X', 'Z', ' ' };
		int t = Univers.getInt(3) + 1;
		int u = Univers.getInt(2);
		char[] tab = new char[2 * t + u];
		for (int i = 0; i < 2 * t; i += 2) {
			tab[i] = alpha[Univers.getInt(27)];
			tab[i + 1] = alpha[Univers.getInt(7)];
		}
		if (u == 1)
			tab[tab.length - 1] = alpha[Univers.getInt(26)];
		for (int i = 1; i < tab.length; i++)
			if (tab[i - 1] != ' ')
				tab[i] = Character.toLowerCase(tab[i]);
		return ((String) new String(tab)).trim();
	}

	// Une fonction qui retourne un nom au hasard.

	public static String getNomLeader() {
		String[] nom = { "Alteron", "Bootu", "Carnak", "Deslok", "Eternox",
				"Forton", "Gergar", "Helfaunt", "Ikul", "Jartol", "Kertilz",
				"Lomax", "Marton", "Negatrik", "Oltru", "Pompatine", "Quikley",
				"Regres", "Sessath", "Trellak", "Ultren", "Vevis", "Welkon",
				"Xexre", "Yulroy", "Zessithriz", "Abee", "Abilene", "Acadia",
				"Acme", "Aetna", "Airdrie", "Alcomdale", "Alderson",
				"Aldersyde", "Alexo", "Alix", "Alliance", "Altario", "Amisk",
				"Ardley", "Ardmore", "Ardrossan", "Armada", "Arrowwood",
				"Ashmont", "Assiniboine", "Athabasca", "Atlee", "Balzac",
				"Banff", "Barnwell", "Barons", "Barrhead", "Bashaw", "Bassano",
				"Bawlf", "Beauvallon", "Beaverlodge", "Beiseker", "Bellevue",
				"Bellis", "Belloy", "Benalto", "Bentley", "Benton", "Berwyn",
				"Beverly", "Bickerdike", "Bindloss", "Bittern", "Blackfalds",
				"Blairmore", "Bluesky", "Bodo", "Bon Accord", "Bonnyville",
				"Botha", "Bowden", "Bowness", "Boyle", "Brant", "Breton",
				"Briereville", "Brocket", "Brooks", "Bruederheim", "Bulwark",
				"Burdett", "Burmis", "Busby", "Byemoore", "Cadogan", "Cadomin",
				"Calmar", "Camrose", "Canmore", "Carbondale", "Cardston",
				"Carmangay", "Carseland", "Carstairws", "Cassils", "Castor",
				"Cavendish", "Cayley", "Cessford", "Chauvin", "Cheadle",
				"Cherhill", "Chinook", "chipewyan", "Chisholm", "Clairmont",
				"Clandonald", "Claresholm", "Clive", "Cluny", "Clyde",
				"Coaldale", "Coalhurst", "Cochrane", "Codesa", "Coleman",
				"Coleridge", "Colinton", "Compeer", "Conklin", "Consort",
				"Coulee", "Coutts", "Cowley", "Craigmyle", "Dalroy", "Dapp",
				"Daysland", "Delburne", "Delia", "Demmitt", "Derwent", "Devon",
				"Dewberry", "Dodds", "Donalda", "Dorenlee", "Dowling",
				"Drayton", "Driftpile", "Drumheller", "Duffield", "Eaglesham",
				"Eckville", "Edberg", "Edgerton", "Edson", "Edwand",
				"Egremont", "Ellscott", "Elnora", "Enchant", "Endiang",
				"Enilda", "Entrance", "Entwistle", "Erskine", "Esther",
				"Etzikom", "Evansburgh", "Exshaw", "Falher", "Ferintosh",
				"ferrier", "Finnegan", "Flatbush", "Galahad", "Gleichen",
				"Glendon", "Grainger", "Granum", "Grimshaw", "Grouard",
				"Gwynne", "Halkirk", "Hardisty", "Hartell", "Haynes", "Hayter",
				"Heinsburg", "Heisler", "Hemaruka", "Hobbema", "Holden",
				"Horburg", "Hussar", "Hylo", "Hythe", "Iddesleigh",
				"Innisfail", "Irma", "Irricana", "Islay", "Jarrow", "Jarvie",
				"Jasper", "Joussard", "Judah", "Kelsey", "Keoma", "Killam",
				"Kinsella", "Kinuso", "Kirkcaldy", "Kirreimuir", "Kitscoty",
				"Lac la Biche", "Lacombe", "Lamont", "Lanfine", "Langdon",
				"Lavoy", "Leduc", "Leyland", "Lomond", "Looma", "Lothrop",
				"Lougheed", "Lousana", "Lundbreck", "Luscar", "Marwayne",
				"Mazeppa", "Mercoal", "Metiskow", "Michichi", "Midnapore",
				"Milo", "Minburn", "Morley", "Morrin", "Mossleigh", "Mundare",
				"Munson", "Myrnam", "Nacmine", "Namaka", "Nampa", "Nanton",
				"Nordegg", "Notikewin", "Obed", "Ohaton", "Okotoks", "Onoway",
				"Oyen", "Pearce", "Peers", "Penhold", "Perryvale", "Pibroch",
				"Pickardville", "Plamondon", "Ponoka", "Provost", "Radway",
				"Raley", "Ranfurly", "Retlaw", "Rimbey", "Robb", "Rosevear",
				"Rowley", "Rumsey", "Rycroft", "Ryley", "Sangudo", "Sawdy",
				"Scapa", "Seba", "Sedalia", "Seebe", "Shouldice", "Sibbald",
				"Skiff", "Sollard", "Spedden", "Spondin", "Stavely", "Sterco",
				"Stettler", "Stirling", "Strome", "Suffield", "Sundre",
				"Sunnynook", "Swalwell", "Taber", "Tawatinaw", "Therien",
				"Thorhild", "Thorsby", "Tilley", "Tofield", "Torrington",
				"Travers", "Trochu", "Turin", "Vegreville", "Vilna", "Wabamun",
				"Wanham", "Warburg", "Wardlow", "Warspite", "Waskatenau",
				"Watino", "Wetaskiwin", "Whatcheer", "Whitla", "Wildwood",
				"Woking", "Wostok", "Wrentham", "Adjud", "Aiud", "Alba Lulia",
				"Anina", "Arad", "Babadag", "Bacau", "Baia Mare", "Bailesti",
				"Bals", "Barlad", "Beius", "Berat", "Beratit", "Bistrita",
				"Blaj", "Botosani", "Boxovici", "Braila", "Brasov", "Buhusi",
				"Burrel", "Buzau", "Calafat", "Calarasi", "Campina",
				"Campulung", "Caracal", "Caransebes", "Carei", "Cernavoda",
				"Chisineu Cris", "Cluj", "Constanta", "Corabia", "Corovode",
				"Craiova", "Curtici", "Cutea de Arges", "Darabani", "Dej",
				"Delvine", "Deva", "Devolt", "Dorohoi", "Drac", "Draganesti",
				"Dragasani", "Drin", "Dukat", "Durres", "Eforie", "Elbasan",
				"Fagaras", "Faiticeni", "Falciu", "Fetesti", "Fier", "Focsani",
				"Gaesti", "Galati", "Gheorgheni", "Gherla", "Giurgiu",
				"Gjinokaster", "Gramsn", "Gura Humorului", "Harlau", "Harsova",
				"Hateg", "Himare", "Huedin", "Hunedoara", "Husi", "Iasi",
				"Isaccea", "Jimbolia", "Karavasta", "Kavaje", "Kefali",
				"Kelcyre", "Kerkira", "Konispol", "Koplik", "Korce", "Kosove",
				"Kruje", "Kukes", "Kute", "Lesh", "Libohove", "Librazhd",
				"Lipova", "Lugoi", "Lupeni", "Lushnje", "Macin", "Mangalia",
				"Marasesti", "Medgidia", "Medias", "Mihai Viteazu", "Mizil",
				"Moinesti", "Nadlac", "Negru Voda", "Ocna Sibiului",
				"Ocnele Mari", "Odobesti", "Odorhei", "Oltenita", "Oradea",
				"Orastie", "Orasul Stalin", "Oravita", "Orsova", "Ostrov",
				"Pali", "Pascani", "Permet", "Peshkopi", "Petroseni",
				"Piatra Neamt", "Pitesti", "Plenita", "Ploesti", "Pogradec",
				"Poshtem", "Prespa", "Radauti", "Ramnicu  Sarat",
				"Ramnicu Valcea", "Reghin", "Resita", "Roman",
				"Rosiorii de Vede", "Salonta", "Sannicolsui Mare", "Sarande",
				"Satu Mare", "Saveni", "Sazan", "Scutari", "Sebes", "Seman",
				"Sfantul Gheorghe", "Shkoder", "Shkumbi", "Sibiu", "Sighet",
				"Sighisoara", "Simluel Silvaniei", "Sinaia", "Siret",
				"Slanic Prhova", "Slatina", "Slobozia", "Steganesti",
				"Strehaia", "Suceava", "Sulina", "Tandarei", "Targoviste",
				"Targul Frumas", "Targul Jiu", "Targul Neamt", "Targul Ocna",
				"Targul Sacusesc", "Tarnaveni", "Techirghiol", "Tecuci",
				"Tepelene", "Timisoara", "Tirane", "Tulcea", "Turda",
				"Turnu Magurele", "Turnu Severin", "Urgu Mures", "Urziceni",
				"Vasui", "Vatra Dornei", "Viziru", "Vlone", "Zalau",
				"Zimnicea", "California", "Oregon", "Washington", "Hawaii",
				"Alaska", "North Dakota", "South Dakota", "Ohio", "Nebraska",
				"Colorado", "New Mexico", "Texas", "Florida", "Tennessee",
				"North Carolina", "South Carolina", "Virginia", "Maine",
				"New York", "Connecticut", "Iowa", "New Jersey", "Georgia",
				"Delaware", "Idaho", "Nevada", "Arizona", "Montana", "Wyoming",
				"Kansas", "Oklahoma", "Louisiana", "Arkansas", "Mississippi",
				"Alabama", "Kentucky", "Indiana", "Illinois", "Wisconsin",
				"Minnesota", "Michigan", "West Virginia", "Vermont",
				"New Hampshire", "Maryland", "Rhode Island", "Missouri",
				"Massachusetts", "Pennsylvania", "Utah" };

		return nom[Univers.getInt(nom.length)];

	}

	public static String maj(String entree) {
		if (entree.length() == 0)
			return entree;
		if (entree.length() == 1)
			return entree.toUpperCase();
		return entree.substring(0, 1).toUpperCase()
				+ entree.substring(1, entree.length());
	}

	// met la premiÃšre lettre du mot en majuscule.

	public static float aD(float valeur, int nbDecimales) {
		float ar = nbDecimales * 10F;
		Integer a = new Integer(Math.round(valeur * ar));
		return a.floatValue() / ar;
	}

	// arrondit Ã  nbDecimales.

	public static float a1D(float valeur) {
		return aD(valeur, 1);
	}

	public static float a2D(float valeur) {
		return aD(valeur, 2);
	}

	// arrondit Ã  une ou deux dÃ©cimales.

	public static int getRaceDeDepart(Position pos) {
		return Univers.getInt(Const.NB_RACES - 1);
	}

	public static boolean AppartiensaZone(Position pos, int zone) {

		if ((pos.getY() > Const.REPARTITION_DES_ZONES[zone][0])
				&& (pos.getY() <= Const.REPARTITION_DES_ZONES[zone][1])
				&& (pos.getX() > Const.REPARTITION_DES_ZONES[zone][2])
				&& (pos.getX() <= Const.REPARTITION_DES_ZONES[zone][3]))
			return true;

		return false;
	}

	// Une fonction qui retourne la race de dÃ©part pour une position donnÃ©e.

	public static String getDate() {
		GregorianCalendar gc = new GregorianCalendar();
		String retour = changeInt(gc.get(GregorianCalendar.DAY_OF_MONTH));
		retour = retour + "-" + changeInt(gc.get(GregorianCalendar.MONTH) + 1);
		retour = retour + "-"
				+ Integer.toString(gc.get(GregorianCalendar.YEAR));
		retour = retour + " "
				+ changeInt(gc.get(GregorianCalendar.HOUR_OF_DAY));
		retour = retour + ":" + changeInt(gc.get(GregorianCalendar.MINUTE));
		retour = retour + ":" + changeInt(gc.get(GregorianCalendar.SECOND));
		return retour;
	}

	public static String getDateRapport() {
		GregorianCalendar gc = new GregorianCalendar();
		String retour = changeInt(gc.get(GregorianCalendar.DAY_OF_MONTH));
		retour = retour + "/" + changeInt(gc.get(GregorianCalendar.MONTH) + 1);
		retour = retour + "/"
				+ Integer.toString(gc.get(GregorianCalendar.YEAR));

		return retour;
	}

	// Une fonction qui retourne une version de la date en chaine de
	// caractÃšres.

	private static String changeInt(int entree) {
		String retour = Integer.toString(entree);
		if (retour.length() == 1)
			retour = "0" + retour;
		return retour;
	}

	// Une fonction qui retourne un nombre Ã  un ou deux chiffres comme String
	// de deux chiffres.

	public static String formatNumber(int nb, int size) {
		String sep = "0";
		String chaine = Integer.toString(nb);
		for (int i = 0; chaine.length() < size; i++)
			chaine = sep + chaine;
		return chaine;
	}

	public static String pareil(Position p) {
		return p.getNumeroGalaxie() + "_" + Utile.formatNumber(p.getY(), 3)
				+ "_" + Utile.formatNumber(p.getX(), 3);
	}


	// une fonction qui indique le plus court chemin entre un point et un autre.
	public static int courtChemin(int depart, int arrivee, int deplacement) {
		if (depart == arrivee) return arrivee;
		int compteur = 0;

		if (depart < arrivee) {
			compteur = Math.min(Math.abs(arrivee - depart), depart - (arrivee - Const.BORNE_MAX));
		} else if (depart > arrivee) {
			compteur = Math.min(Math.abs(depart - arrivee), Math.abs(depart - (arrivee + Const.BORNE_MAX)));
		}
		
		
		if (compteur <= deplacement) {
			return arrivee;
		} else {
			if (depart < arrivee) {
				if (Math.abs(depart - arrivee) < depart - (arrivee - Const.BORNE_MAX)) {
					return (depart + deplacement);
				} else {
					if ((depart - deplacement) < 1) {
						return Const.BORNE_MAX + (depart - deplacement);
					} else {
						return depart - deplacement;
					}
				}
			} else {
				if (Math.abs(depart - arrivee) < Math.abs(depart - (arrivee + Const.BORNE_MAX))) {
					return (depart - deplacement);
				} else {
					if ((depart + deplacement) > Const.BORNE_MAX) {
						return (depart + deplacement - Const.BORNE_MAX);
					} else {
						return depart + deplacement;
					}
				}

			}
		}
	}

	public static int[] transformer(Integer[] t) {
		int[] retour = new int[t.length];
		for (int i = 0; i < t.length; i++)
			retour[i] = t[i].intValue();
		return retour;
	}

	public static int[] transformer3(Object[] t) {
		int[] retour = new int[t.length];
		for (int i = 0; i < t.length; i++)
			retour[i] = Integer.parseInt(t[i].toString());
		return retour;
	}

	public static Integer[] transformer2(int[] t) {
		Integer[] retour = new Integer[t.length];
		for (int i = 0; i < t.length; i++)
			retour[i] = new Integer(t[i]);
		return retour;
	}

	// une fonction qui transforme un tableau d'Integer en tableau d'entier et
	// son inverse.

	public static String[] tableauToString(Object[] t) {
		String[] retour = new String[t.length];
		for (int i = 0; i < t.length; i++)
			retour[i] = t[i].toString();
		return retour;
	}

	// une fonction qui transforme un tableau d'Objet en un tableau des
	// rÃ©sultats des fonctions toString() coorespondants.

	public static Integer[] retournerTableauEntiers(int borne) {
		Integer[] retour = new Integer[borne + 1];
		for (int i = 0; i <= borne; i++)
			retour[i] = new Integer(i);
		return retour;
	}

	// retourne un tableau d'Integer progressifs, utile pour la production des
	// ordres(! borne comprise).

	public static int numeroMarchandise(String code) {
		return Integer.parseInt(code);
	}

	public static int numeroRace(String code) {
		return Mdt.position(Messages.RACES, code);
	}

	// fonctions pour trouver le numÃ©ro correspondant au code.

	public static String choisirLogin() {
		char[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z' };
		char[] retour = new char[Const.TAILLE_LOGIN];
		for (int i = 0; i < Const.TAILLE_LOGIN; i++)
			retour[i] = alpha[Univers.getInt(alpha.length)];
		String l = new String(retour);
		if (!Univers.existenceLogin(l))
			return l;
		else
			return choisirLogin();
	}

	// fonction qui retourne un login.

	public static int[] ordreAuHasard(int t) {
		int[] r = new int[t];
		for (int i = 0; i < t; i++) {
			boolean b = true;
			r[i] = Univers.getInt(t);
			while (b) {
				boolean c = true;
				for (int j = 0; j < i; j++)
					if (r[j] == r[i])
						c = false;
				if (c)
					b = false;
				else
					r[i] = Univers.getInt(t);
			}
		}
		return r;
	}

	// renvoit un tableau d'entier de taille t remplis avec t entiers distincts
	// de 0 Ã  t-1 dans un ordre au hasard.

	public static int getCouleurSecteur(int secteur) {
		if (secteur > 36) {
			secteur = 36;
		}
		int ligne = ((int) ((secteur - 1) / 5)) + 1;
		int colonne = (secteur - 1) % 5;
		Position p = new Position(0, ligne * 20, colonne * 20 + 10);
		int couleur = Utile.getRaceDeDepart(p);
		return couleur;

	}

}
