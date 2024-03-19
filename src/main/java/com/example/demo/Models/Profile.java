package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "profiles")
@Entity
@SQLDelete(sql = "UPDATE profiles SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "PROFILE_TYPE", discriminatorType = DiscriminatorType.STRING)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "FK__USER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    User user;

    private String profilePicture;

    private String coverPhoto;

    private String bio;

    private String address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "countryId", foreignKey = @ForeignKey(name = "FK_PROFILE_COUNTRY", foreignKeyDefinition = "FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE SET NULL"))
    private Country country;

    @Column(nullable = false)
    private VerificationStatus verificationStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cityId", foreignKey = @ForeignKey(name = "FK_PROFILE_CITY", foreignKeyDefinition = "FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE SET NULL"))
    private City city;

    @Column(nullable = false)
    private int followers;

    @Column(nullable = false)
    private int following;

    @Column(nullable = false)
    private int posts;

    @Column(nullable = false)
    private float powerSize;

    private boolean deleted = Boolean.FALSE;
}
