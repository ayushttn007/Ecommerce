//package com.Ecomm.Ecommerce;
//
//import com.Ecomm.Ecommerce.entities.*;
//import com.Ecomm.Ecommerce.repos.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//class EcommerceApplicationTests {
//
//	@Autowired
//	UserRepo userRepository;
//
//	@Autowired
//	CustomerRepo customerRepo;
//
//	@Autowired
//	SellerRepo sellerRepo;
//
//	@Autowired
//	AddressRepo addressRepo;
//
//	@Autowired
//	RoleRepo roleRepo;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	public void addCustomer(){
//		User user=new User();
//		Role role= new Role();
//		role= roleRepo.findById(1L).get();
//		Customer customer= new Customer();
//		user.setEmail("ayushsharma@gmail.com");
//		user.setFirstName("ayush");
//		user.setMiddleName("kumar");
//		user.setLastName("sharma");
//		user.setPassword("password@1234");
//		user.setRole(role);
//		customer.setContact(1234567890);
//		customer.setUser(user);
//		List<Address> addresses= new ArrayList<>();
//		Address address1= new Address();
//		address1.setCity("Shastri Nagar");
//		address1.setState("Delhi");
//		address1.setZipCode(110052);
//		address1.setAddressLine("749");
//		address1.setLabel("Home");
//		address1.setCountry("India");
//		address1.setCustomer(customer);
////		address1.setCustomer(customer);
//		addresses.add(address1);
//
//		Address address2= new Address();
//		address2.setCity("Rohini");
//		address2.setState("Delhi");
//		address2.setZipCode(110039);
//		address2.setAddressLine("1234");
//		address2.setLabel("Office");
//		address2.setCountry("India");
//		address2.setCustomer(customer);
////		address2.setCustomer(customer);
//		addresses.add(address2);
//
//		customer.setAddresses(addresses);
//
////		System.out.println(role.getRole());
////		Set<Role> roles = new HashSet<>();
//////		role.getRole();
////		Role role= roleRepo.findByRole("Customer");
////		roles.add(role);
////		customer.setRoles(roles);
//		customerRepo.save(customer);
//	}
//
//	@Test
//	public void addSeller(){
//
//		User user= new User();
//		Seller seller= new Seller();
//		Role role= new Role();
//		role= roleRepo.findById(1L).get();
//		user.setEmail("tarun@gmail.com");
//		user.setFirstName("Tarun");
//		user.setMiddleName("K");
//		user.setLastName("Singh");
//		user.setPassword("1234");
//		user.setRole(role);
//		seller.setGst("abcgst123");
//		seller.setCompanyName("TTN");
//		seller.setCompanyContact(12345678);
//		seller.setUser(user);
////		Set<Address> addresses= new HashSet<>();
//		Address address1= new Address();
//		address1.setCity("Noida");
//		address1.setState("UP");
//		address1.setZipCode(121032);
//		address1.setAddressLine("7987");
//		address1.setLabel("Office");
//		address1.setCountry("India");
//		address1.setSeller(seller);
////		address1.setCustomer(seller);
////		addresses.add(address1);
//		seller.setAddress(address1);
//		sellerRepo.save(seller);
//	}
//
//
//	@Test
//	public void retrieveCustomerAddressByFirstName(){
//		User user=new User();
//		user=userRepository.findByFirstName("ayush");
//		long userId= user.getId();
//		Customer customer=new Customer();
//		customer=customerRepo.findByUser_Id(userId);
//		long customerId= customer.getId();
//		List<Address> addressList= new ArrayList<>();
//		addressList=addressRepo.findByCustomer_ID(customerId);
//		addressList.forEach(address -> System.out.println(address.toString()));
//	}
//
//}
