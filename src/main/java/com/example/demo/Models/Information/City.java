package com.example.demo.Models.Information;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
@Entity
@Table(name="cities")
@Data
@SQLDelete(sql = "UPDATE cities SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class City {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "countryId", nullable = false, foreignKey = @ForeignKey(name = "FK_country_city", foreignKeyDefinition = "FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE"))
        @ToString.Exclude
        private Country country;

        @NotBlank
        @Column(length = 100, nullable = false, unique = true)
        private String name;

        @Column(nullable = false)
        private String zipCode;

        @Column(nullable = false, length = 150)
        private String state;

        @JsonIgnore
        private boolean deleted = Boolean.FALSE;
}
