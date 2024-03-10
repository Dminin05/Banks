package com.minin.banks.mappers;

import com.minin.banks.dto.EmailDto;
import com.minin.banks.models.Email;
import com.minin.banks.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    @Mapping(source = "user", target = "user")
    Email toEntity(String email, User user);

    EmailDto.Response.BaseInfo toBaseInfo(Email email);

}
