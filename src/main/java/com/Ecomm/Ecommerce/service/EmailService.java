package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.InvalidTokenException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    public void sendEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;

    public String verifyVerificationToken(String token,String SiteUrl) throws MessagingException, UnsupportedEncodingException, InvalidTokenException;

    public String getSiteURL(HttpServletRequest request);


}
