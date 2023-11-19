package com.cbfacademy.apiassessment.DTO;

import lombok.Data;

import java.util.List;

@Data
public class Summary {
    private double totalBudgetAmount;
    private double totalExpensesAmount;
    private List<BudgetSummary> budgetSummaries;
}
