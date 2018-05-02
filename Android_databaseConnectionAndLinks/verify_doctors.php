<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['dr_id'])){

			$db = new DBOperations();
			$result = $db->verify_doctor(($_POST['dr_id']));
			if($result == 1){
						$response['error'] = false; 
						$response['message'] = "Doctor account is verified successfully";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "Some error occurred please try again";
						
					}
				}				
          }
echo json_encode($response);