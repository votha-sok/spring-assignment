package com.study.springbootassignment.service.serviceImp;

import com.study.springbootassignment.configuration.SpecificationBuilder;
import com.study.springbootassignment.entity.UserEntity;
import com.study.springbootassignment.repository.UserRepository;
import com.study.springbootassignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity update(Long id, UserEntity request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
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
}
