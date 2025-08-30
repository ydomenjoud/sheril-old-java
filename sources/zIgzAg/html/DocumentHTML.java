// v 1.10 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.html;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class DocumentHTML {


	private static final int TAILLE_BUFFER_PAR_DEFAUT = 100000;


	private BaliseHTML document;


	private StringBuffer texte;


	private File fichier;


	private int tailleDuBuffer;


	public DocumentHTML(String nomFichier, BaliseHTML baliseRacine) {
		this(nomFichier, baliseRacine, TAILLE_BUFFER_PAR_DEFAUT);
	}


	public DocumentHTML(String nomFichier, BaliseHTML baliseRacine,
			int tailleBuffer) {
		tailleDuBuffer = tailleBuffer;
		fichier = new File(nomFichier);
		document = baliseRacine;
	}

	public static StringBuffer traduire(StringBuffer entree) {
		return entree;
	}


	public void ecrire() {
		texte = new StringBuffer(tailleDuBuffer);
		BaliseHTML.affiche(texte, document);
		traduire(texte);

		if (fichier.getParentFile() != null)
			fichier.getParentFile().mkdirs();
		try {
			BufferedWriter fluxE = new BufferedWriter(new FileWriter(fichier));
			String doc = texte.toString();
			fluxE.write(doc, 0, doc.length());
			fluxE.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BaliseHTML creerTable(Object[][] tableau) {
		BaliseHTML table = new BaliseHTML(BaliseHTML.TABLE);
		BaliseHTML ligne;
		BaliseHTML colonne;
		for (int i = 0; i < tableau.length; i++) {
			ligne = new BaliseHTML(BaliseHTML.TR);
			for (int j = 0; j < tableau[0].length; j++) {
				colonne = new BaliseHTML(BaliseHTML.TD,
						String.valueOf(tableau[i][j]));
				ligne.ajout(colonne);
			}
			table.ajout(ligne);
		}
		return table;
	}


	public static BaliseHTML creerTable(BaliseHTML[][] tableau,
			BaliseHTML[] lignes) {
		BaliseHTML table = new BaliseHTML(BaliseHTML.TABLE);
		for (int i = 0; i < tableau.length; i++) {
			for (int j = 0; j < tableau[0].length; j++)
				lignes[i].ajout(tableau[i][j]);
			table.ajout(lignes[i]);
		}
		return table;
	}


	public static BaliseHTML creerTable(BaliseHTML table, BaliseHTML[][] tableau) {
		BaliseHTML ligne;
		boolean ligneValide;
		for (int i = 0; i < tableau.length; i++) {
			ligne = new BaliseHTML(BaliseHTML.TR);
			ligneValide = false;
			for (int j = 0; j < tableau[0].length; j++)
				if (tableau[i][j] != null) {
					ligne.ajout(tableau[i][j]);
					ligneValide = true;
				}
			if (ligneValide)
				table.ajout(ligne);
		}
		return table;
	}

}