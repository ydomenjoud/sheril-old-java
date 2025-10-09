// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

public class MessagesInfo extends MessagesAbstraits {

	public static final String MAIL_TITRE_RAPPORT = "[Sheril] Rapport du tour {0}";
	public static final String MAIL_CORPS_NOUVEAU_RAPPORT = "Bonjour.\n\nVous vous êtes inscrit à Sheril.\n\nVotre rapport se trouve ici : {2} \n\nVous aurez besoin de:\n  1)votre login: {0}\n  2)votre mot de passe: {1}\n\n Conservez les précieusement!\n\n           (....) \n\n Humm... Un autre événement requiert mon attention et je dois vous quitter, momentanément!\n\n Ah oui! Si vous voulez pour une raison ou une autre vous Désinscrire, allez sur la page "
			+ Chemin.SITE_REGISTRE
			+ " .\n\nA bientôt et bonne chance!\n\nXyur06-Tr";
	public static final String MAIL_REPRISE = "Bonjour,je suis une IA qui répond au doux nom de Xyur06-Tr.\n\nVous êtes inscrit à Sheril, un jeu qui a comme adresse internet "
			+ Chemin.RACINE_SITE
			+ " .\n\nVotre rapport se trouve ici : {2} \n\n" +
					"Vous aurez besoin de:\n  1)votre login: {0}\n  2)votre mot de passe: {1}\n\n" +
					" Conservez les précieusement!\n\n" +
					" Un autre événement requiert mon attention et je dois vous quitter, momentanément!\n\n A bientôt et bonne chance!\n\nXyur06-Tr";
	
	public static final String MAIL_CORPS_RAPPORT = "Bonjour,je suis une IA qui répond au doux nom de Xyur06-Tr.\n\nAttention, les imagesnécessaires au rapport ne sont fournies que lors du premier tour. Si vous avez effacé vos imagesnécessaires pour visualiser votre rapport avec les images, vous pouvez les téléchargez à cette adresse :  http://sheril.free.fr/utile/download.php3 et les Dézipper dans le mêmerépertoire dans lequel vous Dézippez le rapport. \n\n Rappel Sheril:  "
			+ Chemin.RACINE_SITE
			+ " .\n\nVotre rapport se trouve ici : {2} \n\nVous aurez besoin de:\n  1)votre login: {0}\n  2)votre mot de passe: {1}\n\n Conservez les précieusement!\n\n           (....) \n\n Humm... Un autre événement requiert mon attention et je dois vous quitter, momentanément!\n\n Ah oui! Si vous voulez pour une raison ou une autre vous Désinscrire, allez sur la page "
			+ Chemin.SITE_REGISTRE + " .\n\nA bientôt et bonne chance!\n\nMyst";

	public static final String HEROS_MORT_0000 = "Au cours de ce combat, votre lieutenant {0} vient deréchapper de justesse à la mort.";
	public static final String HEROS_MORT_0001 = "Au cours de ce combat, votre lieutenant {0} a trouvé la mort. Un clone de lui est disponible aux Enchêres Galactiques.";
	public static final String HEROS_MORT_0002 = "Le lieutenant {0} du commandant {1} vient trouver la mort lors d'un combat épique. Un nouveau clone est disponible aux Enchères Galactiques.";

	public static final String HEROS_AUGMENTATION_NIVEAU_0000 = "Votre lieutenant {0} vient d'augmenter d'un niveau. Il gagne un point dans sa caractéristique {1} et un niveau en ce qui concerne la compétence {2}.";

	public static final String ELIMINATION_COMMANDANT_0000 = "Nous apprenons avec tristesse que le commandant {0} vient de disparaître. Toutes ses fiefs sont maintenant neutres.";
	public static final String APPARITION_COMMANDANT_0000 = "{0} vient de conquêrir son premier système. Souhaitez lui la bienvenue!";

	public static final String RETOUR_PRET_FLOTTE_0000 = "Retour de prêt du commandant {0}.";
	public static final String RETOUR_PRET_FLOTTE_0001 = "Certains vaisseaux que le commandant {0} vous avait prêtés viennent de regagner leur affectation d'origine.";

	public static final String SYSTEME_EXTERMINATION_0000 = "Votre politique d'extermination sur le système {0} vous as rapporté cette semaine {1} centaures.";

	public static final String EVENEMENT_0000 = "Le fond d'aide aux Déshérités de la <i>Banque Galactique</i> vous vient en aide. Vous recevez {0} centaures.";

	public static final String EVENEMENT_OXOLE = "Découverte d'un gisement de {0} oxole en {1}";
	public static final String EVENEMENT_TIXIUM = "Découverte d'un gisement de {0} tixium en {1}";
	public static final String EVENEMENT_LIXIAM = "Découverte d'un gisement de {0} lixiam en {1}";

	public static final String EVENEMENT_POPULATION_POSITIF_0 = "La planète {1} de votre système {0} connait un babyboom. Ca copule !!! . Sa population augmente de {2} millions.";
	public static final String EVENEMENT_POPULATION_POSITIF_1 = "La planète {1} de votre système {0} assiste à un afflux massif de réfugiés. Sa population augmente de {2} millions.";
	public static final String EVENEMENT_POPULATION_POSITIF_2 = "La planète {1} de votre système {0} vient de Découvrir sur son sol un gisement de Chlorure d'hydrogénate Esterifié. On assiste à une véritable ruée. Sa population augmente de {2} millions.";
	public static final String EVENEMENT_POPULATION_POSITIF_3 = "Le célèbre prophète Nietzche vient de s'établir sur la planète {1} de votre système {0}. La masse de ses adorateurs le suit. La population de la planète augmente de {2} millions.";
	public static final String EVENEMENT_POPULATION_NEGATIF_0 = "La planète {1} de votre système {0} est touché du virus Xu8kh-I, dont le remède n'est actuellement toujours pas connu. Sa population diminue de {2} millions.";
	public static final String EVENEMENT_POPULATION_NEGATIF_1 = "La planète {1} de votre système {0} traverse une grave crise du poisson, surtout le merlan. Sa population diminue de {2} millions.";
	public static final String EVENEMENT_POPULATION_NEGATIF_2 = "La planète {1} de votre système {0} vient de subir une collision avec un météorite géant, mais heureusement Bruce Willis est là !!. Sa population ne diminue que de {2} millions.";
	public static final String EVENEMENT_POPULATION_NEGATIF_3 = "Le squelette de Mahomet vient de maudir la planète {1} de votre système {0}. Les gens fuient devant cette malédiction. La population de la planète diminue de {2} millions.";

	public static final String COMBAT_FLOTTE_0000 = "Combat en {3} entre votre flotte {0} et la flottille {1} du commandant {2}.résultat de combat:<BR>";
	public static final String COMBAT_FLOTTE_0001 = "Votre flotte est anéantie.";
	public static final String COMBAT_FLOTTE_0002 = "Suite au {0} dommage(s) infligés votre flotte a perdu {1} vaisseau(x).";
	public static final String COMBAT_FLOTTE_0003 = "La flotte ennemie est détruite.";
	public static final String COMBAT_FLOTTE_0004 = "La flotte ennemie a subit {0} dommage(s) et a perdu {1} vaisseau(x).";
	public static final String COMBAT_FLOTTE_0005 = "<HR><BIG>Round {3} entre votre flotte {2} et la flotte {1} du commandant {0}</BIG><BR>";
	public static final String COMBAT_FLOTTE_0006 = "Pour voir le Détail du combat";
	public static final String COMBAT_FLOTTE_0007 = "Résultat du combat:<BR>";

	// transfert entre système erreur
	public static final String ER_COMMANDANT_TRANSFERER_0000 = "Transfert : le systeme de chargement {0} n'existe pas";
	public static final String ER_COMMANDANT_TRANSFERER_0001 = "Transfert : le systeme de Déchargement {0} n'existe pas";
	public static final String ER_COMMANDANT_TRANSFERER_0002 = "Transfert : le systeme de chargement {0} ne vous appartient pas";
	public static final String ER_COMMANDANT_TRANSFERER_0003 = "Transfert : le systeme de Déchargement {0} ne vous appartient pas";
	public static final String ER_COMMANDANT_TRANSFERER_0004 = "Transfert : le systeme de chargement {0} n'a pas de planète {1}";
	public static final String ER_COMMANDANT_TRANSFERER_0005 = "Transfert : le systeme de Déchargement {0} n'a pas de planète {1}";
	public static final String ER_COMMANDANT_TRANSFERER_0006 = "Transfert : Le chargement demandé n'a pu être trouvé";
	public static final String ER_COMMANDANT_TRANSFERER_0007 = "Saturation porte spatiale ( max {3} ): vous n'avez pas pu transférer {0} de {1} à {2}";
	public static final String ER_COMMANDANT_TRANSFERER_0008 = "Transfert : le système de destination  {0} est possédé par plusieurs commandants, veuillez spécifier la planète d'arrivée.";
	public static final String ER_COMMANDANT_TRANSFERER_0009 = "Transfert : le système de destination  {0} ne possède pas de planètes permettant de recevoir le chargement: {1}";
	
	// evenement
	public static final String EV_COMMANDANT_TRANSFERER_0000 = "Transfert : Vous avez transferé {0} de {1} à {2}";

	public static final String COMBAT_SYSTEME_0000 = "Heureuse victoire: {0} vient de prendre la planète {1} du système {2} appartenant au commandant {3}.";
	public static final String COMBAT_SYSTEME_0001 = "La flotte {0} du commandant {3} vient de prendre votre planète {1} du système {2}.";
	public static final String COMBAT_SYSTEME_0002 = "Votre flotte {0} vient d'échouer dans son attaque sur la planète {1} du système {2} appartenant au commandant {3}.";
	public static final String COMBAT_SYSTEME_0003 = "Le peuple se Défend! Votre planète {1} du système {2} vient de repousser l'attaque de la flotte {0} du commandant {3}.";
	public static final String COMBAT_SYSTEME_0004 = "<HR><BIG>Tour de combat numéro {4} entre votre planète {2} du système {3} et la flotte {1} du commandant {0}</BIG><BR>";
	public static final String COMBAT_SYSTEME_0005 = "<HR><BIG>Tour de combat numéro {4} entre votre flotte {1} et la planète {2} du système {3} du commandant {0}</BIG><BR>";
	public static final String COMBAT_SYSTEME_0006 = "Combat entre votre flotte {0} et certaines planètes du système {1}. Vous avez pris {2} planète(s).";
	public static final String COMBAT_SYSTEME_0007 = "Combat entre la flotte {0} du commandant {2} et certaines de vos planètes du système {1}. Vous avez perdu {3} planète(s).";
	public static final String COMBAT_SYSTEME_0008 = "Votre flotte {0} n'a pu combattre car les planètes cibles appartiennent à des commandants présents dans vos alliances ou avec lesquels vous avez des pactes de non-agression.";
	public static final String COMBAT_SYSTEME_0009 = "Combat entre votre flotte {0} et certaines planètes du système {1}. Vous avez pillé {2} planète(s).";
	public static final String COMBAT_SYSTEME_0010 = "Votre flotte {0} vient de piller la planète {1} du système {2} appartenant au commandant {3}.";
	public static final String COMBAT_SYSTEME_0011 = "La flotte {0} du commandant {3} vient de piller votre planète {1} du système {2}.";
	public static final String COMBAT_SYSTEME_0012 = "Votre flotte {0} vient d'éradiquer la planète {1} du système {2} appartenant au commandant {3}.";
	public static final String COMBAT_SYSTEME_0013 = "La flotte {0} du commandant {3} vient d'éradiquer votre planète {1} du système {2}.";
	public static final String COMBAT_SYSTEME_0014 = "Votre flotte {0} n'a pu combattre car la planètes cible appartient à un commandant présent dans vos alliances ou avec lesquel vous avez un pacte de non-agression.";
	public static final String COMBAT_SYSTEME_0015 = "Votre flotte {0} n'a pu combattre car elle n'a pas la puissance minimale requise pour attaquer une planète.";

	public static final String ER_COMMANDANT_ALLIANCE_0000 = "Impossible de créer l'alliance {0}. Vous ne disposez que de {1} centaures en ce moment : ce n'est pas suffisant!";
	public static final String ER_COMMANDANT_ALLIANCE_0001 = "Impossible de créer l'alliance {0}. Vous êtes déjà membre du nombre maximal d'alliances permis.";
	public static final String ER_COMMANDANT_ALLIANCE_0002 = "Impossible d'adhérer à l'alliance {0}. Vous ne disposez que de {1} centaures en ce moment : ce n'est pas suffisant pour payer la taxe d'entrée!";
	public static final String ER_COMMANDANT_ALLIANCE_0003 = "Impossible d'adhérer à l'alliance {0}. Vous êtes déjà membre du nombre maximal d'alliances permis.";
	public static final String ER_COMMANDANT_ALLIANCE_0004 = "Impossible de créer l'alliance {0}. Vous êtes déjà dirigeant d'une autre alliance.";
	public static final String ER_COMMANDANT_ALLIANCE_0005 = "Impossible de quitter l'alliance {0}. Vous n'appartenez pas à cette alliance.";
	public static final String ER_COMMANDANT_ALLIANCE_0006 = "Vous ne pouvez renommer l'alliance {0} car vous ne la dirigez pas.";
	public static final String ER_COMMANDANT_ALLIANCE_0007 = "Vous ne pouvez changer l'adresse electronique de l'alliance {0} car vous ne la dirigez pas.";
	public static final String EV_COMMANDANT_ALLIANCE_0000 = "Vous venez de créer l'alliance {0}.";
	public static final String EV_COMMANDANT_ALLIANCE_0001 = "Vous venez d'adhérer à l'alliance {0} en aquittant la taxe d'entrée de {1} centaures.";
	public static final String EV_COMMANDANT_ALLIANCE_0002 = "Vous venez de quitter l'alliance {0}.";
	public static final String EV_COMMANDANT_ALLIANCE_0003 = "Votre adhésion à l'alliance {0} n'a pu s'effectuer car le dirigeant de cette alliance n'a pas valiDé votre adhésion.";
	public static final String EV_COMMANDANT_ALLIANCE_0004 = "Vous venez de voter en faveur du commandant {1} pour l'élire à la tête de l'alliance {0} .";
	public static final String EV_COMMANDANT_ALLIANCE_0005 = "Vous venez d'être exclu de l'alliance {0}.";
	public static final String EV_COMMANDANT_ALLIANCE_0006 = "Vous venez de voter l'exclusion du commandant {1} de l'alliance {0}.";

	public static final String PUBLIC_ALLIANCE_0000 = "L'alliance {0} vient d'être dissoute faute de membres.";
	public static final String PUBLIC_ACHETER_COMMANDANT_0000 = "{0} vient d'enrôler le lieutenant {1} pour la somme de {2} centaures.";
	public static final String PUBLIC_TECHNOLOGIE_0000 = "Boost technologique: {0} est Désormais accessible à tous!";

	public static final String EV_ALLIANCE_0000 = "Le commandant {0} vient d'adhérer à l'alliance.<BR>";
	public static final String EV_ALLIANCE_0001 = "Le commandant {0} vient de quitter l'alliance.<BR>";
	public static final String EV_ALLIANCE_0002 = "Votes en vue de l'élection d'un nouveau dirigeant : <BR>";
	public static final String EV_ALLIANCE_0003 = "Le commandant {0} vient d'être élu comme dirigeant de l'alliance {1} avec {2} voix sur {3} possibles.<BR>";
	public static final String EV_ALLIANCE_0004 = "Le commandant {0} vient d'être exclu de l'alliance.<BR>";
	public static final String EV_ALLIANCE_0005 = "Le commandant {0} vient d'être exclu de l'alliance par {1} voix {2} possibles.<BR>";
	public static final String EV_ALLIANCE_0006 = "L'exclusion du commandant {0} n'a recueilli que {1} voix sur {2} possibles et n'est donc pas effective.<BR>";
	public static final String EV_ALLIANCE_0007 = "L'alliance est renommé par son dirigeant avec pour nouveau nom {0}.<BR>";
	public static final String EV_ALLIANCE_0008 = "Le site de l'alliance a comme nouvelle adresse {0}.<BR>";
	public static final String EV_ALLIANCE_0009 = "Le commandant {0} n'a pu être élu car il est déjà dirigeant d'une alliance. L'élection est donc annulée.<BR>";
	public static final String EV_ALLIANCE_0010 = "Le commandant {0} n'a pu être élu comme dirigeant de l'alliance {1} car il n'a recueilli que {2} voix sur {3} possibles.<BR>";

	public static final String EV_COMMANDANT_PACTE_0000 = "{0} vient de rompre son pacte de non-agression avec vous.";
	public static final String EV_COMMANDANT_PACTE_0001 = "Vous venez de rompre le pacte de non-agression avec le commandant {0}.";
	public static final String EV_COMMANDANT_PACTE_0002 = "Vous venez de signer un pacte de non-agression avec le commandant numéro {0}.";
	public static final String EV_COMMANDANT_PACTE_0003 = "Vous n'avez pas pu signer un pacte de non-agression avec le commandant numéro {0} car il ne l'a pas demanDé de son coté.";

	public static final String ER_COMMANDANT_DON_CENTAURES_0000 = "Vous n'avez pas pu transmettre {0} centaures au commandant numéro {1} : vous n'êtes pas assez riche en ce moment!";
	public static final String EV_COMMANDANT_DON_CENTAURES_0000 = "Le commandant {0} a transmis {2} centaures au commandant {1} cette semaine.";
	public static final String EV_COMMANDANT_DON_CENTAURES_0001 = "Dans votre insondable bonté, vous avez transmis {1} centaures au commandant {0}.";
	public static final String EV_COMMANDANT_DON_CENTAURES_0002 = "Vous avez recu {0} centaures d'un bienfaiteur généreux qui souhaite rester anonyme.";
	public static final String EV_COMMANDANT_DON_CENTAURES_0003 = "Le commandant {0} vous as transmis {1} centaures.";

	public static final String ER_COMMANDANT_DON_TECHNOLOGIE_0000 = "Impossible de transmettre la technologie {0} au commandant numéro {1} : vous ne disposez pas d'assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_DON_TECHNOLOGIE_0001 = "Impossible de transmettre la technologie {0} au commandant numéro {1} : ce commandant connaît déjà cette technologie!";
	public static final String EV_COMMANDANT_DON_TECHNOLOGIE_0000 = "Dans votre insondable bonté, vous avez transmis la technologie {1} au commandant {0}.";
	public static final String EV_COMMANDANT_DON_TECHNOLOGIE_0001 = "Le commandant {0} vous as transmis la technologie {1}.";
	public static final String EV_COMMANDANT_DON_TECHNOLOGIE_0002 = "Le commandant {0} as transmis la technologie {2} au commandant {1}.";
	public static final String EV_COMMANDANT_DON_TECHNOLOGIE_0003 = "Un commandant qui souhaite garder l'anonymat vous as transmis la technologie {0}.";
	public static final String ER_COMMANDANT_DON_TECHNOLOGIE_0002 = "Impossible de transmettre la technologie {0} au commandant numéro {1} : il ne possède pas les prérequis à cette technologie.";

	public static final String EV_COMMANDANT_ELIMINER_TECHNOLOGIE_0000 = "Vous venez d'abandonner toute connaissance sur la technologie {0}.";

	public static final String ER_COMMANDANT_DON_FLOTTE_0000 = "Impossible de transmettre votre flotte {0} au commandant numéro {1} : vous ne disposez pas d'assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_DON_FLOTTE_0001 = "Impossible de donner la flotte numéro {0} au commandant numéro {1} : le flotte contient des vaisseaux en location.";
	public static final String EV_COMMANDANT_DON_FLOTTE_0000 = "Dans votre insondable bonté, vous avez loué votre flotte numéro {1} au commandant {0} pour {2} tours.";
	public static final String EV_COMMANDANT_DON_FLOTTE_0001 = "Le commandant {0} vous a loué sa flotte numéro {1} pour {2} tours.";
	public static final String EV_COMMANDANT_DON_FLOTTE_0002 = "Le commandant qui souhaite garder l'anonymat vous a loué sa flotte numéro {0} pour {1} tours.";
	public static final String EV_COMMANDANT_DON_FLOTTE_0003 = "Le commandant {0} a loué une de ses flottes au commandant {1} cette semaine.";

	public static final String ER_COMMANDANT_VENTE_FLOTTE_0000 = "Impossible de vendre votre flotte {0} au commandant numéro {1} : ce commandant ne dispose pas d'assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_VENTE_FLOTTE_0003 = "Impossible de vendre la flotte numéro {0} au commandant numéro {1} : le flotte contient des vaisseaux en location.";

	public static final String EV_COMMANDANT_VENTE_FLOTTE_0000 = "Dans votre insondable bonté, vous avez vendu votre flotte numéro {1} au commandant {0}.";
	public static final String EV_COMMANDANT_VENTE_FLOTTE_0001 = "Le commandant {0} vous a vendu sa flotte {1} numéro {3} pour {2} centaures.";
	public static final String EV_COMMANDANT_VENTE_FLOTTE_0003 = "Le commandant {0} a vendu une de ses flottes au commandant {1} cette semaine.";

	public static final String ER_COMMANDANT_DON_SYSTEME_0000 = "Impossible de transmettre le systeme {0} au commandant numéro {1} : vous ne disposez pas d'assez de centaures en ce moment.";
	public static final String EV_COMMANDANT_DON_SYSTEME_0000 = "Dans votre insondable bonté, vous avez transmis le système {1} au commandant {0}.";
	public static final String EV_COMMANDANT_DON_SYSTEME_0001 = "Le commandant {0} vous as transmis le système {1}.";
	public static final String EV_COMMANDANT_DON_SYSTEME_0002 = "Le commandant {0} as transmis les planètes de son système {2} au commandant {1} cette semaine.";
	public static final String EV_COMMANDANT_DON_SYSTEME_0003 = "Un commandant qui souhaite garder l'anonymat vous as transmis le système {0}.";

	public static final String ER_COMMANDANT_DON_PLANETE_0000 = "Impossible de transmettre la planète {1} du systeme {0} au commandant numéro {2} : vous ne disposez pas d'assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_DON_PLANETE_0001 = "Impossible de transmettre la planète numéro {2} du systeme {0} au commandant numéro {1} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_DON_PLANETE_0002 = "Impossible de donner la planète {1} du système {0} : cette planète ne vous appartient pas.";
	public static final String EV_COMMANDANT_DON_PLANETE_0000 = "Dans votre insondable bonté, vous avez transmis la planète {2} du système {1} au commandant {0}.";
	public static final String EV_COMMANDANT_DON_PLANETE_0001 = "Le commandant {0} vous as transmis la planète {2} du système {1}.";
	public static final String EV_COMMANDANT_DON_PLANETE_0002 = "Le commandant {0} as transmis une planète du système {2} au commandant {1} cette semaine.";
	public static final String EV_COMMANDANT_DON_PLANETE_0003 = "Un commandant qui souhaite garder l'anonymat vous as transmis la planète {1} du système {0}.";

	public static final String ER_COMMANDANT_MISE_EN_CHANTIER_0000 = "Impossible de mettre en chantier une construction sur la planète numéro {1} du système {0}: cette planète n'existe pas.";
	public static final String ER_COMMANDANT_MISE_EN_CHANTIER_0001 = "Impossible de mettre en chantier une construction le système {0}: vous ne pouvez construire {2} construction(s) de type {1}";
	public static final String EV_COMMANDANT_MISE_EN_CHANTIER_0000 = "Prérogatives: {2} construction(s) de type {1} sur le système {0}.";

	public static final String EV_COMMANDANT_ANNULER_CONSTRUCTION_0000 = "Vous venez d'annuler tous vos projets de constructions en cours sur le système {0}.";

	public static final String EV_COMMANDANT_PROGRAMMER_CONSTRUCTION_0000 = "Vous venez de lancer un programme de construction de type {1} sur le système {0}.";

	public static final String EV_COMMANDANT_ANNULER_PROGRAMME_CONSTRUCTION_0000 = "Vous venez d'annuler votre programme de construction sur le système {0}.";

	public static final String EV_COMMANDANT_MODIFIER_POLITIQUE_0000 = "Changement de politique sur votre système {0}; Désormais {1} est en vigueur.";

	public static final String ER_COMMANDANT_MODIFIER_BUDGET_0000 = "Ordre annulé.Impossible de modifier le budget du système {0} : le total des affectations du nouveau budget est supérieur à 100.";
	public static final String EV_COMMANDANT_MODIFIER_BUDGET_0000 = "{0} est doté d'une nouvelle répartition pour ses impôts.";

	public static final String ER_COMMANDANT_MODIFIER_TAXATION_0000 = "Impossible de modifier la taxation sur la planète numéro {1} du système {0} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_MODIFIER_TAXATION_0001 = "Impossible de modifier la taxation de la planète {1} du système {0} : cette planète ne vous appartient pas.";
	public static final String EV_COMMANDANT_MODIFIER_TAXATION_0000 = "Une taxation {1} est dorénavant présente sur votre système {0}.";
	public static final String EV_COMMANDANT_MODIFIER_TAXATION_0001 = "Un nouvelle taxation de niveau {2} est en vigueur sur la planète {1} de votre système {0}.";

	public static final String ER_COMMANDANT_TERRAFORMER_0000 = "Impossible de terraformer le système {0} : vous n'avez pas assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_TERRAFORMER_0001 = "Impossible de terraformer la planète {1} du système {0} : vous n'avez pas assez de centaures en ce moment.";
	public static final String ER_COMMANDANT_TERRAFORMER_0002 = "Impossible de terraformer la planète numéro {1} du système {0} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_TERRAFORMER_0003 = "Impossible de terraformer la planète {1} du système {0} : cette planète ne vous appartient pas.";
	public static final String ER_COMMANDANT_TERRAFORMER_0004 = "Impossible de terraformer le système {0} : Il l'a déjà été ce tour-ci.";
	public static final String ER_COMMANDANT_TERRAFORMER_0005 = "Impossible de terraformer la planète {1} du système {0} : elle l'a déjà été ce tour-ci.";
	public static final String EV_COMMANDANT_TERRAFORMER_0000 = "Toutes vos planètes de votre système {0} ont été terraformées pour un coût total de {1} centaures.";
	public static final String EV_COMMANDANT_TERRAFORMER_0001 = "Votre planète {1} de votre système {0} a été terraformée pour un coût total de {2} centaures.";

	public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0000 = "Impossible de détruire des {1} sur le système {0} : Ce type de bâtiment n'est pas présent sur ce système.";
	public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0001 = "Impossible de détruire des {1} sur la planète {2} du système {0} : Ce type de bâtiment n'est pas présent sur cette planète.";
	public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0002 = "Impossible de détruire des bâtiments sur la planète numéro {1} du système {0} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_DETRUIRE_BATIMENT_0003 = "Impossible de détruire des bâtiments sur la planète {1} du système {0} : cette planète ne vous appartient pas.";
	public static final String EV_COMMANDANT_DETRUIRE_BATIMENT_0000 = "{2} {1} ont été recyclés sur le système {0} conformêment à vos ordres.";
	public static final String EV_COMMANDANT_DETRUIRE_BATIMENT_0001 = "Seuls {2} {1} sur {3} demanDés ont pu être recyclés sur le système {0}.";
	public static final String EV_COMMANDANT_DETRUIRE_BATIMENT_0002 = "{3} {1} ont été recyclés sur la planète {2} du système {0} conformêment à vos ordres.";
	public static final String EV_COMMANDANT_DETRUIRE_BATIMENT_0003 = "Seuls {3} {1} sur {4} demanDés ont pu être recyclés sur la planète {2} du système {0}.";

	public static final String ER_COMMANDANT_AFFECTER_RECHERCHE_0000 = "Vous ne pouvez mettre en place votre nouveau plan de recherche : il est impossible de chercher plus de trois technologies en même temps!";
	public static final String ER_COMMANDANT_AFFECTER_RECHERCHE_0001 = "Votre nouveau plan de recherche est annulé car le total des affectations Dépasse les 100% !";
	public static final String EV_COMMANDANT_AFFECTER_RECHERCHE_0000 = "Vous venez de mettre en place la recherche de la technologie {0} en y affectant {1}% de votre capacité scientifique.";

	public static final String ER_COMMANDANT_MISSION_SPECIALE_0000 = "Impossible pour vos services secrets d'effectuer une mission spéciale à la position {0} : il n'y a pas de système ici.";
	public static final String ER_COMMANDANT_MISSION_SPECIALE_0001 = "Impossible pour vos services secrets d'effectuer une mission spéciale sur la planète numéro {1} du système {0} : cette planète n'existe pas.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0000 = "Vos services secrets viennent d'échouer dans leur mission {2} sur la planète {1} du système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0001 = "Un rapport de vos services de contre-espionnage : les services secrets du commandant {3} viennent d'échouer dans leur mission {2} sur votre planète {1} du système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0002 = "Vos services secrets vous informent que le commandant {0} ne possède pas de technologie inconnues de vos scientifiques ( Possiblité non exclue de trop faibles moyens employés).";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0003 = "Vos services secrets vous informent qu'ils viennent de se procurer la technologie {0}, en &quot;s'inspirant&quot; des connaissances du commandant {1}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0004 = "Les services secrets du commandant {1} viennent de vous voler la technologie {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0005 = "Vos services secrets viennent de vous faire parvenir un rapport sur le système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0006 = "Vos services secrets viennent de saboter et de détruire tous les bâtiments de la planète {1} du système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0007 = "Vos services de contre-espionnage vous informent que les services secrets du commandant {2} viennent de saboter et de détruire tous les bâtiments de votre planète {1} du système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0008 = "Vos services de contre-espionnage vous informent qu'un service secret dont ils n'ont pasréussi à Déterminer l'identité vient de saboter et de détruire tous les bâtiments de votre planète {1} du système {0}.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0009 = "Vos services secrets viennent d'effectuer une mission de propagande sur votre planète {1} du système {0}, ce qui augmente de {2} sa stabilité.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0010 = "Vos services secrets viennent d'effectuer une mission de propagande sur la planète {1} du système {0}, ce qui diminue de {2} sa stabilité.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0011 = "Vos services de contre-espionnage vous informent que les services secrets du commandant {2} viennent d'effectuer une mission de propagande sur votre planète {1} du système {0}, ce qui diminue de {3} sa stabilité.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0012 = "Vos services de contre-espionnage vous informent qu'un service secret dont ils n'ont pasréussi à Déterminer l'identité vient d'effectuer une mission de propagande sur votre planète {1} du système {0}, ce qui diminue de {2} sa stabilité.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0013 = "La planète {1} du système {0} vient de serévolter et Décide de se mettre sous votre protection.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0014 = "Ta pas honte ?? sur le MJ en plus .... bah essaye toujours: t'as le droit à 3 essai par tour.";
	public static final String EV_COMMANDANT_MISSION_SPECIALE_0015 = "La cours de justice de [Dune] a intercepté un vol techno du commandant {0} contre {1}.";

	public static final String EV_COMMANDANT_DEPLACER_FLOTTE_0000 = "Votre flotte {0} se Déplace vers {1} en directive {2} sans stratégie particulière.";

	public static final String EV_COMMANDANT_DEPLACER_FLOTTE_0001 = "Votre flotte {0} se Déplace vers {1} en directive {2} avec la stratégie: {3}.";

	public static final String ER_COMMANDANT_CHARGER_CARGO_0001 = "Impossible de charger quelque chose dans le vaisseau cargo numéro {1} de la flotte {0} : ce vaisseau cargo n'existe pas.";
	public static final String ER_COMMANDANT_CHARGER_CARGO_0002 = "Impossible de charger quelque chose appartenant au commandant {1} à partir du système {0}  : ce commandant ne possède pas de planètes sur ce système.";
	public static final String ER_COMMANDANT_CHARGER_CARGO_0003 = "Impossible de charger avec votre flotte {0} autre chose que des marchandises dans un système étranger.";
	public static final String ER_COMMANDANT_CHARGER_CARGO_0004 = "Impossible de charger avec votre flotte {0} : le propriétaire du poste ou du système n'est pas précis.";
	public static final String ER_COMMANDANT_CHARGER_CARGO_0005 = "Impossible de charger un cargo à partir de la planète numéro {1} du système {0} : elle n'existe pas.";
	public static final String EV_COMMANDANT_CHARGER_CARGO_0000 = "Vous venez de charger {3} {1} à partir du système {0} dans votre flotte {2}.";
	public static final String EV_COMMANDANT_CHARGER_CARGO_0001 = "Vous n'avez pas pu charger un seul {1} demanDé à partir du système {0} dans votre flotte {2}, soit parce que votre flotte n'a pas de cargo assez grand, soit parce qu'il n'y avait pas ce que vous demandiez sur ce système.";

	public static final String ER_COMMANDANT_DECHARGER_CARGO_0000 = "Impossible de décharger quelque chose sur la planète {1} du système {0} appartenant au commandant {2} : cette planète n'appartient pas à ce commandant.";
	public static final String ER_COMMANDANT_DECHARGER_CARGO_0001 = "Impossible de décharger quelque chose à partir du vaisseau cargo numéro {1} de la flotte {0} : ce vaisseau cargo n'existe pas.";
	public static final String ER_COMMANDANT_DECHARGER_CARGO_0002 = "Impossible de décharger quelque chose sur une planète du commandant {1} sur le système {0}  : ce commandant ne possède pas de planètes sur ce système.";
	public static final String ER_COMMANDANT_DECHARGER_CARGO_0003 = "Impossible de décharger {3} {1} avec la flotte {2} sur le système {0}  : cette flotte ne possède pas de chargement de ce type.";
	public static final String ER_COMMANDANT_DECHARGER_CARGO_0004 = "Impossible de décharger avec votre flotte {0} : le propriétaire du poste ou du système n'est pas précisé.";
	public static final String ER_COMMANDANT_DECHARGER_CARGO_0005 = "Impossible de décharger un cargo sur la planète numéro {1} du système {0} : elle n'existe pas.";
	public static final String EV_COMMANDANT_DECHARGER_CARGO_0000 = "Vous venez de décharger {3} {1} dans le système {0} à partir de votre flotte {2}.";
	public static final String EV_COMMANDANT_DECHARGER_CARGO_0001 = "Vous n'avez pas pu décharger un seul {1} demanDé à partir de votre flotte {2}, parce que votre flotte ne contient pas ce que vous avez demanDé de Décharger.";
	public static final String EV_COMMANDANT_DECHARGER_CARGO_0002 = "Le commandant {3} vient de décharger {4} {1} dans votre système {0} à partir de sa flotte {2}.";

	public static final String ER_COMMANDANT_VENTE_MARCHANDISE_0000 = "Vous ne pouvez acheter {2} marchandise(s) de type {0} sur le système {1} : vos fonds sont insuffisants.";
	public static final String ER_COMMANDANT_VENTE_MARCHANDISE_0001 = "Vous ne pouvez acheter {3} marchandise(s) de type {0} au commandant {2} sur le système {1} : cette marchandise n'est pas présente dans son poste commercial.";
	public static final String ER_COMMANDANT_VENTE_MARCHANDISE_0002 = "Vous ne pouvez acheter {3} marchandise(s) de type {0} au commandant {2} sur le système {1} : Il est impossible d'acheter une marchandise que l'on a préalablement vendu dans un même poste la même semaine.";
	public static final String EV_COMMANDANT_VENTE_MARCHANDISE_0000 = "Le commandant {0} vient d'acheter {3} marchandise(s) de type {1} dans votre poste commercial du système {2}. Vos gains: {4} centaures (taxes comprises).";
	public static final String EV_COMMANDANT_VENTE_MARCHANDISE_0001 = "Vous venez d'acheter dans le poste commercial du système {2} appartenant au commandant {0}, {3} marchandise(s) de type {1}. Coût: {4} centaures (taxes comprises).";
	public static final String EV_COMMANDANT_VENTE_MARCHANDISE_0002 = "Vous venez de charger à partir de votre poste commercial du système {1} {2} marchandise(s) de type {0}.";

	public static final String ER_COMMANDANT_ACHAT_MARCHANDISE_0000 = "Vous ne pouvez vendre {2} marchandise(s) de type {0} sur le système {1} : la Banque Galactique bloque la transaction, les fonds du commandant acheteur étant insuffisants.";
	public static final String EV_COMMANDANT_ACHAT_MARCHANDISE_0000 = "Le commandant {0} vient de vendre {3} marchandise(s) de type {1} dans votre poste commercial du système {2}. Vous devez Débourser: {4} centaures (taxes comprises).";
	public static final String EV_COMMANDANT_ACHAT_MARCHANDISE_0001 = "Vous venez de vendre {3} marchandise(s) de type {1} dans le poste commercial du système {2} appartenant au commandant {0}. Vos gains: {4} centaures (taxes comprises).";
	public static final String EV_COMMANDANT_ACHAT_MARCHANDISE_0002 = "Vous venez de Décharger dans votre poste commercial du système {1} {2} marchandise(s) de type {0}.";

	public static final String ER_COMMANDANT_DIVISER_FLOTTE_0000 = "Impossible de diviser la flotte numéro {0} : elle n'existe pas.";
	public static final String EV_COMMANDANT_DIVISER_FLOTTE_0000 = "Vous venez de diviser votre flotte {0} pour donner la flotte {1}.";

	public static final String ER_COMMANDANT_FUSIONNER_FLOTTE_0000 = "Problème de fusion pour les flotte {0} et {1} : la flotte numéro {0} n'existe pas.";
	public static final String ER_COMMANDANT_FUSIONNER_FLOTTE_0001 = "Impossible de fusionner la flotte numéro {0} et la flotte numéro {1} : elles ont le même numéro!";
	public static final String ER_COMMANDANT_FUSIONNER_FLOTTE_0002 = "La flotte {0} et la flotte {1} ne peuvent fusionner car elles ne se trouvent pas au même endroit!";
	public static final String EV_COMMANDANT_FUSIONNER_FLOTTE_0000 = "Vous venez de fusionner votre flotte {0} et votre flotte {1}.";

	public static final String ER_COMMANDANT_PISTER_FLOTTE_0000 = "System failure, echec du pistage: la flotte numéro {1} du commandant {0} est hors d'atteinte. ";
	public static final String EV_COMMANDANT_PISTER_FLOTTE_0000 = "Votre flotte {0} vient de pister avec succés la flotte numéro {2} du commandant {1}.";
	public static final String EV_COMMANDANT_PISTER_FLOTTE_0001 = "Votre flotte {0} vient d'échouer dans sa mission de pistage de la flotte numéro {2} du commandant {1}.";

	public static final String EV_COMMANDANT_LANCER_MINES_0000 = "Mines larguées: {0} vient de miner la case où elle se trouve par {1} mines anti-matière.";

	public static final String ER_COMMANDANT_COLONISER_PLANETE_0000 = "{1} ne peut coloniser la planète {2} de {0} car cette planète ne vous appartient pas.";
	public static final String ER_COMMANDANT_COLONISER_PLANETE_0001 = "Impossible de coloniser la planète {2} du système {0} avec la flotte numéro {1} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_COLONISER_PLANETE_0002 = "Impossible de coloniser la planète {2} du système {0} avec la flotte numéro {1} : Il n'y a pas assez de place pour leur developpement.";

    public static final String EV_COMMANDANT_COLONISER_PLANETE_0000 = "Des {3} de {1} viennent de coloniser la planète {2} de {0}.";
	public static final String EV_COMMANDANT_COLONISER_PLANETE_0001 = "Votre flotte {1} n'a pu coloniser la planète {2} du système {0} pour les {3} : la planète est inhabitable pour eux ou est déjà colonisée.";
    public static final String EV_COMMANDANT_COLONISER_PLANETE_0002 = "Des {3} de {1} viennent de coloniser la planète {2} de {0}. Suite au génocide de la population présente, la stabilité a été réduite de 10 points et votre réputation de 300 points";

	public static final String EV_COMMANDANT_AFFECTER_HEROS_0000 = "Votre héros {0} vient d'être nommé sur votre flotte {1}.";

	public static final String EV_COMMANDANT_AFFECTER_GOUVERNEUR_0000 = "Votre gouverneur {0} vient d'être affecté sur votre système {1}.";

	public static final String EV_COMMANDANT_LICENCIER_LIEUTENANT_0000 = "Licensiement: Vous venez de vous séparer de votre lieutenant {0}, ce qui vous a rapporté la somme de {1} centaures.";

	public static final String ER_COMMANDANT_ACHETER_LIEUTENANT_0000 = "Impossible d'acheter le lieutenant {0}, vous n'avez pas assez de centaures actuellement!";
	public static final String EV_COMMANDANT_ACHETER_LIEUTENANT_0000 = "Le lieutenant {0} est maintenant sous vos ordres, ce qui vous a couté la somme de {1} centaures.";
	public static final String EV_COMMANDANT_ACHETER_LIEUTENANT_0001 = "Vous n'avez pas pu acheter le lieutenant {0}, pour la somme de {1} centaures, car ce lieutenant demande plus de centaures.";

	public static final String ER_COMMANDANT_RENOMMER_LIEUTENANT_0000 = "Impossible de donner le nom {1} au lieutenant {0} : vous possèdez déjà un lieutenant ayant ce nom.";

	public static final String ER_COMMANDANT_CHANGER_CAPITALE_0000 = "Impossible de changer la capitale en {0} : vous n'avez pas assez de centaures actuellement.";
	public static final String EV_COMMANDANT_CHANGER_CAPITALE_0000 = "Vous venez de Déplacer votre capitale en {0}.";

	public static final String ER_COMMANDANT_DON_PLAN_0000 = "Impossible de donner le plan du vaisseau {1} au commandant {0} : ce commandant possède déjà ce plan!";
	public static final String ER_COMMANDANT_DON_PLAN_0001 = "Impossible de donner le plan du vaisseau {1} au commandant {0} : vous n'avez pas assez de centaures actuellement!";
	public static final String EV_COMMANDANT_DON_PLAN_0000 = "Vous venez de donner le plan du vaisseau {1} au commandant {0}.";
	public static final String EV_COMMANDANT_DON_PLAN_0001 = "Le commandant {0} vient de vous transmettre le plan du vaisseau {1}.";
	public static final String EV_COMMANDANT_DON_PLAN_0002 = "Le commandant qui souhaite rester anonyme vient de vous transmettre le plan du vaisseau {0}.";
	public static final String EV_COMMANDANT_DON_PLAN_0003 = "Le commandant {0} vient de transmettre un plan de vaisseau au commandant {1}.";

	public static final String ER_COMMANDANT_CREER_PLAN_0000 = "Impossible de créer le plan du vaisseau de nom {0} : Vous n'avez pas assez de centaures en ce moment!";
	public static final String EV_COMMANDANT_CREER_PLAN_0000 = "Design: Vous venez de créer le plan du vaisseau de nom {0}.";
	public static final String EV_COMMANDANT_CREER_PLAN_0001 = "Vous venez de supprimer votre plan de vaisseau {0}.";

	public static final String ER_COMMANDANT_CREER_STRATEGIE_0000 = "Impossible de créer la stratégie de nom {0} : Vous avez déjà une stratégie de ce nom!";
	public static final String EV_COMMANDANT_CREER_STRATEGIE_0000 = "Vous venez de créer la stratégie de combat de nom {0}.";

	public static final String ER_COMMANDANT_DON_STRATEGIE_0000 = "Impossible de donner la stratégie de combat {1} au commandant {0} : ce commandant possède déjà une stratégie de ce nom!";
	public static final String ER_COMMANDANT_DON_STRATEGIE_0001 = "Impossible de donner la stratégie de combat {1} au commandant {0} : vous n'avez pas assez de centaures actuellement!";
	public static final String EV_COMMANDANT_DON_STRATEGIE_0000 = "Vous venez de donner la stratégie de combat {1} au commandant {0}.";
	public static final String EV_COMMANDANT_DON_STRATEGIE_0001 = "Le commandant {0} vient de vous transmettre la stratégie de combat {1}.";
	public static final String EV_COMMANDANT_DON_STRATEGIE_0002 = "Le commandant qui souhaite rester anonyme vient de vous transmettre la stratégie de combat {0}.";
	public static final String EV_COMMANDANT_DON_STRATEGIE_0003 = "{0} a transmi une stratégie de combat au commandant {1}.";

	public static final String ER_COMMANDANT_RENOMMER_SYSTEME_0000 = "Impossible de renommer le système {0} : Il estnécessaire que toutes les planètes de ce système vous appartiennent.";
	public static final String EV_COMMANDANT_RENOMMER_SYSTEME_0000 = "Vous venez de renommer votre système {0}.";

	public static final String ER_COMMANDANT_RENOMMER_PLANETE_0000 = "Impossible de renommer la planète numéro {1} du système {0} : cette planète n'existe pas.";
	public static final String ER_COMMANDANT_RENOMMER_PLANETE_0001 = "Impossible de renommer la planète {1} du système {0} : cette planète ne vous appartient pas.";
	public static final String EV_COMMANDANT_RENOMMER_PLANETE_0000 = "Vous venez de renommer votre planète {1} du système {0} : son nouveau nom est {2}.";

	public static final String ER_COMMANDANT_CONSTRUCTION_FLOTTE_0000 = "Impossible de construire quelque chose avec votre flotte numéro {0} : cette flotte n'existe pas.";
	public static final String EV_COMMANDANT_CONSTRUCTION_FLOTTE_0000 = "Vous venez d'assigner à votre flotte numéro {1} un programme de construction de vaisseaux de type {0}.";

	public static final String ER_COMMANDANT_RENOMMER_FLOTTE_0000 = "Impossible de renommer votre flotte numéro {0} : cette flotte n'existe pas.";
	public static final String EV_COMMANDANT_RENOMMER_FLOTTE_0000 = "Vous venez de renommer votre flotte numéro {1} avec pour nouveau nom {0}.";

	public static final String ER_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000 = "Votre flotte numéro {0} ne peut changer de galaxie : elle ne possède pas la capacité intergalactique...";
	public static final String EV_COMMANDANT_UTILISER_PORTE_GALACTIQUE_0000 = "Votre flotte numéro {0} vient de changer de galaxie.";

	public static final String ER_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000 = "Votre flotte numéro {0} ne peut utiliser une porte intragalactique : vous ne possèdez pas assez de centaures";
	public static final String EV_COMMANDANT_UTILISER_PORTE_INTRAGALACTIQUE_0000 = "Votre flotte numéro {0} vient d'utiliser une porte intragalactique.";

	public static final String EV_COMMANDANT_TAUX_POSTE_0000 = "Vous venez de fixer le taux de taxation de vos postes commerciaux à {0}%.";

	public static final String EV_COMMANDANT_FIN_DE_TOUR_0000 = "Vos services d'Intendance vous signalent {1} planète(s) en révolte dans votre système {0}.";

	public static final String EV_COMMANDANT_RECHERCHE_0000 = "Vous venez de découvrir la technologie: {0}.";
	public static final String EV_COMMANDANT_RECHERCHE_0001 = "La technologie {0} étant devenu publique, votre recherche de cette technologie est annulé.";

	public static final String EV_COMMANDANT_CONSTRUCTION_0000 = "Votre système {0} ne peut réaliser ce tour-ci le projet de construction de {2} construction(s) de type {1} car vous n'avez pas assez de centaures.";
	public static final String EV_COMMANDANT_CONSTRUCTION_0001 = "Votre système {0} vient de produire {2} construction(s) de type {1}.";
	public static final String EV_COMMANDANT_CONSTRUCTION_0002 = "Votre système {0} n'a construit que {1} {2} en raison d'un manque de {3}";
	public static final String EV_COMMANDANT_CONSTRUCTION_0003 = "Votre système {0} n'a construit aucun des {1} {2} en raison d'un manque de {3}";

	public static final String EV_COMMANDANT_CONSTRUCTION_0005 = "Votre flotte {0} vient de produire {2} vaisseau(x) de type {1}. Ces vaisseaux sont maintenant dans cette flotte";
	public static final String EV_COMMANDANT_CONSTRUCTION_0006 = "Votre flotte {0} ne peut produire des constructions de type {1} : vous n'avez pas assez de centaures en ce moment.";

	public static final String EV_COMMANDANT_CONSTRUCTION_0007 = "Votre système {0} ne peut réaliser ce tour-ci le projet de construction de {2} construction(s) de type {1} car ce système ne possède pas le chantier naval nécessaire à la production de vaisseaux.";
	public static final String EV_COMMANDANT_CONSTRUCTION_0008 = "Votre flotte {0} vient de produire {3} vaisseau(x) de type {1} et {4} vaisseau(x) de type {2}. Ces vaisseaux sont maintenant dans cette flotte";
	public static final String EV_COMMANDANT_CONSTRUCTION_0009 = "Certains vaisseaux n'ont pas pu être construit par manque de centaures.";
	public static final String EV_COMMANDANT_CONSTRUCTION_00010 = "Rapports des amiraux de votre flotte {1}: Commandant, notre flotte a produit {3} vaisseaux  fictifs de type {2}, ils sont désormais dans votre jeu Starwars...";

	public static final String EV_COMMANDANT_GESTION_FLOTTE_0000 = "Votre flotte {0} ne peut être réparée ce tour-ci car vous n'avez pas assez de centaures.";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0001 = "Votre flotte {0} vient d'être réparée par votre système {1} de {2} points de dommages.";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0002 = "Votre flotte {0} vient d'être réparée par le système {1} de votre allié le commandant {2} de {3} points de dommages.";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0003 = "Votre système {1} vient de réparer la flotte {0} de votre allié le commandant {2} pour {3} points de dommages.";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0004 = "Votre flotte {0} ne peut être réparée ce tour-ci car le commandant {1} n'a pas assez de centaures.";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0005 = "Votre flotte {0} vient de Détecter en {1} une ancienne porte intra-galactique. Vers ou mène elle? Mystère. Par contre, le passage est visiblement payant...";
	public static final String EV_COMMANDANT_GESTION_FLOTTE_0006 = "Argg!!Commandant, un chasseur de {0} vient de s'écraser sur son binome en {1} .. L'incident a causé {2} dommages..";

	public static final String EV_DEBRIS_COLLISION_0000 = "Votre flotte {0} vient de subir {1} point(s) de dommage suite à une collision avec des mines anti-matières.";
	public static final String EV_DEBRIS_COLLISION_0001 = "Votre flotte {0} vient d'être détruite suite aux dommages recus.";
	public static final String EV_DEBRIS_COLLISION_0002 = "Votre flotte {0} vous signale la présence de mines anti-matières sur la case où elle se trouve. Heureusement, aucun dommage n'est à Déplorer.";
	public static final String EV_DEBRIS_COLLISION_0003 = "Votre flotte {0} vient de Désactiver {1} mine(s) anti-matière(s) sur la case où elle se trouve.";
	public static final String EV_DEBRIS_COLLISION_0004 = "Votre flotte {0} vient de récupérer {1} tonne(s) de debris de vaisseaux d'une ancienne bataille sur la case où elle se trouve. Cela diminue le danger de naviguer à cet endroit!";
	public static final String EV_DEBRIS_COLLISION_0005 = "Votre flotte {0} vient de subir {1} point(s) de dommage suite à une collision avec des débris de vaisseaux d'une ancienne bataille.";
	public static final String EV_DEBRIS_COLLISION_0006 = "Votre flotte {0} vous signale la présence de débris de vaisseaux d'une ancienne bataille sur la case où elle se trouve. Heureusement, aucun dommage n'est à Déplorer.";
	public static final String EV_DEBRIS_COLLISION_0007 = "Evaluation du danger : {0} (A partir de 500, le niveau de risque commence à devenir important).";

	public static final String EV_TECHNO_RABAIS_0000 = "Vous avez récupéré {0} centaures que vous avez perdu dans votre plan de recherche mal optimisé.";

	public static final String ER_PLAN_DE_VAISSEAU_0000 = "Impossible de produire le plan du vaisseau de nom {0}, ce nom de vaisseau existe déjà.";
	public static final String ER_PLAN_DE_VAISSEAU_0001 = "Composant non-existant. Impossible de produire le plan de vaisseau {0} avec le composant de code {1}.";
	public static final String ER_PLAN_DE_VAISSEAU_0002 = "Impossible de produire le plan de vaisseau {0}. Le plan de vaisseau comporte plus de 10 boucliers.";
	public static final String ER_PLAN_DE_VAISSEAU_0003 = "Impossible de produire le plan de vaisseau {0}. Le plan de vaisseau comporte plus d'un module de construction.";
	public static final String ER_PLAN_DE_VAISSEAU_0004 = "Impossible de produire le plan de vaisseau {0}: la structure n'est pas fiable ( nb de compo<1). PS: coques vides interdites !";

	public static final String ER_STRATEGIE_COMBAT_0000 = "Impossible de spécifier le type de vaisseau {0} lors de la création de la stratégie de combat. Les différentes cibles ne sont pas correctementréparties en ce qui concerne le vaisseau {0}.";

	public static final String ORDRE_adherer_alliance = "Adherer à l'alliance numéro {0}.";
	public static final String ORDRE_valider_adhesion_alliance = "Valdier l'adhesion du commandant numéro {0}.";
	public static final String ORDRE_nommer_dirigeant = "Voter pour nommer le dirigeant numéro {1} à la tête de l'alliance numéro {0}.";
	public static final String ORDRE_exclure_alliance = "Exclure de l'alliance numéro {0} le dirigeant numéro {1}.";
	public static final String ORDRE_quitter_alliance = "Quitter l'alliance numéro {0}.";
	public static final String ORDRE_signer_pacte = "Signer un pacte avec le commandant numéro {0}.";
	public static final String ORDRE_rompre_pacte = "Rompre un pacte avec le commandant numéro {0}.";
	public static final String ORDRE_affecter_heros = "Affecter le héros {0} sur la flotte numéro {1}.";
	public static final String ORDRE_affecter_gouverneur = "Affecter le gouverneur {0} sur le système {1}.";
	public static final String ORDRE_liberer_lieutenant = "Se séparer du lieutenant {0}.";
	public static final String ORDRE_enroler_lieutenant = "Proposer {0} centaures au lieutenant {1}.";
	public static final String ORDRE_changer_capitale = "Changer ma capitale en {0}.";
	public static final String ORDRE_affecter_recherche = "Affecter {1}% des capacités de recherche à la technologie {0}.";
	public static final String ORDRE_abandonner_technologie = "Abandonner la technologie {0}.";
	public static final String ORDRE_services_speciaux = "Effectuer une mission sur le système {0} de type {1} sur la planète {2}.";
	public static final String ORDRE_annuler_construction = "Annuler les constructions en cours sur le système {0}.";
	public static final String ORDRE_construire = "Construire sur le système {0} {2} {1} pour la planète {3}.";
	public static final String ORDRE_programmer_construction = "Programmer sur le système {0} un programme de type {1}.";
	public static final String ORDRE_deprogrammer_construction = "Annuler le programme de construction sur le système {0}.";
	public static final String ORDRE_modifier_politique = "Passer en politique {1} sur le système {0}.";
	public static final String ORDRE_modifier_budget = "Faire passer le budget {1} sur le système {0} à {2}%.";
	public static final String ORDRE_modifier_taxation_systeme = "Faire passer à {1} la taxation du système {0}.";
	public static final String ORDRE_modifier_taxation_planete = "Faire passer à {1} la taxation du système {0} de ma planète {2}.";
	public static final String ORDRE_terraformer_systeme = "Terraformer le système {0}.";
	public static final String ORDRE_terraformer_planete = "Terraformer la planète {1} du système {0}.";
	public static final String ORDRE_mettre_au_rebus = "Détruire {2} {1} sur le système {0} à partir de la planète {3}.";
	public static final String ORDRE_creer_alliance = "Vous venez de créer l'alliance {0} qui sera de type {2}, secret {1} avec pour taxe d'entrée {3} centaures.";
	public static final String ORDRE_renommer_alliance = "Renommer votre alliance comme {0}.";
	public static final String ORDRE_ecrire_adresse_alliance = "Adresse de votre alliance : {0}.";
	public static final String ORDRE_charger_cargo = "Charger le cargo {3} de la flotte {0} avec {2} {1} pour le poste {4} et la planète {5}.";
	public static final String ORDRE_decharger_cargo = "Décharger le cargo {3} de la flotte {0} avec {2} {1} pour le poste {4} et la planète {5}.";
	public static final String ORDRE_deplacer_flotte = "Déplacer la flotte {0} vers la position {1}-{2}-{3} en directive {4} et stratégie {5}.";
	public static final String ORDRE_pister = "Piste la flotte {1} avec ma flotte {0}.";
	public static final String ORDRE_utiliser_porte_galactique = "Passer dans la galaxie {1} avec ma flotte {0}.";
	public static final String ORDRE_utiliser_colonisateur = "Coloniser avec ma flotte {0}.";
	public static final String ORDRE_larguer_mines = "Lancer des mines avec ma flotte {0}.";
	public static final String ORDRE_construire_flotte = "Commencer un programme de construction {1} avec ma flotte {0}.";
	public static final String ORDRE_fusionner_flotte = "Fusionner ma flotte {0} et {1} en directive {2}.";
	public static final String ORDRE_diviser_flotte = "Diviser ma flotte {0}, nouveau nom {1}.";
	public static final String ORDRE_transferer_centaures = "Transférer {1} centaures pour le commandant {0} en mode {2}.";
	public static final String ORDRE_transferer_technologie = "Transférer la technologie {1} pour le commandant {0} en mode {2}.";
	public static final String ORDRE_transferer_systeme = "Transférer le système {1} pour le commandant {0} en mode {2}.";
	public static final String ORDRE_transferer_planete = "Transférer la planète {2} du système {1} pour le commandant {0} en mode {3}.";
	public static final String ORDRE_transferer_flotte = "Prêter la flotte {1} pour le commandant {0} et pour {2} tours.";
	public static final String ORDRE_vendre_flotte = "Vendre la flotte {1} pour le commandant {0}.";
	public static final String ORDRE_transferer_strategie = "Transférer la stratégie {1} pour le commandant {0} en mode {2}.";
	public static final String ORDRE_fixer_taux_poste = "Fixer le taux de taxation de mes postes commerciaux à {0}%.";
	public static final String ORDRE_donner_plan = "Donner le plan {0} au commandant {1} en mode {2}.";
	public static final String ORDRE_creer_plan = "créer le plan {0}, marque {1}, domaine {2}, royalties {3}%.";
	public static final String ORDRE_creer_strategie = "créer la stratégie {0}, agressivité {1}, cible principale {2}.";
	public static final String ORDRE_renommer_systeme = "Renommer le système {0}, nouveau nom {1}.";
	public static final String ORDRE_renommer_planete = "Renommer la planète {2} du système {0}, nouveau nom {1}.";
	public static final String ORDRE_renommer_flotte = "Renommer la flotte {0}, nouveau nom {1}.";
	public static final String ORDRE_renommer_lieutenant = "Renommer mon lieutenant {0}, nouveau nom {1}.";
	public static final String ORDRE_ecrire_adresse_commandant = "Nouveau site du commandant {0}.";
	public static final String ORDRE_ecrire_article = "Ecrire article {0}.";

}
