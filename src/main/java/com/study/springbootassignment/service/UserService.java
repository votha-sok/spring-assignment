package com.study.springbootassignment.service;


import com.study.springbootassignment.dto.user.ApplyUserRoleDto;
import com.study.springbootassignment.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    UserEntity save(UserEntity userEntity);
    UserEntity update(Long id, UserEntity userEntity);
    List<UserEntity> findAll();
    UserEntity findById(Long id);
    Page<UserEntity> list(Map<String, String> params, int page, int size);
    UserEntity applyUserRole(ApplyUserRoleDto request);

}
