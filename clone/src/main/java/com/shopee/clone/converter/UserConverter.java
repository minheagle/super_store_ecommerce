package com.shopee.clone.converter;

import com.shopee.clone.domain.User;
import com.shopee.clone.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserConverter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public static User toModel(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setPhone(userEntity.getPhone());
        user.setBrithDate(userEntity.getBrithDate());
        user.setRole(userEntity.getRole());
        return user;
    }

    public static UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setPassword(user.getPassword());
        userEntity.setPhone(user.getPhone());
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setBrithDate(userEntity.getBrithDate());
        userEntity.setRole(user.getRole());
        return userEntity;
    }
}
