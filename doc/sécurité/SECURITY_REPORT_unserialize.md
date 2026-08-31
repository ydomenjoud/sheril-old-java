# Rapport de vulnérabilité — Désérialisation PHP non validée dans `principal.txt`

**Date :** 2026-08-28
**Révisé :** 2026-08-28 — portée précisée après confirmation manuelle (voir [Révision](#révision-2026-08-28))
**Sévérité :** CRITIQUE (confirmé — XSS réfléchie exploitable)
**Statut :** Corrigé

---

## Résumé

Le paramètre `$_GET['previous']` est passé directement à `unserialize()` sans aucune validation. Cela permet à un attaquant authentifié d'injecter des valeurs arbitraires dans les champs `$_POST` du formulaire cible. Confirmé exploitable en **XSS réfléchie** via `php/ordres/fr/choix/vendre_galactique.txt`, qui réaffiche une valeur injectée sans échappement dans un attribut HTML.

---

## Localisation

| Fichier | Ligne | Variable vulnérable |
|---------|-------|---------------------|
| `php/ordres/principal.txt` | 18 | `$_GET['previous']` |
| `php/ordres/principal.txt` | 30 | `$_GET['previous']` |
| `php/ordres/fr/choix/vendre_galactique.txt` | 5, 11 | `$_POST['v2']` / `$_POST['v3']` réaffichés sans échappement — point de sortie exploité |

---

## Cause racine

```php
// principal.txt:17-22
if (array_key_exists('previous', $_GET)) {
    $tableau = unserialize(urldecode($_GET['previous']));
    for ($i = 1; $i < sizeof($tableau); $i++) {
        $_POST['v' . ($i - 1)] = $tableau[$i];  // injection dans $_POST
    }
}
```

`unserialize()` est appelé sur une chaîne fournie directement par l'utilisateur. La fonctionnalité légitime est de pré-remplir un formulaire avec les valeurs d'un ordre précédent (bouton "Copier"), mais aucune validation de l'origine ou du contenu n'est effectuée.

`php/ordres/fr/choix/vendre_galactique.txt` réaffiche ensuite ces valeurs sans échappement :

```php
<input type="number" value="<?=(array_key_exists('v2', $_POST) ? $_POST['v2'] : 1)?>" min="1" name="v2">
```

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui — n'importe quel compte joueur |
| Complexité | Faible |
| Outils nécessaires | Navigateur web |

---

## Proof of Concept — XSS réfléchie confirmée

### Requête HTTP

```
GET /ordres/index.php3?table=vendre_galactique&previous=a%3A4%3A%7Bi%3A0%3Bs%3A1%3A%22a%22%3Bi%3A1%3Bs%3A1%3A%22b%22%3Bi%3A2%3Bs%3A1%3A%22c%22%3Bi%3A3%3Bs%3A41%3A%22%22%3E%3Cscript%3Ealert(document.cookie)%3C%2Fscript%3E%22%3B%7D
Cookie: PHPSESSID=<session_valide>
```

### Payload désérialisé

```php
// a:4:{i:0;s:1:"a";i:1;s:1:"b";i:2;s:1:"c";i:3;s:41:""><script>alert(document.cookie)</script>";}
// Résultat : $_POST['v2'] = '"><script>alert(document.cookie)</script>'
```

### Résultat confirmé

Le HTML retourné contient littéralement :
```html
<input type="number" value=""><script>alert(document.cookie)</script>" min="1" name="v2">
```

Le `<script>` casse l'attribut `value` et s'exécute tel quel dans le navigateur de la victime. Confirmé par exécution directe de l'URL — `alert(document.cookie)` se déclenche.

---

## Impact

- **XSS réfléchie confirmée** : exécution de JavaScript arbitraire dans le contexte de la session de la victime (vol de session/cookie, actions au nom du joueur) via un lien forgé — nécessite que la victime clique le lien pendant qu'elle est authentifiée
- **Injection dans les champs de formulaire** : pré-remplissage arbitraire de n'importe quel ordre, y compris pour des `table` où aucun template ne réaffiche la valeur sans échappement (impact limité à la manipulation de formulaire dans ce cas)
- **Contournement de l'interface** : valeurs non autorisées par l'UI peuvent être injectées

---

## Correctif

Remplacer `unserialize()` par `json_decode()` :

```php
// AVANT — vulnérable
$tableau = unserialize(urldecode($_GET['previous']));

// APRÈS — sécurisé
$tableau = json_decode(urldecode($_GET['previous']), true);
if (!is_array($tableau)) $tableau = [];
```

Le bouton "Copier" (`affiche.txt`) qui génère ce paramètre doit être mis à jour en conséquence pour émettre du JSON plutôt que du sérialisé PHP.

> ⚠️ **Insuffisant à lui seul** — voir la note de re-test ci-dessous. Ce correctif ferme le risque d'injection d'objet PHP mais ne ferme pas la XSS : `json_decode()` peut transporter la même chaîne malveillante que `unserialize()`, elle atteint `$_POST['v2']`/`['v3']` de la même façon. Le vrai correctif de la XSS est côté sortie, dans `vendre_galactique.txt`.

### Correctif réel de la XSS — `vendre_galactique.txt`

```php
// AVANT — vulnérable (valeur réaffichée telle quelle dans l'attribut HTML)
<input type="number" value="<?=(array_key_exists('v2', $_POST) ? $_POST['v2'] : 1)?>" min="1" name="v2">

// APRÈS — corrigé (cast en entier, cohérent avec le type réel du champ — int(11) en base)
<input type="number" value="<?=(array_key_exists('v2', $_POST) ? intval($_POST['v2']) : 1)?>" min="1" name="v2">
```

Même correctif pour `v3`.

---

## Révision 2026-08-28

Le rapport initial mentionnait une **RCE potentielle via gadget chains PHP** (méthodes magiques `__destruct`, `__wakeup`, `__toString`) comme impact possible de la désérialisation non validée.

**Analyse à tête reposée :** aucune classe PHP n'est définie dans l'application (`php/`), et le `Dockerfile` (base `php:7.1-apache`) n'installe ni Composer ni `vendor/` — seules les extensions `mysqli`, `pdo_mysql` et `zip` sont chargées, sans `soap` ni autre extension connue pour ses gadget chains triviales. Sans autoloader ni classes applicatives, il n'existe pas de gadget chain immédiatement exploitable dans cette stack : **la mention RCE est retirée**, faute de preuve de concept.

En contrepartie, une **XSS réfléchie concrète et confirmée** a été identifiée en creusant l'usage réel des valeurs injectées (`vendre_galactique.txt`), non mentionnée dans le rapport initial. La sévérité CRITIQUE est donc **maintenue**, mais reposant désormais sur un impact confirmé (vol de session via XSS) plutôt que sur une hypothèse de RCE non démontrée.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'analyse de `principal.txt` |
| 2026-08-28 | Confirmation — valeur injectée dans `$_POST` visible dans la réponse |
| 2026-08-28 | Rapport rédigé (mention RCE non vérifiée) |
| 2026-08-28 | Recherche des points de sortie non échappés → XSS réfléchie confirmée via `vendre_galactique.txt` (`alert(document.cookie)` déclenché) ; mention RCE retirée faute de gadget chain disponible dans la stack |
| 2026-08-28 | Correctif `json_decode()` appliqué (`fix/unserialize-previous-param`) |
| 2026-08-28 | Re-test post-correctif : XSS toujours déclenchée (`alert()` toujours exécutée) — `json_decode()` seul ne ferme pas la XSS, `$_POST['v2']`/`['v3']` toujours réaffichés sans échappement |
| 2026-08-28 | Correctif complémentaire : cast `intval()` sur `v2`/`v3` dans `vendre_galactique.txt` |
