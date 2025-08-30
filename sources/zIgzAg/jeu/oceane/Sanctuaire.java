// v2.00 18/10/2004
// Copyright 2003 Yannick Domenjoud All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;

public class Sanctuaire implements Serializable {

	private int niveau;
	private int nbtechno;

	public int getNiveau() {
		return niveau;
	}

	public int getNbTechno() {
		return nbtechno;
	}

	public void DiminuerNbTechno() {
		nbtechno = nbtechno - 1;
	}

	public void setNiveau(int entree) {
		niveau = entree;
	}

	public Sanctuaire() {
		niveau = Univers.getInt(5) + 1;
		nbtechno = Univers.getInt(2) + 1;
	}

}
