<?php
session_start();
error_reporting(E_ALL & ~E_DEPRECATED);
ini_set('display_errors', 1);
date_default_timezone_set('Europe/Paris');
require_once __DIR__ .'/../secure/connect.txt';
require_once __DIR__ .'/../script/fonctions.txt';
// 1. Définir le chemin absolu vers le fichier
$file_path = __DIR__ . '/../tour.txt';
$tour_information = "";
if (file_exists($file_path)) {
    // 2. Récupérer le contenu (le numéro du tour)
    $numeroTour = intval(trim(file_get_contents($file_path)));

    // 3. Récupérer la date de modification (timestamp)
    $timestamp = filemtime($file_path);

    // 4. Formater la date en français
    $dateFormatee = date("d/m/Y", $timestamp);

    // 5. Générer le HTML
    $tour_information = sprintf(
            '<small>Dernier tour : %s, le %s - <a href="https://discord.gg/bdUtYSqrnK">rejoignez nous sur discord</a></small>',
            htmlspecialchars($numeroTour),
            $dateFormatee
    );
}

// gestion de la connexion
?><!doctype html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/styles.css?d=qsd">
    <title>Sheril, le jeu de conquête galactique</title>
</head>
<body>
<?php
    if(!defined('EMBED') || EMBED === false ) {
?>
<header>
    Sheril, le jeux de conquête galactique
    <?=$tour_information?>
    <div style="font-size: 0.6em;">
        <?php
        $numero = intval(@$_SESSION['commandant_num']);
        if ($numero) {
            echo "Connecté en tant que " . getCommandantHTML($numero);
            echo ' | <a href="/deconnexion.php" style="color: #ccc;">Déconnexion</a>';
        } else {
            echo '<a href="/connexion.php" style="color: #ccc;">Se connecter</a>';
        }
        ?>
    </div>
</header>

<nav>
    <a href="/">Accueil</a>
    <a href="/presentation.php">Présentation</a>
    <a href="/races/fremen.php">Les races</a>
    <a href="/stats.php">Statistiques</a>
    <a href="/ordres/ordres.php3">Console d'ordre</a>
    <a href="/register.php">Registre et Inscription</a>
    <a href="/forum/">Forum</a>
</nav>


<div id="main">
    <?php } ?>
