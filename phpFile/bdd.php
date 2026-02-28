<?php
$bdd = new PDO("mysql:dbname=openminds;host=127.0.0.1", 'root', '');

function userExists($mail, $bdd)
{
    $userexist = $bdd->prepare("SELECT * FROM user WHERE email = ?");
    $userexist->execute(array($mail));
    return $userexist->rowCount() > 0;
}
?>