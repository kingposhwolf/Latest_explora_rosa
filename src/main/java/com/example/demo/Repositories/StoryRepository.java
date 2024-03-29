package com.example.demo.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.SocialMedia.Story;

@Repository
public interface StoryRepository extends CrudRepository<Long, Story> {
}
