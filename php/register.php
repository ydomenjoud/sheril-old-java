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
        border-left: 4px solid #9c27b0;
        padding: 20px;
        color: #c37bcd;
        font-size: 1.3em;
        font-style: italic;
        margin: 10px;
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

    option[value=0], .fre {
        color: #CC00FF;
        font-weight: bold;
        text-transform: capitalize;
    }
    input:not([type="checkbox"]):focus,
    select:focus,
    textarea:focus {
        outline: none;                /* supprime le contour par défaut */
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
    <a href="/stats/">Statistiques</a>
    <a href="/ordres/ordres.php3">Console d'ordre</a>
</nav>


<div id="main">
    <nav>
        <a href="/register.php?p=new">S'inscrire</a>
        <a href="/registration.php">Inscriptions en attente</a>
    </nav>
    <main>
        <h1>Inscription : Forgez votre Destin</h1>
        <blockquote>
            L'univers est vaste, froid et n'attend qu'un leader pour l'unifier.
            Plongez dans Sheril où chaque décision sculpte l'avenir de la galaxie.
            Vous ne commencez qu'avec une modeste colonie, mais l'horizon n'a pas de limites.
        </blockquote>

        <form action="register.php" method="post">
        <?php
            // 2. Vérification que le formulaire a été soumis
            if ($_SERVER["REQUEST_METHOD"] == "POST") {

                if(strtolower($_POST['mj'])!='myst'){
                    echo "Ce n'est pas le pseudo du MJ !";
                } else {
                    @require_once './secure/connect.txt';

                    // 1. Récupération des données du formulaire
                    $pseudo = isset($_POST['pseudo']) ? $_POST['pseudo'] : '';
                    $race = isset($_POST['race']) ? $_POST['race'] : 0;
                    $email = isset($_POST['email']) ? $_POST['email'] : '';

                    // 2. Sécurisation minimale (très important avec les vieilles fonctions)
                    // On utilise mysqli_real_escape_string car vos fonctions utilisent mysqli en interne
                    // Si vous avez une fonction personnalisée mysql_real_escape_string, utilisez-la.
                    $pseudo_safe = @mysql_real_escape_string($pseudo);
                    $email_safe = @mysql_real_escape_string($email);
                    $race_safe = (int)$race;
                    $flotte = "NULL"; // Sera inséré comme valeur SQL NULL

                    // 3. Préparation de la requête
                    $sql = "INSERT INTO aa_inscription (NOM, ADRESSE, RACE, FLOTTE) 
                        VALUES ('$pseudo_safe', '$email_safe', $race_safe, $flotte)";

                    // 4. Exécution de la requête
                    // On utilise mysqli_query car votre serveur semble reposer sur l'objet $link de mysqli
                    if (@mysql($base, $sql)) {
                        echo "Inscription réussie pour le commandant " . htmlspecialchars($pseudo);
                    } else {
                        echo "Erreur lors de l'inscription : " . mysql_error();
                    }
                }
            } else {
        ?>

            <label>
                Nom de commandant <span class="info" title="Sans caractères spéciaux, uniquement lettre/chiffre/underscore">?</span>
                <input type="text" name="pseudo" pattern="^[a-zA-Z0-9_]{3,32}$" required/>
            </label>
            <label>
                Race
                <select required name="race">
                    <option value="0">Fremens</option>
                    <option value="1">Atalantes</option>
                    <option value="2">Zwaias</option>
                    <option value="3">Yoksor</option>
                    <option value="4">Fergok</option>
                </select>
            </label>
            <label>
                Email
                <input type="email" name="email" required/>
            </label>
            <label>
                Le pseudo du MJ <span class="info" title="Pour empécher les r0B0ts de passer">?</span>
                <input type="text" name="mj" required/>
            </label>
            <input type="submit" value="S'inscrire">
        <?php
            }
        ?>
        </form>
    </main>
</div>
<footer>

</footer>

</body>
</html>
