package com.shopee.clone.service.seller.impl;

import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.response.seller.DetailSellerResponse;
import com.shopee.clone.service.seller.SellerService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final ModelMapper mapper;

    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.mapper = mapper;
    }

    @Override
    public SellerEntity getBySellerId(Long sellerId) {
        try{
            SellerEntity sellerEntity = sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
            return sellerEntity;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> responseGetSellerById(Long sellerId) {
        try{
            SellerEntity sellerEntity = getBySellerId(sellerId);
            Seller seller = mapper.map(sellerEntity, Seller.class);
            DetailSellerResponse<Seller> detailSellerResponse = new DetailSellerResponse<>(seller);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get seller detail success")
                                    .results(detailSellerResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }
}
