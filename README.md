# Code Source d'Océane

Voilà donc les sources du jeu.

Elles comprennent:
1. l’interface de passage d’ordres en PHP et MySQL.
2. le programme de gestion en Java.

Vous pouvez télécharger le tout [ici](sources.zip) (680 ko) au format zip ou parcourir la description Javadoc [ici](./javadoc/index.html) et la liste des rares parties non publiques (pour des raisons d’intérêt de jeu) du programme [ici](non_disponibles.htm).

Une liste des changements des sources est disponible [ici](changement.txt) au format texte.

Enfin, un utilitaire à améliorer permet de consulter les sources en ligne [ici](list.php3).

La quasi-totalité du code n’est pas commentée. De plus, certaines modifications ont été faites à la hâte, ce qui explique que le code ne soit pas complètement « internationalisable ». Enfin, des problèmes de mémoire vive ont fait que la classe Rapport.java est légèrement incompréhensible actuellement (avec une classe temp.java). Ce n’est pas la seule modification en cours.

Dans quelques temps, tout le code passera sur Sourceforge, ce qui permettra à tout le monde de bosser dessus. L’objectif est de développer un jeu intéressant en réunissant toutes les énergies, voire de permettre à d’autres projets d’émerger plus facilement. Donc si vous voyez une amélioration possible, n’hésitez pas à la proposer sur ce [forum](http://jeu.oceane.forum.free.fr/w-agora/index.php3?bn=forumso_progra).

Une version 3 d’Océane est par ailleurs en préparation. Les principaux thèmes de la programmation Java seront les suivants à mon avis (par ordre décroissant d’importance :o) ):

- une interface graphique (Swing ?)
- des rapports en XML (double fonction : consultation / base de données pour l’interface graphique)
- la classe Univers ne devrait plus être statique : cela permettrait une interface gérant plusieurs parties/jeux à la fois
- une interface avec des bases de données est à améliorer en partant du package _zIgzAg.sql_
- enfin, petite modification, il faudra utiliser des Map spécifiques pour les entiers (en utilisant le package récent _zIgzAg.collection_), cela devrait permettre pas mal d’économies au niveau mémoire

**Ce programme est un logiciel libre ; il est disponible gratuitement. Vous pouvez le redistribuer et/ou le modifier conformément aux dispositions de la Licence Publique Générale GNU, telle que publiée par la Free Software Foundation.**

Un exemplaire de la licence est disponible ici : [lire la licence](GNU_licence.htm) (en anglais).

Par ailleurs, une traduction française non officielle de la licence est disponible [ici](traduction_GPL.htm). Consultez aussi à ce sujet les sites:
- http://www.opensource.org/osd.html
- http://www.gnu.org/copyleft/copyleft.fr.html
- http://www.gnu.org/copyleft/copyleft.html#translations

@+  
Julien/zIgzAg
