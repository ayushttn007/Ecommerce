package com.Ecomm.Ecommerce.repos;

import com.Ecomm.Ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = "select * from User where First_Name=:firstName", nativeQuery = true)
    public User findByFirstName(@Param("firstName") String firstName);
    public User findByEmail(String Email);

//    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String VerificationCode);

}
