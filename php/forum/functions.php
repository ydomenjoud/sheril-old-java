<?php
error_reporting(E_ALL & ~E_DEPRECATED);
require_once dirname(__FILE__) . '/../mysql_compat.php';
require_once dirname(__FILE__) . '/../secure/connect.txt';

function render_post_body($text) {
    // Quill produit du HTML, on autorise certaines balises et on nettoie le reste
    // Pour cet exercice, on va faire confiance au contenu mais idéalement il faudrait un purificateur HTML (type HTMLPurifier)
    // Ici on va juste s'assurer que si c'est de l'ancien BBCode on le traite encore, 
    // ou si c'est du nouveau HTML de Quill on le laisse passer.
    
    if (strpos($text, '<') !== false && strpos($text, '>') !== false) {
        // Probablement du HTML (Quill)
        return $text;
    }

    // Sinon traiter comme du BBCode (compatibilité anciens messages)
    $text = htmlspecialchars($text);
    $text = nl2br($text);
    
    $search = array(
        '/\[b\](.*?)\[\/b\]/is',
        '/\[i\](.*?)\[\/i\]/is',
        '/\[u\](.*?)\[\/u\]/is',
        '/\[url\](.*?)\[\/url\]/is',
        '/\[url=(.*?)\](.*?)\[\/url\]/is',
        '/\[img\](.*?)\[\/img\]/is',
        '/\[quote\](.*?)\[\/quote\]/is',
        '/\[color=(.*?)\](.*?)\[\/color\]/is',
        '/\[size=(.*?)\](.*?)\[\/size\]/is'
    );
    
    $replace = array(
        '<strong>$1</strong>',
        '<em>$1</em>',
        '<u>$1</u>',
        '<a href="$1" target="_blank">$1</a>',
        '<a href="$1" target="_blank">$2</a>',
        '<img src="$1" alt="Image" style="max-width:100%;">',
        '<blockquote>$1</blockquote>',
        '<span style="color:$1;">$2</span>',
        '<span style="font-size:$1px;">$2</span>'
    );
    
    return preg_replace($search, $replace, $text);
}

require_once dirname(__FILE__) . '/../includes/auth.php';

function check_auth() {
    return auth_check();
}



function format_date($date_str) {
    if (!$date_str || $date_str == '0000-00-00 00:00:00') return "Jamais";
    $time = strtotime($date_str);
    if (!$time) return $date_str;
    return date('d/m/y H\hi', $time);
}