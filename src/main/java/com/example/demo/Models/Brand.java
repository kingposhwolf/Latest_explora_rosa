/*
 * @author Dwight Danda
 *
 */
package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "businessId")
    private BusinessCategory businessCategories;

}
