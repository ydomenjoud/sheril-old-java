<?php
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date du pass� 
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); // toujours modifi� 
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Pragma: no-cache"); // HTTP/1.0 

include "../mysql_compat.php";
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
    $inter1 = $code_ordres[$i];
    $inter2 = $description_ordres[$i];
    $ordreNum = str_pad($i + 1, 2, "0", STR_PAD_LEFT);
    echo("<LI><A href=\"./?table=$inter1\" target=\"fenetre\">$ordreNum. $inter2</A></LI>");
}

?>


<HTML lang="fr">
<HEAD>
    <META content="text/html; charset=UTF-8" http-equiv="Content-Type">
    <META content="zIgzAg" name="Author">
    <TITLE></TITLE>
    <STYLE type="text/css">
        @import url("https://fonts.googleapis.com/css?family=Roboto&display=swap");
        /*@import url("https://necolas.github.io/normalize.css/8.0.1/normalize.css");*/

        /**
        Cyan / turquoise lumineux (#00e6e6 ou #1de9b6) → cohérent avec les tons spatiaux, futuristes.
        Violet électrique (#9c27b0 ou #b388ff) → garde l’impact visuel sans rappeler “danger”.
        Orange doux (#ff9800 ou #ffa726) → attire l’œil, mais plus chaleureux que le rouge.
        Vert néon (#4caf50 ou #69f0ae) → bonne lisibilité sur fond sombre, mais attention à ne pas le confondre avec une validation.
         */
        :root {
            --header: #ff9800;
            --help: #ad98d5;
            --important: #9c27b0;
        }

        .important {
            color: var(--important);
        }
        .important2 {
            color: #69f0ae;
            display: block;
            padding: 5px;
        }

        * {
            box-sizing: border-box;
            font-size: inherit;
        }


        a {
            color: #8BC7FF;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
            text-decoration-style: revert;
            color: #a7d0f6;
        }

        html, body {
            font-size: 12px;
        }

        body {
            padding-top: 30px;
            color: #dedede;
            font-weight: normal;
            font-family: 'Roboto', sans-serif;
            background: linear-gradient(to top right, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 1) 10%, rgba(0, 0, 0, 0.8) 100%) fixed;
        }
        button, select, input:not([type="checkbox"]), textarea {
            background: #001021;
            color: #dedede;
            border: 1px solid #a7d0f6;
            box-shadow: 0px 1px 4px #003963;
            cursor: pointer;
            border-radius: 1px;
            padding: 2px 5px;
        }

        p {
            margin: 0;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            list-style: none;
        }

        li:has(font.important) {
            padding-top: 10px;
            font-size: 1.2em;
            border-bottom: 1px solid var(--important);
            margin-bottom: 5px;
        }

        li a {
            display: block;
            padding: 5px;
        }


        li a:hover {
            background-color: #403f3f;
            text-decoration: none;
        }

        #logout {
            position: absolute;
            top: 10px;
            right: 10px;
        }
        #home {
            position: absolute;
            top: 10px;
            left: 10px;
        }

        input#search {
            display: block;
            width: 100%;
            padding: 5px;
        }

        .hidden {
            display: none;
        }

    </STYLE>
    <script>
        function filterList(event){
            const value = document.querySelector("#search")?.value?.toLowerCase();
            if(value && value.length > 0) {
                Array.from(document.querySelectorAll("#ordersList li")).forEach((item) => {
                    if(item.textContent.toLocaleLowerCase().match(value)){
                        item.classList.remove("hidden");
                    } else {
                        item.classList.add("hidden");
                    }
                })
            } else {

                Array.from(document.querySelectorAll("#ordersList li")).forEach((item) => {
                    item.classList.remove("hidden");
                });
            }
        }
    </script>
</HEAD>
<BODY>
<A id="logout" href="./delog.php3?nom_cookie=<?php echo("$nom_cookie"); ?>" target="principal">Logout</A>
<A id="home" href="./" target="fenetre">Accueil</A>
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
    <LI><FONT class="important">Gestion des syst&#232;mes</FONT></LI>

    <?php
    while ($t0[$j] < 25) {
        affiche_ordre($t0[$j], $code_ordres, $description_ordres);
        $j++;
    }
    ?>

    <LI><FONT class="important">D&#233;placement</FONT></LI>

    <?php
    while ($t0[$j] < 35) {
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

</BODY>
</HTML>