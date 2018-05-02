<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['dr_id'])  
		and isset($_POST['p_id'])  
			and isset($_POST['sch_date']) 
				and isset($_POST['sch_time'])){

			$db = new DBOperations();
			$result = $db->reserveAppointment(($_POST['dr_id']),($_POST['p_id']),($_POST['sch_date']),($_POST['sch_time']));
			if($result == 0){//3
						$response['error'] = true; 
						$response['message'] = "you already have an appointment in this day, cancel the previous or select another day";
				}
				else if ($result == 1){
					$response['error'] = true; 
					$response['message'] = "Sorry, time is reserved, select another date or time";
				}
				else if ($result == 2){
					$response['error'] = false; 
					$response['message'] = "appointment is reserved successfully";
				}
					else  {
						$response['error'] = true; 
						$response['message'] = "invalid reservation";
						
					}


				}				
			

}
echo json_encode($response);

?>