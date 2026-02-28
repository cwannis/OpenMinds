<?php
require_once("bdd.php");
$mail = $_GET['email'];
$mdp = sha1($_GET['password']);
if(!empty($mail)){
    if(userExistsPassword($mail, $bdd, $mdp))
    {
        $user = $bdd->prepare("SELECT id, name, email, organization FROM user WHERE email = ?");
        $user->execute(array($mail));
        echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
    } else
    {
        echo json_encode("utilisateur inexistant");
    }
}
?>