package com.shopee.clone.service.seller;

import com.shopee.clone.DTO.seller.request.SellerRequestUpdate;
import com.shopee.clone.entity.SellerEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface SellerService {
    SellerEntity getBySellerId(Long sellerId);
    ResponseEntity<?> getByStoreName(String storeName);
    ResponseEntity<?> responseGetSellerById(Long sellerId);
    ResponseEntity<?> updatePrivateInformation(SellerRequestUpdate sellerRequestUpdate);
    ResponseEntity<?> updateStoreAvatar(Long storeId, MultipartFile storeAvatar);
    ResponseEntity<?> updateStoreBackground(Long storeId, MultipartFile storeBackground);
}
