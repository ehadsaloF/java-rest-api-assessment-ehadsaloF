package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.*;
import com.cbfacademy.apiassessment.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.cbfacademy.apiassessment.Validators.ValidateArgs.*;

@Service
public class BudgetService implements IBudgetService{

    @Autowired
    BudgetRepository budgetRepository;
    @Autowired
    UserService userService;

    /**
     * Saves a new budget for the specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budget          The budget to be saved
     * @return The saved budget
     * @throws EntityNotFoundException If the user does not exist
     */
    @Override
    public Budget saveBudget(String usernameOrEmail, Budget budget) throws EntityNotFoundException {

        // Check if the user exists
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Budget");
        }

        // Set the user for the budget
        budget.setUser(user.get());

        // Validate the budget parameters
        if (!isAmountValid(budget.getBudgetAmount())) {
            throw new ValidationException("Invalid Amount");
        }

        if (!(isValidCategory(budget.getBudgetCategory().name()))) {
            throw new ValidationException("Invalid Category");
        }

        if (budget.getBudgetSubcategory() != null) {
            if (!(isValidSubCategory(budget.getBudgetSubcategory().name()))) {
                throw new ValidationException("Invalid Subcategory");
            }
        }

        // Save the budget to the repository
        return budgetRepository.save(budget);
    }


    /**
     * Updates a budget by its ID for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The ID of the budget to be updated
     * @param update The field to be updated (amount, category, subcategory, or description).\
     * @param value The new value to set for the specified field
     * @return The updated budget
     * @throws ValidationException If the provided values are invalid
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public Budget updateBudgetByID(String usernameOrEmail, long budgetId, String update, String value)
            throws ValidationException, EntityNotFoundException {
        // Get the user by username or email
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Update Budget");
        }

        // Retrieve the existing budget by user and budget ID
        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user.get(), budgetId);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }


        Budget budget = existingBudget.get();

        // Switch statement to validate and update parameters
        switch (update) {
            case "amount" -> {
                if (!isAmountValid(value)) throw new ValidationException("Invalid Amount, Cannot Update Budget");
                budget.setBudgetAmount(Double.parseDouble(value));
                break;
            }
            case "category" -> {
                if (!isValidCategory(value)) throw new ValidationException("Invalid Category, Cannot Update Budget");
                budget.setBudgetCategory(SubCategories.Category.valueOf(value));
                break;
            }
            case "subcategory" -> {
                if (!(isValidSubCategory(value))) throw new ValidationException("Invalid SubCategory, Cannot Update Budget");
                budget.setBudgetSubcategory(SubCategories.valueOf(value));
                break;
            }
            case "description" -> {
                budget.setDescription(value);
                break;
            }
            default -> {
                throw new ValidationException("Invalid update type");
            }
        }

        budget.setUpdatedAt();

        return budgetRepository.save(budget);
    }


    /**
     * Gets a budget by its ID for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The unique identifier of the budget to retrieve
     * @return  The budget
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public Optional<Budget> getBudgetById(String usernameOrEmail, long budgetId) throws EntityNotFoundException {

        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user.get(), budgetId);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        return existingBudget;
    }


    /**
     * Gets all budgets for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @return List of budgets
     * @throws EntityNotFoundException If the user does not exist
     */
    @Override
    public Optional<List<Budget>> getAllBudgets(String usernameOrEmail) throws EntityNotFoundException {

        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<List<Budget>> existingBudget = budgetRepository.findByUser(user.get());
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        return existingBudget;
    }


    /**
     * Deletes a budget for the specified user based on the budget ID
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The ID of the budget to be deleted
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public void deleteBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException {

        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Delete Budget");
        }

        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user.get(), budgetId);

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        budgetRepository.delete(existingBudget.get());
    }


    /**
     * Gets budgets for a specified user and category
     *
     * @param usernameOrEmail The username or email of the user
     * @param category        The budget category
     * @return List of budgets for the specified user and category
     * @throws EntityNotFoundException If the user or budget with the specified category does not exist
     * @throws ValidationException If the category is not valid
     */
    @Override
    public List<Budget> getBudgetsByCategory(String usernameOrEmail, String category)
            throws EntityNotFoundException, ValidationException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }
        if (!isValidCategory(category)) throw new ValidationException("Invalid Category, Cannot Get Budget");

        Optional<List<Budget>> existingBudget = Optional.ofNullable(budgetRepository.findByUserIdAndBudgetCategory(user.get().getId(), SubCategories.Category.valueOf(category)));

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget with Category"+ category.toString() +" Does Not Exist");
        }
        return existingBudget.get();
    }


    /**
     * Gets a list of budgets within a specified price range for a given user
     *
     * @param usernameOrEmail The username or email of the user
     * @param minPrice        The minimum budget amount in the range
     * @param maxPrice        The maximum budget amount in the range
     * @return A list of budgets within the specified price range
     * @throws EntityNotFoundException If the user does not exist or if no budgets are found within the specified range
     */
    @Override
    public List<Budget> getBudgetsInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException {

        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice) || !isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get budgets within the specified price range
        Optional<List<Budget>> existingBudget = Optional.ofNullable(budgetRepository.findBudgetsInPriceRange(userOptional.get(), minPrice, maxPrice));


        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget within Range " + minPrice + " and " + maxPrice + " Does Not Exist");
        }


        return existingBudget.get();
    }


    /**
     * Gets budgets for a user within a specified date range
     *
     * @param usernameOrEmail The username or email of the user
     * @param startDate        The start date of the date range
     * @param endDate          The end date of the date range
     * @return A list of budgets within the specified date range
     * @throws EntityNotFoundException If the user does not exist or if the budgets within the date range do not exist
     * @throws ValidationException     If the provided date values are invalid
     */
    @Override
    public List<Budget> getBudgetsByDateRange(String usernameOrEmail, String startDate, String endDate) {
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Check if the date values are valid
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            throw new ValidationException("Invalid Date");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Parse the string to get a Date object

        Date parsedStartDate;
        Date parsedEndDate;
        try {
            parsedStartDate = dateFormat.parse(startDate);
            parsedEndDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date");
        }

        // Retrieve budgets within the specified date range
        Optional<List<Budget>> existingBudget = Optional.ofNullable(budgetRepository.findBudgetsByDateRange(userOptional.get(), parsedStartDate, parsedEndDate));

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget within Date range " + startDate + " and " + endDate + " Does Not Exist");
        }

        return existingBudget.get();
    }


    /**
     * Sorts the budgets for a specific user based on the specified criteria
     *
     * @param usernameOrEmail The username or email of the user
     * @param sortBy The sorting criteria ("amount", "category", "subcategory", or "date")
     * @return A list of budgets sorted according to the specified criteria
     * @throws ValidationException if the sorting criteria is invalid
     * @throws EntityNotFoundException if the user or budgets do not exist
     */
    @Override
    public List<Budget> sortBudgetsBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException {

        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<List<Budget>> existingBudget = budgetRepository.findByUser(userOptional.get());
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        List<Budget> budgets = existingBudget.get();

        // Sort budgets based on the specified criteria
        switch (sortBy.toLowerCase()) {
            case "amount" ->
                    budgets.sort(Comparator.comparingDouble(Budget::getBudgetAmount));
            case "category" ->
                    budgets.sort(Comparator.comparing(b -> b.getBudgetCategory().name()));
            case "subcategory" ->
                    budgets.sort(Comparator.comparing(b -> b.getBudgetSubcategory().name()));
            case "date" ->
                    budgets.sort(Comparator.comparing(Budget::getCreatedAt));
            default ->
                    throw new ValidationException("Invalid SortBy value");
        }

        return budgets;
    }


}
