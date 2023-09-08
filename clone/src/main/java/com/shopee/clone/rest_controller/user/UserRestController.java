package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.entity.mongodb.user.User;
import com.shopee.clone.entity.mysql.customer.Customer;
import com.shopee.clone.repository.mongodb.user.IUserRepository;
import com.shopee.clone.repository.mysql.customer.CustomerRepository;
import com.shopee.clone.service.auth.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
