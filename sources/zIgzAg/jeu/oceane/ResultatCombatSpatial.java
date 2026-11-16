package zIgzAg.jeu.oceane;

import java.util.Map;

/** Issue d'un combat spatial résolu par Combat.resoudreCombatSpatial. */
public class ResultatCombatSpatial {

	/** La flotte 2 a été anéantie. */
	public final boolean vainqueur1;

	/** La flotte 1 a été anéantie. */
	public final boolean vainqueur2;

	/** Points de dommages subis par la flotte 1 pendant le combat. */
	public final int dommages1;

	/** Points de dommages subis par la flotte 2 pendant le combat. */
	public final int dommages2;

	public final int nbRounds;

	/** État par type de vaisseau à la fin du combat (Flotte.listeVaisseauxParTypePourCombat). */
	public final Map mem1;

	public final Map mem2;

	public ResultatCombatSpatial(boolean vainqueur1, boolean vainqueur2, int dommages1, int dommages2,
			int nbRounds, Map mem1, Map mem2) {
		this.vainqueur1 = vainqueur1;
		this.vainqueur2 = vainqueur2;
		this.dommages1 = dommages1;
		this.dommages2 = dommages2;
		this.nbRounds = nbRounds;
		this.mem1 = mem1;
		this.mem2 = mem2;
	}

}
