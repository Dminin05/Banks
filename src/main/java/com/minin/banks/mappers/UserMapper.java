package com.minin.banks.mappers;

import com.minin.banks.dto.AuthDto;
import com.minin.banks.dto.UserDto;
import com.minin.banks.models.Account;
import com.minin.banks.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "account", target = "account")
    @Mapping(source = "registerDto.name", target = "name")
    @Mapping(source = "registerDto.surname", target = "surname")
    @Mapping(source = "registerDto.patronymic", target = "patronymic")
    @Mapping(source = "registerDto.username", target = "username")
    @Mapping(source = "registerDto.birthDate", target = "birthDate")
    @Mapping(source = "password", target = "password")
    User toEntity(AuthDto.Request.Registration registerDto, String password, Account account);

    UserDto.Response.BaseInfo toBaseInfo(User user);

    List<UserDto.Response.BaseInfo> toBaseInfoList(List<User> users);

}
