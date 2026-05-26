<?php require_once './includes/top.php'; ?>

<nav>

        <div>Points de victoire</div>
        <a target="stats-detail" href="./stats/general.htm">Général</a>
        <a target="stats-detail" href="./stats/points_victoire.htm">Point de victoire</a>
        <a target="stats-detail" href="./stats/points_victoire_detail.htm">Point de victoire ( détail )</a>

        <div>Classements:</div>
        <a target="stats-detail" href="./stats/puissance.htm">Puissance</a>
        <a target="stats-detail" href="./stats/centaures.htm">Finance</a>
        <a target="stats-detail" href="./stats/planetes.htm">Possession</a>
        <a target="stats-detail" href="./stats/pop.htm">Population</a>
        <a target="stats-detail" href="./stats/reputation.htm">Réputation</a>
        <a target="stats-detail" href="./stats/rayonnement.htm">Rayonnement</a>
        <a target="stats-detail" href="./stats/technologie.htm">Technologie</a>
        <a target="stats-detail" href="./stats/pop_vs.htm">Ville Spatiale</a>
        <a target="stats-detail" href="./stats/offensive.htm">Points de victoire</a>


        <div>Militaire:</div>
        <a target="stats-detail" href="./stats/flotte.htm">TOP 10 flottes</a>
        <a target="stats-detail" href="./stats/combats.htm">Liste des combats</a>
        <a target="stats-detail" href="./stats/vapub.htm">Plans de vaisseaux</a>
        <a target="stats-detail" href="./stats/vaisseaux.htm">Statistiques vaisseaux</a>

        <div>Divers:</div>
        <a target="stats-detail" href="./stats/alliances.htm">Liste des alliances</a>
        <a target="stats-detail" href="./stats/carte.html">Carte de la galaxie</a>

        <a target="stats-detail" href="./stats/encheres.htm">Enchères lieutenants</a>
        <a target="stats-detail" href="./stats/taux_poste.htm">Taux des posts commerciaux</a>
        <a target="stats-detail" href="./stats/univers.htm">Statistiques Univers</a>
        <a target="stats-detail" href="./stats/evt.htm">Evénements publics</a>
        <div>Archives:</div>

        <div id="statsLink"></div>

    </nav>
    <main>

        <iframe name="stats-detail" width="100%" style="min-height: 600px; height:100%" src="./stats/general.htm"></iframe>

    </main>
<script>
    fetch('/tour.txt')
        .then(res => res.text())
        .then(text => {
            const maxTour = parseInt(text.trim(), 10);
            const container = document.querySelector('#statsLink');
            for (let i = 1; i <= maxTour; i++) {
                const a = document.createElement('a');
                a.href = `/stats/statsT${i}.zip`;
                a.target = '_blank';
                a.textContent = `T${i}`;
                container.appendChild(a);
            }
        })
        .catch(err => console.error('Erreur lecture tour.txt:', err));
</script>
<?php require_once './includes/bot.php'; ?>
