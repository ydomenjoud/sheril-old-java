# Rapport de vulnérabilité — Injection SQL dans `elimine.txt`

**Date :** 2026-08-28  
**Sévérité :** HAUTE  
**Statut :** Non corrigé

---

## Résumé

La même injection SQL via `$_GET['table']` que dans `principal.txt` permet ici deux exploitations distinctes : la divulgation du schéma de n'importe quelle table via `SHOW COLUMNS`, et la suppression d'ordres dans des tables arbitraires.

---

## Localisation

| Fichier | Ligne | Variable vulnérable |
|---------|-------|---------------------|
| `php/ordres/elimine.txt` | 10 | `$table` dans `SHOW COLUMNS FROM $table` |
| `php/ordres/elimine.txt` | 20 | `$table` dans `DELETE FROM $table` |
| `php/ordres/elimine.txt` | 23 | `$table` dans `SELECT * FROM $table` |
| `php/ordres/elimine.txt` | 36 | `$table` dans `DELETE FROM $table` |

---

## Cause racine

`$table` provient de `$_GET['table']` (défini dans `principal.txt:4`) et est utilisé sans validation dans plusieurs requêtes SQL de `elimine.txt` :

```php
// elimine.txt:10 — schéma de n'importe quelle table
$res = mysql($base, "SHOW COLUMNS FROM $table");

// elimine.txt:20 — suppression via identifiant
$query = "DELETE FROM $table WHERE $identifierKey='$identifier' and NUMERO='$commandant'";

// elimine.txt:36 — suppression via index de ligne
mysql_query("DELETE FROM $table WHERE $var_result");
```

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui — n'importe quel compte joueur |
| Complexité | Faible |
| Outils nécessaires | Navigateur web |

---

## Exploitation 1 — Divulgation du schéma (SHOW COLUMNS)

### Requête HTTP

```
GET /ordres/index.php3?table=aa_registre&elimine=1&key=id&identifier=1
Cookie: PHPSESSID=<session_valide>
```

### SQL exécuté

```sql
SHOW COLUMNS FROM aa_registre
```

**Effet :** La liste des colonnes de `aa_registre` (LOGIN, MOT_DE_PASSE, NUMERO, RACE, etc.) est chargée en mémoire et validée. Si une colonne du nom passé en `key` y existe, elle est acceptée. Cela confirme la structure interne de tables qui ne sont normalement pas accessibles via l'interface.

---

## Exploitation 2 — Suppression d'ordres dans des tables arbitraires

Un attaquant peut cibler n'importe quelle table d'ordres — y compris celles d'autres joueurs si la table cible ne filtre pas sur `NUMERO`.

### Requête HTTP — supprimer tous les ordres de construction (path sans identifierKey)

```
GET /ordres/index.php3?table=construire&elimine=0
Cookie: PHPSESSID=<session_valide>
```

### SQL exécuté

```sql
SELECT * FROM construire WHERE NUMERO='<commandant>'
-- puis pour chaque ligne correspondant à elimine=0 :
DELETE FROM construire WHERE NUMERO='<commandant>' AND SYSTEME='...' AND CONSTRUCTION='...' AND ...
```

**Effet :** Suppression de l'ordre à l'index `elimine` dans la table cible. En changeant `$table`, l'attaquant peut cibler toute table qui contient sa colonne `NUMERO`.

---

## Note sur la protection partielle de `$identifierKey`

Le paramètre `$_GET['key']` (→ `$identifierKey`) est validé contre les colonnes retournées par `SHOW COLUMNS FROM $table`. Cette validation est correcte **si `$table` est sûre**. Mais puisque `$table` est elle-même injectable, un attaquant peut :
1. Choisir `$table` = une table arbitraire
2. `SHOW COLUMNS` retourne les colonnes de cette table
3. `$identifierKey` est accepté pour n'importe quelle colonne de cette table
4. Le DELETE cible la table choisie

La protection de `$identifierKey` est donc inefficace tant que `$table` est contrôlable.

---

## Impact

- Divulgation du schéma de toutes les tables de la base de données
- Suppression de ses propres ordres dans des tables arbitraires — **limité aux lignes du commandant courant**
  - Chemin 1 : clause `AND NUMERO='$commandant'` toujours présente (ligne 20)
  - Chemin 2 : `SELECT` filtré sur `NUMERO='$commandant'`, première valeur du `DELETE WHERE` hardcodée à `$commandant` (ligne 28)
  - **Impossible de supprimer des données appartenant à d'autres commandants**
- Contournement de l'interface : suppression d'ordres dans des tables non exposées normalement via l'UI
- Confirmation de l'existence de tables et colonnes pour affiner d'autres attaques

---

## Correctif

La correction est identique à celle de `principal.txt` : appliquer une liste blanche stricte sur `$table` avant tout traitement dans `elimine.txt`.

```php
// Vérifier en tête de elimine.txt que $table est autorisée
if (!in_array($table, $allowed_tables, true)) {
    die("Table non autorisée");
}
```

> `$allowed_tables` doit être définie dans `principal.txt` avant l'inclusion de `elimine.txt`, et transmise au contexte inclus.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'audit de `elimine.txt` |
| 2026-08-28 | Rapport rédigé |
| En attente | Correction |
