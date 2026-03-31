<?php
require 'PHPMailer/Exception.php';
require 'PHPMailer/PHPMailer.php';
require 'PHPMailer/SMTP.php';
require_once 'bdd.php';
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

$code = isset($_GET['code']) ? htmlspecialchars($_GET['code']) : "";
$mailTo = isset($_GET['mailTo']) ? htmlspecialchars($_GET['mailTo']) : "";

if (mailExists($mailTo, $bdd)) {
    try {
        $mail = new PHPMailer();
        $mail->isSMTP();
        $mail->Host = getenv('SMTP_HOST') ?: 'sandbox.smtp.mailtrap.io';
        $mail->SMTPAuth = true;
        $mail->Port = getenv('SMTP_PORT') ?: 2525;
        $mail->Username = getenv('SMTP_USER') ?: 'daa7e5c9983ec2';
        $mail->Password = getenv('SMTP_PASS') ?: '94013ef61aa8d0';
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;

        $mail->setFrom('from@example.com', 'OpenMinds');
        $mail->addAddress($mailTo);

        $mail->isHTML(true);
        $mail->Subject = 'Mot de passe oublie - OpenMinds';
        $mail->Body = '<h1>Mot de passe oublie</h1><h2>Votre code de recuperation est : ' . $code . '</h2>';

        $mail->send();
        echo json_encode(["message" => "Code envoye"]);
    } catch (Exception $e) {
        http_response_code(500);
        echo json_encode(["erreur" => "Erreur envoi: {$mail->ErrorInfo}"]);
    }
} else {
    http_response_code(404);
    echo json_encode(["erreur" => "Email introuvable"]);
}
?>
