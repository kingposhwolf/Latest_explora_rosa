//package com.example.demo.Models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name="userPosts")
//public class UserPost {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "userId", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "countryId", nullable = false)
//    private Country country;
//
//    @ManyToMany
//    @JoinColumn()
//}
