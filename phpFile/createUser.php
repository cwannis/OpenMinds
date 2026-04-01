<?php
require_once("bdd.php");
header('Content-Type: application/json');

$name = isset($_GET["name"]) ? trim($_GET["name"]) : "";
$mail = isset($_GET["mail"]) ? trim($_GET["mail"]) : "";
$password = isset($_GET["password"]) ? $_GET["password"] : "";

if (empty($name) || empty($mail) || empty($password)) {
    http_response_code(400);
    echo json_encode(["erreur" => "Tous les champs sont obligatoires"]);
    exit;
}

if (!filter_var($mail, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(["erreur" => "Email invalide"]);
    exit;
}

if (mailExists($mail, $bdd)) {
    http_response_code(409);
    echo json_encode(["erreur" => "Cet email est deja utilise"]);
    exit;
}

$hashedPassword = password_hash($password, PASSWORD_BCRYPT);
$insertUser = $bdd->prepare("INSERT INTO user (name, email, password) VALUES (?, ?, ?)");
$insertUser->execute([$name, $mail, $hashedPassword]);

http_response_code(201);
echo json_encode(["message" => "Utilisateur cree avec succes"]);
?>
