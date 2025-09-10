package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.entity.UserEntity;

public class CreateUserDto extends BaseDtoUser {
    String password;
    public UserEntity toDto() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(getUserName());
        userEntity.setId(getId());
        userEntity.setPhone(getPhone());
        userEntity.setEmail(getEmail());
        userEntity.setPassword(password);
        return userEntity;
    }
}
