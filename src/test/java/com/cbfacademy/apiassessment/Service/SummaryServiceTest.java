package com.cbfacademy.apiassessment.Service;


import com.cbfacademy.apiassessment.DTO.*;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("The Summary Service can")
public class SummaryServiceTest {
    @InjectMocks
    private SummaryService summaryService;

    @Mock
    private BudgetService budgetService;
    @Mock
    private ExpensesService expensesService;


    private Budget budget1;
    private Budget budget2;
    private Expenses updatedExpense1;
    private Expenses updatedExpense2;
    private Expenses updatedExpense3;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        User user = User.builder().
                username("tester").
                name("Test User").
                email("user@email.com").
                build();
        user.setId(1L);

        budget1 = new Budget(120, SubCategories.Category.Food, SubCategories.Restaurant, "Food");
        budget1.setUser(user);
        budget1.setId(1L);


        budget2 = new Budget(400, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        budget2.setUser(user);
        budget2.setId(2L);

        updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);


        updatedExpense2 = new Expenses(5.49, SubCategories.Category.Food, SubCategories.Restaurant,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense1.setBudget(budget1);
        updatedExpense2.setId(2L);


        updatedExpense3 = new Expenses(3.49, SubCategories.Category.Food, SubCategories.Restaurant,  "Kebab Shop");
        updatedExpense3.setUser(user);
        updatedExpense1.setBudget(budget1);
        updatedExpense3.setId(3L);

    }


    @Test
    @DisplayName("can get summary")
    void testGetSummary(){
        // Arrange
        String usernameOrEmail = "user@email.com";

        List<Budget> budgets = Arrays.asList(budget1, budget2);
        List<Expenses> expensesForBudget1 = Arrays.asList(updatedExpense2, updatedExpense3);
        List<Expenses> expensesForBudget2 = Collections.singletonList(updatedExpense1);
        List<Expenses> expenses = Arrays.asList(updatedExpense1, updatedExpense2, updatedExpense3);


        when(budgetService.getAllBudgets(usernameOrEmail)).thenReturn(budgets);
        when(expensesService.getAllExpenses(usernameOrEmail)).thenReturn(expenses);
        when(expensesService.getExpensesByBudget(usernameOrEmail, 1L)).thenReturn(expensesForBudget1);
        when(expensesService.getExpensesByBudget(usernameOrEmail, 2L)).thenReturn(expensesForBudget2);


        // Act
        Summary result = summaryService.getSummary(usernameOrEmail);

        // Assert
        assertEquals(108.98, result.getTotalExpensesAmount());
        assertEquals(2, result.getBudgetSummaries().size());

        BudgetSummary budgetSummary1 = result.getBudgetSummaries().get(0);
        assertEquals(1L, budgetSummary1.getBudgetId());
        assertEquals("Food", budgetSummary1.getBudgetCategory());
        assertEquals("Restaurant", budgetSummary1.getBudgetSubcategory());
        assertEquals(120, budgetSummary1.getBudgetAmount());
        assertEquals(111.02, budgetSummary1.getAmountLeft());

        BudgetSummary budgetSummary2 = result.getBudgetSummaries().get(1);
        assertEquals(2L, budgetSummary2.getBudgetId());
        assertEquals("Savings", budgetSummary2.getBudgetCategory());
        assertEquals("Basic", budgetSummary2.getBudgetSubcategory());
        assertEquals(400, budgetSummary2.getBudgetAmount());
        assertEquals(300, budgetSummary2.getAmountLeft());
    }
}
