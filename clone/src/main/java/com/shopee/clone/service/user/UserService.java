package com.shopee.clone.service.user;

import com.shopee.clone.DTO.auth.user.*;
import com.shopee.clone.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<UserEntity> findUserByID(Long id);
    UserEntity save(UserEntity user);

    ResponseEntity<?> getListUser(Pageable pageable);
    void delete(Long id);

    ResponseEntity<?> updateUser(long userId, UserUpdateDTO userUpdateDTO);
    ResponseEntity<?> changeAvatar(long userId, ChangeAvatarRequest changeAvatarRequest);

    ResponseEntity<?> blockUser(Long id);

    ResponseEntity<?> unBlockUser(Long id);

    ResponseEntity<?> changePassword(Long id,ChangePasswordDTO changePasswordDTO);

    ResponseEntity<?> updateAddress(Long id, UpdateAddressDTO updateAddressDTO);

    ResponseEntity<?> findUserByUserName(String userName);

    ResponseEntity<?> getUserById(Long id);
    ResponseEntity<?> becomeSellerService(Long userId,BecomeSellerRequest becomeSellerRequest);

    ResponseEntity<?> addAddress(Long id, UpdateAddressDTO updateAddressDTO);

    ResponseEntity<?> getListSeller(Pageable pageable);

    ResponseEntity<?> search(String key, Pageable pageable);
}
