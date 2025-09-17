package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.permission.CreatePermission;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.dto.permission.PermissionMapper;
import com.study.springbootassignment.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public List<PermissionDto> getPermissions() {
        return permissionService.findAll().stream().map(PermissionMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/{id}")
    public PermissionDto getPermission(@PathVariable Long id) {
        return  PermissionMapper.toDto(permissionService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('CREATE_PERMISSIONS')")
    @PostMapping
    public PermissionDto save(@Valid @RequestBody CreatePermission request) {
        return PermissionMapper.toDto(permissionService.save(request.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_PERMISSIONS')")
    @PutMapping("/{id}") // 👈 use PUT for full update
    public PermissionDto update(
            @PathVariable Long id,
            @Valid @RequestBody CreatePermission request) {
        return PermissionMapper.toDto(permissionService.update(id, request.toEntity()));
    }
}
