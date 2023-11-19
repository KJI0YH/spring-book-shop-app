package com.example.mybookshopapp.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse {

    @JsonIgnore
    private Boolean result;

    @JsonIgnore
    private String message;

    public ApiResponse(Boolean result) {
        this.result = result;
    }

    public ApiResponse(Boolean result, String message) {
        this(result);
        this.message = message;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("result", result);
        String key = Boolean.TRUE.equals(result) ? "message" : "error";
        if (message != null && !message.isEmpty()) {
            properties.put(key, message);
        }
        return properties;
    }
}
