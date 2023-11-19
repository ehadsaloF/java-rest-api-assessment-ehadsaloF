package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.DTO.BudgetSummary;
import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.DTO.Summary;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SummaryService implements ISummaryService{

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ExpensesService expensesService;

    @Override
    public Summary getSummary(String usernameOrEmail) throws EntityNotFoundException {

        Summary summary = new Summary();

        List<BudgetDTO> budgets = budgetService.getAllBudgets(usernameOrEmail);

        if(budgets.isEmpty()){
            throw new EntityNotFoundException("User Does Not Have Any budgets");
        }

        List<BudgetSummary> budgetSummaries = new ArrayList<>();

        double totalExpensesAmount = 0.0;

        for (BudgetDTO budget : budgets) {
            BudgetSummary budgetSummary = new BudgetSummary();
            budgetSummary.setBudgetId(budget.getId());
            budgetSummary.setBudgetCategory(budget.getCategory());
            budgetSummary.setBudgetSubcategory(budget.getSubcategory());
            budgetSummary.setBudgetAmount(budget.getAmount());

            double amountSpent = 0;

            List<ExpensesDTO> expensesByBudget = expensesService.getExpensesByBudget(usernameOrEmail, budget.getId());

            // Calculate amount left for each budget with connected expenses
            if(expensesByBudget.isEmpty()){
                amountSpent = 0;
            }
            else {
                amountSpent = expensesByBudget.stream().
                        mapToDouble(ExpensesDTO::getAmount).sum();
            }
            double amountLeft = budget.getAmount() - amountSpent;
            budgetSummary.setAmountLeft(amountLeft);

            budgetSummaries.add(budgetSummary);
        }

        List<ExpensesDTO> expensesDTOList = expensesService.getAllExpenses(usernameOrEmail);
        if(expensesDTOList.isEmpty()){
            throw new EntityNotFoundException("User Does Not Have Any Expenses");
        }
        double totalExpenseAmount = expensesDTOList.stream().mapToDouble(ExpensesDTO::getAmount).sum();

        summary.setTotalExpensesAmount(totalExpenseAmount);

        summary.setBudgetSummaries(budgetSummaries);


        return summary;
    }
}
