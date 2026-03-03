<?php
require_once("bdd.php");
header('Content-Type: application/json');
$identifier = "";
$mdp = "";
$id = "";
if(isset($_GET['email'])) $identifier = $_GET['email'];
if(isset($_GET['password'])) $mdp = sha1($_GET['password']);
if(isset($_GET['id'])) $id = $_GET['id'];

if(!empty($identifier) && !empty($mdp)){
    if(userExistsPassword($identifier, $bdd, $mdp))
    {
        $user = $bdd->prepare("SELECT id, name, email, organization, ppLink FROM user WHERE email = ? OR name = ? LIMIT 1");
        $user->execute(array($identifier, $identifier));
        echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
    } else
    {
        http_response_code(401);
        echo json_encode(["erreur" => "utilisateur inexistant"]);
    }
}elseif (!empty($id))
{
    $user = $bdd->prepare("SELECT id, name, email, organization, ppLink FROM user WHERE id = ?");
    $user->execute(array($id));
    echo json_encode($user->fetchAll(PDO::FETCH_ASSOC));
}
?>
