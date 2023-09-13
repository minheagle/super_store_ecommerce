package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.user.ChangePasswordDTO;
import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    @Autowired
    private UserService userService;
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userId, userUpdateDTO);
    }
    @PutMapping("change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(id,changePasswordDTO);
    }
}