<?php
require_once("bdd.php");
header('Content-Type: application/json');

$sessionId = isset($_GET['session_id']) ? intval($_GET['session_id']) : 0;

if (empty($sessionId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "session_id requis"]);
    exit;
}

$stmt = $bdd->prepare("
    SELECT i.id as inscription_id, u.id as user_id, u.name, u.email, i.status, i.inscrit_le
    FROM inscription i
    INNER JOIN user u ON i.user_id = u.id
    WHERE i.session_id = ?
    ORDER BY u.name ASC
");
$stmt->execute([$sessionId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
