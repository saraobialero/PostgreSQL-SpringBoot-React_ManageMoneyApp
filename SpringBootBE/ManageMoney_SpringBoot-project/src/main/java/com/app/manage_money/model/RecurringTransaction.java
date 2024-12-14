package com.app.manage_money.model;

import com.app.manage_money.model.enums.Frequency;
import com.app.manage_money.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "recurring_transactions")
public class RecurringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "name")
    private String name;

    @Column(name = "label_id")
    private int labelId;

    @Column(name = "account_id")
    private int accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;

    //Change type value on db
    @Column(name = "amount")
    private BigDecimal amount;

    //Remove currency (manage on front-end)

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private Frequency frequency;


    @Column(name = "start_date")
    private LocalDate startDate;


    @Column(name = "end_date")
    private LocalDate endDate;


    @Column(name = "next_occurrence")
    private LocalDate nextOccurrence;

    //Remove?
    @Column(name = "description")
    private String description;

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "source")
    private String source;

    @Column(name = "last_processed_date")
    private LocalDate lastProcessedDate;

    @Column(name = "is_active")
    private boolean isActive;

    //REMOVE NOTE
}
