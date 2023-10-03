package com.shopee.clone.service.category.impl;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.DTO.category.ParentCategory;
import com.shopee.clone.DTO.category.create.CreateCategory;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategoryChangeImage;
import com.shopee.clone.DTO.category.update.UpdateCategoryNoChangeImage;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.repository.CategoryRepository;
import com.shopee.clone.response.category.ResponseDetailCategory;
import com.shopee.clone.response.category.ResponseGetAllCategory;
import com.shopee.clone.service.category.ICategoryService;
import com.shopee.clone.service.category.common.CategoryComponent;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryComponent categoryComponent;
    private final IUploadImageService uploadImageService;
    private final ModelMapper modelMapper;
    @Value("${cloudinary.category.folder}")
    private String categoryImageFolder;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryComponent categoryComponent,
                           IUploadImageService uploadImageService,
                           ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryComponent = categoryComponent;
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
            ResponseGetAllCategory<List<CategoryDTO>> responseGetAllCategoryEmpty = new ResponseGetAllCategory<>();
            responseGetAllCategoryEmpty.setData(new ArrayList<>());
            responseGetAllCategoryEmpty.setTotalCount(0);
            if (listAll.size() == 0){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List category is empty !")
                                        .results(responseGetAllCategoryEmpty)
                                        .build()
                        );
            }
            ResponseGetAllCategory<List<CategoryDTO>> responseGetAllCategory = new ResponseGetAllCategory<>();
            responseGetAllCategory.setData(listAll);
            responseGetAllCategory.setTotalCount(listAll.size());
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Get all category success")
                                .results(responseGetAllCategory)
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
    public ResponseEntity<?> getById(Long id) {
        try{
            if(!categoryRepository.existsById(id)){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(404))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Category not found")
                                        .results("")
                                        .build()
                        );
            }
            CategoryEntity categoryEntity = categoryRepository.findById(id).get();
            ResponseDetailCategory<CategoryDTO> responseDetailCategory = new ResponseDetailCategory<>();
            responseDetailCategory.setData(modelMapper.map(categoryEntity, CategoryDTO.class));
            return ResponseEntity
                    .ok()
                    .body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Get category detail success")
                            .results(responseDetailCategory)
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
    @Transactional
    public ResponseEntity<?> create(CreateCategory createCategory) {
        try{
            ImageUploadResult imageUploadResult = uploadImageService.uploadSingle(createCategory.getFile(), categoryImageFolder);
            if(categoryRepository.findAll().size() == 0){
                CategoryEntity rootCategory = CategoryEntity
                        .builder()
                        .left(1)
                        .right(2)
                        .content(createCategory.getContent())
                        .imageUrl(imageUploadResult.getSecure_url())
                        .imagePublicId(imageUploadResult.getPublic_id())
                        .parentId(0L)
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
            if(!categoryRepository.existsById(createCategory.getParentId())){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Parent category not found")
                                        .results("")
                                        .build()
                        );
            }
            if(categoryRepository.existsByContent(createCategory.getContent())){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Category already exists")
                                        .results("")
                                        .build()
                        );
            }
            CategoryEntity parentCategory = categoryRepository.findById(createCategory.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent Category not found"));
            CategoryEntity newCategory = CategoryEntity
                    .builder()
                    .left(parentCategory.getRight())
                    .right(parentCategory.getRight() + 1)
                    .content(createCategory.getContent())
                    .imageUrl(imageUploadResult.getSecure_url())
                    .imagePublicId(imageUploadResult.getPublic_id())
                    .parentId(createCategory.getParentId())
                    .build();
            categoryRepository.updateLeft(parentCategory.getRight(), 2);
            categoryRepository.updateRight(parentCategory.getRight(), 2);
            categoryRepository.save(newCategory);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Create Category Success")
                                    .results("")
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
    public ResponseEntity<?> updateNoChangeImage(UpdateCategoryNoChangeImage updateCategoryNoChangeImage) {
        try{
            CategoryEntity categoryEntity = categoryComponent.getById(updateCategoryNoChangeImage.getId());
            if (!categoryEntity.getContent().equals(updateCategoryNoChangeImage.getContent())){
                if(categoryRepository.existsByContent(updateCategoryNoChangeImage.getContent())){
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("FAIL")
                                            .message("Content already exists")
                                            .results("")
                                            .build()
                            );
                }
            }
            categoryEntity.setContent(updateCategoryNoChangeImage.getContent());
            categoryRepository.save(categoryEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Update success")
                                    .results("")
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
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
    public ResponseEntity<?> updateChangeImage(UpdateCategoryChangeImage updateCategoryChangeImage) {
        try{
            CategoryEntity categoryEntity = categoryComponent.getById(updateCategoryChangeImage.getId());
            if(!categoryEntity.getContent().equals(updateCategoryChangeImage.getContent())){
                if(categoryRepository.existsByContent(updateCategoryChangeImage.getContent())){
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("FAIL")
                                            .message("Content already exists")
                                            .results("")
                                            .build()
                            );
                }
            }
            ImageUploadResult imageUploadResult =  uploadImageService.replaceSingle(updateCategoryChangeImage.getFile(), updateCategoryChangeImage.getImagePublicId(), categoryImageFolder);
            categoryEntity.setContent(updateCategoryChangeImage.getContent());
            categoryEntity.setImageUrl(imageUploadResult.getSecure_url());
            categoryEntity.setImagePublicId(imageUploadResult.getPublic_id());
            categoryRepository.save(categoryEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Update success")
                                    .results("")
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
    public ResponseEntity<?> delete(long id) {
        try{
            CategoryEntity categoryEntity = categoryComponent.getById(id);
            int space = categoryEntity.getRight() - categoryEntity.getLeft() + 1;
            categoryComponent.updateAllPosition(categoryEntity.getRight(), -space);
            categoryRepository.deleteById(id);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Delete category success")
                                    .results("")
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
    public ResponseEntity<?> move(MoveCategory moveCategory) {
        try{
            CategoryEntity currentCategoryEntity = categoryComponent.getById(moveCategory.getCurrentCategoryId());
            int space = currentCategoryEntity.getRight() - currentCategoryEntity.getLeft() + 1;
            CategoryEntity newCategoryEntity = categoryComponent.getById(moveCategory.getNewParentCategoryId());
            categoryComponent.createSpace(newCategoryEntity.getRight(), space);
            int weight;
            if(currentCategoryEntity.getRight() > newCategoryEntity.getRight()){
                weight = currentCategoryEntity.getLeft() - newCategoryEntity.getRight();
                categoryComponent.updatePositionForSubNode(
                        currentCategoryEntity.getLeft() + space,
                        currentCategoryEntity.getRight() + space,
                        -(space + weight)
                );
                categoryComponent.updateAllPosition(currentCategoryEntity.getRight() + space, -space);
            }else if(currentCategoryEntity.getRight() < newCategoryEntity.getRight()){
                weight = newCategoryEntity.getLeft() - currentCategoryEntity.getRight();
                categoryComponent.updatePositionForSubNode(
                        currentCategoryEntity.getLeft(),
                        currentCategoryEntity.getRight(),
                        space + weight);
                categoryComponent.updateAllPosition(currentCategoryEntity.getRight(), -space);
            }
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Move category success")
                                    .results("")
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
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
