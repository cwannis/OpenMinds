<?php
require_once 'bdd.php';
header('Content-Type: application/json');

$mail = isset($_GET['email']) ? trim($_GET['email']) : "";
$newpsw = isset($_GET['newpsw']) ? $_GET['newpsw'] : "";

if (empty($mail) || empty($newpsw)) {
    http_response_code(400);
    echo json_encode(["erreur" => "parametres manquants"]);
    exit;
}

$hashedPassword = password_hash($newpsw, PASSWORD_BCRYPT);
$update = $bdd->prepare("UPDATE user SET password = ? WHERE email = ?");
$update->execute([$hashedPassword, $mail]);

echo json_encode(["message" => "mot de passe change avec succes"]);
?>
