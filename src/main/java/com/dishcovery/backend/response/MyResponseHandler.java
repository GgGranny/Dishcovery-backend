package com.dishcovery.backend.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class MyResponseHandler {

    public static ResponseEntity<Object> responseBuilder(HttpStatus status, String message, Object responseData) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("data", responseData);
        return new ResponseEntity<>(response, status);
    }
}
