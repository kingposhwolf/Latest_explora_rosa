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
import java.util.Collection;

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

    @ManyToMany
    @JoinTable(name = "brandBusinessCategories", joinColumns = @JoinColumn(name = "brandId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "businessCategoryId", referencedColumnName = "id"))
    private Collection<BusinessCategory> businessCategories;

}
