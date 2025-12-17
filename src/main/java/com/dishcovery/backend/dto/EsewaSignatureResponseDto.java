package com.dishcovery.backend.dto;

public class EsewaSignatureResponseDto {
    private String signature;
    private String signed_field_name;

    public EsewaSignatureResponseDto() {
    }

    public EsewaSignatureResponseDto(String signature, String signed_field_name) {
        this.signature = signature;
        this.signed_field_name = signed_field_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSigned_field_name() {
        return signed_field_name;
    }

    public void setSigned_field_name(String signed_field_name) {
        this.signed_field_name = signed_field_name;
    }
}
