package com.study.springbootassignment.exception;

import com.study.springbootassignment.exception.model.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.management.JMException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler  {

    // Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: ", ex);
        return buildResponse(500, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleException(JwtException ex, HttpServletRequest request) {
        log.error("JwtException exception: ", ex);
        return buildResponse(500, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleException(ExpiredJwtException ex, HttpServletRequest request) {
        log.error("ExpiredJwtException exception: ", ex);
        return buildResponse(500, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    }

    // 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("ResourceNotFoundException: ", ex);
        return buildResponse(404, "Resource Not Found", ex.getMessage(), request.getRequestURI());
    }

    // 401 Unauthorized (custom exception)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        log.error("UnauthorizedException: ", ex);
        return buildResponse(401, "Unauthorized", ex.getMessage(), request.getRequestURI());
    }

    // 403 Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.error("AccessDeniedException: ", ex);
        return buildResponse(403, "Access Denied", ex.getMessage(), request.getRequestURI());
    }

    // ResponseStatusException for Spring built-in exceptions
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleStatusException(ResponseStatusException ex, HttpServletRequest request) {
        log.error("ResponseStatusException: ", ex);
        return buildResponse(ex.getStatusCode().value(), ex.getStatusCode().toString(), ex.getReason(), request.getRequestURI());
    }

    // Validation errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        return buildResponse(400, "Validation Failed", errorMsg.toString(), request.getRequestURI());
    }

    // ---------------- Helper method ----------------
    private ResponseEntity<ErrorResponse> buildResponse(int status, String error, String message, String path) {
        String traceId = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        ErrorResponse resp = new ErrorResponse(
                200, // custom app code
                status,
                message != null ? message : error,
                traceId,
                path,
                timestamp
        );
        return ResponseEntity.status(status).body(resp);
    }
}
