package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.DTO.auth.user.ChangePasswordDTO;
import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userId, userUpdateDTO);
    }
    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO);
    }
}