package com.shopee.clone.rest_controller.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.service.auth.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final IAuthService authService;

    public AuthRestController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO){
        return authService.register(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

    @PostMapping("/logout/{user_id}")
    public ResponseEntity<?> logout(@Valid @PathVariable long user_id){
        return authService.logout(user_id);
    }
}
