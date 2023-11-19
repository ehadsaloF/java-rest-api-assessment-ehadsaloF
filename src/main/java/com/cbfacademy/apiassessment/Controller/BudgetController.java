package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Entity.SubCategories;
import com.cbfacademy.apiassessment.Mappers.BudgetMapper;
import com.cbfacademy.apiassessment.Service.BudgetService;
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
@RequestMapping("/PF/user/{usernameOrEmail}/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetMapper budgetMapper;

    @Operation(summary = "Create a Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Budget Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PostMapping("/create")
    public BudgetDTO createBudget(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Budget Amount")
            @RequestParam double budgetAmount,
            @Parameter(description = "Budget Category")
            @RequestParam String budgetCategory,
            @Parameter(description = "Budget Subcategory")
            @RequestParam String budgetSubcategory,
            @Parameter(description = "Budget description")
            @RequestParam String budgetDescription) {
        try {
            Budget budget = new Budget(budgetAmount,
                                       SubCategories.Category.valueOf(budgetCategory),
                                       SubCategories.valueOf(budgetSubcategory),
                                       budgetDescription);

            return budgetMapper.INSTANCE.budgetDTO(budgetService.saveBudget(usernameOrEmail, budget));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Update a Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget Updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Budget Parameters",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @PatchMapping("/update/{budgetId}")
    public BudgetDTO updateBudget(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the budget to be updated")
            @PathVariable long budgetId,
            @Parameter(description = "The field to be updated (amount, category, subcategory, or description)")
            @RequestParam String update,
            @Parameter(description = "The new value to set for the specified field")
            @RequestParam String value) {

        try {
            return budgetMapper.INSTANCE.budgetDTO(budgetService.updateBudgetByID(usernameOrEmail, budgetId, update, value));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get a Budget by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByID/{budgetId}")
    public BudgetDTO getBudgetById(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the budget")
            @PathVariable long budgetId) {
        try {
            return budgetMapper.INSTANCE.budgetDTO(budgetService.getBudgetById(usernameOrEmail, budgetId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }

    }


    @Operation(summary = "Get a Budget by Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Category",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByCat/{category}")
    public List<BudgetDTO> getBudgetByCategory(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The budget category")
            @PathVariable String category){
        try {
            return budgetService.getBudgetsByCategory(usernameOrEmail, category).
                    stream().map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    @Operation(summary = "Get Budgets within a price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget within price range",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount")
    public List<BudgetDTO> getBudgetInPriceRange(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The minimum budget amount in the range")
            @RequestParam double minAmount,
            @Parameter(description = "The maximum budget amount in the range")
            @RequestParam double maxAmount){
        try {
            return budgetService.getBudgetsInPriceRange(usernameOrEmail, minAmount, maxAmount).stream().
                    map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Budgets greater than an amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget greater than amount",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount/>")
    public List<BudgetDTO> getBudgetGreaterThan(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Minimum amount")
            @RequestParam double minAmount){

        try {
            return budgetService.getBudgetsGreaterThan(usernameOrEmail, minAmount).
                    stream().map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Budgets less than an amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget less than amount",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Amount",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByAmount/<")
    public List<BudgetDTO> getBudgetLessThan(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "Maximum Amount")
            @RequestParam double maxAmount){
        try {
            return budgetService.getBudgetsLessThan(usernameOrEmail, maxAmount).
                    stream().map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    @Operation(summary = "Get Budgets created within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget within date range",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate")
    public List<BudgetDTO> getBudgetInDateRange(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The start date of the date range")
            @RequestParam String startDate,
            @Parameter(description = "The end date of the date range")
            @RequestParam String endDate){
        try {
            return budgetService.getBudgetsByDateRange(usernameOrEmail, startDate, endDate).
                    stream().map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Budgets created before a date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget created before date",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate/before")
    public List<BudgetDTO> getBudgetBefore(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The date")
            @RequestParam String endDate){
        try {
            return budgetService.getBudgetsBefore(usernameOrEmail, endDate).
                    stream().map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get Budgets created after a date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Budget created after date",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Date",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getByDate/after")
    public List<BudgetDTO> getBudgetAfter(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The date")
            @RequestParam String startDate){
        try {
            return budgetService.getBudgetsAfter(usernameOrEmail, startDate).stream().
                    map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get All Budgets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Budgets",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAll")
    public List<BudgetDTO> getAllBudgets(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail) {

        try {
            return budgetService.getAllBudgets(usernameOrEmail).stream().
                    map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Get All Budgets as JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Budgets",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/getAll/download")
    public ResponseEntity<?> downloadAllBudgets(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail) {

        try {
            budgetService.getAllBudgetAsJSONFile(usernameOrEmail);

            Path filePath = Paths.get("src/main/resources/AllBudgets.JSON");
            Resource resource = new FileSystemResource(filePath.toFile());

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AllBudgets.json");

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


    @Operation(summary = "Sort Budgets by specified criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets sorted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User has no Budgets",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid Sorting Criteria",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @GetMapping("/sort/{sortBy}")
    public List<BudgetDTO> sortBudgetBy(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The sorting criteria (\"amount\", \"category\", \"subcategory\", or \"date\")")
            @PathVariable String sortBy){

        try {
            return budgetService.sortBudgetsBy(usernameOrEmail, sortBy).stream().
                    map(budgetMapper.INSTANCE::budgetDTO).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    @Operation(summary = "Delete Budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budgets Deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Budget does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing your request",
                    content = @Content)
    })
    @DeleteMapping("/delete/{budgetId}")
    public void deleteBudgetById(
            @Parameter(description = "Username or email of the user")
            @PathVariable String usernameOrEmail,
            @Parameter(description = "The ID of the budget to be deleted")
            @PathVariable long budgetId){

        try {
            budgetService.deleteBudget(usernameOrEmail, budgetId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


}

