// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.Locale;
import zIgzAg.utile.Mdt;

public class Produit extends Technologie implements Serializable {

	static final long serialVersionUID = 5083537388848753024L;

	private int mineraiNecessaire;
	private float prix;
	private int[][] marchandisesNecessaires;

	// mÃ©thode provisoire

	public void setPrix(float p) {
		prix = p;
	}

	// fin mÃ©thode provisoire

	// les mÃ©thodes d'accÃšs

	public int getMineraiNecessaire() {
		return mineraiNecessaire;
	}

	public float getPrix() {
		return prix;
	}

	public int[][] getMarchandises() {
		return marchandisesNecessaires;
	}

	public int getQuantiteMarchandise(int marchandise) {
		return Mdt.valeurCorrespondante(marchandisesNecessaires, marchandise);
	}

    public String getListeMarchandises(Locale l) {
        return getListeMarchandises(l, "<br />");
    }
	public String getListeMarchandises(Locale l, String separator) {
		if (marchandisesNecessaires == null)
			return " ";
		String retour = new String();
		for (int i = 0; i < marchandisesNecessaires.length; i++) {
			retour = retour
					+ Utile.maj(Univers.getMessage("MARCHANDISES",
							marchandisesNecessaires[i][0], l)) + " : "
					+ Integer.toString(marchandisesNecessaires[i][1]);
			if (i != marchandisesNecessaires.length - 1)
				retour = retour + separator;
		}
		return retour;
	}

	public int getNombreMarchandises() {
		if (marchandisesNecessaires == null)
			return 0;
		else
			return marchandisesNecessaires.length;
	}

	// Le constructeur

	protected Produit() {
	}

	public Produit(String code, int niv, String[] parent, int recherche,
			int[][] caracS, int minerai, float pri, int[][] mar) {
		super(code, niv, parent, recherche, caracS);
		mineraiNecessaire = minerai;
		prix = pri;
		marchandisesNecessaires = mar;
	}

	// les autres mÃ©thodes

}
