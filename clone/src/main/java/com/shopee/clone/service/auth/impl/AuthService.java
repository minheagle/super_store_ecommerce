package com.shopee.clone.service.auth.impl;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.converter.CustomUserDetails;
import com.shopee.clone.domain.User;
//import com.shopee.clone.entity.mongodb.user.User;
import com.shopee.clone.repository.UserRepository;
//import com.shopee.clone.repository.mongodb.user.IUserRepository;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.util.JWTProvider;
import com.shopee.clone.util.ResponseObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JWTProvider jwtProvider;


    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateJwtToken(authentication);
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
// Lưu token vào cookie hoặc header của phản hồi
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new ResponseObject(token, "SUCCESS", "", userDetail));
    }

}
