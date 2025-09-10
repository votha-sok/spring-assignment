package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.entity.UserEntity;


public class UserMapper {

    public static BaseDtoUser toDto(UserEntity user) {
        return new BaseDtoUser(user.getId(), user.getUserName(), user.getEmail(), user.getPhone());
    }
}
