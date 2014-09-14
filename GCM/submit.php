<!DOCTYPE html>
<html>
<head>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
<!--  Mobile viewport optimized: j.mp/bplateviewport -->
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="viewport" content="target-densitydpi=device-dpi" />
<script type="text/javascript">
    window.scrollTo(0, window.innerHeight);
</script>
<title>Send GCM</title>
</head>
<body>

<?php
require_once 'db_functions.php';
$db = new DB_Functions();
$users = $db->getAllUsers();
if ($users != false) {
    $no_of_rows = mysql_num_rows($users);
    if ($no_of_rows > 0) {
        $recipients = $users;
        $message = $_POST["message"];
        $sender = "Gray Routes";
        if ($_POST["email"] != "") {
            if (isset($_POST["email"])) {
                $recipients = $db->getUserByEmail($_POST["email"]);
                $sender = "Gray Routes";
            }
        }
if ($_POST["username"] != "") {
            if (isset($_POST["username"])) {
                $username = $_POST["username"]; 
                $recipients = $db->getUserByUserName($username);
                $sender = "Gray Routes personal message for ";
             }
        }
        else
       {
         $username = "";
       }
        if (isset($_POST["sender"])) {
            $sender = $_POST["sender"];
        }
        if ($recipients != false) {
            while ($row = mysql_fetch_array($recipients)) {
                $regId = $row["gcm_regid"];
            
                require_once 'GCM.php';
            
                $gcm = new GCM();
        
                $registration_ids = array($regId);
                $content = array("sender" => $sender,"message" => $message,"user" => $username);
        
                $result = $gcm->send_notification($registration_ids, $content);
        
                echo $result;
            }
        }
    }
}
?>
</body>
</html>		