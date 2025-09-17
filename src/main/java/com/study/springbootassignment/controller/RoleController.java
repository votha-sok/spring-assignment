package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.role.*;
import com.study.springbootassignment.dto.user.UserMapper;
import com.study.springbootassignment.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE')")
    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll().stream().map(RoleMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleFeaturePermissionDto> getById(@PathVariable Long id) {
        RoleFeaturePermissionDto response = RoleMapper.toDetailDto(roleService.findById(id));
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyAuthority('CREATE_ROLE')")
    @PostMapping
    public RoleDto save(@RequestBody CreateRoleDto request) {
        return RoleMapper.toDto(roleService.save(request.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE')")
    @PostMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @Valid @RequestBody CreateRoleDto role) {
        return RoleMapper.toDto(roleService.update(id, role.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('APPLY_FEATURE_ROLE')")
    @PostMapping("/apply-feature")
    public RoleDto update(@RequestBody CreateRoleFeature request) {
        return RoleMapper.toDto(roleService.applyRoleFeature(request));
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE')")
    @GetMapping("/list")
    public Page<RoleDto> list(@RequestParam
                              Map<String, String> params,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "order", defaultValue = "DESC") String order,
                              @RequestParam(value = "sort", defaultValue = "id") String sort) {
        return roleService.list(params, page, size).map(RoleMapper::toDto);
    }
}
