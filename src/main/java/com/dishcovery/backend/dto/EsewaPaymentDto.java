package com.dishcovery.backend.dto;

public class EsewaPaymentDto {
    private int total_amount;
    private String transaction_uuid;
    private String product_code;

    public EsewaPaymentDto() {
    }

    public EsewaPaymentDto(int total_amount, String transaction_uuid, String product_code) {
        this.total_amount = total_amount;
        this.transaction_uuid = transaction_uuid;
        this.product_code = product_code;
    }

    public String getTransaction_uuid() {
        return transaction_uuid;
    }

    public void setTransaction_id(String transaction_uuid) {
        this.transaction_uuid = transaction_uuid;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }


    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
