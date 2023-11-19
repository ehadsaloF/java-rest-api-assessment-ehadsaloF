package com.cbfacademy.apiassessment.DTO;

import lombok.Data;

@Data
public class BudgetSummary {
    private Long budgetId;
    private String budgetCategory;
    private String budgetSubcategory;
    private double budgetAmount;
    private double amountLeft;
}
