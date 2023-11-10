package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity(name = "Expenses")
@Table(name = "Expenses")
public class Expenses extends BaseEntity implements Serializable {
    @Column(name = "amount")
    private double expenseAmount;

    @Column(name = "category")
    private SubCategories.Category expenseCategory;

    @Column(name = "subcategory", nullable = true)
    private SubCategories expenseSubcategory;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // User who logged the expense

    @ManyToOne(optional = true)
    @JoinColumn(name = "budget_id")
    private Budget budget; // The budget to which the expense is connected

    public Expenses(double expenseAmount, SubCategories.Category expenseCategory, SubCategories expenseSubcategory, String description){
        setExpenseAmount(expenseAmount);
        setExpenseCategory(expenseCategory);
        setExpenseSubcategory(expenseSubcategory);
        setDescription(description);
    }
}
