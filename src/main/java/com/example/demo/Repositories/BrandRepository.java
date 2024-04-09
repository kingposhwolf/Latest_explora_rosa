package com.example.demo.Repositories;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
/*
 * @author Dwight Danda
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserManagement.BussinessAccount.Brand;


@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {

    @Query("SELECT DISTINCT l.id as id, l.user.username as username, l.user.name as name, l.user.email as email, l.user.accountType.name as accountTypeName, l.user.accountType.id as accountTypeId, l.profilePicture as profilePicture, l.coverPhoto as coverPhoto, l.bio as bio, l.address as address, c as country, l.verificationStatus as verificationStatus, l.city.id as cityId, l.followers as followers, l.following as following, l.posts as posts, l.powerSize as powerSize, d as businessCategories, l.rates as rates FROM Brand l LEFT JOIN l.country c LEFT JOIN l.businessCategories d WHERE  l.id = :id")
    Map<String, Object> findProfileInfoById(@Param("id") Long id);
}
