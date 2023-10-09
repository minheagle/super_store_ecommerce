package com.shopee.clone.service.promotion.impl;

import com.shopee.clone.DTO.promotion.request.PromotionRequestCreate;
import com.shopee.clone.DTO.promotion.response.PromotionResponse;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.promotion.PromotionRepository;
import com.shopee.clone.service.promotion.IPromotionService;
import com.shopee.clone.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PromotionServiceImpl implements IPromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public ResponseEntity<?> createPromotion(PromotionRequestCreate promotionRequestCreate) {
        try{
            if(sellerRepository.existsById(promotionRequestCreate.getSellerId())){
                SellerEntity seller = sellerRepository.findById(promotionRequestCreate.getSellerId())
                        .orElseThrow(NoSuchElementException::new);
                PromotionEntity promotionEntity = PromotionEntity
                        .builder()
                        .build();
            }


        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message("Product Not Exist!")
                                    .build()
                    );
        }
        return null;
    }
}
