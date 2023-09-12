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
    @GetMapping("delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok("Delete thanh cong!");
    }
}
