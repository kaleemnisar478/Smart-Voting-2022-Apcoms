package com.project.smartvotingsystem.helper_classes;

import java.io.Serializable;

public class User implements Serializable {
    private String fullname;
    private String email;
    private String phoneNo;
    private String profileImage;
    private String cnic;
    private String dob;
    private String userType;
    private String id;
    private String password;
    private String imageData;




    public User() {
        fullname="";
        email="";
        phoneNo="";
    }

    public User(String fullname,  String email,  String phoneNo) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public User(String fullname, String email, String phoneNo, String cnic, String dob, String userType, String id, String password) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.cnic = cnic;
        this.dob = dob;
        this.userType = userType;
        this.id = id;
        this.password = password;
    }

    public User(String fullname, String email, String phoneNo, String cnic, String dob, String userType, String id, String password, String imageData) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.cnic = cnic;
        this.dob = dob;
        this.userType = userType;
        this.id = id;
        this.password = password;
        this.imageData = imageData;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
