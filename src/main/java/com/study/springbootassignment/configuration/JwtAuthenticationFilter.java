package com.study.springbootassignment.configuration;


import com.study.springbootassignment.jwt.JwtUtil;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.jwt.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    private final String HEALTH_CHECK = "/health";
    private final String LOGIN = "/login";
    private final String ADMIN = "/admin/**";
    private final String REGISTER = "/users/register";
    private final List<String> EXCLUDED_PATHS = List.of(
            HEALTH_CHECK, LOGIN, REGISTER, ADMIN
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        BufferedRequestWrapper wrappedRequest = new BufferedRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

// Trace ID setup...
        String traceId = UUID.randomUUID().toString();
        wrappedRequest.setAttribute("traceId", traceId);
        MDC.put("processId", traceId);

// Log request body BEFORE filter chain
        String requestBody = wrappedRequest.getRequestBody(wrappedRequest.getCharacterEncoding());
        if (!requestBody.isEmpty()) {
            log.info("REQUEST: {} {} from {} \n {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    requestBody);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(jwt);
                Long userId = jwtUtil.extractUserId(jwt);
                UserContext.setUserId(userId);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (JwtException e) {
                log.error("JWT error: {}", e.getMessage());
                jwtAuthenticationEntryPoint.commence(request, response,
                        new AuthenticationServiceException(e.getMessage())); // message you want
                return; // stop filter chain
            }
        }

        filterChain.doFilter(wrappedRequest, wrappedResponse);
        byte[] responseArray = wrappedResponse.getContentAsByteArray();
        if (responseArray.length > 0) {
            String responseBody = new String(responseArray, wrappedResponse.getCharacterEncoding());
            log.info("RESPONSE: {} {} from {} \n {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    responseBody);
        }
        wrappedResponse.copyBodyToResponse();
        MDC.clear();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> MATCHER.match(pattern, request.getRequestURI()));
    }
}
