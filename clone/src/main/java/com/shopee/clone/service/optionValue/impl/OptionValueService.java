package com.shopee.clone.service.optionValue.impl;

import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.OptionValue;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.OptionValueRequest;
import com.shopee.clone.entity.OptionValueEntity;
import com.shopee.clone.repository.product.OptionValueRepository;
import com.shopee.clone.service.optionValue.IOptionValueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionValueService implements IOptionValueService {
    private final OptionValueRepository optionValueRepository;
    private final ModelMapper modelMapper;

    public OptionValueService(OptionValueRepository optionValueRepository, ModelMapper modelMapper) {
        this.optionValueRepository = optionValueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createOptionValueByOptionType(OptionValueRequest optionValueRequest,
                                              OptionType optionType, ProductItem productItem) {
        OptionValue optionValue = OptionValue
                .builder()
                .valueName(optionValueRequest.getValueName())
                .optionType(optionType)
                .productItem(productItem)
                .build();
        optionValueRepository.save(modelMapper.map(optionValue,OptionValueEntity.class));
    }

    @Override
    public void saveAllOptionValueByOptionType(List<OptionValueRequest> optionValueRequests, OptionType optionType) {
        List<OptionValue> optionValues = optionValueRequests.stream()
                .map(optionValueRequest -> OptionValue.builder()
                        .valueName(optionValueRequest.getValueName())
//                        .percent_price(optionValueRequest.getPercent_price())
                        .optionType(optionType)
                        .build()).collect(Collectors.toList());

        List<OptionValueEntity> optionValueEntities = optionValues.stream()
                .map(optionValue -> modelMapper.map(optionValue,OptionValueEntity.class)).collect(Collectors.toList());
        optionValueRepository.saveAll(optionValueEntities);
    }
}
