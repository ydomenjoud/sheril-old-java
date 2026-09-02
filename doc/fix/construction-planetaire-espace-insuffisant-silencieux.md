# Construction planétaire silencieusement tronquée par manque d'espace

- **Fichier modifié** : `sources/zIgzAg/jeu/oceane/Possession.java`
- **Méthode en cause** : `Possession.resolutionConstructions`
- **Nature** : bug logique (condition incomplète), pas un problème de
  données — atteignable en jeu normal, sans configuration particulière.

## 1. Comportement observé

Signalé par un utilisateur : sur une planète, une commande de 10
**Boucliers planétaires VII** programmée un tour donné ne voit qu'**une
seule unité sortir**, sans qu'aucun message d'erreur ou d'avertissement
n'explique le manque.

Retrouvé dans les données réelles fournies (`analyse/tour15/dump.sql`,
système 4-20) : le joueur 10, sur la position `0_4_20`, a une entrée de
construction `boucplaVII` avec `nombre = 10` et un avancement de 100 % —
c'est-à-dire que les 10 unités étaient entièrement financées ce tour-là
(assez de points de construction accumulés pour les 10). Seule une unité
a néanmoins été livrée.

## 2. Cause racine

`Possession.resolutionConstructions(Commandant com, Systeme s)` calcule,
pour chaque ligne de construction en cours, quatre limites indépendantes
sur le nombre d'unités qui peuvent réellement sortir ce tour : l'argent
(`pasAssezDeCentaure`), le minerai (`pasAssezDeMinerai`), les
marchandises (`pasAssezDeMarchandises`) et **l'espace libre sur le
système** (`pasAssezDePlace`, dérivée de `l_espace`, elle-même dérivée de
`s.getEspaceLibre(...)` et du nombre de points de structure du bâtiment).

`nbbis` (nombre réellement construit) est bien réduit par les quatre
limites :

```java
if (l_espace < nb) {
    nbbis = Math.min(l_espace, nbbis);
    pasAssezDePlace = true;
}
```

Mais au moment de décider **quel message envoyer au joueur**, seules
trois des quatre limites sont testées :

```java
// On envoi le log de la construction
if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises) {
    String nombreConstruit = nbbis + "/" + nb;
    List<String> manqueL = new ArrayList<>();
    if (pasAssezDeCentaure) manqueL.add("centaure");
    if (pasAssezDeMinerai) manqueL.add(Messages.MINERAI);
    if (pasAssezDeMarchandises) manqueL.add("marchandise (" + ... + ")");
    if (pasAssezDePlace) manqueL.add("d'espace libre"); // <- jamais atteint seul

    com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0002", ...);

} else { // Sinon la construction est OKAY
    com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0001",
            s.getPosition(), descriptionTechno, nbbis);
}
```

`pasAssezDePlace` est bien ajouté à la liste `manqueL` — mais cette
branche n'est atteinte que si l'une des trois *autres* limites est vraie.
Quand l'espace est la **seule** contrainte active (argent, minerai et
marchandises tous suffisants pour la totalité de la commande), la
condition est fausse, le code tombe dans le `else`, et émet
`EV_COMMANDANT_CONSTRUCTION_0001` — l'événement de **succès** — avec
`nbbis` (1, dans le cas rapporté) comme si c'était le nombre demandé. Le
joueur voit "1 Bouclier planétaire VII construit", sans jamais être
informé que 9 autres unités, pourtant payées, restent bloquées faute de
place (le même bug existe symétriquement dans la branche `nbbis == 0`,
avec `EV_COMMANDANT_CONSTRUCTION_0003`).

Les 9 unités restantes ne sont pas perdues (`c[i].diminuerNombre(nbbis)`
ne retire que celles réellement sorties) : elles restent programmées et
pourront sortir un tour ultérieur si de l'espace se libère — mais rien
dans les événements du tour ne le signale.

## 3. Correctif proposé

Inclure `pasAssezDePlace` dans les deux conditions qui décident d'émettre
un message d'avertissement plutôt qu'un message de succès muet :

```diff
-				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises) {
+				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises || pasAssezDePlace) {
 					String nombreConstruit = nbbis + "/" + nb;
 					...
 					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0002", ...);
 				} else { // Sinon la construction est OKAY
 					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0001", ...);
 				}
 			} else { // Si jamais il n'y a aucune construction ( nbbis == 0 )
-				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises) {
+				if (pasAssezDeCentaure || pasAssezDeMinerai || pasAssezDeMarchandises || pasAssezDePlace) {
 					String nombreConstruit = ""+nb;
 					...
 					com.ajouterEvenement("EV_COMMANDANT_CONSTRUCTION_0003", ...);
 				}
```

Aucun correctif compagnon nécessaire : `manqueL.add("d'espace libre")`
existait déjà dans les deux blocs, seule la condition de garde était
incomplète.

## 4. Vérification effectuée

Test écrit et exécuté sur la branche d'investigation
`audit/regles-vs-code-technologies` (`ConstructionEspaceInsuffisantSansMessageTest.java`,
**non commité sur cette branche**, conformément à la consigne de n'y
garder que le correctif), qui appelle directement
`Possession.resolutionConstructions` avec :

- une commande de 10 `boucplaVII` entièrement financée ce tour
  (`pointsEffectues = 10 × pointsParUnite`, `potentiel` mocké en
  conséquence sur `Systeme.getPointsDeConstructionModifie`),
- de l'argent et du minerai largement suffisants pour les 10 unités,
- un espace libre (`Systeme.getEspaceLibre`) ne permettant qu'**une
  seule** unité (structure de 50, espace de 50).

Résultat avant correctif (confirmé en exécutant le vrai code de
production, `Univers` mocké statiquement via Mockito) :

```
Tests run: 1, Failures: 1  // le test attend le message 0002, mais seul 0001 (succès, "1") est émis
```

Contre-vérification (sanity check) : en modifiant temporairement la
condition pour inclure `pasAssezDePlace` (le correctif visé), le même
test bascule de l'échec à la réussite — confirmant que le test discrimine
bien le comportement buggé du comportement corrigé, et n'est pas
tautologique.

Le correctif ci-dessus a ensuite été appliqué sur cette branche
(`fix/construction-planetaire-espace-insuffisant-silencieux`, à partir de
`develop`, disposition `sources/` + `libs/*.jar`) et revérifié via un
`pom.xml` Maven temporaire (non versionné, pointant
`<sourceDirectory>sources</sourceDirectory>` et un
`<testSourceDirectory>` temporaire contenant une copie du test) :

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0 -- ConstructionEspaceInsuffisantSansMessageTest
BUILD SUCCESS
```

Le `pom.xml` temporaire, le dossier de test temporaire et les répertoires
de build générés ont été supprimés après vérification — cette branche ne
contient que le correctif lui-même et ce rapport.

`EV_COMMANDANT_CONSTRUCTION_0002` est bien émis avec `nombreConstruit =
"1/10"` et la mention "d'espace libre" dans le libellé, et
`EV_COMMANDANT_CONSTRUCTION_0001` n'est plus émis pour ce cas. La file de
construction contient toujours, comme avant le correctif, une entrée
résiduelle de 9 unités (le correctif ne change pas ce qui est
effectivement construit, seulement l'information donnée au joueur).

## 5. Portée et limites du correctif

- S'applique à toute construction planétaire (bâtiment avec points de
  structure > 0 : boucliers, batteries, mines, chantiers...) dont l'espace
  libre du système est l'unique facteur limitant le nombre d'unités
  sorties ce tour.
- Ne change ni le nombre d'unités réellement construites (`nbbis`), ni la
  progression de la file de construction restante — seule l'information
  transmise au joueur (message de succès vs message d'avertissement) est
  corrigée.
- Ne résout pas un éventuel manque d'information sur *pourquoi* l'espace
  est insuffisant (nombre de points de structure déjà occupés, capacité
  totale du système) : le message se limite à mentionner "d'espace
  libre", comme pour les autres ressources manquantes — cohérent avec le
  niveau de détail déjà en place pour minerai/centaures/marchandises.
