<?php
require_once("bdd.php");
$name = htmlspecialchars($_GET["name"]);
$mail = htmlspecialchars($_GET["mail"]);
$password = sha1(htmlspecialchars($_GET["password"]));
$orga = htmlspecialchars($_GET["organization"]);

if(!empty($name) && !empty($mail) && !empty($password) && !empty($orga)){
    if(!mailExists($mail, $bdd)) {
        $insertUser = $bdd->prepare("INSERT INTO user (name, email, password, organization) VALUES (?, ?, ?, ?)");
        $insertUser->execute(array($name, $mail, $password, $orga));
        http_response_code(200);
    } else
    {
        http_response_code(401);

    }
}