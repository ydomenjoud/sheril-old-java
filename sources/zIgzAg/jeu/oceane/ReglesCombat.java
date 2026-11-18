package zIgzAg.jeu.oceane;

/**
 * Règles commutables du combat spatial. Les valeurs par défaut reproduisent le moteur
 * tel qu'il tourne en partie ; les autres combinaisons servent au banc d'essai
 * (BenchCombat) pour mesurer l'effet d'une règle.
 */
public class ReglesCombat {

	/** Un vaisseau engage plusieurs cibles par round (Const.NB_CIBLES selon sa taille). Actif dans le moteur ; false = ancien tir sur cible unique. */
	public boolean multiCible = true;

	/** Chaque tir peut s'enrayer, selon la caractéristique « fiabilité » de l'arme. */
	public boolean fiabilite = false;
	/**
	 * Tir continu : à son tour (ordre du tempo, qui dépend de la vitesse des armes), un vaisseau
	 * enchaîne ses cibles tant qu'il lui reste des armes, au lieu d'attendre la passe suivante
	 * pour changer de cible. false = tir par passes (une cible par vaisseau et par passe).
	 */
	public boolean tirContinu = false;
	/**
	 * Cibles vivantes : quand plus aucun vaisseau du type visé ne reste, le choix de cible se
	 * rabat sur toute la flotte adverse ; true en écarte les vaisseaux déjà détruits pendant le
	 * round (false = moteur actuel, qui peut désigner une épave et perdre ce tir).
	 */
	public boolean ciblesVivantes = false;

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

	/** Taux d'enrayement d'une arme portée par un vaisseau de la taille donnée (0 = T1 … 9 = T10). */
	public int tauxEnrayement(Arme arme, int taille) {
		if (enrayementUniforme >= 0)
			return enrayementUniforme;
		if (enrayementT1 >= 0)
			return enrayementT1 + Math.round((enrayementT10 - enrayementT1) * taille / 9f);
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
