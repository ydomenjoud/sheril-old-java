// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;
import java.io.Serializable;

public class Possession implements Serializable {

	static final long serialVersionUID = 1656583489195831918L;

	private ArrayList<Construction> constructions;

	private String programmationConstruction;

	private int politique;

	private HashMap<Integer, Integer> budget;

	private HashMap<Integer, int[]> poste;

	// Les méthodes d'accès

	public void setPolitique(int entree) {
		politique = entree;
	}

	public int getPolitique() {
		return politique;
	}

	public String getDescriptionPolitique(Locale l) {
		return Univers.getMessage("POLITIQUES", politique, l);
	}

	public boolean aPolitiqueAnti() {
		return racePolitiqueAnti() != -1;
	}

	public int racePolitiqueAnti() {
		if (politique == Const.POLITIQUE_ANTI_HUMAIN)
			return 0;
		if (politique == Const.POLITIQUE_ANTI_ZORGLUB)
			return 1;
		if (politique == Const.POLITIQUE_ANTI_GOLO)
			return 2;
		if (politique == Const.POLITIQUE_ANTI_YOZDA)
			return 3;
		if (politique == Const.POLITIQUE_ANTI_FERGOK)
			return 4;
		if (politique == Const.POLITIQUE_ANTI_RAG)
			return 5;
		return -1;
	}

	public boolean possedeStockImportantPoste(int marchandise) {
		return getQuantiteMarchandise(marchandise) >= 100;
	}

	public String getProgrammationConstruction() {
		return programmationConstruction;
	}

	public boolean possedeProgrammationConstruction() {
		return programmationConstruction != null;
	}

	public void programmerConstruction(String c) {
		programmationConstruction = c;
	}

	public void ajouterConstruction(Construction c) {
		// Liste des constructions en cours
		Construction[] l = listeConstructions();
		for (int i = 0; i < l.length; i++)
			// Si les codes ET PLANETE sont égaux, on rajoute le nombre de
			// construction en plus et on quitte la void
			if (l[i].getCode().equals(c.getCode())
					&& l[i].getPlanete() == c.getPlanete()) {
				l[i].augmenterNombre(c.getNombre());
				return;
			}

		// sinon on ajoute le nouveau domaine de construction
		constructions.add(c);

	}

	public Construction[] listeConstructions() {
		return constructions.toArray(new Construction[0]);
	}

	public int getNombreConstructions() {
		return constructions.size();
	}

	public boolean constructionEnCours() {
		return (getNombreConstructions() != 0);
	}

	public int pointsNecessairesPourConstructionDeType(String code) {
		int retour = 0;
		for (int i = 0; i < constructions.size(); i++)
			if (constructions.get(i).getCode().equals(code))
				retour = retour
						+ constructions.get(i)
								.getPointsNecessaires();
		return retour;
	}

	public void setBudget(int typeDeChamp, int pourcentage) {
		budget.put(typeDeChamp, pourcentage);
	}

	public int getBudget(int typeDeChamp) {
		Integer o = budget.get(typeDeChamp);
		if (o == null)
			return 0;
		else
			return o;
	}

	public boolean conformiteBudget() {
		int total = 0;
		for (int i = 0; i < Const.NB_DOMAINES_BUDGET; i++)
			total = total + getBudget(i);
		if (total > 100)
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	public boolean modifierBudget(int[] typeDeChamp, int[] pourcentage) {
		HashMap<Integer, Integer> inter = (HashMap<Integer, Integer>) budget.clone();
		for (int i = 0; i < typeDeChamp.length; i++)
			setBudget(typeDeChamp[i], pourcentage[i]);
		if (conformiteBudget())
			return true;
		else {
			budget = inter;
			return false;
		}
	}

	public int[] listeNumerosMarchandises() {
		Integer[] l = poste.keySet().toArray(new Integer[0]);
		int[] retour = new int[l.length];
		for (int i = 0; i < l.length; i++)
			retour[i] = l[i];
		return retour;
	}

	public void setMarchandise(int marchandise, int[] carac) {
		poste.put(marchandise, carac);
	}

	public void setMarchandise(int marchandise, int prix, int stock) {
		int[] inter = new int[2];
		inter[0] = prix;
		inter[1] = stock;
		setMarchandise(marchandise, inter);
	}

	public boolean contientMarchandise(int marchandise) {
		return poste.containsKey(marchandise);
	}

	public int[] getMarchandise(int marchandise) {
		return poste.get(marchandise);
	}

	public int getPrixMarchandise(int marchandise) {
		if (contientMarchandise(marchandise))
			return getMarchandise(marchandise)[0];
		else
			return Const.PRIX_MARCHANDISE_PAR_DEFAUT;
	}

	public float getPrixMarchandiseFloat(int marchandise) {
		return ((float) getPrixMarchandise(marchandise)) / 10F;
	}

	public void setPrixMarchandise(int marchandise, int prix) {
		setMarchandise(marchandise, Math.min(100, Math.max(1, prix)),
				getQuantiteMarchandise(marchandise));
	}

	public void setPrixMarchandise(int marchandise, float prix) {
		setMarchandise(marchandise, (int) (10 * prix),
				getQuantiteMarchandise(marchandise));
	}

	public int getQuantiteMarchandise(int marchandise) {
		if (contientMarchandise(marchandise))
			return getMarchandise(marchandise)[1];
		else
			return 0;
	}

	public void setQuantiteMarchandise(int marchandise, int quantite) {
		setMarchandise(marchandise, getPrixMarchandise(marchandise), quantite);
	}

	public void ajouterMarchandise(int marchandise, int quantite) {
		if (contientMarchandise(marchandise))
			setQuantiteMarchandise(marchandise, quantite
					+ getQuantiteMarchandise(marchandise));
		else
			setQuantiteMarchandise(marchandise, quantite);
		setPrixMarchandise(marchandise, getPrixMarchandise(marchandise)
				- quantite);
	}

	public void supprimerMarchandise(int marchandise, int quantite) {
		if (contientMarchandise(marchandise)) {
			setQuantiteMarchandise(marchandise,
					Math.max(0, getQuantiteMarchandise(marchandise) - quantite));
		}
		setPrixMarchandise(marchandise, getPrixMarchandise(marchandise)
				+ quantite);
	}

	public void initialiserConstructions() {
		constructions = new ArrayList<>(0);
	}

	public void initialiserProgrammationConstruction() {
		programmationConstruction = null;
	}

	public void initialiserBudget() {
		budget = new HashMap<>(Const.NB_DOMAINES_BUDGET);
	}

	public void initialiserPoste() {
		poste = new HashMap<>(Const.NB_MARCHANDISES);
	}

	// Le constructeur

	public Possession() {
		initialiserConstructions();
		initialiserBudget();
		initialiserPoste();
		initialiserProgrammationConstruction();
	}

	// Les méthodes statiques.

	public static Possession creerAuHasard() {
		Possession retour = new Possession();
		retour.setBudget(0, 0);
		retour.setBudget(1, 0);
		retour.setBudget(2, 0);
		retour.setPolitique(Const.POLITIQUE_CONSTRUCTION);
		return retour;
	}

	// méthodes pour gérer les fins de tour.

	public void miseAJourReputation(Commandant c) {
		if (aPolitiqueAnti())
			c.ajouterReputation(-300);
	}

	public void evolutionPosteCo(int numero, int taux, Systeme s) {
		int bonus = 0;
		Commandant c = Univers.getCommandant(numero);
		int reput = c.getStatutReputationIndex();

		if (politique == Const.POLITIQUE_COMMERCE)
			bonus = 2;
		if (politique == Const.POLITIQUE_TOTALITAIRE)
			bonus = -1;

		 int bonusNourriture=0;
		// Bonus matériel agricole
        // ce sont désormais des déchets dans le moteur
        // et il faudrait avoir un impact négatif dessus , à voir
//		 if(possedeStockImportantPoste(Const.PRODUIT_MATERIEL_AGRICOLE))
//             bonusNourriture=s.nbPlanetesHabitees(numero);
		for (int i = 0; i < Const.NB_MARCHANDISES; i++) {
			int prod = s.getProductionMarchandise(numero, i);
			if (prod != 0)
				ajouterMarchandise(i, prod + bonus + bonusNourriture);
		}

		for (int i = 0; i < Const.NB_MARCHANDISES; i++)
			if (contientMarchandise(i)) {
				int ancienPrix = getPrixMarchandise(i);
				setPrixMarchandise(
						i,
						ancienPrix
								- ((100 - taux) / 100)
								* ((ancienPrix - Univers
										.getPrixMoyenMarchandise(i)) / (2 + getQuantiteMarchandise(i) / 10)));
			}
	}

	public int[] getEvolutionStabilite(Commandant c, Systeme s) {
		int mod_gou, mod_pos, mod_pol, mod_post, mod_tax, mod_race;
		mod_gou = mod_pos = mod_pol = mod_post = mod_tax = mod_race = 0;

		// Gouverneur
		if (c.existenceGouverneurSurPossession(s.getPosition()))
			mod_gou = c.getGouverneurSurPossession(s.getPosition()) .getMoralModifie();

		// Politique
		if (getPolitique() == Const.POLITIQUE_TOTALITAIRE)
			mod_pol = mod_pol + 2;
		if (getPolitique() == Const.POLITIQUE_INTEGRISME)
			mod_pol = mod_pol - 2;
		if (getPolitique() == Const.POLITIQUE_ESCLAVAGISTE)
			mod_pol = mod_pol - 2;
		if (getPolitique() == Const.POLITIQUE_LOISIR)
			mod_pol = mod_pol + 2;
		if (aPolitiqueAnti())
			mod_pol = mod_pol - 5;

		// Stock de marchandises
		/**
		if (possedeStockImportantPoste(Const.PRODUIT_ALCOOLS))
			mod_post = mod_post - 1; // Pas de malus pour systeme de guidage
			**/
		if (possedeStockImportantPoste(Const.PRODUIT_ARMEMENT))
			mod_post = mod_post - 1;
		if (possedeStockImportantPoste(Const.PRODUIT_HOLOFILM))
			mod_post = mod_post + 1;

		// Position Ã  la capitale
		if (c.getCapitale() == null)
			mod_pos = Const.MODIFICATEUR_STABILITE_CAPITALE[Const.MODIFICATEUR_STABILITE_CAPITALE.length - 1][1];
		else {
			int distance = Position.distance(c.getCapitale(), s.getPosition());
			if (distance == 0)
				mod_pos = Const.MODIFICATEUR_STABILITE_CAPITALE[0][1];
			else
				for (int i = Const.MODIFICATEUR_STABILITE_CAPITALE.length - 2; i >= 0; i--)
					if (Const.MODIFICATEUR_STABILITE_CAPITALE[i][0] < distance) {
						mod_pos = Const.MODIFICATEUR_STABILITE_CAPITALE[i + 1][1];
						break;
					}
		}

		// Taxation
		mod_tax = Const.MODIFICATEUR_STABILITE_TAXATION[s.getTaxation(c.getNumero())];

		// Bonus racial Fremen
		if (c.getRace() == 0)
			mod_race = 1;

		return new int[] { mod_gou, mod_pol, mod_post, mod_pos, mod_tax, mod_race };

	}

	public String getStringEvolutionStabilite(Commandant c, Systeme s, boolean tot) {
		int[] tab_stab = getEvolutionStabilite(c, s);
		int total = 0;
		String retour = "";
		String[] nom_cat = new String[] { "Gouverneur", "Politique", "Marchandises", "Position", "Taxation", "Bonus racial" };
		String[] balises = new String[] { "<font color=" + Rapport.cC[6] + ">", "</font>" };

		for (int i = 0; i < tab_stab.length; i++)
			if (tab_stab[i] != 0) {
				retour += nom_cat[i] + ": " + balises[0] + tab_stab[i]
						+ balises[1];
				if (i != tab_stab.length - 1)
					retour += " ; ";
				total = total + tab_stab[i];
			}
		if (tot)
			return (total > 0 ? "+" + total : total + "");

		return retour;
	}

	public void programmationConstructions(Commandant com, Systeme s) {
		// Si il y a un programme
		if (possedeProgrammationConstruction()) {

			// On récupére les construction en cours
			Construction[] lc = listeConstructions();
			int ptNecessaires = 0;
			// On calcul le nombre de point Ã  faire pour ces construction
			for (int i = 0; i < lc.length; i++)
				ptNecessaires = ptNecessaires + lc[i].getPointsNecessaires();

			// potentiel disponible
			int potentiel = s.getPointsDeConstructionModifie(com.getNumero(),
					com.getGouverneurSurPossession(s.getPosition()), this,
					s.getPosition());

			// On retire le nombre de PC necessaire
			potentiel = potentiel - ptNecessaires;

			// Nombre de PC necessaire pour la construction programmée
			int necessaire = Construction
					.getPointsNecessaires(programmationConstruction);

			// Si il y a suffisament de PC on réalise la construction
			if (potentiel > necessaire) {
				// int[][] m=programmationConstruction.getMarchandises(); //
				// marchandise de la construction
				int nb = Math.max(1, potentiel / necessaire);
				Construction c = new Construction(programmationConstruction,
						nb, Integer.MIN_VALUE);
				ajouterConstruction(c);
			}
		}
	}

	public void resolutionConstructions(Commandant com, Systeme s) {

		Construction[] c = listeConstructions();
		int potentiel = s.getPointsDeConstructionModifie(com.getNumero(),
				com.getGouverneurSurPossession(s.getPosition()), this,
				s.getPosition());

		int[] ptNecessaires = new int[c.length];
		for (int i = 0; i < c.length; i++)
			ptNecessaires[i] = c[i].getPointsNecessaires();
		int[] sauvegarde = (int[]) ptNecessaires.clone();
		boolean boucle = true;

		if (c.length == 0)
			boucle = false;

		while (boucle) {
			boolean constructionRestante = false;
			for (int i = 0; i < c.length; i++)
				if (potentiel > 0)
					if (ptNecessaires[i] > 0) {
						ptNecessaires[i]--;
						potentiel--;
						constructionRestante = true;
					}
			if ((potentiel == 0) || (!constructionRestante))
				break;
		}

		for (int i = 0; i < c.length; i++) {
			// On ajoute les points de construction distribué
			c[i].setPointsEffectues(c[i].getPointsEffectues() + sauvegarde[i] - ptNecessaires[i]);

			/** Gestion de la construction en elle meme * */
			boolean isBatiment = Univers.existenceTechnologie(c[i].getCode());
			boolean isVaisseaux = !isBatiment;
			
			// Description de la technologie
			String descriptionTechno = null;
			if (isBatiment){
				descriptionTechno = Univers.getTechnologie(c[i].getCode()).getNomComplet(com.getLocale());
			} else {
				descriptionTechno = c[i].getCode();
			}

			// Code des objets
			String code = c[i].getCode();
			// Nb de Pc d'un object de la construction
			int pc = Construction.getPointsNecessaires(code);
			// Nb d'objet constructible qui pourront sortir dans la Construction
			int nb = c[i].getPointsEffectues() / pc;
			// Nombre de point de structure nécessaire
			int nbPointsDeStructure = isBatiment ? new ConstructionPlanetaire(code).getBatiment().getPointsDeStructure() : 0;
			int espaceLibreSurSysteme = nbPointsDeStructure>0 ? s.getEspaceLibre(com.getNumero()) : 0;
			
			// Variable de test
			boolean pasAssezDeMinerai = false;
			boolean pasAssezDeCentaure = false;
			boolean pasAssezDeMarchandises = false;
			boolean pasAssezDePlace = false;

			// Prix et minerai unitaire
			float prix = Construction.getPrix(code);
			int minerai = Construction.getMinerai(code);

			// Recalcul du nombre d'objet de la construction qui vont sortir ce tour
			int l_argent = (int) (com.getCentaures() / prix); // Limite d'Argent
			int l_minerai = (int) (s.getStockMinerai(com.getNumero()) / minerai); // Limite de Minerai
			int l_espace = nbPointsDeStructure>0 ? (int) (espaceLibreSurSysteme / nbPointsDeStructure) : Integer.MAX_VALUE; // limite d'espace
			int l_marchan = nb; // Limite de Marchandises
			StringBuffer marchandises_manquantes = new StringBuffer("");
			
			 // marchandise de la construction
			int[][] m1 = Construction.getMarchandises(code);
			if (m1 != null){
				for (int j = 0; j < m1.length; j++) {
					 // quantite de marchandise "j" dans le poste commercial
					int nb1 = getQuantiteMarchandise(m1[j][0]);
					int nb2 = m1[j][1]; // Quantitee demandee par la construction
					int lim_locale = Math.min(l_marchan, (int) (nb1 / nb2));
					
					if (lim_locale < l_marchan) {
						l_marchan = lim_locale;
						marchandises_manquantes.append(Univers.getMessage( "MARCHANDISES", m1[j][0], com.getLocale()) + ",");
					}
				}
			}

			int nbbis = nb; // Nombre de construction qui pourront reellement sortir

			if (l_argent < nb) {
				nbbis = Math.min(l_argent, nbbis);
				pasAssezDeCentaure = true;
			}
			if (l_minerai < nb) {
				nbbis = Math.min(l_minerai, nbbis);
				pasAssezDeMinerai = true;
			}
			if (l_marchan < nb) {
				nbbis = Math.min(l_marchan, nbbis);
				pasAssezDeMarchandises = true;
			}
			if (l_espace < nb) {
				nbbis = Math.min(l_espace, nbbis);
				pasAssezDePlace = true;
			}
			

			// Si c'est un vso et qu'il n'y a pas de chantier spatial => erreur
			if (isVaisseaux && s.capaciteSpecialeBatiment(com.getNumero(), Const.BATIMENT_CAPACITE_PRODUCTION_VAISSEAU) == 0){
				com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0007", s.getPosition(), descriptionTechno, c[i].getNombre());
			// Sinon on résout la Construction
			}else if (nbbis > 0) {

				/**
				 * Patch Fergok
				 */
				if( com.getRace() == 4 && isVaisseaux ){ prix = prix * 0.9f; }
				
				// Deduction du prix des constructions
				com.modifierBudget(Const.BUDGET_COMMANDANT_REALISATION_CONSTRUCTION, -prix * nbbis);
				// Suppression du minerai utilisé
				s.supprimerRichesses(com.getNumero(), Messages.MINERAI, minerai * nbbis, Integer.MIN_VALUE);
				// et des marchandises utilisées
				int[][] m2 = Construction.getMarchandises(code);
				if (m2 != null)
					for (int j = 0; j < m2.length; j++)
						supprimerMarchandise(m2[j][0], m2[j][1] * nbbis);

				// Gestion du gouverneur (XP)
				if (com.existenceGouverneurSurPossession(s.getPosition())){
					int iteration = (int) (prix / 10 + minerai + pc / 10) * nbbis;
					for (int a = 0; a < iteration; a++){
						com.getGouverneurSurPossession(s.getPosition()).augmenterExperience();
					}
				}
				
				// Si c'est un batiment
				if (!isVaisseaux) {

					ObjetComplexeTransporte objet = new ObjetComplexeTransporte( code);
					for (int j = 0; j < nbbis; j++){
						objet.ajouterObjet(new ConstructionPlanetaire(code));
					}
					s.ajouterRichesses(com.getNumero(), objet, c[i].getPlanete());

				} else { // Sinon un vso

					Flotte flotte = new Flotte(s.getNom(), s.getPosition());

					PlanDeVaisseau plan = Univers.getPlanDeVaisseau(code);

					int race = s.getRaceMajoritaire(com.getNumero());
					//int planete = c[i].getPlanete();
					//s.getPlanete(planete).getp
					
					// Pour les cyborg
					if (com.getRace() == 5 && !plan.possedeCaracteristiqueSpeciale(Const.COMPOSANT_CAPACITE_COLONISATEUR)) {
						race = 5;
					}
					// Gestion des royalties
					if ((plan.getRoyalties() != 0) && (plan.concepteurExistant())) {
						Commandant beneficiaire = Univers.getCommandant(plan.getNumeroConcepteur());
						float benefice = (plan.getRoyalties() * c[i].getPrix()) / (100 + plan.getRoyalties());
						beneficiaire.modifierBudget( Const.BUDGET_COMMANDANT_PERCEPTION_ROYALTIES, benefice);
					}

					// On prend celle qui a le plus bas numéro, sinon on en
					// créé une nouvelle
					for (int j = 0; j < nbbis; j++)
						flotte.ajouterVaisseau(Vaisseau.creer(code, race));

					// On cherche si il y a une flotte sur le système
					Flotte[] f = com.listeFlottes();
					boolean presence = false;
					for (int e = 0; e < f.length; e++) {
						if (f[e].getPosition().equals(s.getPosition())) {
							f[e].fusion(flotte);
							presence = true;
							break;
						}
					}
					if (!presence)
						com.ajouterFlotte(flotte);
				}

				// On réduis le nombre de construction
				if (c[i].getNombre() > 0)
					c[i].diminuerNombre(nbbis);

				// Et si il ne reste plus que 0 construction dedans, on la vire
				if (c[i].getNombre() < 1)
					constructions.remove(c[i]);

				// On modifie les points effectués pour ne pas avoir de
				// construction négative
				if (c[i].getNombre() > 0)
					c[i].setPointsEffectues(c[i].getPointsEffectues() - nbbis
							* pc);

				// On envoi le log de la construction
				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises) {
					String nombreConstruit = nbbis + "/" + nb;
					StringBuffer manque = new StringBuffer();
					if (pasAssezDeCentaure) manque.append(" centaure, ");
					if (pasAssezDeMinerai) manque.append(" " + Messages.MINERAI + ", ");
					if (pasAssezDeMarchandises) manque.append(" marchandise (" + marchandises_manquantes.toString() + ")");

					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0002",
							s.getPosition(), nombreConstruit,
							descriptionTechno, manque);

				} else { // Sinon la construction est OKAY
					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0001",
							s.getPosition(), descriptionTechno, nbbis);
				}

			} else { // Si jamais il n'y a aucune construction ( nbbis == 0 )
				
				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises) {
					String nombreConstruit = ""+nb;
					StringBuffer manque = new StringBuffer();
					if (pasAssezDeCentaure) manque.append(" centaure, ");
					if (pasAssezDeMinerai) manque.append(" " + Messages.MINERAI + ", ");
					if (pasAssezDeMarchandises) manque.append(" marchandise (" + marchandises_manquantes.toString() + "),");
					if (pasAssezDePlace) manque.append(" d'espace libre ");

					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0003", s.getPosition(), nombreConstruit, descriptionTechno, manque);

				} 
			}

		}
	}

	// autres méthodes.

	public void transfererPossession(Possession p) {
		if (p == null) {
			System.out.println("Possession inexistante");
			System.exit(5);
		}
		int[] l = p.listeNumerosMarchandises();
		for (int i = 0; i < l.length; i++)
			ajouterMarchandise(l[i], p.getQuantiteMarchandise(l[i]) / 2);
	}

	public static void intialiserpossession(Possession p) {
		Construction[] c = p.listeConstructions();
		if (c.length != 0) {
			p.initialiserConstructions();
			p.initialiserProgrammationConstruction();
		}

		p.initialiserBudget();
	}

}
