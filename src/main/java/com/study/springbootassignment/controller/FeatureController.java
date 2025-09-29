package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.feature.*;
import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.FeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<FeaturePermissionResponse>> getFeatures() {
        List<FeaturePermissionResponse> responses = featureService.findAll().stream().map(FeatureMapper::toDetailDto).toList();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public FeatureResponse create(@Valid @RequestBody CreateFeature body) {
        return FeatureMapper.toDto(featureService.save(body.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public FeatureResponse update(@PathVariable Long id, @RequestBody @Valid CreateFeature body) {
        return FeatureMapper.toDto(featureService.update(id, body.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("apply-permission")
    public FeatureResponse applyPermission(@RequestBody CreateFeaturePermission body) {
        return FeatureMapper.toDto(featureService.applyPermissions(body));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/get-menu-item/{id}")
    public List<FeatureResponse> getMenu(@PathVariable Long id) {
        return featureService.getMenuByUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("function-permission/{id}")
    public List<FeaturePermissionResponse> getFeature(@PathVariable Long id) {
        return featureService.getPermissions(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("user-feature")
    public List<FeatureResponse> getFeature() {
        Long userId = UserContext.getUserId();
        log.info("user id is {}", userId);
        return featureService.getMenuByUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("menu-permission")
    public ResponseEntity<List<MenuPermissionResponse>> menuPermission() {
        List<FeatureEntity> allFeatures = featureService.findAll();
        List<MenuPermissionResponse> rootMenus = allFeatures.stream()
                .filter(f -> f.getParentId() == null) // root features
                .map(f -> FeatureMapper.toMenuPermissionDto(f, allFeatures))
                .toList();
        return ResponseEntity.ok(rootMenus);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("feature-permission/{id}")
    public ResponseEntity<List<FeaturePermissionResponse>> featurePermission(@PathVariable Long id) {
        List<FeaturePermissionResponse> response = featureService.findFeatureByRole(id).stream().map(FeatureMapper::toFeaturePermissionDto).toList();
        return ResponseEntity.ok(response);
    }
}
