<?php

require 'DBOperations.php';
$response = array();

 
 if($_SERVER['REQUEST_METHOD']=='POST'){//1
		$db = new DBOperations();
		if(isset($_POST['dr_id'])){
					$response = $db->getDoctorReservations($_POST['dr_id']);
				
				if (empty($response)) {	
					array_push($response,array(
					'error'=>true,
					'massage'=>'No reserved appointments'));
					}
					
 }
 }
		
		
		echo json_encode(array('response'=>$response));
		//echo json_encode($response);
		
		
		
		
		
		
		/*"select schedule.date, schedule.time, patient.name
						from patient inner join schedule
						on patient.p_id = schedule.p_id
						where schedule.dr_id = $dr_id" */
		
		
		
	
?>