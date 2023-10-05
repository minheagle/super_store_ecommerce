package com.shopee.clone.DTO.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionTypeCreate {
    private Long productItemId;
    private List<OptionTypeRequest> optionTypeRequestList;
}