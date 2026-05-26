<?php
require_once '../includes/top.php';
require_once 'functions.php';
$commandant = check_auth();

$id_forum = isset($_GET['id']) ? (int)$_GET['id'] : 0;

$sql_forum = "SELECT * FROM _forum WHERE id_forum = $id_forum";
$res_forum = mysql($base, $sql_forum);
$forum = mysql_fetch_assoc($res_forum);

if (!$forum) {
    die("Forum introuvable.");
}

$sql_topics = "SELECT p.*, r.NUMERO, r.RACE, r.NOM FROM _post p LEFT JOIN aa_registre r ON (r.NUMERO=p.id_author) WHERE id_forum = $id_forum AND id_parent IS NULL ORDER BY record DESC";
$res_topics = mysql($base, $sql_topics);
?>
<style>
    .forum-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    .forum-table th, .forum-table td { border: 1px solid #444; padding: 10px; text-align: left; }
    .forum-table th { background-color: #333; color: #fff; }
    .breadcrumb { margin-bottom: 20px; }
    .btn { padding: 8px 15px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; display: inline-block; }
</style>
<main style="max-width: 100%">
        <div class="breadcrumb">
            <a href="index.php">Accueil Forum</a> &raquo; <?php echo htmlspecialchars($forum['name']); ?>
        </div>
        
        <h1><?php echo htmlspecialchars($forum['name']); ?></h1>
        <p><?php echo htmlspecialchars($forum['description']); ?></p>

        <?php if(check_auth()> 0){ ?>
        <p><a href="post.php?id_forum=<?php echo $id_forum; ?>" class="btn">Nouveau sujet</a></p>
        <?php } ?>
        
        <table class="forum-table">
            <thead>
                <tr>
                    <th>Sujet</th>
                    <th>Auteur</th>
                    <th>Date</th>
                    <th>Réponses</th>
                    <th>Dernier message</th>
                </tr>
            </thead>
            <tbody>
                <?php while ($topic = mysql_fetch_assoc($res_topics)): 
                    $id_t = (int)$topic['id_post'];
                    $sql_count = "SELECT COUNT(*) as nb FROM _post WHERE id_parent = $id_t";
                    $res_count = mysql($base, $sql_count);
                    $count = mysql_fetch_assoc($res_count);

                    // Dernier message du sujet
                    $sql_last_topic = "SELECT p.record, r.NOM, r.NUMERO, r.RACE 
                                       FROM _post p 
                                       LEFT JOIN aa_registre r ON (r.NUMERO = p.id_author) 
                                       WHERE p.id_post = $id_t OR p.id_parent = $id_t 
                                       ORDER BY p.record DESC LIMIT 1";
                    $res_last_topic = mysql($base, $sql_last_topic);
                    $last_topic = mysql_fetch_assoc($res_last_topic);
                ?>
                    <tr>
                        <td>
                            <a href="view_topic.php?id=<?php echo $topic['id_post']; ?>">
                                <?php echo htmlspecialchars($topic['title']); ?>
                            </a>
                        </td>
                        <td><?=display_author($topic['NOM'], $topic['NUMERO'], $topic['RACE'])?></td>
                        <td><?php echo $topic['record']; ?></td>
                        <td><?php echo $count['nb']; ?></td>
                        <td>
                            <?php if ($last_topic): ?>
                                Par <?php echo display_author($last_topic['NOM'], $last_topic['NUMERO'], $last_topic['RACE']); ?><br>
                                Le <?php echo $last_topic['record']; ?>
                            <?php endif; ?>
                        </td>
                    </tr>
<?php endwhile; ?>
            </tbody>
        </table>
    </main>

<?php require_once '../includes/bot.php'; ?>
