package com.minin.banks.controllers.user;

import com.minin.banks.dto.UserDto;
import com.minin.banks.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDto.Response.BaseInfo getBaseInfoByEmailOrMobilePhone(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String mobilePhone
    ) {
        if (email == null) {
            return userService.findByMobilePhone(mobilePhone);
        } else {
            return userService.findByEmail(email);
        }
    }

    @GetMapping("list")
    public List<UserDto.Response.BaseInfo> getBaseInfoListByBirthDateAfter(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date birthDate,
            @RequestParam(required = false) String FIOLike,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "1") int pageSize,
            @RequestParam(required = false, defaultValue = "asc") String sort
    ) {
        if (birthDate == null) {
            return userService.findByFIOLike(FIOLike, page, pageSize, sort);
        } else {
            return userService.findByBirthDateAfter(birthDate, page, pageSize, sort);
        }
    }

}
