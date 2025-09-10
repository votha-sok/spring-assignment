package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.role.CreateRoleDto;
import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.dto.role.RoleDto;
import com.study.springbootassignment.dto.role.RoleMapper;
import com.study.springbootassignment.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll().stream().map(RoleMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public RoleDto getById(@PathVariable Long id) {
        return RoleMapper.toDto(roleService.findById(id));
    }


    @PostMapping
    public RoleDto save(@RequestBody CreateRoleDto request) {
        return RoleMapper.toDto(roleService.save(request.toEntity()));
    }

    @PostMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @Valid @RequestBody CreateRoleDto role) {
        return RoleMapper.toDto(roleService.update(id, role.toEntity()));
    }

    @PostMapping("/apply-feature")
    public RoleDto update(@RequestBody CreateRoleFeature request) {
        return RoleMapper.toDto(roleService.applyRoleFeature(request));
    }

}
