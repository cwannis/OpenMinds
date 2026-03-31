<?php
require_once("bdd.php");
header('Content-Type: application/json');

$thematique = isset($_GET['thematique']) ? trim($_GET['thematique']) : "";
$search = isset($_GET['search']) ? trim($_GET['search']) : "";

if (!empty($thematique)) {
    $stmt = $bdd->prepare("SELECT id, titre, description, thematique, type, imageUrl, videoUrl, duration_minutes, datePubli FROM formation WHERE thematique = ? AND active = 1 ORDER BY datePubli DESC");
    $stmt->execute([$thematique]);
} elseif (!empty($search)) {
    $term = "%" . $search . "%";
    $stmt = $bdd->prepare("SELECT id, titre, description, thematique, type, imageUrl, videoUrl, duration_minutes, datePubli FROM formation WHERE (titre LIKE ? OR description LIKE ? OR thematique LIKE ?) AND active = 1 ORDER BY datePubli DESC");
    $stmt->execute([$term, $term, $term]);
} else {
    $stmt = $bdd->query("SELECT id, titre, description, thematique, type, imageUrl, videoUrl, duration_minutes, datePubli FROM formation WHERE active = 1 ORDER BY datePubli DESC");
}

echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
?>
