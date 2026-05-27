<?php
require_once dirname(__FILE__) . '/../mysql_compat.php';
require_once dirname(__FILE__) . '/../secure/connect.txt';

if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

/**
 * Tente de connecter un utilisateur
 * @param string $login
 * @param string $password
 * @return bool True si succès, False sinon
 */
function auth_login($login, $password) {
    global $base;
    $base_registre = "aa_registre";
    
    $inter0 = mysql_real_escape_string(trim($login));
    $inter1 = mysql_real_escape_string(trim($password));
    
    $sql = "SELECT * FROM $base_registre WHERE LOGIN='$inter0' AND MOT_DE_PASSE='$inter1'";
    $result = mysql_query($sql);
    
    if ($result && mysql_num_rows($result) > 0) {
        $rf = mysql_fetch_assoc($result);
        
        $_SESSION['commandant_num'] = $rf['NUMERO'];
        $_SESSION['commandant_email'] = $rf['EMAIL'];
        $_SESSION['commandant_nom'] = $rf['NOM'];
        
        // Gestion legacy des fichiers de session si nécessaire (pour la console d'ordres par exemple)
        $rep_temp = dirname(__FILE__) . "/../script/tmp/";
        $nom_cookie = "sheril"; // Valeur par défaut si non définie
        
        if (isset($_COOKIE[$nom_cookie])) {
            $token = $_COOKIE[$nom_cookie];
            $inter = "a$token";
            $filename = "$rep_temp$inter";
            
            if (is_writable($rep_temp)) {
                $fd = fopen($filename, "w");
                if ($fd) {
                    fwrite($fd, $rf['NUMERO']);
                    fclose($fd);
                }
            }
        }
        
        return true;
    }
    
    return false;
}

/**
 * Déconnecte l'utilisateur
 */
function auth_logout() {
    $_SESSION = array();
    if (isset($_COOKIE[session_name()])) {
        setcookie(session_name(), '', time()-42000, '/');
    }
    session_destroy();
}

/**
 * Vérifie si l'utilisateur est connecté
 * @return int ID du commandant ou 0
 */
function auth_check() {
    if (isset($_SESSION['commandant_num'])) {
        return intval($_SESSION['commandant_num']);
    }
    return 0;
}
