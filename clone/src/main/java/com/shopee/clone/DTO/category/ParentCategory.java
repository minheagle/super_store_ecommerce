package com.shopee.clone.DTO.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentCategory {
    private Long id;
    private Integer left;
    private Integer right;
}
