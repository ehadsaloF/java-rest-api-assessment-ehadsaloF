package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.DTO.BudgetDTO;
import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Service.BudgetService;
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
@RequestMapping("/PF/user/{usernameOrEmail}/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create")
    public BudgetDTO createBudget(
            @PathVariable String usernameOrEmail,
            @RequestBody Budget budget) {
        return budgetService.saveBudget(usernameOrEmail, budget);
    }

    @PatchMapping("/update/{budgetId}")
    public BudgetDTO updateBudget(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId,
            @RequestParam String update,
            @RequestParam String value) {

        return budgetService.updateBudgetByID(usernameOrEmail, budgetId, update, value);
    }

    @GetMapping("/getByID/{budgetId}")
    public BudgetDTO getBudgetById(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId) {
            return budgetService.getBudgetById(usernameOrEmail, budgetId);
    }

    @GetMapping("/getByCat/{category}")
    public List<BudgetDTO> getBudgetByCategory(
            @PathVariable String usernameOrEmail,
            @PathVariable String category){
        return budgetService.getBudgetsByCategory(usernameOrEmail, category);
    }

    @GetMapping("/getByAmount")
    public List<BudgetDTO> getBudgetInPriceRange(
            @PathVariable String usernameOrEmail,
            @RequestParam double minAmount,
            @RequestParam double maxAmount){
        return budgetService.getBudgetsInPriceRange(usernameOrEmail, minAmount, maxAmount);
    }
    @GetMapping("/getByAmount/>")
    public List<BudgetDTO> getBudgetGreaterThan(
            @PathVariable String usernameOrEmail,
            @RequestParam double minAmount){
        return budgetService.getBudgetsGreaterThan(usernameOrEmail, minAmount);
    }
    @GetMapping("/getByAmount/<")
    public List<BudgetDTO> getBudgetLessThan(
            @PathVariable String usernameOrEmail,
            @RequestParam double maxAmount){
        return budgetService.getBudgetsLessThan(usernameOrEmail, maxAmount);
    }

    @GetMapping("/getByDate")
    public List<BudgetDTO> getBudgetInDateRange(
            @PathVariable String usernameOrEmail,
            @RequestParam String startDate,
            @RequestParam String endDate){
        return budgetService.getBudgetsByDateRange(usernameOrEmail, startDate, endDate);
    }
    @GetMapping("/getByDate/before")
    public List<BudgetDTO> getBudgetBefore(
            @PathVariable String usernameOrEmail,
            @RequestParam String endDate){
        return budgetService.getBudgetsBefore(usernameOrEmail, endDate);
    }
    @GetMapping("/getByDate/after")
    public List<BudgetDTO> getBudgetAfter(
            @PathVariable String usernameOrEmail,
            @RequestParam String startDate){
        return budgetService.getBudgetsAfter(usernameOrEmail, startDate);
    }

    @GetMapping("/getAll")
    public List<BudgetDTO> getAllBudgets(@PathVariable String usernameOrEmail) {

        return budgetService.getAllBudgets(usernameOrEmail);
    }

    @GetMapping("/getAll/download")
    public ResponseEntity<?> downloadAllBudgets(@PathVariable String usernameOrEmail) throws IOException {


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
    }

    @GetMapping("/sort/{sortBy}")
    public List<BudgetDTO> sortBudgetBy(
            @PathVariable String usernameOrEmail,
            @PathVariable String sortBy){
        return budgetService.sortBudgetsBy(usernameOrEmail, sortBy);
    }

    @DeleteMapping("/delete/{budgetId}")
    public void deleteBudgetById(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId){
        budgetService.deleteBudget(usernameOrEmail, budgetId);
    }


}

