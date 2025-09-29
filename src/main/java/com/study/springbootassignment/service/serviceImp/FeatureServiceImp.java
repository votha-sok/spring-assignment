package com.study.springbootassignment.service.serviceImp;


import com.study.springbootassignment.dto.feature.CreateFeaturePermission;
import com.study.springbootassignment.dto.feature.FeaturePermissionResponse;
import com.study.springbootassignment.dto.feature.FeatureResponse;
import com.study.springbootassignment.dto.permission.PermissionDto;
import com.study.springbootassignment.entity.*;
import com.study.springbootassignment.repository.FeatureRepository;
import com.study.springbootassignment.repository.PermissionRepository;
import com.study.springbootassignment.repository.RoleRepository;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureServiceImp implements FeatureService {
    private final FeatureRepository featureRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public FeatureEntity save(FeatureEntity feature) {
        return featureRepository.save(feature);
    }

    @Override
    public List<FeatureEntity> findAll() {
        return featureRepository.findAll();
    }

    @Override
    public FeatureEntity findById(Long id) {
        return featureRepository.findById(id).orElseThrow(() -> new RuntimeException("Feature not found"));
    }

    @Override
    public FeatureEntity update(Long id, FeatureEntity request) {
        FeatureEntity feature = featureRepository.findById(id).orElseThrow(() -> new RuntimeException("Feature not found"));
        feature.setParentId(request.getParentId());
        feature.setMenuOrder(request.getMenuOrder());
        feature.setTitle(request.getTitle());
        feature.setRouterLink(request.getRouterLink());
        feature.setIcon(request.getIcon());
        return featureRepository.save(feature);
    }

    @Override
    public FeatureEntity applyPermissions(CreateFeaturePermission request) {
        FeatureEntity feature = featureRepository.findById(request.getFeatureId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (Long permissionId : request.getPermissionIds()) {
            PermissionEntity permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + permissionId));

            boolean alreadyAssigned = feature.getFeaturePermission().stream()
                    .anyMatch(ur -> ur.getPermission().equals(permission));

            if (!alreadyAssigned) {
                FeaturePermissionEntity featurePermission = new FeaturePermissionEntity();
                featurePermission.setFeature(feature);
                featurePermission.setPermission(permission);
                // Optional: set bidirectional relation
                feature.getFeaturePermission().add(featurePermission);
            }
        }

        return featureRepository.save(feature);
    }

    @Override
    public List<FeaturePermissionResponse> getPermissions(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return roleRepository.findAll().stream()
                .flatMap(userRole -> userRole.getFeatures().stream())
                .map(feature -> new FeaturePermissionResponse(
                        feature.getId(),
                        feature.getTitle(),
                        feature.getPermissions().stream()
                                .map(perm -> new PermissionDto(perm.getId(), perm.getFunctionName(), perm.getFunctionOrder()))
                                .toList()
                ))
                .toList();
    }

    @Override
    public List<FeatureResponse> getMenuByUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<FeatureEntity> userFeatures;
        if (Boolean.TRUE.equals(user.getAdmin())) {
            userFeatures = new HashSet<>(featureRepository.findAll());
        } else {
            userFeatures = user.getRoles().stream()
                    .flatMap(role -> role.getFeatures().stream())
                    .collect(Collectors.toSet());
        }

        // Pre-group features by parentId to avoid N+1 queries
        Map<Long, List<FeatureEntity>> childrenMap = userFeatures.stream()
                .filter(f -> f.getParentId() != null)
                .collect(Collectors.groupingBy(FeatureEntity::getParentId));

        // Only root features
        return userFeatures.stream()
                .filter(f -> f.getParentId() == null)
                .sorted(Comparator.comparingInt(FeatureEntity::getMenuOrder))
                .map(f -> toMenuItem(f, childrenMap))
                .collect(Collectors.toList());
    }

    private FeatureResponse toMenuItem(FeatureEntity feature, Map<Long, List<FeatureEntity>> childrenMap) {
        FeatureResponse item = new FeatureResponse();
        item.setId(feature.getId());
        item.setTitle(feature.getTitle());
        item.setIcon(feature.getIcon());
        item.setMenuOrder(feature.getMenuOrder());

        if (feature.getRouterLink() != null && !feature.getRouterLink().isEmpty()) {
            item.setRouterLink(feature.getRouterLink());
        }

        List<FeatureEntity> children = childrenMap.get(feature.getId());
        if (children != null && !children.isEmpty()) {
            List<FeatureResponse> items = children.stream()
                    .sorted(Comparator.comparingInt(FeatureEntity::getMenuOrder))
                    .map(child -> toMenuItem(child, childrenMap))
                    .collect(Collectors.toList());
            item.setRouterLink(null);
            item.setItems(items);
        } else {
            item.setItems(null);
        }

        return item;
    }

    @Override
    public List<FeatureEntity> findFeatureByRole(Long roleId) {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        return role.getFeatures().stream().filter(f -> f.getParentId() != null).toList();
    }
}
