package com.shopee.clone.rest_controller.admin;

import com.shopee.clone.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @GetMapping("/list-user")
    public ResponseEntity<?> getListUser(){
        return ResponseEntity.ok(userService.getListUser());
    }
    @GetMapping("/ban-user/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id){
        return userService.blockUser(id);
    }

    @GetMapping("/unban-user/{id}")
    public ResponseEntity<?> unBanUser(@PathVariable Long id){
        return userService.unBlockUser(id);
    }
}
