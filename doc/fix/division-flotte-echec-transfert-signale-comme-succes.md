# Division de flotte : succès rapporté même quand aucun vaisseau du type demandé n'existe

- **Fichiers modifiés** : `sources/zIgzAg/jeu/oceane/Commandant.java`
  (`diviserFlotte`), `sources/zIgzAg/jeu/oceane/MessagesInfo.java`
  (nouveau message d'erreur).
- **Fichier analysé (non modifié)** : `sources/zIgzAg/jeu/oceane/Flotte.java`
  (`diviserFlotte`, `trouverNumeroVaisseauLePlusEndommage`) — cause du
  transfert vide, mais le correctif porte sur la détection du résultat,
  pas sur ce comportement best-effort lui-même (voir §6).
- **Nature** : bug logique (absence de vérification du résultat réel
  avant d'annoncer un succès) — confirmé par exécution du vrai code de
  production sur les données réelles de la partie, disponibles dans
  `analyse/tour15/` (mêmes données que celles utilisées pour
  `doc/fix/construction-planetaire-espace-insuffisant-silencieux.md`).

## 1. Comportement observé (signalé par l'utilisateur)

Sur `https://sheril.pbem-france.net/yann/test/1tour16/`, le commandant 1
(« mabeur ») a passé 10 ordres de division de sa flotte **Phalange
Cyan(1)**, prélevant des vaisseaux **Archios II** (et **Curiosity**) pour
créer 10 nouvelles flottes nommées A à J. Les 10 messages de confirmation
apparaissent dans le rapport (`principal.htm`), mais aucune des 10
nouvelles flottes n'apparaît dans la liste des flottes du joueur
(`menu.htm`, `detailF.htm`).

## 2. Cause racine confirmée

Les ordres bruts de ce tour existent dans `analyse/tour15/dump.sql`
(tables `diviser_flotte` et `diviser_flotte_ajouter`, joueur 1, flotte 0
= Phalange Cyan) et l'état du commandant juste avant ce tour est
disponible dans `analyse/tour15/donnees/comm.txt`. Rejoués avec le vrai
code de production (`Commandant.diviserFlotte`, classes compilées du
projet, `Univers` réinitialisé a minima par réflexion — technologies,
plans de vaisseaux, systèmes) :

**Composition réelle de Phalange Cyan avant tout ordre de ce tour** :
```
CleanMate=4, Fregate standard=13, Grand Bombardier standard=13, Sidgin Fantom=3, Snip=3, scylla-vortex=113
```
→ **aucun "Archios II" ni "Curiosity"**. Ces types de vaisseaux, sur
lesquels portent 10 des 11 ordres de division, n'existent tout simplement
pas encore dans cette flotte à ce stade du traitement du tour (ils
proviennent vraisemblablement d'une production de chantier qui rejoint
la flotte plus tard dans le même tour — voir §5).

**Résultat de l'exécution réelle des 11 ordres, dans l'ordre de
soumission (id 125 à 136 dans `dump.sql`)** :

| Ordre | Type(s) demandé(s) | Présent dans Phalange Cyan ? | Vaisseaux transférés | Nouvelle flotte créée ? | Événement émis |
|---|---|---|---|---|---|
| A (div. 1) | 1 Curiosity + 18 Archios II | Non | 0 | **Non** | Succès |
| B (div. 2) | 1 Curiosity + 1 Archios II | Non | 0 | **Non** | Succès |
| C (div. 3) | idem | Non | 0 | **Non** | Succès |
| D (div. 4) | idem | Non | 0 | **Non** | Succès |
| E (div. 5) | 1 Archios II | Non | 0 | **Non** | Succès |
| F (div. 6) | 1 Archios II | Non | 0 | **Non** | Succès |
| G (div. 7) | 1 Archios II | Non | 0 | **Non** | Succès |
| H (div. 8) | 1 Archios II | Non | 0 | **Non** | Succès |
| I (div. 9) | 1 Archios II | Non | 0 | **Non** | Succès |
| J (div. 10) | 1 Curiosity | Non | 0 | **Non** | Succès |
| "22" (div. 11) | 1 scylla-vortex | **Oui** (113 en stock) | 1 | **Oui** | Succès |

Phalange Cyan termine à **148 vaisseaux** (149 − 1, seul le prélèvement
"22" a réellement eu lieu) et **une seule** nouvelle flotte est
effectivement ajoutée (`#14 : "22"`), alors que **les 11 ordres, y
compris les 10 qui n'ont rien transféré, émettent tous un événement de
succès identique** (`EV_COMMANDANT_DIVISER_FLOTTE_0000`, "Vous venez de
diviser votre flotte... pour donner la flotte...").

**Le défaut se situe dans `Commandant.diviserFlotte`** :

```java
Flotte ancienne = getFlotte(getCorrespondanceFlotte(numFlotte));
Flotte nouvelle = ancienne.diviserFlotte(code, nb, nouveauNom);
if (nouvelle.getNombreDeVaisseaux() != 0) {
    ajouterFlotte(nouvelle);
    ajouterCorrespondanceFlotte(10000 + numeroDivision, numeroFlotte(nouvelle));
}
if (ancienne.getNombreDeVaisseaux() == 0)
    eliminerFlotte(numFlotte);

return ajouterEvenement("EV_COMMANDANT_DIVISER_FLOTTE_0000",
        ancienne.getNomNumeroHTML(getCorrespondanceFlotte(numFlotte)),
        nouveauNom);
```

`Flotte.diviserFlotte` (appelé à la ligne 2) est **best-effort et
silencieux** :

```java
public Flotte diviserFlotte(String[] code, int[] nb, String nouveauNom) {
    Flotte retour = new Flotte(nouveauNom, position);
    retour.constructionEnCours = constructionEnCours;
    for (int i = 0; i < code.length; i++) {
        int index = trouverNumeroVaisseauLePlusEndommage(code[i]);
        int compteur = 0;
        while ((index != -1) && (compteur < nb[i])) {
            transfererVaisseau(retour, index);
            compteur++;
            index = trouverNumeroVaisseauLePlusEndommage(code[i]);
        }
    }
    return retour;   // peut être vide si aucun vaisseau du type demandé n'a été trouvé
}
```

Si `trouverNumeroVaisseauLePlusEndommage(code[i])` ne trouve aucun
vaisseau du type demandé (renvoie `-1`), la boucle `while` ne s'exécute
jamais pour ce type — **sans erreur, sans signal**. `nouvelle` peut donc
ressortir totalement vide (0 vaisseau transféré sur les N demandés).

De retour dans `Commandant.diviserFlotte`, le bloc `if
(nouvelle.getNombreDeVaisseaux() != 0)` gère correctement ce cas pour la
création de la flotte (elle n'est pas ajoutée si vide) — mais le
**dernier `return ajouterEvenement(...)` est inconditionnel** : il
s'exécute que la division ait réellement eu lieu ou non, avec exactement
le même message de succès dans les deux cas. Le joueur ne peut donc pas
distinguer "division réussie" de "division n'ayant rien transféré,
faute de vaisseaux du type demandé dans la flotte source".

## 3. Correctif appliqué

Faire dépendre le message émis du résultat réel de la division, à
l'image du principe déjà appliqué pour les constructions planétaires
(`doc/fix/construction-planetaire-espace-insuffisant-silencieux.md`) :
ne jamais annoncer un succès pour ce qui n'a pas eu lieu.

```diff
 		Flotte ancienne = getFlotte(getCorrespondanceFlotte(numFlotte));
 		Flotte nouvelle = ancienne.diviserFlotte(code, nb, nouveauNom);
-		if (nouvelle.getNombreDeVaisseaux() != 0) {
-			ajouterFlotte(nouvelle);
-			ajouterCorrespondanceFlotte(10000 + numeroDivision,
-					numeroFlotte(nouvelle));
-		}
+		if (nouvelle.getNombreDeVaisseaux() == 0)
+			return ajouterErreur("ER_COMMANDANT_DIVISER_FLOTTE_0001",
+					ancienne.getNomNumeroHTML(getCorrespondanceFlotte(numFlotte)));
+
+		ajouterFlotte(nouvelle);
+		ajouterCorrespondanceFlotte(10000 + numeroDivision,
+				numeroFlotte(nouvelle));
 		if (ancienne.getNombreDeVaisseaux() == 0)
 			eliminerFlotte(numFlotte);
 
 		return ajouterEvenement("EV_COMMANDANT_DIVISER_FLOTTE_0000",
 				ancienne.getNomNumeroHTML(getCorrespondanceFlotte(numFlotte)),
 				nouveauNom);
```

avec un nouveau message d'erreur ajouté dans `MessagesInfo.java`, juste
après `ER_COMMANDANT_DIVISER_FLOTTE_0000` :
```java
public static final String ER_COMMANDANT_DIVISER_FLOTTE_0001 = "Impossible de diviser la flotte {0} : aucun des types de vaisseaux demandés n''y a été trouvé.";
```
(apostrophe doublée `n''y` : convention `MessageFormat` déjà documentée
en tête de `MessagesInfo.java`, pour éviter que l'apostrophe simple ne
bascule le message en mode "texte brut" et n'avale le `{0}`.)

**Limite de ce correctif minimal** : il traite le cas "transfert
totalement vide" (celui observé ici, les 10 ordres A-J) mais pas le cas
**partiel** — par exemple demander 18 Archios II alors que la flotte
n'en contient que 5 : `nouvelle` aurait alors 5 vaisseaux (`!= 0`), la
flotte serait créée, l'événement de succès émis, mais rien n'indiquerait
au joueur que seulement 5 des 18 demandés ont été transférés. Une
solution complète nécessiterait que `Flotte.diviserFlotte` retourne
également le nombre réellement transféré par type (pas seulement la
`Flotte` résultante), pour permettre au message de refléter fidèlement
"X/Y {type} transférés" — cohérent avec le motif "nbbis/nb" déjà utilisé
pour les constructions planétaires. Cette extension est délibérément
laissée hors du correctif minimal, volontairement circonscrit au
symptôme confirmé (transfert totalement vide, silencieusement déclaré
réussi).

## 4. Vérification effectuée

Rejeu direct de `Commandant.diviserFlotte` (code réel, non mocké) sur
l'état réel du commandant 1 (`analyse/tour15/donnees/comm.txt`) avec les
11 ordres réels extraits de `analyse/tour15/dump.sql`, dans leur ordre de
soumission — via un `pom.xml` Maven temporaire pointant
`<sourceDirectory>sources</sourceDirectory>` (supprimé après
vérification, comme le reste des artefacts de build).

**Avant correctif** :

```
Composition Phalange Cyan AVANT tout ordre : {CleanMate=4, Fregate standard=13,
    Grand Bombardier standard=13, Sidgin Fantom=3, Snip=3, scylla-vortex=113}

diviserFlotte(0, ..., "A", 1) -> true | flotte0=149 vaisseaux | nbFlottesTotal=22
diviserFlotte(0, ..., "B", 2) -> true | flotte0=149 vaisseaux | nbFlottesTotal=22
... (idem pour C à J : flotte0 reste à 149, nbFlottesTotal reste à 22)
diviserFlotte(0, ..., "22", 11) -> true | flotte0=148 vaisseaux | nbFlottesTotal=23

=== Flottes APRES (23) ===
  #0 : Phalange Cyan (148 vaisseaux)
  ... (les 22 flottes préexistantes, inchangées)
  #14 : 22 (1 vaisseaux)     <- seule nouvelle flotte réellement créée

Evenements generes : 11   <- un succès pour CHACUN des 11 ordres, y compris les 10 qui n'ont rien transféré
```

**Après correctif**, rejeu strictement identique (mêmes 11 ordres, même
état initial) :

```
diviserFlotte(0, ..., "A", 1) -> false | flotte0=149 vaisseaux | nbFlottesTotal=22
diviserFlotte(0, ..., "B", 2) -> false | flotte0=149 vaisseaux | nbFlottesTotal=22
... (idem pour C à J : false, aucun changement d'état)
diviserFlotte(0, ..., "22", 11) -> true | flotte0=148 vaisseaux | nbFlottesTotal=23

=== Flottes APRES (23) ===
  (identique à avant correctif : seule "22" a été ajoutée — le correctif
  ne change aucune mécanique de transfert, seulement le signalement)

Evenements generes : 1
  EVENEMENT EV_COMMANDANT_DIVISER_FLOTTE_0000 params=[Phalange Cyan(1), 22]
```

Cette double exécution confirme, avec le vrai code et les vraies
données :
- **avant** : 10 des 11 divisions n'ont transféré aucun vaisseau et n'ont
  créé aucune flotte (car "Archios II"/"Curiosity" n'existaient pas dans
  Phalange Cyan à ce moment du traitement du tour), mais les 11 ont
  généré un événement de succès identique ;
- **après** : les mêmes 10 divisions renvoient `false` (une erreur est
  ajoutée à la place de l'événement de succès) et un seul événement de
  succès subsiste, pour la seule division ("22") qui a réellement
  transféré un vaisseau — le comportement observable côté état du jeu
  (quels vaisseaux bougent, quelles flottes existent) est rigoureusement
  identique avant/après, seul le signalement change.

## 5. Portée, limites, et question ouverte

- Explique entièrement le symptôme rapporté : les événements de succès
  visibles dans `principal.htm` proviennent bien de
  `diviserFlotte`/`ajouterEvenement`, et l'absence des 10 flottes dans
  `menu.htm`/`detailF.htm` s'explique simplement par le fait qu'elles
  n'ont jamais été créées (`nouvelle.getNombreDeVaisseaux() == 0`),
  sans qu'aucun bug de rendu du rapport ne soit en cause — les
  hypothèses de rendu/filtrage explorées dans une version précédente de
  ce document (collision de numéro de flotte, filtrage du rapport,
  destruction en combat, état périmé) sont donc bien écartées, comme
  déjà noté, mais pour la bonne raison cette fois : il n'y avait tout
  simplement rien à afficher.
- **Question ouverte, hors périmètre de ce correctif** : pourquoi le
  joueur a-t-il demandé de diviser des vaisseaux qui n'étaient pas
  encore dans sa flotte ? Deux explications possibles, non tranchées
  ici : (a) erreur du joueur (préparation de l'ordre sur la base d'un
  état anticipé de sa flotte après une production de chantier attendue
  ce tour, sans que l'interface ne l'avertisse que ces vaisseaux
  n'étaient pas encore présents au moment de la validation de l'ordre) ;
  (b) séquencement du tour : la production de vaisseaux
  (`Possession.resolutionConstructions`) et la réception des ordres
  (`ReceptionOrdres`, incluant `diviser_flotte`) sont deux étapes
  distinctes de `DeroulementDuTour.main` — une vérification du
  positionnement relatif de ces étapes permettrait de confirmer si des
  vaisseaux tout juste construits ce tour peuvent, par construction,
  ne jamais être disponibles pour une division ordonnée le même tour,
  ce qui rendrait ce genre d'erreur silencieuse particulièrement facile
  à déclencher pour n'importe quel joueur combinant construction et
  division dans le même tour.
- Le correctif appliqué (§3) ne change aucune mécanique de jeu (aucun
  vaisseau supplémentaire n'est transféré) : il rend seulement l'échec
  visible au lieu de le maquiller en succès.

## 6. Ce que ce correctif ne change pas (délibérément)

`Flotte.diviserFlotte` reste **best-effort** : demander plus de
vaisseaux d'un type que ce que contient la flotte source ne produit
toujours qu'un transfert partiel, silencieux au niveau de cette méthode.
Le correctif du §3 détecte uniquement le cas extrême (rien transféré du
tout) au niveau de `Commandant.diviserFlotte`, seul point d'entrée qui
communique avec le joueur. Documenté ici pour qu'un futur correctif visant
le cas partiel (§3, "Limite de ce correctif minimal") ne le redécouvre
pas depuis zéro.
