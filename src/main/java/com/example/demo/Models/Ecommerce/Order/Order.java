package com.example.demo.Models;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="orders")
@Data
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMER_ORDER_PROFILE", foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES profiles(id) ON DELETE CASCADE"))
    private Profile customer;
    
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;
    
    private BigDecimal totalAmount;
    
    private String status;

    private boolean deleted = Boolean.FALSE;
}
