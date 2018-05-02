<?php
require 'DBOperations.php';
$response = array();

 
 if($_SERVER['REQUEST_METHOD']=='POST'){//1
		$db = new DBOperations();
		if(isset($_POST['dr_id'])){
					$response = $db->getDoctorWorkingHours($_POST['dr_id']);
				
				if (empty($response)) {	
					array_push($response,array(
					'error'=>true,
					'massage'=>'No time available for this doctor'));
					}
					
 }
 }
		
		
		echo json_encode(array('response'=>$response));




?>