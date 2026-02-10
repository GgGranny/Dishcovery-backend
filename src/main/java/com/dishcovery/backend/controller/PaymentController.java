package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.EsewaPaymentDto;
import com.dishcovery.backend.dto.EsewaSignatureResponseDto;
import com.dishcovery.backend.service.JWTService;
import com.dishcovery.backend.service.PaymentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {

    private PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/generate-signature")
    public EsewaSignatureResponseDto generateSignature(@RequestBody EsewaPaymentDto dto) {
        return paymentService.generateSignature(dto);
    }

    @GetMapping("/esewa/payment-success")
    public ResponseEntity<?> esewaPaymentSuccess(
            @RequestParam(value = "data", required = false) String data
    ) {
        paymentService.verifyPayment(data);
        return  ResponseEntity.status(HttpStatus.OK)
                .header("Location", "http://localhost:5173/payment/success")
                .build();
    }

    @GetMapping("/esewa/payment-failed")
    public ResponseEntity<?> esewaPaymentFailed() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Location", "http://localhost:5173/payment/fail")
                .build();
    }


}
