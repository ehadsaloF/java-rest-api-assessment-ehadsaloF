package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.ExpensesDTO;
import com.cbfacademy.apiassessment.Entity.Expenses;
import com.cbfacademy.apiassessment.Service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/PF/user/{usernameOrEmail}/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;

    @PostMapping("/create")
    public ExpensesDTO createExpense(
            @PathVariable String usernameOrEmail,
            @RequestBody Expenses expenses) {
        return expensesService.saveExpenses(usernameOrEmail, expenses);
    }

    @PostMapping("/create/{budgetId}")
    public ExpensesDTO createExpense(
            @PathVariable String usernameOrEmail,
            @PathVariable Long budgetId,
            @RequestBody Expenses expenses) {
        return expensesService.saveExpenses(usernameOrEmail, budgetId, expenses);
    }

    @PatchMapping("/update/{expensesId}")
    public ExpensesDTO updateExpense(
            @PathVariable String usernameOrEmail,
            @PathVariable long expensesId,
            @RequestParam String update,
            @RequestParam String value) {

        return expensesService.updateExpensesByID(usernameOrEmail, expensesId, update, value);
    }

    @GetMapping("/getByID/{expensesId}")
    public ExpensesDTO getExpensesById(
            @PathVariable String usernameOrEmail,
            @PathVariable long expensesId) {
        return expensesService.getExpensesById(usernameOrEmail, expensesId);
    }

    @GetMapping("/getByBudget/{budgetId}")
    public List<ExpensesDTO> getExpensesByBudget(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId) {
        return expensesService.getExpensesByBudget(usernameOrEmail, budgetId);
    }

    @GetMapping("/getByCat/{category}")
    public List<ExpensesDTO> getExpensesByCategory(
            @PathVariable String usernameOrEmail,
            @PathVariable String category){
        return expensesService.getExpensesByCategory(usernameOrEmail, category);
    }

    @GetMapping("/getByAmount")
    public List<ExpensesDTO> getExpensesInPriceRange(
            @PathVariable String usernameOrEmail,
            @RequestParam double minAmount,
            @RequestParam double maxAmount){
        return expensesService.getExpensesInPriceRange(usernameOrEmail, minAmount, maxAmount);
    }

    @GetMapping("/getByAmount/>")
    public List<ExpensesDTO> getBudgetGreaterThan(
            @PathVariable String usernameOrEmail,
            @RequestParam double minAmount){
        return expensesService.getExpensesGreaterThan(usernameOrEmail, minAmount);
    }
    @GetMapping("/getByAmount/<")
    public List<ExpensesDTO> getBudgetLessThan(
            @PathVariable String usernameOrEmail,
            @RequestParam double maxAmount){
        return expensesService.getExpensesLessThan(usernameOrEmail, maxAmount);
    }

    @GetMapping("/getByDate")
    public List<ExpensesDTO> getExpensesInDateRange(
            @PathVariable String usernameOrEmail,
            @RequestParam String startDate,
            @RequestParam String endDate){
        return expensesService.getExpensesInDateRange(usernameOrEmail, startDate, endDate);
    }

    @GetMapping("/getByDate/before")
    public List<ExpensesDTO> getExpensesBefore(
            @PathVariable String usernameOrEmail,
            @RequestParam String endDate){
        return expensesService.getExpensesBefore(usernameOrEmail, endDate);
    }
    @GetMapping("/getByDate/after")
    public List<ExpensesDTO> getExpensesAfter(
            @PathVariable String usernameOrEmail,
            @RequestParam String startDate){
        return expensesService.getExpensesAfter(usernameOrEmail, startDate);
    }
    @GetMapping("/getAll")
    public List<ExpensesDTO> getAllExpenses(@PathVariable String usernameOrEmail) {

        return expensesService.getAllExpenses(usernameOrEmail);
    }

    @GetMapping("/getAll/download")
    public ResponseEntity<?> downloadAllExpenses(@PathVariable String usernameOrEmail) throws IOException {

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
    }

    @GetMapping("/sort/{sortBy}")
    public List<ExpensesDTO> sortExpensesBy(
            @PathVariable String usernameOrEmail,
            @PathVariable String sortBy){
        return expensesService.sortExpensesBy(usernameOrEmail, sortBy);
    }

    @DeleteMapping("/delete/{expensesId}")
    public void deleteExpensesById(
            @PathVariable String usernameOrEmail,
            @PathVariable long expensesId){
        expensesService.deleteExpense(usernameOrEmail, expensesId);
    }


}

