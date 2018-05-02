<?php 
//3/4/2018 (WALAA)
//Chage all users profile information depending on its usertype
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
	if(isset($_POST['username'])  
		and isset($_POST['id'])
			and isset($_POST['name'])
					and isset($_POST['email'])
						and isset($_POST['phone'])){
			$db = new DBOperations();
			$result = $db->usersUpdateProfile(($_POST['username']),($_POST['id']),($_POST['name']),($_POST['email']),($_POST['phone']));
			if($result == 1){//3
						$response['error'] = false; 
						$response['message'] = "Your Profile Updated Successfully";
				}
				else if ($result == 2){
					$response['error'] = true; 
					$response['message'] = "some errors occurs, please try again later";
				}
				
				else if ($result == 0 ){
					$response['error'] = true; 
					$response['message'] = "There is a wrong with username";	
				}
								
					
				}
}
				
			
			echo json_encode($response);

?>