package com.shopee.clone.service.user;

import com.shopee.clone.DTO.auth.user.ChangePasswordDTO;
import com.shopee.clone.DTO.auth.user.User;
import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<UserEntity> findUserByID(Long id);
    UserEntity save(UserEntity user);

    List<User> getListUser();
    void delete(Long id);

    ResponseEntity<?> updateUser(long userId, UserUpdateDTO userUpdateDTO);

    ResponseEntity<?> blockUser(Long id);

    ResponseEntity<?> unBlockUser(Long id);

    ResponseEntity<?> changePassword(ChangePasswordDTO changePasswordDTO);
}
