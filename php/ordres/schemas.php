<?php
/**
 * Import des schémas (blueprints) générés par l'interface web test-interface-sheril.
 *
 * Un schéma est du base64, éventuellement précédé d'un préfixe de type : « construction|MF8y… ».
 * Sans préfixe, le type est deviné d'après le contenu (schemaDetecter).
 *
 * Types gérés : construction, deplacement, flotte, recherche, revenus, transfert (plan : voir importerSchemaPlan, hors accueil).
 * Chaque import valide tout le schéma avant la moindre insertion : sur erreur (die), rien n'est ajouté.
 * Les fonctions s'appellent depuis l'accueil des ordres (schemaImporter) comme depuis les pages d'ordres (insert.txt).
 */

include_once "schema_revenus.php";

const SCHEMA_SEPARATEUR = "%#!%";
const SCHEMA_PREFIXES = "plan|revenus|construction|deplacement|recherche|transfert|flotte";

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
    // avant « construction », qui commence aussi par un système
    if (preg_match('/^\d+_\d+_\d+:[^:]+:\d+:\d+:\d+_\d+_\d+:\d+$/', $premier)) return "transfert";
    if (preg_match('/^\d+_\d+_\d+:/', $premier)) return "construction";
    $tables = schemaRevenusTables();
    $nom = substr($premier, 0, strcspn($premier, ":"));
    if (in_array($nom, array("diviser_flotte", "fusionner_flotte", "deplacer_flotte"), true)) return "flotte";
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
        // planète 0-based, 100 = non précisée (absente ou vide dans le schéma)
        $planete = (isset($informations[3]) && trim($informations[3]) !== "") ? intval($informations[3]) : 100;
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
 * Ajoute des transferts entre systèmes (table charger_cargo).
 * Format : système départ:objet:nombre:planète départ:système arrivée:planète arrivée, séparés par %#!%.
 * Objet = minerai, n° de marchandise ou code de bâtiment (clés de z_cargaison_chargement) ;
 * planète = n° 0-based, 100 = non précisée (comme select_planete2).
 */
function importerSchemaTransfert($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $systemes = base1($base, $commandant, "z_systemes");
    $objets = base1($base, $commandant, "z_cargaison_chargement");
    if (!is_array($systemes)) $systemes = array();
    if (!is_array($objets)) $objets = array();

    $requetes = array();
    foreach (explode(SCHEMA_SEPARATEUR, $data) as $ligne) {
        $informations = explode(":", $ligne);
        if (count($informations) != 6) {
            die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
        }
        list($depart, $objet, $nombre, $planeteDepart, $arrivee, $planeteArrivee) = $informations;
        if (!array_key_exists($depart, $systemes)) {
            die("Système de départ inconnu : " . schemaEchapper($depart) . $retour);
        }
        if (!array_key_exists($arrivee, $systemes)) {
            die("Système d'arrivée inconnu : " . schemaEchapper($arrivee) . $retour);
        }
        if (!array_key_exists($objet, $objets)) {
            die("Objet non transférable : " . schemaEchapper($objet) . $retour);
        }
        if (!preg_match('/^\d+$/', $nombre) || intval($nombre) <= 0) {
            die("Quantité invalide : " . schemaEchapper($ligne) . $retour);
        }
        foreach (array($planeteDepart, $planeteArrivee) as $planete) {
            if (!preg_match('/^\d+$/', $planete) || (intval($planete) > 19 && intval($planete) != 100)) {
                die("Planète invalide : " . schemaEchapper($ligne) . $retour);
            }
        }
        $depart = mysql_real_escape_string($depart);
        $arrivee = mysql_real_escape_string($arrivee);
        $objet = mysql_real_escape_string($objet);
        $nombre = intval($nombre);
        $planeteDepart = intval($planeteDepart);
        $planeteArrivee = intval($planeteArrivee);
        $requetes[] = "INSERT INTO charger_cargo (NUMERO, SYSTEME, CONSTRUCTION, NOMBRE, PLANETE, SYSTEME2, PLANETE2)
VALUES ($commandant, '$depart', '$objet', $nombre, $planeteDepart, '$arrivee', $planeteArrivee)";
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

/**
 * Ajoute des divisions, des déplacements de flottes divisées et des fusions (schéma « flotte| »).
 * Un ordre « table:param:… » par enregistrement, séparés par %#!% :
 *   diviser_flotte:flotte:nom:div.N          nouvelle flotte « div.N » (N = n° local au schéma)
 *   diviser_flotte_ajouter:div.N:plan:nombre vaisseaux détachés dans div.N
 *   deplacer_flotte:flotte:galaxie_x_y:directive:stratégie
 *   fusionner_flotte:flotte gardée:flotte absorbée:directive
 * Une flotte est un n° de flotte (0-based, ou 10000 + n° d'une division déjà saisie) ou une référence
 * « div.N » d'une division de ce schéma. La console numérote elle-même les divisions (le plus grand n° du
 * commandant + 1, dans l'ordre du schéma) et remplace chaque « div.N » par 10000 + n° attribué.
 */
function importerSchemaFlotte($base, $commandant, $table, $data)
{
    $retour = schemaRetour($table);
    $flottesRapport = base1($base, $commandant, "z_flottes");
    if (!is_array($flottesRapport)) $flottesRapport = array();
    $flottes = fusion_tab2($flottesRapport, base_flotte($base, $commandant));
    $vaisseaux = base3($base, $commandant, "z_vaisseaux");
    if (!is_array($vaisseaux)) $vaisseaux = array();
    $galaxies = base2($base, "z_galaxies");
    $directives = base2($base, "z_directives");
    $strategies = base3($base, $commandant, "z_strategies");

    $lignes = array();
    foreach (explode(SCHEMA_SEPARATEUR, $data) as $ligne) {
        $lignes[] = array($ligne, explode(":", $ligne));
    }

    // 1er passage : les divisions, numérotées à la suite de celles déjà saisies
    $resultat = mysql_query("SELECT MAX(NB_DIVISION) FROM diviser_flotte WHERE NUMERO=$commandant") or die(mysql_error() . $retour);
    $max = mysql_fetch_row($resultat);
    $prochain = $max[0] !== null ? intval($max[0]) + 1 : 1;
    $divisions = array(); // « div.N » => array(flotte divisée, n° attribué, nombre de vaisseaux)
    foreach ($lignes as $l) {
        list($ligne, $informations) = $l;
        if ($informations[0] !== "diviser_flotte") continue;
        if (count($informations) != 4 || !preg_match('/^div\.\d+$/', $informations[3])) {
            die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
        }
        list(, $flotte, $nom, $reference) = $informations;
        if (!preg_match('/^\d+$/', $flotte) || !array_key_exists(intval($flotte), $flottesRapport)) {
            die("Flotte inconnue : " . schemaEchapper($flotte) . $retour);
        }
        if (trim($nom) === "" || strlen($nom) > 100) {
            die("Nom de flotte invalide : " . schemaEchapper($ligne) . $retour);
        }
        if (array_key_exists($reference, $divisions)) {
            die("Division en double : " . schemaEchapper($reference) . $retour);
        }
        $divisions[$reference] = array(intval($flotte), $prochain, 0);
        $prochain++;
    }

    // Désignation d'une flotte dans un ordre : référence div.N de ce schéma, ou n° de flotte existante
    $resoudre = function ($valeur, $ligne) use ($divisions, $flottes, $retour) {
        if (preg_match('/^div\.\d+$/', $valeur)) {
            if (!array_key_exists($valeur, $divisions)) {
                die("Division inconnue dans ce schéma : " . schemaEchapper($valeur) . $retour);
            }
            return 10000 + $divisions[$valeur][1];
        }
        if (!preg_match('/^\d+$/', $valeur) || !is_array($flottes) || !array_key_exists(intval($valeur), $flottes)) {
            die("Flotte inconnue : " . schemaEchapper($valeur) . " (" . schemaEchapper($ligne) . ")" . $retour);
        }
        return intval($valeur);
    };
    $verifierDirective = function ($directive, $ligne) use ($directives, $retour) {
        if (!preg_match('/^\d+$/', $directive) || !is_array($directives) || !array_key_exists(intval($directive), $directives)) {
            die("Directive inconnue : " . schemaEchapper($directive) . " (" . schemaEchapper($ligne) . ")" . $retour);
        }
        return intval($directive);
    };

    // 2e passage : validation de tous les ordres, puis insertion
    $requetes = array();
    foreach ($lignes as $l) {
        list($ligne, $informations) = $l;
        switch ($informations[0]) {
            case "diviser_flotte":
                list(, $flotte, $nom, $reference) = $informations;
                $nom = mysql_real_escape_string($nom);
                $requetes[] = "INSERT INTO diviser_flotte (NUMERO, FLOTTE, NOM, NB_DIVISION)
VALUES ($commandant, " . $divisions[$reference][0] . ", '$nom', " . $divisions[$reference][1] . ")";
                break;
            case "diviser_flotte_ajouter":
                if (count($informations) != 4) {
                    die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
                }
                list(, $reference, $plan, $nombre) = $informations;
                if (!array_key_exists($reference, $divisions)) {
                    die("Division inconnue dans ce schéma : " . schemaEchapper($reference) . $retour);
                }
                if (!in_array($plan, $vaisseaux)) {
                    die("Type de vaisseau inconnu : " . schemaEchapper($plan) . $retour);
                }
                if (!preg_match('/^\d+$/', $nombre) || intval($nombre) <= 0) {
                    die("Nombre de vaisseaux invalide : " . schemaEchapper($ligne) . $retour);
                }
                $divisions[$reference][2] += intval($nombre);
                $plan = mysql_real_escape_string($plan);
                $requetes[] = "INSERT INTO diviser_flotte_ajouter (NUMERO, FLOTTE, NB_DIVISION, TYPE, NOMBRE)
VALUES ($commandant, " . $divisions[$reference][0] . ", " . $divisions[$reference][1] . ", '$plan', " . intval($nombre) . ")";
                break;
            case "deplacer_flotte":
                $position = explode("_", isset($informations[2]) ? $informations[2] : "");
                if (count($informations) < 4 || count($position) != 3) {
                    die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
                }
                $flotte = $resoudre($informations[1], $ligne);
                $galaxie = intval($position[0]);
                if (!is_array($galaxies) || !array_key_exists($galaxie, $galaxies)) {
                    die("Galaxie inconnue : $galaxie" . $retour);
                }
                $x = intval($position[1]);
                $y = intval($position[2]);
                $directive = $verifierDirective($informations[3], $ligne);
                $strategie = isset($informations[4]) && $informations[4] !== "" ? $informations[4] : "par défaut";
                if ($strategie !== "par défaut" && (!is_array($strategies) || !in_array($strategie, $strategies))) {
                    die("Stratégie inconnue : " . schemaEchapper($strategie) . $retour);
                }
                $strategie = mysql_real_escape_string($strategie);
                $requetes[] = "INSERT INTO deplacer_flotte (NUMERO, NUMFLOTTE, POSX, POSY, GALAXIE, DIRECTIVE, STRATEGIE)
VALUES ($commandant, $flotte, $x, $y, $galaxie, $directive, '$strategie')";
                break;
            case "fusionner_flotte":
                if (count($informations) != 4) {
                    die("Schéma invalide : " . schemaEchapper($ligne) . $retour);
                }
                $flotte1 = $resoudre($informations[1], $ligne);
                $flotte2 = $resoudre($informations[2], $ligne);
                if ($flotte1 === $flotte2) {
                    die("Une flotte ne peut pas fusionner avec elle-même : " . schemaEchapper($ligne) . $retour);
                }
                $directive = $verifierDirective($informations[3], $ligne);
                $requetes[] = "INSERT INTO fusionner_flotte (NUMERO, FLOTTE_1, FLOTTE_2, DIRECTIVE)
VALUES ($commandant, $flotte1, $flotte2, $directive)";
                break;
            default:
                die("Ordre non pris en charge dans un schéma de flottes : " . schemaEchapper($ligne) . $retour);
        }
    }
    foreach ($divisions as $reference => $division) {
        if ($division[2] === 0) {
            die("La division " . schemaEchapper($reference) . " ne détache aucun vaisseau." . $retour);
        }
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
        case "transfert":
            return array($type, importerSchemaTransfert($base, $commandant, "", $data));
        case "flotte":
            return array($type, importerSchemaFlotte($base, $commandant, "", $data));
        case "revenus":
            return array($type, importerSchemaRevenus($base, $commandant, "", base64_encode($data)));
        default:
            die("Les plans de vaisseau ne s'importent pas ici : utilisez l'ordre « Créer un plan »." . $retour);
    }
}
