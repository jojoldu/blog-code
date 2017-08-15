package com.blogcode.security.config;

import com.blogcode.security.GoogleUser;
import com.blogcode.security.domain.User;
import com.blogcode.security.domain.UserRepository;
import com.blogcode.security.domain.UserRole;
import com.blogcode.security.domain.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by jojoldu@gmail.com on 2017. 8. 3.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Component
public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private HttpSession httpSession;
    private ObjectMapper objectMapper;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public GoogleAuthenticationSuccessHandler(HttpSession httpSession, ObjectMapper objectMapper, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.httpSession = httpSession;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        httpSession.setAttribute(SessionConstants.LOGIN_USER, getUser(getGoogleUser(authentication))); // 간단한 구글계정 정보를 세션에 저장
        response.sendRedirect("/me");
    }

    private GoogleUser getGoogleUser(Authentication authentication) { // OAuth 인증정보를 통해 GoogleUser 인스턴스 생성
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        return objectMapper.convertValue(oAuth2Authentication.getUserAuthentication().getDetails(), GoogleUser.class);
    }

    private User getUser(GoogleUser google){
        User savedUser = userRepository.findByEmail(google.getEmail());

        if(savedUser == null){
            User newUser = google.toEntity();
            newUser.addRole(userRoleRepository.findDefaultRole());
            savedUser = userRepository.save(newUser);
        }

        return savedUser;
    }
}
