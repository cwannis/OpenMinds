<?php
require_once("bdd.php");
header('Content-Type: application/json');

$formationId = isset($_GET['formation_id']) ? intval($_GET['formation_id']) : 0;

if (empty($formationId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "formation_id requis"]);
    exit;
}

$stmt = $bdd->prepare("SELECT id, titre, description, thematique, type, imageUrl, content, videoUrl, duration_minutes, created_by, datePubli FROM formation WHERE id = ? AND active = 1");
$stmt->execute([$formationId]);
$formation = $stmt->fetch();

if (!$formation) {
    http_response_code(404);
    echo json_encode(["erreur" => "formation introuvable"]);
    exit;
}

echo json_encode($formation);
?>
