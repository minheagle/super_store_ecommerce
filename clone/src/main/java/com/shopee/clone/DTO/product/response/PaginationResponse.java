package com.shopee.clone.DTO.product.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationResponse {
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
}
