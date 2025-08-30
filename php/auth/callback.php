<?php
session_start();
// ⚠️ Mets tes valeurs ici
$client_id     = "1408426801662922845";
$client_secret = "p5_42sz1lt47itNhECzt55kL3nF4S__7";
$redirect_uri  = "https://sheril.pbem-france.net/auth/callback.php";

if (!isset($_GET['code'])) {
    die("Code OAuth2 manquant.");
}

$code = $_GET['code'];

// Étape 1 : échanger le code contre un token
$token_url = "https://discord.com/api/oauth2/token";

$data = [
    "client_id" => $client_id,
    "client_secret" => $client_secret,
    "grant_type" => "authorization_code",
    "code" => $code,
    "redirect_uri" => $redirect_uri
];

$options = [
    "http" => [
        "header"  => "Content-type: application/x-www-form-urlencoded\r\n",
        "method"  => "POST",
        "content" => http_build_query($data)
    ]
];

$context  = stream_context_create($options);
$response = file_get_contents($token_url, false, $context);

if ($response === false) {
    die("Erreur lors de l'échange du code.");
}

$token = json_decode($response, true);

// Étape 2 : récupérer l'utilisateur
$user_url = "https://discord.com/api/users/@me";

$opts = [
    "http" => [
        "header" => "Authorization: Bearer " . $token['access_token']
    ]
];

$context = stream_context_create($opts);
$user = file_get_contents($user_url, false, $context);

if ($user === false) {
    die("Erreur lors de la récupération de l'utilisateur.");
}

$user = json_decode($user, true);

$_SESSION["discord_user_id"] = $user["id"];
