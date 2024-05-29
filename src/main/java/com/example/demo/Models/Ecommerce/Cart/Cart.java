package com.example.demo.Models.Ecommerce.Cart;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.UserManagement.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="cart")
@Data
@SQLDelete(sql = "UPDATE cart SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Cart {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customerId", nullable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMER_CART_PROFILE", foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile customer;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItem> items;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
