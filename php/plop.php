<?php
die();

@require_once './secure/connect.txt';

// 2. Sélection de tous les joueurs ayant un email renseigné
$sql = "SELECT LOGIN, MOT_DE_PASSE, ADRESSE, NOM FROM aa_registre WHERE TOUR_ARRIVEE=5";
$result = mysql_query($sql);

if (!$result) {
    die("Erreur dans la requête : " . @mysql_error());
}

$total = @mysql_num_rows($result);
$envoyes = 0;
$erreurs = 0;

echo "<h2>Début de l'envoi groupé ($total joueurs trouvés)</h2>";
echo "<hr>";

// 3. Boucle d'envoi
while ($row = @mysql_fetch_assoc($result)) {
    $email_dest = trim($row['ADRESSE']);
    $login      = $row['LOGIN'];
    $password   = $row['MOT_DE_PASSE'];
    $nom        = $row['NOM'];

    // On vérifie tout de même que l'email ressemble à quelque chose
    if (strpos($email_dest, '@') !== false) {

        $sujet = "Rappel de vos identifiants de jeu";
        $message = "Bonjour $nom,\n\nVoici vos accès actuels :\n";
        $message .= "Login : $login\n";
        $message .= "Mot de passe : $password\n\n";
        $message .= "Lien du jeu : https://sheril.pbem-france.net/\n";

        $headers = "From: myst@pbem-france.net";

        // Envoi de l'email
        if (mail($email_dest, $sujet, $message, $headers)) {
            echo "Succès : Email envoyé à $login ($email_dest)<br>";
            $envoyes++;
        } else {
            echo "<span style='color:red;'>Échec : Impossible d'envoyer à $login ($email_dest)</span><br>";
            $erreurs++;
        }
    } else {
        echo "Ignoré : Email invalide pour $login ($email_dest)<br>";
    }

    // Petite pause de 0.1 seconde pour ne pas saturer le serveur de mail
    usleep(100000);
}

echo "<hr>";
echo "<h3>Terminé !</h3>";
echo "Emails envoyés : $envoyes <br>";
echo "Erreurs : $erreurs";

?>