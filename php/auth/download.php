<?php

session_start();

$num = intval($_SESSION['commandant_num']);
if($num <= 0){
    die("non num");
}

$tour = intval(file_get_contents("../tour.txt"));
if($tour <= 0){
    die("non tour");
}
$file = __DIR__ . "/../rapports/${tour}/${num}tour${tour}.zip";

if (!file_exists($file)) {
    http_response_code(404);
    exit("Fichier introuvable " . $file);
}

header('Content-Description: File Transfer');
header('Content-Type: application/zip');
header('Content-Disposition: attachment; filename="' . basename($file) . '"');
header('Content-Length: ' . filesize($file));
header('Cache-Control: must-revalidate');
header('Pragma: public');
header('Expires: 0');

readfile($file);
exit;