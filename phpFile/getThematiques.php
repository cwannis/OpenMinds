<?php
require_once("bdd.php");
header('Content-Type: application/json');

$thematiques = $bdd->query("SELECT DISTINCT thematique FROM formation WHERE active = 1 ORDER BY thematique");
echo json_encode($thematiques->fetchAll(PDO::FETCH_ASSOC));
?>
