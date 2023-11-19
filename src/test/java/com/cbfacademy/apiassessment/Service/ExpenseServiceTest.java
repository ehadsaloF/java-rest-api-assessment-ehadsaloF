package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.*;
import com.cbfacademy.apiassessment.Entity.*;
import com.cbfacademy.apiassessment.Mappers.*;
import com.cbfacademy.apiassessment.Repository.ExpensesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@DisplayName("The Expenses Service")
public class ExpenseServiceTest {
    @InjectMocks
    private ExpensesService expensesService;

    @Mock
    private BudgetService budgetService;
    @Mock
    private UserService userService;
    @Mock
    private ExpensesRepository expensesRepository;
    @Mock
    private BudgetMapper budgetMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ExpensesMapper expensesMapper;


    private User user;
    private UserDTO userDTO;

    private Budget budget1;
    private Budget budget2;

    private BudgetDTO budgetDTO1;
    private BudgetDTO budgetDTO2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


        user = User.builder().
                username("tester").
                name("Test User").
                email("user@email.com").
                build();
        user.setId(1L);
        userDTO = userMapper.userDTO(user);

        budget1 = new Budget(90, SubCategories.Category.Food, null, "Food");
        budget1.setUser(user);
        budget1.setId(1L);
        budgetDTO1 = budgetMapper.budgetDTO(budget1);

        budget2 = new Budget(400, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        budget2.setUser(user);
        budget2.setId(2L);
        budgetDTO2 = budgetMapper.budgetDTO(budget2);
    }

    @Test
    @DisplayName("can save Expenses with budget")
    void testSaveExpensesWithBudget() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses newExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary" );
        newExpense.setId(1L);

        Expenses savedExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        savedExpense.setUser(user);
        savedExpense.setId(1L);
        savedExpense.setBudget(budget2);

        ExpensesDTO expectedExpensesDTO = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L, 2L);



        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetService.getBudgetById(usernameOrEmail, 2L)).thenReturn(budgetDTO2);
        when(budgetMapper.toBudget(budgetDTO2)).thenReturn(budget2);
        when(expensesRepository.save(newExpense)).thenReturn(savedExpense);

        // Act
        ExpensesDTO result = expensesService.saveExpenses(usernameOrEmail, 2L, newExpense);

        // Assert
        assertNotNull(result);
        assertEquals(expectedExpensesDTO, result);
    }

    @Test
    @DisplayName("can save Expenses without budget")
    void testSaveExpensesWithoutBudget() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses newExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary" );
        newExpense.setId(1L);

        Expenses savedExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        savedExpense.setUser(user);
        savedExpense.setId(1L);

        ExpensesDTO expectedExpensesDTO = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.save(newExpense)).thenReturn(savedExpense);

        // Act
        ExpensesDTO result = expensesService.saveExpenses(usernameOrEmail, newExpense);

        // Assert
        assertNotNull(result);
        assertEquals(expectedExpensesDTO, result);
    }

    @ParameterizedTest
    @MethodSource("DiffScenarios")
    @DisplayName("can update Expenses with different values")
    void testUpdateExpensesByID(String update, String value){
        // Arrange
        String usernameOrEmail = "user@email.com";
        long expenseId = 1L;

        Expenses savedExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        savedExpense.setUser(user);
        savedExpense.setId(1L);

        Expenses updatedExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense.setUser(user);
        updatedExpense.setId(1L);

        ExpensesDTO expectedExpensesDTO = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L);


        switch (update){
            case "amount" -> {
                updatedExpense.setExpenseAmount(Double.parseDouble(value));
                expectedExpensesDTO.setAmount(Double.parseDouble(value));
            }
            case "category" -> {
                updatedExpense.setExpenseCategory(SubCategories.Category.valueOf(value));
                expectedExpensesDTO.setCategory(value);
            }
            case "subcategory" -> {
                if(value != null) {
                    updatedExpense.setExpenseSubcategory(SubCategories.valueOf(value));
                    expectedExpensesDTO.setSubcategory(value);
                } else {
                    updatedExpense.setExpenseSubcategory(null);
                    expectedExpensesDTO.setSubcategory(null);
                }
            }
            case "description" -> {
                updatedExpense.setDescription(value);
                expectedExpensesDTO.setDescription(value);
            }
            case  "budget" -> {
                updatedExpense.setBudget(budget2);
                expectedExpensesDTO.setBudget_id(budget2.getId());
            }
        }

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetService.getBudgetById(usernameOrEmail, 2L)).thenReturn(budgetDTO2);
        when(budgetMapper.toBudget(budgetDTO2)).thenReturn(budget2);
        when(expensesRepository.findByUserAndId(user, expenseId)).thenReturn(java.util.Optional.of(savedExpense));
        when(expensesRepository.save(ArgumentMatchers.any(Expenses.class))).thenReturn(updatedExpense);

        // Act
        ExpensesDTO result = expensesService.updateExpensesByID(usernameOrEmail, expenseId, update, value);

        // Assert
        assertEquals(expectedExpensesDTO, result);
    }

    private static Stream<Object[]> DiffScenarios() {
        return Stream.of(
                new Object[]{"amount", "1500"},
                new Object[]{"category", "Transport"},
                new Object[]{"subcategory", "Investments"},
                new Object[]{"description", "Food Shop for the Week (Snacks)"},
                new Object[]{"budget", "2"}
        );
    }

    @Test
    @DisplayName("can get Expense by ID")
    void testGetExpenseByID() {
        // Arrange
        String usernameOrEmail = "user@email.com";
        long expenseId = 1L;

        Expenses updatedExpense = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense.setUser(user);
        updatedExpense.setId(1L);

        ExpensesDTO expectedExpensesDTO = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findByUserAndId(user, expenseId)).thenReturn(java.util.Optional.of(updatedExpense));

        // Act
        ExpensesDTO result = expensesService.getExpensesById(usernameOrEmail, expenseId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedExpensesDTO, result);
    }

    @Test
    @DisplayName("can get Expense by Budget")
    void testGetExpenseByBudget() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setBudget(budget1);
        updatedExpense2.setId(2L);

        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L, 1L);


        List<ExpensesDTO> ansExpensesDTOList = List.of(expectedExpensesDTO2);

        List<Expenses> ansExpensesList = List.of(updatedExpense2);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetService.getBudgetById(usernameOrEmail, 1L)).thenReturn(budgetDTO1);
        when(budgetMapper.toBudget(budgetDTO1)).thenReturn(budget1);
        when(expensesRepository.findByUserAndBudget(user, budget1)).thenReturn(ansExpensesList);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);


        // Act
        List<ExpensesDTO> result = expensesService.getExpensesByBudget(usernameOrEmail, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(ansExpensesDTOList, result);
    }

    @Test
    @DisplayName("can get all Expenses")
    void testGetAllExpense() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        ExpensesDTO expectedExpensesDTO1 = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L, 2L);
        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L);


        List<Expenses> expensesList = Arrays.asList(updatedExpense1, updatedExpense2);
        List<ExpensesDTO> expensesDTOList = Arrays.asList( expectedExpensesDTO1, expectedExpensesDTO2);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findByUser(user)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense1)).thenReturn(expectedExpensesDTO1);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);


        // Act
        List<ExpensesDTO> result = expensesService.getAllExpenses(usernameOrEmail);

        // Assert
        assertNotNull(result);
        assertEquals(expensesDTOList, result);
    }

    @Test
    @DisplayName("can get Expenses by Category")
    void testGetExpensesByCategory() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L);


        List<Expenses> expensesList = List.of(updatedExpense2);
        List<ExpensesDTO> expensesDTOList = List.of(expectedExpensesDTO2);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findByUserAndExpenseCategory(user, SubCategories.Category.Food)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);


        // Act
        List<ExpensesDTO> result = expensesService.getExpensesByCategory(usernameOrEmail, "Food");

        // Assert
        assertNotNull(result);
        assertEquals(expensesDTOList, result);
    }

    @Test
    @DisplayName("can get Expenses in Price Range")
    void testGetExpensesByPriceRange() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        Expenses updatedExpense3 = new Expenses(1.49, SubCategories.Category.Food, null,  "Random Snacks");
        updatedExpense3.setUser(user);
        updatedExpense3.setId(3L);

        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L);
        ExpensesDTO expectedExpensesDTO3 = new ExpensesDTO(3L, 1.49, "Food", null, "Random Snacks", 1L);



        List<Expenses> expensesList = Arrays.asList(updatedExpense2, updatedExpense3);
        List<ExpensesDTO> expensesDTOList = Arrays.asList( expectedExpensesDTO2, expectedExpensesDTO3);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findExpensesInPriceRange(user, 1, 50)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);
        when(expensesMapper.expensesDTO(updatedExpense3)).thenReturn(expectedExpensesDTO3);


        // Act
        List<ExpensesDTO> result = expensesService.getExpensesInPriceRange(usernameOrEmail, 1, 50);

        // Assert
        assertNotNull(result);
        assertEquals(expensesDTOList, result);
    }

    @Test
    @DisplayName("can get Expenses greater than an amount")
    void testGetExpensesGreaterThan() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        Expenses updatedExpense3 = new Expenses(1.49, SubCategories.Category.Food, null,  "Random Snacks");
        updatedExpense3.setUser(user);
        updatedExpense3.setId(3L);

        ExpensesDTO expectedExpensesDTO1 = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L, 2L);


        List<Expenses> expensesList = List.of(updatedExpense1);
        List<ExpensesDTO> expensesDTOList = List.of(expectedExpensesDTO1);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findExpensesGreaterThan(user, 20)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense1)).thenReturn(expectedExpensesDTO1);


        // Act
        List<ExpensesDTO> result = expensesService.getExpensesGreaterThan(usernameOrEmail, 20);

        // Assert
        assertNotNull(result);
        assertEquals(expensesDTOList, result);
    }

    @Test
    @DisplayName("can get Expenses less than an amount")
    void testGetExpensesLessThan() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        Expenses updatedExpense3 = new Expenses(1.49, SubCategories.Category.Food, null,  "Random Snacks");
        updatedExpense3.setUser(user);
        updatedExpense3.setId(3L);

        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L);
        ExpensesDTO expectedExpensesDTO3 = new ExpensesDTO(3L, 1.49, "Food", null, "Random Snacks", 1L);



        List<Expenses> expensesList = Arrays.asList(updatedExpense2, updatedExpense3 );
        List<ExpensesDTO> expensesDTOList = Arrays.asList( expectedExpensesDTO2, expectedExpensesDTO3);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findExpensesLessThan(user, 20)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);
        when(expensesMapper.expensesDTO(updatedExpense3)).thenReturn(expectedExpensesDTO3);

        // Act
        List<ExpensesDTO> result = expensesService.getExpensesLessThan(usernameOrEmail, 20);


        // Assert
        assertNotNull(result);
        assertEquals(expensesDTOList, result);
    }

    @ParameterizedTest
    @MethodSource("DiffSortScenarios")
    @DisplayName("can sort Expenses by different values")
    void testSortExpensesBy(String sortBy){
        // Arrange
        String usernameOrEmail = "user@email.com";

        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);

        Expenses updatedExpense2 = new Expenses(10, SubCategories.Category.Food, null,  "KFC");
        updatedExpense2.setUser(user);
        updatedExpense2.setId(2L);

        Expenses updatedExpense3 = new Expenses(1.49, SubCategories.Category.Food, null,  "Random Snacks");
        updatedExpense3.setUser(user);
        updatedExpense3.setId(3L);

        ExpensesDTO expectedExpensesDTO1 = new ExpensesDTO(1L, 100, "Savings", "Basic", "From 1st week Salary", 1L, 2L);
        ExpensesDTO expectedExpensesDTO2 = new ExpensesDTO(2L, 10, "Food", null, "KFC", 1L);
        ExpensesDTO expectedExpensesDTO3 = new ExpensesDTO(3L, 1.49, "Food", null, "Random Snacks", 1L);



        List<Expenses> expensesList = Arrays.asList(updatedExpense1, updatedExpense2, updatedExpense3 );
        List<ExpensesDTO> expensesDTOList = Arrays.asList(expectedExpensesDTO1, expectedExpensesDTO2, expectedExpensesDTO3);


        switch (sortBy){
            case "amount" -> expensesDTOList = Arrays.asList(expectedExpensesDTO3, expectedExpensesDTO2, expectedExpensesDTO1);
            case "category", "subcategory" -> expensesDTOList = Arrays.asList(expectedExpensesDTO2, expectedExpensesDTO3, expectedExpensesDTO1);
        }

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(expensesRepository.findByUser(user)).thenReturn(expensesList);
        when(expensesMapper.expensesDTO(updatedExpense1)).thenReturn(expectedExpensesDTO1);
        when(expensesMapper.expensesDTO(updatedExpense2)).thenReturn(expectedExpensesDTO2);
        when(expensesMapper.expensesDTO(updatedExpense3)).thenReturn(expectedExpensesDTO3);


        // Act
        List<ExpensesDTO> result = expensesService.sortExpensesBy(usernameOrEmail, sortBy);

        // Assert
        assertEquals(expensesDTOList, result);
    }

    private static Stream<String> DiffSortScenarios() {
        return Stream.of(
                "amount", "category", "subcategory"
        );
    }

    @Test
    @DisplayName("can delete expense")
    void testDeleteExpense(){
        // Arrange
        Expenses updatedExpense1 = new Expenses(100, SubCategories.Category.Savings, SubCategories.Basic, "From 1st week Salary");
        updatedExpense1.setUser(user);
        updatedExpense1.setBudget(budget2);
        updatedExpense1.setId(1L);


        when(expensesRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(updatedExpense1));
        when(userService.getUserByUsernameOrEmail(user.getEmail())).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        doNothing().when(expensesRepository).delete(updatedExpense1);

        assertAll(() -> expensesService.deleteExpense(user.getEmail(), 1L));
    }
}
