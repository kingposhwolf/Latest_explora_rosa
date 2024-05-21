package com.example.demo.TempDto;

import java.util.List;

import lombok.Data;

@Data
public class PageDto {
    private int pagenumber;

    private List<Long> contents;
}
