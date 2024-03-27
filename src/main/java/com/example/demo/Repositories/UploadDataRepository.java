package com.example.demo.Repositories;
/*
 * @author Dwight Danda
 *
 */
import org.springframework.data.repository.CrudRepository;

import com.example.demo.Models.MediaManagement.UploadData;

import java.util.Optional;
/*
 * @author Dwight Danda
 *
 */
public interface UploadDataRepository extends CrudRepository<UploadData, Long> {
    Optional<UploadData> findByName(String fileName);

    Iterable<UploadData> findByType(String type);
    Iterable<UploadData> findByTypeStartingWith(String typePrefix);

    Iterable<UploadData> findByBrandIdAndType(Long brandId, String type);
    Iterable<UploadData> findByBrandIdAndTypeStartingWith(Long brandId, String typePrefix);

    Iterable<UploadData> findByBrandId(Long brandId);
}
