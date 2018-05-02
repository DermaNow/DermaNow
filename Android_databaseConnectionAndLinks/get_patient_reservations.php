<?php

require 'DBOperations.php';
$response = array();

 
 if($_SERVER['REQUEST_METHOD']=='POST'){//1
		$db = new DBOperations();
		if(isset($_POST['p_id'])){
					$response = $db->getPatientReservations($_POST['p_id']);
				
				if (empty($response)) {	
					array_push($response,array(
					'error'=>true,
					'massage'=>'No reserved appointments'));
					}
					
 }
 } 
		
		
		echo json_encode(array('response'=>$response));
		
		
		
		
	
?>