<?php
/** @noinspection PhpLanguageLevelInspection */

include "../secure/connect.txt";
include "../script/fonctions.txt";
include "./fr/ordres.txt";

// $code_ordres
// $description_ordres

//phpinfo();

echo "<h1 class='searchable'> Liste des ordres déjà passés <input placeholder='rechercher' id='search' style='font-size: 16px'/></h1>";
for ($i = 0; $i < count($code_ordres); $i++) {
    $table = $code_ordres[$i];
    if(in_array($table,["diviser_flotte_ajouter", "creer_plan_ajouter", "creer_strategie_ajouter"])){
        continue;
    }
    (function ($varzaza) use ($base, $commandant,$langue, $nb_div, $nom_page, $description_ordres, $i) {
        $table = $varzaza;
        $a = $i;
        ob_start();
        include "./data/$varzaza.txt";
        include "affiche.txt";
        $content = ob_get_clean();
        if (strpos($content, "TABLE") !== false) {
            echo "<h2>{$description_ordres[$a]}</h2>";
            echo $content;
        }
    })($table);
}
