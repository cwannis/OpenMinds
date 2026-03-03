<?php
$apiKey = getenv("OPENMINDS_API_KEY");
if (!$apiKey) {
    $apiKey = "testAPIKEY";
}

define("API_KEY", $apiKey);
?>
