<?php
require_once "bdd.php";
header('Content-Type: application/json');
if(isset($_GET['id'])) $id = $_GET['id'];

if(!empty($id)){
    echo getBadgesForUser($id, $bdd);
}