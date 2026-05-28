<?php
define('USE_PDO', true);
define('EMBED', true);
require_once './includes/top.php';

// Paramètres
$tourActuel = array_key_exists('tour', $_GET) ? (int)$_GET['tour'] : $numeroTour;
$tourPrecedent = $tourActuel - 1;

// Liste blanche pour le tri
$colonnes = [
    'numero', 'puissance', 'centaure', 'planetes', 'pop_syst',
    'pop_vs', 'reputation', 'rayonnement', 'technologie', 'offensif', 'pv'
];
$tri = isset($_GET['tri']) && in_array($_GET['tri'], $colonnes) ? $_GET['tri'] : 'puissance';
$ordre = isset($_GET['ordre']) && $_GET['ordre'] === 'asc' ? 'ASC' : 'DESC';

// Requête avec jointure gauche pour calculer les deltas
$sql = "SELECT 
            reg.nom, 
            reg.race,
            curr.*,
            (curr.puissance - IFNULL(prev.puissance, 0)) as d_puissance,
            (curr.centaure - IFNULL(prev.centaure, 0)) as d_centaure,
            (curr.planetes - IFNULL(prev.planetes, 0)) as d_planetes,
            (curr.pop_syst - IFNULL(prev.pop_syst, 0)) as d_pop_syst,
            (curr.pop_vs - IFNULL(prev.pop_vs, 0)) as d_pop_vs,
            (curr.reputation - IFNULL(prev.reputation, 0)) as d_reputation,
            (curr.rayonnement - IFNULL(prev.rayonnement, 0)) as d_rayonnement,
            (curr.technologie - IFNULL(prev.technologie, 0)) as d_technologie,
            (curr.offensif - IFNULL(prev.offensif, 0)) as d_offensif,
            (curr.pv - IFNULL(prev.pv, 0)) as d_pv
        FROM _statistiques curr
        JOIN aa_registre reg ON curr.numero = reg.numero
        LEFT JOIN _statistiques prev ON curr.numero = prev.numero AND prev.tour = :tourPrecedent
        WHERE curr.tour = :tourActuel
        ORDER BY $tri $ordre";

$stmt = $pdo->prepare($sql);
$stmt->execute(['tourActuel' => $tourActuel, 'tourPrecedent' => $tourPrecedent]);
$lignes = $stmt->fetchAll();

/**
 * Fonction utilitaire pour afficher une paire de cellules (Valeur | Delta)
 * @param mixed $valeur_formatted La valeur actuelle
 * @param mixed $delta La différence avec le tour précédent
 * @param bool $important Si vrai, entoure la valeur de balises strong
 */
function afficherCellules($valeur, $delta, $important = false) {
    $valeur_formatted = number_format($valeur, 0, ",", ' ');
    $vDisplay = $important ? "<strong>$valeur_formatted</strong>" : $valeur_formatted;

    // Formatage du delta
    if ($delta == 0) {
        $dDisplay = "-";
        $style = "";
    } else {
        $signe = ($delta > 0) ? "+" : "";
        if($delta === $valeur) {
            $dDisplay = '-';
            $style = "";
        } else {
            $dDisplay = $signe . number_format($delta, 0, ",", ' ');
            $style = " class='".($delta>0 ? "plus" : "moins" )."'";
        }
    }

    echo "<td class='score'>$vDisplay</td>";
    echo "<td$style>($dDisplay)</td>";
}

/**
 * Aide pour les liens de tri
 */
function trie($label, $id, $colspan=2) {
    global $tourActuel, $tri, $ordre;
    $tour = isset($_GET['tour']) ? (int)$_GET['tour'] : $tourActuel;
    $inv = (isset($_GET['tri']) && $_GET['tri'] == $id && $_GET['ordre'] == 'desc') ? 'asc' : 'desc';
    $triActuel = '';
    if($tri == $id ) {
        $triActuel = strtolower($ordre);
    }
    return "<th colspan=\"".$colspan."\" data-sort-order='$triActuel'><a href='?tour=$tour&tri=$id&ordre=$inv'>$label</a></th>";
}
?>

<h1>Statistiques du tour <?=$tourActuel?></h1>

<div style="margin: 10px">
    Tour:
    <?php
    for ($i = $numeroTour; $i > 0; $i--) {
        echo "<a href='".basename(__FILE__)."?tour=$i'>$i</a> | ";
    }
    ?>
</div>

<table class="stylized statistiques">
    <thead>
    <tr>
        <th>Tour</th>
        <th>Position</th>
        <?php echo trie('Commandant', 'numero', 1); ?>
        <?php echo trie('Puissance', 'puissance'); ?>
        <?php echo trie('Centaure', 'centaure'); ?>
        <?php echo trie('Planètes', 'planetes'); ?>
        <?php echo trie('Pop. Syst', 'pop_syst'); ?>
        <?php echo trie('Pop. VS', 'pop_vs'); ?>
        <?php echo trie('Réputation', 'reputation'); ?>
        <?php echo trie('Rayonnement', 'rayonnement'); ?>
        <?php echo trie('Technologique', 'technologie'); ?>
        <?php echo trie('Offensif', 'offensif'); ?>
        <?php echo trie('PV', 'pv'); ?>
    </tr>
    </thead>
    <tbody>
    <?php
    $i = 1;
    foreach ($lignes as $l): ?>
        <tr>
            <td style="text-align: center"><?=$tourActuel?></td>
            <td style="text-align: center"><?=($i++)?></td>
            <td>
                <a href="stats_detail.php?nums=<?=$l['numero']?>">
                <?php echo display_author($l['nom'], $l['numero'], $l['race']); ?>
                </a>
            </td>
            <?php
            // Utilisation de la fonction utilitaire pour chaque stat
            afficherCellules($l['puissance'],   $l['d_puissance']);
            afficherCellules($l['centaure'],   $l['d_centaure']);
            afficherCellules($l['planetes'],    $l['d_planetes']);
            afficherCellules($l['pop_syst'],    $l['d_pop_syst']);
            afficherCellules($l['pop_vs'],      $l['d_pop_vs']);
            afficherCellules($l['reputation'],  $l['d_reputation']);
            afficherCellules($l['rayonnement'], $l['d_rayonnement']);
            afficherCellules($l['technologie'], $l['d_technologie']);
            afficherCellules($l['offensif'],    $l['d_offensif']);
            afficherCellules($l['pv'],          $l['d_pv'], true); // PV mis en gras
            ?>
        </tr>
    <?php endforeach; ?>
    </tbody>
</table>
<?php require_once 'includes/bot.php'; ?>