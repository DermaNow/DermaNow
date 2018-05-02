<?php 
//18/2/2018 (WALAA)
//LOGIN IN DIFFRENT TABLES

require 'DBOperations.php';

$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	//Send dr id
    if(isset($_POST['dr_id'])){
		//create new object of DBOperations
        $db = new DBOperations(); 
		//check the log in in PATIENT Table
			//GET ALL SECRETARY DATA FROM DATABASE
            $user = $db->getSecretaryByDrId($_POST['dr_id']);
            $response['error'] = false; 
            $response['sec_id'] = $user['sec_id'];
            $response['username'] = $user['username'];
			$response['name'] = $user['name'];
			$response['email'] = $user['email']; //--> need for forigrn key
			$response['phone'] = $user['phone']; //--> need for forigrn key
			if( $response['sec_id']==null){
				 $response['error'] = true; 
				 $response['message'] = "Please fill your secretary information to create a your Secretary Account";
			}
    }else{
        $response['error'] = true; 
        $response['message'] = "There's no doctor connected with this Secretary account";
    }
}
echo json_encode($response);
 ?>