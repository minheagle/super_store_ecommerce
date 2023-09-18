package com.shopee.clone.DTO.fieldErrorDTO;

import com.shopee.clone.util.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldError {
    private String fieldError;
    private String errorMessage;

    public static ResponseEntity<?> throwErrorHandler(BindingResult bindingResult) {

            List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message("Validation errors")
                                    .results(fieldErrors)
                                    .build()
                    );
    }
}
