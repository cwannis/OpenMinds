<?php
require_once("bdd.php");
header('Content-Type: application/json');

$form = $bdd->query("SELECT id, titre, description, UNIX_TIMESTAMP(datePubli) * 1000 as datePubli, imageUrl FROM formation ORDER BY datePubli DESC");
echo json_encode($form->fetchAll(PDO::FETCH_ASSOC));
