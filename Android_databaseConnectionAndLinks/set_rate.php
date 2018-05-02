<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['dr_id'])  
		and isset($_POST['p_id'])  
			and isset($_POST['rate_stars'])){

			$db = new DBOperations();
			$result = $db->setDoctorRate(($_POST['dr_id']),($_POST['p_id']),($_POST['rate_stars']));
			if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "Thank You!";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "Some error occurred please try again";
						
					}


				}				
}

echo json_encode($response);
?>