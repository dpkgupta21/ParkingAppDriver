package com.parking.app.parkingappdriver.model;

import java.io.Serializable;

/**
 * Created by Harish on 3/20/2016.
 */
public class LoadJobsDTO implements Serializable {


    private String CustomerName;
    private String email;
    private String mobile_no;
    private String jobId;
    private String Plate_No;
    private String Vehicle_Make;
    private String Vehicle_Model;
    private String Vehicle_Color;
    private String Job_Status;
    private String slotName;
    private String slotLevel;
    private String venueName;


    public String getVehicle_Make() {
        return Vehicle_Make;
    }

    public void setVehicle_Make(String vehicle_Make) {
        Vehicle_Make = vehicle_Make;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPlate_No() {
        return Plate_No;
    }

    public void setPlate_No(String plate_No) {
        Plate_No = plate_No;
    }

    public String getVehicle_Model() {
        return Vehicle_Model;
    }

    public void setVehicle_Model(String vehicle_Model) {
        Vehicle_Model = vehicle_Model;
    }

    public String getVehicle_Color() {
        return Vehicle_Color;
    }

    public void setVehicle_Color(String vehicle_Color) {
        Vehicle_Color = vehicle_Color;
    }

    public String getJob_Status() {
        return Job_Status;
    }

    public void setJob_Status(String job_Status) {
        Job_Status = job_Status;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotLevel() {
        return slotLevel;
    }

    public void setSlotLevel(String slotLevel) {
        this.slotLevel = slotLevel;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
}
