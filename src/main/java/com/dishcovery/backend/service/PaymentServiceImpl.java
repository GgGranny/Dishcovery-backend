package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.EsewaDesearilizeDto;
import com.dishcovery.backend.dto.EsewaPaymentDto;
import com.dishcovery.backend.dto.EsewaSignatureResponseDto;
import com.dishcovery.backend.interfaces.PaymentService;
import com.dishcovery.backend.model.EsewaPayment;
import com.dishcovery.backend.model.Subscription;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.enums.PaymentStatus;
import com.dishcovery.backend.model.enums.SubscriptionStatus;
import com.dishcovery.backend.repo.PaymentRepo;
import com.dishcovery.backend.repo.SubscriptionRepo;
import com.dishcovery.backend.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;


@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final String secretKey = "8gBm/:&EnhH.1/q";

    private UserRepo userRepo;

    private PaymentRepo paymentRepo;

    private SubscriptionImple subscriptionImple;


    public PaymentServiceImpl(UserRepo userRepo, PaymentRepo paymentRepo, SubscriptionImple subscriptionImple) {
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
        this.subscriptionImple = subscriptionImple;
    }
    @Override
    public EsewaSignatureResponseDto generateSignature(EsewaPaymentDto dto) {
        String message =
                "total_amount=" + dto.getTotal_amount() +
                        ",transaction_uuid=" + dto.getTransaction_uuid() +
                        ",product_code=" + dto.getProduct_code();
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            String signature = Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));

            EsewaPayment esewaPayment = new EsewaPayment();
            esewaPayment.setAmount(dto.getTotal_amount());
            esewaPayment.setTransaction_uuid(dto.getTransaction_uuid());
            esewaPayment.setProduct_code(dto.getProduct_code());
            esewaPayment.setStatus(PaymentStatus.PENDING);
            esewaPayment.setTransaction_code(null);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Users user = userRepo.findByUsername(username);
                esewaPayment.setUser(user);
            }
            paymentRepo.save(esewaPayment);

            return new EsewaSignatureResponseDto(
                    signature,
                    "total_amount,transaction_uuid,product_code"
            );
        }catch (Exception e) {
            logger.error("failed to generate signature", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EsewaPayment verifyPayment(String data) {
        try {
            EsewaDesearilizeDto esewaPaymentSuccess = decodeBase64ToObject(data);
            EsewaPayment dbPaymentData = paymentRepo.findByTransaction_Uuid(esewaPaymentSuccess.getTransaction_uuid())
                            .orElseThrow(()-> new RuntimeException("payment not found"));
            dbPaymentData.setTransaction_code(esewaPaymentSuccess.getTransaction_code());
            if(esewaPaymentSuccess.getStatus().equals("COMPLETE")) {
                dbPaymentData.setStatus(PaymentStatus.SUCCESS);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                subscriptionImple.activateSubscription(username);
            }else {
                dbPaymentData.setStatus(PaymentStatus.FAILED);
            }
            EsewaPayment rs = paymentRepo.save(dbPaymentData);
            System.out.println(rs.toString());
            return rs;
        }catch(Exception e) {
            logger.error("failed to verify payment", e);
            e.printStackTrace();
        }
        return null;
    }

    private EsewaDesearilizeDto decodeBase64ToObject(String data) throws IOException {
        byte[] actualData = Base64.getDecoder().decode(data);
        ObjectMapper objectMapper = new ObjectMapper();
        EsewaDesearilizeDto esewaPayment = objectMapper.readValue(actualData, EsewaDesearilizeDto.class);
        return esewaPayment;
    }


//    private void activateSubscription(String username){
//        try {
//             Users user = userRepo.findByUsername(username);
//             Subscription subscription = subscriptionRepo.findByUser_Id(user.getId());
//             if(subscription != null){
//                 subscription.setSubscriptionStatus(SubscriptionStatus.PREMIUM);
//                 subscription.setActive(true);
//                 subscription.setStartDate(LocalDateTime.now());
//                 if(!subscription.getEndData().isBefore(LocalDateTime.now())) {
//                     subscription.setEndData(subscription.getEndData().plusMinutes(2));
//                 }else {
//                     subscription.setEndData(LocalDateTime.now().plusMinutes(2));
//                 }
//                 subscriptionRepo.save(subscription);
//             }else{
//                 Subscription newSubscription = Subscription.builder()
//                         .subscriptionStatus(SubscriptionStatus.PREMIUM)
//                         .startDate(LocalDateTime.now())
//                         .endData(LocalDateTime.now().plusMinutes(2))
//                         .isActive(true)
//                         .user(user)
//                         .build();
//                 subscriptionRepo.save(newSubscription);
//             }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

}
