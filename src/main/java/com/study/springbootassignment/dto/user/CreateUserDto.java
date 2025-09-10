package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;



@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserDto extends BaseDtoUser {
    @NotNull(message = "Password can not be null.")
    private String password;
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
