<?php

//constantes

$rep_de_base="code";
$ce_fichier="list.php3";



//programme pour consulter les fichiers sur le site.

 if(!isset($rep))
  $rep=$rep_de_base;
  
 if(!file_exists($rep))
   $rep=$rep_de_base;
 
 //initialise la liste des répertoires et des fichiers
 $tab_r="";
 $tab_f="";
 $compteur_r=0;
 $compteur_f=0;
 
 $pointeur=opendir("$rep");
 
 //liste le contenu du répertoire
 
 while($filename = readdir($pointeur) ){
 
   if( ($filename!=".") & ($filename!="..") ) {
   
    $fichier="$rep/$filename";
    
    if(is_file($fichier)){
 
     $table[0]=$filename;
     //lit la première ligne du fichier.
     $fd = fopen ($fichier, "r");
     $inter2=fgets($fd,100);
     $inter3=strstr($inter2,"//");
     if($inter3) $table[1]=substr($inter3,2);
     fclose($fd);
     
     $tab_f[$compteur_f]=$table;
     $compteur_f++;
     }
     
     else{
      $tab_r[$compteur_r]=$filename;
      $compteur_r++;
      }
      
    }
    
   }
 
 
 //fonctions principales :o)
 
  function affiche_repertoire($rep,$lien){
   $repertoire="$rep/$lien";
   echo("<TR><TD><A  href=\"$ce_fichier?rep=$repertoire\">$lien</A></TD>");
   echo ("<TD>&nbsp;</TD></TR>");
   }
   
  function affiche_retour(){
   $lien=dirname($rep);
   echo("<TR><TD><A href=\"$ce_fichier?rep=$lien\">..</A></TD><TD>&nbsp;</TD></TR>");
   }
 
  function affiche_fichier($rep,$lien,$info){
     $voir="voir.php3";
     $repertoire="$rep/$lien";
     echo("<TR><TD><A  TARGET=\"_blank\" href=\"$voir?rep=$repertoire\">$lien</A></TD>");
     if($info=="") echo ("<TD>&nbsp;</TD></TR>");
      else echo("<TD>$info</TD></TR>");
   }
 
  //début de la page
  
  echo("<HTML><HEAD><TITLE>Fichiers Sources</TITLE></HEAD><BODY><TABLE BORDER=0>");
 
  //affiche le répertoire ".." éventuellement.
 
   if($rep!=$rep_de_base)
     affiche_retour();
     
  //affiche la liste des répertoires
  
   for($i=0;$i<$compteur_r;$i++)
    affiche_repertoire($rep,$tab_r[$i]);
    
  //affiche la liste des fichiers
  
   for($i=0;$i<$compteur_f;$i++)
    affiche_fichier($rep,$tab_f[$i][0],$tab_f[$i][1]);
    
   //fin de la page
   
   echo("</TABLE></BODY></HTML>");
   
    
   
   
   
   
   
  