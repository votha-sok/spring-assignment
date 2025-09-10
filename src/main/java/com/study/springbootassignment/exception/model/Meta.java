package com.study.springbootassignment.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private int page;
    private int size;
    private int total;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
}