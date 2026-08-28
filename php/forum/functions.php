<?php
error_reporting(E_ALL & ~E_DEPRECATED);
require_once dirname(__FILE__) . '/../mysql_compat.php';
require_once dirname(__FILE__) . '/../secure/connect.txt';

function render_post_body($text) {
    // Quill produit du HTML : on autorise une liste blanche de balises et on
    // supprime les attributs porteurs de JavaScript (on*=, javascript:).

    if (strpos($text, '<') !== false && strpos($text, '>') !== false) {
        $allowed_tags = '<p><br><strong><em><u><b><i><a><img><blockquote><span><ul><ol><li><h1><h2><h3>';
        $clean = strip_tags($text, $allowed_tags);
        // Supprime tout attribut on*="..." (gestionnaires d'évènements JS)
        $clean = preg_replace('/\s+on\w+\s*=\s*("[^"]*"|\'[^\']*\'|[^\s>]+)/i', '', $clean);
        // Neutralise les URLs javascript: dans href/src
        $clean = preg_replace('/(href|src)(\s*=\s*)("|\')\s*javascript:[^"\']*\3/i', '$1$2$3#$3', $clean);
        return $clean;
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