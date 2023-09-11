package com.shopee.clone.rest_controller.admin;

import com.shopee.clone.converter.UserConverter;
import com.shopee.clone.domain.User;
import com.shopee.clone.security.filter.JWTFilter;
import com.shopee.clone.service.auth.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTFilter jwtFilter;
    @GetMapping("user")
    public List<User> getUser(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       jwtFilter.doFilter( request,  response, filterChain);
        return userService.getListUser();
    }
    @GetMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id){
        userService.delete(id);
        return ResponseEntity.ok("Delete success");
    }
}
