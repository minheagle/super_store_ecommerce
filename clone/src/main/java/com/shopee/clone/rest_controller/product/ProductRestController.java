package com.shopee.clone.rest_controller.product;

import com.shopee.clone.DTO.fieldErrorDTO.FieldError;
import com.shopee.clone.DTO.product.request.OptionTypeCreate;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.service.optionType.IOptionTypeService;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductItemService productItemService;
    @Autowired
    private IOptionTypeService optionTypeService;

    @PostMapping("add-new/product")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequestCreate productRequestCreate,
                                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productService.addNewProduct(productRequestCreate);
    }
//    @PostMapping("add-new/pitem-image")
//    public ResponseEntity<?> createProductItemWithProductID(@RequestPart("productId") Long productId,
//                                                            @RequestPart("price") Double price,
//                                                            @RequestPart("qtyInStock") Integer qtyInStock,
//                                                            @RequestPart("imgProductFile") MultipartFile[]imgProductFile,
//                                                            BindingResult bindingResult){
//        ProductItemRequest itemRequest = ProductItemRequest
//                .builder()
//                .productId(productId)
//                .price(price)
//                .qtyInStock(qtyInStock)
//                .imgProductFile(imgProductFile)
//                .build();
//        if(bindingResult.hasErrors()) {
//            if(bindingResult.hasErrors()) {
//                FieldError.throwErrorHandler(bindingResult);
//            }
//        }
//        return productItemService.createProductItemWithImage(itemRequest);
//    }

    @PostMapping("add-new/pitem-image")
    public ResponseEntity<?> createProductItemWithProductID(@Valid ProductItemRequest itemRequest,
                                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            if(bindingResult.hasErrors()) {
                FieldError.throwErrorHandler(bindingResult);
            }
        }
        return productItemService.createProductItemWithImage(itemRequest);
    }

    @PostMapping("add-new/option-by-item")
    public ResponseEntity<?> createOptionWithItemID(@RequestBody OptionTypeCreate optionTypeCreate){
        return optionTypeService.createOptionTypeWithOptionValue(optionTypeCreate);
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<?> getAll(@PathVariable Long shopId){
        return productService.getAllProductBelongWithShop(shopId);
    }
    @GetMapping("/products-by-category/{categoryId}")
    public ResponseEntity<?> getAllProductByCategoryId(@PathVariable Long categoryId){
        return productService.getAllProductByCategoryId(categoryId);
    }
    @GetMapping("/")
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
    public ResponseEntity<?> getById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("product/{id}/item/{itemId}")
    public ResponseEntity<?> makeOrder(@PathVariable Long id, @PathVariable Long itemId){
        return productItemService.getProductItemByShopIdAndParentProductId(id, itemId);
    }
    @DeleteMapping("product/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable Long productId){
        return productService.removeProductById(productId);
    }
    @DeleteMapping("product/{productId}/item/{itemId}")
    public ResponseEntity<?> removeProductItemInProduct(@PathVariable Long productId, @PathVariable Long itemId){
        return productItemService.removeProductItem(productId,itemId);
    }
}
