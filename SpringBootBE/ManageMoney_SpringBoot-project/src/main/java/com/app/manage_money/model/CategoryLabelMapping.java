package com.app.manage_money.model;

import com.app.manage_money.model.enums.CategoryType;
import com.app.manage_money.model.enums.LabelType;
import com.app.manage_money.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "category_label_mappings")
public class CategoryLabelMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private CategoryType categoryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_label_type")
    private LabelType allowedLabelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

}
