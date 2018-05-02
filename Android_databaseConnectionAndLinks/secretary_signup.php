<?php

//add this file in Android folder (xampp>hdocs>Android)
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");
 
 if($_SERVER['REQUEST_METHOD']=='POST'){//1
	if(isset($_POST['name'])  
		and isset($_POST['username'])
			and isset($_POST['password'])
					and isset($_POST['dr_username'])
						and isset($_POST['phone'])
							and isset($_POST['email'])){
				
		 $db = new DBOperations();
		 $result =$db->createSecretary(($_POST['name']),($_POST['username']),($_POST['password']), ($_POST['dr_username']),($_POST['phone']),($_POST['email']));
			if($result == 1){//3
            $response['error'] = false; 
            $response['message'] = "Secretary Account is created successfully";
        }else if($result == 2){//4
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";          
        }else if ($result == 0){//5
             $response['error'] = true; 
			 $response['message'] = "It seems Secretary is already registered, please choose a different username";                    
      			
 }
 }else{//6
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
  
 }
 }else{//7
    $response['error'] = true; 
    $response['message'] = "Invalid Request";
} 
echo json_encode($response);
 
 
 ?>