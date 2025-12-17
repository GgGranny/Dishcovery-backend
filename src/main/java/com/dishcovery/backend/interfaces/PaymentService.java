package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.EsewaPaymentDto;
import com.dishcovery.backend.dto.EsewaSignatureResponseDto;
import com.dishcovery.backend.model.EsewaPayment;

public interface PaymentService {

    // Esewa
    // Generate a Secret Key
    EsewaSignatureResponseDto generateSignature(EsewaPaymentDto dto);

    EsewaPayment verifyPayment(String data);
}
