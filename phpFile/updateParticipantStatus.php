<?php
require_once("bdd.php");
header('Content-Type: application/json');

$formateurId = isset($_GET['formateur_id']) ? intval($_GET['formateur_id']) : 0;
$participantId = isset($_GET['participant_id']) ? intval($_GET['participant_id']) : 0;
$sessionId = isset($_GET['session_id']) ? intval($_GET['session_id']) : 0;
$status = isset($_GET['status']) ? trim($_GET['status']) : "";

if (empty($formateurId) || empty($participantId) || empty($sessionId) || !in_array($status, ['present', 'absent', 'termine'])) {
    http_response_code(400);
    echo json_encode(["erreur" => "parametres invalides"]);
    exit;
}

$checkStmt = $bdd->prepare("SELECT formateur_id FROM session WHERE id = ?");
$checkStmt->execute([$sessionId]);
$session = $checkStmt->fetch();

if (!$session || $session['formateur_id'] != $formateurId) {
    http_response_code(403);
    echo json_encode(["erreur" => "acces non autorise"]);
    exit;
}

$stmt = $bdd->prepare("UPDATE inscription SET status = ? WHERE session_id = ? AND user_id = ?");
$stmt->execute([$status, $sessionId, $participantId]);

if ($stmt->rowCount() > 0) {
    echo json_encode(["message" => "statut mis a jour"]);
} else {
    http_response_code(404);
    echo json_encode(["erreur" => "inscription introuvable"]);
}
?>
