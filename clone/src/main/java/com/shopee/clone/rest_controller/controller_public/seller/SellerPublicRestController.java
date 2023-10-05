package com.shopee.clone.rest_controller.controller_public.seller;

import com.shopee.clone.service.seller.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
