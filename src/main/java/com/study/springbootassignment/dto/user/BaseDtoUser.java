package com.study.springbootassignment.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseDtoUser {
    private Long id;
    @NotNull(message = "User name can not be null.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "User name must contain only letters and spaces.")
    private String userName;
    @NotNull(message = "Email can not be null.")
    @Email(message = "Email must be a valid email address.")
    private String email;
    private String phone;
}
