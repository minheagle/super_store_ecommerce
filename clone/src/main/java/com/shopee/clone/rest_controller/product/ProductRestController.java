package com.shopee.clone.rest_controller.product;

import com.shopee.clone.DTO.fieldErrorDTO.FieldError;
import com.shopee.clone.DTO.product.request.OptionTypeCreate;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.service.optionType.IOptionTypeService;
import com.shopee.clone.service.optionValue.IOptionValueService;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import com.shopee.clone.util.ResponseObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductItemService productItemService;
    @Autowired
    private IOptionTypeService optionTypeService;
    @Autowired
    private IOptionValueService optionValueService;

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

    @GetMapping("product/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("product/{id}/item/{itemId}")
    public ResponseEntity<?> makeOrder(@PathVariable Long id, @PathVariable Long itemId){
        return productService.getProductMakeOrderByParentId(id, itemId);
    }
}
