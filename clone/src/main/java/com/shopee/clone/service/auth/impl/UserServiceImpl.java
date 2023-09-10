package com.shopee.clone.service.auth.impl;

import com.shopee.clone.converter.UserConverter;
import com.shopee.clone.domain.User;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;
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
}
