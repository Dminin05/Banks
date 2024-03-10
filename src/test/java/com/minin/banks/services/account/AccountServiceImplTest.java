package com.minin.banks.services.account;

import com.minin.banks.dto.AccountDto;
import com.minin.banks.exceptions.BadRequestException;
import com.minin.banks.mappers.AccountMapper;
import com.minin.banks.models.Account;
import com.minin.banks.models.User;
import com.minin.banks.repository.AccountRepository;
import com.minin.banks.services.user.UserService;
import jakarta.validation.Validator;
import org.hibernate.dialect.function.AvgFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserService userService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountServiceImpl accountServiceMock;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void transaction_success() {

        final String inputUsername = "test";
        final AccountDto.Request.Transaction inputTransaction = new AccountDto.Request.Transaction(
                UUID.randomUUID(),
                100L
        );

        Account fromAccount = new Account(
                UUID.randomUUID(),
                1000L
        );

        Account toAccount = new Account(
                UUID.randomUUID(),
                190L
        );

        AccountDto.Response.Transaction expectedResult = new AccountDto.Response.Transaction(
                fromAccount.getId(),
                toAccount.getId(),
                inputTransaction.getAmount(),
                fromAccount.getBalance(),
                toAccount.getBalance(),
                fromAccount.getBalance() - inputTransaction.getAmount(),
                toAccount.getBalance() + inputTransaction.getAmount()
        );

        when(userService.findByUsername(inputUsername))
                .thenReturn(
                        new User(
                                UUID.randomUUID(),
                                "testName",
                                "testSurname",
                                "testPatronymic",
                                "testUsername",
                                "testPassword",
                                Date.from(Instant.now()),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                fromAccount
                        )
                );

        when(accountRepository.findById(any()))
                .thenReturn(
                        Optional.of(toAccount)
                );

        when(accountRepository.save(fromAccount)).thenReturn(fromAccount);

        when(accountRepository.save(toAccount)).thenReturn(toAccount);

        AccountDto.Response.Transaction result = accountService.transaction(
                inputUsername,
                inputTransaction
        );

        assertEquals(result, expectedResult);

    }

    @Test
    void transaction_failed_not_enough_money() {
        final String inputUsername = "test";
        final AccountDto.Request.Transaction inputTransaction = new AccountDto.Request.Transaction(
                UUID.randomUUID(),
                1001L
        );

        Account fromAccount = new Account(
                UUID.randomUUID(),
                1000L
        );

        Account toAccount = new Account(
                UUID.randomUUID(),
                190L
        );

        when(userService.findByUsername(inputUsername))
                .thenReturn(
                        new User(
                                UUID.randomUUID(),
                                "testName",
                                "testSurname",
                                "testPatronymic",
                                "testUsername",
                                "testPassword",
                                Date.from(Instant.now()),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                fromAccount
                        )
                );

        when(accountRepository.findById(any()))
                .thenReturn(
                        Optional.of(toAccount)
                );

        assertThrows(BadRequestException.class, () -> {
            AccountDto.Response.Transaction result = accountService.transaction(
                    inputUsername,
                    inputTransaction
            );
        });
    }

}