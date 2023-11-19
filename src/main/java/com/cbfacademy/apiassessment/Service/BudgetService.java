package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.*;
import com.cbfacademy.apiassessment.Entity.*;
import com.cbfacademy.apiassessment.Mappers.*;
import com.cbfacademy.apiassessment.Repository.*;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cbfacademy.apiassessment.Validators.ValidateArgs.*;

@Service
public class BudgetService implements IBudgetService{

    @Autowired
    BudgetRepository budgetRepository;
    @Autowired
    UserService userService;

    BudgetMapper budgetMapper;

    UserMapper userMapper;



    /**
     * Saves a new budget for the specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budget          The budget to be saved
     * @return The saved budget
     * @throws EntityNotFoundException If the user does not exist
     * @throws ValidationException If the Budget parameters are not valid
     */
    @Override
    public BudgetDTO saveBudget(String usernameOrEmail, Budget budget) throws EntityNotFoundException, ValidationException {

        // Check if the user exists
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Budget");
        }

        // Set the user for the budget
        budget.setUser(user);

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
        return budgetMapper.INSTANCE.budgetDTO(budgetRepository.save(budget));
    }


    /**
     * Updates a budget by its ID for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The ID of the budget to be updated
     * @param update The field to be updated (amount, category, subcategory, or description)
     * @param value The new value to set for the specified field
     * @return The updated budget
     * @throws ValidationException If the provided values are invalid
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public BudgetDTO updateBudgetByID(String usernameOrEmail, long budgetId, String update, String value)
            throws ValidationException, EntityNotFoundException {
        // Get the user by username or email
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Update Budget");
        }

        // Retrieve the existing budget by user and budget ID
        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user, budgetId);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }


        Budget budget = existingBudget.get();

        // Switch statement to validate and update parameters
        switch (update) {
            case "amount" -> {
                if (!isAmountValid(value)) throw new ValidationException("Invalid Amount, Cannot Update Budget");
                budget.setBudgetAmount(Double.parseDouble(value));
            }
            case "category" -> {
                if (!isValidCategory(value)) throw new ValidationException("Invalid Category, Cannot Update Budget");
                budget.setBudgetCategory(SubCategories.Category.valueOf(value));
            }
            case "subcategory" -> {
                if (!(isValidSubCategory(value))) throw new ValidationException("Invalid SubCategory, Cannot Update Budget");
                budget.setBudgetSubcategory(SubCategories.valueOf(value));
            }
            case "description" -> budget.setDescription(value);
            default -> throw new ValidationException("Invalid update type");
        }

        budget.setUpdatedAt();

        return budgetMapper.INSTANCE.budgetDTO(budgetRepository.save(budget));
    }


    /**
     * Gets a budget by its ID for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The ID of the budget
     * @return  The budget
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public BudgetDTO getBudgetById(String usernameOrEmail, long budgetId) throws EntityNotFoundException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user, budgetId);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        return budgetMapper.INSTANCE.budgetDTO(existingBudget.get());
    }


    /**
     * Gets all budgets for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @return List of budgets
     * @throws EntityNotFoundException If the user does not exist
     */
    @Override
    public List<BudgetDTO> getAllBudgets(String usernameOrEmail) throws EntityNotFoundException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<List<Budget>> existingBudget = budgetRepository.findByUser(user);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("User has not created any budgets");
        }

        return existingBudget.get().stream()
                .map(budgetMapper::budgetDTO)
                .collect(Collectors.toList());
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
    public List<BudgetDTO> getBudgetsByCategory(String usernameOrEmail, String category)
            throws EntityNotFoundException, ValidationException {
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if(user == null){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }
        if (!isValidCategory(category)) throw new ValidationException("Invalid Category, Cannot Get Budget");

        List<Budget> existingBudget = budgetRepository.findByUserIdAndBudgetCategory(user.getId(), SubCategories.Category.valueOf(category));

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget with Category"+ category +" Does Not Exist");
        }
        return existingBudget.stream()
                .map(budgetMapper::budgetDTO)
                .collect(Collectors.toList());
    }


    /**
     * Gets a list of budgets within a specified price range for a given user
     *
     * @param usernameOrEmail The username or email of the user
     * @param minPrice        The minimum budget amount in the range
     * @param maxPrice        The maximum budget amount in the range
     * @return A list of budgets within the specified price range
     * @throws EntityNotFoundException If the user does not exist or if no budgets are found within the specified range
     * @throws ValidationException If the minPrice or maxPrice is not valid
     */
    @Override
    public List<BudgetDTO> getBudgetsInPriceRange(String usernameOrEmail, double minPrice, double maxPrice)
            throws EntityNotFoundException, ValidationException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice) || !isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get budgets within the specified price range
        Optional<List<Budget>> existingBudget = Optional.ofNullable(budgetRepository.findBudgetsInPriceRange(user, minPrice, maxPrice));


        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget within Range " + minPrice + " and " + maxPrice + " Does Not Exist");
        }


        return existingBudget.get().stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
    }
    @Override
    public List<BudgetDTO> getBudgetsGreaterThan(String usernameOrEmail, double minPrice)
            throws EntityNotFoundException, ValidationException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Validate the price
        if ( !isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }


        List<Budget> existingBudget = budgetRepository.findBudgetsGreaterThan(user, minPrice);


        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budgets Greater than " + minPrice + " Does Not Exist");
        }


        return existingBudget.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<BudgetDTO> getBudgetsLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get budgets within the specified price range
        List<Budget> existingBudget = budgetRepository.findBudgetsLessThan(user, maxPrice);


        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budgets less than " + maxPrice + " Does Not Exist");
        }


        return existingBudget.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
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
    public List<BudgetDTO> getBudgetsByDateRange(String usernameOrEmail, String startDate, String endDate)
            throws EntityNotFoundException, ValidationException{
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
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
        List<Budget> existingBudget = budgetRepository.findBudgetsByDateRange(user, parsedStartDate, parsedEndDate);

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget within Date range " + startDate + " and " + endDate + " Does Not Exist");
        }

        return existingBudget.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<BudgetDTO> getBudgetsBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException{
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Check if the date values are valid
        if (!isValidDate(endDate)) {
            throw new ValidationException("Invalid Date");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Parse the string to get a Date object
        Date parsedEndDate;
        try {
            parsedEndDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date");
        }

        // Retrieve budgets within the specified date range
        List<Budget> existingBudget = budgetRepository.findBudgetsBefore(user, parsedEndDate);

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budgets created before " + endDate + " Does Not Exist");
        }

        return existingBudget.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<BudgetDTO> getBudgetsAfter(String usernameOrEmail, String startDate)
            throws EntityNotFoundException, ValidationException{
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        // Check if the date values are valid
        if (!isValidDate(startDate)) {
            throw new ValidationException("Invalid Date");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Parse the string to get a Date object
        Date parsedStartDate;
        try {
            parsedStartDate = dateFormat.parse(startDate);
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date");
        }

        // Retrieve budgets within the specified date range
        List<Budget> existingBudget = budgetRepository.findBudgetsAfter(user, parsedStartDate);

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budgets created after " + startDate + " Does Not Exist");
        }

        return existingBudget.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
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
    public List<BudgetDTO> sortBudgetsBy(String usernameOrEmail, String sortBy)
            throws ValidationException, EntityNotFoundException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<List<Budget>> existingBudget = budgetRepository.findByUser(user);
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
                    budgets.sort(Comparator.comparing(b ->
                            b.getBudgetSubcategory() != null ? b.getBudgetSubcategory().name() : null, Comparator.nullsFirst(String::compareTo)));

            case "date" ->
                    budgets.sort(Comparator.comparing(Budget::getCreatedAt));
            default ->
                    throw new ValidationException("Invalid SortBy value");
        }

        return budgets.stream().
                map(budgetMapper::budgetDTO).
                collect(Collectors.toList());
    }


    /**
     * Retrieves all budgets for a user and writes them to a JSON file.
     *
     * @throws IOException If an error occurs while generating the file.
     * @throws EntityNotFoundException If the user or budget do not exist.
     */
    @Override
    public void getAllBudgetAsJSONFile(String usernameOrEmail) throws EntityNotFoundException, IOException {
        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Budget");
        }

        Optional<List<Budget>> existingBudget = budgetRepository.findByUser(user);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("User has not created any Budget");
        }

        List<BudgetDTO> budget = existingBudget.get().stream().map(budgetMapper::budgetDTO).collect(Collectors.toList());

        String outputFile = "src/main/resources/AllBudget.JSON";
        Gson gson = new Gson();

        // Delete the existing file if it exists
        File file = new File(outputFile);
        if (file.exists()){

            file.delete();
        }

        try (FileWriter writer = new FileWriter(outputFile)) {
            // Create a new file
            gson.toJson(budget, writer);
        } catch (IOException e) {
            throw new IOException("Error generating File");
        }

    }



    /**
     * Deletes a budget for the specified user based on the budget ID
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId The ID of the budget to be deleted
     * @throws EntityNotFoundException If the user or budget does not exist
     */
    @Override
    public void deleteBudget(String usernameOrEmail, long budgetId)
            throws EntityNotFoundException {

        User user = userMapper.toUser(userService.getUserByUsernameOrEmail(usernameOrEmail));
        if(user == null){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Delete Budget");
        }

        Optional<Budget> existingBudget = budgetRepository.findByUserAndId(user, budgetId);

        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist");
        }

        budgetRepository.delete(existingBudget.get());
    }



}
