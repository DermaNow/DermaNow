<?php

require_once 'constants.php';

//this is our upload folder to store the attachment
$upload_path = 'doctor_job_license/';
 
//Getting the server ip
$server_ip = 'localhost';
 
//creating the upload url
$upload_url = 'http://'.$server_ip.'/Android/'.$upload_path;

//response array
$response = array("error"=>"error", "message"=>"message");
 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
 
    //checking the required parameters from the request
    if(isset($_POST['name']) and isset($_FILES['pdf']['name'])){
		//connecting to the database
        $con = mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME) or die('Unable to Connect...');
		 //getting username from the request
        $name = $_POST['name'];
 
        //getting file info from the request
        $fileinfo = pathinfo($_FILES['pdf']['name']);
 
        //getting the file extension
        $extension = $fileinfo['extension'];
 
        //file url to store in the database
        $file_url = $upload_url . getFileName($name) . '.' . $extension;
 
        //file path to upload in the server
        $file_path = $upload_path . getFileName($name). '.'. $extension;
 
        //trying to save the file in the directory
        try{
            //saving the file
            move_uploaded_file($_FILES['pdf']['tmp_name'],$file_path);
            $sql = "INSERT INTO doctor_job_license (dr_id, url, name) VALUES ((SELECT dr_id from doctor WHERE username = '$name'), '$file_url', (SELECT name from doctor WHERE username = '$name'));";
 
            //adding the path and name to database
            if(mysqli_query($con,$sql)){
 
                //filling response array with values
                $response['error'] = false;
            }
                $response['url'] = $file_url;
                $response['name'] = $name;
            //if some error occurred
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        } 
        //closing the connection
        mysqli_close($con);
    }else{
        $response['error']=true;
        $response['message']='Please choose a file';
    }
    
    //displaying the response
    echo json_encode($response);

	}
	
	//get dr_id , name to rename his/her attachment
function getFileName($name){
    $con = mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME) or die('Unable to Connect...');
    $sql = "SELECT dr_id, name FROM doctor WHERE username = '$name'";
   $row = mysqli_fetch_array(mysqli_query($con,$sql));
	$id_name = $row['dr_id'] . ' - ' . $row['name'];
	return $id_name;
	}
 

	
?>