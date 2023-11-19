package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IExpensesService {
    ExpensesDTO saveExpenses(String usernameOrEmail, long budgetId, Expenses expenses) throws EntityNotFoundException, ValidationException;
    ExpensesDTO saveExpenses(String usernameOrEmail, Expenses expenses) throws EntityNotFoundException;
    ExpensesDTO updateExpensesByID(String usernameOrEmail, long expensesId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    ExpensesDTO getExpensesById(String usernameOrEmail, long expensesId) throws EntityNotFoundException;
    List<ExpensesDTO> getExpensesByBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException;
    List<ExpensesDTO> getAllExpenses(String usernameOrEmail) throws EntityNotFoundException;
    List<ExpensesDTO> getExpensesByCategory(String usernameOrEmail, String category) throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesGreaterThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesInDateRange(String usernameOrEmail, String startDate, String endDate) throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> getExpensesAfter(String usernameOrEmail, String startDate)
            throws EntityNotFoundException, ValidationException;
    List<ExpensesDTO> sortExpensesBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException;
    void getAllExpensesAsJSONFile(String usernameOrEmail) throws IOException;
    void deleteExpense(String usernameOrEmail, long ExpenseId) throws EntityNotFoundException;




}
