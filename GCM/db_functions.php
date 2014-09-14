<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($email, $imei, $name, $gcm_regid) {
        // insert user into database
        $result = mysql_query("INSERT INTO gcm_users(email, gcm_regid, imei, name, created_at) VALUES('$email', '$gcm_regid', '$imei', '$name', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM gcm_users WHERE id = $id") or die(mysql_error());
            // return user details
            if (mysql_num_rows($result) > 0) {
                return mysql_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

/**
     * Storing new user
     * returns user details
     */
    public function storeFirstUser($fn, $ln, $un, $email, $pass) {
        // insert user into database
        $result = mysql_query("INSERT INTO users(fname, lname, uname, email, pass) VALUES('$fn', '$ln', '$un', '$email', '$pass')");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function deleteUser($gcm_regid) {
        // remove user from database
        $result = mysql_query("DELETE FROM gcm_users WHERE gcm_regid = '$gcm_regid'");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function updateUser($old_regid, $gcm_regid) {
        // update user in database
        $result = mysql_query("UPDATE gcm_users SET gcm_regid=$gcm_regid WHERE gcm_regid = '$old_regid'");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
    public function getImeiByEmail($email) {
        $result = mysql_query("SELECT imei FROM gcm_users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        echo $no_of_rows;
        if ($no_of_rows > 0) {
            return $result;
        }  else {
            return false;
        }
    }


    public function updateUserByEmail($email, $gcm_regid) {
        // update user in database
        $result = mysql_query("UPDATE gcm_users SET gcm_regid=$gcm_regid WHERE email = '$email'");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get user by email
     */
    public function getUserByEmail($email) {
        $result = mysql_query("SELECT gcm_regid FROM gcm_users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return $result;
        } else {
            // user not existed
            return false;
        }
    }
    
    public function getUserByUserName($username) {
        $result = mysql_query("SELECT gcm_regid FROM gcm_users WHERE name = '$username'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return $result;
        } else {
            // user not existed
            return false;
        }
    }
    
    public function getUserByGCM($gcm_regid) {
        $result = mysql_query("SELECT * FROM gcm_users WHERE gcm_regid = '$gcm_regid' LIMIT 1");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $row = mysql_fetch_array($result);
            return $row['id'];
        } else {
            // user not existed
            return false;
        }
    }
    
    public function getUserById($id) {
        $result = mysql_query("SELECT * FROM gcm_users WHERE id = '$id' LIMIT 1");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $row = mysql_fetch_array($result);
            return $row['gcm_regid'] or die();
        } else {
            // user not existed
            return false;
        }
    }

    public function getAllUsersEmail() {
        $result = mysql_query("SELECT DISTINCT(email) FROM gcm_users");
        return $result;
    }
    /**
     * Getting all users
     */
    public function getAllUsers() {
        $result = mysql_query("SELECT * FROM gcm_users");
        return $result;
    }

}

?>