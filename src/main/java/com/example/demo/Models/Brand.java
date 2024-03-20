/*
 * @author Dwight Danda
 *
 */
package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@DiscriminatorValue("BUSINESS")
public class Brand extends Profile {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 1, scale=1, nullable = false)
    private BigDecimal rates;

    private String tinNumber;

    @ManyToOne
    @JoinColumn(name = "businessId", foreignKey = @ForeignKey(name = "FK_BRAND_BUSINESS_CATEGORY", foreignKeyDefinition = "FOREIGN KEY (business_id) REFERENCES business_categories(id) ON UPDATE CASCADE"))
    private BusinessCategory businessCategories;

}
