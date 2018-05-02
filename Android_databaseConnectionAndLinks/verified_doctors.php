<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='GET'){//1
		
			$db = new DBOperations();
			$result = $db->getAll_verifiedDoctors();
			if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "successfull";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "Some error occurred please try again";
						
					}


							
}