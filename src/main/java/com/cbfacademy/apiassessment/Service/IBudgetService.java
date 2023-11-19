package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.Entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBudgetService {
    BudgetDTO saveBudget(String usernameOrEmail, Budget budget)
            throws EntityNotFoundException, ValidationException;
    BudgetDTO updateBudgetByID(String usernameOrEmail, long budgetId, String update, String value)
            throws ValidationException, EntityNotFoundException;
    BudgetDTO getBudgetById(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException;
    List<BudgetDTO> getAllBudgets(String usernameOrEmail)
            throws EntityNotFoundException;
    List<BudgetDTO> getBudgetsByCategory(String usernameOrEmail, String category)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsInPriceRange(String usernameOrEmail, double minPrice, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsGreaterThan(String usernameOrEmail, double minPrice)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsByDateRange(String usernameOrEmail, String startDate, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> getBudgetsAfter(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException;
    List<BudgetDTO> sortBudgetsBy(String usernameOrEmail, String sortBy)
            throws ValidationException, EntityNotFoundException;
    void getAllBudgetAsJSONFile(String usernameOrEmail)
            throws EntityNotFoundException, IOException;
    void deleteBudget(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException;


}
