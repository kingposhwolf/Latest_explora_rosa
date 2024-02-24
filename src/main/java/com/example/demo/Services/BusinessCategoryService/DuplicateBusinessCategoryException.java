package com.example.demo.Services.BusinessCategoryService;
/*
 * @author Dwight Danda
 *
 */
public class DuplicateBusinessCategoryException extends RuntimeException{
    public DuplicateBusinessCategoryException(String message){
        super(message);
    }
}
