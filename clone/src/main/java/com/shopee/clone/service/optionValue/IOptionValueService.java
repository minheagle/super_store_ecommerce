package com.shopee.clone.service.optionValue;

import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.OptionValue;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.OptionValueRequest;

import java.util.List;

public interface IOptionValueService {
    void createOptionValueByOptionType(OptionValueRequest optionValueRequest, OptionType optionType, ProductItem productItem);
    void saveAllOptionValueByOptionType(List<OptionValueRequest> optionValueRequests, OptionType optionType);
}
