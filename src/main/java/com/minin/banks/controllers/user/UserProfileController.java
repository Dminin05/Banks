package com.minin.banks.controllers.user;

import com.minin.banks.dto.EmailDto;
import com.minin.banks.dto.MobilePhoneDto;
import com.minin.banks.services.user.UserService;
import com.minin.banks.services.user.profile.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/profile")
public class UserProfileController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("email")
    public void addNewEmail(Principal principal, @RequestBody EmailDto.Request.Create requestDto) {
        userService.addEmail(principal.getName(), requestDto);
    }

    @PostMapping("mobile-phone")
    public void addNewMobilePhone(Principal principal, @RequestBody MobilePhoneDto.Request.Create requestDto) {
        userService.addMobilePhone(principal.getName(), requestDto);
    }

    @PatchMapping("email")
    public void updateEmil(Principal principal, @RequestBody EmailDto.Request.Update requestDto) {
        userService.updateEmail(principal.getName(), requestDto);
    }

    @PatchMapping("mobile-phone")
    public void updateMobilePhone(Principal principal, @RequestBody MobilePhoneDto.Request.Update requestDto) {
        userService.updateMobilePhone(principal.getName(), requestDto);
    }

    @DeleteMapping("email/{email-id}")
    public void deleteEmailById(Principal principal, @PathVariable(name = "email-id") UUID id) {
        userService.deleteEmailById(principal.getName(), id);
    }

    @DeleteMapping("mobile-phone/{mobile-phone}")
    public void deleteMobilePhoneById(Principal principal, @PathVariable(name = "mobile-phone") UUID id) {
        userService.deleteMobilePhoneById(principal.getName(), id);
    }

}
