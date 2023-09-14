package com.shopee.clone.validate;

import com.shopee.clone.DTO.auth.register.RegisterDTO;
import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
@Component
public class UpdateDTOValidate implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
    public void validate(Long id,Object target, Errors errors) {
        Optional<UserEntity> userEntity=  userRepository.findById(id);
        UserUpdateDTO userUpdateDTO = (UserUpdateDTO) target;
        // Kiểm tra userName không được trùng lặp
        if (userRepository.existsByUserName(userUpdateDTO.getUserName()) &&
                !(userEntity.get().getUserName().equals(userUpdateDTO.getUserName()))) {
            errors.rejectValue("userName", "Duplicate.userName", "Username is already taken");
        }

        // Kiểm tra email không được trùng lặp
        if (userRepository.existsByEmail(userUpdateDTO.getEmail()) &&
                !(userEntity.get().getEmail().equals(userUpdateDTO.getEmail()))) {
            errors.rejectValue("email", "Duplicate.email", "Email is already registered");
        }

        // Kiểm tra phone không được trùng lặp
        if (userRepository.existsByPhone(userUpdateDTO.getPhone()) &&
                !(userEntity.get().getPhone().equals(userUpdateDTO.getPhone()))) {
            errors.rejectValue("phone", "Duplicate.phone", "Phone number is already in use");
        }
    }
}
