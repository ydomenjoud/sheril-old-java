<?php
/**
 * mini.php — micro-routeur + moteur de templates, en un seul fichier.
 * Pas d'autoload, pas de vendor. PHP 5.6+.
 *
 * Syntaxe des templates :
 *   {{ var }}   {{ user.name }}   {{ title|upper }}   {{ html|raw }}
 *   {% if cond %} … {% elseif cond %} … {% else %} … {% endif %}
 *   {% for item in items %} … {% endfor %}     {% for k, v in items %}
 *       dans la boucle : loop.index (1,2,3…) loop.index0 (0,1,2…) loop.first loop.last loop.length loop.parent
 *   {% set x = expr %}
 *   {% include 'partials/foo' %}
 *   {% extends 'layout' %}  +  {% block nom %} … {% endblock %}
 *   {# commentaire #}
 *
 * Les variables inexistantes valent null (pas d'erreur). Opérateurs : and, or, not, ~ (concaténation).
 * Filtres : e, raw, upper, lower, trim, nl2br, length, join, default, json, date.
 */
class Mini
{
    public $globals = [];          // variables disponibles dans tous les templates
    public $ext     = '.twig';     // extension ajoutée automatiquement : 'home' -> 'home.twig'

    private $routes  = [];
    private $filters = [];
    private $dir;
    private $cache;
    private $base    = '';
    private $layout  = null;
    private $blocks  = [];
    private $stack   = [];
    private $depth   = 0;          // imbrication des {% for %} pendant la compilation

    public function __construct($templateDir, $cacheDir = null)
    {
        $this->dir   = rtrim($templateDir, '/\\');
        $this->cache = $cacheDir ?: sys_get_temp_dir() . '/mini_' . md5($this->dir);

        $this->filters = [
            'e'       => [$this, 'e'],
            'upper'   => 'mb_strtoupper',
            'lower'   => 'mb_strtolower',
            'trim'    => 'trim',
            'nl2br'   => 'nl2br',
            'length'  => function ($v) { return is_array($v) ? count($v) : mb_strlen((string) $v); },
            'join'    => function ($v, $sep = ', ') { return implode($sep, (array) $v); },
            'default' => function ($v, $d = '') { return ($v === null || $v === '' || $v === false) ? $d : $v; },
            'json'    => function ($v) { return json_encode($v, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES); },
            'date'    => function ($v, $f = 'd/m/Y') { return date($f, is_numeric($v) ? (int) $v : strtotime($v)); },
        ];
    }

    /* ------------------------------------------------------------------ *
     *  Routeur
     * ------------------------------------------------------------------ */

    public function get($path, $tpl = null, $data = [])  { return $this->route('GET', $path, $tpl, $data); }
    public function post($path, $tpl = null, $data = []) { return $this->route('POST', $path, $tpl, $data); }
    public function any($path, $tpl = null, $data = [])  { return $this->route('GET|POST', $path, $tpl, $data); }

    /**
     * $path : '/articles/{slug}' ou '/user/{id:\d+}' (regex personnalisée)
     * $tpl  : nom du template, ou null pour une réponse brute (string) / JSON (array)
     * $data : tableau, ou closure function(array $params): array|string
     */
    public function route($methods, $path, $tpl = null, $data = [])
    {
        $re = '';
        foreach (preg_split('/(\{\w+(?::[^}]+)?\})/', $path, -1, PREG_SPLIT_DELIM_CAPTURE) as $i => $seg) {
            if ($i % 2) {
                preg_match('/\{(\w+)(?::(.+))?\}/', $seg, $m);
                $re .= '(?P<' . $m[1] . '>' . (isset($m[2]) ? $m[2] : '[^/]+') . ')';
            } else {
                $re .= preg_quote($seg, '#');
            }
        }
        $this->routes[] = [
            'methods' => explode('|', strtoupper($methods)),
            're'      => '#^' . rtrim($re, '/') . '/?$#',
            'tpl'     => $tpl,
            'data'    => $data,
        ];
        return $this;
    }

    public function addFilter($name, callable $fn)
    {
        $this->filters[$name] = $fn;
        return $this;
    }

    public static function abort($code = 404, $message = '')
    {
        throw new RuntimeException($message, $code);
    }

    public static function redirect($url, $code = 302)
    {
        header('Location: ' . $url, true, $code);
        exit;
    }

    public function run($uri = null, $method = null)
    {
        $method = strtoupper($method ?: (isset($_SERVER['REQUEST_METHOD']) ? $_SERVER['REQUEST_METHOD'] : 'GET'));
        $uri    = $uri !== null ? $uri : $_SERVER['REQUEST_URI'];
        $path   = rawurldecode((string) parse_url($uri, PHP_URL_PATH));

        // Retire le préfixe du script (/index.php) ou du sous-dossier (/monsite)
        $script = isset($_SERVER['SCRIPT_NAME']) ? $_SERVER['SCRIPT_NAME'] : '/index.php';
        $dir    = rtrim(str_replace('\\', '/', dirname($script)), '/');
        foreach ([$script, $dir] as $prefix) {
            if ($prefix !== '' && preg_match('#^' . preg_quote($prefix, '#') . '(/.*)?$#', $path, $m)) {
                $path = isset($m[1]) ? $m[1] : '';
                $this->base = $prefix;
                break;
            }
        }
        $path = '/' . trim($path, '/');
        $this->globals['base'] = $this->base;

        try {
            $allowed = false;
            foreach ($this->routes as $r) {
                if (!preg_match($r['re'], $path, $m)) continue;
                if (!in_array($method, $r['methods'], true)) { $allowed = true; continue; }
                $params = array_filter($m, 'is_string', ARRAY_FILTER_USE_KEY);
                return $this->dispatch($r, $params);
            }
            self::abort($allowed ? 405 : 404);
        } catch (RuntimeException $e) {
            $code = $e->getCode();
            if ($code < 400 || $code > 599) throw $e;
            $this->fail($code, $e->getMessage());
        }
    }

    private function dispatch(array $r, array $params)
    {
        $data = $r['data'];
        if ($data instanceof Closure) $data = $data($params);

        if ($r['tpl'] === null) {
            if (is_array($data)) {
                header('Content-Type: application/json; charset=utf-8');
                echo json_encode($data, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
            } else {
                echo $data;
            }
            return;
        }
        header('Content-Type: text/html; charset=utf-8');
        echo $this->render($r['tpl'], array_merge($params, (array) $data));
    }

    private function fail($code, $message = '')
    {
        http_response_code($code);
        header('Content-Type: text/html; charset=utf-8');
        foreach ([(string) $code, 'error'] as $t) {                 // 404.twig, puis error.twig
            if (is_file($this->dir . '/' . $this->file($t))) {
                echo $this->render($t, ['code' => $code, 'message' => $message]);
                return;
            }
        }
        echo 'Erreur ' . $code . ($message !== '' ? ' — ' . htmlspecialchars($message) : '');
    }

    /* ------------------------------------------------------------------ *
     *  Rendu
     * ------------------------------------------------------------------ */

    public function render($tpl, array $vars = [])
    {
        $vars = array_merge($this->globals, $vars);
        $this->blocks = [];
        $this->layout = null;

        $out = $this->renderFile($tpl, $vars);
        while ($this->layout !== null) {           // {% extends %}
            $layout = $this->layout;
            $this->layout = null;
            $out = $this->renderFile($layout, $vars);
        }
        return $out;
    }

    public function e($v)
    {
        return htmlspecialchars((string) $v, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
    }

    private function f($name, $value, ...$args)
    {
        if (!isset($this->filters[$name])) throw new RuntimeException('Filtre inconnu : ' . $name);
        return call_user_func($this->filters[$name], $value, ...$args);
    }

    private function partial($name, array $vars)
    {
        return $this->renderFile($name, $vars);
    }

    private function beginBlock($name)
    {
        $this->stack[] = $name;
        ob_start();
    }

    private function endBlock()
    {
        $name    = array_pop($this->stack);
        $content = ob_get_clean();
        if (!isset($this->blocks[$name])) $this->blocks[$name] = $content;  // l'enfant passe en premier
        echo $this->blocks[$name];
    }

    private function renderFile($name, array $vars)
    {
        $file  = $this->compile($name);
        $level = ob_get_level();
        ob_start();
        try {
            // Closure créée dans la classe : $this et l'accès aux méthodes privées sont déjà liés
            $fn = function ($__file, $__vars) { extract($__vars, EXTR_SKIP); include $__file; };
            $fn($file, $vars);
        } catch (Exception $e) {
            while (ob_get_level() > $level) ob_end_clean();
            throw $e;
        }
        return ob_get_clean();
    }

    /* ------------------------------------------------------------------ *
     *  Compilation template -> PHP (mise en cache)
     * ------------------------------------------------------------------ */

    /** 'home' -> 'home.twig' (un nom déjà suffixé est laissé tel quel) */
    private function file($name)
    {
        $n = strlen($this->ext);
        return substr($name, -$n) === $this->ext ? $name : $name . $this->ext;
    }

    private function compile($name)
    {
        $src = $this->dir . '/' . $this->file($name);
        if (!is_file($src)) throw new RuntimeException('Template introuvable : ' . $name);

        $dst = $this->cache . '/' . md5($src) . '.php';
        if (!is_file($dst) || filemtime($dst) < filemtime($src)) {
            if (!is_dir($this->cache)) @mkdir($this->cache, 0775, true);
            file_put_contents($dst, $this->transpile(file_get_contents($src)), LOCK_EX);
        }
        return $dst;
    }

    private function transpile($s)
    {
        $this->depth = 0;
        $s = preg_replace('/\{#.*?#\}/s', '', $s);
        $s = str_replace('<?', '<?php echo "<?"; ?>', $s);   // un "<?" littéral ne doit pas être exécuté

        return preg_replace_callback('/\{\{(.+?)\}\}|\{%\s*(.+?)\s*%\}/s', function ($m) {
            return isset($m[2]) && $m[2] !== '' ? $this->tag($m[2]) : $this->output($m[1]);
        }, $s);
    }

    private function output($src)
    {
        $parts = preg_split('/(?<!\|)\|(?!\|)/', trim($src));   // "a|b", mais pas "a||b"
        $code  = $this->expr(array_shift($parts));
        $raw   = false;

        foreach ($parts as $f) {
            $f = trim($f);
            if ($f === 'raw') { $raw = true; continue; }
            if (!preg_match('/^(\w+)(?:\((.*)\))?$/s', $f, $m)) throw new RuntimeException('Filtre invalide : ' . $f);
            $args = isset($m[2]) && $m[2] !== '' ? ', ' . $this->expr($m[2]) : '';
            $code = '$this->f(\'' . $m[1] . '\', ' . $code . $args . ')';
        }
        return $raw ? '<?= ' . $code . ' ?>' : '<?= $this->e(' . $code . ') ?>';
    }

    private function tag($t)
    {
        $p    = preg_split('/\s+/', $t, 2);
        $kw   = $p[0];
        $rest = isset($p[1]) ? $p[1] : '';

        switch ($kw) {
            case 'if':       return '<?php if (' . $this->expr($rest) . '): ?>';
            case 'elseif':   return '<?php elseif (' . $this->expr($rest) . '): ?>';
            case 'else':     return '<?php else: ?>';
            case 'endif':    return '<?php endif; ?>';
            case 'endfor':   return '<?php endforeach; $loop = $__p' . $this->depth-- . '; ?>';
            case 'block':    return '<?php $this->beginBlock(\'' . trim($rest) . '\'); ?>';
            case 'endblock': return '<?php $this->endBlock(); ?>';
            case 'extends':  return '<?php $this->layout = ' . $this->expr($rest) . '; ?>';
            case 'include':  return '<?= $this->partial(' . $this->expr($rest) . ', get_defined_vars()) ?>';
            case 'set':
                if (!preg_match('/^(\w+)\s*=\s*(.+)$/s', $rest, $m)) break;
                return '<?php $' . $m[1] . ' = ' . $this->expr($m[2]) . '; ?>';
            case 'for':
                if (!preg_match('/^(?:(\w+)\s*,\s*)?(\w+)\s+in\s+(.+)$/s', $rest, $m)) break;
                $k = $m[1] !== '' ? '$' . $m[1] . ' => ' : '';
                $d = ++$this->depth;   // suffixe unique par boucle imbriquée : $__s1, $__s2…
                return '<?php $__s' . $d . ' = ' . $this->expr($m[3]) . ';'
                    . ' if (!is_array($__s' . $d . ')) $__s' . $d . ' = $__s' . $d . ' instanceof Traversable ? iterator_to_array($__s' . $d . ') : [];'
                    . ' $__p' . $d . ' = isset($loop) ? $loop : null; $__n' . $d . ' = 0; $__t' . $d . ' = count($__s' . $d . ');'
                    . ' foreach ($__s' . $d . ' as ' . $k . '$' . $m[2] . '): $__n' . $d . '++;'
                    . ' $loop = array(\'index\' => $__n' . $d . ', \'index0\' => $__n' . $d . ' - 1, \'first\' => $__n' . $d . ' === 1,'
                    . ' \'last\' => $__n' . $d . ' === $__t' . $d . ', \'length\' => $__t' . $d . ', \'parent\' => $__p' . $d . '); ?>';
        }
        throw new RuntimeException('Balise invalide : {% ' . $t . ' %}');
    }

    /**
     * Compile une expression de template en PHP :
     *   user.name == 'x'         ->  (isset($user['name']) ? $user['name'] : null) == 'x'
     *   top[loop.index - 1]      ->  isset($top[<index>-1]) ? … : null      (indices [ ] avec expression)
     */
    private function expr($e)
    {
        // Les chaînes entre quotes sont mises de côté (\x01N\x02) pendant la transformation
        $strings = [];
        $e = preg_replace_callback('/"(?:[^"\\\\]|\\\\.)*"|\'(?:[^\'\\\\]|\\\\.)*\'/s', function ($m) use (&$strings) {
            $strings[] = $m[0];
            return "\x01" . (count($strings) - 1) . "\x02";
        }, $e);

        $e = str_replace('~', '.', $this->exprCode($e));   // ~ = concaténation

        return trim(preg_replace_callback('/\x01(\d+)\x02/', function ($m) use ($strings) {
            return $strings[$m[1]];
        }, $e));
    }

    private function exprCode($e)
    {
        $ops = ['and' => '&&', 'or' => '||', 'not' => '!'];
        $lit = ['true', 'false', 'null'];

        // identifiant suivi de .cle et/ou [expression] (crochets imbriqués gérés par (?3))
        $re = '/(?<![\w$.])([A-Za-z_]\w*)((?:\.\w+|(\[(?:[^\[\]]++|(?3))*\]))*)(?!\w|\()/';

        return preg_replace_callback($re, function ($m) use ($ops, $lit) {
            $w = strtolower($m[1]);
            if ($m[2] === '') {
                if (isset($ops[$w])) return $ops[$w];
                if (in_array($w, $lit, true)) return $m[0];
            }
            $code = '$' . $m[1];
            preg_match_all('/\.(\w+)|(\[((?:[^\[\]]++|(?2))*)\])/', $m[2], $segs, PREG_SET_ORDER);
            foreach ($segs as $s) {
                if (isset($s[3])) {                                   // [ expression ]
                    $code .= '[' . $this->exprCode($s[3]) . ']';
                } else {                                              // .cle
                    $code .= '[' . (ctype_digit($s[1]) ? $s[1] : "'" . $s[1] . "'") . ']';
                }
            }
            return '(isset(' . $code . ') ? ' . $code . ' : null)';   // équivalent de "?? null" (PHP 7)
        }, $e);
    }
}