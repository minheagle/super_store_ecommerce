package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.service.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @GetMapping("")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }
}
