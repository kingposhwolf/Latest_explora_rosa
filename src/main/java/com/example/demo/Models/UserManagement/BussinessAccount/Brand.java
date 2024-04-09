package com.example.demo.Models.UserManagement.BussinessAccount;

/*
 * @author Dwight Danda
 *
 */

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.example.demo.Models.UserManagement.Profile;

@Entity
@Data
@DiscriminatorValue("BUSINESS")
public class Brand extends Profile {

    @Column(precision = 1, scale=1, nullable = false)
    private BigDecimal rates;

    private String tinNumber;

    @ManyToOne
    @JoinColumn(name = "businessId", foreignKey = @ForeignKey(name = "FK_BRAND_BUSINESS_CATEGORY", foreignKeyDefinition = "FOREIGN KEY (business_id) REFERENCES business_categories(id) ON UPDATE CASCADE"))
    private BusinessCategory businessCategories;

}
