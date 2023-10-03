package com.shopee.clone.rest_controller.user;

import com.shopee.clone.DTO.auth.user.*;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.validate.RegisterDTOValidate;
import com.shopee.clone.validate.UpdateDTOValidate;
import com.shopee.clone.validate.seller.BecomeSellerRequestValidate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private UpdateDTOValidate updateDTOValidate;
    @Autowired
    private BecomeSellerRequestValidate becomeSellerRequestValidate;
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid
    @RequestBody UserUpdateDTO userUpdateDTO, BindingResult bindingResult) {
        updateDTOValidate.validate(userId,userUpdateDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return userService.updateUser(userId, userUpdateDTO);
    }
    @PutMapping("/update-address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @RequestBody UpdateAddressDTO updateAddressDTO) {
        return userService.updateAddress(id, updateAddressDTO);
    }
    @PostMapping("/add-address/{id}")
    public ResponseEntity<?> addAddress(@PathVariable Long id, @RequestBody UpdateAddressDTO updateAddressDTO) {
        return userService.addAddress(id, updateAddressDTO);
    }
    @PutMapping("change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(id,changePasswordDTO);
    }

    @PostMapping("/upload-avatar/{id}")
    public ResponseEntity<?> uploadAvatar(@PathVariable("id") Long userId, @Valid ChangeAvatarRequest changeAvatarRequest){
        return userService.changeAvatar(userId, changeAvatarRequest);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){
       return userService.findUserByUserName(userName);
    }

    @PostMapping("/{userId}/become-seller")
    public ResponseEntity<?> becomeSeller(@PathVariable("userId") Long userId,
                                          @RequestBody BecomeSellerRequest becomeSellerRequest,
                                          BindingResult bindingResult){
        becomeSellerRequestValidate.validate(becomeSellerRequest,bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return userService.becomeSellerService(userId, becomeSellerRequest);
    }
}