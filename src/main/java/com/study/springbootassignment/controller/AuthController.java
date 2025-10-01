package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.user.LoginRequest;
import com.study.springbootassignment.dto.user.UserDto;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.exception.model.ErrorResponse;
import com.study.springbootassignment.jwt.JwtUtil;
import com.study.springbootassignment.service.UserService;
import com.study.springbootassignment.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        System.out.println("Login attempt: " + request.getEmail());
        System.out.println("Login attempt: " + request.getPassword());
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userDetails.user(); // ✅ safe

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public UserDto register() {
        UserEntity u1 = new UserEntity();
        u1.setUserName("super-user");
        u1.setEmail("superuser@gmail.com");
        u1.setPassword("super@123");
        u1.setPhone("010700088");
        u1.setAdmin(true);
        return UserMapper.toDto(userService.save(u1));
    }
}
