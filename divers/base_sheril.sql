create table _category
(
    id_category int auto_increment
        primary key,
    name        varchar(255) not null
)
    engine = MyISAM;

create table _forum
(
    id_forum    int auto_increment
        primary key,
    id_category int          not null,
    name        varchar(255) not null,
    description varchar(255) not null
)
    engine = MyISAM;

create index id_category
    on _forum (id_category);

create table _player_ready
(
    num        int                                  null,
    tour       int                                  null,
    created_at datetime default current_timestamp() not null
);

CREATE TABLE _post
(
    id_post   INT AUTO_INCREMENT PRIMARY KEY,
    id_forum  INT          NOT NULL,
    id_parent INT          NULL,
    id_author INT          NULL,
    title     VARCHAR(255) NOT NULL,
    body      MEDIUMTEXT   NOT NULL,
    record    DATETIME     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_forum ON _post(id_forum);
CREATE INDEX idx_parent ON _post(id_parent);

create index id_forum
    on _post (id_forum, id_parent);

create table aa_clone
(
    NUMERO varchar(50)  null,
    LISTE  varchar(200) null,
    DA     int          null
)
    engine = MyISAM;

create table aa_inscription
(
    NOM            varchar(100)                          null,
    ADRESSE        varchar(100)                          null,
    RACE           int                                   null,
    FLOTTE         int                                   null,
    date_insertion timestamp default current_timestamp() null
)
    engine = MyISAM;

create table aa_registre
(
    NUMERO             int          default 0  not null
        primary key,
    LOGIN              varchar(50)             null,
    MOT_DE_PASSE       varchar(50)             null,
    NOM                varchar(100)            null,
    ADRESSE            varchar(100)            null,
    RACE               int                     null,
    TOUR_ARRIVEE       int                     null,
    ORDRE_LOCAL        int                     null,
    ENVOI_STATS        int                     null,
    BG_RAPPORT         int                     null,
    DETAIL_ORDRES      int                     null,
    RECEPTION_RAPPORTS int                     null,
    send               varchar(100) default '' not null,
    auto               int(1)       default 0  not null,
    discord_id         varchar(255)            null
)
    engine = MyISAM;


create table aa_vaisseaux
(
    ADRESSE  varchar(100) null,
    VAISSEAU int          null,
    NOMBRE   int          null
)
    engine = MyISAM;

create table abandonner_technologie
(
    NUMERO int         null,
    NOM    varchar(20) null
)
    engine = MyISAM;

create table adherer_alliance
(
    NUMERO   int null,
    ALLIANCE int null
)
    engine = MyISAM;

create table affecter_gouverneur
(
    NUMERO   int          null,
    NOM      varchar(100) null,
    POSITION varchar(20)  null
)
    engine = MyISAM;

create table affecter_heros
(
    NUMERO   int          null,
    NOM      varchar(100) null,
    POSITION int          null
)
    engine = MyISAM;

create table affecter_recherche
(
    NUMERO      int         null,
    TECHNOLOGIE varchar(20) null,
    AFFECTATION int         null
)
    engine = MyISAM;

create table annuler_construction
(
    NUMERO  int         null,
    SYSTEME varchar(20) null
)
    engine = MyISAM;

create table changer_capitale
(
    NUMERO  int         null,
    SYSTEME varchar(20) null
)
    engine = MyISAM;

create table charger_cargo
(
    NUMERO       int          null,
    SYSTEME      varchar(20)  null,
    CONSTRUCTION varchar(100) null,
    NOMBRE       int          null,
    PLANETE      int          null,
    SYSTEME2     varchar(20)  null,
    PLANETE2     int          null
)
    engine = MyISAM;

create table construire
(
    NUMERO       int          null,
    SYSTEME      varchar(20)  null,
    CONSTRUCTION varchar(100) null,
    NOMBRE       int          null,
    PLANETE      int          null
)
    engine = MyISAM;

create table construire_flotte
(
    NUMERO       int          null,
    FLOTTE       int          null,
    CONSTRUCTION varchar(100) null
)
    engine = MyISAM;

create table creer_alliance
(
    NUMERO   int          null,
    ALLIANCE varchar(100) null,
    SECRET   int          null,
    TYPE     int          null,
    DROITS   int          null
)
    engine = MyISAM;

create table creer_plan
(
    NUMERO    int          null,
    NOM       varchar(100) null,
    MARQUE    varchar(100) null,
    DOMAINE   varchar(100) null,
    ROYALTIES int          null
)
    engine = MyISAM;

create table creer_plan_ajouter
(
    NUMERO int          null,
    NOM    varchar(100) null,
    NOMBRE int          null
)
    engine = MyISAM;

create table creer_strategie
(
    NUMERO      int          null,
    NOM         varchar(100) null,
    AGRESSIVITE int          null,
    CIBLE       int          null
)
    engine = MyISAM;

create table creer_strategie_ajouter
(
    NUMERO  int          null,
    NOM     varchar(100) null,
    POS_X   int          null,
    POS_Y   int          null,
    CIBLE_A int          null,
    CIBLE_B int          null,
    CIBLE_C int          null,
    CIBLE_D int          null,
    CIBLE_E int          null,
    CIBLE_F int          null,
    CIBLE_G int          null,
    CIBLE_K int          null,
    CIBLE_I int          null,
    CIBLE_J int          null
)
    engine = MyISAM;

create table decharger_cargo
(
    NUMERO     int          null,
    FLOTTE     int          null,
    CHARGEMENT varchar(100) null,
    NOMBRE     int          null,
    CARGO      int          null,
    POSTE      int          null,
    PLANETE    int          null
)
    engine = MyISAM;

create table deplacer_flotte
(
    NUMERO    int          null,
    NUMFLOTTE int          null,
    POSX      int          null,
    POSY      int          null,
    GALAXIE   int          null,
    DIRECTIVE int          null,
    STRATEGIE varchar(100) null
)
    engine = MyISAM;

create table deprogrammer_construction
(
    NUMERO  int         null,
    SYSTEME varchar(20) null
)
    engine = MyISAM;

create table diviser_flotte
(
    NUMERO      int          null,
    FLOTTE      int          null,
    NOM         varchar(100) null,
    NB_DIVISION int          null
)
    engine = MyISAM;

create table diviser_flotte_ajouter
(
    NUMERO      int          null,
    FLOTTE      int          null,
    NB_DIVISION int          null,
    TYPE        varchar(100) null,
    NOMBRE      int          null
)
    engine = MyISAM;

create table donner_plan
(
    NUMERO       int          null,
    PLAN         varchar(100) null,
    BENEFICIAIRE int          null,
    MODE         int          null
)
    engine = MyISAM;

create table ecrire_adresse_alliance
(
    NUMERO   int          null,
    ADRESSE  varchar(100) null,
    ALLIANCE int          null
)
    engine = MyISAM;

create table ecrire_adresse_commandant
(
    NUMERO  int          null,
    ADRESSE varchar(100) null
)
    engine = MyISAM;

create table ecrire_article
(
    NUMERO  int        null,
    ARTICLE mediumtext null
)
    engine = MyISAM;

create table enroler_lieutenant
(
    NUMERO int null,
    OFFRE  int null,
    NUM    int null
)
    engine = MyISAM;

create table exclure_alliance
(
    NUMERO   int null,
    ALLIANCE int null,
    VOTE     int null
)
    engine = MyISAM;

create table fixer_taux_poste
(
    NUMERO int null,
    TAUX   int null
)
    engine = MyISAM;

create table fusionner_flotte
(
    NUMERO    int null,
    FLOTTE_1  int null,
    FLOTTE_2  int null,
    DIRECTIVE int null
)
    engine = MyISAM;

create table larguer_mines
(
    NUMERO int null,
    FLOTTE int null
)
    engine = MyISAM;

create table liberer_lieutenant
(
    NUMERO int          null,
    NOM    varchar(100) null
)
    engine = MyISAM;

create table mettre_au_rebus
(
    NUMERO       int         null,
    SYSTEME      varchar(20) null,
    CONSTRUCTION varchar(20) null,
    NOMBRE       int         null,
    PLANETE      int         null
)
    engine = MyISAM;

create table modifier_budget
(
    NUMERO      int         null,
    SYSTEME     varchar(20) null,
    BUDGET      int         null,
    POURCENTAGE int         null
)
    engine = MyISAM;

create table modifier_politique
(
    NUMERO    int         null,
    SYSTEME   varchar(20) null,
    POLITIQUE int         null
)
    engine = MyISAM;

create table modifier_taxation_planete
(
    NUMERO  int         null,
    SYSTEME varchar(20) null,
    TAXE    int         null,
    PLANETE int         null
)
    engine = MyISAM;

create table modifier_taxation_systeme
(
    NUMERO  int         null,
    SYSTEME varchar(20) null,
    TAXE    int         null
)
    engine = MyISAM;

create table nommer_dirigeant
(
    NUMERO    int null,
    ALLIANCE  int null,
    DIRIGEANT int null
)
    engine = MyISAM;

create table pister
(
    NUMERO int         null,
    FLOTTE int         null,
    CIBLE  varchar(20) null
)
    engine = MyISAM;

create table programmer_construction
(
    NUMERO       int          null,
    SYSTEME      varchar(20)  null,
    CONSTRUCTION varchar(100) null
)
    engine = MyISAM;

create table quitter_alliance
(
    NUMERO   int null,
    ALLIANCE int null
)
    engine = MyISAM;

create table renommer_alliance
(
    NUMERO   int          null,
    NOM      varchar(100) null,
    ALLIANCE int          null
)
    engine = MyISAM;

create table renommer_flotte
(
    NUMERO int          null,
    FLOTTE int          null,
    NOM    varchar(100) null
)
    engine = MyISAM;

create table renommer_lieutenant
(
    NUMERO      int          null,
    ANCIEN_NOM  varchar(100) null,
    NOUVEAU_NOM varchar(100) null
)
    engine = MyISAM;

create table renommer_planete
(
    NUMERO  int          null,
    SYSTEME varchar(20)  null,
    NOM     varchar(100) null,
    PLANETE int          null
)
    engine = MyISAM;

create table renommer_systeme
(
    NUMERO  int          null,
    SYSTEME varchar(20)  null,
    NOM     varchar(100) null
)
    engine = MyISAM;

create table rompre_pacte
(
    NUMERO int null,
    PACTE  int null
)
    engine = MyISAM;

create table services_speciaux
(
    NUMERO  int         null,
    SYSTEME varchar(20) null,
    TYPE    int         null,
    PLANETE int         null
)
    engine = MyISAM;

create table signer_pacte
(
    NUMERO int null,
    PACTE  int null
)
    engine = MyISAM;

create table terraformer_planete
(
    NUMERO  int         null,
    SYSTEME varchar(20) null,
    PLANETE int         null
)
    engine = MyISAM;

create table terraformer_systeme
(
    NUMERO  int         null,
    SYSTEME varchar(20) null
)
    engine = MyISAM;

create table transferer_centaures
(
    NUMERO       int null,
    BENEFICIAIRE int null,
    MONTANT      int null,
    MODE         int null
)
    engine = MyISAM;

create table transferer_flotte
(
    NUMERO       int null,
    BENEFICIAIRE int null,
    FLOTTE       int null,
    DUREE        int null,
    MODE         int null
)
    engine = MyISAM;

create table transferer_planete
(
    NUMERO       int         null,
    BENEFICIAIRE int         null,
    SYSTEME      varchar(20) null,
    PLANETE      int         null,
    MODE         int         null
)
    engine = MyISAM;

create table transferer_strategie
(
    NUMERO       int          null,
    BENEFICIAIRE int          null,
    STRATEGIE    varchar(100) null,
    MODE         int          null
)
    engine = MyISAM;

create table transferer_systeme
(
    NUMERO       int         null,
    BENEFICIAIRE int         null,
    SYSTEME      varchar(20) null,
    MODE         int         null
)
    engine = MyISAM;

create table transferer_technologie
(
    NUMERO       int         null,
    BENEFICIAIRE int         null,
    TECHNOLOGIE  varchar(20) null,
    MODE         int         null
)
    engine = MyISAM;

create table utiliser_colonisateur
(
    NUMERO  int null,
    FLOTTE  int null,
    PLANETE int null
)
    engine = MyISAM;

create table utiliser_porte_galactique
(
    NUMERO  int null,
    FLOTTE  int null,
    GALAXIE int null
)
    engine = MyISAM;

create table utiliser_porte_intragalactique
(
    NUMERO int null,
    FLOTTE int null
)
    engine = MyISAM;

create table valider_adhesion_alliance
(
    NUMERO    int null,
    POSTULANT int null,
    ALLIANCE  int null
)
    engine = MyISAM;

create table vendre_flotte
(
    NUMERO       int null,
    BENEFICIAIRE int null,
    FLOTTE       int null
)
    engine = MyISAM;

create table z_alliance_dirigeant
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_alliance_secrete
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_alliance_type
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_alliances_adhesion
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_appartient_alliance_a_1
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_appartient_alliance_a_2
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_appartient_alliance_a_3
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_appartient_alliance_c_1
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_appartient_alliance_c_2
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_budgets
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_cargaison_chargement
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_cargaison_dechargement
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_commandants_adhesion
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_commandants_transfert
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_composants
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_constructions_possibles
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_directives
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_domaine_plan
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_cargo
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_colonisateur
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_detectees
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_galactiques
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_intragalactiques
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_mines
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_flottes_usines
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_galaxies
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_gouverneurs
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_heros
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_leaders
(
    CODE  mediumtext null,
    PARAM mediumtext not null
)
    engine = MyISAM;

create table z_lieutenant_renommer
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_missions
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_mode_transfert
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_ordres
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_plans_cibles
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_plans_constructibles
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_plans_transfert
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_politiques
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_programmation_construction
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_proprios_poste
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_rebus_materiel
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_rebus_systemes
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_rompre_pacte
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_signer_pacte
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_strategie_agressivite
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_strategie_cible
(
    CODE  mediumtext null,
    PARAM mediumtext null
)
    engine = MyISAM;

create table z_strategies
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_systeme_renommer
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_systemes
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_systemes_construction
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_systemes_detectes
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_taxation
(
    CODE mediumtext null
)
    engine = MyISAM;

create table z_techno_transfert
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_technologies_cherchees
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_technologies_connues
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

create table z_vaisseaux
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null
)
    engine = MyISAM;

create table z_vaisseaux_cargo
(
    NUMERO int default 0 not null
        primary key,
    CODE   mediumtext    null,
    PARAM  mediumtext    null
)
    engine = MyISAM;

CREATE TABLE `z_galactique` (
    `NUMERO` int(11) NOT NULL default '0',
    `ID_OFFRE` text NOT NULL,
    `VENDEUR` text NOT NULL,
    `CODE` text NOT NULL,
    `QUANTITE` mediumtext NOT NULL,
    `PRIX` mediumtext NOT NULL
) engine=MyISAM;

CREATE TABLE `vendre_galactique` (
     `NUMERO` int(11) NOT NULL default '0',
     `v0` VARCHAR(20) NOT NULL, -- Position du système (ex: "1.1")
     `v1` text NOT NULL, -- Code marchandise
     `v2` int(11) NOT NULL default '0', -- Quantité
     `v3` int(11) NOT NULL default '0'  -- Prix unitaire
) engine=MyISAM;

CREATE TABLE `acheter_galactique` (
  `NUMERO` int(11) NOT NULL default '0',
  `v0` int(11) NOT NULL default '0', -- ID de l'offre choisie
  `v1` int(11) NOT NULL default '0',  -- Prix enchere
  `v2` VARCHAR(20) NOT NULL  -- Position du système de réception (ex: "1.1.1")
) engine=MyISAM;

create unique index acheter_galactique_uniq_offer
    on acheter_galactique (NUMERO, v0);

-- id pour identification ordre
ALTER TABLE transferer_centaures ADD COLUMN id INT NOT NULL AUTO_INCREMENT PRIMARY KEY;
ALTER TABLE diviser_flotte ADD COLUMN id INT NOT NULL AUTO_INCREMENT PRIMARY KEY;

-- gestion des statistiques
CREATE TABLE IF NOT EXISTS statistiques (
                                            id_stat BIGINT NOT NULL AUTO_INCREMENT,
                                            tour INT NOT NULL,
                                            numero INT NOT NULL,
                                            puissance INT NOT NULL DEFAULT 0,
                                            centaure INT NOT NULL DEFAULT 0,
                                            planetes INT NOT NULL DEFAULT 0,
                                            pop_syst INT NOT NULL DEFAULT 0,
                                            pop_vs INT NOT NULL DEFAULT 0,
                                            reputation INT NOT NULL DEFAULT 0,
                                            rayonnement INT NOT NULL DEFAULT 0,
                                            technologie INT NOT NULL DEFAULT 0,
                                            offensif INT NOT NULL DEFAULT 0,
                                            pv INT NOT NULL DEFAULT 0,
                                            PRIMARY KEY (id_stat),
    -- Empêche les doublons si le script est relancé
    UNIQUE KEY unq_tour_commandant (tour, numero)
    ) ENGINE=InnoDB;

--
-- INDEX POUR LES CALCULS ET CLASSEMENTS
--

-- Accélère la comparaison entre deux tours pour un même commandant
CREATE INDEX idx_progression ON statistiques (numero, tour);

-- Accélère les classements par tour (le tour en premier est crucial)
CREATE INDEX idx_classement_puissance ON statistiques (tour, puissance);
CREATE INDEX idx_classement_pv ON statistiques (tour, pv);
CREATE INDEX idx_classement_techno ON statistiques (tour, technologie);

create table _share_technology
(
    NUMERO     int not null,
    SHARE_WITH int not null,
    constraint _share_technology_pk
        unique (NUMERO, SHARE_WITH)
);

