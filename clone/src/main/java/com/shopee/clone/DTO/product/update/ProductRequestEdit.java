package com.shopee.clone.DTO.product.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestEdit {
    @NotBlank
    @Length(min = 3, max = 150)
    private String productName;
    @NotBlank
    private String description;
}
