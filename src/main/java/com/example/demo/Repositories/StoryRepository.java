package com.example.demo.Repositories;

import com.example.demo.Models.Story;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends CrudRepository<Long, Story> {
}
