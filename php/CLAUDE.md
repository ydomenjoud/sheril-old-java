# CLAUDE.md — site PHP de Sheril

Site web du jeu Sheril (4X spatial au tour par tour), partie **Corylis**.
Servi par le conteneur `console` (`../docker-compose.yml`, PHP 5.6 + Apache, http://localhost:666),
base MariaDB dans le conteneur `db`.

## Fonctionnement du jeu

- Jeu au **tour par tour**, sans temps réel. La résolution du tour a lieu chaque **mercredi à 20h**.
- **Rejoindre la partie** : le joueur s'inscrit sur le site. À la résolution suivante, son commandant est créé
  et il reçoit un e-mail avec ses informations de connexion à la console d'ordres.
  L'inscription est une étape unique, elle ne fait pas partie du cycle d'un tour.
- **Cycle d'un tour** : après chaque résolution, le joueur reçoit son rapport par e-mail, puis passe ses ordres
  dans la console d'ordres pendant la semaine ; les ordres sont résolus le mercredi à 20h.
- Section « Jouer » (`/play`) : page d'accueil avec des tuiles vers quatre sous-parties :
  registre (`/play/listing`), inscription (`/play/register`), console d'ordres (`/play/console`),
  outil d'aide (`/play/tool`).

## Architecture : routeur + templates

Le site est en cours de migration. Les nouvelles pages ne sont plus des fichiers `.php`
autonomes : **`index.php` est le point d'entrée unique** et rend des **templates** du dossier `templates/`.

```
.htaccess          → toute URL qui n'est ni un fichier ni un dossier existant est envoyée à index.php
index.php          → déclare les routes (chemin → template + données) puis $app->run()
includes/mini.php  → classe Mini : micro-routeur + moteur de templates façon Twig (sans dépendance)
includes/data.php  → classe Data : accès BDD (PDO) et préparation des données des pages
templates/         → templates *.twig
rules/*.md         → chapitres des règles, convertis en HTML via include_markdown() (Parsedown)
assets/            → CSS (v2), JS, images
```

Les anciens scripts (`races.php`, `stats.php`, `liste.php`, `forum/`, `ordres/`…) restent
accessibles directement : un fichier existant est servi tel quel par Apache et ne passe pas par le routeur.

### Déclarer une route (`index.php`)

```php
// Page statique
$app->get('/lore/histoire', 'pages/lore/histoire', ['title' => 'Histoire']);

// Paramètre d'URL + données calculées : la closure reçoit les paramètres et renvoie les variables du template
$app->get('/rule/{page}', 'pages/rule/page', function ($p) {
    if (!isset($chapters[$p['page']])) Mini::abort(404);
    return ['title' => …, 'content' => include_markdown(…)];
});

// Sans template : une closure qui renvoie un tableau produit du JSON, une string est affichée telle quelle
$app->get('/api/xxx', null, function () { return [...]; });
```

- Méthodes : `get`, `post`, `any` (GET+POST), ou `route('GET|POST', …)`.
- Paramètres : `{slug}` ou avec regex `{id:\d+}`. Le slash final est optionnel.
- Le nom de template est relatif à `templates/`, l'extension `.twig` est ajoutée automatiquement.
- `Mini::abort(404)` / `Mini::redirect($url)`. En cas d'erreur, le routeur cherche `templates/404.twig`
  puis `templates/error.twig` (pas encore créés → message texte brut).
- Lien actif des navigations : variables globales `chemin` (chemin courant, sans slash final, "/" pour l'accueil)
  et `rubrique` (1er segment : `lore`, `rule`, `play`, `forum`…). Menu principal : `aria-current="true"` si
  `rubrique` correspond ; sous-menus : `aria-current="page"` si `chemin` correspond. Le CSS cible `[aria-current]`.
- Variables globales à tous les templates : `$app->globals` (`gameName`, `site.tourNumber`,
  `site.tourLastDate`) + `base` (préfixe d'URL, ajouté par le routeur).
- La logique métier / SQL va dans `Data` (`includes/data.php`), pas dans les templates ni dans `index.php`.
  `Data::$pdo` est initialisé dans `index.php` (connexion dans `secure/connect.txt`).
  Le numéro de tour est lu dans `tour.txt`.

### Templates (`templates/`)

```
layout.twig                 → gabarit HTML global (head, header, nav principale, footer, sprite d'icônes SVG)
layout_embed.twig           → gabarit minimal (head + CSS, sans en-tête) des pages affichées dans une iframe du site
pages/home.twig             → accueil
pages/lore/_layout.twig     → sous-gabarit du lore (sous-navigation) : bloc `lore`
pages/lore/*.twig           → pages du lore (présentation, histoire, une page par race)
pages/rule/page.twig        → un seul template pour tous les chapitres des règles
pages/play/_layout.twig     → sous-gabarit de la section « Jouer » (sans sous-navigation) : bloc `play`
pages/play/index.twig       → accueil de « Jouer » : tuiles vers les sous-parties + explication du tour
pages/play/listing.twig     → registre des commandants (Data::getRegistre)
pages/play/register.twig    → inscription : formulaire (Data::inscrire → aa_inscription) + inscriptions en attente
pages/play/console.twig     → console d'ordres : iframe pleine hauteur (.page-frame) vers /ordres/ (ancienne console)
pages/forum/_layout.twig    → sous-gabarit du forum : fil d'Ariane (main > nav, bloc `ariane`) + bloc `forum`
pages/forum/index.twig      → catégories et forums ; forum.twig → sujets d'un forum ; topic.twig → messages + réponse
pages/forum/edit.twig       → nouveau sujet / modification d'un message ; _editor.twig → éditeur Quill (CDN jsdelivr)
pages/stats/index.twig      → /statistiques : liens (Data::getStatsLiens) + iframe .page-frame, repris de stats.php
                              (pages générées par le moteur dans stats/*.htm, archives stats/statsT<n>.zip)
pages/stats/general.twig    → /statistiques/general : classement général triable (Data::getStatsGeneral), dans l'iframe
pages/stats/detail.twig     → /statistiques/detail?nums=3,,7 : progression comparée (Data::getStatsDetail) avec Chart.js
                              (assets/js/stats-detail.js) ; chaque position de nums = une couleur --stats-serie-N,
                              un commandant retiré laisse sa place vide pour que les autres gardent leur couleur
pages/archives.twig         → /archives et /archives/{dossier} : sous-menu d'un lien par dossier de archive/
                              (Data::getArchives, plus récent d'abord) + iframe .page-frame vers /archive/<dossier>/
pages/compte.twig           → /compte (connecté) : fiche du commandant, statistiques, liens, déconnexion
pages/connexion.twig        → connexion avec les identifiants de la console d'ordres
pages/play/tool.twig        → outil d'aide : iframe pleine hauteur (.page-frame) vers https://ydomenjoud.github.io/test-interface-sheril/
```

Conventions :
- Une page étend soit `layout.twig` (bloc `content`), soit le `_layout.twig` de sa section.
  Les fichiers préfixés `_` sont des gabarits/partiels, pas des pages routées.
- Le `<main>` de chaque section porte un id (`#lore`, `#rule`, `#play`…) utilisé par la feuille `screen/_<section>.scss`.

Syntaxe supportée par `Mini` (sous-ensemble de Twig, **pas Twig complet**) :
- `{{ var }}`, `{{ user.name }}`, `{{ tab[loop.index0] }}` — échappé en HTML par défaut, `|raw` pour ne pas échapper.
- `{% if %}…{% elseif %}…{% else %}…{% endif %}`, `{% for v in items %}` / `{% for k, v in items %}`
  avec `loop.index`, `loop.index0`, `loop.first`, `loop.last`, `loop.length`, `loop.parent`.
- `{% set x = expr %}`, `{% include 'partials/foo' %}`, `{% extends 'layout' %}` + `{% block nom %}…{% endblock %}`,
  `{# commentaire #}`.
- Opérateurs : `and`, `or`, `not`, `~` (concaténation), comparaisons PHP.
- Filtres : `e`, `raw`, `upper`, `lower`, `trim`, `nl2br`, `length`, `join`, `default`, `json`, `date`.
  Ajout possible via `$app->addFilter('nom', callable)`.
- Pas d'appel de fonction ni de méthode dans les templates : tout doit être préparé côté PHP.
- Variable inexistante → `null`, sans erreur.

Les templates sont compilés en PHP et mis en cache dans `sys_get_temp_dir()/mini_<hash>/`
(recompilés automatiquement si le `.twig` est plus récent).

### Ajouter une page

1. Créer `templates/pages/<section>/<page>.twig` qui étend `layout.twig` ou le `_layout.twig` de la section.
2. Ajouter la route dans `index.php` (avec `title` au minimum).
3. Si besoin de données, ajouter une méthode statique dans `Data` et l'appeler depuis la route.
4. Ajouter le lien dans la nav concernée (`layout.twig` ou `_layout.twig` de la section).

### Session, connexion et formulaires

- `index.php` démarre la session ; le commandant connecté est dans `$_SESSION['commandant_num']` (même session
  que l'ancienne console d'ordres). Variables globales des templates : `user` (`numero`, `nom`, ou null) et `csrf`.
- En-tête connecté (`layout.twig`) : boutons « Télécharger le rapport » (/rapport) et « Passer ses ordres » (/play/console) et avatar de race dans le coin
  haut droit (`body > header > a`, lien vers `/compte`) ; la déconnexion est sur `/compte`.
  `user` contient aussi `race` et `avatar` (race mise en session à la connexion).
- `/rapport` et `/rapport/{tour}` (connecté) : téléchargement du zip `rapports/<tour>/<numéro>tour<tour>.zip`
  (`Data::rapportFichier`, dernier tour par défaut) ; remplace `auth/download.php`.
- `/connexion` (identifiants `LOGIN` / `MOT_DE_PASSE` de `aa_registre`, paramètre `retour` = chemin local),
  `/deconnexion` en POST.
- Tout formulaire POST d'un utilisateur connecté contient `<input type="hidden" name="csrf" value="{{ csrf }}">`
  et la route appelle `Data::checkCsrf()`. `exiger_connexion()` (index.php) redirige vers `/connexion` si besoin.

### Forum

- Routes : `/forum`, `/forum/{id}`, `/forum/{id}/new`, `/forum/topic/{id}` (GET + POST réponse),
  `/forum/post/{id}/edit` (auteur uniquement). Logique dans `Data::forum*()`.
- Tables `_category` → `_forum` → `_post`. Un sujet est un `_post` sans `id_parent` (NULL ou 0) ; les réponses
  ont `id_parent` = id du sujet. `id_author` = `aa_registre.NUMERO`.
- Le corps des messages est du HTML Quill (ou du BBCode pour les anciens) stocké brut et **nettoyé à l'affichage**
  par `Data::forumCorps()` : toujours passer par cette fonction avant un `|raw`.
- Dans un sujet, chaque message a le profil de l'auteur à gauche (`Data::forumProfils`) : avatar de race
  (`assets/img/avatar/<race>.png`, `Data::$avatars`), statistiques du dernier tour (puissance, planètes,
  population, PV) et nombre de messages ; le contenu est à droite.
- L'ancien forum (`forum/*.php`) reste en place ; `.htaccess` envoie `/forum` et `/forum/` au routeur.

## CSS

Source SCSS dans `assets/css/v2/`, compilée en `assets/css/v2/sheril.css` (le CSS compilé est versionné).
Trois couches : `abstracts/` (palette → tokens → mixins), `base/`, `components/`, plus `screen/` pour
le style propre à chaque section. Les composants n'utilisent que les tokens (`--sheril-*`), jamais la palette.
Toute nouvelle feuille `screen/_x.scss` doit être ajoutée dans `sheril.scss`.

**Préférer le HTML sémantique aux classes.** Éviter de multiplier les classes CSS : cibler d'abord les balises
sémantiques (`header`, `nav`, `aside`, `article`, `figure`, `dl`, `time`…), les attributs (`[aria-current]`,
`[hidden]`, `[data-*]`) et la structure (`#section > aside`, `:has()`), dans le périmètre de l'id de la section.
Une classe n'est ajoutée que pour un composant réutilisable (`.card`, `.button`, `.badge`…) ou quand aucune balise
ni structure ne suffit.
Formulaires : composant `components/_form.scss` (`<form class="form">` + `<label class="field">`),
retours via `.banner--positive` / `.banner--negative`.
Thèmes : `themes/<nom>.scss` → `themes/<nom>.css` (compilé à part, ne redéfinit que des `--sheril-*`),
chargé après `sheril.css` pour le commandant connecté dont `aa_configuration.theme` (table `aa_configuration`, clé `NUMERO`) le désigne. Choix par `/theme/<nom>` (connexion requise)
(`/theme/defaut` pour revenir), sans lien dans l'interface pour l'instant ; logique dans `Data::theme()` / `Data::setTheme()`.

## Contraintes

- **PHP 5.6** en production : pas de `??` (utiliser `isset($x) ? $x : null`), pas de `<=>`, pas de types
  scalaires ni de types de retour dans les signatures, pas de classes anonymes ni de `?->`.
  Autorisés : tableaux courts `[]`, closures, `...$args`, `const` avec expressions.
- Pas de Composer / vendor : bibliothèques incluses à la main dans `includes/`.
- Code, commentaires et contenus en français.
