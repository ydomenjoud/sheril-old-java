<?php
// mysql_compat.php - Polyfill MySQL vers MySQLi pour compatibilité PHP 7+
// À inclure en tout début de script

if (!function_exists('ereg_replace')) {
    function ereg_replace($pattern, $replacement, $string) {
        // ereg_replace utilisait des regex POSIX, preg_replace utilise PCRE
        // On convertit le pattern en délimiteur PCRE (ajouter /)
        return preg_replace('/' . $pattern . '/', $replacement, $string);
    }
}

if (!function_exists('ereg')) {
    function ereg($pattern, $string) {
        return preg_match('/' . $pattern . '/', $string);
    }
}

if (!function_exists('mysql_connect')) {
    $GLOBALS['___mysql_default_link'] = null;

    function mysql($base, $query) {
        return mysqli_query( $GLOBALS['___mysql_default_link'], $query);
    }
    function mysql_num_fields($result) {
        if ($result instanceof mysqli_result) {
            return mysqli_num_fields($result);
        }
        trigger_error("mysql_num_fields() : mauvais type de résultat", E_USER_WARNING);
        return false;
    }

    function mysql_connect($server = null, $username = null, $password = null, $new_link = false, $client_flags = 0) {
        $link = mysqli_connect($server, $username, $password);
        if ($link) {
            $GLOBALS['___mysql_default_link'] = $link;
        }
        return $link;
    }

    function mysql_pconnect($server = null, $username = null, $password = null, $client_flags = 0) {
        return mysql_connect('p:' . $server, $username, $password);
    }

    function mysql_close($link_identifier = null) {
        return mysqli_close($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }

    function mysql_select_db($database_name, $link_identifier = null) {
        return mysqli_select_db($link_identifier ?: $GLOBALS['___mysql_default_link'], $database_name);
    }

    function mysql_query($query, $link_identifier = null) {
        return mysqli_query($link_identifier ?: $GLOBALS['___mysql_default_link'], $query);
    }

    function mysql_unbuffered_query($query, $link_identifier = null) {
        return mysqli_query($link_identifier ?: $GLOBALS['___mysql_default_link'], $query, MYSQLI_USE_RESULT);
    }

    function mysql_fetch_row($result) {
        return mysqli_fetch_row($result);
    }

    function mysql_fetch_array($result, $result_type = MYSQLI_BOTH) {
        return mysqli_fetch_array($result, $result_type);
    }

    function mysql_fetch_assoc($result) {
        return mysqli_fetch_assoc($result);
    }

    function mysql_fetch_object($result, $class_name = 'stdClass', $params = []) {
        return mysqli_fetch_object($result, $class_name, $params);
    }

    function mysql_num_rows($result) {
        return mysqli_num_rows($result);
    }

    function mysql_numfields($result) {
        return mysqli_num_fields($result);
    }

    function mysql_field_name($result, $field_offset) {
        $props = mysqli_fetch_field_direct($result, $field_offset);
        return $props ? $props->name : false;
    }

    function mysql_data_seek($result, $row_number) {
        return mysqli_data_seek($result, $row_number);
    }

    function mysql_free_result($result) {
        return mysqli_free_result($result);
    }

    function mysql_affected_rows($link_identifier = null) {
        return mysqli_affected_rows($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }

    function mysql_insert_id($link_identifier = null) {
        return mysqli_insert_id($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }

    function mysql_error($link_identifier = null) {
        return mysqli_error($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }

    function mysql_errno($link_identifier = null) {
        return mysqli_errno($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }

    function mysql_real_escape_string($unescaped_string, $link_identifier = null) {
        return mysqli_real_escape_string($link_identifier ?: $GLOBALS['___mysql_default_link'], $unescaped_string);
    }

    function mysql_escape_string($unescaped_string) {
        return addslashes($unescaped_string); // comportement original
    }

    function mysql_result($result, $row, $field = 0) {
        mysqli_data_seek($result, $row);
        $datarow = mysqli_fetch_array($result);
        return isset($datarow[$field]) ? $datarow[$field] : false;
    }

    function mysql_set_charset($charset, $link_identifier = null) {
        return mysqli_set_charset($link_identifier ?: $GLOBALS['___mysql_default_link'], $charset);
    }

    function mysql_get_server_info($link_identifier = null) {
        return mysqli_get_server_info($link_identifier ?: $GLOBALS['___mysql_default_link']);
    }
} else {
//    echo "no overload";
}