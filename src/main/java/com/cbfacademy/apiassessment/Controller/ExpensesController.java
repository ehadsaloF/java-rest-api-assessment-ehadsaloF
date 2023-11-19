package com.cbfacademy.apiassessment.Controller;


import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Mappers.ExpensesMapper;
import com.cbfacademy.apiassessment.Service.ExpensesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/PF/user/{usernameOrEmail}/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;


    private ExpensesMapper expensesMapper;

    @Operation(summary = "Create an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Expense Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PostMapping("/create")
    public ExpensesDTO createExpense(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Expense Amount")
            @RequestParam double expenseAmount,
            @Parameter(description = "Expense Category")
            @RequestParam String expenseCategory,
            @Parameter(description = "Expense Subcategory")
            @RequestParam String expenseSubcategory,
            @Parameter(description = "Expense description")
            @RequestParam String expenseDescription){

        try {
            Expenses expenses = new Expenses(expenseAmount,
                    SubCategories.Category.valueOf(expenseCategory),
                    SubCategories.valueOf(expenseSubcategory),
                    expenseDescription);

            return expensesMapper.INSTANCE.expensesDTO(expensesService.saveExpenses(usernameOrEmail, expenses));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }

    }

    @Operation(summary = "Create an Expense Connected to a Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Expense Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PostMapping("/create/{budgetId}")
    public ExpensesDTO createExpense(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Budget ID")
            @PathVariable Long budgetId,
            @Parameter(description = "Expense Amount")
            @RequestParam double expenseAmount,
            @Parameter(description = "Expense Category")
            @RequestParam String expenseCategory,
            @Parameter(description = "Expense Subcategory")
            @RequestParam String expenseSubcategory,
            @Parameter(description = "Expense description")
            @RequestParam String expenseDescription) {
        try {
            Expenses expenses = new Expenses(expenseAmount,
                    SubCategories.Category.valueOf(expenseCategory),
                    SubCategories.valueOf(expenseSubcategory),
                    expenseDescription);

            return expensesMapper.INSTANCE.expensesDTO(expensesService.saveExpenses(usernameOrEmail,budgetId, expenses));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    @Operation(summary = "Update an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense Updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Expense Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PatchMapping("/update/{expensesId}")
    public ExpensesDTO updateExpense(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the expense to be updated")
            @PathVariable long expensesId,
            @Parameter(description = "The field to be updated (amount, category, subcategory, description or budget)")
            @RequestParam String update,
            @Parameter(description = "The new value to set for the specified field")
            @RequestParam String value) {

        try {
            return expensesMapper.INSTANCE.expensesDTO(expensesService.updateExpensesByID(usernameOrEmail, expensesId, update, value));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }

    }


    @Operation(summary = "Get an Expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByID/{expensesId}")
    public ExpensesDTO getExpensesById(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the expense")
            @PathVariable long expensesId) {
        try {
            return expensesMapper.INSTANCE.expensesDTO(expensesService.getExpensesById(usernameOrEmail, expensesId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses Associated With a Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses Associated with budget",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByBudget/{budgetId}")
    public List<ExpensesDTO> getExpensesByBudget(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the associated Budget")
            @PathVariable long budgetId) {
        try {
            return expensesService.getExpensesByBudget(usernameOrEmail, budgetId).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses by Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByCat/{category}")
    public List<ExpensesDTO> getExpensesByCategory(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The expense category")
            @PathVariable String category) {
        try{
            return expensesService.getExpensesByCategory(usernameOrEmail, category).
                stream().map(expensesMapper.INSTANCE::expensesDTO).
                collect(Collectors.toList());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses within a price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses within price range",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount")
    public List<ExpensesDTO> getExpensesInPriceRange(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The minimum expense amount in the range")
            @RequestParam double minAmount,
            @Parameter(description = "The maximum expense amount in the range")
            @RequestParam double maxAmount){
        return expensesService.getExpensesInPriceRange(usernameOrEmail, minAmount, maxAmount).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
    }

    @Operation(summary = "Get Expenses greater than an amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses found",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount/>")
    public List<ExpensesDTO> getExpensesGreaterThan(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Minimum amount")
            @RequestParam double minAmount){
        try{
            return expensesService.getExpensesGreaterThan(usernameOrEmail, minAmount).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get a Expenses less than an amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses found",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount/<")
    public List<ExpensesDTO> getExpensesLessThan(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Maximum Amount")
            @RequestParam double maxAmount){
        try {
            return expensesService.getExpensesLessThan(usernameOrEmail, maxAmount).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses created within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses within date range",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate")
    public List<ExpensesDTO> getExpensesInDateRange(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The start date of the date range")
            @RequestParam String startDate,
            @Parameter(description = "The end date of the date range")
            @RequestParam String endDate){
        try {
            return expensesService.getExpensesInDateRange(usernameOrEmail, startDate, endDate).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses created before a date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses created before date",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate/before")
    public List<ExpensesDTO> getExpensesBefore(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The date")
            @RequestParam String endDate){
        try {
            return expensesService.getExpensesBefore(usernameOrEmail, endDate).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Expenses created after a date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Expenses created after date",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate/after")
    public List<ExpensesDTO> getExpensesAfter(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The date")
            @RequestParam String startDate){
        try {
            return expensesService.getExpensesAfter(usernameOrEmail, startDate).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get All Expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Expenses",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAll")
    public List<ExpensesDTO> getAllExpenses(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail) {

        try {
            return expensesService.getAllExpenses(usernameOrEmail).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get All Expenses as JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Expenses",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAll/download")
    public ResponseEntity<?> downloadAllExpenses(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail) {

        try {
            expensesService.getAllExpensesAsJSONFile(usernameOrEmail);

            Path filePath = Paths.get("src/main/resources/AllExpenses.JSON");
            Resource resource = new FileSystemResource(filePath.toFile());

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllExpenses.json");

            try {
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resource);
            } catch (IOException e) {
                throw new IOException("Error Processing Request");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    @Operation(summary = "Sort Expenses by specified criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses sorted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Expenses",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Sorting Criteria",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/sort/{sortBy}")
    public List<ExpensesDTO> sortExpensesBy(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The sorting criteria (\"amount\", \"category\", \"subcategory\", or \"date\")")
            @PathVariable String sortBy){
        try {
            return expensesService.sortExpensesBy(usernameOrEmail, sortBy).
                    stream().map(expensesMapper.INSTANCE::expensesDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Delete Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense sorted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @DeleteMapping("/delete/{expensesId}")
    public void deleteExpensesById(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the expense to be deleted")
            @PathVariable long expensesId){
        try {
            expensesService.deleteExpense(usernameOrEmail, expensesId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


}

