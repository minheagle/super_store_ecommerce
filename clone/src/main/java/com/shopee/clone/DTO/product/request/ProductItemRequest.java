package com.shopee.clone.DTO.product.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemRequest {
    private Long productId;
    @Digits(integer = 9,fraction = 3)
    private Double price;
//    @NotBlank
    private Integer qtyInStock;
    private MultipartFile[] imgProductFile;
}
