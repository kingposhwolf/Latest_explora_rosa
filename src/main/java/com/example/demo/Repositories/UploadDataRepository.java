package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import com.example.demo.Models.UploadData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UploadDataRepository extends CrudRepository<UploadData, Long> {
    Optional<UploadData> findByName(String fileName);
}
