package zIgzAg.jeu.oceane;

/**
 * Règles commutables du combat spatial. Les valeurs par défaut reproduisent le moteur
 * tel qu'il tourne en partie ; les autres combinaisons servent au banc d'essai
 * (BenchCombat) pour mesurer l'effet d'une règle.
 */
public class ReglesCombat {

	/** Un vaisseau engage plusieurs cibles par round (Const.NB_CIBLES selon sa taille). Actif dans le moteur ; false = ancien tir sur cible unique. */
	public boolean multiCible = true;

	/** Chaque tir peut s'enrayer (Arme.getTauxEnrayement : 20 % − niveau de l'arme). Actif dans le moteur. */
	public boolean fiabilite = true;
	/**
	 * Tir continu : à son tour (ordre du tempo, qui dépend de la vitesse des armes), un vaisseau
	 * enchaîne ses cibles tant qu'il lui reste des armes, au lieu d'attendre la passe suivante
	 * pour changer de cible. Actif dans le moteur ; false = ancien tir par passes (une cible par
	 * vaisseau et par passe).
	 */
	public boolean tirContinu = true;
	/**
	 * Cibles vivantes : quand plus aucun vaisseau du type visé ne reste, le choix de cible se
	 * rabat sur toute la flotte adverse ; true en écarte les vaisseaux déjà détruits pendant le
	 * round. Actif dans le moteur ; false = ancien moteur, qui peut désigner une épave et perdre ce tir.
	 */
	public boolean ciblesVivantes = true;

	/** Si >= 0 : taux d'enrayement identique pour toutes les armes (règle fiabNN, NN = fiabilité en %). */
	public int enrayementUniforme = -1;

	/**
	 * Si >= 0 : taux d'enrayement selon la taille du vaisseau qui tire (règle fiabtA-B),
	 * de enrayementT1 % pour une coque T1 à enrayementT10 % pour une coque T10, linéaire entre les deux.
	 */
	public int enrayementT1 = -1;
	public int enrayementT10 = -1;

	/**
	 * Si >= 0 : le taux de l'arme (qui baisse déjà avec son niveau) baisse encore de
	 * reductionParTaille points par cran de taille du vaisseau qui tire (T1 : 0, T2 : 1 × N…).
	 */
	public int reductionParTaille = -1;

	/**
	 * Si >= 0 : le taux de l'arme (20 % − niveau) monte de hausseParTaille points par cran de taille
	 * au-dessus de T5 et baisse d'autant en dessous (règle fiabplustailleK ; arme V, K = 5 : T3 6 %, T9 36 %).
	 */
	public int hausseParTaille = -1;

	/**
	 * Règle fiabniveautaille : taux de l'arme (20 % − niveau) plus (10 − taille), T1 … T10
	 * (arme V : T3 23 %, T6 20 %, T9 17 %).
	 */
	public boolean enrayementNiveauTaille = false;

	/**
	 * Règle fiaborigine : taux propre à chaque arme (colonne fiabilité de ListeCaracArmes) moins le niveau,
	 * comme avant le commit 82b6849 (arme V : laser et plasma 16 %, torpille 21 %, missile 26 %).
	 */
	public boolean enrayementOrigine = false;

	/**
	 * Si > 0 (règle salveN, tir continu seulement) : à chaque passage, un vaisseau utilise au plus
	 * salve × (taille + 1)² armes (T3 : 9, T6 : 36, T9 : 81 pour salve1), puis attend que tous les
	 * vaisseaux aient fait leur passage avant de reprendre.
	 */
	public int salve = -1;

	/** Taux d'enrayement d'une arme portée par un vaisseau de la taille donnée (0 = T1 … 9 = T10). */
	public int tauxEnrayement(Arme arme, int taille) {
		if (enrayementUniforme >= 0)
			return enrayementUniforme;
		if (enrayementT1 >= 0)
			return enrayementT1 + Math.round((enrayementT10 - enrayementT1) * taille / 9f);
		if (enrayementOrigine)
			return Math.max(0, Univers.getCaracArme(arme.getCorpsCode())[Const.ARME_FIABILITE] - arme.getNiveau());
		if (enrayementNiveauTaille)
			return Math.max(0, arme.getTauxEnrayement() + (10 - (taille + 1)));
		if (hausseParTaille >= 0)
			return Math.max(0, arme.getTauxEnrayement() + hausseParTaille * (taille - 4));
		if (reductionParTaille >= 0)
			return Math.max(0, arme.getTauxEnrayement() - reductionParTaille * taille);
		return arme.getTauxEnrayement();
	}

	/** Écriture du journal de combat (fichier de log, console, SherilLogger). */
	public boolean journal = true;

	private static ReglesCombat ACTIVES = new ReglesCombat();

	public static ReglesCombat actives() {
		return ACTIVES;
	}

	public static void activer(ReglesCombat regles) {
		ACTIVES = regles;
	}

}
