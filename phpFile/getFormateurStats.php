<?php
require_once("bdd.php");
header('Content-Type: application/json');

$userId = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if (empty($userId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "user_id requis"]);
    exit;
}

$stmt = $bdd->prepare("
    SELECT f.id, f.titre, f.thematique,
           COUNT(DISTINCT s.id) as nb_sessions,
           COUNT(DISTINCT i.id) as nb_inscrits,
           COUNT(DISTINCT qr.id) as nb_quiz_passes,
           COUNT(DISTINCT CASE WHEN qr.passed = 1 THEN qr.id END) as nb_quiz_reussis
    FROM formation f
    LEFT JOIN session s ON f.id = s.formation_id AND s.formateur_id = ?
    LEFT JOIN inscription i ON s.id = i.session_id
    LEFT JOIN quiz q ON f.id = q.formation_id
    LEFT JOIN quiz_result qr ON q.id = qr.quiz_id
    WHERE f.created_by = ?
    GROUP BY f.id, f.titre, f.thematique
");
$stmt->execute([$userId, $userId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
