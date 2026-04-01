<?php
require_once("bdd.php");
header('Content-Type: application/json');

$stmt = $bdd->query("
    SELECT f.id, f.titre, f.description, f.thematique, f.type, f.imageUrl, f.videoUrl, f.duration_minutes, f.datePubli,
           COUNT(i.id) as nb_inscrits
    FROM formation f
    LEFT JOIN session s ON f.id = s.formation_id AND s.active = 1
    LEFT JOIN inscription i ON s.id = i.session_id
    WHERE f.active = 1
    GROUP BY f.id
    ORDER BY nb_inscrits DESC, f.datePubli DESC
    LIMIT 4
");
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
