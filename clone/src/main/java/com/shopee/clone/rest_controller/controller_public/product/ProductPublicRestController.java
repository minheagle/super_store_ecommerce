package com.shopee.clone.rest_controller.controller_public.product;

import com.shopee.clone.service.product.IProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/public/products")
public class ProductPublicRestController {

    private final IProductService productService;

    public ProductPublicRestController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<?> getProductsPaging(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                               @RequestParam(name = "size", required = false, defaultValue = "25") Integer size,
                                               @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("productId").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("productId").descending();
        }
        Pageable pageable = PageRequest.of((page-1), size, sortable);
        return productService.getAllProductPaging(pageable);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
