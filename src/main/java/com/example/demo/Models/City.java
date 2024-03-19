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
        @JoinColumn(name = "countryId", foreignKey = @ForeignKey(name = "FK_country_city", foreignKeyDefinition = "FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE"))
        private Country country;

        @NotBlank
        @Column(length = 100, nullable = false, unique = true)
        private String name;

        @Column(nullable = false)
        private String zipCode;

        @Column(nullable = false, length = 150)
        private String state;
}
