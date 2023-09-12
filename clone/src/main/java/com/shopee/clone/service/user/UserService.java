package com.shopee.clone.service.user;

import com.shopee.clone.DTO.auth.user.User;
import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<UserEntity> findUserByID(Long id);
    UserEntity update(UserEntity user);

    List<UserEntity> getListUser();
    void delete(Long id);

    ResponseEntity<?> updateUser(long userId, UserUpdateDTO userUpdateDTO);
}
