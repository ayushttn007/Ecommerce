package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.dto.UserDto;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.entities.VerificationToken;
import com.Ecomm.Ecommerce.handler.InvalidTokenException;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.repository.VerificationTokenRepository;
import com.Ecomm.Ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    UserRepo userRepo;

    @Autowired
    VerificationTokenRepository verificationTokenRepo;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException{
        VerificationToken verificationToken= new VerificationToken(user);
        verificationTokenRepo.save(verificationToken);
         String toAddress = user.getEmail();
         String fromAddress = fromEmail;
         String senderName = "Ecommerce Application";
         String subject = "Please verify your registration";
         String emailMessage = "Dear [[name]], <br>"
                 +"Please click the link below to verify your registration:<br>"+
                 "This link is valid only for 5 minutes."
                 + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                 + "Thank you,<br>"
                 + "Ecommerce Application.";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        emailMessage = emailMessage.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/confirm?token=" + (verificationToken.getVerificationToken());


        emailMessage = emailMessage.replace("[[URL]]", verifyURL);

        helper.setText(emailMessage, true);

        javaMailSender.send(message);
    }

    public String verifyVerificationToken(String token,String SiteUrl) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = verificationTokenRepo.findByVerificationToken(token);
        if(verificationToken == null){
            return "Invalid Token";
        }
        User user = userRepo.findByEmail(verificationToken.getUser().getEmail());
        if(user == null){
            return "Invalid Token";
        }
        else if (user.isActive()) {
            return "Account is Already Verified";
        }else{
            Calendar calendar = Calendar.getInstance();
            if(verificationToken.getExpiryDate().getTime()-calendar.getTime().getTime()<=0){
                User newUser = verificationToken.getUser();
                verificationTokenRepo.delete(verificationToken);
                sendEmail(newUser,SiteUrl);
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

    public String regenerateToken(String userEmail,String siteUrl) throws MessagingException, UnsupportedEncodingException {
        User user = userRepo.findByEmail(userEmail);
        // check if user exists
        if(user == null) {
            return "Account does not exists";
        }
        // check is user account is already active
        else if(user.isActive()){
            return "your account is Already Verified";
        }else{
            VerificationToken token = verificationTokenRepo.findByUser(user);

            // delete token if already exists
            if(token!=null){
                verificationTokenRepo.delete(token);
                sendEmail(user, siteUrl);
            }
            // else send new mail
            else{
                sendEmail(user,siteUrl);
            }
        }
        return "Check your mail for Account Verification link";
    }
}
