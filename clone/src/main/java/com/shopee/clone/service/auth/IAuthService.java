package com.shopee.clone.service.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> login(LoginDTO loginDTO, HttpServletResponse response);
//    ResponseEntity<?> logout();
}
