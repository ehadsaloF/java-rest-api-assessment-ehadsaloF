package com.cbfacademy.apiassessment.Mappers;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    UserDTO userDTO (User user);
    User toUser (UserDTO userDTO);
}
