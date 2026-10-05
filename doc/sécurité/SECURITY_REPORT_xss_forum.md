# Rapport de vulnérabilité — XSS Stockée dans le forum

**Date :** 2026-08-28
**Révisé :** 2026-08-28 — exploitation confirmée manuellement en conditions réelles
**Sévérité :** CRITIQUE
**Statut :** Corrigé

---

## Résumé

Le contenu des posts du forum est affiché sans sanitization HTML. Un attaquant authentifié peut poster un message contenant du JavaScript malveillant qui s'exécute dans le navigateur de tous les lecteurs du topic.

---

## Localisation

| Fichier | Ligne | Variable vulnérable |
|---------|-------|---------------------|
| `php/forum/functions.php` | 12-14 | `$text` retourné brut si HTML détecté |
| `php/forum/view_topic.php` | 52, 69 | `echo render_post_body(...)` |
| `php/forum/view_topic.php` | 119 | `bodyInput.value = quill.root.innerHTML` |

---

## Cause racine

### Côté affichage — `functions.php:12-14`

```php
function render_post_body($text) {
    if (strpos($text, '<') !== false && strpos($text, '>') !== false) {
        return $text;  // HTML retourné brut sans aucun échappement
    }
    // ...
}
```

Tout message contenant `<` et `>` est retourné tel quel dans la page.

### Côté soumission — `view_topic.php:119`

```javascript
form.onsubmit = function() {
    bodyInput.value = quill.root.innerHTML;  // HTML brut envoyé au serveur
};
```

Le contenu Quill (HTML riche) est soumis sans sanitization.

---

## Conditions d'exploitation

| Condition | Valeur |
|-----------|--------|
| Authentification | Oui — n'importe quel compte joueur |
| Complexité | Très faible |
| Outils nécessaires | Navigateur web (éditeur Quill intégré, ou soumission directe du formulaire) |

---

## Proof of Concept confirmé manuellement

### Payload utilisé

```html
<img src=x onerror="document.title='XSS_OK'; console.log('XSS EXÉCUTÉE', document.cookie)">
```

### Méthode de confirmation

1. Environnement local sans forum existant (`_category`/`_forum` vides par défaut) — un forum de test a dû être créé manuellement via SQL pour disposer d'un `id_forum` valide
2. Publication du post via le formulaire réel de `post.php` (payload injecté dans le champ caché `body`, contournant l'éditeur Quill — reproduit fidèlement ce qu'un attaquant scriptant sa requête ferait)
3. Ouverture du topic généré (`view_topic.php?id=...`)

### Résultat observé

L'onglet du navigateur s'est renommé en **`XSS_OK`**, confirmant l'exécution du JavaScript injecté pour tout visiteur du topic — exploitation confirmée en conditions réelles.

> **Effet de bord découvert pendant le test, sans lien avec cette faille :** `post.php` fait `require_once '../includes/top.php'` qui émet du HTML avant que `post.php` n'appelle `header("Location: ...")` en fin de traitement. La redirection échoue systématiquement (`Warning: Cannot modify header information - headers already sent`), pour tout post créé ou édité, indépendamment de tout payload malveillant. L'insertion en base a lieu avant cet appel et n'est donc pas affectée — mais l'UX de post/édition est cassée. **Hors scope de ce rapport**, à traiter séparément.

---

## Impact

- **Vol de sessions** : récupération du `PHPSESSID` de tous les lecteurs
- **Usurpation de compte** : connexion sous l'identité des victimes
- **Passage d'ordres** : ordres de jeu passés au nom des joueurs compromis
- **Persistance** : le payload reste en base de données et affecte tous les visiteurs futurs

---

## Correctif

### Option retenue — liste blanche de balises + suppression des attributs dangereux

Préserve l'essentiel du rendu Quill (gras, liens, images, citations, listes), sans nouvelle dépendance :

```php
function render_post_body($text) {
    if (strpos($text, '<') !== false && strpos($text, '>') !== false) {
        $allowed_tags = '<p><br><strong><em><u><b><i><a><img><blockquote><span><ul><ol><li><h1><h2><h3>';
        $clean = strip_tags($text, $allowed_tags);
        // Supprime tout attribut on*="..." (gestionnaires d'évènements JS)
        $clean = preg_replace('/\s+on\w+\s*=\s*("[^"]*"|\'[^\']*\'|[^\s>]+)/i', '', $clean);
        // Neutralise les URLs javascript: dans href/src
        $clean = preg_replace('/(href|src)(\s*=\s*)("|\')\s*javascript:[^"\']*\3/i', '$1$2$3#$3', $clean);
        return $clean;
    }
    // ... (BBCode inchangé)
}
```

### Options écartées

**HTMLPurifier** (recommandation initiale, la plus robuste) — écartée : ajouterait une vraie dépendance externe (~200 Ko) à un projet qui n'a actuellement aucune gestion de dépendances PHP (pas de Composer/`vendor/`). Resterait la meilleure option si le projet adopte Composer à l'avenir.

**Échappement complet (`htmlspecialchars` + `nl2br`)** — écartée : la plus sûre et la plus simple, mais casse entièrement la mise en forme Quill (posts existants et futurs affichés en texte brut avec balises visibles). Rejetée pour préserver l'UX du forum ; à reconsidérer si la liste blanche s'avère insuffisante en pratique.

---

## Timeline

| Date | Événement |
|------|-----------|
| 2026-08-28 | Découverte lors de l'analyse de `functions.php` |
| 2026-08-28 | Rapport rédigé |
| 2026-08-28 | Exploitation confirmée manuellement (`XSS_OK` déclenché) ; effet de bord non lié découvert (`headers already sent` sur `post.php`, hors scope) |
| 2026-08-28 | Correctif appliqué (`fix/forum-stored-xss`) |
