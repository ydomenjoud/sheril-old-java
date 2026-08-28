<?php
define('EMBED', true);
require_once '../includes/top.php'; ?>

<?php include_once __DIR__. "/../includes/headers.php"; ?>


<?php require_once '../includes/nav.php'; ?>
<div id="main">
    <?php require_once './nav.php'; ?>
    <main>
        <h1>
            <span class="race3">Les Yoksors</span>
            , les habitants les plus ridicules de <?=$game_name?>, mais aussi les plus brillants.
        </h1>

        <section>
            <h2>Description physique et Comportement</h2>
            <img src="./img/yoksor-chercheur.jpeg" alt="Yoksor" class="float">

            <h3>Morphologie</h3>
            <dl>
                <dt>Apparence</dt>
                <dd>Petits êtres humanoïdes à l'allure frêle.</dd>

                <dt>Posture</dt>
                <dd>Posture "cassée en deux" due à une vie passée penchée sur des éprouvettes et des pupitres.</dd>

                <dt>Poids moyen</dt>
                <dd>50 kg.</dd>

                <dt>Espérance de vie</dt>
                <dd>Environ 90-100 ans grâce à leurs recherches avancées.</dd>
            </dl>
            <h3>Description</h3>

            <p>
                Bien qu'ils semblent souvent petits car bossus et rachitiques, les <span class="race3">Yoksors</span> sont le fruit de mutations visant à créer des
                <strong>super chercheurs</strong>. Ils ne cessent de marmonner des formules complexes et ne quittent pratiquement
                jamais leurs laboratoires, si ce n'est "les pieds en avant".
            </p>
            <p>
                Leur caractéristique la plus frappante reste leur tête au cortex surdéveloppé, témoignant de leur capacité à
                étudier les lois physiques les plus abstraites.
            </p>
        </section>

        <section>
            <h2>Société et Mentalité</h2>
            <img src="./img/yoksor-obsession.jpeg" alt="Yoksor" class="float left">
            <p>
                Les <span class="race3">Yoksors</span> vivent dans une société de haute technologie, mais sont des <strong>êtres solitaires</strong>.
                Cet isolationnisme et ce manque de cohésion sociale sont les principaux freins à leur expansion galactique.
            </p>
            <p>
                Scientifiques avant tout, ils ne possèdent pas de religion et ne jurent que par les faits. Cependant,
                ils partagent une obsession commune : l'étude des <span class="race2">Zwaias</span>. Le rêve de chaque <span class="race3">Yoksor</span> est d'en capturer un
                pour l'examiner en détail afin de percer les origines de l'univers.
            </p>
        </section>

        <section>
            <h2>Capacités et Relations</h2>
            <img src="./img/yoksor-soldat.jpeg" alt="Yoksor" class="float">

            <article>
                <h3>Potentiel Militaire</h3>
                <p>
                    Se fier uniquement à leur apparence ridicule serait une erreur fatale. Grâce à leurs
                    <strong>avancées technologiques</strong>, ils peuvent se transformer en de très bons guerriers,
                    compensant leur faiblesse physique par une puissance de feu sophistiquée.
                </p>
            </article>

            <article>
                <h3>Diplomatie et Historique</h3>
                <ul>
                    <li>
                        <strong>Cyborgs :</strong> Subissent encore la rancœur liée à leur passé d'esclavagistes.
                    </li>
                    <li>
                        <strong>Fergoks :</strong> Méprisent ouvertement la faiblesse physique des <span class="race3">Yoksors</span>.
                    </li>
                    <li>
                        <strong>Zwaias :</strong> Les rapports sont extrêmement tendus en raison des rumeurs d'expériences
                        et de la volonté manifeste des <span class="race3">Yoksors</span> de les disséquer.
                    </li>
                </ul>
            </article>
        </section>

        <!-- Nouvelle section Héritage -->
        <section id="heritage">
            <h2>Héritage</h2>
            <article>
                <h3>Composant de vaisseau</h3>
                <p>Au démarrage de la partie, les <span class="race3">Yoksors</span> héritent de la connaissance "Missile de type I", qui leur permet de créer des plans de vaisseaux efficace en combat spatial contre toute taille de vaisseau ennemi faiblement protégés.</p>
            </article>
            <article>
                <h3>Bâtiment</h3>
                <p>Les <span class="race3">Yoksors</span> peuvent construire sur leurs systèmes de départ des "Stations de logiciels de type II", qui produisent 10 marchandises de type "logiciels" par tour.
                   Un stock de 100 logiciels sur un système octroie un bonus de 25% sur le budget recherche du système.
                   Les logiciels sont utilisés pour la construction d'usines d'optimisation planétaire, de radar, de boucliers planétaires, de batteries de défense, et pour la production de vaisseaux spatiaux, notamment ceux dotés de missiles.
                </p>
            </article>
            <article>
                <h3>Plan de vaisseau</h3>
                <p>Les anciens ont légué aux <span class="race3">Yoksors</span> les plans du vaisseau "Spiteur" utile en combat spatial. C'est un chasseur de taille 3 équipé d'un bouclier et de missiles.</p>
            </article>
        </section>
    </main>
</div>
<footer>

</footer>

</body>
</html>
