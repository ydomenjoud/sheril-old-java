// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import zIgzAg.html.BaliseHTML;
import zIgzAg.html.DocumentHTML;

public class SecteursAtlas {

	public static boolean test8(int k) {
		if ((k > 20) || (k == 5) || (k == 10) || (k == 15) || (k == 20)
				|| (k == 0))
			return false;
		else
			return true;
	}

	public static boolean test7(int k) {
		if ((k > 20) || (k == 0))
			return false;
		else
			return true;
	}

	public static boolean test4(int k) {
		if ((k % 5) == 1)
			return false;
		else
			return true;
	}

	public static boolean test5(int k) {
		if ((k % 5) == 0)
			return false;
		else
			return true;
	}

	public static boolean test6(int k) {
		if ((k > 20) || (k == 1) || (k == 6) || (k == 11) || (k == 16))
			return false;
		else
			return true;
	}

	public static boolean test1(int k) {
		if (((k > 0) && (k < 6)) || (k == 21) || (k == 6) || (k == 11)
				|| (k == 16))
			return false;
		else
			return true;
	}

	public static boolean test2(int k) {
		if ((k < 6) && (k > 0))
			return false;
		else
			return true;
	}

	public static boolean test3(int k) {
		if ((k < 6) || (k == 10) || (k == 15) || (k == 20) || (k == 25))
			return false;
		else
			return true;
	}

	public static String t1(int val) {
		return Integer.toString(val - 6);
	}

	public static String t2(int val) {
		return Integer.toString(val - 5);
	}

	public static String t3(int val) {
		return Integer.toString(val - 4);
	}

	public static String t4(int val) {
		return Integer.toString(val - 1);
	}

	public static String t5(int val) {
		return Integer.toString(val + 1);
	}

	public static String t6(int val) {
		return Integer.toString(val + 4);
	}

	public static String t7(int val) {
		return Integer.toString(val + 5);
	}

	public static String t8(int val) {
		return Integer.toString(val + 6);
	}

	public static void main(String[] args) {
		if (args.length != 1)
			System.exit(0);
		int numG = Integer.parseInt(args[0]);
		ecritureSecteursAtlas(numG);
	}

	public static void ecritureSecteursAtlas(int galaxie) {
		BaliseHTML[][] b = new BaliseHTML[9][2];
		BaliseHTML u = new BaliseHTML(BaliseHTML.U);
		b[0][0] = Rapport.getTD(BaliseHTML.CENTER, "2").ajout(
				Rapport.getFont(Rapport.cC[4], "4").ajout(
						Rapport.getP().ajout(
								u.ajout(Rapport.getText("Légende")))));
		b[1][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile1.gif", 0, 0));
		b[1][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("étoile bleue"));
		b[2][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile2.gif", 0, 0));
		b[2][1] = Rapport.getTD("left", null).ajout(Rapport.getText("Nova"));
		b[3][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile3.gif", 0, 0));
		b[3][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("étoile blanche"));
		b[4][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile4.gif", 0, 0));
		b[4][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("Naine orange"));
		b[5][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile5.gif", 0, 0));
		b[5][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("Naine bleue"));
		b[6][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/etoile6.gif", 0, 0));
		b[6][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("Naine rouge"));
		b[7][1] = Rapport.getTD(null, "2").setTexteContenu("&nbsp;");
		b[8][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getImage("../images/portegal.gif", 0, 0));
		b[8][1] = Rapport.getTD("left", null).ajout(
				Rapport.getText("Passage Intergalactique"));
		BaliseHTML legende = Rapport.getTD("left", null).ajout(
				DocumentHTML.creerTable(Rapport.getTable(), b)
						.ajout(BaliseHTML.BGCOLOR, "")
						.ajout(BaliseHTML.BORDER, "0")
						.ajout(BaliseHTML.CELLPADING, "1"));

		for (int i = 1; i < Const.NB_SECTEURS + 1; i++) {
			BaliseHTML div = Rapport.getDiv();
			div.ajout(
					Rapport.getP().ajout(
							Rapport.getFont(Rapport.cC[3], "6").ajout(
									Rapport.getText("Secteur "
											+ Integer.toString(i))))).ajout(
					Rapport.sautP());
			BaliseHTML[][] a = new BaliseHTML[3][4];

			a[0][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test1(i))
				a[0][0].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t1(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t1(i))));
			a[0][1] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test2(i))
				a[0][1].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t2(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t2(i))));
			a[0][2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test3(i))
				a[0][2].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t3(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t3(i))));

			a[1][0] = Rapport.getTD(BaliseHTML.CENTER, null)
					.ajout(BaliseHTML.WIDTH, "33%").setTexteContenu("&nbsp;");
			if (test4(i))
				a[1][0].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t4(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t4(i))));
			a[1][1] = Rapport
					.getTD(BaliseHTML.CENTER, null)
					.ajout(BaliseHTML.WIDTH, "33%")
					.ajout(Rapport.getImage(Integer.toString(galaxie) + "_"
							+ Integer.toString(i) + ".gif", 0, 0));
			a[1][2] = legende;
			a[1][3] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test5(i))
				a[1][3].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t5(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t5(i))));

			a[2][0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test6(i))
				a[2][0].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t6(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t6(i))));
			a[2][1] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test7(i))
				a[2][1].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t7(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t7(i))));
			a[2][2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					BaliseHTML.WIDTH, "33%");
			if (test8(i))
				a[2][2].ajout(Rapport.getALienE(
						Integer.toString(galaxie) + "_" + t8(i) + ".htm")
						.ajout(Rapport.getText("Secteur " + t8(i))));

			div.ajout(DocumentHTML.creerTable(Rapport.getTable(), a)
					.ajout(BaliseHTML.WIDTH, "80%")
					.ajout(BaliseHTML.BORDER, "0")
					.ajout(BaliseHTML.CELLPADING, "0")
					.ajout(BaliseHTML.BGCOLOR, ""));
			div.ajout(Rapport.getText("<HR>"));
			div.ajout(Rapport.getP().ajout(
					Rapport.getALienE(Integer.toString(galaxie) + ".htm")
							.ajout(Rapport.getText("Retour Ã  l'atlas"))));
			div.ajout(Rapport.getP().ajout(
					Rapport.getALienE("../principal.htm").ajout(
							Rapport.getText("Retour au menu"))));
			DocumentHTML d = Rapport
					.getDocument(Chemin.ATLAS + Integer.toString(galaxie) + "_"
							+ Integer.toString(i) + ".htm", "Secteur "
							+ Integer.toString(i), Rapport.getBody().ajout(div));
			d.ecrire();
		}
	}

}
