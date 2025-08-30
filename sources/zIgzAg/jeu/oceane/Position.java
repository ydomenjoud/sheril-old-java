// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.Locale;
import java.util.ArrayList;
import zIgzAg.html.BaliseHTML;

@SuppressWarnings("rawtypes")
public class Position implements Serializable, Comparable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3643415888303832192L;
	
	private int galaxie;
	private int[] pos;

	public Position() {
		pos = new int[2];
	}

	public Position(int gala, int y, int x) {
		this();
		galaxie = gala;
		pos[0] = y;
		pos[1] = x;
	}

	public Position(int gala, int[] position) {
		galaxie = gala;
		pos = (int[]) position.clone();
	}

	public int getNumeroGalaxie() {
		return galaxie;
	}

	public String getGalaxie(Locale loc) {
		return Univers.getMessage("NOMS_GALAXIES", galaxie, loc);
	}

	public String getDescription(Locale loc) {
		String cle = "TD_POS_" + Integer.toString(galaxie);
		BaliseHTML b = BaliseHTML.getModele(cle);
		if (b == null) {
			b = new BaliseHTML(BaliseHTML.FONT, BaliseHTML.COLOR,
					Rapport.COULEURS_GALAXIES[galaxie]);
			BaliseHTML.ajouterModele(cle, b);
		}
		b.setTexteContenu(Integer.toString(pos[0]) + "-"
				+ Integer.toString(pos[1]) + " [" + getGalaxie(loc) + "]");
		return b.toString();
	}

	public String getDescription() {
		int numero = Univers.getTheSecteur(this);
		int secteur = Utile.getCouleurSecteur(numero);

		String cle = "TD_POS_" + Integer.toString(galaxie);
		BaliseHTML b = BaliseHTML.getModele(cle);
		if (b == null) {
			b = new BaliseHTML(BaliseHTML.FONT, BaliseHTML.COLOR,
					Rapport.COULEURS_GALAXIES[galaxie]);
			BaliseHTML.ajouterModele(cle, b);
		}
		b.setTexteContenu(Integer.toString(pos[0]) + "-"
				+ Integer.toString(pos[1]) + " s(" + numero + ")");
		//
		return b.toString();
	}

	public String getDescriptionSimple(Locale loc) {
		return Integer.toString(pos[0]) + "-" + Integer.toString(pos[1]) + " ["
				+ getGalaxie(loc) + "]";
	}

	public int[] getPos() {
		return pos;
	}

	public int getY() {
		return pos[0];
	}

	public int getX() {
		return pos[1];
	}

	public void setNumeroGalaxie(int e) {
		galaxie = e;
	}

	public void setPos(int[] e) {
		pos = e;
	}

	public int getNumeroSecteur() {
        int ratio = (int) Math.sqrt(Const.NB_SECTEURS);
		if (1 + ((pos[1] - 1) / 20) + ratio * ((pos[0] - 1) / 20) > Const.NB_SECTEURS) {
			return Const.NB_SECTEURS;
		} else {
			return 1 + ((pos[1] - 1) / 20) + ratio * ((pos[0] - 1) / 20);
		}
	}
	
	public boolean estValide(){
		return existe(this);
	}

	// mÃ©thode pour obtenir une position au hasard.

	public static Position auHasard(int galaxie) {
		Position retour = new Position();
		retour.setNumeroGalaxie(galaxie);
		retour.setPos(Univers.getTabInt(Const.BORNE_MAX, 1, 2));
		return retour;
	}
    public static Position auHasardInSector(int galaxie, int sector){
        Position retour = new Position();
        retour.setNumeroGalaxie(galaxie);
        retour.setPos(Univers.getTabInt(Const.BORNE_MAX, 1, 2));
        return retour;
    }

	// methode pour vÃ©rifier si une position est dans les bornes prÃ©vues.

	public static boolean existe(Position p) {
		if ((p.getNumeroGalaxie() < 0) || (p.getNumeroGalaxie() > Const.NB_GALAXIES))
			return false;
		if ((p.getY() < 1) || (p.getY() > Const.BORNE_MAX))
			return false;
		if ((p.getX() < 1) || (p.getX() > Const.BORNE_MAX))
			return false;
		return true;
	}

	// mÃ©thodes pour tester l'Ã©galitÃ© et utiliser la classe Position comme
	// clÃ© dans une Hashtable ou une TreeMap.

	public boolean equals(Object objet) {
		if (objet == null)
			return false;
		Position entree = (Position) objet;
		if ((entree.getNumeroGalaxie() == galaxie) && (entree.getY() == pos[0])
				&& (entree.getX() == pos[1]))
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (galaxie * 100000 + pos[0] * 1000 + pos[1]);
	}

	public int compareTo(Object objet) {
		return (hashCode() - ((Position) objet).hashCode());
	}

	// pour cloner une position.

	public Object clone() {
		return new Position(galaxie, pos);
	}

	// reprÃ©sentation "de base".

	public String toString() {
		return Integer.toString(galaxie) + "_" + Integer.toString(pos[0]) + "_"
				+ Integer.toString(pos[1]);
	}

	// sens inverse

	public static Position traduction(String s) {
		try {
			char[] t = s.toCharArray();
			int[] inter = new int[3];
			StringBuffer sb = new StringBuffer(3);
			int i = 0;
			while (t[i] != '_')
				sb.append(t[i++]);
			inter[0] = Integer.parseInt(sb.toString());
			sb = new StringBuffer(3);
			i++;
			while (t[i] != '_')
				sb.append(t[i++]);
			inter[1] = Integer.parseInt(sb.toString());
			sb = new StringBuffer(3);
			i++;
			while (i < t.length)
				sb.append(t[i++]);
			inter[2] = Integer.parseInt(sb.toString());
			return new Position(inter[0], inter[1], inter[2]);
		} catch (Exception e) {
			Univers.ajouterErreur("Hum hum", "ER_POSITION_0000", s);
			return Position.auHasard(0);
		}
	}

	// nouvelle position aprÃšs un dÃ©placement de x case et autres fonctions de
	// dÃ©placement.

	public static Position positionAtteinte(Position depart, Position arrivee,
			int x) {
		Position retour = (Position) depart.clone();
		int[] inter = retour.getPos();
		inter[0] = Utile.courtChemin(depart.getY(), arrivee.getY(), x);
		inter[1] = Utile.courtChemin(depart.getX(), arrivee.getX(), x);
		return retour;
	}

	public static int distance(Position p1, Position p2) {
		if (p1.getNumeroGalaxie() != p2.getNumeroGalaxie())
			return Integer.MAX_VALUE;
		int distanceY = Math.abs(p1.getY() - p2.getY());
		int distanceX = Math.abs(p1.getX() - p2.getX());
		if (distanceY > Const.BORNE_MAX / 2)
			distanceY = Const.BORNE_MAX - distanceY;
		if (distanceX > Const.BORNE_MAX / 2)
			distanceX = Const.BORNE_MAX - distanceX;
		int distance = Math.max(distanceY, distanceX);
		return distance;
	}

	public static Position[] getPositionsADistance(Position base,
			Position[] liste, int portee) {
		if (portee == 0)
			return new Position[0];
		ArrayList l = new ArrayList(liste.length);
		for (int i = 0; i < liste.length; i++)
			if (distance(base, liste[i]) <= portee)
				l.add(liste[i]);
		return (Position[]) l.toArray(new Position[0]);
	}

	public static Position positionLaPlusProche(Position depart,
			int[][] arrivees) {
		int min = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < arrivees.length; i++)
			if (Math.max(Math.abs(depart.getY() - arrivees[i][0]),
					Math.abs(depart.getX() - arrivees[i][1])) < min) {
				min = Math.max(Math.abs(depart.getY() - arrivees[i][0]),
						Math.abs(depart.getX() - arrivees[i][1]));
				index = i;
			}
		if (index != -1)
			return new Position(depart.getNumeroGalaxie(), arrivees[index]);
		else
			return (Position) depart.clone();
	}

	public static Position deplacementVers(Position depart, Position arrivee, int deplacement, boolean interGalactique) {
		if (depart.getNumeroGalaxie() == arrivee.getNumeroGalaxie())
			return positionAtteinte(depart, arrivee, deplacement);
		else {
			Position passage = positionLaPlusProche(depart, Const.PASSAGES_GALACTIQUES);
			if ((interGalactique) || (depart.equals(passage))) {
				Position retour = positionAtteinte(depart, arrivee, deplacement);
				retour.setNumeroGalaxie(arrivee.getNumeroGalaxie());
				return retour;
			} else
				return positionAtteinte(depart, passage, deplacement);
		}
	}

}
