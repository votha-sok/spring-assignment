package com.study.springbootassignment.controller.admin;

import com.study.springbootassignment.dto.role.*;
import com.study.springbootassignment.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
@AllArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll().stream().map(RoleMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoleFeaturePermissionDto> getById(@PathVariable Long id) {
        RoleFeaturePermissionDto response = RoleMapper.toDetailDto(roleService.findById(id));
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public RoleDto save(@RequestBody CreateRoleDto request) {
        return RoleMapper.toDto(roleService.save(request.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @Valid @RequestBody CreateRoleDto role) {
        return RoleMapper.toDto(roleService.update(id, role.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/apply-feature")
    public RoleDto update(@RequestBody CreateRoleFeature request) {
        return RoleMapper.toDto(roleService.applyRoleFeature(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
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
