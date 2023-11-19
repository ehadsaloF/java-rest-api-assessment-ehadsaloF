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


    private User user;
    private Budget budget1;
    private Budget budget2;
    private BudgetDTO budgetDTO1;
    private BudgetDTO budgetDTO2;
    private Expenses updatedExpense1;
    private Expenses updatedExpense2;
    private Expenses updatedExpense3;
    private ExpensesDTO expensesDTO1;
    private ExpensesDTO expensesDTO2;
    private ExpensesDTO expensesDTO3;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = User.builder().
                username("tester").
                name("Test User").
                email("user@email.com").
                build();
        user.setId(1L);

        budget1 = new Budget(120, SubCategories.Category.Food, SubCategories.Restaurant, "Food");
        budget1.setUser(user);
        budget1.setId(1L);
        budgetDTO1 = new BudgetDTO(1L, 120, "Food", "Restaurant", "Food", 1L);


        budget2 = new Budget(400, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        budget2.setUser(user);
        budget2.setId(2L);
        budgetDTO2 = new BudgetDTO(2L, 400, "Savings", "Basic", "From Weekend Bar Shift", 1L);

        updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);
        expensesDTO1 = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L, 2L);


        updatedExpense2 = new Expenses(5.49, SubCategories.Category.Food, SubCategories.Restaurant,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense1.setBudget(budget1);
        updatedExpense2.setId(2L);
        expensesDTO2 = new ExpensesDTO(2L, 5.49, "Food", "Restaurant", "KFC", 1L, 2L);


        updatedExpense3 = new Expenses(3.49, SubCategories.Category.Food, SubCategories.Restaurant,  "Kebab Shop");
        updatedExpense3.setUser(user);
        updatedExpense1.setBudget(budget1);
        updatedExpense3.setId(3L);
        expensesDTO3 = new ExpensesDTO(3L, 3.49, "Food", "Restaurant", "Kebab Shop", 1L, 2L);

    }


    @Test
    @DisplayName("can get summary")
    void testGetSummary(){
        // Arrange
        String usernameOrEmail = "user@email.com";

        List<BudgetDTO> budgets = Arrays.asList(budgetDTO1, budgetDTO2);
        List<ExpensesDTO> expensesForBudget1 = Arrays.asList(expensesDTO2, expensesDTO3);
        List<ExpensesDTO> expensesForBudget2 = Collections.singletonList(expensesDTO1);
        List<ExpensesDTO> expenses = Arrays.asList(expensesDTO1, expensesDTO2, expensesDTO3);


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
