// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;


public class Recuperation {

	public static void main(String[] args) {

		Univers univers = new Univers(true, "SUrprise");

		Commandant[] cL = Univers.getListeCommandants();

		Commandant[] cL2 = Univers.getListeCommandantsHumains();
		for (int i = 0; i < cL2.length; i++) {

			System.out
					.println("INSERT INTO aa_registre (NUMERO,LOGIN,MOT_DE_PASSE,NOM,ADRESSE,RACE,TOUR_ARRIVEE) VALUES ('"
							+ cL2[i].getNumero()
							+ "','"
							+ cL2[i].getLogin()
							+ "','"
							+ cL2[i].getMotDePasse()
							+ "','"
							+ cL2[i].getNom()
							+ "','"
							+ cL2[i].getAdresseElectronique()
							+ "','"
							+ cL2[i].getRace()
							+ "','"
							+ cL2[i].getTourArrivee() + "');");
		}
	}

}
