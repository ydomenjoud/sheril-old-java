<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/styles.css">
    <title>Sheril, les atalantes</title>
    <script src="/assets/js/script.js" defer></script>
</head>
<body>

<header>
    Sheril, le jeux de conquête galactique
</header>

<?php require_once '../includes/nav.php'; ?>
<div id="main">
    <?php require_once './nav.php'; ?>
    <main>
        <h1>
            <span class="race1">Les Atalantes</span>
            , une race issue de diverses mutations visant à coloniser les mondes
            océaniques.
        </h1>


        <!-- Section Morphologie -->
        <section id="morphologie">
            <h2>Morphologie</h2>
        <img src="./img/atalante-leader.jpeg" alt="Atalante" class="float">

            <!-- Liste de description pour les données chiffrées/caractéristiques -->
            <dl>
                <dt><strong>Taille :</strong></dt>
                <dd>Grande</dd>

                <dt><strong>Poids moyen :</strong></dt>
                <dd>80 kg</dd>

                <dt><strong>Espérance de vie :</strong></dt>
                <dd>120 ans</dd>
            </dl>

            <article>
                <h3>Description physique</h3>
                <p>Les <span class="race1">Atalantes</span> sont une race issue de diverses mutations visant à coloniser les mondes océaniques.
                    Pour la plupart grands et élancés, ils ont gardé un double système respiratoire, leurs branchies se
                    situant sous leurs oreilles. Leur peau est plutôt bleuâtre avec parfois quelques nuances de
                    vert.</p>
                <p>Au fil du temps, certaines ethnies sont apparues. Les classes ouvrières devant vivre dans les
                    profondeurs, là où les conditions de vie étaient les plus dures, ont fini par évoluer et par
                    développer une plus grande adaptabilité aux profondeurs importantes, leur conférant ainsi un aspect
                    plus massif et moins gracieux.</p>
                <p>A l'opposé, certains <span class="race1">atalantes</span> obligés de vivre durablement à bord de vaisseaux spatiaux, dans des
                    environnements avec peu d'eau et principalement à l'air libre, ont vu leur peau blanchir des
                    rayonnements des astres et leurs corps s'affiner pour gagner en mobilité et adresse.</p>
                <p>Les habitants des villes-dômes sont un savant mélange entre ces deux extrêmes avec des motifs de peau
                    qui varient en formes et couleurs selon leurs mondes et continents d'appartenance.</p>
            </article>
        </section>

        <!-- Section Psychologie -->
        <section id="psychologie">
            <h2>Psychologie</h2>
            <p>Les <span class="race1">Atalantes</span> croient surtout en eux-mêmes et en leur propre supériorité en tant que race par rapport aux
                autres de l'univers. Ils pensent être les descendants de la noblesse de l'Empire Scio. De ce fait, ils
                sont généralement assez hautains dans leurs attitudes voire méprisants avec certaines races de barbares,
                notamment les Fergoks.</p>
            <p>L’océan étant leur lieu de vie préféré, les <span class="race1">Atalantes</span> ont développé une forte et une forte conscience
                écologique.</p>
        </section>

        <!-- Section Société -->
        <section id="societe">
            <h2>Société</h2>

        <img src="./img/atalante-city.jpeg" alt="Atalante" class="float left">
            <article>
                <h3>Organisation sociale</h3>
                <p>Les dirigeants <span class="race1">Atalantes</span> sont choisis pour leur sagesse ou la richesse de leurs connaissances et
                    savoirs-faire. Ils sont conseillés par plusieurs anciens réunis en conseils de sages, aussi bien
                    pour les affaires courantes que pour les choix militaires, économiques ou politiques.</p>
            </article>

            <article>
                <h3>Lieux de vie</h3>
                <p>Habitués à vivre sous des dômes au fond des océans où les tâches sont pour la plupart automatisées,
                    les <span class="race1">Atalantes</span> jouissent de beaucoup de temps libre voué à l'acquisition de savoirs et la pratique et
                    disciplines artistiques.</p>
                <p>Ils ont des goûts raffinés, et cela se voit dans leur architecture tout en courbes lisses et
                    élégantes et aux décorations artistiques d'une grande diversité et richesse.</p>
            </article>
        </section>

        <!-- Section Religion et Croyances -->
        <section id="religion">
            <h2>Religion et Croyances</h2>
            <img src="./img/atalante-technology.jpeg" alt="Atalante" class="float">
            <p>Pendant longtemps, les <span class="race1">Atalantes</span> ont été assez discrets sur leurs croyances, peu mystiques et attachés à
                une compréhension scientifique et pragmatique de l'univers. Se sont surtout développées de nombreuses
                loges d'initiés constituées autour de savoirs scientifiques et des connaissances secrètes jalousement
                gardées.</p>

            <aside>
                <h3>Le Mythe de Scion</h3>
                <p>Un mythe circule concernant la planète où les <span class="race1">Atalantes</span> ont été créés, Scion, planète disparue depuis
                    des millénaires. Certains véhiculent l'idée que cette planète serait le siège de toutes les
                    connaissances de l'univers l'ancienne cœur du Directoire de l'Empire Scio. Lors du siège du
                    Directoire, les ancêtres des <span class="race1">Atalantes</span> auraient conservé la carte qui mène à Scion et en auraient
                    confié un fragment à chaque lignée exilée.</p>
            </aside>

            <article>
                <h3>La Religion de la Grande Baveuse</h3>
                <p>Une religion s'est également répandue sur plusieurs mondes <span class="race1">atalantes</span>, aux alentours de la 12ème année
                    de l'ère mutante. À cette époque, Astheon, un simple Duc, a progressivement fédéré les couches
                    populaires autour d'une croyance basée sur une divinité, la Grande Baveuse.</p>
                <p>Selon cette religion, c'est la Grande Baveuse qui a ensemencé le monde en créant la vie au sein de
                    l'Eau, pure et source de toute existence. Progressivement, les êtres vivants se sont détournés de la
                    Grande Baveuse en quittant les océans à un point tel qu'ils ne pouvaient plus y respirer, faisant
                    d'eux des hérétiques. En partant de là, seuls ceux qui gardent la capacité à vivre sous l'eau, les
                    <span class="race1">Atalantes</span>, se montrent dignes d'Elle et les autres races sont donc toutes coupables de s'être
                    détournées de cette divinité.</p>
                <p>Cette religion s'est fortement développée car elle parvenait à flatter les égouts (égos) de tous au
                    sein de la société <span class="race1">Atalante</span> : Elle empruntait de nombreux éléments déjà présents dans la culture de
                    l'aristocratie <span class="race1">Atalante</span> (le peuple élu de Sion, la supériorité des <span class="race1">Atalantes</span> sur les autres,
                    l'existence d'une planète d'origine). Le bas peuple, habitué aux grandes profondeurs pour les
                    travaux les plus pénibles, se trouvait valorisé à tout point de vue : Il était plus proche encore
                    que les autres des profondeurs de Sa mer.</p>
                <p>La religion de la Grande Baveuse s'est également très bien intégrée aux franges basses de la société
                    par son approche intégriste : Elle est apparue dans une période d'incertitudes sur le futur, de
                    tensions avec les autres races. Le Duc Astheon a alors exploité les peurs du peuple pour installer
                    au mieux sa religion. Depuis, cette religion, jugée trop extrémiste mais néanmoins un moyen commode
                    d'influence des populations, a connu de nombreuses nouvelles déclinaisons, encouragée par les autres
                    dirigeants <span class="race1">atalantes</span>.</p>
            </article>
        </section>

        <!-- Section Historique -->
        <section id="historique">
            <h2>Historique</h2>
            <img src="./img/atalante-soldat.jpeg" alt="Atalante" class="float left">

            <article>
                <h3>Origines</h3>
                <p><em>(Aucune donnée enregistrée pour le moment)</em></p>
            </article>

            <article>
                <h3>Histoire récente</h3>
                <p>Au-delà du plaisir qu'ils tirent à la rappeler, les <span class="race1">atalantes</span> sont un peuple ancien, parmi les
                    premiers nés de Dune ; leur goût pour les connaissances dissimulées et leur perfectionnisme les ont
                    souvent empêchés de se hisser à un niveau technologique aussi abouti que les <span class="race3">Yoksors</span> ou de prendre
                    de décisions politiques de premier plan dans les conflits de l'univers.</p>
                <p>Ainsi, alors que les conflits ont fait rage entre <span class="race3">Yoksors</span>, Fremens et Fergoks durant de nombreux
                    cycles, les <span class="race1">Atalantes</span> ont montré des réticences à prendre parti, méprisant des conflits considérés
                    comme mesquins et indignes d'eux. Cette neutralité individualiste affichée par la majorité d'entre
                    eux a suscité les méfiances de potentiels alliés des races voisines et compromis leur essor
                    économique.</p>
                <p>Cependant, les visées religieuses ou les alliances guerrières de certains d'entre eux ont contribué à
                    remettre en cause cette philosophie de non-implication dans les affaires politiques de l'univers.
                    Certains jeunes dirigeants <span class="race1">atalantes</span> défendent une politique plus agressive en vue de préserver les
                    richesses d'un univers ravagé par des guerres barbares aux dommages irréparables ; revendiquant pour
                    eux l'ancien patrimoine du Directoire de l'Empire Scio, ils se posent en gardiens éclairés face aux
                    races ignorantes.</p>
            </article>
        </section>

        <!-- Section Diplomatie -->
        <section id="diplomatie">
            <h2>Diplomatie</h2>

            <article>
                <h3>Relation avec les autres espèces</h3>
                <p>Les <span class="race1">Atalantes</span> ne sont pas particulièrement amis ou ennemis avec les autres races. En réalité, ils ont
                    juste tendance à les prendre un peu de haut, sauf peut-être les Zwaias qui selon eux ne
                    descendraient pas de la légendaire Scion.</p>

                <ul>
                    <li><strong><span class="race5">Cyborgs</span> :</strong> Considérés comme une anomalie hideuse engendrée par les <span class="race3">Yoksors</span>.</li>
<!--                    <li><strong>Koros :</strong> Considérés comme un ennemi infiniment nuisible, à détruire-->
<!--                        impitoyablement.-->
<!--                    </li>-->
                    <li><strong><span class="race4">Fergoks</span> :</strong> Définis comme <q>des lourdauds n'ayant pas achevé leur évolution vers
                            le statut de race civilisée</q>.
                    </li>
                    <li><strong><span class="race2">Zwaias</span> :</strong> Seuls à susciter chez eux un respect teinté d'une curiosité qui
                        pourrait s'assimiler à de la sympathie.
                    </li>
                </ul>
            </article>
        </section>

    </main>
</div>
<footer>

</footer>

</body>
</html>
