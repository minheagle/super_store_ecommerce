package com.shopee.clone.DTO.product.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemRequest {
    private Long productId;
    @Digits(integer = 9,fraction = 3)
    private Double price;
    @NotNull
    private Integer qtyInStock;
    private Boolean status;
    private MultipartFile[] imgProductFile;
}
