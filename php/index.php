<?php
const USE_PDO = true;
date_default_timezone_set('Europe/Paris');
require_once __DIR__ . '/secure/connect.txt';
require __DIR__ . '/includes/mini.php';
require __DIR__ . '/includes/data.php';
Data::$pdo = $pdo;

if (session_status() == PHP_SESSION_NONE) session_start();


$app = new Mini(__DIR__ . '/templates');

/* ---------- Données communes à tous les templates ---------- */
$app->globals = [
    'gameName' => 'Corylis',
    'site' => ['tourNumber' => Data::$tourNumber, 'tourLastDate' => Data::$tourLastDate, 'tourLastDateIso' => Data::$tourLastDateIso],
    'user' => Data::currentUser(),
    // Page courante, pour marquer le lien actif des navigations (aria-current)
    'chemin' => chemin_courant(),
    'rubrique' => (string) strtok(ltrim(chemin_courant(), '/'), '/'),
    'csrf' => Data::csrf(),
    'theme' => Data::theme(),
    'navigation' => navigation_principale(),
];

/** Chemin de la page demandée, sans paramètres ni slash final ("/" pour l'accueil). */
function chemin_courant()
{
    $chemin = rawurldecode((string) parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH));
    return '/' . trim($chemin, '/');
}

/**
 * Liens de la navigation principale, avec la valeur d'aria-current du lien actif
 * ("page" pour l'accueil, "true" pour une rubrique). 'courant' : libellé de la rubrique
 * active, affiché sur le bouton du menu déroulant en mobile ("Menu" si aucune).
 */
function navigation_principale()
{
    $chemin = chemin_courant();
    $rubrique = (string) strtok(ltrim($chemin, '/'), '/');
    $liens = [
        ['url' => '/', 'libelle' => 'Accueil', 'rubrique' => ''],
        ['url' => '/lore/presentation', 'libelle' => 'Présentation', 'rubrique' => 'lore'],
        ['url' => '/rule/sommaire', 'libelle' => 'Règles du jeu', 'rubrique' => 'rule'],
        ['url' => '/play', 'libelle' => 'Jouer', 'rubrique' => 'play'],
        ['url' => '/forum', 'libelle' => 'Forum', 'rubrique' => 'forum'],
        ['url' => '/statistiques', 'libelle' => 'Statistiques', 'rubrique' => 'statistiques'],
        ['url' => '/archives', 'libelle' => 'Archives', 'rubrique' => 'archives'],
    ];
    $courant = 'Menu';
    foreach ($liens as $i => $lien) {
        $actif = $lien['rubrique'] === '' ? $chemin === '/' : $rubrique === $lien['rubrique'];
        $liens[$i]['aria'] = $actif ? ($lien['rubrique'] === '' ? 'page' : 'true') : null;
        if ($actif) $courant = $lien['libelle'];
    }
    return ['liens' => $liens, 'courant' => $courant];
}

/** Adresse de retour après connexion : chemin local uniquement. */
function url_retour($url)
{
    $url = is_string($url) ? $url : '';
    return (substr($url, 0, 1) === '/' && substr($url, 0, 2) !== '//' && strpos($url, '\\') === false) ? $url : '/';
}

/** Commandant connecté, sinon redirection vers la page de connexion. */
function exiger_connexion()
{
    $user = Data::currentUser();
    if (!$user) Mini::redirect('/connexion?retour=' . rawurlencode($_SERVER['REQUEST_URI']));
    return $user;
}

/* ---------- Routes : chemin, template, données ---------- */
$articles = [];
// Données statiques
$app->get('/', 'pages/home', function () {
    return [
        'title' => 'Accueil',
        'data' => Data::getHomeData(),
        // Jumbotron : début de la dernière gazette pour un commandant connecté
        'gazette' => Data::currentUser() ? Data::getGazetteUne() : null,
    ];
});
# LORE
$app->get('/lore/presentation', 'pages/lore/presentation', ['title' => 'Présentation']);
$app->get('/lore/histoire', 'pages/lore/histoire', ['title' => 'Histoire']);
$app->get('/lore/fremen', 'pages/lore/fremen', ['title' => 'Fremen']);
$app->get('/lore/atalante', 'pages/lore/atalante', ['title' => 'Atalante']);
$app->get('/lore/zwaia', 'pages/lore/zwaia', ['title' => 'Zwaia']);
$app->get('/lore/yoksor', 'pages/lore/yoksor', ['title' => 'Yoksor']);
$app->get('/lore/fergok', 'pages/lore/fergok', ['title' => 'Fergok']);
# RULE
$app->get('/rule/{page}', 'pages/rule/page', function ($p) {

    $chapters = [
        'sommaire' => 'Sommaire',
        '0_introduction_et_situation_de_depart' => '0. Intro',
        '1_galaxie_systemes_planetes' => '1. Galaxie',
        '2_population' => '2. Population',
        '3_constructions' => '3. Constructions',
        '4_flottes' => '4. Flottes',
        '5_combats' => '5. Combats',
        '6_recherches_technologiques' => '6. Recherche',
        '7_relations_entre_les_commandants' => '7. Relations',
        '8_lieutenants' => '8. Lieutenants',
        '9_ordres_de_la_console_et_tour' => '9. Ordres & Tour',
    ];

    if(!array_key_exists('page', $p)
    || !isset($chapters[$p['page']])) {
        Mini::abort(404);
    }

    $page = $p['page'];

    // récupération du markdown
    $file = __DIR__ . '/rules/' . $page . '.md';
    if(!file_exists($file)) {  Mini::abort(404); }

    return [
        'title' => $chapters[$page],
        'chapters' => $chapters,
        'content' => include_markdown($file),
    ];
});
$app->get('/play', 'pages/play/index', ['title' => 'Jouer']);
$app->get('/play/listing', 'pages/play/listing', function () {
    return ['title' => 'Registre', 'registre' => Data::getRegistre()];
});
$app->get('/play/console', 'pages/play/console', ['title' => "Console d'ordres"]);
$app->get('/play/tool', 'pages/play/tool', ['title' => "Outil d'aide"]);
$app->any('/play/register', 'pages/play/register', function () {
    $form = ['nom' => '', 'email' => '', 'race' => '', 'mj' => ''];
    $erreurs = [];
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        foreach ($form as $k => $v) $form[$k] = isset($_POST[$k]) ? (string) $_POST[$k] : '';
        $erreurs = Data::inscrire($form['nom'], $form['email'], $form['race'], $form['mj']);
        // Post/Redirect/Get : un rechargement ne renvoie pas le formulaire
        if (!$erreurs) Mini::redirect('/play/register?ok=' . rawurlencode(trim($form['nom'])));
    }
    $inscription = Data::getInscription();
    foreach ($inscription['races'] as $i => $race) {
        $inscription['races'][$i]['checked'] = $form['race'] !== '' && (int) $form['race'] === $race['id'];
    }
    // Message de succès seulement si le nom est bien parmi les inscriptions en attente
    $inscrit = '';
    if (isset($_GET['ok']) && is_string($_GET['ok'])) {
        foreach ($inscription['attente'] as $j) {
            if (strtolower($j['nom']) === strtolower(trim($_GET['ok']))) $inscrit = $j['nom'];
        }
    }
    return [
        'title' => 'Inscription',
        'inscription' => $inscription,
        'form' => $form,
        'erreurs' => $erreurs,
        'inscrit' => $inscrit,
    ];
});

# STATISTIQUES (pages générées par le moteur dans stats/, affichées dans une iframe comme l'ancien stats.php)
// ?page=<chemin> : page à ouvrir dans l'iframe (lien direct vers /statistiques/detail depuis /compte…)
$app->get('/statistiques', 'pages/stats/index', function () {
    $page = isset($_GET['page']) ? (string) $_GET['page'] : '';
    if (!preg_match('#^/(statistiques|stats)/[\w./?=&,%-]*$#', $page) || strpos($page, '..') !== false) {
        $page = '/statistiques/general';
    }
    return ['title' => 'Statistiques', 'cadre' => $page] + Data::getStatsLiens();
});

// Classement général, affiché dans l'iframe de /statistiques (gabarit sans en-tête)
$app->get('/statistiques/general', 'pages/stats/general', function () {
    $s = Data::getStatsGeneral(
        isset($_GET['tour']) ? (int) $_GET['tour'] : 0,
        isset($_GET['tri']) ? (string) $_GET['tri'] : '',
        isset($_GET['ordre']) ? (string) $_GET['ordre'] : ''
    );
    return ['title' => 'Classement général · tour ' . $s['tour'], 'stats' => $s, 'parent' => '/statistiques'];
});

// Progression comparée de commandants, affichée dans l'iframe de /statistiques
$app->get('/statistiques/detail', 'pages/stats/detail', function () {
    $d = Data::getStatsDetail(isset($_GET['nums']) ? $_GET['nums'] : '', isset($_GET['ajout']) ? $_GET['ajout'] : 0);
    // Ajout via le formulaire : on redirige vers l'adresse canonique (?nums=…)
    if (isset($_GET['ajout'])) Mini::redirect('/statistiques/detail?nums=' . $d['nums']);
    return ['title' => 'Progression des commandants', 'detail' => $d, 'parent' => '/statistiques'];
});

# GAZETTE (saison/<saison>/gazette/*.md, une par tour, réservée aux commandants connectés)
$app->get('/gazette', null, function () {
    exiger_connexion();
    $gazettes = Data::getGazettes();
    if (!$gazettes) Mini::abort(404);
    Mini::redirect($gazettes[0]['url']);
});
$app->get('/gazette/{tour:\d+}', 'pages/gazette', function ($p) {
    exiger_connexion();
    $gazettes = Data::getGazettes();
    foreach ($gazettes as $g) {
        if ($g['tour'] === (int) $p['tour']) {
            return [
                'title' => 'Gazette · tour ' . $g['tour'],
                'gazettes' => $gazettes,
                'courante' => $g,
                'content' => include_markdown($g['fichier']),
            ];
        }
    }
    Mini::abort(404);
});
# ARCHIVES (anciennes parties dans archive/<dossier>/, affichées dans une iframe)
$app->get('/archives', 'pages/archives', function () {
    return ['title' => 'Archives', 'archives' => Data::getArchives(), 'courante' => null];
});
$app->get('/archives/{nom}', 'pages/archives', function ($p) {
    $archives = Data::getArchives();
    foreach ($archives as $a) {
        // Seuls les dossiers existants sont acceptés (pas de chemin arbitraire)
        if ($a['nom'] === $p['nom']) return ['title' => 'Archives · ' . $a['nom'], 'archives' => $archives, 'courante' => $a];
    }
    Mini::abort(404);
});

# CONNEXION (identifiants de la console d'ordres)
$app->any('/connexion', 'pages/connexion', function () {
    $retour = url_retour(isset($_REQUEST['retour']) ? $_REQUEST['retour'] : '/');
    if (Data::currentUser()) Mini::redirect($retour);
    $erreur = false;
    $login = '';
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        Data::checkCsrf();
        $login = isset($_POST['login']) ? (string) $_POST['login'] : '';
        $password = isset($_POST['password']) ? (string) $_POST['password'] : '';
        if (Data::login($login, $password)) Mini::redirect($retour);
        $erreur = true;
    }
    return ['title' => 'Connexion', 'retour' => $retour, 'login' => $login, 'erreur' => $erreur];
});
$app->post('/deconnexion', null, function () {
    Data::checkCsrf();
    Data::logout();
    Mini::redirect('/');
});

# MON COMPTE
// POST : envoi (action=avatar) ou suppression (action=avatar-supprimer) de l'avatar personnalisé
$app->any('/compte', 'pages/compte', function () {
    $user = exiger_connexion();
    $erreurs = [];
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        // Envoi plus gros que post_max_size : PHP vide $_POST et $_FILES
        if (!$_POST && !empty($_SERVER['CONTENT_LENGTH'])) {
            $erreurs = ["L'image dépasse 200 ko."];
        } else {
            Data::checkCsrf();
            $action = isset($_POST['action']) ? $_POST['action'] : '';
            if ($action === 'avatar-supprimer') {
                Data::supprimerAvatar($user);
                Mini::redirect('/compte?avatar=supprime');
            }
            $erreurs = Data::setAvatar($user, isset($_FILES['avatar']) ? $_FILES['avatar'] : null);
            if (!$erreurs) Mini::redirect('/compte?avatar=enregistre');
        }
    }
    $compte = Data::getCompte(Data::currentUser());
    if (!$compte) Mini::abort(404);
    return ['title' => 'Mon compte', 'commandant' => $compte, 'erreurs' => $erreurs,
            'avatarMessage' => isset($_GET['avatar']) ? $_GET['avatar'] : null];
});

# FICHE PUBLIQUE D'UN COMMANDANT (liens du registre)
$app->get('/commandant/{numero:\d+}', 'pages/commandant', function ($p) {
    $commandant = Data::getCommandant($p['numero']);
    if (!$commandant) Mini::abort(404);
    return ['title' => $commandant['nom'], 'commandant' => $commandant];
});

# RAPPORT : téléchargement du zip du commandant connecté (dernier tour, ou /rapport/{tour})
$rapport = function ($p) {
    $user = exiger_connexion();
    list($fichier, $tour) = Data::rapportFichier($user, isset($p['tour']) ? $p['tour'] : 0);
    if (!$fichier) Mini::abort(404, 'Aucun rapport disponible pour le tour ' . $tour . '.');

    header('Content-Type: application/zip');
    header('Content-Disposition: attachment; filename="' . basename($fichier) . '"');
    header('Content-Length: ' . filesize($fichier));
    header('Cache-Control: private, no-cache');
    readfile($fichier);
    exit;
};
$app->get('/rapport', null, $rapport);
$app->get('/rapport/{tour:\d+}', null, $rapport);

# FORUM
$app->get('/forum', 'pages/forum/index', function () {
    return ['title' => 'Forum', 'categories' => Data::forumIndex()];
});
$app->get('/forum/{id:\d+}', 'pages/forum/forum', function ($p) {
    $forum = Data::forumGet($p['id']);
    if (!$forum) Mini::abort(404);
    return ['title' => $forum['nom'], 'forum' => $forum, 'sujets' => Data::forumSujets($forum['id'])];
});
// Nouveau sujet
$app->any('/forum/{id:\d+}/new', 'pages/forum/edit', function ($p) {
    $forum = Data::forumGet($p['id']);
    if (!$forum) Mini::abort(404);
    $user = exiger_connexion();
    $form = ['titre' => '', 'corps' => ''];
    $erreurs = [];
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        Data::checkCsrf();
        $form = ['titre' => isset($_POST['titre']) ? (string) $_POST['titre'] : '', 'corps' => isset($_POST['corps']) ? (string) $_POST['corps'] : ''];
        $r = Data::forumPoster($user, $forum['id'], 0, $form['titre'], $form['corps']);
        if (isset($r['id'])) Mini::redirect('/forum/topic/' . $r['id']);
        $erreurs = $r['erreurs'];
        $form['corps'] = Data::forumCorps($form['corps']);
    }
    return ['title' => 'Nouveau sujet', 'forum' => $forum, 'avecTitre' => true, 'form' => $form, 'erreurs' => $erreurs,
            'action' => '/forum/' . $forum['id'] . '/new', 'bouton' => 'Publier le sujet', 'annuler' => '/forum/' . $forum['id']];
});
// Sujet + réponse
$app->any('/forum/topic/{id:\d+}', 'pages/forum/topic', function ($p) {
    $erreurs = [];
    $corps = '';
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $user = exiger_connexion();
        Data::checkCsrf();
        $corps = isset($_POST['corps']) ? (string) $_POST['corps'] : '';
        $r = Data::forumPoster($user, 0, (int) $p['id'], '', $corps);
        if (isset($r['id'])) Mini::redirect('/forum/topic/' . (int) $p['id'] . '#post-' . $r['id']);
        $erreurs = $r['erreurs'];
        $corps = Data::forumCorps($corps);
    }
    $sujet = Data::forumSujet($p['id'], Data::currentUser());
    if (!$sujet) Mini::abort(404);
    return ['title' => $sujet['titre'], 'sujet' => $sujet, 'forum' => Data::forumGet($sujet['idForum']),
            'erreurs' => $erreurs, 'form' => ['corps' => $corps]];
});
// Modifier un de ses messages
$app->any('/forum/post/{id:\d+}/edit', 'pages/forum/edit', function ($p) {
    $user = exiger_connexion();
    $message = Data::forumMessage($p['id']);
    if (!$message) Mini::abort(404);
    if ($message['idAuteur'] !== $user['numero']) Mini::abort(403, "Vous n'êtes pas l'auteur de ce message.");
    $form = ['titre' => $message['titre'], 'corps' => $message['corps']];
    $erreurs = [];
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        Data::checkCsrf();
        $form = ['titre' => isset($_POST['titre']) ? (string) $_POST['titre'] : '', 'corps' => isset($_POST['corps']) ? (string) $_POST['corps'] : ''];
        $erreurs = Data::forumModifier($message, $form['titre'], $form['corps']);
        if (!$erreurs) Mini::redirect('/forum/topic/' . $message['idSujet'] . '#post-' . $message['id']);
        $form['corps'] = Data::forumCorps($form['corps']);
    }
    return ['title' => 'Modifier le message', 'forum' => Data::forumGet($message['idForum']), 'avecTitre' => $message['estSujet'],
            'form' => $form, 'erreurs' => $erreurs, 'action' => '/forum/post/' . $message['id'] . '/edit',
            'bouton' => 'Enregistrer', 'annuler' => '/forum/topic/' . $message['idSujet'] . '#post-' . $message['id']];
});

$app->get('/a-propos', 'about.html', ['title' => 'À propos']);

# THÈME (pas de lien dans l'interface pour l'instant : URL à donner aux testeurs)
// Réservé aux commandants connectés, choix enregistré en base (aa_configuration.theme).
// /theme/violet active le thème, /theme/defaut revient au thème d'origine ; ?retour=/page pour revenir ailleurs qu'à l'accueil
$app->get('/theme/{nom}', null, function ($p) {
    Data::setTheme(exiger_connexion(), $p['nom']);
    Mini::redirect(url_retour(isset($_GET['retour']) ? $_GET['retour'] : '/'));
});


$app->run();
