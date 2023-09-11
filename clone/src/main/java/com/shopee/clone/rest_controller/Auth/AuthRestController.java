package com.shopee.clone.rest_controller.Auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;

import com.shopee.clone.domain.User;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.service.auth.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final IAuthService authService;
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO, HttpServletResponse response){
        return authService.login(loginDTO,response);
    }
    @PostMapping("register")
    public ResponseEntity<?> register (@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user)+ "Dang ky thanh cong");
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, @CookieValue(name = "authCookie", required = false) String cookieValue) {

        if (cookieValue != null) {
            // Xóa cookie bằng cách đặt lại nó với thời gian sống là 0
            Cookie authCookie = new Cookie("accessToken", "");
            authCookie.setMaxAge(0);
            response.addCookie(authCookie);
        }
        return "Logged out successfully";
    }

}
