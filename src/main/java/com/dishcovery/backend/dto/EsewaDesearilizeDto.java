package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.enums.PaymentStatus;
import jakarta.persistence.Column;

public class EsewaDesearilizeDto {
    private String transaction_code;
    private String status;
    private String transaction_uuid;
    private String product_code;
    private double total_amount;
    private String signed_field_names;
    private String signature;

    public EsewaDesearilizeDto() {
    }

    public EsewaDesearilizeDto(String transaction_code, String status, String transaction_uuid, String product_code, double total_amount, String signed_field_names, String signature) {
        this.transaction_code = transaction_code;
        this.status = status;
        this.transaction_uuid = transaction_uuid;
        this.product_code = product_code;
        this.total_amount = total_amount;
        this.signed_field_names = signed_field_names;
        this.signature = signature;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransaction_uuid() {
        return transaction_uuid;
    }

    public void setTransaction_uuid(String transaction_uuid) {
        this.transaction_uuid = transaction_uuid;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getSigned_field_names() {
        return signed_field_names;
    }

    public void setSigned_field_names(String signed_field_names) {
        this.signed_field_names = signed_field_names;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "EsewaDesearilizeDto{" +
                "transaction_code='" + transaction_code + '\'' +
                ", status='" + status + '\'' +
                ", transaction_uuid='" + transaction_uuid + '\'' +
                ", product_code='" + product_code + '\'' +
                ", total_amount=" + total_amount +
                ", signed_field_names='" + signed_field_names + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
