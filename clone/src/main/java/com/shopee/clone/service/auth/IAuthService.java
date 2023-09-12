package com.shopee.clone.service.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> login(LoginDTO loginDTO);
    ResponseEntity<?> register(RegisterDTO registerDTO);
    ResponseEntity<?> refreshToken(String refreshToken);
    ResponseEntity<?> logout(long user_id);
}
