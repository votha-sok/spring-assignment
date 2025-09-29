package com.study.springbootassignment.exception.model;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int code;       // custom app code (0=success, 1=error, etc.)
    private int status;     // HTTP status
    private String message; // error or success message
    private String traceId; // unique id for tracking
    private String path;    // API path
    private String timestamp;
}
