package com.cbfacademy.apiassessment.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "Budget")
@Table(name = "Budget")
public class Budget extends BaseEntity implements Serializable {
    @Column(name = "amount")
    private double budgetAmount;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private SubCategories.Category budgetCategory;

    @Column(name = "subcategory", nullable = true)
    @Enumerated(EnumType.STRING)
    private SubCategories budgetSubcategory;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expenses> expenses;

    public Budget(double budgetAmount, SubCategories.Category budgetCategory, SubCategories budgetSubcategory, String description) {
        setBudgetAmount(budgetAmount);
        setBudgetCategory(budgetCategory);
        setBudgetSubcategory(budgetSubcategory);
        setDescription(description);
    }

    public String toString() {
        return "Budget(budgetAmount=" + this.getBudgetAmount() + ", budgetCategory=" + this.getBudgetCategory() + ", budgetSubcategory=" + this.getBudgetSubcategory() + ", description=" + this.getDescription() + ", expenses=" + this.getExpenses() + ")";
    }
}
