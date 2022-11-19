package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "Email")
    private String email;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Middle_Name")
    private String middleName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Password")
    private String password;

    @Column(name = "Is_Deleted")
    private boolean isDeleted;

    @Column(name = "Is_Active")
    private boolean isActive;

    @Column(name = "Is_Expired")
    private boolean isExpired;

    @Column(name = "Is_Locked")
    private boolean isLocked;

    @Column(name = "Invalid_Attempt_Count")
    private int invalidAttemptCount;

    @Column(name = "Password_Update_Date")
    private Date passwordUpdateDate;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Seller seller;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private verifications verifications;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "User_Role"
            ,joinColumns = {@JoinColumn(name = "User_id", referencedColumnName = "id")}
            ,inverseJoinColumns = {@JoinColumn(name = "Role_id", referencedColumnName = "id")})
    private Role role;




}