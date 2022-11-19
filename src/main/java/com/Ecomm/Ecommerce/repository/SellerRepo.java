package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller, Long> {

    @Query(value = "select * from Seller where User_Id=:userId",nativeQuery = true)
    public Seller findByUser_Id(@Param("userId") long userId);

    Optional<Seller> findByGst(String gst);

}