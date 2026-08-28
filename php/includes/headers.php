<header>
    Sheril: partie <?=$game_name?>, le jeux de conquête galactique
    <?=$tour_information?>
    <div style="font-size: 0.6em;">
        <?php
        $numero = intval(@$_SESSION['commandant_num']);
        if ($numero) {
            echo "Connecté en tant que " . getCommandantHTML($numero);
            echo ' | <a href="/deconnexion.php" style="color: #ccc;">Déconnexion</a>';
        } else {
            echo '<a href="/connexion.php" style="color: #ccc;">Se connecter</a>';
        }
        ?>
    </div>
</header>