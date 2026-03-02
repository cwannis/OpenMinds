<?php
require_once 'bdd.php';
$mail = htmlspecialchars($_GET['email']);
$newpsw = sha1(htmlspecialchars($_GET['newpsw']));
if(!empty($mail) && !empty($newpsw)){
    $update = $bdd->prepare("UPDATE user SET password = ? WHERE email = ?");
    $update->execute(array($newpsw, $mail));
    echo 'mdp change avec brio';
}