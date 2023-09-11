package com.shopee.clone.service.auth.impl;

import com.shopee.clone.converter.UserConverter;
import com.shopee.clone.domain.User;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<User> getListUser() {
        return userRepository.findAll().stream().
                map(UserConverter::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email)  {
            return userRepository.findByEmail(email).map(UserConverter::toModel);
     }

    @Override
    public UserEntity save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(UserConverter.toEntity(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return UserConverter.toModel(userRepository.findById(id).orElse(new UserEntity()));
    }
}
