// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;



public interface ListeTechnologique{
//SECTION SPECIALE
 public static final Arme mystI=new Arme("myst",0,ListeParents.mystI,50,null,1,1F,null,1,Const.CV_ARME_CS);
 public static final ComposantDeVaisseau mystII=new ComposantDeVaisseau("myst",1,ListeParents.mystI,100,ListeCaracSpeciales.mystII,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystIII=new ComposantDeVaisseau("myst",2,ListeParents.mystI,100,ListeCaracSpeciales.mystIII,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystIV=new ComposantDeVaisseau("myst",3,ListeParents.mystI,100,ListeCaracSpeciales.mystIV,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystV=new ComposantDeVaisseau("myst",4,ListeParents.mystI,100,ListeCaracSpeciales.mystV,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystVI=new ComposantDeVaisseau("myst",5,ListeParents.mystI,100,ListeCaracSpeciales.mystVI,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystVII=new ComposantDeVaisseau("myst",6,ListeParents.mystI,100,ListeCaracSpeciales.mystVII,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystVIII=new ComposantDeVaisseau("myst",7,ListeParents.mystI,100,ListeCaracSpeciales.mystVIII,1,1F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau mystIX=new ComposantDeVaisseau("myst",8,ListeParents.mystI,100,ListeCaracSpeciales.mystIX,1,1F,null,Const.CV_AUTRE,1);
 public static final Batiment mystX=new Batiment("myst",9,ListeParents.mystI,100,ListeCaracSpeciales.mystX,1,1F,null,20000,1,null);



 public static final Arme laserI=new Arme("laser",0,ListeParents.laserI,50,null,			7, 6F,ListeMarchandises.laserI,1,Const.CV_ARME_CS);
 public static final Arme laserII=new Arme("laser",1,ListeParents.laserII,100,null,			6, 7F,ListeMarchandises.laserII,1,Const.CV_ARME_CS);
 public static final Arme laserIII=new Arme("laser",2,ListeParents.laserIII,250,null,		5, 8F,ListeMarchandises.laserIII,1,Const.CV_ARME_CS);
 public static final Arme laserIV=new Arme("laser",3,ListeParents.laserIV,600,null,			5, 9F,ListeMarchandises.laserIV,1,Const.CV_ARME_CS);
 public static final Arme laserV=new Arme("laser",4,ListeParents.laserV,1400,null,			5, 10F,ListeMarchandises.laserV,1,Const.CV_ARME_CS);
 public static final Arme laserVI=new Arme("laser",5,ListeParents.laserVI,3000,null,		4, 11F,ListeMarchandises.laserVI,1,Const.CV_ARME_CS);
 public static final Arme laserVII=new Arme("laser",6,ListeParents.laserVII,6500,null,		3, 12F,ListeMarchandises.laserVII,1,Const.CV_ARME_CS);
 public static final Arme laserVIII=new Arme("laser",7,ListeParents.laserVIII,12500,null,	3, 13F,ListeMarchandises.laserVIII,1,Const.CV_ARME_CS);
 public static final Arme laserIX=new Arme("laser",8,ListeParents.laserIX,32000,null,		3, 14F,ListeMarchandises.laserIX,1,Const.CV_ARME_CS);
 public static final Arme laserX=new Arme("laser",9,ListeParents.laserX,50000,null,			2, 15F,ListeMarchandises.laserX,1,Const.CV_ARME_CS);

 public static final Arme plasmaI=new Arme("plasma",0,ListeParents.plasmaI,100,null,			7, 6F,ListeMarchandises.plasmaI,1,Const.CV_ARME_CS);
 public static final Arme plasmaII=new Arme("plasma",1,ListeParents.plasmaII,200,null,			6, 7F,ListeMarchandises.plasmaII,1,Const.CV_ARME_CS);
 public static final Arme plasmaIII=new Arme("plasma",2,ListeParents.plasmaIII,500,null,		5, 8F,ListeMarchandises.plasmaIII,1,Const.CV_ARME_CS);
 public static final Arme plasmaIV=new Arme("plasma",3,ListeParents.plasmaIV,1000,null,			5, 9F,ListeMarchandises.plasmaIV,1,Const.CV_ARME_CS);
 public static final Arme plasmaV=new Arme("plasma",4,ListeParents.plasmaV,1500,null,			5, 10F,ListeMarchandises.plasmaV,1,Const.CV_ARME_CS);
 public static final Arme plasmaVI=new Arme("plasma",5,ListeParents.plasmaVI,3000,null,			4, 11F,ListeMarchandises.plasmaVI,1,Const.CV_ARME_CS);
 public static final Arme plasmaVII=new Arme("plasma",6,ListeParents.plasmaVII,5000,null,		3, 12F,ListeMarchandises.plasmaVII,1,Const.CV_ARME_CS);
 public static final Arme plasmaVIII=new Arme("plasma",7,ListeParents.plasmaVIII,10000,null,	3, 13F,ListeMarchandises.plasmaVIII,1,Const.CV_ARME_CS);
 public static final Arme plasmaIX=new Arme("plasma",8,ListeParents.plasmaIX,15000,null,		3, 14F,ListeMarchandises.plasmaIX,1,Const.CV_ARME_CS);
 public static final Arme plasmaX=new Arme("plasma",9,ListeParents.plasmaX,30000,null,			2, 15F,ListeMarchandises.plasmaX,1,Const.CV_ARME_CS);

 public static final Arme torpI=new Arme("torp",0,ListeParents.torpI,100,null,			7, 6F,ListeMarchandises.torpI,1,Const.CV_ARME_CS);
 public static final Arme torpII=new Arme("torp",1,ListeParents.torpII,200,null,		6, 7F,ListeMarchandises.torpII,1,Const.CV_ARME_CS);
 public static final Arme torpIII=new Arme("torp",2,ListeParents.torpIII,400,null,		5, 8F,ListeMarchandises.torpIII,1,Const.CV_ARME_CS);
 public static final Arme torpIV=new Arme("torp",3,ListeParents.torpIV,800,null,		5, 9F,ListeMarchandises.torpIV,1,Const.CV_ARME_CS);
 public static final Arme torpV=new Arme("torp",4,ListeParents.torpV,1500,null,			5, 10F,ListeMarchandises.torpV,1,Const.CV_ARME_CS);
 public static final Arme torpVI=new Arme("torp",5,ListeParents.torpVI,3000,null,		4, 11F,ListeMarchandises.torpVI,1,Const.CV_ARME_CS);
 public static final Arme torpVII=new Arme("torp",6,ListeParents.torpVII,5000,null,		3, 12F,ListeMarchandises.torpVII,1,Const.CV_ARME_CS);
 public static final Arme torpVIII=new Arme("torp",7,ListeParents.torpVIII,10000,null,	3, 13F,ListeMarchandises.torpVIII,1,Const.CV_ARME_CS);
 public static final Arme torpIX=new Arme("torp",8,ListeParents.torpIX,15000,null,		3, 14F,ListeMarchandises.torpIX,1,Const.CV_ARME_CS);
 public static final Arme torpX=new Arme("torp",9,ListeParents.torpX,30000,null,		2, 15F,ListeMarchandises.torpX,1,Const.CV_ARME_CS);

 public static final Arme missI=new Arme("miss",0,ListeParents.missI,100,null,			7, 6F,ListeMarchandises.missI,1,Const.CV_ARME_CS);
 public static final Arme missII=new Arme("miss",1,ListeParents.missII,200,null,		6, 7F,ListeMarchandises.missII,1,Const.CV_ARME_CS);
 public static final Arme missIII=new Arme("miss",2,ListeParents.missIII,400,null,		5, 8F,ListeMarchandises.missIII,1,Const.CV_ARME_CS);
 public static final Arme missIV=new Arme("miss",3,ListeParents.missIV,800,null,		5, 9F,ListeMarchandises.missIV,1,Const.CV_ARME_CS);
 public static final Arme missV=new Arme("miss",4,ListeParents.missV,1500,null,			5, 10F,ListeMarchandises.missV,1,Const.CV_ARME_CS);
 public static final Arme missVI=new Arme("miss",5,ListeParents.missVI,3000,null,		4, 11F,ListeMarchandises.missVI,1,Const.CV_ARME_CS);
 public static final Arme missVII=new Arme("miss",6,ListeParents.missVII,5000,null,		3, 12F,ListeMarchandises.missVII,1,Const.CV_ARME_CS);
 public static final Arme missVIII=new Arme("miss",7,ListeParents.missVIII,10000,null,	3, 13F,ListeMarchandises.missVIII,1,Const.CV_ARME_CS);
 public static final Arme missIX=new Arme("miss",8,ListeParents.missIX,15000,null,		3, 14F,ListeMarchandises.missIX,1,Const.CV_ARME_CS);
 public static final Arme missX=new Arme("miss",9,ListeParents.missX,30000,null,		2, 15F,ListeMarchandises.missX,1,Const.CV_ARME_CS);


 public static final ComposantDeVaisseau absorbI    = new ComposantDeVaisseau("absorb",0,ListeParents.absorbI,1000,ListeCaracSpeciales.absorbI,30,100F,ListeMarchandises.absorbI,Const.CV_AUTRE,10);
 public static final ComposantDeVaisseau absorbII   = new ComposantDeVaisseau("absorb",1,ListeParents.absorbII,2000,ListeCaracSpeciales.absorbII,30,150F,ListeMarchandises.absorbII,Const.CV_AUTRE,20);
 public static final ComposantDeVaisseau absorbIII  = new ComposantDeVaisseau("absorb",2,ListeParents.absorbIII,5000,ListeCaracSpeciales.absorbIII,30,200F,ListeMarchandises.absorbIII,Const.CV_AUTRE,30);
 public static final ComposantDeVaisseau absorbIV   = new ComposantDeVaisseau("absorb",3,ListeParents.absorbIV,10000,ListeCaracSpeciales.absorbIV,30,250F,ListeMarchandises.absorbIV,Const.CV_AUTRE,50);
 public static final ComposantDeVaisseau absorbV    = new ComposantDeVaisseau("absorb",4,ListeParents.absorbV,17500,ListeCaracSpeciales.absorbV,30,300F,ListeMarchandises.absorbV,Const.CV_AUTRE,70);
 public static final ComposantDeVaisseau absorbVI   = new ComposantDeVaisseau("absorb",5,ListeParents.absorbVI,30000,ListeCaracSpeciales.absorbVI,30,350F,ListeMarchandises.absorbVI,Const.CV_AUTRE,100);
 public static final ComposantDeVaisseau absorbVII  = new ComposantDeVaisseau("absorb",6,ListeParents.absorbVII,50000,ListeCaracSpeciales.absorbVII,30,400F,ListeMarchandises.absorbVII,Const.CV_AUTRE,140);
 public static final ComposantDeVaisseau absorbVIII = new ComposantDeVaisseau("absorb",7,ListeParents.absorbVIII,75000,ListeCaracSpeciales.absorbVIII,30,450F,ListeMarchandises.absorbVIII,Const.CV_AUTRE,180);
 public static final ComposantDeVaisseau absorbIX   = new ComposantDeVaisseau("absorb",8,ListeParents.absorbIX,110000,ListeCaracSpeciales.absorbIX,30,500F,ListeMarchandises.absorbIX,Const.CV_AUTRE,230);
 public static final ComposantDeVaisseau absorbX    = new ComposantDeVaisseau("absorb",9,ListeParents.absorbX,175000,ListeCaracSpeciales.absorbX,30,600F,ListeMarchandises.absorbX,Const.CV_AUTRE,300);

 public static final ComposantDeVaisseau balastI=new ComposantDeVaisseau("balast",0,ListeParents.balastI,250,null,2,5F,null,Const.CV_AUTRE,10);
 public static final ComposantDeVaisseau balastII=new ComposantDeVaisseau("balast",1,ListeParents.balastII,500,null,4,10F,null,Const.CV_AUTRE,40);
 public static final ComposantDeVaisseau balastIII=new ComposantDeVaisseau("balast",2,ListeParents.balastIII,800,null,6,20F,null,Const.CV_AUTRE,100);
 public static final ComposantDeVaisseau balastIV=new ComposantDeVaisseau("balast",3,ListeParents.balastIV,1200,null,10,40F,null,Const.CV_AUTRE,250);
 public static final ComposantDeVaisseau balastV=new ComposantDeVaisseau("balast",4,ListeParents.balastV,2000,null,20,70F,ListeMarchandises.balastV,Const.CV_AUTRE, 600);

    /* 
 public static final ComposantDeVaisseau balastVI=new ComposantDeVaisseau("balast",5,ListeParents.balastVI,4000,null,30,120F,ListeMarchandises.balastVI,Const.CV_AUTRE,70);
 public static final ComposantDeVaisseau balastVII=new ComposantDeVaisseau("balast",6,ListeParents.balastVII,7000,null,40,160F,ListeMarchandises.balastVII,Const.CV_AUTRE,100);
 public static final ComposantDeVaisseau balastVIII=new ComposantDeVaisseau("balast",7,ListeParents.balastVIII,12000,null,50,170F,ListeMarchandises.balastVIII,Const.CV_AUTRE,140);
 public static final ComposantDeVaisseau balastIX=new ComposantDeVaisseau("balast",8,ListeParents.balastIX,20000,null,60,240F,ListeMarchandises.balastIX,Const.CV_AUTRE,190);
 public static final ComposantDeVaisseau balastX=new ComposantDeVaisseau("balast",9,ListeParents.balastX,50000,null,80,320F,ListeMarchandises.balastX,Const.CV_AUTRE,250);
    */

 public static final Arme bombeI=new Arme("bombe",0,ListeParents.bombeI,100,null,			5, 7F,ListeMarchandises.bombeI,1,Const.CV_ARME_CP);
 public static final Arme bombeII=new Arme("bombe",1,ListeParents.bombeII,200,null,			5, 8F,ListeMarchandises.bombeII,1,Const.CV_ARME_CP);
 public static final Arme bombeIII=new Arme("bombe",2,ListeParents.bombeIII,400,null,		4, 9F,ListeMarchandises.bombeIII,1,Const.CV_ARME_CP);
 public static final Arme bombeIV=new Arme("bombe",3,ListeParents.bombeIV,800,null,			4, 10F,ListeMarchandises.bombeIV,1,Const.CV_ARME_CP);
 public static final Arme bombeV=new Arme("bombe",4,ListeParents.bombeV,1500,null,			3, 11F,ListeMarchandises.bombeV,1,Const.CV_ARME_CP);
 public static final Arme bombeVI=new Arme("bombe",5,ListeParents.bombeVI,3000,null,		3, 12F,ListeMarchandises.bombeVI,1,Const.CV_ARME_CP);
 public static final Arme bombeVII=new Arme("bombe",6,ListeParents.bombeVII,5000,null,		2, 13F,ListeMarchandises.bombeVII,1,Const.CV_ARME_CP);
 public static final Arme bombeVIII=new Arme("bombe",7,ListeParents.bombeVIII,10000,null,	2, 14F,ListeMarchandises.bombeVIII,1,Const.CV_ARME_CP);
 public static final Arme bombeIX=new Arme("bombe",8,ListeParents.bombeIX,15000,null,		2, 15F,ListeMarchandises.bombeIX,1,Const.CV_ARME_CP);
 public static final Arme bombeX=new Arme("bombe",9,ListeParents.bombeX,30000,null,			1, 16F,ListeMarchandises.bombeX,1,Const.CV_ARME_CP);

 public static final ComposantDeVaisseau moteurI=new ComposantDeVaisseau("moteur",0,null,50,ListeCaracSpeciales.moteurI,1,4F,ListeMarchandises.moteurI,Const.CV_MOTEUR,1);
 public static final ComposantDeVaisseau moteurII=new ComposantDeVaisseau("moteur",1,ListeParents.moteurII,100,ListeCaracSpeciales.moteurII,4,8F,ListeMarchandises.moteurII,Const.CV_MOTEUR,4);
 public static final ComposantDeVaisseau moteurIII=new ComposantDeVaisseau("moteur",2,ListeParents.moteurIII,200,ListeCaracSpeciales.moteurIII,8,12F,ListeMarchandises.moteurIII,Const.CV_MOTEUR,8);
 public static final ComposantDeVaisseau moteurIV=new ComposantDeVaisseau("moteur",3,ListeParents.moteurIV,400,ListeCaracSpeciales.moteurIV,15,16F,ListeMarchandises.moteurIV,Const.CV_MOTEUR,15);
 public static final ComposantDeVaisseau moteurV=new ComposantDeVaisseau("moteur",4,ListeParents.moteurV,800,ListeCaracSpeciales.moteurV,30,30F,ListeMarchandises.moteurV,Const.CV_MOTEUR,30);
 public static final ComposantDeVaisseau moteurVI=new ComposantDeVaisseau("moteur",5,ListeParents.moteurVI,1500,ListeCaracSpeciales.moteurVI,60,60F,ListeMarchandises.moteurVI,Const.CV_MOTEUR,60);
 public static final ComposantDeVaisseau moteurVII=new ComposantDeVaisseau("moteur",6,ListeParents.moteurVII,3000,ListeCaracSpeciales.moteurVII,120,120F,ListeMarchandises.moteurVII,Const.CV_MOTEUR,120);
 public static final ComposantDeVaisseau moteurVIII=new ComposantDeVaisseau("moteur",7,ListeParents.moteurVIII,7500,ListeCaracSpeciales.moteurVIII,250,250F,ListeMarchandises.moteurVIII,Const.CV_MOTEUR,250);
 public static final ComposantDeVaisseau moteurIX=new ComposantDeVaisseau("moteur",8,ListeParents.moteurIX,15000,ListeCaracSpeciales.moteurIX,500,500F,ListeMarchandises.moteurIX,Const.CV_MOTEUR,500);
 public static final ComposantDeVaisseau moteurX=new ComposantDeVaisseau("moteur",9,ListeParents.moteurX,30000,ListeCaracSpeciales.moteurX,1000,1000F,ListeMarchandises.moteurX,Const.CV_MOTEUR,1000);

 public static final ComposantDeVaisseau intraI=new ComposantDeVaisseau("intra",0,ListeParents.intraI,500000,ListeCaracSpeciales.intraI,10,25F,ListeMarchandises.intraI,Const.CV_MOTEUR,1);

 public static final ComposantDeVaisseau interI=new ComposantDeVaisseau("inter",0,ListeParents.interI,200000,ListeCaracSpeciales.interI,20,50F,ListeMarchandises.interI,Const.CV_MOTEUR,2);

 public static final ComposantDeVaisseau scanI=new ComposantDeVaisseau("scan",0,null,100,ListeCaracSpeciales.scanI,5,10F,null,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau scanII=new ComposantDeVaisseau("scan",1,ListeParents.scanII,200,ListeCaracSpeciales.scanII,10,15F,ListeMarchandises.scanII,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau scanIII=new ComposantDeVaisseau("scan",2,ListeParents.scanIII,400,ListeCaracSpeciales.scanIII,20,15F,ListeMarchandises.scanIII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau scanIV=new ComposantDeVaisseau("scan",3,ListeParents.scanIV,800,ListeCaracSpeciales.scanIV,25,20F,ListeMarchandises.scanIV,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau scanV=new ComposantDeVaisseau("scan",4,ListeParents.scanV,1600,ListeCaracSpeciales.scanV,30,25F,ListeMarchandises.scanV,Const.CV_AUTRE,11);
 public static final ComposantDeVaisseau scanVI=new ComposantDeVaisseau("scan",5,ListeParents.scanVI,3000,ListeCaracSpeciales.scanVI,40,35F,ListeMarchandises.scanVI,Const.CV_AUTRE,14);
 public static final ComposantDeVaisseau scanVII=new ComposantDeVaisseau("scan",6,ListeParents.scanVII,5000,ListeCaracSpeciales.scanVII,70,80F,ListeMarchandises.scanVII,Const.CV_AUTRE,17);
 public static final ComposantDeVaisseau scanVIII=new ComposantDeVaisseau("scan",7,ListeParents.scanVIII,10000,ListeCaracSpeciales.scanVIII,140,160F,ListeMarchandises.scanVIII,Const.CV_AUTRE,21);
 public static final ComposantDeVaisseau scanIX=new ComposantDeVaisseau("scan",8,ListeParents.scanIX,15000,ListeCaracSpeciales.scanIX,320,300F,ListeMarchandises.scanIX,Const.CV_AUTRE,26);
 public static final ComposantDeVaisseau scanX=new ComposantDeVaisseau("scan",9,ListeParents.scanX,30000,ListeCaracSpeciales.scanX,600,700F,ListeMarchandises.scanX,Const.CV_AUTRE,32);

 public static final ComposantDeVaisseau bouclierI=new ComposantDeVaisseau("bouclier",0,ListeParents.bouclierI,100,ListeCaracSpeciales.bouclierI,10,10F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau bouclierII=new ComposantDeVaisseau("bouclier",1,ListeParents.bouclierII,200,ListeCaracSpeciales.bouclierII,10,20F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau bouclierIII=new ComposantDeVaisseau("bouclier",2,ListeParents.bouclierIII,400,ListeCaracSpeciales.bouclierIII,15,30F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau bouclierIV=new ComposantDeVaisseau("bouclier",3,ListeParents.bouclierIV,800,ListeCaracSpeciales.bouclierIV,20,40F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau bouclierV=new ComposantDeVaisseau("bouclier",4,ListeParents.bouclierV,1600,ListeCaracSpeciales.bouclierV,25,50F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau bouclierVI=new ComposantDeVaisseau("bouclier",5,ListeParents.bouclierVI,3000,ListeCaracSpeciales.bouclierVI,30,60F,null,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau bouclierVII=new ComposantDeVaisseau("bouclier",6,ListeParents.bouclierVII,5000,ListeCaracSpeciales.bouclierVII,35,70F,null,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau bouclierVIII=new ComposantDeVaisseau("bouclier",7,ListeParents.bouclierVIII,10000,ListeCaracSpeciales.bouclierVIII,40,80F,ListeMarchandises.bouclierVIII,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau bouclierIX=new ComposantDeVaisseau("bouclier",8,ListeParents.bouclierIX,15000,ListeCaracSpeciales.bouclierIX,45,90F,ListeMarchandises.bouclierIX,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau bouclierX=new ComposantDeVaisseau("bouclier",9,ListeParents.bouclierX,30000,ListeCaracSpeciales.bouclierX,50,100F,ListeMarchandises.bouclierX,Const.CV_AUTRE,5);

 public static final ComposantDeVaisseau lmineI=new ComposantDeVaisseau("lmine",0,ListeParents.lmineI,100,ListeCaracSpeciales.lmineI,10,10F,ListeMarchandises.lmineI,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau lmineII=new ComposantDeVaisseau("lmine",1,ListeParents.lmineII,200,ListeCaracSpeciales.lmineII,15,20F,ListeMarchandises.lmineII,Const.CV_AUTRE,2);
 public static final ComposantDeVaisseau lmineIII=new ComposantDeVaisseau("lmine",2,ListeParents.lmineIII,400,ListeCaracSpeciales.lmineIII,20,30F,ListeMarchandises.lmineIII,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau lmineIV=new ComposantDeVaisseau("lmine",3,ListeParents.lmineIV,800,ListeCaracSpeciales.lmineIV,25,40F,ListeMarchandises.lmineIV,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau lmineV=new ComposantDeVaisseau("lmine",4,ListeParents.lmineV,1600,ListeCaracSpeciales.lmineV,30,50F,ListeMarchandises.lmineV,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau lmineVI=new ComposantDeVaisseau("lmine",5,ListeParents.lmineVI,3000,ListeCaracSpeciales.lmineVI,35,55F,ListeMarchandises.lmineVI,Const.CV_AUTRE,6);
 public static final ComposantDeVaisseau lmineVII=new ComposantDeVaisseau("lmine",6,ListeParents.lmineVII,5000,ListeCaracSpeciales.lmineVII,40,60F,ListeMarchandises.lmineVII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau lmineVIII=new ComposantDeVaisseau("lmine",7,ListeParents.lmineVIII,10000,ListeCaracSpeciales.lmineVIII,45,70F,ListeMarchandises.lmineVIII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau lmineIX=new ComposantDeVaisseau("lmine",8,ListeParents.lmineIX,15000,ListeCaracSpeciales.lmineIX,50,80F,ListeMarchandises.lmineIX,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau lmineX=new ComposantDeVaisseau("lmine",9,ListeParents.lmineX,30000,ListeCaracSpeciales.lmineX,55,85F,ListeMarchandises.lmineX,Const.CV_AUTRE,10);

 public static final ComposantDeVaisseau dmineI=new ComposantDeVaisseau("dmine",0,ListeParents.dmineI,100,ListeCaracSpeciales.dmineI,5,10F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau dmineII=new ComposantDeVaisseau("dmine",1,ListeParents.dmineII,200,ListeCaracSpeciales.dmineII,10,20F,null,Const.CV_AUTRE,2);
 public static final ComposantDeVaisseau dmineIII=new ComposantDeVaisseau("dmine",2,ListeParents.dmineIII,400,ListeCaracSpeciales.dmineIII,15,30F,ListeMarchandises.dmineIII,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau dmineIV=new ComposantDeVaisseau("dmine",3,ListeParents.dmineIV,800,ListeCaracSpeciales.dmineIV,20,40F,ListeMarchandises.dmineIV,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau dmineV=new ComposantDeVaisseau("dmine",4,ListeParents.dmineV,1600,ListeCaracSpeciales.dmineV,25,50F,ListeMarchandises.dmineV,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau dmineVI=new ComposantDeVaisseau("dmine",5,ListeParents.dmineVI,3000,ListeCaracSpeciales.dmineVI,30,60F,ListeMarchandises.dmineVI,Const.CV_AUTRE,6);
 public static final ComposantDeVaisseau dmineVII=new ComposantDeVaisseau("dmine",6,ListeParents.dmineVII,5000,ListeCaracSpeciales.dmineVII,35,70F,ListeMarchandises.dmineVII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau dmineVIII=new ComposantDeVaisseau("dmine",7,ListeParents.dmineVIII,10000,ListeCaracSpeciales.dmineVIII,40,80F,ListeMarchandises.dmineVIII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau dmineIX=new ComposantDeVaisseau("dmine",8,ListeParents.dmineIX,15000,ListeCaracSpeciales.dmineIX,45,90F,ListeMarchandises.dmineIX,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau dmineX=new ComposantDeVaisseau("dmine",9,ListeParents.dmineX,30000,ListeCaracSpeciales.dmineX,50,100F,ListeMarchandises.dmineX,Const.CV_AUTRE,10);

 public static final ComposantDeVaisseau mconstruI=new ComposantDeVaisseau("mconstru",0,ListeParents.mconstruI,600,ListeCaracSpeciales.mconstruI,20,50F,ListeMarchandises.mconstruI,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau mconstruII=new ComposantDeVaisseau("mconstru",1,ListeParents.mconstruII,800,ListeCaracSpeciales.mconstruII,40,100F,ListeMarchandises.mconstruII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau mconstruIII=new ComposantDeVaisseau("mconstru",2,ListeParents.mconstruIII,1250,ListeCaracSpeciales.mconstruIII,70,150F,ListeMarchandises.mconstruIII,Const.CV_AUTRE,12);
 public static final ComposantDeVaisseau mconstruIV=new ComposantDeVaisseau("mconstru",3,ListeParents.mconstruIV,5000,ListeCaracSpeciales.mconstruIV,150,200F,ListeMarchandises.mconstruIV,Const.CV_AUTRE,16);
 public static final ComposantDeVaisseau mconstruV=new ComposantDeVaisseau("mconstru",4,ListeParents.mconstruV,12500,ListeCaracSpeciales.mconstruV,240,1000F,ListeMarchandises.mconstruV,Const.CV_AUTRE,20);
 public static final ComposantDeVaisseau mconstruVI=new ComposantDeVaisseau("mconstru",5,ListeParents.mconstruVI,25000,ListeCaracSpeciales.mconstruVI,400,2000F,ListeMarchandises.mconstruVI,Const.CV_AUTRE,30);
 public static final ComposantDeVaisseau mconstruVII=new ComposantDeVaisseau("mconstru",6,ListeParents.mconstruVII,60000,ListeCaracSpeciales.mconstruVII,700,5000F,ListeMarchandises.mconstruVII,Const.CV_AUTRE,40);
 
 public static final ComposantDeVaisseau bscanI    = new ComposantDeVaisseau("bscan",0,ListeParents.bscanI,100,ListeCaracSpeciales.bscanI,5,10F,null,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau bscanII   = new ComposantDeVaisseau("bscan",1,ListeParents.bscanII,200,ListeCaracSpeciales.bscanII,10,20F,ListeMarchandises.bscanII,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau bscanIII  = new ComposantDeVaisseau("bscan",2,ListeParents.bscanIII,400,ListeCaracSpeciales.bscanIII,15,30F,ListeMarchandises.bscanIII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau bscanIV   = new ComposantDeVaisseau("bscan",3,ListeParents.bscanIV,800,ListeCaracSpeciales.bscanIV,20,40F,ListeMarchandises.bscanIV,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau bscanV    = new ComposantDeVaisseau("bscan",4,ListeParents.bscanV,1600,ListeCaracSpeciales.bscanV,25,50F,ListeMarchandises.bscanV,Const.CV_AUTRE,11);
 public static final ComposantDeVaisseau bscanVI   = new ComposantDeVaisseau("bscan",5,ListeParents.bscanVI,3000,ListeCaracSpeciales.bscanVI,30,60F,ListeMarchandises.bscanVI,Const.CV_AUTRE,14);
 public static final ComposantDeVaisseau bscanVII  = new ComposantDeVaisseau("bscan",6,ListeParents.bscanVII,5000,ListeCaracSpeciales.bscanVII,35,70F,ListeMarchandises.bscanVII,Const.CV_AUTRE,17);
 public static final ComposantDeVaisseau bscanVIII = new ComposantDeVaisseau("bscan",7,ListeParents.bscanVIII,10000,ListeCaracSpeciales.bscanVIII,40,100F,ListeMarchandises.bscanVIII,Const.CV_AUTRE,21);
 public static final ComposantDeVaisseau bscanIX   = new ComposantDeVaisseau("bscan",8,ListeParents.bscanIX,15000,ListeCaracSpeciales.bscanIX,45,200F,ListeMarchandises.bscanIX,Const.CV_AUTRE,26);
 public static final ComposantDeVaisseau bscanX    = new ComposantDeVaisseau("bscan",9,ListeParents.bscanX,30000,ListeCaracSpeciales.bscanX,50,500F,ListeMarchandises.bscanX,Const.CV_AUTRE,32);

 public static final ComposantDeVaisseau tractI=new ComposantDeVaisseau("tract",0,ListeParents.tractI,200,ListeCaracSpeciales.tractI,5,10F,null,Const.CV_AUTRE,1);
 

 public static final ComposantDeVaisseau cargoI=new ComposantDeVaisseau("cargo",0,ListeParents.cargoI,100,ListeCaracSpeciales.cargoI,1,5F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau cargoII=new ComposantDeVaisseau("cargo",1,ListeParents.cargoII,200,ListeCaracSpeciales.cargoII,2,10F,null,Const.CV_AUTRE,2);
 public static final ComposantDeVaisseau cargoIII=new ComposantDeVaisseau("cargo",2,ListeParents.cargoIII,400,ListeCaracSpeciales.cargoIII,3,20F,null,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau cargoIV=new ComposantDeVaisseau("cargo",3,ListeParents.cargoIV,800,ListeCaracSpeciales.cargoIV,4,40F,null,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau cargoV=new ComposantDeVaisseau("cargo",4,ListeParents.cargoV,1600,ListeCaracSpeciales.cargoV,5,50F,ListeMarchandises.cargoV,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau cargoVI=new ComposantDeVaisseau("cargo",5,ListeParents.cargoVI,3000,ListeCaracSpeciales.cargoVI,7,60F,ListeMarchandises.cargoVI,Const.CV_AUTRE,6);
 public static final ComposantDeVaisseau cargoVII=new ComposantDeVaisseau("cargo",6,ListeParents.cargoVII,5000,ListeCaracSpeciales.cargoVII,9,70F,ListeMarchandises.cargoVII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau cargoVIII=new ComposantDeVaisseau("cargo",7,ListeParents.cargoVIII,10000,ListeCaracSpeciales.cargoVIII,12,80F,ListeMarchandises.cargoVIII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau cargoIX=new ComposantDeVaisseau("cargo",8,ListeParents.cargoIX,15000,ListeCaracSpeciales.cargoIX,15,90F,ListeMarchandises.cargoIX,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau cargoX=new ComposantDeVaisseau("cargo",9,ListeParents.cargoX,30000,ListeCaracSpeciales.cargoX,20,100F,ListeMarchandises.cargoX,Const.CV_AUTRE,10);


 public static final ComposantDeVaisseau villeI=new ComposantDeVaisseau("ville",0,ListeParents.villeI,100,ListeCaracSpeciales.villeI,5,10F,ListeMarchandises.villeI,Const.CV_AUTRE, 20);
 public static final ComposantDeVaisseau villeII=new ComposantDeVaisseau("ville",1,ListeParents.villeII,200,ListeCaracSpeciales.villeII,10,20F,ListeMarchandises.villeII,Const.CV_AUTRE, 30);
 public static final ComposantDeVaisseau villeIII=new ComposantDeVaisseau("ville",2,ListeParents.villeIII,400,ListeCaracSpeciales.villeIII,15,30F,ListeMarchandises.villeIII,Const.CV_AUTRE,40);
 public static final ComposantDeVaisseau villeIV=new ComposantDeVaisseau("ville",3,ListeParents.villeIV,800,ListeCaracSpeciales.villeIV,20,40F,ListeMarchandises.villeIV,Const.CV_AUTRE,60);
 public static final ComposantDeVaisseau villeV=new ComposantDeVaisseau("ville",4,ListeParents.villeV,5000,ListeCaracSpeciales.villeV,25,50F,ListeMarchandises.villeV,Const.CV_AUTRE,80);
 public static final ComposantDeVaisseau villeVI=new ComposantDeVaisseau("ville",5,ListeParents.villeVI,10000,ListeCaracSpeciales.villeVI,30,60F,ListeMarchandises.villeVI,Const.CV_AUTRE,100);
 public static final ComposantDeVaisseau villeVII=new ComposantDeVaisseau("ville",6,ListeParents.villeVII,20000,ListeCaracSpeciales.villeVII,35,70F,ListeMarchandises.villeVII,Const.CV_AUTRE,150);
    /**
 public static final ComposantDeVaisseau villeVIII=new ComposantDeVaisseau("ville",7,ListeParents.villeVIII,50000,ListeCaracSpeciales.villeVIII,40,80F,ListeMarchandises.villeVIII,Const.CV_AUTRE,200);
 public static final ComposantDeVaisseau villeIX=new ComposantDeVaisseau("ville",8,ListeParents.villeIX,100000,ListeCaracSpeciales.villeIX,45,90F,ListeMarchandises.villeIX,Const.CV_AUTRE,260);
 public static final ComposantDeVaisseau villeX=new ComposantDeVaisseau("ville",9,ListeParents.villeX,200000,ListeCaracSpeciales.villeX,50,100F,ListeMarchandises.villeX,Const.CV_AUTRE,350);
    */
 public static final ComposantDeVaisseau robocoI=new ComposantDeVaisseau("roboco",0,ListeParents.robocoI,100,ListeCaracSpeciales.robocoI,10,50F,ListeMarchandises.robocoI,Const.CV_AUTRE,12);
    /*
 public static final ComposantDeVaisseau hscanI=new ComposantDeVaisseau("hscan",0,ListeParents.hscanI,100,ListeCaracSpeciales.hscanI,5,10F,null,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau hscanII=new ComposantDeVaisseau("hscan",1,ListeParents.hscanII,200,ListeCaracSpeciales.hscanII,10,15F,ListeMarchandises.hscanII,Const.CV_AUTRE,2);
 public static final ComposantDeVaisseau hscanIII=new ComposantDeVaisseau("hscan",2,ListeParents.hscanIII,400,ListeCaracSpeciales.hscanIII,20,15F,ListeMarchandises.hscanIII,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau hscanIV=new ComposantDeVaisseau("hscan",3,ListeParents.hscanIV,800,ListeCaracSpeciales.hscanIV,25,20F,ListeMarchandises.hscanIV,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau hscanV=new ComposantDeVaisseau("hscan",4,ListeParents.hscanV,1600,ListeCaracSpeciales.hscanV,30,25F,ListeMarchandises.hscanV,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau hscanVI=new ComposantDeVaisseau("hscan",5,ListeParents.hscanVI,3000,ListeCaracSpeciales.hscanVI,40,35F,ListeMarchandises.hscanVI,Const.CV_AUTRE,6);
 public static final ComposantDeVaisseau hscanVII=new ComposantDeVaisseau("hscan",6,ListeParents.hscanVII,5000,ListeCaracSpeciales.hscanVII,70,80F,ListeMarchandises.hscanVII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau hscanVIII=new ComposantDeVaisseau("hscan",7,ListeParents.hscanVIII,10000,ListeCaracSpeciales.hscanVIII,140,160F,ListeMarchandises.hscanVIII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau hscanIX=new ComposantDeVaisseau("hscan",8,ListeParents.hscanIX,15000,ListeCaracSpeciales.hscanIX,320,300F,ListeMarchandises.hscanIX,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau hscanX=new ComposantDeVaisseau("hscan",9,ListeParents.hscanX,30000,ListeCaracSpeciales.hscanX,600,700F,ListeMarchandises.hscanX,Const.CV_AUTRE,10);
    */
 public static final ComposantDeVaisseau draminI=new ComposantDeVaisseau("dramin",0,ListeParents.draminI,100,ListeCaracSpeciales.draminI,1,10F,ListeMarchandises.draminI,Const.CV_AUTRE,1);
 public static final ComposantDeVaisseau draminII=new ComposantDeVaisseau("dramin",1,ListeParents.draminII,200,ListeCaracSpeciales.draminII,2,15F,ListeMarchandises.draminII,Const.CV_AUTRE,2);
 public static final ComposantDeVaisseau draminIII=new ComposantDeVaisseau("dramin",2,ListeParents.draminIII,400,ListeCaracSpeciales.draminIII,2,20F,ListeMarchandises.draminIII,Const.CV_AUTRE,3);
 public static final ComposantDeVaisseau draminIV=new ComposantDeVaisseau("dramin",3,ListeParents.draminIV,800,ListeCaracSpeciales.draminIV,3,30F,ListeMarchandises.draminIV,Const.CV_AUTRE,4);
 public static final ComposantDeVaisseau draminV=new ComposantDeVaisseau("dramin",4,ListeParents.draminV,1600,ListeCaracSpeciales.draminV,4,40F,ListeMarchandises.draminV,Const.CV_AUTRE,5);
 public static final ComposantDeVaisseau draminVI=new ComposantDeVaisseau("dramin",5,ListeParents.draminVI,3000,ListeCaracSpeciales.draminVI,5,50F,ListeMarchandises.draminVI,Const.CV_AUTRE,6);
 public static final ComposantDeVaisseau draminVII=new ComposantDeVaisseau("dramin",6,ListeParents.draminVII,5000,ListeCaracSpeciales.draminVII,6,60F,ListeMarchandises.draminVII,Const.CV_AUTRE,7);
 public static final ComposantDeVaisseau draminVIII=new ComposantDeVaisseau("dramin",7,ListeParents.draminVIII,10000,ListeCaracSpeciales.draminVIII,7,70F,ListeMarchandises.draminVIII,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau draminIX=new ComposantDeVaisseau("dramin",8,ListeParents.draminIX,15000,ListeCaracSpeciales.draminIX,8,80F,ListeMarchandises.draminIX,Const.CV_AUTRE,9);
 public static final ComposantDeVaisseau draminX=new ComposantDeVaisseau("dramin",9,ListeParents.draminX,30000,ListeCaracSpeciales.draminX,10,100F,ListeMarchandises.draminX,Const.CV_AUTRE,10);



    // COMPOSANT CYBORGS

 public static final ComposantDeVaisseau cyb_mdc_t1_I   = new ComposantDeVaisseau("cyb_mdc_t1_",0,ListeParents.cyb_mdc_t1_I,600,ListeCaracSpeciales.cyb_mdc_t1_I,20,50F,ListeMarchandises.cyb_mdc_t1_I,Const.CV_AUTRE, 4);
 public static final ComposantDeVaisseau cyb_mdc_t1_II  = new ComposantDeVaisseau("cyb_mdc_t1_",1,ListeParents.cyb_mdc_t1_II,800,ListeCaracSpeciales.cyb_mdc_t1_II,40,100F,ListeMarchandises.cyb_mdc_t1_II,Const.CV_AUTRE,8);
 public static final ComposantDeVaisseau cyb_mdc_t1_III = new ComposantDeVaisseau("cyb_mdc_t1_",2,ListeParents.cyb_mdc_t1_III,1250,ListeCaracSpeciales.cyb_mdc_t1_III,70,150F,ListeMarchandises.cyb_mdc_t1_III,Const.CV_AUTRE,12);
 public static final ComposantDeVaisseau cyb_mdc_t1_IV  = new ComposantDeVaisseau("cyb_mdc_t1_",3,ListeParents.cyb_mdc_t1_IV,5000,ListeCaracSpeciales.cyb_mdc_t1_IV,150,200F,ListeMarchandises.cyb_mdc_t1_IV,Const.CV_AUTRE,16);
 public static final ComposantDeVaisseau cyb_mdc_t1_V   = new ComposantDeVaisseau("cyb_mdc_t1_",4,ListeParents.cyb_mdc_t1_V,12500,ListeCaracSpeciales.cyb_mdc_t1_V,240,1000F,ListeMarchandises.cyb_mdc_t1_V,Const.CV_AUTRE,20);
 public static final ComposantDeVaisseau cyb_mdc_t1_VI  = new ComposantDeVaisseau("cyb_mdc_t1_",5,ListeParents.cyb_mdc_t1_VI,25000,ListeCaracSpeciales.cyb_mdc_t1_VI,400,2000F,ListeMarchandises.cyb_mdc_t1_VI,Const.CV_AUTRE,30);
 public static final ComposantDeVaisseau cyb_mdc_t1_VII = new ComposantDeVaisseau("cyb_mdc_t1_",6,ListeParents.cyb_mdc_t1_VII,60000,ListeCaracSpeciales.cyb_mdc_t1_VII,700,5000F,ListeMarchandises.cyb_mdc_t1_VII,Const.CV_AUTRE,40);

 public static final ComposantDeVaisseau cyb_mdc_t2_I   = new ComposantDeVaisseau("cyb_mdc_t2_",0,ListeParents.cyb_mdc_t2_I,600,ListeCaracSpeciales.cyb_mdc_t2_I,20,50F,ListeMarchandises.cyb_mdc_t2_I,Const.CV_AUTRE, 5);
 public static final ComposantDeVaisseau cyb_mdc_t2_II  = new ComposantDeVaisseau("cyb_mdc_t2_",1,ListeParents.cyb_mdc_t2_II,800,ListeCaracSpeciales.cyb_mdc_t2_II,40,100F,ListeMarchandises.cyb_mdc_t2_II,Const.CV_AUTRE,10);
 public static final ComposantDeVaisseau cyb_mdc_t2_III = new ComposantDeVaisseau("cyb_mdc_t2_",2,ListeParents.cyb_mdc_t2_III,1250,ListeCaracSpeciales.cyb_mdc_t2_III,70,150F,ListeMarchandises.cyb_mdc_t2_III,Const.CV_AUTRE,15);
 public static final ComposantDeVaisseau cyb_mdc_t2_IV  = new ComposantDeVaisseau("cyb_mdc_t2_",3,ListeParents.cyb_mdc_t2_IV,5000,ListeCaracSpeciales.cyb_mdc_t2_IV,150,200F,ListeMarchandises.cyb_mdc_t2_IV,Const.CV_AUTRE, 20);
 public static final ComposantDeVaisseau cyb_mdc_t2_V   = new ComposantDeVaisseau("cyb_mdc_t2_",4,ListeParents.cyb_mdc_t2_V,12500,ListeCaracSpeciales.cyb_mdc_t2_V,240,1000F,ListeMarchandises.cyb_mdc_t2_V,Const.CV_AUTRE, 35);
 public static final ComposantDeVaisseau cyb_mdc_t2_VI  = new ComposantDeVaisseau("cyb_mdc_t2_",5,ListeParents.cyb_mdc_t2_VI,25000,ListeCaracSpeciales.cyb_mdc_t2_VI,400,2000F,ListeMarchandises.cyb_mdc_t2_VI,Const.CV_AUTRE, 60);

 public static final ComposantDeVaisseau cyb_mdc_t3_I   = new ComposantDeVaisseau("cyb_mdc_t3_",0,ListeParents.cyb_mdc_t3_I,600,ListeCaracSpeciales.cyb_mdc_t3_I,20,50F,ListeMarchandises.cyb_mdc_t3_I,Const.CV_AUTRE, 8);
 public static final ComposantDeVaisseau cyb_mdc_t3_II  = new ComposantDeVaisseau("cyb_mdc_t3_",1,ListeParents.cyb_mdc_t3_II,800,ListeCaracSpeciales.cyb_mdc_t3_II,40,100F,ListeMarchandises.cyb_mdc_t3_II,Const.CV_AUTRE,16);
 public static final ComposantDeVaisseau cyb_mdc_t3_III = new ComposantDeVaisseau("cyb_mdc_t3_",2,ListeParents.cyb_mdc_t3_III,1250,ListeCaracSpeciales.cyb_mdc_t3_III,70,150F,ListeMarchandises.cyb_mdc_t3_III,Const.CV_AUTRE, 24);
 public static final ComposantDeVaisseau cyb_mdc_t3_IV  = new ComposantDeVaisseau("cyb_mdc_t3_",3,ListeParents.cyb_mdc_t3_IV,5000,ListeCaracSpeciales.cyb_mdc_t3_IV,150,200F,ListeMarchandises.cyb_mdc_t3_IV,Const.CV_AUTRE, 32);
 public static final ComposantDeVaisseau cyb_mdc_t3_V   = new ComposantDeVaisseau("cyb_mdc_t3_",4,ListeParents.cyb_mdc_t3_V,12500,ListeCaracSpeciales.cyb_mdc_t3_V,240,1000F,ListeMarchandises.cyb_mdc_t3_V,Const.CV_AUTRE, 50);
 public static final ComposantDeVaisseau cyb_mdc_t3_VI  = new ComposantDeVaisseau("cyb_mdc_t3_",5,ListeParents.cyb_mdc_t3_VI,25000,ListeCaracSpeciales.cyb_mdc_t3_VI,400,2000F,ListeMarchandises.cyb_mdc_t3_VI,Const.CV_AUTRE, 90);


 public static final ComposantDeVaisseau cyb_vs_tt_I   = new ComposantDeVaisseau("cyb_vs_tt_",0,ListeParents.cyb_vs_tt_I,1000,ListeCaracSpeciales.cyb_vs_tt_I,5,10F,ListeMarchandises.cyb_vs_tt_I, Const.CV_AUTRE, 20);
 public static final ComposantDeVaisseau cyb_vs_tt_II  = new ComposantDeVaisseau("cyb_vs_tt_",1,ListeParents.cyb_vs_tt_II,2000,ListeCaracSpeciales.cyb_vs_tt_II,5,10F,ListeMarchandises.cyb_vs_tt_II, Const.CV_AUTRE, 30);
 public static final ComposantDeVaisseau cyb_vs_tt_III = new ComposantDeVaisseau("cyb_vs_tt_",2,ListeParents.cyb_vs_tt_III,4000,ListeCaracSpeciales.cyb_vs_tt_III,5,10F,ListeMarchandises.cyb_vs_tt_III, Const.CV_AUTRE, 50);
 public static final ComposantDeVaisseau cyb_vs_tt_IV  = new ComposantDeVaisseau("cyb_vs_tt_",3,ListeParents.cyb_vs_tt_IV,10000,ListeCaracSpeciales.cyb_vs_tt_IV,5,10F,ListeMarchandises.cyb_vs_tt_IV, Const.CV_AUTRE, 70);
 public static final ComposantDeVaisseau cyb_vs_tt_V   = new ComposantDeVaisseau("cyb_vs_tt_",4,ListeParents.cyb_vs_tt_V,20000,ListeCaracSpeciales.cyb_vs_tt_V,5,10F,ListeMarchandises.cyb_vs_tt_V, Const.CV_AUTRE, 100);


 public static final ComposantDeVaisseau cyb_vs_te_I   = new ComposantDeVaisseau("cyb_vs_te_",0,ListeParents.cyb_vs_te_I,1000,ListeCaracSpeciales.cyb_vs_te_I,5,10F,ListeMarchandises.cyb_vs_te_I, Const.CV_AUTRE, 20);
 public static final ComposantDeVaisseau cyb_vs_te_II  = new ComposantDeVaisseau("cyb_vs_te_",1,ListeParents.cyb_vs_te_II,2000,ListeCaracSpeciales.cyb_vs_te_II,5,10F,ListeMarchandises.cyb_vs_te_II, Const.CV_AUTRE, 30);
 public static final ComposantDeVaisseau cyb_vs_te_III = new ComposantDeVaisseau("cyb_vs_te_",2,ListeParents.cyb_vs_te_III,4000,ListeCaracSpeciales.cyb_vs_te_III,5,10F,ListeMarchandises.cyb_vs_te_III, Const.CV_AUTRE, 50);
 public static final ComposantDeVaisseau cyb_vs_te_IV  = new ComposantDeVaisseau("cyb_vs_te_",3,ListeParents.cyb_vs_te_IV,10000,ListeCaracSpeciales.cyb_vs_te_IV,5,10F,ListeMarchandises.cyb_vs_te_IV, Const.CV_AUTRE, 70);
 public static final ComposantDeVaisseau cyb_vs_te_V   = new ComposantDeVaisseau("cyb_vs_te_",4,ListeParents.cyb_vs_te_V,20000,ListeCaracSpeciales.cyb_vs_te_V,5,10F,ListeMarchandises.cyb_vs_te_V, Const.CV_AUTRE, 100);


 public static final ComposantDeVaisseau cyb_vs_tc_I   = new ComposantDeVaisseau("cyb_vs_tc_",0,ListeParents.cyb_vs_tc_I,1000,ListeCaracSpeciales.cyb_vs_tc_I,5,10F,ListeMarchandises.cyb_vs_tc_I, Const.CV_AUTRE, 20);
 public static final ComposantDeVaisseau cyb_vs_tc_II  = new ComposantDeVaisseau("cyb_vs_tc_",1,ListeParents.cyb_vs_tc_II,2000,ListeCaracSpeciales.cyb_vs_tc_II,5,10F,ListeMarchandises.cyb_vs_tc_II, Const.CV_AUTRE, 30);
 public static final ComposantDeVaisseau cyb_vs_tc_III = new ComposantDeVaisseau("cyb_vs_tc_",2,ListeParents.cyb_vs_tc_III,4000,ListeCaracSpeciales.cyb_vs_tc_III,5,10F,ListeMarchandises.cyb_vs_tc_III, Const.CV_AUTRE, 50);
 public static final ComposantDeVaisseau cyb_vs_tc_IV  = new ComposantDeVaisseau("cyb_vs_tc_",3,ListeParents.cyb_vs_tc_IV,10000,ListeCaracSpeciales.cyb_vs_tc_IV,5,10F,ListeMarchandises.cyb_vs_tc_IV, Const.CV_AUTRE, 70);
 public static final ComposantDeVaisseau cyb_vs_tc_V   = new ComposantDeVaisseau("cyb_vs_tc_",4,ListeParents.cyb_vs_tc_V,20000,ListeCaracSpeciales.cyb_vs_tc_V,5,10F,ListeMarchandises.cyb_vs_tc_V, Const.CV_AUTRE, 100);





 public static final Batiment mineI         = new Batiment("mine", 0, null,   25, ListeCaracSpeciales.mineI,   1, 10F, null, 20, 1, null);
    /**
       public static final Batiment mineII        = new Batiment("mine", 1, ListeParents.mineII,   200, ListeCaracSpeciales.mineII,  2, 20F, ListeMarchandises.mineII,  20, 2, null);
       public static final Batiment mineIII       = new Batiment("mine", 2, ListeParents.mineIII,  500, ListeCaracSpeciales.mineIII, 3, 30F, ListeMarchandises.mineIII, 20, 3, null);
       public static final Batiment mineIV        = new Batiment("mine", 3, ListeParents.mineIV,  1000, ListeCaracSpeciales.mineIV,  4, 40F, ListeMarchandises.mineIV,  20, 4, null);
       public static final Batiment mineV         = new Batiment("mine", 4, ListeParents.mineV,   2000, ListeCaracSpeciales.mineV,   5, 50F, ListeMarchandises.mineV,   20, 5, null);
    **/
 public static final Batiment chantierI   = new Batiment("chantier",0,null,50,ListeCaracSpeciales.chantierI,10,100F,ListeMarchandises.chantierI,200,10,null);
 public static final Batiment retraiteI   = new Batiment("retraite",0,ListeParents.retraiteI,400,ListeCaracSpeciales.retraiteI,10,50F,ListeMarchandises.retraiteI,50,10,null); 

 public static final Batiment optpI     = new Batiment("optp", 0, ListeParents.optpI   ,   200, ListeCaracSpeciales.optpI    , 10, 100F, ListeMarchandises.optpI    , 50, 5, null);
 public static final Batiment optpII    = new Batiment("optp", 1, ListeParents.optpII  ,   400, ListeCaracSpeciales.optpII   , 19, 190F, ListeMarchandises.optpII   , 50, 5, null);
 public static final Batiment optpIII   = new Batiment("optp", 2, ListeParents.optpIII ,   800, ListeCaracSpeciales.optpIII  , 27, 270F, ListeMarchandises.optpIII  , 50, 5, null);
 public static final Batiment optpIV    = new Batiment("optp", 3, ListeParents.optpIV  ,  1600, ListeCaracSpeciales.optpIV   , 34, 340F, ListeMarchandises.optpIV   , 50, 5, null);
 public static final Batiment optpV     = new Batiment("optp", 4, ListeParents.optpV   ,  3000, ListeCaracSpeciales.optpV    , 40, 400F, ListeMarchandises.optpV    , 50, 5, null);
 public static final Batiment optpVI    = new Batiment("optp", 5, ListeParents.optpVI  ,  5000, ListeCaracSpeciales.optpVI   , 45, 450F, ListeMarchandises.optpVI   , 50, 5, null);
 public static final Batiment optpVII   = new Batiment("optp", 6, ListeParents.optpVII , 10000, ListeCaracSpeciales.optpVII  , 49, 490F, ListeMarchandises.optpVII  , 50, 5, null);
 public static final Batiment optpVIII  = new Batiment("optp", 7, ListeParents.optpVIII, 17500, ListeCaracSpeciales.optpVIII , 52, 520F, ListeMarchandises.optpVIII , 50, 5, null);
 public static final Batiment optpIX    = new Batiment("optp", 8, ListeParents.optpIX  , 30000, ListeCaracSpeciales.optpIX   , 54, 540F, ListeMarchandises.optpIX   , 50, 5, null);
 public static final Batiment optpX     = new Batiment("optp", 9, ListeParents.optpX   , 60000, ListeCaracSpeciales.optpX    , 55, 550F, ListeMarchandises.optpX    , 50, 5, null);

 public static final Batiment optsI=new Batiment("opts",0,ListeParents.optsI,400,ListeCaracSpeciales.optsI,5,50F,ListeMarchandises.optsI,50,5,null);

 public static final Batiment repareI=new Batiment("repare"  ,0, ListeParents.repareI  ,   400, ListeCaracSpeciales.repareI  , 20,  200F, ListeMarchandises.repareI  ,  100, 20, null);
 public static final Batiment repareII=new Batiment("repare" ,1, ListeParents.repareII ,  1000, ListeCaracSpeciales.repareII , 30,  400F, ListeMarchandises.repareII ,  100, 30, null);
 public static final Batiment repareIII=new Batiment("repare",2, ListeParents.repareIII,  5000, ListeCaracSpeciales.repareIII, 40,  800F, ListeMarchandises.repareIII,  100, 40, null);
 public static final Batiment repareIV=new Batiment("repare" ,3, ListeParents.repareIV , 15000, ListeCaracSpeciales.repareIV , 50, 1600F, ListeMarchandises.repareIV ,  100, 50, null);
 public static final Batiment repareV=new Batiment("repare"  ,4, ListeParents.repareV  , 40000, ListeCaracSpeciales.repareV  , 60, 3200F, ListeMarchandises.repareV  ,  100, 60, null);

 public static final Batiment boucplaI=new Batiment("boucpla",0,null,100,ListeCaracSpeciales.boucplaI,10,100F,null,100,10,null);
 public static final Batiment boucplaII=new Batiment("boucpla",1,ListeParents.boucplaII,200,ListeCaracSpeciales.boucplaII,15,150F,ListeMarchandises.boucplaII,400,15,null);
 public static final Batiment boucplaIII=new Batiment("boucpla",2,ListeParents.boucplaIII,400,ListeCaracSpeciales.boucplaIII,20,200F,ListeMarchandises.boucplaIII,1350,20,null);
 public static final Batiment boucplaIV=new Batiment("boucpla",3,ListeParents.boucplaIV,800,ListeCaracSpeciales.boucplaIV,25,250F,ListeMarchandises.boucplaIV,3200,25,null);
 public static final Batiment boucplaV=new Batiment("boucpla",4,ListeParents.boucplaV,1600,ListeCaracSpeciales.boucplaV,30,300F,ListeMarchandises.boucplaV,6250,30,null);
 public static final Batiment boucplaVI=new Batiment("boucpla",5,ListeParents.boucplaVI,3000,ListeCaracSpeciales.boucplaVI,35,350F,ListeMarchandises.boucplaVI,10800,35,null);
 public static final Batiment boucplaVII=new Batiment("boucpla",6,ListeParents.boucplaVII,5000,ListeCaracSpeciales.boucplaVII,40,400F,ListeMarchandises.boucplaVII,17150,40,null);
 public static final Batiment boucplaVIII=new Batiment("boucpla",7,ListeParents.boucplaVIII,10000,ListeCaracSpeciales.boucplaVIII,45,450F,ListeMarchandises.boucplaVIII,25600,45,null);
 public static final Batiment boucplaIX=new Batiment("boucpla",8,ListeParents.boucplaIX,15000,ListeCaracSpeciales.boucplaIX,50,500F,ListeMarchandises.boucplaIX,36450,50,null);
 public static final Batiment boucplaX=new Batiment("boucpla",9,ListeParents.boucplaX,30000,ListeCaracSpeciales.boucplaX,55,550F,ListeMarchandises.boucplaX,50000,55,null);

 public static final Batiment radarI=new Batiment("radar",0,null,100,ListeCaracSpeciales.radarI,10,100F,null,100,10,null);
 public static final Batiment radarII=new Batiment("radar",1,ListeParents.radarII,200,ListeCaracSpeciales.radarII,15,150F,ListeMarchandises.radarII,150,15,null);
 public static final Batiment radarIII=new Batiment("radar",2,ListeParents.radarIII,400,ListeCaracSpeciales.radarIII,20,200F,ListeMarchandises.radarIII,200,20,null);
 public static final Batiment radarIV=new Batiment("radar",3,ListeParents.radarIV,800,ListeCaracSpeciales.radarIV,25,250F,ListeMarchandises.radarIV,350,25,null);
 public static final Batiment radarV=new Batiment("radar",4,ListeParents.radarV,1600,ListeCaracSpeciales.radarV,30,300F,ListeMarchandises.radarV,400,30,null);
 public static final Batiment radarVI=new Batiment("radar",5,ListeParents.radarVI,3000,ListeCaracSpeciales.radarVI,35,350F,ListeMarchandises.radarVI,450,35,null);
 public static final Batiment radarVII=new Batiment("radar",6,ListeParents.radarVII,5000,ListeCaracSpeciales.radarVII,40,400F,ListeMarchandises.radarVII,500,40,null);
 public static final Batiment radarVIII=new Batiment("radar",7,ListeParents.radarVIII,10000,ListeCaracSpeciales.radarVIII,45,450F,ListeMarchandises.radarVIII,550,45,null);
 public static final Batiment radarIX=new Batiment("radar",8,ListeParents.radarIX,15000,ListeCaracSpeciales.radarIX,50,500F,ListeMarchandises.radarIX,650,50,null);
 public static final Batiment radarX=new Batiment("radar",9,ListeParents.radarX,30000,ListeCaracSpeciales.radarX,55,550F,ListeMarchandises.radarX,700,55,null);

 public static final Batiment extraI  = new Batiment("extra",0,ListeParents.extraI,500,ListeCaracSpeciales.extraI,2,50F,ListeMarchandises.extraI,200,10,null);
 public static final Batiment extraII = new Batiment("extra",1,ListeParents.extraII,1000,ListeCaracSpeciales.extraII,4,100F,ListeMarchandises.extraII,200,15,null);
 public static final Batiment extraIII= new Batiment("extra",2,ListeParents.extraIII,10000,ListeCaracSpeciales.extraIII,8,150F,ListeMarchandises.extraIII,200,20,null);
 public static final Batiment extraIV = new Batiment("extra",3,ListeParents.extraIV,20000,ListeCaracSpeciales.extraIV,15,200F,ListeMarchandises.extraIV,200,25,null);
 public static final Batiment extraV  = new Batiment("extra",4,ListeParents.extraV,50000,ListeCaracSpeciales.extraV,30,250F,ListeMarchandises.extraV,200,30,null);

    // Usines
public static final Batiment agroI=new Batiment("agro",0,ListeParents.agroI,100,ListeCaracSpeciales.agroI,10,100F,ListeMarchandises.agroI,100,5,null);
public static final Batiment agroII=new Batiment("agro",1,ListeParents.agroII,200,ListeCaracSpeciales.agroII,19,190F,ListeMarchandises.agroII,100,10,null);
public static final Batiment agroIII=new Batiment("agro",2,ListeParents.agroIII,500,ListeCaracSpeciales.agroIII,27,270F,ListeMarchandises.agroIII,100,14,null);
public static final Batiment agroIV=new Batiment("agro",3,ListeParents.agroIV,1000,ListeCaracSpeciales.agroIV,34,340F,ListeMarchandises.agroIV,100,17,null);
public static final Batiment agroV=new Batiment("agro",4,ListeParents.agroV,1700,ListeCaracSpeciales.agroV,40,400F,ListeMarchandises.agroV,100,20,null);
public static final Batiment agroVI=new Batiment("agro",5,ListeParents.agroVI,2600,ListeCaracSpeciales.agroVI,45,450F,ListeMarchandises.agroVI,100,23,null);
public static final Batiment agroVII=new Batiment("agro",6,ListeParents.agroVII,3700,ListeCaracSpeciales.agroVII,49,490F,ListeMarchandises.agroVII,100,25,null);
public static final Batiment agroVIII=new Batiment("agro",7,ListeParents.agroVIII,5000,ListeCaracSpeciales.agroVIII,52,520F,ListeMarchandises.agroVIII,100,26,null);
public static final Batiment agroIX=new Batiment("agro",8,ListeParents.agroIX,6500,ListeCaracSpeciales.agroIX,54,540F,ListeMarchandises.agroIX,100,27,null);
public static final Batiment agroX=new Batiment("agro",9,ListeParents.agroX,8200,ListeCaracSpeciales.agroX,55,550F,ListeMarchandises.agroX,100,28,null);

public static final Batiment agricoleI=new Batiment("agricole",0,ListeParents.agricoleI,100,ListeCaracSpeciales.agricoleI,10,100F,ListeMarchandises.agricoleI,100,5,null);
public static final Batiment agricoleII=new Batiment("agricole",1,ListeParents.agricoleII,200,ListeCaracSpeciales.agricoleII,19,190F,ListeMarchandises.agricoleII,100,10,null);
public static final Batiment agricoleIII=new Batiment("agricole",2,ListeParents.agricoleIII,500,ListeCaracSpeciales.agricoleIII,27,270F,ListeMarchandises.agricoleIII,100,14,null);
public static final Batiment agricoleIV=new Batiment("agricole",3,ListeParents.agricoleIV,1000,ListeCaracSpeciales.agricoleIV,34,340F,ListeMarchandises.agricoleIV,100,17,null);
public static final Batiment agricoleV=new Batiment("agricole",4,ListeParents.agricoleV,1700,ListeCaracSpeciales.agricoleV,40,400F,ListeMarchandises.agricoleV,100,20,null);
public static final Batiment agricoleVI=new Batiment("agricole",5,ListeParents.agricoleVI,2600,ListeCaracSpeciales.agricoleVI,45,450F,ListeMarchandises.agricoleVI,100,23,null);
public static final Batiment agricoleVII=new Batiment("agricole",6,ListeParents.agricoleVII,3700,ListeCaracSpeciales.agricoleVII,49,490F,ListeMarchandises.agricoleVII,100,25,null);
public static final Batiment agricoleVIII=new Batiment("agricole",7,ListeParents.agricoleVIII,5000,ListeCaracSpeciales.agricoleVIII,52,520F,ListeMarchandises.agricoleVIII,100,26,null);
public static final Batiment agricoleIX=new Batiment("agricole",8,ListeParents.agricoleIX,6500,ListeCaracSpeciales.agricoleIX,54,540F,ListeMarchandises.agricoleIX,100,27,null);
public static final Batiment agricoleX=new Batiment("agricole",9,ListeParents.agricoleX,8200,ListeCaracSpeciales.agricoleX,55,550F,ListeMarchandises.agricoleX,100,28,null);

public static final Batiment modeI=new Batiment("mode",0,ListeParents.modeI,100,ListeCaracSpeciales.modeI,10,100F,ListeMarchandises.modeI,100,5,null);
public static final Batiment modeII=new Batiment("mode",1,ListeParents.modeII,200,ListeCaracSpeciales.modeII,19,190F,ListeMarchandises.modeII,100,10,null);
public static final Batiment modeIII=new Batiment("mode",2,ListeParents.modeIII,500,ListeCaracSpeciales.modeIII,27,270F,ListeMarchandises.modeIII,100,14,null);
public static final Batiment modeIV=new Batiment("mode",3,ListeParents.modeIV,1000,ListeCaracSpeciales.modeIV,34,340F,ListeMarchandises.modeIV,100,17,null);
public static final Batiment modeV=new Batiment("mode",4,ListeParents.modeV,1700,ListeCaracSpeciales.modeV,40,400F,ListeMarchandises.modeV,100,20,null);
public static final Batiment modeVI=new Batiment("mode",5,ListeParents.modeVI,2600,ListeCaracSpeciales.modeVI,45,450F,ListeMarchandises.modeVI,100,23,null);
public static final Batiment modeVII=new Batiment("mode",6,ListeParents.modeVII,3700,ListeCaracSpeciales.modeVII,49,490F,ListeMarchandises.modeVII,100,25,null);
public static final Batiment modeVIII=new Batiment("mode",7,ListeParents.modeVIII,5000,ListeCaracSpeciales.modeVIII,52,520F,ListeMarchandises.modeVIII,100,26,null);
public static final Batiment modeIX=new Batiment("mode",8,ListeParents.modeIX,6500,ListeCaracSpeciales.modeIX,54,540F,ListeMarchandises.modeIX,100,27,null);
public static final Batiment modeX=new Batiment("mode",9,ListeParents.modeX,8200,ListeCaracSpeciales.modeX,55,550F,ListeMarchandises.modeX,100,28,null);

public static final Batiment culturelI=new Batiment("culturel",0,ListeParents.culturelI,100,ListeCaracSpeciales.culturelI,10,100F,ListeMarchandises.culturelI,100,5,null);
public static final Batiment culturelII=new Batiment("culturel",1,ListeParents.culturelII,200,ListeCaracSpeciales.culturelII,19,190F,ListeMarchandises.culturelII,100,10,null);
public static final Batiment culturelIII=new Batiment("culturel",2,ListeParents.culturelIII,500,ListeCaracSpeciales.culturelIII,27,270F,ListeMarchandises.culturelIII,100,14,null);
public static final Batiment culturelIV=new Batiment("culturel",3,ListeParents.culturelIV,1000,ListeCaracSpeciales.culturelIV,34,340F,ListeMarchandises.culturelIV,100,17,null);
public static final Batiment culturelV=new Batiment("culturel",4,ListeParents.culturelV,1700,ListeCaracSpeciales.culturelV,40,400F,ListeMarchandises.culturelV,100,20,null);
public static final Batiment culturelVI=new Batiment("culturel",5,ListeParents.culturelVI,2600,ListeCaracSpeciales.culturelVI,45,450F,ListeMarchandises.culturelVI,100,23,null);
public static final Batiment culturelVII=new Batiment("culturel",6,ListeParents.culturelVII,3700,ListeCaracSpeciales.culturelVII,49,490F,ListeMarchandises.culturelVII,100,25,null);
public static final Batiment culturelVIII=new Batiment("culturel",7,ListeParents.culturelVIII,5000,ListeCaracSpeciales.culturelVIII,52,520F,ListeMarchandises.culturelVIII,100,26,null);
public static final Batiment culturelIX=new Batiment("culturel",8,ListeParents.culturelIX,6500,ListeCaracSpeciales.culturelIX,54,540F,ListeMarchandises.culturelIX,100,27,null);
public static final Batiment culturelX=new Batiment("culturel",9,ListeParents.culturelX,8200,ListeCaracSpeciales.culturelX,55,550F,ListeMarchandises.culturelX,100,28,null);

public static final Batiment transfoI=new Batiment("transfo",0,ListeParents.transfoI,100,ListeCaracSpeciales.transfoI,10,100F,ListeMarchandises.transfoI,100,5,null);
public static final Batiment transfoII=new Batiment("transfo",1,ListeParents.transfoII,200,ListeCaracSpeciales.transfoII,19,190F,ListeMarchandises.transfoII,100,10,null);
public static final Batiment transfoIII=new Batiment("transfo",2,ListeParents.transfoIII,500,ListeCaracSpeciales.transfoIII,27,270F,ListeMarchandises.transfoIII,100,14,null);
public static final Batiment transfoIV=new Batiment("transfo",3,ListeParents.transfoIV,1000,ListeCaracSpeciales.transfoIV,34,340F,ListeMarchandises.transfoIV,100,17,null);
public static final Batiment transfoV=new Batiment("transfo",4,ListeParents.transfoV,1700,ListeCaracSpeciales.transfoV,40,400F,ListeMarchandises.transfoV,100,20,null);
public static final Batiment transfoVI=new Batiment("transfo",5,ListeParents.transfoVI,2600,ListeCaracSpeciales.transfoVI,45,450F,ListeMarchandises.transfoVI,100,23,null);
public static final Batiment transfoVII=new Batiment("transfo",6,ListeParents.transfoVII,3700,ListeCaracSpeciales.transfoVII,49,490F,ListeMarchandises.transfoVII,100,25,null);
public static final Batiment transfoVIII=new Batiment("transfo",7,ListeParents.transfoVIII,5000,ListeCaracSpeciales.transfoVIII,52,520F,ListeMarchandises.transfoVIII,100,26,null);
public static final Batiment transfoIX=new Batiment("transfo",8,ListeParents.transfoIX,6500,ListeCaracSpeciales.transfoIX,54,540F,ListeMarchandises.transfoIX,100,27,null);
public static final Batiment transfoX=new Batiment("transfo",9,ListeParents.transfoX,8200,ListeCaracSpeciales.transfoX,55,550F,ListeMarchandises.transfoX,100,28,null);

public static final Batiment pharmaI=new Batiment("pharma",0,ListeParents.pharmaI,100,ListeCaracSpeciales.pharmaI,10,100F,ListeMarchandises.pharmaI,100,5,null);
public static final Batiment pharmaII=new Batiment("pharma",1,ListeParents.pharmaII,200,ListeCaracSpeciales.pharmaII,19,190F,ListeMarchandises.pharmaII,100,10,null);
public static final Batiment pharmaIII=new Batiment("pharma",2,ListeParents.pharmaIII,500,ListeCaracSpeciales.pharmaIII,27,270F,ListeMarchandises.pharmaIII,100,14,null);
public static final Batiment pharmaIV=new Batiment("pharma",3,ListeParents.pharmaIV,1000,ListeCaracSpeciales.pharmaIV,34,340F,ListeMarchandises.pharmaIV,100,17,null);
public static final Batiment pharmaV=new Batiment("pharma",4,ListeParents.pharmaV,1700,ListeCaracSpeciales.pharmaV,40,400F,ListeMarchandises.pharmaV,100,20,null);
public static final Batiment pharmaVI=new Batiment("pharma",5,ListeParents.pharmaVI,2600,ListeCaracSpeciales.pharmaVI,45,450F,ListeMarchandises.pharmaVI,100,23,null);
public static final Batiment pharmaVII=new Batiment("pharma",6,ListeParents.pharmaVII,3700,ListeCaracSpeciales.pharmaVII,49,490F,ListeMarchandises.pharmaVII,100,25,null);
public static final Batiment pharmaVIII=new Batiment("pharma",7,ListeParents.pharmaVIII,5000,ListeCaracSpeciales.pharmaVIII,52,520F,ListeMarchandises.pharmaVIII,100,26,null);
public static final Batiment pharmaIX=new Batiment("pharma",8,ListeParents.pharmaIX,6500,ListeCaracSpeciales.pharmaIX,54,540F,ListeMarchandises.pharmaIX,100,27,null);
public static final Batiment pharmaX=new Batiment("pharma",9,ListeParents.pharmaX,8200,ListeCaracSpeciales.pharmaX,55,550F,ListeMarchandises.pharmaX,100,28,null);

public static final Batiment infoI=new Batiment("info",0,ListeParents.infoI,100,ListeCaracSpeciales.infoI,10,100F,ListeMarchandises.infoI,100,5,null);
public static final Batiment infoII=new Batiment("info",1,ListeParents.infoII,200,ListeCaracSpeciales.infoII,19,190F,ListeMarchandises.infoII,100,10,null);
public static final Batiment infoIII=new Batiment("info",2,ListeParents.infoIII,500,ListeCaracSpeciales.infoIII,27,270F,ListeMarchandises.infoIII,100,14,null);
public static final Batiment infoIV=new Batiment("info",3,ListeParents.infoIV,1000,ListeCaracSpeciales.infoIV,34,340F,ListeMarchandises.infoIV,100,17,null);
public static final Batiment infoV=new Batiment("info",4,ListeParents.infoV,1700,ListeCaracSpeciales.infoV,40,400F,ListeMarchandises.infoV,100,20,null);
public static final Batiment infoVI=new Batiment("info",5,ListeParents.infoVI,2600,ListeCaracSpeciales.infoVI,45,450F,ListeMarchandises.infoVI,100,23,null);
public static final Batiment infoVII=new Batiment("info",6,ListeParents.infoVII,3700,ListeCaracSpeciales.infoVII,49,490F,ListeMarchandises.infoVII,100,25,null);
public static final Batiment infoVIII=new Batiment("info",7,ListeParents.infoVIII,5000,ListeCaracSpeciales.infoVIII,52,520F,ListeMarchandises.infoVIII,100,26,null);
public static final Batiment infoIX=new Batiment("info",8,ListeParents.infoIX,6500,ListeCaracSpeciales.infoIX,54,540F,ListeMarchandises.infoIX,100,27,null);
public static final Batiment infoX=new Batiment("info",9,ListeParents.infoX,8200,ListeCaracSpeciales.infoX,55,550F,ListeMarchandises.infoX,100,28,null);

public static final Batiment robotI=new Batiment("robot",0,ListeParents.robotI,100,ListeCaracSpeciales.robotI,10,100F,ListeMarchandises.robotI,100,5,null);
public static final Batiment robotII=new Batiment("robot",1,ListeParents.robotII,200,ListeCaracSpeciales.robotII,19,190F,ListeMarchandises.robotII,100,10,null);
public static final Batiment robotIII=new Batiment("robot",2,ListeParents.robotIII,500,ListeCaracSpeciales.robotIII,27,270F,ListeMarchandises.robotIII,100,14,null);
public static final Batiment robotIV=new Batiment("robot",3,ListeParents.robotIV,1000,ListeCaracSpeciales.robotIV,34,340F,ListeMarchandises.robotIV,100,17,null);
public static final Batiment robotV=new Batiment("robot",4,ListeParents.robotV,1700,ListeCaracSpeciales.robotV,40,400F,ListeMarchandises.robotV,100,20,null);
public static final Batiment robotVI=new Batiment("robot",5,ListeParents.robotVI,2600,ListeCaracSpeciales.robotVI,45,450F,ListeMarchandises.robotVI,100,23,null);
public static final Batiment robotVII=new Batiment("robot",6,ListeParents.robotVII,3700,ListeCaracSpeciales.robotVII,49,490F,ListeMarchandises.robotVII,100,25,null);
public static final Batiment robotVIII=new Batiment("robot",7,ListeParents.robotVIII,5000,ListeCaracSpeciales.robotVIII,52,520F,ListeMarchandises.robotVIII,100,26,null);
public static final Batiment robotIX=new Batiment("robot",8,ListeParents.robotIX,6500,ListeCaracSpeciales.robotIX,54,540F,ListeMarchandises.robotIX,100,27,null);
public static final Batiment robotX=new Batiment("robot",9,ListeParents.robotX,8200,ListeCaracSpeciales.robotX,55,550F,ListeMarchandises.robotX,100,28,null);

public static final Batiment technoI=new Batiment("techno",0,ListeParents.technoI,100,ListeCaracSpeciales.technoI,10,100F,ListeMarchandises.technoI,100,5,null);
public static final Batiment technoII=new Batiment("techno",1,ListeParents.technoII,200,ListeCaracSpeciales.technoII,19,190F,ListeMarchandises.technoII,100,10,null);
public static final Batiment technoIII=new Batiment("techno",2,ListeParents.technoIII,500,ListeCaracSpeciales.technoIII,27,270F,ListeMarchandises.technoIII,100,14,null);
public static final Batiment technoIV=new Batiment("techno",3,ListeParents.technoIV,1000,ListeCaracSpeciales.technoIV,34,340F,ListeMarchandises.technoIV,100,17,null);
public static final Batiment technoV=new Batiment("techno",4,ListeParents.technoV,1700,ListeCaracSpeciales.technoV,40,400F,ListeMarchandises.technoV,100,20,null);
public static final Batiment technoVI=new Batiment("techno",5,ListeParents.technoVI,2600,ListeCaracSpeciales.technoVI,45,450F,ListeMarchandises.technoVI,100,23,null);
public static final Batiment technoVII=new Batiment("techno",6,ListeParents.technoVII,3700,ListeCaracSpeciales.technoVII,49,490F,ListeMarchandises.technoVII,100,25,null);
public static final Batiment technoVIII=new Batiment("techno",7,ListeParents.technoVIII,5000,ListeCaracSpeciales.technoVIII,52,520F,ListeMarchandises.technoVIII,100,26,null);
public static final Batiment technoIX=new Batiment("techno",8,ListeParents.technoIX,6500,ListeCaracSpeciales.technoIX,54,540F,ListeMarchandises.technoIX,100,27,null);
public static final Batiment technoX=new Batiment("techno",9,ListeParents.technoX,8200,ListeCaracSpeciales.technoX,55,550F,ListeMarchandises.technoX,100,28,null);

public static final Batiment armeI=new Batiment("arme",0,ListeParents.armeI,100,ListeCaracSpeciales.armeI,10,100F,ListeMarchandises.armeI,100,5,null);
public static final Batiment armeII=new Batiment("arme",1,ListeParents.armeII,200,ListeCaracSpeciales.armeII,19,190F,ListeMarchandises.armeII,100,10,null);
public static final Batiment armeIII=new Batiment("arme",2,ListeParents.armeIII,500,ListeCaracSpeciales.armeIII,27,270F,ListeMarchandises.armeIII,100,14,null);
public static final Batiment armeIV=new Batiment("arme",3,ListeParents.armeIV,1000,ListeCaracSpeciales.armeIV,34,340F,ListeMarchandises.armeIV,100,17,null);
public static final Batiment armeV=new Batiment("arme",4,ListeParents.armeV,1700,ListeCaracSpeciales.armeV,40,400F,ListeMarchandises.armeV,100,20,null);
public static final Batiment armeVI=new Batiment("arme",5,ListeParents.armeVI,2600,ListeCaracSpeciales.armeVI,45,450F,ListeMarchandises.armeVI,100,23,null);
public static final Batiment armeVII=new Batiment("arme",6,ListeParents.armeVII,3700,ListeCaracSpeciales.armeVII,49,490F,ListeMarchandises.armeVII,100,25,null);
public static final Batiment armeVIII=new Batiment("arme",7,ListeParents.armeVIII,5000,ListeCaracSpeciales.armeVIII,52,520F,ListeMarchandises.armeVIII,100,26,null);
public static final Batiment armeIX=new Batiment("arme",8,ListeParents.armeIX,6500,ListeCaracSpeciales.armeIX,54,540F,ListeMarchandises.armeIX,100,27,null);
public static final Batiment armeX=new Batiment("arme",9,ListeParents.armeX,8200,ListeCaracSpeciales.armeX,55,550F,ListeMarchandises.armeX,100,28,null);

public static final Batiment raffineI=new Batiment("raffine",0,ListeParents.raffineI,100,ListeCaracSpeciales.raffineI,10,100F,ListeMarchandises.raffineI,100,5,null);
public static final Batiment raffineII=new Batiment("raffine",1,ListeParents.raffineII,200,ListeCaracSpeciales.raffineII,19,190F,ListeMarchandises.raffineII,100,10,null);
public static final Batiment raffineIII=new Batiment("raffine",2,ListeParents.raffineIII,500,ListeCaracSpeciales.raffineIII,27,270F,ListeMarchandises.raffineIII,100,14,null);
public static final Batiment raffineIV=new Batiment("raffine",3,ListeParents.raffineIV,1000,ListeCaracSpeciales.raffineIV,34,340F,ListeMarchandises.raffineIV,100,17,null);
public static final Batiment raffineV=new Batiment("raffine",4,ListeParents.raffineV,1700,ListeCaracSpeciales.raffineV,40,400F,ListeMarchandises.raffineV,100,20,null);
public static final Batiment raffineVI=new Batiment("raffine",5,ListeParents.raffineVI,2600,ListeCaracSpeciales.raffineVI,45,450F,ListeMarchandises.raffineVI,100,23,null);
public static final Batiment raffineVII=new Batiment("raffine",6,ListeParents.raffineVII,3700,ListeCaracSpeciales.raffineVII,49,490F,ListeMarchandises.raffineVII,100,25,null);
public static final Batiment raffineVIII=new Batiment("raffine",7,ListeParents.raffineVIII,5000,ListeCaracSpeciales.raffineVIII,52,520F,ListeMarchandises.raffineVIII,100,26,null);
public static final Batiment raffineIX=new Batiment("raffine",8,ListeParents.raffineIX,6500,ListeCaracSpeciales.raffineIX,54,540F,ListeMarchandises.raffineIX,100,27,null);
public static final Batiment raffineX=new Batiment("raffine",9,ListeParents.raffineX,8200,ListeCaracSpeciales.raffineX,55,550F,ListeMarchandises.raffineX,100,28,null);

public static final Batiment lourdeI=new Batiment("lourde",0,ListeParents.lourdeI,100,ListeCaracSpeciales.lourdeI,10,100F,ListeMarchandises.lourdeI,100,5,null);
public static final Batiment lourdeII=new Batiment("lourde",1,ListeParents.lourdeII,200,ListeCaracSpeciales.lourdeII,19,190F,ListeMarchandises.lourdeII,100,10,null);
public static final Batiment lourdeIII=new Batiment("lourde",2,ListeParents.lourdeIII,500,ListeCaracSpeciales.lourdeIII,27,270F,ListeMarchandises.lourdeIII,100,14,null);
public static final Batiment lourdeIV=new Batiment("lourde",3,ListeParents.lourdeIV,1000,ListeCaracSpeciales.lourdeIV,34,340F,ListeMarchandises.lourdeIV,100,17,null);
public static final Batiment lourdeV=new Batiment("lourde",4,ListeParents.lourdeV,1700,ListeCaracSpeciales.lourdeV,40,400F,ListeMarchandises.lourdeV,100,20,null);
public static final Batiment lourdeVI=new Batiment("lourde",5,ListeParents.lourdeVI,2600,ListeCaracSpeciales.lourdeVI,45,450F,ListeMarchandises.lourdeVI,100,23,null);
public static final Batiment lourdeVII=new Batiment("lourde",6,ListeParents.lourdeVII,3700,ListeCaracSpeciales.lourdeVII,49,490F,ListeMarchandises.lourdeVII,100,25,null);
public static final Batiment lourdeVIII=new Batiment("lourde",7,ListeParents.lourdeVIII,5000,ListeCaracSpeciales.lourdeVIII,52,520F,ListeMarchandises.lourdeVIII,100,26,null);
public static final Batiment lourdeIX=new Batiment("lourde",8,ListeParents.lourdeIX,6500,ListeCaracSpeciales.lourdeIX,54,540F,ListeMarchandises.lourdeIX,100,27,null);
public static final Batiment lourdeX=new Batiment("lourde",9,ListeParents.lourdeX,8200,ListeCaracSpeciales.lourdeX,55,550F,ListeMarchandises.lourdeX,100,28,null);

public static final Batiment metauxI=new Batiment("metaux",0,ListeParents.metauxI,100,ListeCaracSpeciales.metauxI,10,100F,ListeMarchandises.metauxI,100,5,null);
public static final Batiment metauxII=new Batiment("metaux",1,ListeParents.metauxII,200,ListeCaracSpeciales.metauxII,19,190F,ListeMarchandises.metauxII,100,10,null);
public static final Batiment metauxIII=new Batiment("metaux",2,ListeParents.metauxIII,500,ListeCaracSpeciales.metauxIII,27,270F,ListeMarchandises.metauxIII,100,14,null);
public static final Batiment metauxIV=new Batiment("metaux",3,ListeParents.metauxIV,1000,ListeCaracSpeciales.metauxIV,34,340F,ListeMarchandises.metauxIV,100,17,null);
public static final Batiment metauxV=new Batiment("metaux",4,ListeParents.metauxV,1700,ListeCaracSpeciales.metauxV,40,400F,ListeMarchandises.metauxV,100,20,null);
public static final Batiment metauxVI=new Batiment("metaux",5,ListeParents.metauxVI,2600,ListeCaracSpeciales.metauxVI,45,450F,ListeMarchandises.metauxVI,100,23,null);
public static final Batiment metauxVII=new Batiment("metaux",6,ListeParents.metauxVII,3700,ListeCaracSpeciales.metauxVII,49,490F,ListeMarchandises.metauxVII,100,25,null);
public static final Batiment metauxVIII=new Batiment("metaux",7,ListeParents.metauxVIII,5000,ListeCaracSpeciales.metauxVIII,52,520F,ListeMarchandises.metauxVIII,100,26,null);
public static final Batiment metauxIX=new Batiment("metaux",8,ListeParents.metauxIX,6500,ListeCaracSpeciales.metauxIX,54,540F,ListeMarchandises.metauxIX,100,27,null);
public static final Batiment metauxX=new Batiment("metaux",9,ListeParents.metauxX,8200,ListeCaracSpeciales.metauxX,55,550F,ListeMarchandises.metauxX,100,28,null);



    // Fin usines

public static final Batiment poly_constI   = new Batiment("poly_const", 0, ListeParents.poly_constI,  100, ListeCaracSpeciales.poly_constI,    10,  500F, ListeMarchandises.poly_constI,   300, 25, null);
public static final Batiment poly_constII  = new Batiment("poly_const", 1, ListeParents.poly_constII, 100, ListeCaracSpeciales.poly_constII,   10,  900F, ListeMarchandises.poly_constII,  300, 25, null);
public static final Batiment poly_constIII = new Batiment("poly_const", 2, ListeParents.poly_constIII, 100, ListeCaracSpeciales.poly_constIII, 10, 1700F, ListeMarchandises.poly_constIII, 300, 25, null);
public static final Batiment poly_constIV  = new Batiment("poly_const", 3, ListeParents.poly_constIV, 100, ListeCaracSpeciales.poly_constIV,   10, 3200F, ListeMarchandises.poly_constIV,  300, 25, null);
public static final Batiment poly_constV   = new Batiment("poly_const", 4, ListeParents.poly_constV,  100, ListeCaracSpeciales.poly_constV,    10, 6000F, ListeMarchandises.poly_constV,   300, 25, null);

public static final Batiment poly_popI   = new Batiment("poly_pop", 0, ListeParents.poly_popI,  100, ListeCaracSpeciales.poly_popI,    10,  500F, ListeMarchandises.poly_popI,   300, 25, null);
public static final Batiment poly_popII  = new Batiment("poly_pop", 1, ListeParents.poly_popII, 100, ListeCaracSpeciales.poly_popII,   10,  900F, ListeMarchandises.poly_popII,  300, 25, null);
public static final Batiment poly_popIII = new Batiment("poly_pop", 2, ListeParents.poly_popIII, 100, ListeCaracSpeciales.poly_popIII, 10, 1700F, ListeMarchandises.poly_popIII, 300, 25, null);
public static final Batiment poly_popIV  = new Batiment("poly_pop", 3, ListeParents.poly_popIV, 100, ListeCaracSpeciales.poly_popIV,   10, 3200F, ListeMarchandises.poly_popIV,  300, 25, null);
public static final Batiment poly_popV   = new Batiment("poly_pop", 4, ListeParents.poly_popV,  100, ListeCaracSpeciales.poly_popV,    10, 6000F, ListeMarchandises.poly_popV,   300, 25, null);

    // Techno zwaias
    


    // Debut batiments

 public static final Batiment battlaI=new Batiment("battla",0,null,100,null,5,80F,null,25,5,"laser");
 public static final Batiment battlaII=new Batiment("battla",1,ListeParents.battlaII,200,null,5,150F,ListeMarchandises.battlaII,50,10,"laser");
 public static final Batiment battlaIII=new Batiment("battla",2,ListeParents.battlaIII,400,null,10,200F,ListeMarchandises.battlaIII,100,10,"laser");
 public static final Batiment battlaIV=new Batiment("battla",3,ListeParents.battlaIV,800,null,10,250F,ListeMarchandises.battlaIV,150,10,"laser");
 public static final Batiment battlaV=new Batiment("battla",4,ListeParents.battlaV,1600,null,15,300F,ListeMarchandises.battlaV,200,15,"laser");
 public static final Batiment battlaVI=new Batiment("battla",5,ListeParents.battlaVI,3000,null,15,350F,ListeMarchandises.battlaVI,250,15,"laser");
 public static final Batiment battlaVII=new Batiment("battla",6,ListeParents.battlaVII,5000,null,20,400F,ListeMarchandises.battlaVII,300,20,"laser");
 public static final Batiment battlaVIII=new Batiment("battla",7,ListeParents.battlaVIII,10000,null,20,450F,ListeMarchandises.battlaVIII,350,20,"laser");
 public static final Batiment battlaIX=new Batiment("battla",8,ListeParents.battlaIX,15000,null,25,500F,ListeMarchandises.battlaIX,400,25,"laser");
 public static final Batiment battlaX=new Batiment("battla",9,ListeParents.battlaX,30000,null,30,550F,ListeMarchandises.battlaX,450,30,"laser");

 public static final Batiment battplaI=new Batiment("battpla",0,ListeParents.battplaI,100,null,5,80F,null,25,5,"plasma");
 public static final Batiment battplaII=new Batiment("battpla",1,ListeParents.battplaII,200,null,5,150F,ListeMarchandises.battplaII,50,10,"plasma");
 public static final Batiment battplaIII=new Batiment("battpla",2,ListeParents.battplaIII,400,null,10,200F,ListeMarchandises.battplaIII,100,10,"plasma");
 public static final Batiment battplaIV=new Batiment("battpla",3,ListeParents.battplaIV,800,null,10,250F,ListeMarchandises.battplaIV,150,10,"plasma");
 public static final Batiment battplaV=new Batiment("battpla",4,ListeParents.battplaV,1600,null,15,300F,ListeMarchandises.battplaV,200,15,"plasma");
 public static final Batiment battplaVI=new Batiment("battpla",5,ListeParents.battplaVI,3000,null,15,350F,ListeMarchandises.battplaVI,250,15,"plasma");
 public static final Batiment battplaVII=new Batiment("battpla",6,ListeParents.battplaVII,5000,null,20,400F,ListeMarchandises.battplaVII,300,20,"plasma");
 public static final Batiment battplaVIII=new Batiment("battpla",7,ListeParents.battplaVIII,10000,null,20,450F,ListeMarchandises.battplaVIII,350,20,"plasma");
 public static final Batiment battplaIX=new Batiment("battpla",8,ListeParents.battplaIX,15000,null,25,500F,ListeMarchandises.battplaIX,400,25,"plasma");
 public static final Batiment battplaX=new Batiment("battpla",9,ListeParents.battplaX,30000,null,30,550F,ListeMarchandises.battplaX,500,30,"plasma");

 public static final Batiment rampemisI=new Batiment("rampemis",0,ListeParents.rampemisI,100,null,5,80F,null,25,5,"miss");
 public static final Batiment rampemisII=new Batiment("rampemis",1,ListeParents.rampemisII,200,null,5,150F,ListeMarchandises.rampemisII,50,10,"miss");
 public static final Batiment rampemisIII=new Batiment("rampemis",2,ListeParents.rampemisIII,400,null,10,200F,ListeMarchandises.rampemisIII,100,10,"miss");
 public static final Batiment rampemisIV=new Batiment("rampemis",3,ListeParents.rampemisIV,800,null,10,250F,ListeMarchandises.rampemisIV,150,10,"miss");
 public static final Batiment rampemisV=new Batiment("rampemis",4,ListeParents.rampemisV,1600,null,15,300F,ListeMarchandises.rampemisV,200,15,"miss");
 public static final Batiment rampemisVI=new Batiment("rampemis",5,ListeParents.rampemisVI,3000,null,15,350F,ListeMarchandises.rampemisVI,250,15,"miss");
 public static final Batiment rampemisVII=new Batiment("rampemis",6,ListeParents.rampemisVII,5000,null,20,400F,ListeMarchandises.rampemisVII,300,20,"miss");
 public static final Batiment rampemisVIII=new Batiment("rampemis",7,ListeParents.rampemisVIII,10000,null,20,450F,ListeMarchandises.rampemisVIII,350,20,"miss");
 public static final Batiment rampemisIX=new Batiment("rampemis",8,ListeParents.rampemisIX,15000,null,25,500F,ListeMarchandises.rampemisIX,400,25,"miss");
 public static final Batiment rampemisX=new Batiment("rampemis",9,ListeParents.rampemisX,30000,null,30,550F,ListeMarchandises.rampemisX,500,30,"miss");

 public static final Batiment lancetorI=new Batiment("lancetor",0,ListeParents.lancetorI,100,null,5,80F,null,25,5,"torp");
 public static final Batiment lancetorII=new Batiment("lancetor",1,ListeParents.lancetorII,200,null,5,150F,ListeMarchandises.lancetorII,50,5,"torp");
 public static final Batiment lancetorIII=new Batiment("lancetor",2,ListeParents.lancetorIII,400,null,10,200F,ListeMarchandises.lancetorIII,100,10,"torp");
 public static final Batiment lancetorIV=new Batiment("lancetor",3,ListeParents.lancetorIV,800,null,10,250F,ListeMarchandises.lancetorIV,150,10,"torp");
 public static final Batiment lancetorV=new Batiment("lancetor",4,ListeParents.lancetorV,1600,null,15,300F,ListeMarchandises.lancetorV,200,15,"torp");
 public static final Batiment lancetorVI=new Batiment("lancetor",5,ListeParents.lancetorVI,3000,null,15,350F,ListeMarchandises.lancetorVI,250,15,"torp");
 public static final Batiment lancetorVII=new Batiment("lancetor",6,ListeParents.lancetorVII,5000,null,20,400F,ListeMarchandises.lancetorVII,300,20,"torp");
 public static final Batiment lancetorVIII=new Batiment("lancetor",7,ListeParents.lancetorVIII,10000,null,20,450F,ListeMarchandises.lancetorVIII,350,20,"torp");
 public static final Batiment lancetorIX=new Batiment("lancetor",8,ListeParents.lancetorIX,15000,null,25,500F,ListeMarchandises.lancetorIX,400,25,"torp");
 public static final Batiment lancetorX=new Batiment("lancetor",9,ListeParents.lancetorX,30000,null,30,550F,ListeMarchandises.lancetorX,500,30,"torp");

 public static final Technologie stratcoI=new Technologie("stratco",0,null,50,null);
 public static final Technologie diploI=new Technologie("diplo",0,null,50,null);
 public static final Technologie progcoI=new Technologie("progco",0,null,50,null);
 public static final Technologie gestplaI=new Technologie("gestpla",0,null,50,null);
 public static final Technologie creplanI=new Technologie("creplan",0, ListeParents.creplanI,50,null);

 public static final Technologie maitmilI=new Technologie("maitmil",0,ListeParents.maitmilI,500,null);
 public static final Technologie maitmilII=new Technologie("maitmil",1,ListeParents.maitmilII,1000,null);
 public static final Technologie maitmilIII=new Technologie("maitmil",2,ListeParents.maitmilIII,1500,null);
 public static final Technologie maitmilIV=new Technologie("maitmil",3,ListeParents.maitmilIV,2500,null);
 public static final Technologie maitmilV=new Technologie("maitmil",4,ListeParents.maitmilV,4000,null);
 public static final Technologie maitmilVI=new Technologie("maitmil",5,ListeParents.maitmilVI,6000,null);
 public static final Technologie maitmilVII=new Technologie("maitmil",6,ListeParents.maitmilVII,8500,null);
 public static final Technologie maitmilVIII=new Technologie("maitmil",7,ListeParents.maitmilVIII,12500,null);
 public static final Technologie maitmilIX=new Technologie("maitmil",8,ListeParents.maitmilIX,18000,null);
 public static final Technologie maitmilX=new Technologie("maitmil",9,ListeParents.maitmilX,30000,null);
 
 public static final Technologie maitstargateI=new Technologie("maitstargate",0,ListeParents.maitstargateI,500,null);
 public static final Technologie maitstargateII=new Technologie("maitstargate",1,ListeParents.maitstargateII,1000,null);
 public static final Technologie maitstargateIII=new Technologie("maitstargate",2,ListeParents.maitstargateIII,1500,null);
 public static final Technologie maitstargateIV=new Technologie("maitstargate",3,ListeParents.maitstargateIV,2500,null);
 public static final Technologie maitstargateV=new Technologie("maitstargate",4,ListeParents.maitstargateV,4000,null);
 public static final Technologie maitstargateVI=new Technologie("maitstargate",5,ListeParents.maitstargateVI,6000,null);
 public static final Technologie maitstargateVII=new Technologie("maitstargate",6,ListeParents.maitstargateVII,8500,null);
 public static final Technologie maitstargateVIII=new Technologie("maitstargate",7,ListeParents.maitstargateVIII,12500,null);
 public static final Technologie maitstargateIX=new Technologie("maitstargate",8,ListeParents.maitstargateIX,18000,null);
 public static final Technologie maitstargateX=new Technologie("maitstargate",9,ListeParents.maitstargateX,30000,null);

 public static final Technologie maitdeI   = new Technologie("maitde",0,ListeParents.maitdeI,1000,null);
 public static final Technologie maitdeII  = new Technologie("maitde",1,ListeParents.maitdeII,5000,null);
 public static final Technologie maitdeIII = new Technologie("maitde",2,ListeParents.maitdeIII,12000,null);

    // Maitrise de races
    public static final Technologie maitr0_I = new Technologie("maitr0_",0, ListeParents.maitr0_I, 50, null );
    public static final Technologie maitr1_I = new Technologie("maitr1_",0, ListeParents.maitr1_I, 50, null );
    public static final Technologie maitr2_I = new Technologie("maitr2_",0, ListeParents.maitr2_I, 50, null );
    public static final Technologie maitr3_I = new Technologie("maitr3_",0, ListeParents.maitr3_I, 50, null );
    public static final Technologie maitr4_I = new Technologie("maitr4_",0, ListeParents.maitr4_I, 50, null );
    public static final Technologie maitr5_I = new Technologie("maitr5_",0, ListeParents.maitr5_I, 50, null );

}






