package com.minin.banks.services.account;

import com.minin.banks.dto.AccountDto;
import com.minin.banks.models.Account;

import java.util.UUID;

public interface AccountService {

    Account findById(UUID id);

    AccountDto.Response.Transaction transaction(String fromUsername, AccountDto.Request.Transaction requestDto);

    Account debit(Account account, long amount);

    Account credit(Account account, long amount);

    Account create(long startBalance);

}
