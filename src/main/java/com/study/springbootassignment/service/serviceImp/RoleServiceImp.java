package com.study.springbootassignment.service.serviceImp;


import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.dto.role.ApplyPermissionDto;
import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.entity.*;
import com.study.springbootassignment.repository.FeatureRepository;
import com.study.springbootassignment.repository.PermissionRepository;
import com.study.springbootassignment.repository.RoleFeaturePermissionRepository;
import com.study.springbootassignment.repository.RoleRepository;
import com.study.springbootassignment.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;
    private final FeatureRepository featureRepository;
    private final PermissionRepository permissionRepository;
    private final RoleFeaturePermissionRepository roleFeaturePermissionRepository;
    @Override
    public RoleEntity findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role Not Found"));
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public RoleEntity update(Long id, RoleEntity role) {
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role Not Found"));
        roleEntity.setName(role.getName());
        roleEntity.setDescription(role.getDescription());
        return roleRepository.save(role);
    }


//    @Override
//    public RoleEntity applyRoleFeature(CreateRoleFeature request) {
//        RoleEntity roleEntity = roleRepository.findById(request.getRoleId())
//                .orElseThrow(() -> new RuntimeException("Role  not found"));
//        log.info("Applying Role Feature {}",roleEntity);
//        roleEntity.getRoleFeature().clear();
//        for (Long featureId : request.getFeatureIds()) {
//            FeatureEntity feature = featureRepository.findById(featureId).orElseThrow(() -> new RuntimeException("Feature not found"));
//            log.info("feature {}", feature);
//            RoleFeatureEntity roleFeature = new RoleFeatureEntity();
//            roleFeature.setFeature(feature);
//            roleFeature.setRole(roleEntity);
//            roleEntity.getRoleFeature().add(roleFeature);
//        }
//        return roleRepository.save(roleEntity);
//    }
    @Override
    @Transactional
    public RoleEntity applyRoleFeature(CreateRoleFeature request) {
        RoleEntity roleEntity = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // clear old mappings
        roleEntity.getRoleFeature().clear();

        // fetch all features in one go
        List<FeatureEntity> features = featureRepository.findAllById(request.getFeatureIds());
        if (features.size() != request.getFeatureIds().size()) {
            throw new RuntimeException("Some features not found");
        }

        for (FeatureEntity feature : features) {
            RoleFeatureEntity rf = new RoleFeatureEntity();
            rf.setRole(roleEntity);
            rf.setFeature(feature);
            roleEntity.getRoleFeature().add(rf);
        }
        return roleRepository.save(roleEntity);
    }

    @Transactional
    @Override
    public void applyFeaturePermission(List<ApplyPermissionDto> requests,Long roleId) {
//        for (ApplyPermissionDto request : requests) {
//            log.info("request: {}", request);
//            FeatureEntity featureEntity = featureRepository.findById(request.getFeatureId())
//                    .orElseThrow(() -> new RuntimeException("Feature not found: " + request.getFeatureId()));
//
//            // Clear old mappings
//            featureEntity.getFeaturePermission().clear();
//
//            // Rebuild associations
//            for (Long permissionId : request.getPermissionIds()) {
//                log.info("permissionId: {}", permissionId);
//                PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
//                FeaturePermissionEntity fp = new FeaturePermissionEntity();
//                fp.setFeature(featureEntity);
//                fp.setPermission(permission);
//                fp.setStatus(true);
//                featureEntity.getFeaturePermission().add(fp);
//            }
//
//            featureRepository.save(featureEntity);
//        }

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        for (ApplyPermissionDto request : requests) {
            log.info("request: {}", request);

            FeatureEntity feature = featureRepository.findById(request.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found: " + request.getFeatureId()));

            // Clear old role-feature-permission mappings
            roleFeaturePermissionRepository.deleteByRoleAndFeature(role, feature);

            // Rebuild associations
            for (Long permissionId : request.getPermissionIds()) {
                PermissionEntity permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));

                RoleFeaturePermissionEntity rfp = new RoleFeaturePermissionEntity();
                rfp.setRole(role);
                rfp.setFeature(feature);
                rfp.setPermission(permission);

                roleFeaturePermissionRepository.save(rfp);
            }
        }
    }



    @Override
    public Page<RoleEntity> list(Map<String, String> params, int page, int size) {
        Specification<RoleEntity> spec = SpecificationBuilder.buildFromParams(params, RoleEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        return roleRepository.findAll(spec, pageable);
    }

}
