package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


//    public ResponseEntity<String> ForgotPassword(){
//        return ResponseEntity.ok().body("Password");
//    }

}
