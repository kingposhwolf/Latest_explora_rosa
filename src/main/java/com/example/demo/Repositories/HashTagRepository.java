package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.SocialMedia.HashTag;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends CrudRepository<HashTag, Long> {
    Optional<HashTag> findByName(String name);

    List<HashTag> findAllByIdIn(List<Long> hashTagIds);
}
