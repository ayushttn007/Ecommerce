package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.entities.User;
import javax.servlet.http.HttpServletRequest;


public interface EmailService {
    public void sendEmail(User user, String mailMessage);

    public String verifyVerificationToken(String token,String SiteUrl);


    public void sendEmailSeller(User user);
    public void sendEmailCustomer(User user,String siteUrl);

    public void sendEmailForgotPassword(User user);

    public void sendPasswordChangeMail(User user);

    public String resetPasswordEmail(String token, String userPassword, String userConfirmPassword);
    public String getSiteURL(HttpServletRequest request);

    public String regenerateToken(String userEmail, String siteUrl);


}
