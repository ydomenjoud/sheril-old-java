<?php
require_once 'includes/top.php';
require_once 'includes/auth.php';

$error = "";

// Si déjà connecté, redirection vers l'accueil ou le forum
if (auth_check() > 0) {
    header("Location: /index.php");
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $login = isset($_POST['login']) ? $_POST['login'] : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';

    if (auth_login($login, $password)) {
        header("Location: /index.php");
        exit;
    } else {
        $error = "Identifiants incorrects. Veuillez réessayer.";
    }
}
?>

<main style="max-width: 600px; margin: 0 auto; padding: 20px;">
    <h1>Connexion</h1>

    <?php if ($error): ?>
        <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
            <?php echo $error; ?>
        </div>
    <?php endif; ?>

    <form action="connexion.php" method="post" style="background-color: #333; padding: 20px; border-radius: 8px;">
        <p>
            <label for="login">Nom d'utilisateur / Login :</label><br>
            <input type="text" name="login" id="login" style="width: 100%; padding: 8px; margin-top: 5px; background: #222; color: #fff; border: 1px solid #444;" required>
        </p>
        <p>
            <label for="password">Mot de passe :</label><br>
            <input type="password" name="password" id="password" style="width: 100%; padding: 8px; margin-top: 5px; background: #222; color: #fff; border: 1px solid #444;" required>
        </p>
        <p style="text-align: right; margin-top: 20px;">
            <input type="submit" value="Se connecter" class="btn" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
        </p>
    </form>
    
    <p style="margin-top: 20px; text-align: center;">
        Pas encore inscrit ? <a href="register.php">Créez un compte</a>
    </p>
</main>

<?php require_once 'includes/bot.php'; ?>
