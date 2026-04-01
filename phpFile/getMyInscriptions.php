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
    SELECT i.id, i.session_id, i.status, i.inscrit_le,
           s.formation_id, f.titre as formation_titre, f.thematique,
           s.date_debut, s.date_fin, s.location, s.is_online, s.meeting_link,
           u.name as formateur_name
    FROM inscription i
    INNER JOIN session s ON i.session_id = s.id
    INNER JOIN formation f ON s.formation_id = f.id
    LEFT JOIN user u ON s.formateur_id = u.id
    WHERE i.user_id = ?
    ORDER BY s.date_debut ASC
");
$stmt->execute([$userId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
