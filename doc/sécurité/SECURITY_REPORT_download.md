# Rapport de vulnérabilité — Divulgation de chemin serveur dans `download.php`

**Date :** 2026-08-28
**Révisé :** 2026-08-28 — portée réduite après confirmation manuelle (voir [Révision](#révision-2026-08-28))
**Sévérité :** FAIBLE (révisée à la baisse — voir Révision)
**Statut :** Corrigé

---

## Résumé

`download.php` expose le chemin absolu du serveur dans son message d'erreur lorsqu'un rapport de tour demandé n'existe pas sur le disque. C'est une divulgation d'information mineure : aucun contournement d'authentification, aucun accès aux données d'un autre joueur.

---

## Localisation

| Fichier | Ligne | Problème |
|---------|-------|---------|
| `php/auth/download.php` | 20 | Message d'erreur exposant le chemin complet |

---

## Cause racine

```php
// download.php — VULNÉRABLE
if (!file_exists($file)) {
    exit("Fichier introuvable " . $file);  // expose /var/www/html/../rapports/...
}
```

Le chemin absolu complet (`$file = __DIR__ . "/../rapports/${tour}/${num}tour${tour}.zip"`) est concaténé dans le message d'erreur renvoyé au client.

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui |
| Complexité | Faible |
| Outils nécessaires | Navigateur web |

---

## Proof of Concept confirmé

Requête pour un tour valide (dans la plage `1..currentTour`) mais dont le rapport n'existe pas sur disque pour le commandant courant :

```
GET /auth/download.php?turn=<tour_sans_rapport_pour_ce_commandant>
Cookie: PHPSESSID=<session_valide>
```

**Réponse observée (avant correctif) :**
```
Fichier introuvable /var/www/html/auth/../rapports/<tour>/<num>tour<tour>.zip
```

Confirmé manuellement en déplaçant temporairement un fichier `php/rapports/<tour>/<num>tour<tour>.zip` pour simuler l'absence de rapport.

---

## Impact

- **Divulgation d'information** : chemin absolu du serveur (`/var/www/html/...`) révélé dans le message d'erreur
- Impact limité : ne permet ni contournement d'authentification, ni accès aux rapports d'un autre joueur (le numéro de commandant provient de `$_SESSION`, non falsifiable côté client sans compromission de session)
- Utile principalement pour de la reconnaissance (confirmation de la structure de déploiement du serveur)

---

## Correctif

```php
// AVANT — vulnérable
if (!file_exists($file)) {
    exit("Fichier introuvable " . $file);
}

// APRÈS — corrigé
if (!file_exists($file)) {
    http_response_code(404);
    exit("Fichier non trouvé");  // pas de chemin dans le message
}
```

---

## Révision 2026-08-28

Le rapport initial mentionnait un second problème, un opérateur `&` (bitwise) utilisé à la place de `&&` (logique) ligne 12 :

```php
$tour = ($givenTurn > 0 & $givenTurn <= $currentTurn) ? $givenTurn : $currentTurn;
```

**Analyse à tête reposée :** les deux opérandes de `&` sont déjà des booléens (résultats de `>` et `<=`), donc `0` ou `1`. Pour deux valeurs dans `{0, 1}`, le ET bit-à-bit (`&`) et le ET logique (`&&`) produisent exactement le même résultat — la seule différence entre les deux opérateurs est l'évaluation court-circuit de `&&`, sans effet ici puisque `$givenTurn <= $currentTurn` n'a pas d'effet de bord. **Ce n'est donc pas un bug exploitable** : aucun contournement de la validation du numéro de tour n'a pu être démontré. Le titre du rapport ("IDOR") a également été retiré : aucun accès aux données d'un autre joueur n'était possible, le rapport initial le notait déjà explicitement dans sa section Impact.

La sévérité est révisée de HAUTE à FAIBLE en conséquence. Le correctif ne porte donc que sur la divulgation de chemin ; l'opérateur `&`/`&&` n'a pas été modifié (remplacé uniquement par souci de lisibilité, sans lien avec une faille de sécurité).

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'audit de `download.php` |
| 2026-08-28 | Rapport rédigé (portée initiale surestimée) |
| 2026-08-28 | Confirmation manuelle de la divulgation de chemin ; ré-analyse du bug `&`/`&&` (non exploitable) ; sévérité révisée HAUTE → FAIBLE |
| 2026-08-28 | Correctif appliqué (`fix/download-path-disclosure`) |
