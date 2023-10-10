package com.shopee.clone.service.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.register.AddChatIdRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> login(LoginDTO loginDTO);
    ResponseEntity<?> register(RegisterDTO registerDTO);
    ResponseEntity<?> addChatId(Long id, AddChatIdRequest addChatIdRequest);
    ResponseEntity<?> refreshToken(String refreshToken);
}
