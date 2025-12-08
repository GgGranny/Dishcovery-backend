package com.dishcovery.backend.controller;


import com.dishcovery.backend.service.JWTService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {
    private JWTService jwtService;

    public PaymentController(JWTService jwtService) {
        this.jwtService = jwtService;
    }
    @GetMapping("/esewa/pay")
    public String esewaPay(){
        return null;
    }
}
