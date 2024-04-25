package com.nilo.communityapplication.model.mapper;

import com.nilo.communityapplication.model.dto.UserDTO;
import com.nilo.communityapplication.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserMapper implements Function<User, UserDTO> {


    @Override
    public UserDTO apply(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());

        return userDto;
    }
}
