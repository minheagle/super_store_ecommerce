package com.shopee.clone.validate;

import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterDTOValidate implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDTO registerDTO = (RegisterDTO) target;

        // Kiểm tra userName không được trùng lặp
        if (userRepository.existsByUserName(registerDTO.getUserName())) {
            errors.rejectValue("userName", "Duplicate.userName", "Username is already taken");
        }

        // Kiểm tra email không được trùng lặp
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            errors.rejectValue("email", "Duplicate.email", "Email is already registered");
        }

        // Kiểm tra phone không được trùng lặp
        if (userRepository.existsByPhone(registerDTO.getPhone())) {
            errors.rejectValue("phone", "Duplicate.phone", "Phone number is already in use");
        }
    }
}