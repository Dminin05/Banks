package com.minin.banks.dto;

import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.util.UUID;

public enum AccountDto {;

    private interface Id { UUID getId(); }
    private interface Amount { @Positive(message = "amount_must_be_greater_than_0") Long getAmount(); }

    private interface FromAccountId {UUID getFromAccountId(); }
    private interface ToAccountId {UUID getToAccountId(); }
    private interface BeforeBalanceInFromAccount {Long getBeforeBalanceInFromAccount(); }
    private interface BeforeBalanceInToAccount {Long getBeforeBalanceInToAccount(); }
    private interface AfterBalanceInFromAccount {Long getAfterBalanceInFromAccount(); }
    private interface AfterBalanceInToAccount {Long getAfterBalanceInToAccount(); }

    public enum Request {;

        @Value
        public static class Transaction implements Id, Amount {
            UUID id;
            Long amount;
        }

    }

    public enum Response {;

        @Value
        public static class Transaction implements FromAccountId, ToAccountId, Amount, BeforeBalanceInFromAccount, BeforeBalanceInToAccount, AfterBalanceInFromAccount, AfterBalanceInToAccount {
            UUID fromAccountId;
            UUID toAccountId;
            Long amount;
            Long beforeBalanceInFromAccount;
            Long beforeBalanceInToAccount;
            Long afterBalanceInFromAccount;
            Long afterBalanceInToAccount;
        }

    }


}
