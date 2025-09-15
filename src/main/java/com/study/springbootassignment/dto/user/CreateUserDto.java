package com.study.springbootassignment.dto.user;


import com.study.springbootassignment.entity.UserEntity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateUserDto extends UserDto {
    private final PasswordEncoder encoder;

    @NotNull(message = "Password can not be null or empty.")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number")
    private String password;
    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(getUserName());
        userEntity.setId(getId());
        userEntity.setPhone(getPhone());
        userEntity.setEmail(getEmail());
        userEntity.setPassword(encoder.encode(password));
        return userEntity;
    }
}
