<?php
	//Basic Connection to Derma Now Database
	class DBOperations{
		private $con ;
		private $db ;
		private $db_table_patient = "patient";
		private $db_table_doctor = "doctor";
		function __construct(){
			require_once 'DBConnect.php';
			$db = new DBConnect();
			$this->con = $db->connect();
		}
	//-----------------------------------------------------------------------------------------------------------------------------
	//CREATE DOCTOR METHOD(WALAA)
		function createDoctor ( $dr_ssn , $name , $clinic_name ,$latitude ,$longitude , $major , $degree , $username , $password , $email , $phone){
		if(($this->isUserExistDoctor($username))||($this->isUserExistPatient($username))||($this->isUserExistSecretary($username))){
                return 0; 
            }else{
				//$password = md5($pass);
			$stmt = $this->con->prepare("INSERT INTO `doctor` (`dr_id`, `dr_ssn`, `name`, `clinic_name`, `latitude`, `longitude`, `major`, `degree`, `username`, `password`, `admin_verification`)
			VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, '0');");
			$stmt->bind_param("issssssss", $dr_ssn , $name , $clinic_name ,$latitude ,$longitude , $major , $degree, $username, $password);
			if($stmt->execute()){
				//get the last p_id inseted to be insert to forign tables
				$id = mysqli_insert_id($this->con);
				$forgienKeyQuary1 = $this->con->prepare("INSERT INTO `doctor_email_contact` (`email`, `dr_id`) VALUES (?,(SELECT dr_id from doctor WHERE username= ?) );");
				$forgienKeyQuary1->bind_param("ss",$email, $username);
				if($forgienKeyQuary1->execute()){
				$forgienKeyQuary2 = $this->con->prepare("INSERT INTO `doctor_phone_contact` (`phone`, `dr_id`) VALUES (?,(SELECT dr_id from doctor WHERE username= ?) );");
				$forgienKeyQuary2->bind_param("ss", $phone , $username);
				$forgienKeyQuary2->execute();
				}
				return 1;

			}else{
				return 2;
			}
		}
	}
	//------------------------------------------------------------------------------------------------------------------------------
	//CREATE PATIENT METHOD(WALAA)
		function createPatient( $name , $sex , $marital_status , $b_day , $username , $password ,$email , $phone){
		if(($this->isUserExistDoctor($username))||($this->isUserExistPatient($username))||($this->isUserExistSecretary($username))){
                return 0; 
            }else{
				//$password = md5($pass);
			$stmt = $this->con->prepare("INSERT INTO `patient` (`p_id`, `name`, `sex`, `marital_status`, `b_day`,`username`, `password`) 
			VALUES (NULL, ?, ?, ?, ?, ?, ?);");
			$stmt->bind_param("ssssss", $name , $sex , $marital_status , $b_day , $username, $password);
			if($stmt->execute()){
				//get the last dr_id inseted to be insert to forign tables
				$forgienKeyQuary1 = $this->con->prepare("INSERT INTO `patient_email_contact` (`email`, `p_id`) VALUES (?,(SELECT p_id from patient WHERE username= ?) );");
				$forgienKeyQuary1->bind_param("ss",$email, $username);
				if($forgienKeyQuary1->execute()){
				$forgienKeyQuary2 = $this->con->prepare("INSERT INTO `patient_phone_contact` (`phone`, `p_id`) VALUES (?,(SELECT p_id from patient WHERE username= ?) );");
				$forgienKeyQuary2->bind_param("ss", $phone , $username);
				$forgienKeyQuary2->execute();
				}
				return 1;
			}else{
				return 2;
			}
		}
	}
	//-----------------------------------------------------------------------------------------------------------------------------
	//CREATE SECRETARY METHOD(RAHAF)18/2 
	public function createSecretary($name, $username, $password, $dr_username, $phone , $email){
		if(($this->isUserExistDoctor($username))||($this->isUserExistPatient($username))||($this->isUserExistSecretary($username))){
                return 0; 
            }else{
			$stmt = $this->con->prepare("INSERT INTO `secretary` (`sec_id`, `name`, `username`, `password`, `dr_id`)
			VALUES (NULL, ?, ?, ?, (SELECT dr_id FROM doctor WHERE username = ?));");
			$stmt->bind_param("ssss", $name , $username, $password, $dr_username);
			if($stmt->execute()){
				//get the last dr_id inseted to be insert to forign tables
				$forgienKeyQuary1 = $this->con->prepare("INSERT INTO `secretary_phone_contact` (`sec_id`, `phone`) VALUES ((SELECT sec_id from secretary WHERE username= ?), ? );");
				$forgienKeyQuary1->bind_param("ss",$username, $phone);
				$forgienKeyQuary1->execute();
		if($forgienKeyQuary1->execute()){
				$forgienKeyQuary2 = $this->con->prepare("INSERT INTO `secretary_email_contact` (`sec_id`, `email`) VALUES ((SELECT sec_id from secretary WHERE username= ?), ? );");
				$forgienKeyQuary2->bind_param("ss",$username, $email);
				$forgienKeyQuary2->execute();
				}
				return 1;
			}else{
				return 2;
			}
	}
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//Is Doctor username exist in the Patient table (WALAA)
	 private function isUserExistDoctor($username){
            $stmt = $this->con->prepare("SELECT dr_id FROM doctor WHERE username = ? ");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }
	//------------------------------------------------------------------------------------------------------------------------------
	//Is Patient username exist in the Doctor table (WALAA)
	 private function isUserExistPatient($username){
            $stmt = $this->con->prepare("SELECT p_id FROM patient WHERE username = ? ");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }	
	//------------------------------------------------------------------------------------------------------------------------------
	//Is Secretary username exist in the Secretary table(RAHAF) 18/2
	private function isUserExistSecretary($username){
			//-----------------------------------
            $stmt = $this->con->prepare("SELECT sec_id FROM secretary WHERE username = ? ");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }
	//-----------------------------------------------------------------------------------------------------------------------------
	//18/2/2018 (WALAA) 
	//Login Patient Method
	public function loginPatient($username, $password){
				$stmt = $this->con->prepare("SELECT * FROM patient WHERE username = ? AND password = ? ");
			    $stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
				return $stmt->num_rows > 0;
				}
	//-----------------------------------------------------------------------------------------------------------------------------	
	//18/2/2018 (WALAA)
	//Login Doctor Method 
	public function loginDoctor($username, $password){
				$stmt = $this->con->prepare("SELECT * FROM doctor WHERE username = ? AND password = ? ");
			    $stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
				return $stmt->num_rows > 0;
				}
	//-----------------------------------------------------------------------------------------------------------------------------
	//11/4/2018 (WALAA)
	//Is Doctor Account has been verified By the Admin or not(WALAA)
	 public function isDoctorAccountVerified($username){
            $stmt = $this->con->prepare("SELECT dr_id FROM doctor WHERE username = ? AND admin_verification = 1");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }
	//------------------------------------------------------------------------------------------------------------------------------
	//11/4/2018 (WALAA)
	//Login Doctor Method 
	public function loginDoctorVerified($username, $password){
		if($this->isDoctorAccountVerified($username)){
			return 0 ;
		}else{
				$stmt = $this->con->prepare("SELECT * FROM doctor WHERE username = ? AND password = ? ");
			    $stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
				return $stmt->num_rows > 0;
		}
	}
	//-----------------------------------------------------------------------------------------------------------------------------
	//20/2/2018 (WALAA)
	//Login Secretary Method
	public function loginSecretary($username, $password){
				$stmt = $this->con->prepare("SELECT * FROM secretary WHERE username = ? AND password = ? ");
			    $stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
				return $stmt->num_rows > 0;
				
				}	
	//-----------------------------------------------------------------------------------------------------------------------------
	//18/2/2018 (WALAA) --> Updated on 3/3/2018 (WALAA)
	//GET ALL USER INFORMATION FROM DATABASE EVEN WITH THE FORIGEN KEYS
	//Get Patient By Username Method**
        public function getPatientByUsername($username){
            $stmt = $this->con->prepare("SELECT * FROM patient 
											 INNER JOIN patient_email_contact ON patient.p_id = patient_email_contact.p_id 
											 INNER JOIN patient_phone_contact ON patient.p_id = patient_phone_contact.p_id 
											 WHERE patient.username = ? ");
            $stmt->bind_param("s",$username);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
	//-----------------------------------------------------------------------------------------------------------------------------	
	//18/2/2018 (WALAA)
	//Get Doctor By Username Method**
		 public function getDoctorByUsername($username){
            $stmt = $this->con->prepare("SELECT * FROM doctor 
											 INNER JOIN doctor_email_contact ON doctor.dr_id = doctor_email_contact.dr_id 
											 INNER JOIN doctor_phone_contact ON doctor.dr_id = doctor_phone_contact.dr_id 
											 WHERE doctor.username = ? ");
            $stmt->bind_param("s",$username);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
	//----------------------------------------------------------------------------------------------------------------------------	
	//20/2/2018 (WALAA)
	//Get Secretary By Username Method**
        public function getSecretaryByUsername($username){
            $stmt = $this->con->prepare("SELECT * FROM secretary 
											 INNER JOIN secretary_email_contact ON secretary.sec_id = secretary_email_contact.sec_id 
											 INNER JOIN secretary_phone_contact ON secretary.sec_id = secretary_phone_contact.sec_id 
											 WHERE secretary.username = ? ");
            $stmt->bind_param("s",$username);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
	//-----------------------------------------------------------------------------------------------------------------------------
	//15/3/2018 (WALAA)
	//Get Secretary ALL INFORMATION By Doctor ID Method
        public function getSecretaryByDrId($dr_id){
            $stmt = $this->con->prepare("SELECT * FROM secretary 
											 INNER JOIN secretary_email_contact ON secretary.sec_id = secretary_email_contact.sec_id 
											 INNER JOIN secretary_phone_contact ON secretary.sec_id = secretary_phone_contact.sec_id 
											 WHERE secretary.dr_id = ? ");
            $stmt->bind_param("s",$dr_id);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
	//----------------------------------------------------------------------------------------------------------------------------
	//3/4/2018 (WALAA)
	//Update Profile 
	public function drUpdateProfile($username ,$id, $name, $email, $phone){
		if(!($this->isUserExistDoctor($username))){
			return 0;
		}else {
			$stmt = $this->con->prepare("UPDATE doctor 
									    INNER JOIN doctor_email_contact ON doctor.dr_id = doctor_email_contact.dr_id 
										INNER JOIN doctor_phone_contact ON doctor.dr_id = doctor_phone_contact.dr_id 
										SET doctor.name = ? ,
										    doctor_email_contact.email = ? ,
										    doctor_phone_contact.phone = ? 
										WHERE doctor.dr_id = ? ");
			$stmt -> bind_param("ssss", $name, $email, $phone ,$id);
			if($stmt->execute()){
				return 1;
				}
			else {
				return 2;
			}
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//3/4/2018 (WALAA)
	//Update Profile 
	public function usersUpdateProfile($username ,$id, $name , $email, $phone){
		if($this->isUserExistDoctor($username)){
			$stmt = $this->con->prepare("UPDATE doctor 
									    INNER JOIN doctor_email_contact ON doctor.dr_id = doctor_email_contact.dr_id 
										INNER JOIN doctor_phone_contact ON doctor.dr_id = doctor_phone_contact.dr_id 
										SET doctor.name = ? ,
										    doctor_email_contact.email = ? ,
										    doctor_phone_contact.phone = ? 
										WHERE doctor.dr_id = ?");
			$stmt -> bind_param("ssss", $name, $email, $phone ,$id);
			if($stmt->execute()){
				return 1;
			}else {
				return 2;
				}
		}else if($this->isUserExistPatient($username)){
			$stmt = $this->con->prepare("UPDATE patient 
									    INNER JOIN patient_email_contact ON patient.p_id = patient_email_contact.p_id 
										INNER JOIN patient_phone_contact ON patient.p_id = patient_phone_contact.p_id 
										SET patient.name = ? ,
										    patient_email_contact.email = ? ,
										    patient_phone_contact.phone = ? 
										WHERE patient.p_id = ? ");
			$stmt -> bind_param("ssss", $name, $email, $phone ,$id);
			if($stmt->execute()){
				return 1;
			}else {
				return 2;
				}
		}else if($this->isUserExistSecretary($username)){
			$stmt = $this->con->prepare("UPDATE secretary 
									    INNER JOIN secretary_email_contact ON secretary.sec_id = secretary_email_contact.sec_id 
										INNER JOIN secretary_phone_contact ON secretary.sec_id = secretary_phone_contact.sec_id 
										SET secretary.name = ? ,
										    secretary_email_contact.email = ? ,
										    secretary_phone_contact.phone = ? 
										WHERE secretary.sec_id = ? ");
			$stmt -> bind_param("ssss", $name, $email, $phone ,$id);
			if($stmt->execute()){
				return 1;
			}else {
				return 2;
				}
		}else {
			return 0 ;	
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//RESERVE AN APPOINTMENT(RAHAF) 22/2
	public function reserveAppointment($dr_id, $p_id, $sch_date, $sch_time){
		//------------------------------------------------------------------
		if($this->isScheduleExistInSameDay($p_id, $sch_date)){
			return 0;
	}
		else if($this->isScheduleExist($dr_id,$sch_date, $sch_time)){
			return 1;
		} else {
		$stmt = $this->con->prepare("INSERT INTO `schedule` (`app_id`,`dr_id`,`p_id`,`sch_date`,`sch_time`) 
			VALUES (NULL, ?, ?, ?, ?);");
			$stmt->bind_param("ssss", $dr_id , $p_id , $sch_date , $sch_time);
			if ($stmt->execute()){
				return 2;
			
			}else {
				return -1;
			}
		}			
				
	}
	//-----------------------------------------------------------------------
	//CHECK IF THERE A SCHEDULE WITH SAME DATE/TIME (RAHAF)
	public function isScheduleExist($dr_id, $sch_date, $sch_time){
		$stmt = $this->con->prepare("SELECT * FROM schedule WHERE dr_id = ? 
									AND sch_date = ? AND sch_time = ?;");
		$stmt->bind_param("sss",$dr_id, $sch_date, $sch_time);
		$stmt->execute(); 
        $stmt->store_result(); 
        return $stmt->num_rows > 0; 
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//CHECK IF THERE 2 APPOINTEMENT WITH SAME PATIENT IN ONE DAY (RAHAF)
	public function isScheduleExistInSameDay($p_id, $sch_date){
		$stmt = $this->con->prepare("SELECT * FROM schedule WHERE p_id = ? 
									AND sch_date = ?;");
		$stmt->bind_param("ss",$p_id, $sch_date);
		$stmt->execute(); 
        $stmt->store_result(); 
        return $stmt->num_rows > 0;	
	}
	//----------------------------------------------------------------------------------------------------------------------------
	
	//GET ALL DOCTORS LIST(RAHAF) 18/2
	// get doctors names of selected clinic (Nintendoo) 18/2 & edited by (Alaa) 9/3
	public function getDoctorsNames($clinic){
		$stmt = $this->con->prepare("SELECT dr_id,name FROM doctor WHERE clinic_name = ?;");
		$stmt -> bind_param("s",$clinic);
		//executing the query 
		$stmt->execute();
		$out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array('dr_id'=>$row['dr_id'],'name'=>$row['name']));
				}
				echo json_encode(array('result'=>$result));
			}
	//----------------------------------------------------------------------------------------------------------------------------
    //SET DOCTOR RATE METHOD(ALAA) 11/3
	public function setDoctorRate($dr_id, $p_id, $rate_stars){
		$stmt = $this->con->prepare("INSERT INTO `rate` (`dr_id`,`p_id`,`rate_stars`) 
			VALUES (?, ?, ?);");
			$stmt->bind_param("sss", $dr_id , $p_id , $rate_stars);
			if ($stmt->execute()){
				return 1;
			}else{
				return 2;
			}
		}
	//----------------------------------------------------------------------------------------------------------------------------
    //GET DOCTOR RATE METHOD(ALAA) 11/3
	public function getDoctorRate($id){
		$stmt = $this->con->prepare("SELECT dr_id,rate_stars FROM rate WHERE dr_id = ?;");
		$stmt -> bind_param("s",$id);
		//executing the query 
		$stmt->execute();
		$out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                    'dr_id'=>$row['dr_id'],
					'rate_stars'=>$row['rate_stars']) );	
					}
				echo json_encode(array('result'=>$result));
				}
	//----------------------------------------------------------------------------------------------------------------------------
	//STORE DOCOTOR WORKING HOURS METHOD(RAHAF)
	public function workingHours($username, $time, $shift){
	
		$stmt = $this->con->prepare("INSERT INTO `working_hours` (`dr_id`,`hour`,`shift`)
		VALUES((SELECT dr_id FROM doctor WHERE username = ?),?,?)"); 
		$stmt->bind_param("sss", $username, $time, $shift);
		if($stmt->execute()){
			return 1;	
		}else {
		return 2;
		}
	}
	//----------------------------------------------------------------------------------------------------------------------------
	//CLEAR DOCOR WORKING HOURS METHOD (RAHAF)
	public function clearSchedule($username, $shift){
		$stmt = $this->con->prepare("DELETE FROM `working_hours` WHERE shift = ? AND dr_id = (SELECT dr_id FROM doctor WHERE username = ?)"); 
		$stmt->bind_param("ss", $shift, $username);
		if($stmt->execute()){
			return 1;	
		}else {
			return 2;
			}
			
			}
	//----------------------------------------------------------------------------------------------------------------------------
	//GET ALL DOCTOR RESERVATION METHOD(RAHAF)
	public function getDoctorReservations($dr_id){
		$stmt = $this->con->prepare("SELECT * from patient 
								INNER JOIN schedule ON patient.p_id = schedule.p_id
								INNER JOIN patient_email_contact ON patient.p_id = patient_email_contact.p_id
								WHERE schedule.dr_id = ? 
								ORDER BY schedule.sch_date,schedule.sch_time ASC"); 							
			$stmt->bind_param("s",$dr_id);
            $stmt->execute();
			$out = $stmt->get_result();
            $result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
					'app_id'=>$row['app_id'],
                    'sch_date'=>$row['sch_date'],
					'sch_time'=>$row['sch_time'],
					'p_id'=>$row['p_id'],
					'name'=>$row['name'],
					'email'=>$row['email'],
					'dr_id'=>$row['dr_id'],
					'massage'=>'successful'));				
				}			
				return $result;
}
	//------------------------------------------------------------------------------
	//GET DOCTOR'S AVAILABLE WORKING HOURS METHOD(RAHAF)
	public function getDoctorWorkingHours($dr_id){
	$stmt = $this->con->prepare("SELECT hour, shift FROM working_hours WHERE dr_id = ? 
								ORDER BY hour ASC;");
	$stmt -> bind_param("s",$dr_id);
	$stmt->execute();
	$out = $stmt->get_result();
	$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                    'hour'=>$row['hour'],
					'shift'=>$row['shift'],
					'massage'=>'successful'));
					
				}
				
				return $result;
	
	}
	
	//--------------------------------------------------------------------------------
	//CHANGE APPOINTMENT'S DATE/TIME METHOD(RAHAF)
	public function appointmentChangeDateTime( $app_id, $sch_date, $sch_time, $dr_id, $p_id){
	 if($this->isScheduleExist($dr_id,$sch_date, $sch_time)){
			return 0;
		} else {
	$stmt = $this->con->prepare("UPDATE schedule SET sch_date = ? , sch_time =  ?  WHERE app_id = ? ;");
	$stmt -> bind_param("sss",$sch_date, $sch_time, $app_id);
	if($stmt->execute()){
		return 1;
	}
	else {
		return 2;
	}
		
		}
	}
	//-----------------------------------------------------------------------------
	//CANCEL APPOINTMENT METHOD(RAHAF)
	public function cancelAppointment($app_id){
		$stmt = $this->con->prepare("DELETE FROM schedule WHERE app_id = ?"); 
	$stmt->bind_param("s", $app_id);
	if($stmt->execute()){
		return 1;	
	}
	else {
		return 2;
	}
				
	}
        //----------------------------------------------------------------------------
        //SEARCH BY RATE (ALAA) 11/3
        public function search_rate($stars){
               $stmt = $this->con->prepare("SELECT * FROM full_rate_view WHERE rates = ?;");


          $stmt->bind_param("s",$stars);
          //executing the query 
          $stmt->execute();
          $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                          'name'=>$row['name'],
                                          'clinic_name'=>$row['clinic_name'],
                                          'major'=>$row['major'],
                                          'degree'=>$row['degree'],
                                           'phone'=>$row['phone'],
                                            'rates'=>$row['rates'],
                                          'num_rates'=>$row['num_rates']));	
				}
				echo json_encode(array('result'=>$result));
          }
         //-----------------------------------------------------------------------------
         // SEARCH BY DOCTOR NAME (ALAA) 17/3
         public function search_name($name){
             $stmt = $this->con->prepare("SELECT * FROM full_rate_view WHERE name LIKE CONCAT('%' ,?, '%');");
             $stmt->bind_param("s",$name);
              //executing the query 
                $stmt->execute(); 
                    $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                          'name'=>$row['name'],
                                          'clinic_name'=>$row['clinic_name'],
                                          'major'=>$row['major'],
                                          'degree'=>$row['degree'],
                                           'phone'=>$row['phone'],
                                            'rates'=>$row['rates'],
                                          'num_rates'=>$row['num_rates']));
				}
				echo json_encode(array('result'=>$result));
         }
         //------------------------------------------------------------------------------
         // SEARCH BY CLINIC NAME(ALAA) 18/3
          public function search_clinic($clinic){
             $stmt = $this->con->prepare("SELECT * FROM clinics WHERE clinic_name LIKE CONCAT('%' , ?, '%') ;");
                  $stmt->bind_param("s",$clinic);
                  //executing the query 
                   $stmt->execute();
                          $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                          'clinic_name'=>$row['clinic_name'],
                                          'latitude'=>$row['latitude'],
                                          'longitude'=>$row['longitude']));		
				}
				echo json_encode(array('result'=>$result));
       }
       //-----------------------------------------------------------------------------
       // INSERT CLINIC INFORMATION (ALAA) 18/3
        public function clinic_data($clinic,$dist,$street,$city,$country){
		$stmt = $this->con->prepare("INSERT INTO `clinic` (`name`,`district`,`street`,`city`,`country`) 
			VALUES (?,?,?,?,?);");
			$stmt->bind_param("sssss", $clinic , $dist,$street,$city,$country);
			if ($stmt->execute()){
				return 1;
			}else{
				return 2;
			}				
	}
        //--------------------------------------------------------------------------------
        // SEARCH BY DISTRICT (ALAA) 18/3
        public function search_district($district){
		$stmt = $this->con->prepare("select * from clinic Where district LIKE CONCAT('%' , ?, '%') ;");
			$stmt->bind_param("s",$district);
			//executing the query 
                          $stmt->execute();
                         $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                          'name'=>$row['name'],
                                          'district'=>$row['district'],
                                             'street'=>$row['street'],
                                              'city'=>$row['city'],
                                                'country'=>$row['country']));
				}
				echo json_encode(array('result'=>$result));			
	}
	//---------------------------------------------------------------------------
	//GET ALL PATIENT RESERVATION METHOD(RAHAF)
	public function getPatientReservations($p_id){
		$stmt = $this->con->prepare("SELECT * from patient 
								INNER JOIN schedule ON patient.p_id = schedule.p_id
								INNER JOIN doctor ON doctor.dr_id = schedule.dr_id
								WHERE schedule.p_id = ? 
								ORDER BY schedule.sch_date,schedule.sch_time ASC"); 
								
			$stmt->bind_param("s",$p_id);
            $stmt->execute();
			$out = $stmt->get_result();
            $result = array();
			
				while($row=$out->fetch_assoc()){
					array_push($result,array(
						'app_id'=>$row['app_id'],
						'sch_date'=>$row['sch_date'],
						'sch_time'=>$row['sch_time'],
						'p_id'=>$row['p_id'],
						'dr_id'=>$row['dr_id'],
						'dr_name'=>$row['name'],
						'massage'=>'successful'));
					
				}
				
				return $result;
				}
	//-----------------------------------------------------------------------
        //GET PATIENT APPOINTMENT (ALAA) 30/3
        public function getPatientApp($p_id){
		$stmt = $this->con->prepare("select * from schedule_ratedialog Where p_id = ? ;");
			$stmt->bind_param("s",$p_id);
			//executing the query 
                          $stmt->execute();
                         $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                          'dr_id'=>$row['dr_id'],
                                             'p_id'=>$row['p_id'],
                                              'sch_date'=>$row['sch_date'],
                                                'sch_time'=>$row['sch_time'],
                                                   'dr_name'=>$row['dr_name']));
				}
				echo json_encode(array('result'=>$result));			
	}
       //--------------------------------------------------------------------------------------
       //CHECK DOCTOR RATE METHOD(ALAA) 11/3
	public function checkRate($dr_id,$p_id){
	$stmt = $this->con->prepare("select dr_id ,p_id  from rate Where dr_id = ? AND p_id = ? ;");
			$stmt->bind_param("ss",$dr_id,$p_id);
			//executing the query 
                          $stmt->execute();
                    $stmt->store_result(); 
            $checked = $stmt->num_rows > 0; 
            $result = array();
					array_push($result,array('checked'=>$checked));
                                  echo json_encode(array('result'=>$result));
      }
      //--------------------------------------------------------------------------------------
       // VERIFY DOCTOR METHOD (ALAA) 10/4
        public function verify_doctor($dr_id){
		$stmt = $this->con->prepare("UPDATE doctor set admin_verification = '1' WHERE dr_id = ? ;");
			$stmt->bind_param("s", $dr_id);
			if ($stmt->execute()){
				return 1;
			}else{
				return 2;
			}				
	}
      //-----------------------------------------------------------------------------
       // REJECT DOCTOR METHOD (ALAA) 10/4
        public function reject_doctor($dr_id){
		$stmt = $this->con->prepare("DELETE FROM doctor WHERE dr_id = ? ;");
			$stmt->bind_param("s", $dr_id);
			if ($stmt->execute()){
				return 1;
			}else{
				return 2;
			}				
	}
      //-------------------------------------------------------------------------------
      //GET DOCTOR INFO METHOD(ALAA) 11/3
	public function getAll_unverifiedDoctors(){
	$stmt = $this->con->prepare("SELECT doctor.dr_id,doctor.dr_ssn,doctor.name,doctor.clinic_name,doctor.major,doctor.degree,
                                     doctor_phone_contact.phone,doctor_email_contact.email,doctor_job_license.url AS job_url,
                                      doctor_clinic_license.url AS clinic_url FROM doctor
                                             INNER JOIN doctor_job_license ON doctor.dr_id = doctor_job_license.dr_id
                                             INNER JOIN doctor_clinic_license ON doctor.dr_id = doctor_clinic_license.dr_id
                                             INNER JOIN doctor_phone_contact ON doctor.dr_id = doctor_phone_contact.dr_id
                                             INNER JOIN doctor_email_contact ON doctor.dr_id = doctor_email_contact.dr_id
                                               WHERE doctor.admin_verification = '0' group by dr_id ;");
			//executing the query 
                         $stmt->execute();
                         $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                                   'dr_id'=>$row['dr_id'],
                                                    'dr_ssn'=>$row['dr_ssn'],
                                                    'name'=>$row['name'],
                                                   'clinic_name'=>$row['clinic_name'],
                                                   'major'=>$row['major'],
                                                   'degree'=>$row['degree'],
                                                   'phone'=>$row['phone'],
                                                   'email'=>$row['email'],
                                                  'job_url'=>$row['job_url'],
                                                  'clinic_url'=>$row['clinic_url']));
				}
				echo json_encode(array('result'=>$result));	
}	
              //---------------------------------------------------------------------------------------
              //GET DOCTOR INFO METHOD(ALAA) 11/3
	       public function getAll_verifiedDoctors(){
	       $stmt = $this->con->prepare("SELECT DISTINCT  doctor.dr_id,doctor.dr_ssn,doctor.name,doctor.clinic_name,doctor.major,doctor.degree,
                                     doctor_phone_contact.phone,doctor_email_contact.email,doctor_job_license.url AS job_url,
                                      doctor_clinic_license.url AS clinic_url FROM doctor
                                             INNER JOIN doctor_job_license ON doctor.dr_id = doctor_job_license.dr_id
                                             INNER JOIN doctor_clinic_license ON doctor.dr_id = doctor_clinic_license.dr_id
                                             INNER JOIN doctor_phone_contact ON doctor.dr_id = doctor_phone_contact.dr_id
                                             INNER JOIN doctor_email_contact ON doctor.dr_id = doctor_email_contact.dr_id
                                                    WHERE doctor.admin_verification = '1' group by dr_id ;");
			//executing the query 
                         $stmt->execute();
                         $out = $stmt->get_result();
			$result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
                                                   'dr_id'=>$row['dr_id'],
                                                    'dr_ssn'=>$row['dr_ssn'],
                                                    'name'=>$row['name'],
                                                   'clinic_name'=>$row['clinic_name'],
                                                   'major'=>$row['major'],
                                                   'degree'=>$row['degree'],
                                                   'phone'=>$row['phone'],
                                                   'email'=>$row['email'],
                                                  'job_url'=>$row['job_url'],
                                                   'clinic_url'=>$row['clinic_url']));
				}
				echo json_encode(array('result'=>$result));	
}	
//-----------------------------------------------------------------------------------------------------------------------------
	//ADMIN LOGIN Method 13/4/2018 (ALAA) 
	public function loginAdmin($username, $password){
				$stmt = $this->con->prepare("SELECT * FROM admin WHERE username = ? AND password = ? ");
			    $stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
				return $stmt->num_rows > 0;
				
			}
	//-----------------------------------------------------------------------
        //GET PATIENT OR DOCTOR IMAGE (ISRAA) 14/4
         public function getdisease(){
			 $img_id = '1';
		$stmt = $this->con->prepare("SELECT * FROM user_disease_img WHERE img_id = (SELECT max(img_id) from user_disease_img);");
			//$stmt->bind_param("s",$img_id);
			//executing the query 
                          $stmt->execute();
                         $out = $stmt->get_result();
			    $result = array();
				while($row=$out->fetch_assoc()){
					array_push($result,array(
					                    'img_id'=>$row['img_id'],
                                         'disease_name'=>$row['disease_name'],
                                         'description'=>$row['description'],
                                         'symptoms'=>$row['symptoms'],
                                         'precautions'=>$row['precautions'],
                                         'medication'=>$row['medication'],
										 'severity'=>$row['severity'],
                                         'img_url'=>$row['img_url']));
				}
				return $result;
						
	}
	      //--------------------------------------------------------------------------------------	
	  //Save the image url at the database with incremented id (WALAA) 21/4
		public function saveImageUrl($img_url){
		$stmt = $this->con->prepare("INSERT INTO `user_disease_img` (`img_id`,`img_url`)VALUES(NULL , ?)"); 
		$stmt->bind_param("s", $img_url);
		if($stmt->execute()){
			return 1;	
		}else {
		    return 2;
		}
      }
  //------------------------------------------------------------------------------
  
	}//END OF DBOPERATION CLASS
?>