package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.Optional;
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query(value = "select * from Customer where User_Id=:userId",nativeQuery = true)
    public Customer findByUser_Id(@Param("userId") long userId);

    public Customer findByContact(String contact);

    public Customer findByUser(User user);


}