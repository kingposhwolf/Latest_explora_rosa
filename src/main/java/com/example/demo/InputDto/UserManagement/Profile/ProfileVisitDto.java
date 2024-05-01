package com.example.demo.InputDto.UserManagement.Profile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfileVisitDto {
    @NotNull
    private Long ownerId;

    @NotNull
    private Long visitorId;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static ProfileVisitDto fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ProfileVisitDto.class);
    }
}
