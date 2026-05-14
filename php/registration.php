<!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="assets/css/styles.css?v2">
    <title>Sheril, le jeu de conquête galactique</title>
    <script src="assets/js/script.js" defer></script>
</head>
<body>

<style>

    blockquote {
        border-left: 2px solid #003963;
        padding: 20px;
    }

    label {
        margin-bottom: 10px;
        display: flex;
        justify-content: space-between;
    }

    form {
        max-width: 400px;
        margin: 0 auto;
    }

    input[type="text"], input[type="email"], select {
        width: 50%;
    }

    button, select, input:not([type="checkbox"]), textarea {
        background: #001021;
        color: #dedede;
        border: 1px solid #a7d0f6;
        box-shadow: 0px 1px 4px #003963;
        cursor: pointer;
        border-radius: 1px;
        padding: 2px 5px;
    }

    input[type=submit] {
        background: #dedede;
        color: black;
        border: 1px solid #999;
        font-weight: bold;
        margin: 0 5px;
    }

    input[type=submit]:hover {
        filter: brightness(150%);
    }


    input:not([type="checkbox"]):focus,
    select:focus,
    textarea:focus {
        outline: none; /* supprime le contour par défaut */
        box-shadow: 0px 0px 16px 12px #003963;
        transition: box-shadow 0.2s ease;
    }

    .race0{ color: #CC00FF;
        font-weight: bold;
        text-transform: capitalize;
    }
    .race1 {
        color: #0066CC;
        font-weight: bold;
        text-transform: capitalize;
    }

    .race2 {
        color: #FFCC00;
        font-weight: bold;
        text-transform: capitalize;
    }

    .race3 {
        color: #CC0033;
        font-weight: bold;
        text-transform: capitalize;
    }

    .race4 {
        color: #009933;
        font-weight: bold;
        text-transform: capitalize;
    }

    .race5 {
        color: #777777;
        font-weight: bold;
        text-transform: capitalize;
    }

    .info {
        display: inline-flex;
        width: 20px;
        height: 20px;
        background: #EEEE;
        border-radius: 50%;
        color: black;
        font-weight: bold;
        justify-content: center;
        align-items: center;
    }

    .pie {
        width: 200px;
        height: 200px;
        border-radius: 50%;
        margin: 20px auto;
        border: 2px solid #a7d0f6;
    }

    .legend {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 15px;
        margin-top: 20px;
        font-size: 14px;
    }

    .legend-item {
        display: flex;
        align-items: center;
        gap: 5px;
    }

    .legend-color {
        width: 12px;
        height: 12px;
        border-radius: 2px;
    }

</style>
<header>
    Sheril, le jeux de conquête galactique
</header>

<nav>
    <a href="index.html">Accueil</a>
    <a href="presentation.html">Présentation</a>
    <a href="races.html">Les races</a>
    <a href="/stats.html">Statistiques</a>
    <a href="/ordres/ordres.php3">Console d'ordre</a>
</nav>
<style>
    table { border-collapse: collapse; width: 80%; margin: 20px auto; font-family: Arial, sans-serif; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #2c3e50; color: white; }
    tr:nth-child(even) { background-color: #012642; }
    .empty { text-align: center; font-style: italic; color: #777; }
</style>

<div id="main">
    <nav>
        <a href="/register.php?p=new">S'inscrire</a>
        <a href="/registration.php">Inscriptions en attente</a>
    </nav>
    <main>

        <?php
        @require_once './secure/connect.txt';


        // 1. Définition des noms de races (pour un affichage plus lisible)
        $races_noms = [
                0 => "Fremens",
                1 => "Atalantes",
                2 => "Zwaias",
                3 => "Yoksor",
                4 => "Fergok",
                5 => "Cyborg"
        ];
        // 2. Requête pour récupérer les données
        $sql = "SELECT NOM, ADRESSE, RACE, FLOTTE FROM aa_inscription ORDER BY date_insertion ASC";
        $result = @mysql($base, $sql);
        if (!$result) {
            echo "Erreur" . mysql_error();
        }
        $count = @mysql_num_rows($result);

        // Préparation des données pour le camembert et stockage des inscrits
        $inscrits = [];
        $repartition_races = [];
        if ($count > 0) {
            while ($row = mysql_fetch_assoc($result)) {
                $inscrits[] = $row;
                $race_id = $row['RACE'];
                $repartition_races[$race_id] = (isset($repartition_races[$race_id]) ? $repartition_races[$race_id] : 0) + 1;
            }
        }

        // Couleurs des races
        $races_couleurs = [
            0 => '#CC00FF',
            1 => '#0066CC',
            2 => '#FFCC00',
            3 => '#CC0033',
            4 => '#009933',
            5 => '#777777'
        ];

        // Calcul du gradient pour le camembert CSS
        $gradient_parts = [];
        if ($count > 0) {
            $current_percent = 0;
            foreach ($repartition_races as $id => $nb) {
                $percent = ($nb / $count) * 100;
                $color = isset($races_couleurs[$id]) ? $races_couleurs[$id] : '#FFFFFF';
                $next_percent = $current_percent + $percent;
                $gradient_parts[] = $color . " " . number_format($current_percent, 2, '.', '') . "% " . number_format($next_percent, 2, '.', '') . "%";
                $current_percent = $next_percent;
            }
        }
        $conic_gradient = implode(", ", $gradient_parts);
        ?>
        <h1>Liste des inscriptions en attente <?=$count?></h1>

        <?php if ($count > 0): ?>
        <div class="pie" style="background: conic-gradient(<?php echo $conic_gradient; ?>);"></div>

        <div class="legend">
            <?php foreach ($repartition_races as $id => $nb): ?>
                <div class="legend-item">
                    <div class="legend-color" style="background-color: <?php echo isset($races_couleurs[$id]) ? $races_couleurs[$id] : '#FFFFFF'; ?>;"></div>
                    <span><?php echo isset($races_noms[$id]) ? $races_noms[$id] : "Inconnue"; ?> (<?php echo $nb; ?>)</span>
                </div>
            <?php endforeach; ?>
        </div>
        <?php endif; ?>

        <table>
            <thead>
            <tr>
                <th>Nom de Commandant</th>
                <th>Race</th>
            </tr>
            </thead>
            <tbody>
            <?php if ($count > 0): ?>
                <?php foreach ($inscrits as $row): ?>
                    <tr>
                        <td <?php echo "class='race" . $row['RACE'] . "'"; ?>><?php echo ucfirst(htmlspecialchars($row['NOM'])); ?></td>
                        <td <?php echo "class='race" . $row['RACE'] . "'"; ?>>
                            <?php
                            // Affiche le nom de la race si l'ID existe dans notre tableau, sinon affiche l'ID
                            echo isset($races_noms[$row['RACE']]) ? $races_noms[$row['RACE']] : "Inconnue (" . $row['RACE'] . ")";
                            ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            <?php else: ?>
                <tr>
                    <td colspan="4" class="empty">Aucun inscrit pour le moment.</td>
                </tr>
            <?php endif; ?>
            </tbody>
        </table>
    </main>
</div>
<footer>

</footer>

</body>
</html>
