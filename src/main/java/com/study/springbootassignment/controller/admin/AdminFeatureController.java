package com.study.springbootassignment.controller.admin;

import com.study.springbootassignment.dto.feature.*;
import com.study.springbootassignment.jwt.UserContext;
import com.study.springbootassignment.service.FeatureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/feature")
@AllArgsConstructor
public class AdminFeatureController {
    private final FeatureService featureService;

    @PreAuthorize("hasAnyAuthority('FEATURE_VIEW')")
    @GetMapping
    public List<FeatureResponse> getFeatures() {
        return featureService.findAll().stream().map(FeatureMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyAuthority('FEATURE_CREATE')")
    @PostMapping
    public FeatureResponse create(@Valid @RequestBody CreateFeature body) {
        return FeatureMapper.toDto(featureService.save(body.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('FEATURE_UPDATE')")
    @PostMapping("/{id}")
    public FeatureResponse update(@PathVariable Long id, @RequestBody @Valid CreateFeature body) {
        return FeatureMapper.toDto(featureService.update(id, body.toEntity()));
    }

    @PreAuthorize("hasAnyAuthority('FEATURE_APPLY_PERMISSION')")
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
        return featureService.getMenuByUser(userId);
    }

}
