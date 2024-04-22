package com.example.demo.InputDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowUnFollowDto {
    @NotNull
    private Long accountId;

    @NotNull
    private Long user;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static FollowUnFollowDto fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, FollowUnFollowDto.class);
    }
}
