<?php
require_once("bdd.php");
header('Content-Type: application/json');
$mail = $_GET['email'];
$mdp = sha1($_GET['password']);
if(isset($_GET['id'])) $id = $_GET['id'];

if(!empty($mail) && !empty($mdp)){
    if(userExistsPassword($mail, $bdd, $mdp))
    {
        $user = $bdd->prepare("SELECT id, name, email, organization FROM user WHERE email = ?");
        $user->execute(array($mail));
        echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
    } else
    {
        http_response_code(401);
        echo json_encode(["erreur" => "utilisateur inexistant"]);
    }
}elseif (!empty($id))
{
    //TODO revoye les donne user avec l'id
}
?>