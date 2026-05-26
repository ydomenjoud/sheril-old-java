<?php
error_reporting(E_ALL & ~E_DEPRECATED);
require_once __DIR__ . '/../mysql_compat.php';
require_once __DIR__ . '/../secure/connect.txt';

function bbcode_to_html($text) {
    $text = htmlspecialchars($text);
    $text = nl2br($text);
    
    $search = [
        '/\[b\](.*?)\[\/b\]/is',
        '/\[i\](.*?)\[\/i\]/is',
        '/\[u\](.*?)\[\/u\]/is',
        '/\[url\](.*?)\[\/url\]/is',
        '/\[url=(.*?)\](.*?)\[\/url\]/is',
        '/\[img\](.*?)\[\/img\]/is',
        '/\[quote\](.*?)\[\/quote\]/is',
        '/\[color=(.*?)\](.*?)\[\/color\]/is',
        '/\[size=(.*?)\](.*?)\[\/size\]/is'
    ];
    
    $replace = [
        '<strong>$1</strong>',
        '<em>$1</em>',
        '<u>$1</u>',
        '<a href="$1" target="_blank">$1</a>',
        '<a href="$1" target="_blank">$2</a>',
        '<img src="$1" alt="Image" style="max-width:100%;">',
        '<blockquote>$1</blockquote>',
        '<span style="color:$1;">$2</span>',
        '<span style="font-size:$1px;">$2</span>'
    ];
    
    return preg_replace($search, $replace, $text);
}

function check_auth() {
    if (session_status() == PHP_SESSION_NONE) {
        session_start();
    }
//    if (!isset($_SESSION['commandant_num'])) {
//        die("Vous devez être connecté pour accéder au forum.");
//    }
    return intval(@$_SESSION['commandant_num']);
}

function get_author_name($id_author) {
    global $base;

    if (empty($id_author)) return null;
    $id_author = (int)$id_author;
    $sql = "SELECT NOM FROM aa_registre WHERE NUMERO = $id_author";
    $res = mysql($base, $sql);
    if ($res && $row = mysql_fetch_assoc($res)) {
        return htmlspecialchars($row['NOM']);
    }
    return "Inconnu (" . $id_author . ")";
}

function display_author($name, $numero, $race){
    return "<span class='race$race'>$name ($numero)</span>";
}