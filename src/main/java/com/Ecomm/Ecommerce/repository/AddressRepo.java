package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {

    @Query(value = "select * from Address where Customer_ID=:customerId",nativeQuery = true)
    public List<Address> findByCustomer_ID(@Param("customerId") long customerId);

    @Query(value = "select * from Address where Seller_ID=:sellerId",nativeQuery = true)
    public Address findBySeller_ID(@Param("sellerId") long sellerId);
}