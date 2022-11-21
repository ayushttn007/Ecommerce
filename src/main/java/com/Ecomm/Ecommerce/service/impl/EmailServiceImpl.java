package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.dto.PasswordDto;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.entities.VerificationToken;
import com.Ecomm.Ecommerce.handler.InvalidTokenException;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.repository.VerificationTokenRepository;
import com.Ecomm.Ecommerce.service.EmailService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;


@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    UserRepo userRepo;
    @Autowired
    VerificationTokenRepository verificationTokenRepo;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendEmail(User user,String mailMessage){
        logger.info("SendEmail Executed");
        String toEmail = user.getEmail();
        String senderEmail = fromEmail;
        String senderName = "Ecommerce Application";
        String subject = "Thank you for Registration";
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(mailMessage);

        javaMailSender.send(message);
    }

    public void sendEmailSeller(User user){
        logger.info("SendEmailSeller Executed");
       String  message = "Dear [[name]]"
               + "Congratulations, Your account has been Created and Waiting for Approval."
               + "Thank you."
               + "Ecommerce Application.";

        message = message.replace("[[name]]", user.getFirstName());
            sendEmail(user,message);

    }

    public void sendEmailCustomer(User user,String siteUrl){
        logger.info("SendEmailCustomer Executed");
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepo.save(verificationToken);
        String emailMessage = "Dear [[name]], <br>"
                + "Please click the link below to verify your registration:<br>" +
                "This link is valid only for 5 minutes."
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Ecommerce Application.";

        String verifyURL = siteUrl + "/confirm?token=" + (verificationToken.getVerificationToken());
        emailMessage = emailMessage.replace("[[URL]]", verifyURL);
        sendEmail(user,emailMessage);

    }

    public void sendEmailForgotPassword(User user) {
        logger.info("sendEmailForgetPassword Executed");
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepo.save(verificationToken);
        String emailMessage = "Dear [[name]]"+ "\n" +
                "We have received a request to Reset your password." +"/n"
                + "Please click on the following link, (or paste this in your browser) to complete the process within five minutes of receiving it"+"\n\n"
                +"[[URL]]"+ "\n\n"
                +"Regards,\n" +
                "Ecommerce Application";
        String verifyURL = "localhost:8080" + "/reset_password?token=" + (verificationToken.getVerificationToken());
        emailMessage = emailMessage.replace("[[URL]]", verifyURL);
        sendEmail(user,emailMessage);
    }

    // Method to verify user email verification token
    public String verifyVerificationToken(String token, String SiteUrl){
        logger.info("verifyVerificationToken Executed");
        // take verification Token from verificationToken table using findByVerification query
        VerificationToken verificationToken = verificationTokenRepo.findByVerificationToken(token);
        // check if verificationToken not exists
        if (verificationToken == null) {
            return "Invalid Token";
        }
        User user = userRepo.findByEmail(verificationToken.getUser().getEmail());
        if (user == null) {
            return "Invalid Token";
        } else if (user.isActive()) {
            return "Account is Already Verified";
        } else {
            Calendar calendar = Calendar.getInstance();
            if (verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
                User newUser = verificationToken.getUser();
                verificationTokenRepo.delete(verificationToken);
                logger.info(SiteUrl);  // for debuging purpose
                sendEmailCustomer(newUser,SiteUrl);
                return "Verification link is Expired.Please check your mail new Verification link has been sent to your email ";
            }
            user.setActive(true);
            userRepo.save(user);
            verificationTokenRepo.delete(verificationToken);
            return "Congratulations, your account has been verified.";
        }

    }

    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");

    }

    public String regenerateToken(String userEmail, String siteUrl){
        logger.info("regenerateToken Executed");
        User user = userRepo.findByEmail(userEmail);
        // check if user exists
        if (user == null) {
            return "Account does not exists";
        }
        // check is user account is already active
        else if (user.isActive()) {
            return "your account is Already Verified";
        } else {
            VerificationToken token = verificationTokenRepo.findByUser(user);

            // delete token if already exists
            if (token != null) {
                verificationTokenRepo.delete(token);
                sendEmail(user, siteUrl);
            }
            // else send new mail
            else {
                sendEmailCustomer(user, siteUrl);
            }
        }
        return "Check your mail for Account Verification link";
    }

   // Method to verify token & give next steps to reset password
    public String resetPasswordEmail(String token, String userPassword, String userConfirmPassword) {
        // get token from verification repo
        VerificationToken verificationToken = verificationTokenRepo.findByVerificationToken(token);
        // check
        if(verificationToken == null){
            throw new InvalidTokenException("Given Token is invalid");
        }
        else{
            User user = userRepo.findByEmail(verificationToken.getUser().getEmail());
            Calendar calendar = Calendar.getInstance();
            if(verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0){
                verificationTokenRepo.delete(verificationToken);
                throw new InvalidTokenException("Token has been expired");
            }else{
                if(!(userPassword.equals(userConfirmPassword))){
                    throw new PasswordNotMatchedException("Password do not Matched");
                }
                String userEncodePassword = passwordEncoder.encode(userConfirmPassword);
                user.setPassword(userEncodePassword);
                user.setPasswordUpdateDate(new Date());
                userRepo.save(user);
                verificationTokenRepo.delete(verificationToken);
                sendPasswordChangeMail(user);
                return "Password Changed Successfully";
            }

        }
    }

    // Method to send successful password change mail.
    public void sendPasswordChangeMail(User user) {
        String  message = "Dear [[name]]"
                + "Congratulations, Your Password Changed Successfully."
                + "Thank you."
                + "Ecommerce Application.";

        message = message.replace("[[name]]", user.getFirstName());
        sendEmail(user,message);
    }
}
