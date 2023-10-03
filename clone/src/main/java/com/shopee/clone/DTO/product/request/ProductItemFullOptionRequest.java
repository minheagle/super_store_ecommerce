package com.shopee.clone.DTO.product.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductItemFullOptionRequest {
    private Long productId;
    @Digits(integer = 9,fraction = 3)
    private Double price;
    @NotNull
    private Integer qtyInStock;
    private Boolean status;
    private MultipartFile[] imgProductFile;
    private List<OptionTypeRequest> optionTypeRequestList;
}
