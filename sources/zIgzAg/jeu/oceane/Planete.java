// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Planete implements Serializable {

	static final long serialVersionUID = -5284664557190021848L;

	// Les caractÃ©ristiques de base.

	private int radiation;

	private int temperature;

	private int gravite;

	private int atmosphere;

	private int taille;

	private int type;

	private int productionMinerai;

	private int productionMarchandise;

	private int nombreProductionMarchandise;

	// Les caractÃ©ristiques Ã©volutives.

	private int terraformation;

	private int stockMinerai;

	private int taxation;

	private int stabilite;

	private String nom;

	private boolean revolte;

	private ArrayList populations;

	private ArrayList batiments;

	private int proprietaire;

	private boolean possedeSanctuaire;

	private Sanctuaire sanc;

	private transient boolean estTerraforme;

	// Les mÃ©thodes d'accÃšs.

	public Sanctuaire getSanctuaire() {
		return sanc;
	}

	public int getRadiation() {
		return radiation;
	}

	public int getTemperature() {
		return temperature;
	}

	public int getGravite() {
		return gravite;
	}

	public int getAtmosphere() {
		return atmosphere;
	}

	public int getTaille() {
		return taille;
	}

	public int getType() {
		return type;
	}

	public int getProductionMinerai() {
		return productionMinerai;
	}

	public int getProductionMarchandise() {
		return productionMarchandise;
	}

	public int getNombreProductionMarchandise() {
		return nombreProductionMarchandise;
	}

	public int getTerraformation() {
		return terraformation;
	}

	public int getStockMinerai() {
		return stockMinerai;
	}

	public int getTaxation() {
		return taxation;
	}

	public int getStabilite() {
		return stabilite;
	}

	public String getNom() {
		return nom;
	}

	public boolean getRevolte() {
		return revolte;
	}

	public int getProprio() {
		return proprietaire;
	}

	public ArrayList getPopulations() {
		return populations;
	}

	public Population getPopulation(int race) {
		int inter = getNiveauPopulation(race);
		if (inter == -1)
			return null;
		else
			return (Population) populations.get(inter);
	}

	// Capacité Encombrement de la planête
	public int getCapaciteEncombrement() {

		float mod = 1;

		/**
		 * Patch Atalante: +10% encombrement max
		 */
		if (Univers.getCommandant(getProprio()).getRace() == 1){
			mod = 1.1f;
		}

		int rtr = 1000 + (taille * taille * 50) + terraformation * Const.MODIFICATEUR_ENCOMBREMENT_TERRAFORMATION;
		rtr = (int)(rtr * mod);
		
		return rtr;
	}

	// Encombrement effectif de la planête
	public int getEncombrement() {
		ConstructionPlanetaire[] c = getBatiments();
		int enc = 0;
		for (int i = 0; i < c.length; i++)
			enc += c[i].getBatiment().getPointsDeConstruction();
		return enc;
	}

	public int[] racesPresentes() {
		int[] retour = new int[populations.size()];
		for (int i = 0; i < populations.size(); i++)
			retour[i] = ((Population) populations.get(i)).getRace();
		return retour;
	}

	public int getNombreDeTypeDePopulationsPresentes() {
		return populations.size();
	}

	public int getNiveauPopulation(int race) {
		for (int i = 0; i < populations.size(); i++)
			if (((Population) populations.get(i)).getRace() == race)
				return i;
		return -1;
	}

	public void eradiquerPopulation(int race) {
		int n = getNiveauPopulation(race);
		populations.remove(n);
	}

	public void setSanctuaire(Sanctuaire s) {
		sanc = s;
	}

	public void setRadiation(int e) {
		radiation = e;
	}

	public void setTemperature(int e) {
		temperature = e;
	}

	public void setGravite(int e) {
		gravite = e;
	}

	public void setAtmosphere(int e) {
		atmosphere = e;
	}

	public void setTaille(int e) {
		taille = e;
	}

	public void setType(int e) {
		type = e;
	}

	public void setProductionMinerai(int e) {
		productionMinerai = e;
	}

	public void setProductionMarchandise(int e) {
		productionMarchandise = e;
	}

	public void setNombreProductionMarchandise(int e) {
		nombreProductionMarchandise = e;
	}

	public void setTerraformation(int e) {
		terraformation = e;
	}

	public void setStockMinerai(int e) {
		stockMinerai = e;
	}

	public void setTaxation(int e) {
		taxation = e;
	}

	public void setStabilite(int e) {
		stabilite = Math.min(100, Math.max(e, 0));
	}

	public void setNom(String e) {
		nom = e;
	}

	public void setRevolte(boolean e) {
		revolte = e;
	}

	public void setProprio(int e) {
		proprietaire = e;
	}

	public boolean estProprio(int numero) {
		if (proprietaire == numero)
			return true;
		else
			return false;
	}

	public boolean estHabite() {
		if (populations.size() > 0)
			return true;
		else
			return false;
	}

	public boolean estSanctuaire() {
		return possedeSanctuaire;
	}

	public float coutTerraformation() {
		return Const.COUT_BASE_TERRAFORMATION + (terraformation + 1)
				* Const.COUT_PALIER_TERRAFORMATION;
	}

	public void terraformer() {
		if (!estTerraforme) {
			terraformation++;
			estTerraforme = true;
		} 
	}

	public boolean estDejaTerraforme() {
		return estTerraforme;
	}
	
	public void setEstDejaTerraforme(boolean terraforme){
		this.estTerraforme = terraforme;
	}

	public void initialiserPopulation() {
		populations = new ArrayList(0);
	}

	public void initialiserBatiments() {
		batiments = new ArrayList(0);
	}

	public void ajouterBatiment(ConstructionPlanetaire b) {
		batiments.add(b);
	}

	public void eliminerBatiment(ConstructionPlanetaire b) {
		batiments.remove(b);
	}

	public ConstructionPlanetaire[] getBatiments() {
		return (ConstructionPlanetaire[]) batiments
				.toArray(new ConstructionPlanetaire[0]);
	}

	public boolean contientBatiment(String code) {
		ConstructionPlanetaire[] c = getBatiments();
		for (int i = 0; i < c.length; i++)
			if (c[i].estBatimentDeType(code))
				return true;
		return false;
	}

	public ConstructionPlanetaire[] getBatimentsDeType(String code) {
		ArrayList<ConstructionPlanetaire> inter = new ArrayList<ConstructionPlanetaire>();
		ConstructionPlanetaire[] liste = getBatiments();
		for (int i = 0; i < liste.length; i++)
			if (liste[i].estBatimentDeType(code)) inter.add(liste[i]);
		
		return (ConstructionPlanetaire[]) inter.toArray(new ConstructionPlanetaire[0]);
	}

	public int nombreBatimentsDeType(String code) {
		return getBatimentsDeType(code).length;
	}

	public String[] listeTypeDeBatimentsPresents() {
		ArrayList inter = new ArrayList();
		ConstructionPlanetaire[] c = getBatiments();
		for (int i = 0; i < c.length; i++)
			if (!inter.contains(c[i].getCode()))
				inter.add(c[i].getCode());
		return (String[]) inter.toArray(new String[0]);
	}

	public Map listeEquipementsNombresDommages() {
		ConstructionPlanetaire[] c = getBatiments();
		HashMap h = new HashMap(c.length);
		for (int i = 0; i < c.length; i++) {
			Object o = h.get(c[i].getCode());
			int[] inter = null;
			if (o == null) {
				inter = new int[2];
				inter[0] = 1;
				inter[1] = c[i].getDommages();
				h.put(c[i].getCode(), inter);
			} else {
				inter = (int[]) o;
				inter[0]++;
				inter[1] = inter[1] + c[i].getDommages();
				h.put(c[i].getCode(), inter);
			}
		}
		return h;
	}

	public Map.Entry[] listeEquipement() {
		HashMap retour = new HashMap();
		String[] l = listeTypeDeBatimentsPresents();
		for (int j = 0; j < l.length; j++)
			retour.put(l[j], new Integer(nombreBatimentsDeType(l[j])));
		return (Map.Entry[]) retour.entrySet().toArray(new Map.Entry[0]);
	}

	public void reparation(int potentiel) {

		if (Univers.getCommandant(getProprio()).getRace() == 0)
			potentiel = potentiel * 2;

		ConstructionPlanetaire[] c = getBatiments();

		for (int i = 0; i < c.length; i++)
			c[i].reparation(potentiel);
	}

	public ConstructionPlanetaire getBatimentLePlusEndommageDeType(String code) {
		ConstructionPlanetaire[] c = getBatimentsDeType(code);
		int max = Integer.MIN_VALUE;
		int index = -1;
		for (int i = 0; i < c.length; i++)
			if (c[i].getDommages() > max) {
				max = c[i].getDommages();
				index = i;
			}
		if (index == -1)
			return null;
		else
			return c[index];
	}

	public int sommeCapacitesSpecialesBatiment(int carac) {
		Technologie[] t = Univers.getTechnologiesPossedantCapaciteSpeciale(
				carac, Const.TECHNOLOGIE_TYPE_BATIMENT);
		if (t == null)
			return 0;
		if (!estHabite()) {
			ArrayList inter = new ArrayList();
			for (int i = 0; i < t.length; i++)
				if (t[i].possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE))
					inter.add(t[i]);
			t = (Technologie[]) inter.toArray(new Technologie[0]);
		}
		int compteur = 0;
		for (int i = 0; i < t.length; i++)
			compteur = compteur + nombreBatimentsDeType(t[i].getCode())
					* t[i].getValeurCaracteristiqueSpeciale(carac);
		return compteur;
	}

	public int capaciteSpecialeBatiment(int carac) {
		Technologie[] t = Univers.getTechnologiesPossedantCapaciteSpeciale(
				carac, Const.TECHNOLOGIE_TYPE_BATIMENT);
		if (t == null)
			return 0;
		if (!estHabite()) {
			ArrayList inter = new ArrayList();
			for (int i = 0; i < t.length; i++)
				if (t[i].possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE))
					inter.add(t[i]);
			t = (Technologie[]) inter.toArray(new Technologie[0]);
		}
		int retour = 0;
		for (int i = 0; i < t.length; i++)
			if (contientBatiment(t[i].getCode()))
				retour = Math.max(retour,
						t[i].getValeurCaracteristiqueSpeciale(carac));
		return retour;
	}

	public int nombreFoisNiveauCapaciteSpeciale(int carac, int niv) {
		Technologie[] t = Univers.getTechnologiesPossedantCapaciteSpeciale(carac, Const.TECHNOLOGIE_TYPE_BATIMENT);
		if (t == null)
			return 0;

		ArrayList inter = new ArrayList();

		for (int i = 0; i < t.length; i++)
			if (t[i].getValeurCaracteristiqueSpeciale(carac) == niv)
				inter.add(t[i]);

		t = (Technologie[]) inter.toArray(new Technologie[0]);

		// Si la planÃšte n'est pas habité on regarde les batiments qui
		// fonctionnent sans personnel
		if (!estHabite()) {
			inter = new ArrayList();
			for (int i = 0; i < t.length; i++)
				if (t[i].possedeCaracteristiqueSpeciale(Const.BATIMENT_CAPACITE_NON_PRESENCE_HUMAINE))
					inter.add(t[i]);
			t = (Technologie[]) inter.toArray(new Technologie[0]);
		}

		// Une fois la liste de techno retournée , on regarde
		int retour = 0;

		if (carac == Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE)
			for (int i = 0; i < t.length; i++)
				retour = retour + (t[i].getNiveau() + 1) * nombreBatimentsDeType(t[i].getCode());
		else
			for (int i = 0; i < t.length; i++)
				retour = retour + nombreBatimentsDeType(t[i].getCode());
		return retour;
	}

	public boolean contientUniteDeRecyclage() {
		if (capaciteSpecialeBatiment(Const.BATIMENT_CAPACITE_RECYCLAGE_MINERAI) != 0)
			return true;
		else
			return false;
	}

	public int capaciteExtractionMinerai() {
		return sommeCapacitesSpecialesBatiment(Const.BATIMENT_CAPACITE_EXTRACTION_MINERAI);
	}

	public int capaciteProductionMinerai() {
		return productionMinerai
				+ capaciteSpecialeBatiment(Const.BATIMENT_CAPACITE_EXTRACTION_AVANCE);
	}

	public int getNombreDeMineUtile() {
		// if( getRevolte() ) return 0;
		int ce = capaciteExtractionMinerai();
		int cp = capaciteProductionMinerai();
		if (ce > cp)
			return cp;
		else
			return ce;
	}

	public int calculeRevenuMinerai() {
		if( getRevolte() ){
			return 0;
		}
		
		/**
		 * Patch atalante +1 en production de minerai
		 */
		Commandant commandant = Univers.getCommandant(proprietaire);
		int bonus = commandant.getRace() == 1 ? 1 : 0;
		
		int c = capaciteProductionMinerai() + bonus;
		int e = capaciteExtractionMinerai();
		int retour = 0;
		for (int i = 0; i < c; i++)
			if (e > i)
				retour = retour + c - i;
		return retour;
	}

	public int getPointsDeConstruction() {
		if (getRevolte())
			return 0;
		else
			return 1 + sommeCapacitesSpecialesBatiment(Const.BATIMENT_GAIN_POINTS_CONSTRUCTION);
	}

	public int getDifferenceCapaciteProductionMinerai() {
		return capaciteProductionMinerai() - capaciteExtractionMinerai();
	}

	public int getPorteeRadar() {
		if (getRevolte())
			return 0;
		else
			return capaciteSpecialeBatiment(Const.BATIMENT_PORTEE_RADAR);
	}

	public int sommeNiveauBatiment() {
		// Liste des batiments
		int retour = 0;
		String[] liste = listeTypeDeBatimentsPresents();
		for (int i = 0; i < liste.length; i++) {
			retour += Univers.getTechnologie(liste[i]).getNiveau() + 1;
		}
		return retour;
	}
	
	public int sommeNiveauBatimentParCaracEtValeur(int carac, int var){

		int retour = 0;
		ConstructionPlanetaire[] liste = getBatiments();
		for (int i = 0; i < liste.length; i++) {
			Technologie t = Univers.getTechnologie(liste[i].getCode());
			if( t.possedeCaracteristiqueSpecialeParCaractEtValeur(carac,var) ){
				retour += (t.getNiveau()+1);
			}
		}
		return retour;
	}

	public int getProductionMarchandise(int mar) {
		if (getRevolte())
			return 0;
		int retour = 0;
		if (productionMarchandise == mar)
			retour = retour + nombreProductionMarchandise;
		if (mar == 1) {
			retour = retour + sommeNiveauBatiment();
		} else {
			retour = retour + 5 * sommeNiveauBatimentParCaracEtValeur( Const.BATIMENT_CAPACITE_PRODUCTION_MARCHANDISE, mar + 1);
		}
		return retour;
	}

	public void ajouterTypeDePopulation(Population p) {
		populations.add(p);
		tamiserPopulation();
	}

	// constructeur

	public Planete() {
		possedeSanctuaire = false;
	}

	// Les méthodes statiques.

	public static Planete creerAuHasard(int raceDeDepart, String nom, int ordre) {

		int[] tab_race = { 10, 15, 10, 6, 10, 0 };
		Planete planete = new Planete();
		if (Univers.getTest(10)) {
			if (Univers.getTest(tab_race[raceDeDepart])) {
				planete.setSanctuaire(new Sanctuaire());
			}
		}
		planete.creerCaracteristiques(nom, ordre);
		planete.initialiserPopulation();
		planete.initialiserBatiments();

		planete.explorerPlanete(raceDeDepart);
		planete.ajouterPopulation(raceDeDepart, Univers.getInt(50) + 50);

		planete.initialiserBatiments();

		if (Univers.getTest(20)) {
			if (Univers.getTest(2)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("boucplaIII"));
			} else if (Univers.getTest(4)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("boucplaII"));
			} else if (Univers.getTest(6)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("boucplaI"));
			}
		}
		if (Univers.getTest(20)) {
			if (Univers.getTest(3)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("battplaI"));
			} else if (Univers.getTest(6)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("battlaI"));
			}
		}

		if (Univers.getTest(20)) {
			if (Univers.getTest(2)) {
				planete.ajouterBatiment(new ConstructionPlanetaire("radarIII"));
			}
		}

		return planete;
	}

	// Les méthodes pour gérer les planÃštes en fin de tour.

	public void testerRevolte() {
		revolte = !Univers.getTest(Math.max(1, stabilite + 1));
		if (!estHabite())
			revolte = false;
	}

	public void evolutionStabilite(int modificateurSysteme, Position pos) {
		int modRelation = 0;
		int[] race = racesPresentes();
		for (int i = 0; i < race.length; i++)
			for (int j = i + 1; j < race.length; j++)
				modRelation = modRelation + Systeme.niveauRelation(Univers.getRelationRaces(pos, race[i], race[j]));
		setStabilite(stabilite + modificateurSysteme + modRelation + Const.MODIFICATEUR_STABILITE_TAXATION[taxation]);
	}

	public void evolutionPopulation(int modificateurSysteme,
			boolean esclavagisme) {
		if (!revolte) {
			Population[] p = (Population[]) populations
					.toArray(new Population[0]);
			for (int i = 0; i < p.length; i++)
				ajouterPopulation(
						p[i].getRace(),
						(esclavagisme ? 10
								: Math.max(
										10,
										(p[i].getPopActuelle() * (calculeProgressionPop(p[i]
												.getRace()) + modificateurSysteme)) / 100)));
		}
	}

	public int evolutionPopulation(int modificateurSysteme,
			boolean esclavagisme, boolean test) {
		int excedent = 0;
		if (!revolte) {
			Population[] p = (Population[]) populations
					.toArray(new Population[0]);
			for (int i = 0; i < p.length; i++)
				excedent += ajouterPopulation(
						p[i].getRace(),
						(esclavagisme ? 10
								: Math.max(
										10,
										(p[i].getPopActuelle() * (calculeProgressionPop(p[i]
												.getRace()) + modificateurSysteme)) / 100)),
						true);
		}
		return excedent;
	}

	public int politiqueExtermination(int race) {
		Population p = getPopulation(race);
		if (p == null)
			return 0;
		if (p.getPopActuelle() > 30) {
			int retour = p.getPopActuelle() / 2;
			p.setPopActuelle(retour);
			return retour;
		}
		eradiquerPopulation(race);
		return p.getPopActuelle();
	}

	public void evolutionMinerai() {
		if (!revolte)
			ajouterMinerai(calculeRevenuMinerai());
	}

	// Les autres méthodes.

	public void creerCaracteristiques(String n, int o) {
		nom = n + " " + Integer.toString(o + 1);
		taille = Univers.getInt(Const.TAILLE_MAX_DE_PLANETE) + 1;
		type = Univers.getInt(Const.TYPE_MAX_DE_PLANETE + 1);
		productionMinerai = Univers.getInt(Const.PRODUCTION_MINERAI_MAX - 2) + 3;
		nombreProductionMarchandise = Univers.getInt(2);
		productionMarchandise = Univers.getInt(Const.NB_MARCHANDISES - 3);
		atmosphere = Univers.getInt(Const.NB_SORTES_ATMOSPHERES);
		if (Univers.getInt(100) < 75) {
			radiation = 1 + Univers.getInt(85);
			temperature = -80 + Univers.getInt(151);
			if (Univers.getInt(100) < 50) {
				gravite = 1 + Univers.getInt(40);
			} else {
				gravite = 50 + Univers.getInt(50);
			}
		} else {
			radiation = Const.RAD_MIN
					+ Univers.getInt(Const.RAD_MAX - Const.RAD_MIN);
			temperature = (Const.TEMP_MIN + (Univers.getInt(Const.TEMP_MAX
					- Const.TEMP_MIN) + type * Univers.getInt(4))
					% (Const.TEMP_MAX - Const.TEMP_MIN));
			gravite = (Const.GRA_MIN + (Univers.getInt(Const.GRA_MAX
					- Const.GRA_MIN) + taille * Univers.getInt(3))
					% (Const.GRA_MAX - Const.GRA_MIN));
		}

		taxation = 2;
		stabilite = 100;
		stockMinerai = Univers.getInt(20);
	}

	public boolean peutEtreColoniseParRace(int race){
		return calculeMaxPop(race)>0;
	}
	
	public boolean explorerPlanete(int race) {
		if (!racePresente(race))
			if (calculeMaxPop(race) > 0) {
				Population pop = new Population();
				pop.setRace(race);
				ajouterTypeDePopulation(pop);
				return true;
			}
		return false;
	}

	public void tamiserPopulation() {
		for (int i = 0; i < populations.size(); i++) {
			Population p = (Population) populations.get(i);
			p.setPopActuelle(Math.min(p.getPopActuelle(),
					calculeMaxPop(p.getRace())));
		}
	}

	public void diminuerPopulation(int nb) {
		int c = nb;
		Population[] p = (Population[]) populations.toArray(new Population[0]);
		boolean passe = true;
		while ((c > 0) && (passe)) {
			passe = false;
			for (int i = 0; i < p.length; i++)
				if ((p[i].getPopActuelle() > 0) && (c > 0)) {
					p[i].setPopActuelle(p[i].getPopActuelle() - 1);
					c--;
					passe = true;
				}
		}
	}

	public boolean racePresente(int race) {
		Population p = getPopulation(race);
		if (p == null)
			return false;
		else
			return true;
	}

	public int getPopActuelle(int race) {
		Population p = getPopulation(race);
		if (p == null)
			return 0;
		else
			return p.getPopActuelle();
	}

	public int calculeMaxPop(int race) {
		//int base = calculeMaxPopDeBase(race);
		//if (!racePresente(race))
		//	return modificateurOccupationPopulation(base, getNombreDeTypeDePopulationsPresentes() + 1);
		//else
		//   return modificateurOccupationPopulation(base, getNombreDeTypeDePopulationsPresentes());
		return calculeMaxPopDeBase(race);
	}

	private int modificateurOccupationPopulation(int base, int place) {
		if (place == 0)
			return 0;
		if (place == 1)
			return base;
		if ((place > 1) && (place < 6))
			return (int) (base * 3 / 4);
		else
			return (int) (base / 10F);
	}

	public int meilleurPop() {
		int rtr = -1;
		int meilleur = 0;
		for (int i = 0; i < Const.NB_RACES - 1; i++) {
			int local = calculeMaxPopDeBase(i);
			if (local > meilleur) {
				meilleur = local;
				rtr = i;
			}
		}
		return rtr;
	}

	private int calculeMaxPopDeBase(int race) {
		int r, t, g;
		if ((radiation < (-2 * terraformation + Const.HABITAT_RADIATION[race][0]))
				|| (radiation > (2 * terraformation + Const.HABITAT_RADIATION[race][1]))
				|| (temperature < (-2 * terraformation + Const.HABITAT_TEMPERATURE[race][0]))
				|| (temperature > (2 * terraformation + Const.HABITAT_TEMPERATURE[race][1]))
				|| (gravite < Const.HABITAT_GRAVITE[race][0])
				|| (gravite > Const.HABITAT_GRAVITE[race][1]))
			return 0;

		else {
			// formule=-1000*(x-x1)(x-x2)/(x1-x2)Â²

			r = 1
					- (1000 * (radiation - (-2 * terraformation + Const.HABITAT_RADIATION[race][0])) * (radiation - (2 * terraformation + Const.HABITAT_RADIATION[race][1])))
					/ ((int) Math.pow(2 * terraformation, 2) + (int) Math.pow(
							Const.HABITAT_RADIATION[race][0]
									- Const.HABITAT_RADIATION[race][1], 2));
			t = 1
					- (1000 * (temperature - (-2 * terraformation + Const.HABITAT_TEMPERATURE[race][0])) * (temperature - (2 * terraformation + Const.HABITAT_TEMPERATURE[race][1])))
					/ ((int) Math.pow(2 * terraformation, 2) + (int) Math.pow(
							Const.HABITAT_TEMPERATURE[race][0]
									- Const.HABITAT_TEMPERATURE[race][1], 2));
			g = 1
					- (1000 * (gravite - Const.HABITAT_GRAVITE[race][0]) * (gravite - Const.HABITAT_GRAVITE[race][1]))
					/ ((int) Math.pow(Const.HABITAT_GRAVITE[race][0]
							- Const.HABITAT_GRAVITE[race][1], 2));

			return Math.max(
				(
						( (r + t + g) * 2 * taille + Const.RACES_ATMOSPHERES[race][atmosphere] * 100 * taille ) * 9 / 10
				)
					, 10);
		}
	}

	public int calculeProgressionPop(int race) {
		if (calculeMaxPopDeBase(race) == 0)
			return 0;
		int retour;
		retour = 8
				+ (type / 2)
				+ (calculeMaxPopDeBase(race) / 500)
				+ Const.RACES_ATMOSPHERES[race][atmosphere]
				+ Const.RACES_CARACTERISTIQUES[race][Const.RACE_CARACTERISTIQUE_AUGMENTATION_POPULATION];
		return Math.max(retour, 1);
	}

	public int augmentationMoyenne() {
		int retour = 0;
		for (int i = 0; i < populations.size(); i++)
			retour = retour
					+ ((Population) populations.get(i)).getPopActuelle()
					* calculeProgressionPop(((Population) populations.get(i))
							.getRace());
		if (populationTotale() > 0)
			return retour / populationTotale();
		else
			return 0;
	}

	public int populationTotale() {
		int retour = 0;
		for (int i = 0; i < populations.size(); i++)
			retour = retour
					+ ((Population) populations.get(i)).getPopActuelle();
		return retour;
	}

	public int populationMaximaleTotale() {
		int retour = 0;
		for (int i = 0; i < populations.size(); i++)
			retour = retour
					+ calculeMaxPop(((Population) populations.get(i)).getRace());
		return retour;
	}

	public ObjetTransporte eliminerBatiment(Batiment b, int nombre) {
		ObjetComplexeTransporte o = new ObjetComplexeTransporte(b.getCode());
		ConstructionPlanetaire c = getBatimentLePlusEndommageDeType(b.getCode());
		while ((o.getNombreObjets() < nombre) && (c != null)) {
			eliminerBatiment(c);
			o.ajouterObjet(c);
			c = getBatimentLePlusEndommageDeType(b.getCode());
		}
		return o;
	}

	public void ajouterBatiment(ObjetTransporte o) {
		for (int i = 0; i < o.getNombreObjets(); i++)
			try {
				ajouterBatiment((ConstructionPlanetaire) ((ObjetComplexeTransporte) o)
						.getObjet(i));
			} catch (Exception e) {
				System.out.println("ajouterBatiment " + o.getCode() + ": " + e.getMessage());
			}
	}

	public float getValeurBatiments() {
		ConstructionPlanetaire[] c = getBatiments();
		float retour = 0F;
		for (int i = 0; i < c.length; i++)
			retour = retour + c[i].getPrix();
		return retour;
	}

	public int recyclerMateriel(Batiment b, int nombre) {
		int nbElimine = eliminerBatiment(b, nombre).getNombreObjets();
		if (nbElimine > 0)
			if (contientUniteDeRecyclage())
				ajouterMinerai(b.getMineraiNecessaire() * nbElimine);
		return nbElimine;
	}

	public float getRevenu() {
		if (!revolte)
			return ((float) populationTotale() * taxation) / 10;
		else
			return 0F;
	}

	public float getRevenuTechnologique() {
		float retour = 0F;
		for (int i = 0; i < populations.size(); i++) {
			Population p = (Population) populations.get(i);
			int potentiel = (p.getPopActuelle() * taxation) / 10;
			retour = retour + potentiel;
		}
		return retour;
	}

	public float getEntretien() {
		ConstructionPlanetaire[] c = getBatiments();
		float retour = 0F;
		for (int i = 0; i < c.length; i++)
			retour = retour + c[i].getEntretien();
		return retour;
	}

	public ObjetTransporte supprimerRichesse(String code, int nombre) {
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_BATIMENT)
			return eliminerBatiment((Batiment) Univers.getTechnologie(code),
					nombre);
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_MINERAI)
			return eliminerMinerai(nombre);
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_POPULATION)
			return eliminerPopulation(code, nombre);
		return null;
	}

	public void ajouterRichesse(ObjetTransporte o) {
		if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_BATIMENT)
			ajouterBatiment(o);
		else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_MINERAI)
			ajouterMinerai(o);
		else if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_POPULATION)
			ajouterPopulation(o);
	}

	public ObjetTransporte eliminerMinerai(int nombre) {
		int nbElimine = Math.min(stockMinerai, nombre);
		stockMinerai = stockMinerai - nbElimine;
		return new ObjetSimpleTransporte(Messages.MINERAI, nbElimine);
	}

	public void ajouterMinerai(int nombre) {
		stockMinerai = stockMinerai + nombre;
	}

	public void ajouterMinerai(ObjetTransporte o) {
		ajouterMinerai(o.getNombreObjets());
	}

	public ObjetTransporte eliminerPopulation(String code, int nombre) {
		int race = Utile.numeroRace(code);
		if (!racePresente(race))
			return null;
		Population p = getPopulation(race);
		int nbElimine = Math.min(nombre, p.getPopActuelle());
		p.setPopActuelle(p.getPopActuelle() - nbElimine);
		return new ObjetSimpleTransporte(code, nbElimine);
	}

	public int ajouterPopulation(int race, int nombre, boolean test) {
		int excedent = 0;
		if (racePresente(race)) {
			Population p = getPopulation(race);
			excedent = Math.max(0, (calculeMaxPop(race) - p.getPopActuelle())
					- nombre);
			int nbAjoute = Math.min(nombre,
					calculeMaxPop(race) - p.getPopActuelle());
			p.setPopActuelle(p.getPopActuelle() + nbAjoute);
		}
		return excedent;
	}

	// save de ajouterPopulation
	public int ajouterPopulation(int race, int nombre) {
		if (racePresente(race)) {
			Population p = getPopulation(race);
			int nbAjoute = Math.min(nombre,
					calculeMaxPop(race) - p.getPopActuelle());
			p.setPopActuelle(p.getPopActuelle() + nbAjoute);
			return nbAjoute;
		}
		return 0;
	}

	public int ajouterPopulation(String code, int nombre) {
		return ajouterPopulation(Utile.numeroRace(code), nombre);
	}

	public void ajouterPopulation(ObjetTransporte o) {
		ajouterPopulation(o.getCode(), o.getNombreObjets());
	}

	public void eliminerPertesBatiments() {
		ArrayList elimine = new ArrayList();
		for (int i = 0; i < batiments.size(); i++)
			if (((ConstructionPlanetaire) batiments.get(i)).estDetruit())
				elimine.add(batiments.get(i));
		for (int i = 0; i < elimine.size(); i++)
			eliminerBatiment((ConstructionPlanetaire) elimine.get(i));
	}

	public static void upgradeplanete(Planete p) {
		p.setTerraformation(p.getTerraformation() + 8);
	}
	
	/**
	 * Calcule l'espace libre sur une planète
	 * @return espace libre
	 */
	public int getEspaceLibre(){
		return getCapaciteEncombrement() - getEncombrement();
	}

}
