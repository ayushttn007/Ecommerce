package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Integer> {
    VerificationToken findByVerificationToken(String confirmationToken);


    VerificationToken findByUser(User user);
}