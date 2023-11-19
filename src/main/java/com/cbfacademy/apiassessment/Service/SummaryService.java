package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.BudgetSummary;
import com.cbfacademy.apiassessment.DTO.Summary;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService implements ISummaryService {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ExpensesService expensesService;

    @Override
    public Summary getSummary(String usernameOrEmail) {
        Summary summary = new Summary();

        List<Budget> budgets = budgetService.getAllBudgets(usernameOrEmail);

        if (budgets.isEmpty()) {
            // No budgets available, set empty list
            summary.setBudgetSummaries(new ArrayList<>());
        }
        else {
            List<BudgetSummary> budgetSummaries = new ArrayList<>();
            

            for (Budget budget : budgets) {
                BudgetSummary budgetSummary = new BudgetSummary();
                budgetSummary.setBudgetId(budget.getId());
                budgetSummary.setBudgetCategory(budget.getBudgetCategory().name());
                budgetSummary.setBudgetSubcategory(budget.getBudgetSubcategory().name());
                budgetSummary.setBudgetAmount(budget.getBudgetAmount());

                double amountSpent = 0;

                List<Expenses> expensesByBudget = expensesService.getExpensesByBudget(usernameOrEmail, budget.getId());

                if (!expensesByBudget.isEmpty()) {
                    amountSpent = expensesByBudget.stream().mapToDouble(Expenses::getExpenseAmount).sum();
                }

                double amountLeft = budget.getBudgetAmount() - amountSpent;
                budgetSummary.setAmountLeft(amountLeft);

                budgetSummaries.add(budgetSummary);
            }
            summary.setBudgetSummaries(budgetSummaries);
        }

        // Handling expenses separately
        List<Expenses> expensesList = expensesService.getAllExpenses(usernameOrEmail);
        if (expensesList.isEmpty()) {
            // No expenses available, set total expenses to zero
            summary.setTotalExpensesAmount(0);
        }
        else {
            double totalExpensesAmount = expensesList.stream().mapToDouble(Expenses::getExpenseAmount).sum();
            summary.setTotalExpensesAmount(totalExpensesAmount);
        }

        return summary;
    }
}
