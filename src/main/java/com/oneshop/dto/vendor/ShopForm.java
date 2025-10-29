package com.oneshop.dto.vendor;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ShopForm {
    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @Size(max = 300)
    private String address;

    @Size(max = 50)
    private String phone;

    private MultipartFile logo; // optional

    // view purposes
    private String currentLogo;
}
