package com.shopee.clone.service.category_public;

import org.springframework.http.ResponseEntity;

public interface CategoryPublic {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getAllLeaf();
    ResponseEntity<?> getChildFromParent(Long parentId);
}
