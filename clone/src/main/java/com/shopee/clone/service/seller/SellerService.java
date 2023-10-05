package com.shopee.clone.service.seller;

import com.shopee.clone.entity.SellerEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface SellerService {
    SellerEntity getBySellerId(Long sellerId);
    ResponseEntity<?> responseGetSellerById(Long sellerId);
}
