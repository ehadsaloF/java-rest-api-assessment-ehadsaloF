package com.cbfacademy.apiassessment.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Expenses")
public class Budget extends BaseEntity implements Serializable {
    @Column(name = "amount")
    private double budgetAmount;

    @Column(name = "category")
    private Category budgetCategory;

    @Column(name = "subcategory", nullable = true)
    private Subcategory budgetSubcategory;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Expenses> expenses;

    public Budget(double budgetAmount, Category budgetCategory, Subcategory budgetSubcategory, String description){
        setBudgetAmount(budgetAmount);
        setBudgetCategory(budgetCategory);
        setBudgetSubcategory(budgetSubcategory);
        setDescription(description);
    }
}
