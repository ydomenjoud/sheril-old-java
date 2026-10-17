<?php
/**
 * Import d'un schéma d'ordres de revenus (écran « Planification des Revenus » de l'interface web).
 *
 * Schéma = base64( ordre %#!% ordre %#!% ... ), avec ordre = table:param:param...
 * Les paramètres suivent l'ordre des colonnes de la table (hors NUMERO). Voir docs/ordres-revenus.md
 * du projet test-interface-sheril.
 *
 * Les ordres sont AJOUTÉS aux ordres déjà passés : rien n'est supprimé.
 * Tout le schéma est validé avant la moindre insertion : s'il contient une erreur, aucun ordre n'est ajouté.
 */

const SCHEMA_REVENUS_SEPARATEUR = "%#!%";

/** Colonnes et contrôles de chaque table d'ordre acceptée : [colonne, type, min, max] */
function schemaRevenusTables()
{
    return array(
        "modifier_taxation_systeme" => array(array("SYSTEME", "systeme"), array("TAXE", "int", 0, 5)),
        "modifier_taxation_planete" => array(array("SYSTEME", "systeme"), array("TAXE", "int", 0, 5), array("PLANETE", "int", 0, 99)),
        "modifier_politique" => array(array("SYSTEME", "systeme"), array("POLITIQUE", "politique")),
        "modifier_budget" => array(array("SYSTEME", "systeme"), array("BUDGET", "budget"), array("POURCENTAGE", "int", 0, 100)),
        "affecter_gouverneur" => array(array("NOM", "gouverneur"), array("POSITION", "position")),
        "terraformer_systeme" => array(array("SYSTEME", "systeme")),
        "terraformer_planete" => array(array("SYSTEME", "systeme"), array("PLANETE", "int", 0, 99)),
    );
}

/** Valide et ajoute les ordres. Retourne le nombre d'ordres ajoutés ; s'arrête (die) sur la première erreur. */
function importerSchemaRevenus($base, $commandant, $table, $schemaBase64)
{
    $retour = "<br /><a href='index.php3?table=$table'>Retour</a>";
    $tables = schemaRevenusTables();

    $data = base64_decode(trim($schemaBase64), true);
    if ($data === false || $data === "") {
        die("Schéma illisible (base64 invalide)." . $retour);
    }

    // références du commandant pour contrôler les valeurs
    $systemes = base1($base, $commandant, "z_systemes");
    $gouverneurs = base3($base, $commandant, "z_gouverneurs");
    $politiques = base2($base, "z_politiques");
    $budgets = base2($base, "z_budgets");
    if (!is_array($systemes)) $systemes = array();
    if (!is_array($gouverneurs)) $gouverneurs = array();
    if (!is_array($politiques)) $politiques = array();
    if (!is_array($budgets)) $budgets = array();

    $requetes = array();
    $lignes = explode(SCHEMA_REVENUS_SEPARATEUR, $data);
    foreach ($lignes as $numero => $ligne) {
        $ou = " (ordre " . ($numero + 1) . " : " . htmlspecialchars($ligne, ENT_QUOTES, "UTF-8") . ")";
        $valeurs = explode(":", $ligne);
        $nomTable = array_shift($valeurs);
        if (!array_key_exists($nomTable, $tables)) {
            die("Ordre inconnu : " . htmlspecialchars($nomTable, ENT_QUOTES, "UTF-8") . $retour);
        }
        $colonnes = $tables[$nomTable];
        if (count($valeurs) !== count($colonnes)) {
            die("Nombre de paramètres incorrect" . $ou . ", " . count($colonnes) . " attendu(s)." . $retour);
        }

        $noms = array();
        $valeursSql = array();
        foreach ($colonnes as $i => $colonne) {
            $valeur = $valeurs[$i];
            $ok = false;
            switch ($colonne[1]) {
                case "systeme":
                    $ok = array_key_exists($valeur, $systemes);
                    break;
                case "position":
                    $ok = $valeur === "0_0_0" || array_key_exists($valeur, $systemes);
                    break;
                case "gouverneur":
                    $ok = in_array($valeur, $gouverneurs, true);
                    break;
                case "politique":
                    $ok = array_key_exists($valeur, $politiques);
                    break;
                case "budget":
                    $ok = array_key_exists($valeur, $budgets);
                    break;
                case "int":
                    $ok = preg_match('/^\d+$/', $valeur) === 1 && intval($valeur) >= $colonne[2] && intval($valeur) <= $colonne[3];
                    break;
            }
            if (!$ok) {
                die("Valeur incorrecte pour " . $colonne[0] . $ou . $retour);
            }
            $noms[] = $colonne[0];
            $valeursSql[] = "'" . mysql_real_escape_string($valeur) . "'";
        }
        $requetes[] = "INSERT INTO $nomTable (NUMERO, " . implode(", ", $noms) . ") VALUES ($commandant, " . implode(", ", $valeursSql) . ")";
    }

    foreach ($requetes as $requete) {
        mysql_query($requete) or die(mysql_error() . $retour);
    }
    return count($requetes);
}
