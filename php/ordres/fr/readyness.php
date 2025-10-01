<?php
//
$tour =  intval(file_get_contents("../tour.txt"));
//echo "plop $commandant $tour ";

// --- traiter action ---
const ACTION_NAME = 'imready';
if (isset($_GET[ACTION_NAME])) {
    if ($_GET[ACTION_NAME] === 'on') {
        // marquer prêt (si pas déjà présent)
        mysql($base, "INSERT IGNORE INTO _player_ready (num, tour) VALUES (" . intval($commandant) . ", $tour)");
    } elseif ($_GET[ACTION_NAME] === 'off') {
        // marquer non prêt
        mysql($base, "DELETE FROM _player_ready WHERE num=" . intval($commandant)." AND tour=$tour");
    }
}

// --- récupérer tous les commandants prêts ---
$commandants = [];
$result = mysql($base, "SELECT
   a.NUMERO,
   a.NOM,
   a.RACE
   FROM _player_ready pr 
   JOIN aa_registre a ON pr.num=a.NUMERO");

$nb_lignes = mysql_num_rows($result);

for ($i = 0; $i < $nb_lignes; $i++) {
    $rf = mysql_fetch_row($result);
    $num  = $rf[0];
    $nom  = $rf[1];
    $race = $rf[2];
    $commandants[] = ["num" => $num, "nom" => $nom, "race" => $race];
}

// --- vérifier si le commandant courant est dans la liste ---
$deja_pret = false;
foreach ($commandants as $c) {
    if ($c['num'] == $commandant) {
        $deja_pret = true;
        break;
    }
}

// --- afficher lien ---
if ($deja_pret) {
    echo '<a href="/ordres/?'.ACTION_NAME.'=off">Je ne suis pas prêt</a><br />';
} else {
    echo '<a href="/ordres/?'.ACTION_NAME.'=on">Je suis prêt !</a><br />';
}

// --- afficher la liste ---
echo "Déjà prêt : ";
foreach ($commandants as $c) {
    echo "<span class='commandant race{$c['race']}'>{$c['nom']} ({$c['num']})</span> ";
}