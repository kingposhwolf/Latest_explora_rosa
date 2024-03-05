package com.example.demo.Repositories;

import com.example.demo.Models.Suggestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends CrudRepository<Suggestion, Long> {

}
