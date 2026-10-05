# Rapport de vulnérabilité — Injection SQL dans `division.php`

**Date :** 2026-08-28  
**Sévérité :** CRITIQUE  
**Statut :** Non corrigé

---

## Résumé

Une injection SQL dans le paramètre `identifier` de `division.php` permet à tout joueur authentifié d'exécuter des requêtes SQL arbitraires sur la base de données.

---

## Localisation

| Fichier | Ligne | Variable vulnérable |
|---------|-------|---------------------|
| `php/ordres/division.php` | 50 | `$_GET['identifier']` |

---

## Cause racine

```php
// VULNÉRABLE — $_GET['identifier'] concaténé sans validation ni cast
if (isset($_GET['elimine']) && $_GET['elimine'] == 0) {
    mysql($base, "DELETE FROM diviser_flotte WHERE id = {$_GET['identifier']} AND NUMERO=$commandant");
```

`$_GET['identifier']` est interpolé directement dans la requête SQL sans `intval()` ni requête préparée.

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui — n'importe quel compte joueur |
| Complexité | Faible |
| Outils nécessaires | Navigateur web |

---

## Proof of Concept — Confirmation par injection temporelle

La page est accessible via `index.php3` (qui initialise la session et `$commandant`). Avec 2 lignes présentes dans `diviser_flotte` pour le commandant courant :

**Requête HTTP :**
```
GET /ordres/index.php3?table=diviser_flotte&elimine=0&flotte_id=1&identifier=SLEEP(5)
Cookie: PHPSESSID=<session_valide>
```

**SQL exécuté :**
```sql
DELETE FROM diviser_flotte WHERE id = SLEEP(5) AND NUMERO=<commandant>
```

**Résultat mesuré :**

| Payload | Lignes en base | Délai observé | Attendu |
|---------|---------------|---------------|---------|
| `SLEEP(5)` | 2 | **10 111 ms** | 2 × 5 000 ms |
| `SLEEP(0)` | 2 | 148 ms | ~0 ms |

Le délai de 10 111 ms = 2 × SLEEP(5) confirme l'exécution de la condition WHERE ligne par ligne.

> **Note :** Aucune donnée n'est supprimée — `SLEEP(5)` retourne `0`, et aucune ligne n'a `id = 0`.

---

## Impact

- Lecture arbitraire de la base de données (extraction time-based)
- Exécution de fonctions MySQL (`SLEEP`, `EXTRACTVALUE`, etc.)
- Combiné avec `principal.txt` (voir rapport dédié) : extraction directe des credentials

---

## Correctif

**`php/ordres/division.php:50`** — une ligne à modifier :

```php
// AVANT
mysql($base, "DELETE FROM diviser_flotte WHERE id = {$_GET['identifier']} AND NUMERO=$commandant");

// APRÈS
$identifier = intval($_GET['identifier']);
mysql($base, "DELETE FROM diviser_flotte WHERE id = $identifier AND NUMERO=$commandant");
```

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte |
| 2026-08-28 | Confirmation par mesure temporelle (10 111 ms) |
| 2026-08-28 | Rapport rédigé |
| En attente | Correction |
