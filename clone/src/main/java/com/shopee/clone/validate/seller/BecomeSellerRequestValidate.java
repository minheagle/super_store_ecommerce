package com.shopee.clone.validate.seller;

import com.shopee.clone.DTO.auth.user.BecomeSellerRequest;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BecomeSellerRequestValidate implements Validator {
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return BecomeSellerRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BecomeSellerRequest becomeSellerRequest = (BecomeSellerRequest) target;

        if(sellerRepository.existsByStoreName(becomeSellerRequest.getStoreName())){
            errors.rejectValue("storeName", "Exists.storeName", "Store name already exists");
        }
    }
    public void validate(Long userId ,Object target, Errors errors) {
        BecomeSellerRequest becomeSellerRequest = (BecomeSellerRequest) target;

        if(!userRepository.existsById(userId)){
            errors.rejectValue("", "", "User not found");
        }
        UserEntity userEntity = userRepository.findById(userId).get();

        if(sellerRepository.existsByStoreName(becomeSellerRequest.getStoreName())){
            errors.rejectValue("storeName", "Exists.storeName", "Store name already exists");
        }
    }
}
