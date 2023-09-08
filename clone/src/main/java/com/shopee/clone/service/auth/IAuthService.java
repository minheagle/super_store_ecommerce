package com.shopee.clone.service.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> login(LoginDTO loginDTO);
}
