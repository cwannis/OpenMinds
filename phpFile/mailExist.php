<?php
require_once("bdd.php");
$mail = $_GET['email'];
if(!empty($mail)){
    if(mailExists($mail, $bdd)) http_response_code(401);
     else http_response_code(200);

}