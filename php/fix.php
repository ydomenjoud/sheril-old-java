<?php
session_start();
// rapport.php
$commandant = $_SESSION['commandant_num'];
$email = $_SESSION['commandant_email'];
$tour =  intval(file_get_contents("tour.txt"));

if(!$commandant || !$email || !$tour){
    header('Location: /ordres/ordres.php3');;
}

if (!$email || !$tour || !$commandant) {
    http_response_code(400);
    echo "Paramètres manquants";
    exit;
}

$filePath  = __DIR__ . "/errata/xml/$commandant/rapport.xml";

if(!file_exists($filePath)){
    http_response_code(404);
    echo "Fichier non trouvé";
    exit;
}

header('Content-Type: application/xml');
header('Content-Disposition: attachment; filename="detection.xml"');
header('Content-Length: ' . filesize($filePath));
readfile($filePath);
exit;


