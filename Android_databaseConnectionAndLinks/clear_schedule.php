<?php

require 'DBOperations.php';
$response = array("error"=>"error", "message"=>"message");


if($_SERVER['REQUEST_METHOD']=='POST'){//1
		$db = new DBOperations();
		if(isset($_POST['shift'])
			and isset($_POST['username'])){ 
			$result1 = $db->clearSchedule(($_POST['username']),($_POST['shift']));
			if($result1 == 1){//3
            $response['error'] = false; 
            $response['message'] = "deleted successfully";
	}
	else {
		$response['error'] = true; 
        $response['message'] = "Some error occurred please try again";          
       
	}
			}else{
				$response['error'] = true; 
        $response['message'] = "Some error occurred please try again";
			}
			}
	
echo json_encode($response);

?>