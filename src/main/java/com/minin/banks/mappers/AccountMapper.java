package com.minin.banks.mappers;

import com.minin.banks.models.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "startBalance", target = "balance")
    Account toEntity(Long startBalance);

}
