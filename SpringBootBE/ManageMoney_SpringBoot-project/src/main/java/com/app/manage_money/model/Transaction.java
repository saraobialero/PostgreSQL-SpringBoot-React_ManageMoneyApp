package com.app.manage_money.model;

import com.app.manage_money.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "label_id")
    private int labelId;

    @Column(name = "account_id")
    private int accountId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;

    @Column(name = "location")
    private String location;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "source")
    private String source;

    @Column(name = "is_recurring")
    private boolean isRecurring;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;



}
