package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Product;
import com.Ecomm.Ecommerce.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product ,Long> {

    @Query(value = "SELECT * FROM product WHERE name=:name and category_id=:catId and seller_id=:sellerId and brand=:brand" ,nativeQuery = true)
    Optional<Product> findExistingProduct(String name, Long sellerId, String brand, Long catId);

    List<Product> findBySeller(Seller seller);
}