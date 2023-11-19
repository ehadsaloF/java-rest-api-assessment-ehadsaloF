package com.cbfacademy.apiassessment.Service;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.DTO.UserDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Entity.User;
import com.cbfacademy.apiassessment.Mappers.BudgetMapper;
import com.cbfacademy.apiassessment.Mappers.UserMapper;
import com.cbfacademy.apiassessment.Repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("The Budget Service")
public class BudgetServiceTest {
    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private UserService userService;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private BudgetMapper budgetMapper;
    @Mock
    private UserMapper userMapper;


    private User user;
    private UserDTO userDTO;

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
    }

    @Test
    @DisplayName("can create Budget for existing user")
    void testSaveBudget() {
        // Arrange
        String usernameOrEmail = "user@email.com";
        Budget inputBudget = new Budget(1000, SubCategories.Category.Food, null, "Food");
        inputBudget.setId(1L);

        Budget savedBudget = new Budget(1000, SubCategories.Category.Food, null, "Food");
        savedBudget.setUser(user);
        savedBudget.setId(1L);

        BudgetDTO expectedBudgetDTO = new BudgetDTO(1L,1000,"Food",null,"Food",1L);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.save(inputBudget)).thenReturn(savedBudget);

        // Act
        BudgetDTO result = budgetService.saveBudget(usernameOrEmail,inputBudget );

        // Assert
        assertNotNull(result);
        assertEquals(expectedBudgetDTO, result);
    }

    @ParameterizedTest
    @MethodSource("DiffScenarios")
    @DisplayName("can update Budget with different values")
    void testUpdateBudgetByID(String update, String value){
        // Arrange
        String usernameOrEmail = "user@email.com";
        long budgetId = 1L;

        Budget existingBudget = new Budget(1000, SubCategories.Category.Food, null, "Food");
        existingBudget.setId(budgetId);
        existingBudget.setUser(user);

        Budget updatedBudget = new Budget(1000, SubCategories.Category.Food, null, "Food");
        updatedBudget.setId(budgetId);
        updatedBudget.setUser(user);

        BudgetDTO expectedBudgetDTO = new BudgetDTO();
        expectedBudgetDTO.setAmount(1000);
        expectedBudgetDTO.setCategory("Food");
        expectedBudgetDTO.setSubcategory(null);
        expectedBudgetDTO.setDescription("Food");
        expectedBudgetDTO.setUser_id(user.getId());
        expectedBudgetDTO.setId(budgetId);

        switch (update){
            case "amount" -> {
                updatedBudget.setBudgetAmount(Double.parseDouble(value));
                expectedBudgetDTO.setAmount(Double.parseDouble(value));
            }
            case "category" -> {
                updatedBudget.setBudgetCategory(SubCategories.Category.valueOf(value));
                expectedBudgetDTO.setCategory(value);
            }
            case "subcategory" -> {
                if(value != null) {
                    updatedBudget.setBudgetSubcategory(SubCategories.valueOf(value));
                    expectedBudgetDTO.setSubcategory(value);
                }
                updatedBudget.setBudgetSubcategory(null);
                expectedBudgetDTO.setSubcategory(null);
            }
            case "description" -> {
                updatedBudget.setDescription(value);
                expectedBudgetDTO.setDescription(value);
            }
        }

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findByUserAndId(user, budgetId)).thenReturn(java.util.Optional.of(existingBudget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(updatedBudget);

        // Act
        BudgetDTO result = budgetService.updateBudgetByID(usernameOrEmail, budgetId, update, value);

        // Assert
        assertEquals(expectedBudgetDTO, result);
    }

    private static Stream<Object[]> DiffScenarios() {
        return Stream.of(
                new Object[]{"amount", "1500"},
                new Object[]{"category", "Transport"},
                new Object[]{"subcategory", "Groceries"},
                new Object[]{"description", "Food Shop for the Week (Snacks)"}
        );
    }


    @Test
    @DisplayName("can get Budget by ID")
    void testGetBudgetByID() {
        // Arrange
        String usernameOrEmail = "user@email.com";
        Long budgetId = 1L;

        Budget savedBudget1 = new Budget(90, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        BudgetDTO expectedBudgetDTO = new BudgetDTO();
        expectedBudgetDTO.setAmount(90);
        expectedBudgetDTO.setCategory("Food");
        expectedBudgetDTO.setSubcategory(null);
        expectedBudgetDTO.setDescription("Food");
        expectedBudgetDTO.setUser_id(user.getId());
        expectedBudgetDTO.setId(budgetId);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findByUserAndId(user, budgetId)).thenReturn(java.util.Optional.of(savedBudget1));

        // Act
        BudgetDTO result = budgetService.getBudgetById(usernameOrEmail, budgetId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedBudgetDTO, result);
    }

    @Test
    @DisplayName("can get all Budgets")
    void testGetAllBudgets() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(90, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        BudgetDTO BudgetDTO1 = new BudgetDTO(1L, 90, "Food", null, "Food", user.getId());

        BudgetDTO BudgetDTO2 = new BudgetDTO(2L, 40, "Savings", "Basic", "From Weekend Bar Shift", user.getId());


        List<Budget> budgetList = Arrays.asList(savedBudget1, savedBudget2);
        List<BudgetDTO> budgetDTOList = Arrays.asList(BudgetDTO1, BudgetDTO2);


        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findByUser(user)).thenReturn(java.util.Optional.of(budgetList));
        when(budgetMapper.budgetDTO(savedBudget1)).thenReturn(BudgetDTO1);
        when(budgetMapper.budgetDTO(savedBudget2)).thenReturn(BudgetDTO2);

        // Act
        List<BudgetDTO> result = budgetService.getAllBudgets(usernameOrEmail);

        // Assert
        assertNotNull(result);
        assertEquals(budgetDTOList, result);
    }

    @Test
    @DisplayName("can get Budgets by Category")
    void testGetBudgetsByCategory() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(90, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        Budget savedBudget3 = new Budget(20, SubCategories.Category.Food, SubCategories.Restaurant, "KFC");
        savedBudget3.setUser(user);
        savedBudget3.setId(3L);

        BudgetDTO BudgetDTO1 = new BudgetDTO(1L, 90, "Food", null, "Food", user.getId());

        BudgetDTO BudgetDTO3 = new BudgetDTO(3L, 20, "Food", "Restaurant", "KFC", user.getId());



        List<BudgetDTO> ansbudgetDTOList = Arrays.asList(BudgetDTO1, BudgetDTO3);

        List<Budget> ansBudgetList = Arrays.asList(savedBudget1, savedBudget3);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findByUserIdAndBudgetCategory(user.getId(), SubCategories.Category.Food)).thenReturn(ansBudgetList);
        when(budgetMapper.budgetDTO(savedBudget1)).thenReturn(BudgetDTO1);
        when(budgetMapper.budgetDTO(savedBudget3)).thenReturn(BudgetDTO3);

        // Act
        List<BudgetDTO> result = budgetService.getBudgetsByCategory(usernameOrEmail, "Food");

        // Assert
        assertNotNull(result);
        assertEquals(ansbudgetDTOList, result);
    }

    @Test
    @DisplayName("can get Budgets in Price Range")
    void testGetBudgetsByPriceRange() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(190, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        Budget savedBudget3 = new Budget(20, SubCategories.Category.Food, SubCategories.Restaurant, "KFC");
        savedBudget3.setUser(user);
        savedBudget3.setId(3L);

        BudgetDTO BudgetDTO2 = new BudgetDTO(2L, 40, "Savings", "Basic", "From Weekend Bar Shift", user.getId());

        BudgetDTO BudgetDTO3 = new BudgetDTO(3L, 20, "Food", "Restaurant", "KFC", user.getId());


        List<BudgetDTO> budgetDTOList = Arrays.asList( BudgetDTO2, BudgetDTO3);

        List<Budget> ansBudgetList = Arrays.asList(savedBudget2, savedBudget3);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findBudgetsInPriceRange(user, 10, 100)).thenReturn(ansBudgetList);
        when(budgetMapper.budgetDTO(savedBudget2)).thenReturn(BudgetDTO2);
        when(budgetMapper.budgetDTO(savedBudget3)).thenReturn(BudgetDTO3);

        // Act
        List<BudgetDTO> result = budgetService.getBudgetsInPriceRange(usernameOrEmail, 10, 100);

        // Assert
        assertNotNull(result);
        assertEquals(budgetDTOList, result);
    }

    @Test
    @DisplayName("can get Budgets greater than an amount")
    void testGetBudgetsGreaterThan() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(190, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        Budget savedBudget3 = new Budget(20, SubCategories.Category.Food, SubCategories.Restaurant, "KFC");
        savedBudget3.setUser(user);
        savedBudget3.setId(3L);

        BudgetDTO BudgetDTO1 = new BudgetDTO(1L, 190, "Food", null, "Food", user.getId());

        BudgetDTO BudgetDTO2 = new BudgetDTO(2L, 40, "Savings", "Basic", "From Weekend Bar Shift", user.getId());


        List<BudgetDTO> budgetDTOList = Arrays.asList( BudgetDTO1, BudgetDTO2);

        List<Budget> ansBudgetList = Arrays.asList(savedBudget1, savedBudget2);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findBudgetsGreaterThan(user, 10)).thenReturn(ansBudgetList);
        when(budgetMapper.budgetDTO(savedBudget1)).thenReturn(BudgetDTO1);
        when(budgetMapper.budgetDTO(savedBudget2)).thenReturn(BudgetDTO2);

        // Act
        List<BudgetDTO> result = budgetService.getBudgetsGreaterThan(usernameOrEmail, 10);

        // Assert
        assertNotNull(result);
        assertEquals(budgetDTOList, result);
    }

    @Test
    @DisplayName("can get Budgets less than an amount")
    void testGetBudgetsLessThan() {
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(190, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        Budget savedBudget3 = new Budget(20, SubCategories.Category.Food, SubCategories.Restaurant, "KFC");
        savedBudget3.setUser(user);
        savedBudget3.setId(3L);

        BudgetDTO BudgetDTO2 = new BudgetDTO(2L, 40, "Savings", "Basic", "From Weekend Bar Shift", user.getId());

        BudgetDTO BudgetDTO3 = new BudgetDTO(3L, 20, "Food", "Restaurant", "KFC", user.getId());


        List<BudgetDTO> budgetDTOList = Arrays.asList( BudgetDTO2, BudgetDTO3);

        List<Budget> ansBudgetList = Arrays.asList(savedBudget2, savedBudget3);

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findBudgetsLessThan(user, 50)).thenReturn(ansBudgetList);
        when(budgetMapper.budgetDTO(savedBudget2)).thenReturn(BudgetDTO2);
        when(budgetMapper.budgetDTO(savedBudget3)).thenReturn(BudgetDTO3);

        // Act
        List<BudgetDTO> result = budgetService.getBudgetsLessThan(usernameOrEmail, 50);

        // Assert
        assertNotNull(result);
        assertEquals(budgetDTOList, result);
    }

    @ParameterizedTest
    @MethodSource("DiffSortScenarios")
    @DisplayName("can sort Budget")
    void testSortBudget(String sortBy){
        // Arrange
        String usernameOrEmail = "user@email.com";

        Budget savedBudget1 = new Budget(190, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);

        Budget savedBudget2 = new Budget(40, SubCategories.Category.Savings, SubCategories.Basic, "From Weekend Bar Shift");
        savedBudget2.setUser(user);
        savedBudget2.setId(2L);

        Budget savedBudget3 = new Budget(20, SubCategories.Category.Food, SubCategories.Restaurant, "KFC");
        savedBudget3.setUser(user);
        savedBudget3.setId(3L);

        BudgetDTO BudgetDTO1 = new BudgetDTO(1L, 190, "Food", null, "Food", user.getId());

        BudgetDTO BudgetDTO2 = new BudgetDTO(2L, 40, "Savings", "Basic", "From Weekend Bar Shift", user.getId());

        BudgetDTO BudgetDTO3 = new BudgetDTO(3L, 20, "Food", "Restaurant", "KFC", user.getId());



        List<Budget> budgetList = Arrays.asList(savedBudget1, savedBudget2, savedBudget3);
        List<BudgetDTO> budgetDTOList = Arrays.asList( BudgetDTO1, BudgetDTO2, BudgetDTO3) ;


        switch (sortBy){
            case "amount" -> budgetDTOList = Arrays.asList( BudgetDTO3, BudgetDTO2, BudgetDTO1);
            case "category" -> budgetDTOList = Arrays.asList( BudgetDTO1, BudgetDTO3, BudgetDTO2);
            case "subcategory" -> budgetDTOList = Arrays.asList( BudgetDTO1, BudgetDTO2, BudgetDTO3);
        }

        when(userService.getUserByUsernameOrEmail(usernameOrEmail)).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(budgetRepository.findByUser(user)).thenReturn(Optional.of(budgetList));
        when(budgetMapper.budgetDTO(savedBudget1)).thenReturn(BudgetDTO1);
        when(budgetMapper.budgetDTO(savedBudget2)).thenReturn(BudgetDTO2);
        when(budgetMapper.budgetDTO(savedBudget3)).thenReturn(BudgetDTO3);


        // Act
        List<BudgetDTO> result = budgetService.sortBudgetsBy(usernameOrEmail, sortBy);

        // Assert
        assertEquals(budgetDTOList, result);
    }

    private static Stream<String> DiffSortScenarios() {
        return Stream.of(
                "amount", "category", "subcategory"
        );
    }

    @Test
    @DisplayName("can delete budget")
    void testDeleteBudget(){
        // Arrange
        Budget savedBudget1 = new Budget(190, SubCategories.Category.Food, null, "Food");
        savedBudget1.setUser(user);
        savedBudget1.setId(1L);


        when(budgetRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(savedBudget1));
        when(userService.getUserByUsernameOrEmail(user.getEmail())).thenReturn(userDTO);
        when(userMapper.toUser(userDTO)).thenReturn(user);
        doNothing().when(budgetRepository).delete(savedBudget1);

        assertAll(() -> budgetService.deleteBudget(user.getEmail(), 1L));
    }

}
