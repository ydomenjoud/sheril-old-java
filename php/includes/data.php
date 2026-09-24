<?php

require_once __DIR__ . '/Parsedown.php';

$file_path = __DIR__ . '/../tour.txt';
$tour_information = "";
if (file_exists($file_path)) {
    Data::$tourNumber = intval(trim(file_get_contents($file_path)));
    Data::$tourLastDate = date("d/m/Y", filemtime($file_path));
}

class Data
{
    static $tourNumber = 12;
    static $tourLastDate = '';

    static $pdo = null;

    // Noms des races, indexés par leur numéro (colonne RACE de aa_registre)
    static $races = [
        0 => 'Fremens',
        1 => 'Atalantes',
        2 => 'Zwaias',
        3 => 'Yoksors',
        4 => 'Fergoks',
        5 => 'Cyborgs',
    ];

    /**
     * Registre des commandants : liste triée par numéro + effectif par race.
     * L'adresse e-mail (ADRESSE) n'est volontairement pas lue.
     */
    static function getRegistre()
    {
        $stmt = self::$pdo->query('SELECT NOM, RACE, NUMERO, TOUR_ARRIVEE FROM aa_registre ORDER BY NUMERO ASC');

        $joueurs = [];
        $effectifs = array_fill_keys(array_keys(self::$races), 0);
        foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $row) {
            $race = (int) $row['RACE'];
            $joueurs[] = [
                'nom' => ucfirst($row['NOM']),
                'numero' => (int) $row['NUMERO'],
                'race' => $race,
                'raceNom' => isset(self::$races[$race]) ? self::$races[$race] : 'Inconnue (' . $race . ')',
                'tourArrivee' => (int) $row['TOUR_ARRIVEE'],
            ];
            if (isset($effectifs[$race])) $effectifs[$race]++;
        }

        // Répartition par race, sans les races absentes de la partie
        $races = [];
        foreach ($effectifs as $id => $nb) {
            if ($nb > 0) $races[] = ['id' => $id, 'nom' => self::$races[$id], 'nb' => $nb];
        }

        return ['joueurs' => $joueurs, 'races' => $races, 'total' => count($joueurs)];
    }

    // Races jouables à l'inscription : image de la fiche et page du lore
    static $racesJouables = [
        0 => ['img' => '/assets/img/lore/fremen-soldat.jpeg', 'lore' => '/lore/fremen'],
        1 => ['img' => '/assets/img/lore/atalante-soldat.jpeg', 'lore' => '/lore/atalante'],
        2 => ['img' => '/assets/img/lore/zwaia-combat.jpeg', 'lore' => '/lore/zwaia'],
        3 => ['img' => '/assets/img/lore/yoksor-soldat.jpeg', 'lore' => '/lore/yoksor'],
        4 => ['img' => '/assets/img/lore/fergok-soldat.jpeg', 'lore' => '/lore/fergok'],
    ];

    /**
     * Données de la page d'inscription : races proposées + inscriptions en attente
     * (elles deviennent des commandants à la prochaine résolution).
     */
    static function getInscription()
    {
        $choix = [];
        foreach (self::$racesJouables as $id => $race) {
            $choix[] = ['id' => $id, 'nom' => self::$races[$id], 'img' => $race['img'], 'lore' => $race['lore']];
        }

        $stmt = self::$pdo->query('SELECT NOM, RACE FROM aa_inscription ORDER BY date_insertion ASC');
        $attente = [];
        $effectifs = array_fill_keys(array_keys(self::$races), 0);
        foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $row) {
            $race = (int) $row['RACE'];
            $attente[] = [
                'nom' => ucfirst($row['NOM']),
                'race' => $race,
                'raceNom' => isset(self::$races[$race]) ? self::$races[$race] : 'Inconnue (' . $race . ')',
            ];
            if (isset($effectifs[$race])) $effectifs[$race]++;
        }
        $races = [];
        foreach ($effectifs as $id => $nb) {
            if ($nb > 0) $races[] = ['id' => $id, 'nom' => self::$races[$id], 'nb' => $nb];
        }

        return ['races' => $choix, 'attente' => $attente, 'attenteRaces' => $races, 'attenteTotal' => count($attente)];
    }

    /**
     * Valide et enregistre une inscription (table aa_inscription).
     * Renvoie la liste des erreurs, vide si l'inscription est enregistrée.
     */
    static function inscrire($nom, $email, $race, $mj)
    {
        $nom = trim($nom);
        $email = trim($email);
        $erreurs = [];

        if (!preg_match('/^[a-zA-Z0-9_ ]{3,32}$/', $nom)) {
            $erreurs[] = 'Le nom de commandant doit faire de 3 à 32 caractères : lettres sans accent, chiffres, espace ou _.';
        }
        if (!filter_var($email, FILTER_VALIDATE_EMAIL) || strlen($email) > 100) {
            $erreurs[] = "L'adresse e-mail n'est pas valide.";
        }
        if (!ctype_digit((string) $race) || !isset(self::$racesJouables[(int) $race])) {
            $erreurs[] = 'Choisissez une race.';
        }
        if (strtolower(trim($mj)) !== 'myst') {
            $erreurs[] = "Ce n'est pas le pseudo du MJ.";
        }
        if ($erreurs) return $erreurs;

        // Nom déjà pris par un commandant ou une inscription en attente
        $stmt = self::$pdo->prepare(
            'SELECT (SELECT COUNT(*) FROM aa_registre WHERE LOWER(NOM) = LOWER(:n1))
                  + (SELECT COUNT(*) FROM aa_inscription WHERE LOWER(NOM) = LOWER(:n2))'
        );
        $stmt->execute(['n1' => $nom, 'n2' => $nom]);
        if ($stmt->fetchColumn() > 0) {
            $erreurs[] = 'Ce nom de commandant est déjà pris.';
        }

        // Une seule inscription en attente par adresse
        $stmt = self::$pdo->prepare('SELECT COUNT(*) FROM aa_inscription WHERE LOWER(ADRESSE) = LOWER(:e)');
        $stmt->execute(['e' => $email]);
        if ($stmt->fetchColumn() > 0) {
            $erreurs[] = 'Une inscription est déjà en attente avec cette adresse e-mail.';
        }
        if ($erreurs) return $erreurs;

        $stmt = self::$pdo->prepare('INSERT INTO aa_inscription (NOM, ADRESSE, RACE, FLOTTE) VALUES (:nom, :email, :race, NULL)');
        $stmt->execute(['nom' => $nom, 'email' => $email, 'race' => (int) $race]);

        return [];
    }

    static function getHomeData()
    {

        $pdo = self::$pdo;
        // Paramètres
        $tourActuel = DATA::$tourNumber;
        $tourPrecedent = $tourActuel - 1;

        // sql
        $sql = "
WITH stats_deltas AS (
    SELECT
        reg.nom,
        reg.numero,
        reg.race,
        (curr.centaure - IFNULL(prev.centaure, 0)) AS d_centaure,
        (curr.planetes - IFNULL(prev.planetes, 0)) AS d_planetes,
        (curr.pop_syst - IFNULL(prev.pop_syst, 0)) AS d_pop_syst,
        (curr.pv - IFNULL(prev.pv, 0))             AS d_pv
    FROM _statistiques curr
    JOIN aa_registre reg ON curr.numero = reg.numero
    LEFT JOIN _statistiques prev ON curr.numero = prev.numero AND prev.tour = :tourPrecedent
    WHERE curr.tour = :tourActuel
),
stats_ranked AS (
    SELECT *,
        -- Centaures
        ROW_NUMBER() OVER (ORDER BY d_centaure DESC) AS rank_top_centaure,
        ROW_NUMBER() OVER (ORDER BY d_centaure ASC)  AS rank_flop_centaure,
        -- Planètes
        ROW_NUMBER() OVER (ORDER BY d_planetes DESC) AS rank_top_planetes,
        ROW_NUMBER() OVER (ORDER BY d_planetes ASC)  AS rank_flop_planetes,
        -- Pop Syst
        ROW_NUMBER() OVER (ORDER BY d_pop_syst DESC) AS rank_top_pop_syst,
        ROW_NUMBER() OVER (ORDER BY d_pop_syst ASC)  AS rank_flop_pop_syst,
        -- PV
        ROW_NUMBER() OVER (ORDER BY d_pv DESC)       AS rank_top_pv,
        ROW_NUMBER() OVER (ORDER BY d_pv ASC)        AS rank_flop_pv
    FROM stats_deltas
)
SELECT *
FROM stats_ranked
WHERE rank_top_centaure <= 5 OR rank_flop_centaure <= 5
   OR rank_top_planetes <= 5 OR rank_flop_planetes <= 5
   OR rank_top_pop_syst  <= 5 OR rank_flop_pop_syst  <= 5
   OR rank_top_pv        <= 5 OR rank_flop_pv        <= 5;
";

        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            'tourActuel' => $tourActuel,
            'tourPrecedent' => $tourPrecedent
        ]);

        $donnees = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $criteres = ['centaure', 'planetes', 'pop_syst', 'pv'];
        $labels = [
            'centaure' => 'Centaures',
            'planetes' => 'Planètes',
            'pop_syst' => 'Population',
            'pv' => 'Points de Victoire',
        ];
        $classements = [];

        foreach ($criteres as $critere) {
            $keyTop = "rank_top_" . $critere;
            $keyFlop = "rank_flop_" . $critere;

            // --- TOP 5 ---
            $top = array_filter($donnees, function ($row) use ($keyTop) {
                return $row[$keyTop] <= 5;
            });
            usort($top, function ($a, $b) use ($keyTop) {
                if ($a[$keyTop] == $b[$keyTop]) return 0;
                return ($a[$keyTop] < $b[$keyTop]) ? -1 : 1;
            });

            // --- FLOP 5 ---
            $flop = array_filter($donnees, function ($row) use ($keyFlop) {
                return $row[$keyFlop] <= 5;
            });
            usort($flop, function ($a, $b) use ($keyFlop) {
                if ($a[$keyFlop] == $b[$keyFlop]) return 0;
                return ($a[$keyFlop] < $b[$keyFlop]) ? -1 : 1;
            });

            $classements[$critere] = [
                'top' => array_values($top),
                'flop' => array_values($flop)
            ];
        }

        $sqlVictoire = "
WITH totaux_galaxie AS (
    -- 1. Calcul du total global de la galaxie pour le tour actuel
    SELECT 
        SUM(pop_syst) AS total_pop_galaxie,
        SUM(planetes) AS total_planetes_galaxie
    FROM _statistiques
    WHERE tour = :tourActuel
),
stats_victoire AS (
    -- 2. Calcul du pourcentage détenu par chaque joueur
    SELECT 
        reg.nom,
        reg.race,
        reg.numero,
        curr.pop_syst,
        curr.planetes,
        (curr.pop_syst / tot.total_pop_galaxie) * 100 AS pct_age_dor,
        (curr.planetes / tot.total_planetes_galaxie) * 100 AS pct_empire_galactique
    FROM _statistiques curr
    JOIN aa_registre reg ON curr.numero = reg.numero
    CROSS JOIN totaux_galaxie tot
    WHERE curr.tour = :tourActuel
),
stats_victoire_ranked AS (
    -- 3. Attributions des rangs Top 5
    SELECT *,
        ROW_NUMBER() OVER (ORDER BY pct_age_dor DESC)           AS rank_age_dor,
        ROW_NUMBER() OVER (ORDER BY pct_empire_galactique DESC) AS rank_empire_galactique
    FROM stats_victoire
)
-- 4. Filtrage des Top 5
SELECT * 
FROM stats_victoire_ranked
WHERE rank_age_dor <= 5 
   OR rank_empire_galactique <= 5;
   ";
        $stmtVictoire = $pdo->prepare($sqlVictoire);
        $stmtVictoire->execute(['tourActuel' => $tourActuel]);
        $donneesVictoire = $stmtVictoire->fetchAll(PDO::FETCH_ASSOC);

        // Tri et extraction du Top 5 Âge d'or
        $topAgeDor = array_filter($donneesVictoire, function ($row) {
            return $row['rank_age_dor'] <= 5;
        });
        usort($topAgeDor, function ($a, $b) {
            return ($a['rank_age_dor'] < $b['rank_age_dor']) ? -1 : 1;
        });

        // Tri et extraction du Top 5 Empire Galactique
        $topEmpire = array_filter($donneesVictoire, function ($row) {
            return $row['rank_empire_galactique'] <= 5;
        });
        usort($topEmpire, function ($a, $b) {
            return ($a['rank_empire_galactique'] < $b['rank_empire_galactique']) ? -1 : 1;
        });


        // 1. Forums à cibler
        $sql_recent = "SELECT 
                    IF(p.id_parent IS NULL OR p.id_parent = 0, p.id_post, p.id_parent) AS target_topic_id,
                    MAX(p.id_post) AS last_post_id,
                    MAX(p.record) AS max_record,
                    COALESCE(p_parent.title, p.title) AS topic_title,
                    f.name AS forum_name,
                    f.id_forum,
                    -- Informations sur le dernier auteur du sujet
                    SUBSTRING_INDEX(GROUP_CONCAT(r.NOM ORDER BY p.record DESC, p.id_post DESC), ',', 1) AS NOM,
                    SUBSTRING_INDEX(GROUP_CONCAT(r.NUMERO ORDER BY p.record DESC, p.id_post DESC), ',', 1) AS NUMERO,
                    SUBSTRING_INDEX(GROUP_CONCAT(r.RACE ORDER BY p.record DESC, p.id_post DESC), ',', 1) AS RACE
                FROM _post p
                INNER JOIN _forum f ON (p.id_forum = f.id_forum)
                LEFT JOIN _post p_parent ON (p.id_parent = p_parent.id_post)
                LEFT JOIN aa_registre r ON (r.NUMERO = p.id_author)
                GROUP BY target_topic_id, topic_title, forum_name, f.id_forum
                ORDER BY max_record DESC
                LIMIT 6";

        // 3. Exécution PDO
        $stmt_recent = $pdo->prepare($sql_recent);
        $stmt_recent->execute();
        $recentMessages = $stmt_recent->fetchAll(PDO::FETCH_ASSOC);

        return [
            'classements'     => $classements,
            'labels'          => $labels,
            'topAgeDor'       => $topAgeDor,
            'topEmpire'       => $topEmpire,
            'recentMessages'  => $recentMessages,
        ];

    }

    /**
     * Liens de la page statistiques (repris de l'ancien stats.php) : pages générées par
     * le moteur dans stats/ + archive zip de chaque tour.
     */
    static function getStatsLiens()
    {
        $groupes = [
            ['titre' => 'Classement', 'liens' => [
                ['label' => 'Classement général', 'url' => '/statistiques/general'],
                ['label' => 'Classement des PV', 'url' => '/stats/points_victoire.htm'],
                ['label' => 'Détail des PV', 'url' => '/stats/points_victoire_detail.htm'],
            ]],
            ['titre' => 'Militaire', 'liens' => [
                ['label' => 'Top 10 flottes', 'url' => '/stats/flotte.htm'],
                ['label' => 'Top 10 leaders', 'url' => '/stats/leaders.htm'],
                ['label' => 'Liste des combats', 'url' => '/stats/combats.htm'],
                ['label' => 'Plans de vaisseaux', 'url' => '/stats/vapub.htm'],
                ['label' => 'Statistiques vaisseaux', 'url' => '/stats/vaisseaux.htm'],
            ]],
            ['titre' => 'Divers', 'liens' => [
                ['label' => 'Liste des alliances', 'url' => '/stats/alliances.htm'],
                ['label' => 'Carte de la galaxie', 'url' => '/stats/carte.html'],
                ['label' => 'Enchères lieutenants', 'url' => '/stats/encheres.htm'],
                ['label' => 'Taux des postes commerciaux', 'url' => '/stats/taux_poste.htm'],
                ['label' => 'Statistiques univers', 'url' => '/stats/univers.htm'],
                ['label' => 'Événements publics', 'url' => '/stats/evt.htm'],
            ]],
        ];

        $archives = [];
        for ($i = self::$tourNumber; $i >= 1; $i--) {
            $archives[] = ['label' => 'T' . str_pad($i, 2, '0', STR_PAD_LEFT), 'url' => '/stats/statsT' . $i . '.zip'];
        }
        return ['groupes' => $groupes, 'archives' => $archives];
    }

    // Colonnes du classement général : clé SQL => libellé
    static $statsColonnes = [
        'puissance' => 'Puissance',
        'centaure' => 'Centaures',
        'planetes' => 'Planètes',
        'pop_syst' => 'Pop. syst.',
        'pop_vs' => 'Pop. VS',
        'reputation' => 'Réputation',
        'rayonnement' => 'Rayonnement',
        'technologie' => 'Technologie',
        'offensif' => 'Offensif',
        'pv' => 'PV',
    ];

    /**
     * Classement général d'un tour avec l'écart au tour précédent (repris de stats_general.php).
     * $tri : une colonne de $statsColonnes, 'numero', ou 'd_<colonne>' pour trier sur l'écart.
     */
    static function getStatsGeneral($tour, $tri, $ordre)
    {
        $tours = array_map('intval', self::$pdo->query('SELECT DISTINCT tour FROM _statistiques ORDER BY tour DESC')->fetchAll(PDO::FETCH_COLUMN));
        if (!in_array((int) $tour, $tours, true)) {
            $tour = in_array(self::$tourNumber, $tours, true) ? self::$tourNumber : ($tours ? $tours[0] : 0);
        }
        $tour = (int) $tour;

        // Tri : liste blanche (le nom de colonne est inséré dans la requête)
        $triables = ['numero'];
        foreach (array_keys(self::$statsColonnes) as $c) { $triables[] = $c; $triables[] = 'd_' . $c; }
        if (!in_array($tri, $triables, true)) $tri = 'puissance';
        $ordre = $ordre === 'asc' ? 'asc' : 'desc';

        $deltas = [];
        foreach (array_keys(self::$statsColonnes) as $c) {
            $deltas[] = "(curr.$c - IFNULL(prev.$c, 0)) AS d_$c";
        }
        $stmt = self::$pdo->prepare(
            'SELECT reg.nom, reg.race, curr.*, ' . implode(', ', $deltas) . '
             FROM _statistiques curr
             JOIN aa_registre reg ON curr.numero = reg.numero
             LEFT JOIN _statistiques prev ON curr.numero = prev.numero AND prev.tour = :prec
             WHERE curr.tour = :tour
             ORDER BY ' . ($tri === 'numero' ? 'curr.numero' : $tri) . ' ' . strtoupper($ordre) . ', curr.numero ASC'
        );
        $stmt->execute(['tour' => $tour, 'prec' => $tour - 1]);

        $lignes = [];
        foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $row) {
            $valeurs = [];
            foreach (array_keys(self::$statsColonnes) as $c) {
                $d = (int) $row['d_' . $c];
                $valeurs[] = [
                    'valeur' => number_format($row[$c], 0, ',', ' '),
                    'delta' => $d === 0 ? '–' : ($d > 0 ? '+' : '') . number_format($d, 0, ',', ' '),
                    'sens' => $d > 0 ? 'positive' : ($d < 0 ? 'negative' : ''),
                ];
            }
            $lignes[] = ['nom' => $row['nom'], 'numero' => (int) $row['numero'], 'race' => (int) $row['race'], 'valeurs' => $valeurs];
        }

        // En-têtes : lien de tri sur la valeur et sur l'écart ; un second clic inverse l'ordre
        $url = function ($t) use ($tour, $tri, $ordre) {
            return '?tour=' . $tour . '&tri=' . $t . '&ordre=' . ($tri === $t && $ordre === 'desc' ? 'asc' : 'desc');
        };
        $etat = function ($t) use ($tri, $ordre) { return $tri === $t ? $ordre : ''; };
        $colonnes = [];
        foreach (self::$statsColonnes as $c => $label) {
            $colonnes[] = ['label' => $label, 'url' => $url($c), 'tri' => $etat($c), 'urlDelta' => $url('d_' . $c), 'triDelta' => $etat('d_' . $c)];
        }

        return [
            'tour' => $tour,
            'tours' => $tours,
            'colonnes' => $colonnes,
            'commandant' => ['url' => $url('numero'), 'tri' => $etat('numero')],
            'lignes' => $lignes,
        ];
    }

    const STATS_MAX_COMPARES = 8; // une couleur de la palette par commandant comparé

    /**
     * Comparaison de l'historique de plusieurs commandants (repris de stats_detail.php).
     * $nums : "3,,12" — chaque position est un emplacement de couleur ; un commandant retiré laisse
     * sa place vide pour que les autres gardent leur couleur, un ajout prend la première place libre.
     */
    static function getStatsDetail($nums, $ajout)
    {
        $slots = array_slice(explode(',', (string) $nums), 0, self::STATS_MAX_COMPARES);
        $slots = array_map(function ($n) { return ctype_digit(trim($n)) ? (int) $n : 0; }, $slots);
        $ajout = (int) $ajout;
        if ($ajout > 0 && !in_array($ajout, $slots, true)) {
            $libre = array_search(0, $slots, true);
            if ($libre !== false) $slots[$libre] = $ajout;
            elseif (count($slots) < self::STATS_MAX_COMPARES) $slots[] = $ajout;
        }
        // Doublons et places vides en fin de liste
        $vus = [];
        foreach ($slots as $i => $n) {
            if ($n && isset($vus[$n])) $slots[$i] = 0;
            $vus[$n] = true;
        }
        while ($slots && end($slots) === 0) array_pop($slots);

        $choisis = array_values(array_filter($slots));
        $infos = [];
        $historique = [];
        if ($choisis) {
            $in = implode(',', array_map('intval', $choisis));
            foreach (self::$pdo->query("SELECT numero, nom, race FROM aa_registre WHERE numero IN ($in)") as $r) {
                $infos[(int) $r['numero']] = $r;
            }
            $cols = implode(', ', array_keys(self::$statsColonnes));
            foreach (self::$pdo->query("SELECT numero, tour, $cols FROM _statistiques WHERE numero IN ($in) ORDER BY tour ASC") as $r) {
                $historique[(int) $r['numero']][] = $r;
            }
        }

        $chaine = function ($s) { return implode(',', array_map(function ($n) { return $n ? $n : ''; }, $s)); };
        $series = [];
        foreach ($slots as $i => $n) {
            if (!$n || !isset($infos[$n])) continue;
            $sans = $slots;
            $sans[$i] = 0;
            while ($sans && end($sans) === 0) array_pop($sans);

            $points = [];
            $lignes = [];
            foreach (isset($historique[$n]) ? $historique[$n] : [] as $h) {
                $valeurs = [];
                foreach (array_keys(self::$statsColonnes) as $c) {
                    $points[$c][] = ['x' => (int) $h['tour'], 'y' => (int) $h[$c]];
                    $valeurs[] = number_format($h[$c], 0, ',', ' ');
                }
                $lignes[] = ['tour' => (int) $h['tour'], 'valeurs' => $valeurs];
            }
            $series[] = [
                'slot' => $i + 1,
                'numero' => $n,
                'nom' => $infos[$n]['nom'] . ' (' . $n . ')',
                'race' => (int) $infos[$n]['race'],
                'retirer' => '?nums=' . $chaine($sans),
                'points' => $points,
                'lignes' => $lignes,
            ];
        }

        // Commandants proposés à l'ajout
        $options = [];
        $complet = count($choisis) >= self::STATS_MAX_COMPARES;
        if (!$complet) {
            foreach (self::$pdo->query('SELECT DISTINCT r.numero, r.nom FROM aa_registre r JOIN _statistiques s ON r.numero = s.numero ORDER BY r.numero') as $r) {
                if (!in_array((int) $r['numero'], $choisis, true)) $options[] = ['numero' => (int) $r['numero'], 'nom' => $r['nom']];
            }
        }

        $stats = [];
        foreach (self::$statsColonnes as $c => $label) $stats[] = ['cle' => $c, 'label' => $label];

        return [
            'nums' => $chaine($slots),
            'series' => $series,
            'options' => $options,
            'complet' => $complet,
            'max' => self::STATS_MAX_COMPARES,
            'stats' => $stats,
            // Pour le script des graphiques : [{slot, nom, points: {stat: [{x, y}]}}]
            // JSON_HEX_TAG : peut être inséré tel quel dans une balise <script>
            'json' => json_encode(
                array_map(function ($s) { return ['slot' => $s['slot'], 'nom' => $s['nom'], 'points' => $s['points']]; }, $series),
                JSON_HEX_TAG | JSON_HEX_AMP | JSON_UNESCAPED_UNICODE
            ),
        ];
    }

    /**
     * Archives des anciennes parties : un sous-dossier de archive/ par partie,
     * affiché dans une iframe (/archive/<dossier>/). Triés du plus récent au plus ancien (ordre naturel).
     */
    static function getArchives()
    {
        $dir = __DIR__ . '/../archive';
        $noms = [];
        foreach (is_dir($dir) ? scandir($dir) : [] as $n) {
            if ($n[0] !== '.' && is_dir($dir . '/' . $n)) $noms[] = $n;
        }
        natcasesort($noms);
        $archives = [];
        foreach (array_reverse($noms) as $n) {
            $archives[] = ['nom' => $n, 'url' => '/archives/' . rawurlencode($n), 'src' => '/archive/' . rawurlencode($n) . '/'];
        }
        return $archives;
    }

    /* ------------------------------------------------------------------ *
     *  Session : commandant connecté, jeton anti-CSRF
     * ------------------------------------------------------------------ */

    /** Commandant connecté (['numero', 'nom']) ou null. */
    static function currentUser()
    {
        if (empty($_SESSION['commandant_num'])) return null;
        // Race : mise en session à la connexion (lue en base pour les sessions ouvertes par l'ancien site)
        if (!isset($_SESSION['commandant_race'])) {
            $stmt = self::$pdo->prepare('SELECT RACE FROM aa_registre WHERE NUMERO = :n');
            $stmt->execute(['n' => (int) $_SESSION['commandant_num']]);
            $_SESSION['commandant_race'] = (int) $stmt->fetchColumn();
        }
        $race = (int) $_SESSION['commandant_race'];
        return [
            'numero' => (int) $_SESSION['commandant_num'],
            'nom' => isset($_SESSION['commandant_nom']) ? $_SESSION['commandant_nom'] : '',
            'race' => $race,
            'avatar' => '/assets/img/avatar/' . (isset(self::$avatars[$race]) ? self::$avatars[$race] : 'cyborg') . '.png',
        ];
    }

    /**
     * Rapport d'un commandant (zip généré par le moteur) : rapports/<tour>/<numéro>tour<tour>.zip.
     * $tour : 0 = dernier tour ; un tour futur est ramené au dernier. Renvoie [chemin, tour] (chemin null si absent).
     */
    static function rapportFichier($user, $tour)
    {
        $tour = (int) $tour;
        if ($tour <= 0 || $tour > self::$tourNumber) $tour = self::$tourNumber;
        $fichier = __DIR__ . '/../rapports/' . $tour . '/' . $user['numero'] . 'tour' . $tour . '.zip';
        return [is_file($fichier) ? $fichier : null, $tour];
    }

    /** Page « Mon compte » : fiche du commandant, statistiques du dernier tour. */
    static function getCompte($user)
    {
        $stmt = self::$pdo->prepare('SELECT NOM, RACE, NUMERO, TOUR_ARRIVEE, ADRESSE FROM aa_registre WHERE NUMERO = :n');
        $stmt->execute(['n' => $user['numero']]);
        $r = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$r) return null;
        $profils = self::forumProfils([$user['numero']]);
        $profil = isset($profils[$user['numero']]) ? $profils[$user['numero']] : null;
        return [
            'nom' => $r['NOM'],
            'numero' => (int) $r['NUMERO'],
            'race' => (int) $r['RACE'],
            'raceNom' => isset(self::$races[(int) $r['RACE']]) ? self::$races[(int) $r['RACE']] : '',
            'email' => $r['ADRESSE'],
            'tourArrivee' => (int) $r['TOUR_ARRIVEE'],
            'avatar' => $user['avatar'],
            'profil' => $profil,
        ];
    }

    /** Connexion avec les identifiants de la console d'ordres (table aa_registre). */
    static function login($login, $password)
    {
        $stmt = self::$pdo->prepare('SELECT NUMERO, NOM, ADRESSE, RACE, theme FROM aa_registre WHERE LOGIN = :l AND MOT_DE_PASSE = :p');
        $stmt->execute(['l' => trim($login), 'p' => trim($password)]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$row) return false;

        session_regenerate_id(true);
        $_SESSION['commandant_num'] = $row['NUMERO'];
        $_SESSION['commandant_email'] = $row['ADRESSE'];
        $_SESSION['commandant_nom'] = $row['NOM'];
        $_SESSION['commandant_race'] = (int) $row['RACE'];
        $_SESSION['commandant_theme'] = $row['theme'] ?: null;
        return true;
    }

    static function logout()
    {
        $_SESSION = [];
        session_destroy();
    }

    /** Jeton anti-CSRF de la session, à mettre dans un champ caché `csrf` des formulaires. */
    static function csrf()
    {
        if (empty($_SESSION['csrf'])) {
            $_SESSION['csrf'] = bin2hex(openssl_random_pseudo_bytes(16));
        }
        return $_SESSION['csrf'];
    }

    static function checkCsrf()
    {
        $t = isset($_POST['csrf']) ? (string) $_POST['csrf'] : '';
        if ($t === '' || empty($_SESSION['csrf']) || $t !== $_SESSION['csrf']) Mini::abort(403, 'Formulaire expiré, rechargez la page.');
    }

    /* ------------------------------------------------------------------ *
     *  Thème : feuille assets/css/v2/themes/<nom>.css chargée après sheril.css,
     *  choix du commandant connecté enregistré dans aa_registre.theme
     *  (copié en session pour ne pas relire la base à chaque page)
     * ------------------------------------------------------------------ */

    /** Vrai si $nom désigne une feuille de thème existante (nom simple, pas de chemin). */
    static function themeExiste($nom)
    {
        return is_string($nom) && preg_match('/^[a-z0-9-]+$/', $nom)
            && is_file(__DIR__ . '/../assets/css/v2/themes/' . $nom . '.css');
    }

    /** Thème du commandant connecté, ou null (visiteur, aucun thème choisi, feuille supprimée). */
    static function theme()
    {
        if (empty($_SESSION['commandant_num'])) return null;
        // Lu en base pour les sessions ouvertes avant l'ajout des thèmes
        if (!array_key_exists('commandant_theme', $_SESSION)) {
            $stmt = self::$pdo->prepare('SELECT theme FROM aa_registre WHERE NUMERO = :n');
            $stmt->execute(['n' => (int) $_SESSION['commandant_num']]);
            $_SESSION['commandant_theme'] = $stmt->fetchColumn() ?: null;
        }
        return self::themeExiste($_SESSION['commandant_theme']) ? $_SESSION['commandant_theme'] : null;
    }

    /** Enregistre le thème du commandant ; un nom inconnu (ex. « defaut ») revient au thème par défaut. */
    static function setTheme($user, $nom)
    {
        $nom = self::themeExiste($nom) ? $nom : null;
        $stmt = self::$pdo->prepare('UPDATE aa_registre SET theme = :t WHERE NUMERO = :n');
        $stmt->execute(['t' => $nom, 'n' => $user['numero']]);
        $_SESSION['commandant_theme'] = $nom;
    }

    /* ------------------------------------------------------------------ *
     *  Forum (tables _category, _forum, _post)
     *  Un sujet est le premier message (id_parent NULL ou 0), les réponses
     *  pointent vers lui par id_parent.
     * ------------------------------------------------------------------ */

    // Colonnes d'un message + auteur, pour les requêtes du forum
    const FORUM_POST_COLS = 'p.id_post, p.id_forum, p.id_parent, p.id_author, p.title, p.record, r.NOM, r.NUMERO, r.RACE';

    /** Auteur d'un message au format attendu par les templates. */
    private static function forumAuteur($row)
    {
        if ($row['NUMERO'] === null) return ['nom' => 'Inconnu', 'numero' => '?', 'race' => 5];
        return ['nom' => $row['NOM'], 'numero' => (int) $row['NUMERO'], 'race' => (int) $row['RACE']];
    }

    /** Dernier message (auteur, date, lien) d'un forum ou d'un sujet. */
    private static function forumDernier($row)
    {
        if (!$row) return null;
        $sujet = $row['id_parent'] ? (int) $row['id_parent'] : (int) $row['id_post'];
        return [
            'auteur' => self::forumAuteur($row),
            'date' => format_date($row['record']),
            'url' => '/forum/topic/' . $sujet . '#post-' . $row['id_post'],
        ];
    }

    /** Accueil du forum : catégories → forums avec nombre de sujets et dernier message. */
    static function forumIndex()
    {
        $pdo = self::$pdo;
        $forums = $pdo->query(
            'SELECT f.id_forum, f.id_category, f.name, f.description,
                    (SELECT COUNT(*) FROM _post t WHERE t.id_forum = f.id_forum AND (t.id_parent IS NULL OR t.id_parent = 0)) AS nb_sujets,
                    (SELECT COUNT(*) FROM _post t WHERE t.id_forum = f.id_forum) AS nb_messages
             FROM _forum f ORDER BY f.id_forum'
        )->fetchAll(PDO::FETCH_ASSOC);

        $last = $pdo->prepare('SELECT ' . self::FORUM_POST_COLS . ' FROM _post p LEFT JOIN aa_registre r ON r.NUMERO = p.id_author
                               WHERE p.id_forum = :f ORDER BY p.record DESC, p.id_post DESC LIMIT 1');

        $categories = [];
        foreach ($pdo->query('SELECT id_category, name FROM _category ORDER BY id_category') as $c) {
            $categories[$c['id_category']] = ['nom' => $c['name'], 'forums' => []];
        }
        foreach ($forums as $f) {
            if (!isset($categories[$f['id_category']])) continue;
            $last->execute(['f' => $f['id_forum']]);
            $categories[$f['id_category']]['forums'][] = [
                'id' => (int) $f['id_forum'],
                'nom' => $f['name'],
                'description' => $f['description'],
                'nbSujets' => (int) $f['nb_sujets'],
                'nbMessages' => (int) $f['nb_messages'],
                'dernier' => self::forumDernier($last->fetch(PDO::FETCH_ASSOC)),
            ];
        }
        return array_values($categories);
    }

    /** Un forum (id, nom, description) ou null. */
    static function forumGet($id)
    {
        $stmt = self::$pdo->prepare('SELECT id_forum, name, description FROM _forum WHERE id_forum = :id');
        $stmt->execute(['id' => (int) $id]);
        $f = $stmt->fetch(PDO::FETCH_ASSOC);
        return $f ? ['id' => (int) $f['id_forum'], 'nom' => $f['name'], 'description' => $f['description']] : null;
    }

    /** Sujets d'un forum, du plus récemment actif au plus ancien. */
    static function forumSujets($idForum)
    {
        $pdo = self::$pdo;
        $stmt = $pdo->prepare(
            'SELECT ' . self::FORUM_POST_COLS . ',
                    (SELECT COUNT(*) FROM _post x WHERE x.id_parent = p.id_post) AS nb_reponses,
                    (SELECT MAX(x.record) FROM _post x WHERE x.id_post = p.id_post OR x.id_parent = p.id_post) AS activite
             FROM _post p LEFT JOIN aa_registre r ON r.NUMERO = p.id_author
             WHERE p.id_forum = :f AND (p.id_parent IS NULL OR p.id_parent = 0)
             ORDER BY activite DESC, p.id_post DESC'
        );
        $stmt->execute(['f' => (int) $idForum]);

        $last = $pdo->prepare('SELECT ' . self::FORUM_POST_COLS . ' FROM _post p LEFT JOIN aa_registre r ON r.NUMERO = p.id_author
                               WHERE p.id_parent = :t ORDER BY p.record DESC, p.id_post DESC LIMIT 1');
        $sujets = [];
        foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $row) {
            $nb = (int) $row['nb_reponses'];
            $dernier = null;
            if ($nb > 0) {
                $last->execute(['t' => $row['id_post']]);
                $dernier = self::forumDernier($last->fetch(PDO::FETCH_ASSOC));
            }
            $sujets[] = [
                'id' => (int) $row['id_post'],
                'titre' => $row['title'],
                'auteur' => self::forumAuteur($row),
                'date' => format_date($row['record']),
                'nbReponses' => $nb,
                'dernier' => $dernier,
            ];
        }
        return $sujets;
    }

    /** Un sujet et ses réponses (HTML nettoyé), ou null. $user : commandant connecté, pour les droits d'édition. */
    static function forumSujet($id, $user)
    {
        $stmt = self::$pdo->prepare(
            'SELECT ' . self::FORUM_POST_COLS . ', p.body FROM _post p LEFT JOIN aa_registre r ON r.NUMERO = p.id_author
             WHERE p.id_post = :id OR p.id_parent = :id2
             ORDER BY (p.id_post = :id3) DESC, p.record ASC, p.id_post ASC'
        );
        $stmt->execute(['id' => (int) $id, 'id2' => (int) $id, 'id3' => (int) $id]);
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
        if (!$rows || (int) $rows[0]['id_post'] !== (int) $id || $rows[0]['id_parent']) return null;

        $profils = self::forumProfils(array_map(function ($r) { return (int) $r['id_author']; }, $rows));
        $messages = [];
        foreach ($rows as $row) {
            $messages[] = [
                'id' => (int) $row['id_post'],
                'auteur' => self::forumAuteur($row),
                'profil' => isset($profils[(int) $row['id_author']]) ? $profils[(int) $row['id_author']] : null,
                'date' => format_date($row['record']),
                'corps' => self::forumCorps($row['body']),
                'editable' => $user && (int) $row['id_author'] === $user['numero'],
            ];
        }
        return ['id' => (int) $rows[0]['id_post'], 'idForum' => (int) $rows[0]['id_forum'], 'titre' => $rows[0]['title'], 'messages' => $messages];
    }

    // Avatar de chaque race (assets/img/avatar/), indexé par numéro de race
    static $avatars = [0 => 'fremen', 1 => 'atalante', 2 => 'zwaia', 3 => 'yoksor', 4 => 'fergok', 5 => 'cyborg'];

    /**
     * Profil affiché à gauche des messages, par numéro de commandant : avatar et nom de race,
     * statistiques du dernier tour connu, nombre de messages sur le forum.
     */
    private static function forumProfils($numeros)
    {
        $numeros = array_values(array_unique(array_filter(array_map('intval', $numeros))));
        if (!$numeros) return [];
        $in = implode(',', $numeros);

        $profils = [];
        foreach (self::$pdo->query("SELECT NUMERO, RACE FROM aa_registre WHERE NUMERO IN ($in)") as $r) {
            $race = (int) $r['RACE'];
            $profils[(int) $r['NUMERO']] = [
                'avatar' => '/assets/img/avatar/' . (isset(self::$avatars[$race]) ? self::$avatars[$race] : 'cyborg') . '.png',
                'race' => isset(self::$races[$race]) ? self::$races[$race] : '',
                'stats' => [],
                'tour' => null,
                'messages' => 0,
            ];
        }

        // Statistiques du dernier tour de chaque commandant
        $stats = self::$pdo->query(
            "SELECT s.numero, s.tour, s.puissance, s.planetes, s.pop_syst, s.pv
             FROM _statistiques s
             JOIN (SELECT numero, MAX(tour) AS tour FROM _statistiques WHERE numero IN ($in) GROUP BY numero) m
               ON m.numero = s.numero AND m.tour = s.tour"
        );
        foreach ($stats as $r) {
            $n = (int) $r['numero'];
            if (!isset($profils[$n])) continue;
            $profils[$n]['tour'] = (int) $r['tour'];
            foreach (['puissance' => 'Puissance', 'planetes' => 'Planètes', 'pop_syst' => 'Population', 'pv' => 'PV'] as $c => $label) {
                $profils[$n]['stats'][] = ['label' => $label, 'valeur' => number_format($r[$c], 0, ',', ' ')];
            }
        }

        foreach (self::$pdo->query("SELECT id_author, COUNT(*) AS nb FROM _post WHERE id_author IN ($in) GROUP BY id_author") as $r) {
            if (isset($profils[(int) $r['id_author']])) $profils[(int) $r['id_author']]['messages'] = (int) $r['nb'];
        }
        return $profils;
    }

    /** Un message brut (pour l'édition) ou null. */
    static function forumMessage($id)
    {
        $stmt = self::$pdo->prepare('SELECT id_post, id_forum, id_parent, id_author, title, body FROM _post WHERE id_post = :id');
        $stmt->execute(['id' => (int) $id]);
        $m = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$m) return null;
        return [
            'id' => (int) $m['id_post'],
            'idForum' => (int) $m['id_forum'],
            'idSujet' => $m['id_parent'] ? (int) $m['id_parent'] : (int) $m['id_post'],
            'estSujet' => !$m['id_parent'],
            'idAuteur' => (int) $m['id_author'],
            'titre' => $m['title'],
            'corps' => self::forumCorps($m['body']),
        ];
    }

    /**
     * Valide un titre et un corps de message. Renvoie [titre, corps, erreurs].
     * Le corps vient de l'éditeur Quill (HTML) : il est nettoyé à l'affichage, pas à l'enregistrement.
     */
    private static function forumValider($titre, $corps, $avecTitre)
    {
        $titre = trim(preg_replace('/\s+/u', ' ', (string) $titre));
        $corps = trim((string) $corps);
        $erreurs = [];
        if ($avecTitre && ($titre === '' || mb_strlen($titre, 'UTF-8') > 255)) $erreurs[] = 'Le titre doit faire de 1 à 255 caractères.';
        if (trim(strip_tags($corps, '<img>')) === '') $erreurs[] = 'Le message est vide.';
        if (strlen($corps) > 1000000) $erreurs[] = 'Le message est trop long.';
        return [$titre, $corps, $erreurs];
    }

    /** Crée un message (sujet si $idSujet vaut 0). Renvoie ['id' => nouvel id] ou ['erreurs' => […]]. */
    static function forumPoster($user, $idForum, $idSujet, $titre, $corps)
    {
        if ($idSujet) {
            $sujet = self::forumMessage($idSujet);
            if (!$sujet || !$sujet['estSujet']) Mini::abort(404);
            $idForum = $sujet['idForum'];
            $titre = 'Re: ' . $sujet['titre'];
        }
        list($titre, $corps, $erreurs) = self::forumValider($titre, $corps, !$idSujet);
        if ($erreurs) return ['erreurs' => $erreurs];

        $stmt = self::$pdo->prepare('INSERT INTO _post (id_forum, id_parent, id_author, title, body, record)
                                     VALUES (:f, :p, :a, :t, :b, NOW())');
        $stmt->execute([
            'f' => (int) $idForum,
            'p' => $idSujet ? (int) $idSujet : null,
            'a' => $user['numero'],
            't' => mb_substr($titre, 0, 255, 'UTF-8'),
            'b' => $corps,
        ]);
        return ['id' => (int) self::$pdo->lastInsertId()];
    }

    /** Modifie un message de son auteur. Renvoie la liste des erreurs (vide si enregistré). */
    static function forumModifier($message, $titre, $corps)
    {
        list($titre, $corps, $erreurs) = self::forumValider($titre, $corps, $message['estSujet']);
        if ($erreurs) return $erreurs;

        $stmt = self::$pdo->prepare('UPDATE _post SET title = :t, body = :b WHERE id_post = :id');
        $stmt->execute(['t' => $message['estSujet'] ? $titre : $message['titre'], 'b' => $corps, 'id' => $message['id']]);
        return [];
    }

    /**
     * HTML d'un message, nettoyé. Les messages récents viennent de Quill (HTML),
     * les anciens sont en BBCode.
     */
    static function forumCorps($text)
    {
        if (strpos($text, '<') !== false && strpos($text, '>') !== false) {
            $clean = strip_tags($text, '<p><br><strong><em><u><s><b><i><a><img><blockquote><pre><code><span><ul><ol><li><h1><h2><h3>');
            // Gestionnaires d'évènements JS (on*=…)
            $clean = preg_replace('/\s+on\w+\s*=\s*("[^"]*"|\'[^\']*\'|[^\s>]+)/i', '', $clean);
            // URL javascript:/vbscript:/data: (sauf images data:image) dans href/src, avec ou sans guillemets
            $clean = preg_replace('/\s(href|src)\s*=\s*(["\']?)\s*(javascript|vbscript|data(?!:image\/(png|jpe?g|gif|webp);))[^"\'\s>]*\2/i', ' $1="#"', $clean);
            // Styles dangereux (url(), expression())
            $clean = preg_replace('/\sstyle\s*=\s*("[^"]*(url|expression)\s*\([^"]*"|\'[^\']*(url|expression)\s*\([^\']*\')/i', '', $clean);
            // Liens externes dans un nouvel onglet
            $clean = preg_replace('/<a\s(?![^>]*target=)/i', '<a target="_blank" rel="noopener" ', $clean);
            return $clean;
        }

        // BBCode des anciens messages
        $text = nl2br(htmlspecialchars($text));
        $search = [
            '/\[b\](.*?)\[\/b\]/is',
            '/\[i\](.*?)\[\/i\]/is',
            '/\[u\](.*?)\[\/u\]/is',
            '/\[url\](https?:\/\/[^\s\[]*?)\[\/url\]/is',
            '/\[url=(https?:\/\/[^\s\]]*?)\](.*?)\[\/url\]/is',
            '/\[img\](https?:\/\/[^\s\[]*?)\[\/img\]/is',
            '/\[quote\](.*?)\[\/quote\]/is',
            '/\[color=([#a-z0-9]+)\](.*?)\[\/color\]/is',
            '/\[size=(\d+)\](.*?)\[\/size\]/is',
        ];
        $replace = [
            '<strong>$1</strong>',
            '<em>$1</em>',
            '<u>$1</u>',
            '<a href="$1" target="_blank" rel="noopener">$1</a>',
            '<a href="$1" target="_blank" rel="noopener">$2</a>',
            '<img src="$1" alt="Image">',
            '<blockquote>$1</blockquote>',
            '<span style="color:$1;">$2</span>',
            '<span style="font-size:$1px;">$2</span>',
        ];
        return preg_replace($search, $replace, $text);
    }
}

function afficherProgressionVictoire($pct)
{
    $pctFormate = number_format($pct, 2, ',', ' ') . '%';

    // Si proche ou ayant dépassé le seuil de victoire de 66%
    if ($pct >= 66) {
        $classe = 'victoire';
    } elseif ($pct >= 33) {
        $classe = 'positif';
    } else {
        $classe = 'neutre';
    }

    return '<span class="' . $classe . '">' . $pctFormate . '</span>';
}

// Affichage du nom du joueur
function afficherJoueur($joueur)
{
    return '<span class="race' . ($joueur['race']) . '">' . htmlspecialchars($joueur['nom']) . '&nbsp;(' . ($joueur['numero']) . ')</span>';
}

// Affichage de la valeur (delta) formatée avec couleur
function afficherScore($valeur)
{
    if ($valeur > 0) {
        $signe = '+';
        $classe = 'score-positif';
    } elseif ($valeur < 0) {
        $signe = ''; // le signe - est déjà inclus par PHP
        $classe = 'score-negatif';
    } else {
        $signe = '';
        $classe = 'score-neutre';
    }

    $valeurFormatee = $signe . number_format($valeur, 0, ',', ' ');
    return '<span class="' . $classe . '">' . $valeurFormatee . '</span>';
}

function format_date($date_str)
{
    if (!$date_str || $date_str == '0000-00-00 00:00:00') return "Jamais";
    $time = strtotime($date_str);
    if (!$time) return $date_str;
    return date('d/m/y H\hi', $time);
}


function include_markdown($path) {
    if (!file_exists($path)) {
        return '';
    }

    $parsedown = new Parsedown();
    $parsedown->setSafeMode(false);

    return $parsedown->text(file_get_contents($path));
}
