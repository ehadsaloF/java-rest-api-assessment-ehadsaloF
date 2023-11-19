package com.cbfacademy.apiassessment.Mappers;

import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Entity.UserRoles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mappings({
            @Mapping(source = "createdAt", target = "created"),
            @Mapping(source = "updatedAt", target = "updated")
    })
    UserDTO userDTO (User user);

    User toUser (UserDTO userDTO);
}
