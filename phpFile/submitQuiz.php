<?php
require_once("bdd.php");
header('Content-Type: application/json');

$userId = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;
$quizId = isset($_GET['quiz_id']) ? intval($_GET['quiz_id']) : 0;
$answers = isset($_GET['answers']) ? $_GET['answers'] : "";

if (empty($userId) || empty($quizId) || empty($answers)) {
    http_response_code(400);
    echo json_encode(["erreur" => "parametres manquants"]);
    exit;
}

$qStmt = $bdd->prepare("SELECT id, correct_answer FROM quiz_question WHERE quiz_id = ?");
$qStmt->execute([$quizId]);
$questions = $qStmt->fetchAll(PDO::FETCH_ASSOC);

if (empty($questions)) {
    http_response_code(404);
    echo json_encode(["erreur" => "quiz introuvable"]);
    exit;
}

$score = 0;
$answersArr = explode(",", $answers);
foreach ($questions as $i => $q) {
    if (isset($answersArr[$i]) && trim($answersArr[$i]) === $q['correct_answer']) {
        $score++;
    }
}

$totalQuestions = count($questions);
$passed = ($score / $totalQuestions * 100) >= 60;

$stmt = $bdd->prepare("INSERT INTO quiz_result (user_id, quiz_id, score, total_questions, passed) VALUES (?, ?, ?, ?, ?)");
$stmt->execute([$userId, $quizId, $score, $totalQuestions, $passed ? 1 : 0]);

if ($passed) {
    $quizInfo = $bdd->prepare("SELECT formation_id FROM quiz WHERE id = ?");
    $quizInfo->execute([$quizId]);
    $quizData = $quizInfo->fetch();
    if ($quizData) {
        $badgeStmt = $bdd->prepare("SELECT badge_id FROM formation_badge WHERE formation_id = ?");
        $badgeStmt->execute([$quizData['formation_id']]);
        $badgeData = $badgeStmt->fetch();
        if ($badgeData) {
            $checkBadge = $bdd->prepare("SELECT 1 FROM abadge WHERE idUser = ? AND idBadge = ?");
            $checkBadge->execute([$userId, $badgeData['badge_id']]);
            if ($checkBadge->rowCount() === 0) {
                $insertBadge = $bdd->prepare("INSERT INTO abadge (idBadge, idUser) VALUES (?, ?)");
                $insertBadge->execute([$badgeData['badge_id'], $userId]);
            }
        }
    }
}

echo json_encode([
    "score" => $score,
    "total_questions" => $totalQuestions,
    "passed" => $passed ? 1 : 0,
    "percentage" => round($score / $totalQuestions * 100)
]);
?>
