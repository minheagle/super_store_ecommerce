package com.shopee.clone.service.optionValue.impl;

import com.shopee.clone.DTO.product.OptionValue;
import com.shopee.clone.entity.OptionValueEntity;
import com.shopee.clone.repository.OptionValueRepository;
import com.shopee.clone.service.optionValue.IOptionValueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionValueService implements IOptionValueService {
    private final OptionValueRepository optionValueRepository;
    private final ModelMapper modelMapper;

    public OptionValueService(OptionValueRepository optionValueRepository, ModelMapper modelMapper) {
        this.optionValueRepository = optionValueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createOptionValue(OptionValue optionValue) {
        optionValueRepository.save(modelMapper.map(optionValue, OptionValueEntity.class));
    }
}
