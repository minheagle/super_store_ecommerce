package com.shopee.clone.service.category;

import com.shopee.clone.DTO.category.CategoryDTO;
import com.shopee.clone.DTO.category.ParentCategory;
import com.shopee.clone.DTO.category.create.CreateCategoryRequest;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getAllLeafFromParent(ParentCategory parentCategory);
    ResponseEntity<?> getAllLeaf();
    ResponseEntity<?> create(CreateCategoryRequest createCategoryRequest);
    ResponseEntity<?> update(UpdateCategory updateCategory);
    ResponseEntity<?> delete(long id);
    ResponseEntity<?> move(MoveCategory moveCategory);
}
