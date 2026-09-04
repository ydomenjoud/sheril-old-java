package zIgzAg.jeu.oceane;

import java.io.Serializable;

/**
 * Représente un paquet de départ réservé pour un futur joueur au tour 0.
 */
public class PaquetDepart implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Position capitale;
	private final Position secondSysteme;
	private final Position[] systemesNeutres;
	private boolean attribue;

	public PaquetDepart(Position capitale, Position secondSysteme,
			Position[] systemesNeutres) {
		this.capitale = capitale;
		this.secondSysteme = secondSysteme;
		this.systemesNeutres = systemesNeutres;
	}

	public Position getCapitale() {
		return capitale;
	}

	public Position getSecondSysteme() {
		return secondSysteme;
	}

	public Position[] getSystemesNeutres() {
		return systemesNeutres;
	}

	public boolean estAttribue() {
		return attribue;
	}

	public void attribuer() {
		attribue = true;
	}
}
