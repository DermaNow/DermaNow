<?php 
//18/2/2018 (WALAA)
//Modified
//11/4/2018 (WALAA)
//LOGIN IN DIFFRENT TABLES

require 'DBOperations.php';

$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	//Send Username and Password
    if(isset($_POST['username']) and isset($_POST['password'])){
		//create new object of DBOperations
        $db = new DBOperations(); 
		//check the log in in PATIENT Table
        if($db->loginPatient($_POST['username'], $_POST['password'])){
			//GET ALL PATIENT DATA FROM DATABASE
            $user = $db->getPatientByUsername($_POST['username']);
            $response['error'] = false; 
			$response['TableName'] = 'PATIENT'; 
            $response['p_id'] = $user['p_id'];
            $response['username'] = $user['username'];
			$response['name'] = $user['name'];
			$response['sex'] = $user['sex'];
			$response['marital_status'] = $user['marital_status'];
			$response['email'] = $user['email']; //--> need for forigrn key
			$response['phone'] = $user['phone']; //--> need for forigrn key
			//-----------------------------------------------------------------------------------
		//check the log in in DOCTOR Table
        }else if($db->loginDoctorVerified($_POST['username'], $_POST['password'])){
			//GET ALL DOCTOR DATA FROM DATABASE
            $user = $db->getDoctorByUsername($_POST['username']);
            $response['error'] = false; 
			$response['TableName'] = 'DOCTOR';
            $response['dr_id'] = $user['dr_id'];
            $response['username'] = $user['username'];  
			$response['name'] = $user['name'];	
			$response['clinic_name'] = $user['clinic_name'];	
			$response['major'] = $user['major'];
			$response['degree'] = $user['degree'];
			$response['email'] = $user['email']; //--> need for forigrn key
			$response['phone'] = $user['phone'];
			$response['admin_verification'] = $user['admin_verification'];
			//--> need for forigrn key
			//-----------------------------------------------------------------------------------
        }else if($db->loginSecretary($_POST['username'], $_POST['password'])){
			//GET ALL SECRETARY DATA FROM DATABASE
            $user = $db->getSecretaryByUsername($_POST['username']);
            $response['error'] = false; 
			$response['TableName'] = 'SECRETARY'; 
            $response['sec_id'] = $user['sec_id'];
            $response['username'] = $user['username'];
			$response['name'] = $user['name'];
			$response['email'] = $user['email']; //--> need for forigrn key
			$response['phone'] = $user['phone']; //--> need for forigrn key
			$response['dr_id'] = $user['dr_id'];
		 }else if($db->loginAdmin($_POST['username'], $_POST['password'])){
            $response['error'] = false; 
			$response['TableName'] = 'ADMIN'; 
        }
                     else if($db->isDoctorAccountVerified($_POST['username'])){
			//If the doctor account is not varified it will return the error message 
            $response['error'] = true; 
            $response['message'] = "Sorry , Your account has not been varified yet by the admin , we still working on your request ";       
        }else{
            $response['error'] = true; 
            $response['message'] = "Invalid username or password";          
        }
    }else{
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
}
 
echo json_encode($response);
 ?>   