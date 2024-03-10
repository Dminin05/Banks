package com.minin.banks.services.auth;

import com.minin.banks.dto.AuthDto;
import com.minin.banks.exceptions.AlreadyExistsException;
import com.minin.banks.exceptions.AuthenticationException;
import com.minin.banks.models.Account;
import com.minin.banks.models.User;
import com.minin.banks.services.account.AccountService;
import com.minin.banks.services.user.UserService;
import com.minin.banks.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthDto.Response.Tokens registration(AuthDto.Request.Registration registerDto) {

        String codedPassword = passwordEncoder.encode(registerDto.getPassword());

        Account account = accountService.create(registerDto.getStartBalance());
        User user = userService.create(registerDto, account, codedPassword);

        UserDetails userDetails = loadUserByUsername(user.getUsername());
        String accessToken = jwtUtils.generateAccessToken(userDetails);

        return new AuthDto.Response.Tokens(accessToken);
    }

    @Override
    public AuthDto.Response.Tokens login(AuthDto.Request.Login loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new AuthenticationException(ex.getMessage());
        }

        UserDetails userDetails = loadUserByUsername(loginRequest.getUsername());
        String accessToken = jwtUtils.generateAccessToken(userDetails);

        return new AuthDto.Response.Tokens(accessToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

}
