<?php
error_reporting(E_ALL & ~E_DEPRECATED);
ini_set('display_errors', 1);
$nom_page="index.php3";

if (!ini_get('register_globals')) {
    $superglobals = array($_SERVER, $_ENV, $_FILES, $_COOKIE, $_POST, $_GET);
    if (isset($_SESSION)) {
        array_unshift($superglobals, $_SESSION);
    }

    foreach ($superglobals as $superglobal) {
        foreach ($superglobal as $key => $value) {
            // Ne pas écraser les variables superglobales elles-mêmes
            if (!in_array($key, array('GLOBALS', '_SERVER', '_ENV', '_FILES', '_COOKIE', '_POST', '_GET', '_SESSION'))) {
                $GLOBALS[$key] = $value;
            }
        }
    }
}

include "../mysql_compat.php";
include "../secure/config.php";
include "../secure/connect.txt";
include "../script/aut.txt";
include "principal.txt";