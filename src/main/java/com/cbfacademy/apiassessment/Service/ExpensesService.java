package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.Entity.*;
import com.cbfacademy.apiassessment.Mappers.BudgetMapper;
import com.cbfacademy.apiassessment.Mappers.ExpensesMapper;
import com.cbfacademy.apiassessment.Mappers.UserMapper;
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
public class ExpensesService implements IExpensesService{

    @Autowired
    ExpensesRepository expensesRepository;
    @Autowired
    UserService userService;
    @Autowired
    BudgetService budgetService;
    BudgetMapper budgetMapper;
    ExpensesMapper expensesMapper;
    UserMapper userMapper;





    /**
     * Saves a new expense for the specified user
     * The Expense is connected to a budget
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId          The budget the expense is associated with
     * @param expenses          The expense to be saved
     * @return The saved expense
     * @throws EntityNotFoundException If the user does not exist, If the budget doesn't exist
     * @throws ValidationException If the Expense parameters are not valid
     */
    @Override
    public Expenses saveExpenses(String usernameOrEmail, long budgetId, Expenses expenses)
            throws EntityNotFoundException, ValidationException {

        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Expense");
        }

        Budget existingBudget = budgetService.getBudgetById(usernameOrEmail, budgetId);
        if (existingBudget == null) {
            throw new EntityNotFoundException("Budget Does Not Exist, Cannot create Expense");
        }

        // Set user and budget for the expense
        expenses.setUser(user);
        expenses.setBudget(existingBudget);

        // Validate the expense parameters
        if (!isAmountValid(expenses.getExpenseAmount())) {
            throw new ValidationException("Invalid Amount");
        }

        if (!(isValidCategory(expenses.getExpenseCategory().name()))) {
            throw new ValidationException("Invalid Category");
        }

        if (expenses.getExpenseSubcategory() != null) {
            if (!(isValidSubCategory(expenses.getExpenseSubcategory().name()))) {
                throw new ValidationException("Invalid Subcategory");
            }
        }

        if (!existingBudget.getBudgetCategory().equals(expenses.getExpenseCategory())) {
            throw new ValidationException("Budget Category "+ existingBudget.getBudgetCategory().name() + " and Expense Category " + expenses.getExpenseCategory().name() +" do not match");
        }

        if (!existingBudget.getBudgetSubcategory().equals(expenses.getExpenseSubcategory())) {
            throw new ValidationException("Budget Subcategory "+ existingBudget.getBudgetSubcategory().name() + " and Expense Subcategory " + expenses.getExpenseSubcategory() +" do not match");
        }


        // Save the expenses to the repository
        return expensesRepository.save(expenses);
    }


    /**
     * Saves a new expense for the specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param expenses          The expense to be saved
     * @return The saved expense
     * @throws EntityNotFoundException If the user does not exist
     * @throws ValidationException If the Expense parameters are not valid
     */
    @Override
    public Expenses saveExpenses(String usernameOrEmail, Expenses expenses)
            throws EntityNotFoundException {

        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Expense");
        }

        // Set user for the expense
        expenses.setUser(user);

        // Set budget for the expense
        expenses.setBudget(null);

        // Validate the expense parameters
        if (!isAmountValid(expenses.getExpenseAmount())) {
            throw new ValidationException("Invalid Amount");
        }

        if (!(isValidCategory(expenses.getExpenseCategory().name()))) {
            throw new ValidationException("Invalid Category");
        }

        if (expenses.getExpenseSubcategory() != null) {
            if (!(isValidSubCategory(expenses.getExpenseSubcategory().name()))) {
                throw new ValidationException("Invalid Subcategory");
            }
        }

        // Save the expenses to the repository
        return expensesRepository.save(expenses);

    }


    /**
     * Updates an expense by its ID for the specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param expensesId The ID of the expense to be updated
     * @param update The field to be updated (amount, category, subcategory, or description)
     * @param value The new value to set for the specified field
     * @return The updated expense
     * @throws EntityNotFoundException If the user or expense does not exist
     * @throws ValidationException If the update or value is not valid
     */
    @Override
    public Expenses updateExpensesByID(String usernameOrEmail, long expensesId, String update, String value)
            throws ValidationException, EntityNotFoundException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Update Expense");
        }

        // Retrieve the existing expenses by user and expense ID
        Optional<Expenses> existingExpense = expensesRepository.findByUserAndId(user, expensesId);
        if ( existingExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense Does Not Exist");
        }


        Expenses expenses = existingExpense.get();

        // Switch statement to validate and update parameters
        switch (update) {
            case "amount" -> {
                if (!isAmountValid(value)) throw new ValidationException("Invalid Amount, Cannot Update Expense");
                expenses.setExpenseAmount(Double.parseDouble(value));
            }
            case "budget" ->{
                // Check if Budget exists
                Budget existingBudget = budgetService.getBudgetById(usernameOrEmail, Long.parseLong(value));
                if (existingBudget == null) {
                    throw new EntityNotFoundException("Budget Does Not Exist, Cannot update Expense");
                }

                if (!existingBudget.getBudgetCategory().equals(expenses.getExpenseCategory())) {
                    throw new ValidationException("Budget Category "+ existingBudget.getBudgetCategory().name() + " and Expense Category " + expenses.getExpenseCategory().name() +" do not match");
                }


                if(existingBudget.getBudgetSubcategory() != null & expenses.getExpenseSubcategory() != null) {
                    if (!existingBudget.getBudgetSubcategory().equals(expenses.getExpenseSubcategory())) {
                        throw new ValidationException("Budget Subcategory " + existingBudget.getBudgetSubcategory().name() + " and Expense Subcategory " + expenses.getExpenseSubcategory().name() + " do not match");
                    }
                }
                expenses.setBudget(existingBudget);

            }
            case "category" -> {
                if (!isValidCategory(value)) throw new ValidationException("Invalid Category, Cannot Update Expense");

                if(expenses.getBudget() != null){
                    if(!expenses.getBudget().getBudgetCategory().name().equals(value)){
                        throw new ValidationException("Budget Category "+ expenses.getBudget().getBudgetCategory().name() + " and Expense Category " + value +" do not match");
                    }
                }
                expenses.setExpenseCategory(SubCategories.Category.valueOf(value));
            }
            case "subcategory" -> {
                if (!isValidSubCategory(value)) throw new ValidationException("Invalid Subcategory, Cannot Update Expense");

                if(expenses.getBudget() != null) {
                    if (!expenses.getBudget().getBudgetSubcategory().name().equals(value)) {
                        throw new ValidationException("Budget Subcategory " + expenses.getBudget().getBudgetSubcategory().name() + " and Expense subcategory " + value + " do not match");
                    }
                }
                expenses.setExpenseSubcategory(SubCategories.valueOf(value));
            }
            case "description" -> expenses.setDescription(value);
            default -> throw new ValidationException("Invalid update type");
        }

        expenses.setUpdatedAt();

        return expensesRepository.save(expenses);
    }


    /**
     * Gets an expense by its ID for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param expensesId The ID of the expense
     * @return  The expense
     * @throws EntityNotFoundException If the user or expenses does not exist
     */
    @Override
    public Expenses getExpensesById(String usernameOrEmail, long expensesId) throws EntityNotFoundException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        Optional<Expenses> existingExpenses = expensesRepository.findByUserAndId(user, expensesId);
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        return existingExpenses.get();
    }


    /**
     * Gets all expenses associated with a budget for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId budget ID of associated budget
     * @return List of Expenses
     * @throws EntityNotFoundException If the user, Budget or expenses does not exist
     */
    @Override
    public List<Expenses> getExpensesByBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        Budget budget = budgetService.getBudgetById(usernameOrEmail, budgetId);
        if (budget == null) {
            throw new EntityNotFoundException("Budget Does Not Exist, Cannot Get Expenses");
        }

        List<Expenses> existingExpenses = expensesRepository.findByUserAndBudget(user, budget);
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("No Expenses Associated With This Budget");
        }

        return existingExpenses;
    }


    /**
     * Gets all expenses for a specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @return List of expenses
     * @throws EntityNotFoundException If the user or expenses does not exist
     */
    @Override
    public List<Expenses> getAllExpenses(String usernameOrEmail) throws EntityNotFoundException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        List<Expenses> existingExpenses = expensesRepository.findByUser(user);
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        return existingExpenses;
    }


    /**
     * Gets expenses for a specified user and category
     *
     * @param usernameOrEmail The username or email of the user
     * @param category        The expense category
     * @return List of expenses for the specified user and category
     * @throws EntityNotFoundException If the user or expense with the specified category does not exist
     * @throws ValidationException If the category is not valid
     */
    @Override
    public List<Expenses> getExpensesByCategory(String usernameOrEmail, String category)
            throws EntityNotFoundException, ValidationException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }
        if (!isValidCategory(category)) throw new ValidationException("Invalid Category, Cannot Get Expenses");

       List<Expenses> existingExpenses = expensesRepository.findByUserAndExpenseCategory(user, SubCategories.Category.valueOf(category));

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expense with Category"+ category +" Does Not Exist");
        }
        return existingExpenses;
    }


    /**
     * Gets a list of expenses within a specified price range for a given user
     *
     * @param usernameOrEmail The username or email of the user
     * @param minPrice        The minimum expense amount in the range
     * @param maxPrice        The maximum expense amount in the range
     * @return A list of expense within the specified price range
     * @throws EntityNotFoundException If the user does not exist or if no expenses are found within the specified range
     * @throws ValidationException If the minPrice or maxPrice is not valid
     */
    @Override
    public List<Expenses> getExpensesInPriceRange(String usernameOrEmail, double minPrice, double maxPrice)
            throws EntityNotFoundException, ValidationException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice) || !isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get expenses within the specified price range
        List<Expenses> existingExpenses = expensesRepository.findExpensesInPriceRange(user, minPrice, maxPrice);


        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses within Range " + minPrice + " and " + maxPrice + " Does Not Exist");
        }


        return existingExpenses;
    }

    @Override
    public List<Expenses> getExpensesGreaterThan(String usernameOrEmail, double minPrice)
            throws EntityNotFoundException, ValidationException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        // Validate the price range
        if (!isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get expenses within the specified price range
        List<Expenses> existingExpenses = expensesRepository.findExpensesGreaterThan(user, minPrice);


        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses greater than " + minPrice + " Does Not Exist");
        }


        return existingExpenses;
    }

    @Override
    public List<Expenses> getExpensesLessThan(String usernameOrEmail, double maxPrice)
            throws EntityNotFoundException, ValidationException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get expenses within the specified price range
        List<Expenses> existingExpenses = expensesRepository.findExpensesLessThan(user, maxPrice);


        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses greater that " + maxPrice + " Does Not Exist");
        }


        return existingExpenses;
    }

    /**
     * Gets expenses for a user within a specified date range
     *
     * @param usernameOrEmail The username or email of the user
     * @param startDate        The start date of the date range
     * @param endDate          The end date of the date range
     * @return A list of expenses within the specified date range
     * @throws EntityNotFoundException If the user does not exist or if the expenses within the date range do not exist
     * @throws ValidationException     If the provided date values are invalid
     */
    @Override
    public List<Expenses> getExpensesInDateRange(String usernameOrEmail, String startDate, String endDate)
            throws EntityNotFoundException, ValidationException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        // Check if the date values are valid
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            throw new ValidationException("Invalid Date");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date parsedStartDate;
        Date parsedEndDate;
        try {
            parsedStartDate = dateFormat.parse(startDate);
            parsedEndDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
            throw new ValidationException("Invalid Date");
        }

        // Retrieve budgets within the specified date range
        List<Expenses>existingExpenses = expensesRepository.findExpensesByDateRange(user, parsedStartDate, parsedEndDate);

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses within Date range " + startDate + " and " + endDate + " Does Not Exist");
        }

        return existingExpenses;
    }


    @Override
    public List<Expenses> getExpensesBefore(String usernameOrEmail, String endDate)
            throws EntityNotFoundException, ValidationException{
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
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
        List<Expenses> existingExpenses = expensesRepository.findExpensesBefore(user, parsedEndDate);

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses created before " + endDate + " Does Not Exist");
        }

        return existingExpenses;
    }

    @Override
    public List<Expenses> getExpensesAfter(String usernameOrEmail, String startDate)
            throws EntityNotFoundException, ValidationException{
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
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
        List<Expenses> existingExpenses = expensesRepository.findExpensesAfter(user, parsedStartDate);

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses created after " + startDate + " Does Not Exist");
        }

        return existingExpenses;
    }


    /**
     * Sorts the Expenses for a specific user based on the specified criteria
     *
     * @param usernameOrEmail The username or email of the user
     * @param sortBy The sorting criteria ("amount", "category", "subcategory", or "date")
     * @return A list of expenses sorted according to the specified criteria
     * @throws ValidationException if the sorting criteria is invalid
     * @throws EntityNotFoundException if the user or expenses do not exist
     */
    @Override
    public List<Expenses> sortExpensesBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException {

        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        List<Expenses> expenses = expensesRepository.findByUser(user);
        if (expenses.isEmpty()) {
            throw new EntityNotFoundException("User has not created any Expenses");
        }



        // Sort budgets based on the specified criteria
        switch (sortBy.toLowerCase()) {
            case "amount" ->
                    expenses.sort(Comparator.comparingDouble(Expenses::getExpenseAmount));
            case "category" ->
                    expenses.sort(Comparator.comparing(e -> e.getExpenseCategory().name()));
            case "subcategory" ->
                    expenses.sort(Comparator.comparing(e ->
                            e.getExpenseSubcategory() != null ? e.getExpenseSubcategory().name() : null, Comparator.nullsFirst(String::compareTo)));

            case "date" ->
                    expenses.sort(Comparator.comparing(Expenses::getCreatedAt));
            default ->
                    throw new ValidationException("Invalid SortBy value");
        }

        return expenses;
    }


    /**
     * Retrieves all expenses and writes them to a JSON file.
     *
     * @throws IOException If an error occurs while generating the file.
     * @throws EntityNotFoundException If the user or expenses do not exist.
     */
    @Override
    public void getAllExpensesAsJSONFile(String usernameOrEmail) throws EntityNotFoundException, IOException {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot get Expense");
        }

        List<Expenses> existingExpenses = expensesRepository.findByUser(user);
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("User has not created any Expenses");
        }

        List<ExpensesDTO> expenses = existingExpenses.stream().map(expensesMapper.INSTANCE::expensesDTO).collect(Collectors.toList());

        String outputFile = "src/main/resources/AllExpenses.JSON";
        Gson gson = new Gson();

        // Delete the existing file if it exists
        File file = new File(outputFile);
        if (file.exists()){

            file.delete();
        }

        try (FileWriter writer = new FileWriter(outputFile)) {
            // Create a new file
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            throw new IOException("Error generating File");
        }

    }


    /**
     * Deletes an Expense for the specified user based on the expense ID
     *
     * @param usernameOrEmail The username or email of the user
     * @param ExpenseId The ID of the expense to be deleted
     * @throws EntityNotFoundException If the user or expense does not exist
     */
    @Override
    public void deleteExpense(String usernameOrEmail, long ExpenseId) throws EntityNotFoundException  {
        // Check if the user exists
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user == null) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot delete Expense");
        }

        Optional<Expenses> existingExpense = expensesRepository.findByUserAndId(user, ExpenseId);

        if (existingExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense Does Not Exist");
        }

        expensesRepository.delete(existingExpense.get());

    }


}
