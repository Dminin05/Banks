package com.minin.banks.mappers;

import com.minin.banks.models.MobilePhone;
import com.minin.banks.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MobilePhoneMapper {

    @Mapping(source = "user", target = "user")
    MobilePhone toEntity(String mobilePhone, User user);

}
