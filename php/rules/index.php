<?php
require_once __DIR__ . '/../includes/top.php';
require_once __DIR__ . '/../includes/Parsedown.php';

$rulesDir = __DIR__;

// Récupération de la page demandée
$requestedPage = isset($_GET['page']) ? trim($_GET['page']) : 'sommaire.md';

// Sécurisation du nom de fichier (protection contre traversal path)
$pageFile = basename($requestedPage);

// Si l'extension .md est absente, on l'ajoute
if (pathinfo($pageFile, PATHINFO_EXTENSION) === '') {
    $pageFile .= '.md';
}

$filePath = $rulesDir . '/' . $pageFile;
$notFound = false;

if (!file_exists($filePath) || !is_readable($filePath) || pathinfo($filePath, PATHINFO_EXTENSION) !== 'md') {
    $notFound = true;
    $filePath = $rulesDir . '/sommaire.md';
    $pageFile = 'sommaire.md';
}

$markdownContent = file_exists($filePath) ? file_get_contents($filePath) : '';

// Conversion Markdown vers HTML
$parsedown = new Parsedown();
$parsedown->setSafeMode(false); // Autorise les balises HTML nécessaires (tableaux HTML natifs, etc.)
$htmlContent = $parsedown->text($markdownContent);

// Liste des chapitres pour la barre de navigation
$chapters = [
    'sommaire.md' => 'Sommaire',
    '0_introduction_et_situation_de_depart.md' => '0. Intro',
    '1_galaxie_systemes_planetes.md' => '1. Galaxie',
    '2_population.md' => '2. Population',
    '3_constructions.md' => '3. Constructions',
    '4_flottes.md' => '4. Flottes',
    '5_combats.md' => '5. Combats',
    '6_recherches_technologiques.md' => '6. Recherche',
    '7_relations_entre_les_commandants.md' => '7. Relations',
    '8_lieutenants.md' => '8. Lieutenants',
    '9_ordres_de_la_console_et_tour.md' => '9. Ordres & Tour',
];
?>

<style>
.rules-container {
    width: 100%;
    margin: 0 auto;
    padding: 20px 15px;
    color: #e0e0e0;
}
.rules-nav {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    background: rgba(0, 0, 0, 0.4);
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 25px;
    border: 1px solid #333;
}
.rules-nav a {
    color: #9ac;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 4px;
    background: #1a1a24;
    font-size: 0.9em;
    transition: background 0.2s, color 0.2s;
    border: 1px solid #2a2a3a;
}
.rules-nav a:hover {
    background: #2b3b55;
    color: #fff;
}
.rules-nav a.active {
    background: #345b88;
    color: #fff;
    font-weight: bold;
    border-color: #4a7ab8;
}
.rules-content {
    background: rgba(15, 15, 25, 0.85);
    padding: 30px;
    border-radius: 8px;
    border: 1px solid #282838;
    line-height: 1.7;
}
.rules-content h1 {
    color: #63a4ff;
    border-bottom: 2px solid #345b88;
    padding-bottom: 8px;
    margin-top: 0;
    margin-bottom: 20px;
}
.rules-content h2 {
    color: #90caf9;
    border-bottom: 1px solid #2a3a50;
    padding-bottom: 6px;
    margin-top: 30px;
    margin-bottom: 15px;
}
.rules-content h3 {
    color: #bbdefb;
    margin-top: 25px;
    margin-bottom: 10px;
}
.rules-content p {
    margin-bottom: 5px;
}
.rules-content ul, .rules-content ol {
    margin-bottom: 20px;
    padding-left: 25px;
}
.rules-content li {
    margin-bottom: 6px;
}
.rules-content table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
    background: #12121c;
}
.rules-content th, .rules-content td {
    border: 1px solid #333d4d;
    padding: 10px 14px;
    text-align: left;
    vertical-align: top;
}
.rules-content th {
    background: #1e293b;
    color: #90caf9;
    font-weight: bold;
}
.rules-content tr:nth-child(even) {
    background: #171724;
}
.rules-content tr:hover {
    background: #202032;
}
.rules-content img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 15px 0;
    border: 1px solid #444;
}
.rules-content blockquote {
    border-left: 4px solid #345b88;
    padding-left: 15px;
    margin: 15px 0;
    color: #a0a0b0;
    background: rgba(52, 91, 136, 0.1);
    padding-top: 8px;
    padding-bottom: 8px;
}
.rules-content a {
    color: #64b5f6;
    text-decoration: underline;
}
.rules-content a:hover {
    color: #90caf9;
}
.rules-alert-404 {
    background: #4a1515;
    border: 1px solid #8b2525;
    color: #ffcccc;
    padding: 12px 16px;
    border-radius: 6px;
    margin-bottom: 20px;
}
</style>

<div class="rules-container">
    <nav class="rules-nav">
        <?php foreach ($chapters as $file => $label): ?>
            <a href="<?= urlencode($file) ?>" class="<?= ($pageFile === $file) ? 'active' : '' ?>">
                <?= htmlspecialchars($label) ?>
            </a>
        <?php endforeach; ?>
    </nav>

    <?php if ($notFound): ?>
        <div class="rules-alert-404">
            Le fichier de règles demandé (<strong><?= htmlspecialchars($requestedPage) ?></strong>) n'a pas été trouvé. Redirection vers le sommaire.
        </div>
    <?php endif; ?>

    <article class="rules-content">
        <?= $htmlContent ?>
    </article>
</div>

<?php
require_once __DIR__ . '/../includes/bot.php';
