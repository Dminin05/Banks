package com.minin.banks.controllers;

import com.minin.banks.dto.AccountDto;
import com.minin.banks.exceptions.BadRequestException;
import com.minin.banks.services.account.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/account")
@Tag(name = "Account controller")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("transaction")
    public AccountDto.Response.Transaction transaction(
            Principal principal,
            @Valid @RequestBody AccountDto.Request.Transaction requestDto,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return accountService.transaction(principal.getName(), requestDto);
    }

}
