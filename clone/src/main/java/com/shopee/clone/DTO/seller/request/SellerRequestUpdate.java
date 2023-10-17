package com.shopee.clone.DTO.seller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SellerRequestUpdate {
    private Long id;
    @NotBlank
    @Length(min = 3, max = 150)
    private String storeName;
    @NotBlank
    private String storeAddress;
    @NotBlank
    @Pattern(regexp = "(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b")
    private String storePhoneNumber;
    @NotBlank
    private String storeBankName;
    @NotBlank
    private String storeBankAccountNumber;

}
