package com.example.demo.Models.SocialMedia;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("BUSINESS_POST")
public class BusinessPost extends UserPost {

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rate;
}