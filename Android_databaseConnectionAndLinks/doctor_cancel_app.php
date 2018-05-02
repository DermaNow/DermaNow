<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

 if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['app_id'])){
					
			$db = new DBOperations();
			$result = $db->cancelAppointment(($_POST['app_id']));
			if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "Appointment is canceled successfully";
				}
				else if ($result == 2){
					$response['error'] = false; 
					$response['message'] = "some errors occurs, try again later";
				}
					
										
				}
}
							
			echo json_encode($response);

?>