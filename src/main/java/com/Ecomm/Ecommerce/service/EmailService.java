package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.InvalidTokenException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendEmail(User user, String mailMessage);

    public String verifyVerificationToken(String token,String SiteUrl);


    public void sendEmailSeller(User user);
    public void sendEmailCustomer(User user,String siteUrl);
    public String getSiteURL(HttpServletRequest request);


}
