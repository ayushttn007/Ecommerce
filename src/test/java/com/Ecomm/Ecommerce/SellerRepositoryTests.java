package com.Ecomm.Ecommerce;

import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repos.SellerRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SellerRepositoryTests {

    @Autowired
    private SellerRepo sellerRepo;

    @Test
    @Order(1)
    public void saveSellerTest(){
        User user = new User();
        user.setFirstName("marti");
        user.setMiddleName("k");
        user.setLastName("swatt");
        user.setEmail("marti@gmail.com");
        user.setPassword("passcodemarti");

        Address address = new Address();
        address.setCity("Athina");
        address.setState("Attica");
        address.setZipCode(15125);
        address.setAddressLine("Distomou 9");
        address.setCountry("Greece");
        address.setLabel("office");


        Seller seller = new Seller();
        seller.setGst("GST123");
        seller.setCompanyContact(9876543210L);
        seller.setCompanyName("greecesolutions");


        seller.setUser(user);

        address.setSeller(seller);
        seller.setAddress(address);

        sellerRepo.save(seller);

        Assertions.assertThat(seller.getId()).isGreaterThan(0);

    }

    @Test
    @Order(2)
    public void getSellerTest(){
        Seller seller = sellerRepo.findByGst("GST123").get();
        Assertions.assertThat(seller.getGst()).isEqualTo("GST123");
    }

    @Test
    @Order(3)
    public void updateSellerTest(){
        Seller seller = sellerRepo.findByGst("GST123").get();
        seller.setCompanyContact(1414141414);

        Seller sellerUpdated = sellerRepo.save(seller);

        Assertions.assertThat(sellerUpdated.getCompanyContact()).isEqualTo(1414141414);

    }

    @Test
    @Order(4)
    public void deleteSellerTest(){
        Seller seller = sellerRepo.findByGst("GST123").get();
        sellerRepo.delete(seller);

        Optional<Seller> optionalSeller = sellerRepo.findByGst("GST123");

        Seller dummySeller = null;

        if(optionalSeller.isPresent()){
            dummySeller = optionalSeller.get();
        }

        Assertions.assertThat(dummySeller).isNull();
    }
}