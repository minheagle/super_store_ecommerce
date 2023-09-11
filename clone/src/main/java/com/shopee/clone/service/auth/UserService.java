package com.shopee.clone.service.auth;

import com.shopee.clone.domain.User;
import com.shopee.clone.entity.UserEntity;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getListUser();
    Optional<User> findByEmail(String email);
    UserEntity save(User user);
    void delete(Long id);
    User findById(Long id);
}
