<?php
require_once("bdd.php");
header('Content-Type: application/json');

$identifier = isset($_GET['email']) ? trim($_GET['email']) : "";
$password = isset($_GET['password']) ? $_GET['password'] : "";
$id = isset($_GET['id']) ? intval($_GET['id']) : 0;

if (!empty($identifier) && !empty($password)) {
    if (verifyPassword($identifier, $bdd, $password)) {
        $user = $bdd->prepare("SELECT id, name, email, role, ppLink FROM user WHERE email = ? OR name = ? LIMIT 1");
        $user->execute([$identifier, $identifier]);
        echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
    } else {
        http_response_code(401);
        echo json_encode(["erreur" => "identifiants incorrects"]);
    }
} elseif (!empty($id)) {
    $user = $bdd->prepare("SELECT id, name, email, role, ppLink FROM user WHERE id = ?");
    $user->execute([$id]);
    echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
} else {
    http_response_code(400);
    echo json_encode(["erreur" => "parametres manquants"]);
}
?>
