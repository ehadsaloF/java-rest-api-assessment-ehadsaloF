package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.Entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBudgetService {
    Budget saveBudget(String usernameOrEmail, Budget budget)
            throws EntityNotFoundException, ValidationException;
    Budget updateBudgetByID(String usernameOrEmail, long budgetId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    Budget getBudgetById(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException;
    List<Budget> getAllBudgets(String usernameOrEmail)
            throws EntityNotFoundException;
    List<Budget> getBudgetsByCategory(String usernameOrEmail, String category)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsInPriceRange(String usernameOrEmail, double minPrice, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsGreaterThan(String usernameOrEmail, double minPrice)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsByDateRange(String usernameOrEmail, String startDate, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<Budget> getBudgetsAfter(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<Budget> sortBudgetsBy(String usernameOrEmail, String sortBy)
            throws ValidationException, EntityNotFoundException;
    void getAllBudgetAsJSONFile(String usernameOrEmail)
            throws EntityNotFoundException, IOException;
    void deleteBudget(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException;


}
