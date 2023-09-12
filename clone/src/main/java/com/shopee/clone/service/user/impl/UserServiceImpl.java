package com.shopee.clone.service.user.impl;

import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm cập nhật thông tin người dùng
    public ResponseEntity<?> updateUser(long userId, UserUpdateDTO userUpdateDTO) {
        try {
            // Tìm kiếm người dùng trong cơ sở dữ liệu bằng userId
            Optional<UserEntity> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                // Lấy đối tượng người dùng từ Optional
                UserEntity user = optionalUser.get();

                // Cập nhật thông tin người dùng từ DTO (Data Transfer Object)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setUserName(userUpdateDTO.getUserName());
                user.setFullName(userUpdateDTO.getFullName());
                user.setPhone(userUpdateDTO.getPhone());
                user.setAvatar(userUpdateDTO.getAvatar());
                user.setEmail(userUpdateDTO.getEmail());
                user.setDataOfBirth(userUpdateDTO.getDateOfBirth());

                // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
                userRepository.save(user);

                // Trả về ResponseEntity chứa thông tin cập nhật thành công
                return ResponseEntity.ok().body(new ResponseObject("SUCCESS",
                        "User updated successfully",user));
            } else {
                // Trả về ResponseEntity chứa thông tin lỗi nếu không tìm thấy người dùng
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("User not found")
                                .results("")
                                .build());
            }
        } catch (Exception e) {
            // Trả về ResponseEntity chứa thông tin lỗi nếu có lỗi xảy ra
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build());
        }
    }
    @Override
    public Optional<UserEntity> findUserByID(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }



}
