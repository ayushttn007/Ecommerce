package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.handler.AccountNotActive;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.repository.VerificationTokenRepository;
import com.Ecomm.Ecommerce.service.UserAccountService;
import com.Ecomm.Ecommerce.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private TokenStore tokenStore;

   @Autowired
    UserRepo userRepo;

   @Autowired
    EmailServiceImpl emailService;

   @Autowired
    VerificationTokenRepository verificationTokenRepository;

    public String userLogin(String userEmail, String userPassword){
           return "demostring";
    }
    public ResponseEntity<String> userLogout(HttpServletRequest request){
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.contains("Bearer")) {
                String tokenValue = authorization.replace("Bearer", "").trim();

                OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
                tokenStore.removeAccessToken(accessToken);

                //OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);
                OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
                tokenStore.removeRefreshToken(refreshToken);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid access token");
        }


        return ResponseEntity.ok().body("Logout successfully");
    }


    public String userForgotPassword(String email){
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new BadCredentialsException("Invalid Email");
        }else{
            if(user.isActive()){
                VerificationToken verificationToken = verificationTokenRepository.findByUser(user);

                if(verificationToken != null){
                    verificationTokenRepository.delete(verificationToken);
                     emailService.sendEmailForgotPassword(user);

                }else{
                    emailService.sendEmailForgotPassword(user);
                }
            }else{
                throw new AccountNotActive("Account Associated with the given email is not Active");
            }
        }
        return "Please check your email.An email has been sent to the given email";
    }

}
