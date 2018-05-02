<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
		if(isset($_POST['clinic'])  
		and isset($_POST['dist'])
               and isset($_POST['street'])
               and isset($_POST['city'])
               and isset($_POST['country'])){

			$db = new DBOperations();
			$result = $db->clinic_data(($_POST['clinic']),($_POST['dist']),($_POST['street']),
                                   ($_POST['city']),($_POST['country']));
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

echo json_encode($response);
?>