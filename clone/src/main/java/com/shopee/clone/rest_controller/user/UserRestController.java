package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.login.LoginDTO;

//import com.shopee.clone.entity.UserEntity;
//import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.service.auth.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserRestController {
    private final IAuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }

}
