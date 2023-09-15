package com.shopee.clone.DTO.product.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequestCreate {
    @NotBlank()
    @Length(min = 3, max = 150)
    private String productName;
    @NotBlank
    private String description;
    @Digits(integer = 9,fraction = 3)
    private Double price;
    @NotNull
    private Integer qtyInStock;
    private MultipartFile[] imgProductFile;
    private String optionName;
    private String valueName;
    private Double percent_price;
}
