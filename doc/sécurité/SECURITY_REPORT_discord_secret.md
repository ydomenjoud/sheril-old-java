# Rapport de vulnérabilité — Secret OAuth Discord exposé dans le code source

**Date :** 2026-08-28  
**Sévérité :** CRITIQUE  
**Statut :** Non corrigé — **action immédiate requise**

---

## Résumé

Le secret client OAuth de l'application Discord est écrit en clair dans le code source versionné. Toute personne ayant accès au dépôt peut l'utiliser pour usurper l'identité de l'application Discord.

---

## Localisation

| Fichier | Ligne | Donnée exposée |
|---------|-------|----------------|
| `php/auth/callback.php` | 4 | `$client_secret` — secret OAuth Discord |
| `php/auth/callback.php` | 3 | `$client_id` — identifiant de l'application |
| `php/auth/callback.php` | 5 | `$redirect_uri` — URL de production |

---

## Cause racine

```php
// callback.php:3-5
$client_id     = "1408426801662922845";
$client_secret = "p5_42sz1lt47itNhECzt55kL3nF4S__7";
$redirect_uri  = "https://sheril.pbem-france.net/auth/callback.php";
```

Le secret est hardcodé directement dans le fichier PHP, committés dans le dépôt Git. L'historique Git conserve cette valeur même si le fichier est modifié ultérieurement.

---

## Impact

- **Usurpation de l'application Discord** : un attaquant peut s'authentifier en tant qu'application et accéder aux webhooks, modifier les paramètres de l'application
- **Compromission des comptes Discord liés** : les tokens OAuth émis via cet identifiant peuvent être révoqués ou utilisés
- **Persistance dans l'historique Git** : même après correction, la valeur reste dans `git log`

---

## Actions requises

### Immédiat — Révoquer le secret

1. Se connecter sur https://discord.com/developers/applications
2. Sélectionner l'application correspondante
3. Onglet **OAuth2** → **Reset Secret**
4. Générer un nouveau secret

### Court terme — Externaliser la configuration

```php
// AVANT — vulnérable
$client_secret = "p5_42sz1lt47itNhECzt55kL3nF4S__7";

// APRÈS — sécurisé
$client_secret = $_ENV['DISCORD_CLIENT_SECRET'] ?? getenv('DISCORD_CLIENT_SECRET');
if (!$client_secret) die("Configuration manquante");
```

Stocker la valeur dans `php/secure/connect.txt` (exclu du dépôt) ou dans une variable d'environnement Docker.

### Nettoyer l'historique Git

```bash
git filter-branch --force --index-filter \
  'git rm --cached --ignore-unmatch php/auth/callback.php' \
  --prune-empty --tag-name-filter cat -- --all
git push origin --force --all
```

> Attention : `filter-branch` réécrit l'historique — coordonner avec tous les contributeurs.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'audit de `callback.php` |
| 2026-08-28 | Rapport rédigé |
| **Immédiat** | Révoquer le secret sur Discord Developer Portal |
| En attente | Externalisation de la configuration |
