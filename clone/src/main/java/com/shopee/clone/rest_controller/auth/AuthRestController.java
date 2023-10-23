package com.shopee.clone.rest_controller.auth;

import com.shopee.clone.DTO.auth.login.LoginDTO;
import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenRequest;
import com.shopee.clone.DTO.auth.register.AddChatIdRequest;
import com.shopee.clone.DTO.auth.register.RegisterDTO;
import org.springframework.validation.BindingResult;
import com.shopee.clone.service.auth.IAuthService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.validate.RegisterDTOValidate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final IAuthService authService;
    @Autowired
    private RegisterDTOValidate registerDTOValidate;
    @Autowired
    private IUploadImageService uploadImageService;

    public AuthRestController(IAuthService authService) {
        this.authService = authService;
    }

    @Validated
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO ,BindingResult bindingResult){
        // Kiểm tra dữ liệu và xử lý lỗi nếu cần
        registerDTOValidate.validate(registerDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return authService.register(registerDTO);
    }

    @PostMapping("/add-chat-id/{id}")
    public ResponseEntity<?> addChatId(@PathVariable Long id, @RequestBody AddChatIdRequest addChatIdRequest){
        return authService.addChatId(id, addChatIdRequest);
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
