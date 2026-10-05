<?php
require_once __DIR__ . '/../includes/Parsedown.php';

function include_markdown($path) {
    if (!file_exists($path)) {
        return '';
    }

    $parsedown = new Parsedown();
    $parsedown->setSafeMode(false);

    return $parsedown->text(file_get_contents($path));
}
