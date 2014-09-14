<?php

// response json
$json = array();

if (isset($_POST["FN"]) && isset($_POST["LN"]) && isset($_POST["UN"]) && isset($_POST["EMAIL"]) && isset($_POST["PASS"])){
    $email = $_POST["EMAIL"];
    $pass = $_POST["PASS"];
    $firstname = $_POST["FN"];
    $lastname = $_POST["LN"]; 
    $username = $_POST["UN"]; 
    
    // Store user details in db
    require_once 'db_functions.php';
    require_once 'GCM.php';

    $db = new DB_Functions();
    $gcm = new GCM();

    $res = $db->storeFirstUser($firstname, $lastname, $username, $email, $pass);

    echo $res;
}
else
echo "Get request successful";
 ?>			