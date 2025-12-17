package com.dishcovery.backend;

import com.dishcovery.backend.interfaces.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void paymentTest() {
        paymentService.verifyPayment("eyJ0cmFuc2FjdGlvbl9jb2RlIjoiMDAwREQ5MyIsInN0YXR1cyI6IkNPTVBMRVRFIiwidG90YWxfYW1vdW50IjoiNDAuMCIsInRyYW5zYWN0aW9uX3V1aWQiOiI0Mzg4IiwicHJvZHVjdF9jb2RlIjoiRVBBWVRFU1QiLCJzaWduZWRfZmllbGRfbmFtZXMiOiJ0cmFuc2FjdGlvbl9jb2RlLHN0YXR1cyx0b3RhbF9hbW91bnQsdHJhbnNhY3Rpb25fdXVpZCxwcm9kdWN0X2NvZGUsc2lnbmVkX2ZpZWxkX25hbWVzIiwic2lnbmF0dXJlIjoiRDdIYVhyU0g0QTV4eWJNWm1OUmtTOVJVMXU1QklmajNYTTZxRjhZNGIvVT0ifQ==");
    }
}
