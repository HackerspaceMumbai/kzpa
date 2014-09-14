<?php

/**
 * Description of GCM
 *
 * @author Ravi Tamada, Lounge Katt
 */
class GCM {

    //put your code here
    // constructor
    function __construct() {
        
    }

    /**
     * Sending Push Notification
     */
    public function send_notification($registration_ids, $message) {
        // include config
        require_once 'config.php';

        // Set POST variables
        $url = 'https://android.googleapis.com/gcm/send';

        $fields = array(
            'registration_ids' => $registration_ids,
            'data' => $message
        );

        $headers = array(
            'Authorization: key='.GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        } else {
            $jsonres = json_decode($result);
            if(!empty($jsonres->results)){

                require_once 'db_functions.php';
                $db = new DB_Functions();

                for($i=0; $i<count($jsonres->results);$i++){
                    if(isset($jsonres->results[$i]->registration_id)){
                        $new = $db->updateUser($registration_ids[$i], $jsonres->results[$i]->registration_id);
                    } else if(isset($jsonres->results[$i]->error)){
                        if($jsonres->results[$i]->error == "NotRegistered"){
                            $res = $db->deleteUser($registration_ids[$i]);
                        }
                    }
                }
                echo $result."\n";
                $canonical_ids_count = $jsonres->canonical_ids;
                if($canonical_ids_count){
                    echo count($canonical_ids_count)." registrations updated\n";
                }
            }
        }

        // Close connection
        curl_close($ch);
    }

}

?>