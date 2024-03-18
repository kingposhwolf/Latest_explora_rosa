package com.example.demo.Models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("BUSINESS")
public class BusinessPost extends UserPost{
    private double price;
    private String currency;
}
