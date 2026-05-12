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
        <h1>Liste des inscriptions en attente</h1>

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
        $sql = "SELECT NOM, ADRESSE, RACE, FLOTTE FROM aa_inscription ORDER BY NOM ASC";
        $result = @mysql($base, $sql);
        if (!$result) {
            echo "Erreur" . mysql_error();
        }
        ?>

        <table>
            <thead>
            <tr>
                <th>Nom de Commandant</th>
                <th>Race</th>
            </tr>
            </thead>
            <tbody>
            <?php if (mysql_num_rows($result) > 0): ?>
                <?php while ($row = mysql_fetch_assoc($result)): ?>
                    <tr>
                        <td <?php echo "class='race" . $row['RACE'] . "'"; ?>><?php echo ucfirst(htmlspecialchars($row['NOM'])); ?></td>
                        <td <?php echo "class='race" . $row['RACE'] . "'"; ?>>
                            <?php
                            // Affiche le nom de la race si l'ID existe dans notre tableau, sinon affiche l'ID
                            echo isset($races_noms[$row['RACE']]) ? $races_noms[$row['RACE']] : "Inconnue (" . $row['RACE'] . ")";
                            ?>
                        </td>
                    </tr>
                <?php endwhile; ?>
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
