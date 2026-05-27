<?php
require_once '../includes/top.php';
require_once 'functions.php';
$commandant_num = check_auth();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id_post = isset($_POST['id_post']) ? (int)$_POST['id_post'] : 0;
    $id_forum = (int)$_POST['id_forum'];
    $id_parent_val = isset($_POST['id_parent']) && $_POST['id_parent'] !== "" && $_POST['id_parent'] !== "0" ? (int)$_POST['id_parent'] : "NULL";
    $id_parent = $id_parent_val;
    $title = mysql_real_escape_string($_POST['title']);
    $body = mysql_real_escape_string($_POST['body']);
    $id_author = (int)$commandant_num;
    global $base;
    $record = date('Y-m-d H:i:s');

    if ($id_post > 0) {
        // Mode Edition : vérifier l'auteur
        $sql_check = "SELECT id_author FROM _post WHERE id_post = $id_post";
        $res_check = mysql($base, $sql_check);
        $check = mysql_fetch_assoc($res_check);
        if ($check && (int)$check['id_author'] === $id_author) {
            $sql = "UPDATE _post SET title = '$title', body = '$body' WHERE id_post = $id_post";
            $res = mysql($base, $sql);
            if ($res) {
                if ($id_parent_val === "NULL" || (int)$id_parent_val === 0) {
                    header("Location: view_topic.php?id=" . $id_post);
                } else {
                    header("Location: view_topic.php?id=" . $id_parent_val);
                }
                exit;
            }
        } else {
            die("Vous n'avez pas l'autorisation de modifier ce post.");
        }
    } else {
        // Mode Création
        $sql = "INSERT INTO _post (id_forum, id_parent, id_author, title, body, record) 
                VALUES ($id_forum, $id_parent, $id_author, '$title', '$body', '$record')";
        
        $res = mysql($base, $sql);
        
        if ($res) {
            $id_new = mysql_insert_id($GLOBALS['___mysql_default_link']);
            if ($id_parent_val === "NULL" || (int)$id_parent_val === 0) {
                header("Location: view_topic.php?id=" . $id_new);
            } else {
                header("Location: view_topic.php?id=" . $id_parent_val);
            }
            exit;
        } else {
            die("Erreur lors de l'enregistrement du post : " . mysql_error());
        }
    }
}

// Si on arrive ici en GET
global $base;
$id_post = isset($_GET['edit']) ? (int)$_GET['edit'] : 0;
$id_forum = isset($_GET['id_forum']) ? (int)$_GET['id_forum'] : 0;
$id_parent = isset($_GET['id_parent']) ? (int)$_GET['id_parent'] : 0;

$post_to_edit = null;
if ($id_post > 0) {
    $sql_edit = "SELECT * FROM _post WHERE id_post = $id_post";
    $res_edit = mysql($base, $sql_edit);
    $post_to_edit = mysql_fetch_assoc($res_edit);
    if (!$post_to_edit || (int)$post_to_edit['id_author'] !== (int)$commandant_num) {
        die("Post introuvable ou vous n'êtes pas l'auteur.");
    }
    $id_forum = $post_to_edit['id_forum'];
    $id_parent = $post_to_edit['id_parent'];
}

$sql_forum = "SELECT * FROM _forum WHERE id_forum = $id_forum";
$res_forum = mysql($base, $sql_forum);
$forum = mysql_fetch_assoc($res_forum);

if (!$forum) {
    die("Forum introuvable.");
}
?>

<!-- Quill 2.0.3 resources -->
<link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>

<style>
    #editor { height: 300px; background-color: #fff; color: #000; }
    .ql-toolbar { background-color: #eee; }
    .preview-box { border: 1px solid #444; padding: 20px; background-color: #1a1a1a; margin-top: 20px; display: none; }
    .btn-preview { background-color: #6c757d; }
</style>

<main style="max-width: 100%">
    <div id="container">
        <div class="breadcrumb">
            <a href="index.php">Accueil Forum</a> &raquo; 
            <a href="view_forum.php?id=<?php echo $id_forum; ?>"><?php echo htmlspecialchars($forum['name']); ?></a> &raquo; 
            <?php echo $post_to_edit ? "Editer un post" : ($id_parent ? "Répondre" : "Nouveau sujet"); ?>
        </div>
        
        <h1><?php echo $post_to_edit ? "Editer" : ($id_parent ? "Répondre" : "Nouveau sujet dans " . htmlspecialchars($forum['name'])); ?></h1>
        
        <form id="post-form" action="post.php" method="post">
            <input type="hidden" name="id_forum" value="<?php echo $id_forum; ?>">
            <input type="hidden" name="id_parent" value="<?php echo $id_parent; ?>">
            <input type="hidden" name="id_post" value="<?php echo $id_post; ?>">
            <input type="hidden" name="body" id="body-input">
            
            <p>
                <label>Titre :</label><br>
                <input type="text" name="title" style="width: 100%;" required value="<?php 
                    if ($post_to_edit) {
                        echo htmlspecialchars($post_to_edit['title']);
                    } elseif ($id_parent) {
                        // Pour une réponse, on peut essayer de mettre "Re: " + titre du parent
                        $sql_parent = "SELECT title FROM _post WHERE id_post = $id_parent";
                        $res_p = mysql($base, $sql_parent);
                        $row_p = mysql_fetch_assoc($res_p);
                        echo "Re: " . htmlspecialchars(isset($row_p['title']) ? $row_p['title'] : "");
                    }
                ?>">
            </p>
            <p>
                <label>Message :</label><br>
                <div id="editor"><?php echo $post_to_edit ? $post_to_edit['body'] : ""; ?></div>
            </p>
            <p>
                <input type="button" id="preview-btn" value="Prévisualiser" class="btn btn-preview">
                <input type="submit" value="<?php echo $post_to_edit ? "Enregistrer les modifications" : "Envoyer"; ?>" class="btn">
            </p>
        </form>

        <div id="preview-container" class="preview-box">
            <h3>Aperçu :</h3>
            <div id="preview-content"></div>
        </div>
    </div>
</main>

<script>
    const quill = new Quill('#editor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{ 'header': [1, 2, 3, false] }],
                ['bold', 'italic', 'underline', 'strike'],
                ['link', 'blockquote', 'code-block', 'image'],
                [{ 'list': 'ordered'}, { 'list': 'bullet' }],
                [{ 'color': [] }, { 'background': [] }],
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

<?php require_once '../includes/bot.php'; ?>
