<?php
require_once("bdd.php");
header('Content-Type: application/json');

$formationId = isset($_GET['formation_id']) ? intval($_GET['formation_id']) : 0;

if (empty($formationId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "formation_id requis"]);
    exit;
}

$stmt = $bdd->prepare("SELECT id, formation_id, titre, passing_score FROM quiz WHERE formation_id = ?");
$stmt->execute([$formationId]);
$quizzes = $stmt->fetchAll(PDO::FETCH_ASSOC);

foreach ($quizzes as &$quiz) {
    $qStmt = $bdd->prepare("SELECT id, quiz_id, question, option_a, option_b, option_c, option_d FROM quiz_question WHERE quiz_id = ? ORDER BY id");
    $qStmt->execute([$quiz['id']]);
    $quiz['questions'] = $qStmt->fetchAll(PDO::FETCH_ASSOC);
}

echo json_encode($quizzes);
?>
