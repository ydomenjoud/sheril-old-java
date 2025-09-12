// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;




public class ListeDescription extends MessagesAbstraits{
//SECTION SPECIALE
 public static final Description myst=new Description("techno de Myst","technos de myst","cherche pas c'est trop complexe..");


 public static final Description design=new Description("Maîtrise de l'ingenièrie","","vous ouvre les portes de l'art du design de plan de vaisseau.");
 public static final Description biolog=new Description("Maîtrise de la biologie","","vous ouvre les portes de la biologie.");
 public static final Description combus=new Description("Maîtrise de la combustion","","vous ouvre les portes de la combustion.");
 public static final Description robinf=new Description("Maîtrise de la robotique","","vous ouvre les portes de la robotisation.");
 public static final Description armure=new Description("Maîtrise de l'armurerie","","vous ouvre les portes de l'art des armes.");
 public static final Description indusp=new Description("Maîtrise des technologies avançée","","vous ouvre les portes de technologies liées à l'industrie de pointe.");
 


// fin maitrises

 public static final Description laser=new Description("laser","lasers","Cette arme vous permettra de concevoir des plans de vaisseaux plus efficaces.");
 public static final Description plasma=new Description("canon à plasma","canons à plasma","Cette arme vous permettra de concevoir des plans de vaisseaux plus efficaces.");
 public static final Description torp=new Description("lance-torpille","lance-torpilles","Cette arme vous permettra de concevoir des plans de vaisseaux plus efficaces.");
 public static final Description miss=new Description("missile","missiles","Cette arme vous permettra de concevoir des plans de vaisseaux plus efficaces.");

 public static final Description bombe=new Description("bombe","bombes","Cette arme vous permettra de concevoir des plans de vaisseaux plus efficaces.");

 public static final Description moteur=new Description("réacteur","réacteurs","Un réacteur permet à un vaisseau de se déplacer à la vitesse classique correspondant à sa taille, auquel on ajoute le bonus de la caractéristique &quot;Propulsion&quot; du réacteur.");
 public static final Description intra=new Description("réacteur intragalactique","réacteurs intragalactiques","Un réacteur intragalactique est un composant de vaisseau.<br>Il permet au vaisseau qui en possède un de se déplacer instantanément d'un point à l'autre de la galaxie.");
 public static final Description inter=new Description("réacteur intergalactique","réacteurs intergalactiques","Un réacteur intergalactique est un composant de vaisseau.<br>Il permet au vaisseau qui en possède un de se déplacer instantanément d'une galaxie à une autre, sans passer par une porte galactique.");

 public static final Description scan=new Description("scanner","scanners","Un scanner est un composant de vaisseau.<br> Le scanner permet d'apercevoir des systèmes à une distance égale à sa caractéristique &quot;Portée scanner&quot;.");

public static final Description absorb=new Description("absorbeur","absorbeurs","L'absorbeur est un composant générant un puissant champ energétique à la surface même de la coque.<br />Il permet au vaisseau d'absorber un certain nombre de dommages par vaisseaux ennemis, se rechargeant après chaque salve.<br /> Plusieurs absorbeurs sur un même vaisseau n'ont pas d'effets cumulatifs.<br /> ");// armes de def

 public static final Description bouclier=new Description("bouclier","boucliers","Un bouclier est un composant de vaisseau.<br> Un bouclier permet de parer un nombre de dommages égal à sa capacité &quot;Capacité bouclier magnétique&quot; et se régénère aprés le combat. <br> Il ne peut pas y avoir plus de 10 boucliers par vaisseau.");
 public static final Description lmine=new Description("lanceur de mines AM","lanceurs de mines AM","Un lanceur de mines est un composant de vaisseau.<br>Un lanceur de mine permet de lancer un nombre de mines anti-matière classiques par tour égal à sa caractéristique &quot;Lanceur de mines&quot;.");
 public static final Description dmine=new Description("détecteur de mines","détecteurs de mines","Un détecteur de mines est un composant de vaisseau.<br>Un détecteur de mines permet au vaisseau qui en possède un d'éviter tout dégât avec des mines lors d'un tour avec un pourcentage de chance égal à sa caractéristique &quot;détection de mines&quot;. Plusieurs détecteurs de mines sur un même vaisseau n'ont pas d'effets cumulatifs.");
 public static final Description mconstru=new Description("module de construction","modules de construction","Un module de construction est un composant de vaisseau.<br>Un module de construction permet de disposer d'un nombre de points de construction par tour dans l'espace pour construire des vaisseaux égal à la caractéristique &quot;Potentiel navire usine&quot;. Il ne peut pas y avoir plus d'un module de construction par vaisseau.");
 public static final Description bscan=new Description("brouilleur radar","brouilleurs radars","Un brouilleur radar est un composant de vaisseau.<br>un brouilleur radar a un pourcentage de chance égal à sa caractéristique &quot;Brouillage radar&quot; de rendre la flotte du vaisseau invisible aux radars ce tour-ci.<br> Plusieurs brouilleurs radars n'ont pas d'effets cumulatifs : c'est le brouillage le plus puissant qui est pris en compte pour la flotte .");
 public static final Description tract=new Description("rayon tracteur","rayons tracteur","Un rayon tracteur est un composant de vaisseau<br>Il permet de récupérer des containers de marchandises perdus par des vaisseaux ennemis détruiits au cours du combat. Pour qu'un rayon tracteur ait une utilité, il est nécessaire que des vaisseaux dans la même flotte possèdent des soutes cargos pour pouvoir stocker les marchandises récupérées.");
    public static final Description cargo=new Description("COMP.","COMP. OBSOLETES","OBSOLETE");
 public static final Description balast=new Description("balast","balasts","Un balast est un composant de vaisseau.<br>Un balast permet de grossir un vaisseau");
 public static final Description ville=new Description("ville spatiale","villes spatiales","Une ville spatiale est un composant de vaisseau.<br>une ville spatiale est un espace de vie pour un nombre d'unités de population égal à sa caractéristique &quot;Capacité ville spatiale&quot;.");
 public static final Description roboco=new Description("robot colonisateur","robots colonisateurs","Un robot colonisateur est un composant de vaisseau.<br>Un robot colonisateur permet d'explorer une planète pour voir si elle peut être habitée par des membres de la race de l'équipage.");
 public static final Description hscan=new Description("hyper-scanner","hyper-scanners","Un hyper-scanner est un composant de vaisseau.<br> Ce scanner permet d'apercevoir des flottes à une distance égale à sa caractéristique &quot;Portée scanner&quot;.");
 public static final Description dramin=new Description("dragueur de mines","dragueurs de mines","Un dragueur de mines est un composant de vaisseau.<br>Il permet de désactiver un nombre de mines ou de débris diverses par tour égal à sa caractéristique &quot;Capacité dragueur mines&quot;.<br>Un vaisseau dont la moitié ou plus de ses composants sont des dragueurs de mines ne peut pas subir de dommages dÃ»s aux mines.");

  //les batiments -->

 public static final Description chantier=new Description("chantier naval","chantiers navals","Le chantier naval permet de construire des vaisseaux spatiaux à partir du système qui en possède.<br>Il permet également de réparer 20 points de dégats par tour pour une flotte.");
 public static final Description mine=new Description("mine","mines","Une mine procure un revenu en minerai en fonction de la planète sur laquelle elle est construite.");
 public static final Description retraite=new Description("usine de retraitement","usines de retraitement","Une usine de retraitement permet de récupérer du minerai en détruiisant des constructions planétaires sur la planète sur laquelle elle est présente.");
 public static final Description optp=new Description("usine d'optimisation planétaire","usines d'optimisation planétaires","Une usine d'optimisation planétaire permet d'augmenter de 1 pt par niveau par usine construite le nombre de points de construction du système.");
 public static final Description opts=new Description("usine d'optimisation spatiale","usines d'optimisation spatiales","Une usine d'optimisation spatiale permet d'augmenter de 2 pt par usine construite le nombre de points de construction du système.");
 public static final Description repare=new Description("centre de réparation spatial","centres de réparation spatiaux","Un centre de réparation spatial permet de renflouer les flottes endommagées qui orbitent autour du système, à raison de sa caractéristique &quot;Capacité réparation vaisseaux&quot; points par tour.");
 public static final Description boucpla=new Description("bouclier planétaire","boucliers planétaires","Un bouclier planétaire procure une protection efficace en cas d'attaque de la planète.<br>Les attaquants doivent d'abord le détruire avant de pilonner la population et les éventuelles constructions de défense.");
 public static final Description agro=new Description("station de produits alimentaires","stations de produits alimentaires","Une station de produits alimentaires procure (5 fois le niveau du bâtiment ) unités de produits alimentaires par tour.");
 public static final Description agricole=new Description("usine agricole","usines agricoles","Une usine agricole procure (5 fois le niveau du bâtiment )  unités de materiel agricole par tour.");
 public static final Description mode=new Description("station de produits de luxe","stations de produits de luxe","Une station de produits de luxe procure (5 fois le niveau du bâtiment ) unités de produits de luxe par tour.");
 public static final Description culturel=new Description("station d'holofilms et hololivres","stations d'holofilms et hololivres","Une station d'holofilms et hololivres procure (5 fois le niveau du bâtiment )  unités de holofilms et hololivres par tour.");
 public static final Description transfo=new Description("station de système de guidage","stations de système de guidage","Une station de système de guidage procure (5 fois le niveau du bâtiment ) unités de systèmes de guidage par tour.");
 public static final Description pharma=new Description("station de médicaments","stations de médicaments","Une station de médicaments procure (5 fois le niveau du bâtiment )  unités de médicaments par tour.");
 public static final Description info=new Description("station de logiciels","stations de logiciels","Une station de logiciels procure (5 fois le niveau du bâtiment )  unités de logiciels par tour.");
 public static final Description robot=new Description("station de robots","stations de robots","Une station de robots procure (5 fois le niveau du bâtiment ) unités de robots par tour.");
 public static final Description techno=new Description("station de composants électroniques","stations de composants électroniques","Un station de composants électroniques procure (5 fois le niveau du bâtiment ) unités de composants électroniques par tour.");
 public static final Description arme=new Description("station d'armement et explosifs","stations d'armement et explosifs","Une station d'armement et explosifs procure (5 fois le niveau du bâtiment ) unités d'armement et d'explosif par tour.");
 public static final Description raffine=new Description("station d'unité energétique","stations d'unité energétique","Une station d'unité energétique procure (5 fois le niveau du bâtiment ) unités energétique par tour.");
 public static final Description lourde=new Description("station de pièce industrielle","stations de pièce industrielle","Une station de pièce industrielle procure (5 fois le niveau du bâtiment ) unités de pièces industrielles par tour.");
 public static final Description metaux=new Description("station de métaux précieux","stations de métaux précieux","Une station de métaux précieux procure (5 fois le niveau du bâtiment ) unités de métaux précieux par tour.");
 public static final Description radar=new Description("radar","radars","Un radar permet de détecter les flottes et les systèmes qui se trouvent au plus à une distance égale à sa caractéristique &quot;Portée radar&quot;.");
 public static final Description extra=new Description("usine d'extraction avancée","usines d'extraction avancée","Une usine d'extraction avancée permet de rajouter une capacité égale à sa caractéristique &quot;Capacité extraction avancée&quot; au potentiel de production de minerai de la planète. Il ne peut pas y avoir plus d'une usine d'extraction utile par planète.");

 public static final Description battla=new Description("batterie de lasers","batteries de lasers","Une batterie laser permet de mieux défendre la planète sur laquelle elle est construite.");
 public static final Description battpla=new Description("batterie de plasmas","batteries de plasmas","Une batterie plasma permet de mieux défendre la planète sur laquelle elle est construite.");
 public static final Description rampemis=new Description("rampe de missiles","rampes de missiles","Une rampe de missile permet de mieux défendre la planète sur laquelle elle est construite.");
 public static final Description lancetor=new Description("lance-torpilles","lances-torpilles","Un lance-torpilles permet de mieux défendre la planète sur laquelle il est construit.");

    //technologies simples -->

 public static final Description stratco=new Description("Maîtrise du combat","","La connaissance de cette technologie permet de créer des stratégies de combat pour ses flottes.");
 public static final Description diplo=new Description("Maîtrise de la diplomatie","","La connaissance de cette technologie permet de créer des alliances.");
 public static final Description creplan=new Description("Maîtrise de l'espace","","La connaissance de cette technologie permet de créer des plans de vaisseaux.");
 public static final Description progco=new Description("Maîtrise de la planification","","La connaissance de cette technologie permet d'ordonner à un système de construire un type de bâtiment.<BR>Le système gèrera ensuite lui-même le lancement des constructions.");
 public static final Description gestpla=new Description("Maîtrise de la gestion","","La connaissance de cette technologie permet de détruiire des bâtiments inutiles sur ses systèmes et de modifier la taxation et la terraformation de planètes précises, au lieu d'être obligé de modifier toutes les planètes du système en même temps.");




 public static final Description maitmil = new Description("Maîtrise militaire","","Permet de pousser le niveau d'étude de vos chasseurs d'un niveau de maitrise supplémentaire");
 public static final Description maitde = new Description("Maîtrise du développement","","Permet principalement l'amélioration des usines de productions");
 public static final Description maitstargate = new Description("Maîtrise des étoiles","","Permet d'accroître le débit de vos portes spatiales");

 public static final Description maitr0_ = new Description("Maîtrise du savoir Fremen","","Vous permet d'accéder aux technologies de votre race");
 public static final Description maitr1_ = new Description("Maîtrise du savoir Atalante","","Vous permet d'accéder aux technologies de votre race");
 public static final Description maitr2_ = new Description("Maîtrise du savoir Zwaia","","Vous permet d'accéder aux technologies de votre race");
 public static final Description maitr3_ = new Description("Maîtrise du savoir Yoksor","","Vous permet d'accéder aux technologies de votre race");
 public static final Description maitr4_ = new Description("Maîtrise du savoir Fergok","","Vous permet d'accéder aux technologies de votre race");
 public static final Description maitr5_ = new Description("Maîtrise du savoir Cyborg","","Vous permet d'accéder aux technologies de votre race");


    // TECHNOLOGIES CYBORG

    public static final Description cyb_mdc_t1_ = new Description("Complexe spatial","Complexes spatiaux","Un complexe spatial est un composant de vaisseau qui permet de disposer d'un nombre de points de construction par tour dans l'espace pour construire des vaisseaux égal à la caractéristique &quot;Potentiel navire usine&quot;. Il ne peut pas y avoir plus d'un module de construction par vaisseau.");
    public static final Description cyb_mdc_t2_ = new Description("Complexe spatial avançé","Complexes spatiaux avançés","Un complexe spatial avanc&eacute; permet de disposer d'un nombre de points de construction par tour dans l'espace pour construire des vaisseaux égal à la caractéristique &quot;Potentiel navire usine&quot;. Il ne peut pas y avoir plus d'un module de construction par vaisseau.");
    public static final Description cyb_mdc_t3_ = new Description("Complexe spatial TerXD ","Complexes spatiaux TerXD","Un complexe spatial TerXD permet de disposer d'un nombre de points de construction par tour dans l'espace pour construire des vaisseaux égal à la caractéristique &quot;Potentiel navire usine&quot;. Il ne peut pas y avoir plus d'un module de construction par vaisseau.");
    public static final Description cyb_vs_tt_ = new Description("Centre technologique embarqué","Centres technologiques embarqués","Un centre technologique est un laboratoire embarqué qui permet de rechercher des technologies en fonction de la population de chercheurs travaillant dans le centre.");
    public static final Description cyb_vs_te_ = new Description("Centre d'espionnage embarqué","Centres d'espionnage embarqués","Un centre d'espionnage est un centre d'entrainement de vos espions. Il permet d'effectuer des missions spéciales en fonction de la population travaillant dans le centre.");
    public static final Description cyb_vs_tc_ = new Description("Centre de surveillance embarqué","Centres de surveillance embarqués","Un centre de surveillance est un composant de vaisseau; il permet de surveiller votre domaine et de coincer les espions ennemis");

    // Zwaias
    public static final Description poly_const = new Description("Centre de production militaire avançé","Centre de production militaire avançé","Produis plusieurs marchandise en meme temps (5 fois le niveau de l'usine)");
    public static final Description poly_pop = new Description("Centre d'aménagement avançé","Centre d'aménagement avançé","Produis plusieurs marchandise en meme temps (5 fois le niveau de l'usine)");

 }



