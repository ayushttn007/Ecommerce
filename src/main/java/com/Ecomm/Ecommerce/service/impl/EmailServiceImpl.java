package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.dto.PasswordDto;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.entities.VerificationToken;
import com.Ecomm.Ecommerce.handler.InvalidTokenException;
import com.Ecomm.Ecommerce.handler.PasswordNotMatchedException;
import com.Ecomm.Ecommerce.handler.UserNotFoundException;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.repository.VerificationTokenRepository;
import com.Ecomm.Ecommerce.service.EmailService;
import net.bytebuddy.asm.Advice;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

    @Autowired
    MessageSource messageSource;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendEmail(User user,String mailMessage,String subjectMessage){
        logger.info("SendEmail Executed");
        String toEmail = user.getEmail();
        String senderEmail = fromEmail;
        String subject = subjectMessage;
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(mailMessage);

        javaMailSender.send(message);
    }


    // Method to verify user email verification token
    public String verifyVerificationToken(String token, String SiteUrl){
        logger.info("verifyVerificationToken Executed");
        // take verification Token from verificationToken table using findByVerification query
        VerificationToken verificationToken = verificationTokenRepo.findByVerificationToken(token);
        // check if verificationToken not exists
        if (verificationToken == null) {
            throw new InvalidTokenException(
                    messageSource.getMessage("api.error.InvalidToken",null,Locale.ENGLISH)
            );
        }
        User user = userRepo.findByEmail(verificationToken.getUser().getEmail());
        if (user == null) {
            throw new InvalidTokenException(
                  messageSource.getMessage("api.error.InvalidToken",null,Locale.ENGLISH)
            );
        } else if (user.isActive()) {
            return messageSource.getMessage("api.response.user.AccountVerified",null,Locale.ENGLISH);
        } else {
            Calendar calendar = Calendar.getInstance();
            if (verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
                User newUser = verificationToken.getUser();
                verificationTokenRepo.delete(verificationToken);
                logger.info(SiteUrl);  // for debuging purpose
                sendEmailCustomer(newUser,SiteUrl);
                return messageSource.getMessage("api.response.verifyVerificationToken.isExpired",null,Locale.ENGLISH);
            }
            user.setActive(true);
            userRepo.save(user);
            verificationTokenRepo.delete(verificationToken);
            return messageSource.getMessage("api.response.accountVerified",null,Locale.ENGLISH);
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
            throw new UserNotFoundException(
                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
            );
        }
        // check is user account is already active
        else if (user.isActive()) {
            return messageSource.getMessage("api.response.user.AccountVerified",null,Locale.ENGLISH);
        } else {
            VerificationToken token = verificationTokenRepo.findByUser(user);
            // delete token if already exists
            if (token != null) {
                verificationTokenRepo.delete(token);
                sendEmail(user, siteUrl , "");
            }
            // else send new mail
            else {
                sendEmailCustomer(user, siteUrl);
            }
        }
        return messageSource.getMessage("api.response.checkMail",null,Locale.ENGLISH);
    }

   // Method to verify token & give next steps to reset password
    public String resetPasswordEmail(String token, String userPassword, String userConfirmPassword) {
        // get token from verification repo
        VerificationToken verificationToken = verificationTokenRepo.findByVerificationToken(token);
        // check
        if(verificationToken == null){
            throw new InvalidTokenException(
                    messageSource.getMessage("api.error.InvalidToken",null,Locale.ENGLISH)
            );
        }
        else{
            User user = userRepo.findByEmail(verificationToken.getUser().getEmail());
            Calendar calendar = Calendar.getInstance();
            if(verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0){
                verificationTokenRepo.delete(verificationToken);
                throw new InvalidTokenException(
                        messageSource.getMessage("api.error.tokenExpired",null,Locale.ENGLISH)
                );
            }else{
                if(!(userPassword.equals(userConfirmPassword))){
                    throw new PasswordNotMatchedException(
                            messageSource.getMessage("api.error.passwordNotMatched",null,Locale.ENGLISH)
                    );

                }
                String userEncodePassword = passwordEncoder.encode(userConfirmPassword);
                user.setPassword(userEncodePassword);
                user.setPasswordUpdateDate(new Date());
                userRepo.save(user);
                verificationTokenRepo.delete(verificationToken);
                sendPasswordChangeMail(user);
                return messageSource.getMessage("api.response.passwordChanged",null,Locale.ENGLISH);
            }

        }
    }


    public void sendEmailSeller(User user){
        logger.info("SendEmailSeller Executed");
        String  message = messageSource.getMessage("api.response.seller.Register",null,Locale.ENGLISH);
        String subject = messageSource.getMessage("api.response.seller.Register.subject",null,Locale.ENGLISH);
        message = message.replace("[[name]]", user.getFirstName());
        sendEmail(user,message,subject);

    }

    public void sendEmailCustomer(User user,String siteUrl){
        logger.info("SendEmailCustomer Executed");
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepo.save(verificationToken);
        String emailMessage = messageSource.getMessage("api.response.user.userRegister",null,Locale.ENGLISH);
        String subject = messageSource.getMessage("api.response.user.userVerifyAccount.subject",null,Locale.ENGLISH);
        String verifyURL = siteUrl + "/confirm?token=" + (verificationToken.getVerificationToken());
        emailMessage = emailMessage.replace("[[name]]", user.getFirstName());
        emailMessage = emailMessage.replace("[[URL]]", verifyURL);
        sendEmail(user,emailMessage,subject);

    }


    public void sendEmailForgotPassword(User user) {
        logger.info("sendEmailForgetPassword Executed");
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepo.save(verificationToken);
        String emailMessage = messageSource.getMessage("api.response.user.userForgotPassword",null,Locale.ENGLISH);
        String subject = messageSource.getMessage("api.response.user.userForgotPassword.subject",null,Locale.ENGLISH);
        String verifyURL = messageSource.getMessage("api.response.user.resetPasswordVerifyUrl",null,Locale.ENGLISH) + (verificationToken.getVerificationToken());
        emailMessage = emailMessage.replace("[[name]]", user.getFirstName());
        emailMessage = emailMessage.replace("[[URL]]", verifyURL);
        sendEmail(user,emailMessage,subject);
    }

    // Method to send successful password change mail.
    public void sendPasswordChangeMail(User user) {
        String message = messageSource.getMessage("api.response.user.passwordUpdate.successful",null, Locale.CHINA);
        String subject = messageSource.getMessage("api.response.user.passwordUpdate.subject",null,Locale.ENGLISH);
        message = message.replace("[[name]]", user.getFirstName());
        sendEmail(user,message,subject);
    }

    public void sendUserActiveMail(User user) {
        String  message = messageSource.getMessage("api.response.user.userActive.successful",null, Locale.ENGLISH);
        String subject = messageSource.getMessage("api.response.user.userActive.subject",null, Locale.ENGLISH);
        message = message.replace("[[name]]", user.getFirstName());
        sendEmail(user,message,subject);
    }

    public void sendUserDeactivedMail(User user) {
        String  message = messageSource.getMessage("api.response.user.userDeActive",null,Locale.ENGLISH);
        String subject = messageSource.getMessage("api.response.user.userDeActive.subject",null,Locale.ENGLISH);
        message = message.replace("[[name]]", user.getFirstName());
        sendEmail(user,message,subject);
    }
}
