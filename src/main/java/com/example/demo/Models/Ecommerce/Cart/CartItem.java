package com.example.demo.Models.Ecommerce.Cart;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.UserPost;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="cartItems")
@Data
@SQLDelete(sql = "UPDATE cart_items SET deleted = true WHERE id=?")
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
    @JoinColumn(name = "cartId", nullable = false, foreignKey = @ForeignKey(name = "FK_CART_BH55YI6H_ITEM", foreignKeyDefinition = "FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE"))
    @ToString.Exclude
    private Cart cart;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
