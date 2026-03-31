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
    SELECT s.id, s.formation_id, f.titre as formation_titre, f.thematique,
           s.date_debut, s.date_fin, s.location, s.is_online, s.meeting_link,
           COUNT(i.id) as nb_inscrits
    FROM session s
    INNER JOIN formation f ON s.formation_id = f.id
    LEFT JOIN inscription i ON s.id = i.session_id
    WHERE s.formateur_id = ? AND s.active = 1
    GROUP BY s.id
    ORDER BY s.date_debut ASC
");
$stmt->execute([$userId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
