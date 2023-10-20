package com.shopee.clone.rest_controller.controller_public.product;

import com.shopee.clone.service.product.IProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping("search")
    public ResponseEntity<?> searchAndFilter(@RequestParam(name = "productName", required = false, defaultValue = "") String productName,
                                             @RequestParam(name = "minPrice", required = false, defaultValue = "") Double minPrice,
                                             @RequestParam(name = "maxPrice", required = false, defaultValue = "") Double maxPrice,
                                             @RequestParam(name = "categoryId", required = false, defaultValue = "") Long categoryId,
                                             @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                             @RequestParam(name = "sort", required = false, defaultValue = "new-product") String sort){
        Sort sortable = null;
        if(sort.equalsIgnoreCase("old-product")){
            sortable = Sort.by("productId").ascending();
        }
        if(sort.equalsIgnoreCase("new-product")){
            sortable = Sort.by("productId").descending();
        }

        if (sort.equalsIgnoreCase("rating-asc")) {
            sortable = Sort.by("voteStar").ascending();
        }
        if (sort.equalsIgnoreCase("rating-desc")) {
            sortable = Sort.by("voteStar").descending();
        }

        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of((page-1), size, sortable);

        return  productService.searchAndFilter(productName, minPrice, maxPrice, categoryId, pageable);
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<?> getAllProductByShopId(@PathVariable Long shopId){
        return productService.getAllProductBelongWithShop(shopId);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
