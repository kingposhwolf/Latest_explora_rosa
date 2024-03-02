package com.example.demo.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository2 extends CrudRepository<User2,String>{
    List<User2> findAllByStatus(Status2 status);
}
