<?php

$table = "diviser_flotte";

include_once "../secure/connect.txt";
include_once "../script/fonctions.txt";
include_once "./fr/ordres.txt";

$flotte_selectionnee = isset($_POST['flotte_id']) ? intval($_POST['flotte_id']) : (isset($_GET['flotte_id']) ? intval($_GET['flotte_id']) : null);

// --- TRAITEMENT DES ACTIONS ---
// 1. Création d'une nouvelle division
if (isset($_POST['action']) && $_POST['action'] == 'creer_division' && !empty($_POST['nom_division']) && $flotte_selectionnee !== null) {
    $nom = mysql_real_escape_string($_POST['nom_division']);
    // Trouver le prochain NB_DIVISION pour cette flotte
    $res_max = mysql($base, "SELECT MAX(NB_DIVISION) as max_div FROM diviser_flotte WHERE NUMERO='$commandant'");
    $row_max = mysql_fetch_assoc($res_max);
    $next_div = ($row_max['max_div'] !== null) ? $row_max['max_div'] + 1 : 1;

    mysql($base, "INSERT INTO diviser_flotte (NUMERO, FLOTTE, NOM, NB_DIVISION) VALUES ('$commandant', '$flotte_selectionnee', '$nom', '$next_div')");
    header("Location: ?table=$table&flotte_id=$flotte_selectionnee");
    exit;
}

// 2. Mise à jour des quantités (Formulaire Global)
if (isset($_POST['action']) && $_POST['action'] == 'update_quantites' && $flotte_selectionnee !== null) {
    // On commence par supprimer les affectations existantes pour cette flotte
    mysql($base, "DELETE FROM diviser_flotte_ajouter WHERE NUMERO='$commandant' AND FLOTTE='$flotte_selectionnee'");

    if (isset($_POST['qte']) && is_array($_POST['qte'])) {
        foreach ($_POST['qte'] as $nb_div => $vaisseaux) {
            foreach ($vaisseaux as $type_vaisseau => $nombre) {
                $nombre = intval($nombre);
                if ($nombre > 0) {
                    $type_vaisseau_esc = mysql_real_escape_string($type_vaisseau);
                    mysql($base, "INSERT INTO diviser_flotte_ajouter (NUMERO, FLOTTE, NB_DIVISION, TYPE, NOMBRE) 
                                  VALUES ('$commandant', '$flotte_selectionnee', '$nb_div', '$type_vaisseau_esc', '$nombre')");
                }
            }
        }
    }
}

if (isset($_GET['elimine']) && $_GET['elimine'] == 0) {
    mysql($base, "DELETE FROM diviser_flotte WHERE id = {$_GET['identifier']} AND NUMERO=$commandant");
    header("Location: ?table=$table&flotte_id=$flotte_selectionnee");
    exit;
}

// On récupère la liste des flottes qui possèdent une divisions
$res_flottes = mysql($base, "SELECT DISTINCT FLOTTE FROM diviser_flotte WHERE NUMERO='$commandant' ORDER BY FLOTTE");
$flottes = [];
while ($row = mysql_fetch_assoc($res_flottes)) {
    $flottes[] = $row['FLOTTE'];
}

// on inclus après pour être sûr de ne pas avoir de double header
include "body.txt";

// --- RECUPERATION DES DONNEES ---
$t0 = base1($base, $commandant, "z_flottes"); // Liste des flottes
$t1 = base3($base, $commandant, "z_vaisseaux"); // Types de vaisseaux dispo

$divisions = [];
$affectations = [];
if ($flotte_selectionnee !== null) {
    $res1 = mysql($base, "SELECT NOM, NB_DIVISION, id FROM diviser_flotte WHERE NUMERO='$commandant' AND FLOTTE='$flotte_selectionnee' ORDER BY NB_DIVISION");
    while ($row1 = mysql_fetch_assoc($res1)) {
        $divisions[$row1['NB_DIVISION']] = [$row1['NOM'], $row1['id']];
    }
    $res2 = mysql($base, "SELECT NB_DIVISION, TYPE, NOMBRE FROM diviser_flotte_ajouter WHERE NUMERO='$commandant' AND FLOTTE='$flotte_selectionnee'");
    while ($row2 = mysql_fetch_assoc($res2)) {
        $affectations[$row2['NB_DIVISION']][$row2['TYPE']] = $row2['NOMBRE'];
    }
}
if (count($divisions) > 0) {
    $keys = array_map(function ($k) {
        return $k + 10000;
    }, array_keys($divisions));
    $searchMovementResult = mysql_query("SELECT * FROM deplacer_flotte WHERE NUMERO='$commandant' AND NUMFLOTTE IN (" . implode(",", $keys) . ")");
    while ($row = mysql_fetch_row($searchMovementResult)) {
        $divisions[$row[1] - 10000][2] = $row[2] . "-" . $row[3];
    }
}

?>

<h1>Division de flotte</h1>


<style>
    table thead tr th {
        background: #000;
    }

    table tr td:first-child {
        position: sticky;
        left: 0px;
        background-color: black;
        font-weight: bold;
        white-space: nowrap;
        z-index: 100;
    }

    td:has(input:not(.notempty)) {
        opacity: 0.3;
    }

    input:not([type=submit]) {
        line-height: 1.5em;
        font-size: 1.5em;
    }

    table td {
        border: 0;
        padding: 2px;
        background: black;
    }

    tr:nth-child(even) td {
        background: #700e91;
    }

    tr:nth-child(even) td input:not([type=submit]) {
        background: #490560;
    }
</style>

<form method="get" action="<?= $nom_page ?>" id="form_flotte" style="display: flex; gap: 5px; align-items: center;">
    <input type="hidden" name="table" value="<?= $table ?>">
    Choisir une flotte :
    <?php
    // On simule select1 mais avec un onchange pour soumettre
    echo("<SELECT SIZE=\"1\" NAME=\"flotte_id\" onchange='this.form.submit()' class='select' style='width: 200px; '>");
    echo("<OPTION VALUE=\"\">-- Sélectionner --</OPTION>");
    if (is_array($t0)) {
        foreach ($t0 as $id => $nom) {
            echo("<OPTION " . ($flotte_selectionnee == $id ? 'selected ' : '') . "VALUE=\"$id\">$nom</OPTION>");
        }
    }
    echo("</SELECT>");
    ?>
    <button type="submit">Valider</button>
    <?php if (count($flottes) > 0) { ?>
        Flottes divisées :
        <?php foreach ($flottes as $num) { ?>
            <a href="<?= $nom_page ?>?table=<?= $table ?>&flotte_id=<?= $num ?>"><?= $t0[$num] ?></a>
        <?php } ?>
    <?php } ?>
</form>

<?php if ($flotte_selectionnee !== null): ?>
    <hr>
    <h3>Nouvelle division</h3>
    <form method="post" action="<?= $nom_page ?>?table=<?= $table ?>">
        <input type="hidden" name="flotte_id" value="<?php echo $flotte_selectionnee; ?>">
        <input type="hidden" name="action" value="creer_division">
        Nom de la division : <input type="text" name="nom_division" required>
        <input type="submit" value="Ajouter">
    </form>
    <?php if (count($divisions) > 0) { ?>
        <hr>
        <form method="post">
            <input type="hidden" name="flotte_id" value="<?php echo $flotte_selectionnee; ?>">
            <input type="hidden" name="action" value="update_quantites">

            <table style="width: auto">
                <thead>
                <tr>
                    <th style="width: 150px">Vaisseau</th>
                    <?php foreach ($divisions as $nb_div => [$nom, $id]): ?>
                        <th>
                            <?php echo $nom; ?>
                            <a class="delete"
                               title="supprimer"
                               href="<?= $nom_page ?>?table=<?= $table ?>&elimine=0&identifierKey=id&identifier=<?= $id ?>&flotte_id=<?= intval($flotte_selectionnee) ?>">
                                X
                            </a>
                            <!--                               <A HREF=\"$nom_page?table=$table&elimine=$i&identifierKey=id&identifier={$rf[$idPosition]}\" class='delete'>$suppression</A> -->
                        </th>
                    <?php endforeach; ?>
                    <th style="width: 100px">Somme</th>
                </tr>
                </thead>
                <tbody>
                <?php
                if (is_array($t1)) {
                    foreach ($t1 as $type_vaisseau) {
                        echo "<tr>";
                        echo "<td>$type_vaisseau</td>";
                        $somme_ligne = 0;
                        foreach ($divisions as $nb_div => [$nom, $id]) {
                            $val = isset($affectations[$nb_div][$type_vaisseau]) ? $affectations[$nb_div][$type_vaisseau] : 0;
                            echo "<td><input type='number' name='qte[$nb_div][$type_vaisseau]' " . ($val > 0 ? "value='$val'" : "") . " " . ($val > 0 ? "class='notempty'" : "") . " min='0' style='width: 60px;'></td>";
                            $somme_ligne += $val;
                        }
                        echo "<td><strong>$somme_ligne</strong></td>";
                        echo "</tr>";
                    }
                }
                ?>
                </tbody>
                <tfoot>
                <tr>
                    <td>Déplacement prévu</td>
                    <?php foreach ($divisions as $nb_div => $data) { ?>
                        <td style="font-size: 20px;"><?php
                            if (count($data) == 3) {
                                echo $data[2];
                            } ?></td>
                    <?php } ?>
                </tr>
                </tfoot>
            </table>
            <br>
            <input type="submit" value="Enregistrer les modifications">
        </form>

    <?php } ?>

    <script>

        Array.from(document.querySelectorAll('input[type=number]')).forEach(i => {
            i.addEventListener('keyup', function () {
                const val = i.value;
                i.classList.toggle('notempty', val > 0);
            });
        });
    </script>

<?php endif; ?>

</BODY>
</HTML>