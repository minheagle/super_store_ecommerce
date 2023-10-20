package com.shopee.clone.rest_controller.controller_public.category;

import com.shopee.clone.service.category_public.CategoryPublic;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/public/categories")
public class CategoryPublicRestController {
    private final CategoryPublic categoryPublic;

    public CategoryPublicRestController(CategoryPublic categoryPublic) {
        this.categoryPublic = categoryPublic;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCategory(){
        return categoryPublic.getAll();
    }

    @GetMapping("/all-leaf")
    public ResponseEntity<?> getAllLeafCategory(){
        return categoryPublic.getAllLeaf();
    }
}
