package com.shopee.clone.rest_controller.product;

import com.shopee.clone.DTO.fieldErrorDTO.FieldError;
import com.shopee.clone.DTO.product.request.*;
import com.shopee.clone.DTO.product.response.ProductResponseDTO;
import com.shopee.clone.DTO.product.update.ProductItemRequestEdit;
import com.shopee.clone.DTO.product.update.ProductRequestEdit;
import com.shopee.clone.DTO.product.update.SingleUpdateChangeImageProductItem;
import com.shopee.clone.service.imageProduct.impl.ImageProductService;
import com.shopee.clone.service.optionType.IOptionTypeService;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    private final IProductService productService;
    private final IProductItemService productItemService;
    private final IOptionTypeService optionTypeService;
    private final ImageProductService imageProductService;

    public ProductRestController(IProductService productService, IProductItemService productItemService, IOptionTypeService optionTypeService, ImageProductService imageProductService) {
        this.productService = productService;
        this.productItemService = productItemService;
        this.optionTypeService = optionTypeService;
        this.imageProductService = imageProductService;
    }

    @PostMapping("add-new/product") // Step 1
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequestCreate productRequestCreate,
                                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productService.addNewProduct(productRequestCreate);
    }

    @PostMapping("add-new/pitem-image") // Step 2
    public ResponseEntity<?> createProductItemWithProductID(@Valid ProductItemRequest itemRequest,
                                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productItemService.createProductItemWithImage(itemRequest);
    }

    //WARNING
    @PostMapping("add-new/product-item")
    public ResponseEntity<?> createProductItemWithProductID(@Valid ProductItemFullOptionRequest productItemFullOptionRequest,
                                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return productItemService.createProductItemFullOption(productItemFullOptionRequest);
    }

    @PostMapping("add-new/option-by-item") // Step 3
    public ResponseEntity<?> createOptionWithItemID(@RequestBody OptionTypeCreate optionTypeCreate){
        return optionTypeService.createOptionTypeWithOptionValue(optionTypeCreate);
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<?> getAllProductByShopId(@PathVariable Long shopId){
        return productService.getAllProductBelongWithShop(shopId);
    }
    @GetMapping("/products-by-category/{categoryId}")
    public ResponseEntity<?> getAllProductByCategoryId(@PathVariable Long categoryId){
        return productService.getAllProductByCategoryId(categoryId);
    }
    @GetMapping("product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("confirm-finish-create/{id}")
    public ResponseEntity<?> confirmFinishCreateProduct(@PathVariable @NotBlank Long id){
        return productService.confirmFinishCreateProduct(id);
    }

    @GetMapping("product/{id}/item/{itemId}")
    public ResponseEntity<?> makeOrder(@PathVariable Long id, @PathVariable Long itemId){
        return productItemService.getProductItemByShopIdAndParentProductId(id, itemId);
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

    @PutMapping("product/update/{id}")
    public ResponseEntity<?> editProductById(@PathVariable Long id, @RequestBody ProductRequestEdit pRequestEdit){
        return productService.editProductById(id, pRequestEdit);
    }
    @PutMapping("product/update-item/{id}")
    public ResponseEntity<?> editProductItemById(@PathVariable Long id, @RequestBody ProductItemRequestEdit itemRequestEdit){
        return productItemService.editProductItemById(id, itemRequestEdit);
    }
    @PutMapping("product/image-product-item")
    public ResponseEntity<?> changeImageProductItem(@Valid SingleUpdateChangeImageProductItem changeImageProductItem){
        return imageProductService.changeImageProduct(changeImageProductItem);
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
