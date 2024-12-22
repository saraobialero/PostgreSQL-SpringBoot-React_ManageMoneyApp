package com.app.manage_money.model;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "account_recurring_transactions")
public class AccountRecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //verify for SK
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "transaction_id")
    private Integer transactionId;

    //Keep? As enum?
    @Column(name = "transaction_role")
    private String transactionRole;


}
