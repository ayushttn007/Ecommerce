package com.Ecomm.Ecommerce;

import com.Ecomm.Ecommerce.entities.Address;
import com.Ecomm.Ecommerce.entities.Customer;
import com.Ecomm.Ecommerce.entities.Role;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repository.CustomerRepo;
import com.Ecomm.Ecommerce.repository.RoleRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CustomerRepositoryTests {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    RoleRepo roleRepo;
//
//    @Test
//    @Order(1)
//    public void saveCustomerTest(){
//
//        // setting user details
//        User user = new User();
//        user.setFirstName("Ayush");
//        user.setMiddleName("k");
//        user.setLastName("sharma");
//        user.setEmail("ayushsharma@gmail.com");
//        user.setPassword("passcode@123");
//        Role role =roleRepo.findById(3L).get();
//        user.setRole(role);                     //6570
//
//        // list of address - for multiple addresses
//        List<Address> addresses = new ArrayList<>();
//
//        Address address = new Address();
//        address.setCity("New Delhi");
//        address.setState("Delhi");
//        address.setZipCode(110074);
//        address.setAddressLine("shahdara");
//        address.setCountry("India");
//        address.setLabel("Home");
//
//        Address altAddress = new Address();
//        altAddress.setCity("New Delhi");
//        altAddress.setState("Delhi");
//        altAddress.setZipCode(110017);
//        altAddress.setAddressLine("gamma");
//        altAddress.setCountry("India");
//        altAddress.setLabel("Home");
//
//        // adding addresses to the list
//        addresses.add(address);
//        addresses.add(altAddress);
//
//        // setting customer details
//        Customer customer = new Customer();
//        customer.setContact(6565656565L);
//
//
//        customer.setUser(user);
//        address.setCustomer(customer);
//        altAddress.setCustomer(customer);
//        customer.setAddresses(addresses);
//
//        customerRepo.save(customer);
//
//        Assertions.assertThat(customer.getId()).isGreaterThan(0);
//        Assertions.assertThat(customer.getAddresses()).hasSize(2);
//
//    }

//    @Test
//    @Order(2)
//    public void getCustomerTest(){
//       Customer customer = customerRepo.findByContact(6565656565L).get();
//        Assertions.assertThat(customer.getContact()).isEqualTo(6565656565L);
//    }
//
//    @Test
//    @Order(3)
//    public void updateCustomerTest(){
//        Customer customer = customerRepo.findByContact(6565656565L).get();
//        customer.setContact(7575757575L);
//
//        Customer customerUpdated = customerRepo.save(customer);
//
//        Assertions.assertThat(customerUpdated.getContact()).isEqualTo(7575757575L);
//
//    }

//    @Test
//    @Order(4)
//    public void deleteCustomerTest(){
//        Customer customer = customerRepo.findByContact("7575757575").get();
//        customerRepo.delete(customer);
//
//        Optional<Customer> optionalCustomer = customerRepo.findByContact(7575757575L);
//
//        Customer dummyCustomer = null;
//
//        if(optionalCustomer.isPresent()){
//            dummyCustomer = optionalCustomer.get();
//        }
//
//        Assertions.assertThat(dummyCustomer).isNull();
//    }
}