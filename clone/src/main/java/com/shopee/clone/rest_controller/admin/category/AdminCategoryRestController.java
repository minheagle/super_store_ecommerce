package com.shopee.clone.rest_controller.admin.category;

import com.shopee.clone.DTO.category.create.CreateCategory;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategoryChangeImage;
import com.shopee.clone.DTO.category.update.UpdateCategoryNoChangeImage;
import com.shopee.clone.service.category.ICategoryService;
import com.shopee.clone.util.ResponseObject;
import com.shopee.clone.validate.category.CreateCategoryValidator;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/categories")
public class AdminCategoryRestController {
    private final ICategoryService categoryService;
    private final CreateCategoryValidator createCategoryValidator;

    public AdminCategoryRestController(ICategoryService categoryService,
                                       CreateCategoryValidator createCategoryValidator) {
        this.categoryService = categoryService;
        this.createCategoryValidator = createCategoryValidator;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(){
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Object id) {
        try{
            Long idLong = Long.valueOf((String) id);
            if (idLong <= 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Not found")
                                .results("")
                                .build());
            }
            return categoryService.getById(idLong);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Not found")
                            .results("")
                            .build()
                    );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid CreateCategory createCategoryRequest){
        return categoryService.create(createCategoryRequest);
    }

    @PutMapping("/update/{id}/no-image")
    public ResponseEntity<?> updateCategoryNoImage(@PathVariable Object id , @Valid @RequestBody UpdateCategoryNoChangeImage updateCategoryNoChangeImage){
        try{
            Long idLong = Long.valueOf((String) id);
            if (idLong <= 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Category not found")
                                .results("")
                                .build());
            }
            return categoryService.updateNoChangeImage(updateCategoryNoChangeImage);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Category not found")
                            .results("")
                            .build()
                    );
        }
    }

    @PutMapping("/update/{id}/change-image")
    public ResponseEntity<?> updateCategoryWithChangeImage(@PathVariable Object id ,@Valid UpdateCategoryChangeImage updateCategoryChangeImage){
        try{
            Long idLong = Long.valueOf((String) id);
            if (idLong <= 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Category not found")
                                .results("")
                                .build());
            }
            return categoryService.updateChangeImage(updateCategoryChangeImage);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Category not found")
                            .results("")
                            .build()
                    );
        }
    }

    @PostMapping("/move")
    public ResponseEntity<?> moveCategory(@Valid @RequestBody MoveCategory moveCategory){
        return categoryService.move(moveCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Object id){
        try{
            Long idLong = Long.valueOf((String) id);
            if (idLong <= 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Category not found")
                                .results("")
                                .build());
            }
            return categoryService.delete(idLong);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Category not found")
                            .results("")
                            .build()
                    );
        }
    }
}
