<?php
require 'DBOperations.php';
 $response = array("error"=>"error", "message"=>"message");

if($_SERVER['REQUEST_METHOD']=='POST'){//1
	if( isset($_POST['dr_ssn']) 
			and isset($_POST['name'])  
				and isset($_POST['clinic_name']) 
                                      and isset($_POST['latitude'])
                                           and isset($_POST['longitude'])
							and isset($_POST['major']) 
								and isset($_POST['degree']) 
									and isset($_POST['username']) 
										and isset($_POST['password'])
											and isset($_POST['email'])
												and isset($_POST['phone'])){
		$db = new DBOperations();
		 $result =$db->createDoctor(($_POST['dr_ssn']),($_POST['name']),($_POST['clinic_name']),
		 ($_POST['latitude']),($_POST['longitude']),($_POST['major']),($_POST['degree']),($_POST['username']),($_POST['password']),($_POST['email']),($_POST['phone'])); 					 
			 if($result == 1){//3
            $response['error'] = false;  
            $response['message'] = "User registered successfully , Wait for our admin verification we will contact you soon";
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