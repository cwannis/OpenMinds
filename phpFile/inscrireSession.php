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

$checkStmt = $bdd->prepare("SELECT id FROM inscription WHERE user_id = ? AND session_id = ?");
$checkStmt->execute([$userId, $sessionId]);
if ($checkStmt->rowCount() > 0) {
    http_response_code(409);
    echo json_encode(["erreur" => "deja inscrit a cette session"]);
    exit;
}

$capacityStmt = $bdd->prepare("SELECT max_participants FROM session WHERE id = ?");
$capacityStmt->execute([$sessionId]);
$session = $capacityStmt->fetch();
if ($session && $session['max_participants'] > 0) {
    $countStmt = $bdd->prepare("SELECT COUNT(*) as cnt FROM inscription WHERE session_id = ?");
    $countStmt->execute([$sessionId]);
    $count = $countStmt->fetch();
    if ($count['cnt'] >= $session['max_participants']) {
        http_response_code(400);
        echo json_encode(["erreur" => "session complete"]);
        exit;
    }
}

$stmt = $bdd->prepare("INSERT INTO inscription (user_id, session_id, status) VALUES (?, ?, 'inscrit')");
$stmt->execute([$userId, $sessionId]);

http_response_code(201);
echo json_encode(["message" => "inscription effectuee"]);
?>
