package com.oneshop.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private Long id;
    private String username;
    private String email;
    private String password; // để trống khi edit nếu không đổi
    private List<String> roles;
}