<?php
define('USE_PDO', true);
define('EMBED', true);
require_once 'includes/top.php';


// Récupération des IDs (Compatibilité PHP 5.6 : pas de ?? )
$nums_str = isset($_GET['nums']) ? $_GET['nums'] : '';
$nums_array = array_filter(array_map('intval', explode(',', $nums_str)));

$historyData = array();
$commandantsInfos = array();

if (!empty($nums_array)) {
    // 1. Infos des commandants sélectionnés
    $in_query = implode(',', $nums_array);
    $stmt = $pdo->query("SELECT numero, nom, race FROM aa_registre WHERE numero IN ($in_query)");
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $commandantsInfos[$row['numero']] = $row['nom']." (" .$row['numero'].")";
    }

    // 2. Historique complet
    $stmt = $pdo->query("SELECT * FROM _statistiques WHERE numero IN ($in_query) ORDER BY tour ASC");
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $historyData[$row['numero']][] = $row;
    }
}

// 3. Liste pour le formulaire (syntaxe array() pour 5.6)
$listeDispo = $pdo
        ->query("SELECT DISTINCT r.numero, r.nom FROM aa_registre r 
    JOIN _statistiques s ON r.numero = s.numero ORDER BY r.numero ASC")
        ->fetchAll(PDO::FETCH_ASSOC);

$statsToDisplay = array('puissance', 'centaure', 'planetes', 'pop_syst', 'pop_vs',
        'reputation', 'rayonnement', 'technologie', 'offensif', 'pv');


?>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    .grid-container { width: 100%; text-align: center; }
    .chart-box {
        display: inline-block;
        width: 500px; height: 400px;
        border: 1px solid #ccc; margin: 10px; padding: 5px;
    }
    .controls { background: #040d4c; padding: 15px; margin-bottom: 20px; font-family: sans-serif; }
</style>

<div class="controls">
    <form method="GET">
        <strong>Comparer :</strong>
        <?php foreach($commandantsInfos as $id => $nom): ?>
            <span style="background:#030722; border:1px solid #999; padding:2px 5px; margin-right:10px;">
                    <?php echo ($nom); ?>
                <?php
                // Calcul des IDs restants pour le lien de suppression
                $restants = array_diff($nums_array, array($id));
                $lienReduit = "?nums=" . implode(',', $restants);
                ?>
                    <a href="<?php echo $lienReduit; ?>" style="color:red; font-weight:bold; text-decoration:none;">&times;</a>
                </span>
        <?php endforeach; ?>

        <select onchange="addCommandant(this.value)">
            <option value="">+ Ajouter un commandant...</option>
            <?php foreach($listeDispo as $c): ?>
                <?php if(!isset($commandantsInfos[$c['numero']])): ?>
                    <option value="<?php echo $c['numero']; ?>"><?php echo htmlspecialchars($c['nom']); ?> (<?= $c['numero'] ?>)</option>
                <?php endif; ?>
            <?php endforeach; ?>
        </select>
    </form>
</div>

<div class="grid-container">
    <?php foreach ($statsToDisplay as $stat): ?>
        <div class="chart-box">
            <canvas id="chart_<?php echo $stat; ?>" style="background: black; color: white"></canvas>
        </div>
    <?php endforeach; ?>
</div>

<script>
    function getColor(index) {
        // Palette de base étendue (12 couleurs)
        const basePalette = [
            '#3498db', '#e74c3c', '#2ecc71', '#f1c40f', '#9b59b6', '#34495e',
            '#1abc9c', '#e67e22', '#e91e63', '#795548', '#607d8b', '#00bcd4'
        ];

        if (index < basePalette.length) {
            return basePalette[index];
        }

        // Si on dépasse, on génère une couleur en changeant la teinte (HSL)
        const hue = (index * 137.5) % 360; // Nombre d'or pour bien répartir les teintes
        return 'hsl(' + hue + ', 70%, 60%)';
    }
    function addCommandant(id) {
        if(!id) return;
        var currentNums = "<?php echo $nums_str; ?>";
        var newNums = currentNums ? currentNums + ',' + id : id;
        window.location.href = "?nums=" + newNums;
    }

    var historyData = <?php echo json_encode($historyData); ?>;
    var names = <?php echo json_encode($commandantsInfos); ?>;
    var statsList = <?php echo json_encode($statsToDisplay); ?>;

    var couleurParId = {};
    var ids = Object.keys(historyData);
    ids.forEach(function(id, index) {
        couleurParId[id] = getColor(index);
    });

    var palette = ['#3498db', '#e74c3c', '#2ecc71', '#f1c40f', '#9b59b6', '#34495e'];
    Chart.defaults.color = '#FFF';
    statsList.forEach(function(stat) {
        var ctx = document.getElementById('chart_' + stat).getContext('2d');
        var datasets = [];
        var colorIdx = 0;

        for (var num in historyData) {
            var dataPoints = [];
            for (var i = 0; i < historyData[num].length; i++) {
                dataPoints.push({
                    x: parseInt(historyData[num][i].tour),
                    y: parseFloat(historyData[num][i][stat])
                });
            }

            datasets.push({
                label: names[num],
                data: dataPoints,
                borderColor: couleurParId[num],
                backgroundColor: couleurParId[num],
                tension: 0.1,
                fill: false
            });
            colorIdx++;
        }

        new Chart(ctx, {
            type: 'line',
            data: { datasets: datasets },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {x: {
                        type: 'linear',
                        position: 'bottom',
                        title: {
                            display: true,
                            text: 'Tour',
                            color: '#FFF' // Titre de l'axe X
                        },
                        ticks: {
                            precision: 0,
                            stepSize: 1,
                            color: '#FFF' // Chiffres de l'axe X
                        },
                        grid: {
                            color: 'rgba(255, 255, 255, 0.1)' // Lignes de grille discrètes
                        }
                    },
                    y: {
                        ticks: { color: '#FFF' }, // Chiffres de l'axe Y
                        grid: { color: 'rgba(255, 255, 255, 0.1)' }
                    }
                },
                ticks: {
                    precision: 0,
                    stepSize: 1,
                    color: '#FFF'
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: {
                            color: '#FFF',
                            usePointStyle: true, // Utilise des cercles au lieu de rectangles (plus compact)
                            padding: 5,         // Espace entre les éléments
                            font: {
                                size: 9         // Texte plus petit
                            }
                        }
                    },
                    title: { display: true, text: stat.toUpperCase() }
                }
            }
        });
    });
</script>
<?php require_once 'includes/bot.php'; ?>