package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {

    Optional<Category> findById(Category parent);
}
