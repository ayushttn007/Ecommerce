package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {

    @Query(value = "select * from Seller where User_Id=:userId",nativeQuery = true)
    public Seller findByUser_Id(@Param("userId") long userId);

    Optional<Seller> findByGst(String gst);

    public Seller findByUser(User user);

}