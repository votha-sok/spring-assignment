package com.study.springbootassignment.dto.feature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDto {
    private Long id;
    @NotBlank(message = "Title required.")
    private String title;
    private String icon;
    @NotNull(message = "Menu order required.")
    private Integer menuOrder;
    private List<String> routerLink;
}
