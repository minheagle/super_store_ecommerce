package com.shopee.clone.service.optionType.impl;

import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.OptionTypeCreate;
import com.shopee.clone.DTO.product.request.OptionTypeRequest;
import com.shopee.clone.entity.OptionTypeEntity;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.repository.product.OptionTypeRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.optionType.IOptionTypeService;
import com.shopee.clone.service.optionValue.impl.OptionValueService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class OptionTypeService implements IOptionTypeService {
    private final OptionTypeRepository optionTypeRepository;
    private final ModelMapper modelMapper;
    private final OptionValueService optionValueService;
    private final ProductItemRepository itemRepository;

    public OptionTypeService(OptionTypeRepository optionTypeRepository,
                             ModelMapper modelMapper,
                             OptionValueService optionValueService, ProductItemRepository itemRepository) {
        this.optionTypeRepository = optionTypeRepository;
        this.modelMapper = modelMapper;
        this.optionValueService = optionValueService;
        this.itemRepository = itemRepository;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> createOptionTypeWithOptionValue(OptionTypeCreate optionTypeCreate) {
        try {
            Long itemId = optionTypeCreate.getProductItemId();

            if (itemRepository.existsById(itemId)) {
                ProductItemEntity productItemEntity = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NoSuchElementException("ProductItem Not Found"));
                ProductItem productItem = ProductItem
                        .builder()
                        .pItemId(productItemEntity.getPItemId())
                        .price(productItemEntity.getPrice())
                        .qtyInStock(productItemEntity.getQtyInStock())
                        .status(productItemEntity.getStatus())
                        .build();
                Set<OptionTypeEntity> setTypes = new HashSet<>();

                List<OptionTypeEntity> optionTypeEntities = optionTypeRepository.findAll();
                List<OptionTypeRequest> optionTypeRequests = optionTypeCreate.getOptionTypeRequestList();

                for (OptionTypeRequest optionTypeRequest : optionTypeRequests) {
                    OptionTypeEntity existingOptionType = optionTypeEntities.stream()
                            .filter(typeEntity -> typeEntity.getOptionName().equalsIgnoreCase(optionTypeRequest.getOptionName()))
                            .findFirst()
                            .orElseGet(() -> {
                                OptionType optionType = OptionType.builder()
                                        .optionName(optionTypeRequest.getOptionName())
                                        .productItems(Set.of(productItem))
                                        .build();
                                return optionTypeRepository.save(modelMapper.map(optionType, OptionTypeEntity.class));
                            });

                    OptionType typeSaved = OptionType
                            .builder()
                            .opTypeId(existingOptionType.getOpTypeId())
                            .optionName(existingOptionType.getOptionName())
                            .build();
                    optionValueService.createOptionValueByOptionType(optionTypeRequest.getOptionValueRequest(), typeSaved, productItem);

                    setTypes.add(existingOptionType);
                }

                productItemEntity.setOptionTypes(setTypes);
                itemRepository.save(productItemEntity);
            }

            return ResponseEntity
                        .status(HttpStatusCode.valueOf(201))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add OptionType With OptionValue Success")
                                        .results("")
                                        .build()
                        );
            }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }
}
