package com.shopee.clone.service.category;

import com.shopee.clone.DTO.category.ParentCategory;
import com.shopee.clone.DTO.category.create.CreateCategory;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategoryChangeImage;
import com.shopee.clone.DTO.category.update.UpdateCategoryNoChangeImage;
import org.springframework.http.ResponseEntity;

public interface ICategoryService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getAllLeafFromParent(ParentCategory parentCategory);
    ResponseEntity<?> getAllLeaf();
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> create(CreateCategory createCategory);
    ResponseEntity<?> updateNoChangeImage(UpdateCategoryNoChangeImage updateCategoryNoChangeImage);
    ResponseEntity<?> updateChangeImage(UpdateCategoryChangeImage updateCategoryChangeImage);
    ResponseEntity<?> delete(long id);
    ResponseEntity<?> move(MoveCategory moveCategory);
}
