package com.study.springbootassignment.controller.admin;

import com.study.springbootassignment.dto.role.CreateRoleDto;
import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.dto.role.RoleDto;
import com.study.springbootassignment.dto.role.RoleMapper;
import com.study.springbootassignment.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
@AllArgsConstructor
public class AdminRoleController {

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
