package zIgzAg.jeu.oceane;

import zIgzAg.html.BaliseHTML;

public class RapportCombat {

	public static final int TYPE_COMBAT_SPATIAL = 0;

	public static final int TYPE_COMBAT_PLANETAIRE = 1;

	private Position position;

	private Commandant attaquant;

	private Commandant defenseur;

	private int typeCombat;

	private String nomFlotteAttaquante;

	// optionnel suivant type de combat --->

	private String nomFlotteDefensive;

	private String nomSystemeAttaque;

	private String nomsPlanetesPrises;

	public RapportCombat(Position p, Commandant att, Commandant def, int type) {
		this.position = p;
		this.attaquant = att;
		this.defenseur = def;
		this.typeCombat = type;
	}

	public void setCombatSpatial(Flotte flotteAtt, Flotte flotteDef) {
		nomFlotteAttaquante = flotteAtt.getNom();
		nomFlotteDefensive = flotteDef.getNom();
	}

	public void setCombatPlanetaire(Flotte flotteAtt, Systeme s,
			int[] numPlanetes) {
		nomFlotteAttaquante = flotteAtt.getNom();
		nomSystemeAttaque = s.getNom();
		if ((numPlanetes == null) || (numPlanetes.length == 0))
			nomsPlanetesPrises = " (aucune plan&egrave;te prise)";
		else
			nomsPlanetesPrises = " (";
		if (numPlanetes != null)
			for (int i = 0; i < numPlanetes.length; i++) {
				if (i != 0)
					nomsPlanetesPrises = nomsPlanetesPrises + ", ";
				nomsPlanetesPrises = nomsPlanetesPrises
						+ s.getPlanete(numPlanetes[i]).getNom();
			}
		if ((numPlanetes != null) && (numPlanetes.length != 0))
			nomsPlanetesPrises = nomsPlanetesPrises + ")";
	}

	public Position getPosition() {
		return position;
	}

	public BaliseHTML[] getDescription() {
		int raceSecteur = Utile.getRaceDeDepart(position);

		BaliseHTML[] retour = new BaliseHTML[5];
		retour[0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getFont(Rapport.COULEURS_RACES[attaquant.getRace()],
						null).ajout(Rapport.getText(attaquant.getNomNumero())));
		retour[1] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getFont(Rapport.COULEURS_RACES[defenseur.getRace()],
						null).ajout(Rapport.getText(defenseur.getNomNumero())));
		if (typeCombat == TYPE_COMBAT_SPATIAL)
			retour[2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					Rapport.getText("spatial"));
		else
			retour[2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					Rapport.getText("planÃ©taire"));

		retour[3] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getText(nomFlotteAttaquante));
		if (typeCombat == TYPE_COMBAT_SPATIAL)
			retour[4] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					Rapport.getText("<I>" + nomFlotteDefensive + "</I>"));
		else
			retour[4] = Rapport
					.getTD(BaliseHTML.CENTER, null)
					.ajout(Rapport.getFont(Rapport.COULEURS_RACES[raceSecteur],
							null).ajout(Rapport.getText(nomSystemeAttaque)))
					.ajout(Rapport.getFont(Rapport.cC[7], null).ajout(
							Rapport.getText(nomsPlanetesPrises)));
		return retour;
	}

}