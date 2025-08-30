// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;

public class Population implements Serializable {

	private int race;
	private int popActuelle;

	// les mÃ©thodes d'accÃšs.

	public int getRace() {
		return race;
	}

	public int getPopActuelle() {
		return popActuelle;
	}

	public void setRace(int entree) {
		race = entree;
	}

	public void setPopActuelle(int entree) {
		popActuelle = entree;
	}

	public Population() {
	}

}
