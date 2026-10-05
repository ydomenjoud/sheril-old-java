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
		this.capitale = copier(capitale);
		this.secondSysteme = copier(secondSysteme);
		this.systemesNeutres = copier(systemesNeutres);
	}

	public Position getCapitale() {
		return copier(capitale);
	}

	public Position getSecondSysteme() {
		return copier(secondSysteme);
	}

	public Position[] getSystemesNeutres() {
		return copier(systemesNeutres);
	}

	private static Position copier(Position position) {
		return position == null ? null : (Position) position.clone();
	}

	private static Position[] copier(Position[] positions) {
		if (positions == null)
			return new Position[0];
		Position[] copie = new Position[positions.length];
		for (int i = 0; i < positions.length; i++)
			copie[i] = copier(positions[i]);
		return copie;
	}

	public boolean estAttribue() {
		return attribue;
	}

	public void attribuer() {
		attribue = true;
	}
}
