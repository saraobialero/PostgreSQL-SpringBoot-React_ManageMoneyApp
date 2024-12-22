package com.app.manage_money.model;

import com.app.manage_money.model.enums.TransactionRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account_recurring_transactions")
public class AccountRecurringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_id")
    private RecurringTransaction recurringTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionRole transactionRole;
}
