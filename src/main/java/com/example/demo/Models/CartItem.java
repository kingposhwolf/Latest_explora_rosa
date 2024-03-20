package com.example.demo.Models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="cartItems")
@Data
@SQLDelete(sql = "UPDATE account_type SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class CartItem {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCT_CART_ITEM", foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost product;
    
    private int quantity;
    
    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

    private boolean deleted = Boolean.FALSE;
}
