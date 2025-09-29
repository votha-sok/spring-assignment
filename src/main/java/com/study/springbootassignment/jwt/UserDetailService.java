package com.study.springbootassignment.jwt;

import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.repository.RoleFeaturePermissionRepository;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.util.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleFeaturePermissionRepository roleFeaturePermissionRepository;
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {

            UserEntity user = userRepository.findByEmail((email))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Set<GrantedAuthority> authorities = new HashSet<>();
            user.getUserRoles().forEach(userRole -> {
                userRole.getRole().getRoleFeaturePermissions().forEach(rfp -> {
                    String authority = rfp.getPermission().getFunctionName().toUpperCase() + "_" + rfp.getFeature().getTitle().toUpperCase();
                    authorities.add(new SimpleGrantedAuthority(authority));
                });
            });
            if (user.getAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            log.info("Authorities: {}", authorities);
            return new UserPrincipal(user, authorities);
    }
}
