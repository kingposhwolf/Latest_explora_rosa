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
@Table(name="brands")
@Data
public class Brand {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userId" , nullable= false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cityId")
    private City city;

    @Column(precision = 1, scale=1, nullable = false)
    private BigDecimal rates;

    private String tinNumber;

    private String address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "businessId")
    private BusinessCategory businessCategories;

}
