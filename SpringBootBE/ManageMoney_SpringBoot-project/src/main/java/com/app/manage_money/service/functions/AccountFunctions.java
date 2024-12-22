package com.app.manage_money.service.functions;

import java.math.BigDecimal;

public interface AccountFunctions {
    void updateBalance(Integer accountId, BigDecimal amount);
    void transferMoney(Integer sourceAccountId, Integer destinationAccountId, BigDecimal amount);
    BigDecimal calculateTotalBalance(Integer accountId);
    void checkBalanceThreshold(Integer accountId, BigDecimal threshold);
}
