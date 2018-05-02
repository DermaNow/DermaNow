<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['district'])){

			$db = new DBOperations();
			$result = $db->search_district(($_POST['district']));
			if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "successfull";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "Some error occurred please try again";
						
					}


				}				
}