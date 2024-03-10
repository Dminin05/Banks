package com.minin.banks.controllers;

import com.minin.banks.dto.AuthDto;
import com.minin.banks.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("registration")
    public AuthDto.Response.Tokens registration(@RequestBody AuthDto.Request.Registration registrationDto) {
        return authService.registration(registrationDto);
    }

    @PostMapping("login")
    public AuthDto.Response.Tokens login(@RequestBody AuthDto.Request.Login loginDto) {
        return authService.login(loginDto);
    }

}
