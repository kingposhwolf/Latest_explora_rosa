/*
 * @author Dwight Danda
 *
 */
package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="accountType")
@Data
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

}
