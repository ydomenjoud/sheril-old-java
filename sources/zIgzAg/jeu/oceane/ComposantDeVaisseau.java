// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

public class ComposantDeVaisseau extends Produit {

	private String type;
	private int nombreDeCasesPrises;

	// les mÃ©thodes d'accÃšs

	public String getTypeArme() {
		return type;
	}

	public int getNombreDeCases() {
		return nombreDeCasesPrises;
	}

	public boolean estArme() {
		if (type.equals(Const.CV_ARME))
			return true;
		else
			return false;
	}

	public boolean estMoteur() {
		if (type.equals(Const.CV_MOTEUR))
			return true;
		else
			return false;
	}

	public boolean estBouclier() {
		if (possedeCaracteristiqueSpeciale(Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE))
			return true;
		else
			return false;
	}

	public boolean estUsineVaisseaux() {
		if (possedeCaracteristiqueSpeciale(Const.COMPOSANT_CAPACITE_NAVIRE_USINE))
			return true;
		else
			return false;
	}

	public int getNombreDeCasesPrises() {
		return nombreDeCasesPrises;
	}

	public int getNiveauBouclier() {
		return getValeurCaracteristiqueSpeciale(Const.COMPOSANT_CAPACITE_BOUCLIER_MAGNETIQUE);
	}

	// les mÃ©thodes statiques

	// Le constructeur

	protected ComposantDeVaisseau() {
	}

	public ComposantDeVaisseau(String code, int niv, String[] parent,
			int recherche, int[][] caracS, int minerai, float pri, int[][] mar,
			String typ, int nbCases) {

		super(code, niv, parent, recherche, caracS, minerai, pri, mar);
		type = typ;
		nombreDeCasesPrises = nbCases;
	}

	// les autres mÃ©thodes

}
