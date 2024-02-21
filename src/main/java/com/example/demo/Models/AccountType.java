package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="accountType")
@Data
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="accountTypeId")
    private Long id;

    @Column(name ="accountTypeName")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccountType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
