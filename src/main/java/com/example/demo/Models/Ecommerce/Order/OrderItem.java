package com.example.demo.Models.Ecommerce.Order;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.demo.Models.SocialMedia.UserPost;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="orderItems")
@Data
@SQLDelete(sql = "UPDATE order_items SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false, foreignKey = @ForeignKey(name = "FK_HR873GJ_ORDER_ITEM", foreignKeyDefinition = "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE"))
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCT_ORDER_ITEM", foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    private UserPost product;
    
    private int quantity;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;
}
