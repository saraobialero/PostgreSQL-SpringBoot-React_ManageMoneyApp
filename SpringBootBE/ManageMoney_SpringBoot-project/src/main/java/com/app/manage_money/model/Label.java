package com.app.manage_money.model;

import com.app.manage_money.model.enums.*;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_ype")
    private CategoryType categoryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "label_type")
    private LabelType labelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "is_active")
    private boolean isActive;

}
