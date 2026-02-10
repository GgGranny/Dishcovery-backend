package com.dishcovery.backend.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdResponse {
    private Long ad_id;

    private String adUrl;

    private String adName;

    private String contentType;

    private int clickCount = 0;

    private Double duration;
}
