package com.shopee.clone.DTO.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestCreate {
    private Long sellerId;
    private Long categoryId;
    @NotBlank
    @Length(min = 3, max = 150)
    private String productName;
    @NotBlank
    private String description;
}
