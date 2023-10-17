package com.shopee.clone.service.category_public.impl;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.repository.CategoryRepository;
import com.shopee.clone.service.category_public.CategoryPublic;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryPublicImpl implements CategoryPublic {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryPublicImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<?> getAll() {
        try{
            List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
            if (categoryEntityList.size() == 0){
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List category is empty")
                                        .results(categoryEntityList)
                                        .build()
                        );
            }
            List<CategoryDTO> categoryDTOList = categoryEntityList
                    .stream()
                    .map(item -> mapper.map(item, CategoryDTO.class))
                    .toList();
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get list category success")
                                    .results(categoryDTOList)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
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

    @Override
    public ResponseEntity<?> getAllLeaf() {
        try {
            List<CategoryEntity> categoryEntityList = categoryRepository.findAllLeaf();
            if (categoryEntityList.size() == 0){
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List category is empty")
                                        .results(categoryEntityList)
                                        .build()
                        );
            }
            List<CategoryDTO> categoryDTOList = categoryEntityList
                    .stream()
                    .map(item -> mapper.map(item, CategoryDTO.class))
                    .toList();
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get list category success")
                                    .results(categoryDTOList)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                    );
        }
    }

    @Override
    public ResponseEntity<?> getChildFromParent(Long parentId) {
        return null;
    }
}
