package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Entity
@Table(name="cities")
@Data
public class City {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "countryId")
        private Country country;

        @NotBlank
        @Column(length = 100, nullable = false)
        private String name;

        @Column(nullable = false)
        private String zipCode;

        @Column(nullable = false)
        private String state;



}
