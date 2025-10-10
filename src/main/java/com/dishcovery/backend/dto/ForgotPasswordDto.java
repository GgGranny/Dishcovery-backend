package com.dishcovery.backend.dto;

public class ForgotPasswordDto {
    private String email;
    private String newPassword;

    public ForgotPasswordDto() {
    }

    public ForgotPasswordDto(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    @Override
    public String toString() {
        return "ForgotPasswordDto{" +
                "email='" + email + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
