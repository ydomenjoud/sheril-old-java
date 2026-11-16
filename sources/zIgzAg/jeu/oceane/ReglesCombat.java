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
