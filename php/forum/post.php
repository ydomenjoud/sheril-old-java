<?php
require_once '../includes/top.php';
require_once 'functions.php';
$commandant_num = check_auth();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id_forum = (int)$_POST['id_forum'];
    $id_parent = isset($_POST['id_parent']) && $_POST['id_parent'] !== "" ? (int)$_POST['id_parent'] : "NULL";
    $title = mysql_real_escape_string($_POST['title']);
    $body = mysql_real_escape_string($_POST['body']);
    $id_author = (int)$commandant_num;
    $record = date('Y-m-d H:i:s');

    $sql = "INSERT INTO _post (id_forum, id_parent, id_author, title, body, record) 
            VALUES ($id_forum, $id_parent, $id_author, '$title', '$body', '$record')";
    
    $res = mysql($base, $sql);
    
    if ($res) {
        $id_new = mysql_insert_id(); // Note: mysql_insert_id peut ne pas prendre $base selon l'implémentation, mais le polyfill le gère
        if ($id_parent === "NULL") {
            header("Location: view_topic.php?id=" . $id_new);
        } else {
            header("Location: view_topic.php?id=" . $id_parent);
        }
        exit;
    } else {
        die("Erreur lors de l'enregistrement du post : " . mysql_error());
    }
}

// Si on arrive ici en GET, c'est pour créer un nouveau sujet
$id_forum = (int)$_GET['id_forum'];
$sql_forum = "SELECT * FROM _forum WHERE id_forum = $id_forum";
$res_forum = mysql($base, $sql_forum);
$forum = mysql_fetch_assoc($res_forum);

if (!$forum) {
    die("Forum introuvable.");
}
?>

<main style="max-width: 100%">
    <div id="container">
        <div class="breadcrumb">
            <a href="index.php">Accueil Forum</a> &raquo; 
            <a href="view_forum.php?id=<?php echo $id_forum; ?>"><?php echo htmlspecialchars($forum['name']); ?></a> &raquo; 
            Nouveau sujet
        </div>
        
        <h1>Nouveau sujet dans <?php echo htmlspecialchars($forum['name']); ?></h1>
        
        <form action="post.php" method="post">
            <input type="hidden" name="id_forum" value="<?php echo $id_forum; ?>">
            <p>
                <label>Titre :</label><br>
                <input type="text" name="title" style="width: 100%;" required>
            </p>
            <p>
                <label>Message (BBCode autorisé) :</label><br>
                <textarea name="body" rows="15" style="width: 100%;" required></textarea>
            </p>
            <p>
                <input type="submit" value="Créer le sujet" class="btn">
            </p>
        </form>
    </div>
</main>

<?php require_once '../includes/bot.php'; ?>
