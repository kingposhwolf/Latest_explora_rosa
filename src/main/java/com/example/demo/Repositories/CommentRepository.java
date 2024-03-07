package com.example.demo.Repositories;

import com.example.demo.Models.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
