package com.shopee.clone.rest_controller.test;

import com.shopee.clone.DTO.address_data.GetAddressDetailRequest;
import com.shopee.clone.DTO.category.move.MoveCategory;
import com.shopee.clone.DTO.category.update.UpdateCategoryChangeImage;
import com.shopee.clone.DTO.category.update.UpdateCategoryNoChangeImage;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.service.address.AddressService;
import com.shopee.clone.service.address_data.AddressDataService;
import com.shopee.clone.service.category.ICategoryService;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestUploadImage {
    @Autowired
    private IUploadImageService uploadImageService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private AddressDataService addressDataService;
    @Autowired
    private IProductService productService;

    @Value("${cloudinary.category.folder}")
    private String categoryFolder;
    @PostMapping("/images")
    public ResponseEntity<?> upload(@RequestParam(name = "images") MultipartFile[] images){
        try{
            List<ImageUploadResult> result = uploadImageService.uploadMultiple(images, categoryFolder);
            return ResponseEntity.ok().body(ResponseObject.builder().status("SUCCESS").message("OK").results(result).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ResponseObject.builder().status("FAIL").message(e.getMessage()).results(""));
        }

    }

    @PostMapping("/move")
    public ResponseEntity<?> moveCategory(@RequestBody MoveCategory moveCategory){
        return categoryService.move(moveCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        return categoryService.delete(id);
    }

    @PutMapping("/update/{id}/no-image")
    public ResponseEntity<?> updateCategoryNoChangeImage(@PathVariable Long id, @RequestBody UpdateCategoryNoChangeImage updateCategoryNoChangeImage){
        return categoryService.updateNoChangeImage(updateCategoryNoChangeImage);
    }

    @PutMapping("/update/{id}/change-image")
    public ResponseEntity<?> updateCategoryChangeImage(@PathVariable Long id, UpdateCategoryChangeImage updateCategoryChangeImage){
        return categoryService.updateChangeImage(updateCategoryChangeImage);
    }

    @PostMapping("/address-detail")
    public ResponseEntity<?> getDetailAddress(@RequestBody GetAddressDetailRequest getAddressDetailRequest){
        return addressDataService.getDetailAddress(getAddressDetailRequest);
    }
}
