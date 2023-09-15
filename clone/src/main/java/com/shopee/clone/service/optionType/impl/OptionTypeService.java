package com.shopee.clone.service.optionType.impl;

import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.entity.OptionTypeEntity;
import com.shopee.clone.repository.OptionTypeRepository;
import com.shopee.clone.service.optionType.IOptionTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionTypeService implements IOptionTypeService {
    private final OptionTypeRepository optionTypeRepository;
    private final ModelMapper modelMapper;

    public OptionTypeService(OptionTypeRepository optionTypeRepository, ModelMapper modelMapper) {
        this.optionTypeRepository = optionTypeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public OptionType createOptionType(OptionType optionType) {
        return modelMapper.map(optionTypeRepository
                .save(modelMapper.map(optionType, OptionTypeEntity.class)),OptionType.class);
    }
}
