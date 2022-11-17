//package com.Ecomm.Ecommerce.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//public class verifications {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "token_id")
//    private  int id;
//
//    private String token;
//
//    private DateTimeFormat created_at;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "User_Id")
//    private User user;
//
//}
