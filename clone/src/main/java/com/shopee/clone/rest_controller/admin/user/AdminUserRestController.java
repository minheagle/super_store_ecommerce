package com.shopee.clone.rest_controller.admin.user;

import com.shopee.clone.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/users")
public class AdminUserRestController {
    private final UserService userService;

    public AdminUserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list-user")
    public ResponseEntity<?> getListUser(){
        return userService.getListUser();
    }
    @GetMapping("/list-seller")
    public ResponseEntity<?> getListSeller(){
        return userService.getListSeller();
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
