package com.oneshop.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductForm {
    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stock;

    private Boolean active = true;

    private MultipartFile image; // optional on edit
}
