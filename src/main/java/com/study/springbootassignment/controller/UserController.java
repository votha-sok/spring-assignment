package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.user.BaseDtoUser;
import com.study.springbootassignment.dto.user.CreateUserDto;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public BaseDtoUser register() {
        UserEntity u1 = new UserEntity();
        u1.setUserName("votha");
        u1.setEmail("vothasok@example.com");
        u1.setPassword(encoder.encode("1234"));
        u1.setPhone("015600022");
        u1.setIsSuperAdmin(true);
        return UserMapper.toDto(userService.save(u1));
    }


    @PostMapping
    public BaseDtoUser create(@Valid @RequestBody CreateUserDto request) {
        return UserMapper.toDto(userService.save(request.toDto()));
    }

    @PostMapping("/{id}")
    public BaseDtoUser update(@PathVariable Long id, @RequestBody CreateUserDto request) {
        return UserMapper.toDto(userService.update(id, request.toDto()));
    }


    @GetMapping
    public List<BaseDtoUser> finAll() {
        return userService.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BaseDtoUser findById(@PathVariable Long id) {
        return  UserMapper.toDto(userService.findById(id));
    }

    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping("/list")
    public Page<BaseDtoUser> list(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sort) {
        return userService.list(params, page, size).map(UserMapper::toDto);
    }
}
