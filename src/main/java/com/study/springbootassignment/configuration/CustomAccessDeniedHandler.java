package com.study.springbootassignment.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springbootassignment.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.error("Access denied: {}", accessDeniedException.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                1,
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden: " + accessDeniedException.getMessage(),
                UUID.randomUUID().toString(),
                request.getRequestURI(),
                Instant.now().toString()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
