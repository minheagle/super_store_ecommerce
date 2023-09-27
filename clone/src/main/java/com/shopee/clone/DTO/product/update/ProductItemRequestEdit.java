package com.shopee.clone.DTO.product.update;

import com.shopee.clone.DTO.product.request.OptionTypeRequest;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemRequestEdit {
    @Digits(integer = 9,fraction = 3)
    private Double price;
    @NotNull
    private Integer qtyInStock;
    //Khong duoc sua optionType - chi dc sua optionValue thoi
    //front-end van hien cho optionTypeName nhung khong cho sua doi
    private List<OptionTypeRequest> optionTypes;
}
