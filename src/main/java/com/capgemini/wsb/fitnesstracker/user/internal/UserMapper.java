package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(),
                           user.getFirstName(),
                           user.getLastName(),
                           user.getBirthdate(),
                           user.getEmail());
    }

    UserSimpleDto toSimpleDto(User user) {
        return new UserSimpleDto(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }

    public User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

    EmailDto toEmailDto(User user) {
        return new EmailDto(user.getId(),
                user.getEmail());
    }

    public User updateToEntity(UserDto userDto, Long id) {
        return new User(
                id,
                userDto.firstName(),
                userDto.lastName(),
                userDto.birthdate(),
                userDto.email());
    }

}
