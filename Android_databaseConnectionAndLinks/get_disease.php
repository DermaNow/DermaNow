<?php

require 'DBOperations.php';
$result = array();

 
 if($_SERVER['REQUEST_METHOD']=='GET'){//1
		$db = new DBOperations();
		//if(isset($_POST['img_id'])){
				$result = $db->getdisease();
				
				if (empty($result)) {	
					array_push($result,array(
					'error'=>true,
					'massage'=>'No result'));
					}
					
// }
 } 
			
		echo json_encode(array('result'=>$result));		
	
?>