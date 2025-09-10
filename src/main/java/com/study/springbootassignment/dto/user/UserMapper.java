package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.entity.UserEntity;


public class UserMapper {

    public static UserDto toDto(UserEntity user) {
        return new UserDto(user.getId(), user.getUserName(), user.getEmail(), user.getPhone(), user.getIsSuperAdmin());
    }
}
