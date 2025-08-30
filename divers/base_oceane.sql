# phpMyAdmin MySQL-Dump
# http://phpwizard.net/phpMyAdmin/
#
# Serveur: localhost Base de données: base_oceane

# --------------------------------------------------------
#
# Structure de la table 'java_lang_integer'
#

CREATE TABLE java_lang_integer (
   c_ int(11) DEFAULT '0' NOT NULL,
   value int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_alliance'
#

CREATE TABLE zigzag_jeu_oceane_alliance (
   c_ int(11) DEFAULT '0' NOT NULL,
   numeroAlliance int(11),
   nom text,
   droitsEntree float,
   _type_ int(11),
   secrete tinyint(4),
   dirigeantNum int(11),
   concepteurNum int(11),
   concepteurNom text,
   tourDeCreation int(11),
   adresseElectronique text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_commandant'
#

CREATE TABLE zigzag_jeu_oceane_commandant (
   c_ int(11) DEFAULT '0' NOT NULL,
   capitale int(11),
   domaine int(11),
   flottes int(11),
   recherches int(11),
   reputation int(11),
   centaures float,
   tauxTaxationPoste int(11),
   alliances int(11),
   pactesDeNonAgression int(11),
   technologiesConnues int(11),
   plansDeVaisseaux int(11),
   heros int(11),
   gouverneurs int(11),
   strategies int(11),
   adresseElectronique text,
   motDePasse text,
   login text,
   tourArrivee int(11),
   dernierTourRendu int(11),
   site text,
   ordrePasseEnLocale tinyint(4),
   envoiStats tinyint(4),
   rapportSansBgNoir tinyint(4),
   detailOrdresPasses tinyint(4),
   maniereDeRecevoirSesRapports int(11),
   langue int(11),
   nom text,
   race int(11),
   numero int(11),
   typeDeJoueur int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_construction'
#

CREATE TABLE zigzag_jeu_oceane_construction (
   c_ int(11) DEFAULT '0' NOT NULL,
   code text,
   nombre int(11),
   pointsEffectues int(11),
   planete int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_constructionplanetaire'
#

CREATE TABLE zigzag_jeu_oceane_constructionplanetaire (
   c_ int(11) DEFAULT '0' NOT NULL,
   code text,
   dommages int(11),
   experience int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_debris'
#

CREATE TABLE zigzag_jeu_oceane_debris (
   c_ int(11) DEFAULT '0' NOT NULL,
   position int(11),
   debris int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_flotte'
#

CREATE TABLE zigzag_jeu_oceane_flotte (
   c_ int(11) DEFAULT '0' NOT NULL,
   nom text,
   constructionEnCours text,
   vaisseaux int(11),
   position int(11),
   direction int(11),
   directive int(11),
   directivePrecision int(11),
   strategie text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_gouverneur'
#

CREATE TABLE zigzag_jeu_oceane_gouverneur (
   c_ int(11) DEFAULT '0' NOT NULL,
   position int(11),
   nom text,
   competencesDepart int(11),
   vitesseDepart int(11),
   attaqueDepart int(11),
   defenseDepart int(11),
   moralDepart int(11),
   marchandDepart int(11),
   competences int(11),
   vitesse int(11),
   attaque int(11),
   defense int(11),
   moral int(11),
   marchand int(11),
   race int(11),
   experience int(11),
   niveau int(11),
   tourDeCreation int(11),
   tourApparition int(11),
   changementNom tinyint(4),
   nombreDeMorts int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_heros'
#

CREATE TABLE zigzag_jeu_oceane_heros (
   c_ int(11) DEFAULT '0' NOT NULL,
   position int(11),
   nom text,
   competencesDepart int(11),
   vitesseDepart int(11),
   attaqueDepart int(11),
   defenseDepart int(11),
   moralDepart int(11),
   marchandDepart int(11),
   competences int(11),
   vitesse int(11),
   attaque int(11),
   defense int(11),
   moral int(11),
   marchand int(11),
   race int(11),
   experience int(11),
   niveau int(11),
   tourDeCreation int(11),
   tourApparition int(11),
   changementNom tinyint(4),
   nombreDeMorts int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_objetcomplexetransporte'
#

CREATE TABLE zigzag_jeu_oceane_objetcomplexetransporte (
   c_ int(11) DEFAULT '0' NOT NULL,
   contenu int(11),
   code text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_objetsimpletransporte'
#

CREATE TABLE zigzag_jeu_oceane_objetsimpletransporte (
   c_ int(11) DEFAULT '0' NOT NULL,
   nombre int(11),
   code text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_plandevaisseau'
#

CREATE TABLE zigzag_jeu_oceane_plandevaisseau (
   c_ int(11) DEFAULT '0' NOT NULL,
   composants int(11),
   royalties int(11),
   concepteurNum int(11),
   concepteurNom text,
   nom text,
   marque text,
   description text,
   acces int(11),
   precisionAcces int(11),
   tourDeCreation int(11),
   nbCases int(11),
   mineraiNecessaire int(11),
   prix float,
   marchandisesNecessaires int(11),
   caracteristiquesSpeciales int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_planete'
#

CREATE TABLE zigzag_jeu_oceane_planete (
   c_ int(11) DEFAULT '0' NOT NULL,
   radiation int(11),
   temperature int(11),
   gravite int(11),
   atmosphere int(11),
   taille int(11),
   _type_ int(11),
   productionMinerai int(11),
   productionMarchandise int(11),
   nombreProductionMarchandise int(11),
   terraformation int(11),
   stockMinerai int(11),
   taxation int(11),
   stabilite int(11),
   nom text,
   revolte tinyint(4),
   populations int(11),
   batiments int(11),
   proprietaire int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_population'
#

CREATE TABLE zigzag_jeu_oceane_population (
   c_ int(11) DEFAULT '0' NOT NULL,
   race int(11),
   popActuelle int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_position'
#

CREATE TABLE zigzag_jeu_oceane_position (
   c_ int(11) DEFAULT '0' NOT NULL,
   galaxie int(11),
   pos int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_possession'
#

CREATE TABLE zigzag_jeu_oceane_possession (
   c_ int(11) DEFAULT '0' NOT NULL,
   constructions int(11),
   programmationConstruction text,
   politique int(11),
   budget int(11),
   poste int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_sessionunivers'
#

CREATE TABLE zigzag_jeu_oceane_sessionunivers (
   c_ int(11) DEFAULT '0' NOT NULL,
   SYSTEMES int(11),
   DEBRIS int(11),
   COMMANDANTS int(11),
   PLANS_DE_VAISSEAUX int(11),
   ALLIANCES int(11),
   TECHNOLOGIES_PUBLIQUES int(11),
   LEADERS_EN_VENTE int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_strategiedecombatspatial'
#

CREATE TABLE zigzag_jeu_oceane_strategiedecombatspatial (
   c_ int(11) DEFAULT '0' NOT NULL,
   nom text,
   positionnement int(11),
   comportement int(11),
   agressivite int(11),
   typeCible int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_systeme'
#

CREATE TABLE zigzag_jeu_oceane_systeme (
   c_ int(11) DEFAULT '0' NOT NULL,
   position int(11),
   typeEtoile int(11),
   pla int(11),
   nom text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_jeu_oceane_vaisseau'
#

CREATE TABLE zigzag_jeu_oceane_vaisseau (
   c_ int(11) DEFAULT '0' NOT NULL,
   nom text,
   _type_ text,
   experience int(11),
   raceEquipage int(11),
   moral int(11),
   dommages int(11),
   cargaison int(11),
   proprietaire int(11),
   tourRetourLocation int(11),
   populationVilleSpatiale int(11),
   detruit tinyint(4),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_sql_collectionsql'
#

CREATE TABLE zigzag_sql_collectionsql (
   c_ int(11) DEFAULT '0' NOT NULL,
   _table_ int(11),
   _type_ text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_sql_mapsql'
#

CREATE TABLE zigzag_sql_mapsql (
   c_ int(11) DEFAULT '0' NOT NULL,
   _type_ text,
   cles int(11),
   valeurs int(11),
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_sql_stringsql'
#

CREATE TABLE zigzag_sql_stringsql (
   c_ int(11) DEFAULT '0' NOT NULL,
   chaine text,
   PRIMARY KEY (c_)
);


# --------------------------------------------------------
#
# Structure de la table 'zigzag_sql_tablesql'
#

CREATE TABLE zigzag_sql_tablesql (
   c_ int(11) DEFAULT '0' NOT NULL,
   dim text,
   membres text,
   _type_ text,
   PRIMARY KEY (c_)
);

