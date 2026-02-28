<?php
$headers = getallheaders();

// 3. Vérifier si la clé est présente et valide
if (!isset($headers['X-Api-Key']) || $headers['X-Api-Key'] !== MON_API_KEY_SECRETE) {
    // Si la clé est fausse ou absente, on bloque tout !
    http_response_code(401); // Erreur 401 : Non autorisé
    echo json_encode(["erreur" => "Accès refusé : Clé API invalide."]);
    exit(); // On arrête l'exécution du script ici
}

$bdd = new PDO("mysql:dbname=openminds;host=127.0.0.1", 'root', '');

function userExists($mail, $bdd)
{
    $userexist = $bdd->prepare("SELECT * FROM user WHERE email = ?");
    $userexist->execute(array($mail));
    return $userexist->rowCount() > 0;
}

function userExistsPassword($mail, $bdd, $password)
{
    $userexist = $bdd->prepare("SELECT * FROM user WHERE email = ? AND password = ?");
    $userexist->execute(array($mail, $password));
    return $userexist->rowCount() > 0;
}
?>