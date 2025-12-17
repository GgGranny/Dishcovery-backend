package com.dishcovery.backend.model;

import com.dishcovery.backend.model.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
public class EsewaPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = true)
    private String transaction_code;

    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name="transactionUuid",unique = true, nullable = false)
    private String transaction_uuid;

    @Column(nullable = false)
    private String product_code;

    @Column(nullable = false)
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public EsewaPayment() {
    }

    public EsewaPayment(String transaction_code, PaymentStatus status, String transaction_uuid, String product_code, double amount, Users user) {
        this.transaction_code = transaction_code;
        this.status = status;
        this.transaction_uuid = transaction_uuid;
        this.product_code = product_code;
        this.amount = amount;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "EsewaPayment{" +
                "id=" + id +
                ", transaction_code='" + transaction_code + '\'' +
                ", status='" + status + '\'' +
                ", transaction_uuid='" + transaction_uuid + '\'' +
                ", product_code='" + product_code + '\'' +
                ", amount=" + amount +
                '}';
    }
}
