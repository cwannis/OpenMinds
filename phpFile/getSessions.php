<?php
require_once("bdd.php");
header('Content-Type: application/json');

$formationId = isset($_GET['formation_id']) ? intval($_GET['formation_id']) : 0;

if (empty($formationId)) {
    http_response_code(400);
    echo json_encode(["erreur" => "formation_id requis"]);
    exit;
}

$stmt = $bdd->prepare("
    SELECT s.id, s.formation_id, s.formateur_id, u.name as formateur_name, s.date_debut, s.date_fin, s.location, s.max_participants, s.is_online, s.meeting_link, s.active
    FROM session s
    LEFT JOIN user u ON s.formateur_id = u.id
    WHERE s.formation_id = ? AND s.active = 1 AND s.date_debut >= NOW()
    ORDER BY s.date_debut ASC
");
$stmt->execute([$formationId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
