<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['app_id'])  
			and isset($_POST['sch_date'])  
				and isset($_POST['sch_time'])
					and isset($_POST['dr_id'])
						and isset($_POST['p_id'])){
					
			$db = new DBOperations();
			$result = $db->appointmentChangeDateTime(($_POST['app_id']),($_POST['sch_date']),($_POST['sch_time']),($_POST['dr_id']), ($_POST['p_id']));
				if($result == 0){
						$response['error'] = true; 
						$response['message'] = "Sorry, time is reserved, select another date or time";
				}
				else if ($result == 1){
					$response['error'] = false; 
					$response['message'] = "Changes are saved successfully";
				}
				else  {
						$response['error'] = true; 
						$response['message'] = "Some error occured, please try again";
						
					}
				}
				}		
			echo json_encode($response);
?>