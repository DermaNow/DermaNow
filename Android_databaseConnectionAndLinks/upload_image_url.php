<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
	if( isset($_POST['img_url'])) {
		$db = new DBOperations();
		 $result =$db->saveImageUrl($_POST['img_url']);					 
			 if($result == 1){//3
            $response['error'] = false;  
            $response['message'] = "Uploaded url successfully";
        }else if($result == 2){//4
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";   
	}}
	}else{
		$response['error'] = true; 
            $response['message'] = "Invalid Request";   
		}		
echo json_encode($response);

?>