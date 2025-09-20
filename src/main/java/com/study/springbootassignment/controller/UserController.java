package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.user.*;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasAnyAuthority('VIEW_USER')")
    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> info() {
        UserInfoDto response = UserMapper.toInfoDto(userService.findById(UserContext.getUserId()));
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyAuthority('CREATE_USER')")
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserDto request) {
        UserDto response = UserMapper.toDto(userService.save(request.toEntity()));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDto> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto request) {
        UserDetailDto response = UserMapper.toDetailDto(userService.update(id, request.toEntity()));
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyAuthority('VIEW_USER')")
    @GetMapping
    public ResponseEntity<List<UserDto>> finAll() {
        List<UserDto> response = userService.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDto> findById(@PathVariable Long id) {
        UserDetailDto response = UserMapper.toDetailDto(userService.findById(id));
        return ResponseEntity.ok(response);
    }

//    @PreAuthorize("hasAnyAuthority('APPLY_ROLE_USER')")
    @PostMapping("/apply-role")
    public ResponseEntity<UserInfoDto> assignRole(@RequestBody @Valid ApplyUserRoleDto request) {
        UserEntity user = userService.applyUserRole(request);
        UserInfoDto response = UserMapper.toInfoDto(user);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_USER')")
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
