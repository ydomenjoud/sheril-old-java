<?php
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date du passé 
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); // toujours modifié 
header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
header("Pragma: no-cache"); // HTTP/1.0 

$base="jeu.oceane"; 
$langue="fr";
$nom_page="menu.php3";
$nom_cookie="ordres";
include "../script/aut.txt";
?>

<?php
include "../secure/connect.txt";
$base_ordre="z_ordres";


include "../script/fonctions.txt";

$t0=base3($base,$commandant,$base_ordre);
if(sizeof($t0)==0) {echo("Vos ordres ne sont pas disponibles");exit();}
if($t0=="") {echo("Vos ordres ne sont pas disponibles");exit();}

include "./fr/ordres.txt";

function affiche_ordre($i,$code_ordres,$description_ordres){
 $inter1=$code_ordres[$i];
 $inter2=$description_ordres[$i];
 echo("<LI><A href=\"./?table=$inter1\" target=\"fenetre\">$inter2</A></LI>");
 }
 
?>


<HTML>
<HEAD>
<META content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
</META>
<META content="zIgzAg" name="Author">
</META>
<TITLE></TITLE>
</HEAD>
<BODY background="../images/blkbck31.jpg" vlink="#008080" bgcolor="#000000" text="#FFFF00" link="#008080">
<B><FONT size="2">
<UL>
<LI><A href="./delog.php3?nom_cookie=<?php echo("$nom_cookie");?>" target="principal"><FONT color="#00FFFF">Quitter la console des ordres</FONT></A><br>(par sécurité, utilisez<i>
toujours</i> ce lien quand vous avez terminé de passer vos ordres)</LI>
<LI><A href="./" target="fenetre"><FONT color="#00FFFF">Page principale des ordres</FONT></A></LI>
<P>&nbsp;</P>
<LI><FONT color="#80FF80"><I><B>R&#233;solution des collisions entre les ast&#233;ro&#239;des, les mines anti-mati&#232;res,etc. et les flottes</B></I></FONT></LI>
<LI><FONT color="#FF0000">Diplomatie et recherche</FONT></LI>
<?php
 $j=0;
 while($t0[$j]<4) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>

<LI><FONT color="#80FF80"><I><B>R&#233;solution des votes au sein des alliances</B></I></FONT></LI>

<?php
 while($t0[$j]<11) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>

<LI><FONT color="#80FF80"><I><B>R&#233;solution des ench&#232;res</B></I></FONT></LI>

<?php
 while($t0[$j]<14) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>
<LI><FONT color="#FF0000">Gestion des syst&#232;mes</FONT></LI>

<?php
 while($t0[$j]<25) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>

<LI><FONT color="#FF0000">D&#233;placement</FONT></LI>

<?php
 while($t0[$j]<35) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>
<LI><FONT color="#FF0000">Dons et pr&#234;ts</FONT></LI>

<?php
 while($t0[$j]<43) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>
<LI><FONT color="#FF0000">Divers</FONT></LI>

<?php
 while($j<sizeof($t0)) {
  affiche_ordre($t0[$j],$code_ordres,$description_ordres);
  $j++;
  }
?>
<LI><FONT color="#80FF80"><I><B>- R&#233;solution des combats<BR>- Perception des revenus<BR>- Gestion des syst&#232;mes et des constructions<BR>- Finalisation du budget</B></I></FONT></LI></UL></FONT></B>

</BODY></HTML>