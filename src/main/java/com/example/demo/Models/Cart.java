package com.example.demo.Models;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="cart")
@Data
@SQLDelete(sql = "UPDATE account_type SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Cart {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMER_CART_PROFILE", foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile customer;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    private boolean deleted = Boolean.FALSE;
}
