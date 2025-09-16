package com.study.springbootassignment.controller.admin;

import com.study.springbootassignment.dto.user.*;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserDetailDto> info() {
        UserDetailDto response = UserMapper.toDetailDto(userService.findById(UserContext.getUserId()));
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserDto request) {
        UserDto response = UserMapper.toDto(userService.save(request.toEntity()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto request) {
        UserDto response = UserMapper.toDto(userService.update(id, request.toEntity()));
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<UserDto>> finAll() {
        List<UserDto> response = userService.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto response = UserMapper.toDto(userService.findById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply-role")
    public ResponseEntity<UserDetailDto> assignRole(@RequestBody @Valid ApplyUserRoleDto request) {
        UserEntity user = userService.applyUserRole(request);
        UserDetailDto response = UserMapper.toDetailDto(user);
        return ResponseEntity.ok(response);
    }

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
