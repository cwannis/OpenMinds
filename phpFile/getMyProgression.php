<?php
require_once("bdd.php");
header('Content-Type: application/json');

$userId = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if (empty($userId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "user_id requis"]);
    exit;
}

$progressStmt = $bdd->prepare("
    SELECT f.id, f.titre, f.thematique, f.duration_minutes,
           COALESCE(MAX(qr.passed), 0) as quiz_passed,
           COALESCE(MAX(qr.score), 0) as quiz_score,
           COALESCE(MAX(qr.total_questions), 0) as quiz_total
    FROM formation f
    LEFT JOIN quiz q ON f.id = q.formation_id
    LEFT JOIN quiz_result qr ON q.id = qr.quiz_id AND qr.user_id = ?
    WHERE f.active = 1
    GROUP BY f.id, f.titre, f.thematique, f.duration_minutes
    ORDER BY f.datePubli DESC
");
$progressStmt->execute([$userId]);
$formations = $progressStmt->fetchAll(PDO::FETCH_ASSOC);

foreach ($formations as &$f) {
    $inscStmt = $bdd->prepare("
        SELECT COUNT(*) as cnt
        FROM inscription i
        INNER JOIN session s ON i.session_id = s.id
        WHERE i.user_id = ? AND s.formation_id = ?
    ");
    $inscStmt->execute([$userId, $f['id']]);
    $f['sessions_inscrites'] = $inscStmt->fetch()['cnt'];
}

echo json_encode($formations);
?>
