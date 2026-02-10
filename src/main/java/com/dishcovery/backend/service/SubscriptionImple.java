package com.dishcovery.backend.service;

import com.dishcovery.backend.dto.SubscriptionResponse;
import com.dishcovery.backend.interfaces.SubscriptionService;
import com.dishcovery.backend.model.Subscription;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.enums.SubscriptionStatus;
import com.dishcovery.backend.repo.SubscriptionRepo;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class SubscriptionImple implements SubscriptionService {
    private UserRepo userRepo;
    private SubscriptionRepo subscriptionRepo;


    public SubscriptionImple(UserRepo userRepo, SubscriptionRepo subscriptionRepo){
        this.userRepo = userRepo;
        this.subscriptionRepo = subscriptionRepo;
    }


    @Override
    public void subscribeAsFreeUser(String username) {
        try {
            Users user = userRepo.findByUsername(username);
            Subscription newSubscription = Subscription.builder()
                    .subscriptionStatus(SubscriptionStatus.FREE)
                    .startDate(LocalDateTime.now())
                    .endData(LocalDateTime.now().plusMinutes(2))
                    .isActive(false)
                    .user(user)
                    .build();
            subscriptionRepo.save(newSubscription);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activateSubscription(String username) {
        try {
            Users user = userRepo.findByUsername(username);
            Subscription subscription = subscriptionRepo.findByUser_Id(user.getId());
            if(subscription != null){
                subscription.setSubscriptionStatus(SubscriptionStatus.PREMIUM);
                subscription.setActive(true);
                subscription.setStartDate(LocalDateTime.now());
                if(!subscription.getEndData().isBefore(LocalDateTime.now())) {
                    subscription.setEndData(subscription.getEndData().plusMinutes(2));
                }else {
                    subscription.setEndData(LocalDateTime.now().plusMinutes(2));
                }
                subscriptionRepo.save(subscription);
            }else{
                Subscription newSubscription = Subscription.builder()
                        .subscriptionStatus(SubscriptionStatus.PREMIUM)
                        .startDate(LocalDateTime.now())
                        .endData(LocalDateTime.now().plusMinutes(2))
                        .isActive(true)
                        .user(user)
                        .build();
                subscriptionRepo.save(newSubscription);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSubscriptionExpired(int userId) {
        Subscription subscription = subscriptionRepo.findByUser_Id(userId);

        if(subscription == null) {
            return true;
        }

        if(subscription.getEndData().isBefore(LocalDateTime.now())) {
            subscription.setActive(false);
            subscription.setSubscriptionStatus(SubscriptionStatus.FREE);
            subscriptionRepo.save(subscription);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserSubscribed(int userId) {
        return subscriptionRepo.existsByUser_Id(userId);
    }



}
