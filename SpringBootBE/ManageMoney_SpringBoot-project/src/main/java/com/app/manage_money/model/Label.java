package com.app.manage_money.model;

import com.app.manage_money.model.enums.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Set;


@Data
@Entity
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "category_type", referencedColumnName = "category_type"),
            @JoinColumn(name = "label_type", referencedColumnName = "allowed_label_type")
    })
    private CategoryLabelMapping categoryLabelMapping;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "label")
    private Set<RecurringTransaction> recurringTransactions;

    @OneToMany(mappedBy = "label")
    private Set<Transaction> transactions;
}