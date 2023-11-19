package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.Expenses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IExpensesService {
    Expenses saveExpenses(String usernameOrEmail, long budgetId, Expenses expenses) throws EntityNotFoundException, ValidationException;
    Expenses saveExpenses(String usernameOrEmail, Expenses expenses) throws EntityNotFoundException;
    Expenses updateExpensesByID(String usernameOrEmail, long expensesId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    Expenses getExpensesById(String usernameOrEmail, long expensesId) throws EntityNotFoundException;
    List<Expenses> getExpensesByBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException;
    List<Expenses> getAllExpenses(String usernameOrEmail) throws EntityNotFoundException;
    List<Expenses> getExpensesByCategory(String usernameOrEmail, String category) throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesGreaterThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesInDateRange(String usernameOrEmail, String startDate, String endDate) throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<Expenses> getExpensesAfter(String usernameOrEmail, String startDate)
            throws EntityNotFoundException, ValidationException;
    List<Expenses> sortExpensesBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException;
    void getAllExpensesAsJSONFile(String usernameOrEmail) throws IOException;
    void deleteExpense(String usernameOrEmail, long ExpenseId) throws EntityNotFoundException;




}
