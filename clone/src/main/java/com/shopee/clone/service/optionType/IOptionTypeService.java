package com.shopee.clone.service.optionType;

import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.OptionTypeCreate;
import com.shopee.clone.DTO.product.request.OptionTypeRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOptionTypeService {
    ResponseEntity<?> createOptionTypeWithOptionValue(OptionTypeCreate optionTypeCreate);
}
