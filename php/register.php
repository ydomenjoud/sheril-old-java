<?php require_once './includes/top.php'; ?>

    <style>

        blockquote {
            border-left: 4px solid #9c27b0;
            padding: 20px;
            color: #c37bcd;
            font-size: 1.3em;
            font-style: italic;
            margin: 10px;
        }

        label.field-label {
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        form {
            max-width: 650px;
            margin: 0 auto;
        }
        input[type="text"], input[type="email"] {
            width: 50%;
        }

        button, select, input:not([type="checkbox"]):not([type="radio"]), textarea {
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
            margin: 15px auto 0 auto;
            display: block;
            padding: 8px 20px;
        }
        input[type=submit]:hover {
            filter: brightness(150%);
        }

        input:not([type="checkbox"]):not([type="radio"]):focus,
        select:focus,
        textarea:focus {
            outline: none;
            box-shadow: 0px 0px 16px 12px #003963;
            transition: box-shadow 0.2s ease;
        }

        /* --- STYLES DES RACES --- */
        .race0 { color: #CC00FF; font-weight: bold; text-transform: capitalize; }
        .race1 { color: #0066CC; font-weight: bold; text-transform: capitalize; }
        .race2 { color: #FFCC00; font-weight: bold; text-transform: capitalize; }
        .race3 { color: #CC0033; font-weight: bold; text-transform: capitalize; }
        .race4 { color: #009933; font-weight: bold; text-transform: capitalize; }
        .race5 { color: #777777; font-weight: bold; text-transform: capitalize; }

        /* --- SÉLECTEUR DE RACE VISUEL --- */
        .race-select-title {
            margin: 15px 0 10px 0;
            display: block;
            font-weight: bold;
        }
        .race-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
            gap: 12px;
            margin-bottom: 20px;
        }
        .race-card {
            position: relative;
            cursor: pointer;
        }
        .race-card input[type="radio"] {
            position: absolute;
            top: 6px;
            left: 6px;
            z-index: 2;
            cursor: pointer;
        }
        .race-card-content {
            background: #001021;
            border: 1px solid #003963;
            border-radius: 4px;
            padding: 8px;
            text-align: center;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
            transition: all 0.2s ease;
        }
        .race-card-content:hover {
            border-color: #a7d0f6;
            box-shadow: 0 0 10px #003963;
        }
        .race-card input[type="radio"]:checked + .race-card-content {
            border-color: #a7d0f6;
            box-shadow: 0 0 12px 2px #0066CC;
            background: #001a33;
        }
        .race-avatar {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 3px;
            border: 1px solid #333;
            background: #000;
        }
        .race-name {
            font-size: 0.95em;
        }
        .race-link {
            font-size: 0.75em;
            color: #a7d0f6;
            text-decoration: underline;
            z-index: 2;
        }
        .race-link:hover {
            color: #ffffff;
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

        table { border-collapse: collapse; width: 80%; margin: 20px auto; font-family: Arial, sans-serif; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #2c3e50; color: white; }
        tr:nth-child(even) { background-color: #012642; }
        .empty { text-align: center; font-style: italic; color: #777; }
    </style>

    <nav>
        <a href="/liste.php">Registre actuel</a>
        <a href="/register.php?p=new">S'inscrire</a>
    </nav>
    <main>
        <h1>Inscription : Forgez votre Destin</h1>
        <h2>Commandant, la galaxie est réinitialisée. Vos usines tournent à vide, vos sondes sont prêtes
            et vos rivaux rédigent déjà leurs premiers ordres !</h2>
        <blockquote>
            L'univers est vaste, froid et n'attend qu'un leader pour l'unifier.
            Plongez dans Sheril où chaque décision sculpte l'avenir de la galaxie.
            Vous ne commencez qu'avec une modeste colonie, mais l'horizon n'a pas de limites.
        </blockquote>

        <form action="register.php" method="post">
            <?php
            // Données d'affichage des races (Fiches & Images)
            $races_details = [
                    0 => ['nom' => 'Fremens',  'page' => 'https://sheril.pbem-france.net/races/fremen.php',   'img' => '/races/img/fremen-soldat.jpeg'],
                    1 => ['nom' => 'Atalantes', 'page' => 'https://sheril.pbem-france.net/races/atalante.php', 'img' => '/races/img/atalante-soldat.jpeg'],
                    2 => ['nom' => 'Zwaias',    'page' => 'https://sheril.pbem-france.net/races/zwaia.php',    'img' => '/races/img/zwaia-city.jpeg'],
                    3 => ['nom' => 'Yoksors',   'page' => 'https://sheril.pbem-france.net/races/yoksor.php',   'img' => '/races/img/yoksor-soldat.jpeg'],
                    4 => ['nom' => 'Fergoks',   'page' => 'https://sheril.pbem-france.net/races/fergok.php',   'img' => '/races/img/fergok-fight.jpeg']
            ];

            if ($_SERVER["REQUEST_METHOD"] == "POST") {

                if(strtolower($_POST['mj'])!='myst'){
                    echo "Ce n'est pas le pseudo du MJ !";
                } else {
                    @require_once './secure/connect.txt';

                    $pseudo = isset($_POST['pseudo']) ? $_POST['pseudo'] : '';
                    $race = isset($_POST['race']) ? $_POST['race'] : 0;
                    $email = isset($_POST['email']) ? $_POST['email'] : '';

                    $pseudo_safe = @mysql_real_escape_string($pseudo);
                    $email_safe = @mysql_real_escape_string($email);
                    $race_safe = (int)$race;
                    $flotte = "NULL";

                    $sql = "INSERT INTO aa_inscription2 (NOM, ADRESSE, RACE, FLOTTE) 
                        VALUES ('$pseudo_safe', '$email_safe', $race_safe, $flotte) ORDER BY date_insertion DESC";

                    if (@mysql($base, $sql)) {
                        echo "Inscription réussie pour le commandant " . htmlspecialchars($pseudo);
                    } else {
                        echo "Erreur lors de l'inscription : " . mysql_error();
                    }
                }
            } else {
                ?>

                <label class="field-label">
                    Nom de commandant <span class="info" title="Sans caractères spéciaux, uniquement lettre/chiffre/underscore">?</span>
                    <input type="text" name="pseudo" pattern="^[a-zA-Z0-9_ ]{3,32}$" required/>
                </label>

                <label class="field-label">
                    Ton Email
                    <input type="email" name="email" required/>
                </label>
                <label class="field-label">
                    Le pseudo du MJ <span class="info" title="Pour empêcher les r0B0ts de passer, la réponse c'est: Myst">?</span>
                    <input type="text" name="mj" required/>
                </label>

                <span class="race-select-title">Choisissez votre Race :</span>
                <div class="race-grid">
                    <?php foreach ($races_details as $id => $data): ?>
                        <label class="race-card">
                            <input type="radio" name="race" value="<?=$id?>" <?=$id === 0 ? 'checked' : ''?> required/>
                            <div class="race-card-content">
                                <img src="<?=$data['img']?>" alt="<?=$data['nom']?>" class="race-avatar"/>
                                <span class="race-name race<?=$id?>"><?=$data['nom']?></span>
                                <a href="<?=$data['page']?>" target="_blank" class="race-link" onclick="event.stopPropagation();">Fiche</a>
                            </div>
                        </label>
                    <?php endforeach; ?>
                </div>

                <input type="submit" value="S'inscrire">
                <?php
            }
            ?>
        </form>

        <?php
        @require_once './secure/connect.txt';

        $races_noms = [
                0 => "Fremens",
                1 => "Atalantes",
                2 => "Zwaias",
                3 => "Yoksor",
                4 => "Fergok",
                5 => "Cyborg"
        ];

        $sql = "SELECT NOM, ADRESSE, RACE, FLOTTE FROM aa_inscription2 ORDER BY date_insertion ASC";
        $result = @mysql($base, $sql);
        if (!$result) {
            echo "Erreur" . mysql_error();
        }
        $count = @mysql_num_rows($result);

        $inscrits = [];
        $repartition_races = [];
        if ($count > 0) {
            while ($row = mysql_fetch_assoc($result)) {
                $inscrits[] = $row;
                $race_id = $row['RACE'];
                $repartition_races[$race_id] = (isset($repartition_races[$race_id]) ? $repartition_races[$race_id] : 0) + 1;
            }
        }

        $races_couleurs = [
                0 => '#CC00FF',
                1 => '#0066CC',
                2 => '#FFCC00',
                3 => '#CC0033',
                4 => '#009933',
                5 => '#777777'
        ];

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
        <h2>Liste des inscriptions en attente <?=$count?></h2>

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
                            echo isset($races_noms[$row['RACE']]) ? $races_noms[$row['RACE']] : "Inconnue (" . $row['RACE'] . ")";
                            ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            <?php else: ?>
                <tr>
                    <td colspan="2" class="empty">Aucun inscrit pour le moment.</td>
                </tr>
            <?php endif; ?>
            </tbody>
        </table>

    </main>
<?php require_once './includes/bot.php'; ?>