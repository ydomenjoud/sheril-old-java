<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/styles.css">
    <title>Sheril, les fremens</title>
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
            <span class="race0">Les Fremens</span>,
            anciens habitants des plaines, désormais maîtres du désert.
        </h1>

        <section id="description">
            <h2>Description</h2>
            <img src="./img/fremen-soldat.jpeg" alt="Fremen" class="float">

            <h3>Morphologie</h3>
            <dl>
                <dt>Taille</dt>
                <dd>1,70m en moyenne</dd>

                <dt>Poids moyen</dt>
                <dd>70 kg</dd>

                <dt>Espérance de vie</dt>
                <dd>50 Cycles</dd>

            </dl>

            <h3>Description physique</h3>
            <p>
                Teint basané et <strong>yeux d'un bleu plus scintillant que l'éclat de l'oxole</strong>.
            </p>

            <p>
                Les <span class="race0">Fremens</span> sont des humains qui ont quitté les plaines verdoyantes pour se retirer dans l'isolement du désert.
                Humanoïdes trapus et résistants, ils sont capables de travaux de force et possèdent une grande endurance.
                Leur vie en milieu hostile fait d'eux des hommes qui <strong>s'adaptent très facilement à tout nouvel environnement</strong>.
            </p>
            <p>
                Cependant, cette coupure de la civilisation et du progrès ne leur a pas été profitable au niveau intellectuel :
                ce sont des êtres passablement bêtes, bien que leurs réflexes soient vifs.
            </p>
        </section>

        <section id="psychologie">
            <h2>Psychologie</h2>
            <img src="./img/fremen-duel.jpeg" alt="Fremen" class="float left">
            <p>
                D’une loyauté et intégrité à toute épreuve, ils savent témoigner d’une amitié indéfectible.
                Leur force réside dans leur capacité à encaisser les « coups durs ».
            </p>
            <blockquote>
                Malgré leurs lacunes intellectuelles, ils demeurent de <strong>bons guerriers</strong> et
                probablement les meilleurs combattants au corps à corps de la galaxie.
            </blockquote>
            <p>
                Habitués à avoir peu, ils sont généreux avec leurs pairs, mais deviennent impitoyables en temps de guerre.
            </p>
        </section>

        <section id="societe">
            <h2>Société</h2>
            <img src="./img/fremen-sietch.jpeg" alt="Fremen" class="float">

            <h3>Organisation sociale</h3>
            <p>
                Les <span class="race0">Fremens</span> sont organisés en tribus très soudées. Chaque clan possède un leader politique
                (souvent un vétéran stratégique) et un guide spirituel, garant des traditions orales.
            </p>

            <h3>Code d'honneur et Justice</h3>
            <p>
                Ils suivent un code d'honneur rigoureux basé sur le jugement par le combat.
                Leurs duels à mort règlent les questions d'étiquette, de loi ou d'honneur.
                Le vainqueur assume la responsabilité des biens et de la famille du vaincu.
            </p>

            <h3>Lieux de vie</h3>
            <p>
                Ils vivent dans des complexes principalement souterrains, souvent dans des contrées inhospitalières.
                Sous leur abord rugueux, les <span class="race0">Fremens</span> cachent un artisanat merveilleux qu'ils partagent avec leurs hôtes.
            </p>
        </section>

        <section id="religion">
            <h2>Religion & Croyances</h2>
            <p>
                Peuple profondément croyant et superstitieux, les <span class="race0">Fremens</span> vouent un culte principal à <strong>l'Eau</strong>,
                symbole de vie en monde aride. Ils attendent la venue d'un prophète pour les guider vers une terre promise.
            </p>
        </section>

        <section id="historique">
            <h2>Historique</h2>
            <article>
                <h3>Origines</h3>
                <p>Anciens résidents des plaines fertiles ayant choisi l'exil volontaire dans le désert.</p>
            </article>
            <article>
                <h3>Relations avec les autres races</h3>
                <p>Diplomatie souvent marquée par leur méfiance naturelle et leur isolement volontaire.</p>
            </article>
        </section>

        <!-- Nouvelle section Héritage -->
        <section id="heritage">
            <h2>Héritage</h2>
            <article>
                <h3>Composant de vaisseau</h3>
                <p>Au démarrage de la partie, les <span class="race0">Fremens</span> héritent de la connaissance "Scanner de type I", qui leur permet de créer des plans de vaisseaux pouvant détecter des flottes et systèmes environnant.</p>
            </article>
            <article>
                <h3>Bâtiment</h3>
                <p>Les <span class="race0">Fremens</span> peuvent construire sur leurs systèmes de départ des "Stations d'enrichissement des métaux de type II", qui produisent 10 marchandises de type "métaux précieux" par tour.
                   Un stock de 100 métaux précieux sur un système octroie un bonus de 5% sur les revenus du système.
                   Les unités métaux précieux sont utilisées pour la construction de bâtiments de production de marchandises avancés, et pour la production de vaisseaux spatiaux, notamment ceux dotés de scanners, brouilleurs et réacteurs avancés.
                </p>
            </article>
            <article>
                <h3>Plan de vaisseau</h3>
                <p>Les anciens ont légué aux <span class="race0">Fremens</span> les plans du vaisseau "Sidjin" utile pour détecter flottes et systèmes. C'est un éclaireur de taille 3 équipé d'un scanner de type IV.</p>
            </article>
        </section>
    </main>
</div>
<footer>

</footer>

</body>
</html>
