<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/styles.css">
    <title>Sheril, les yoksors</title>
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
            <span class="race4">Les Fergoks</span>,
            tas de muscles qui réfléchissent avec leurs poings.
        </h1>
        <section id="morphologie">
            <h2>Morphologie</h2>
            <img src="./img/fergok-soldat.jpeg" alt="fergok" class="float">
            <dl>
                <dt>Taille moyenne</dt>
                <dd>3 mètres</dd>

                <dt>Espérance de vie moyenne</dt>
                <dd>70 cycles</dd>

                <dt>Poids moyen</dt>
                <dd>100 kg (et plus selon la musculature)</dd>

                <dt>Habitat</dt>
                <dd>Terre Ferme</dd>
            </dl>

            <h3>Description</h3>

            <p>
                D'apparence humanoïde, les <span class="race4">Fergok</span> sont de très grande taille et extrêmement bien bâtis.
                Contrairement à d'autres formes de vie sur Dune, ils occupent la <strong>Terre Ferme</strong> ;
                leur carrure robuste leur permet de supporter les grandes différences de températures liées aux changements climatiques brutaux.
            </p>
            <p>
                Issus d'une sélection naturelle rigoureuse, ils possèdent une forte ossature et une musculature impressionnante.
                Leurs traits rudes et leur faciès bestial sont souvent recouverts de tatouages guerriers.
            </p>
        </section>

        <section id="caractere">
            <h2>Caractère et Aptitudes</h2>
            <img src="./img/fergok-fight.jpeg" alt="fergok" class="float left">
            <p>
                Amateurs de combats en tout genre et de guerres meurtrières, les <span class="race4">Fergok</span> brillent davantage par leur
                <strong>force de frappe extrêmement destructrice</strong> que par leur intelligence.
            </p>
            <p>
                Bien qu'ils ne soient pas dénués de réflexion, ils privilégient leur instinct, ce qui en fait une race
                impulsive et imprévisible. Cette nature instable attire d'ailleurs la méfiance des autres races de la galaxie.
            </p>
        </section>

        <section id="societe">
            <h2>Société et Identité</h2>
            <p>
                Leur société est organisée en clans (<em>hapu</em>) regroupés en tribus (<em>iwi</em>).
                Leur identité est marquée par :
            </p>
            <img src="./img/fergok-election.jpeg" alt="fergok" class="float ">
            <ul>
                <li><strong>Le sens de la famille :</strong> Un pilier social absolu pour chaque individu.</li>
                <li><strong>La tradition orale :</strong> Bien qu'un code écrit existe, les <strong>tatouages</strong> restent le principal vecteur d'identité, marquant le rang et la généalogie dès la puberté.</li>
                <li><strong>L'art de la guerre :</strong> Un attrait culturel commun à tous les groupes.</li>
            </ul>

            <blockquote cite="Une vie chez les Fergok">
                <p>
                    « Les élections <span class="race4">Fergok</span> sont souvent directes… on se lance les urnes à travers la figure jusqu’à ce que le dernier reste debout… »
                </p>
                <footer>— Extrait de <em>« Une vie chez les <span class="race4">Fergok</span> »</em></footer>
            </blockquote>
        </section>

        <section id="relations">
            <h2>Relations avec les autres espèces</h2>
            <img src="./img/fergok-forteresse.jpeg" alt="fergok" class="float left">
            <p>
                Historiquement, les <span class="race4">Fergok</span> n'ont jamais vraiment eu de contacts prolongés avec les autres formes de vie de Dune.
                De ce fait, ils n'ont pas d'<strong>ennemis prédestinés</strong>. Cependant, leur comportement brutal et
                leur tendance à régler tout différend par la force limitent grandement leurs opportunités diplomatiques.
            </p>
        </section>
    </main>
</div>
<footer>

</footer>

</body>
</html>
