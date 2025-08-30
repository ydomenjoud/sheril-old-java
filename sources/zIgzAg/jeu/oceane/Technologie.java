// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import zIgzAg.utile.Mdt;

import java.util.Arrays;
import java.util.Locale;
import java.util.ArrayList;
import java.io.Serializable;

public class Technologie implements Serializable {

	static final long serialVersionUID = 1220715082795147854L;

	private String codeDeBase;
	private int niveau;
	private transient String[] parents;
	private transient int pointsDeRecherche;
	private int[][] caracteristiquesSpeciales;

	// les mÃ©thodes d'accÃšs

	public String getCode() {
		return (codeDeBase + getRepresentationNiveau());
	}

	public String getCodeBase(){
		return codeDeBase;
	}
	
	public String getCorpsCode() {
		return codeDeBase;
	}

	public int getNiveau() {
		return niveau;
	}

	public String getRepresentationNiveau() {
		return Utile.ROMAINS[niveau];
	}

	public String getNom(Locale loc) {
		return Univers.getNomTechno(getCorpsCode(), loc);
	}

	public String getNomPluriel(Locale loc) {
		return Univers.getNomPlurielTechno(getCorpsCode(), loc);
	}

	public String getDescription(Locale loc) {
		return Univers.getDescriptionTechno(getCorpsCode(), loc);
	}

	public String getNomComplet(Locale loc) {
		if ((niveau == 0)
				&& (!Univers
						.existenceTechnologie(codeDeBase + Utile.ROMAINS[1])))
			return getNom(loc);
		else
			return getNom(loc) + Univers.getMessage("DE_TYPE", loc)
					+ getRepresentationNiveau();
	}

	public String getNomPlurielComplet(Locale loc) {
		if ((niveau == 0)
				&& (!Univers
						.existenceTechnologie(codeDeBase + Utile.ROMAINS[1])))
			return getNomPluriel(loc);
		else
			return getNomPluriel(loc) + Univers.getMessage("DE_TYPE", loc)
					+ getRepresentationNiveau();
	}

	public String getParent(int num) {
		if (num < getNombreParents())
			return parents[num];
		else
			return null;
	}

	public int getNombreParents() {
		if (parents == null)
			return 0;
		else
			return parents.length;
	}

	public String[] getParents() {
		return parents;
	}

	public int getPointsDeRecherche() {
		return pointsDeRecherche * Const.MODIFICATEUR_DIFFICULTE_RECHERCHE;
	}

	public String getDescriptionParents(Locale l) {
		if (parents == null)
			return Utile.maj(Univers.getMessage("TECHNOLOGIE_AUCUN_PARENT", l));
		String retour = "";
		for (int i = 0; i < parents.length; i++) {
			retour = retour
					+ Utile.maj(Univers.getTechnologie(parents[i])
							.getNomComplet(l));
			if (i != parents.length - 1)
				retour = retour + "<BR>";
		}
		return retour;
	}

	public int[][] getCaracteristiquesSpeciales() {
		return caracteristiquesSpeciales;
	}

	public boolean possedeCaracteristiqueSpeciale(int c) {
		if (getValeurCaracteristiqueSpeciale(c) != 0)
			return true;
		else
			return false;
	}


	public boolean possedeCaracteristiqueSpecialeParCaractEtValeur(int caract, int val) {
		if (caracteristiquesSpeciales == null ){
			return false;
		}

		for (int i = 0; i < caracteristiquesSpeciales.length; i++) {
			if (caracteristiquesSpeciales[i][0] == caract && caracteristiquesSpeciales[i][1] == val){
				return true;
			}
		}
		
		return false;
	}

	public int getValeurCaracteristiqueSpeciale(int c) {
		if (caracteristiquesSpeciales == null)
			return 0;
		return Mdt.valeurCorrespondante(caracteristiquesSpeciales, c);
	}

	public String getDescriptionCaracteristiquesSpeciales(Locale l) {
		// System.out.println(getCode());
		String retour = "";
		if (caracteristiquesSpeciales == null)
			return null;
		for (int i = 0; i < caracteristiquesSpeciales.length; i++) {
			if (estBatiment()) {
				retour = retour
						+ Utile.maj(Univers.getMessage(
								"CARAC_SPECIALES_BATIMENTS",
								caracteristiquesSpeciales[i][0], l));
			
				if ((caracteristiquesSpeciales[i][0] == Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE)) {
					retour = retour
							+ " : "
							+ ((getNiveau() + 1) * 5)
							+ " <i>"
							+ Messages.MARCHANDISES[caracteristiquesSpeciales[i][1] - 1]
							+ "</i> par tour";
				} else {
					if( 
							caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE
							&&  caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI
							&&  caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE
							&&  caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_PRODUCTION_VAISSEAU
							&&  caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_RECYCLAGE_MINERAI
							&&  caracteristiquesSpeciales[i][0] != Const.BATIMENT_CAPACITE_BOUCLIER
							){
						retour = retour + " : "
								+ Integer.toString(caracteristiquesSpeciales[i][1]);
					}
				}
			}
			if (estComposantDeVaisseau()) {
				retour = retour
						+ Utile.maj(Univers.getMessage(
								"CARAC_SPECIALES_COMPOSANTS",
								caracteristiquesSpeciales[i][0], l));
				if ((caracteristiquesSpeciales[i][0] != Const.COMPOSANT_CAPACITE_PROPULSION_INTRAGALACTIQUE)
						&& (caracteristiquesSpeciales[i][0] != Const.COMPOSANT_CAPACITE_PROPULSION_INTERGALACTIQUE)
						&& (caracteristiquesSpeciales[i][0] != Const.COMPOSANT_CAPACITE_COLONISATEUR)) {
					retour = retour + " : "
							+ Integer.toString(caracteristiquesSpeciales[i][1]);
				}
			}
			if (i != caracteristiquesSpeciales.length - 1)
				retour = retour + "<br />";
		}
		return retour;
	}

	public boolean estBatiment() {
		return this instanceof Batiment;
	}

	public boolean estComposantDeVaisseau() {
		return this instanceof ComposantDeVaisseau;
	}

	public boolean estTechnologieSimple() {
		return (!estBatiment()) && (!estComposantDeVaisseau());
	}

	public int getTypeDeTechno() {
		if (estBatiment())
			return Const.TECHNOLOGIE_TYPE_BATIMENT;
		if (estComposantDeVaisseau())
			return Const.TECHNOLOGIE_TYPE_COMPOSANT_DE_VAISSEAU;
		return Const.TECHNOLOGIE_TYPE_SIMPLE;
	}

	// les mÃ©thodes statiques

	public static Technologie[] transformationCode(String[] entree) {
		Technologie[] retour = new Technologie[entree.length];
		for (int i = 0; i < entree.length; i++) {
			// System.out.println(" - " + entree[i] + " - ");
			retour[i] = Univers.getTechnologie(entree[i]);
		}
		return retour;
	}

	public static String[] listeDesTechnologiesAtteignables(
			ArrayList<String> technologiesConnues) {
		ArrayList<String> liste = new ArrayList<>();
		Technologie[] t = Univers.getListeTechnologies();
		boolean possible;
		for (int i = 0; i < t.length; i++) {
            if (!technologiesConnues.contains(t[i].getCode()))
                if (t[i].parents == null)
                    liste.add(t[i].getCode());
                else {
                    possible = true;
                    for (int j = 0; j < t[i].parents.length; j++)
                        if (!technologiesConnues.contains(
                                t[i].parents[j])) {
                            possible = false;
                            break;
                        }
                    if (possible)
                        liste.add(t[i].getCode());
                }
        }
		return liste.toArray(new String[0]);
	}

	public static void testDevenirTechnologiesPubliques() {

		Commandant[] c = Univers.getListeCommandantsHumains();

        // ca fou la merde ça , plus jamais
//		if (Univers.getTour() == 0) {
//			Univers.ajouterEvenement("PUBLIC_TECHNOLOGIE_0000", "mineI");
//			Univers.ajouterTechnologieAuDomainePublic("mineI");
//			Univers.ajouterEvenement("PUBLIC_TECHNOLOGIE_0000", "chantierI");
//			Univers.ajouterTechnologieAuDomainePublic("chantierI");
//		}

		Technologie[] t = Univers.getListeTechnologies();
		String[] t_reserved = Const.TECHNOLOGIES_RESERVED;

		for (int i = 0; i < t.length; i++)
			if (!Univers.estTechnologiePublique(t[i].getCode())) {
				int nb = 0;
				for (int j = 0; j < c.length; j++)
					if (c[j].estTechnologieConnue(t[i].getCode())
							&& Arrays.binarySearch(t_reserved,
									t[i].getCorpsCode()) == -1)
						nb++;
				if (nb > (c.length * 60) / 100) {
					Univers.ajouterEvenement("PUBLIC_TECHNOLOGIE_0000", t[i]);
					Univers.ajouterTechnologieAuDomainePublic(t[i].getCode());
					for (int j = 0; j < c.length; j++) {
						if (c[j].estTechnologieConnue(t[i].getCode())) {
							c[j].eliminerTechnologieConnue(t[i].getCode());
						}
						if (c[j].existenceDomaineDeRecherche(t[i].getCode())) {
							c[j].suppressionDomaineDeRecherche(t[i].getCode());
							c[j].ajouterEvenement(
									"EV_COMMANDANT_RECHERCHE_0001", t[i]);
						}
					}
				}
			}
	}

	// Le constructeur

	protected Technologie() {
	}

	public Technologie(String code, int niv, String[] parent, int recherche,
			int[][] caracS) {
		codeDeBase = code;
		niveau = niv;
		parents = parent;
		pointsDeRecherche = recherche;
		caracteristiquesSpeciales = caracS;
	}

	// les autres mÃ©thodes

	public static int possedeMaitriseMilitaire(Commandant c) {
		int militaire = Const.EFFETS_MAITRISE_MILITAIRE[0];
		if (c.estTechnologieConnue("maitmilI"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[1];
		if (c.estTechnologieConnue("maitmilII"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[2];
		if (c.estTechnologieConnue("maitmilIII"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[3];
		if (c.estTechnologieConnue("maitmilIV"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[4];
		if (c.estTechnologieConnue("maitmilV"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[5];
		if (c.estTechnologieConnue("maitmilVI"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[6];
		if (c.estTechnologieConnue("maitmilVII"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[7];
		if (c.estTechnologieConnue("maitmilVIII"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[8];
		if (c.estTechnologieConnue("maitmilIX"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[9];
		if (c.estTechnologieConnue("maitmilX"))
			militaire = Const.EFFETS_MAITRISE_MILITAIRE[10];
		return militaire;
	}

}
