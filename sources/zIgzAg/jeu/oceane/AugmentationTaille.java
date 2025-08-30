/* Code pour sheril, by hastake, copyright moi sous license GPL 
 *  D'apres AjoutDeGalaxie
 */

package zIgzAg.jeu.oceane;

import java.util.*;

public class AugmentationTaille {

	static void peupleSecteur(int x, int y, Commandant commandantNeutre) {
		boolean[][] presenceSys = new boolean[120][120];

		int nbSys = 60;

		for (int i = 0; i < nbSys; i++) {
			Position pos = new Position(0, Math.abs((new Random()).nextInt())
					% 20 + y + 1, Math.abs((new Random()).nextInt()) % 20 + x
					+ 1);

			if (!presenceSys[pos.getY() - 1][pos.getX() - 1]) {
				Univers.setSysteme(Systeme.creerAuHasard(pos));
				commandantNeutre.ajouterPossession(pos,
						Possession.creerAuHasard());
				presenceSys[pos.getY() - 1][pos.getX() - 1] = true;

				if (Univers.getTest(50)) {
					Flotte f = Flotte.creerAuHasard(pos, "Flotte neutre",
							Utile.getRaceDeDepart(pos), Univers.getInt(500));
					f.setDirective(Const.DIRECTIVE_FLOTTE_ATTAQUE_PREVENTIVE);
					commandantNeutre.ajouterFlotte(f);
				}
			} else
				i--;
		}
	}

	public static void main(String ag[]) {
		int numi = Integer.parseInt("0");

		Univers univers = new Univers(true, Const.MESSAGE_U_00002);
		Commandant commandantNeutre = Univers.getCommandant(0);

		peupleSecteur(100, 0, commandantNeutre);
		peupleSecteur(100, 20, commandantNeutre);
		peupleSecteur(100, 40, commandantNeutre);
		peupleSecteur(100, 60, commandantNeutre);
		peupleSecteur(100, 80, commandantNeutre);

		peupleSecteur(0, 100, commandantNeutre);
		peupleSecteur(20, 100, commandantNeutre);
		peupleSecteur(40, 100, commandantNeutre);
		peupleSecteur(60, 100, commandantNeutre);
		peupleSecteur(80, 100, commandantNeutre);

		peupleSecteur(100, 100, commandantNeutre);

		Position pos[] = univers.listePositionsSystemes();
		for (int i = 0; i < pos.length; i++)
			System.out.println(univers.getSysteme(pos[i]).getNom() + ": x-"
					+ univers.getSysteme(pos[i]).getPosition().getX() + ";y-"
					+ univers.getSysteme(pos[i]).getPosition().getY() + "; p-"
					+ univers.getSysteme(pos[i]).getNombrePlanetes() + "; r:"
					+ Utile.getRaceDeDepart(pos[i]) + "; s:"
					+ Univers.getTheSecteur(pos[i]));

		Univers.setCommandant(commandantNeutre);
		univers.sauvegarder();
	}

}
