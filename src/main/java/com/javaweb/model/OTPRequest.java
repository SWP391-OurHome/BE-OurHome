package com.javaweb.model;

public class OTPRequest {
    private String email;
    private String otp;

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
}

