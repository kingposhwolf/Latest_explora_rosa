package com.example.demo.Repositories;

import com.example.demo.Models.HashTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashTagRepository extends CrudRepository<HashTag, Long> {
    Optional<HashTag> findByName(String name);

}
