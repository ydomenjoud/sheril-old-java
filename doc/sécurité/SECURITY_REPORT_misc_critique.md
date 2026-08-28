# Rapport de vulnérabilité — Vulnérabilités de configuration critique

**Date :** 2026-08-28
**Révisé :** 2026-08-28 — portée et correctifs précisés par vuln (voir [Révision](#révision-2026-08-28))
**Sévérité :** CRITIQUE (vuln 3), CRITIQUE (vuln 2), CRITIQUE — non traitée (vuln 1, voir révision)
**Statut :** Partiellement corrigé (vulns 2 et 3 — voir révision pour le détail)

---

## Résumé

Trois vulnérabilités de configuration indépendantes, toutes de sévérité critique, regroupées dans ce rapport.

---

## Vuln 1 — Mots de passe envoyés en clair par email (`plop.php`)

**Statut : non traitée dans ce correctif, décision produit.**

### Localisation

| Fichier | Ligne | Problème |
|---------|-------|---------|
| `php/plop.php` | ~25, 34 | `$message .= "Mot de passe : $password\n\n"` |

### Description

`php/plop.php` lit le mot de passe en clair depuis la base de données et l'inclut dans le corps d'un email :

```php
$password = $row['MOT_DE_PASSE'];
$message .= "Mot de passe : $password\n\n";
```

Les mots de passe circulent en clair dans les emails et sont stockés dans les boîtes mail des destinataires indéfiniment. C'est une violation directe de l'Art. 32 RGPD.

**Nuance importante** : le fichier commence par `die();` (ligne 2) — le script est actuellement inerte, ce n'est pas une fonctionnalité active exposée sur le site. C'est un outil d'envoi groupé manuel, déclenché ponctuellement en retirant temporairement cette ligne.

### Décision

Le rappel des identifiants par email en clair est un **comportement standard et intentionnel du jeu** (rappel de mot de passe aux joueurs) — pas un bug isolé. Le corriger correctement nécessite de revoir le flux d'authentification (stockage des mots de passe, mécanisme de réinitialisation) et concerne l'ensemble du produit, pas uniquement ce script. **Non traité dans ce correctif** : à planifier séparément avec le porteur du produit, faute de quoi un changement unilatéral casserait un usage établi sans concertation.

### Correctif proposé (pour référence future, non implémenté)

```php
$token = bin2hex(random_bytes(32));
// Stocker hash($token) en base avec expiration
// Envoyer uniquement : "Cliquez ici pour réinitialiser : https://sheril.../reset?token=$token"
```

---

## Vuln 2 — Permissions `0777` sur `php/live/` (`init.sh`)

### Localisation

| Fichier | Ligne | Problème |
|---------|-------|---------|
| `scripts/init.sh` | 3 | `chmod -R 0777 php/live/` |

### Description

Le script d'initialisation applique des permissions `0777` (lecture/écriture/exécution pour tous) sur le répertoire `php/live/`. Depuis le conteneur Apache, n'importe quel processus peut écrire dans ce répertoire. Combiné avec une vulnérabilité d'upload ou d'injection, un attaquant peut déposer un fichier PHP et l'exécuter directement (webshell).

`php/live/` est effectivement écrit à l'exécution (confirmé dans `php/live/read.php` : `mkdir($destDir, ...)`, `fopen($targetPath, 'wb')`), donc un simple retrait de tout accès en écriture casserait la génération des rapports en ligne.

### Correctif

```bash
# AVANT
chmod -R 0777 php/live/

# APRÈS
chmod -R 0770 php/live/
```

Retire l'accès en écriture pour "les autres" tout en conservant l'accès complet au propriétaire et au groupe — voir la note de révision pour le choix de ne pas suivre le `chown www-data` recommandé initialement.

---

## Vuln 3 — Injection SQL côté Java dans `SessionSQL.java`

### Localisation

| Fichier | Ligne | Problème |
|---------|-------|---------|
| `sources/zIgzAg/sql/SessionSQL.java` | `champsTraduction2()`, `champsTraduction3()` | Valeurs concaténées directement dans le SQL, entre quotes, sans échappement |

### Description

Les méthodes `champsTraduction2()` (valeurs d'un `INSERT`) et `champsTraduction3()` (conditions `WHERE`) construisent des fragments SQL par concaténation directe sans échappement :

```java
// SessionSQL.java — champsTraduction3()
retour.append(v[i]);  // v[i] non échappé, concaténé directement dans le SQL
```

3 points d'appel identifiés : `ReceptionOrdres.java` (x2), `ProductionOrdres.java`, `InputSQLWriter.java` — tous via `selectionner()`, qui utilise `champsTraduction3()`.

### Correctif

Échappement des quotes et backslash dans les valeurs avant interpolation (même principe que `mysql_real_escape_string`), sans changer la signature publique des méthodes — voir la note de révision pour le choix par rapport à une migration `PreparedStatement`.

---

## Révision 2026-08-28

Ce rapport regroupait 3 problèmes de nature très différente ; chacun a été traité séparément après analyse de faisabilité :

- **Vuln 1 (mots de passe en clair par email)** : retirée du scope de ce correctif — décision produit, pas de correction unilatérale d'un comportement établi sans concertation (voir section dédiée ci-dessus).

- **Vuln 2 (permissions)** : le correctif initial (`chown www-data:www-data` + `chmod 0755`) a été écarté. `init.sh` s'exécute dans le conteneur `engine` (image Java `eclipse-temurin`), qui n'a très probablement pas d'utilisateur `www-data` — un `chown` par nom y échouerait silencieusement, et un `chmod 0755` sur un répertoire resté root/hors `www-data` casserait l'écriture des rapports par le conteneur `console` (Apache/PHP, qui écrit dans `php/live/` en tant que `www-data`). Correctif retenu : `chmod 0770` seul (retire l'accès "autres", conserve propriétaire+groupe), sans dépendance à une correspondance d'UID entre conteneurs.

- **Vuln 3 (SQL injection Java)** : le correctif initial (migration `PreparedStatement`) a été écarté pour ce correctif. Il changerait la signature publique de `selectionner()`/`insererLigne()`, utilisées à 3 endroits du moteur (`ReceptionOrdres.java`, `ProductionOrdres.java`, `InputSQLWriter.java`), nécessitant recompilation et re-test du moteur de jeu (`sheril.jar`, commande `newRound`) — risque de régression disproportionné pour ce correctif. Retenu : échappement des valeurs dans `champsTraduction2()`/`champsTraduction3()`, qui ferme l'injection sans toucher à l'API publique. La migration `PreparedStatement` reste la solution de fond recommandée, à planifier séparément avec du temps de test du moteur.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découvertes lors des audits PHP et Java |
| 2026-08-28 | Rapport rédigé |
| 2026-08-28 | Analyse de faisabilité par vuln ; vuln 1 écartée (décision produit) ; vulns 2 et 3 recadrées vers des correctifs à moindre risque |
| 2026-08-28 | Correctifs vulns 2 et 3 appliqués (`fix/misc-critical-config`) — vuln 1 non traitée (décision produit) |
