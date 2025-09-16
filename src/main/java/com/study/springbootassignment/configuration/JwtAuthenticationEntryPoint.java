package com.study.springbootassignment.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springbootassignment.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {

        log.error("Authentication error: {}", authException.getMessage());
        String traceId = (String) request.getAttribute("traceId");
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized: " + authException.getMessage(),
                traceId,
                request.getRequestURI(),
                Instant.now().toString()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
