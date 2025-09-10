package com.study.springbootassignment.jwt;

import com.study.springbootassignment.entity.UserEntity;
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

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByEmail((email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getUserRoles().forEach(userRole -> {
            userRole.getRole().getFeatures().forEach(feature -> {
                feature.getPermissions().forEach(permission -> {
                    String authority = feature.getTitle().toUpperCase()
                            + "_" + permission.getFunctionName().toUpperCase();
                    authorities.add(new SimpleGrantedAuthority(authority));
                });
            });
        });
        log.info("Authorities: {}", authorities);
        return new UserPrincipal(user, authorities);

    }
}
