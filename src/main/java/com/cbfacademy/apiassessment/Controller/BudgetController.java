package com.cbfacademy.apiassessment.Controller;

import com.cbfacademy.apiassessment.Entity.Budget;
import com.cbfacademy.apiassessment.Service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/PF/user/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create/{usernameOrEmail}")
    public String createBudget(
            @PathVariable String usernameOrEmail,
            @RequestBody Budget budget) {
        return budgetService.saveBudget(usernameOrEmail, budget).toString();
    }

    @PutMapping("/update/{usernameOrEmail}/{budgetId}")
    public Budget updateBudget(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId,
            @RequestParam String update,
            @RequestParam String value) {

        return budgetService.updateBudgetByID(usernameOrEmail, budgetId, update, value);
    }

    @GetMapping("/getByID/{usernameOrEmail}/{budgetId}")
    public Optional<Budget> getBudgetById(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId) {
            return budgetService.getBudgetById(usernameOrEmail, budgetId);
    }

    @GetMapping("/getByCat/{usernameOrEmail}/{category}")
    public List<Budget> getBudgetByCategory(
            @PathVariable String usernameOrEmail,
            @PathVariable String category){
        return budgetService.getBudgetsByCategory(usernameOrEmail, category);
    }

    @GetMapping("/getByAmount/{usernameOrEmail}")
    public List<Budget> getBudgetInPriceRange(
            @PathVariable String usernameOrEmail,
            @RequestParam double minAmount,
            @RequestParam double maxAmount){
        return budgetService.getBudgetsInPriceRange(usernameOrEmail, minAmount, maxAmount);
    }

    @GetMapping("/getByDate/{usernameOrEmail}")
    public List<Budget> getBudgetInDateRange(
            @PathVariable String usernameOrEmail,
            @RequestParam String startDate,
            @RequestParam String endDate){
        return budgetService.getBudgetsByDateRange(usernameOrEmail, startDate, endDate);
    }

    @GetMapping("/getAll/{usernameOrEmail}")
    public List<Budget> getAllBudgets(@PathVariable String usernameOrEmail) {

        return budgetService.getAllBudgets(usernameOrEmail).orElseThrow();
    }

    @GetMapping("/sort/{usernameOrEmail}/{sortBy}")
    public List<Budget> sortBudgetBy(
            @PathVariable String usernameOrEmail,
            @PathVariable String sortBy){
        return budgetService.sortBudgetsBy(usernameOrEmail, sortBy);
    }

    @DeleteMapping("/delete/{usernameOrEmail}/{budgetId}")
    public void deleteBudgetById(
            @PathVariable String usernameOrEmail,
            @PathVariable long budgetId){
        budgetService.deleteBudget(usernameOrEmail, budgetId);
    }


}

