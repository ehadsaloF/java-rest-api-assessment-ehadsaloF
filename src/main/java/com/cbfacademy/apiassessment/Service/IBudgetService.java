package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public interface IBudgetService {
    Budget saveBudget(String usernameOrEmail, Budget budget) throws EntityNotFoundException;
    Budget updateBudgetByID(String usernameOrEmail, long budgetId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    Optional<Budget> getBudgetById(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException;
    Optional<List<Budget>> getAllBudgets(String usernameOrEmail)
            throws EntityNotFoundException;
    void deleteBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException;
    List<Budget> getBudgetsByCategory(String usernameOrEmail, String category) throws EntityNotFoundException;
    List<Budget> getBudgetsInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException;
    List<Budget> getBudgetsByDateRange(String usernameOrEmail, String startDate, String endDate) throws EntityNotFoundException;
    List<Budget> sortBudgetsBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException;


}
