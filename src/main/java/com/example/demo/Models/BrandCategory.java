package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "BrandCategories")
@Data
public class BrandCategory {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "businessCategoryId", nullable = false)
    private BusinessCategory businessCategory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

}
