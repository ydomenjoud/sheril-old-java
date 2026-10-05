<?php
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date du pass� 
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); // toujours modifi� 
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Pragma: no-cache"); // HTTP/1.0 

include "../script/mysql_compat.php";
include "../secure/config.php";
include "../script/aut.txt";

include "../secure/connect.txt";
$base_ordre = "z_ordres";


include "../script/fonctions.txt";

$t0 = base3($base, $commandant, $base_ordre);
if (sizeof($t0) == 0) {
    echo("Vos ordres ne sont pas disponibles");
    exit();
}
if ($t0 == "") {
    echo("Vos ordres ne sont pas disponibles");
    exit();
}

include "./fr/ordres.txt";

function affiche_ordre($i, $code_ordres, $description_ordres)
{

    $hiddenOrdres = array(54, 55, 56);

    $inter1 = $code_ordres[$i];
    $inter2 = $description_ordres[$i];
    $ordreNum = str_pad($i + 1, 2, "0", STR_PAD_LEFT);
    if(!in_array($i, $hiddenOrdres)){
        echo("<LI><A href=\"./?table=$inter1\" target=\"fenetre\">$ordreNum. $inter2</A></LI>");
    }
}

?><HTML lang="fr">
<HEAD>
    <META content="text/html; charset=UTF-8" http-equiv="Content-Type">
    <META content="zIgzAg" name="Author">
    <TITLE></TITLE>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Chakra+Petch:wght@500;600;700&family=IBM+Plex+Sans:ital,wght@0,400;0,500;0,600;1,400&family=IBM+Plex+Mono:wght@400;500&display=swap">
    <link rel="stylesheet" href="/assets/css/v2/sheril.css">
    <script>
        function filterList(event){
            const value = document.querySelector("#search")?.value?.toLowerCase();
            if(value && value.length > 0) {
                Array.from(document.querySelectorAll("#orderslist li")).forEach((item) => {
                    if(item.textContent.toLocaleLowerCase().match(value)){
                        item.classList.remove("hidden");
                    } else {
                        item.classList.add("hidden");
                    }
                })
            } else {

                Array.from(document.querySelectorAll("#orderslist li")).forEach((item) => {
                    item.classList.remove("hidden");
                });
            }
        }
    </script>
</HEAD>
<BODY id="console">
<div class="split">
    <A href="./" target="fenetre">Accueil</A>
    <A href="/rule/9_ordres_de_la_console_et_tour?embed=1" target="fenetre">Ordre du tour</A>
</div>
<input id="search" type="text" placeholder="rechercher un ordre" onkeyup="filterList()" />
<UL id="orderslist">
<!--    <LI><a href="index.php3?table=list_ordres" target="fenetre">Liste des ordres déjà passés</a></LI>-->
<!--    <P>&nbsp;</P>-->
    <LI><span class="important2">R&#233;solution des collisions entre les ast&#233;ro&#239;des, les mines
                    anti-mati&#232;res,etc. et les flottes</span></LI>
    <LI><FONT class="important">Diplomatie et recherche</FONT></LI>
    <?php
    $j = 0;
    while ($t0[$j] < 4) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>

    <LI><span class="important2">R&#233;solution des votes au sein des alliances</span></LI>

    <?php
    while ($t0[$j] < 11) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>

    <LI><span class="important2">R&#233;solution des ench&#232;res</span></LI>

    <?php
    while ($t0[$j] < 14) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>
    <LI><FONT class="important">Syst&#232;mes</FONT></LI>

    <?php
    while ($t0[$j] < 27) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>

    <LI><FONT class="important">Flottes</FONT></LI>

    <?php
    while ($t0[$j] < 36) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>
    <LI><FONT class="important">Dons et pr&#234;ts</FONT></LI>

    <?php
    while ($t0[$j] < 43) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>
    <LI><FONT class="important">Divers</FONT></LI>

    <?php
    while ($j < sizeof($t0)-2) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>

    <LI><FONT class="important">Marché galactique</FONT></LI>
    <?php
    while ($j < sizeof($t0)) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>
    <LI><span class="important2">R&#233;solution des combats</span></li>
    <LI><span class="important2">Perception des revenus</span></li>
    <LI><span class="important2">Gestion des syst&#232;mes et des constructions</span></li>
    <LI><span class="important2">Finalisation du budget</span></LI>
</UL>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const liens = document.querySelectorAll('#orderslist a');
        liens.forEach(lien => {
            lien.addEventListener('click', (event) => {
                liens.forEach(lien => lien.classList.remove('active'));
                lien.classList.add('active');
            });
        });
    })

</script>
</BODY>
</HTML>