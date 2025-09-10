package com.study.springbootassignment.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDtoUser {
    private Long id;
    private String userName;
    private String email;
    private String phone;
}
