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

    Iterable<UploadData> findByType(String type);
    Iterable<UploadData> findByTypeStartingWith(String typePrefix);

    Iterable<UploadData> findByBrandIdAndType(Long brandId, String type);
    Iterable<UploadData> findByBrandIdAndTypeStartingWith(Long brandId, String typePrefix);

    Iterable<UploadData> findByBrandId(Long brandId);
}
