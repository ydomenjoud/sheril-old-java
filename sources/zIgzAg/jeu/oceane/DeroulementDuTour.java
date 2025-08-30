// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import zIgzAg.utile.Copie;

public class DeroulementDuTour {

	public static void main(String[] args) {
		
		Univers univers = new Univers(true, "Déroulement du tour...");
		System.out.println(" Debut du tour " + Univers.getTour());

		//Univers.corrigerPlanDeVaisseau();

		Commandant[] cL = Univers.getListeCommandants();
		Commandant[] cL2 = Univers.getListeCommandantsHumains();

		for (int i = 0; i < cL.length; i++) {
			cL[i].initialiserChampsTransients();

			if (Commandant.testNeutre(cL[i])) {
				gestionNeutre(cL[i]);
			}
		}

		// On va remettre les flottes au bon endroit
		for (int i = 0; i < cL2.length; i++) {
			Flotte[] lf = cL2[i].listeFlottes();
			for(Flotte flotte:lf){
				if( !flotte.getPosition().estValide() ){
					flotte.setPosition(cL2[i].getCapitale());
					flotte.setDirection(cL2[i].getCapitale());
				}
			}
		}

		for (int i = 0; i < cL2.length; i++)
			cL2[i].setBudget(Const.BUDGET_COMMANDANT_TOUR_PRECEDENT,
					cL2[i].getCentaures());

		System.out.println("Gestion des collisions...");
		for (int i = 0; i < cL.length; i++)
			cL[i].resolutionCollisions();
		Univers.phaseSuivante();

		Univers.notify("Reception des ordres et gestion ...");
		ReceptionOrdres ro = new ReceptionOrdres();
		Map m = ro.deroulementOrdres();
		ro = null;

		Integer[] listeOrdresRendus = (Integer[]) m.keySet().toArray(
				new Integer[0]);
		System.out.println("");

		Commandant.resolutionEvenements();

		//System.out.println("Debut des combats...");
		Univers.notify("Debut des combats...");
		Combat.resolutionCombats();
		System.out.println("Gestion fin du tour...");
		//System.out.println("Progression gouverneur");

		for (int i = 0; i < cL2.length; i++) {
			augmenterGouverneurParMab(cL2[i]);
		}

		Alliance.gererAlliances1();
		System.out.println("alliance");
		gestionFinDeTour(cL2);
		System.out.println("gestion fin de tour");

		Univers.gestionTechno();
		System.out.println("gestion  techno");

		Technologie.testDevenirTechnologiesPubliques();
		Univers.augmenterNumeroTour();
		System.out.println("Mise à jour de la base des commandants...");
		ProductionOrdres.produireRegistre(Utile.transformer(listeOrdresRendus));
		Leader.produireEncheres();
		Alliance.gererAlliances2();

		cL = null;
		m = null;
		listeOrdresRendus = null;
		cL2 = Univers.getListeCommandantsHumains();

		Rapport.ecrireMessagesSystemes();
		Stats.afficher(Locale.FRENCH);
		ProductionOrdres.produireOrdresGeneraux();
		ProductionOrdres.productionDonneeRaces(Locale.FRENCH);

		Map tran = new HashMap();
		Map tran2 = new HashMap();

		System.out.print("Détermination flottes détectées : ");
		Univers.notify("Détermination flottes détectées : ");
		for (int i = cL2.length - 1; i >= 0; i--) {
			if (i % 19 == 0) System.out.println("");
			System.out.print(cL2[i].getNumero() + "-");

			// Detections
			cL2[i].determinerFlottesDetectes();
			cL2[i].determinerSystemesDetectes();

			tran.put(new Integer(cL2[i].getNumero()), cL2[i].stockerChampsTransients());
		}
		
		// gestion detection alliance
		Alliance[] a = Univers.getListeAlliances();
		for (int i = 0; i < a.length; i++) {
			a[i].determinerFlottesDetectees();
			a[i].determinerSystemesDetectes();
		}

		// gestion detection alliance par commandant
		for (int i = cL2.length - 1; i >= 0; i--) {
			cL2[i].determinerFlottesDetectesParAlliance();
			cL2[i].determinerSystemesDetectesParAlliance();
		}
		
		
		System.out.print("Rapport: ");
		for (int i = cL2.length - 1; i >= 0; i--) {

			if (i % 19 == 0)
				System.out.println("");

			System.out.print(cL2[i].getNumero() + "-");

			cL2[i].chargerChampsTransients((List) tran.get(new Integer(cL2[i].getNumero())));

			Rapport r = new Rapport(cL2[i]);
			r.creation();
			r.ecriture();
			r = null;
			RapportXML rxml = new RapportXML(cL2[i]);
			rxml.ecrireRapportXML();
			/**
			 * RapportTechnoXML rtxml = new RapportTechnoXML(cL2[i]);
			 * rtxml.ecrireRapportXML();
			 */
			ProductionOrdres prod = new ProductionOrdres(cL2[i]);
			prod.creation();
			cL2[i].initialiserChampsTransients();
			tran.remove(new Integer(cL2[i].getNumero()));
		}

		// zippage des rapports
		System.out.println("zippage");
		Univers.notify("zippage rapport");
		for (int i = 0; i < cL2.length; i++) {
			System.out.print(cL2[i].getNumero() + "-");
			EnvoyerRapport.zipper(cL2[i]);
		}

		// zippage des stats
		String[] g = new String[] { Chemin.STATS };
		Copie.zipper(g, Chemin.RACINE + "stats", "statsT" + Univers.getTour()+ ".zip");

		System.out.println("");
		System.out.println("Sauvegarde: ");
		Univers.notify("Sauvegarde: ");
		univers.sauvegarder();

	}

	public static Flotte createFlotte(Commandant c, String name, Position position, int directive, int vaisseaux){
	
		Flotte f = new Flotte(name, position);
		f.setDirective(directive);

		for(int ct=0; ct<vaisseaux; ct++){
			f.ajouterVaisseau(Vaisseau.creer("Executeur", 6));
		}
		c.ajouterFlotte(f);
		
		return f;
	}

	public static Flotte createFlotte(Commandant c, String name, Position position, int directive){
		return createFlotte(c, name, position, directive, 1000);
	}
	
	public static void gestionFinDeTour(Commandant[] c) {
		for (int i = 0; i < c.length; i++) {

			
			c[i].resolutionGestionSystemes();
			Univers.phaseSuivante();

			c[i].resolutionGestionFlottes();
			Univers.phaseSuivante();

			c[i].progressionNiveauLieutenant();
			Univers.phaseSuivante();

			// if(c[i].estJoueurHumain())
			// c[i].resolutionEvenements();Univers.phaseSuivante();
			c[i].resolutionProgressionRecherche();
			Univers.phaseSuivante();
			c[i].finaliserBudget();
			Univers.phaseSuivante();
			c[i].ajouterReputation(Univers.getInt(10));

			Position[] p = c[i].listePossession();
			for (int a = 0; a < c[i].getNombrePossessions(); a++) {
				Systeme s = Univers.getSysteme(p[a]);
				Possession fief = c[i].getPossession(p[a]);
				if (fief.getPolitique() == Const.POLITIQUE_LOISIR)
					c[i].ajouterReputation(s.getNombrePlanetesPossedees(c[i].getNumero()) * 2);
				else if (fief.getPolitique() == Const.POLITIQUE_ESCLAVAGISTE)
					c[i].ajouterReputation(-(s.getNombrePlanetesPossedees(c[i].getNumero()) * 2));
				else if (fief.getPolitique() == Const.POLITIQUE_INTEGRISME)
					c[i].ajouterReputation(-(s.getNombrePlanetesPossedees(c[i].getNumero()) * 1));
				else if (fief.getPolitique() == Const.POLITIQUE_TOTALITAIRE)
					c[i].ajouterReputation(-(s.getNombrePlanetesPossedees(c[i].getNumero()) * 1));
			}

		}

	}

	public static void gestionNeutre(Commandant c) {
		c.initialiserTechnologiesConnues();
		/***********************************************************************
		 * boolean[][] tabGal = new boolean[Const.BORNE_MAX+2][]; for(int
		 * h=0;h<Const.BORNE_MAX+2; h++){ boolean[] temp = new
		 * boolean[Const.BORNE_MAX+2]; java.util.Arrays.fill(temp, false);
		 * tabGal[h] = temp; }
		 * 
		 * Flotte[] f=c.listeFlottes(); for(int j=0;j<f.length;j++){ int x =
		 * f[j].getPosition().getX(); int y = f[j].getPosition().getY();
		 * 
		 * try{ if(!tabGal[x][y]) tabGal[x][y] = true; else
		 * c.eliminerFlotte(c.numeroFlotte(f[j])); } catch(Exception e){
		 * System.out.println("Alors le pb vient de :
		 * "+f[j].getPosition().toString()+" numero : "+j); System.exit(0); }
		 * /** Position[] pl = c[i].listePossession(); for(int
		 * b=0;b<pl.length;b++){ Position plop = pl[b];
		 * if(Univers.getSysteme(plop).getProprios().length == 1 &&
		 * Univers.getInt(100)>40){ // System.out.println("Letz fuckb "+plop);
		 * c[i].ajouterFlotte(Flotte.creerAuHasard(plop,"flotte neutre",
		 * Univers.getInt(5), 200)); } } }
		 **********************************************************************/
		/**
		 * Flotte[] f=c.listeFlottes(); for(int j=0;j<f.length;j++){
		 * c.eliminerFlotte(c.numeroFlotte(f[j])); }
		 * 
		 * 
		 * Flotte[] f=c.listeFlottes(); for(int j=0;j<f.length;j++){
		 * f[j].setDirective(2); }
		 */

	}

	public static void ecrirestats() {
		Stats.ecrireHeros(Commandant.listeHerosTotale(), Locale.FRENCH);
	}

	public static void augmenterGouverneurParMab(Commandant c) {

		Position[] p = c.listePossession();

		for (int i = 0; i < c.getNombrePossessions(); i++) {
			Systeme s = Univers.getSysteme(p[i]);
			Possession fief = c.getPossession(p[i]);
			if (c.existenceGouverneurSurPossession(s.getPosition())) {
				int compteurXP = 0;
				Gouverneur gougou = c.getGouverneurSurPossession(s
						.getPosition());
				if (s.getPopulationMaximale(c.getNumero()) == s.getPopulation(c
						.getNumero())) {
					compteurXP = 0;
				}

				else {
					compteurXP = Math.min(
							s.getPopulation(c.getNumero())
									* (s.getAugmentationMoyennePopulation(c
											.getNumero())) / 2,
							Leader.REPUT_GOU[gougou.getNiveau() - 1]
									/ (2 * gougou.getNiveau()));
				}

				for (int a = 0; a < (compteurXP); a++) {
					gougou.augmenterExperience();
				}

			}
		}

	}

}
