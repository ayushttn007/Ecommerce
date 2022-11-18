package com.Ecomm.Ecommerce.utils;

import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repos.UserRepo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    UserRepo userRepo;

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
//       System.out.println("Current time of the day using Calendar - 2dd-MM-yyyy HH:mm:ss format: "+ formattedDate);
    }
    public void register(User user, String siteUrl)throws UnsupportedEncodingException, MessagingException {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setCreated_at(getCurrentTimeUsingCalendar());
        user.setActive(false);

       //  userRepo.save(user);
        sendEmailVerification(user, siteUrl);
    }

    public boolean verify(String verificationCode)  {
        boolean isVerify = true;
        User user = userRepo.findByVerificationCode(verificationCode);
        if (user == null || user.isActive()) {
          return   isVerify = false;
        }
        String verificationTokenDate = user.getCreated_at();
//        String currentDate = emailService.getCurrentTimeUsingCalendar();
        String currentDate = getCurrentTimeUsingCalendar();
        boolean istokenActive = finddateAndtimeDifference(verificationTokenDate,currentDate);

        if(!istokenActive){
            user.setVerificationCode(null);
            user.setCreated_at(null);
            isVerify = false;
        }else {
            user.setCreated_at(null);
            user.setVerificationCode(null);
            user.setActive(true);
        }
        userRepo.save(user);
        return isVerify;
    }


    public boolean finddateAndtimeDifference(String verificationTokenDate,
                                             String currentDate)  {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Try Class

        // parse method is used to parse
        // the text from a string to
        // produce the date
        try {
            Date tokenDate = sdf.parse(verificationTokenDate);
            Date currentdate = sdf.parse(currentDate);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time = currentdate.getTime() - tokenDate.getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days
//            long difference_In_Seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time) % 60;

            long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;

//            long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;

            long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

            long difference_In_Years = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365l;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            boolean isValid;
            if (difference_In_Years > 0 || difference_In_Days > 0) {
                isValid = false;
            } else if (difference_In_Minutes > 1) {
                isValid = false;
            } else {
                isValid = true;
            }
            return isValid;
        }catch (ParseException e) {
            e.printStackTrace();
        }

        // Print result
//                  long leftminutes =  difference_In_Years  + difference_In_Days + difference_In_Hours + difference_In_Minutes + difference_In_Seconds;
//                  return leftminutes;


        return false;
    }
}
