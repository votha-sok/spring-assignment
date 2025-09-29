package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.role.*;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.service.RoleFeaturePermissionService;
import com.study.springbootassignment.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final RoleFeaturePermissionService roleFeaturePermissionService;

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE','ROLE_ADMIN')")
    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll().stream().map(RoleMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleFeaturePermissionDto> getById(@PathVariable Long id) {
        RoleFeaturePermissionDto response = RoleMapper.toDetailDto(roleService.findById(id));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE','ROLE_ADMIN')")
    @GetMapping("/role-feature-permission/{id}")
    public ResponseEntity<List<FeaturePermissionCheckDto>> getRoleFeaturePermission(@PathVariable Long id) {
        RoleEntity role = roleService.findById(id);
        List<FeaturePermissionCheckDto> response = RoleMapper.toRoleFeaturePermissionDto(roleFeaturePermissionService.findAllByRole(role));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_ROLE','ROLE_ADMIN')")
    @PostMapping
    public RoleDto save(@RequestBody CreateRoleDto request) {
        return RoleMapper.toDto(roleService.save(request.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE','ROLE_ADMIN')")
    @PostMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @Valid @RequestBody CreateRoleDto role) {
        return RoleMapper.toDto(roleService.update(id, role.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('APPLY_ROLE','ROLE_ADMIN')")
    @PostMapping("/apply-feature")
    public RoleDto applyFeature(@RequestBody CreateRoleFeature request) {
        return RoleMapper.toDto(roleService.applyRoleFeature(request));
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE','ROLE_ADMIN')")
    @PostMapping("/apply-permission/{id}")
    public ResponseEntity<?> applyPermission(@PathVariable Long id, @Valid @RequestBody List<ApplyPermissionDto> request) {
        roleService.applyFeaturePermission(request, id);
        return ResponseEntity.ok(request);
    }

    @PreAuthorize("hasAnyAuthority('VIEW_ROLE','ROLE_ADMIN')")
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
