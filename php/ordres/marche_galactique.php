<?php
include "body.txt";
include "../script/fonctions.txt";

if (POST('action') === 'vendre') {
    $v0 = mysql_real_escape_string(POST('v0'));
    $v1 = mysql_real_escape_string(POST('v1'));
    $v2 = intval(POST('v2'));
    $v3 = intval(POST('v3'));
    if (!empty($v0) && !empty($v1) && $v2 > 0 && $v3 > 0) {
        $query = "INSERT INTO vendre_galactique (NUMERO, v0, v1, v2, v3) 
                  VALUES ($commandant, '$v0', '$v1', $v2, $v3)";
        var_dump($query);
        mysql_query($query) or die("zIgzAg need HELP! Erreur insertion<BR>");
    }
}

echo "<h1>Marché Galactique</h1>";

// 1. FORMULAIRE DE VENTE
$t_sys = base1($base, $commandant, "z_systemes");
$t_march = base1($base, $commandant, "z_cargaison_chargement");
?>
<style>
    .split {
        display: flex;
    }
    .split > div {
        flex: 1;
    }
</style>
<section class="split">
    <div>
        <h2>Mettre en vente</h2>
        <form action="index.php3?table=vendre_acheter_galactique" method="POST">
            <input type="hidden" name="action" value="vendre"/>
            Je souhaite vendre sur le marché galactique : <br/>
            <input type="number" name="v2" size="5" value="1" min="1">
            <?php select1("v1", $t_march); // marchandise ?>
            de
            <?php select1("v0", $t_sys); // système ?>
            au prix unitaire de centaures
            <input type="number" name="v3" size="5" value="1" min="1">
            <!--    ( optionnel : planète-->
            <!--        --><?php //select_planete2("v4"); ?>
            <!--    )-->
            <input type="submit" name="ajout" value="Mettre en vente">
        </form>

        <h2>Liste des ordres déjà passés</h2>

        <?php

        $result = mysql_query("SELECT * FROM vendre_galactique WHERE NUMERO='$commandant'");
        for ($i = 0; $i < mysql_num_rows($result); $i++) {
            $row = mysql_fetch_array($result);
            ?>
            Vendre <?= $row['v2'] ?> <?= $t_march[$row['v1']] ?> à <?= $row['v3'] ?> centaures l'unité depuis <?= $t_sys[$row['v0']] ?>
            <br/>
            <?php
        }
        ?>
    </div>
    <div>
        <h2>Acheter</h2>
        <form action="index.php3?table=vendre_acheter_galactique" method="POST">
            <input type="hidden" name="action" value="acheter"/>

        </form>
    </div>
</section>
<?php
//CREATE TABLE `vendre_galactique` (
//`NUMERO` int(11) NOT NULL default '0',
//     `v0` text NOT NULL, -- Position du système (ex: "1.1")
//     `v1` text NOT NULL, -- Code marchandise
//     `v2` int(11) NOT NULL default '0', -- Quantité
//     `v3` int(11) NOT NULL default '0'  -- Prix unitaire
//) engine=MyISAM;

?>

<!---->
<!--echo "<hr>";-->
<!---->
<!--// 2. FORMULAIRE D'ACHAT / ANNULATION-->
<!--echo "<h2>Offres disponibles</h2>";-->
<!--$t_offres = base2($base, "z_galactique");-->
<!---->
<!--if (!$t_offres || count($t_offres) == 0) {-->
<!--    echo "<p>Aucune offre n'est disponible sur le marché actuellement.</p>";-->
<!--} else {-->
<!--    echo '<form action="index.php3?table=acheter_galactique" method="POST">';-->
<!--    echo "Sélectionner une offre : ";-->
<!--    select1("v0", $t_offres); // offre id-->
<!--    echo "<br>Recevoir sur le système : ";-->
<!--    select1("v1", $t_sys); // système de réception-->
<!--    echo ' <input type="submit" name="ajout" value="Acheter l\'offre sélectionnée">';-->
<!--    echo "<p><i>Note : Si vous achetez votre propre offre, elle sera annulée et les marchandises vous seront rendues sur le système d'origine.</i></p>";-->
<!--    echo "</form>";-->
<!--}-->

<!--?>-->
