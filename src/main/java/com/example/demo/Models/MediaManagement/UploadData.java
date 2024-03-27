package com.example.demo.Models.MediaManagement;
import com.example.demo.Models.UserManagement.BussinessAccount.Brand;

/*
 * @author Dwight Danda
 *
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="uploadData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String path;
}
