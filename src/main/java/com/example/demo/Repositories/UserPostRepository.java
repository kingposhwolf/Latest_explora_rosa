package com.example.demo.Repositories;

import com.example.demo.Models.UserPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends CrudRepository<UserPost, Long> {
}
