package com.shopee.clone.service.auth.impl;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.entity.mongodb.user.User;
import com.shopee.clone.repository.mongodb.user.IUserRepository;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.util.JWTProvider;
import com.shopee.clone.util.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    public AuthService(AuthenticationManager authenticationManager, IUserRepository userRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateJwtToken(authentication);
        User userDetail = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new ResponseObject(token, "SUCCESS", "", userDetail));
    }
}
