package com.shopee.clone.rest_controller.product;

import com.shopee.clone.DTO.auth.user.UserUpdateDTO;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.service.product.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    @Autowired
    private IProductService productService;

    @PostMapping("add-new")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequestCreate productRequestCreate) {
        return productService.addNew(productRequestCreate);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("product/{id}/item/{itemId}")
    public ResponseEntity<?> makeOrder(@PathVariable Long id, @PathVariable Long itemId){
        return productService.getProductMakeOrderByParentId(id, itemId);
    }
}
