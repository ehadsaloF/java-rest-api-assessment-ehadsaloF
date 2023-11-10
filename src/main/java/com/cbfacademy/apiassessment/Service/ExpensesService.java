package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Repository.BudgetRepository;
import com.cbfacademy.apiassessment.Repository.ExpensesRepository;
import com.cbfacademy.apiassessment.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.cbfacademy.apiassessment.Validators.ValidateArgs.*;

public class ExpensesService implements IExpensesService{

    @Autowired
    ExpensesRepository expensesRepository;
    @Autowired
    UserService userService;
    @Autowired
    BudgetService budgetService;

    /**
     * Saves a new expense for the specified user
     *
     * @param usernameOrEmail The username or email of the user
     * @param budgetId          The budget the expense is associated with
     * @param expenses          The expense to be saved
     * @return The saved expense
     * @throws EntityNotFoundException If the user does not exist, If the budget doesn't exist
     */
    @Override
    public Expenses saveExpenses(String usernameOrEmail, long budgetId, Expenses expenses) throws EntityNotFoundException {

        // Check if the user exists
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Expense");
        }

        // Check if Budget exists
        Optional<Budget> existingBudget = budgetService.getBudgetById(usernameOrEmail, budgetId);
        if (existingBudget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist, Cannot Create Expense");
        }

        // Set user and budget for the expense
        expenses.setUser(user.get());
        Budget budget = existingBudget.get();
        expenses.setBudget(budget);

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

        if (!budget.getBudgetCategory().equals(expenses.getExpenseCategory())) {
            throw new ValidationException("Budget Category "+ budget.getBudgetCategory().name() + " and Expense Category " + expenses.getExpenseCategory().name() +" do not match");
        }

        if (!budget.getBudgetSubcategory().equals(expenses.getExpenseSubcategory())) {
            throw new ValidationException("Budget Subcategory "+ budget.getBudgetSubcategory().name() + " and Expense Subcategory " + expenses.getExpenseSubcategory().name() +" do not match");
        }


        // Save the expenses to the repository
        return expensesRepository.save(expenses);
    }

    @Override
    public Expenses saveExpenses(String usernameOrEmail, Expenses expenses) throws EntityNotFoundException {

        // Check if the user exists
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Create Expense");
        }

        // Set user for the expense
        expenses.setUser(user.get());

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

    @Override
    public Expenses updateExpensesByID(String usernameOrEmail, long expensesId, String update, String value) throws ValidationException, EntityNotFoundException {
        // Get the user by username or email
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Update Expense");
        }

        // Retrieve the existing expenses by user and expense ID
        Optional<Expenses> existingExpense = Optional.ofNullable(expensesRepository.findByUserAndID(user.get(), expensesId));
        if ( existingExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense Does Not Exist");
        }


        Expenses expenses = existingExpense.get();

        // Switch statement to validate and update parameters
        switch (update) {
            case "amount" -> {
                if (!isAmountValid(value)) throw new ValidationException("Invalid Amount, Cannot Update Expense");
                expenses.setExpenseAmount(Double.parseDouble(value));
                break;
            }
            case "category" -> {
                if (!isValidCategory(value)) throw new ValidationException("Invalid Category, Cannot Update Expense");
                if(!expenses.getBudget().getBudgetCategory().name().equals(value)){
                    throw new ValidationException("Budget Category "+ expenses.getBudget().getBudgetCategory().name() + " and Expense Category " + value +" do not match");
                }
                expenses.setExpenseCategory(SubCategories.Category.valueOf(value));
                break;
            }
            case "subcategory" -> {
                if (!isValidSubCategory(value)) throw new ValidationException("Invalid Subcategory, Cannot Update Expense");
                if(!expenses.getBudget().getBudgetSubcategory().name().equals(value)){
                    throw new ValidationException("Budget Subcategory "+ expenses.getBudget().getBudgetSubcategory().name() + " and Expense subcategory " + value +" do not match");
                }
                expenses.setExpenseSubcategory(SubCategories.valueOf(value));
                break;
            }
            case "description" -> {
                expenses.setDescription(value);
                break;
            }
            default -> {
                throw new ValidationException("Invalid update type");
            }
        }

        expenses.setUpdatedAt();

        return expensesRepository.save(expenses);
    }

    @Override
    public Optional<Expenses> getExpensesById(String usernameOrEmail, long expensesId) throws EntityNotFoundException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expense");
        }

        Optional<Expenses> existingExpenses = Optional.ofNullable(expensesRepository.findByUserAndID(user.get(), expensesId));
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        return existingExpenses;
    }

    @Override
    public List<Expenses> getExpensesByBudget(String usernameOrEmail, long budgetId) throws EntityNotFoundException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
        }

        Optional<Budget> budget = budgetService.getBudgetById(usernameOrEmail, budgetId);
        if (budget.isEmpty()) {
            throw new EntityNotFoundException("Budget Does Not Exist, Cannot Get Expenses");
        }

        List<Expenses> existingExpenses = expensesRepository.findByUserAndBudget(user.get(), budget.get());
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        return existingExpenses;
    }

    @Override
    public List<Expenses> getAllExpenses(String usernameOrEmail) throws EntityNotFoundException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
        }

        Optional<List<Expenses>> existingExpenses = Optional.ofNullable(expensesRepository.findByUser(user.get()));
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        return existingExpenses.get();
    }

    @Override
    public List<Expenses> getExpensesByCategory(String usernameOrEmail, String category) throws EntityNotFoundException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
        }
        if (!isValidCategory(category)) throw new ValidationException("Invalid Category, Cannot Get Expenses");

        Optional<List<Expenses>> existingExpenses = Optional.ofNullable(expensesRepository.findByUserAndExpenseCategory(user.get(), SubCategories.Category.valueOf(category)));

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expense with Category"+ category +" Does Not Exist");
        }
        return existingExpenses.get();
    }

    @Override
    public List<Expenses> getExpensesInPriceRange(String usernameOrEmail, double minPrice, double maxPrice) throws EntityNotFoundException {
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
        }

        // Validate the price range
        if (!isAmountValid(maxPrice) || !isAmountValid(minPrice)) {
            throw new ValidationException("Invalid Amount");
        }

        // Get expenses within the specified price range
        Optional<List<Expenses>> existingExpenses = Optional.ofNullable(expensesRepository.findExpensesInPriceRange(userOptional.get(), minPrice, maxPrice));


        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses within Range " + minPrice + " and " + maxPrice + " Does Not Exist");
        }


        return existingExpenses.get();
    }

    @Override
    public List<Expenses> getExpensesInDateRange(String usernameOrEmail, String startDate, String endDate) throws EntityNotFoundException {
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
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
        Optional<List<Expenses>> existingExpenses = Optional.ofNullable(expensesRepository.findExpensesByDateRange(userOptional.get(), parsedStartDate, parsedEndDate));

        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses within Date range " + startDate + " and " + endDate + " Does Not Exist");
        }

        return existingExpenses.get();
    }

    @Override
    public List<Expenses> sortExpensesBy(String usernameOrEmail, String sortBy) throws ValidationException, EntityNotFoundException {

        Optional<User> userOptional = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User Does Not Exist, Cannot Get Expenses");
        }

        Optional<List<Expenses>> existingExpenses = Optional.ofNullable(expensesRepository.findByUser(userOptional.get()));
        if (existingExpenses.isEmpty()) {
            throw new EntityNotFoundException("Expenses Does Not Exist");
        }

        List<Expenses> expenses = existingExpenses.get();

        // Sort budgets based on the specified criteria
        switch (sortBy.toLowerCase()) {
            case "amount" ->
                    expenses.sort(Comparator.comparingDouble(Expenses::getExpenseAmount));
            case "category" ->
                    expenses.sort(Comparator.comparing(e -> e.getExpenseCategory().name()));
            case "subcategory" ->
                    expenses.sort(Comparator.comparing(e -> e.getExpenseSubcategory().name()));
            case "date" ->
                    expenses.sort(Comparator.comparing(Expenses::getCreatedAt));
            default ->
                    throw new ValidationException("Invalid SortBy value");
        }

        return expenses;
    }

    @Override
    public void deleteExpense(String usernameOrEmail, long ExpenseId) throws EntityNotFoundException {
        Optional<User> user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User Does Not Exist, Cannot Delete Expense");
        }

        Optional<Expenses> existingExpense = Optional.ofNullable(expensesRepository.findByUserAndID(user.get(), ExpenseId));

        if (existingExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense Does Not Exist");
        }

        expensesRepository.delete(existingExpense.get());

    }
}
