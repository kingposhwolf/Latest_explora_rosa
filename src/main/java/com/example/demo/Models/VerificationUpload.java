package com.example.demo.Models;
/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "verificationUploads")
@Data
public class VerificationUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "uploadId", nullable = false)
    private Upload upload;



}
