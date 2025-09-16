package com.study.springbootassignment.service.serviceImp;


import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.dto.role.CreateRoleFeature;
import com.study.springbootassignment.entity.FeatureEntity;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.RoleFeatureEntity;
import com.study.springbootassignment.repository.FeatureRepository;
import com.study.springbootassignment.repository.RoleRepository;
import com.study.springbootassignment.service.RoleService;
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


    @Override
    public RoleEntity applyRoleFeature(CreateRoleFeature request) {
        RoleEntity roleEntity = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role  not found"));

        for (Long featureId : request.getFeatureIds()) {
            FeatureEntity feature = featureRepository.findById(featureId).orElseThrow(() -> new RuntimeException("Feature not found"));
            log.info("feature {}", feature);
            RoleFeatureEntity roleFeature = new RoleFeatureEntity();
            roleFeature.setFeature(feature);
            roleFeature.setRole(roleEntity);
            roleEntity.getRoleFeature().add(roleFeature);
        }
        return roleRepository.save(roleEntity);
    }

    @Override
    public Page<RoleEntity> list(Map<String, String> params, int page, int size) {
        Specification<RoleEntity> spec = SpecificationBuilder.buildFromParams(params, RoleEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        return roleRepository.findAll(spec, pageable);
    }

}
