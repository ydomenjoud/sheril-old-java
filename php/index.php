<?php

define('USE_PDO', true);
require_once './includes/top.php';


// Paramètres
$tourActuel = $numeroTour;
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
$topAgeDor = array_filter($donneesVictoire, function($row) {
    return $row['rank_age_dor'] <= 5;
});
usort($topAgeDor, function($a, $b) {
    return ($a['rank_age_dor'] < $b['rank_age_dor']) ? -1 : 1;
});

// Tri et extraction du Top 5 Empire Galactique
$topEmpire = array_filter($donneesVictoire, function($row) {
    return $row['rank_empire_galactique'] <= 5;
});
usort($topEmpire, function($a, $b) {
    return ($a['rank_empire_galactique'] < $b['rank_empire_galactique']) ? -1 : 1;
});

function afficherProgressionVictoire($pct) {
    $pctFormate = number_format($pct, 2, ',', ' ') . '%';

    // Si proche ou ayant dépassé le seuil de victoire de 66%
    if ($pct >= 66) {
        $classe = 'score-victoire';
    } elseif ($pct >= 30) {
        $classe = 'score-positif';
    } else {
        $classe = 'score-neutre';
    }

    return '<span class="' . $classe . '">(' . $pctFormate . ' / 66%)</span>';
}
// Affichage du nom du joueur
function afficherJoueur($joueur)
{
    return '<span class="race' . ($joueur['race']) . '">' . htmlspecialchars($joueur['nom']) . ' (' . ($joueur['numero']) . ')</span>';
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
    return '<span class="' . $classe . '">(' . $valeurFormatee . ')</span>';
}

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
                LIMIT 5";

// 3. Exécution PDO
$stmt_recent = $pdo->prepare($sql_recent);
$stmt_recent->execute();
$recent_messages = $stmt_recent->fetchAll(PDO::FETCH_ASSOC);


function format_date($date_str) {
    if (!$date_str || $date_str == '0000-00-00 00:00:00') return "Jamais";
    $time = strtotime($date_str);
    if (!$time) return $date_str;
    return date('d/m/y H\hi', $time);
}
?>
    <nav>
        <a href="/stats.php">Voir les stats</a>
        <a href="/ordres/ordres.php3">Télécharger son rapport</a>
        <a href="/ordres/ordres.php3">Passer ses ordres</a>
        <a href="/rapports/images.zip">Télécharger les images du rapport</a>
    </nav>
    <style>
        blockquote {
            padding: 10px;
            font-style: italic;
            overflow-x: auto;
            margin: 10px;
        }


        /* Container global */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 15px;
            background-color: #000000;
            padding: 15px;
            font-family: 'Courier New', Courier, monospace; /* Style console/retro */
            color: #e0e0e0;
        }

        /* Carte par critère */
        .stat-card {
            background-color: #050811;
            border: 1px solid #00ffff; /* Cyan de ta bordure extérieure */
            box-shadow: 0 0 5px rgba(0, 255, 255, 0.3);
            padding: 10px;
        }

        .stat-card h2 {
            color: #00ffff;
            text-align: center;
            margin: 0 0 10px 0;
            font-size: 1.1em;
            border-bottom: 1px dashed #00ffff;
            padding-bottom: 5px;
        }

        /* Titles Top / Flop */
        .section-top h3 {
            color: #2bd849; /* Vert néon */
            border-bottom: 1px solid #2bd849;
            font-size: 0.9em;
            margin: 10px 0 5px 0;
        }

        .section-flop h3 {
            color: #fb5757; /* Rouge néon */
            border-bottom: 1px solid #fb5757;
            font-size: 0.9em;
            margin: 15px 0 5px 0;
        }

        /* Listes */
        ol {
            padding-left: 20px;
            margin: 0;
        }

        li {
            display: flex;
            justify-content: space-between;
            padding: 2px 0;
            font-size: 0.85em;
        }

        .score-positif {
            color: #2bd849; /* Vert comme dans ton tableau */
        }

        .score-negatif {
            color: #fb5757; /* Rouge comme dans ton tableau */
        }

        .score-neutre {
            color: #888888;
        }

    </style>
    <main>

        <h1>Sheril, le jeu de stratégie au tour par tour</h1>
        <blockquote>
            « L'humanité a créé les mutants pour sauver son empire… et les mutants ont effacé l'humanité pour fonder le
            leur. »
            Sheril vous plonge au cœur d'un jeu de stratégie 4X spatial au lore riche et impitoyable. Après des siècles
            de guerre totale et de mutations forcées sous les colonnes de radiations bleutées, l'ancien ordre cosmique
            est tombé. À la tête de l'un des peuples mutants nés de cet enfer — stratèges hors pair, colosses de combat
            ou maîtres des environnements hostiles —, prenez le contrôle du cosmos. Explorez des systèmes solaires
            dévastés, développez votre empire, négociez vos alliances et subjuguez vos rivaux dans une lutte acharnée
            pour la domination absolue de la galaxie.
        </blockquote>
        <div style="margin: 20px; display: flex; justify-content: center; gap: 40px">
            <a class="btn" href="/races/histoire.php">Découvrir l'histoire</a>
            <a class="btn" href="">Voir les statistiques</a>
        </div>

        <h2>DERNIERS MESSAGES DU FORUM  - <a href="/forum/">voir le forum</a></h2>
        <div  style="grid-column: span 2; padding: 10px;">
            <table class="forum-table" style="width: 100%;">
                <thead>
                <tr>
                    <th style="text-align: left;">Sujet</th>
                    <th style="text-align: left;">Forum</th>
                    <th style="text-align: left;">Dernier auteur</th>
                    <th style="text-align: right;">Date</th>
                </tr>
                </thead>
                <tbody>
                <?php foreach ($recent_messages as $msg): ?>
                    <tr>
                        <td>
                            <a href="forum/view_topic.php?id=<?php echo $msg['target_topic_id']; ?>#post-<?php echo $msg['last_post_id']; ?>"
                            >
                                <?php echo htmlspecialchars($msg['topic_title']); ?>
                            </a>
                        </td>
                        <td>
                            <a href="forum/view_forum.php?id=<?php echo $msg['id_forum']; ?>" >
                                <?php echo htmlspecialchars($msg['forum_name']); ?>
                            </a>
                        </td>
                        <td>
                            <?php echo display_author($msg['NOM'], $msg['NUMERO'], $msg['RACE']); ?>
                        </td>
                        <td style="text-align: right; color: #888; font-size: 0.85em;">
                            <?php echo format_date($msg['max_record']); ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
                </tbody>
            </table>
        </div>

        <h2>Victoire par Mort Subite - <a href="/stats.php">voir les statistiques</a></h2>
        <div class="stats-grid">
            <!-- ÂGE D'OR -->
            <div class="stat-card">
                <h2>👑 ÂGE D'OR</h2>
                <div class="section-top">
                    <h3>TOP 5 - OBJECTIF 66%</h3>
                    <ol>
                        <?php foreach ($topAgeDor as $joueur): ?>
                            <li>
                                <?php echo afficherJoueur($joueur); ?>
                                <?php echo afficherProgressionVictoire($joueur['pct_age_dor']); ?>
                            </li>
                        <?php endforeach; ?>
                    </ol>
                </div>
            </div>

            <!-- EMPIRE GALACTIQUE -->
            <div class="stat-card">
                <h2>🚀 EMPIRE GALACTIQUE</h2>
                <div class="section-top">
                    <h3>TOP 5 - OBJECTIF 66%</h3>
                    <ol>
                        <?php foreach ($topEmpire as $joueur): ?>
                            <li>
                                <?php echo afficherJoueur($joueur); ?>
                                <?php echo afficherProgressionVictoire($joueur['pct_empire_galactique']); ?>
                            </li>
                        <?php endforeach; ?>
                    </ol>
                </div>
            </div>
        </div>
        <h2>Les Top/Flop du tour <?=$tourActuel?> - <a href="/stats.php">voir les statistiques</a></h2>

        <div class="stats-grid">
            <?php foreach ($criteres as $critere): ?>
                <div class="stat-card">
                    <h2><?php echo strtoupper($labels[$critere]); ?></h2>

                    <!-- TOP 5 -->
                    <div class="section-top">
                        <h3>TOP 5</h3>
                        <ol>
                            <?php foreach ($classements[$critere]['top'] as $joueur): ?>
                                <li>
                                    <?php echo afficherJoueur($joueur); ?>
                                    <?php echo afficherScore($joueur['d_' . $critere]); ?>
                                </li>
                            <?php endforeach; ?>
                        </ol>
                    </div>

                    <!-- FLOP 5 -->
                    <div class="section-flop">
                        <h3>FLOP 5</h3>
                        <ol>
                            <?php foreach ($classements[$critere]['flop'] as $joueur): ?>
                                <li>
                                    <?php echo afficherJoueur($joueur); ?>
                                    <?php echo afficherScore($joueur['d_' . $critere]); ?>
                                </li>
                            <?php endforeach; ?>
                        </ol>
                    </div>
                </div>
            <?php endforeach; ?>
        </div>

        <!--        <iframe src="https://discord.com/widget?id=1407654775649992897&theme=dark" width="350" height="500"-->
        <!--                allowtransparency="true" frameborder="0"-->
        <!--                sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>-->

        <!--        <ul>-->
        <!--            <li><a href="/stats.php">Voir les stats</a></li>-->
        <!--            <li><a href="/ordres/ordres.php3">Télécharger son rapport</a></li>-->
        <!--            <li><a href="/ordres/ordres.php3">Passer ses ordres</a></li>-->
        <!--            <li><a href="/rapports/images.zip">Télécharger les images du rapport</a></li>-->
        <!--        </ul>-->

    </main>
<?php require_once './includes/bot.php'; ?>