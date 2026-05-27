<?php
require_once '../includes/top.php';
require_once 'functions.php';
$commandant = check_auth();
global $base;

$id_topic = isset($_GET['id']) ? (int)$_GET['id'] : 0;

// Récupérer le post initial (sujet)
$sql_topic = "SELECT p.*, r.NUMERO, r.RACE, r.NOM FROM _post p LEFT JOIN aa_registre r ON (r.NUMERO=p.id_author) WHERE id_post = $id_topic AND (id_parent IS NULL OR id_parent = 0)";
$res_topic = mysql($base, $sql_topic);
$topic = mysql_fetch_assoc($res_topic);

if (!$topic) {
    die("Sujet introuvable.");
}

// Récupérer le forum pour le fil d'ariane
$sql_forum = "SELECT * FROM _forum WHERE id_forum = " . (int)$topic['id_forum'];
$res_forum = mysql($base, $sql_forum);
$forum = mysql_fetch_assoc($res_forum);

// Récupérer les réponses
$sql_replies = "SELECT p.*, r.NUMERO, r.RACE, r.NOM FROM _post p LEFT JOIN aa_registre r ON (r.NUMERO=p.id_author) WHERE id_parent = $id_topic ORDER BY record ASC";
$res_replies = mysql($base, $sql_replies);
?>
<style>
    .post-container { border: 1px solid #444; margin-bottom: 20px; }
    .post-header { background-color: #333; padding: 10px; border-bottom: 1px solid #444; display: flex; justify-content: space-between; }
    .post-body { padding: 20px; background-color: #1a1a1a; min-height: 100px; }
    .post-author { font-weight: bold; }
    .post-date { font-size: 0.8em; color: #aaa; }
    .breadcrumb { margin-bottom: 20px; }
    .ql-editor { font-size: 16px; min-height: 150px; }
    .ql-toolbar { background-color: #eee; }
</style>
<main style="max-width: 100%">
        <div class="breadcrumb">
            <a href="index.php">Accueil Forum</a> &raquo; 
            <a href="view_forum.php?id=<?php echo $forum['id_forum']; ?>"><?php echo htmlspecialchars($forum['name']); ?></a> &raquo; 
            <?php echo htmlspecialchars($topic['title']); ?>
        </div>
        
        <h1><?php echo htmlspecialchars($topic['title']); ?></h1>
        
        <!-- Post initial -->
        <div class="post-container">
            <div class="post-header">
                <span class="post-author"><?php echo display_author($topic['NOM'], $topic['NUMERO'], $topic['RACE']); ?></span>
                <span class="post-date">
                    <?php if ((int)$topic['id_author'] === (int)$commandant): ?>
                        <a href="post.php?edit=<?php echo $topic['id_post']; ?>" style="color: #007bff; margin-right: 10px;">Editer</a>
                    <?php endif; ?>
                    <?php echo format_date($topic['record']); ?>
                </span>
            </div>
            <div class="post-body">
                <?php echo render_post_body($topic['body']); ?>
            </div>
        </div>
        
        <!-- Réponses -->
        <?php while ($reply = mysql_fetch_assoc($res_replies)): ?>
            <div class="post-container">
                <div class="post-header">
                    <span class="post-author"><?php echo display_author($reply['NOM'], $reply['NUMERO'], $reply['RACE']); ?></span>
                    <span class="post-date">
                        <?php if ((int)$reply['id_author'] === (int)$commandant): ?>
                            <a href="post.php?edit=<?php echo $reply['id_post']; ?>" style="color: #007bff; margin-right: 10px;">Editer</a>
                        <?php endif; ?>
                        <?php echo format_date($reply['record']); ?>
                    </span>
                </div>
                <div class="post-body">
                    <?php echo render_post_body($reply['body']); ?>
                </div>
            </div>
        <?php endwhile; ?>


    <?php if(check_auth()> 0){ ?>
        <hr>
        <h3>Répondre</h3>

        <!-- Quill 2.0.3 resources -->
        <link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>
        <style>
            #editor { height: 200px; background-color: #fff; color: #000; }
            .preview-box { border: 1px solid #444; padding: 20px; background-color: #1a1a1a; margin-top: 20px; display: none; }
        </style>

        <form id="post-form" action="post.php" method="post">
            <input type="hidden" name="id_parent" value="<?php echo $id_topic; ?>">
            <input type="hidden" name="id_forum" value="<?php echo $topic['id_forum']; ?>">
            <input type="hidden" name="title" value="Re: <?php echo htmlspecialchars($topic['title']); ?>">
            <input type="hidden" name="body" id="body-input">
            <p>
                <div id="editor"></div>
            </p>
            <p>
                <input type="button" id="preview-btn" value="Prévisualiser" class="btn" style="background-color: #6c757d;">
                <input type="submit" value="Poster ma réponse" class="btn">
            </p>
        </form>

        <div id="preview-container" class="preview-box">
            <h3>Aperçu :</h3>
            <div id="preview-content"></div>
        </div>

        <script>
            const quill = new Quill('#editor', {
                theme: 'snow',
                modules: {
                    toolbar: [
                        ['bold', 'italic', 'underline'],
                        ['link', 'blockquote', 'image'],
                        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
                        ['clean']
                    ]
                }
            });

            const form = document.querySelector('#post-form');
            form.onsubmit = function() {
                const bodyInput = document.querySelector('#body-input');
                bodyInput.value = quill.root.innerHTML;
            };

            const previewBtn = document.querySelector('#preview-btn');
            const previewContainer = document.querySelector('#preview-container');
            const previewContent = document.querySelector('#preview-content');

            previewBtn.onclick = function() {
                previewContent.innerHTML = quill.root.innerHTML;
                previewContainer.style.display = 'block';
                previewContainer.scrollIntoView({ behavior: 'smooth' });
            };
        </script>
    <?php } ?>
    </div>
    </main>

    <?php require_once '../includes/bot.php'; ?>
