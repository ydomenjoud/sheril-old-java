<?php
$fd = fopen ($rep, "r");
$texte=fread ($fd, filesize ($rep)); 
$texte=nl2br($texte);
echo($texte);

fclose($fd);
 
 ?>
