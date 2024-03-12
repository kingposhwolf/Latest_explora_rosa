package com.example.demo.Dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShareDto {
    @NotNull
    private Long profileId;

    @NotNull
    private Long postId;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static ShareDto fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ShareDto.class);
    }
}
