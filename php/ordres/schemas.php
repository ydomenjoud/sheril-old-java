<?php
/**
 * Import des schémas (blueprints) générés par l'interface web test-interface-sheril.
 *
 * Un schéma est du base64, éventuellement précédé d'un préfixe de type : « construction|MF8y… ».
 * Sans préfixe, le type est deviné d'après le contenu (schemaDetecter).
 *
 * Types gérés : construction, deplacement, recherche, revenus (plan : voir importerSchemaPlan, hors accueil).
 * Chaque import valide tout le schéma avant la moindre insertion : sur erreur (die), rien n'est ajouté.
 * Les fonctions s'appellent depuis l'accueil des ordres (schemaImporter) comme depuis les pages d'ordres (insert.txt).
 */

include_once "schema_revenus.php";

const SCHEMA_SEPARATEUR = "%#!%";
const SCHEMA_PREFIXES = "plan|revenus|construction|deplacement|recherche";

function schemaRetour($table)
{
    return "<br /><a href='index.php3?table=$table'>Retour</a>";
}

function schemaEchapper($texte)
{
    return htmlspecialchars($texte, ENT_QUOTES, "UTF-8");
}

/** Extrait le préfixe éventuel et décode le base64. Retourne array(prefixe ou null, données décodées ou false). */
function schemaDecoder($texte)
{
    $texte = trim($texte);
    $prefixe = null;
    if (preg_match('/^(' . SCHEMA_PREFIXES . ')\|(.*)$/s', $texte, $m)) {
        $prefixe = $m[1];
        $texte = trim($m[2]);
    }
    return array($prefixe, base64_decode($texte, true));
}

/** Devine le type d'un schéma sans préfixe d'après son premier élément. Retourne null si rien ne correspond. */
function schemaDeviner($data)
{
    $elements = explode(SCHEMA_SEPARATEUR, $data);
    $premier = $elements[0];
    if (preg_match('/^\d+:\d+_\d+_\d+:/', $premier)) return "deplacement";
    if (preg_match('/^\d+_\d+_\d+:/', $premier)) return "construction";
    $tables = schemaRevenusTables();
    $nom = substr($premier, 0, strcspn($premier, ":"));
    if (array_key_exists($nom, $tables)) return "revenus";
    // code:nombre%code:nombre : recherche ou plan de vaisseau, indiscernables sur la forme
    if (preg_match('/^[^:%]+:\d+(%[^:%]+:\d+)*$/', $data)) return "recherche";
    return null;
}

/** Ajoute des ordres de construction. Format : systeme:quantité:code:planète, séparés par %#!%. */
function importerSchemaConstruction($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $systemes = base1($base, $commandant, "z_systemes");
    $possibles = base1($base, $commandant, "z_constructions_possibles");
    if (!is_array($systemes)) $systemes = array();
    if (!is_array($possibles)) $possibles = array();

    $requetes = array();
    foreach (explode(SCHEMA_SEPARATEUR, $data) as $ligne) {
        $informations = explode(":", $ligne);
        if (count($informations) < 3) {
            die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
        }
        $systeme = $informations[0];
        $quantite = intval($informations[1]);
        $code = $informations[2];
        $planete = isset($informations[3]) ? intval($informations[3]) : 0;
        if (!array_key_exists($code, $possibles)) {
            die("Technologie inconnue : " . schemaEchapper($code) . $retour);
        }
        if (!array_key_exists($systeme, $systemes)) {
            die("Système inconnu : " . schemaEchapper($systeme) . $retour);
        }
        $systeme = mysql_real_escape_string($systeme);
        $code = mysql_real_escape_string($code);
        $requetes[] = "INSERT INTO construire (NUMERO, SYSTEME, CONSTRUCTION, NOMBRE, PLANETE)
VALUES ($commandant, '$systeme', '$code', $quantite, $planete)";
    }
    return schemaExecuter($requetes, $retour);
}

/** Ajoute des déplacements de flotte. Format : flotte:galaxie_x_y:directive:stratégie, séparés par %#!%. */
function importerSchemaDeplacement($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $flottes = fusion_tab2(base1($base, $commandant, "z_flottes"), base_flotte($base, $commandant));
    $galaxies = base2($base, "z_galaxies");
    $directives = base2($base, "z_directives");
    $strategies = base3($base, $commandant, "z_strategies");

    $requetes = array();
    foreach (explode(SCHEMA_SEPARATEUR, $data) as $ligne) {
        $informations = explode(":", $ligne);
        $position = explode("_", isset($informations[1]) ? $informations[1] : "");
        if (count($informations) < 3 || count($position) != 3) {
            die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
        }
        $flotte = intval($informations[0]);
        $galaxie = intval($position[0]);
        $x = intval($position[1]);
        $y = intval($position[2]);
        $directive = intval($informations[2]);
        $strategie = isset($informations[3]) && $informations[3] !== "" ? $informations[3] : "par défaut";

        if (!is_array($flottes) || !array_key_exists($flotte, $flottes)) {
            die("Flotte inconnue : $flotte" . $retour);
        }
        if (!is_array($galaxies) || !array_key_exists($galaxie, $galaxies)) {
            die("Galaxie inconnue : $galaxie" . $retour);
        }
        if (!is_array($directives) || !array_key_exists($directive, $directives)) {
            die("Directive inconnue : $directive" . $retour);
        }
        if ($strategie !== "par défaut" && (!is_array($strategies) || !in_array($strategie, $strategies))) {
            die("Stratégie inconnue : " . schemaEchapper($strategie) . $retour);
        }
        $strategie = mysql_real_escape_string($strategie);
        $requetes[] = "INSERT INTO deplacer_flotte (NUMERO, NUMFLOTTE, POSX, POSY, GALAXIE, DIRECTIVE, STRATEGIE)
VALUES ($commandant, $flotte, $x, $y, $galaxie, $directive, '$strategie')";
    }
    return schemaExecuter($requetes, $retour);
}

/**
 * REMPLACE toutes les affectations de recherche. Format : technologie:pourcentage, séparés par %.
 * Le total des pourcentages ne doit pas dépasser 100.
 */
function importerSchemaRecherche($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $technologies = base1($base, $commandant, "z_technologies_cherchees");
    if (!is_array($technologies)) $technologies = array();

    $requetes = array();
    $total = 0;
    foreach (explode("%", $data) as $ligne) {
        $composant = explode(":", $ligne);
        if (count($composant) !== 2 || !preg_match('/^\d+$/', $composant[1])) {
            die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
        }
        if (!array_key_exists($composant[0], $technologies)) {
            die("Technologie non recherchable : " . schemaEchapper($composant[0])
                . " (s'agit-il d'un plan de vaisseau ?)" . $retour);
        }
        $pourcentage = intval($composant[1]);
        $total += $pourcentage;
        $code = mysql_real_escape_string($composant[0]);
        $requetes[] = "INSERT INTO affecter_recherche (NUMERO, TECHNOLOGIE, AFFECTATION) VALUES ($commandant, '$code', $pourcentage)";
    }
    if ($total > 100) {
        die("Le total des pourcentages de recherche dépasse 100 ($total)." . $retour);
    }
    mysql_query("DELETE FROM affecter_recherche WHERE NUMERO=$commandant") or die(mysql_error() . $retour);
    return schemaExecuter($requetes, $retour);
}

/** Ajoute des composants à un plan de vaisseau en cours de création. Format : composant:nombre, séparés par %. */
function importerSchemaPlan($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $connues = array_keys(base1($base, $commandant, "z_composants"));

    $requetes = array();
    foreach (explode("%", $data) as $ligne) {
        $composant = explode(":", $ligne);
        if (!in_array($composant[0], $connues)) {
            die("Technologie inconnue : " . schemaEchapper($composant[0]) . $retour);
        }
        $nb = isset($composant[1]) ? intval($composant[1]) : 0;
        $tech = mysql_real_escape_string($composant[0]);
        $requetes[] = "INSERT INTO creer_plan_ajouter (NUMERO, NOM, NOMBRE) VALUES ($commandant, '$tech', $nb)";
    }
    return schemaExecuter($requetes, $retour);
}

function schemaExecuter($requetes, $retour)
{
    foreach ($requetes as $requete) {
        mysql_query($requete) or die(mysql_error() . $retour);
    }
    return count($requetes);
}

/**
 * Point d'entrée de l'accueil : détecte le type du schéma (préfixe, sinon contenu) et l'importe.
 * Les plans de vaisseau sont refusés ici. Retourne array(type, nombre d'ordres).
 */
function schemaImporter($base, $commandant, $texte)
{
    $retour = schemaRetour("");
    list($prefixe, $data) = schemaDecoder($texte);
    if ($data === false || $data === "") {
        die("Schéma illisible (base64 invalide)." . $retour);
    }

    $type = $prefixe !== null ? $prefixe : schemaDeviner($data);
    if ($type === null) {
        die("Type de schéma non reconnu." . $retour);
    }

    switch ($type) {
        case "construction":
            return array($type, importerSchemaConstruction($base, $commandant, "", $data));
        case "deplacement":
            return array($type, importerSchemaDeplacement($base, $commandant, "", $data));
        case "recherche":
            return array($type, importerSchemaRecherche($base, $commandant, "", $data));
        case "revenus":
            return array($type, importerSchemaRevenus($base, $commandant, "", base64_encode($data)));
        default:
            die("Les plans de vaisseau ne s'importent pas ici : utilisez l'ordre « Créer un plan »." . $retour);
    }
}
