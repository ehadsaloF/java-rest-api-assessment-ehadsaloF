package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public interface IExpensesService {
    Expenses saveExpenses(String usernameOrEmail, long budgetId, Expenses expenses) throws EntityNotFoundException;
    Expenses saveExpenses(String usernameOrEmail, Expenses expenses) throws EntityNotFoundException;
    Expenses updateExpensesByID(String usernameOrEmail, long expensesId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    Optional<Expenses> getExpensesById(String usernameOrEmail, long expensesId) throws EntityNotFoundException;
    List<Expenses> getExpensesByBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException;
    List<Expenses> getAllExpenses(String usernameOrEmail) throws EntityNotFoundException;
    List<Expenses> getExpensesByCategory(String usernameOrEmail, String category) throws EntityNotFoundException;
    List<Expenses> getExpensesInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException;
    List<Expenses> getExpensesInDateRange(String usernameOrEmail, String startDate, String endDate) throws EntityNotFoundException;
    List<Expenses> sortExpensesBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException;
    void deleteExpense(String usernameOrEmail, long ExpenseId) throws EntityNotFoundException;




}
