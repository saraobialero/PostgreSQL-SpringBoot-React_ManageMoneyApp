package com.app.manage_money.model;

import com.app.manage_money.model.enums.TransactionRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Getter
@Setter
@ToString(exclude = {"account", "recurringTransaction"})
@EqualsAndHashCode(exclude = {"account", "recurringTransaction"})
@Table(name = "account_recurring_transactions")
public class AccountRecurringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recurring_id")
    private RecurringTransaction recurringTransaction;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TransactionRole transactionRole;
}
