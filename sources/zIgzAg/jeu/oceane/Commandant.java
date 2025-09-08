// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import zIgzAg.utile.Mdt;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.io.Serializable;

public class Commandant extends Joueur implements Serializable {

    static final long serialVersionUID = -5043427151493459788L;

    private transient Commentaire evenement;

    private transient Commentaire erreur;

    private transient Commentaire combat;

    private transient Commentaire ordres;

    private Position capitale;

    private Map<Position, Possession> domaine;

    private TreeMap<Integer, Flotte> flottes;

    private HashMap<String, int[]> recherches;

    private int reputation;

    private float centaures;

    private int tauxTaxationPoste;

    private ArrayList<Integer> alliances;

    private ArrayList<Integer> pactesDeNonAgression;

    private ArrayList<String> technologiesConnues;

    private ArrayList<String> plansDeVaisseaux;

    private ArrayList<Heros> heros;

    private ArrayList<Gouverneur> gouverneurs;

    private ArrayList<Marchand> marchands;

    private HashMap<String, StrategieDeCombatSpatial> strategies;

    private transient TreeMap<Integer, Float> budget;

    private transient ArrayList<Position> positionsEspionnees;

    private transient ArrayList<Position> systemesDetectes;
    private transient HashMap<Alliance, ArrayList<int[][]>> listeDetectionsFlotteAlliance;

    private transient ArrayList<int[]> flottesDetectees;

    private transient ArrayList<Transfert> transfertsEnAttentes;

    private transient HashMap<Integer, Integer> correspondanceFlotteDivisee;

    // pour contrer la "spéculation"
    private transient Map<Position, Map<Integer, Set<Integer>>> speculation;

    private void ajouterVisaDechargement(Position p, int numeroCommandant,
                                         int numeroMarchandise) {
        if (speculation == null)
            speculation = new HashMap<>();
        Map<Integer, Set<Integer>> m = speculation.computeIfAbsent(p, k -> new HashMap<>());
        Set<Integer> s = m.computeIfAbsent(numeroCommandant, k -> new HashSet<>());
        s.add(numeroMarchandise);
    }

    public static boolean testNeutre(Commandant c) {
        return c.estJoueurNeutre();
    }

    private boolean existenceVisaDechargement(Position p, int numeroCommandant,
                                              int numeroMarchandise) {
        if (speculation == null)
            return false;
        Map<Integer, Set<Integer>> m = speculation.get(p);
        if (m == null)
            return false;
        Set<Integer> s = m.get(numeroCommandant);
        if (s == null)
            return false;
        return s.contains(numeroMarchandise);
    }

    // les méthodes d'accés.

    public void setCapitale(Position pos) {
        capitale = pos;
    }

    public boolean existenceCapitale() {
        if (capitale == null)
            return false;
        else
            return true;
    }

    public boolean estCapitale(Position pos) {
        if (capitale == null)
            return false;
        else
            return capitale.equals(pos);
    }

    public void supprimerCapitale() {
        capitale = null;
    }

    public Position getCapitale() {
        return capitale;
    }

    public int getTauxTaxationPoste() {
        return tauxTaxationPoste;
    }

    public void setReputation(int rep) {
        reputation = rep;
    }

    public void setCentaures(float fric) {
        centaures = fric;
    }

    public void setTauxTaxationPoste(int e) {
        tauxTaxationPoste = e;
    }

    public float getCentaures() {
        return centaures;
    }

    public boolean estRuine() {
        return centaures < 0F;
    }

    public void ajouterReputation(int ajout) {
        reputation = reputation + ajout;
    }

    public int getReputation() {
        return reputation;
    }

    public int getStatutReputationIndex() {
        if (reputation < -10000)
            return 0; // méchant extrême
        else if (reputation < -5000)
            return 1; // mechant moyen
        else if (reputation < -1000)
            return 2; // méchant
        else if (reputation < 1000)
            return 3; // neutre
        else if (reputation < 5000)
            return 4; // bon moyen
        else
            return 5; // bon extrême
    }

    public String getStatutReputation() {
        return Univers.getMessage("REPUTATION", getStatutReputationIndex(),
                getLocale());
    }

    public void initialiserFlottes() {
        flottes = new TreeMap<>();
    }

    public void initialiserBudget() {
        budget = new TreeMap<>();
    }

    public void initialiserDomaine() {
        domaine = new TreeMap<>();
    }

    public void initialiserDomainesDeRecherche() {
        recherches = new HashMap<>(11);
    }

    public void initialiserAlliances() {
        alliances = new ArrayList<>(2);
    }

    public void initialiserHeros() {
        heros = new ArrayList<>(0);
    }

    public void initialiserGouverneurs() {
        gouverneurs = new ArrayList<>(0);
    }

    public void initialiserMarchands() {
        marchands = new ArrayList<>(0);
    }

    public void initialiserPactesDeNonAgression() {
        pactesDeNonAgression = new ArrayList<>(0);
    }

    public void initialiserTechnologiesConnues() {
        technologiesConnues = new ArrayList<>(0);
    }

    public void initialiserPlansDeVaisseaux() {
        plansDeVaisseaux = new ArrayList<>(0);
    }

    public void initialiserPositionsEspionnees() {
        positionsEspionnees = new ArrayList<>(0);
    }

    public void initialiserStrategies() {
        strategies = new HashMap<>(11);
    }

    public void initialiserSystemesDetectes() {
        systemesDetectes = new ArrayList<>();
    }

    public void initialiserFlottesDetectees() {
        flottesDetectees = new ArrayList<>();
    }

    public void initialiserCorrespondanceFlotteDivisee() {
        correspondanceFlotteDivisee = new HashMap<>(11);
    }

    public float getBudget(int index) {
        Float o = budget.get(index);
        if (o == null)
            return 0F;
        else
            return o;
    }

    public void setBudget(int index, float somme) {
        budget.put(index, somme + getBudget(index));
    }

    public void modifierBudget(int index, float somme) {
        setBudget(index, somme);
        centaures = centaures + somme;
    }

    public StrategieDeCombatSpatial getStrategie(String code) {
        StrategieDeCombatSpatial o = strategies.get(code);
        if (StrategieDeCombatSpatial.estStrategieParDefaut(code) || (o == null))
            return Const.STRATEGIE_DEFAUT;
        else
            return o;
    }

    public boolean connaitStrategie(String code) {
        return strategies.containsKey(code);
    }

    public String[] listeCodesStrategies() {
        return strategies.keySet().toArray(new String[0]);
    }

    public void ajouterStrategie(StrategieDeCombatSpatial ajout) {
        strategies.put(ajout.getNom(), ajout);
    }

    // Liste des noms des équipements des systèmes sous forme d'un arraylist
    public ArrayList<String> listeEquipementArray() {
        ArrayList<String> rtr = new ArrayList<>(50);

        Position[] p = listePossession();
        for (int i = 0; i < p.length; i++)
            rtr.addAll(Univers.getSysteme(p[i]).listeEquipementArray(numero));

        return rtr;
    }

    public Map<String, Integer> listeEquipement() {
        Position[] p = listePossession();
        HashMap<String, Integer> h = new HashMap<>(11);
        for (int i = 0; i < p.length; i++) {
            Map.Entry<String, Integer>[] m = Univers.getSysteme(p[i]).listeEquipement(numero);
            for (int j = 0; j < m.length; j++) {
                Integer o = h.get(m[j].getKey());
                if (o == null)
                    h.put(m[j].getKey(), m[j].getValue());
                else
                    h.put(m[j].getKey(), o + m[j].getValue());
            }
        }
        return h;
    }

    public Map<String, Integer> listeVaisseauxTriee() {
        HashMap<String, Integer> h = new HashMap<>();
        Flotte[] f = listeFlottes();
        for (int i = 0; i < f.length; i++) {
            Vaisseau[] v = f[i].listeVaisseaux();
            for (int j = 0; j < v.length; j++) {
                Integer o = h.get(v[j].getType());
                if (o == null)
                    h.put(v[j].getType(), 1);
                else
                    h.put(v[j].getType(), o + 1);
            }
        }
        return h;
    }

    public ObjetTransporte[] listeCargaisonTransporteeTriee() {
        Flotte[] f = listeFlottes();
        ArrayList<ObjetTransporte> retour = new ArrayList<>();
        for (int i = 0; i < f.length; i++) {
            ObjetTransporte[] inter = f[i].listeCargaisonTransporteTriee();
            for (int j = 0; j < inter.length; j++) {
                boolean dejaVu = false;
                for (int k = 0; k < retour.size(); k++)
                    if (inter[j].estDeType(retour.get(k)
                            .getCode())) {
                        retour.get(k).ajout(inter[j]);
                        dejaVu = true;
                    }
                if (!dejaVu)
                    retour.add((ObjetTransporte) inter[j].clone());
            }
        }
        return retour.toArray(new ObjetTransporte[0]);
    }

    public void ajouterPossession(Position pos, Possession entree) {
        domaine.put(pos, entree);
    }

    public void eliminerPossession(Position pos) {
        domaine.remove(pos);
        if (estCapitale(pos))
            supprimerCapitale();
        if (existenceGouverneurSurPossession(pos))
            getGouverneurSurPossession(pos).setPosition(null);
    }

    public Possession getPossession(Position pos) {
        return domaine.get(pos);
    }

    public void transfererPossession(Position pos, Possession possession) {
        if (existencePossession(pos)) {
            Possession p = getPossession(pos);
            p.transfererPossession(possession);
        } else
            ajouterPossession(pos, possession);
    }

    public boolean existencePossession(Position pos) {
        return domaine.containsKey(pos);
    }

    public Position[] listePossession() {
        return (Position[]) domaine.keySet().toArray(new Position[0]);
    }

    public Possession[] listeVraiePossession() {
        return domaine.values().toArray(new Possession[0]);
    }

    public int getNombrePossessions() {
        return domaine.size();
    }

    public int getNombrePlanetesPossedees() {
        Position[] p = listePossession();
        int retour = 0;
        for (int i = 0; i < p.length; i++)
            retour = retour
                    + Univers.getSysteme(p[i]).getNombrePlanetesPossedees(
                    numero);
        return retour;
    }

    public String getGrade() {
        int n = getNombrePlanetesPossedees();
        int index = 0;
        if (n < 10)
            index = 0;
        else if (n < 20)
            index = 1;
        else if (n < 50)
            index = 2;
        else if (n < 100)
            index = 3;
        else if (n < 150)
            index = 4;
        else if (n < 200)
            index = 5;
        else if (n < 300)
            index = 6;
        else
            index = 7;
        return Univers.getMessage("GRADE", index, getLocale());
    }

    public int getPuissanceFlottes() {
        int retour = 0;
        Flotte[] f = listeFlottes();
        for (int i = 0; i < f.length; i++)
            retour = retour + f[i].getPuissance();
        return retour;
    }

    public int getPlusGrossePuissanceDeFlotte() {
        Flotte flo = getPlusGrosseFlotte();
        if (flo != null)
            return flo.getPuissance();
        else
            return -1;
    }

    public Flotte getPlusGrosseFlotte() {
        Flotte[] flottes;
        Flotte retour = null;
        int high;

        // Initialisation de puissance
        high = -1;
        // Liste des flottes
        flottes = listeFlottes();
        // Si le commandant posséde au moins une flotte
        if (flottes.length > 0) {
            // Recherche des puissances des flottes de c[i]
            for (int a = 0; a < flottes.length; a++) {
                if (flottes[a].getPuissance() > high) {
                    high = flottes[a].getPuissance();
                    retour = flottes[a];
                }
            }
        }

        return retour;

    }

    public int getPuissanceSystemes() {
        Position[] p = listePossession();
        int retour = 0;
        for (int i = 0; i < p.length; i++)
            retour = retour + Univers.getSysteme(p[i]).getPuissance(numero);
        return retour;
    }

    public int getValeurTotaleLeaders() {
        Leader[] l = listeLieutenants();
        float total = 0F;
        for (int i = 0; i < l.length; i++)
            total = total + l[i].getValeur();
        return (int) total;
    }

    public int getPuissance() {
        return 10 * getPuissanceFlottes() + getPuissanceSystemes()
                + nombreTechnologiesNonPubliquesConnues() * 100
                + getValeurTotaleLeaders() + (int) centaures;
    }

    public int getNombrePopulationDeRace(int r) {
        int retour = 0;
        Position[] p = listePossession();
        for (int i = 0; i < p.length; i++)
            retour = retour
                    + Univers.getSysteme(p[i]).getPopulationDeRace(numero, r);
        return retour;
    }

    public int getNombrePopulationMaxDeRace(int r) {
        int retour = 0;
        Position[] p = listePossession();
        for (int i = 0; i < p.length; i++)
            retour = retour
                    + Univers.getSysteme(p[i]).getPopulationMaximaleDeRace(
                    numero, r);
        return retour;
    }

    public int getPopulationTotale() {
        int retour = 0;
        Position[] p = listePossession();
        for (int i = 0; i < p.length; i++)
            retour = retour + Univers.getSysteme(p[i]).getPopulation(numero);
        return retour;
    }

    public int getPopulationTotaleMaximale() {
        int retour = 0;
        Position[] p = listePossession();
        for (int i = 0; i < p.length; i++)
            retour = retour
                    + Univers.getSysteme(p[i]).getPopulationMaximale(numero);
        return retour;
    }

    private float getBudgetDepense(int typeBudget) {
        float retour = 0F;
        Position[] liste = listePossession();
        for (int i = 0; i < liste.length; i++) {
            Possession p = getPossession(liste[i]);
            float t = 0F;
            t = Univers.getSysteme(liste[i]).getRevenu(numero,
                    getGouverneurSurPossession(liste[i]), p)
                    * p.getBudget(typeBudget);
            retour = retour + t;
        }
        return retour / 100F + getBudgetFlottes(typeBudget);
    }

    private float getBudgetPossession(int typeBudget) {
        float retour = 0F;
        Position[] liste = listePossession();
        for (int i = 0; i < liste.length; i++) {
            Possession p = getPossession(liste[i]);
            float t = 0F;
            if (typeBudget == Const.DOMAINES_BUDGET_TECHNOLOGIQUE) {
                t = Univers.getSysteme(liste[i]).getRevenuTechnologique(numero,
                        getGouverneurSurPossession(liste[i]), p)
                        * p.getBudget(typeBudget);

                // bonus commandant
                t *= (float) (100 + Const.RACES_CARACTERISTIQUES[getRace()][Const.RACE_CARACTERISTIQUE_BONUS_TECHNOLOGIQUE]) / 100;
            }
            else
                t = Univers.getSysteme(liste[i]).getRevenu(numero,
                        getGouverneurSurPossession(liste[i]), p)
                        * p.getBudget(typeBudget);

            if ((typeBudget == Const.DOMAINES_BUDGET_TECHNOLOGIQUE)
                    && (p.possedeStockImportantPoste(Const.PRODUIT_LOGICIEL)))
                t = t + (25 * t) / 100F;
            if (((typeBudget == Const.DOMAINES_BUDGET_SERVICES_SPECIAUX) || (typeBudget == Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE))
                    && (p.possedeStockImportantPoste(Const.PRODUIT_COMPOSANT_ELECTRONIQUE)))
                t = t + (25 * t) / 100F;

            retour = retour + t;
        }

        return retour / 100F + getBudgetFlottes(typeBudget);
    }

    public float getBudgetFlottes(int typeBudget) {
        float retour = 0F;

        if (getRace() == 5) {
            Flotte[] flottes = listeFlottes();
            for (int i = 0; i < flottes.length; i++) {
                if (typeBudget == Const.DOMAINES_BUDGET_TECHNOLOGIQUE)
                    retour += flottes[i]
                            .entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO);
                else if (typeBudget == Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE)
                    retour += flottes[i]
                            .entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE);
                else if (typeBudget == Const.DOMAINES_BUDGET_SERVICES_SPECIAUX)
                    retour += flottes[i]
                            .entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE);
            }
        }
        return retour;
    }

    public float getBudgetServiceSpeciaux() {
        return getBudgetPossession(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX);
    }

    public float getBudgetTechnologique() {
        return getBudgetPossession(Const.DOMAINES_BUDGET_TECHNOLOGIQUE);
    }

    public float getBudgetContreEspionnage() {
        return getBudgetPossession(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE);
    }

    public float getEntretienLieutenants() {
        float retour = 0F;
        Leader[] l = listeLieutenants();
        for (int i = 0; i < l.length; i++)
            retour = retour + l[i].getEntretien();
        return retour;
    }

    public Leader[] listeLieutenants() {
        Heros[] h = listeHeros();
        Gouverneur[] g = listeGouverneur();
        Marchand[] m = listeMarchand();
        Leader[] l = new Leader[h.length + g.length + m.length];
        for (int i = 0; i < h.length; i++)
            l[i] = h[i];
        for (int i = h.length; i < h.length + g.length; i++)
            l[i] = g[i - h.length];
        for (int i = h.length + g.length; i < h.length + g.length + m.length; i++)
            l[i] = m[i - (h.length + g.length)];
        return l;
    }

    public Leader getLieutenant(String nomLieutenant) {
        Leader[] h = listeLieutenants();
        for (int i = 0; i < h.length; i++)
            if (h[i].getNom().equals(nomLieutenant))
                return h[i];
        return null;
    }

    public boolean existenceLieutenant(String nomLieutenant) {
        return getLieutenant(nomLieutenant) != null;
    }

    public void supprimerLieutenant(String nomLieutenant) {
        Leader l = getLieutenant(nomLieutenant);
        if (l instanceof Heros)
            heros.remove(l);
        else if (l instanceof Gouverneur)
            gouverneurs.remove(l);
        else
            marchands.remove(l);
    }

    // HEROS

    public void ajouterHeros(Heros h) {
        heros.add(h);
    }

    public Heros getHeros(String nomHeros) {
        Heros[] h = listeHeros();
        for (int i = 0; i < h.length; i++)
            if (h[i].getNom().equals(nomHeros))
                return h[i];
        return null;
    }

    public boolean existenceHeros(String nomHeros) {
        return getHeros(nomHeros) != null;
    }

    public static Heros[] listeHerosTotale() {
        Commandant[] cL = Univers.getListeCommandantsHumains();
        int nbleader = 0;
        for (int i = 0; i < cL.length; i++) {
            Heros[] joueur = cL[i].listeHeros();
            nbleader = nbleader + joueur.length;
        }
        nbleader = (int) nbleader;
        System.out.print(nbleader);
        Heros[] H = new Heros[nbleader];
        int a = 0;
        int index = 0;
        for (int i = 0; i < cL.length; i++) {
            System.out.print(cL[i]);
            System.out.print("");
            Heros[] joueur = cL[i].listeHeros();
            for (a = index; a < joueur.length + index; a++) {
                System.out.print(joueur[a - index].getNom() + " / ");
                System.out.print("");
                H[a] = joueur[a - index];
            }
            index = a;
        }
        return H;
    }

    public Heros[] listeHeros() {
        return (Heros[]) heros.toArray(new Heros[0]);
    }

    public boolean existenceHerosSurFlotte(int num) {
        Heros[] h = listeHeros();
        for (int i = 0; i < h.length; i++)
            if (h[i].getPosition() == num)
                return true;
        return false;
    }

    public Heros getHerosSurFlotte(int num) {
        Heros[] h = listeHeros();
        for (int i = 0; i < h.length; i++)
            if (h[i].getPosition() == num)
                return h[i];
        return null;
    }

    // FIN HERO

    // GOUVERNEUR

    public Gouverneur getGouverneur(String nomGouverneur) {
        Gouverneur[] h = listeGouverneur();
        for (int i = 0; i < h.length; i++)
            if (h[i].getNom().equals(nomGouverneur))
                return h[i];
        return null;
    }

    public boolean existenceGouverneur(String nomGouverneur) {
        return getGouverneur(nomGouverneur) != null;
    }

    public void ajouterGouverneur(Gouverneur g) {
        gouverneurs.add(g);
    }

    public Gouverneur[] listeGouverneur() {
        return (Gouverneur[]) gouverneurs.toArray(new Gouverneur[0]);
    }

    public boolean existenceGouverneurSurPossession(Position pos) {
        Gouverneur[] h = listeGouverneur();
        for (int i = 0; i < h.length; i++)
            if (pos.equals(h[i].getPosition()))
                return true;
        return false;
    }

    public Gouverneur getGouverneurSurPossession(Position pos) {
        Gouverneur[] h = listeGouverneur();
        for (int i = 0; i < h.length; i++)
            if (pos.equals(h[i].getPosition()))
                return h[i];
        return null;
    }

    // FIN GOUVERNEUR

    // MARCHANDS

    public Marchand[] listeMarchand() {
        if (marchands == null)
            initialiserMarchands();
        return (Marchand[]) marchands.toArray(new Marchand[0]);
    }

    public void ajouterMarchand(Marchand m) {
        marchands.add(m);
    }

    public boolean existenceMarchand(String nomMarchand) {
        return getMarchand(nomMarchand) != null;
    }

    public Marchand getMarchand(String nomMarchand) {
        Marchand[] m = listeMarchand();
        for (int i = 0; i < m.length; i++)
            if (m[i].getNom().equals(nomMarchand))
                return m[i];
        return null;
    }

    // FIN MARCHANDS

    public Flotte[] listeFlottes() {
        return flottes.values().toArray(new Flotte[0]);
    }

    public Map<Integer, Flotte> clonerListeFlottes() {
        return (Map<Integer, Flotte>) flottes.clone();
    }

    @SuppressWarnings("unchecked")
    public Map.Entry<Integer, Flotte>[] listeFlottesEtNumeros() {
        return flottes.entrySet().toArray(new Map.Entry[0]);
    }

    @SuppressWarnings("unchecked")
    public Map.Entry<Integer, Flotte>[] listeFlottesEtNumerosCargosSurPoste() {
        Map.Entry<Integer, Flotte>[] m = listeFlottesEtNumeros();
        ArrayList<Map.Entry<Integer, Flotte>> a = new ArrayList<>(m.length);
        for (int i = 0; i < m.length; i++)
            if ((m[i].getValue()).estTransporteur())
                if (Univers.existenceSysteme((m[i].getValue())
                        .getPosition()))
                    a.add(m[i]);
        return a.toArray(new Map.Entry[0]);
    }

    public Integer[] listeNumerosFlottes2() {
        return flottes.keySet().toArray(new Integer[0]);
    }

    public int[] listeNumerosFlottes() {
        return Utile.transformer(listeNumerosFlottes2());
    }

    public int numeroFlotte(Flotte f) {
        Flotte[] lf = listeFlottes();
        Integer[] ln = listeNumerosFlottes2();
        for (int i = 0; i < lf.length; i++)
            if (lf[i].equals(f))
                return ln[i].intValue();
        return -1;
    }

    public int numeroFlotteDisponible() {
        int[] travail = listeNumerosFlottes();
        for (int i = 0; i < travail.length; i++)
            if (travail[i] != i)
                return i;
        return travail.length;
    }

    public boolean existenceFlotte(int num) {
        return flottes.containsKey(num);
    }

    public void ajouterFlotte(Flotte entree) {
        flottes.put(numeroFlotteDisponible(), entree);
    }

    public void eliminerFlotte(int num) {
        flottes.remove(num);
        if (existenceHerosSurFlotte(num))
            getHerosSurFlotte(num).setPosition(-1);
    }

    public Flotte getFlotte(int num) {
        return flottes.get(num);
    }

    public void setFlotte(Flotte f, int num) {
        flottes.put(num, f);
    }

    public int getNombreDeFlottes() {
        return flottes.size();
    }

    public boolean nombreAlliancesTropGrand() {
        return false;
        // if(alliances.size()>=Const.NB_MAX_ALLIANCES) return true;
        // else return false;
    }

    public void ajouterAlliance(int numeroAlliance) {
        alliances.add(numeroAlliance);
    }

    public void enleverAlliance(int numeroAlliance) {
        Alliance a = Univers.getAlliance(numeroAlliance);
        if (a.estDirigeePar(numero))
            a.setDirigeant(-1);
        alliances.remove(Integer.valueOf(numeroAlliance));
    }

    public int[] numerosAlliances() {
        return Utile.transformer(alliances.toArray(new Integer[0]));
    }

    public boolean appartientAAlliance(int numeroAlliance) {
        return alliances.contains(numeroAlliance);
    }

    public int[] getAllianceDirigee() {
        int[] a = numerosAlliances();
        List<Integer> l = new ArrayList<>(a.length);
        for (int i = 0; i < a.length; i++)
            if (Univers.getAlliance(a[i]).estDirigeePar(numero))
                l.add(a[i]);
        return Utile.transformer(l.toArray(new Integer[0]));
    }

    public boolean estDirigeantAlliance() {
        return (getAllianceDirigee().length != 0);
    }

    public int getPlaceAlliance(int numeroAlliance) {
        return alliances.indexOf(numeroAlliance);
    }

    /*
     * public boolean nombreDomainesDeRechercheTropGrand(){
     * if(recherches.size()>=Const.NB_MAX_RECHERCHES) return true; else return
     * false; }
     */
    public boolean existenceDomaineDeRecherche(String code) {
        return recherches.containsKey(code);
    }

    public int totalAffectationPourcentage() {
        int retour = 0;
        int[][] t = (int[][]) recherches.values().toArray(new int[0][0]);
        for (int i = 0; i < t.length; i++)
            retour = retour + t[i][0];
        return retour;
    }

    public void suppressionDomaineDeRecherche(String code) {
        recherches.remove(code);
    }

    public int nombreDePointsDeRecherche(String code) {
        return ((int[]) recherches.get(code))[1];
    }

    public void ajouterPointsDeRecherche(String code, int nb) {
        creationDomaineDeRecherche(code, pourcentageAffecte(code),
                nombreDePointsDeRecherche(code) + nb);
    }

    public int pourcentageAffecte(String code) {
        return ((int[]) recherches.get(code))[0];
    }

    public boolean ajouterDomaineDeRecherche(String code, int pourcentage) {
        if (existenceDomaineDeRecherche(code)) {
            creationDomaineDeRecherche(code, pourcentage,
                    nombreDePointsDeRecherche(code));
        } else
            creationDomaineDeRecherche(code, pourcentage, 0);
        return true;
    }

    public String[] recherchesActuelles() {
        return (String[]) recherches.keySet().toArray(new String[0]);
    }

    public void creationDomaineDeRecherche(String code, int pourcentage,
                                           int pointsDejaTrouves) {
        int[] carac = new int[2];
        carac[0] = pourcentage;
        carac[1] = pointsDejaTrouves;
        recherches.put(code, carac);
    }

    public void ajouterPacteDeNonAgression(int numeroJoueur) {
        pactesDeNonAgression.add(numeroJoueur);
    }

    public boolean existencePacteDeNonAgression(int numeroJoueur) {
        return pactesDeNonAgression.contains(numeroJoueur);
    }

    public void romprePacteDeNonAgression(int numeroJoueur) {
        pactesDeNonAgression.remove(Integer.valueOf(numeroJoueur));
    }

    public int[] listePactesDeNonAgression() {
        return Utile.transformer(pactesDeNonAgression
                .toArray(new Integer[0]));
    }

    public String[] listeTechnologiesPouvantEtreCherchees() {
        return Technologie
                .listeDesTechnologiesAtteignables(listeTechnologiesConnues());
    }

    public boolean peutChercherTechnologie(String code) {
        return Mdt.estPresent(listeTechnologiesPouvantEtreCherchees(), code);
    }

    public void ajouterTechnologieConnue(String code) {
        if (code != null) {
            if (!estTechnologieConnue(code))
                technologiesConnues.add(code);
        }
        // nécessaire pour éliminer des recherches éventuelles sur cette
        // technologie ->
        suppressionDomaineDeRecherche(code);

    }

    public boolean estTechnologieConnue(String code) {
        return technologiesConnues.contains(code)
                || Univers.estTechnologiePublique(code);
    }

    public void eliminerTechnologieConnue(String code) {
        technologiesConnues.remove(code);
    }

    public String[] listeTechnologiesNonPubliquesConnues() {
        return technologiesConnues.toArray(new String[0]);
    }

    public String[] listeTechnologiesConnues() {
        return Mdt.fusion(
                Univers.getListeCodeTechnologiePubliques(),
                technologiesConnues.toArray(new String[0]));
    }

    public int nombreTechnologiesNonPubliquesConnues() {
        return technologiesConnues.size();
    }

    public float getEntretienTechnologies() {
        return 0;
    }

    public float getEntretienNombrePlanete() {
        int compteur = getNombrePlanetesPossedees();
        return compteur * getBudget(Const.BUDGET_COMMANDANT_REVENUS_SYSTEMES)
                / (Const.NB_SYSTEME * 2);
    }

    public void ajouterPlanDeVaisseau(String code) {
        plansDeVaisseaux.add(code);
    }

    public void supprimerPlanDeVaisseau(String code) {
        plansDeVaisseaux.remove(code);
    }

    public String[] listePlansPrives() {
        return plansDeVaisseaux.toArray(new String[0]);
    }

    public PlanDeVaisseau[] listePlansConnusNonPublics() {
        ArrayList<PlanDeVaisseau> a = new ArrayList<>();
        PlanDeVaisseau[] p = Univers.listePlansDeVaisseaux();
        for (int i = 0; i < p.length; i++)
            if ((!p[i].estPublic()) && (estPlanDeVaisseauConnu(p[i])))
                a.add(p[i]);
        return a.toArray(new PlanDeVaisseau[0]);
    }

    public String[] listePlansConnus() {
        PlanDeVaisseau[] p = Univers.listePlansDeVaisseaux();
        ArrayList<String> retour = new ArrayList<>(p.length);
        for (int i = 0; i < p.length; i++)
            if (estPlanDeVaisseauConnu(p[i]))
                retour.add(p[i].getNom());
        return retour.toArray(new String[0]);
    }

    public boolean estPlanDeVaisseauConnu(PlanDeVaisseau p) {
        if (plansDeVaisseaux.contains(p.getNom()))
            return true;
        if (p.estConcepteur(numero))
            return true;
        if (p.estPublic())
            return true;
        if (p.estDAlliance())
            return appartientAAlliance(p.getPrecisionEtat());
        if (p.estRacial())
            return (race == p.getPrecisionEtat());
        return false;
    }

    public boolean estPlanDeVaisseauConnu(String p) {
        if (Univers.existencePlanDeVaisseau(p))
            return estPlanDeVaisseauConnu(Univers.getPlanDeVaisseau(p));
        else
            return false;
    }

    public boolean estPlanDeVaisseauConnuPrive(String code) {
		return plansDeVaisseaux.contains(code);
	}

	public String[] listeBatimentsConnus() {
		String[] listeTechno = listeTechnologiesConnues();
		ArrayList<String> retour = new ArrayList<>(listeTechno.length);
		for (int i = 0; i < listeTechno.length; i++)
			if (Univers.getTechnologie(listeTechno[i]).estBatiment())
				retour.add(listeTechno[i]);
		String[] liste = retour.toArray(new String[0]);
		Arrays.sort(liste);
		return liste;
	}

	public String[] listeDesMisesEnChantierPossibles() {
		String[] b = listeBatimentsConnus();
		String[] p = listePlansConnus();
		
		ArrayList<String> retour = new ArrayList<>(b.length + p.length);
		retour.addAll(Arrays.asList(b));
		retour.addAll(Arrays.asList(p));
		return retour.toArray(new String[0]);
	}

	public String[] listeNomsDesMisesEnChantierPossibles() {
		Technologie[] b = Technologie.transformationCode(listeBatimentsConnus());
		String[] v = new String[b.length];
		for (int i = 0; i < v.length; i++)
			v[i] = b[i].getNomComplet(getLocale());
		String[] p = listePlansConnus();
		ArrayList<String> retour = new ArrayList<>(v.length + p.length);
		retour.addAll(Arrays.asList(v));
		retour.addAll(Arrays.asList(p));
		return retour.toArray(new String[0]);
	}

	public boolean peutMettreEnChantier(String code) {
		return Mdt.estPresent(listeDesMisesEnChantierPossibles(), code);
	}

	public void ajouterPositionEspionnee(Position pos) {
		positionsEspionnees.add(pos);
	}

	public Position[] getPositionsEspionnees() {
		return positionsEspionnees.toArray(new Position[0]);
	}

	public Position[] getSystemesDetectes() {
		return systemesDetectes.toArray(new Position[0]);
	}

	public ArrayList<Position> getSystemesDetectesArrayList() {
		return systemesDetectes;
	}

	public ArrayList<int[]> getFlottesDetecteesArrayList() {
		return flottesDetectees;
	}
	
	public int[][] getFlottesDetectees() {
		return flottesDetectees.toArray(new int[0][0]);
	}

	public void determinerSystemesDetectes() {
		Position[] l = Univers.listePositionsSystemes();
		Position[] p = listePossession();
		for (int i = 0; i < p.length; i++) {
			Position[] r = Position.getPositionsADistance(p[i], l, Univers
					.getSysteme(p[i]).getPorteeRadar(numero));
			for (int j = 0; j < r.length; j++)
				if ((!systemesDetectes.contains(r[j]))
						&& (!existencePossession(r[j])))
					systemesDetectes.add(r[j]);
		}
		Flotte[] f = listeFlottes();
		for (int i = 0; i < f.length; i++) {
			Position[] r = Position.getPositionsADistance(f[i].getPosition(),
					l, f[i].getPorteeScannerSysteme());
			for (int j = 0; j < r.length; j++)
				if ((!systemesDetectes.contains(r[j]))
						&& (!existencePossession(r[j])))
					systemesDetectes.add(r[j]);
		}
		Collections.sort(systemesDetectes);
	}

	public void determinerFlottesDetectes() {
		HashMap<int[], Flotte> h = Univers.listeFlottes();
		@SuppressWarnings("unchecked")
		Map.Entry<int[], Flotte>[] m = h.entrySet().toArray(new Map.Entry[0]);

		Position[] p = listePossession();
		for (int i = 0; i < p.length; i++) {
			int portee = Univers.getSysteme(p[i]).getPorteeRadar(numero);
			if (portee != 0)
				for (int j = 0; j < m.length; j++)
					if (Position.distance( ((Flotte) m[j].getValue()).getPosition(), p[i]) <= portee)
						if ((!flottesDetectees.contains(m[j].getKey())) && (numero != ((int[]) m[j].getKey())[0])){
							flottesDetectees.add(m[j].getKey());
						}
		}

		// Liste des flottes
		Flotte[] f = listeFlottes();
		for (int i = 0; i < f.length; i++) {
			int portee = f[i].getPorteeScannerFlotte();
			if (portee != 0)
				for (int j = 0; j < m.length; j++)
					if (Position.distance(
							((Flotte) m[j].getValue()).getPosition(),
							f[i].getPosition()) <= portee)
						if ((!flottesDetectees.contains(m[j].getKey())) && (numero != ((int[]) m[j].getKey())[0]))
							flottesDetectees.add(m[j].getKey());
		}
		
		// Brouillage
		for (int i = 0; i < flottesDetectees.size(); i++) {
			int[] inter = (int[]) flottesDetectees.get(i);
			Flotte flo = Univers.getCommandant(inter[0]).getFlotte(inter[1]);

			boolean remove = false;

			// yoksor
			if (getRace() == 3) {
				if (Univers.getTest(flo.getBrouillageRadar()))
					remove = true;
				else if (Univers.getTest(flo.getBrouillageRadar()))
					remove = true;
			} else {
				if (Univers.getTest(flo.getBrouillageRadar()))
					remove = true;
			}

			if (remove)
				flottesDetectees.remove(inter);
		}
	}

	public void determinerFlottesDetectesParAlliance(){
		
		for(int numAlliance:numerosAlliances()){
			Alliance alliance = Univers.getAlliance(numAlliance);
			
			ArrayList<int[]> liste = alliance.getListeflottesDetectees();
			for (int[] detect : liste) {
				if( !flottesDetectees.contains(detect) ){
					flottesDetectees.add(detect);
				}
			}
		}
	}
 	

	public void determinerSystemesDetectesParAlliance(){
		
		for(int numAlliance:numerosAlliances()){
			Alliance alliance = Univers.getAlliance(numAlliance);
			
			ArrayList<Position> liste = alliance.getListeSystemesDetectees();
			for (Position detect : liste) {
				if( !systemesDetectes.contains(detect) ){
					systemesDetectes.add(detect);
				}
			}
		}
	}
	
	public void ajouterCorrespondanceFlotte(int numeroCode, int numero) {
		correspondanceFlotteDivisee.put(numeroCode, numero);
	}

	public int getCorrespondanceFlotte(int numeroCode) {
		if (numeroCode < 10000)
			return numeroCode;
		Integer o = correspondanceFlotteDivisee.get(numeroCode);
		if (o == null)
			return -1;
		else
			return o;
	}

	public void initialiserListesMessages() {
		erreur = new Commentaire();
		evenement = new Commentaire();
		combat = new Commentaire();
		ordres = new Commentaire();
	}
	
	public void initialiserTransfertEntreSysteme(){
		transfertsEnAttentes = new ArrayList<>();
	}

	public void initialiserChampsTransients() {
		initialiserListesMessages();
		initialiserSystemesDetectes();
		initialiserFlottesDetectees();
		initialiserBudget();
		initialiserPositionsEspionnees();
		initialiserCorrespondanceFlotteDivisee();
		initialiserTransfertEntreSysteme();
	}

	// Champs transients --->

	public List<Object> stockerChampsTransients() {
		ArrayList<Object> l = new ArrayList<>();
		l.add(erreur);
		l.add(evenement);
		l.add(combat);
		l.add(ordres);
		l.add(budget);
		l.add(positionsEspionnees);
		l.add(systemesDetectes);
		l.add(flottesDetectees);
		l.add(correspondanceFlotteDivisee);
		return l;
	}

	@SuppressWarnings("unchecked")
	public void chargerChampsTransients(List<?> l) {
		erreur = (Commentaire) l.get(0);
		evenement = (Commentaire) l.get(1);
		combat = (Commentaire) l.get(2);
		ordres = (Commentaire) l.get(3);
		budget = (TreeMap<Integer, Float>) l.get(4);
		positionsEspionnees = (ArrayList<Position>) l.get(5);
		systemesDetectes = (ArrayList<Position>) l.get(6);
		flottesDetectees = (ArrayList<int[]>) l.get(7);
		correspondanceFlotteDivisee = (HashMap<Integer, Integer>) l.get(8);
	}

	public Commentaire getErreurs() {
		return erreur;
	}

	public Commentaire getEvenements() {
		return evenement;
	}

	public Commentaire getCombats() {
		return combat;
	}

	public Commentaire getOrdres() {
		return ordres;
	}

	// méthodes statiques

	public static String getListeCommandants(int[] l) {
		Commandant[] inter = new Commandant[l.length];
		for (int i = 0; i < l.length; i++)
			inter[i] = Univers.getCommandant(l[i]);
		return getListeCommandants(inter);
	}

	public static String getListeCommandants(Commandant[] l) {
		String retour = new String();
		for (int i = 0; i < l.length; i++) {
			retour = retour + l[i].getNomNumero();
			if (i != l.length - 1)
				retour = retour + ", ";
		}
		return retour;
	}

	// le constructeur.

	public Commandant() {
	}

	public Commandant(String n, int ra, int num, String adresse, String log,
			String mot, int tour) {
		super(n, ra, num, adresse, log, mot, tour);
		tauxTaxationPoste = 50;
		initialiserFlottes();
		initialiserDomaine();
		initialiserDomainesDeRecherche();
		initialiserAlliances();
		initialiserHeros();
		initialiserGouverneurs();
		initialiserMarchands();
		initialiserPactesDeNonAgression();
		initialiserTechnologiesConnues();
		initialiserPlansDeVaisseaux();
		initialiserStrategies();
		initialiserChampsTransients();
	}

	// Les méthodes de gestion de fin et de début de tour.

	public void resolutionCollisions() {
		Flotte[] f = listeFlottes();
		for (int i = 0; i < f.length; i++) {
			Debris d;
			if ((d = Univers.getDebris(f[i].getPosition())) != null)
				d.gererCollisions(this, f[i]);
		}
	}

	public void progressionNiveauLieutenant() {
		Leader[] l = listeLieutenants();
		for (int i = 0; i < l.length; i++)
			for (int j = 0; j < Univers.getInt(10); j++)
				l[i].augmenterExperience();
		for (int i = 0; i < l.length; i++)
			l[i].testerProgressionNiveau(this);
	}

	public void resolutionGestionFlottes() {
		Flotte[] f = listeFlottes();

		for (int i = 0; i < f.length; i++) {

			int[] inter = f[i].numeroPorteIntraGalactique();
			if (inter != null)
				ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0005",
						f[i].getNomNumero(numeroFlotte(f[i])),
						f[i].getPosition());
		}

		// résolution mdc de flotte
		for (int i = 0; i < f.length; i++)
			f[i].resolutionConstruction(this);
		// System.out.print("construF-");

		// résolution réparation des flottes
		for (int i = 0; i < f.length; i++)
			if (f[i].dommagesTotaux() > 0)
				if (Univers.existenceSysteme(f[i].getPosition())) {
					Systeme s = Univers.getSysteme(f[i].getPosition());
					int potentiel = s.getPotentielReparationFlotte(numero);
					boolean fait = false;
					if (potentiel != 0)
						if (getCentaures() > potentiel
								* Const.COUT_REPARATION_VAISSEAU) {
							int nbRepare = f[i].reparer(potentiel);
							modifierBudget(
									Const.BUDGET_COMMANDANT_REPARATION_FLOTTE,
									-nbRepare * Const.COUT_REPARATION_VAISSEAU);
							ajouterEvenement(
									"EV_COMMANDANT_GESTION_FLOTTE_0001",
									f[i].getNomNumero(numeroFlotte(f[i])),
									s.getPosition(), nbRepare);
							fait = true;
							s.setPointsRepare(numero, nbRepare);
						} else
							ajouterEvenement(
									"EV_COMMANDANT_GESTION_FLOTTE_0000",
									f[i].getNomNumero(numeroFlotte(f[i])));
					if (!fait) {
						int[] proprio = s.getProprios();
						for (int j = 0; j < proprio.length; j++)
							if ((!fait) && (proprio[j] != numero)) {
								Commandant commandant = Univers
										.getCommandant(proprio[j]);
								if (Combat.commandantsAllies(this, commandant)) {
									potentiel = s
											.getPotentielReparationFlotte(commandant
													.getNumero());
									if (potentiel != 0)
										if (commandant.getCentaures() > potentiel
												* Const.COUT_REPARATION_VAISSEAU) {
											int nbRepare = f[i]
													.reparer(potentiel);
											commandant
													.modifierBudget(
															Const.BUDGET_COMMANDANT_REPARATION_FLOTTE,
															-nbRepare
																	* Const.COUT_REPARATION_VAISSEAU);
											ajouterEvenement(
													"EV_COMMANDANT_GESTION_FLOTTE_0002",
													f[i].getNomNumero(numeroFlotte(f[i])),
													s.getPosition(), commandant
															.getNomNumero(),
													nbRepare);
											commandant
													.ajouterEvenement(
															"EV_COMMANDANT_GESTION_FLOTTE_0003",
															f[i].getNomNumero(numeroFlotte(f[i])),
															s.getPosition(),
															getNomNumero(),
															nbRepare);
											fait = true;
											s.setPointsRepare(
													commandant.getNumero(),
													nbRepare);
										} else
											ajouterEvenement(
													"EV_COMMANDANT_GESTION_FLOTTE_0004",
													f[i].getNomNumero(numeroFlotte(f[i])),
													commandant.getNomNumero());
								}
							}
					}

				}

		// collision interne hehe :)
		int militaire = Technologie.possedeMaitriseMilitaire(this);

		for (int i = 0; i < f.length; i++) {
			int dommages = 0;
			int degat_t = (int) (f[i].getNombreDeVaisseaux() / (45 + 5 * Univers.getInt(5)) - 2 * (militaire + 1));

			if (degat_t > 0) { 
				dommages = f[i].gererCollisionVaisseaux(Math.abs(degat_t * degat_t));

				if (dommages > 0) {
					ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0006",
							f[i].getNomNumero(numeroFlotte(f[i])),
							f[i].getPosition(), dommages);
					// System.out.println("collision effective : "+
					// dommages+"/"+Math.abs(degat_t * degat_t));
				}
			}
		}

		// Entretien flotte
		float entretien = 0F;
		for (int i = 0; i < f.length; i++) {
			boolean carburant = false;
			if ((existencePossession(f[i].getPosition()))
					&& (getPossession(f[i].getPosition())
							.possedeStockImportantPoste(Const.PRODUIT_CARBURANT)))
				carburant = true;
			entretien = entretien
					+ f[i].getEntretien(getHerosSurFlotte(numeroFlotte(f[i])),
							carburant);
		}
		
		if( getRace() == 4 ){
			entretien = entretien * 0.9f;
		}
		
		modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_FLOTTE, -entretien);

		// Ville spatiales
		for (int i = 0; i < f.length; i++) {
			modifierBudget(
					Const.BUDGET_REVENUS_VILLE_SPATIALE,
					f[i].entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE)
							+ f[i].entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_TECHNO)
							+ f[i].entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_CONTRE)
							+ f[i].entretienVilleSpatiale(Const.COMPOSANT_CAPACITE_VILLE_SPATIALE_ESPIONNAGE));
			f[i].augmenterPopulationVilleSpatiale();
		}

		// Location
		for (int i = 0; i < f.length; i++)
			if (f[i].estLoueEnPartie())
				f[i].retourLocation(this, Univers.getTour(), -1);
	}

	public void resolutionGestionSystemes() {
		Systeme[] s = Univers.listeSystemes(listePossession());
		System.out.print(getNumero() + "-");

		
		for (int i = 0; i < s.length; i++) {
			s[i].testerRevoltes(numero);

			if (s[i].getNombrePlanetesRevoltees(numero) != 0)
				ajouterEvenement("EV_COMMANDANT_FIN_DE_TOUR_0000",
						s[i].getPosition(),
						s[i].getNombrePlanetesRevoltees(numero));

			getPossession(s[i].getPosition()).miseAJourReputation(this);

			if (getPossession(s[i].getPosition()).aPolitiqueAnti())
				s[i].politiqueExtermination(this,
						getPossession(s[i].getPosition()).racePolitiqueAnti());

		}

		// gestion transfert
		if( transfertsEnAttentes.size()>0 ){
			int maxNbTransfert = getNombreMaximalDeTransfertEntreSysteme();
			int max = Math.min(maxNbTransfert, transfertsEnAttentes.size());
			
			int current = 0;
			for(Transfert t:transfertsEnAttentes){
				if( current < max ){
					if( transfererEntreSysteme(t.getDepart(), t.getCode(), t.getNombre(), t.getPlaneteDepart(), t.getArrivee(), t.getPlaneteArrivee()) ){
						current++;
					}
				} else {
					ObjetSimpleTransporte o = new ObjetSimpleTransporte(t.getCode(), t.getNombre());
					ajouterErreur(
							"ER_COMMANDANT_TRANSFERER_0007", 
							"<font color=\"#00f1af\">" + ObjetTransporte.getDescriptionListeChargement(new ObjetTransporte[] { o }, getLocale()) + "</font>", 
							t.getDepart(), t.getArrivee(),
							max);
				}
			}
		}
		

		if (numero != 0)
			for (int i = 0; i < s.length; i++)
				s[i].evolutionStabilite(numero, getCapitale(), getGouverneurSurPossession(s[i].getPosition()), getPossession(s[i].getPosition()));
		
		// excedent de pop
		int excedent = 0;
		for (int i = 0; i < s.length; i++)
			excedent += s[i].evolutionPopulation(numero, getPossession(s[i].getPosition()));
		for (int i = 0; i < s.length; i++)
			s[i].evolutionMinerai(numero);

		Possession[] p = listeVraiePossession();
		for (int i = 0; i < p.length; i++)
			p[i].evolutionPosteCo(numero, tauxTaxationPoste, s[i]);
		for (int i = 0; i < p.length; i++)
			p[i].programmationConstructions(this, s[i]);
		for (int i = 0; i < p.length; i++) {
			Univers.phaseSuivante();
			p[i].resolutionConstructions(this, s[i]);
		}
		for (int i = 0; i < s.length; i++)
			s[i].reparation(numero, getGouverneurSurPossession(s[i].getPosition()));
		
		// On remet à zéro les terraformations.
		for (int i = 0; i < s.length; i++){
			for(int pla=0;pla<s[i].getNombrePlanetes();pla++){
				s[i].getPlanete(pla).setEstDejaTerraforme(false);
			}
		}
	}

	public void resolutionProgressionRecherche() {


		if (totalAffectationPourcentage() < 100) {
			float argent = (float) ((100 - totalAffectationPourcentage())
					* getBudgetDepense(Const.DOMAINES_BUDGET_TECHNOLOGIQUE)
					* 0.8 / 100);
			if (argent > 0) {
				modifierBudget(Const.BUDGET_COMMANDANT_REVENUS_DIVERS, argent);
				ajouterEvenement("EV_TECHNO_RABAIS_0000", argent);
			}
		}
		
		float r = getBudgetTechnologique();
		String[] s = recherchesActuelles();
		for (int i = 0; i < s.length; i++) {
			Technologie t = Univers.getTechnologie(s[i]);
			if (Univers.estTechnologiePublique(s[i])) {
				suppressionDomaineDeRecherche(s[i]);
				ajouterEvenement("EV_COMMANDANT_RECHERCHE_0001", t);
			} else {
				int pourcentage = pourcentageAffecte(s[i]);
				int nb = nombreDePointsDeRecherche(s[i]);
				ajouterPointsDeRecherche(s[i], (int) (r * pourcentage) / 100);

				if (t.getPointsDeRecherche() <= nombreDePointsDeRecherche(s[i])) {
					suppressionDomaineDeRecherche(s[i]);
					ajouterTechnologieConnue(s[i]);
					ajouterEvenement("EV_COMMANDANT_RECHERCHE_0000", t);
				}
			}
		}
	}

	public void finaliserBudget() {
		Systeme[] s = Univers.listeSystemes(listePossession());
		float revenu = 0F;
		for (int i = 0; i < s.length; i++)
			revenu = revenu
					+ s[i].getRevenu(numero,
							getGouverneurSurPossession(s[i].getPosition()),
							getPossession(s[i].getPosition()));
		modifierBudget(Const.BUDGET_COMMANDANT_REVENUS_SYSTEMES, revenu);
		float entretien = 0F;
		for (int i = 0; i < s.length; i++) {
			entretien = entretien
					+ s[i].getEntretien(numero,
							getGouverneurSurPossession(s[i].getPosition()),
							getPossession(s[i].getPosition()));
		}
		
		/**
		 * Patch Fergok
		 */
		
		if( getRace() == 4 ){
			entretien = entretien * 0.9f;
		}
		
		// entretien=entretien+getEntretienNombrePlanete();
		modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_SYSTEME, -entretien);
		modifierBudget(Const.BUDGET_COMMANDANT_RECHERCHE,
				-getBudgetDepense(Const.DOMAINES_BUDGET_TECHNOLOGIQUE));
		modifierBudget(Const.BUDGET_COMMANDANT_SERVICES_SPECIAUX,
				-getBudgetDepense(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX));
		modifierBudget(Const.BUDGET_COMMANDANT_CONTRE_ESPIONNAGE,
				-getBudgetDepense(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE));
		modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_LIEUTENANTS,
				-getEntretienLieutenants());

		float recette = 0F;
		for (int i = Const.BUDGET_COMMANDANT_TOUR_PRECEDENT + 1; i < Const.BUDGET_COMMANDANT_TOTAL_RECETTE; i++)
			recette = recette + getBudget(i);
		setBudget(Const.BUDGET_COMMANDANT_TOTAL_RECETTE, recette);
		float depenses = 0F;
		for (int i = Const.BUDGET_COMMANDANT_TOTAL_RECETTE + 1; i < Const.BUDGET_COMMANDANT_TOTAL_DEPENSE; i++)
			depenses = depenses + getBudget(i);
		setBudget(Const.BUDGET_COMMANDANT_TOTAL_DEPENSE, depenses);
		setBudget(Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE, centaures);
	}

	public static void resolutionEvenements() {

		// Oxole
		for (int a = 0; a < 1 + Univers.getInt(Math.max(2, Univers.getTour() - 1)); a++) {
			Position[] p = Univers.listePositionsSystemes();
			Position choix = p[Univers.getInt(p.length)];
			Systeme systeme = Univers.getSysteme(choix);
			int[] proprios = systeme.getProprios();
			int prop = proprios[Univers.getInt(proprios.length)];
			Commandant c = Univers.getCommandant(prop);
			Possession poss = c.getPossession(choix);
			int nb = Univers.getInt(20) + 50;
			poss.ajouterMarchandise(15, nb);
			Univers.ajouterEvenement("EVENEMENT_OXOLE", nb, choix);
		}

		// Lixiam
		for (int a = 0; a < 1 + Univers.getInt(2); a++) {
			Position[] p = Univers.listePositionsSystemes();
			Position choix = p[Univers.getInt(p.length)];
			Systeme systeme = Univers.getSysteme(choix);
			int[] proprios = systeme.getProprios();
			int prop = proprios[Univers.getInt(proprios.length)];
			Commandant c = Univers.getCommandant(prop);
			Possession poss = c.getPossession(choix);
			int nb = Univers.getInt(20) + 50;
			poss.ajouterMarchandise(14, nb);
			Univers.ajouterEvenement("EVENEMENT_LIXIAM", nb, choix);
		}

		// Tixium
		for (int a = 0; a < 1 + Univers.getInt(2); a++) {
			Position[] p = Univers.listePositionsSystemes();
			Position choix = p[Univers.getInt(p.length)];
			Systeme systeme = Univers.getSysteme(choix);
			int[] proprios = systeme.getProprios();
			int prop = proprios[Univers.getInt(proprios.length)];
			Commandant c = Univers.getCommandant(prop);
			Possession poss = c.getPossession(choix);
			int nb = Univers.getInt(20) + 50;
			poss.ajouterMarchandise(13, nb);
			Univers.ajouterEvenement("EVENEMENT_TIXIUM", nb, choix);
		}

	}

	// Les messages d'info.

	public void ajouterOrdres(String message, String[] param) {
		ordres.ajouter(message, param);
	}

	public void ajouterCombat(Object o) {
		combat.ajouter("{0}", o);
	}

	public void ajouterCombat(String message, Object v1, Object v2, Object v3,
			int v4) {
		combat.ajouter(message, v1, v2, v3, v4);
	}

	public void ajouterCombat(String message, Object v1, Object v2, Object v3,
			Object v4, int v5) {
		combat.ajouter(message, v1, v2, v3, v4, v5);
	}

	public boolean ajouterErreur(String message) {
		erreur.ajouter(message);
		return false;
	}

	public boolean ajouterErreur(String message, Object var) {
		erreur.ajouter(message, var);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, float var2) {
		erreur.ajouter(message, var1, var2);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, int var2) {
		erreur.ajouter(message, var1, var2);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, Object var2) {
		erreur.ajouter(message, var1, var2);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, Object var2,
			int var3) {
		erreur.ajouter(message, var1, var2, var3);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, Object var2,
			Object var3) {
		erreur.ajouter(message, var1, var2, var3);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, Object var2,
			Object var3, int var4) {
		erreur.ajouter(message, var1, var2, var3, var4);
		return false;
	}

	public boolean ajouterErreur(String message, int var) {
		erreur.ajouter(message, var);
		return false;
	}

	public boolean ajouterErreur(String message, int var1, int var2) {
		erreur.ajouter(message, var1, var2);
		return false;
	}

	public boolean ajouterErreur(String message, Object var1, int var2, int var3) {
		erreur.ajouter(message, var1, var2, var3);
		return false;
	}

	public boolean ajouterEvenement(String message) {
		evenement.ajouter(message);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var) {
		evenement.ajouter(message, var);
		return true;
	}

	public boolean ajouterEvenement(String message, int var) {
		evenement.ajouter(message, var);
		return true;
	}

	public boolean ajouterEvenement(String message, int var1, int var2) {
		evenement.ajouter(message, var1, var2);
		return true;
	}

	public boolean ajouterEvenement(String message, float var) {
		evenement.ajouter(message, var);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2) {
		evenement.ajouter(message, var1, var2);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			float var3, int var4) {
		evenement.ajouter(message, var1, var2, var3,
				var4);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3, int var4) {
		evenement.ajouter(message, var1, var2, var3, var4);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3, Object var4, int var5) {
		evenement.ajouter(message, var1, var2, var3, var4, var5);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3, int var4, int var5) {
		evenement.ajouter(message, var1, var2, var3, var4,
				var5);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3, int var4, float var5) {
		evenement.ajouter(message, var1, var2, var3, var4,
				var5);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3) {
		evenement.ajouter(message, var1, var2, var3);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			Object var3, Object var4) {
		evenement.ajouter(message, var1, var2, var3, var4);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			int var3) {
		evenement.ajouter(message, var1, var2, var3);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			int var3, int var4) {
		evenement.ajouter(message, var1, var2, var3,
				var4);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, int var2) {
		evenement.ajouter(message, var1, var2);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, float var2) {
		evenement.ajouter(message, var1, var2);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, Object var2,
			float var3) {
		evenement.ajouter(message, var1, var2, var3);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, int var2,
			float var3) {
		evenement.ajouter(message, var1, var2, var3);
		return true;
	}

	public boolean ajouterEvenement(String message, Object var1, int var2,
			int var3) {
		evenement.ajouter(message, var1, var2, var3);
		return true;
	}

	// Les méthodes utilitaires.

	public static void transfertPlanete(Commandant ancien, Commandant nouveau,
			Systeme sys, int numPlanete) {
		sys.getPlanete(numPlanete).setProprio(nouveau.getNumero());
		if (!sys.estProprio(ancien.getNumero())) {
			Possession.intialiserpossession(ancien.getPossession(sys
					.getPosition()));
			nouveau.transfererPossession(sys.getPosition(),
					ancien.getPossession(sys.getPosition()));
			ancien.eliminerPossession(sys.getPosition());
		}
		if (!nouveau.existencePossession(sys.getPosition()))
			nouveau.ajouterPossession(sys.getPosition(), new Possession());
	}

	public ObjetTransporte enleverChargementDeSysteme(Commandant chargeur,
			int numFlotte, Position pos, int numPlanete, String code, int nombre) {
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_MARCHANDISE)
			return vendreMarchandises(chargeur, numFlotte, pos, code, nombre);
		else {
			Systeme sys = Univers.getSysteme(pos);
			ObjetTransporte retour = sys.supprimerRichesses(numero, code,
					nombre, numPlanete);
			Univers.setSysteme(sys);
			return retour;
		}
	}

	public ObjetTransporte ajouterChargementSurSysteme(Commandant dechargeur,
			int numFlotte, Position pos, int numPlanete, ObjetTransporte o) {
		if (ObjetTransporte.typeDeCodeChargement(o.getCode()) == Const.TRANSPORT_MARCHANDISE)
			return acheterMarchandises(dechargeur, numFlotte, pos, o.getCode(),
					o.getNombreObjets());
		else {
			Systeme sys = Univers.getSysteme(pos);
			sys.ajouterRichesses(numero, o, numPlanete);
			Univers.setSysteme(sys);
			return null;
		}
	}

	public ObjetTransporte vendreMarchandises(Commandant acheteur,
			int numFlotte, Position pos, String code, int nombre) {
		float modif = 1F;

		Possession p = getPossession(pos);
		int nb = p.getQuantiteMarchandise(Utile.numeroMarchandise(code));
		int prix = p.getPrixMarchandise(Utile.numeroMarchandise(code));
		int nombreVendu = Math.min(nb, nombre);
		float prixEffectif = ((float) (prix * nombreVendu)) / 10F;
		Heros h = acheteur.getHerosSurFlotte(numFlotte);
		Gouverneur g = getGouverneurSurPossession(pos);
		if (h != null)
			for (int i = 0; i < (1 + nombre) / modif; i++)
				h.augmenterExperience();
		if (g != null)
			for (int i = 0; i < (1 + nombre) / modif; i++)
				g.augmenterExperience();
		prixEffectif = Math.max(
				0.1F,
				prixEffectif
						+ (prixEffectif * ((h == null ? 0 : h
								.getMarchandModifie()) - (g == null ? 0 : g
								.getMarchandModifie()))) / 20F);

		prixEffectif = (prixEffectif + (prixEffectif * getTauxTaxationPoste()) / 100)
				/ modif;

		if (nombreVendu == 0) {
			acheteur.ajouterErreur(
					"ER_COMMANDANT_VENTE_MARCHANDISE_0001",
					ObjetTransporte.traductionChargement(code, nombre,
							acheteur.getLocale()), pos, this.getNomNumero(),
					nombre);
			return null;
		}
		if (acheteur != this) {
			if (acheteur.centaures < prixEffectif) {
				acheteur.ajouterErreur("ER_COMMANDANT_VENTE_MARCHANDISE_0000",
						ObjetTransporte.traductionChargement(code, nombre,
								acheteur.getLocale()), pos, nombre);
				return null;
			}

			if (acheteur.existenceVisaDechargement(pos, numero,
					Utile.numeroMarchandise(code))) {
				acheteur.ajouterErreur("ER_COMMANDANT_VENTE_MARCHANDISE_0002",
						ObjetTransporte.traductionChargement(code, nombre,
								acheteur.getLocale()), pos, acheteur
								.getNomNumero(), nombre);
				return null;
			}
			modifierBudget(Const.BUDGET_COMMANDANT_VENTE_MARCHANDISE,
					prixEffectif);
			acheteur.modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_MARCHANDISE,
					-prixEffectif);
			ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0000",
					acheteur.getNomNumero(),
					ObjetTransporte.traductionChargement(code, nombreVendu,
							getLocale()), pos, nombreVendu, prixEffectif);
			acheteur.ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0001",
					getNomNumero(), ObjetTransporte.traductionChargement(code,
							nombreVendu, acheteur.getLocale()), pos,
					nombreVendu, prixEffectif);
		} else
			ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0002",
					ObjetTransporte.traductionChargement(code, nombreVendu,
							getLocale()), pos, nombreVendu);
		p.supprimerMarchandise(Utile.numeroMarchandise(code), nombreVendu);
		return new ObjetSimpleTransporte(code, nombreVendu);
	}

	public ObjetTransporte acheterMarchandises(Commandant vendeur,
			int numFlotte, Position pos, String code, int nombre) {
		float modif = 0F;

		if (this.estJoueurNeutre()) {
			modif = 1 / 100F;
		} else {
			modif = 1F;
		}

		Possession p = getPossession(pos);
		int prix = p.getPrixMarchandise(Utile.numeroMarchandise(code));
		float prixEffectif = ((float) (prix * nombre)) / 10F;
		Heros h = vendeur.getHerosSurFlotte(numFlotte);
		Gouverneur g = getGouverneurSurPossession(pos);
		if (h != null)
			for (int i = 0; i < (1 + nombre) / modif; i++)
				h.augmenterExperience();
		if (g != null)
			for (int i = 0; i < (1 + nombre) / modif; i++)
				g.augmenterExperience();
		prixEffectif = Math.max(
				0.1F,
				prixEffectif
						- (prixEffectif * ((h == null ? 0 : h
								.getMarchandModifie()) - (g == null ? 0 : g
								.getMarchandModifie()))) / 20F);
		prixEffectif = (prixEffectif - (prixEffectif * getTauxTaxationPoste()) / 100)
				* modif;

		if (vendeur != this) {
			if (centaures < 2 * prixEffectif) {
				vendeur.ajouterErreur("ER_COMMANDANT_ACHAT_MARCHANDISE_0000",
						ObjetTransporte.traductionChargement(code, nombre,
								vendeur.getLocale()), pos, nombre);
				return new ObjetSimpleTransporte(code, nombre);
			}

			vendeur.ajouterVisaDechargement(pos, numero,
					Utile.numeroMarchandise(code));

			modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_MARCHANDISE,
					-prixEffectif);
			vendeur.modifierBudget(Const.BUDGET_COMMANDANT_VENTE_MARCHANDISE,
					prixEffectif);
			ajouterEvenement("EV_COMMANDANT_ACHAT_MARCHANDISE_0000",
					vendeur.getNomNumero(),
					ObjetTransporte.traductionChargement(code, nombre,
							getLocale()), pos, nombre, prixEffectif);
			vendeur.ajouterEvenement(
					"EV_COMMANDANT_ACHAT_MARCHANDISE_0001",
					getNomNumero(),
					ObjetTransporte.traductionChargement(code, nombre,
							vendeur.getLocale()), pos, nombre, prixEffectif);
		} else
			ajouterEvenement("EV_COMMANDANT_ACHAT_MARCHANDISE_0002",
					ObjetTransporte.traductionChargement(code, nombre,
							getLocale()), pos, nombre);

		if (numero == 0)
			p.ajouterMarchandise(Utile.numeroMarchandise(code), nombre / 4);
		else
			p.ajouterMarchandise(Utile.numeroMarchandise(code), nombre);

		return null;
	}

	// les méthodes utilitaires privées.

	private void repartirDroitsEntreeAlliance(Alliance a) {
		if (a.estAutocratique())
			Univers.getCommandant(a.getNumeroDirigeant()).modifierBudget(
					Const.BUDGET_COMMANDANT_TAXE_SUR_DROITS_ENTREE_ALLIANCE,
					a.getDroitsEntree());
		else {
			Commandant[] c = a.getAdherents();
			if (c.length != 0) {
				float gain = a.getDroitsEntree() / c.length;
				for (int i = 0; i < c.length; i++)
					c[i].modifierBudget(
							Const.BUDGET_COMMANDANT_TAXE_SUR_DROITS_ENTREE_ALLIANCE,
							gain);
			}
		}
	}

	private String trouverTechnoAVoler(Commandant cible, int fric) {
		String[] liste = cible.listeTechnologiesNonPubliquesConnues();
		for (int i = 0; i < liste.length; i++)
			if (!estTechnologieConnue(liste[i])
					&& peutChercherTechnologie(liste[i])
					&& fric >= (Univers.getTechnologie(liste[i])
							.getPointsDeRecherche() / 10))
				return liste[i];
		return null;
	}

	// les méthodes pour gérer les commandants.

	public boolean dechirerPacteDeNonAgression(int numeroCible) {
		if (!existencePacteDeNonAgression(numeroCible))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PACTE_0000", numeroCible);

		romprePacteDeNonAgression(numeroCible);
		Commandant cible = Univers.getCommandant(numeroCible);
		cible.romprePacteDeNonAgression(numero);
		reputation = reputation + Const.REPUTATION_RUPTURE_DE_PACTE;

		cible.ajouterEvenement("EV_COMMANDANT_PACTE_0000", getNomNumero());
		return ajouterEvenement("EV_COMMANDANT_PACTE_0001",
				cible.getNomNumero());
	}

	public boolean signerPacteDeNonAgression(int numeroCible) {
		if (existencePacteDeNonAgression(numeroCible))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PACTE_0001", numeroCible);

		ajouterPacteDeNonAgression(numeroCible);
		reputation = reputation + Const.REPUTATION_SIGNATURE_DE_PACTE;

		return ajouterEvenement("EV_COMMANDANT_PACTE_0002", numeroCible);
	}

	public boolean creerAlliance(int droitsEntree, String titre, int type,
			boolean secrete) {
		if (!estTechnologieConnue("diploI"))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0011");
		// if(nombreAlliancesTropGrand()) return
		// ajouterErreur("ER_COMMANDANT_ALLIANCE_0001",titre);
		float cout;
		if (secrete)
			cout = Const.COUT_CREATION_ALLIANCE_SECRETE;
		else
			cout = Const.COUT_CREATION_ALLIANCE_NON_SECRETE;
		if (centaures < cout)
			return ajouterErreur("ER_COMMANDANT_ALLIANCE_0000", titre,
					centaures);
		// if(estDirigeantAlliance()) return
		// ajouterErreur("ER_COMMANDANT_ALLIANCE_0004",titre);

		modifierBudget(Const.BUDGET_COMMANDANT_CREATION_ALLIANCE, -cout);
		Alliance a = new Alliance(Univers.trouverNumeroLibreAlliance(), titre,
				(float) droitsEntree, type, secrete, numero, nom,
				Univers.getTour());
		Univers.ajouterAlliance(a);
		ajouterAlliance(a.getNumero());

		return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0000", titre);
	}

	public boolean adhererAlliance(int num) {
		if (!Univers.allianceExistante(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0002", num);
		// if(nombreAlliancesTropGrand()) return
		// ajouterErreur("ER_COMMANDANT_ALLIANCE_0003",num);
		Alliance a = Univers.getAlliance(num);
		if (centaures < a.getDroitsEntree())
			return ajouterErreur("ER_COMMANDANT_ALLIANCE_0002", a.getNom(),
					a.getDroitsEntree());

		modifierBudget(Const.BUDGET_COMMANDANT_ADHESION_ALLIANCE,
				-a.getDroitsEntree());
		repartirDroitsEntreeAlliance(a);
		ajouterAlliance(a.getNumero());

		a.ajouterEvenement("EV_ALLIANCE_0000", getNomNumero());
		return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0001", a.getNom(),
				a.getDroitsEntree());
	}

	public boolean voterElectionDirigeant(int num, int dirigeant) {
		if (!Univers.existenceCommandant(dirigeant))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0003", dirigeant);
		if (!Univers.getCommandant(dirigeant).appartientAAlliance(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0004", dirigeant, num);
		if (!appartientAAlliance(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0000", num);
		Alliance a = Univers.getAlliance(num);
		if ((!a.estDemocratique())
				&& ((!a.estAnarchique()) || (a.estSecrete())))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0001", num);

		a.ajouterVoteDirigeant(dirigeant);

		return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0004", a.getNom(),
				Univers.getCommandant(dirigeant).getNomNumero());
	}

	public boolean voterExclusionCommandant(int num, int cible) {
		if (!Univers.existenceCommandant(cible))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0008", cible);
		if (!Univers.getCommandant(cible).appartientAAlliance(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0009", cible, num);
		if (!appartientAAlliance(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0005", num);
		Alliance a = Univers.getAlliance(num);
		if (a.estAnarchique())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0010", num);
		if ((a.estAutocratique()) && (!a.estDirigeePar(numero)))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0011", num);

		a.ajouterVoteExclusion(cible);

		return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0006", a.getNom(),
				Univers.getCommandant(cible).getNomNumero());
	}

	public boolean quitterAlliance(int num) {
		if (!Univers.allianceExistante(num))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0006", num);
		Alliance a = Univers.getAlliance(num);
		if (!appartientAAlliance(a.getNumero()))
			return ajouterErreur("ER_COMMANDANT_ALLIANCE_0005", a.getNom());

		enleverAlliance(a.getNumero());

		a.ajouterEvenement("EV_ALLIANCE_0001", getNomNumero());
		return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0002", a.getNom());
	}

	public boolean renommerAlliance(String titre, int numeroAlliance) {
		if (!Univers.allianceExistante(numeroAlliance))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0012", numeroAlliance);
		Alliance a = Univers.getAlliance(numeroAlliance);
		if (!a.estDirigeePar(numero))
			return ajouterErreur("ER_COMMANDANT_ALLIANCE_0006", a.getNom());

		a.setNom(titre);

		a.ajouterEvenement("EV_ALLIANCE_0007", titre);
		return true;
	}

	public boolean ecrireAdresseAlliance(String titre, int numeroAlliance) {
		if (!Univers.allianceExistante(numeroAlliance))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ALLIANCE_0013", numeroAlliance);
		Alliance a = Univers.getAlliance(numeroAlliance);
		if (!a.estDirigeePar(numero))
			return ajouterErreur("ER_COMMANDANT_ALLIANCE_0007", a.getNom());

		a.setAdresseElectronique(titre);

		a.ajouterEvenement("EV_ALLIANCE_0008", titre);
		return true;
	}

	public boolean transfertCentaures(int destinataire, int don,
			int modeTransfert) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_CENTAURES_0000", destinataire);
		float cout = (float) don;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = cout + Const.SURCOUT_DON_CENTAURES_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = cout + Const.SURCOUT_DON_CENTAURES_ANONYME;
		if (cout > centaures)
			return ajouterErreur("ER_COMMANDANT_DON_CENTAURES_0000", don,
					destinataire);

		modifierBudget(Const.BUDGET_COMMANDANT_DON_CENTAURES, -cout);
		Commandant cible = Univers.getCommandant(destinataire);
		cible.modifierBudget(Const.BUDGET_COMMANDANT_RECEPTION_CENTAURES,
				(float) don);
		Univers.ajouterRelationRaces(cible.getCapitale(), getRace(),
				cible.getRace(), don / 100);
		Univers.ajouterTransfert(this, cible,
				"centaures : " + Integer.toString(don));

		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (don > Const.DON_MAXIMUM_SECRET))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0000",
					getNomNumero(), cible.getNomNumero(),
					"<font color=\"#FBF7AF\">" + don + "</font>");
		ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0001",
				cible.getNomNumero(), don);
		if (modeTransfert == Const.DON_MODE_ANONYME)
			return cible.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0002",
					"<font color=\"#FBF7AF\">" + don + "</font>");
		else
			return cible.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0003",
					getNomNumero(), "<font color=\"#FBF7AF\">" + don
							+ "</font>");
	}

	public boolean abandonnerTechnologie(String codeTechno) {
		if (!estTechnologieConnue(codeTechno))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ELIMINER_TECHNOLOGIE_0000",
					"<font color=\"#00F1AF\">" + codeTechno + "</font>");

		eliminerTechnologieConnue(codeTechno);

		Technologie t = Univers.getTechnologie(codeTechno);
		return ajouterEvenement("EV_COMMANDANT_ELIMINER_TECHNOLOGIE_0000",
				"<font color=\"ffdead\">" + t.getCode() + "</font>");
	}

	public boolean transfertTechnologie(int destinataire, String codeTechno,
			int modeTransfert) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_TECHNOLOGIE_0000", codeTechno,
					destinataire);
		if (!estTechnologieConnue(codeTechno))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_TECHNOLOGIE_0001",
					"<font color=\"#00F1AF\">" + codeTechno + "</font>",
					destinataire);
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_TECHNO_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_TECHNO_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_TECHNOLOGIE_0000",
					Univers.getNomTechno(codeTechno, getLocale()), destinataire);
		Commandant cible = Univers.getCommandant(destinataire);
		if (cible.estTechnologieConnue(codeTechno))
			return ajouterErreur("ER_COMMANDANT_DON_TECHNOLOGIE_0001",
					Univers.getNomTechno(codeTechno, getLocale()), destinataire);

		if ( !cible.peutChercherTechnologie(codeTechno)/** && (cible.getRace() != 3)**/) {
			return ajouterErreur("ER_COMMANDANT_DON_TECHNOLOGIE_0002",
					Univers.getNomTechno(codeTechno, getLocale()), destinataire);
		}
		cible.ajouterTechnologieConnue(codeTechno);
		cible.suppressionDomaineDeRecherche(codeTechno);
		if (cout != 0F)
			modifierBudget(Const.BUDGET_COMMANDANT_DON_TECHNOLOGIE, -cout);
		Univers.ajouterRelationRaces(cible.getCapitale(), getRace(),
				cible.getRace(), Const.RELATION_TRANSFERT_TECHNOLOGIE);
		Univers.ajouterTransfert(this, cible, "technologie : " + codeTechno);

		Technologie t = Univers.getTechnologie(codeTechno);
		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_TECHNO_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0002",
					getNomNumero(), cible.getNomNumero(), t);
		ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0000",
				cible.getNomNumero(), t);
		if (modeTransfert == Const.DON_MODE_ANONYME)
			return cible.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0003",
					t);
		else
			return cible.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0001",
					getNomNumero(), t);
	}

	public boolean transfertFlotte(int destinataire, int numeroFlotte,
			int nbTours, int modeTransfert) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_FLOTTE_0000", numeroFlotte + 1,
					destinataire);
		if (!existenceFlotte(numeroFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_FLOTTE_0001", numeroFlotte + 1,
					destinataire);
		Flotte f = getFlotte(numeroFlotte);
		if (f.estLoueEnPartie())
			return ajouterErreur("ER_COMMANDANT_DON_FLOTTE_0001",
					numeroFlotte + 1, destinataire);
		Commandant cible = Univers.getCommandant(destinataire);
		if (!cible.estJoueurHumain())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_FLOTTE_0002", numeroFlotte + 1,
					destinataire);
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_FLOTTE_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_FLOTTE_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_FLOTTE_0000",
					f.getNomNumero(numeroFlotte), destinataire);

		f.planifierTransmission(numero, Univers.getTour() + nbTours);
		cible.ajouterFlotte(f);
		eliminerFlotte(numeroFlotte);
		modifierBudget(Const.BUDGET_COMMANDANT_PRET_FLOTTE, -cout);
		Univers.ajouterRelationRaces(cible.getCapitale(), getRace(),
				cible.getRace(),
				5 * Vaisseau.retournerNiveauPuissance(f.getPuissance()));
		Univers.ajouterTransfert(this, cible, "prêt flotte puissance : "
				+ Integer.toString(f.getPuissance()));

		ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0000", cible.getNomNumero(),
				numeroFlotte + 1, nbTours);
		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_FLOTTE_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0003",
					getNomNumero(), cible.getNomNumero());
		if (modeTransfert == Const.DON_MODE_ANONYME)
			return cible.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0002",
					numeroFlotte + 1, nbTours);
		else
			return cible.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0001",
					getNomNumero(), numeroFlotte + 1, nbTours);
	}

	public boolean venteFlotte(int destinataire, int numeroFlotte) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_VENTE_FLOTTE_0000", numeroFlotte + 1,
					destinataire);
		if (!existenceFlotte(numeroFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_VENTE_FLOTTE_0001", numeroFlotte + 1,
					destinataire);
		Flotte f = getFlotte(numeroFlotte);
		if (f.estLoueEnPartie())
			return ajouterErreur("ER_COMMANDANT_VENTE_FLOTTE_0003",
					numeroFlotte + 1, destinataire);
		Commandant cible = Univers.getCommandant(destinataire);
		// if(!cible.estJoueurHumain())
		// return
		// Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_VENTE_FLOTTE_0002",numeroFlotte+1,destinataire);
		float cout = f.getValeur() / 2;

		if (cout > cible.getCentaures())
			return ajouterErreur("ER_COMMANDANT_VENTE_FLOTTE_0000",
					f.getNomNumero(numeroFlotte), destinataire);

		f.initialiserEquipages();
		cible.ajouterFlotte(f);
		eliminerFlotte(numeroFlotte);
		cible.modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_FLOTTE, -cout);
		modifierBudget(Const.BUDGET_COMMANDANT_VENTE_FLOTTE, cout);
		Univers.ajouterTransfert(this, cible, "vente flotte puissance : "
				+ Integer.toString(f.getPuissance()));

		Univers.ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0003",
				getNomNumero(), cible.getNomNumero());
		ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0000",
				cible.getNomNumero(), numeroFlotte + 1);
		return cible.ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0001",
				getNomNumero(), Flotte.getNomOut(f), cout, numeroFlotte + 1);
	}

	public boolean transfertSysteme(int destinataire, Position pos,
			int modeTransfert) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_SYSTEME_0000", destinataire);
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_SYSTEME_0001", pos);
		Systeme sys = Univers.getSysteme(pos);
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_SYSTEME_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_SYSTEME_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_SYSTEME_0000", pos,
					destinataire);

		Commandant cible = Univers.getCommandant(destinataire);
		Univers.ajouterTransfert(this, cible, "don système puissance : "
				+ Integer.toString(sys.getPuissance(numero)));
		Possession.intialiserpossession(getPossession(pos));
		sys.changementDeProprietaire(numero, destinataire);
		Univers.setSysteme(sys);
		cible.transfererPossession(pos, getPossession(pos));
		eliminerPossession(pos);
		if (cout != 0F)
			modifierBudget(Const.BUDGET_COMMANDANT_DON_SYSTEME, -cout);
		Univers.ajouterRelationRaces(cible.getCapitale(), getRace(),
				cible.getRace(), Const.RELATION_TRANSFERT_SYSTEME);

		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_SYSTEME_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0002",
					getNomNumero(), cible.getNomNumero(), pos);
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cible.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0003", pos);
		else
			cible.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0001",
					getNomNumero(), pos);
		return ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0000",
				cible.getNomNumero(), pos);
	}

	// fonction necessaire

	public boolean transfertPlanete(int destinataire, Position pos,
			int numPlanete, int modeTransfert) {
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_PLANETE_0000", destinataire);
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_PLANETE_0001", pos);
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0001", pos,
					destinataire, numPlanete + 1);
		if (sys.getPlanete(numPlanete).getProprio() != numero)
			return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0002", pos,
					sys.getNomNumeroPlanete(numPlanete));
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_PLANETE_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_PLANETE_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0000", pos,
					sys.getNomNumeroPlanete(numPlanete), destinataire);

		Commandant cible = Univers.getCommandant(destinataire);
		transfertPlanete(this, cible, sys, numPlanete);
		Univers.setSysteme(sys);
		if (cout != 0F)
			modifierBudget(Const.BUDGET_COMMANDANT_DON_PLANETE, -cout);
		Univers.ajouterTransfert(this, cible, "don planète");

		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_PLANETE_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0002",
					getNomNumero(), cible.getNomNumero(), pos);
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cible.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0003", pos,
					sys.getNomNumeroPlanete(numPlanete));
		else
			cible.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0001",
					getNomNumero(), pos, sys.getNomNumeroPlanete(numPlanete));
		return ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0000",
				cible.getNomNumero(), pos, sys.getNomNumeroPlanete(numPlanete));
	}

	public boolean mettreEnChantier(Position pos, int nombre, String codeConstruction, int numPlanete) {
		
		if (!existencePossession(pos)) {
            return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_MISE_EN_CHANTIER_0000", pos);
        }
		
		if (!peutMettreEnChantier(codeConstruction)) {
            ajouterErreur("ER_COMMANDANT_MISE_EN_CHANTIER_0001", pos, codeConstruction, nombre);
            return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_MISE_EN_CHANTIER_0001", codeConstruction);
        }

		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_MISE_EN_CHANTIER_0000", pos, numPlanete + 1);

		if (nombre > 0){
			getPossession(pos).ajouterConstruction(new Construction(codeConstruction, nombre, numPlanete));

			if (Univers.existenceTechnologie(codeConstruction)){
				return ajouterEvenement("EV_COMMANDANT_MISE_EN_CHANTIER_0000", pos, Univers.getTechnologie(codeConstruction), nombre);
			} else {
				return ajouterEvenement("EV_COMMANDANT_MISE_EN_CHANTIER_0000", pos, codeConstruction, nombre);
			}
			
		} else {
			
			if (Univers.existenceTechnologie(codeConstruction)){
				return ajouterEvenement("ER_COMMANDANT_MISE_EN_CHANTIER_0001", pos, Univers.getTechnologie(codeConstruction), nombre);
			} else {
				return ajouterEvenement("ER_COMMANDANT_MISE_EN_CHANTIER_0001", pos, codeConstruction, nombre);
			}
		}

	}

	public boolean annulerConstruction(Position pos) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ANNULER_CONSTRUCTION_0000", pos);

		getPossession(pos).initialiserConstructions();

		return ajouterEvenement("EV_COMMANDANT_ANNULER_CONSTRUCTION_0000", pos);
	}

	public boolean programmerConstruction(Position pos, String codeConstruction) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000", pos);
		if (!peutMettreEnChantier(codeConstruction))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0001",
					codeConstruction);
		if (!estTechnologieConnue("progcoI"))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0002");

		getPossession(pos).programmerConstruction(codeConstruction);

		if (Univers.existenceTechnologie(codeConstruction))
			return ajouterEvenement(
					"EV_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000", pos,
					Univers.getTechnologie(codeConstruction));
		else
			return ajouterEvenement(
					"EV_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000", pos,
					codeConstruction);
	}

	public boolean annulerProgramationConstruction(Position pos) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000", pos);

		getPossession(pos).initialiserProgrammationConstruction();

		return ajouterEvenement(
				"EV_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000", pos);
	}

	public boolean modifierPolitique(Position pos, int politique) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_POLITIQUE_0000", pos);
		if (politique > Const.NB_POLITIQUES)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_POLITIQUE_0001", pos);

		getPossession(pos).setPolitique(politique);
		modifierBudget(Const.BUDGET_COMMANDANT_CHANGER_POLITIQUE,
				-Const.COUT_CHANGER_POLITIQUE);

		return ajouterEvenement("EV_COMMANDANT_MODIFIER_POLITIQUE_0000", pos,
				Univers.getMessage("POLITIQUES", politique, getLocale()));
	}

	public boolean modifierBudget(Position pos, int[] domaine, int[] valeur) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_BUDGET_0000", pos);
		for (int i = 0; i < domaine.length; i++)
			if (domaine[i] > Const.NB_DOMAINES_BUDGET)
				return Univers.ajouterErreur(getNomNumero(),
						"ER_COMMANDANT_MODIFIER_BUDGET_0001", pos);

		if (!getPossession(pos).modifierBudget(domaine, valeur))
			return ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_BUDGET_0000", pos);

		return ajouterEvenement("EV_COMMANDANT_MODIFIER_BUDGET_0000", pos);
	}

	public boolean modifierTaxation(Position pos, int taxation) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_TAXATION_0000", pos);
		if (taxation > Const.TAXATION_MAXIMALE)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_TAXATION_0001", pos);

		Systeme sys = Univers.getSysteme(pos);
		sys.modifierTaxation(numero, taxation);
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_MODIFIER_TAXATION_0000", pos,
				taxation);
	}

	public boolean modifierTaxationPlanete(Position pos, int taxation,
			int numPlanete) {
		if (!estTechnologieConnue("gestplaI"))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_TAXATION_0004");
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_TAXATION_0002", pos);
		if (taxation > Const.TAXATION_MAXIMALE)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MODIFIER_TAXATION_0003", pos);
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_MODIFIER_TAXATION_0000", pos,
					numPlanete + 1);
		if (!sys.getPlanete(numPlanete).estProprio(numero))
			return ajouterErreur("ER_COMMANDANT_MODIFIER_TAXATION_0001", pos,
					sys.getNomNumeroPlanete(numPlanete));

		sys.getPlanete(numPlanete).setTaxation(taxation);
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_MODIFIER_TAXATION_0001", pos,
				sys.getNomNumeroPlanete(numPlanete), taxation);
	}

	public boolean terraformer(Position pos) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_TERRAFORMER_0000", pos);
		Systeme sys = Univers.getSysteme(pos);
		if (sys.estDejaTerraforme(numero))
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0004", pos);
		float cout = sys.coutTerraformation(numero);
		
		/**
		 * Patch fremen
		 * cout de terraformation: -10%
		 */
		if( this.getRace() == 0 ){
			cout = cout * 0.9f;
		}
		
		if (cout > centaures)
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0000", pos);

		modifierBudget(Const.BUDGET_COMMANDANT_TERRAFORMATION, -cout);
		sys.terraformer(numero);
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_TERRAFORMER_0000", pos, cout);
	}

	public boolean terraformerPlanete(Position pos, int numPlanete) {
		if (!estTechnologieConnue("gestplaI"))
			return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_TERRAFORMER_0002");
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_TERRAFORMER_0001", pos);
		
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0002", pos, numPlanete + 1);
		if (!sys.getPlanete(numPlanete).estProprio(numero))
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0003", pos, sys.getNomNumeroPlanete(numPlanete));
		if (sys.getPlanete(numPlanete).estDejaTerraforme())
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0005", pos, sys.getNomNumeroPlanete(numPlanete));

		if (sys.getPlanete(numPlanete).estDejaTerraforme())
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0005", pos, numPlanete + 1);
		
		float cout = sys.getPlanete(numPlanete).coutTerraformation();

		/**
		 * Patch fremen
		 * on divise par deux le cout de terraformation.
		 */
		if( this.getRace() == 0 ){
			cout = cout/2;
		}
		
		if (cout > centaures)
			return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0001", pos,
					sys.getNomNumeroPlanete(numPlanete));

		modifierBudget(Const.BUDGET_COMMANDANT_TERRAFORMATION, -cout);
		sys.getPlanete(numPlanete).terraformer();
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_TERRAFORMER_0001", pos,
				sys.getNomNumeroPlanete(numPlanete), cout);
	}

	public boolean detruireBatiments(Position pos, String code, int nombre,
			int numPlanete) {

		if (!estTechnologieConnue("gestplaI"))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DETRUIRE_BATIMENT_0002");
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DETRUIRE_BATIMENT_0000", pos);
		if ((!Univers.existenceTechnologie(code))
				|| (!Univers.getTechnologie(code).estBatiment()))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DETRUIRE_BATIMENT_0001", pos, code);
		Technologie t = Univers.getTechnologie(code);
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete == Integer.MIN_VALUE) {
			if (!sys.contientBatiment(numero, code))
				return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0000",
						pos, t);
		} else {
			if (numPlanete >= sys.getNombrePlanetes())
				return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0002",
						pos, numPlanete + 1);
			if (!sys.getPlanete(numPlanete).estProprio(numero))
				return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0003",
						pos, sys.getNomNumeroPlanete(numPlanete));
			if (!sys.getPlanete(numPlanete).contientBatiment(code))
				return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0001",
						pos, t, sys.getNomNumeroPlanete(numPlanete));
		}

		int nb = 0;
		if (numPlanete == Integer.MIN_VALUE)
			nb = sys.recyclerMateriel(numero, (Batiment) t, nombre);
		else
			nb = sys.getPlanete(numPlanete).recyclerMateriel((Batiment) t,
					nombre);
		Univers.setSysteme(sys);

		if (numPlanete == Integer.MIN_VALUE)
			if (nb == nombre)
				return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0000",
						pos, t, nb);
			else
				return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0001",
						pos, t, nb, nombre);
		else if (nb == nombre)
			return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0002",
					pos, t, sys.getNomNumeroPlanete(numPlanete), nb);
		else
			return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0003",
					pos, t, sys.getNomNumeroPlanete(numPlanete), nb, nombre);
	}

	public boolean affecterDomainesDeRecherche(String[] codeTechno,
			int[] pourcentage) {
		HashMap inter = (HashMap) recherches.clone();

		for (int i = 0; i < codeTechno.length; i++) {

			if ((!existenceDomaineDeRecherche(codeTechno[i]))
					&& (!peutChercherTechnologie(codeTechno[i]))) {
				recherches = inter;
				Univers.ajouterErreur(getNomNumero(),
						"ER_COMMANDANT_AFFECTER_RECHERCHE_0000", codeTechno[i]);
			} else if (!ajouterDomaineDeRecherche(codeTechno[i], pourcentage[i])) {
				recherches = inter;
				return ajouterErreur("ER_COMMANDANT_AFFECTER_RECHERCHE_0000");
			}
		}

		if (totalAffectationPourcentage() > 100) {
			// Il ne faut pas prendre ce buget tech car il n'est pas correct et correspond au budget du tour précédent.
			// Il faudrait déplacer cette partie dans la méthode "resolutionProgressionRecherche"
			/** float arg = (float) (getBudgetDepense(Const.DOMAINES_BUDGET_TECHNOLOGIQUE) * 0.8 / 100);
			modifierBudget(Const.BUDGET_COMMANDANT_REVENUS_DIVERS, arg);
			ajouterEvenement("EV_TECHNO_RABAIS_0000", arg);
			**/
			recherches = inter;
			return ajouterErreur("ER_COMMANDANT_AFFECTER_RECHERCHE_0001");
		}

		for (int i = 0; i < codeTechno.length; i++)
			ajouterEvenement("EV_COMMANDANT_AFFECTER_RECHERCHE_0000",
					Univers.getTechnologie(codeTechno[i]), pourcentage[i]);
		return true;
	}

	public boolean effectuerMissionSpeciale(Position pos, int typeMission, int numPlanete) {
		float attaque = getBudgetServiceSpeciaux();
		if (typeMission > Const.NB_MISSIONS)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_MISSION_SPECIALE_0000");
		if (!Univers.existenceSysteme(pos))
			return ajouterErreur("ER_COMMANDANT_MISSION_SPECIALE_0000", pos);
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_MISSION_SPECIALE_0001", pos,
					numPlanete);

		int nPlanete = numPlanete;
		if (numPlanete == Integer.MIN_VALUE)
			nPlanete = sys.trouverPlanetePropagandable(numero);

		float defense = sys.getBudgetContreEspionnage(numero);
		int reussite = 0;

		if (typeMission == Const.MISSION_ESPIONNAGE)
			reussite = Univers.getInt(Math.max(1,
					(int) (attaque - (defense + 300))));
		if (typeMission == Const.MISSION_SABOTAGE)
			reussite = Univers.getInt(Math.max(1,
					(int) (attaque - (10 * defense + 300))));
		if (typeMission == Const.MISSION_VOL_TECHNOLOGIE)
			reussite = Univers.getInt(Math.max(1,
					(int) (attaque - (10 * defense + 1000))));
		if (typeMission == Const.MISSION_PROPAGANDE)
			reussite = Univers
					.getInt(Math.max(
							1,
							(int) (attaque - ((2 + Univers.getInt(2)) * defense + 500))));

		Commandant c = Univers.getCommandant(sys.getPlanete(nPlanete)
				.getProprio());

		if (reussite == 0) {
			ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0000", pos,
					sys.getNomNumeroPlanete(nPlanete),
					Univers.getMessage("MISSIONS", typeMission, getLocale()));
			c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0001", pos,
					sys.getNomNumeroPlanete(nPlanete),
					Univers.getMessage("MISSIONS", typeMission, c.getLocale()),
					getNomNumero());
			if (typeMission == Const.MISSION_VOL_TECHNOLOGIE) {
				Univers.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0015",
						getNomNumero(), c.getNomNumero());
			}

			return false;
		}

		if (typeMission == Const.MISSION_VOL_TECHNOLOGIE) {
			String technoVolee = trouverTechnoAVoler(c, reussite);
			if (technoVolee == null)
				ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0002",
						c.getNomNumero());
			else {
				ajouterTechnologieConnue(technoVolee);
				ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0003",
						Univers.getTechnologie(technoVolee), c.getNomNumero());
				if (reussite < 15)
					c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0004",
							Univers.getTechnologie(technoVolee), getNomNumero());
			}
		}

		if (typeMission == Const.MISSION_ESPIONNAGE) {
			ajouterPositionEspionnee(pos);
			ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0005",
					"<A href=\"" + Rapport.lienRapportEspion(pos) + "\">"
							+ Univers.getSysteme(pos).getNomPosition() + "</A>");
		}

		if (typeMission == Const.MISSION_SABOTAGE) {
			sys.detruireToutBatimentDePlanete(nPlanete);
			Univers.setSysteme(sys);
			ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0006", pos,
					sys.getNomNumeroPlanete(nPlanete));
			if (reussite < 10)
				c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0007", pos,
						sys.getNomNumeroPlanete(nPlanete), getNomNumero());
			else
				c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0008", pos,
						sys.getNomNumeroPlanete(nPlanete));
		}

		if (typeMission == Const.MISSION_PROPAGANDE) {
			int stab = sys.getPlanete(nPlanete).getStabilite();
			if (c == this) {
				sys.getPlanete(nPlanete).setStabilite(
						stab + Math.min(20, reussite));
				ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0009", pos,
						sys.getNomNumeroPlanete(nPlanete),
						sys.getPlanete(nPlanete).getStabilite() - stab);
			} else {
				int descente = Math.min(60, Univers.getInt(reussite));
				sys.getPlanete(nPlanete).setStabilite(stab - descente);
				ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0010", pos,
						sys.getNomNumeroPlanete(nPlanete), stab
								- sys.getPlanete(nPlanete).getStabilite());
				if (reussite < 10)
					c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0011",
							pos, sys.getNomNumeroPlanete(nPlanete),
							getNomNumero(), stab
									- sys.getPlanete(nPlanete).getStabilite());
				else
					c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0012",
							pos, sys.getNomNumeroPlanete(nPlanete), stab
									- sys.getPlanete(nPlanete).getStabilite());
				if ((c.estJoueurNeutre())
						&& (Univers.getInt(Univers.getInt(10) + 30) + 10 >= sys
								.getPlanete(nPlanete).getStabilite())) {
					transfertPlanete(c, this, sys, nPlanete);
					sys.getPlanete(nPlanete).setStabilite(
							100 - sys.getPlanete(nPlanete).getStabilite());
					ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0013",
							pos, sys.getNomNumeroPlanete(nPlanete));
				}
				if ((!c.estJoueurNeutre())
						&& (c != this)
						&& (Univers.getInt(Univers.getInt(10) + 20) + 10 >= sys
								.getPlanete(nPlanete).getStabilite())) {
					transfertPlanete(c, this, sys, nPlanete);
					ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0013",
							pos, sys.getNomNumeroPlanete(nPlanete));
					sys.getPlanete(nPlanete).setStabilite(
							100 - sys.getPlanete(nPlanete).getStabilite());

				}
			}
		}
		Univers.setSysteme(sys);
		return true;
	}

	public boolean deplacerFlotte(int nFlotte, Position direction, int directive, int directivePrecision, String stra) {
		int numFlotte = getCorrespondanceFlotte(nFlotte);
		
		if (!Flotte.directiveExistante(directive, directivePrecision))
			return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_DEPLACER_FLOTTE_0000", directive, directivePrecision);
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_DEPLACER_FLOTTE_0001", numFlotte + 1);
		if ( !direction.estValide() )
			return Univers.ajouterErreur(getNomNumero(), "ER_COMMANDANT_DEPLACER_FLOTTE_0002", numFlotte + 1, direction);
		
		Flotte f = getFlotte(numFlotte);
		if (f.nonDeplacee()) {
			String strat = Const.STRATEGIE_DEFAUT.getNom();
			if (connaitStrategie(stra)){ 
				strat = stra; 
			}
			f.deplacer(direction, directive, directivePrecision, getHerosSurFlotte(numFlotte), strat, this);
			if (connaitStrategie(stra)){
				return ajouterEvenement("EV_COMMANDANT_DEPLACER_FLOTTE_0001",
						f.getNomNumero(numFlotte), direction,
						Flotte.traductionDirective(directive,
								directivePrecision, getLocale()), strat);
			}else{
				return ajouterEvenement("EV_COMMANDANT_DEPLACER_FLOTTE_0000",
						f.getNomNumero(numFlotte), direction,
						Flotte.traductionDirective(directive,
								directivePrecision, getLocale()));
			}
		} else {
			return false;
		}
	}

	// Nouvelle interface de cargo
	public void ajouterTransfertEntreSysteme(Transfert t){
		transfertsEnAttentes.add(t);
	}
	
	public boolean transfererEntreSysteme(Position pos1, String code, int nb, int pla1, Position pos2, int pla2) {

		// On vérifie l'existence d'un système sur la position
		if (!Univers.existenceSysteme(pos1))
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0000", pos1); // Position
																			// depart
		if (!Univers.existenceSysteme(pos2))
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0001", pos2); // Position
																			// arrivee

		// On vérifie l'existence d'une possession sur la position
		if (!existencePossession(pos1))
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0002", pos1); // Position
																			// depart
		/** if (!existencePossession(pos2))
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0003", pos2); // Position
																			// arrivee
		**/
		
		Systeme s1 = Univers.getSysteme(pos1);
		Systeme s2 = Univers.getSysteme(pos2);
		// On vérifie que le nombre de planêtes est bien inférieur au nombre de
		// planète du système
		if (pla1 >= s1.getNombrePlanetes()){
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0004", s1.getNomPosition(), pla1+1);
		}
		if (pla2 >= s2.getNombrePlanetes()){
			return ajouterErreur("ER_COMMANDANT_TRANSFERER_0005", s2.getNomPosition(), pla2+1);
		}

		ObjetTransporte o = null;
		// si c'est une marchandise :
		if (ObjetTransporte.typeDeCodeChargement(code) == Const.TRANSPORT_MARCHANDISE) {
			Possession p1 = getPossession(pos1);
			
			/** looking for the receiver **/
			Possession p2 = getPossession(pos2);
			if(p2 == null){
				// Not the current Commandant
				if( pla2>=0){
					p2 = Univers.getCommandant(s2.getPlanete(pla2).getProprio()).getPossession(pos2);
				} else {
					int[] proprios = s2.getProprios();
					if( proprios.length == 1 ){
						p2 = Univers.getCommandant(proprios[0]).getPossession(pos2);
					} else {
						return ajouterErreur("ER_COMMANDANT_TRANSFERER_0008", s2.getNomPosition());
					}
				}
			}

			int present = p1.getQuantiteMarchandise(Utile.numeroMarchandise(code));
			int quantite2a = p2.getQuantiteMarchandise(Utile.numeroMarchandise(code));

			int nb2 = Math.min(present, nb);

			o = new ObjetSimpleTransporte(code, nb);
			p1.supprimerMarchandise(Utile.numeroMarchandise(code), nb2);
			p2.ajouterMarchandise(Utile.numeroMarchandise(code), nb2);

			int quantite1b = p1.getQuantiteMarchandise(Utile.numeroMarchandise(code));
			int quantite2b = p2.getQuantiteMarchandise(Utile.numeroMarchandise(code));

			// System.out.println(getNumero()+" : de "+pos1+"("+pla1+") vers
			// "+pos2+"("+pla2+"), "+code+" - "+nb+" : ( #1: "+present+"
			// "+quantite1b+", #2: "+quantite2a+" "+quantite2b+" )");
			return ajouterEvenement(
					"EV_COMMANDANT_TRANSFERER_0000",
					"<font color=\"#00f1af\">"
							+ ObjetTransporte.getDescriptionListeChargement(
									new ObjetTransporte[] { o }, getLocale())
							+ "</font>", pos1, pos2);
		}

		// System.out.println(" - "+numero+" de "+pos1+"("+pla1+") "+nb+"
		// "+code+" vers "+pos2+"("+pla2+")");

		// On le retire sur s1
		o = s1.supprimerRichesses(numero, code, nb, pla1);
		Univers.setSysteme(s1);
		
		// Et on l'ajoute sur s2
		if (o != null) {
			
			s2.ajouterRichesses(numero, o, pla2);
			Univers.setSysteme(s2);

			return ajouterEvenement(
					"EV_COMMANDANT_TRANSFERER_0000",
					"<font color=\"#00f1af\">"
							+ ObjetTransporte.getDescriptionListeChargement(
									new ObjetTransporte[] { o }, getLocale())
							+ "</font>", pos1, pos2);
		} else {
			return ajouterEvenement("ER_COMMANDANT_TRANSFERER_0006");
		}
		

	}

	public boolean chargerCargo(int numFlotte, int numTransport, int nombre,
			String code, int joueur, int numPlanete) {
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CHARGER_CARGO_0000", numFlotte + 1);
		if ((joueur != Integer.MIN_VALUE)
				&& (!Univers.existenceCommandant(joueur)))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CHARGER_CARGO_0001", joueur);
		if (!ObjetTransporte.existenceCodeChargement(code))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CHARGER_CARGO_0004", code);
		Flotte f = getFlotte(numFlotte);
		if (!Univers.existenceSysteme(f.getPosition()))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CHARGER_CARGO_0002", f.getPosition());
		Systeme sys = Univers.getSysteme(f.getPosition());
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0005",
					f.getPosition(), numPlanete + 1);
		if (numTransport >= f.nbVaisseauxCargos())
			return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0001",
					f.getNomNumero(numFlotte), numTransport);
		int numJou = joueur;
		if (joueur == Integer.MIN_VALUE)
			if (sys.nbProprios() == 1)
				numJou = sys.getProprios()[0];
			else if (numPlanete != Integer.MIN_VALUE)
				numJou = sys.getPlanete(numPlanete).getProprio();
			else if (sys.estProprio(numero))
				numJou = numero;
			else
				return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0004",
						f.getNomNumero(numFlotte));
		Commandant c = Univers.getCommandant(numJou);
		if (!c.existencePossession(f.getPosition()))
			return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0002",
					f.getPosition(), c.getNomNumero());
		if ((numJou != numero)
				&& (ObjetTransporte.typeDeCodeChargement(code) != Const.TRANSPORT_MARCHANDISE))
			return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0003",
					f.getNomNumero(numFlotte));

		try {

			int transportMax = f.nombreChargementPouvantEtreTransporte(code,
					numTransport);
			if (transportMax == 0)
				return ajouterEvenement("EV_COMMANDANT_CHARGER_CARGO_0001",
						f.getPosition(), ObjetTransporte.traductionChargement(
								code, 0, getLocale()),
						f.getNomNumero(numFlotte));
			// System.out.println(getNomNumero()+"-"+numTransport+"-"+code+"-"+transportMax);
			ObjetTransporte o = c.enleverChargementDeSysteme(this, numFlotte,
					f.getPosition(), numPlanete, code,
					Math.min(transportMax, nombre));
			// System.out.println(getNomNumero()+"-"+numTransport+"-"+code+"-"+nombre);
			int charge = 0;
			if (o != null) {
				f.chargerChargement(o, numTransport);
				charge = o.getNombreObjets();
			}

			if (ObjetTransporte.typeDeCodeChargement(code) != Const.TRANSPORT_MARCHANDISE)
				if (o != null)
					return ajouterEvenement(
							"EV_COMMANDANT_CHARGER_CARGO_0000",
							f.getPosition(),
							ObjetTransporte.traductionChargement(code,
									o.getNombreObjets(), getLocale()),
							f.getNomNumero(numFlotte), charge);
				else
					return ajouterEvenement("EV_COMMANDANT_CHARGER_CARGO_0001",
							f.getPosition(),
							ObjetTransporte.traductionChargement(code, 0,
									getLocale()), f.getNomNumero(numFlotte));

		} catch (Exception e) {
			System.out.println(joueur + "-" + code + "-" + e.getMessage());
		}

		return true;
	}

	public boolean dechargerCargo(int numFlotte, int numTransport, int nombre,
			String code, int joueur, int numPlanete) {
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DECHARGER_CARGO_0000", numFlotte);
		if ((joueur != Integer.MIN_VALUE)
				&& (!Univers.existenceCommandant(joueur)))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DECHARGER_CARGO_0001", joueur);
		if (!ObjetTransporte.existenceCodeChargement(code))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DECHARGER_CARGO_0004", code);
		Flotte f = getFlotte(numFlotte);
		if (!Univers.existenceSysteme(f.getPosition()))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DECHARGER_CARGO_0002", f.getPosition());
		Systeme sys = Univers.getSysteme(f.getPosition());
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0005",
					f.getPosition(), numPlanete + 1);
		int numJou = joueur;
		if (joueur == Integer.MIN_VALUE)
			if (sys.nbProprios() == 1)
				numJou = sys.getProprios()[0];
			else if (sys.estProprio(numero))
				numJou = numero;
			else if (numPlanete != Integer.MIN_VALUE)
				numJou = sys.getPlanete(numPlanete).getProprio();
			else
				return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0004",
						f.getNomNumero(numFlotte));
		Commandant c = Univers.getCommandant(numJou);
		if (numTransport >= f.nbVaisseauxCargos())
			return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0001",
					f.getNomNumero(numFlotte), numTransport + 1);
		if (!c.existencePossession(f.getPosition()))
			return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0002",
					f.getPosition(), c.getNomNumero());

		int transportMax = f.nombreChargementTransporte(code, numTransport);
		ObjetTransporte o1 = f.dechargerChargement(code,
				Math.min(transportMax, nombre), numTransport);
		if (o1 == null)
			return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0003",
					f.getPosition(), ObjetTransporte.traductionChargement(code,
							nombre, getLocale()), f.getNomNumero(numFlotte),
					nombre);

		ObjetTransporte o2 = c.ajouterChargementSurSysteme(this, numFlotte,
				f.getPosition(), numPlanete, o1);
		if ((o2 != null) && (o2.getNombreObjets() > 0))
			f.chargerChargement(o2, numTransport);

		int nombreCharge;
		if (o2 == null)
			nombreCharge = o1.getNombreObjets();
		else
			nombreCharge = o1.getNombreObjets() - o2.getNombreObjets();
		if (ObjetTransporte.typeDeCodeChargement(code) != Const.TRANSPORT_MARCHANDISE)
			if (nombreCharge > 0) {
				ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0000",
						f.getPosition(), ObjetTransporte.traductionChargement(
								code, nombreCharge, getLocale()),
						f.getNomNumero(numFlotte), nombreCharge);
				if (c != this)
					c.ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0002", f
							.getPosition(), ObjetTransporte
							.traductionChargement(code, nombreCharge,
									c.getLocale()), f.getNomNumero(numFlotte),
							getNomNumero(), nombreCharge);
			} else
				ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0001",
						f.getPosition(), ObjetTransporte.traductionChargement(
								code, 0, getLocale()),
						f.getNomNumero(numFlotte));
		return true;
	}

	public boolean diviserFlotte(int numFlotte, String[] code, int[] nb,
			String nouveauNom, int numeroDivision) {
		if (getCorrespondanceFlotte(10000 + numeroDivision) != -1)
			return false;
		if (!existenceFlotte(getCorrespondanceFlotte(numFlotte)))
			return ajouterErreur("ER_COMMANDANT_DIVISER_FLOTTE_0000", numFlotte);
		for (int i = 0; i < code.length; i++)
			if ((!Univers.existencePlanDeVaisseau(code[i])) || (nb[i] < 0))
				return Univers.ajouterErreur(getNomNumero(),
						"ER_COMMANDANT_DIVISER_FLOTTE_0000", code[i], nb[i]);

		Flotte ancienne = getFlotte(getCorrespondanceFlotte(numFlotte));
		Flotte nouvelle = ancienne.diviserFlotte(code, nb, nouveauNom);
		if (nouvelle.getNombreDeVaisseaux() != 0) {
			ajouterFlotte(nouvelle);
			ajouterCorrespondanceFlotte(10000 + numeroDivision,
					numeroFlotte(nouvelle));
		}
		if (ancienne.getNombreDeVaisseaux() == 0)
			eliminerFlotte(numFlotte);

		return ajouterEvenement("EV_COMMANDANT_DIVISER_FLOTTE_0000",
				ancienne.getNomNumero(getCorrespondanceFlotte(numFlotte)),
				nouveauNom);
	}

	public boolean fusionnerFlotte(int nFlotte1, int nFlotte2, int directive,
			int directivePrecision) {
		if (!Flotte.directiveExistante(directive, directivePrecision))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_FUSIONNER_FLOTTE_0000", directive,
					directivePrecision);
		int numFlotte1 = getCorrespondanceFlotte(nFlotte1);
		int numFlotte2 = getCorrespondanceFlotte(nFlotte2);
		if (!existenceFlotte(numFlotte1))
			return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0000",
					numFlotte1, numFlotte2);
		if (!existenceFlotte(numFlotte2))
			return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0000",
					numFlotte2, numFlotte1);
		if (numFlotte1 == numFlotte2)
			return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0001",
					numFlotte2, numFlotte1);
		if (nFlotte1 > nFlotte2) {
			int inter = numFlotte1;
			numFlotte1 = numFlotte2;
			numFlotte2 = inter;
		}
		Flotte f1 = getFlotte(numFlotte1);
		Flotte f2 = getFlotte(numFlotte2);
		if (!f1.getPosition().equals(f2.getPosition()))
			return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0002",
					f1.getNomNumero(numFlotte1), f2.getNomNumero(numFlotte2));

		int nouveauNum = numFlotte1;
		int numSupprime = numFlotte2;

		Flotte f3 = f1.fusion(f2);
		f3.setDirectiveComplete(directive, directivePrecision);

		if ((existenceHerosSurFlotte(numFlotte1))
				&& (existenceHerosSurFlotte(numFlotte2))) {
			getHerosSurFlotte(numFlotte2).mettreEnReserve();
			getHerosSurFlotte(numFlotte1).setPosition(nouveauNum);
		} else if (existenceHerosSurFlotte(numFlotte1))
			getHerosSurFlotte(numFlotte1).setPosition(nouveauNum);
		else if (existenceHerosSurFlotte(numFlotte2))
			getHerosSurFlotte(numFlotte2).setPosition(nouveauNum);
		setFlotte(f3, nouveauNum);
		eliminerFlotte(numSupprime);

		return ajouterEvenement("EV_COMMANDANT_FUSIONNER_FLOTTE_0000",
				f1.getNomNumero(numFlotte1), f2.getNomNumero(numFlotte2));
	}

	public boolean pisterFlotte(int nFlotte, int numFlotteVisee, int joueurVise) {
		int numFlotte = getCorrespondanceFlotte(nFlotte);
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PISTER_FLOTTE_0000", numFlotte + 1);
		if (!Univers.existenceCommandant(joueurVise))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_PISTER_FLOTTE_0001", joueurVise);
		Commandant c = Univers.getCommandant(joueurVise);
		if (!c.existenceFlotte(numFlotteVisee))
			return ajouterErreur("ER_COMMANDANT_PISTER_FLOTTE_0000",
					c.getNomNumero(), numFlotteVisee + 1);

		Flotte f = getFlotte(numFlotte);
		boolean reussite = f.pister(c.getFlotte(numFlotteVisee), joueurVise,
				getHerosSurFlotte(numFlotte), c);

		if (reussite)
			return ajouterEvenement("EV_COMMANDANT_PISTER_FLOTTE_0000",
					f.getNomNumero(numFlotte), c.getNomNumero(),
					numFlotteVisee + 1);
		else
			return ajouterEvenement("EV_COMMANDANT_PISTER_FLOTTE_0001",
					f.getNomNumero(numFlotte), c.getNomNumero(),
					numFlotteVisee + 1);
	}

	public boolean lancerMines(int numFlotte) {
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_LANCER_MINES_0000", numFlotte + 1);
		Flotte f = getFlotte(numFlotte);
		if (!f.estLanceurDeMines())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_LANCER_MINES_0001", numFlotte + 1);

		int nbMines = f.lancerMines(numero);

		return ajouterEvenement("EV_COMMANDANT_LANCER_MINES_0000",
				f.getNomNumero(numFlotte), nbMines);
	}

	public boolean coloniserPlanetes(int numFlotte, int numPlanete) {

		// si la flotte n'existe pas
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_COLONISER_PLANETE_0000", numFlotte + 1);

		Flotte f = getFlotte(numFlotte);

		// Si la flotte ne contient pas de colo
		if (!f.contientColonisateur())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_COLONISER_PLANETE_0003", numFlotte + 1);

		// SI le system n'existe pas
		if (!Univers.existenceSysteme(f.getPosition()))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_COLONISER_PLANETE_0001", numFlotte + 1);

		Systeme sys = Univers.getSysteme(f.getPosition());

		// Si le num de planète est supérieur au nb de pla du systeme
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_COLONISER_PLANETE_0001",
					f.getPosition(), numFlotte + 1, numPlanete);

		// Si le joueur n'est pas proprio de la planete
		if (!sys.getPlanete(numPlanete).estProprio(numero))
			return ajouterErreur("ER_COMMANDANT_COLONISER_PLANETE_0000",
					f.getPosition(), f.getNomNumero(numFlotte),
					sys.getNomNumeroPlanete(numPlanete));

		Planete pla = sys.getPlanete(numPlanete);
		int numRace = f.trouverColonisateur().getRaceEquipage();

		// Si la race ne peut pas coloniser la planète
		if( !pla.peutEtreColoniseParRace(numRace) ){
			return ajouterEvenement("EV_COMMANDANT_COLONISER_PLANETE_0001",
					f.getPosition(), f.getNomNumero(numFlotte),
					sys.getNomNumeroPlanete(numPlanete),
					Univers.getMessage("RACES", numRace, getLocale()));
		}
		
		// Si ya déjà de la population sur le système
		if (pla.getNombreDeTypeDePopulationsPresentes() > 0) {

			// On regarde combien il y a de races sur la planete
			int[] racesPresentes = pla.racesPresentes();

			// Si il y'a plus d'une race, on la supprime
			if (racesPresentes.length > 0 ){
				pla.initialiserPopulation();
			}

		}

		boolean reussite = pla.explorerPlanete(numRace);
		if (reussite) {
			sys.getPlanete(numPlanete).ajouterPopulation(numRace, 100);
			f.supprimerVaisseau(f.trouverNumeroColonisateur());
			if (f.getNombreDeVaisseaux() == 0)
				eliminerFlotte(numFlotte);
			ajouterReputation(Const.REPUTATION_COLONISER_PLANETE);
		}

		if (reussite)
			return ajouterEvenement(
					"EV_COMMANDANT_COLONISER_PLANETE_0000",
					f.getPosition(),
					f.getNomNumero(numFlotte),
					sys.getNomNumeroPlanete(numPlanete),
					"<span class=\"race"
							+ numRace
							+ "\">"
							+ Utile.maj(Univers.getMessage("RACES", numRace,
									getLocale())) + "</span>");
		else
			return ajouterEvenement("EV_COMMANDANT_COLONISER_PLANETE_0001",
					f.getPosition(), f.getNomNumero(numFlotte),
					sys.getNomNumeroPlanete(numPlanete),
					Univers.getMessage("RACES", numRace, getLocale()));
	}

	public boolean affecterHeros(String nomHeros, int numFlotte) {
		if ((!existenceFlotte(numFlotte))
				&& (numFlotte != ProductionOrdres.VALEUR_DEFAUT.intValue()))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_AFFECTER_HEROS_0000", numFlotte + 1);
		if (!existenceHeros(nomHeros))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_AFFECTER_HEROS_0001", nomHeros);

		if (numFlotte == ProductionOrdres.VALEUR_DEFAUT.intValue()) {
			getHeros(nomHeros).mettreEnReserve();
			return true;
		} else {
			if (existenceHerosSurFlotte(numFlotte))
				getHerosSurFlotte(numFlotte).mettreEnReserve();
			getHeros(nomHeros).setPosition(numFlotte);
		}

		return ajouterEvenement("EV_COMMANDANT_AFFECTER_HEROS_0000", nomHeros,
				getFlotte(numFlotte).getNomNumero(numFlotte));
	}

	public boolean affecterGouverneur(String nomGouverneur, Position pos) {
		if ((!existencePossession(pos))
				&& (!pos.equals(ProductionOrdres.POSITION_DEFAUT)))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_AFFECTER_GOUVERNEUR_0000", pos);
		if (!existenceGouverneur(nomGouverneur))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_AFFECTER_GOUVERNEUR_0001", nomGouverneur);

		if (pos.equals(ProductionOrdres.POSITION_DEFAUT)) {
			getGouverneur(nomGouverneur).mettreEnReserve();
			return true;
		} else {
			if (existenceGouverneurSurPossession(pos))
				getGouverneurSurPossession(pos).mettreEnReserve();
			getGouverneur(nomGouverneur).setPosition(pos);
		}

		return ajouterEvenement("EV_COMMANDANT_AFFECTER_GOUVERNEUR_0000",
				nomGouverneur, pos);
	}

	public boolean licencierLieutenant(String nomLieutenant) {
		if (!existenceLieutenant(nomLieutenant))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_LICENCIER_LIEUTENANT_0000", nomLieutenant);

		Leader l = getLieutenant(nomLieutenant);
		supprimerLieutenant(nomLieutenant);
		modifierBudget(Const.BUDGET_COMMANDANT_LICENCIER_LIEUTENANT,
				l.getValeur());
		Univers.ajouterLeaderEnVente(l);

		return ajouterEvenement("EV_COMMANDANT_LICENCIER_LIEUTENANT_0000",
				nomLieutenant, l.getValeur());
	}

	public boolean renommerLieutenant(String nomLieutenant, String nouveauNom) {
		if (!existenceLieutenant(nomLieutenant))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_RENOMMER_LIEUTENANT_0000", nomLieutenant);
		Leader l = getLieutenant(nomLieutenant);
		if (l.estNomme())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_RENOMMER_LIEUTENANT_0001", nomLieutenant);
		if (existenceLieutenant(nouveauNom))
			return ajouterErreur("ER_COMMANDANT_RENOMMER_LIEUTENANT_0000",
					nomLieutenant, nouveauNom);

		l.setNom(nouveauNom);

		return true;
	}

	public boolean changerCapitale(Position pos) {
		if (!existencePossession(pos))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CHANGER_CAPITALE_0000", pos);
		if (Const.COUT_CHANGER_CAPITALE > centaures)
			return ajouterErreur("ER_COMMANDANT_CHANGER_CAPITALE_0000", pos);

		modifierBudget(Const.BUDGET_COMMANDANT_CHANGER_CAPITALE,
				-Const.COUT_CHANGER_CAPITALE);
		setCapitale(pos);

		return ajouterEvenement("EV_COMMANDANT_CHANGER_CAPITALE_0000", pos);
	}

	public boolean donnerPlanDeVaisseau(String code, int destinataire,
			int modeTransfert) {
		if (!estPlanDeVaisseauConnuPrive(code)
				&& !Univers.getPlanDeVaisseau(code).estConcepteur(numero))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_PLAN_0000", code);
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_PLAN_0001", destinataire);
		Commandant c = Univers.getCommandant(destinataire);
		if (c.estPlanDeVaisseauConnuPrive(code))
			return ajouterErreur("ER_COMMANDANT_DON_PLAN_0000",
					c.getNomNumero(), code);
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_PLAN_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_PLAN_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_PLAN_0001", code,
					destinataire);

		modifierBudget(Const.BUDGET_COMMANDANT_DON_PLAN, -cout);
		c.ajouterPlanDeVaisseau(code);
		Univers.ajouterTransfert(this, c, "don plan : " + code);

		ajouterEvenement("EV_COMMANDANT_DON_PLAN_0000", c.getNomNumero(), code);
		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_PLAN_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0003",
					getNomNumero(), c.getNomNumero());
		if (modeTransfert == Const.DON_MODE_ANONYME)
			return c.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0002", code);
		else
			return c.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0001",
					getNomNumero(), code);
	}

	public boolean creerPlanDeVaisseau(String code, String marque,
			String domaine, int royalties, String[] compo, int[] nbcompo) {

		code = Univers.supprimerAccent(code);	
		
		if (((Univers.existenceCode(code)) && (marque == "remote"))
				&& (royalties == -1)) {
			Univers.supprimerPlanDeVaisseau(code);
			return ajouterErreur("EV_COMMANDANT_CREER_PLAN_0001", code);
		}

		else {
			
			if (royalties > Const.ROYALTIES_MAXIMALES)
				return Univers.ajouterErreur(getNomNumero(),
						"ER_COMMANDANT_CREER_PLAN_0000", royalties);
			if (!PlanDeVaisseau.verificationConformite(this, compo, code)){
				System.out.println("failed conformité");
				return false;
			}
			int[] acces = PlanDeVaisseau.traductionDomaine(domaine);
			ArrayList a = new ArrayList();
			for (int i = 0; i < compo.length; i++)
				for (int j = 0; j < nbcompo[i]; j++)
					a.add(compo[i]);
			String[] compo2 = (String[]) a.toArray(new String[0]);
			PlanDeVaisseau p = new PlanDeVaisseau(numero, getNom(), compo2,
					code, marque, "", acces[0], acces[1], royalties,
					PlanDeVaisseau.determinerCaracteristiquesSpeciales(this,
							compo2),
					PlanDeVaisseau.determinerMineraiNecessaire(this, compo2),
					PlanDeVaisseau.determinerPrix(this, compo2, royalties),
					PlanDeVaisseau.determinerMarchandisesNecessaires(this,
							compo2),
					PlanDeVaisseau.determinerNombreDeCases(compo2),
					Univers.getTour(), true);
			if (centaures < p.getPrix()
					* Const.MODIFICATEUR_MULTIPLICATEUR_CREATION)
				return ajouterErreur("ER_COMMANDANT_CREER_PLAN_0000", code);

			Univers.ajouterPlanDeVaisseau(p);
			if (p.estPrive())
				ajouterPlanDeVaisseau(code);
			modifierBudget(Const.BUDGET_COMMANDANT_CREATION_PLAN, -p.getPrix()
					* Const.MODIFICATEUR_MULTIPLICATEUR_CREATION);

			return ajouterEvenement("EV_COMMANDANT_CREER_PLAN_0000", code);
		}
	}

	public boolean creerStrategie(String code, int agressivite,
			int ciblePrincipale, String[] vaisseau, int[][] pos,
			int[][] tailleCible) {

		code = Univers.supprimerAccent(code);
		
		if (connaitStrategie(code))
			return ajouterErreur("ER_COMMANDANT_CREER_STRATEGIE_0000", code);
		boolean[] b = null;
		if ((b = StrategieDeCombatSpatial.estStrategieValide(this, agressivite,
				ciblePrincipale, vaisseau, pos, tailleCible)) == null)
			return false;

		int nb = 0;
		for (int i = 0; i < b.length; i++)
			if (b[i])
				nb++;
		String[] v = new String[nb];
		int[][] p = new int[nb][2];
		int[][] t = new int[nb][10];
		nb = 0;
		for (int i = 0; i < b.length; i++)
			if (b[i]) {
				v[nb] = vaisseau[i];
				p[nb] = pos[i];
				t[nb++] = tailleCible[i];
			}
		StrategieDeCombatSpatial s = new StrategieDeCombatSpatial(code,
				agressivite, ciblePrincipale, v, p, t);
		ajouterStrategie(s);

		return ajouterEvenement("EV_COMMANDANT_CREER_STRATEGIE_0000", code);
	}

	public boolean donnerStrategie(String code, int destinataire,
			int modeTransfert) {
		if (!connaitStrategie(code))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_STRATEGIE_0000", code);
		if (!Univers.existenceCommandant(destinataire))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_DON_STRATEGIE_0001", destinataire);
		Commandant c = Univers.getCommandant(destinataire);
		if (c.connaitStrategie(code))
			return ajouterErreur("ER_COMMANDANT_DON_STRATEGIE_0000",
					c.getNomNumero(), code);
		float cout = 0F;
		if (modeTransfert == Const.DON_MODE_CACHE)
			cout = Const.SURCOUT_DON_STRATEGIE_CACHE;
		if (modeTransfert == Const.DON_MODE_ANONYME)
			cout = Const.SURCOUT_DON_STRATEGIE_ANONYME;
		if ((cout != 0F) && (cout > centaures))
			return ajouterErreur("ER_COMMANDANT_DON_STRATEGIE_0001", code,
					destinataire);

		modifierBudget(Const.BUDGET_COMMANDANT_DON_STRATEGIE, -cout);
		c.ajouterStrategie(getStrategie(code));

		ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0000", c.getNomNumero(),
				code);
		if ((modeTransfert == Const.DON_MODE_NORMAL)
				&& (Univers.getTest(Const.CHANCE_DON_STRATEGIE_PUBLIC)))
			Univers.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0003",
					getNomNumero(), c.getNomNumero());
		if (modeTransfert == Const.DON_MODE_ANONYME)
			return c.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0002", code);
		else
			return c.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0001",
					getNomNumero(), code);
	}

	public boolean renommerSysteme(Position pos, String nouveauNom) {
		if (!existencePossession(pos))
			ajouterErreur("ER_COMMANDANT_RENOMMER_SYSTEME_0000", pos);
		Systeme sys = Univers.getSysteme(pos);
		if (sys.nbProprios() != 1)
			return ajouterErreur("ER_COMMANDANT_RENOMMER_SYSTEME_0000", pos);

		sys.setNom(nouveauNom);
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_RENOMMER_SYSTEME_0000", pos);
	}

	public boolean renommerPlanete(Position pos, String nouveauNom,
			int numPlanete) {
		if (!existencePossession(pos))
			return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0000", pos,
					numPlanete + 1);
		Systeme sys = Univers.getSysteme(pos);
		if (numPlanete >= sys.getNombrePlanetes())
			return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0000", pos,
					numPlanete + 1);
		if (!sys.getPlanete(numPlanete).estProprio(numero))
			return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0001", pos,
					sys.getNomNumeroPlanete(numPlanete));

		sys.getPlanete(numPlanete).setNom(nouveauNom);
		Univers.setSysteme(sys);

		return ajouterEvenement("EV_COMMANDANT_RENOMMER_PLANETE_0000", pos,
				sys.getNomNumeroPlanete(numPlanete), nouveauNom);
	}

	public boolean renommerFlotte(int numFlotte, String nouveauNom) {
		if (!existenceFlotte(numFlotte))
			return ajouterErreur("ER_COMMANDANT_RENOMMER_FLOTTE_0000",
					numFlotte + 1);

		getFlotte(numFlotte).setNom(nouveauNom);

		return ajouterEvenement("EV_COMMANDANT_RENOMMER_FLOTTE_0000",
				nouveauNom, numFlotte + 1);
	}

	public boolean programmerConstructionFlotte(int numFlotte,
			String codeConstruction) {
		if (!existenceFlotte(numFlotte))
			return ajouterErreur("ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000",
					numFlotte + 1);
		Flotte f = getFlotte(numFlotte);
		if (!f.aCapaciteNavireUsine())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CONSTRUCTION_FLOTTE_0001", numFlotte + 1);
		if (!estPlanDeVaisseauConnu(codeConstruction))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000", codeConstruction);

		f.setConstructionEnCours(codeConstruction);

		return ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_FLOTTE_0000",
				codeConstruction, numFlotte + 1);
	}

	public boolean utiliserPorteGalactique(int numFlotte, int galaxie) {
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",
					numFlotte + 1);
		Flotte f = getFlotte(numFlotte);
		boolean bonnePlace = false;
		for (int i = 0; i < Const.PASSAGES_GALACTIQUES.length; i++)
			if (Arrays.equals(Const.PASSAGES_GALACTIQUES[i], f.getPosition()
					.getPos()))
				bonnePlace = true;
		if ((!bonnePlace)
				&& (!f.estInterGalactique())
				&& (existenceHerosSurFlotte(numFlotte) ? !getHerosSurFlotte(
						numFlotte).possedeCompetence(
						Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE) : true))
			return ajouterErreur(
					"ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",
					numFlotte + 1);
		if (galaxie >= Const.NB_GALAXIES)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0001", galaxie);

		f.getPosition().setNumeroGalaxie(galaxie);

		return ajouterEvenement("EV_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",
				numFlotte + 1);
	}

	public boolean utiliserPorteIntraGalactique(int numFlotte) {
		if (!existenceFlotte(numFlotte))
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",
					numFlotte + 1);
		Flotte f = getFlotte(numFlotte);
		if (!f.estSurPorteIntraGalactique())
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0001",
					numFlotte + 1);

		float cout = (float) f.nombreTotalCases();

		if (centaures < cout)
			return ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",
					numFlotte + 1);

		int[] num = f.numeroPorteIntraGalactique();
		if (num[1] == 0)
			f.getPosition().setPos(
					new int[] { Const.PORTES[num[0]][3],
							Const.PORTES[num[0]][4] });
		else
			f.getPosition().setPos(
					new int[] { Const.PORTES[num[0]][1],
							Const.PORTES[num[0]][2] });
		modifierBudget(
				Const.BUDGET_COMMANDANT_FRANCHISSEMENT_PORTE_INTRAGALACTIQUE,
				-cout);
		f.marquerDeplacement();

		return ajouterEvenement(
				"EV_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",
				numFlotte + 1);
	}

	public boolean fixerTauxPostes(int taux) {
		if (taux > 100)
			return Univers.ajouterErreur(getNomNumero(),
					"ER_COMMANDANT_TAUX_POSTE_0000", taux);

		tauxTaxationPoste = taux;

		return ajouterEvenement("EV_COMMANDANT_TAUX_POSTE_0000", taux);
	}
	

	public int getNombreMaximalDeTransfertEntreSysteme(){
		int max = 1;

		// Chaque système possédé à 75% permet un transfert
		Systeme[] listeSystemes = Univers.listeSystemes(listePossession());
		for(Systeme s:listeSystemes){
			int nombreDePlanetePossedee = s.getNombrePlanetesPossedees(getNumero());
			if( nombreDePlanetePossedee/s.getNombrePlanetes() > 0.75 ){
				max++;
			}
		}
		
		// Chaque niveau de la maitrise spatiale permet deux transfert
		max += getNiveauMaxMaitriseSpatiale() * 5;
		
		/**
		 * Patch Zwaia
		 */
		if( getRace() == 2){
			max = (int)(max * 120/100);
		}
		
		return max;
	}
	

	public int getNiveauMaxMaitriseSpatiale() {
		int spatial = 0;
		if (estTechnologieConnue("maitstargateI"))
			spatial = 1;
		if (estTechnologieConnue("maitstargateII"))
			spatial = 2;
		if (estTechnologieConnue("maitstargateIII"))
			spatial = 3;
		if (estTechnologieConnue("maitstargateIV"))
			spatial = 4;
		if (estTechnologieConnue("maitstargateV"))
			spatial = 5;
		if (estTechnologieConnue("maitstargateVI"))
			spatial = 6;
		if (estTechnologieConnue("maitstargateVII"))
			spatial = 7;
		if (estTechnologieConnue("maitstargateVIII"))
			spatial = 8;
		if (estTechnologieConnue("maitstargateIX"))
			spatial = 9;
		if (estTechnologieConnue("maitstargateX"))
			spatial = 10;
		return spatial;
	}

	public int getDebitMaximalPorteSpatiale(){
		return Const.EFFETS_MAITRISE_SPATIALE[getNiveauMaxMaitriseSpatiale()];
	}

}
