package com.shopee.clone.rest_controller.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final IAuthService authService;

    @Autowired
    private IUploadImageService uploadImageService;

    public AuthRestController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO){
        return authService.register(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){
        return authService.login(loginDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

//    @PostMapping("/images")
//    public ResponseEntity<?> upload(@RequestParam(name = "images") MultipartFile[] images){
//        try{
//            uploadImageService.uploadMultipleProductImage(images);
//            return null;
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(ResponseObject.builder().status("FAIL").message(e.getMessage()).results(""));
//        }
//
//    }
}
