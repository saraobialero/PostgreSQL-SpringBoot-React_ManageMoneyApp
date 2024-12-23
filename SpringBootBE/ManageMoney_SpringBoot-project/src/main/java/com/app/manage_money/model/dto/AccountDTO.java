package com.app.manage_money.model.dto;


import com.app.manage_money.model.enums.AccountType;
import com.app.manage_money.model.enums.State;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDTO {
    private Integer id;
    private AccountType accountType;
    private State state;
    private BigDecimal balance;
}
