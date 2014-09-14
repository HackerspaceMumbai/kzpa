<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gray Routes GCM</title>
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">

    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
<div class = "container">
<center>
        <?php
        require_once 'db_functions.php';
        $db = new DB_Functions();
        $users = $db->getAllUsers();
        $email= $db->getAllUsersEmail();
        $name= $db->getAllUsers();
        
        if ($users != false)
            $no_of_users = mysql_num_rows($users);
        else
            $no_of_users = 0;
        ?>
            <h1>Devices Registered: <?php echo $no_of_users; ?></h1>
            <hr/>
            <?php if ($no_of_users > 0) { ?>
            <br />

<form role="form" name="broadcast" id="broadcast" action="submit.php" method="POST" data-ajax="false">
  <div class="form-group">
 <select name="email" id = "email" class = "btn btn-primary"> 
 <option selected value="">Select User Email (Leave blank to broadcast)</option>
	  
<?php while($row1 = mysql_fetch_array($email)) {
   echo "<option>" . $row1['email'] . "</option>"; } ?>
</select>
  </div>

<div class="form-group">
 <select name = "username" id = "username" class = "btn btn-primary"> 
 <option selected value="">Select Username (Optional)</option>
	  
<?php while($row2 = mysql_fetch_array($name)) {
   echo "<option>" . $row2['name'] . "</option>"; } ?>
</select>
</div>
 
  <div class="form-group">
    <label for="message">Message</label>
    <input name = "message" type="text" id="message" class="form-control" placeholder="Enter Message (Leave Blank to Tickle)">
  </div>
  <button type="submit" name="submit" class="btn btn-primary">Submit</button>
</form>
            <?php } else { ?> 
                No Users Registered Yet!
            <?php } ?>
</center>
</div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
<body>
</html>					