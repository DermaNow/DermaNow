<?php

require 'DBOperations.php';
$response = array("error"=>"error", "message"=>"message");

	if($_SERVER['REQUEST_METHOD']=='POST'){//1
		$db = new DBOperations();
			if(isset($_POST['username'])  
					and isset($_POST['hour'])
						and isset($_POST['shift'])){
				
					$result = $db->workingHours(($_POST['username']),($_POST['hour']),($_POST['shift']));
					if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "schedule is created successfully";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "invalid schedule";
						
					}
					
				
				}else{
						$response['error'] = false; 
						$response['message'] = "schedule is empty";
					}
					
				}
echo json_encode($response);		

?>