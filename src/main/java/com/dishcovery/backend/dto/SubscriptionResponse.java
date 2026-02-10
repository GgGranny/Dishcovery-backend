package com.dishcovery.backend.dto;

import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.model.enums.SubscriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubscriptionResponse {
    private int userId;

    private SubscriptionStatus subscriptionStatus;

    private LocalDateTime startDate;

    private LocalDateTime endData;

    private boolean isActive;

}
