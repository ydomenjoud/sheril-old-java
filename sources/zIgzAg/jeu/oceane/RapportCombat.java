package zIgzAg.jeu.oceane;

import zIgzAg.html.BaliseHTML;

public class RapportCombat {

	public static final int TYPE_COMBAT_SPATIAL = 0;

	public static final int TYPE_COMBAT_PLANETAIRE = 1;

	/**
	 * Échelle d'intensité des combats : {dégâts min, dégâts max, intitulé}.
	 * Les dégâts sont le total des points de dommages infligés par les deux
	 * camps (vaisseaux, bâtiments planétaires et milices). Bornes incluses,
	 * Integer.MAX_VALUE pour la dernière tranche.
	 */
	private static final Object[][] ECHELLE_INTENSITE = {
			{ 0, 4, "Escarmouche" },
			{ 5, 14, "Accrochage" },
			{ 15, 29, "Échauffourée" },
			{ 30, 59, "Engagement" },
			{ 60, 99, "Affrontement" },
			{ 100, 174, "Bataille" },
			{ 175, 299, "Bataille rangée" },
			{ 300, 499, "Choc frontal" },
			{ 500, 799, "Déluge de feu" },
			{ 800, 1199, "Carnage" },
			{ 1200, 1999, "Hécatombe" },
			{ 2000, 3499, "Massacre" },
			{ 3500, 5999, "Cataclysme" },
			{ 6000, 9999, "Apocalypse" },
			{ 10000, Integer.MAX_VALUE, "Armageddon" } };

	private Position position;

	private Commandant attaquant;

	private Commandant defenseur;

	private int typeCombat;

	private String nomFlotteAttaquante;

	// optionnel suivant type de combat --->

	private String nomFlotteDefensive;

	private String nomSystemeAttaque;

	private String nomsPlanetesPrises;

	private int degats;

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

    public boolean estSpatial() {
        return typeCombat == TYPE_COMBAT_SPATIAL;
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

	public void setDegats(int degats) {
		this.degats = Math.max(0, degats);
	}

	public int getDegats() {
		return degats;
	}

	private Object[] getTrancheIntensite() {
		for (Object[] tranche : ECHELLE_INTENSITE)
			if (degats >= (Integer) tranche[0] && degats <= (Integer) tranche[1])
				return tranche;
		return ECHELLE_INTENSITE[ECHELLE_INTENSITE.length - 1];
	}

	public String getIntensite() {
		return (String) getTrancheIntensite()[2];
	}

	private String getLibelleTrancheIntensite() {
		Object[] tranche = getTrancheIntensite();
		int max = (Integer) tranche[1];
		if (max == Integer.MAX_VALUE)
			return tranche[0] + " dégâts et plus";
		return tranche[0] + " à " + max + " dégâts";
	}

	public Position getPosition() {
		return position;
	}
    public Commandant getAttaquant() {
        return attaquant;
    }
    public Commandant getDefenseur() {
        return defenseur;
    }

	public BaliseHTML[] getDescription() {
		int raceSecteur = Utile.getRaceDeDepart(position);

		BaliseHTML[] retour = new BaliseHTML[6];
		retour[0] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getFont(Rapport.COULEURS_RACES[attaquant.getRace()],
						null).ajout(Rapport.getText(attaquant.getNomNumeroHtml())));
		retour[1] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
				Rapport.getFont(Rapport.COULEURS_RACES[defenseur.getRace()],
						null).ajout(Rapport.getText(defenseur.getNomNumeroHtml())));
		if (typeCombat == TYPE_COMBAT_SPATIAL)
			retour[2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					Rapport.getText("spatial"));
		else
			retour[2] = Rapport.getTD(BaliseHTML.CENTER, null).ajout(
					Rapport.getText("planétaire"));

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
		retour[5] = Rapport.getTD(BaliseHTML.CENTER, null)
				.ajout(BaliseHTML.TITLE, getLibelleTrancheIntensite())
				.ajout(Rapport.getText(getIntensite()));
		return retour;
	}

}