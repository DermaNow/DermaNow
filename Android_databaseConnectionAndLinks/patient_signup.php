<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
	if(isset($_POST['name'])  
		and isset($_POST['sex'])  
			and isset($_POST['marital_status']) 
				and isset($_POST['b_day']) 
					and isset($_POST['username']) 
						and isset($_POST['password'])
							and isset($_POST['email'])
								and isset($_POST['phone'])) {
		$db = new DBOperations();
		 $result =$db->createPatient(($_POST['name']),($_POST['sex']),($_POST['marital_status']),
		 ($_POST['b_day']),($_POST['username']),($_POST['password']),($_POST['email']),($_POST['phone'])); 					 
			 if($result == 1){//3
            $response['error'] = false; 
            $response['message'] = "User registered successfully";
        }else if($result == 2){//4
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";          
        }else if ($result == 0){//5
             $response['error'] = true; 
			 $response['message'] = "It seems you are already registered, please choose a different username";                    
        }
    }else{//6
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
}else{//7
    $response['error'] = true; 
    $response['message'] = "Invalid Request";
} 
echo json_encode($response);
?>