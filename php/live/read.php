<?php
session_start();
// rapport.php
$commandant = $_SESSION['commandant_num'];
$email = $_SESSION['commandant_email'];
$tour =  intval(file_get_contents("../tour.txt"));

if(!$commandant || !$email || !$tour){
    header('Location: /ordres/ordres.php3');;
}

if (!$email || !$tour || !$commandant) {
    http_response_code(400);
    echo "Paramètres manquants";
    exit;
}


// clé secrète stockée dans config ou env
$secret = 'e65c065b-2620-4147-84d7-f0be7d2fd476';
if (!$secret) {
    http_response_code(500);
    echo "Clé secrète manquante";
    exit;
}

// hash indévinable
$hash = hash_hmac('sha256', "$email|$tour|$commandant", $secret);

$zipPath  = __DIR__ . "/../rapports/$tour/{$commandant}tour{$tour}.zip";
$destDir  = __DIR__ . "/../live/$hash";

if(!file_exists($destDir)){

    if (!is_readable($zipPath)) {
        http_response_code(404);
        echo "Archive introuvable";
        exit;
    }

    $zip = new ZipArchive();
    if ($zip->open($zipPath) !== true) {
        http_response_code(500);
        echo "Impossible d'ouvrir l'archive";
        exit;
    }

    if (!is_dir($destDir) && !mkdir($destDir, 0755, true)) {
        http_response_code(500);
        echo "Impossible de créer le dossier de destination";
        exit;
    }

    for ($i = 0; $i < $zip->numFiles; $i++) {
        $entryName = str_replace('\\', '/', $zip->getNameIndex($i));
        $parts = array_values(array_filter(explode('/', $entryName), function($p){ return $p !== '';}));
        if (count($parts) === 0) continue;
        array_shift($parts); // on ignore le premier dossier
        $relative = implode('/', $parts);
        if ($relative === '') continue;
        if (strpos($relative, '..') !== false) continue; // sécurité

        $targetPath = "$destDir/$relative";

        if (substr($entryName, -1) === '/') {
            @mkdir($targetPath, 0755, true);
            continue;
        }

        @mkdir(dirname($targetPath), 0755, true);
        $stream = $zip->getStream($entryName);
        if ($stream) {
            $out = fopen($targetPath, 'wb');
            while (!feof($stream)) fwrite($out, fread($stream, 8192));
            fclose($out);
            fclose($stream);
        }
    }

    $zip->close();
}


header("Location: /live/$hash/RAPPORT.htm");

