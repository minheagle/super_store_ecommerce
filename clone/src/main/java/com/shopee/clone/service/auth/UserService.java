package com.shopee.clone.service.auth;

import com.shopee.clone.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getListUser();
    Optional<User> findByEmail(String email);
}
