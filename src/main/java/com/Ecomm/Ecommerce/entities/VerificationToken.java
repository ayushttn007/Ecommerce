package com.Ecomm.Ecommerce.entities;

import com.Ecomm.Ecommerce.utils.Auditable;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class VerificationToken  extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private long tokenId;

    @Column(name="confirmation_token")
    private String verificationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    static int expiryTime = 15;
    public VerificationToken(User user) {
        this.user = user;
        createdDate = new Date();
        verificationToken = UUID.randomUUID().toString();
        expiryDate = calculateExpiryDate(expiryTime);

    }

    private Date calculateExpiryDate(int expiryTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE,expiryTime);
        return new Date(calendar.getTime().getTime());
    }
    public VerificationToken() {

    }
}