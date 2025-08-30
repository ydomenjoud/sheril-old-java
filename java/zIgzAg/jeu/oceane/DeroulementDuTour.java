// v2.00 13/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.



package zIgzAg.jeu.oceane;
/*
 * @author  Julien Buret
 * @version 2.00, 13/01/01
 */

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import zIgzAg.utile.Copie;

 public class DeroulementDuTour{

  public static void main(String[] args){

   Univers univers=new Univers(true,"Déroulement du tour...");



   Commandant[] cL=Univers.getListeCommandants();
   for(int i=0;i<cL.length;i++) cL[i].initialiserChampsTransients();
   for(int i=0;i<cL.length;i++) cL[i].setBudget(Const.BUDGET_COMMANDANT_TOUR_PRECEDENT,cL[i].getCentaures());

   //spécial pour ce tour.



   //fin spécial

   System.out.println("Gestion des collisions...");
   for(int i=0;i<cL.length;i++) cL[i].resolutionCollisions();
   Univers.phaseSuivante();


   System.out.println("Reception des ordres et gestion de ces ordres...");
   //ReceptionOrdres.chargementOrdres();
   ReceptionOrdres ro=new ReceptionOrdres();
   Map m=ro.deroulementOrdres();
   ro=null;

   Integer[] listeOrdresRendus=(Integer[])m.keySet().toArray(new Integer[0]);
   System.out.println("");

   System.out.println("Debut des combats...");
   Combat.resolutionCombats();
   System.out.println("Gestion fin du tour...");
   Alliance.gererAlliances1();
   gestionFinDeTour(cL);
   Technologie.testDevenirTechnologiesPubliques();

   Univers.augmenterNumeroTour();
   System.out.println("Mise à jour de la base des commandants...");
   ProductionOrdres.produireRegistre(Utile.transformer(listeOrdresRendus));
   Leader.produireEncheres();
   Alliance.gererAlliances2();


   cL=null;m=null;listeOrdresRendus=null;

   Commandant[] cL2=Univers.getListeCommandantsHumains();

   for(int i=cL2.length-1;i>=0;i--){

    System.out.print(cL2[i].toString()+"-");
    cL2[i].determinerFlottesDetectes();
    cL2[i].determinerSystemesDetectes();

    Rapport r=new Rapport(cL2[i]);
    r.creation();
    r.ecriture();

    ProductionOrdres prod=new ProductionOrdres(cL2[i]);
    prod.creation();
    cL2[i].initialiserChampsTransients();

    }

   Rapport.ecrireMessagesSystemes();
   Stats.afficher(Locale.FRENCH);
   ProductionOrdres.productionMenuComplet(Locale.FRENCH);
   ProductionOrdres.produireOrdresGeneraux();
   ProductionOrdres.productionDonneeRaces(Locale.FRENCH);

   univers.sauvegarder();

   }


 public static void gestionFinDeTour(Commandant[] c){
  for(int i=0;i<c.length;i++){
   if(c[i].estJoueurHumain()) c[i].resolutionEvenements();
   c[i].progressionNiveauLieutenant();Univers.phaseSuivante();
   c[i].resolutionGestionSystemes();Univers.phaseSuivante();
   c[i].resolutionGestionFlottes();Univers.phaseSuivante();
   c[i].resolutionProgressionRecherche();Univers.phaseSuivante();
   c[i].finaliserBudget();Univers.phaseSuivante();
   c[i].ajouterReputation(Univers.getInt(50)+50);
   }
  }

 }
