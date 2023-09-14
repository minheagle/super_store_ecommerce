package com.shopee.clone.service.category.impl;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.DTO.category.ParentCategory;
import com.shopee.clone.DTO.category.create.CreateCategoryRequest;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategory;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.repository.CategoryRepository;
import com.shopee.clone.service.category.ICategoryService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final IUploadImageService uploadImageService;
    private final ModelMapper modelMapper;
    @Value("${cloudinary.category.folder}")
    private String categoryImageFolder;

    public CategoryService(CategoryRepository categoryRepository,
                           IUploadImageService uploadImageService,
                           ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.uploadImageService = uploadImageService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ResponseEntity<?> getAll() {
        try{
            List<CategoryDTO> listAll =  categoryRepository.findAll()
                    .stream()
                    .map(item -> modelMapper.map(item, CategoryDTO.class))
                    .collect(Collectors.toList());
            if (listAll.size() == 0){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List category is empty !")
                                        .results("")
                                        .build()
                        );
            }
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Get all category success")
                                .results(listAll)
                                .build()
                    );
        }catch (Exception e){
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

    @Override
    @Transactional
    public ResponseEntity<?> getAllLeafFromParent(ParentCategory parentCategory) {
        try{
            List<CategoryDTO> listAllLeafFromParent =  categoryRepository.findAllByParentCategory(parentCategory.getLeft(), parentCategory.getRight())
                    .stream()
                    .map(item -> modelMapper.map(item, CategoryDTO.class))
                    .collect(Collectors.toList());
            if (listAllLeafFromParent.size() == 0){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List all leaf from parent category is empty !")
                                        .results("")
                                        .build()
                        );
            }
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get all leaf from parent category success")
                                    .results(listAllLeafFromParent)
                                    .build()
                    );
        }catch (Exception e){
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

    @Override
    @Transactional
    public ResponseEntity<?> getAllLeaf() {
        try{
            List<CategoryDTO> listAllLeaf =  categoryRepository.findAllLeaf()
                    .stream()
                    .map(item -> modelMapper.map(item, CategoryDTO.class))
                    .collect(Collectors.toList());
            if (listAllLeaf.size() == 0){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List all leaf category is empty !")
                                        .results("")
                                        .build()
                        );
            }
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get all leaf category success")
                                    .results(listAllLeaf)
                                    .build()
                    );
        }catch (Exception e){
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

    @Override
    @Transactional
    public ResponseEntity<?> create(CreateCategoryRequest createCategoryRequest) {
        try{
            ImageUploadResult imageUploadResult = uploadImageService.uploadSingle(createCategoryRequest.getNewCategory().getFile(), categoryImageFolder);
            if(categoryRepository.findAll().size() == 0){
                CategoryEntity rootCategory = CategoryEntity
                        .builder()
                        .left(1)
                        .right(2)
                        .content(createCategoryRequest.getNewCategory().getContent())
                        .imageUrl(imageUploadResult.getSecure_url())
                        .imagePublicId(imageUploadResult.getPublic_id())
                        .build();
                categoryRepository.save(rootCategory);
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Create category success")
                                        .results("")
                                        .build()
                        );
            }
            CategoryEntity newCategory = CategoryEntity
                    .builder()
                    .left(createCategoryRequest.getParentCategory().getRight())
                    .right(createCategoryRequest.getParentCategory().getRight() + 1)
                    .content(createCategoryRequest.getNewCategory().getContent())
                    .imageUrl(imageUploadResult.getSecure_url())
                    .imagePublicId(imageUploadResult.getPublic_id())
                    .build();
            categoryRepository.updateLeft(createCategoryRequest.getParentCategory().getRight(), 2);
            categoryRepository.updateLeft(createCategoryRequest.getParentCategory().getRight(), 2);
            categoryRepository.save(newCategory);
        }catch (Exception e){
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


    return null;
    }

    @Override
    public ResponseEntity<?> update(UpdateCategory updateCategory) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> move(MoveCategory moveCategory) {
        return null;
    }
}
