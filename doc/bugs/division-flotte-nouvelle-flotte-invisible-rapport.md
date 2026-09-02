# [EN COURS] Flotte issue d'une division introuvable dans le rapport de tour

- **Fichier(s) suspecté(s)** : `sources/zIgzAg/jeu/oceane/Commandant.java`
  (`diviserFlotte`), possiblement `Flotte.java` ou la séquence de
  résolution de tour (`DeroulementDuTour.java`, `Combat.java`).
- **Nature** : comportement non reproduit localement — analyse menée
  uniquement à partir d'un rapport de tour déjà généré, sans accès aux
  ordres bruts ni à l'état sérialisé (`comm.txt`/`sys.txt`) de cette
  partie. Contrairement aux investigations précédentes sur ce dépôt (voir
  `doc/fix/construction-planetaire-espace-insuffisant-silencieux.md`),
  aucune reproduction empirique sur du vrai code n'a pu être réalisée ici
  faute de données brutes.

## 1. Comportement observé (signalé par l'utilisateur)

Sur la partie accessible en `https://sheril.pbem-france.net/yann/test/1tour16/`,
le commandant 1 (« mabeur ») a passé 10 ordres de division de flotte,
prélevant des vaisseaux **Archios II** sur sa flotte **Phalange Cyan (1)**
pour créer 10 nouvelles flottes. D'après l'utilisateur, les divisions
« sont prises en compte mais ne sont pas affichées dans le rapport ».

## 2. Confirmé (preuves directes, récupérées par téléchargement brut +
   recherche exacte — pas de résumé IA, qui s'est révélé peu fiable sur
   ce fichier de 1,1 Mo, voir §5)

- **`principal.htm`** contient bien 10 messages d'événement du type :

  > Vous venez de diviser votre flotte **Phalange Cyan(1)** pour donner
  > la flotte **A** en y affectant **1 Curiosity, 18 Archios II**.

  (et 9 autres, donnant les flottes B, C, D, E, F, G, H, I, J, avec des
  compositions variées : `1 Curiosity, 18 Archios II`, puis huit fois
  `1 Curiosity/rien, 1 Archios II`). Ces messages correspondent très
  exactement au template `EV_COMMANDANT_DIVISER_FLOTTE_0000` = *"Vous
  venez de diviser votre flotte {0} pour donner la flotte {1}."*
  (`MessagesInfo.java`), donc à un appel réel et réussi de
  `Commandant.ajouterEvenement("EV_COMMANDANT_DIVISER_FLOTTE_0000", ...)`
  en fin de `Commandant.diviserFlotte`.

- **Le prélèvement côté source a bien eu lieu.** Reconstitution numérique
  à partir de `detailF.htm` (état final des flottes) et `combat.htm`
  (état de la flotte pendant la résolution des combats, qui a lieu
  *après* le traitement des ordres mais *avant* la génération du
  rapport — voir `DeroulementDuTour.java` lignes 63-75-167) :
  - `detailF.htm` : Phalange Cyan(1) termine le tour avec **23 Archios
    II**.
  - `combat.htm` : au début des rounds de combat de Phalange Cyan contre
    plusieurs planètes ennemies, la flotte affiche **28 Archios II**,
    puis diminue par pertes de combat (28 → 26 → 23) au fil des rounds —
    cohérent avec les 23 relevés en fin de tour dans `detailF.htm`.
  - Les 10 divisions retirent au total 18 + 1×8 = **26 Archios II**
    (la dixième, flotte J, ne prélève qu'1 Curiosity, pas d'Archios II).
  - `28 (vu en combat) + 26 (retirés par les 10 divisions) = 54` :
    cohérent avec un effectif de départ de 54 Archios II sur Phalange
    Cyan avant traitement des ordres de ce tour.

  **Conclusion de ce calcul** : le retrait des vaisseaux de la flotte
  source a réellement eu lieu (`Flotte.diviserFlotte` /
  `transfererVaisseau` ont fonctionné correctement) ; le combat qui suit
  n'implique que Phalange Cyan, jamais une des 10 nouvelles flottes.

- **Aucune des 10 flottes créées (A à J) n'apparaît nulle part dans le
  reste du rapport.** Recherche exacte (téléchargement direct + `grep`,
  pas de résumé IA) :
  - `menu.htm` (liste des flottes du joueur) : exactement 24 flottes
    listées (`Phalange Cyan(1)` à `Aethelgard(13)`, `Hadalis(16)`,
    `E1(21)` à `E8(28)`) — aucune trace de A, B, C, D, E, F, G, H, I ou
    J.
  - `detailF.htm` (détail de chaque flotte, une section par ancre
    `FLO0`…`FLO27`) : exactement les mêmes 24 flottes (ancres `FLO0` à
    `FLO15` puis `FLO20` à `FLO27`, aucune ancre supplémentaire) —
    confirmé par recherche des ancres, pas seulement des noms (élimine
    l'hypothèse d'un lien cassé mais d'un contenu présent ailleurs).
  - `combat.htm` : aucune mention de A à J, aucune mention de flotte
    détruite (`grep` sur "détruite" : 0 résultat).

  `Rapport.getDetailFlottes()` (source de `detailF.htm`, et des liens de
  `menu.htm`) itère `c.listeFlottesEtNumeros()` sans filtre autre qu'un
  garde-fou global (« si plus de 2000 vaisseaux au total, n'afficher
  aucune flotte ») manifestement non déclenché ici (les 24 flottes
  normales s'affichent). Cette boucle est exhaustive sur la map
  `flottes` du commandant : si une entrée y était présente au moment de
  la génération du rapport, elle apparaîtrait. Absence dans les deux
  pages ⇒ absence de l'entrée correspondante dans `Commandant.flottes` à
  l'instant de `Rapport.creation()`, malgré l'événement de succès déjà
  émis plus tôt dans le traitement du tour.

## 3. Hypothèses testées par lecture de code et écartées

1. **`Commandant.eliminerFlotte(numFlotte)` appelé avec un numéro non
   résolu.** Dans `diviserFlotte` :
   ```java
   if (ancienne.getNombreDeVaisseaux() == 0)
       eliminerFlotte(numFlotte);   // <- numFlotte brut, pas getCorrespondanceFlotte(numFlotte)
   ```
   alors que partout ailleurs dans la méthode, `numFlotte` passe par
   `getCorrespondanceFlotte(...)` avant usage. C'est une incohérence
   réelle (voir §6), mais elle ne peut pas expliquer *cette* disparition :
   Phalange Cyan ne s'est jamais vidée (elle finit le tour avec 23
   Archios II), donc `ancienne.getNombreDeVaisseaux() == 0` est faux à
   chacun des 10 appels, et cette ligne n'est jamais atteinte ici.
2. **Collision de numéro lors de `ajouterFlotte`/`numeroFlotteDisponible()`
   pour 10 flottes créées à la suite dans le même tour.** `flottes` est un
   `TreeMap<Integer,Flotte>` ; `numeroFlotteDisponible()` relit
   `listeNumerosFlottes()` (donc l'état courant, trié) à chaque appel et
   retourne le premier "trou" ou `travail.length`. Comme la map est
   mutée (`put`) avant l'appel suivant, deux créations successives ne
   peuvent pas recevoir la même clé — pas de collision possible par ce
   mécanisme.
3. **Réutilisation du même `numeroDivision` pour les 10 ordres, bloquant
   les 9 suivants via le garde `if (getCorrespondanceFlotte(10000 +
   numeroDivision) != -1) return false;`.** Écarté : si c'était le cas,
   un seul message d'événement serait généré (le premier), les 9 autres
   ordres retournant silencieusement `false` sans rien faire. Or les 10
   messages sont bien présents, chacun avec une composition différente
   et correcte (18 puis 1×8) — donc chaque appel a bien exécuté sa propre
   logique de division jusqu'au bout, `numeroDivision` était distinct à
   chaque fois.
4. **Filtrage de l'affichage côté rapport (`Rapport.getDetailFlottes()`).**
   Écarté : la boucle est exhaustive sur `c.listeFlottesEtNumeros()`, sans
   filtre par position/propriétaire/type — seul un total de vaisseaux
   supérieur à 2000 ferait tout disparaître (non déclenché, les 24
   flottes normales s'affichent bien).
5. **Destruction en combat.** Écarté : `combat.htm` ne mentionne jamais
   A à J, uniquement Phalange Cyan ; aucune occurrence de
   "détruite"/"detruite" dans tout le fichier.
6. **Rapport généré à partir d'un état de commandant périmé (rechargé
   depuis le disque entre le traitement des ordres et la génération du
   rapport).** Peu probable à la lecture de `DeroulementDuTour.main` :
   `ReceptionOrdres` et la boucle de génération de rapport utilisent tous
   deux `Univers.getListeCommandants()` / `Univers.getCommandant(...)`,
   c'est-à-dire les mêmes références d'objets en mémoire (pas de
   re-désérialisation identifiée entre les deux). Non totalement exclu
   sans exécution instrumentée (voir §4).

## 4. Ce qui manque pour trancher définitivement

Contrairement au cas `boucplaVII` du système 4-20 (`doc/fix/
construction-planetaire-espace-insuffisant-silencieux.md`), cette partie
(`sheril.pbem-france.net/yann/test/1tour16`) n'est pas un dépôt de
données que nous contrôlons : nous n'avons accès qu'aux pages HTML déjà
générées, pas aux fichiers `comm.txt`/`sys.txt` sérialisés ni à la table
d'ordres bruts. Pour confirmer une cause précise plutôt que la
circonscrire, il faudrait :

- soit un export `comm.txt` (ou équivalent) de cette partie juste après
  ce tour, pour vérifier directement le contenu de `flottes` sur le
  commandant 1 et son `correspondanceFlotteDivisee` ;
- soit les 10 lignes d'ordre `diviser_flotte` / `diviser_flotte_ajouter`
  brutes soumises par le joueur, pour vérifier notamment si le même
  `numeroDivision` ou le même nom (`nouveauNom`) a pu être réutilisé
  d'une façon qui produirait un effet différent de celui supposé en §3.3 ;
- soit une exécution instrumentée de `DeroulementDuTour` (ou au moins de
  `Commandant.diviserFlotte` + `resolutionGestionFlottes` +
  `Rapport.getDetailFlottes()`) avec des logs ajoutés autour de
  `ajouterFlotte`/`eliminerFlotte`/`flottes.size()`, rejouée sur un jeu
  de données reproduisant ce scénario (une flotte, 10 divisions
  successives dans le même tour, puis un combat de la flotte source).

## 5. Remarque méthodologique (pour de futures investigations sur rapports distants)

La recherche initiale via l'outil de récupération web avec résumé par IA
a signalé, à tort, une **absence totale** de toute mention de
"diviser"/"division" dans `principal.htm` (1,1 Mo) — un faux négatif dû à
la troncature/au résumé du contenu avant analyse, non à une absence
réelle. Le téléchargement brut du fichier (`curl`) suivi d'une recherche
exacte (`grep`) a immédiatement révélé les 10 événements. **Sur des
pages volumineuses, préférer systématiquement le téléchargement brut et
une recherche exacte à un résumé automatique**, qui peut masquer
l'information recherchée sans le signaler comme tel.

## 6. Défaut connexe identifié (indépendant de ce rapport, à corriger par ailleurs)

`Commandant.diviserFlotte` (ligne ~3469) :

```java
if (ancienne.getNombreDeVaisseaux() == 0)
    eliminerFlotte(numFlotte);
```

utilise le numéro de flotte **brut** (`numFlotte`, tel que soumis par le
joueur dans l'ordre) au lieu du numéro résolu via
`getCorrespondanceFlotte(numFlotte)`, contrairement à tous les autres
usages de `numFlotte` dans la même méthode (`getFlotte(getCorrespondanceFlotte(numFlotte))`
à la ligne précédente, et de nouveau à la ligne du message final).

`getCorrespondanceFlotte(n)` renvoie `n` tel quel pour tout `n < 10000`
(la très grande majorité des cas : un ordre portant sur une flotte "réelle"
déjà numérotée), donc cette incohérence est **invisible dans le cas
courant**. Elle ne se manifeste que lorsque `numFlotte` est lui-même une
référence "virtuelle" à une flotte issue d'une division antérieure **du
même tour** (`numFlotte >= 10000`, cf. `ajouterCorrespondanceFlotte(10000
+ numeroDivision, ...)`) — c'est-à-dire lorsqu'un joueur enchaîne, dans le
même tour, une nouvelle division sur une flotte tout juste créée par une
division précédente, et que cette sous-flotte se retrouve entièrement
vidée par la seconde division. Dans ce cas précis :

- `eliminerFlotte(numFlotte)` avec `numFlotte >= 10000` ne supprime rien
  de `flottes` (aucune entrée n'existe à cette clé), laissant une flotte
  fantôme à 0 vaisseau dans la map, jamais nettoyée ;
- le message final reste correct (il recalcule
  `getCorrespondanceFlotte(numFlotte)` séparément), donc ce défaut est
  silencieux : ni erreur, ni incohérence visible dans le rapport, juste
  une entrée à 0 vaisseau qui traîne indéfiniment dans les données du
  commandant.

Ce n'est **pas la cause confirmée** du symptôme de ce rapport (le
scénario qui le déclenche — division en chaîne sur une sous-flotte issue
d'une division du même tour — ne correspond pas au cas observé, où les
10 divisions partent toutes directement de Phalange Cyan). Il est
documenté ici comme défaut réel trouvé pendant l'investigation, distinct
du symptôme à l'origine du signalement.

## 7. Correctif proposé pour le défaut du §6 — non implémenté

```diff
 		if (ancienne.getNombreDeVaisseaux() == 0)
-			eliminerFlotte(numFlotte);
+			eliminerFlotte(getCorrespondanceFlotte(numFlotte));
```

Aligne ce dernier usage de `numFlotte` sur tous les autres de la même
méthode. Changement d'une ligne, sans effet sur le cas courant
(`getCorrespondanceFlotte(numFlotte) == numFlotte` pour `numFlotte <
10000`), et qui supprime correctement la sous-flotte vidée dans le cas
d'une division en chaîne au sein du même tour.

**Non implémenté à ce stade** : ce correctif, bien que sain en soi,
n'ayant pas été identifié comme la cause du symptôme rapporté (§3.1), son
application ne doit pas être présentée comme une résolution du ticket
utilisateur tant que la cause réelle de la disparition des 10 nouvelles
flottes n'est pas confirmée (§4).

## 8. Prochaines étapes

1. Demander au porteur de la partie (ou à l'administrateur du serveur
   `sheril.pbem-france.net`) un export de l'état du commandant 1 juste
   après ce tour (`comm.txt` ou équivalent), ou l'accès aux ordres bruts
   des 10 divisions.
2. Une fois ces données disponibles, reproduire l'investigation avec la
   même méthode que pour `doc/fix/construction-planetaire-espace-
   insuffisant-silencieux.md` (§3-4 de ce document) : rejouer le/les
   ordres avec le vrai code de production (`Commandant.diviserFlotte`,
   `resolutionGestionFlottes`, `Rapport.getDetailFlottes()`) sur l'état
   réel, écrire un test isolé qui verrouille le comportement observé.
3. Appliquer le correctif du §6/§7 dans tous les cas (défaut réel,
   indépendant de la conclusion sur la cause de ce ticket), avec son
   propre test de non-régression (scénario : division en chaîne dans le
   même tour, sous-flotte intermédiaire totalement reprélevée).
