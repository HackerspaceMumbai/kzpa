<?php

// response json
$json = array();

if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["imei"])){
    $email = $_POST["email"];
    $imei = $_POST["imei"];
    $name = $_POST["name"];
    $gcm_regid = $_POST["regId"]; // GCM Registration ID
    // Store user details in db
    require_once 'db_functions.php';
    require_once 'GCM.php';

    $db = new DB_Functions();
    $gcm = new GCM();

    $upz = $db->getImeiByEmail($email);
    if($upz == false)
      echo "false";
    else
     echo $upz;
      if($upz == $imei){
          $upzresult = $db->updateUserByEmail($email, $gcm_regid);
           echo $upzresult;
       }
    $res = $db->storeUser($email, $imei, $name, $gcm_regid);

    $registration_ids = array($gcm_regid);
    $message = array("registration" => "Registration successful");

    $result = $gcm->send_notification($registration_ids, $message);
   echo $result;
}
else
echo "Get request successful";
 ?>			