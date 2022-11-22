package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.handler.AccountNotActive;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.repository.VerificationTokenRepository;
import com.Ecomm.Ecommerce.service.UserAccountService;
import com.Ecomm.Ecommerce.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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
    MessageSource messageSource;

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
            return ResponseEntity.badRequest().body(
                   messageSource.getMessage("api.error.InvalidAccessToken",null, Locale.ENGLISH)
            );
        }


        return ResponseEntity.ok().body(
                messageSource.getMessage("api.response.logOut",null,Locale.ENGLISH)
        );
    }


    public String userForgotPassword(String email){
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new BadCredentialsException(
                    messageSource.getMessage("api.error.userNotFound",null,Locale.ENGLISH)
            );
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
                throw new AccountNotActive(
                        messageSource.getMessage("api.error.accountNotActive",null,Locale.ENGLISH)
                );
            }
        }
        return messageSource.getMessage("api.response.checkMailForgotPassword",null,Locale.ENGLISH);
    }

}
