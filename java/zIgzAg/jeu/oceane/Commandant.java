// v2.04 24/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;

/**
 * @author  Julien Buret,Roland Trouville
 * @version 2.04, 24/02/01
 */

import zIgzAg.utile.Mdt;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.io.Serializable;

public class Commandant extends Joueur implements Serializable{

  static final long serialVersionUID=-5043427151493459788L;

  private transient Commentaire evenement;
  private transient Commentaire erreur;
  private transient Commentaire combat;
  private transient Commentaire ordres;

  private Position capitale;

  private Map domaine;
  private TreeMap flottes;

  private HashMap recherches;

  private int reputation;
  private float centaures;
  private int tauxTaxationPoste;

  private ArrayList alliances;
  private ArrayList pactesDeNonAgression;
  private ArrayList technologiesConnues;
  private ArrayList plansDeVaisseaux;
  private ArrayList heros;
  private ArrayList gouverneurs;
  private HashMap strategies;

  private transient TreeMap budget;
  private transient ArrayList positionsEspionnees;
  private transient ArrayList systemesDetectes;
  private transient ArrayList flottesDetectees;

  private transient HashMap correspondanceFlotteDivisee;

  //pour contrer la "spéculation"
  private transient Map speculation;

  private void ajouterVisaDechargement(Position p,int numeroCommandant,int numeroMarchandise){
   if(speculation==null) speculation=new HashMap();
   Map m=(Map)speculation.get(p);
   if(m==null) m=new HashMap();
   Set s=(Set)m.get(new Integer(numeroCommandant));
   if(s==null) s=new HashSet();
   s.add(new Integer(numeroMarchandise));
   m.put(new Integer(numeroCommandant),s);
   speculation.put(p,m);
   }

  private boolean existenceVisaDechargement(Position p,int numeroCommandant,int numeroMarchandise){
   if(speculation==null) return false;
   Map m=(Map)speculation.get(p);
   if(m==null) return false;
   Set s=(Set)m.get(new Integer(numeroCommandant));
   if(s==null) return false;
   return s.contains(new Integer(numeroMarchandise));
   }


 //les méthodes d'accès.

  public void setCapitale(Position pos){capitale=pos;}
  public boolean existenceCapitale(){if(capitale==null) return false; else return true;}
  public boolean estCapitale(Position pos){if(capitale==null) return false;else return capitale.equals(pos);}
  public void supprimerCapitale(){capitale=null;}
  public Position getCapitale(){return capitale;}
  public int getTauxTaxationPoste(){return tauxTaxationPoste;}

  public void setReputation(int rep){reputation=rep;}
  public void setCentaures(float fric){centaures=fric;}
  public void setTauxTaxationPoste(int e){tauxTaxationPoste=e;}

  public float getCentaures(){return centaures;}

  public boolean estRuine(){return centaures<0F;}

  public void ajouterReputation(int ajout){reputation=reputation+ajout;}
  public int getReputation(){return reputation;}

  public int getStatutReputationIndex(){
   if(reputation<-10000) return 0;
   else if(reputation<-5000) if(numero==5) return 0; else return 1;
   else if(reputation<-1000) if(numero==5) return 0; else return 2;
   else if(reputation<1000) return 3;
   else if(reputation<5000) return 4;
   else return 5;
   }
  public String getStatutReputation(){
   return Univers.getMessage("REPUTATION",getStatutReputationIndex(),getLocale());
   }

  public void initialiserFlottes(){flottes=new TreeMap();}
  public void initialiserBudget(){budget=new TreeMap();}
  public void initialiserDomaine(){domaine=new TreeMap();}
  public void initialiserDomainesDeRecherche(){recherches=new HashMap(11);}
  public void initialiserAlliances(){alliances=new ArrayList(2);}
  public void initialiserHeros(){heros=new ArrayList(0);}
  public void initialiserGouverneurs(){gouverneurs=new ArrayList(0);}
  public void initialiserPactesDeNonAgression(){pactesDeNonAgression=new ArrayList(0);}
  public void initialiserTechnologiesConnues(){technologiesConnues=new ArrayList(0);}
  public void initialiserPlansDeVaisseaux(){plansDeVaisseaux=new ArrayList(0);}
  public void initialiserPositionsEspionnees(){positionsEspionnees=new ArrayList(0);}
  public void initialiserStrategies(){strategies=new HashMap(11);}
  public void initialiserSystemesDetectes(){systemesDetectes=new ArrayList();}
  public void initialiserFlottesDetectees(){flottesDetectees=new ArrayList();}
  public void initialiserCorrespondanceFlotteDivisee(){correspondanceFlotteDivisee=new HashMap(11);}

  public float getBudget(int index){
   Object o=budget.get(new Integer(index));
   if(o==null) return 0F;
    else return ((Float)o).floatValue();
   }

  public void setBudget(int index,float somme){
   budget.put(new Integer(index),new Float(somme+getBudget(index)));
   }

  public void modifierBudget(int index,float somme){
   setBudget(index,somme);
   centaures=centaures+somme;
   }

  public StrategieDeCombatSpatial getStrategie(String code){
   Object o=strategies.get(code);
   if(StrategieDeCombatSpatial.estStrategieParDefaut(code)||(o==null)) return Const.STRATEGIE_DEFAUT;
    else return (StrategieDeCombatSpatial)o;
   }

  public boolean connaitStrategie(String code){
   return strategies.containsKey(code);
   }

  public String[] listeCodesStrategies(){
   return (String[])strategies.keySet().toArray(new String[0]);
   }

  public void ajouterStrategie(StrategieDeCombatSpatial ajout){
   strategies.put(ajout.getNom(),ajout);
   }

  public Map listeEquipement(){
   Position[] p=listePossession();
   HashMap h=new HashMap(11);
   for(int i=0;i<p.length;i++){
    Map.Entry[] m=Univers.getSysteme(p[i]).listeEquipement(numero);
    for(int j=0;j<m.length;j++){
     Object o=h.get(m[j].getKey());
     if(o==null) h.put(m[j].getKey(),m[j].getValue());
      else h.put(m[j].getKey(),new Integer(((Integer)o).intValue()+((Integer)m[j].getValue()).intValue()));
     }
    }
   return h;
   }

  public Map listeVaisseauxTriee(){
   HashMap h=new HashMap();
   Flotte[] f=listeFlottes();
   for(int i=0;i<f.length;i++){
    Vaisseau[] v=f[i].listeVaisseaux();
    for(int j=0;j<v.length;j++){
     Object o=h.get(v[j].getType());
     if(o==null) h.put(v[j].getType(),new Integer(1));
      else h.put(v[j].getType(),new Integer(((Integer)o).intValue()+1));
     }
    }
   return h;
   }

  public ObjetTransporte[] listeCargaisonTransporteeTriee(){
   Flotte[] f=listeFlottes();
   ArrayList retour=new ArrayList();
   for(int i=0;i<f.length;i++){
    ObjetTransporte[] inter=f[i].listeCargaisonTransporteTriee();
    for(int j=0;j<inter.length;j++){
     boolean dejaVu=false;
     for(int k=0;k<retour.size();k++)
      if(inter[j].estDeType(((ObjetTransporte)retour.get(k)).getCode())){
       ((ObjetTransporte)retour.get(k)).ajout(inter[j]);
       dejaVu=true;
       }
     if(!dejaVu) retour.add(inter[j].clone());
     }
    }
   return (ObjetTransporte[])retour.toArray(new ObjetTransporte[0]);
   }

  public void ajouterPossession(Position pos,Possession entree){domaine.put(pos,entree);}

  public void eliminerPossession(Position pos){
   domaine.remove(pos);
   if(estCapitale(pos)) supprimerCapitale();
   if(existenceGouverneurSurPossession(pos)) getGouverneurSurPossession(pos).setPosition(null);
   }

  public Possession getPossession(Position pos){
   return (Possession)domaine.get(pos);
   }

  public void transfererPossession(Position pos,Possession possession){
   if(existencePossession(pos)){
    Possession p=getPossession(pos);
    p.transfererPossession(possession);
    }
    else ajouterPossession(pos,possession);
   }

  public boolean existencePossession(Position pos){
   return domaine.containsKey(pos);
   }

  public Position[] listePossession(){
   return (Position[])domaine.keySet().toArray(new Position[0]);
   }

  public Possession[] listeVraiePossession(){
   return (Possession[])domaine.values().toArray(new Possession[0]);
   }

  public int getNombrePossessions(){return domaine.size();}

  public int getNombrePlanetesPossedees(){
   Position[] p=listePossession();
   int retour=0;
   for(int i=0;i<p.length;i++)
    retour=retour+Univers.getSysteme(p[i]).getNombrePlanetesPossedees(numero);
   return retour;
   }

  public String getGrade(){
   int n=getNombrePlanetesPossedees();
   int index=0;
   if(n<10) index=0;
    else if(n<20) index=1;
     else if(n<50) index=2;
      else if(n<100) index=3;
       else if(n<150) index=4;
        else if(n<200) index=5;
         else if(n<300) index=6;
          else index=7;
   return Univers.getMessage("GRADE",index,getLocale());
   }

  public int getPuissanceFlottes(){
   int retour=0;
   Flotte[] f=listeFlottes();
   for(int i=0;i<f.length;i++)
    retour=retour+f[i].getPuissance();
   return retour;
   }

  public int getPuissanceSystemes(){
   Position[] p=listePossession();
   int retour=0;
   for(int i=0;i<p.length;i++)
    retour=retour+Univers.getSysteme(p[i]).getPuissance(numero);
   return retour;
   }

  public int getValeurTotaleLeaders(){
   Leader[] l=listeLieutenants();
   float total=0F;
   for(int i=0;i<l.length;i++)
    total=total+l[i].getValeur();
   return (int)total;
   }

  public int getPuissance(){
   return 10*getPuissanceFlottes()+getPuissanceSystemes()+nombreTechnologiesNonPubliquesConnues()*100
          +getValeurTotaleLeaders()+(int)centaures;
   }

  public int getNombrePopulationDeRace(int r){
   int retour=0;
   Position[] p=listePossession();
   for(int i=0;i<p.length;i++)
    retour=retour+Univers.getSysteme(p[i]).getPopulationDeRace(numero,r);
   return retour;
   }

  public int getPopulationTotale(){
   int retour=0;
   Position[] p=listePossession();
   for(int i=0;i<p.length;i++)
    retour=retour+Univers.getSysteme(p[i]).getPopulation(numero);
   return retour;
   }

  private float getBudgetPossession(int typeBudget){
   float retour=0F;
   Position[] liste=listePossession();
   for(int i=0;i<liste.length;i++){
    Possession p=getPossession(liste[i]);
    float t=0F;
    if(typeBudget==Const.DOMAINES_BUDGET_TECHNOLOGIQUE)
     t=Univers.getSysteme(liste[i]).getRevenuTechnologique(numero,getGouverneurSurPossession(liste[i]),p)*p.getBudget(typeBudget);
     else t=Univers.getSysteme(liste[i]).getRevenu(numero,getGouverneurSurPossession(liste[i]),p)*p.getBudget(typeBudget);

    if(  (typeBudget==Const.DOMAINES_BUDGET_TECHNOLOGIQUE)&&(p.possedeStockImportantPoste(Const.PRODUIT_LOGICIEL)))
     t=t+(25*t)/100F;
    if(((typeBudget==Const.DOMAINES_BUDGET_SERVICES_SPECIAUX)||(typeBudget==Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE))
        &&(p.possedeStockImportantPoste(Const.PRODUIT_COMPOSANT_ELECTRONIQUE)))
            t=t+(25*t)/100F;

    retour=retour+t;
    }
   return retour/100F;
   }

  public float getBudgetServiceSpeciaux(){
   return getBudgetPossession(Const.DOMAINES_BUDGET_SERVICES_SPECIAUX);
   }

  public float getBudgetTechnologique(){
   return getBudgetPossession(Const.DOMAINES_BUDGET_TECHNOLOGIQUE);
   }

  public float getBudgetContreEspionnage(){
   return getBudgetPossession(Const.DOMAINES_BUDGET_CONTRE_ESPIONNAGE);
   }

  public float getEntretienLieutenants(){
   float retour=0F;
   Leader[] l=listeLieutenants();
   for(int i=0;i<l.length;i++) retour=retour+l[i].getEntretien();
   return retour;
   }

  public Leader[] listeLieutenants(){
   Heros[] h=listeHeros();
   Gouverneur[] g=listeGouverneur();
   Leader[] l=new Leader[h.length+g.length];
   for(int i=0;i<h.length;i++) l[i]=h[i];
   for(int i=h.length;i<h.length+g.length;i++) l[i]=g[i-h.length];
   return l;
   }

  public Leader getLieutenant(String nomLieutenant){
   Leader[] h=listeLieutenants();
   for(int i=0;i<h.length;i++)
    if(h[i].getNom().equals(nomLieutenant)) return h[i];
   return null;
   }

  public boolean existenceLieutenant(String nomLieutenant){
   return getLieutenant(nomLieutenant)!=null;
   }

  public void supprimerLieutenant(String nomLieutenant){
   Leader l=getLieutenant(nomLieutenant);
   if(l instanceof Heros) heros.remove(l);
    else gouverneurs.remove(l);
   }

  public void ajouterHeros(Heros h){heros.add(h);}

  public Heros getHeros(String nomHeros){
   Heros[] h=listeHeros();
   for(int i=0;i<h.length;i++)
    if(h[i].getNom().equals(nomHeros)) return h[i];
   return null;
   }

  public boolean existenceHeros(String nomHeros){
   return getHeros(nomHeros)!=null;
   }

  public Heros[] listeHeros(){
   return (Heros[])heros.toArray(new Heros[0]);
   }

  public boolean existenceHerosSurFlotte(int num){
   Heros[] h=listeHeros();
   for(int i=0;i<h.length;i++) if(h[i].getPosition()==num) return true;
   return false;
   }

  public Heros getHerosSurFlotte(int num){
   Heros[] h=listeHeros();
   for(int i=0;i<h.length;i++) if(h[i].getPosition()==num) return h[i];
   return null;
   }

  public Gouverneur getGouverneur(String nomGouverneur){
   Gouverneur[] h=listeGouverneur();
   for(int i=0;i<h.length;i++)
    if(h[i].getNom().equals(nomGouverneur)) return h[i];
   return null;
   }

  public boolean existenceGouverneur(String nomGouverneur){
   return getGouverneur(nomGouverneur)!=null;
   }

  public void ajouterGouverneur(Gouverneur g){gouverneurs.add(g);}

  public Gouverneur[] listeGouverneur(){
   return (Gouverneur[])gouverneurs.toArray(new Gouverneur[0]);
   }

  public boolean existenceGouverneurSurPossession(Position pos){
   Gouverneur[] h=listeGouverneur();
   for(int i=0;i<h.length;i++) if(pos.equals(h[i].getPosition())) return true;
   return false;
   }

  public Gouverneur getGouverneurSurPossession(Position pos){
   Gouverneur[] h=listeGouverneur();
   for(int i=0;i<h.length;i++) if(pos.equals(h[i].getPosition())) return h[i];
   return null;
   }

  public Flotte[] listeFlottes(){
   return (Flotte[])flottes.values().toArray(new Flotte[0]);
   }

  public Map clonerListeFlottes(){
   return (Map)flottes.clone();
   }

  public Map.Entry[] listeFlottesEtNumeros(){
   return (Map.Entry[])flottes.entrySet().toArray(new Map.Entry[0]);
   }

  public Map.Entry[] listeFlottesEtNumerosCargosSurPoste(){
   Map.Entry[] m=listeFlottesEtNumeros();
   ArrayList a=new ArrayList(m.length);
   for(int i=0;i<m.length;i++)
    if(((Flotte)m[i].getValue()).estTransporteur())
     if(Univers.existenceSysteme(((Flotte)m[i].getValue()).getPosition()))
      a.add(m[i]);
   return (Map.Entry[])a.toArray(new Map.Entry[0]);
   }

  public Integer[] listeNumerosFlottes2(){
   return (Integer[])flottes.keySet().toArray(new Integer[0]);
   }

  public int[] listeNumerosFlottes(){
   return Utile.transformer(listeNumerosFlottes2());
   }

  public int numeroFlotte(Flotte f){
   Flotte[] lf=listeFlottes();
   Integer[] ln=listeNumerosFlottes2();
   for(int i=0;i<lf.length;i++)
    if(lf[i].equals(f)) return ln[i].intValue();
   return -1;
   }

  public int numeroFlotteDisponible(){
   int[] travail=listeNumerosFlottes();
   for(int i=0;i<travail.length;i++)
    if(travail[i]!=i) return i;
   return travail.length;
   }

  public boolean existenceFlotte(int num){
   return flottes.containsKey(new Integer(num));
   }

  public void ajouterFlotte(Flotte entree){
   flottes.put(new Integer(numeroFlotteDisponible()),entree);
   }

  public void eliminerFlotte(int num){
   flottes.remove(new Integer(num));
   if(existenceHerosSurFlotte(num)) getHerosSurFlotte(num).setPosition(-1);
   }

  public Flotte getFlotte(int num){
   return (Flotte)flottes.get(new Integer(num));
   }

  public void setFlotte(Flotte f,int num){
   flottes.put(new Integer(num),f);
   }

  public int getNombreDeFlottes(){return flottes.size();}

  public boolean nombreAlliancesTropGrand(){
	  return false;
   //if(alliances.size()>=Const.NB_MAX_ALLIANCES) return true;
   // else return false;
   }

  public void ajouterAlliance(int numeroAlliance){
   alliances.add(new Integer(numeroAlliance));
   }

  public void enleverAlliance(int numeroAlliance){
   Alliance a=Univers.getAlliance(numeroAlliance);
   if(a.estDirigeePar(numero)) a.setDirigeant(-1);
   alliances.remove(new Integer(numeroAlliance));
   }

  public int[] numerosAlliances(){
   return Utile.transformer((Integer[])alliances.toArray(new Integer[0]));
   }

  public boolean appartientAAlliance(int numeroAlliance){
   return alliances.contains(new Integer(numeroAlliance));
   }

  public int[] getAllianceDirigee(){
   int[] a=numerosAlliances();
   List l=new ArrayList(a.length);
   for(int i=0;i<a.length;i++)
    if(Univers.getAlliance(a[i]).estDirigeePar(numero)) l.add(new Integer(a[i]));
   return Utile.transformer((Integer[])l.toArray(new Integer[0]));
   }

  public boolean estDirigeantAlliance(){
   return (getAllianceDirigee().length!=0);
   }

  public int getPlaceAlliance(int numeroAlliance){
   return alliances.indexOf(new Integer(numeroAlliance));
   }

/*
  public boolean nombreDomainesDeRechercheTropGrand(){
   if(recherches.size()>=Const.NB_MAX_RECHERCHES) return true;
    else return false;
   }
*/
  public boolean existenceDomaineDeRecherche(String code){
   return recherches.containsKey(code);
   }

  public int totalAffectationPourcentage(){
   int retour=0;
   int[][] t=(int[][])recherches.values().toArray(new int[0][0]);
   for(int i=0;i<t.length;i++)
    retour=retour+t[i][0];
   return retour;
   }

  public void suppressionDomaineDeRecherche(String code){
   recherches.remove(code);
   }

  public int nombreDePointsDeRecherche(String code){
   return ((int[])recherches.get(code))[1];
   }

  public void ajouterPointsDeRecherche(String code,int nb){
   creationDomaineDeRecherche(code,pourcentageAffecte(code),nombreDePointsDeRecherche(code)+nb);
   }

  public int pourcentageAffecte(String code){
   return ((int[])recherches.get(code))[0];
   }

  public boolean ajouterDomaineDeRecherche(String code,int pourcentage){
   if(existenceDomaineDeRecherche(code))
    creationDomaineDeRecherche(code,pourcentage,nombreDePointsDeRecherche(code));
    else creationDomaineDeRecherche(code,pourcentage,0);
   return true;
   }

  public String[] recherchesActuelles(){
   return (String[])recherches.keySet().toArray(new String[0]);
   }

  public void creationDomaineDeRecherche(String code,int pourcentage,int pointsDejaTrouves){
   int[] carac=new int[2];
   carac[0]=pourcentage;
   carac[1]=pointsDejaTrouves;
   recherches.put(code,carac);
   }

  public void ajouterPacteDeNonAgression(int numeroJoueur){
   pactesDeNonAgression.add(new Integer(numeroJoueur));
   }

  public boolean existencePacteDeNonAgression(int numeroJoueur){
   return pactesDeNonAgression.contains(new Integer(numeroJoueur));
   }

  public void romprePacteDeNonAgression(int numeroJoueur){
   pactesDeNonAgression.remove(new Integer(numeroJoueur));
   }

  public int[] listePactesDeNonAgression(){
   return Utile.transformer((Integer[])pactesDeNonAgression.toArray(new Integer[0]));
   }

  public String[] listeTechnologiesPouvantEtreCherchees(){
   return Technologie.listeDesTechnologiesAtteignables(listeTechnologiesConnues());
   }

  public boolean peutChercherTechnologie(String code){
   return Mdt.estPresent(listeTechnologiesPouvantEtreCherchees(),code);
   }

  public void ajouterTechnologieConnue(String code){
   if(!estTechnologieConnue(code))
    technologiesConnues.add(code);
   }

  public boolean estTechnologieConnue(String code){
   return technologiesConnues.contains(code);
   }

  public void eliminerTechnologieConnue(String code){
   technologiesConnues.remove(code);
   }

  public String[] listeTechnologiesNonPubliquesConnues(){
   return (String[])technologiesConnues.toArray(new String[0]);
   }

  public String[] listeTechnologiesConnues(){
   return (String[])Mdt.fusion(Univers.getListeCodeTechnologiePubliques(),listeTechnologiesNonPubliquesConnues());
   }

  public int nombreTechnologiesNonPubliquesConnues(){
   return technologiesConnues.size();
   }

  public float getEntretienTechnologies(){
   int compteur=0;
   String[] l=listeTechnologiesNonPubliquesConnues();
   for(int i=0;i<l.length;i++)
    if(!Univers.getTechnologie(l[i]).estComposantDeVaisseau()) compteur++;
   return compteur*getBudget(Const.BUDGET_COMMANDANT_REVENUS_SYSTEMES)/100;
   }

  public void ajouterPlanDeVaisseau(String code){
   plansDeVaisseaux.add(code);
   }

  public void supprimerPlanDeVaisseau(String code){
   plansDeVaisseaux.remove(code);
   }

  public String[] listePlansPrives(){
   return (String[])plansDeVaisseaux.toArray(new String[0]);
   }

  public PlanDeVaisseau[] listePlansConnusNonPublics(){
   ArrayList a=new ArrayList();
   PlanDeVaisseau[] p=Univers.listePlansDeVaisseaux();
   for(int i=0;i<p.length;i++)
    if((!p[i].estPublic())&&(estPlanDeVaisseauConnu(p[i])))
     a.add(p[i]);
   return (PlanDeVaisseau[])a.toArray(new PlanDeVaisseau[0]);
   }

  public String[] listePlansConnus(){
   PlanDeVaisseau[] p=Univers.listePlansDeVaisseaux();
   ArrayList retour=new ArrayList(p.length);
   for(int i=0;i<p.length;i++)
    if(estPlanDeVaisseauConnu(p[i])) retour.add(p[i].getNom());
   return (String[])retour.toArray(new String[0]);
   }

  public boolean estPlanDeVaisseauConnu(PlanDeVaisseau p){
   if(plansDeVaisseaux.contains(p.getNom())) return true;
   if(p.estPublic()) return true;
   if(p.estDAlliance()) return appartientAAlliance(p.getPrecisionEtat());
   if(p.estRacial()) return (race==p.getPrecisionEtat());
   return false;
   }

  public boolean estPlanDeVaisseauConnu(String p){
   if(Univers.existencePlanDeVaisseau(p)) return estPlanDeVaisseauConnu(Univers.getPlanDeVaisseau(p));
    else return false;
   }

  public boolean estPlanDeVaisseauConnuPrive(String code){
   return plansDeVaisseaux.contains(code);
   }


  public String[] listeBatimentsConnus(){
   String[] listeTechno=listeTechnologiesConnues();
   ArrayList retour=new ArrayList(listeTechno.length);
   for(int i=0;i<listeTechno.length;i++)
    if(Univers.getTechnologie(listeTechno[i]).estBatiment()) retour.add(listeTechno[i]);
   return (String[])retour.toArray(new String[0]);
   }

  public String[] listeDesMisesEnChantierPossibles(){
   String[] b=listeBatimentsConnus();
   String[] p=listePlansConnus();
   ArrayList retour=new ArrayList(b.length+p.length);
   retour.addAll(Arrays.asList(b));
   retour.addAll(Arrays.asList(p));
   return (String[])retour.toArray(new String[0]);
   }

  public String[] listeNomsDesMisesEnChantierPossibles(){
   Technologie[] b=Technologie.transformationCode(listeBatimentsConnus());
   String[] v=new String[b.length];
   for(int i=0;i<v.length;i++) v[i]=b[i].getNomComplet(getLocale());
   String[] p=listePlansConnus();
   ArrayList retour=new ArrayList(v.length+p.length);
   retour.addAll(Arrays.asList(v));
   retour.addAll(Arrays.asList(p));
   return (String[])retour.toArray(new String[0]);
   }

  public boolean peutMettreEnChantier(String code){
   return Mdt.estPresent(listeDesMisesEnChantierPossibles(),code);
   }

  public void ajouterPositionEspionnee(Position pos){
   positionsEspionnees.add(pos);
   }

  public Position[] getPositionsEspionnees(){
   return (Position[])positionsEspionnees.toArray(new Position[0]);
   }

  public Position[] getSystemesDetectes(){
   return (Position[])systemesDetectes.toArray(new Position[0]);
   }

  public int[][] getFlottesDetectees(){
   return (int[][])flottesDetectees.toArray(new int[0][0]);
   }

  public void determinerSystemesDetectes(){
   Position[] l=Univers.listePositionsSystemes();
   Position[] p=listePossession();
   for(int i=0;i<p.length;i++){
    Position[] r=Position.getPositionsADistance(p[i],l,Univers.getSysteme(p[i]).getPorteeRadar(numero));
    for(int j=0;j<r.length;j++)
     if((!systemesDetectes.contains(r[j]))&&(!existencePossession(r[j]))) systemesDetectes.add(r[j]);
    }
   Flotte[] f=listeFlottes();
   for(int i=0;i<f.length;i++){
    Position[] r=Position.getPositionsADistance(f[i].getPosition(),l,f[i].getPorteeScannerSysteme());
    for(int j=0;j<r.length;j++)
     if((!systemesDetectes.contains(r[j]))&&(!existencePossession(r[j]))) systemesDetectes.add(r[j]);
    }
   }

  public void determinerFlottesDetectes(){
   HashMap h=Univers.listeFlottes();
   Map.Entry[] m=(Map.Entry[])h.entrySet().toArray(new Map.Entry[0]);

   Position[] p=listePossession();
   for(int i=0;i<p.length;i++){
    int portee=Univers.getSysteme(p[i]).getPorteeRadar(numero);
    if(portee!=0)
     for(int j=0;j<m.length;j++)
      if(Position.distance(((Flotte)m[j].getValue()).getPosition(),p[i])<=portee)
       if((!flottesDetectees.contains(m[j].getKey()))&&(numero!=((int[])m[j].getKey())[0]))
        flottesDetectees.add(m[j].getKey());
    }


   Flotte[] f=listeFlottes();
   for(int i=0;i<f.length;i++){
    int portee=f[i].getPorteeScannerFlotte();
    if(portee!=0)
     for(int j=0;j<m.length;j++)
      if(Position.distance(((Flotte)m[j].getValue()).getPosition(),f[i].getPosition())<=portee)
       if((!flottesDetectees.contains(m[j].getKey()))&&(numero!=((int[])m[j].getKey())[0]))
        flottesDetectees.add(m[j].getKey());
    }

   for(int i=0;i<flottesDetectees.size();i++){
    int[] inter=(int[])flottesDetectees.get(i);
    Flotte flo=Univers.getCommandant(inter[0]).getFlotte(inter[1]);
    if(Univers.getTest(flo.getBrouillageRadar())) flottesDetectees.remove(inter);
    }
   }

  public void ajouterCorrespondanceFlotte(int numeroCode,int numero){
   correspondanceFlotteDivisee.put(new Integer(numeroCode),new Integer(numero));
   }

  public int getCorrespondanceFlotte(int numeroCode){
   if(numeroCode<10000) return numeroCode;
   Object o=correspondanceFlotteDivisee.get(new Integer(numeroCode));
   if(o==null) return -1;
    else return ((Integer)o).intValue();
   }

  public void initialiserListesMessages(){
   erreur=new Commentaire();
   evenement=new Commentaire();
   combat=new Commentaire();
   ordres=new Commentaire();
   }

  public void initialiserChampsTransients(){
   initialiserListesMessages();
   initialiserSystemesDetectes();
   initialiserFlottesDetectees();
   initialiserBudget();
   initialiserPositionsEspionnees();
   initialiserCorrespondanceFlotteDivisee();
   }

  public Commentaire getErreurs(){return erreur;}
  public Commentaire getEvenements(){return evenement;}
  public Commentaire getCombats(){return combat;}
  public Commentaire getOrdres(){return ordres;}

 //méthodes statiques

 public static String getListeCommandants(int[] l){
  Commandant[] inter=new Commandant[l.length];
  for(int i=0;i<l.length;i++) inter[i]=Univers.getCommandant(l[i]);
  return getListeCommandants(inter);
  }

 public static String getListeCommandants(Commandant[] l){
  String retour=new String();
  for(int i=0;i<l.length;i++){
   retour=retour+l[i].getNomNumero();
   if(i!=l.length-1) retour=retour+", ";
   }
  return retour;
  }

 //le constructeur.

 public Commandant(){
  }

 public Commandant(String n,int ra,int num,String adresse,String log,String mot,int tour){
  super(n,ra,num,adresse,log,mot,tour);
  tauxTaxationPoste=50;
  initialiserFlottes();
  initialiserDomaine();
  initialiserDomainesDeRecherche();
  initialiserAlliances();
  initialiserHeros();
  initialiserGouverneurs();
  initialiserPactesDeNonAgression();
  initialiserTechnologiesConnues();
  initialiserPlansDeVaisseaux();
  initialiserStrategies();
  initialiserChampsTransients();
  }

 //Les méthodes de gestion de fin  et de début de tour.

 public void resolutionCollisions(){
  Flotte[] f=listeFlottes();
  for(int i=0;i<f.length;i++){
   Debris d;
   if((d=Univers.getDebris(f[i].getPosition()))!=null)
    d.gererCollisions(this,f[i]);
   }
  }

 public void progressionNiveauLieutenant(){
  Leader[] l=listeLieutenants();
  for(int i=0;i<l.length;i++)
   for(int j=0;j<Univers.getInt(10);j++) l[i].augmenterExperience();
  for(int i=0;i<l.length;i++) l[i].testerProgressionNiveau(this);
  }

 public void resolutionGestionFlottes(){
  Flotte[] f=listeFlottes();

  for(int i=0;i<f.length;i++){
   int[] inter=f[i].numeroPorteIntraGalactique();
   if(inter!=null)
    ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0001",f[i].getNomNumero(numeroFlotte(f[i])),f[i].getPosition());
   }

  for(int i=0;i<f.length;i++) f[i].resolutionConstruction(this);
  //System.out.print("construF-");

  for(int i=0;i<f.length;i++)
   if(f[i].dommagesTotaux()>0)
    if(Univers.existenceSysteme(f[i].getPosition())){
     Systeme s=Univers.getSysteme(f[i].getPosition());
     int potentiel=s.sommeCapacitesSpecialesBatiment(numero,Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU);
     boolean fait=false;
     if(potentiel!=0)
      if(getCentaures()>potentiel*Const.COUT_REPARATION_VAISSEAU){
       int nbRepare=f[i].reparer(potentiel);
       modifierBudget(Const.BUDGET_COMMANDANT_REPARATION_FLOTTE,-nbRepare*Const.COUT_REPARATION_VAISSEAU);
       ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0001",f[i].getNomNumero(numeroFlotte(f[i])),
                       s.getPosition(),nbRepare);
       fait=true;
       }
       else ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0000",f[i].getNomNumero(numeroFlotte(f[i])));
     if(!fait){
      int[] proprio=s.getProprios();
      for(int j=0;j<proprio.length;j++)
       if((!fait)&&(proprio[j]!=numero)){
        Commandant commandant=Univers.getCommandant(proprio[j]);
        if(Combat.commandantsAllies(this,commandant)){
         potentiel=s.capaciteSpecialeBatiment(commandant.getNumero(),Const.BATIMENT_CAPACITE_REPARATION_VAISSEAU);
         if(potentiel!=0)
          if(commandant.getCentaures()>potentiel*Const.COUT_REPARATION_VAISSEAU){
           int nbRepare=f[i].reparer(potentiel);
           commandant.modifierBudget(Const.BUDGET_COMMANDANT_REPARATION_FLOTTE,-nbRepare*Const.COUT_REPARATION_VAISSEAU);
           ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0002",f[i].getNomNumero(numeroFlotte(f[i])),s.getPosition(),
                                commandant.getNomNumero(),nbRepare);
           commandant.ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0003",f[i].getNomNumero(numeroFlotte(f[i])),
                                s.getPosition(),getNomNumero(),nbRepare);
           fait=true;
           }
          else ajouterEvenement("EV_COMMANDANT_GESTION_FLOTTE_0004",f[i].getNomNumero(numeroFlotte(f[i])),
                                 commandant.getNomNumero());
         }
        }
      }
     }

  float entretien=0F;
  for(int i=0;i<f.length;i++){
   boolean carburant=false;
   if((existencePossession(f[i].getPosition()))&&
      (getPossession(f[i].getPosition()).possedeStockImportantPoste(Const.PRODUIT_CARBURANT))) carburant=true;
   entretien=entretien+f[i].getEntretien(getHerosSurFlotte(numeroFlotte(f[i])),carburant);
   }
  modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_FLOTTE,-entretien);

  for(int i=0;i<f.length;i++)
   modifierBudget(Const.BUDGET_REVENUS_VILLE_SPATIALE,f[i].entretienVilleSpatiale());

  for(int i=0;i<f.length;i++)
   if(f[i].estLoueEnPartie())
    f[i].retourLocation(this,Univers.getTour(),-1);
  }

 public void resolutionGestionSystemes(){
  Systeme[] s=Univers.listeSystemes(listePossession());
  System.out.print(getNomNumero()+"-");
  for(int i=0;i<s.length;i++) s[i].testerRevoltes(numero);
  for(int i=0;i<s.length;i++)
   if(s[i].getNombrePlanetesRevoltees(numero)!=0)
    ajouterEvenement("EV_COMMANDANT_FIN_DE_TOUR_0000",s[i].getPosition(),s[i].getNombrePlanetesRevoltees(numero));

  for(int i=0;i<s.length;i++) getPossession(s[i].getPosition()).miseAJourReputation(this);

  for(int i=0;i<s.length;i++)
   if(getPossession(s[i].getPosition()).aPolitiqueAnti())
    s[i].politiqueExtermination(this,getPossession(s[i].getPosition()).racePolitiqueAnti());

  if(numero!=0)
   for(int i=0;i<s.length;i++) s[i].evolutionStabilite(numero,getCapitale(),getGouverneurSurPossession(s[i].getPosition()),
                                                   getPossession(s[i].getPosition()));
  for(int i=0;i<s.length;i++) s[i].evolutionPopulation(numero,getPossession(s[i].getPosition()));
  for(int i=0;i<s.length;i++) s[i].evolutionMinerai(numero);

  Possession[] p=listeVraiePossession();
  for(int i=0;i<p.length;i++) p[i].evolutionPosteCo(numero,tauxTaxationPoste,s[i]);
  //System.out.print("poste-");
  for(int i=0;i<p.length;i++) p[i].programmationConstructions(this,s[i]);
  //System.out.print("progC-");
  for(int i=0;i<p.length;i++){Univers.phaseSuivante();p[i].resolutionConstructions(this,s[i]);}
  //System.out.print("construS-");
  for(int i=0;i<s.length;i++) s[i].reparation(numero,getGouverneurSurPossession(s[i].getPosition()));
  }

 public void resolutionProgressionRecherche(){
  float r=getBudgetTechnologique();
  String[] s=recherchesActuelles();
  for(int i=0;i<s.length;i++){
   int pourcentage=pourcentageAffecte(s[i]);
   int nb=nombreDePointsDeRecherche(s[i]);
   ajouterPointsDeRecherche(s[i],(int)(r*pourcentage)/100);
   Technologie t=Univers.getTechnologie(s[i]);
   if(t.getPointsDeRecherche()<=nombreDePointsDeRecherche(s[i])){
    suppressionDomaineDeRecherche(s[i]);
    ajouterTechnologieConnue(s[i]);
    ajouterEvenement("EV_COMMANDANT_RECHERCHE_0000",t);
    }
   }
  }

 public void finaliserBudget(){
  Systeme[] s=Univers.listeSystemes(listePossession());
  float revenu=0F;
  for(int i=0;i<s.length;i++) revenu=revenu+s[i].getRevenu(numero,getGouverneurSurPossession(s[i].getPosition()),
     getPossession(s[i].getPosition()));
  modifierBudget(Const.BUDGET_COMMANDANT_REVENUS_SYSTEMES,revenu);
  float entretien=0F;
  for(int i=0;i<s.length;i++)
   entretien=entretien+s[i].getEntretien(
    numero,getGouverneurSurPossession(s[i].getPosition()),getPossession(s[i].getPosition()));
  modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_SYSTEME,-entretien);
  modifierBudget(Const.BUDGET_COMMANDANT_RECHERCHE,-getBudgetTechnologique());
  modifierBudget(Const.BUDGET_COMMANDANT_SERVICES_SPECIAUX,-getBudgetServiceSpeciaux());
  modifierBudget(Const.BUDGET_COMMANDANT_CONTRE_ESPIONNAGE,-getBudgetContreEspionnage());
  modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_LIEUTENANTS,-getEntretienLieutenants());
  modifierBudget(Const.BUDGET_COMMANDANT_ENTRETIEN_TECHNOLOGIES,-getEntretienTechnologies());

  float recette=0F;
  for(int i=Const.BUDGET_COMMANDANT_TOUR_PRECEDENT+1;i<Const.BUDGET_COMMANDANT_TOTAL_RECETTE;i++)
   recette=recette+getBudget(i);
  setBudget(Const.BUDGET_COMMANDANT_TOTAL_RECETTE,recette);
  float depenses=0F;
  for(int i=Const.BUDGET_COMMANDANT_TOTAL_RECETTE+1;i<Const.BUDGET_COMMANDANT_TOTAL_DEPENSE;i++)
   depenses=depenses+getBudget(i);
  setBudget(Const.BUDGET_COMMANDANT_TOTAL_DEPENSE,depenses);
  setBudget(Const.BUDGET_COMMANDANT_TOTAL_DISPONIBLE,centaures);
  }


 public void resolutionEvenements(){
   //le code de cette fonction n'a pas à être public actuellement :o)

  }


 //Les messages d'info.

  public void ajouterOrdres(String message,String[] param){
   ordres.ajouter(message,param);
   }

  public void ajouterCombat(Object o){
   combat.ajouter("{0}",o);
   }

  public void ajouterCombat(String message,Object v1,Object v2,Object v3,int v4){
   combat.ajouter(message,v1,v2,v3,new Integer(v4));
   }

  public void ajouterCombat(String message,Object v1,Object v2,Object v3,Object v4,int v5){
   combat.ajouter(message,v1,v2,v3,v4,new Integer(v5));
   }


  public boolean ajouterErreur(String message){
   erreur.ajouter(message);
   return false;
   }

  public boolean ajouterErreur(String message,Object var){
   erreur.ajouter(message,var);
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,float var2){
   erreur.ajouter(message,var1,new Float(var2));
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,int var2){
   erreur.ajouter(message,var1,new Integer(var2));
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,Object var2){
   erreur.ajouter(message,var1,var2);
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,Object var2,int var3){
   erreur.ajouter(message,var1,var2,new Integer(var3));
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,Object var2,Object var3){
   erreur.ajouter(message,var1,var2,var3);
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,Object var2,Object var3,int var4){
   erreur.ajouter(message,var1,var2,var3,new Integer(var4));
   return false;
   }

  public boolean ajouterErreur(String message,int var){
   erreur.ajouter(message,new Integer(var));
   return false;
   }

  public boolean ajouterErreur(String message,int var1,int var2){
   erreur.ajouter(message,new Integer(var1),new Integer(var2));
   return false;
   }

  public boolean ajouterErreur(String message,Object var1,int var2,int var3){
   erreur.ajouter(message,var1,new Integer(var2),new Integer(var3));
   return false;
   }

  public boolean ajouterEvenement(String message){
   evenement.ajouter(message);
   return true;
   }

  public boolean ajouterEvenement(String message,Object var){
   evenement.ajouter(message,var);
   return true;
   }

  public boolean ajouterEvenement(String message,int var){
   evenement.ajouter(message,new Integer(var));
   return true;
   }

  public boolean ajouterEvenement(String message,int var1,int var2){
   evenement.ajouter(message,new Integer(var1),new Integer(var2));
   return true;
   }

  public boolean ajouterEvenement(String message,float var){
   evenement.ajouter(message,new Float(var));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2){
   evenement.ajouter(message,var1,var2);
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3,int var4){
   evenement.ajouter(message,var1,var2,var3,new Integer(var4));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3,Object var4,int var5){
   evenement.ajouter(message,var1,var2,var3,var4,new Integer(var5));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3,int var4,int var5){
   evenement.ajouter(message,var1,var2,var3,new Integer(var4),new Integer(var5));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3,int var4,float var5){
   evenement.ajouter(message,var1,var2,var3,new Integer(var4),new Float(var5));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3){
   evenement.ajouter(message,var1,var2,var3);
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,Object var3,Object var4){
   evenement.ajouter(message,var1,var2,var3,var4);
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,int var3){
   evenement.ajouter(message,var1,var2,new Integer(var3));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,int var3,int var4){
   evenement.ajouter(message,var1,var2,new Integer(var3),new Integer(var4));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,int var2){
   evenement.ajouter(message,var1,new Integer(var2));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,float var2){
   evenement.ajouter(message,var1,new Float(var2));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,Object var2,float var3){
   evenement.ajouter(message,var1,var2,new Float(var3));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,int var2,float var3){
   evenement.ajouter(message,var1,new Integer(var2),new Float(var3));
   return true;
   }

  public boolean ajouterEvenement(String message,Object var1,int var2,int var3){
   evenement.ajouter(message,var1,new Integer(var2),new Integer(var3));
   return true;
   }


 //Les méthodes utilitaires.

  public static void transfertPlanete(Commandant ancien,Commandant nouveau,Systeme sys,int numPlanete){
   sys.getPlanete(numPlanete).setProprio(nouveau.getNumero());
   if(!sys.estProprio(ancien.getNumero())){
    nouveau.transfererPossession(sys.getPosition(),ancien.getPossession(sys.getPosition()));
    ancien.eliminerPossession(sys.getPosition());
    }
   if(!nouveau.existencePossession(sys.getPosition())) nouveau.ajouterPossession(sys.getPosition(),new Possession());
   }

  public ObjetTransporte enleverChargementDeSysteme(Commandant chargeur,int numFlotte,Position pos,int numPlanete,
                                                       String code,int nombre){
   if(ObjetTransporte.typeDeCodeChargement(code)==Const.TRANSPORT_MARCHANDISE)
    return vendreMarchandises(chargeur,numFlotte,pos,code,nombre);
    else{
     Systeme sys=Univers.getSysteme(pos);
     ObjetTransporte retour=sys.supprimerRichesses(numero,code,nombre,numPlanete);
     Univers.setSysteme(sys);
     return retour;
     }
   }

  public ObjetTransporte ajouterChargementSurSysteme(Commandant dechargeur,int numFlotte,Position pos,int numPlanete,
                                                      ObjetTransporte o){
   if(ObjetTransporte.typeDeCodeChargement(o.getCode())==Const.TRANSPORT_MARCHANDISE)
    return acheterMarchandises(dechargeur,numFlotte,pos,o.getCode(),o.getNombreObjets());
    else{
     Systeme sys=Univers.getSysteme(pos);
     sys.ajouterRichesses(numero,o,numPlanete);
     Univers.setSysteme(sys);
     return null;
     }
   }

  public ObjetTransporte vendreMarchandises(Commandant acheteur,int numFlotte,Position pos,String code,int nombre){
   Possession p=getPossession(pos);
   int nb=p.getQuantiteMarchandise(Utile.numeroMarchandise(code));
   int prix=p.getPrixMarchandise(Utile.numeroMarchandise(code));
   int nombreVendu=Math.min(nb,nombre);
   float prixEffectif=((float)(prix*nombreVendu))/10F;
   Heros h=acheteur.getHerosSurFlotte(numFlotte);
   Gouverneur g=getGouverneurSurPossession(pos);
   if(h!=null) for(int i=0;i<1+nombre;i++) h.augmenterExperience();
   if(g!=null) for(int i=0;i<1+nombre;i++) g.augmenterExperience();
   prixEffectif=Math.max(0.1F,prixEffectif+
    (prixEffectif*( (h==null ? 0 : h.getMarchandModifie())-(g==null ? 0 : g.getMarchandModifie())))/20F);
   prixEffectif=prixEffectif+(prixEffectif*getTauxTaxationPoste())/100F;

   if(nombreVendu==0){
    acheteur.ajouterErreur("ER_COMMANDANT_VENTE_MARCHANDISE_0001",
              ObjetTransporte.traductionChargement(code,nombre,acheteur.getLocale()),pos,this.getNomNumero(),nombre);
    return null;
    }
   if(acheteur!=this){
    if(acheteur.centaures<prixEffectif){
     acheteur.ajouterErreur("ER_COMMANDANT_VENTE_MARCHANDISE_0000",
              ObjetTransporte.traductionChargement(code,nombre,acheteur.getLocale()),pos,nombre);
     return null;
     }

    if(acheteur.existenceVisaDechargement(pos,numero,Utile.numeroMarchandise(code))){
     acheteur.ajouterErreur("ER_COMMANDANT_VENTE_MARCHANDISE_0002",
              ObjetTransporte.traductionChargement(code,nombre,acheteur.getLocale()),pos,acheteur.getNomNumero(),nombre);
     return null;
     }
    modifierBudget(Const.BUDGET_COMMANDANT_VENTE_MARCHANDISE,prixEffectif);
    acheteur.modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_MARCHANDISE,-prixEffectif);
    ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0000",acheteur.getNomNumero(),
                     ObjetTransporte.traductionChargement(code,nombreVendu,getLocale()),pos,nombreVendu,prixEffectif);
    acheteur.ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0001",getNomNumero(),
          ObjetTransporte.traductionChargement(code,nombreVendu,acheteur.getLocale()),pos,nombreVendu,prixEffectif);
    }
    else ajouterEvenement("EV_COMMANDANT_VENTE_MARCHANDISE_0002",
      ObjetTransporte.traductionChargement(code,nombreVendu,getLocale()),pos,nombreVendu);
   p.supprimerMarchandise(Utile.numeroMarchandise(code),nombreVendu);
   return new ObjetSimpleTransporte(code,nombreVendu);
   }

  public ObjetTransporte acheterMarchandises(Commandant vendeur,int numFlotte,Position pos,String code,int nombre){
   Possession p=getPossession(pos);
   int prix=p.getPrixMarchandise(Utile.numeroMarchandise(code));
   float prixEffectif=((float)(prix*nombre))/10F;
   Heros h=vendeur.getHerosSurFlotte(numFlotte);
   Gouverneur g=getGouverneurSurPossession(pos);
   if(h!=null) for(int i=0;i<1+nombre;i++) h.augmenterExperience();
   if(g!=null) for(int i=0;i<1+nombre;i++) g.augmenterExperience();
   prixEffectif=Math.max(0.1F,prixEffectif-
    (prixEffectif*( (h==null ? 0 : h.getMarchandModifie())-(g==null ? 0 : g.getMarchandModifie())))/20F);
   prixEffectif=prixEffectif-(prixEffectif*getTauxTaxationPoste())/100F;

   if(vendeur!=this){
    if(centaures<2*prixEffectif){
     vendeur.ajouterErreur("ER_COMMANDANT_ACHAT_MARCHANDISE_0000",
              ObjetTransporte.traductionChargement(code,nombre,vendeur.getLocale()),pos,nombre);
     return new ObjetSimpleTransporte(code,nombre);
     }

    vendeur.ajouterVisaDechargement(pos,numero,Utile.numeroMarchandise(code));

    modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_MARCHANDISE,-prixEffectif);
    vendeur.modifierBudget(Const.BUDGET_COMMANDANT_VENTE_MARCHANDISE,prixEffectif);
    ajouterEvenement("EV_COMMANDANT_ACHAT_MARCHANDISE_0000",vendeur.getNomNumero(),
                     ObjetTransporte.traductionChargement(code,nombre,getLocale()),pos,nombre,prixEffectif);
    vendeur.ajouterEvenement("EV_COMMANDANT_ACHAT_MARCHANDISE_0001",getNomNumero(),
                             ObjetTransporte.traductionChargement(code,nombre,vendeur.getLocale()),pos,nombre,prixEffectif);
    }
    else ajouterEvenement("EV_COMMANDANT_ACHAT_MARCHANDISE_0002",
      ObjetTransporte.traductionChargement(code,nombre,getLocale()),pos,nombre);
   p.ajouterMarchandise(Utile.numeroMarchandise(code),nombre);
   return null;
   }

 //les méthodes utilitaires privées.

 private void repartirDroitsEntreeAlliance(Alliance a){
  if(a.estAutocratique())
   Univers.getCommandant(a.getNumeroDirigeant()).modifierBudget(
	         Const.BUDGET_COMMANDANT_TAXE_SUR_DROITS_ENTREE_ALLIANCE,a.getDroitsEntree());
  else{
   Commandant[] c=a.getAdherents();
   if(c.length!=0){
    float gain=a.getDroitsEntree()/c.length;
    for(int i=0;i<c.length;i++)
     c[i].modifierBudget(Const.BUDGET_COMMANDANT_TAXE_SUR_DROITS_ENTREE_ALLIANCE,gain);
    }
   }
  }

 private String trouverTechnoAVoler(Commandant cible){
  String[] liste=cible.listeTechnologiesNonPubliquesConnues();
  for(int i=0;i<liste.length;i++) if(!estTechnologieConnue(liste[i])) return liste[i];
  return null;
  }


 //les méthodes pour gérer les commandants.

 public boolean dechirerPacteDeNonAgression(int numeroCible){
  if(!existencePacteDeNonAgression(numeroCible))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PACTE_0000",numeroCible);

  romprePacteDeNonAgression(numeroCible);
  Commandant cible=Univers.getCommandant(numeroCible);   cible.romprePacteDeNonAgression(numero);
  reputation=reputation+Const.REPUTATION_RUPTURE_DE_PACTE;

  cible.ajouterEvenement("EV_COMMANDANT_PACTE_0000",getNomNumero());
  return ajouterEvenement("EV_COMMANDANT_PACTE_0001",cible.getNomNumero());
  }

 public boolean signerPacteDeNonAgression(int numeroCible){
  if(existencePacteDeNonAgression(numeroCible))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PACTE_0001",numeroCible);

  ajouterPacteDeNonAgression(numeroCible);
  reputation=reputation+Const.REPUTATION_SIGNATURE_DE_PACTE;

  return ajouterEvenement("EV_COMMANDANT_PACTE_0002",numeroCible);
  }

 public boolean creerAlliance(int droitsEntree,String titre,int type,boolean secrete){
  if(!estTechnologieConnue("diploI")) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0011");
  //if(nombreAlliancesTropGrand()) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0001",titre);
  float cout;
  if (secrete) cout=Const.COUT_CREATION_ALLIANCE_SECRETE;
   else cout=Const.COUT_CREATION_ALLIANCE_NON_SECRETE;
  if(centaures<cout) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0000",titre,centaures);
  //if(estDirigeantAlliance()) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0004",titre);

  modifierBudget(Const.BUDGET_COMMANDANT_CREATION_ALLIANCE,-cout);
  Alliance a=new Alliance(Univers.trouverNumeroLibreAlliance(),titre,(float)droitsEntree,type,secrete,numero,nom,
                          Univers.getTour());
  Univers.ajouterAlliance(a);
  ajouterAlliance(a.getNumero());

  return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0000",titre);
  }

 public boolean adhererAlliance(int num){
  if(!Univers.allianceExistante(num)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0002",num);
  if(nombreAlliancesTropGrand()) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0003",num);
  Alliance a=Univers.getAlliance(num);
  if(centaures<a.getDroitsEntree()) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0002",a.getNom(),a.getDroitsEntree());

  modifierBudget(Const.BUDGET_COMMANDANT_ADHESION_ALLIANCE,-a.getDroitsEntree());
  repartirDroitsEntreeAlliance(a);
  ajouterAlliance(a.getNumero());

  a.ajouterEvenement("EV_ALLIANCE_0000",getNomNumero());
  return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0001",a.getNom(),a.getDroitsEntree());
  }

 public boolean voterElectionDirigeant(int num,int dirigeant){
  if(!Univers.existenceCommandant(dirigeant))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0003",dirigeant);
  if(!Univers.getCommandant(dirigeant).appartientAAlliance(num))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0004",dirigeant,num);
  if(!appartientAAlliance(num)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0000",num);
  Alliance a=Univers.getAlliance(num);
  if((!a.estDemocratique())&&((!a.estAnarchique())||(a.estSecrete())))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0001",num);

  a.ajouterVoteDirigeant(dirigeant);

  return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0004",a.getNom(),Univers.getCommandant(dirigeant).getNomNumero());
  }

 public boolean voterExclusionCommandant(int num,int cible){
  if(!Univers.existenceCommandant(cible))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0008",cible);
  if(!Univers.getCommandant(cible).appartientAAlliance(num))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0009",cible,num);
  if(!appartientAAlliance(num)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0005",num);
  Alliance a=Univers.getAlliance(num);
  if(a.estAnarchique())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0010",num);
  if((a.estAutocratique())&&(!a.estDirigeePar(numero)))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0011",num);

  a.ajouterVoteExclusion(cible);

  return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0006",a.getNom(),Univers.getCommandant(cible).getNomNumero());
  }

 public boolean quitterAlliance(int num){
  if(!Univers.allianceExistante(num)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0006",num);
  Alliance a=Univers.getAlliance(num);
  if(!appartientAAlliance(a.getNumero())) return ajouterErreur("ER_COMMANDANT_ALLIANCE_0005",a.getNom());

  enleverAlliance(a.getNumero());

  a.ajouterEvenement("EV_ALLIANCE_0001",getNomNumero());
  return ajouterEvenement("EV_COMMANDANT_ALLIANCE_0002",a.getNom());
  }

 public boolean renommerAlliance(String titre,int numeroAlliance){
  if(!Univers.allianceExistante(numeroAlliance))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0012",numeroAlliance);
  Alliance a=Univers.getAlliance(numeroAlliance);
  if(!a.estDirigeePar(numero))
   return ajouterErreur("ER_COMMANDANT_ALLIANCE_0006",a.getNom());

  a.setNom(titre);

  a.ajouterEvenement("EV_ALLIANCE_0007",titre);
  return true;
  }

 public boolean ecrireAdresseAlliance(String titre,int numeroAlliance){
  if(!Univers.allianceExistante(numeroAlliance))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ALLIANCE_0013",numeroAlliance);
  Alliance a=Univers.getAlliance(numeroAlliance);
  if(!a.estDirigeePar(numero))
   return ajouterErreur("ER_COMMANDANT_ALLIANCE_0007",a.getNom());

  a.setAdresseElectronique(titre);

  a.ajouterEvenement("EV_ALLIANCE_0008",titre);
  return true;
  }

 public boolean transfertCentaures(int destinataire,int don,int modeTransfert){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_CENTAURES_0000",destinataire);
  float cout=(float)don;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=cout+Const.SURCOUT_DON_CENTAURES_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=cout+Const.SURCOUT_DON_CENTAURES_ANONYME;
  if(cout>centaures) return ajouterErreur("ER_COMMANDANT_DON_CENTAURES_0000",don,destinataire);

  modifierBudget(Const.BUDGET_COMMANDANT_DON_CENTAURES,-cout);
  Commandant cible=Univers.getCommandant(destinataire);
  cible.modifierBudget(Const.BUDGET_COMMANDANT_RECEPTION_CENTAURES,(float)don);
  Univers.ajouterRelationRaces(cible.getCapitale(),getRace(),cible.getRace(),don/200);
  Univers.ajouterTransfert(this,cible,"centaures : "+Integer.toString(don));

  if((modeTransfert==Const.DON_MODE_NORMAL)&&(don>Const.DON_MAXIMUM_SECRET))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0000",getNomNumero(),cible.getNomNumero(),don);
  ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0001",cible.getNomNumero(),don);
  if(modeTransfert==Const.DON_MODE_ANONYME) return cible.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0002",don);
   else return cible.ajouterEvenement("EV_COMMANDANT_DON_CENTAURES_0003",getNomNumero(),don);
   }

 public boolean abandonnerTechnologie(String codeTechno){
  if(!estTechnologieConnue(codeTechno))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ELIMINER_TECHNOLOGIE_0000",codeTechno);

  eliminerTechnologieConnue(codeTechno);

  Technologie t=Univers.getTechnologie(codeTechno);
  return ajouterEvenement("EV_COMMANDANT_ELIMINER_TECHNOLOGIE_0000",t);
  }

 public boolean transfertTechnologie(int destinataire,String codeTechno,int modeTransfert){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_TECHNOLOGIE_0000",codeTechno,destinataire);
  if(!estTechnologieConnue(codeTechno))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_TECHNOLOGIE_0001",codeTechno,destinataire);
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_TECHNO_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_TECHNO_ANONYME;
  if((cout!=0F)&&(cout>centaures))
   return ajouterErreur("ER_COMMANDANT_DON_TECHNOLOGIE_0000",Univers.getNomTechno(codeTechno,getLocale()),destinataire);
  Commandant cible=Univers.getCommandant(destinataire);
  if(cible.estTechnologieConnue(codeTechno))
   return ajouterErreur("ER_COMMANDANT_DON_TECHNOLOGIE_0001",Univers.getNomTechno(codeTechno,getLocale()),destinataire);


  cible.ajouterTechnologieConnue(codeTechno);
  cible.suppressionDomaineDeRecherche(codeTechno);
  if(cout!=0F) modifierBudget(Const.BUDGET_COMMANDANT_DON_TECHNOLOGIE,-cout);
  Univers.ajouterRelationRaces(cible.getCapitale(),getRace(),cible.getRace(),Const.RELATION_TRANSFERT_TECHNOLOGIE);
  Univers.ajouterTransfert(this,cible,"technologie : "+codeTechno);

  Technologie t=Univers.getTechnologie(codeTechno);
  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_TECHNO_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0002",getNomNumero(),cible.getNomNumero(),t);
  ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0000",cible.getNomNumero(),t);
  if(modeTransfert==Const.DON_MODE_ANONYME)
   return cible.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0003",t);
   else return cible.ajouterEvenement("EV_COMMANDANT_DON_TECHNOLOGIE_0001",getNomNumero(),t);
  }

 public boolean transfertFlotte(int destinataire,int numeroFlotte,int nbTours,int modeTransfert){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_FLOTTE_0000",numeroFlotte+1,destinataire);
  if(!existenceFlotte(numeroFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_FLOTTE_0001",numeroFlotte+1,destinataire);
  Flotte f=getFlotte(numeroFlotte);
  if(f.estLoueEnPartie())
   return ajouterErreur("ER_COMMANDANT_DON_FLOTTE_0001",numeroFlotte+1,destinataire);
  Commandant cible=Univers.getCommandant(destinataire);
  if(!cible.estJoueurHumain())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_FLOTTE_0002",numeroFlotte+1,destinataire);
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_FLOTTE_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_FLOTTE_ANONYME;
  if((cout!=0F)&&(cout>centaures))
   return ajouterErreur("ER_COMMANDANT_DON_FLOTTE_0000",f.getNomNumero(numeroFlotte),destinataire);

  f.planifierTransmission(numero,Univers.getTour()+nbTours);
  cible.ajouterFlotte(f);
  eliminerFlotte(numeroFlotte);
  modifierBudget(Const.BUDGET_COMMANDANT_PRET_FLOTTE,-cout);
  Univers.ajouterRelationRaces(cible.getCapitale(),getRace(),cible.getRace(),
       2*Vaisseau.retournerNiveauPuissance(f.getPuissance()));
  Univers.ajouterTransfert(this,cible,"prêt flotte puissance : "+Integer.toString(f.getPuissance()));

  ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0000",cible.getNomNumero(),numeroFlotte+1,nbTours);
  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_FLOTTE_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0003",getNomNumero(),cible.getNomNumero());
  if(modeTransfert==Const.DON_MODE_ANONYME)
   return cible.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0002",numeroFlotte+1,nbTours);
   else return cible.ajouterEvenement("EV_COMMANDANT_DON_FLOTTE_0001",getNomNumero(),numeroFlotte+1,nbTours);
  }

 public boolean venteFlotte(int destinataire,int numeroFlotte){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_VENTE_FLOTTE_0000",numeroFlotte+1,destinataire);
  if(!existenceFlotte(numeroFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_VENTE_FLOTTE_0001",numeroFlotte+1,destinataire);
  Flotte f=getFlotte(numeroFlotte);
  if(f.estLoueEnPartie())
   return ajouterErreur("ER_COMMANDANT_VENTE_FLOTTE_0003",numeroFlotte+1,destinataire);
  Commandant cible=Univers.getCommandant(destinataire);
  //if(!cible.estJoueurHumain())
  // return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_VENTE_FLOTTE_0002",numeroFlotte+1,destinataire);
  float cout=f.getValeur()/2;

  if(cout>cible.getCentaures())
   return ajouterErreur("ER_COMMANDANT_VENTE_FLOTTE_0000",f.getNomNumero(numeroFlotte),destinataire);

  f.initialiserEquipages();
  cible.ajouterFlotte(f);
  eliminerFlotte(numeroFlotte);
  cible.modifierBudget(Const.BUDGET_COMMANDANT_ACHAT_FLOTTE,-cout);
  modifierBudget(Const.BUDGET_COMMANDANT_VENTE_FLOTTE,cout);
  Univers.ajouterTransfert(this,cible,"vente flotte puissance : "+Integer.toString(f.getPuissance()));

  Univers.ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0003",getNomNumero(),cible.getNomNumero());
  ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0000",cible.getNomNumero(),numeroFlotte+1);
  return cible.ajouterEvenement("EV_COMMANDANT_VENTE_FLOTTE_0001",getNomNumero(),numeroFlotte+1,cout);
  }

 public boolean transfertSysteme(int destinataire,Position pos,int modeTransfert){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_SYSTEME_0000",destinataire);
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_SYSTEME_0001",pos);
  Systeme sys=Univers.getSysteme(pos);
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_SYSTEME_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_SYSTEME_ANONYME;
  if((cout!=0F)&&(cout>centaures)) return ajouterErreur("ER_COMMANDANT_DON_SYSTEME_0000",pos,destinataire);

  Commandant cible=Univers.getCommandant(destinataire);
  Univers.ajouterTransfert(this,cible,"don système puissance : "+Integer.toString(sys.getPuissance(numero)));
  sys.changementDeProprietaire(numero,destinataire);
  Univers.setSysteme(sys);
  cible.transfererPossession(pos,getPossession(pos));
  eliminerPossession(pos);
  if(cout!=0F) modifierBudget(Const.BUDGET_COMMANDANT_DON_SYSTEME,-cout);
  Univers.ajouterRelationRaces(cible.getCapitale(),getRace(),cible.getRace(),Const.RELATION_TRANSFERT_SYSTEME);

  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_SYSTEME_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0002",getNomNumero(),cible.getNomNumero(),pos);
  if(modeTransfert==Const.DON_MODE_ANONYME)
    cible.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0003",pos);
   else cible.ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0001",getNomNumero(),pos);
  return ajouterEvenement("EV_COMMANDANT_DON_SYSTEME_0000",cible.getNomNumero(),pos);
  }

 public boolean transfertPlanete(int destinataire,Position pos,int numPlanete,int modeTransfert){
  //if(destinataire>143) destinataire=destinataire-1;

  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_PLANETE_0000",destinataire);
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_PLANETE_0001",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes())
   return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0001",pos,destinataire,numPlanete+1);
  if(sys.getPlanete(numPlanete).getProprio()!=numero)
   return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0002",pos,sys.getNomNumeroPlanete(numPlanete));
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_PLANETE_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_PLANETE_ANONYME;
  if((cout!=0F)&&(cout>centaures))
   return ajouterErreur("ER_COMMANDANT_DON_PLANETE_0000",pos,sys.getNomNumeroPlanete(numPlanete),destinataire);

  Commandant cible=Univers.getCommandant(destinataire);
  transfertPlanete(this,cible,sys,numPlanete);
  Univers.setSysteme(sys);
  if(cout!=0F) modifierBudget(Const.BUDGET_COMMANDANT_DON_PLANETE,-cout);
  Univers.ajouterTransfert(this,cible,"don planète");

  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_PLANETE_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0002",getNomNumero(),cible.getNomNumero(),pos);
  if(modeTransfert==Const.DON_MODE_ANONYME)
    cible.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0003",pos,sys.getNomNumeroPlanete(numPlanete));
   else cible.ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0001",getNomNumero(),pos,sys.getNomNumeroPlanete(numPlanete));
  return ajouterEvenement("EV_COMMANDANT_DON_PLANETE_0000",cible.getNomNumero(),pos,sys.getNomNumeroPlanete(numPlanete));
  }

 public boolean mettreEnChantier(Position pos,int nombre,String codeConstruction,int numPlanete){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MISE_EN_CHANTIER_0000",pos);
  if(!peutMettreEnChantier(codeConstruction))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MISE_EN_CHANTIER_0001",codeConstruction);
  //if(existenceGouverneurSurPossession(pos))
   //return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MISE_EN_CHANTIER_0002",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_MISE_EN_CHANTIER_0000",pos,numPlanete+1);

  if(nombre!=0) getPossession(pos).ajouterConstruction(new Construction(codeConstruction,nombre,numPlanete));

  if(Univers.existenceTechnologie(codeConstruction))
   return ajouterEvenement("EV_COMMANDANT_MISE_EN_CHANTIER_0000",pos,Univers.getTechnologie(codeConstruction),nombre);
   else return ajouterEvenement("EV_COMMANDANT_MISE_EN_CHANTIER_0000",pos,codeConstruction,nombre);
  }

 public boolean annulerConstruction(Position pos){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ANNULER_CONSTRUCTION_0000",pos);

  getPossession(pos).initialiserConstructions();

  return ajouterEvenement("EV_COMMANDANT_ANNULER_CONSTRUCTION_0000",pos);
  }

 public boolean programmerConstruction(Position pos,String codeConstruction){
  if(!existencePossession(pos))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000",pos);
  if(!peutMettreEnChantier(codeConstruction))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0001",codeConstruction);
  if(!estTechnologieConnue("progcoI"))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PROGRAMMER_CONSTRUCTION_0002");

  getPossession(pos).programmerConstruction(codeConstruction);

  if(Univers.existenceTechnologie(codeConstruction))
   return ajouterEvenement("EV_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000",pos,Univers.getTechnologie(codeConstruction));
   else return ajouterEvenement("EV_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000",pos,codeConstruction);
  }

 public boolean annulerProgramationConstruction(Position pos){
  if(!existencePossession(pos))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000",pos);

  getPossession(pos).initialiserProgrammationConstruction();

  return ajouterEvenement("EV_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000",pos);
  }

 public boolean modifierPolitique(Position pos,int politique){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_POLITIQUE_0000",pos);
  if(politique>Const.NB_POLITIQUES)return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_POLITIQUE_0001",pos);

  getPossession(pos).setPolitique(politique);
  modifierBudget(Const.BUDGET_COMMANDANT_CHANGER_POLITIQUE,-Const.COUT_CHANGER_POLITIQUE);

  return ajouterEvenement("EV_COMMANDANT_MODIFIER_POLITIQUE_0000",pos,
                           Univers.getMessage("POLITIQUES",politique,getLocale()));
  }

 public boolean modifierBudget(Position pos,int[] domaine,int[] valeur){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_BUDGET_0000",pos);
  for(int i=0;i<domaine.length;i++)
   if(domaine[i]>Const.NB_DOMAINES_BUDGET)
    return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_BUDGET_0001",pos);

  if(!getPossession(pos).modifierBudget(domaine,valeur))
   return ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_BUDGET_0000",pos);

  return ajouterEvenement("EV_COMMANDANT_MODIFIER_BUDGET_0000",pos);
  }

 public boolean modifierTaxation(Position pos,int taxation){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_TAXATION_0000",pos);
  if(taxation>Const.TAXATION_MAXIMALE)
    return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_TAXATION_0001",pos);

  Systeme sys=Univers.getSysteme(pos);
  sys.modifierTaxation(numero,taxation);
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_MODIFIER_TAXATION_0000",pos,taxation);
  }

 public boolean modifierTaxationPlanete(Position pos,int taxation,int numPlanete){
  if(!estTechnologieConnue("gestplaI")) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_TAXATION_0004");
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_TAXATION_0002",pos);
  if(taxation>Const.TAXATION_MAXIMALE)
    return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MODIFIER_TAXATION_0003",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_MODIFIER_TAXATION_0000",pos,numPlanete+1);
  if(!sys.getPlanete(numPlanete).estProprio(numero))
   return ajouterErreur("ER_COMMANDANT_MODIFIER_TAXATION_0001",pos,sys.getNomNumeroPlanete(numPlanete));

  sys.getPlanete(numPlanete).setTaxation(taxation);
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_MODIFIER_TAXATION_0001",pos,sys.getNomNumeroPlanete(numPlanete),taxation);
  }

 public boolean terraformer(Position pos){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_TERRAFORMER_0000",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(sys.estDejaTerraforme(numero)) return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0004",pos);
  float cout=sys.coutTerraformation(numero);
  if(cout>centaures) return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0000",pos);

  modifierBudget(Const.BUDGET_COMMANDANT_TERRAFORMATION,-cout);
  sys.terraformer(numero);
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_TERRAFORMER_0000",pos,cout);
  }

 public boolean terraformerPlanete(Position pos,int numPlanete){
  if(!estTechnologieConnue("gestplaI")) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_TERRAFORMER_0002");
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_TERRAFORMER_0001",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0002",pos,numPlanete+1);
  if(!sys.getPlanete(numPlanete).estProprio(numero))
   return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0003",pos,sys.getNomNumeroPlanete(numPlanete));
  if(sys.getPlanete(numPlanete).estDejaTerraforme())
    return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0005",pos,sys.getNomNumeroPlanete(numPlanete));
  float cout=sys.getPlanete(numPlanete).coutTerraformation();
  if(cout>centaures) return ajouterErreur("ER_COMMANDANT_TERRAFORMER_0001",pos,sys.getNomNumeroPlanete(numPlanete));

  modifierBudget(Const.BUDGET_COMMANDANT_TERRAFORMATION,-cout);
  sys.getPlanete(numPlanete).terraformer();
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_TERRAFORMER_0001",pos,sys.getNomNumeroPlanete(numPlanete),cout);
  }

 public boolean detruireBatiments(Position pos,String code,int nombre,int numPlanete){
  if(!estTechnologieConnue("gestplaI")) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DETRUIRE_BATIMENT_0002");
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DETRUIRE_BATIMENT_0000",pos);
  if((!Univers.existenceTechnologie(code))||(!Univers.getTechnologie(code).estBatiment()))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DETRUIRE_BATIMENT_0001",pos,code);
  Technologie t=Univers.getTechnologie(code);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete==Integer.MIN_VALUE){
   if(!sys.contientBatiment(numero,code)) return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0000",pos,t);
   }
   else{
    if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0002",pos,numPlanete+1);
    if(!sys.getPlanete(numPlanete).estProprio(numero))
       return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0003",pos,sys.getNomNumeroPlanete(numPlanete));
    if(!sys.getPlanete(numPlanete).contientBatiment(code))
       return ajouterErreur("ER_COMMANDANT_DETRUIRE_BATIMENT_0001",pos,t,sys.getNomNumeroPlanete(numPlanete));
    }

  int nb=0;
  if(numPlanete==Integer.MIN_VALUE) nb=sys.recyclerMateriel(numero,(Batiment)t,nombre);
   else nb=sys.getPlanete(numPlanete).recyclerMateriel((Batiment)t,nombre);
  Univers.setSysteme(sys);

  if(numPlanete==Integer.MIN_VALUE)
   if(nb==nombre) return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0000",pos,t,nb);
    else return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0001",pos,t,nb,nombre);
   else
    if(nb==nombre)
     return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0002",pos,t,sys.getNomNumeroPlanete(numPlanete),nb);
    else
    return ajouterEvenement("EV_COMMANDANT_DETRUIRE_BATIMENT_0003",pos,t,sys.getNomNumeroPlanete(numPlanete),nb,nombre);
  }

 public boolean affecterDomainesDeRecherche(String[] codeTechno,int[] pourcentage){
  HashMap inter=(HashMap)recherches.clone();
  for(int i=0;i<codeTechno.length;i++){
   if((!existenceDomaineDeRecherche(codeTechno[i]))&&(!peutChercherTechnologie(codeTechno[i]))){
    recherches=inter;
    return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_AFFECTER_RECHERCHE_0000",codeTechno[i]);
    }
   if(!ajouterDomaineDeRecherche(codeTechno[i],pourcentage[i])){
    recherches=inter;
    return ajouterErreur("ER_COMMANDANT_AFFECTER_RECHERCHE_0000");
    }
   }
  if(totalAffectationPourcentage()>100){
    recherches=inter;
    return ajouterErreur("ER_COMMANDANT_AFFECTER_RECHERCHE_0001");
    }

  for(int i=0;i<codeTechno.length;i++)
   ajouterEvenement("EV_COMMANDANT_AFFECTER_RECHERCHE_0000",Univers.getTechnologie(codeTechno[i]),pourcentage[i]);
  return true;
  }

 public boolean effectuerMissionSpeciale(Position pos,int typeMission,int numPlanete){
  float attaque=getBudgetServiceSpeciaux();
  if(typeMission>Const.NB_MISSIONS) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_MISSION_SPECIALE_0000");
  if(!Univers.existenceSysteme(pos)) return ajouterErreur("ER_COMMANDANT_MISSION_SPECIALE_0000",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_MISSION_SPECIALE_0001",pos,numPlanete);

  int nPlanete=numPlanete;
  if(numPlanete==Integer.MIN_VALUE) nPlanete=sys.trouverPlanetePropagandable(numero);

  float defense=sys.getBudgetContreEspionnage(numero);
  int reussite=0;

  if(typeMission==Const.MISSION_ESPIONNAGE) reussite=Univers.getInt(Math.max(1,(int)(attaque-defense)));
  if(typeMission==Const.MISSION_SABOTAGE) reussite=Univers.getInt(Math.max(1,(int)(attaque-10*defense)));
  if(typeMission==Const.MISSION_VOL_TECHNOLOGIE) reussite=Univers.getInt(Math.max(1,(int)(attaque-20*defense)));
  if(typeMission==Const.MISSION_PROPAGANDE) reussite=Univers.getInt(Math.max(1,(int)(attaque-5*defense)));

  Commandant c=Univers.getCommandant(sys.getPlanete(nPlanete).getProprio());

  if(reussite==0){
   ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0000",pos,sys.getNomNumeroPlanete(nPlanete),
                     Univers.getMessage("MISSIONS",typeMission,getLocale()));
   c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0001",pos,sys.getNomNumeroPlanete(nPlanete),
                     Univers.getMessage("MISSIONS",typeMission,c.getLocale()),getNomNumero());
   return false;
   }

  if(typeMission==Const.MISSION_VOL_TECHNOLOGIE){
   String technoVolee=trouverTechnoAVoler(c);
   if(technoVolee==null) ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0002",c.getNomNumero());
    else{
     ajouterTechnologieConnue(technoVolee);
     ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0003",Univers.getTechnologie(technoVolee),c.getNomNumero());
     if(reussite<5)
      c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0004",Univers.getTechnologie(technoVolee),getNomNumero());
     }
    }

  if(typeMission==Const.MISSION_ESPIONNAGE){
   ajouterPositionEspionnee(pos);
   ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0005","<A href=\""+Rapport.lienRapportEspion(pos)+"\">"+
                      Univers.getSysteme(pos).getNomPosition()+"</A>");
   }

  if(typeMission==Const.MISSION_SABOTAGE){
   sys.detruireToutBatimentDePlanete(nPlanete);
   Univers.setSysteme(sys);
   ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0006",pos,sys.getNomNumeroPlanete(nPlanete));
   if(reussite<5)
    c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0007",pos,sys.getNomNumeroPlanete(nPlanete),getNomNumero());
    else c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0008",pos,sys.getNomNumeroPlanete(nPlanete));
   }

  if(typeMission==Const.MISSION_PROPAGANDE){
   int stab=sys.getPlanete(nPlanete).getStabilite();
   if(c==this){
    sys.getPlanete(nPlanete).setStabilite(stab+Math.min(20,reussite));
    ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0009",pos,sys.getNomNumeroPlanete(nPlanete),
     sys.getPlanete(nPlanete).getStabilite()-stab);
    }
    else{
     sys.getPlanete(nPlanete).setStabilite(stab-Math.min(80,Univers.getInt(reussite)));
     ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0010",pos,sys.getNomNumeroPlanete(nPlanete),
      stab-sys.getPlanete(nPlanete).getStabilite());
     if(reussite<5)
      c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0011",pos,sys.getNomNumeroPlanete(nPlanete),getNomNumero(),
        stab-sys.getPlanete(nPlanete).getStabilite());
       else c.ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0012",pos,sys.getNomNumeroPlanete(nPlanete),
             stab-sys.getPlanete(nPlanete).getStabilite());
     if((c.estJoueurNeutre())&&(sys.getPlanete(nPlanete).getStabilite()<50)){
      transfertPlanete(c,this,sys,nPlanete);
      ajouterEvenement("EV_COMMANDANT_MISSION_SPECIALE_0013",pos,sys.getNomNumeroPlanete(nPlanete));
      }
     }
   }
  Univers.setSysteme(sys);
  return true;
  }

 public boolean deplacerFlotte(int nFlotte,Position direction,int directive,int directivePrecision,String stra){
  int numFlotte=getCorrespondanceFlotte(nFlotte);
  if(!Flotte.directiveExistante(directive,directivePrecision))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DEPLACER_FLOTTE_0000",directive,directivePrecision);
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DEPLACER_FLOTTE_0001",numFlotte+1);


  Flotte f=getFlotte(numFlotte);
  if(f.nonDeplacee()){
   String strat=Const.STRATEGIE_DEFAUT.getNom();
   if(connaitStrategie(stra)) strat=stra;
   f.deplacer(direction,directive,directivePrecision,getHerosSurFlotte(numFlotte),strat);
   return ajouterEvenement("EV_COMMANDANT_DEPLACER_FLOTTE_0000",f.getNomNumero(numFlotte),direction,
                                 Flotte.traductionDirective(directive,directivePrecision,getLocale()));
   }
   else return false;
  }

 public boolean chargerCargo(int numFlotte,int numTransport,int nombre,String code,int joueur,int numPlanete){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CHARGER_CARGO_0000",numFlotte+1);
  if((joueur!=Integer.MIN_VALUE)&&(!Univers.existenceCommandant(joueur)))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CHARGER_CARGO_0001",joueur);
  if(!ObjetTransporte.existenceCodeChargement(code))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CHARGER_CARGO_0004",code);
  Flotte f=getFlotte(numFlotte);
  if(!Univers.existenceSysteme(f.getPosition()))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CHARGER_CARGO_0002",f.getPosition());
  Systeme sys=Univers.getSysteme(f.getPosition());
  if(numPlanete>=sys.getNombrePlanetes())
   return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0005",f.getPosition(),numPlanete+1);
  if(numTransport>f.nbVaisseauxCargos())
   return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0001",f.getNomNumero(numFlotte),numTransport);
  int numJou=joueur;
  if(joueur==Integer.MIN_VALUE)
   if(sys.nbProprios()==1) numJou=sys.getProprios()[0];
    else if(sys.estProprio(numero)) numJou=numero;
     else if(numPlanete!=Integer.MIN_VALUE) numJou=sys.getPlanete(numPlanete).getProprio();
      else return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0004",f.getNomNumero(numFlotte));
  Commandant c=Univers.getCommandant(numJou);
  if(!c.existencePossession(f.getPosition()))
   return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0002",f.getPosition(),c.getNomNumero());
  if((numJou!=numero)&&
    (ObjetTransporte.typeDeCodeChargement(code)!=Const.TRANSPORT_MARCHANDISE))
   return ajouterErreur("ER_COMMANDANT_CHARGER_CARGO_0003",f.getNomNumero(numFlotte));

  try{

  int transportMax=f.nombreChargementPouvantEtreTransporte(code,numTransport);
  //System.out.println(getNomNumero()+"-"+numTransport+"-"+code+"-"+transportMax);
  ObjetTransporte o=
   c.enleverChargementDeSysteme(this,numFlotte,f.getPosition(),numPlanete,code,Math.min(transportMax,nombre));
  //System.out.println(getNomNumero()+"-"+numTransport+"-"+code+"-"+nombre);
  int charge=0;
  if(o!=null) {f.chargerChargement(o,numTransport);charge=o.getNombreObjets();}


  if(ObjetTransporte.typeDeCodeChargement(code)!=Const.TRANSPORT_MARCHANDISE)
   if(o!=null)
    return ajouterEvenement("EV_COMMANDANT_CHARGER_CARGO_0000",f.getPosition(),
      ObjetTransporte.traductionChargement(code,o.getNombreObjets(),getLocale()),
            f.getNomNumero(numFlotte),charge);
    else return ajouterEvenement("EV_COMMANDANT_CHARGER_CARGO_0001",f.getPosition(),
                                 ObjetTransporte.traductionChargement(code,0,getLocale()),f.getNomNumero(numFlotte));

  }catch(Exception e){System.out.println(joueur+"-"+code+"-"+e.getMessage());}

  return true;
  }

 public boolean dechargerCargo(int numFlotte,int numTransport,int nombre,String code,int joueur,int numPlanete){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DECHARGER_CARGO_0000",numFlotte);
  if((joueur!=Integer.MIN_VALUE)&&(!Univers.existenceCommandant(joueur)))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DECHARGER_CARGO_0001",joueur);
  if(!ObjetTransporte.existenceCodeChargement(code))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DECHARGER_CARGO_0004",code);
  Flotte f=getFlotte(numFlotte);
  if(!Univers.existenceSysteme(f.getPosition()))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DECHARGER_CARGO_0002",f.getPosition());
  Systeme sys=Univers.getSysteme(f.getPosition());
  if(numPlanete>=sys.getNombrePlanetes())
   return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0005",f.getPosition(),numPlanete+1);
  int numJou=joueur;
  if(joueur==Integer.MIN_VALUE)
   if(sys.nbProprios()==1) numJou=sys.getProprios()[0];
    else if(sys.estProprio(numero)) numJou=numero;
     else if(numPlanete!=Integer.MIN_VALUE) numJou=sys.getPlanete(numPlanete).getProprio();
      else return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0004",f.getNomNumero(numFlotte));
  Commandant c=Univers.getCommandant(numJou);
  if(numTransport>f.nbVaisseauxCargos())
   return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0001",f.getNomNumero(numFlotte),numTransport);
  if(!c.existencePossession(f.getPosition()))
   return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0002",f.getPosition(),c.getNomNumero());

  int transportMax=f.nombreChargementTransporte(code,numTransport);
  ObjetTransporte o1=f.dechargerChargement(code,Math.min(transportMax,nombre),numTransport);
  if(o1==null) return ajouterErreur("ER_COMMANDANT_DECHARGER_CARGO_0003",f.getPosition(),
   ObjetTransporte.traductionChargement(code,nombre,getLocale()),f.getNomNumero(numFlotte),nombre);



  ObjetTransporte o2=c.ajouterChargementSurSysteme(this,numFlotte,f.getPosition(),numPlanete,o1);
  if((o2!=null)&&(o2.getNombreObjets()>0)) f.chargerChargement(o2,numTransport);

  int nombreCharge;
  if(o2==null) nombreCharge=o1.getNombreObjets();
               else nombreCharge=o1.getNombreObjets()-o2.getNombreObjets();
  if(ObjetTransporte.typeDeCodeChargement(code)!=Const.TRANSPORT_MARCHANDISE)
   if(nombreCharge>0){
    ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0000",f.getPosition(),
      ObjetTransporte.traductionChargement(code,nombreCharge,getLocale()),f.getNomNumero(numFlotte),nombreCharge);
    if(c!=this) c.ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0002",f.getPosition(),
      ObjetTransporte.traductionChargement(code,nombreCharge,c.getLocale()),
        f.getNomNumero(numFlotte),getNomNumero(),nombreCharge);
    }
    else ajouterEvenement("EV_COMMANDANT_DECHARGER_CARGO_0001",f.getPosition(),
                                 ObjetTransporte.traductionChargement(code,0,getLocale()),f.getNomNumero(numFlotte));
  return true;
  }

 public boolean diviserFlotte(int numFlotte,String[] code,int[] nb,String nouveauNom,int numeroDivision){
  if(getCorrespondanceFlotte(10000+numeroDivision)!=-1) return false;
  if(!existenceFlotte(getCorrespondanceFlotte(numFlotte)))
   return ajouterErreur("ER_COMMANDANT_DIVISER_FLOTTE_0000",numFlotte);
  for(int i=0;i<code.length;i++)
   if((!Univers.existencePlanDeVaisseau(code[i]))||(nb[i]<0))
    return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DIVISER_FLOTTE_0000",code[i],nb[i]);

  Flotte ancienne=getFlotte(getCorrespondanceFlotte(numFlotte));
  Flotte nouvelle=ancienne.diviserFlotte(code,nb,nouveauNom);
  if(nouvelle.getNombreDeVaisseaux()!=0) {
   ajouterFlotte(nouvelle);
   ajouterCorrespondanceFlotte(10000+numeroDivision,numeroFlotte(nouvelle));
   }
  if(ancienne.getNombreDeVaisseaux()==0) eliminerFlotte(numFlotte);

  return ajouterEvenement("EV_COMMANDANT_DIVISER_FLOTTE_0000",ancienne.getNomNumero(getCorrespondanceFlotte(numFlotte)));
  }

 public boolean fusionnerFlotte(int nFlotte1,int nFlotte2,int directive,int directivePrecision){
  if(!Flotte.directiveExistante(directive,directivePrecision))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_FUSIONNER_FLOTTE_0000",directive,directivePrecision);
  int numFlotte1=getCorrespondanceFlotte(nFlotte1);
  int numFlotte2=getCorrespondanceFlotte(nFlotte2);
  if(!existenceFlotte(numFlotte1))
   return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0000",numFlotte1,numFlotte2);
  if(!existenceFlotte(numFlotte2))
   return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0000",numFlotte2,numFlotte1);
  if(numFlotte1==numFlotte2)
   return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0001",numFlotte2,numFlotte1);
  if(numFlotte1>numFlotte2){
   int inter=numFlotte1;
   numFlotte1=numFlotte2;
   numFlotte2=inter;
   }
  Flotte f1=getFlotte(numFlotte1);
  Flotte f2=getFlotte(numFlotte2);
  if(!f1.getPosition().equals(f2.getPosition()))
   return ajouterErreur("ER_COMMANDANT_FUSIONNER_FLOTTE_0002",f1.getNomNumero(numFlotte1),f2.getNomNumero(numFlotte2));

  int nouveauNum=numFlotte1;
  int numSupprime=numFlotte2;

  Flotte f3=f1.fusion(f2);
  f3.setDirectiveComplete(directive,directivePrecision);

  if((existenceHerosSurFlotte(numFlotte1))&&(existenceHerosSurFlotte(numFlotte2))){
   getHerosSurFlotte(numFlotte2).mettreEnReserve();
   getHerosSurFlotte(numFlotte1).setPosition(nouveauNum);
   }
   else if(existenceHerosSurFlotte(numFlotte1)) getHerosSurFlotte(numFlotte1).setPosition(nouveauNum);
    else if(existenceHerosSurFlotte(numFlotte2)) getHerosSurFlotte(numFlotte2).setPosition(nouveauNum);
  setFlotte(f3,nouveauNum);
  eliminerFlotte(numSupprime);

  return ajouterEvenement("EV_COMMANDANT_FUSIONNER_FLOTTE_0000",f1.getNomNumero(numFlotte1),f2.getNomNumero(numFlotte2));
  }

 public boolean pisterFlotte(int nFlotte,int numFlotteVisee,int joueurVise){
  int numFlotte=getCorrespondanceFlotte(nFlotte);
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PISTER_FLOTTE_0000",numFlotte+1);
  if(!Univers.existenceCommandant(joueurVise))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_PISTER_FLOTTE_0001",joueurVise);
  Commandant c=Univers.getCommandant(joueurVise);
  if(!c.existenceFlotte(numFlotteVisee))
   return ajouterErreur("ER_COMMANDANT_PISTER_FLOTTE_0000",c.getNomNumero(),numFlotteVisee+1);

  Flotte f=getFlotte(numFlotte);
  boolean reussite=f.pister(c.getFlotte(numFlotteVisee),joueurVise,getHerosSurFlotte(numFlotte));

  if(reussite)
   return ajouterEvenement("EV_COMMANDANT_PISTER_FLOTTE_0000",f.getNomNumero(numFlotte),c.getNomNumero(),numFlotteVisee+1);
   else
    return ajouterEvenement("EV_COMMANDANT_PISTER_FLOTTE_0001",f.getNomNumero(numFlotte),c.getNomNumero(),numFlotteVisee+1);
  }

 public boolean lancerMines(int numFlotte){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_LANCER_MINES_0000",numFlotte+1);
  Flotte f=getFlotte(numFlotte);
  if(!f.estLanceurDeMines())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_LANCER_MINES_0001",numFlotte+1);

  int nbMines=f.lancerMines(numero);

  return ajouterEvenement("EV_COMMANDANT_LANCER_MINES_0000",f.getNomNumero(numFlotte),nbMines);
  }

 public boolean coloniserPlanetes(int numFlotte,int numPlanete){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_COLONISER_PLANETE_0000",numFlotte+1);
  Flotte f=getFlotte(numFlotte);
  if(!f.contientColonisateur())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_COLONISER_PLANETE_0003",numFlotte+1);
  if(!Univers.existenceSysteme(f.getPosition()))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_COLONISER_PLANETE_0001",numFlotte+1);
  Systeme sys=Univers.getSysteme(f.getPosition());
  if(numPlanete>=sys.getNombrePlanetes())
   return ajouterErreur("ER_COMMANDANT_COLONISER_PLANETE_0001",f.getPosition(),numFlotte+1,numPlanete);
  if(!sys.getPlanete(numPlanete).estProprio(numero))
   return ajouterErreur("ER_COMMANDANT_COLONISER_PLANETE_0000",f.getPosition(),f.getNomNumero(numFlotte),
                         sys.getNomNumeroPlanete(numPlanete));

  int numRace=f.trouverColonisateur().getRaceEquipage();
  boolean reussite=sys.getPlanete(numPlanete).explorerPlanete(numRace);
  if(reussite) {
   f.supprimerVaisseau(f.trouverNumeroColonisateur());
   if(f.getNombreDeVaisseaux()==0) eliminerFlotte(numFlotte);
   ajouterReputation(Const.REPUTATION_COLONISER_PLANETE);
   }

  if(reussite) return ajouterEvenement("EV_COMMANDANT_COLONISER_PLANETE_0000",f.getPosition(),f.getNomNumero(numFlotte),
                         sys.getNomNumeroPlanete(numPlanete),Univers.getMessage("RACES",numRace,getLocale()));
   else return ajouterEvenement("EV_COMMANDANT_COLONISER_PLANETE_0001",f.getPosition(),f.getNomNumero(numFlotte),
                         sys.getNomNumeroPlanete(numPlanete),Univers.getMessage("RACES",numRace,getLocale()));
  }

 public boolean affecterHeros(String nomHeros,int numFlotte){
  if((!existenceFlotte(numFlotte))&&(numFlotte!=ProductionOrdres.VALEUR_DEFAUT.intValue()))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_AFFECTER_HEROS_0000",numFlotte+1);
  if(!existenceHeros(nomHeros)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_AFFECTER_HEROS_0001",nomHeros);

  if(numFlotte==ProductionOrdres.VALEUR_DEFAUT.intValue()){
   getHeros(nomHeros).mettreEnReserve();
   return true;
   }
   else{
    if(existenceHerosSurFlotte(numFlotte)) getHerosSurFlotte(numFlotte).mettreEnReserve();
    getHeros(nomHeros).setPosition(numFlotte);
    }

  return ajouterEvenement("EV_COMMANDANT_AFFECTER_HEROS_0000",nomHeros,getFlotte(numFlotte).getNomNumero(numFlotte));
  }

 public boolean affecterGouverneur(String nomGouverneur,Position pos){
  if((!existencePossession(pos))&&(!pos.equals(ProductionOrdres.POSITION_DEFAUT)))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_AFFECTER_GOUVERNEUR_0000",pos);
  if(!existenceGouverneur(nomGouverneur))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_AFFECTER_GOUVERNEUR_0001",nomGouverneur);

  if(pos.equals(ProductionOrdres.POSITION_DEFAUT)){
	getGouverneur(nomGouverneur).mettreEnReserve();
	return true;
    }
   else{
    if(existenceGouverneurSurPossession(pos)) getGouverneurSurPossession(pos).mettreEnReserve();
    getGouverneur(nomGouverneur).setPosition(pos);
    }

  return ajouterEvenement("EV_COMMANDANT_AFFECTER_GOUVERNEUR_0000",nomGouverneur,pos);
  }

 public boolean licencierLieutenant(String nomLieutenant){
  if(!existenceLieutenant(nomLieutenant))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_LICENCIER_LIEUTENANT_0000",nomLieutenant);

  Leader l=getLieutenant(nomLieutenant);
  supprimerLieutenant(nomLieutenant);
  modifierBudget(Const.BUDGET_COMMANDANT_LICENCIER_LIEUTENANT,l.getValeur());
  Univers.ajouterLeaderEnVente(l);

  return ajouterEvenement("EV_COMMANDANT_LICENCIER_LIEUTENANT_0000",nomLieutenant,l.getValeur());
  }

 public boolean renommerLieutenant(String nomLieutenant,String nouveauNom){
  if(!existenceLieutenant(nomLieutenant))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_RENOMMER_LIEUTENANT_0000",nomLieutenant);
  Leader l=getLieutenant(nomLieutenant);
  if(l.estNomme()) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_RENOMMER_LIEUTENANT_0001",nomLieutenant);
  if(existenceLieutenant(nouveauNom))
   return ajouterErreur("ER_COMMANDANT_RENOMMER_LIEUTENANT_0000",nomLieutenant,nouveauNom);

  l.setNom(nouveauNom);

  return true;
  }

 public boolean changerCapitale(Position pos){
  if(!existencePossession(pos)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CHANGER_CAPITALE_0000",pos);
  if(Const.COUT_CHANGER_CAPITALE>centaures)
   return ajouterErreur("ER_COMMANDANT_CHANGER_CAPITALE_0000",pos);

  modifierBudget(Const.BUDGET_COMMANDANT_CHANGER_CAPITALE,-Const.COUT_CHANGER_CAPITALE);
  setCapitale(pos);

  return ajouterEvenement("EV_COMMANDANT_CHANGER_CAPITALE_0000",pos);
  }

 public boolean donnerPlanDeVaisseau(String code,int destinataire,int modeTransfert){
  if(!estPlanDeVaisseauConnuPrive(code)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_PLAN_0000",code);
  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_PLAN_0001",destinataire);
  Commandant c=Univers.getCommandant(destinataire);
  if(c.estPlanDeVaisseauConnuPrive(code)) return ajouterErreur("ER_COMMANDANT_DON_PLAN_0000",c.getNomNumero(),code);
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_PLAN_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_PLAN_ANONYME;
  if((cout!=0F)&&(cout>centaures))
   return ajouterErreur("ER_COMMANDANT_DON_PLAN_0001",code,destinataire);

  modifierBudget(Const.BUDGET_COMMANDANT_DON_PLAN,-cout);
  c.ajouterPlanDeVaisseau(code);
  Univers.ajouterTransfert(this,c,"don plan : "+code);

  ajouterEvenement("EV_COMMANDANT_DON_PLAN_0000",c.getNomNumero(),code);
  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_PLAN_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0003",getNomNumero(),c.getNomNumero());
  if(modeTransfert==Const.DON_MODE_ANONYME)
   return c.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0002",code);
   else return c.ajouterEvenement("EV_COMMANDANT_DON_PLAN_0001",getNomNumero(),code);
  }

 public boolean creerPlanDeVaisseau(String code,String marque,String domaine,int royalties,String[] compo,int[] nbcompo){
  if(royalties>Const.ROYALTIES_MAXIMALES)
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CREER_PLAN_0000",royalties);
  if(!PlanDeVaisseau.verificationConformite(this,compo,code)) return false;
  int[] acces=PlanDeVaisseau.traductionDomaine(domaine);
  ArrayList a=new ArrayList();
  for(int i=0;i<compo.length;i++)
   for(int j=0;j<nbcompo[i];j++)
    a.add(compo[i]);
  String[] compo2=(String[])a.toArray(new String[0]);
  Vaisseau p=new PlanDeVaisseau(numero,getNom(),compo2,code,marque,"",acces[0],acces[1],royalties,
                                        PlanDeVaisseau.determinerCaracteristiquesSpeciales(this,compo2),
                                        PlanDeVaisseau.determinerMineraiNecessaire(this,compo2),
                                        PlanDeVaisseau.determinerPrix(this,compo2,royalties),
                                        PlanDeVaisseau.determinerMarchandisesNecessaires(this,compo2),
                                        PlanDeVaisseau.determinerNombreDeCases(compo2),Univers.getTour());
  if(centaures<p.getPrix()*Const.MODIFICATEUR_MULTIPLICATEUR_CREATION)
   return ajouterErreur("ER_COMMANDANT_CREER_PLAN_0000",code);

  Univers.ajouterPlanDeVaisseau(p);
  if(p.estPrive()) ajouterPlanDeVaisseau(code);
  modifierBudget(Const.BUDGET_COMMANDANT_CREATION_PLAN,-p.getPrix()*Const.MODIFICATEUR_MULTIPLICATEUR_CREATION);

  return ajouterEvenement("EV_COMMANDANT_CREER_PLAN_0000",code);
  }

 public boolean creerStrategie(String code,int agressivite,int ciblePrincipale,
                               String[] vaisseau,int[][] pos,int[][] tailleCible){
  if(connaitStrategie(code)) return ajouterErreur("ER_COMMANDANT_CREER_STRATEGIE_0000",code);
  boolean[] b=null;
  if((b=StrategieDeCombatSpatial.estStrategieValide(this,agressivite,ciblePrincipale,vaisseau,pos,tailleCible))==null)
   return false;

  int nb=0;
  for(int i=0;i<b.length;i++) if(b[i]) nb++;
  String[] v=new String[nb];
  int[][] p=new int[nb][2];
  int[][] t=new int[nb][10];
  nb=0;
  for(int i=0;i<b.length;i++)
   if(b[i]){
    v[nb]=vaisseau[i];
    p[nb]=pos[i];
    t[nb++]=tailleCible[i];
    }
  StrategieDeCombatSpatial s=new StrategieDeCombatSpatial(code,agressivite,ciblePrincipale,v,p,t);
  ajouterStrategie(s);

  return ajouterEvenement("EV_COMMANDANT_CREER_STRATEGIE_0000",code);
  }

 public boolean donnerStrategie(String code,int destinataire,int modeTransfert){
  if(!connaitStrategie(code)) return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_STRATEGIE_0000",code);
  if(!Univers.existenceCommandant(destinataire))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_DON_STRATEGIE_0001",destinataire);
  Commandant c=Univers.getCommandant(destinataire);
  if(c.connaitStrategie(code)) return ajouterErreur("ER_COMMANDANT_DON_STRATEGIE_0000",c.getNomNumero(),code);
  float cout=0F;
  if(modeTransfert==Const.DON_MODE_CACHE) cout=Const.SURCOUT_DON_STRATEGIE_CACHE;
  if(modeTransfert==Const.DON_MODE_ANONYME) cout=Const.SURCOUT_DON_STRATEGIE_ANONYME;
  if((cout!=0F)&&(cout>centaures))
   return ajouterErreur("ER_COMMANDANT_DON_STRATEGIE_0001",code,destinataire);

  modifierBudget(Const.BUDGET_COMMANDANT_DON_STRATEGIE,-cout);
  c.ajouterStrategie(getStrategie(code));

  ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0000",c.getNomNumero(),code);
  if((modeTransfert==Const.DON_MODE_NORMAL)&&(Univers.getTest(Const.CHANCE_DON_STRATEGIE_PUBLIC)))
   Univers.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0003",getNomNumero(),c.getNomNumero());
  if(modeTransfert==Const.DON_MODE_ANONYME)
   return c.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0002",code);
   else return c.ajouterEvenement("EV_COMMANDANT_DON_STRATEGIE_0001",getNomNumero(),code);
  }

 public boolean renommerSysteme(Position pos,String nouveauNom){
  if(!existencePossession(pos)) ajouterErreur("ER_COMMANDANT_RENOMMER_SYSTEME_0000",pos);
  Systeme sys=Univers.getSysteme(pos);
  if(sys.nbProprios()!=1) return ajouterErreur("ER_COMMANDANT_RENOMMER_SYSTEME_0000",pos);

  sys.setNom(nouveauNom);
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_RENOMMER_SYSTEME_0000",pos);
  }

 public boolean renommerPlanete(Position pos,String nouveauNom,int numPlanete){
  if(!existencePossession(pos)) return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0000",pos,numPlanete+1);
  Systeme sys=Univers.getSysteme(pos);
  if(numPlanete>=sys.getNombrePlanetes()) return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0000",pos,numPlanete+1);
  if(!sys.getPlanete(numPlanete).estProprio(numero))
   return ajouterErreur("ER_COMMANDANT_RENOMMER_PLANETE_0001",pos,sys.getNomNumeroPlanete(numPlanete));

  sys.getPlanete(numPlanete).setNom(nouveauNom);
  Univers.setSysteme(sys);

  return ajouterEvenement("EV_COMMANDANT_RENOMMER_PLANETE_0000",pos,sys.getNomNumeroPlanete(numPlanete),nouveauNom);
  }

 public boolean renommerFlotte(int numFlotte,String nouveauNom){
  if(!existenceFlotte(numFlotte)) return ajouterErreur("ER_COMMANDANT_RENOMMER_FLOTTE_0000",numFlotte+1);

  getFlotte(numFlotte).setNom(nouveauNom);

  return ajouterEvenement("EV_COMMANDANT_RENOMMER_FLOTTE_0000",nouveauNom,numFlotte+1);
  }

 public boolean programmerConstructionFlotte(int numFlotte,String codeConstruction){
  if(!existenceFlotte(numFlotte)) return ajouterErreur("ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000",numFlotte+1);
  Flotte f=getFlotte(numFlotte);
  if(!f.aCapaciteNavireUsine())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CONSTRUCTION_FLOTTE_0001",numFlotte+1);
  if(!estPlanDeVaisseauConnu(codeConstruction))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000",codeConstruction);

  f.setConstructionEnCours(codeConstruction);

  return ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_FLOTTE_0000",codeConstruction,numFlotte+1);
  }

 public boolean utiliserPorteGalactique(int numFlotte,int galaxie){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",numFlotte+1);
  Flotte f=getFlotte(numFlotte);
  boolean bonnePlace=false;
  for(int i=0;i<Const.PASSAGES_GALACTIQUES.length;i++)
   if(Arrays.equals(Const.PASSAGES_GALACTIQUES[i],f.getPosition().getPos())) bonnePlace=true;
  if((!bonnePlace)&&(!f.estInterGalactique())&&(existenceHerosSurFlotte(numFlotte) ?
       !getHerosSurFlotte(numFlotte).possedeCompetence(Const.COMPETENCE_LEADER_VOYAGE_INTERGALACTIQUE) : true))
   return ajouterErreur("ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",numFlotte+1);
  if(galaxie>=Const.NB_GALAXIES)
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0001",galaxie);

  f.getPosition().setNumeroGalaxie(galaxie);

  return ajouterEvenement("EV_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000",numFlotte+1);
  }

 public boolean utiliserPorteIntraGalactique(int numFlotte){
  if(!existenceFlotte(numFlotte))
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",numFlotte+1);
  Flotte f=getFlotte(numFlotte);
  if(!f.estSurPorteIntraGalactique())
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0001",numFlotte+1);

  float cout=(float)f.nombreTotalCases();

  if(centaures<cout)
   return ajouterErreur(getNomNumero(),"ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",numFlotte+1);

  int[] num=f.numeroPorteIntraGalactique();
  if(num[1]==0) f.getPosition().setPos(new int[]{Const.PORTES[num[0]][3],Const.PORTES[num[0]][4]});
   else f.getPosition().setPos(new int[]{Const.PORTES[num[0]][1],Const.PORTES[num[0]][2]});
  modifierBudget(Const.BUDGET_COMMANDANT_FRANCHISSEMENT_PORTE_INTRAGALACTIQUE,-cout);
  f.marquerDeplacement();

  return ajouterEvenement("EV_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000",numFlotte+1);
  }

 public boolean fixerTauxPostes(int taux){
  if(taux>100)
   return Univers.ajouterErreur(getNomNumero(),"ER_COMMANDANT_TAUX_POSTE_0000",taux);

  tauxTaxationPoste=taux;

  return ajouterEvenement("EV_COMMANDANT_TAUX_POSTE_0000",taux);
  }












 }
