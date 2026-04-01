<?php
require_once("bdd.php");
header('Content-Type: application/json');

$userId = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;
$sessionId = isset($_GET['session_id']) ? intval($_GET['session_id']) : 0;

if (empty($userId) || empty($sessionId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "parametres manquants"]);
    exit;
}

$stmt = $bdd->prepare("DELETE FROM inscription WHERE user_id = ? AND session_id = ?");
$stmt->execute([$userId, $sessionId]);

if ($stmt->rowCount() > 0) {
    echo json_encode(["message" => "desinscription effectuee"]);
} else {
    http_response_code(404);
    echo json_encode(["erreur" => "inscription introuvable"]);
}
?>
