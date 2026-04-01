<?php
require_once("bdd.php");
header('Content-Type: application/json');

$mail = isset($_GET['email']) ? trim($_GET['email']) : "";

if (empty($mail)) {
    http_response_code(400);
    echo json_encode(["erreur" => "email requis"]);
    exit;
}

if (mailExists($mail, $bdd)) {
    http_response_code(409);
    echo json_encode(["exists" => true]);
} else {
    http_response_code(200);
    echo json_encode(["exists" => false]);
}
?>
