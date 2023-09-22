package com.shopee.clone.validate.category;

import com.shopee.clone.DTO.category.create.CreateCategory;
import com.shopee.clone.entity.CategoryEntity;
import com.shopee.clone.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateCategoryValidator implements Validator {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateCategory.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateCategory createCategory = (CreateCategory) target;
        //Nếu Category trống thì bỏ qua kiểm tra parent category
        if(categoryRepository.findAll().size() != 0){
            try{
                CategoryEntity parentCategory = categoryRepository.findById(createCategory.getParentId())
                        .orElseThrow(RuntimeException::new);
            }catch (Exception e){
                errors.rejectValue("parentId", "parentId", "Parent category not found");
            }
        }
        if(categoryRepository.existsByContent(createCategory.getContent())){
            errors.rejectValue("content", "content", "Category is exists");
        }
    }
}
