// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import zIgzAg.utile.Mdt;

public class Construction implements Serializable {

	static final long serialVersionUID = 5944192000879941192L;

	private String code;

	private int nombre;

	private int pointsEffectues;

	private int planete;

	public void setPointsEffectues(int p) {
		pointsEffectues = p;
	}

	public void diminuerNombre(int nb) {
		nombre = nombre - nb;
	}

	public void augmenterNombre(int nb) {
		nombre = nombre + nb;
	}

	public String getCode() {
		return code;
	}

	public int getNombre() {
		return nombre;
	}

	public int getPlanete() {
		return planete;
	}

	public int getPointsEffectues() {
		return pointsEffectues;
	}

	public int getPointsNecessaires() {
		return getPointsNecessaires(code) * nombre - pointsEffectues;
	}

	public static int getPointsNecessaires(String code) {
		try {
			int coutParUnite = 0;
			if (Univers.existenceTechnologie(code))
				coutParUnite = ((Batiment) Univers.getTechnologie(code))
						.getPointsDeConstruction();
			else
				coutParUnite = Math.max(1, Univers.getPlanDeVaisseau(code)
						.getNombreDeCases() / 2);
			return coutParUnite;
		} catch (Exception e) {
			System.out.println("erreur dans le calcul des points nécessaire pour " + code +  " : " + e.getMessage());
			return 0;
		}

	}

	public float getPrix() {
		return getPrix(code) * nombre;
	}

	public static float getPrix(String code) {
		float prix = 0F;
		if (Univers.existenceTechnologie(code))
			prix = ((Batiment) Univers.getTechnologie(code)).getPrix();
		else
			prix = Univers.getPlanDeVaisseau(code).getPrix();
		return prix;
	}

	public static float getPrixConstructionEspace(String code) {
		return Univers.getPlanDeVaisseau(code).getPrixConstructionEspace();
	}

	public int getMinerai() {
		return getMinerai(code) * nombre;
	}

	public static int getMinerai(String code) {
		int minerai = 0;
		if (Univers.existenceTechnologie(code))
			minerai = ((Batiment) Univers.getTechnologie(code))
					.getMineraiNecessaire();
		else
			minerai = Univers.getPlanDeVaisseau(code).getMineraiNecessaire();
		return minerai;
	}

	public int[][] getMarchandises() {
		int[][] retour = (int[][]) Mdt.cloneTableau(getMarchandises(code));
		if (retour == null)
			return new int[0][0];
		for (int i = 0; i < retour.length; i++)
			retour[i][1] = retour[i][1] * nombre;
		return retour;
	}

	public static int[][] getMarchandises(String code) {
		int[][] marchandises = null;
		if (Univers.existenceTechnologie(code))
			marchandises = ((Batiment) Univers.getTechnologie(code))
					.getMarchandises();
		else
			marchandises = Univers.getPlanDeVaisseau(code).getMarchandises();
		return marchandises;
	}

	private Construction() {
	}

	public Construction(String co, int nb, int pla) {
		code = co;
		nombre = nb;
		planete = pla;
	}

}
