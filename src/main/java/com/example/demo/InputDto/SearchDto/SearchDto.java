package com.example.demo.InputDto.SearchDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchDto {
    @NotNull
    private Long profileId;

    @NotNull
    private String keyword;

    @NotNull
    private Long countryId;

    private int pageNumber;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static  SearchDto fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json,  SearchDto.class);
    }
}
