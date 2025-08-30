// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.Locale;

public class Marchand extends Leader implements Serializable {

	public boolean estEnReserve() {
		return false;
	}

	public void mettreEnReserve() {
	}

	public String descriptionPosition(Locale l) {
		return "En service";
	}

	public int nombreDeNiveauxDeCompetence(int comp) {
		if ((comp == 6) || (comp == 9) || (comp == 10) || (comp == 11))
			return 0;
		return 5;
	}

	public int competenceNouvelleAuHasard(int r) {
		while (true) {
			int hasard = Univers.getInt(101);
			int index = 0;
			for (int i = 0; i < Const.NOMBRE_COMPETENCES; i++)
				if (hasard > 0) {
					hasard = hasard
							- Const.CHANCE_TROUVER_COMPETENCE_GOUVERNEUR[r][i];
					index = i;
				}
			if (getNiveauCompetence(index) < nombreDeNiveauxDeCompetence(index))
				return index;
		}
	}

	private Marchand() {
	}

	public Marchand(String n, int[][] comp, int vit, int att, int def, int mor,
			int march, int rac, int tour) {
		super(n, comp, vit, att, def, mor, march, rac, tour);
	}

}
