package com.Ecomm.Ecommerce.utils;

import com.Ecomm.Ecommerce.entities.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmailVerification(User user, String siteURL) throws MessagingException, UnsupportedEncodingException{
         String toAddress = user.getEmail();
         String fromAddress = fromEmail;
         String senderName = "Ecommerce Application";
         String subject = "Please verify your registration";
         String content = "Dear [[name]], <br>"
                 +"Please click the link below to verify your registration:<br>"
                 + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                 + "Thank you,<br>"
                 + "Ecommerce Application.";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        javaMailSender.send(message);
    }

    public  String getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate=dateFormat.format(date);
       return formattedDate;
//       System.out.println("Current time of the day using Calendar - 24 hour format: "+ formattedDate);
    }
    public void register(User user, String siteUrl)throws UnsupportedEncodingException, MessagingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setCreated_at(getCurrentTimeUsingCalendar());
        user.setActive(false);

       //  userRepo.save(user);

    }
}
