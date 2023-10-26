package com.shopee.clone.rest_controller.controller_public.seller;

import com.shopee.clone.DTO.fieldErrorDTO.FieldError;
import com.shopee.clone.DTO.seller.request.SellerRequestUpdate;
import com.shopee.clone.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v1/public/sellers")
public class SellerPublicRestController {
    private final SellerService sellerService;

    public SellerPublicRestController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getDetailSeller(@PathVariable("sellerId") Long sellerId){
        return sellerService.responseGetSellerById(sellerId);
    }

    @GetMapping("/detail/{storeName}")
    public ResponseEntity<?> getDetailByStoreName(@PathVariable("storeName") String storeName){
        return sellerService.getByStoreName(storeName);
    }
    @PutMapping("private-information")
    public ResponseEntity<?> updatePrivateInformation(@RequestBody @Valid SellerRequestUpdate sellerRequestUpdate,
                                                      BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            FieldError.throwErrorHandler(bindingResult);
        }
        return sellerService.updatePrivateInformation(sellerRequestUpdate);
    }
    @PatchMapping("/{id}/avatar")
    public ResponseEntity<?> updateStoreAvatar(@PathVariable(name = "id") Long storeId, @RequestBody MultipartFile storeAvatar){
        return sellerService.updateStoreAvatar(storeId, storeAvatar);
    }
    @PatchMapping("/{id}/background")
    public ResponseEntity<?> updateStoreBackground(@PathVariable(name = "id") Long storeId, @RequestBody MultipartFile storeBackground){
        return sellerService.updateStoreBackground(storeId, storeBackground);
    }
}
