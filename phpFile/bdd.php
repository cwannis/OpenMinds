<?php
require_once("Api.php");
$headers = getallheaders();

if (!isset($headers['X-Api-Key']) || $headers['X-Api-Key'] !== API_KEY) {
    http_response_code(401);
    echo json_encode(["erreur" => "Acces refuse : Cle API invalide."]);
    exit();
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
    $userexist = $bdd->prepare("SELECT 1 FROM user WHERE email = ?");
    $userexist->execute([$mail]);
    return $userexist->rowCount() > 0;
}

function verifyPassword($identifier, $bdd, $password)
{
    $stmt = $bdd->prepare("SELECT id, password FROM user WHERE email = ? OR name = ?");
    $stmt->execute([$identifier, $identifier]);
    $row = $stmt->fetch();
    if (!$row) return false;
    return password_verify($password, $row['password']);
}

function getBadgesForUser($id, $bdd)
{
    $req = $bdd->prepare("SELECT b.id, b.titre, b.description, UNIX_TIMESTAMP(b.datePubli) * 1000 as datePubli, b.imageUrl FROM abadge AS a INNER JOIN badge AS b ON a.idBadge = b.id WHERE a.idUser = ?");
    $req->execute([$id]);
    return $req->fetchAll(PDO::FETCH_ASSOC);
}
?>
