package com.minin.banks.services.auth;

import com.minin.banks.dto.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    AuthDto.Response.Tokens registration(AuthDto.Request.Registration registerDto);

    AuthDto.Response.Tokens login(AuthDto.Request.Login loginRequest);

}
