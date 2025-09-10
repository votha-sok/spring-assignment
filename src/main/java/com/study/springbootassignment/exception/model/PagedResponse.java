package com.study.springbootassignment.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse <T> extends Response<List<T>> {
    private  List<T> data;
    private  Meta meta;
}
