package com.study.springbootassignment.controller;


import com.study.springbootassignment.dto.feature.*;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.FeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;

//        @PreAuthorize("hasAnyAuthority('FEATURE_VIEW')")
    @GetMapping
    public List<FeatureResponse> getFeatures() {
        return featureService.findAll().stream().map(FeatureMapper::toDto).toList();
    }

//        @PreAuthorize("hasAnyAuthority('FEATURE_CREATE')")
    @PostMapping
    public FeatureResponse create(@Valid @RequestBody CreateFeature body) {
        return FeatureMapper.toDto(featureService.save(body.toEntity()));
    }

//        @PreAuthorize("hasAnyAuthority('FEATURE_UPDATE')")
    @PostMapping("/{id}")
    public FeatureResponse update(@PathVariable Long id, @RequestBody @Valid CreateFeature body) {
        return FeatureMapper.toDto(featureService.update(id, body.toEntity()));
    }

//        @PreAuthorize("hasAnyAuthority('FEATURE_APPLY_PERMISSION')")
    @PostMapping("apply-permission")
    public FeatureResponse applyPermission(@RequestBody CreateFeaturePermission body) {
        return FeatureMapper.toDto(featureService.applyPermissions(body));
    }

    @GetMapping("/get-menu-item/{id}")
    public List<FeatureResponse> getMenu(@PathVariable Long id) {
        return featureService.getMenuByUser(id);
    }

    @GetMapping("function-permission/{id}")
    public List<FeaturePermissionResponse> getFeature(@PathVariable Long id) {
        return featureService.getPermissions(id);
    }

    @GetMapping("user-feature")
    public List<FeatureResponse> getFeature() {
        Long userId = UserContext.getUserId();
        log.info("user id is {}", userId);
        return featureService.getMenuByUser(userId);
    }

}
