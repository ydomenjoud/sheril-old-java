<?php

include "../secure/connect.txt";
include "../script/fonctions.txt";
include "./fr/ordres.txt";
include "body.txt";

// récupération des divisions en cours
$result1 = mysql($base, "SELECT FLOTTE, NOM, NB_DIVISION FROM diviser_flotte WHERE NUMERO='$commandant'");
var_dump(mysql_fetch_assoc($result1));

// récupération des affectations de division en cours
$result2 = mysql($base, "SELECT FLOTTE, NB_DIVISION, TYPE, NOMBRE FROM diviser_flotte_ajouter WHERE NUMERO='$commandant'");
var_dump(mysql_fetch_assoc($result2));


?>

<h1>Division de flotte</h1>

</BODY>
</HTML>