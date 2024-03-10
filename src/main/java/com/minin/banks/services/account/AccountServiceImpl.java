package com.minin.banks.services.account;

import com.minin.banks.dto.AccountDto;
import com.minin.banks.exceptions.BadRequestException;
import com.minin.banks.exceptions.ResourceNotFoundException;
import com.minin.banks.mappers.AccountMapper;
import com.minin.banks.models.Account;
import com.minin.banks.models.User;
import com.minin.banks.repository.AccountRepository;
import com.minin.banks.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final UserService userService;

    @Override
    public Account findById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("account_not_found"));
    }

    @Override
    public Account create(long startBalance) {
        return accountRepository.save(accountMapper.toEntity(startBalance));
    }

    @Override
    @Transactional
    public AccountDto.Response.Transaction transaction(String fromUsername, AccountDto.Request.Transaction requestDto) {

        User user = userService.findByUsername(fromUsername);

        Account fromAccount = user.getAccount();
        Account toAccount = findById(requestDto.getId());

        Long beforeBalanceInFromAccount = fromAccount.getBalance();
        Long beforeBalanceInToAccount = toAccount.getBalance();

        if (fromAccount.getBalance() < requestDto.getAmount()) {
            throw new BadRequestException("not_enough_money_on_your_balance");
        }

        int compare = fromAccount.toString().compareTo(toAccount.toString());

        if (compare > 0) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    fromAccount = debit(fromAccount, requestDto.getAmount());
                    toAccount = credit(toAccount, requestDto.getAmount());
                }
            }
        } else {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    fromAccount = debit(fromAccount, requestDto.getAmount());
                    toAccount = credit(toAccount, requestDto.getAmount());
                }
            }
        }

        return new AccountDto.Response.Transaction(
                fromAccount.getId(),
                toAccount.getId(),
                requestDto.getAmount(),
                beforeBalanceInFromAccount,
                beforeBalanceInToAccount,
                fromAccount.getBalance(),
                toAccount.getBalance()
        );
    }

    @Override
    public Account debit(Account account, long amount) {
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Override
    public Account credit(Account account, long amount) {
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

}
