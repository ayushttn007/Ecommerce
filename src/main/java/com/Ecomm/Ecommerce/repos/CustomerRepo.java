package com.Ecomm.Ecommerce.repos;

import com.Ecomm.Ecommerce.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query(value = "select * from Customer where User_Id=:userId",nativeQuery = true)
    public Customer findByUser_Id(@Param("userId") long userId);

    Optional<Customer> findByContact(Long contact);


}