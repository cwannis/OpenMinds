<?php
require_once("Api.php");
$headers = getallheaders();

// 3. Vérifier si la clé est présente et valide
if (!isset($headers['X-Api-Key']) || $headers['X-Api-Key'] !== API_KEY) {
    // Si la clé est fausse ou absente, on bloque tout !
    http_response_code(401); // Erreur 401 : Non autorisé
    echo json_encode(["erreur" => "Acces refuse : Cle API invalide."]);
    exit(); // On arrête l'exécution du script ici
}

$dbHost = getenv("OPENMINDS_DB_HOST") ?: "db";
$dbName = getenv("OPENMINDS_DB_NAME") ?: "openminds";
$dbUser = getenv("OPENMINDS_DB_USER") ?: "openminds";
$dbPass = getenv("OPENMINDS_DB_PASSWORD") ?: "openminds";

$bdd = new PDO(
    "mysql:dbname={$dbName};host={$dbHost};charset=utf8mb4",
    $dbUser,
    $dbPass,
    [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    ]
);

function mailExists($mail, $bdd)
{
    $userexist = $bdd->prepare("SELECT * FROM user WHERE email = ?");
    $userexist->execute(array($mail));
    return $userexist->rowCount() > 0;
}

function userExistsPassword($identifier, $bdd, $password)
{
    $userexist = $bdd->prepare("SELECT * FROM user WHERE (email = ? OR name = ?) AND password = ?");
    $userexist->execute(array($identifier, $identifier, $password));
    return $userexist->rowCount() > 0;
}

function getBadgesForUser($id, $bdd)
{
    $req = $bdd->prepare("SELECT SELECT b.id, b.titre, b.description, UNIX_TIMESTAMP(b.datePubli) * 1000 as datePubli, b.imageUrl FROM abadge AS a INNER JOIN badge AS b ON a.idBadge = b.id WHERE a.idUser = ?");
    $req->execute(array($id));
    return $req->fetchAll(PDO::FETCH_ASSOC);
}
?>
