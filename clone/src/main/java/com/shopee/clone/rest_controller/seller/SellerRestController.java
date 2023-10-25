package com.shopee.clone.rest_controller.seller;

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
@RequestMapping("/api/v1/sellers")
public class SellerRestController {
    private final SellerService sellerService;

    public SellerRestController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getDetailSeller(@PathVariable("sellerId") Long sellerId){
        return sellerService.responseGetSellerById(sellerId);
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
