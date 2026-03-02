<?php
require 'PHPMailer/Exception.php';
require 'PHPMailer/PHPMailer.php';
require 'PHPMailer/SMTP.php';
require_once 'bdd.php';
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

$code = htmlspecialchars($_GET['code']);
$mailTo = htmlspecialchars($_GET['mailTo']);

if(mailExists($mailTo, $bdd)) {
    try {
        $mail = new PHPMailer();
        $mail->isSMTP();
        $mail->Host = 'sandbox.smtp.mailtrap.io';
        $mail->SMTPAuth = true;
        $mail->Port = 2525;
        $mail->Username = 'daa7e5c9983ec2';
        $mail->Password = '94013ef61aa8d0';
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;

        // Destinataires
        $mail->setFrom('from@example.com', 'OpenMinds Admin');
        $mail->addAddress($mailTo, 'WANNIS');

        // Contenu
        $mail->isHTML(true);
        $mail->Subject = 'Bienvenue sur OpenMinds';
        $mail->Body = '
    <h1>Mot de passe oublie</h1>
    
    <h2>votre code de recuperation est : ' . $code . ' </h2>
    ';

        $mail->send();
        echo 'Message envoyé !';
    } catch (Exception $e) {
        echo "Erreur lors de l'envoi : {$mail->ErrorInfo}";
    }
}
?>