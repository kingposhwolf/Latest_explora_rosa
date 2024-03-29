package com.example.demo.Models.UserManagement.PersonalAccount;

import com.example.demo.Models.UserManagement.Profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("PERSONAL")
public class Personal extends Profile{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "titleId")
    private Title title;
}
