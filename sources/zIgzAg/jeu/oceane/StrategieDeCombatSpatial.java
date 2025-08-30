// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.HashMap;

import zIgzAg.utile.Mdt;

public class StrategieDeCombatSpatial implements Serializable {

	static final long serialVersionUID = -5672522962514066266L;

	String nom;
	HashMap positionnement;
	HashMap comportement;
	int agressivite;
	int typeCible;

	// les mÃ©thodes d'accÃšs

	public String getNom() {
		return nom;
	}

	public String[] getTypesDeVaisseau() {
		return (String[]) positionnement.keySet().toArray(new String[0]);
	}

	public int[] getPositionnement(String code) {
		Object o = positionnement.get(code);
		if (o != null)
			return (int[]) o;
		else
			return null;
	}

	public void setPositionnement(String code, int[] pos) {
		positionnement.put(code, pos);
	}

	public int[] getCibles(String code) {
		Object o = comportement.get(code);
		if (o != null)
			return (int[]) o;
		else
			return null;
	}

	public int getAgressivite() {
		return agressivite;
	}

	public int getTypeCible() {
		return typeCible;
	}

	public static boolean estStrategieParDefaut(String code) {
		return code.equals(Const.STRATEGIE_DEFAUT.getNom());
	}

	// les mÃ©thodes statiques

	public static boolean[] estStrategieValide(Commandant c, int agress,
			int typeC, String[] vaisseau, int[][] pos, int[][] tC) {
		if (agress > Const.STRATEGIE_AGRESSIVITE_NB_MAXIMAL) {
			Univers.ajouterErreur(c.getNomNumero(), "ER_STRATEGIE_COMBAT_0000",
					agress);
			return null;
		}
		if (typeC > Const.STRATEGIE_AGRESSIVITE_NB_MAXIMAL) {
			Univers.ajouterErreur(c.getNomNumero(), "ER_STRATEGIE_COMBAT_0001",
					typeC);
			return null;
		}
		boolean[] retour = new boolean[vaisseau.length];
		for (int i = 0; i < vaisseau.length; i++) {
			boolean ok = true;
			if (!Univers.existencePlanDeVaisseau(vaisseau[i])) {
				Univers.ajouterErreur(c.getNomNumero(),
						"ER_STRATEGIE_COMBAT_0002", vaisseau[i]);
				ok = false;
			}
			if ((pos[i][0] > Const.COMBAT_X_MAX)
					|| (pos[i][1] > Const.COMBAT_Y_MAX)) {
				Univers.ajouterErreur(c.getNomNumero(),
						"ER_STRATEGIE_COMBAT_0003", vaisseau[i], pos[i][0],
						pos[i][1]);
				ok = false;
			}
			boolean bon = true;
			for (int j = 1; j < 11; j++)
				if (!Mdt.estPresent(tC[i], j))
					bon = false;
			if (!bon) {
				c.ajouterErreur("ER_STRATEGIE_COMBAT_0000", vaisseau[i]);
				ok = false;
			}
			if (ok)
				retour[i] = true;
		}

		return retour;
	}

	// Le constructeur

	private StrategieDeCombatSpatial() {
	}

	public StrategieDeCombatSpatial(String n, int agress, int typeCib,
			String[] typeDeVaisseau, int[][] place, int[][] cibles) {
		nom = n;
		agressivite = agress;
		typeCible = typeCib;
		comportement = new HashMap(typeDeVaisseau.length, 1F);
		positionnement = new HashMap(typeDeVaisseau.length, 1F);
		for (int i = 0; i < typeDeVaisseau.length; i++) {
			comportement.put(typeDeVaisseau[i], cibles[i]);
			int[] inter = new int[3];
			inter[0] = place[i][0];
			inter[1] = place[i][1];
			positionnement.put(typeDeVaisseau[i], inter);
		}
	}

	// les autres mÃ©thodes

}
