// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import zIgzAg.utile.Copie;

public class Chemin {

	 public static String RACINE = "";
	 public static String RACINE_ZIP = "";
	 public static String NUMERO_DU_TOUR = "";
	 public static String PATH_STATS = "";
	
	static {
		Properties properties = new Properties();
		try {
		    properties.load(new FileInputStream(Univers.config));
		    RACINE = properties.getProperty("PATH_DATA");
		    RACINE_ZIP = properties.getProperty("PATH_EXPORT_ZIPPED_RAPPORT");
			NUMERO_DU_TOUR = properties.getProperty("PATH_TOUR");
			PATH_STATS = properties.getProperty("PATH_STATS");
		} catch (IOException e) {
			System.out.println("Cant load config file. file config.properties should be on the same directory as the jar file");
		}

	}
	
	/*
	 * Cette classe contient les différents chemins où sont stockés certains
	 * fichiers
	 */

	
	// le repertoire où on va tout ranger.

	public static final String CARNET_DE_BORD = RACINE + "carnet.txt";

	public static final String RACINE_SITE = "http://sheril.pbem-france.net/";
	public static final String SITE = RACINE_SITE + "principal.htm";
	public static final String SITE_REGISTRE = RACINE_SITE + "info/reg.htm";

	public static final String MJ = RACINE + "mj/";
	// Le repertoire où est stocké les informations nécessaires au mj.

	public static final String IMAGES_RESERVE = RACINE + "images/";
	// les images de réserve

	public static final String ATLAS = RACINE + "atlas/";
	// Le repertoire où est stocké les informations de l'atlas.

	public static final String FICHIERS = RACINE + "fichiers/";
	// Le repertoire où sont stockés les fichiers "persistants";

	public static final String FICHIER_RAPPORTS_IMAGES = FICHIERS + "images";
	public static final String FICHIER_STATS = FICHIERS + "stats.htm";

	public static String DONNEES;
	// le repertoire où sont stokées les données.

	public static String COMMANDANTS;
	public static String SYSTEMES;
	public static String DEBRIS;
	public static String PLANS_DE_VAISSEAUX;
	public static String COMPOSANTS_DE_VAISSEAUX;
	public static String BATIMENTS;
	public static String ALLIANCES;
	public static String TECHNOLOGIES_PUBLIQUES;
	public static String LEADERS_EN_VENTE;
	public static String BASE_STATS;
	public static String RELATIONS_RACES;
	public static String TRANSFERTS;
	public static String TRANSIENTS;

	public static String RAPPORTS;
	// le repertoire où sont stockés les rapports.

	public static String ZIP;
	// le repertoire où sont stockés les rapports.

	public static String RAPPORTS_IMAGES;
	// Le répertoire où sont stockées les images des rapports.

	public static String ORDRES;
	// le repertoire où sont stockés les fichiers nécessaires pour passer les
	// ordres.

	public static String DONNEES_ORDRES;
	// le fichier sql principal.

	public static String SECURITE_RAPPORT;
	// le répertoire de sécurité pour les rapports.

	public static String DONNEES_RACES;
	// le fichier descriptif des races.

	public static String SECURITE;
	// le repertoire où sont stockés les informations à sécuriser.

	public static String STATS;

	// Le repertoire où sont stockées les différentes stats.

	private static String c(String valeur, boolean rep) {
		String inter = valeur;
		if (inter.charAt(inter.length() - 1) == '/')
			inter = inter.substring(0, inter.length() - 1);
		if (rep) {
			File f = new File(inter);
			f.mkdirs();
		}
		return valeur;
	}

	public static void initialiserChemins(int numeroTour) {
		String rep = RACINE + "tour" + Integer.toString(numeroTour) + "/";

		DONNEES = c(rep + "donnees/", true);

		COMMANDANTS = c(DONNEES + "comm.txt", false);
		SYSTEMES = c(DONNEES + "sys.txt", false);
		DEBRIS = c(DONNEES + "debris.txt", false);
		PLANS_DE_VAISSEAUX = c(DONNEES + "plans.txt", false);
		COMPOSANTS_DE_VAISSEAUX = c(DONNEES + "compo.txt", false);
		BATIMENTS = c(DONNEES + "bati.txt", false);
		ALLIANCES = c(DONNEES + "alliance.txt", false);
		TECHNOLOGIES_PUBLIQUES = c(DONNEES + "techpub.txt", false);
		LEADERS_EN_VENTE = c(DONNEES + "leader.txt", false);
		BASE_STATS = c(DONNEES + "stats.txt", false);
		RELATIONS_RACES = c(DONNEES + "rel.txt", false);
		TRANSFERTS = c(DONNEES + "transfert.txt", false);
		TRANSIENTS = c(DONNEES + "trans.txt", false);

		SECURITE_RAPPORT = c(rep + "secure2/", true);
		RAPPORTS = c(rep + "rapports/", true);
		ZIP = c(rep + "zip/", true);
		RAPPORTS_IMAGES = c(RAPPORTS + "images", true);
		ORDRES = c(rep + "ordres/", true);
		DONNEES_ORDRES = c(ORDRES + "ordres", false);
		DONNEES_RACES = c(ORDRES + "race.txt", false);
		SECURITE = c(rep + "secure/", true);
		STATS = c(rep + "stats/", true);

		Copie.copie(FICHIER_RAPPORTS_IMAGES, RAPPORTS_IMAGES);
		Copie.copie(FICHIER_STATS, STATS + "stats.htm");

	}

}
