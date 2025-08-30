// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import zIgzAg.utile.Mdt;

public class Systeme implements Serializable {

	static final long serialVersionUID = 3047196692470242253L;

	private Position position;

	private int typeEtoile;

	private Planete[] pla;

	private String nom;

	// Les mÃ©thodes d'accÃšs

	public Position getPosition() {
		return position;
	}

	public int getTypeEtoile() {
		return typeEtoile;
	}

	public Planete[] getPlanetes() {
		return pla;
	}

	public Planete getPlanete(int index) {
		return pla[index];
	}

	public int getNombrePlanetes() {
		return pla.length;
	}

	public String getNomNumeroPlanete(int index) {
		return pla[index].getNom() + " (" + Integer.toString(index + 1) + ")";
	}

	public String getNom() {
		return nom;
	}

	public String getNomPosition(Locale loc) {
		return nom + " : " + position.getDescription(loc);
	}

	public String getNomPositionSimple(Locale loc) {
		return nom + " : " + position.getDescriptionSimple(loc);
	}

	public String getNomPosition() {
		return nom + "(" + position.getDescription() + ")";
	}

	public int getCapaciteEncombrement(int player) {
		int enc = 0;
		for (int i = 0; i < pla.length; i++){
			if( player == pla[i].getProprio() ){
				enc += pla[i].getCapaciteEncombrement();
			}
		}
		return enc;
	}

	
	public int getCapaciteEncombrement() {
		int enc = 0;
		for (int i = 0; i < pla.length; i++)
			enc += pla[i].getCapaciteEncombrement();
		return enc;
	}

	public int getEncombrement(int player) {
		int enc = 0;
		for (int i = 0; i < pla.length; i++){
			if( player == pla[i].getProprio() ){
				enc += pla[i].getEncombrement();
			}
		}
		return enc;
	}

	public int getEncombrement() {
		int enc = 0;
		for (int i = 0; i < pla.length; i++)
			enc += pla[i].getEncombrement();
		return enc;
	}

	public boolean estProprio(int numero) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				return true;
		return false;
	}

	public void setPosition(Position e) {
		position = e;
	}

	public void setTypeEtoile(int e) {
		typeEtoile = e;
	}

	public void setPlanete(Planete e, int num) {
		pla[num] = e;
	}

	public void setPlanetes(Planete[] e) {
		pla = e;
	}

	public void setNom(String e) {
		nom = e;
	}

	public void initialiserPlanetes(int nbPlanetes) {
		pla = new Planete[nbPlanetes];
		for (int i = 0; i < nbPlanetes; i++)
			pla[i] = new Planete();
	}

	// le constructeur

	public Systeme() {
	}

	// Les mÃ©thodes statiques.

	public static Systeme creerAuHasard(Position pos, int nbpla) {
		Systeme systeme = new Systeme();

		systeme.setPosition(pos);
		systeme.setNom(Utile.getNom());
		systeme.setTypeEtoile(Univers.getInt(Const.NB_ETOILES));

		// Le nombre de planete du systeme
		int nbPlanetes = 0;
		// Si le nb de pla est -1, il faut gÃ©nÃ©rer un nombre alÃ©atoire
		// de pla
		if (nbpla == -1)
			nbPlanetes = 10
					+ Univers.getInt(10)
					+ ((Const.NB_ETOILES - 1 - systeme.getTypeEtoile()) * Univers
							.getInt(2));
		// Sinon c le nombre passÃ© en parametre
		else
			nbPlanetes = nbpla;

		if (nbPlanetes > 20)
			nbPlanetes = 20;
		systeme.initialiserPlanetes(nbPlanetes);

		int raceDeDepart = Utile.getRaceDeDepart(pos);

		for (int i = 0; i < nbPlanetes; i++)
			systeme.setPlanete(
					Planete.creerAuHasard(raceDeDepart, systeme.getNom(), i), i);

		return systeme;
	}

	public static Systeme creerAuHasard(Position pos) {
		return creerAuHasard(pos, -1);
	}

	public static Systeme[] getListeSystemes(Position[] p) {
		Systeme[] retour = new Systeme[p.length];
		for (int i = 0; i < p.length; i++)
			retour[i] = Univers.getSysteme(p[i]);
		return retour;
	}

	public static String[] getListeDescription(Systeme[] s, Locale l) {
		String[] retour = new String[s.length];
		for (int i = 0; i < s.length; i++)
			retour[i] = s[i].getNomPositionSimple(l);
		return retour;
	}

	public static int getPopulationTotale(Systeme[] s) {
		int retour = 0;
		for (int i = 0; i < s.length; i++)
			retour = retour + s[i].getPopulation(-1);
		return retour;
	}

	public static int getPopulationTotaleDeRace(Systeme[] s, int race) {
		int retour = 0;
		for (int i = 0; i < s.length; i++)
			retour = retour + s[i].getPopulationDeRace(-1, race);
		return retour;
	}

	public static int[][] getMarchandises(Systeme[] s) {
		int[][] r = new int[Const.NB_MARCHANDISES][2];
		for (int i = 0; i < s.length; i++) {
			int[] inter = s[i].getProprios();
			for (int j = 0; j < inter.length; j++) {
				Commandant c = Univers.getCommandant(inter[j]);
				Possession p = c.getPossession(s[i].getPosition());
				for (int k = 0; k < Const.NB_MARCHANDISES; k++) {
					r[k][0] = r[k][0] + p.getQuantiteMarchandise(k);
					r[k][1] = r[k][1] + p.getQuantiteMarchandise(k)
							* p.getPrixMarchandise(k);
				}
			}
		}
		for (int k = 0; k < Const.NB_MARCHANDISES; k++)
			if (r[k][0] != 0)
				r[k][1] = r[k][1] / r[k][0];
		return r;
	}

	public static int niveauRelation(int r) {
		if (r < -20000)
			return -5;
		if (r < -10000)
			return -4;
		if (r < -4000)
			return -3;
		if (r < -2000)
			return -2;
		if (r < -1000)
			return -1;
		if (r < 1000)
			return 0;
		if (r < 2000)
			return 1;
		if (r < 4000)
			return 2;
		if (r < 10000)
			return 3;
		if (r < 20000)
			return 4;
		return 5;
	}

	// mÃ©thodes pour gÃ©rer les systÃšmes en fin de tour.

	public void testerRevoltes(int numero) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				pla[i].testerRevolte();
	}

	public void evolutionStabilite(int numero, Position capitale, Gouverneur g, Possession pos) {
		/**
		int modCapitale = 0;
		if (capitale == null)
			modCapitale = Const.MODIFICATEUR_STABILITE_CAPITALE[Const.MODIFICATEUR_STABILITE_CAPITALE.length - 1][1];
		else {
			int distance = Position.distance(capitale, position);
			if (distance == 0)
				modCapitale = Const.MODIFICATEUR_STABILITE_CAPITALE[0][1];
			else
				for (int i = Const.MODIFICATEUR_STABILITE_CAPITALE.length - 2; i >= 0; i--)
					if (Const.MODIFICATEUR_STABILITE_CAPITALE[i][0] < distance) {
						modCapitale = Const.MODIFICATEUR_STABILITE_CAPITALE[i + 1][1];
						break;
					}
		}

		int modGouverneur = 0;
		if (g != null)
			modGouverneur = g.getMoralModifie();

		int modPolitique = 0;
		int modRace = 0;
		Commandant c = Univers.getCommandant(numero);
		if (c.getRace() == 0)
			modRace = 1;
		int reput = c.getStatutReputationIndex();
		int modPoste = 0;

		if (pos.getPolitique() == Const.POLITIQUE_TOTALITAIRE)
			modPolitique = +1;
		if (pos.getPolitique() == Const.POLITIQUE_INTEGRISME)
			modPolitique = -2;
		if (pos.getPolitique() == Const.POLITIQUE_ESCLAVAGISTE)
			modPolitique = -2;
		if (pos.getPolitique() == Const.POLITIQUE_LOISIR)
			modPolitique = +2;

		if (pos.aPolitiqueAnti())
			modPolitique = -5;

		if (pos.possedeStockImportantPoste(Const.PRODUIT_ALCOOLS))
			modPoste = modPoste - 1;
		if (pos.possedeStockImportantPoste(Const.PRODUIT_ARMEMENT))
			modPoste = modPoste - 1;
		if (pos.possedeStockImportantPoste(Const.PRODUIT_HOLOFILM))
			modPoste = modPoste + 1;

		 **/
		
		int[] evolutionStab = pos.getEvolutionStabilite(Univers.getCommandant(numero), this);
		// mod_gou, mod_pol, mod_post, mod_pos, mod_tax, mod_race 
		
		int evoStab = 0;
		for(int a=0; a<evolutionStab.length; a++){ 
			if( a != 4 ){ // On supprime la taxation qui rentre plus tard dans le calcul
				evoStab+=evolutionStab[a]; 
			}
		}
		
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				pla[i].evolutionStabilite(evoStab, position);

	}

	public void politiqueExtermination(Commandant c, int race) {
		float retour = 0F;
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(c.getNumero()))
				retour = retour + (float) pla[i].politiqueExtermination(race);
		c.ajouterEvenement("SYSTEME_EXTERMINATION_0000", position, retour);
		c.modifierBudget(Const.BUDGET_COMMANDANT_POLITIQUE_EXTERMINATION,
				retour);
	}

	public int evolutionPopulation(int numero, Possession pos) {
		int excedent = 0;
		int modPolitique = 0;
		Commandant c = Univers.getCommandant(numero);
		int reput = c.getStatutReputationIndex();

		if (pos.getPolitique() == Const.POLITIQUE_EXPANSION)
			modPolitique = 5;
		if (pos.getPolitique() == Const.POLITIQUE_INTEGRISME)
			modPolitique = 10;

		boolean esclavagisme = pos.getPolitique() == Const.POLITIQUE_ESCLAVAGISTE;

		int modPoste = 0;
		if (pos.possedeStockImportantPoste(Const.PRODUIT_NOURRITURE))
			modPoste = modPoste + 5;
		if (pos.possedeStockImportantPoste(Const.PRODUIT_MEDICAMENT))
			modPoste = modPoste + 10;

		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				excedent += pla[i].evolutionPopulation(modPolitique + modPoste,
						esclavagisme, true);
		return excedent;
	}

	public void evolutionMinerai(int numero) {

		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				pla[i].evolutionMinerai();

	}

	// autres mÃ©thodes.

	public void changementDeProprietaire(int ancien, int nouveau) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(ancien))
				pla[i].setProprio(nouveau);
	}

	public void modifierTaxation(int numero, int taxe) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				pla[i].setTaxation(taxe);
	}

	public float coutTerraformation(int numero) {
		float retour = 0F;
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				retour = retour + pla[i].coutTerraformation();
		return retour;
	}

	public void terraformer(int numero) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero) && !pla[i].estDejaTerraforme())
				pla[i].terraformer();
	}

	public void initialiserTerraformation(int numero) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				pla[i].setTerraformation(1);
	}

	public boolean estDejaTerraforme(int numero) {
		for (int i = 0; i < pla.length; i++){
			if (pla[i].estProprio(numero) && pla[i].estDejaTerraforme()){
					return true;
			}
		}
		return false;
	}

	public boolean contientBatiment(int numero, String code) {
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				if (pla[i].contientBatiment(code))
					return true;
		return false;
	}

	public Planete trouverPlaneteQuiContientLePlusDeBatimentDeType(int numero,
			String code) {
		int max = 0;
		int index = -1;
		int inter;

		// On regarde si la tech a besoin de pop
		Technologie t = Univers.getTechnologie(code);
		boolean needPop = !(t
				.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE));
		boolean mine = ((Batiment) t)
				.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI);

		for (int i = 0; i < pla.length; i++)
			if ((((needPop && pla[i].estHabite()) || !needPop) || !mine)
					&& (pla[i].estProprio(numero))
					&& ((inter = pla[i].nombreBatimentsDeType(code)) > max)) {
				max = inter;
				index = i;
			}
		if (index != -1)
			return pla[index];
		else
			return null;
	}

	public Planete trouverPlaneteQuiContientLeMoinsDeBatimentDeType(int numero, String code) {
		int min = Integer.MAX_VALUE;
		int index = -1;
		int inter;

		// On regarde si la tech a besoin de pop
		Technologie t = Univers.getTechnologie(code);
		boolean needPop = !(t.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE));
		int pointDeStructure = new ConstructionPlanetaire(code).getBatiment().getPointsDeStructure();

		for (int i = 0; i < pla.length; i++){
			boolean testProprio = numero>-1 ? pla[i].estProprio(numero) : true;
			if (((needPop && pla[i].estHabite()) || !needPop)
					&& (testProprio)
					&& ((inter = pla[i].nombreBatimentsDeType(code)) < min)
					&& (pla[i].getEspaceLibre() > pointDeStructure)) {
				min = inter;
				index = i;
			}
		}
		
		// Si on arrive là c'est que la contrainte "pas de population" a posé des soucis
		// Pareil qu'au dessus, mais on supprime la contrainte de population
		if( index == -1 ){
			for (int i = 0; i < pla.length; i++){
				
				boolean testProprio = numero>-1 ? pla[i].estProprio(numero) : true;
				if ((testProprio) && ((inter = pla[i].nombreBatimentsDeType(code)) < min) && (pla[i].getEspaceLibre() > pointDeStructure)) {
					min = inter;
					index = i;
				}
			}
		}
		
		if( index == -1 ){
			//System.out.println("error");
			//Univers.getCommandant(numero).ajouterErreur("ER_COMMANDANT_TRANSFERER_0009", this.getNomPosition());
			//Univers.notify("Erreur, la planète recherchée n'a pas été trouvée" +
			//		" ( système: "+getPosition().toString()+", batiment: "+code+", espace libre: "+getEspaceLibre(numero)+", numero: "+numero+")");
			//index = 0;
			
		}
		
		return index>-1 ? pla[index] : null;
	}

	public Planete trouverPlaneteQuiALaPlusGrandeSurcapaciteMines(int numero,
			String code) {
		int max = 0;
		int index = -1;
		int inter;

		for (int i = 0; i < pla.length; i++)
			if ((pla[i].estProprio(numero))
					&& (pla[i].contientBatiment(code) && ((inter = -pla[i]
							.getDifferenceCapaciteProductionMinerai()) > max))) {
				max = inter;
				index = i;
			}
		if (index == -1)
			return null;
		else
			return pla[index];
	}

	public Planete trouverPlaneteQuiALaPlusGrandeSouscapaciteMines(int numero, String code) {
		int max = 0;
		int index = -1;
		int inter;
		for (int i = 0; i < pla.length; i++)
			if ((pla[i].estProprio(numero))
					&& ((inter = pla[i]
							.getDifferenceCapaciteProductionMinerai()) > max)) {
				max = inter;
				index = i;
			}
		if (index == -1)
			return null;
		else
			return pla[index];
	}

	public Planete trouverPlaneteSurLaquelleEliminerBatimentDeType(int numero,
			Batiment b) {
		Planete retour = null;
		if (b.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI))
			retour = trouverPlaneteQuiALaPlusGrandeSurcapaciteMines(numero,
					b.getCode());
		if (retour == null)
			retour = trouverPlaneteQuiContientLePlusDeBatimentDeType(numero,
					b.getCode());
		return retour;
	}

	public Planete trouverPlaneteSurLaquelleAjouterBatimentDeType(int numero, Batiment b) {
		Planete retour = null;
		if (b.possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI)){
			retour = trouverPlaneteQuiALaPlusGrandeSouscapaciteMines(numero, b.getCode());
		}
		
		if (retour == null){
			retour = trouverPlaneteQuiContientLeMoinsDeBatimentDeType(numero, b.getCode());
		}
		return retour;
	}

	public int recyclerMateriel(int numero, Batiment b, int nombre) {
		int nbElimine = 0;
		Planete p = trouverPlaneteSurLaquelleEliminerBatimentDeType(numero, b);
		while ((nbElimine < nombre) && (p != null)) {
			p.recyclerMateriel(b, 1);
			nbElimine++;
			p = trouverPlaneteSurLaquelleEliminerBatimentDeType(numero, b);
		}
		return nbElimine;
	}

	public int trouverPlanetePropagandable(int numero) {
		int test = Univers.getInt(pla.length);
		int compteur = 0;
		int index = 0;
		int compteurBoucleInfinie = 0;
		while ((compteur != test) && (compteurBoucleInfinie < 1000)) {
			if ((!pla[index].estProprio(numero)) && (pla[index].estHabite()))
				compteur++;
			if (compteur != test)
				index++;
			if (index == pla.length)
				index = 0;
			compteurBoucleInfinie++;
		}
		if (compteurBoucleInfinie == 1000)
			return test;
		else
			return index;
	}

	public void detruireToutBatimentDePlanete(int planete) {
		getPlanete(planete).initialiserBatiments();
	}

	public float getRevenu(int numero, Gouverneur g, Possession p) {
		float retour = 0F;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getRevenu();

		if (g != null)
			retour = retour
					+ (retour * 20 * g
							.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_FINANCE))
					/ 100;
		Commandant c = Univers.getCommandant(numero);
		int reput = c.getStatutReputationIndex();
		if (p.getPolitique() == Const.POLITIQUE_IMPOT)
			retour = retour + retour / 10;
		if (p.getPolitique() == Const.POLITIQUE_LOISIR)
			retour = retour - retour / 5;

		if (p.possedeStockImportantPoste(Const.PRODUIT_LUXE))
			retour = retour + retour / 10;
		if (p.possedeStockImportantPoste(Const.PRODUIT_METAUX_PRECIEUX))
			retour = retour + retour / 20;

		Commandant com = Univers.getCommandant(numero);
		// if(com.getRace() == 5 && com.getCapitale()!=getPosition())
		// return 0;

		return retour;
	}

	public float getEntretien(int numero, Gouverneur g, Possession p) {
		float retour = 0F;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getEntretien();
		if (g != null)
			retour = retour
					- (retour * 20 * g
							.getNiveauCompetence(Const.COMPETENCE_LEADER_ENTRETIEN_SYSTEME))
					/ 100;
		if (p.possedeStockImportantPoste(Const.PRODUIT_PIECE_INDUSTRIELLE))
			retour = retour - retour / 10;
		return retour;
	}

	public int[] getProprios() {
		int[] retour = new int[1];
		retour[0] = pla[0].getProprio();
		for (int i = 1; i < pla.length; i++)
			if (!Mdt.estPresent(retour, pla[i].getProprio()))
				retour = Mdt.ajout(retour, pla[i].getProprio());
		return retour;
	}

	public int nbProprios() {
		return getProprios().length;
	}

	public int getNombrePlanetesPossedees(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if (pla[i].estProprio(numero))
				retour++;
		return retour;
	}

	public int getPuissance(int numero) {
		return getPopulation(numero) + (int) getValeurBatiments(numero);
	}

	public int getPopulation(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].populationTotale();
		return retour;
	}

	public int getRaceMajoritaire(int numero) {
		int max = 0;
		int index = 0;
		for (int i = 0; i < Const.NB_RACES; i++)
			if (getPopulationDeRace(numero, i) > max) {
				max = getPopulationDeRace(numero, i);
				index = i;
			}
		return index;
	}

	public int getPopulationMaximale(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].populationMaximaleTotale();
		return retour;
	}

	public int getPopulationMaximaleDeRace(int numero, int race) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				if (pla[i].racePresente(race))
					retour = retour + pla[i].calculeMaxPop(race);
		return retour;
	}

	public int getPopulationDeRace(int numero, int race) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)

			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getPopActuelle(race);
		return retour;
	}

	public int getAugmentationMoyennePopulationDeRace(int numero, int race) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getPopActuelle(race)
						* pla[i].calculeProgressionPop(race);
		int inter = getPopulationDeRace(numero, race);
		if (inter != 0)
			return retour / inter;
		else
			return 0;
	}

	public int getAugmentationMoyennePopulation(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].populationTotale()
						* pla[i].augmentationMoyenne();
		int inter = getPopulation(numero);
		if (inter != 0)
			return retour / inter;
		else
			return 0;
	}

	public int getNombreTypePopulation(int numero) {
		int compteur = 0;
		for (int j = 0; j < Const.NB_RACES; j++)
			if (populationPresente(numero, j))
				compteur++;
		return compteur;
	}

	public boolean populationPresente(int numero, int race) {
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				if (pla[i].racePresente(race))
					return true;
		return false;
	}

	public int nbPlanetesHabitees(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				if (pla[i].populationTotale() > 0)
					retour++;
		return retour;
	}

	public float getValeurBatiments(int numero) {
		float retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getValeurBatiments();
		return retour;
	}

	public int getRevenuMinerai(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].calculeRevenuMinerai();
		return retour;
	}

	public int getStockMinerai(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getStockMinerai();
		return retour;
	}

	public int getNombreDeMineUtile(int num) {
		Planete[] p = getPlanetes();
		int rtr = 0;
		for (int i = 0; i < p.length; i++)
			if (pla[i].estProprio(num))
				rtr += p[i].getNombreDeMineUtile();
		return rtr;
	}

	public int getNombreDeMineMax(int num) {
		Planete[] p = getPlanetes();
		int rtr = 0;
		for (int i = 0; i < p.length; i++)
			if (pla[i].estProprio(num))
				rtr += p[i].capaciteProductionMinerai();
		return rtr;
	}

	public int[] AppartiensAquelleZone(Position pos) {
		int[] zone = new int[Const.NOMBRE_DE_ZONES];
		int a = 0;
		for (int i = 0; i < Const.NOMBRE_DE_ZONES; i++) {
			if ((pos.getY() > Const.REPARTITION_DES_ZONES[i][0])
					&& (pos.getY() <= Const.REPARTITION_DES_ZONES[i][1])
					&& (pos.getX() > Const.REPARTITION_DES_ZONES[i][2])
					&& (pos.getX() <= Const.REPARTITION_DES_ZONES[i][3])) {
				zone[a] = i;
				a++;
			}
		}
		return zone;
	}

	public String getNomZone(int[] a) {
		String nomZone = "";
		for (int j = 0; j < a.length; j++) {
			int zone = 0;
			zone = a[j];
			nomZone = nomZone + Const.NOM_DES_ZONES[zone];
		}
		return nomZone;
	}

	public int getPointsDeConstruction(int numero, Gouverneur g, Possession p) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getPointsDeConstruction();

		if (g != null)
			retour = retour + g.getVitesseModifie();

		if (p.getPolitique() == Const.POLITIQUE_CONSTRUCTION)
			retour = retour + retour / 2;
		if (p.getPolitique() == Const.POLITIQUE_ESCLAVAGISTE)
			retour = retour * 2;

		if (p.possedeStockImportantPoste(Const.PRODUIT_ROBOT))
			retour = retour + 5;

		return retour;
	}

	public int getPointsDeConstructionModifie(int numero, Gouverneur g, Possession p, Position pos) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getPointsDeConstruction();

		//Commandant c = Univers.getCommandant(numero);
		//int reput = c.getStatutReputationIndex();

		if (p.getPolitique() == Const.POLITIQUE_CONSTRUCTION)
			retour = retour + retour / 2;
		if (p.getPolitique() == Const.POLITIQUE_ESCLAVAGISTE)
			retour = retour * 2;

		if (g != null)
			retour = retour + g.getVitesseModifie();

		if (p.possedeStockImportantPoste(Const.PRODUIT_ROBOT))
			retour = retour + 5;
			//retour = retour + 10 * p.getQuantiteMarchandise(Const.PRODUIT_ROBOT) / 100;

		return retour;
	}

	public int getTaxation(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getTaxation();
		if (getNombrePlanetesPossedees(numero) > 0)
			return retour / getNombrePlanetesPossedees(numero);
		else
			return 0;
	}

	public int getStabilite(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getStabilite();
		if (getNombrePlanetesPossedees(numero) > 0)
			return retour / getNombrePlanetesPossedees(numero);
		else
			return 0;
	}

	public int getProductionMarchandise(int numero, int mar) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getProductionMarchandise(mar);
		return retour;
	}

	public int getTerraformation(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getTerraformation();
		if (getNombrePlanetesPossedees(numero) > 0)
			return retour / getNombrePlanetesPossedees(numero);
		else
			return 0;
	}

	public int getNombrePlanetesRevoltees(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				if (pla[i].getRevolte())
					retour++;
		return retour;
	}

	public int getPorteeRadar(int numero) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = Math.max(retour, pla[i].getPorteeRadar());
		return retour;
	}

	public float getRevenuTechnologique(int numero, Gouverneur g, Possession p) {

		float retour = 0F;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].getRevenuTechnologique();

		if (g != null)
			retour = retour
					+ (retour * 20 * g
							.getNiveauCompetence(Const.COMPETENCE_LEADER_MAITRISE_FINANCE))
					/ 100;

		Commandant c = Univers.getCommandant(numero);
		int reput = c.getStatutReputationIndex();

		if (p.getPolitique() == Const.POLITIQUE_IMPOT)
			retour = retour + retour / 10;
		if (p.getPolitique() == Const.POLITIQUE_LOISIR)
			retour = retour - retour / 5;

		if (p.possedeStockImportantPoste(Const.PRODUIT_LUXE))
			retour = retour + retour / 10;
		if (p.possedeStockImportantPoste(Const.PRODUIT_METAUX_PRECIEUX))
			retour = retour + retour / 10;

		return retour;
	}

	public float getBudgetContreEspionnage(int numero) {
		float retour = 0F;
		int[] liste = getProprios();
		for (int i = 0; i < liste.length; i++)
			if (liste[i] != numero) {
				Commandant c = Univers.getCommandant(liste[i]);
				if (!c.estJoueurNeutre())
					retour = retour + c.getBudgetContreEspionnage();
				retour = retour
						+ (c.getPossession(position).getBudget(
								Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE) * getRevenu(
								liste[i],
								c.getGouverneurSurPossession(position),
								c.getPossession(position))) / 100;
			}
		return retour;
	}

	// Liste des noms des Ã©quipements des systÃšmes sous forme d'un
	// arraylist
	public ArrayList listeEquipementArray(int numero) {
		ArrayList rtr = new ArrayList(50);
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				rtr.addAll(Arrays.asList(pla[i].listeTypeDeBatimentsPresents()));
		return rtr;
	}

	@SuppressWarnings("unchecked")
	public Map.Entry<String, Integer>[] listeEquipement(int numero) {
		HashMap<String, Integer> retour = new HashMap<>();
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero))) {
				String[] l = pla[i].listeTypeDeBatimentsPresents();
				for (int j = 0; j < l.length; j++)
					if (retour.containsKey(l[j]))
						retour.put(
								l[j],
								retour.get(l[j])
										+ pla[i].nombreBatimentsDeType(l[j]));
					else
						retour.put(l[j],
								pla[i].nombreBatimentsDeType(l[j]));
			}
		return retour.entrySet().toArray(new Map.Entry[0]);
	}

	public void reparation(int numero, Gouverneur g) {
		int potentiel = Const.POINTS_REPARATION_BATIMENT;
		if (g != null)
			potentiel = potentiel
					* (1 + g.getNiveauCompetence(Const.COMPETENCE_LEADER_ENTRETIEN_SYSTEME));
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				pla[i].reparation(potentiel);
	}

	public int capaciteSpecialeBatiment(int numero, int capacite) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour + pla[i].capaciteSpecialeBatiment(capacite);
		return retour;
	}

	public int sommeCapacitesSpecialesBatiment(int numero, int capacite) {
		int retour = 0;
		for (int i = 0; i < pla.length; i++)
			if ((numero == -1) || (pla[i].estProprio(numero)))
				retour = retour
						+ pla[i].sommeCapacitesSpecialesBatiment(capacite);
		return retour;
	}

	public ObjetTransporte supprimerRichesses(int numero, String code, int nb,
			int numPlanete) {
		if (numPlanete != Integer.MIN_VALUE)
			return pla[numPlanete].supprimerRichesse(code, nb);
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_BATIMENT) {
			Batiment b = (Batiment) Univers.getTechnologie(code);
			ObjetComplexeTransporte o = new ObjetComplexeTransporte(code);
			Planete p = trouverPlaneteSurLaquelleEliminerBatimentDeType(numero,
					b);
			while ((o.getNombreObjets() < nb) && (p != null)) {
				o.ajout(p.eliminerBatiment(b, 1));
				p = trouverPlaneteSurLaquelleEliminerBatimentDeType(numero, b);
			}
			return o;
		}
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_MINERAI) {
			ObjetSimpleTransporte o = new ObjetSimpleTransporte(code, 0);
			int tempo = 0;
			while ((o.getNombreObjets() < nb) && (tempo < 10000)) {
				for (int i = 0; i < pla.length; i++)
					if ((o.getNombreObjets() < nb)
							&& (pla[i].estProprio(numero)))
						o.ajout(pla[i].eliminerMinerai(1));
				tempo++;
			}
			return o;
		}
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_POPULATION) {
			ObjetSimpleTransporte o = new ObjetSimpleTransporte(code, 0);
			int tempo = 0;
			while ((o.getNombreObjets() < nb) && (tempo < 10000)) {
				for (int i = 0; i < pla.length; i++)
					if ((o.getNombreObjets() < nb)
							&& (pla[i].estProprio(numero)))
						o.ajout(pla[i].eliminerPopulation(code, 1));
				tempo++;
			}
			return o;
		}
		return null;
	}

	public void ajouterRichesses(int numero, ObjetTransporte o, int numPlanete) {
		if (numPlanete != Integer.MIN_VALUE){
			pla[numPlanete].ajouterRichesse(o);
		} else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_BATIMENT) {
			Batiment b = (Batiment) Univers.getTechnologie(o.getCode());
			Planete p = trouverPlaneteSurLaquelleAjouterBatimentDeType(numero, b);
			
			if( p == null ){
				Univers.getCommandant(numero).ajouterErreur("ER_COMMANDANT_TRANSFERER_0009", this.getNomPosition(), o.getDescription());
			}
			
			int nbAjouter = 0;
			while ((nbAjouter < o.getNombreObjets()) && (p != null)) {
				p.ajouterBatiment((ConstructionPlanetaire) ((ObjetComplexeTransporte) o) .getObjet(nbAjouter));
				nbAjouter++;
				p = trouverPlaneteSurLaquelleAjouterBatimentDeType(numero, b);
			}
		} else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_MINERAI) {
			
			int nbAjouter = 0;
			int tempo = 0;
			while ((nbAjouter < o.getNombreObjets()) && (tempo < 10000)) {
				for (int i = 0; i < pla.length; i++){
					if ((nbAjouter < o.getNombreObjets()) && (pla[i].estProprio(numero))) {
						nbAjouter++;
						pla[i].ajouterMinerai(1);
					}
				}
				tempo++;
			}
		} else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_POPULATION) {
			int nbAjouter = 0;
			int tempo = 0;
			while ((nbAjouter < o.getNombreObjets()) && (tempo < 10000)) {
				for (int i = 0; i < pla.length; i++)
					if ((nbAjouter < o.getNombreObjets())
							&& (pla[i].estProprio(numero)))
						nbAjouter = nbAjouter
								+ pla[i].ajouterPopulation(o.getCode(), 1);
				tempo++;
			}
		}

	}

	// reparation de flotte

	public int getPotentielReparationFlotte(int numero) {
		int capaciteReparation = sommeCapacitesSpecialesBatiment(numero,
				Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU);
		int pointsDejaRepare = Univers.getPointsRepareDunSysteme(getPosition(),
				numero);
		int pts = capaciteReparation - pointsDejaRepare;
		return (pts > 0 ? pts : 0);

	}

	public void setPointsRepare(int numero, int nb) {
		Univers.setPointsRepareDunSysteme(getPosition(), numero, nb);
	}

	public static void UpgradeSyst(Systeme s, Commandant c) {
		Planete[] filles = s.getPlanetes();
		for (int i = 0; i < filles.length; i++) {

			if (filles[i].estProprio(c.getNumero())) {
				Planete.upgradeplanete(filles[i]);
			}
		}

	}

	public int getEspaceLibre(int numero){
		int espace = 0;
		
		espace = getCapaciteEncombrement(numero) - getEncombrement(numero);
		espace = Math.max(0, espace); // doit etre supérieur à 0
		
		return espace;
	}

}
