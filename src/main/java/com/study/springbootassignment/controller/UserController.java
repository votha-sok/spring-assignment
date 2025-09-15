package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.user.UserDetailDto;
import com.study.springbootassignment.dto.user.UserDto;
import com.study.springbootassignment.dto.user.CreateUserDto;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/info")
    public UserDetailDto info() {
        Long userId = UserContext.getUserId();
        return UserMapper.toDetailDto(userService.findById(userId));
    }


    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserDto request) {
        return UserMapper.toDto(userService.save(request.toEntity()));
    }

    @PostMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody CreateUserDto request) {
        return UserMapper.toDto(userService.update(id, request.toEntity()));
    }


    @GetMapping
    public List<UserDto> finAll() {
        return userService.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return  UserMapper.toDto(userService.findById(id));
    }

//    @GetMapping("/list")
//    public Page<UserDto> list(
//            @RequestParam Map<String, String> params,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            @RequestParam(value = "order", defaultValue = "DESC") String order,
//            @RequestParam(value = "sort", defaultValue = "id") String sort) {
//        return userService.list(params, page, size).map(UserMapper::toDto);
//    }
    @GetMapping("/list")
    HttpEntity<Page<UserDto>> list(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "DESC") String order,
            @RequestParam(value = "sort", defaultValue = "id") String sor
    ) {
        Page<UserDto> people = userService.list(params, page, size).map(UserMapper::toDto);
        return ResponseEntity.ok(people);
    }
}
