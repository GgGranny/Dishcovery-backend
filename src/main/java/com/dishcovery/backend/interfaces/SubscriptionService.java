package com.dishcovery.backend.interfaces;

import com.dishcovery.backend.dto.SubscriptionResponse;

public interface SubscriptionService {
    //Subscribe as a free User
    void subscribeAsFreeUser(String username);

    // Activate User Subscription
    void activateSubscription(String username);

    // Check for expiration
    boolean isSubscriptionExpired(int userId);

    // Check if User Subscribed
    boolean isUserSubscribed(int userId);

}
