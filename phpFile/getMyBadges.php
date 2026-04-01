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
    SELECT b.id, b.titre, b.description, b.imageUrl, b.thematique, a.dateObtention
    FROM abadge a
    INNER JOIN badge b ON a.idBadge = b.id
    WHERE a.idUser = ?
    ORDER BY a.dateObtention DESC
");
$stmt->execute([$userId]);
echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
