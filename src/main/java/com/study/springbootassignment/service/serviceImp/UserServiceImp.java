package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.dto.user.ApplyUserRoleDto;
import com.study.springbootassignment.entity.RoleEntity;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.entity.UserRoleEntity;
import com.study.springbootassignment.repository.RoleRepository;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserEntity save(UserEntity userEntity) {
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity update(Long id, UserEntity request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        // Only encode if new password is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            // Avoid re-encoding if it looks like a BCrypt hash already
            if (!request.getPassword().startsWith("$2a$")) {
                user.setPassword(encoder.encode(request.getPassword()));
            } else {
                user.setPassword(request.getPassword()); // already encoded (edge case, e.g., data migration)
            }
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Page<UserEntity> list(Map<String, String> params, int page, int size) {
        Specification<UserEntity> spec = SpecificationBuilder.buildFromParams(params, UserEntity.class);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("DESC"), "id"));
        // ✅ Convert Page<UserEntity> -> Page<UserDto>
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public UserEntity applyUserRole(ApplyUserRoleDto request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (Long roleId : request.getRoleIds()) {
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

            boolean alreadyAssigned = user.getUserRoles().stream()
                    .anyMatch(ur -> ur.getRole().equals(role));

            if (!alreadyAssigned) {
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setUser(user);
                userRole.setRole(role);
                // Optional: set bidirectional relation
                user.getUserRoles().add(userRole);
            }
        }
        return userRepository.save(user);
    }
}
