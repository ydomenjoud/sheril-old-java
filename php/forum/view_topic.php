<?php
require_once '../includes/top.php';
require_once 'functions.php';
$commandant = check_auth();

$id_topic = isset($_GET['id']) ? (int)$_GET['id'] : 0;

// Récupérer le post initial (sujet)
$sql_topic = "SELECT p.*, r.NUMERO, r.RACE, r.NOM FROM _post p LEFT JOIN aa_registre r ON (r.NUMERO=p.id_author) WHERE id_post = $id_topic AND id_parent IS NULL";
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
    .btn { padding: 8px 15px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; display: inline-block; }
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
                <span class="post-author"><?=display_author($topic['NOM'], $topic['NUMERO'], $topic['RACE'])?></span>
                <span class="post-date"><?php echo $topic['record']; ?></span>
            </div>
            <div class="post-body">
                <?php echo bbcode_to_html($topic['body']); ?>
            </div>
        </div>
        
        <!-- Réponses -->
        <?php while ($reply = mysql_fetch_assoc($res_replies)): ?>
            <div class="post-container">
                <div class="post-header">
                    <span class="post-author"><?=display_author($reply['NOM'], $reply['NUMERO'], $reply['RACE'])?></span>
                    <span class="post-date"><?php echo $reply['record']; ?></span>
                </div>
                <div class="post-body">
                    <?php echo bbcode_to_html($reply['body']); ?>
                </div>
            </div>
        <?php endwhile; ?>


    <?php if(check_auth()> 0){ ?>
        <hr>
        <h3>Répondre</h3>
        <form action="post.php" method="post">
            <input type="hidden" name="id_parent" value="<?php echo $id_topic; ?>">
            <input type="hidden" name="id_forum" value="<?php echo $topic['id_forum']; ?>">
            <input type="hidden" name="title" value="Re: <?php echo htmlspecialchars($topic['title']); ?>">
            <p>
                <textarea name="body" rows="10" style="width: 100%;" required></textarea>
            </p>
            <p>
                <input type="submit" value="Poster ma réponse" class="btn">
            </p>
        </form>
    <?php } ?>
    </div>
    </main>

    <?php require_once '../includes/bot.php'; ?>
