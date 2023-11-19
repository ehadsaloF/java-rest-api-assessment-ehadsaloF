package com.cbfacademy.apiassessment.Entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity(name = "Expenses")
@Table(name = "Expenses")
public class Expenses extends BaseEntity implements Serializable {
    @Column(name = "amount")
    private double expenseAmount;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private SubCategories.Category expenseCategory;

    @Column(name = "subcategory", nullable = true)
    @Enumerated(EnumType.STRING)
    private SubCategories expenseSubcategory;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // User who logged the expense

    @ManyToOne(optional = true)
    @JoinColumn(name = "budget_id")
    @JsonIgnore
    private Budget budget; // The budget to which the expense is connected

    public Expenses(double expenseAmount, SubCategories.Category expenseCategory, SubCategories expenseSubcategory, String description) {
        setExpenseAmount(expenseAmount);
        setExpenseCategory(expenseCategory);
        setExpenseSubcategory(expenseSubcategory);
        setDescription(description);
    }

    public String toString() {
        if (getBudget() != null) {
            return "Expenses{" +
                    "id=" + getId() +
                    ", createdAt=" + getCreatedAt() +
                    ", updatedAt=" + getUpdatedAt() +
                    ", expenseAmount=" + getExpenseAmount() +
                    ", expenseCategory=" + getExpenseCategory() +
                    ", expenseSubcategory=" + getExpenseSubcategory() +
                    ", description='" + getDescription() + '\'' +
                    ", user=" + getUser().getId() +
                    ", budget=" + getBudget().getId() +
                    '}';
        } else {
            return "Expenses{" +
                    "id=" + getId() +
                    ", createdAt=" + getCreatedAt() +
                    ", updatedAt=" + getUpdatedAt() +
                    ", expenseAmount=" + getExpenseAmount() +
                    ", expenseCategory=" + getExpenseCategory() +
                    ", expenseSubcategory=" + getExpenseSubcategory() +
                    ", description='" + getDescription() + '\'' +
                    ", user=" + getUser().getId() +
                    ", budget=null" +
                    '}';
        }
    }
}
