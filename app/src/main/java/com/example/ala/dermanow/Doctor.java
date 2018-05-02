package com.example.ala.dermanow;

/**
 * Created by Ala on 13/04/18.
 */


public class Doctor {
    String ID, Name, SSN, clinicName, Email, Phone, Major, Degree, admin_verification, Job_url, Clinic_url;

    public Doctor() {

    }

    public Doctor(String ID, String Name, String SSN, String clinicName, String Email, String Phone, String Major, String Degree,
                  String admin_verification, String Job_url, String Clinic_url) {
        this.ID = ID;
        this.Name = Name;
        this.SSN = SSN;
        this.clinicName = clinicName;
        this.Email = Email;
        this.Phone = Phone;
        this.Major = Major;
        this.Degree = Degree;
        this.admin_verification = admin_verification;
        this.Job_url = Job_url;
        this.Clinic_url = Clinic_url;


    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getAdmin_verification() {
        return admin_verification;
    }

    public void setAdmin_verification(String admin_verification) {
        this.admin_verification = admin_verification;
    }

    public String getJob_url() {
        return Job_url;
    }

    public void setJob_url(String job_url) {
        Job_url = job_url;
    }

    public String getClinic_url() {
        return Clinic_url;
    }

    public void setClinic_url(String clinic_url) {
        Clinic_url = clinic_url;
    }

}
