<?php
require_once("bdd.php");
header('Content-Type: application/json');

$userId = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if (empty($userId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "user_id requis"]);
    exit;
}

$stmt = $bdd->prepare("SELECT role FROM user WHERE id = ?");
$stmt->execute([$userId]);
$user = $stmt->fetch();

if (!$user || $user['role'] !== 'admin') {
    http_response_code(403);
    echo json_encode(["erreur" => "acces admin requis"]);
    exit;
}

$statsStmt = $bdd->query("
    SELECT
        (SELECT COUNT(*) FROM user WHERE role = 'benevole') as nb_benevoles,
        (SELECT COUNT(*) FROM user WHERE role = 'formateur') as nb_formateurs,
        (SELECT COUNT(*) FROM formation WHERE active = 1) as nb_formations,
        (SELECT COUNT(*) FROM session WHERE active = 1) as nb_sessions,
        (SELECT COUNT(*) FROM inscription) as nb_inscriptions,
        (SELECT COUNT(*) FROM quiz_result) as nb_quiz_passes,
        (SELECT COUNT(*) FROM quiz_result WHERE passed = 1) as nb_quiz_reussis
");
$stats = $statsStmt->fetch();

$reussite = 0;
if ($stats['nb_quiz_passes'] > 0) {
    $reussite = round($stats['nb_quiz_reussis'] * 100 / $stats['nb_quiz_passes']);
}
$stats['taux_reussite'] = $reussite;

$recentStmt = $bdd->query("
    SELECT u.name, f.titre as formation_titre, i.status, i.inscrit_le
    FROM inscription i
    INNER JOIN user u ON i.user_id = u.id
    INNER JOIN session s ON i.session_id = s.id
    INNER JOIN formation f ON s.formation_id = f.id
    ORDER BY i.inscrit_le DESC
    LIMIT 10
");
$recent = $recentStmt->fetchAll(PDO::FETCH_ASSOC);

echo json_encode([
    "stats" => $stats,
    "recent_inscriptions" => $recent
]);
?>
