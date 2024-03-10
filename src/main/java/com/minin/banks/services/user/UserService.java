package com.minin.banks.services.user;

import com.minin.banks.dto.AuthDto;
import com.minin.banks.dto.EmailDto;
import com.minin.banks.dto.MobilePhoneDto;
import com.minin.banks.dto.UserDto;
import com.minin.banks.models.Account;
import com.minin.banks.models.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User findById(UUID id);

    User findByUsername(String username);

    UserDto.Response.BaseInfo findByEmail(String email);

    UserDto.Response.BaseInfo findByMobilePhone(String mobilePhone);

    List<UserDto.Response.BaseInfo> findByBirthDateAfter(Date birthDate, int page, int pageSize, String sort);

    List<UserDto.Response.BaseInfo> findByFIOLike(String like, int page, int pageSize, String sort);

    User create(AuthDto.Request.Registration registerDto, Account account, String codedPassword);

    void addEmail(String username, EmailDto.Request.Create requestDto);

    void addMobilePhone(String username, MobilePhoneDto.Request.Create requestDto);

    void updateMobilePhone(String username, MobilePhoneDto.Request.Update requestDto);

    void updateEmail(String username, EmailDto.Request.Update requestDto);

    void deleteEmailById(String username, UUID id);

    void deleteMobilePhoneById(String username, UUID id);

    void deleteById(UUID id);

    boolean isAlreadyExistsByUsername(String username);

}
